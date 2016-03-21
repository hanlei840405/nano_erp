package com.nano.service.productmapping;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.github.pagehelper.PageInfo;
import com.nano.constant.Constant;
import com.nano.domain.base.Product;
import com.nano.domain.base.ShopProperty;
import com.nano.domain.base.Stock;
import com.nano.domain.productmapping.TaoBaoProduct;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.productmapping.TaoBaoProductMapper;
import com.nano.service.base.ProductService;
import com.nano.service.base.ShopPropertyService;
import com.nano.service.base.SkuService;
import com.nano.service.base.StockService;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.domain.Item;
import com.taobao.api.domain.Sku;
import com.taobao.api.request.ItemGetRequest;
import com.taobao.api.request.ItemSkuGetRequest;
import com.taobao.api.request.ItemUpdateDelistingRequest;
import com.taobao.api.request.ItemUpdateListingRequest;
import com.taobao.api.request.ItemsCustomGetRequest;
import com.taobao.api.request.SkusCustomGetRequest;
import com.taobao.api.request.SkusQuantityUpdateRequest;
import com.taobao.api.response.ItemGetResponse;
import com.taobao.api.response.ItemSkuGetResponse;
import com.taobao.api.response.ItemsCustomGetResponse;
import com.taobao.api.response.SkusCustomGetResponse;

@Service
public class TaoBaoProductServiceImpl implements TaoBaoProductService {
	private static final Logger logger = LoggerFactory.getLogger(TaoBaoProductServiceImpl.class);

	@Autowired
	private TaoBaoProductMapper taoBaoProductMapper;

	@Autowired
	private ProductService productService;

	@Autowired
	private SkuService skuService;

	@Autowired
	private ShopPropertyService shopPropertyService;

	@Autowired
	private StockService stockService;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Transactional
	public List<TaoBaoProduct> fetchData(Map<String, String> fetchParams) {
		/**
		 * 12061846 8738af4807587e2d3ef5b9add9b75dba
		 * 6100111a49185bf41181fd3c99b23953b26c6b691cc4284263817957
		 */
		Date begin = new Date();
		ShopProperty shopQueryParam = new ShopProperty();
		shopQueryParam.setShopId(fetchParams.get("shopId"));
		Map<String, String> params = requestParams(shopQueryParam);
		TaobaoClient client = new DefaultTaobaoClient(Constant.TAOBAO_URL, params.get("appkey"), params.get("secret"));
		String requestParams = null;
		try {
			List<TaoBaoProduct> taoBaoProducts = new ArrayList<>();
			if (!StringUtils.isEmpty(fetchParams.get("outerId"))) {
				requestParams = fetchParams.get("user") + "," + fetchParams.get("outerId");
				ItemsCustomGetRequest itemsCustomGetRequest = new ItemsCustomGetRequest();
				itemsCustomGetRequest.setOuterId(fetchParams.get("outerId"));
				itemsCustomGetRequest.setFields("num_iid,num,list_time,delist_time,title,price,outer_id,approve_status,sku");
				ItemsCustomGetResponse itemsCustomGetResponse = client.execute(itemsCustomGetRequest, params.get("sessionkey"));
				List<Item> items = itemsCustomGetResponse.getItems();
				for (Item item : items) {
					List<Sku> skus = item.getSkus();
					TaoBaoProduct taoBaoProductQueryParam = new TaoBaoProduct();
					taoBaoProductQueryParam.setProductOuterId(item.getOuterId());
					taoBaoProductQueryParam.setShopId(fetchParams.get("shopId"));
					List<TaoBaoProduct> exists = find(taoBaoProductQueryParam);
					for (TaoBaoProduct exist : exists) {
						delete(exist.getId(), fetchParams.get("user"));
					}
					Product productQueryParam = new Product();
					productQueryParam.setCode(item.getOuterId());
					Product product = productService.findOne(productQueryParam);
					for (Sku sku : skus) {
						ItemSkuGetRequest itemSkuGetRequest = new ItemSkuGetRequest();
						itemSkuGetRequest.setSkuId(sku.getSkuId());
						itemSkuGetRequest.setNumIid(item.getNumIid());
						itemSkuGetRequest.setFields("sku_id,barcode,properties_name,quantity,price,outer_id");
						ItemSkuGetResponse rsp = client.execute(itemSkuGetRequest, params.get("sessionkey"));
						sku = rsp.getSku();
						TaoBaoProduct platformProduct = new TaoBaoProduct();
						platformProduct.setDeListTime(item.getDelistTime());
						platformProduct.setListTime(item.getListTime());
						platformProduct.setNum(item.getNum());
						platformProduct.setPlatformProductId(item.getNumIid());
						platformProduct.setProductPrice(item.getPrice());
						platformProduct.setTitle(item.getTitle());
						platformProduct.setApproveStatus("onsale".equals(item.getApproveStatus()) ? "1" : "0");
						platformProduct.setProductOuterId(item.getOuterId());
						platformProduct.setPlatformSkuId(sku.getSkuId());
						platformProduct.setSkuOuterId(sku.getOuterId());
						platformProduct.setSkuPrice(sku.getPrice());
						platformProduct.setProperties(sku.getPropertiesName());
						platformProduct.setQuantity(sku.getQuantity());
						com.nano.domain.base.Sku skuQueryParam = new com.nano.domain.base.Sku();
						skuQueryParam.setProductId(product.getId());
						skuQueryParam.setCode(sku.getOuterId()); // 淘宝商品规则：货号规格码
						com.nano.domain.base.Sku exist = skuService.findOne(skuQueryParam);
						platformProduct.setSkuId(exist.getId());
						platformProduct.setProductId(product.getId());
						platformProduct.setShopId(fetchParams.get("shopId"));
						platformProduct.setBarcode(exist.getBarcode());
						save(platformProduct);
						taoBaoProducts.add(platformProduct);
					}
				}
				logger.info(String.format("调用淘宝查询商品接口,开始时间:%s，结束时间:%s", format.format(begin), format.format(new Date())));
				return taoBaoProducts;
			} else {
				requestParams = fetchParams.get("user") + "," + fetchParams.get("barcode");
				// 拿到sku平台的outer，转化为只保留字母与数字的格式
				String skuOuterId = fetchParams.get("barcode").replaceAll(Constant.MATCH_BARCODE, "");
				char[] array = skuOuterId.toCharArray();
				List<String> barcodeArray = new ArrayList<>();
				for (char arr : array) {
					barcodeArray.add(String.valueOf(arr));
				}
				SkusCustomGetRequest skusCustomGetRequest = new SkusCustomGetRequest();
				skusCustomGetRequest.setOuterId(MessageFormat.format(Constant.FORMAT_TAOBAO_SKU_OUTER_ID, barcodeArray.toArray()));
				skusCustomGetRequest.setFields("sku_id,barcode,properties_name,quantity,price,outer_id");
				SkusCustomGetResponse skusCustomGetResponse = client.execute(skusCustomGetRequest, params.get("sessionkey"));
				List<Sku> skus = skusCustomGetResponse.getSkus();
				for (Sku sku : skus) {
					ItemGetRequest itemGetRequest = new ItemGetRequest();
					itemGetRequest.setNumIid(sku.getNumIid());
					itemGetRequest.setFields("num_iid,num,list_time,delist_time,title,price,outer_id,approve_status");
					ItemGetResponse itemGetResponse = client.execute(itemGetRequest, params.get("sessionkey"));
					Item item = itemGetResponse.getItem();
					Product productQueryParam = new Product();
					productQueryParam.setCode(item.getOuterId());
					Product product = productService.findOne(productQueryParam);

					TaoBaoProduct platformProduct = new TaoBaoProduct();
					platformProduct.setDeListTime(item.getDelistTime());
					platformProduct.setListTime(item.getListTime());
					platformProduct.setNum(item.getNum());
					platformProduct.setPlatformProductId(item.getNumIid());
					platformProduct.setProductPrice(item.getPrice());
					platformProduct.setTitle(item.getTitle());
					platformProduct.setApproveStatus("onsale".equals(item.getApproveStatus()) ? "1" : "0");
					platformProduct.setProductOuterId(item.getOuterId());
					platformProduct.setShopId(fetchParams.get("shopId"));
					platformProduct.setPlatformSkuId(sku.getSkuId());
					platformProduct.setSkuOuterId(sku.getOuterId());
					platformProduct.setSkuPrice(sku.getPrice());
					platformProduct.setProperties(sku.getPropertiesName());
					platformProduct.setQuantity(sku.getQuantity());

					com.nano.domain.base.Sku skuQueryParam = new com.nano.domain.base.Sku();
					skuQueryParam.setProductId(product.getId());
					skuQueryParam.setCode(sku.getOuterId()); // 淘宝商品规则：货号规格码
					com.nano.domain.base.Sku exist = skuService.findOne(skuQueryParam);
					platformProduct.setSkuId(exist.getId());
					platformProduct.setProductId(product.getId());
					platformProduct.setBarcode(exist.getBarcode());
					TaoBaoProduct taoBaoProductQueryParam = new TaoBaoProduct();
					taoBaoProductQueryParam.setSkuOuterId(item.getOuterId());
					taoBaoProductQueryParam.setShopId(fetchParams.get("shopId"));
					TaoBaoProduct tbProduct = get(taoBaoProductQueryParam);
					delete(tbProduct.getId(), fetchParams.get("user"));
					save(platformProduct);
					taoBaoProducts.add(platformProduct);
				}
				logger.info(String.format("调用淘宝查询商品接口,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()), requestParams));
				return taoBaoProducts;
			}
		} catch (ApiException e) {
			logger.error(String.format("调用淘宝查询商品接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()), requestParams));
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	public TaoBaoProduct save(TaoBaoProduct taoBaoProduct) {
		taoBaoProductMapper.saveOne(taoBaoProduct);
		return taoBaoProduct;
	}

	@Transactional
	public boolean onSale(List<TaoBaoProduct> taoBaoProducts, String user) {
		Date begin = new Date();
		String requestParams = user + ",";
		ShopProperty shopQueryParam = new ShopProperty();
		Map<String, String> params = null;
		TaobaoClient client = null;
		try {
			for (TaoBaoProduct taoBaoProduct : taoBaoProducts) {
				requestParams += taoBaoProduct.getBarcode() + ",";
				if (StringUtils.isEmpty(shopQueryParam.getShopId())) {
					shopQueryParam.setShopId(taoBaoProduct.getShopId());
					params = requestParams(shopQueryParam);
					client = new DefaultTaobaoClient(Constant.TAOBAO_URL, params.get("appkey"), params.get("secret"));
				}
				ItemUpdateListingRequest req = new ItemUpdateListingRequest();
				req.setNumIid(taoBaoProduct.getPlatformProductId());
				client.execute(req, params.get("sessionKey"));
				taoBaoProduct.setApproveStatus("1");
				logger.info(String.format("商品【%s】调用淘宝商品上架接口,开始时间:%s，结束时间:%s,操作人:%s", taoBaoProduct.getBarcode(), format.format(begin), format.format(new Date()), user));
				update(taoBaoProduct);
			}
			return true;
		} catch (ApiException e) {
			logger.error(String.format("调用淘宝商品上架接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()), requestParams));
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	public boolean inStock(List<TaoBaoProduct> taoBaoProducts, String user) {
		Date begin = new Date();
		String requestParams = user + ",";
		ShopProperty shopQueryParam = new ShopProperty();
		Map<String, String> params = null;
		TaobaoClient client = null;
		try {
			for (TaoBaoProduct taoBaoProduct : taoBaoProducts) {
				requestParams += taoBaoProduct.getBarcode() + ",";
				if (StringUtils.isEmpty(shopQueryParam.getShopId())) {
					shopQueryParam.setShopId(taoBaoProduct.getShopId());
					params = requestParams(shopQueryParam);
					client = new DefaultTaobaoClient(Constant.TAOBAO_URL, params.get("appkey"), params.get("secret"));
				}
				ItemUpdateDelistingRequest req = new ItemUpdateDelistingRequest();
				req.setNumIid(taoBaoProduct.getPlatformProductId());
				client.execute(req, params.get("sessionKey"));
				taoBaoProduct.setApproveStatus("0");
				update(taoBaoProduct);
				logger.info(String.format("商品【%s】调用淘宝商品下架接口,开始时间:%s，结束时间:%s,操作人:%s", taoBaoProduct.getBarcode(), format.format(begin), format.format(new Date()), user));
			}
			return true;
		} catch (ApiException e) {
			logger.error(String.format("调用淘宝商品下架接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()), requestParams));
			throw new RuntimeException(e.getMessage());
		}
	}

	@Transactional
	public boolean syncStock(List<TaoBaoProduct> taoBaoProducts, String user) {
		Date begin = new Date();
		String requestParams = user + ",";
		ShopProperty shopQueryParam = new ShopProperty();
		Map<String, String> params = null;
		TaobaoClient client = null;
		Map<Long, List<TaoBaoProduct>> productSkuQuantityMap = new HashMap<>();
		try {
			for (TaoBaoProduct taoBaoProduct : taoBaoProducts) {
				requestParams += taoBaoProduct.getBarcode() + ",";
				if (StringUtils.isEmpty(shopQueryParam.getShopId())) {
					shopQueryParam.setShopId(taoBaoProduct.getShopId());
					params = requestParams(shopQueryParam);
					client = new DefaultTaobaoClient(Constant.TAOBAO_URL, params.get("appkey"), params.get("secret"));
				}
				if (!productSkuQuantityMap.containsKey(taoBaoProduct.getPlatformProductId())) {
					productSkuQuantityMap.put(taoBaoProduct.getPlatformProductId(), new ArrayList<TaoBaoProduct>());
				}

				Map<String, String> stockQueryParam = new HashMap<String, String>();
				stockQueryParam.put("skuId", taoBaoProduct.getSkuId());
				List<Stock> stocks = stockService.find(stockQueryParam);
				long quantity = 0;
				for (Stock stock : stocks) {
					quantity += stock.getQuantity();
				}
				taoBaoProduct.setQuantity(quantity);
				productSkuQuantityMap.get(taoBaoProduct.getPlatformProductId()).add(taoBaoProduct);
			}
			for (Map.Entry<Long, List<TaoBaoProduct>> entry : productSkuQuantityMap.entrySet()) {
				List<TaoBaoProduct> tbProducts = entry.getValue();
				if (!tbProducts.isEmpty()) {
					StringBuilder stringBuilder = new StringBuilder();
					int times = tbProducts.size() / 20;
					for (int i = 0; i < times; i++) {
						int start = 20 * i;
						List<TaoBaoProduct> subList = tbProducts.subList(start, start + 20);
						for (TaoBaoProduct tbProduct : subList) {
							stringBuilder.append(tbProduct.getPlatformSkuId()).append(":").append(tbProduct.getQuantity()).append(";");
						}
					}
					if (times * 20 < tbProducts.size()) {
						List<TaoBaoProduct> subList = tbProducts.subList(times * 20, tbProducts.size());
						for (TaoBaoProduct tbProduct : subList) {
							stringBuilder.append(tbProduct.getPlatformSkuId()).append(":").append(tbProduct.getQuantity()).append(";");
						}
					}
					stringBuilder.deleteCharAt(stringBuilder.lastIndexOf(";"));
					SkusQuantityUpdateRequest skusQuantityUpdateRequest = new SkusQuantityUpdateRequest();
					skusQuantityUpdateRequest.setNumIid(entry.getKey());
					skusQuantityUpdateRequest.setSkuidQuantities(stringBuilder.toString());
					client.execute(skusQuantityUpdateRequest, params.get("sessionKey"));
					logger.info(String.format("调用淘宝商品同步库存接口,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()), user + "," + skusQuantityUpdateRequest));
				}
			}
			return true;
		} catch (ApiException e) {
			logger.error(String.format("调用淘宝商品同步库存接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()),requestParams));
			throw new RuntimeException(e.getMessage());
		}
	}

	public TaoBaoProduct get(String id) {
		return taoBaoProductMapper.findOne(id);
	}

	public TaoBaoProduct get(TaoBaoProduct taoBaoProduct) {
		RowBounds rowBounds = new RowBounds(0, 1);
		List<TaoBaoProduct> taoBaoProducts = taoBaoProductMapper.findMany(taoBaoProduct, rowBounds);
		if (taoBaoProducts.isEmpty()) {
			return null;
		}
		return taoBaoProducts.get(0);
	}

	public List<TaoBaoProduct> find(TaoBaoProduct taoBaoProduct) {
		return taoBaoProductMapper.findMany(taoBaoProduct);
	}

	public PageInfo<TaoBaoProduct> find(TaoBaoProduct taoBaoProduct, int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<TaoBaoProduct> taoBaoProducts = taoBaoProductMapper.findMany(taoBaoProduct, rowBounds);
		PageInfo<TaoBaoProduct> pageInfo = new PageInfo<TaoBaoProduct>(taoBaoProducts);
		return pageInfo;
	}

	@Transactional
	public TaoBaoProduct update(TaoBaoProduct taoBaoProduct) {
		long resultCnt = taoBaoProductMapper.updateOne(taoBaoProduct);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION, taoBaoProduct.getId() + " : " + taoBaoProduct.getBarcode()));
		}
		return taoBaoProduct;
	}

	@Transactional
	public TaoBaoProduct delete(String id, String user) {
		TaoBaoProduct taoBaoProduct = taoBaoProductMapper.findOne(id);
		logger.info(String.format("用户【%s】删除淘宝铺货信息【%s】.", user, taoBaoProduct.getBarcode()));
		long resultCnt = taoBaoProductMapper.deleteOne(taoBaoProduct);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION, taoBaoProduct.getId() + " : " + taoBaoProduct.getBarcode()));
		}
		return taoBaoProduct;
	}

	@Transactional
	public List<TaoBaoProduct> delete(List<String> ids, String user) {
		List<TaoBaoProduct> taoBaoProducts = new ArrayList<>();
		for (String id : ids) {
			taoBaoProducts.add(delete(id, user));
		}
		return taoBaoProducts;
	}

	@Transactional
	public List<TaoBaoProduct> save(List<TaoBaoProduct> taoBaoProducts) {
		for (TaoBaoProduct taoBaoProduct : taoBaoProducts) {
			save(taoBaoProduct);
		}
		return taoBaoProducts;
	}

	private Map<String, String> requestParams(ShopProperty shopQueryParam) {
		List<ShopProperty> shopProperties = shopPropertyService.find(shopQueryParam);
		Map<String, String> map = new HashMap<String, String>();
		for (ShopProperty shopProperty : shopProperties) {
			if ("appkey".equals(shopProperty.getCode().toLowerCase())) {
				map.put("appkey", shopProperty.getValue());
				continue;
			}
			if ("secret".equals(shopProperty.getCode().toLowerCase())) {
				map.put("secret", shopProperty.getValue());
				continue;
			}
			if ("sessionkey".equals(shopProperty.getCode().toLowerCase())) {
				map.put("sessionkey", shopProperty.getValue());
				continue;
			}
		}
		return map;
	}
}
