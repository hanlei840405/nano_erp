package com.nano.service.productmapping;

import java.text.MessageFormat;
import java.text.ParseException;
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
import com.jd.open.api.sdk.DefaultJdClient;
import com.jd.open.api.sdk.JdClient;
import com.jd.open.api.sdk.JdException;
import com.jd.open.api.sdk.domain.ware.Sku;
import com.jd.open.api.sdk.domain.ware.Ware;
import com.jd.open.api.sdk.request.ware.SkuCustomGetRequest;
import com.jd.open.api.sdk.request.ware.WareGetRequest;
import com.jd.open.api.sdk.request.ware.WareInfoByInfoRequest;
import com.jd.open.api.sdk.request.ware.WareSkuStockUpdateRequest;
import com.jd.open.api.sdk.request.ware.WareSkusGetRequest;
import com.jd.open.api.sdk.request.ware.WareUpdateDelistingRequest;
import com.jd.open.api.sdk.request.ware.WareUpdateListingRequest;
import com.jd.open.api.sdk.response.ware.SkuCustomGetResponse;
import com.jd.open.api.sdk.response.ware.WareGetResponse;
import com.jd.open.api.sdk.response.ware.WareInfoByInfoSearchResponse;
import com.jd.open.api.sdk.response.ware.WareSkusGetResponse;
import com.nano.constant.Constant;
import com.nano.domain.base.ShopProperty;
import com.nano.domain.base.Stock;
import com.nano.domain.productmapping.JingDongProduct;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.productmapping.JingDongProductMapper;
import com.nano.service.base.ShopPropertyService;
import com.nano.service.base.SkuService;
import com.nano.service.base.StockService;
import com.nano.util.UUID;

@Service
public class JingDongProductServiceImpl implements JingDongProductService {
	private static final Logger logger = LoggerFactory.getLogger(JingDongProductServiceImpl.class);

	@Autowired
	private JingDongProductMapper jingDongProductMapper;

	@Autowired
	private SkuService skuService;

	@Autowired
	private ShopPropertyService shopPropertyService;

	@Autowired
	private StockService stockService;

	private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

	@Transactional
	public List<JingDongProduct> fetchData(Map<String, String> fetchParams) {
		Date begin = new Date();
		ShopProperty shopQueryParam = new ShopProperty();
		shopQueryParam.setShopId(fetchParams.get("shopId"));
		Map<String, String> params = requestParams(shopQueryParam);
		JdClient client = new DefaultJdClient(Constant.JINGDONG_URL, params.get("token"), params.get("appkey"), params.get("secret"));
		String requestParams = null;
		try {
			List<JingDongProduct> jingDongProducts = new ArrayList<>();

			if (fetchParams.containsKey("outerId")) {
				requestParams = fetchParams.get("user") + "," + fetchParams.get("outerId");
				WareInfoByInfoRequest wareInfoByInfoRequest = new WareInfoByInfoRequest();
				wareInfoByInfoRequest.setPage("1");
				wareInfoByInfoRequest.setPageSize("100");
				wareInfoByInfoRequest.setItemNum(fetchParams.get("outerId"));
				wareInfoByInfoRequest.setFields("ware_id,ware_status,title,item_num,online_time,offline_time,jd_price,stock_num");
				WareInfoByInfoSearchResponse response = client.execute(wareInfoByInfoRequest);
				List<Ware> wares = response.getWareInfos();
				for (Ware ware : wares) {
					WareSkusGetRequest wareSkusGetRequest = new WareSkusGetRequest();
					wareSkusGetRequest.setWareIds(ware.getWareId().toString());
					wareSkusGetRequest.setFields("sku_id,status,jd_price,stock_num,outer_id,color_value,size_value");
					WareSkusGetResponse wareSkusGetResponse = client.execute(wareSkusGetRequest);
					List<Sku> skus = wareSkusGetResponse.getSkus();
					JingDongProduct jingDongProductQueryParam = new JingDongProduct();
					jingDongProductQueryParam.setShopId(fetchParams.get("shopId"));
					jingDongProductQueryParam.setProductOuterId(ware.getItemNum());
					List<JingDongProduct> exists = find(jingDongProductQueryParam);
					for (JingDongProduct exist : exists) {
						delete(exist.getId(), fetchParams.get("user"));
					}
					for (Sku platformSku : skus) { // 京东平台sku的outerId为条码的全信息
						com.nano.domain.base.Sku skuQueryParam = new com.nano.domain.base.Sku();
						// 拿到sku平台的outer，转化为只保留字母与数字的格式
						String skuOuterId = platformSku.getOuterId().replaceAll(Constant.MATCH_BARCODE, "");
						char[] array = skuOuterId.toCharArray();
						List<String> barcodeArray = new ArrayList<>();
						for (char arr : array) {
							barcodeArray.add(String.valueOf(arr));
						}
						// 匹配本地sku的条码格式
						skuQueryParam.setBarcode(MessageFormat.format(Constant.FORMAT_SYSTEM_BARCODE, barcodeArray.toArray()));
						com.nano.domain.base.Sku sku = skuService.findOne(skuQueryParam);
						JingDongProduct jdProduct = new JingDongProduct();
						jdProduct.setApproveStatus("ON_SALE".equals(ware.getWareStatus()) ? "1" : "0");
						jdProduct.setBarcode(sku.getBarcode());
						jdProduct.setSkuOuterId(platformSku.getOuterId());
						jdProduct.setDeListTime(format.parse(ware.getOfflineTime()));
						jdProduct.setListTime(format.parse(ware.getOnlineTime()));
						jdProduct.setNum(platformSku.getStockNum());
						jdProduct.setProductOuterId(ware.getItemNum());
						jdProduct.setPlatformProductId(ware.getWareId());
						jdProduct.setPlatformSkuId(platformSku.getSkuId());
						jdProduct.setProductId(sku.getProductId());
						jdProduct.setProductPrice(ware.getJdPrice());
						jdProduct.setProperties(platformSku.getColorValue() + " " + platformSku.getSizeValue());
						jdProduct.setQuantity(platformSku.getStockNum());
						jdProduct.setShopId(fetchParams.get("shopId"));
						jdProduct.setSkuId(sku.getId());
						jdProduct.setSkuPrice(platformSku.getJdPrice());
						jdProduct.setTitle(ware.getTitle());
						jingDongProducts.add(jdProduct);
						save(jdProduct);
					}
				}
			} else {
				requestParams = fetchParams.get("user") + "," + fetchParams.get("barcode");
				com.nano.domain.base.Sku skuQueryParam = new com.nano.domain.base.Sku();
				// 拿到sku平台的outer，转化为只保留字母与数字的格式
				String skuOuterId = fetchParams.get("barcode").replaceAll(Constant.MATCH_BARCODE, "");
				char[] array = skuOuterId.toCharArray();
				List<String> barcodeArray = new ArrayList<>();
				for (char arr : array) {
					barcodeArray.add(String.valueOf(arr));
				}
				// 匹配本地sku的条码格式
				skuQueryParam.setBarcode(MessageFormat.format(Constant.FORMAT_SYSTEM_BARCODE, barcodeArray.toArray()));
				com.nano.domain.base.Sku sku = skuService.findOne(skuQueryParam);
				SkuCustomGetRequest skuCustomGetRequest = new SkuCustomGetRequest();
				skuCustomGetRequest.setOuterId(MessageFormat.format(Constant.FORMAT_JINGDONG_SKU_OUTER_ID, barcodeArray.toArray()));
				skuCustomGetRequest.setFields("sku_id,status,jd_price,stock_num,outer_id,color_value,size_value,ware_id");
				SkuCustomGetResponse skuCustomGetResponse = client.execute(skuCustomGetRequest);
				Sku platformSku = skuCustomGetResponse.getSku();
				WareGetRequest wareGetRequest = new WareGetRequest();
				wareGetRequest.setWareId(String.valueOf(platformSku.getWareId()));
				wareGetRequest.setFields("ware_id,ware_status,title,item_num,online_time,offline_time,jd_price");
				WareGetResponse wareGetResponse = client.execute(wareGetRequest);
				Ware ware = wareGetResponse.getWare();
				JingDongProduct jdProduct = new JingDongProduct();
				jdProduct.setApproveStatus("ON_SALE".equals(ware.getWareStatus()) ? "1" : "0");
				jdProduct.setBarcode(sku.getBarcode());
				jdProduct.setSkuOuterId(platformSku.getOuterId());
				jdProduct.setDeListTime(format.parse(ware.getOfflineTime()));
				jdProduct.setListTime(format.parse(ware.getOnlineTime()));
				jdProduct.setNum(ware.getStockNum());
				jdProduct.setProductOuterId(ware.getItemNum());
				jdProduct.setPlatformProductId(ware.getWareId());
				jdProduct.setPlatformSkuId(platformSku.getSkuId());
				jdProduct.setProductId(sku.getProductId());
				jdProduct.setProductPrice(ware.getJdPrice());
				jdProduct.setProperties(platformSku.getColorValue() + " " + platformSku.getSizeValue());
				jdProduct.setQuantity(platformSku.getStockNum());
				jdProduct.setShopId(fetchParams.get("shopId"));
				jdProduct.setSkuId(sku.getId());
				jdProduct.setSkuPrice(platformSku.getJdPrice());
				jdProduct.setTitle(ware.getTitle());
				JingDongProduct jingDongProductQueryParam = new JingDongProduct();
				jingDongProductQueryParam.setProductOuterId(ware.getItemNum());
				jingDongProductQueryParam.setShopId(fetchParams.get("shopId"));
				JingDongProduct exist = get(jingDongProductQueryParam);
				delete(exist.getId(), fetchParams.get("user"));
				save(jdProduct);
				jingDongProducts.add(jdProduct);
			}
			logger.info(String.format("调用京东查询商品接口,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()), requestParams));
			return jingDongProducts;
		} catch (JdException e) {
			logger.info(String.format("调用京东查询商品接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()), requestParams));
			throw new RuntimeException();
		} catch (ParseException e) {
			logger.info(String.format("调用京东查询商品接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()), requestParams));
			throw new RuntimeException();
		}
	}

	@Transactional
	public boolean onSale(List<JingDongProduct> jingDongProducts, String user) {
		Date begin = new Date();
		String requestParams = user + ",";
		ShopProperty shopQueryParam = new ShopProperty();
		Map<String, String> params = null;
		JdClient client = null;
		try {
			for (JingDongProduct jingDongProduct : jingDongProducts) {
				requestParams += jingDongProduct.getBarcode() + ",";
				if (StringUtils.isEmpty(shopQueryParam.getShopId())) {
					shopQueryParam.setShopId(jingDongProduct.getShopId());
					params = requestParams(shopQueryParam);
					client = new DefaultJdClient(Constant.JINGDONG_URL, params.get("accessToken"), params.get("appKey"), params.get("appSecret"));
				}
				WareUpdateListingRequest request = new WareUpdateListingRequest();
				request.setWareId(jingDongProduct.getPlatformProductId().toString());
				request.setTradeNo(UUID.generateShortUuid());
				client.execute(request);
				jingDongProduct.setApproveStatus("1");
				update(jingDongProduct);
				logger.info(String.format("商品【%s】调用京东商品上架接口,开始时间:%s，结束时间:%s,操作人:%s", jingDongProduct.getBarcode(), format.format(begin), format.format(new Date()), user));
			}
			return true;
		} catch (JdException e) {
			logger.info(String.format("调用京东商品上架接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()), requestParams));
			throw new RuntimeException();
		}
	}

	@Transactional
	public boolean inStock(List<JingDongProduct> jingDongProducts, String user) {
		Date begin = new Date();
		String requestParams = user + ",";
		ShopProperty shopQueryParam = new ShopProperty();
		Map<String, String> params = null;
		JdClient client = null;
		try {
			for (JingDongProduct jingDongProduct : jingDongProducts) {
				requestParams += jingDongProduct.getBarcode() + ",";
				if (StringUtils.isEmpty(shopQueryParam.getShopId())) {
					shopQueryParam.setShopId(jingDongProduct.getShopId());
					params = requestParams(shopQueryParam);
					client = new DefaultJdClient(Constant.JINGDONG_URL, params.get("accessToken"), params.get("appKey"), params.get("appSecret"));
				}
				WareUpdateDelistingRequest request = new WareUpdateDelistingRequest();
				request.setWareId(jingDongProduct.getPlatformProductId().toString());
				request.setTradeNo(UUID.generateShortUuid());
				client.execute(request);
				jingDongProduct.setApproveStatus("0");
				update(jingDongProduct);
				logger.info(String.format("商品【%s】调用京东商品下架接口,开始时间:%s，结束时间:%s,操作人:%s", jingDongProduct.getBarcode(), format.format(begin), format.format(new Date()), user));
			}
			return true;
		} catch (JdException e) {
			logger.info(String.format("调用京东商品下架接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()), requestParams));
			throw new RuntimeException();
		}
	}

	@Transactional
	public boolean syncStock(List<JingDongProduct> jingDongProducts, String user) {
		Date begin = new Date();
		String requestParams = user + ",";
		ShopProperty shopQueryParam = new ShopProperty();
		Map<String, String> params = null;
		JdClient client = null;
		try {
			for (JingDongProduct jingDongProduct : jingDongProducts) {
				requestParams += jingDongProduct.getBarcode() + ",";
				if (StringUtils.isEmpty(shopQueryParam.getShopId())) {
					shopQueryParam.setShopId(jingDongProduct.getShopId());
					params = requestParams(shopQueryParam);
					client = new DefaultJdClient(Constant.JINGDONG_URL, params.get("accessToken"), params.get("appKey"), params.get("appSecret"));
				}
				Map<String, String> stockQueryParam = new HashMap<String, String>();
				stockQueryParam.put("skuId", jingDongProduct.getSkuId());
				List<Stock> stocks = stockService.find(stockQueryParam);
				Long quantity = 0L;
				for (Stock stock : stocks) {
					quantity += stock.getQuantity();
				}
				WareSkuStockUpdateRequest request = new WareSkuStockUpdateRequest();
				request.setSkuId(jingDongProduct.getPlatformSkuId().toString());
				request.setOuterId(jingDongProduct.getBarcode());
				request.setQuantity(quantity.toString());
				request.setTradeNo(UUID.generateShortUuid());
				client.execute(request);
				logger.info(String.format("商品【%s】调用京东商品库存同步接口,开始时间:%s，结束时间:%s,操作人:%s", jingDongProduct.getBarcode(), format.format(begin), format.format(new Date()), user));
			}
			return true;
		} catch (JdException e) {
			logger.info(String.format("调用京东商品库存同步接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()), requestParams));
			throw new RuntimeException();
		}
	}

	public JingDongProduct get(String id) {
		return jingDongProductMapper.findOne(id);
	}

	public JingDongProduct get(JingDongProduct jingDongProduct) {
		RowBounds rowBounds = new RowBounds(0, 1);
		List<JingDongProduct> jingDongProducts = jingDongProductMapper.findMany(jingDongProduct, rowBounds);
		if (jingDongProducts.isEmpty()) {
			return null;
		}
		return jingDongProducts.get(0);
	}

	public List<JingDongProduct> find(JingDongProduct jingDongProduct) {
		return jingDongProductMapper.findMany(jingDongProduct);
	}

	public PageInfo<JingDongProduct> find(JingDongProduct jingDongProduct, int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<JingDongProduct> jingDongProducts = jingDongProductMapper.findMany(jingDongProduct, rowBounds);
		PageInfo<JingDongProduct> pageInfo = new PageInfo<>(jingDongProducts);
		return pageInfo;
	}

	@Transactional
	public JingDongProduct save(JingDongProduct jingDongProduct) {
		jingDongProductMapper.saveOne(jingDongProduct);
		return jingDongProduct;
	}

	@Transactional
	public List<JingDongProduct> save(List<JingDongProduct> jingDongProducts) {
		for (JingDongProduct jingDongProduct : jingDongProducts) {
			save(jingDongProduct);
		}
		return jingDongProducts;
	}

	@Transactional
	public JingDongProduct update(JingDongProduct jingDongProduct) {
		long resultCnt = jingDongProductMapper.updateOne(jingDongProduct);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION, jingDongProduct.getId() + " : " + jingDongProduct.getBarcode()));
		}
		return jingDongProduct;
	}

	@Transactional
	public JingDongProduct delete(String id, String user) {
		JingDongProduct jingDongProduct = get(id);
		logger.info(String.format("用户【%s】删除京东铺货信息【%s】.", user, jingDongProduct.getBarcode()));
		long resultCnt = jingDongProductMapper.deleteOne(jingDongProduct);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION, jingDongProduct.getId() + " : " + jingDongProduct.getBarcode()));
		}
		return jingDongProduct;
	}

	@Transactional
	public List<JingDongProduct> delete(List<String> ids, String user) {
		List<JingDongProduct> jingDongProducts = new ArrayList<JingDongProduct>();
		for (String id : ids) {
			jingDongProducts.add(delete(id, user));
		}
		return jingDongProducts;
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
			if ("accesstoken".equals(shopProperty.getCode().toLowerCase())) {
				map.put("token", shopProperty.getValue());
				continue;
			}
		}
		return map;
	}
}
