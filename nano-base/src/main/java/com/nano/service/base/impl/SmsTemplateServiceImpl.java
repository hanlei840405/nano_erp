package com.nano.service.base.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.SmsTemplate;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.SmsTemplateMapper;
import com.nano.service.base.SmsTemplateService;

@Service
public class SmsTemplateServiceImpl implements SmsTemplateService {

	@Autowired
	private SmsTemplateMapper smsTemplateMapper;

	@Transactional	
	public SmsTemplate save(SmsTemplate smsTemplate) {
		smsTemplateMapper.saveOne(smsTemplate);
		return smsTemplate;
	}

	@Transactional
	public List<SmsTemplate> save(List<SmsTemplate> smsTemplates) {
		smsTemplateMapper.saveMany(smsTemplates);
		return smsTemplates;
	}

	@Transactional
	public void delete(String id) {
		SmsTemplate smsTemplate = findOne(id);
		long resultCnt = smsTemplateMapper.deleteOne(smsTemplate);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					smsTemplate.getId()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public SmsTemplate findOne(String id) {
		return smsTemplateMapper.findOne(id);
	}

	@Transactional
	public void update(SmsTemplate smsTemplate) {
		long resultCnt = smsTemplateMapper.updateOne(smsTemplate);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					smsTemplate.getId()));
		}
	}

	@Transactional
	public void update(List<SmsTemplate> smsTemplates) {
		for (SmsTemplate smsTemplate : smsTemplates) {
			update(smsTemplate);
		}

	}

	public PageInfo<SmsTemplate> find(SmsTemplate smsTemplate,int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<SmsTemplate> smsTemplates = smsTemplateMapper.findMany(smsTemplate,rowBounds);
		PageInfo<SmsTemplate> pageInfo = new PageInfo<SmsTemplate>(smsTemplates);
		return pageInfo;
	}

	public List<SmsTemplate> find(SmsTemplate smsTemplate) {
		return smsTemplateMapper.findMany(smsTemplate);
	}

}
