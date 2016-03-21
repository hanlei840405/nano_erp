package com.nano.persistence.system;

import java.util.List;
import java.util.Map;

import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface UserRoleMapper {
	public int saveMany(List<Map<String,String>> userRoles) throws NanoDBInsertRuntimeException;
	
	public int deleteMany(Map<String,String> userRole) throws NanoDBDeleteRuntimeException;
	
	public List<Map<String,String>> findMany(Map<String,String> userRole) throws NanoDBSelectRuntimeException;
}
