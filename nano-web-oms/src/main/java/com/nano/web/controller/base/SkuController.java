package com.nano.web.controller.base;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.io.FileUtils;
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
import org.springframework.web.multipart.MultipartFile;

import com.nano.domain.base.Dictionary;
import com.nano.domain.base.DictionaryItem;
import com.nano.domain.base.Sku;
import com.nano.service.base.DictionaryItemService;
import com.nano.service.base.DictionaryService;
import com.nano.service.base.SkuService;
import com.nano.util.OSUtil;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.SkuVO;

@Controller
@RequestMapping("base/sku")
public class SkuController {
	private static final Logger logger = LoggerFactory.getLogger(SkuController.class);
	@Autowired
	private SkuService skuService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private DictionaryItemService dictionaryItemService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<SkuVO> save(SkuVO vo, @RequestParam(value = "file", required = false) MultipartFile file) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行保存SKU操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<SkuVO> responseVO = new ResponseVO<>();
		Sku sku;
		try {
			if (!StringUtils.isEmpty(vo.getId())) {
				sku = skuService.findOne(vo.getId());
				sku.setModifier(session.getAttribute("userId").toString());
			} else {
				sku = new Sku();
				sku.setCreator(session.getAttribute("userId").toString());
			}
			BeanUtils.copyProperties(vo, sku, "id", "image");
			if (!file.isEmpty()) {
				Dictionary dictionary = dictionaryService.findOne("UPLOAD_FILE_PATH");
				DictionaryItem queryParam = new DictionaryItem();
				queryParam.setDictionaryId(dictionary.getId());
				if (OSUtil.isWindows()) {
					queryParam.setCode("windows");
				} else if (OSUtil.isLinux()) {
					queryParam.setCode("linux");
				} else if (OSUtil.isMacOS()) {
					queryParam.setCode("mac");
				} else {
					queryParam.setCode("windows");
				}
				List<DictionaryItem> dictionaryItems = dictionaryItemService.find(queryParam);
				String path = dictionaryItems.get(0).getName();
				File folder = new File(path);
				if (!folder.exists()) {
					folder.mkdirs();
				}
				sku.setImage(path + file.getOriginalFilename());
				File destFile = new File(path + file.getOriginalFilename());
				FileUtils.copyInputStreamToFile(file.getInputStream(), destFile);
			}

			if (!StringUtils.isEmpty(vo.getId())) {
				skuService.update(sku);
			} else {
				skuService.save(sku);
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
	public @ResponseBody ResponseVO<SkuVO> delete(@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行删除SKU操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<SkuVO> responseVO = new ResponseVO<>();
		try {
			skuService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(true);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/querySkus", method = { RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List<SkuVO> queryProductes(SkuVO vo) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行查询SKU操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		Sku queryParam = new Sku();
		BeanUtils.copyProperties(vo, queryParam);
		List<Sku> skus = skuService.find(queryParam);
		List<SkuVO> vos = new ArrayList<>();
		for (Sku sku : skus) {
			SkuVO skuVO = new SkuVO();
			skuVO.setId(sku.getId());
			BeanUtils.copyProperties(sku, skuVO);
			vos.add(skuVO);
		}
		return vos;
	}
}
