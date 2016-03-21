package com.nano.persistence.system;

import java.util.List;
import java.util.Map;

import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface MenuPrivilegeMapper {
	public int saveMany(List<Map<String,String>> menuPrivileges) throws NanoDBInsertRuntimeException;
	
	public int deleteMany(Map<String,String> menuPrivilege) throws NanoDBDeleteRuntimeException;
	
	public List<Map<String,String>> findMany(Map<String,String> menuPrivilege) throws NanoDBSelectRuntimeException;
}
