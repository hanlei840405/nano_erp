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
import com.nano.domain.productmapping.DangDangProduct;
import com.nano.service.base.CategoryService;
import com.nano.service.base.ContactService;
import com.nano.service.base.PlatformService;
import com.nano.service.base.ProductService;
import com.nano.service.base.ShopCategoryService;
import com.nano.service.base.ShopService;
import com.nano.service.base.SkuService;
import com.nano.service.productmapping.DangDangProductService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.productmapping.DangDangProductVO;

@Controller
@RequestMapping("productMapping/dangDang")
public class DangDangController {
	private static final Logger logger = LoggerFactory.getLogger(DangDangController.class);
	@Autowired
	private DangDangProductService dangDangProductService;
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
	public @ResponseBody ResponseVO<DangDangProductVO> fetch(String shopId,String outerId,String barcode) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行商品当当平台铺货操作.", session.getAttribute("username"), subject.getPrincipal()));
		ResponseVO<DangDangProductVO> responseVO = new ResponseVO<>();
		try {
			Map<String,String> fetchParams = new HashMap<>();
			fetchParams.put("user", String.format("%s【%s】", session.getAttribute("username"), subject.getPrincipal()));
			fetchParams.put("shopId", shopId);
			if(!StringUtils.isEmpty(outerId)){
				fetchParams.put("outerId", outerId);
			}else{
				fetchParams.put("barcode", barcode);
			}
			dangDangProductService.fetchData(fetchParams);
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
	public @ResponseBody ResponseVO<DangDangProductVO> synchStock(@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行商品当当平台库存同步操作.", session.getAttribute("username"), subject.getPrincipal()));
		ResponseVO<DangDangProductVO> responseVO = new ResponseVO<>();
		try {
			List<DangDangProduct> dangDangProducts = new ArrayList<>();
			for(String id : ids){
				DangDangProduct dangDangProduct = dangDangProductService.get(id);
				dangDangProducts.add(dangDangProduct);
			}
			dangDangProductService.syncStock(dangDangProducts,String.format("%s【%s】", session.getAttribute("username"), subject.getPrincipal()));
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
	public @ResponseBody ResponseVO<DangDangProductVO> delete(@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行删除当当平台铺货信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<DangDangProductVO> responseVO = new ResponseVO<>();
		try {
			dangDangProductService.delete(ids,String.format("%s【%s】", session.getAttribute("username"), subject.getPrincipal()));
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(true);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/queryDangDangProductSkuMapping", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody PageVO<DangDangProductVO> queryDangDangProductSkuMapping(DangDangProductVO vo, @RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行当当平台铺货信息查询操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		DangDangProduct dangDangProductQueryParam = new DangDangProduct();
		if(StringUtils.isEmpty(vo.getShopId())){
			dangDangProductQueryParam.setShopId(vo.getShopId());
		}
		if(StringUtils.isEmpty(vo.getProductOuterId())){
			dangDangProductQueryParam.setProductOuterId(vo.getProductOuterId());
		}
		if(StringUtils.isEmpty(vo.getBarcode())){
			dangDangProductQueryParam.setBarcode(vo.getBarcode());
		}
		
		PageInfo<DangDangProduct> pageInfo = dangDangProductService.find(dangDangProductQueryParam, page, rows);
		PageVO<DangDangProductVO> pageVO = new PageVO<>();
		pageVO.setTotal(pageInfo.getTotal());
		for (DangDangProduct dangDangProduct : pageInfo.getList()) {
			Product product = productService.findOne(dangDangProduct.getProductId());
			Shop shop = shopService.findOne(product.getShopId());
			Sku sku = skuService.findOne(dangDangProduct.getSkuId());
			Contact contact = contactService.findOne(product.getContactId());
			DangDangProductVO dangDangProductVO = new DangDangProductVO();
			dangDangProductVO.setApproveStatus(dangDangProduct.getApproveStatus());
			dangDangProductVO.setSkuOuterId(dangDangProduct.getSkuOuterId());
			dangDangProductVO.setBarcode(dangDangProduct.getBarcode());
			dangDangProductVO.setUpdateTime(format.format(dangDangProduct.getUpdateTime()));
			dangDangProductVO.setId(dangDangProduct.getId());
			dangDangProductVO.setName(product.getFullName());
			dangDangProductVO.setNum(dangDangProduct.getNum());
			dangDangProductVO.setPlatformProductId(dangDangProduct.getPlatformProductId());
			dangDangProductVO.setProductOuterId(dangDangProduct.getProductOuterId());
			dangDangProductVO.setProductPrice(dangDangProduct.getProductPrice());
			dangDangProductVO.setProperties(dangDangProduct.getProperties());
			dangDangProductVO.setQuantity(dangDangProduct.getQuantity());
			dangDangProductVO.setShopId(shop.getId());
			dangDangProductVO.setShopName(shop.getName());
			dangDangProductVO.setPlatformSkuId(dangDangProduct.getPlatformSkuId());
			dangDangProductVO.setSkuPrice(dangDangProduct.getSkuPrice());
			dangDangProductVO.setSpec(sku.getSpec());
			dangDangProductVO.setTitle(dangDangProduct.getTitle());
			dangDangProductVO.setContact(contact.getName());
			pageVO.getRows().add(dangDangProductVO);
		}
		return pageVO;
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST, RequestMethod.GET })
	public String init(Model model) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问当当铺货页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
		Platform platformQueryParam = new Platform();
		platformQueryParam.setCode("dangdang");
		List<Platform> platforms = platformService.find(platformQueryParam);
		if(platforms.isEmpty()){
			model.addAttribute("platformId", null);
		}else{
			model.addAttribute("platformId", platforms.get(0).getId());
		}
		return "productmapping/dangdang/init";
	}
}
