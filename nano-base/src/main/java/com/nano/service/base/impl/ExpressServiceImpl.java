package com.nano.service.base.impl;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Express;
import com.nano.domain.base.PlatformExpress;
import com.nano.domain.base.RegionExpress;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.ExpressMapper;
import com.nano.service.base.ExpressService;
import com.nano.service.base.PlatformExpressService;
import com.nano.service.base.RegionExpressService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class ExpressServiceImpl implements ExpressService {

	@Autowired
	private ExpressMapper expressMapper;
	@Autowired
	private PlatformExpressService platformExpressService;
	@Autowired
	private RegionExpressService regionExpressService;

	@Transactional
	public Express save(Express express) {
		expressMapper.saveOne(express);
		if(!express.getRegionIds().isEmpty()){
			List<RegionExpress> regionExpresses = new ArrayList<RegionExpress>();
			for(String id : express.getRegionIds()){
				RegionExpress regionExpress = new RegionExpress();
				regionExpress.setExpressId(express.getId());
				regionExpress.setRegionId(id);
				regionExpresses.add(regionExpress);
			}
			regionExpressService.save(regionExpresses);
		}
		return express;
	}

	@Transactional
	public List<Express> save(List<Express> expresses) {
		expressMapper.saveMany(expresses);
		return expresses;
	}

	@Transactional
	public void delete(String id) {
		Express express = findOne(id);
		long resultCnt = expressMapper.deleteOne(express);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					express.getId() + " : " + express.getName()));
		}
		PlatformExpress platformExpress = new PlatformExpress();
		platformExpress.setExpressId(id);
		platformExpressService.delete(platformExpress);

		RegionExpress regionExpress = new RegionExpress();
		regionExpress.setExpressId(id);
		regionExpressService.delete(regionExpress);
	}

	@Transactional
	public void delete(List<String> ids) {
		for(String id : ids){
			delete(id);
		}
	}

	public Express findOne(String id) {
		return expressMapper.findOne(id);
	}

	@Transactional
	public void update(Express express) {
		long resultCnt = expressMapper.updateOne(express);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					express.getId() + " : " + express.getName()));
		}
		RegionExpress queryParam = new RegionExpress();
		queryParam.setExpressId(express.getId());
		regionExpressService.delete(queryParam);
		if(!express.getRegionIds().isEmpty()){
			List<RegionExpress> regionExpresses = new ArrayList<RegionExpress>();
			for(String id : express.getRegionIds()){
				RegionExpress regionExpress = new RegionExpress();
				regionExpress.setExpressId(express.getId());
				regionExpress.setRegionId(id);
				regionExpresses.add(regionExpress);
			}
			regionExpressService.save(regionExpresses);
		}
	}

	@Transactional
	public void update(List<Express> expresses) {
		for(Express express : expresses){
			update(express);
		}

	}

	@Transactional
	public void startStop(Express express) {
		long resultCnt = expressMapper.updateOne(express);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					express.getId() + " : " + express.getName()));
		}
	}

	@Transactional
	public void startStop(List<Express> expresses) {
		for(Express express : expresses){
			update(express);
		}

	}

	public PageInfo<Express> find(Express express, int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo, pageSize);
		List<Express> expresses = expressMapper.findMany(express, rowBounds);
		PageInfo<Express> pageInfo = new PageInfo<Express>(expresses);
		return pageInfo;
	}

	public List<Express> find(Express express) {
		return expressMapper.findMany(express);
	}

}
