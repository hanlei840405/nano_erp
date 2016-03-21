package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Platform;
import com.nano.domain.base.PlatformExpress;

public interface PlatformService {

	Platform save(Platform platform,List<PlatformExpress> inserts);

	List<Platform> save(List<Platform> platforms);

	void delete(String id);

	void delete(List<String> ids);

	Platform findOne(String id);

	void update(Platform platform,List<PlatformExpress> inserts);

	void update(List<Platform> platforms);

	PageInfo<Platform> find(Platform platform, int pageNo, int pageSize);

	List<Platform> find(Platform platform);
}
