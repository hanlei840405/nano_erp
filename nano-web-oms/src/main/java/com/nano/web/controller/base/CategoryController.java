package com.nano.web.controller.base;

import java.util.ArrayList;
import java.util.List;

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

import com.nano.constant.Constant;
import com.nano.domain.base.Category;
import com.nano.service.base.CategoryService;
import com.nano.util.UUID;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.CategoryVO;

@Controller
@RequestMapping("base/category")
public class CategoryController {
	private static final Logger logger = LoggerFactory
			.getLogger(CategoryController.class);
	@Autowired
	private CategoryService categoryService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<CategoryVO> save(CategoryVO vo) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行保存类目信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<CategoryVO> responseVO = new ResponseVO<>();
		Category category;
		try {
			if (!StringUtils.isEmpty(vo.getId())) {
				category = categoryService.findOne(vo.getId());
				category.setModifier(session.getAttribute("userId").toString());
			} else {
				category = new Category();
				category.setCreator(session.getAttribute("userId").toString());
			}
			BeanUtils.copyProperties(vo, category, "id", "parentId");
			if (!StringUtils.isEmpty(vo.getParentId())) {
				Category parent = categoryService.findOne(vo.getParentId());
				category.setParentId(parent.getId());
				category.setCode(parent.getCode() + "-"
						+ UUID.generateShortUuid());
			} else {
				category.setCode(UUID.generateShortUuid());
			}

			if (!StringUtils.isEmpty(vo.getId())) {
				categoryService.update(category);
			} else {
				categoryService.save(category);
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
	public @ResponseBody ResponseVO<CategoryVO> delete(
			@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行删除类目信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<CategoryVO> responseVO = new ResponseVO<>();
		try {
			categoryService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(true);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/queryCategories", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody List<CategoryVO> queryCategories() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行查询类目信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		List<Category> categories = categoryService.findRoots();
		List<CategoryVO> vos = initCategoryTreeGrid(categories);
		return vos;
	}

	@RequestMapping(value = "/queryCategoriesForSelect", method = {
			RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List<CategoryVO> queryCategoriesForSelect() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行查询供选择类目信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		List<Category> categories = categoryService.findRoots();
		List<CategoryVO> vos = initCategoryTreeGrid(categories);
		return vos;
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String init() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问类目信息页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
		return "base/category/init";
	}

	private List<CategoryVO> initCategoryTreeGrid(List<Category> categories) {
		List<CategoryVO> vos = new ArrayList<>();
		for (Category category : categories) {
			CategoryVO vo = new CategoryVO();
			vo.setId(category.getId());
			vo.setName(category.getName());
			vo.setCode(category.getCode());
			vo.setText(category.getName());
			vo.setState(Constant.OPEN);
			vo.setParentId(category.getId());
			Category queryParams = new Category();
			queryParams.setParentId(category.getId());
			vo.getChildren()
					.addAll(initCategoryTreeGrid(categoryService
							.find(queryParams)));
			vos.add(vo);
		}
		return vos;
	}
}
