package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.SmsTemplate;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface SmsTemplateMapper {

	SmsTemplate findOne(String id) throws NanoDBSelectRuntimeException;

	List<SmsTemplate> findMany(SmsTemplate smsTemplate) throws NanoDBSelectRuntimeException;

	List<SmsTemplate> findMany(SmsTemplate smsTemplate,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(SmsTemplate smsTemplate) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<SmsTemplate> smsTemplates) throws NanoDBInsertRuntimeException;
	
	Long updateOne(SmsTemplate smsTemplate) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(SmsTemplate smsTemplate) throws NanoDBDeleteRuntimeException;
}
