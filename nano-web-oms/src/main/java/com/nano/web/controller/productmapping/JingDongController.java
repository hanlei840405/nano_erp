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
import com.nano.domain.productmapping.JingDongProduct;
import com.nano.service.base.CategoryService;
import com.nano.service.base.ContactService;
import com.nano.service.base.PlatformService;
import com.nano.service.base.ProductService;
import com.nano.service.base.ShopCategoryService;
import com.nano.service.base.ShopService;
import com.nano.service.base.SkuService;
import com.nano.service.productmapping.JingDongProductService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.productmapping.JingDongProductVO;

@Controller
@RequestMapping("productMapping/jingDong")
public class JingDongController {
	private static final Logger logger = LoggerFactory.getLogger(JingDongController.class);
	@Autowired
	private JingDongProductService jingDongProductService;
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
	public @ResponseBody ResponseVO<JingDongProductVO> fetch(String shopId,String outerId,String barcode) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行商品京东平台铺货操作.", session.getAttribute("username"), subject.getPrincipal()));
		ResponseVO<JingDongProductVO> responseVO = new ResponseVO<>();
		try {
			Map<String,String> fetchParams = new HashMap<>();
			fetchParams.put("user", String.format("%s【%s】", session.getAttribute("username"), subject.getPrincipal()));
			fetchParams.put("shopId", shopId);
			if(!StringUtils.isEmpty(outerId)){
				fetchParams.put("outerId", outerId);
			}else{
				fetchParams.put("barcode", barcode);
			}
			jingDongProductService.fetchData(fetchParams);
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
	public @ResponseBody ResponseVO<JingDongProductVO> synchStock(@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行商品京东平台库存同步操作.", session.getAttribute("username"), subject.getPrincipal()));
		ResponseVO<JingDongProductVO> responseVO = new ResponseVO<>();
		try {
			List<JingDongProduct> jingDongProducts = new ArrayList<>();
			for(String id : ids){
				JingDongProduct jingDongProduct = jingDongProductService.get(id);
				jingDongProducts.add(jingDongProduct);
			}
			jingDongProductService.syncStock(jingDongProducts,String.format("%s【%s】", session.getAttribute("username"), subject.getPrincipal()));
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
	public @ResponseBody ResponseVO<JingDongProductVO> delete(@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行删除京东平台铺货信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<JingDongProductVO> responseVO = new ResponseVO<>();
		try {
			jingDongProductService.delete(ids,String.format("%s【%s】", session.getAttribute("username"), subject.getPrincipal()));
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(true);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/queryJingDongProductSkuMapping", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody PageVO<JingDongProductVO> queryJingDongProductSkuMapping(JingDongProductVO vo, @RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行京东平台铺货信息查询操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		JingDongProduct jingDongProductQueryParam = new JingDongProduct();
		if(StringUtils.isEmpty(vo.getShopId())){
			jingDongProductQueryParam.setShopId(vo.getShopId());
		}
		if(StringUtils.isEmpty(vo.getProductOuterId())){
			jingDongProductQueryParam.setProductOuterId(vo.getProductOuterId());
		}
		if(StringUtils.isEmpty(vo.getBarcode())){
			jingDongProductQueryParam.setBarcode(vo.getBarcode());
		}
		
		PageInfo<JingDongProduct> pageInfo = jingDongProductService.find(jingDongProductQueryParam, page, rows);
		PageVO<JingDongProductVO> pageVO = new PageVO<>();
		pageVO.setTotal(pageInfo.getTotal());
		for (JingDongProduct jingDongProduct : pageInfo.getList()) {
			Product product = productService.findOne(jingDongProduct.getProductId());
			Shop shop = shopService.findOne(product.getShopId());
			Sku sku = skuService.findOne(jingDongProduct.getSkuId());
			Contact contact = contactService.findOne(product.getContactId());
			JingDongProductVO jingDongProductVO = new JingDongProductVO();
			jingDongProductVO.setApproveStatus(jingDongProduct.getApproveStatus());
			jingDongProductVO.setSkuOuterId(jingDongProduct.getSkuOuterId());
			jingDongProductVO.setBarcode(jingDongProduct.getBarcode());
			jingDongProductVO.setDeListTime(format.format(jingDongProduct.getDeListTime()));
			jingDongProductVO.setId(jingDongProduct.getId());
			jingDongProductVO.setListTime(format.format(jingDongProduct.getListTime()));
			jingDongProductVO.setName(product.getFullName());
			jingDongProductVO.setNum(jingDongProduct.getNum());
			jingDongProductVO.setPlatformProductId(jingDongProduct.getPlatformProductId());
			jingDongProductVO.setProductOuterId(jingDongProduct.getProductOuterId());
			jingDongProductVO.setProductPrice(jingDongProduct.getProductPrice());
			jingDongProductVO.setProperties(jingDongProduct.getProperties());
			jingDongProductVO.setQuantity(jingDongProduct.getQuantity());
			jingDongProductVO.setShopId(shop.getId());
			jingDongProductVO.setShopName(shop.getName());
			jingDongProductVO.setPlatformSkuId(jingDongProduct.getPlatformSkuId());
			jingDongProductVO.setSkuPrice(jingDongProduct.getSkuPrice());
			jingDongProductVO.setSpec(sku.getSpec());
			jingDongProductVO.setTitle(jingDongProduct.getTitle());
			jingDongProductVO.setContact(contact.getName());
			pageVO.getRows().add(jingDongProductVO);
		}
		return pageVO;
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST, RequestMethod.GET })
	public String init(Model model) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问京东铺货页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
		Platform platformQueryParam = new Platform();
		platformQueryParam.setCode("jingdong");
		List<Platform> platforms = platformService.find(platformQueryParam);
		if(platforms.isEmpty()){
			model.addAttribute("platformId", null);
		}else{
			model.addAttribute("platformId", platforms.get(0).getId());
		}
		return "productmapping/jingdong/init";
	}
}
