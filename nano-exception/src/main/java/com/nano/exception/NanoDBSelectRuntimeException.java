package com.nano.exception;

import com.nano.exception.index.ExceptionIndex;

public class NanoDBSelectRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NanoDBSelectRuntimeException() {
		super(ExceptionIndex.DB_SELECT_EXCEPTION);
	}

	public NanoDBSelectRuntimeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(String.format(ExceptionIndex.DB_SELECT_EXCEPTION,message), cause, enableSuppression, writableStackTrace);
	}

	public NanoDBSelectRuntimeException(String message, Throwable cause) {
		super(String.format(ExceptionIndex.DB_SELECT_EXCEPTION,message), cause);
	}

	public NanoDBSelectRuntimeException(String message) {
		super(String.format(ExceptionIndex.DB_SELECT_EXCEPTION,message));
	}

	public NanoDBSelectRuntimeException(Throwable cause) {
		super(cause);
	}
}
