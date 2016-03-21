package com.nano.web.controller.base;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Express;
import com.nano.domain.base.PlatformExpress;
import com.nano.domain.base.RegionExpress;
import com.nano.service.base.ExpressService;
import com.nano.service.base.PlatformExpressService;
import com.nano.service.base.RegionExpressService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.ExpressVO;
import com.nano.web.vo.base.PlatformExpressVO;
import com.nano.web.vo.base.RegionExpressVO;
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
@RequestMapping("base/express")
public class ExpressController {
	private static final Logger logger = LoggerFactory
			.getLogger(ExpressController.class);
	@Autowired
	private ExpressService expressService;
	@Autowired
	private PlatformExpressService platformExpressService;
	@Autowired
	private RegionExpressService regionExpressService;

    private ObjectMapper mapper = new ObjectMapper();

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<ExpressVO> save(ExpressVO vo,HttpServletRequest request) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行保存快递信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<ExpressVO> responseVO = new ResponseVO<>();
		Express express;
		try {
			if (!StringUtils.isEmpty(vo.getId())) {
				express = expressService.findOne(vo.getId());
				express.setModifier(session.getAttribute("userId").toString());
			} else {
				express = new Express();
				express.setCreator(session.getAttribute("userId").toString());
			}
			BeanUtils.copyProperties(vo, express, "id","creator","modifier");
			List<String> regionIds = mapper.readValue(
                    request.getParameter("regionIds"),
                    new TypeReference<List<String>>() {
                    });

			express.setRegionIds(regionIds);
			if (!StringUtils.isEmpty(vo.getId())) {
				expressService.update(express);
			} else {
				express.setStatus("1");
				expressService.save(express);
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

	@RequestMapping(value = "/start-stop", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<ExpressVO> startStop(ExpressVO vo) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行启用、停用快递信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<ExpressVO> responseVO = new ResponseVO<>();
		Express express = expressService.findOne(vo.getId());
		try {
			express.setStatus("1".equals(express.getStatus()) ? "0" : "1");
			expressService.startStop(express);
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
	public @ResponseBody ResponseVO<ExpressVO> delete(
			@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行删除快递信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<ExpressVO> responseVO = new ResponseVO<>();
		try {
			expressService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(true);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/queryExpresses", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody PageVO<ExpressVO> queryExpresses(ExpressVO vo,
			@RequestParam(required = true) int page,
			@RequestParam(required = true) int rows) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行分页查询快递信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		Express queryParmas = new Express();
		queryParmas.setName(StringUtils.isEmpty(vo.getName()) ? null : vo
				.getName());
		queryParmas.setCode(StringUtils.isEmpty(vo.getCode()) ? null : vo
				.getCode());
		queryParmas.setStatus(StringUtils.isEmpty(vo.getStatus()) ? null : vo
				.getStatus());
		PageInfo<Express> pageInfo = expressService.find(queryParmas,
				page - 1, rows);
		PageVO<ExpressVO> pageVO = new PageVO<>();
		pageVO.setTotal(pageInfo.getTotal());
		for (Express express : pageInfo.getList()) {
			ExpressVO expressVO = dataTransfer(express);
			RegionExpress queryParam = new RegionExpress();
			queryParam.setExpressId(express.getId());
			List<RegionExpress> regionExpresses = regionExpressService.find(queryParam);
			for(RegionExpress regionExpress : regionExpresses){
				expressVO.getRegionIds().add(regionExpress.getRegionId());
			}
			pageVO.getRows().add(expressVO);
		}
		return pageVO;
	}

	@RequestMapping(value = "/queryAllExpresses", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody List<?> queryAllExpresses(String platformId,String regionId) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行查询全部快递信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		if(!StringUtils.isEmpty(platformId)){
			List<PlatformExpressVO> vos = new ArrayList<>();
			PlatformExpress platformExpress = new PlatformExpress();
			platformExpress.setPlatformId(platformId);
			List<PlatformExpress> platformExpresses = platformExpressService.find(platformExpress);
			for(PlatformExpress pe : platformExpresses){
				Express express = expressService.findOne(pe.getExpressId());
				PlatformExpressVO platformExpressVO = new PlatformExpressVO();
				platformExpressVO.setAlias(pe.getAlias());
				platformExpressVO.setPlatformId(pe.getPlatformId());
				platformExpressVO.setCode(pe.getCode());
				platformExpressVO.setExpressId(pe.getExpressId());
				platformExpressVO.setStandardCode(express.getCode());
				platformExpressVO.setName(express.getName());
				vos.add(platformExpressVO);
			}
			return vos;
		}else if(!StringUtils.isEmpty(regionId)){
			List<RegionExpressVO> vos = new ArrayList<>();
			RegionExpress regionExpress =  new RegionExpress();
			regionExpress.setRegionId(regionId);
			List<RegionExpress> regionExpresses = regionExpressService.find(regionExpress);
			for(RegionExpress re : regionExpresses){
				Express express = expressService.findOne(re.getExpressId());
				RegionExpressVO expressVO = new RegionExpressVO();
				expressVO.setExpressId(express.getId());
				expressVO.setExpressCode(express.getCode());
				expressVO.setExpressName(express.getName());
				expressVO.setRegionId(re.getRegionId());
				expressVO.setPriority(re.getPriority());
				vos.add(expressVO);
			}
			return vos;
		}else{
			List<ExpressVO> vos = new ArrayList<>();
			Express queryParmas = new Express();
			List<Express> expresses = expressService.find(queryParmas);
			for (Express express : expresses) {
				ExpressVO expressVO = dataTransfer(express);
				vos.add(expressVO);
			}
			return vos;
		}
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String init() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问快递信息页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
		return "base/express/init";
	}
	
	private ExpressVO dataTransfer(Express express){
		ExpressVO expressVO = new ExpressVO();
		expressVO.setId(express.getId());
		expressVO.setName(express.getName());
		expressVO.setCode(express.getCode());
		expressVO.setStatus(express.getStatus());
		expressVO.setTelephone(express.getTelephone());
		expressVO.setContact(express.getContact());
		expressVO.setText(express.getName());
		return expressVO;
	}
}
