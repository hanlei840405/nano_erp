package com.nano.service.base.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nano.domain.base.PlatformExpress;
import com.nano.persistence.base.PlatformExpressMapper;
import com.nano.service.base.PlatformExpressService;

@Service
public class PlatformExpressServiceImpl implements PlatformExpressService {

	@Autowired
	private PlatformExpressMapper platformExpressMapper;
	
	public List<PlatformExpress> find(PlatformExpress platformExpress) {
		return platformExpressMapper.findMany(platformExpress);
	}

	@Transactional
	public void save(PlatformExpress platformExpress) {
		platformExpressMapper.saveOne(platformExpress);
	}

	@Transactional
	public void save(List<PlatformExpress> platformExpresses) {
		Map<String,Object> params = new HashMap<String, Object>();
		params.put("platformExpresses",platformExpresses);
		platformExpressMapper.saveMany(params);
	}

	@Transactional
	public void delete(PlatformExpress platformExpress) {
		platformExpressMapper.delete(platformExpress);
	}

}
