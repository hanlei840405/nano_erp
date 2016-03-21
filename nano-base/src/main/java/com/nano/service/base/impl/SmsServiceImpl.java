package com.nano.service.base.impl;

import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Sms;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.SmsMapper;
import com.nano.service.base.SmsService;

@Service
public class SmsServiceImpl implements SmsService {

	@Autowired
	private SmsMapper smsMapper;

	@Transactional	
	public Sms save(Sms sms) {
		smsMapper.saveOne(sms);
		return sms;
	}

	@Transactional
	public List<Sms> save(List<Sms> smses) {
		smsMapper.saveMany(smses);
		return smses;
	}

	@Transactional
	public void delete(String id) {
		Sms sms = findOne(id);
		long resultCnt = smsMapper.deleteOne(sms);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					sms.getId()));
		}
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public Sms findOne(String id) {
		return smsMapper.findOne(id);
	}

	public PageInfo<Sms> find(Sms sms,int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Sms> smses = smsMapper.findMany(sms,rowBounds);
		PageInfo<Sms> pageInfo = new PageInfo<Sms>(smses);
		return pageInfo;
	}

	public List<Sms> find(Sms sms) {
		return smsMapper.findMany(sms);
	}

}
