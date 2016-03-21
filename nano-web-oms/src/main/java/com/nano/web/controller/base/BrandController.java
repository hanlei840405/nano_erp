package com.nano.web.controller.base;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Brand;
import com.nano.domain.base.BrandCategory;
import com.nano.domain.base.Category;
import com.nano.service.base.BrandCategoryService;
import com.nano.service.base.BrandService;
import com.nano.service.base.CategoryService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.BrandVO;
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

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("base/brand")
public class BrandController {
    private static final Logger logger = LoggerFactory
            .getLogger(BrandController.class);
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private BrandCategoryService brandCategoryService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVO<BrandVO> save(BrandVO vo) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行保存品牌信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<BrandVO> responseVO = new ResponseVO<>();
        Brand brand;
        try {
            if (!StringUtils.isEmpty(vo.getId())) {
                brand = brandService.findOne(vo.getId());
                brand.setModifier(session.getAttribute("userId").toString());
            } else {
                brand = new Brand();
                brand.setCreator(session.getAttribute("userId").toString());
            }
            if(!vo.getCategoryIds().isEmpty()){
                for(String id : vo.getCategoryIds()){
                    Category category = categoryService.findOne(id);
                    brand.getCategories().add(category);
                }
            }
            BeanUtils.copyProperties(vo, brand, "id");
            if (!StringUtils.isEmpty(vo.getId())) {
                brandService.update(brand);
            } else {
                brandService.save(brand);
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
    public
    @ResponseBody
    ResponseVO<BrandVO> delete(
            @RequestParam("ids[]") List<String> ids) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行删除品牌信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<BrandVO> responseVO = new ResponseVO<>();
        try {
            brandService.delete(ids);
            responseVO.setYesOrNo(true);
            return responseVO;
        } catch (Exception e) {
            logger.error(Marker.ANY_MARKER, e.getMessage());
            responseVO.setYesOrNo(true);
            responseVO.setErrorMsg(e.getMessage());
            return responseVO;
        }
    }

    @RequestMapping(value = "/queryBrands", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    PageVO<BrandVO> queryBrands(BrandVO vo, @RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行查询品牌信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        Brand queryParmas = new Brand();
        queryParmas.setName(StringUtils.isEmpty(vo.getName()) ? null : vo.getName());
        queryParmas.setCode(StringUtils.isEmpty(vo.getCode()) ? null : vo.getCode());
        queryParmas.setStatus(StringUtils.isEmpty(vo.getStatus()) ? null : vo.getStatus());
        PageInfo<Brand> pageInfo = brandService.find(queryParmas, page - 1, rows);
        PageVO<BrandVO> pageVO = new PageVO<>();
        pageVO.setTotal(pageInfo.getTotal());
        for (Brand brand : pageInfo.getList()) {
            BrandVO brandVO = new BrandVO();
            brandVO.setId(brand.getId());
            brandVO.setName(brand.getName());
            brandVO.setCode(brand.getCode());
            brandVO.setStatus(brand.getStatus());
            brandVO.setCreated(brand.getCreated());
            BrandCategory brandCategory = new BrandCategory();
            brandCategory.setBrandId(brand.getId());
            List<BrandCategory> brandCategories = brandCategoryService.find(brandCategory);
            for(BrandCategory bc : brandCategories){
                Category category = categoryService.findOne(bc.getCategoryId());
                brandVO.getCategoryIds().add(category.getId());
                brandVO.getCategoryNames().add(category.getName());
            }
            pageVO.getRows().add(brandVO);
        }
        return pageVO;
    }

    @RequestMapping(value = "/queryAllBrands", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    List<BrandVO> queryAllBrands() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行查询全部供选择品牌信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        Brand queryParmas = new Brand();
        List<Brand> brands = brandService.find(queryParmas);
        List<BrandVO> vos = new ArrayList<>();
        for (Brand brand : brands) {
            BrandVO brandVO = new BrandVO();
            brandVO.setId(brand.getId());
            brandVO.setName(brand.getName());
            brandVO.setCode(brand.getCode());
            brandVO.setStatus(brand.getStatus());
            brandVO.setCreated(brand.getCreated());
            BrandCategory brandCategory = new BrandCategory();
            brandCategory.setBrandId(brand.getId());
            List<BrandCategory> brandCategories = brandCategoryService.find(brandCategory);
            for(BrandCategory bc : brandCategories){
                Category category = categoryService.findOne(bc.getCategoryId());
                brandVO.getCategoryIds().add(category.getId());
                brandVO.getCategoryNames().add(category.getName());
            }
            vos.add(brandVO);
        }
        return vos;
    }

    @RequestMapping(value = "/init", method = {RequestMethod.POST,
            RequestMethod.GET})
    public String init() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】访问品牌信息页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
        return "base/brand/init";
    }
}
