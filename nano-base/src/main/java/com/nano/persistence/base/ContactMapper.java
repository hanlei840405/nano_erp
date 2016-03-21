package com.nano.persistence.base;

import java.util.List;

import org.apache.ibatis.session.RowBounds;

import com.nano.domain.base.Contact;
import com.nano.exception.NanoDBDeleteRuntimeException;
import com.nano.exception.NanoDBInsertRuntimeException;
import com.nano.exception.NanoDBSelectRuntimeException;
import com.nano.exception.NanoDBUpdateRuntimeException;

public interface ContactMapper {

	Contact findOne(String id) throws NanoDBSelectRuntimeException;

	List<Contact> findMany(Contact contact) throws NanoDBSelectRuntimeException;

	List<Contact> findMany(Contact contact,RowBounds rowBounds) throws NanoDBSelectRuntimeException;

	void saveOne(Contact contact) throws NanoDBInsertRuntimeException;
	
	void saveMany(List<Contact> contacts) throws NanoDBInsertRuntimeException;
	
	Long updateOne(Contact contact) throws NanoDBUpdateRuntimeException;
	
	Long deleteOne(Contact contact) throws NanoDBDeleteRuntimeException;
}
