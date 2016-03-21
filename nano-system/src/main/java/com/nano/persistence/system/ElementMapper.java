package com.nano.persistence.system;

import com.nano.domain.system.Element;
import com.nano.exception.NanoDBSelectRuntimeException;

public interface ElementMapper {
	public Element findOne(String id) throws NanoDBSelectRuntimeException;
}
