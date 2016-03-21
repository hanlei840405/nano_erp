package com.nano.service.base.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nano.domain.base.RegionExpress;
import com.nano.persistence.base.RegionExpressMapper;
import com.nano.service.base.RegionExpressService;

@Service
public class RegionExpressServiceImpl implements RegionExpressService {

	@Autowired
	private RegionExpressMapper regionExpressMapper;

	public List<RegionExpress> find(RegionExpress regionExpress) {
		return regionExpressMapper.findMany(regionExpress);
	}

	@Transactional
	public void save(RegionExpress regionExpress) {
		delete(regionExpress);
		regionExpressMapper.saveOne(regionExpress);
	}

	@Transactional
	public void save(List<RegionExpress> regionExpresses) {
		for(RegionExpress regionExpress : regionExpresses){
			delete(regionExpress);
		}
		Map<String,Object> params = new HashMap<>();
		params.put("regionExpresses", regionExpresses);
		regionExpressMapper.saveMany(params);
	}

	@Transactional
	public void delete(RegionExpress regionExpress) {
		regionExpressMapper.delete(regionExpress);
	}

}
