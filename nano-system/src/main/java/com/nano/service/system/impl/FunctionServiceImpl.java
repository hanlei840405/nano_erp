package com.nano.service.system.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.nano.domain.system.Function;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.system.FunctionMapper;
import com.nano.persistence.system.FunctionPrivilegeMapper;
import com.nano.service.system.FunctionService;

@Service
public class FunctionServiceImpl implements FunctionService {

	@Autowired
	private FunctionMapper functionMapper;
	
	@Autowired
	private FunctionPrivilegeMapper functionPrivilegeMapper;

	public Function get(String id) {
		return functionMapper.findOne(id);
	}

	@Transactional
	public Function save(Function function) {
		functionMapper.saveOne(function);
		return function;
	}

	public List<Function> save(List<Function> functions) {
		if(!functions.isEmpty()){
			functionMapper.saveMany(functions);
		}
		return functions;
	}

	public List<Function> queryFunctionsByMenu(String menuId) {
		return functionMapper.findManyByMenu(menuId);
	}

	@Transactional
	public void delete(String id) {
		Function function = get(id);
		Map<String, String> params = new HashMap<String, String>();
		params.put("functionId", id);
		functionPrivilegeMapper.deleteMany(params);
		long resultCnt = functionMapper.delete(function);
		if(resultCnt == 0){
			throw new NanoDBConsistencyRuntimeException(String.format(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,function.getId() + " : " + function.getName()));
		}
	}

	@Transactional
	public void deleteManyByMenu(String menuId) {
		List<Function> functions = functionMapper.findManyByMenu(menuId);
		Map<String, String> params = new HashMap<String, String>();
		for(Function function : functions){
			params.put("functionId", function.getId());
			functionPrivilegeMapper.deleteMany(params);
		}
		Function function = new Function();
		function.setMenuId(menuId);
		functionMapper.delete(function);
	}

	@Transactional
	public void delete(List<String> ids) {
		Map<String, String> params = new HashMap<String, String>();
		for(String id : ids){
			params.put("functionId", id);
			functionPrivilegeMapper.deleteMany(params);
		}
		functionMapper.deleteMany(ids);		
	}

	@Transactional
	public void update(Function function) {
		long resultCnt = functionMapper.updateOne(function);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,function.getId() + " : " + function.getName()));
		}
	}

	@Transactional
	public void update(List<Function> functions) {
		functionMapper.updateMany(functions);
	}

}
