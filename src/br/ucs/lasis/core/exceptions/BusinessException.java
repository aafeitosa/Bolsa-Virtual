package br.ucs.lasis.core.exceptions;

import javax.ejb.ApplicationException;

@ApplicationException(rollback = true)
public class BusinessException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	private static final String MESSAGE = "";

	public BusinessException() {
		super(MESSAGE);
	}

	public BusinessException(String arg0, Throwable arg1, boolean arg2, boolean arg3) {
		super(MESSAGE + " : " + arg0, arg1, arg2, arg3);
	}

	public BusinessException(String arg0, Throwable arg1) {
		super(MESSAGE + " : " + arg0, arg1);
	}

	public BusinessException(String arg0) {
		super(MESSAGE + " : " + arg0);
	}

	public BusinessException(Throwable arg0) {
		super(MESSAGE, arg0);
	}

}
