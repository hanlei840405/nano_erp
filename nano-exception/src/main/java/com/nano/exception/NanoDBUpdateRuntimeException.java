package com.nano.exception;

import com.nano.exception.index.ExceptionIndex;

public class NanoDBUpdateRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NanoDBUpdateRuntimeException() {
		super(ExceptionIndex.DB_UPDATE_EXCEPTION);
	}

	public NanoDBUpdateRuntimeException(String message, Throwable cause,
			boolean enableSuppression, boolean writableStackTrace) {
		super(String.format(ExceptionIndex.DB_UPDATE_EXCEPTION,message), cause, enableSuppression, writableStackTrace);
	}

	public NanoDBUpdateRuntimeException(String message, Throwable cause) {
		super(String.format(ExceptionIndex.DB_UPDATE_EXCEPTION,message), cause);
	}

	public NanoDBUpdateRuntimeException(String message) {
		super(String.format(ExceptionIndex.DB_UPDATE_EXCEPTION,message));
	}

	public NanoDBUpdateRuntimeException(Throwable cause) {
		super(cause);
	}
}
