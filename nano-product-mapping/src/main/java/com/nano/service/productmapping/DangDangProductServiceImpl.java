package com.nano.service.productmapping;

import java.io.IOException;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.ibatis.session.RowBounds;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.constant.Constant;
import com.nano.domain.base.ShopProperty;
import com.nano.domain.productmapping.DangDangProduct;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.productmapping.DangDangProductMapper;
import com.nano.service.base.ShopPropertyService;
import com.nano.service.base.SkuService;
import com.nano.service.base.StockService;
import com.nano.util.MD5Util;

@Service
public class DangDangProductServiceImpl implements DangDangProductService {
    private static final Logger logger = LoggerFactory.getLogger(DangDangProductServiceImpl.class);

    @Autowired
    private DangDangProductMapper dangDangProductMapper;

    @Autowired
    private SkuService skuService;

    @Autowired
    private ShopPropertyService shopPropertyService;

    @Autowired
    private StockService stockService;

    private SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @SuppressWarnings("unchecked")
	@Transactional
    public List<DangDangProduct> fetchData(Map<String, String> fetchParams) {
        Date begin = new Date();
        
        ShopProperty shopQueryParam = new ShopProperty();
        shopQueryParam.setShopId(fetchParams.get("shopId"));
        Map<String, String> params = requestParams(shopQueryParam);
        String secret = params.get("secret");
        String appkey = params.get("appkey");
        String session = params.get("session");
        String timestamp = format.format(begin);
        RequestConfig requestConfig = RequestConfig.custom().setSocketTimeout(3000).setConnectTimeout(3000).build();
        CloseableHttpClient client = HttpClientBuilder.create().build();
    	String requestParams = null;
        try {
            String method = "dangdang.items.list.get";
            String temp = secret + "app_key" + appkey + "formatxmlmethod" + method + "session" + session + "sign_methodmd5timestamp" + timestamp + "v1.0" + secret;
            String sign = MD5Util.getMD5(temp);
            String urlParams;
            String url;
            if (fetchParams.containsKey("outerId")) {
            	requestParams = fetchParams.get("user") + "," + fetchParams.get("outerId");
                // 删除现有的铺货数据
                DangDangProduct dangDangProductQueryParam = new DangDangProduct();
                dangDangProductQueryParam.setShopId(fetchParams.get("shopId"));
                dangDangProductQueryParam.setProductOuterId(fetchParams.get("outerId"));
                List<DangDangProduct> dangDangProducts = find(dangDangProductQueryParam);
                for(DangDangProduct dangDangProduct : dangDangProducts){
                	delete(dangDangProduct.getId(), fetchParams.get("user"));
                }
                
            	urlParams = "v=1.0&sign=" + sign + "&sign_method=md5&timestamp=" + timestamp + "&app_key=" + appkey + "&method=" + method + "&format=xml&session=" + session;
                url = Constant.DANGDANG_URL + urlParams + "&itm=" + fetchParams.get("outerId");
                url = url.replaceAll(" ", "%20");
            } else {
            	requestParams = fetchParams.get("user") + "," + fetchParams.get("barcode");
                // 删除现有的铺货数据
                DangDangProduct dangDangProductQueryParam = new DangDangProduct();
                dangDangProductQueryParam.setShopId(fetchParams.get("shopId"));
                dangDangProductQueryParam.setBarcode(fetchParams.get("barcode"));
                DangDangProduct dangDangProduct = get(dangDangProductQueryParam);
                dangDangProductQueryParam.setBarcode(null);
                dangDangProductQueryParam.setProductOuterId(dangDangProduct.getProductOuterId());
                List<DangDangProduct> dangDangProducts = find(dangDangProductQueryParam);
                for(DangDangProduct ddProduct : dangDangProducts){
                	delete(ddProduct.getId(), fetchParams.get("user"));
                }
                urlParams = "v=1.0&sign=" + sign + "&sign_method=md5&timestamp=" + timestamp + "&app_key=" + appkey + "&method=" + method + "&format=xml&session=" + session;
                url = Constant.DANGDANG_URL + urlParams + "&bar_code=" + fetchParams.get("barcode");
                url = url.replaceAll(" ", "%20");
            }
            HttpPost httpPost = new HttpPost(url);
            httpPost.setConfig(requestConfig);
            CloseableHttpResponse closeableHttpResponse = client.execute(httpPost);
            HttpEntity httpEntity = closeableHttpResponse.getEntity();
            SAXReader saxReader = new SAXReader();
			Document document = saxReader.read(httpEntity.getContent());
			Element root = document.getRootElement();
			Element itemList = root.element("ItemsList");
			List<Element> itemInfos = itemList.elements("ItemInfo");
			method = "dangdang.item.get";
			List<DangDangProduct> dangDangProducts = new ArrayList<>();
			for (Element el : itemInfos) {
				timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
				temp = secret + "app_key" + appkey + "formatxmlmethod" + method + "session" + session + "sign_methodmd5timestamp" + timestamp + "v1.0" + secret;
				sign = MD5Util.getMD5(temp);
				urlParams = "v=1.0&sign=" + sign + "&sign_method=md5&timestamp=" + timestamp + "&app_key=" + appkey + "&method=" + method + "&format=xml&session=" + session;
				url = Constant.DANGDANG_URL + urlParams + "&it=" + el.valueOf("itemID");
				url = url.replaceAll(" ", "%20");
	            HttpPost post = new HttpPost(url);
	            post.setConfig(requestConfig);
	            CloseableHttpResponse response = client.execute(post);
	            HttpEntity entity = response.getEntity();
	            SAXReader detailReader = new SAXReader();
				Document detailDocument = detailReader.read(entity.getContent());
				Element detailRoot = detailDocument.getRootElement();
				Element itemDetail = detailRoot.element("ItemDetail");
				List<Element> siiELList = itemDetail.elements("SpecilaItemInfo");
				for (Element sii : siiELList) {
					com.nano.domain.base.Sku skuQueryParam = new com.nano.domain.base.Sku();
					// 拿到sku平台的outer，转化为只保留字母与数字的格式
					String skuOuterId = sii.valueOf("outerItemID").replaceAll(Constant.MATCH_BARCODE, "");
					char[] array = skuOuterId.toCharArray();
					List<String> barcodeArray = new ArrayList<>();
					for(char arr : array){
						barcodeArray.add(String.valueOf(arr));
					}
					// 匹配本地sku的条码格式
					skuQueryParam.setBarcode(MessageFormat.format(Constant.FORMAT_SYSTEM_BARCODE, barcodeArray.toArray()));
					com.nano.domain.base.Sku sku = skuService.findOne(skuQueryParam);
					
					DangDangProduct dangDangProduct = new DangDangProduct();
					dangDangProduct.setShopId(fetchParams.get("shopId"));
					dangDangProduct.setProductId(sku.getProductId());
					dangDangProduct.setSkuId(sku.getId());
					dangDangProduct.setBarcode(sku.getBarcode());
					dangDangProduct.setApproveStatus("itemState".equals(el.valueOf("itemState")) ? "1" : "0");
					dangDangProduct.setPlatformProductId((Long.valueOf(el.valueOf("itemID"))));
					dangDangProduct.setProductOuterId(el.valueOf("model"));
					dangDangProduct.setTitle(el.valueOf("itemName"));
					dangDangProduct.setApproveStatus(el.valueOf("itemState"));
					dangDangProduct.setProductPrice(el.valueOf("unitPrice"));
					dangDangProduct.setNum(Long.valueOf(el.valueOf("stockCount")));
					dangDangProduct.setSkuOuterId(sii.valueOf("outerItemID"));
					dangDangProduct.setSkuPrice(sii.valueOf("unitPrice"));
					dangDangProduct.setQuantity(Long.valueOf(sii.valueOf("stockCount")));
					dangDangProduct.setPlatformSkuId(Long.valueOf(sii.valueOf("subItemID")));
					dangDangProduct.setUpdateTime(format.parse(el.valueOf("updateTime")));

					String specialAttribute = sii.valueOf("specialAttribute");
					if (specialAttribute != null) {
						StringBuffer buffer = new StringBuffer();
						String[] specArray = specialAttribute.split(";");
						for (String spec : specArray) {
							String[] spArray = spec.split(">>");
							buffer.append(spArray[1]).append(" ");
						}
						buffer.deleteCharAt(buffer.lastIndexOf(" "));
						dangDangProduct.setProperties(buffer.toString());
					}
					save(dangDangProduct);
					dangDangProducts.add(dangDangProduct);
				}
				response.close();
			}
            closeableHttpResponse.close();
    		logger.info(String.format("调用当当查询商品接口,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()),requestParams));
            return dangDangProducts;
        } catch (IOException e) {
			logger.info(String.format("调用京东查询商品接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()),requestParams));
            throw new RuntimeException();
        } catch (IllegalStateException e) {
			logger.info(String.format("调用京东查询商品接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()),requestParams));
			throw new RuntimeException();
		} catch (DocumentException e) {
			logger.info(String.format("调用京东查询商品接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()),requestParams));
			throw new RuntimeException();
		} catch (ParseException e) {
			logger.info(String.format("调用京东查询商品接口出现异常,开始时间:%s，结束时间:%s,相关内容:%s", format.format(begin), format.format(new Date()),requestParams));
			throw new RuntimeException();
		}
    }

    @Transactional
    public boolean onSale(List<DangDangProduct> dangDangProducts,String user) {
        return false;
    }

    @Transactional
    public boolean inStock(List<DangDangProduct> dangDangProducts,String user) {
        return false;
    }

    @Transactional
    public boolean syncStock(List<DangDangProduct> dangDangProducts,String user) {
        return false;
    }

    public DangDangProduct get(String id) {
        return dangDangProductMapper.findOne(id);
    }

    public DangDangProduct get(DangDangProduct dangDangProduct) {
        RowBounds rowBounds = new RowBounds(0, 1);
        List<DangDangProduct> dangDangProducts = dangDangProductMapper.findMany(dangDangProduct, rowBounds);
        if (dangDangProducts.isEmpty()) {
            return null;
        }
        return dangDangProducts.get(0);
    }

    public List<DangDangProduct> find(DangDangProduct dangDangProduct) {
        return dangDangProductMapper.findMany(dangDangProduct);
    }

    public PageInfo<DangDangProduct> find(DangDangProduct dangDangProduct, int pageNo, int pageSize) {
        RowBounds rowBounds = new RowBounds(pageNo, pageSize);
        List<DangDangProduct> dangDangProducts = dangDangProductMapper.findMany(dangDangProduct, rowBounds);
        PageInfo<DangDangProduct> pageInfo = new PageInfo<>(dangDangProducts);
        return pageInfo;
    }

    @Transactional
    public DangDangProduct save(DangDangProduct dangDangProduct) {
        dangDangProductMapper.saveOne(dangDangProduct);
        return dangDangProduct;
    }

    @Transactional
    public List<DangDangProduct> save(List<DangDangProduct> dangDangProducts) {
        for (DangDangProduct dangDangProduct : dangDangProducts) {
            save(dangDangProduct);
        }
        return dangDangProducts;
    }

    @Transactional
    public DangDangProduct update(DangDangProduct dangDangProduct) {
        long resultCnt = dangDangProductMapper.updateOne(dangDangProduct);
        if (resultCnt == 0) {
            throw new NanoDBConsistencyRuntimeException(String.format(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION, dangDangProduct.getId() + " : " + dangDangProduct.getBarcode()));
        }
        return dangDangProduct;
    }

    @Transactional
    public DangDangProduct delete(String id, String user) {
        DangDangProduct dangDangProduct = get(id);
        logger.info(String.format("用户【%s】删除当当铺货信息【%s】.", user, dangDangProduct.getBarcode()));
        long resultCnt = dangDangProductMapper.deleteOne(dangDangProduct);
        if (resultCnt == 0) {
            throw new NanoDBConsistencyRuntimeException(String.format(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION, dangDangProduct.getId() + " : " + dangDangProduct.getBarcode()));
        }
        return dangDangProduct;
    }

    @Transactional
    public List<DangDangProduct> delete(List<String> ids, String user) {
        List<DangDangProduct> dangDangProducts = new ArrayList<DangDangProduct>();
        for (String id : ids) {
            dangDangProducts.add(delete(id, user));
        }
        return dangDangProducts;
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
            if ("session".equals(shopProperty.getCode().toLowerCase())) {
                map.put("session", shopProperty.getValue());
                continue;
            }
        }
        return map;
    }
}
