package com.nano.service.system.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.nano.domain.system.Element;
import com.nano.persistence.system.ElementMapper;
import com.nano.service.system.ElementService;

@Service
public class ElementServiceImpl implements ElementService {

	@Autowired
	private ElementMapper elementMapper;
	
	public Element get(String id) {
		return elementMapper.findOne(id);
	}

}
