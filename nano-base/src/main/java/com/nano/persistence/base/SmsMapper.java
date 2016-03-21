package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Sms;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface SmsMapper {

	Sms findOne(String id) throws NanoDBSelectRuntimeException;

	List<Sms> findMany(Sms sms) throws NanoDBSelectRuntimeException;

	List<Sms> findMany(Sms sms,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Sms sms) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Sms> smses) throws NanoDBInsertRuntimeException;
	
	Long deleteOne(Sms sms) throws NanoDBDeleteRuntimeException;
}
