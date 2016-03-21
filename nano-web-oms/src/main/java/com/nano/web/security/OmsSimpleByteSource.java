package com.nano.web.security;

import java.io.Serializable;

import org.apache.shiro.util.SimpleByteSource;

public class OmsSimpleByteSource extends SimpleByteSource implements
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public OmsSimpleByteSource(byte[] bytes) {
		super(bytes);
	}

}