package com.nano.service.base;

import java.util.List;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Contact;

public interface ContactService {

	Contact save(Contact contact);

	List<Contact> save(List<Contact> contacts);

	void delete(String id);

	void delete(List<String> ids);

	Contact findOne(String id);

	void update(Contact contact);

	void update(List<Contact> contacts);
	
	PageInfo<Contact> find(Contact contact,int pageNo,int pageSize);
	
	List<Contact> find(Contact contact);
}
