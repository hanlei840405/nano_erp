package com.nano.web.controller.base;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.SmsTemplate;
import com.nano.service.base.SmsTemplateService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.ResponseVO;
import com.nano.web.vo.base.SmsTemplateVO;
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
@RequestMapping("base/smsTemplate")
public class SmsTemplateController {
	private static final Logger logger = LoggerFactory
			.getLogger(SmsTemplateController.class);
	@Autowired
	private SmsTemplateService smsTemplateService;

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public @ResponseBody ResponseVO<SmsTemplateVO> save(SmsTemplateVO vo) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行保存短信模板操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<SmsTemplateVO> responseVO = new ResponseVO<>();
		SmsTemplate smsTemplate;
		try {
			if (!StringUtils.isEmpty(vo.getId())) {
				smsTemplate = smsTemplateService.findOne(vo.getId());
				smsTemplate.setModifier(session.getAttribute("userId").toString());
			} else {
				smsTemplate = new SmsTemplate();
				smsTemplate.setCreator(session.getAttribute("userId").toString());
			}
			BeanUtils.copyProperties(vo, smsTemplate, "id","creator","modifier");
			if (!StringUtils.isEmpty(vo.getId())) {
				smsTemplateService.update(smsTemplate);
			} else {
				smsTemplateService.save(smsTemplate);
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
	public @ResponseBody ResponseVO<SmsTemplateVO> delete(
			@RequestParam("ids[]") List<String> ids) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行删除短信模板操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		ResponseVO<SmsTemplateVO> responseVO = new ResponseVO<>();
		try {
			smsTemplateService.delete(ids);
			responseVO.setYesOrNo(true);
			return responseVO;
		} catch (Exception e) {
			logger.error(Marker.ANY_MARKER, e.getMessage());
			responseVO.setYesOrNo(true);
			responseVO.setErrorMsg(e.getMessage());
			return responseVO;
		}
	}

	@RequestMapping(value = "/querySmsTemplates", method = {
			RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody PageVO<SmsTemplateVO> querySmsTemplates(
			SmsTemplateVO vo, @RequestParam(required = true) int page,
			@RequestParam(required = true) int rows) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行分页查询短信模板操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		SmsTemplate queryParmas = new SmsTemplate();
		queryParmas.setName(StringUtils.isEmpty(vo.getName()) ? null : vo
				.getName());
		queryParmas.setCode(StringUtils.isEmpty(vo.getCode()) ? null : vo
				.getCode());
		PageInfo<SmsTemplate> pageInfo = smsTemplateService.find(queryParmas, page - 1, rows);
		PageVO<SmsTemplateVO> pageVO = new PageVO<>();
		pageVO.setTotal(pageInfo.getTotal());
		for (SmsTemplate smsTemplate : pageInfo.getList()) {
			SmsTemplateVO smsTemplateVO = new SmsTemplateVO();
			smsTemplateVO.setId(smsTemplate.getId());
			smsTemplateVO.setName(smsTemplate.getName());
			smsTemplateVO.setCode(smsTemplate.getCode());
			smsTemplateVO.setTemplate(smsTemplate.getTemplate());
			pageVO.getRows().add(smsTemplateVO);
		}
		return pageVO;
	}

	@RequestMapping(value = "/queryAllSmsTemplates", method = {
			RequestMethod.POST, RequestMethod.GET })
	public @ResponseBody List<SmsTemplateVO> queryAllSmsTemplates() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行查询全部短信模板操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		SmsTemplate queryParmas = new SmsTemplate();
		List<SmsTemplate> smsTemplates = smsTemplateService.find(queryParmas);
		List<SmsTemplateVO> vos = new ArrayList<>();
		for (SmsTemplate smsTemplate : smsTemplates) {
			SmsTemplateVO smsTemplateVO = new SmsTemplateVO();
			smsTemplateVO.setId(smsTemplate.getId());
			smsTemplateVO.setName(smsTemplate.getName());
			smsTemplateVO.setCode(smsTemplate.getCode());
			smsTemplateVO.setTemplate(smsTemplate.getTemplate());
			vos.add(smsTemplateVO);
		}
		return vos;
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String init() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问短信模板页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
		return "base/smsTemplate/init";
	}
}
