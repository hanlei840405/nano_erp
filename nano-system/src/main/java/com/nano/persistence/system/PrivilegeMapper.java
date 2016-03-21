package com.nano.persistence.system;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.system.Privilege;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface PrivilegeMapper {

	Privilege findOne(String id) throws NanoDBSelectRuntimeException;

	List<Privilege> findMany(Map<String,String> params) throws NanoDBSelectRuntimeException;

	List<Privilege> findMany(Map<String,String> params,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Privilege privilege) throws NanoDBInsertRuntimeException;

	void saveMany(List<Privilege> privileges) throws NanoDBInsertRuntimeException;

	Long updateOne(Privilege privilege) throws NanoDBUpdateRuntimeException;

	void updateMany(List<Privilege> privileges) throws NanoDBUpdateRuntimeException;

	Long deleteOne(Privilege privilege) throws NanoDBDeleteRuntimeException;

	void deleteMany(List<String> ids) throws NanoDBDeleteRuntimeException;
}
