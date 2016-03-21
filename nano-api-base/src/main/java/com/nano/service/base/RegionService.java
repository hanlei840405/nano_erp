package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Region;

public interface RegionService {

	Region save(Region region);

	void delete(String id);

	void delete(List<String> ids);

	Region findOne(String id);

	void update(Region region);

	void update(List<Region> regions);

	PageInfo<Region> find(Region region, int pageNo, int pageSize);

	List<Region> find(Region region);

	List<Region> findRoots();

	void save(byte[] in);
}
