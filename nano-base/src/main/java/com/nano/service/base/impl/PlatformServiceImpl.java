package com.nano.service.base.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Platform;
import com.nano.domain.base.PlatformExpress;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.PlatformMapper;
import com.nano.service.base.PlatformExpressService;
import com.nano.service.base.PlatformService;

@Service
public class PlatformServiceImpl implements PlatformService {

	@Autowired
	private PlatformMapper platformMapper;
	@Autowired
	private PlatformExpressService platformExpressService;

	@Transactional	
	public Platform save(Platform platform,List<PlatformExpress> inserts) {
		platformMapper.saveOne(platform);
		if(!inserts.isEmpty()){
			platformExpressService.save(inserts);
		}
		return platform;
	}

	@Transactional
	public List<Platform> save(List<Platform> platforms) {
		platformMapper.saveMany(platforms);
		return platforms;
	}

	@Transactional
	public void delete(String id) {
		Platform platform = findOne(id);
		long resultCnt = platformMapper.deleteOne(platform);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					platform.getId()));
		}
		PlatformExpress platformExpress = new PlatformExpress();
		platformExpress.setPlatformId(id);
		platformExpressService.delete(platformExpress);
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public Platform findOne(String id) {
		return platformMapper.findOne(id);
	}

	@Transactional
	public void update(Platform platform,List<PlatformExpress> inserts) {
		long resultCnt = platformMapper.updateOne(platform);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					platform.getId()));
		}
		PlatformExpress platformExpress = new PlatformExpress();
		platformExpress.setPlatformId(platform.getId());
		platformExpressService.delete(platformExpress);
		if(!inserts.isEmpty()){
			platformExpressService.save(inserts);
		}
	}

	@Transactional
	public void update(List<Platform> platforms) {
		for (Platform platform : platforms) {
			update(platform,new ArrayList<PlatformExpress>());
		}

	}

	public PageInfo<Platform> find(Platform platform,int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Platform> platforms = platformMapper.findMany(platform,rowBounds);
		PageInfo<Platform> pageInfo = new PageInfo<Platform>(platforms);
		return pageInfo;
	}

	public List<Platform> find(Platform platform) {
		return platformMapper.findMany(platform);
	}

}
