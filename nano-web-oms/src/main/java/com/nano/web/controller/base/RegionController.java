package com.nano.web.controller.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.IOUtils;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nano.constant.Constant;
import com.nano.domain.base.Dictionary;
import com.nano.domain.base.DictionaryItem;
import com.nano.domain.base.Express;
import com.nano.domain.base.Region;
import com.nano.domain.base.RegionExpress;
import com.nano.service.base.DictionaryItemService;
import com.nano.service.base.DictionaryService;
import com.nano.service.base.ExpressService;
import com.nano.service.base.RegionExpressService;
import com.nano.service.base.RegionService;
import com.nano.util.OSUtil;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.RegionExpressVO;
import com.nano.web.vo.base.RegionVO;
import com.nano.web.vo.base.SkuVO;

@Controller
@RequestMapping("base/region")
public class RegionController {
	private static final Logger logger = LoggerFactory
			.getLogger(RegionController.class);
	@Autowired
	private RegionService regionService;
	@Autowired
	private ExpressService expressService;
	@Autowired
	private RegionExpressService regionExpressService;
	@Autowired
	private DictionaryService dictionaryService;
	@Autowired
	private DictionaryItemService dictionaryItemService;

	private ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<RegionVO> save(RegionVO vo,
			HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行保存区域信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<RegionVO> responseVO = new ResponseVO<>();
		Region region;
		try {
			if (!StringUtils.isEmpty(vo.getId())) {
				region = regionService.findOne(vo.getId());
				region.setModifier(session.getAttribute("userId").toString());
			} else {
				region = new Region();
				region.setCreator(session.getAttribute("userId").toString());
			}
			region.setCode(vo.getCode());
			region.setName(vo.getName());
			if (!StringUtils.isEmpty(vo.getParentId())) {
				region.setParentId(vo.getParentId());
			}

			if (!StringUtils.isEmpty(vo.getId())) {
				regionService.update(region);
			} else {
				regionService.save(region);
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

	@RequestMapping(value = "/upload", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<RegionVO> upload(SkuVO vo,
			@RequestParam(value = "file", required = false) MultipartFile file)
			throws IOException {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行上传区域信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		if (file.isEmpty()) {
			throw new IOException("文件不存在");
		}
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
		List<DictionaryItem> dictionaryItems = dictionaryItemService
				.find(queryParam);
		String path = dictionaryItems.get(0).getName();
		File folder = new File(path);
		if (!folder.exists()) {
			folder.mkdirs();
		}
		FileItemFactory factory = new DiskFileItemFactory(
				Constant.SIZE_THRESHOLD, folder);
		ServletFileUpload fu = new ServletFileUpload(factory);
		fu.setSizeMax(500 * 1024 * 1024); // 设置允许用户上传文件大小,单位:位
		InputStream in = (InputStream) file.getInputStream();

		regionService.save(IOUtils.toByteArray(in));
		ResponseVO<RegionVO> responseVO = new ResponseVO<>();
		try {
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
	public @ResponseBody ResponseVO<RegionVO> delete(
			@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行删除区域信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<RegionVO> responseVO = new ResponseVO<>();
		try {
			regionService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(false);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/queryRegions", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody List<RegionVO> queryRegions(RegionVO vo) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行查询区域信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		List<Region> regions = new ArrayList<Region>();
		if (StringUtils.isEmpty(vo.getId())) {
			regions = regionService.findRoots();
		} else {
			Region region = new Region();
			region.setParentId(vo.getId());
			regions = regionService.find(region);
		}
		List<RegionVO> vos = new ArrayList<>();
		for (Region region : regions) {
			RegionVO regionVO = new RegionVO();
			regionVO.setId(region.getId());
			regionVO.setName(region.getName());
			regionVO.setCode(region.getCode());
			regionVO.setText(region.getName());
			regionVO.setState(Constant.CLOSED);
			regionVO.setParentId(region.getParentId());
			RegionExpress queryParam = new RegionExpress();
			queryParam.setRegionId(region.getId());
			List<RegionExpress> regionExpresses = regionExpressService.find(queryParam);
			for(RegionExpress regionExpress : regionExpresses){
				regionVO.getExpressIds().add(regionExpress.getExpressId());
				Express express = expressService.findOne(regionExpress.getExpressId());
				regionVO.getExpressNames().add(String.format("%s[%s,%s]", express.getName(),express.getCode(),regionExpress.getPriority()));
			}
			vos.add(regionVO);
		}
		return vos;
	}



	@RequestMapping(value = "/configRegionExpressPriority", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<RegionVO> configRegionExpressPriority(RegionVO vo,
												   HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行设置区域快递优先级操作.", session
				.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<RegionVO> responseVO = new ResponseVO<>();
		try {
			List<RegionExpressVO> regionExpressVOs = mapper.readValue(
					request.getParameter("regionExpresses"),
					new TypeReference<List<RegionExpressVO>>() {
					});
			List<RegionExpress> regionExpresses = new ArrayList<>();
			for(RegionExpressVO regionExpressVO : regionExpressVOs){
				RegionExpress regionExpress = new RegionExpress();
				regionExpress.setRegionId(vo.getId());
				regionExpress.setExpressId(regionExpressVO.getExpressId());
				regionExpress.setPriority(regionExpressVO.getPriority());
				regionExpresses.add(regionExpress);
			}
			regionExpressService.save(regionExpresses);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(false);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}
	@RequestMapping(value = "/init", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String init() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问区域信息页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
		return "base/region/init";
	}
}
