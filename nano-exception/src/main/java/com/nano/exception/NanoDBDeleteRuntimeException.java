package com.nano.exception;

import com.nano.exception.index.ExceptionIndex;


public class NanoDBDeleteRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public NanoDBDeleteRuntimeException() {
		super(ExceptionIndex.DB_DELETE_EXCEPTION);
	}
	public NanoDBDeleteRuntimeException(String message) {
		super(message);
	}
}
