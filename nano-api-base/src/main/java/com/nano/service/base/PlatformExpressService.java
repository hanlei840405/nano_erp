package com.nano.service.base;

import java.util.List;

import com.nano.domain.base.PlatformExpress;

public interface PlatformExpressService {

	List<PlatformExpress> find(PlatformExpress platformExpress);

	void save(PlatformExpress platformExpress);
	
	void save(List<PlatformExpress> platformExpresses);
	
	void delete(PlatformExpress platformExpress);
}
