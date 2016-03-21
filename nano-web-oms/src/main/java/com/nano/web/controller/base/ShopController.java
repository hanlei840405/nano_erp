package com.nano.web.controller.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Category;
import com.nano.domain.base.Shop;
import com.nano.domain.base.ShopCategory;
import com.nano.domain.base.ShopProperty;
import com.nano.service.base.*;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.ShopPropertyVO;
import com.nano.web.vo.base.ShopVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("base/shop")
public class ShopController {
	private static final Logger logger = LoggerFactory.getLogger(ShopController.class);
	@Autowired
	private ShopService shopService;
	@Autowired
	private ShopPropertyService shopPropertyService;
	@Autowired
	private ShopCategoryService shopCategoryService;
	@Autowired
	private CategoryService categoryService;
	@Autowired
	private PlatformService platformService;

	private ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<ShopVO> save(ShopVO vo, HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行保存店铺信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<ShopVO> responseVO = new ResponseVO<>();
		Shop shop;
		try {

			if (StringUtils.isEmpty(vo.getId())) {
				shop = new Shop();
				shop.setCreator(session.getAttribute("userId").toString());
			} else {
				shop = shopService.findOne(vo.getId());
				shop.setModifier(session.getAttribute("userId").toString());
			}
			BeanUtils.copyProperties(vo, shop, "id", "creator", "modifier");

			List<ShopPropertyVO> shopPropertyVOs = mapper.readValue(request.getParameter("properties"), new TypeReference<List<ShopPropertyVO>>() {
			});
			for (ShopPropertyVO shopPropertyVO : shopPropertyVOs) {
				ShopProperty shopProperty = new ShopProperty();
				BeanUtils.copyProperties(shopPropertyVO, shopProperty, "id", "creator", "shopId");
				shopProperty.setCreator(session.getAttribute("userId").toString());
				shopProperty.setShopId(shop.getId());
				shop.getShopProperties().add(shopProperty);
			}

			List<String> categoryIds = mapper.readValue(request.getParameter("categories"), new TypeReference<List<String>>() {
			});
			for (String categoryId : categoryIds) {
				ShopCategory shopCategory = new ShopCategory();
				shopCategory.setCategoryId(categoryId);
				shopCategory.setShopId(shop.getId());
				shop.getShopCategories().add(shopCategory);
			}

			if (StringUtils.isEmpty(vo.getId())) {
				shopService.save(shop);
			} else {
				shopService.update(shop);
			}
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
	public @ResponseBody ResponseVO<ShopVO> delete(@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行删除店铺信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<ShopVO> responseVO = new ResponseVO<>();
		try {
			shopService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(true);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/queryShops", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody PageVO<ShopVO> queryShops(ShopVO vo, @RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行分页查询店铺信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		Shop queryParam = new Shop();
		BeanUtils.copyProperties(vo, queryParam, "id");
		PageInfo<Shop> pageInfo = shopService.find(queryParam, page - 1, rows);
		PageVO<ShopVO> pageVO = new PageVO<>();
		pageVO.setTotal(pageInfo.getTotal());
		for (Shop shop : pageInfo.getList()) {
			ShopVO shopVO = new ShopVO();
			shopVO.setCode(shop.getCode());
			shopVO.setName(shop.getName());
			shopVO.setId(shop.getId());
			shopVO.setStatus(shop.getStatus());
			if (!StringUtils.isEmpty(shop.getPlatformId())) {
				shopVO.setPlatformId(shop.getPlatformId());
				shopVO.setPlatformName(platformService.findOne(shop.getPlatformId()).getName());
			}
			ShopCategory shopCategoryQuery = new ShopCategory();
			shopCategoryQuery.setShopId(shop.getId());
			List<ShopCategory> shopCategories = shopCategoryService.find(shopCategoryQuery);
			for (ShopCategory shopCategory : shopCategories) {
				shopVO.getCategoryIds().add(shopCategory.getCategoryId());
				Category category = categoryService.findOne(shopCategory.getCategoryId());
				shopVO.getCategoryNames().add(category.getName());
			}
			pageVO.getRows().add(shopVO);
		}
		return pageVO;
	}

	@RequestMapping(value = "/queryAllShops", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List<ShopVO> queryAllShops(String platformId) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行查询全部店铺信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		Shop queryParam = new Shop();
		if(!StringUtils.isEmpty(platformId)){
			queryParam.setPlatformId(platformId);
		}
		List<Shop> shops = shopService.find(queryParam);
		List<ShopVO> vos = new ArrayList<>();
		for (Shop shop : shops) {
			ShopVO shopVO = new ShopVO();
			shopVO.setCode(shop.getCode());
			shopVO.setName(shop.getName());
			shopVO.setId(shop.getId());
			shopVO.setStatus(shop.getStatus());
			if (!StringUtils.isEmpty(shop.getPlatformId())) {
				shopVO.setPlatformId(shop.getPlatformId());
				shopVO.setPlatformName(platformService.findOne(shop.getPlatformId()).getName());
			}
			ShopCategory shopCategoryQuery = new ShopCategory();
			shopCategoryQuery.setShopId(shop.getId());
			List<ShopCategory> shopCategories = shopCategoryService.find(shopCategoryQuery);
			for (ShopCategory shopCategory : shopCategories) {
				shopVO.getCategoryIds().add(shopCategory.getCategoryId());
				Category category = categoryService.findOne(shopCategory.getCategoryId());
				shopVO.getCategoryNames().add(category.getName());
			}
			vos.add(shopVO);
		}
		return vos;
	}

	@RequestMapping(value = "/queryShopProperties", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List<ShopPropertyVO> queryShopProperties(String shopId) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行查询店铺属性信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		List<ShopPropertyVO> vos = new ArrayList<>();
		if (StringUtils.isEmpty(shopId)) {
			return vos;
		}
		ShopProperty queryParam = new ShopProperty();
		queryParam.setShopId(shopId);
		List<ShopProperty> shopProperties = shopPropertyService.find(queryParam);
		for (ShopProperty shopProperty : shopProperties) {
			ShopPropertyVO vo = new ShopPropertyVO();
			vo.setId(shopProperty.getId());
			vo.setShopId(shopProperty.getShopId());
			vo.setCode(shopProperty.getCode());
			vo.setValue(shopProperty.getValue());
			vos.add(vo);
		}
		return vos;
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST, RequestMethod.GET })
	public String init() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问店铺页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
		return "base/shop/init";
	}

}
