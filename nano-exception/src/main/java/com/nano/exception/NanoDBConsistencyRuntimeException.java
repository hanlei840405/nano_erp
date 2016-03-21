package com.nano.exception;

import com.nano.exception.index.ExceptionIndex;

public class NanoDBConsistencyRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NanoDBConsistencyRuntimeException() {
		super(ExceptionIndex.DB_TRANSACTION_CONSISTENCY_EXCEPTION);
	}

	public NanoDBConsistencyRuntimeException(String message) {
		super(message);
	}

	public NanoDBConsistencyRuntimeException(Throwable cause) {
		super(cause);
	}

}
