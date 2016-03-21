package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Sms;

public interface SmsService {

	Sms save(Sms sms);

	List<Sms> save(List<Sms> smses);

	void delete(String id);

	void delete(List<String> ids);

	Sms findOne(String id);

	PageInfo<Sms> find(Sms sms, int pageNo, int pageSize);

	List<Sms> find(Sms sms);
}
