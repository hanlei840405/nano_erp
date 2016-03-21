package com.nano.web.controller.base;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Sms;
import com.nano.domain.base.SmsTemplate;
import com.nano.service.base.SmsService;
import com.nano.service.base.SmsTemplateService;
import com.nano.web.vo.PageVO;
import com.nano.web.vo.base.SmsVO;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.session.Session;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("base/sms")
public class SmsController {
	private static final Logger logger = LoggerFactory
			.getLogger(SmsController.class);
	@Autowired
	private SmsService smsService;

	@Autowired
	private SmsTemplateService smsTemplateService;

	@RequestMapping(value = "/querySmses", method = { RequestMethod.POST,
			RequestMethod.GET })
	public @ResponseBody PageVO<SmsVO> querySmses(SmsVO vo, @RequestParam(required = true) int page, @RequestParam(required = true) int rows) {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】执行分页查询短信信息操作.", session.getAttribute("username"), subject.getPrincipal().toString()));
		Sms queryParmas = new Sms();
		queryParmas.setTelephone(vo.getTelephone());
		queryParmas.setTemplateId(vo.getTemplateId());
		PageInfo<Sms> pageInfo = smsService.find(queryParmas, page - 1, rows);
		PageVO<SmsVO> pageVO = new PageVO<>();
		pageVO.setTotal(pageInfo.getTotal());
		for (Sms sms : pageInfo.getList()) {
			SmsVO smsVO = new SmsVO();
			smsVO.setId(sms.getId());
			smsVO.setTelephone(sms.getTelephone());
			smsVO.setContent(sms.getContent());
			SmsTemplate smsTemplate = smsTemplateService.findOne(sms
					.getTemplateId());
			smsVO.setTemplateId(smsTemplate.getId());
			smsVO.setTemplateName(smsTemplate.getName());
			pageVO.getRows().add(smsVO);
		}
		return pageVO;
	}

	@RequestMapping(value = "/init", method = { RequestMethod.POST,
			RequestMethod.GET })
	public String init() {
		Subject subject = SecurityUtils.getSubject();
		Session session = subject.getSession();
		logger.info(String.format("%s【%s】访问短信页面.", session.getAttribute("username"), subject.getPrincipal().toString()));
		return "base/sms/init";
	}
}
