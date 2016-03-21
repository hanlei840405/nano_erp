package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.SmsTemplate;

public interface SmsTemplateService {

	SmsTemplate save(SmsTemplate smsTemplate);

	List<SmsTemplate> save(List<SmsTemplate> SmsTemplates);

	void delete(String id);

	void delete(List<String> ids);

	SmsTemplate findOne(String id);

	void update(SmsTemplate smsTemplate);

	void update(List<SmsTemplate> SmsTemplates);

	PageInfo<SmsTemplate> find(SmsTemplate smsTemplate, int pageNo, int pageSize);

	List<SmsTemplate> find(SmsTemplate smsTemplate);
}
