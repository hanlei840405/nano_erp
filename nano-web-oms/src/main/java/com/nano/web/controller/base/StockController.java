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
import com.nano.domain.base.Product;
import com.nano.domain.base.Sku;
import com.nano.domain.base.Stock;
import com.nano.domain.base.Storage;
import com.nano.domain.base.Warehouse;
import com.nano.service.base.ProductService;
import com.nano.service.base.SkuService;
import com.nano.service.base.StockService;
import com.nano.service.base.StorageService;
import com.nano.service.base.WarehouseService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.StockVO;
import com.nano.web.vo.base.StorageVO;
import com.nano.web.vo.base.WarehouseVO;

@Controller
@RequestMapping("base/stock")
public class StockController {
	private static final Logger logger = LoggerFactory
			.getLogger(StockController.class);
	@Autowired
	private StockService stockService;
	@Autowired
	private StorageService storageService;
	@Autowired
	private WarehouseService warehouseService;
	@Autowired
	private SkuService skuService;
	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/queryStocks", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody PageVO<StockVO> queryStocks(StockVO vo,
			@RequestParam(required = true) int page,
			@RequestParam(required = true) int rows) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行查询库存分页信息操作.", session
				.getAttribute("username"), subject.getPrincipal().toString()));

		Map<String, String> queryParmas = new HashMap<>();
		if (!StringUtils.isEmpty(vo.getProductCode())) {
			queryParmas.put("productCode", vo.getProductCode());

		}
		if (!StringUtils.isEmpty(vo.getBarcode())) {
			queryParmas.put("barcode", vo.getBarcode());

		}
		if (!StringUtils.isEmpty(vo.getWarehouseId())) {
			queryParmas.put("warehouseId", vo.getWarehouseId());

		}
		PageInfo<Stock> pageInfo = stockService.find(queryParmas, page - 1,
				rows);
		PageVO<StockVO> pageVO = new PageVO<>();
		pageVO.setTotal(pageInfo.getTotal());
		for (Stock stock : pageInfo.getList()) {
			StockVO stockVO = new StockVO();
			stockVO.setId(stock.getId());
			stockVO.setQuantity(stock.getQuantity());
			stockVO.setSafeQuantity(stock.getSafeQuantity());
			Sku sku = skuService.findOne(stock.getSkuId());
			stockVO.setBarcode(sku.getBarcode());
			Product product = productService.findOne(stock.getProductId());
			stockVO.setProductCode(product.getCode());
			Warehouse warehouse = warehouseService.findOne(stock.getWarehouseId());
			stockVO.setWarehouseName(warehouse.getName());
			pageVO.getRows().add(stockVO);
		}
		return pageVO;
	}

	@RequestMapping(value = "/stock/init", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String searchInit() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问库存查询页面.", session
				.getAttribute("username"), subject.getPrincipal().toString()));
		return "base/stock/stock";
	}

	@RequestMapping(value = "/storage/init", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String storageInit() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问入库管理页面.", session
				.getAttribute("username"), subject.getPrincipal().toString()));
		return "base/stock/storage";
	}

	@RequestMapping(value = "/storage/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<StorageVO> save(StorageVO vo) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行保存入库信息操作.", session
				.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<StorageVO> responseVO = new ResponseVO<>();
		Storage storage;
		try {
			if (!StringUtils.isEmpty(vo.getId())) {
				storage = storageService.findOne(vo.getId());
				if ("1".equals(storage.getStatus())) {
					responseVO.setYesOrNo(false);
					responseVO.setErrorMsg("已入库，不可修改!");
					return responseVO;
				}
				storage.setModifier(session.getAttribute("userId").toString());
			} else {
				storage = new Storage();
				storage.setCreator(session.getAttribute("userId").toString());
			}
			BeanUtils.copyProperties(vo, storage, "id", "creator", "modifier","status");
			Sku skuQuery = new Sku();
			skuQuery.setBarcode(vo.getBarcode());
			PageInfo<Sku> skus = skuService.find(skuQuery,0,1);
			if(skus.getList().isEmpty()){
				responseVO.setYesOrNo(false);
				responseVO.setErrorMsg("未找到SKU");
				return responseVO;
			}
			Sku sku = skus.getList().get(0);
			storage.setSkuId(sku.getId());
			storage.setProductId(sku.getProductId());
			if (!StringUtils.isEmpty(vo.getId())) {
				storageService.update(storage);
			} else {
				storageService.save(storage);
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

	@RequestMapping(value = "/storage/storage", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<StorageVO> storage(@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行允许入库操作.", session
				.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<StorageVO> responseVO = new ResponseVO<>();
		try {
			List<Storage> storages = new ArrayList<>();
			for(String id : ids){
				Storage storage = storageService.findOne(id);
				storage.setStatus("1");
				storages.add(storage);
			}
			storageService.storage(storages);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(false);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/queryStorages", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody PageVO<StorageVO> queryStorages(StorageVO vo,
			@RequestParam(required = true) int page,
			@RequestParam(required = true) int rows) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行分页查询入库信息操作.", session
				.getAttribute("username"), subject.getPrincipal().toString()));

		Map<String, String> queryParmas = new HashMap<>();
		if (!StringUtils.isEmpty(vo.getProductCode())) {
			queryParmas.put("productCode", vo.getProductCode());

		}
		if (!StringUtils.isEmpty(vo.getBarcode())) {
			queryParmas.put("barcode", vo.getBarcode());

		}
		if (!StringUtils.isEmpty(vo.getWarehouseId())) {
			queryParmas.put("warehouseId", vo.getWarehouseId());

		}
		PageInfo<Storage> pageInfo = storageService.find(queryParmas, page - 1,
				rows);
		PageVO<StorageVO> pageVO = new PageVO<>();
		pageVO.setTotal(pageInfo.getTotal());
		for (Storage storage : pageInfo.getList()) {
			StorageVO storageVO = new StorageVO();
			storageVO.setId(storage.getId());
			storageVO.setNo(storage.getNo());
			storageVO.setStatus(storage.getStatus());
			storageVO.setQuantity(storage.getQuantity());
			Sku sku = skuService.findOne(storage.getSkuId());
			storageVO.setBarcode(sku.getBarcode());
			Product product = productService.findOne(storage.getProductId());
			storageVO.setProductCode(product.getCode());
			Warehouse warehouse = warehouseService.findOne(storage.getWarehouseId());
			storageVO.setWarehouseId(storage.getWarehouseId());
			storageVO.setWarehouseName(warehouse.getName());
			pageVO.getRows().add(storageVO);
		}
		return pageVO;
	}

	@RequestMapping(value = "/storage/delete", method = RequestMethod.GET)
	public @ResponseBody ResponseVO<WarehouseVO> delete(@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行删除入库信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<WarehouseVO> responseVO = new ResponseVO<>();
		try {
			storageService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(true);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}
}
