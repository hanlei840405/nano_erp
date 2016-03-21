package com.nano.exception;

import com.nano.exception.index.ExceptionIndex;

public class NanoExcelReadRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public NanoExcelReadRuntimeException() {
		super(ExceptionIndex.EXCEL_READ_NO_SHEET_EXCEPTION);
	}

	public NanoExcelReadRuntimeException(String message) {
		super(message);
	}

	public NanoExcelReadRuntimeException(Throwable cause) {
		super(cause);
	}
}
