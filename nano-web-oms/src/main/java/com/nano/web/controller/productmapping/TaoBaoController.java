package com.nano.web.controller.productmapping;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Contact;
import com.nano.domain.base.Platform;
import com.nano.domain.base.Product;
import com.nano.domain.base.Shop;
import com.nano.domain.base.Sku;
import com.nano.domain.productmapping.TaoBaoProduct;
import com.nano.service.base.CategoryService;
import com.nano.service.base.ContactService;
import com.nano.service.base.PlatformService;
import com.nano.service.base.ProductService;
import com.nano.service.base.ShopCategoryService;
import com.nano.service.base.ShopService;
import com.nano.service.base.SkuService;
import com.nano.service.productmapping.TaoBaoProductService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.productmapping.TaoBaoProductVO;

@Controller
@RequestMapping("productMapping/taoBao")
public class TaoBaoController {
	private static final Logger logger = LoggerFactory.getLogger(TaoBaoController.class);
	@Autowired
	private TaoBaoProductService taoBaoProductService;
	@Autowired
	private ProductService productService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private ShopService shopService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private PlatformService platformService;
	@Autowired
	private ContactService contactService;

	@RequestMapping(value = "/fetch", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<TaoBaoProductVO> fetch(String shopId,String outerId,String barcode) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行商品淘宝平台铺货操作.", session.getAttribute("username"), subject.getPrincipal()));
		ResponseVO<TaoBaoProductVO> responseVO = new ResponseVO<>();
		try {
			Map<String,String> fetchParams = new HashMap<>();
			fetchParams.put("user", String.format("%s【%s】", session.getAttribute("username"), subject.getPrincipal()));
			fetchParams.put("shopId", shopId);
			if(!StringUtils.isEmpty(outerId)){
				fetchParams.put("outerId", outerId);
			}else{
				fetchParams.put("barcode", barcode);
			}
			taoBaoProductService.fetchData(fetchParams);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(false);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/synchStock", method = RequestMethod.GET)
	public @ResponseBody ResponseVO<TaoBaoProductVO> synchStock(@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行商品淘宝平台库存同步操作.", session.getAttribute("username"), subject.getPrincipal()));
		ResponseVO<TaoBaoProductVO> responseVO = new ResponseVO<>();
		try {
			List<TaoBaoProduct> taoBaoProducts = new ArrayList<>();
			for(String id : ids){
				TaoBaoProduct taoBaoProduct = taoBaoProductService.get(id);
				taoBaoProducts.add(taoBaoProduct);
			}
			taoBaoProductService.syncStock(taoBaoProducts,String.format("%s【%s】", session.getAttribute("username"), subject.getPrincipal()));
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(false);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/delete", method = RequestMethod.GET)
	public @ResponseBody ResponseVO<TaoBaoProductVO> delete(@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行删除淘宝平台铺货信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<TaoBaoProductVO> responseVO = new ResponseVO<>();
		try {
			taoBaoProductService.delete(ids,String.format("%s【%s】", session.getAttribute("username"), subject.getPrincipal()));
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(true);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/queryTaoBaoProductSkuMapping", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody PageVO<TaoBaoProductVO> queryTaoBaoProductSkuMapping(TaoBaoProductVO vo, @RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行淘宝平台铺货信息查询操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		TaoBaoProduct taoBaoProductQueryParam = new TaoBaoProduct();
		if(StringUtils.isEmpty(vo.getShopId())){
			taoBaoProductQueryParam.setShopId(vo.getShopId());
		}
		if(StringUtils.isEmpty(vo.getProductOuterId())){
			taoBaoProductQueryParam.setProductOuterId(vo.getProductOuterId());
		}
		if(StringUtils.isEmpty(vo.getBarcode())){
			taoBaoProductQueryParam.setBarcode(vo.getBarcode());
		}
		
		PageInfo<TaoBaoProduct> pageInfo = taoBaoProductService.find(taoBaoProductQueryParam, page, rows);
		PageVO<TaoBaoProductVO> pageVO = new PageVO<>();
		pageVO.setTotal(pageInfo.getTotal());
		for (TaoBaoProduct taoBaoProduct : pageInfo.getList()) {
			Product product = productService.findOne(taoBaoProduct.getProductId());
			Shop shop = shopService.findOne(product.getShopId());
			Sku sku = skuService.findOne(taoBaoProduct.getSkuId());
			Contact contact = contactService.findOne(product.getContactId());
			TaoBaoProductVO taoBaoProductVO = new TaoBaoProductVO();
			taoBaoProductVO.setApproveStatus(taoBaoProduct.getApproveStatus());
			taoBaoProductVO.setSkuOuterId(taoBaoProduct.getSkuOuterId());
			taoBaoProductVO.setBarcode(taoBaoProduct.getBarcode());
			taoBaoProductVO.setDeListTime(format.format(taoBaoProduct.getDeListTime()));
			taoBaoProductVO.setId(taoBaoProduct.getId());
			taoBaoProductVO.setListTime(format.format(taoBaoProduct.getListTime()));
			taoBaoProductVO.setName(product.getFullName());
			taoBaoProductVO.setNum(taoBaoProduct.getNum());
			taoBaoProductVO.setPlatformProductId(taoBaoProduct.getPlatformProductId());
			taoBaoProductVO.setProductOuterId(taoBaoProduct.getProductOuterId());
			taoBaoProductVO.setProductPrice(taoBaoProduct.getProductPrice());
			taoBaoProductVO.setProperties(taoBaoProduct.getProperties());
			taoBaoProductVO.setQuantity(taoBaoProduct.getQuantity());
			taoBaoProductVO.setShopId(shop.getId());
			taoBaoProductVO.setShopName(shop.getName());
			taoBaoProductVO.setPlatformSkuId(taoBaoProduct.getPlatformSkuId());
			taoBaoProductVO.setSkuPrice(taoBaoProduct.getSkuPrice());
			taoBaoProductVO.setSpec(sku.getSpec());
			taoBaoProductVO.setTitle(taoBaoProduct.getTitle());
			taoBaoProductVO.setContact(contact.getName());
			pageVO.getRows().add(taoBaoProductVO);
		}
		return pageVO;
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST, RequestMethod.GET })
	public String init(Model model) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问淘宝铺货页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
		Platform platformQueryParam = new Platform();
		platformQueryParam.setCode("taobao");
		List<Platform> platforms = platformService.find(platformQueryParam);
		if(platforms.isEmpty()){
			model.addAttribute("platformId", null);
		}else{
			model.addAttribute("platformId", platforms.get(0).getId());
		}
		return "productmapping/taobao/init";
	}
}
