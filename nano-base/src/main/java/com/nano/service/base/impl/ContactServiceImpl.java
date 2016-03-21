package com.nano.service.base.impl;

import com.github.pagehelper.PageInfo;
import com.nano.domain.base.Contact;
import com.nano.exception.NanoDBConsistencyRuntimeException;
import com.nano.exception.index.ExceptionIndex;
import com.nano.persistence.base.ContactMapper;
import com.nano.persistence.base.ContactUserMapper;
import com.nano.service.base.ContactService;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ContactServiceImpl implements ContactService {

	@Autowired
	private ContactMapper contactMapper;

	@Autowired
	private ContactUserMapper contactUserMapper;

	@Transactional
	public Contact save(Contact contact) {
		contactMapper.saveOne(contact);
		if (!contact.getUserIds().isEmpty()) {
			List<Map<String, String>> params = new ArrayList<>();
			for (String userId : contact.getUserIds()) {
				Map<String, String> contactUser = new HashMap<String, String>();
				contactUser.put("contactId", contact.getId());
				contactUser.put("userId", userId);
				params.add(contactUser);
			}
			contactUserMapper.saveMany(params);
		}
		return contact;
	}

	@Transactional
	public List<Contact> save(List<Contact> contacts) {
		contactMapper.saveMany(contacts);
		List<Map<String, String>> params = new ArrayList<>();
		for (Contact contact : contacts) {
			if (!contact.getUserIds().isEmpty()) {
				for (String userId : contact.getUserIds()) {
					Map<String, String> contactUser = new HashMap<String, String>();
					contactUser.put("contactId", contact.getId());
					contactUser.put("userId", userId);
					params.add(contactUser);
				}
			}
		}
		contactUserMapper.saveMany(params);
		return contacts;
	}

	@Transactional
	public void delete(String id) {
		Contact contact = findOne(id);
		long resultCnt = contactMapper.deleteOne(contact);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					contact.getId() + " : " + contact.getName()));
		}
		Map<String, String> contactUser = new HashMap<String, String>();
		contactUser.put("contactId", id);
		contactUserMapper.deleteMany(contactUser);
	}

	@Transactional
	public void delete(List<String> ids) {
		for (String id : ids) {
			delete(id);
		}
	}

	public Contact findOne(String id) {
		Contact contact = contactMapper.findOne(id);
		Map<String, String> contactUser = new HashMap<String, String>();
		contactUser.put("contactId", contact.getId());
		List<Map<String,String>> contactUsers = contactUserMapper.findMany(contactUser);
		for(Map<String,String> param : contactUsers){
			contact.getUserIds().add(param.get("user_id"));
		}
		return contact;
	}

	@Transactional
	public void update(Contact contact) {
		long resultCnt = contactMapper.updateOne(contact);
		if (resultCnt == 0) {
			throw new NanoDBConsistencyRuntimeException(String.format(
					ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION,
					contact.getId() + " : " + contact.getName()));
		}
		Map<String, String> contactUser = new HashMap<String, String>();
		contactUser.put("contactId", contact.getId());
		contactUserMapper.deleteMany(contactUser);
		if (!contact.getUserIds().isEmpty()) {
			List<Map<String, String>> params = new ArrayList<>();
			for (String userId : contact.getUserIds()) {
				Map<String, String> insert = new HashMap<String, String>();
				insert.put("contactId", contact.getId());
				insert.put("userId", userId);
				params.add(insert);
			}
			contactUserMapper.saveMany(params);
		}
	}

	@Transactional
	public void update(List<Contact> contacts) {
		for(Contact contact : contacts){
			update(contact);
		}
	}

	public PageInfo<Contact> find(Contact contact, int pageNo, int pageSize) {
		RowBounds rowBounds = new RowBounds(pageNo,pageSize);
		List<Contact> contacts = contactMapper.findMany(contact, rowBounds);
		Map<String, String> contactUser = new HashMap<String, String>();
		for(Contact c : contacts){
			contactUser.put("contactId", c.getId());
			List<Map<String,String>> contactUsers = contactUserMapper.findMany(contactUser);
			for(Map<String,String> param : contactUsers){
				c.getUserIds().add(param.get("user_id"));
			}
		}
		PageInfo<Contact> pageInfo = new PageInfo<Contact>(contacts);
		return pageInfo;
	}

	public List<Contact> find(Contact contact) {
		List<Contact> contacts = contactMapper.findMany(contact);
		Map<String, String> contactUser = new HashMap<String, String>();
		for(Contact c : contacts){
			contactUser.put("contactId", c.getId());
			List<Map<String,String>> contactUsers = contactUserMapper.findMany(contactUser);
			for(Map<String,String> param : contactUsers){
				c.getUserIds().add(param.get("user_id"));
			}
		}
		return contacts;
	}

}
