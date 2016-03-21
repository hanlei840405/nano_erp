package com.nano.persistence.base;

import java.util.List;
import java.util.Map;

import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface ContactUserMapper {
	public int saveMany(List<Map<String,String>> contactUsers) throws NanoDBInsertRuntimeException;
	
	public int deleteMany(Map<String,String> contactUsers) throws NanoDBDeleteRuntimeException;
	
	public List<Map<String,String>> findMany(Map<String,String> contactUser) throws NanoDBSelectRuntimeException;
}
