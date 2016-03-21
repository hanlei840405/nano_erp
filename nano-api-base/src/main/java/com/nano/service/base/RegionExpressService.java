package com.nano.service.base;

import java.util.List;

import com.nano.domain.base.RegionExpress;

public interface RegionExpressService {

	List<RegionExpress> find(RegionExpress regionExpress);

	void save(RegionExpress regionExpress);
	
	void save(List<RegionExpress> regionExpresses);
	
	void delete(RegionExpress regionExpress);
}
