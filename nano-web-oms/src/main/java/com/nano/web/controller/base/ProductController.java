package com.nano.web.controller.base;

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

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Brand;
import com.nano.domain.base.Category;
import com.nano.domain.base.Contact;
import com.nano.domain.base.Product;
import com.nano.domain.base.Shop;
import com.nano.service.base.BrandService;
import com.nano.service.base.CategoryService;
import com.nano.service.base.ContactService;
import com.nano.service.base.ProductService;
import com.nano.service.base.ShopService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.ProductVO;

@Controller
@RequestMapping("base/product")
public class ProductController {
    private static final Logger logger = LoggerFactory
            .getLogger(ProductController.class);
    @Autowired
    private ProductService productService;
    @Autowired
    private ContactService contactService;
    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private ShopService shopService;

    @RequestMapping(value = "/save", method = RequestMethod.POST)
    public
    @ResponseBody
    ResponseVO<ProductVO> save(ProductVO vo) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行保存商品信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<ProductVO> responseVO = new ResponseVO<>();
        Product product;
        try {
            if (!StringUtils.isEmpty(vo.getId())) {
            	product = productService.findOne(vo.getId());
                product.setModifier(session.getAttribute("userId").toString());
            } else {
            	product = new Product();
                product.setCreator(session.getAttribute("userId").toString());
            }
            BeanUtils.copyProperties(vo, product, "id","creator","modifier");
            if (!StringUtils.isEmpty(vo.getId())) {
            	productService.update(product);
            } else {
            	productService.save(product);
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
    ResponseVO<ProductVO> delete(
            @RequestParam("ids[]") List<String> ids) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行删除商品信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        ResponseVO<ProductVO> responseVO = new ResponseVO<>();
        try {
        	productService.delete(ids);
            responseVO.setYesOrNo(true);
            return responseVO;
        } catch (Exception e) {
            logger.error(Marker.ANY_MARKER, e.getMessage());
            responseVO.setYesOrNo(true);
            responseVO.setErrorMsg(e.getMessage());
            return responseVO;
        }
    }

    @RequestMapping(value = "/queryProducts", method = {RequestMethod.POST,
            RequestMethod.GET})
    public
    @ResponseBody
    PageVO<ProductVO> queryProductes(ProductVO vo,@RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】执行分页查询商品信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
        Product queryParam = new Product();
        BeanUtils.copyProperties(vo, queryParam);
    	PageInfo<Product> pageInfo = productService.find(queryParam, page - 1, rows);
        PageVO<ProductVO> pageVO = new PageVO<>();
        pageVO.setTotal(pageInfo.getTotal());
        for (Product product : pageInfo.getList()) {
            ProductVO productVO = new ProductVO();
            productVO.setId(product.getId());
            BeanUtils.copyProperties(product, productVO);
            Brand brand = brandService.findOne(product.getBrandId());
            productVO.setBrandName(brand.getName());
            Category category = categoryService.findOne(product.getCategoryId());
            productVO.setCategoryName(category.getName());
            Contact contact = contactService.findOne(product.getContactId());
            productVO.setContactName(contact.getName());
            Shop shop = shopService.findOne(product.getShopId());
            productVO.setShopName(shop.getName());
            pageVO.getRows().add(productVO);
        }
        return pageVO;
    }

    @RequestMapping(value = "/init", method = {RequestMethod.POST,
            RequestMethod.GET})
    public String init() {
        Subject subject = SecurityUtils.getSubject();
        Session session = subject.getSession();
        logger.info(String.format("%s【%s】访问商品信息页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
        return "base/product/init";
    }
}
