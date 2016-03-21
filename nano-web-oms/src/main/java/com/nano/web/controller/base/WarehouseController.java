package com.nano.web.controller.base;

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
import com.nano.domain.base.Product;
import com.nano.domain.base.Sku;
import com.nano.domain.base.Warehouse;
import com.nano.domain.base.WarehouseBrand;
import com.nano.service.base.BrandService;
import com.nano.service.base.ProductService;
import com.nano.service.base.SkuService;
import com.nano.service.base.WarehouseBrandService;
import com.nano.service.base.WarehouseService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.SkuVO;
import com.nano.web.vo.base.WarehouseVO;

@Controller
@RequestMapping("base/warehouse")
public class WarehouseController {
	private static final Logger logger = LoggerFactory.getLogger(WarehouseController.class);
	@Autowired
	private WarehouseService warehouseService;
	@Autowired
	private BrandService brandService;
	@Autowired
	private WarehouseBrandService warehouseBrandService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<WarehouseVO> save(WarehouseVO vo) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行保存仓库信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<WarehouseVO> responseVO = new ResponseVO<>();
		Warehouse warehouse;
		try {
			if (!StringUtils.isEmpty(vo.getId())) {
				warehouse = warehouseService.findOne(vo.getId());
				warehouse.setModifier(session.getAttribute("userId").toString());
			} else {
				warehouse = new Warehouse();
				warehouse.setCreator(session.getAttribute("userId").toString());
			}
			BeanUtils.copyProperties(vo, warehouse, "id", "creator", "modifier");
			if (!vo.getBrandIds().isEmpty()) {
				for (String id : vo.getBrandIds()) {
					WarehouseBrand warehouseBrand = new WarehouseBrand();
					warehouseBrand.setBrandId(id);
					warehouseBrand.setWarehouseId(warehouse.getId());
					warehouse.getWarehouseBrands().add(warehouseBrand);
				}
			}
			if (!StringUtils.isEmpty(vo.getId())) {
				warehouseService.update(warehouse);
			} else {
				warehouseService.save(warehouse);
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
	public @ResponseBody ResponseVO<WarehouseVO> delete(@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行删除仓库信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<WarehouseVO> responseVO = new ResponseVO<>();
		try {
			warehouseService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(true);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/queryWarehouses", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody PageVO<WarehouseVO> queryWarehouses(WarehouseVO vo, @RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行分页查询仓库信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		Map<String, String> queryParmas = new HashMap<>();
		if (!StringUtils.isEmpty(vo.getName())) {
			queryParmas.put("name", vo.getName());
		}
		if (!StringUtils.isEmpty(vo.getCode())) {
			queryParmas.put("code", vo.getCode());
		}
		if (!StringUtils.isEmpty(vo.getStatus())) {
			queryParmas.put("status", vo.getStatus());
		}
		PageInfo<Warehouse> pageInfo = warehouseService.find(queryParmas, page - 1, rows);
		PageVO<WarehouseVO> pageVO = new PageVO<>();
		pageVO.setTotal(pageInfo.getTotal());
		for (Warehouse warehouse : pageInfo.getList()) {
			WarehouseVO warehouseVO = new WarehouseVO();
			warehouseVO.setId(warehouse.getId());
			warehouseVO.setName(warehouse.getName());
			warehouseVO.setCode(warehouse.getCode());
			warehouseVO.setStatus(warehouse.getStatus());
			WarehouseBrand warehouseBrandQuery = new WarehouseBrand();
			warehouseBrandQuery.setWarehouseId(warehouse.getId());
			List<WarehouseBrand> warehouseBrands = warehouseBrandService.find(warehouseBrandQuery);
			for (WarehouseBrand warehouseBrand : warehouseBrands) {
				Brand brand = brandService.findOne(warehouseBrand.getBrandId());
				warehouseVO.getBrandIds().add(brand.getId());
				warehouseVO.getBrandNames().add(brand.getName());
			}
			pageVO.getRows().add(warehouseVO);
		}
		return pageVO;
	}

	@RequestMapping(value = "/queryAllWarehouses", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List<WarehouseVO> queryAllWarehouses() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行查看仓库全部信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		Map<String, String> queryParmas = new HashMap<>();
		List<Warehouse> warehouses = warehouseService.find(queryParmas);
		List<WarehouseVO> vos = new ArrayList<>();
		for (Warehouse warehouse : warehouses) {
			WarehouseVO warehouseVO = new WarehouseVO();
			warehouseVO.setId(warehouse.getId());
			warehouseVO.setName(warehouse.getName());
			warehouseVO.setCode(warehouse.getCode());
			warehouseVO.setStatus(warehouse.getStatus());
			vos.add(warehouseVO);
		}
		return vos;
	}

	@RequestMapping(value = "/queryWarehousesBySku", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List<WarehouseVO> queryWarehousesBySku(SkuVO skuVO) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行根据sku查询仓库操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		Sku skuQuery = new Sku();
		skuQuery.setBarcode(skuVO.getBarcode());
		PageInfo<Sku> skus = skuService.find(skuQuery, 0, 1);
		if (skus.getList().isEmpty()) {
			List<WarehouseVO> vos = new ArrayList<>();
			return vos;
		}
		Product product = productService.findOne(skus.getList().get(0).getProductId());
		Brand brand = brandService.findOne(product.getBrandId());
		return queryWarehousesByBrand(brand);
	}

	private List<WarehouseVO> queryWarehousesByBrand(Brand brand) {
		List<WarehouseVO> vos = new ArrayList<>();
		Map<String, String> queryParmas = new HashMap<>();
		queryParmas.put("brandId", brand.getId());
		List<Warehouse> warehouses = warehouseService.find(queryParmas);
		for (Warehouse warehouse : warehouses) {
			WarehouseVO warehouseVO = new WarehouseVO();
			warehouseVO.setId(warehouse.getId());
			warehouseVO.setName(warehouse.getName());
			warehouseVO.setCode(warehouse.getCode());
			warehouseVO.setStatus(warehouse.getStatus());
			vos.add(warehouseVO);
		}
		return vos;
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST, RequestMethod.GET })
	public String init() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问仓库页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
		return "base/warehouse/init";
	}
}
