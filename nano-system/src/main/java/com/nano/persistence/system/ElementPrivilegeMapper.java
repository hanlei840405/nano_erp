package com.nano.persistence.system;

import java.util.List;
import java.util.Map;

import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface ElementPrivilegeMapper {
	public int saveMany(List<Map<String,String>> elementPrivileges) throws NanoDBInsertRuntimeException;
	
	public int deleteMany(Map<String,String> elementPrivilege) throws NanoDBDeleteRuntimeException;
	
	public List<Map<String,String>> findMany(Map<String,String> elementPrivilege) throws NanoDBSelectRuntimeException;
}
