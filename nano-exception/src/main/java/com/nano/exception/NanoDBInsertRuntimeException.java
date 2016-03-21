package com.nano.exception;

import com.nano.exception.index.ExceptionIndex;

public class NanoDBInsertRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NanoDBInsertRuntimeException() {
		super(ExceptionIndex.DB_INSERT_EXCEPTION);
	}

	public NanoDBInsertRuntimeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(String.format(ExceptionIndex.DB_INSERT_EXCEPTION,message), cause, enableSuppression, writableStackTrace);
	}

	public NanoDBInsertRuntimeException(String message, Throwable cause) {
		super(String.format(ExceptionIndex.DB_INSERT_EXCEPTION,message), cause);
	}

	public NanoDBInsertRuntimeException(String message) {
		super(String.format(ExceptionIndex.DB_INSERT_EXCEPTION,message));
	}

	public NanoDBInsertRuntimeException(Throwable cause) {
		super(cause);
	}
}
