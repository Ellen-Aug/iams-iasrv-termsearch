/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : ServiceException.java
 *
 *********************************************************************
 * VERSION         : 1.0.0
 * REF NO          : 
 * DATE CREATED    : May 21, 2012
 * CREATED BY      : Kenneth Pang, HAITS AP(AI3)1
 *********************************************************************
 */
package hk.org.ha.iams.termsearch.exception;

import hk.org.ha.iams.termsearch.validation.ReturnMessage.Error;

public class ServiceException extends Exception {

	private static final long serialVersionUID = -2253318806167066904L;

	private Integer code;

	/**
	 * Constructs a ServiceException with the specified detail message.
	 * @param message the detail message.
	 */
	public ServiceException(String message) {
		super(message);
	}

	/**
	 * Constructs a ServiceException with the specified error code.
	 * @param code the error code.
	 */
	public ServiceException(Integer code) {
		super(generateMessageByCode(code));

		this.code = code;
	}

	/**
	 * Constructs a ServiceException with the specified error code and detail message.
	 * @param code the error code.
	 * @param message the detail message.
	 */
	public ServiceException(Integer code, String message) {
		super(generateMessageByCode(code) + " (" + message + ")");

		this.code = code;
	}

	/**
	 * Returns the error code.
	 * <p>
	 * 1 - General error.<br>
	 * 4 - Unknown error.<br>
	 * 5 - Missing parameter.<br>
	 * 6 - Missing mandatory field(s).<br>
	 * 7 - No record found.<br>
	 * </p>
	 * @return the error code.
	 */
	public Integer getCode() {
		return code;
	}

	private static String generateMessageByCode(Integer code) {
		String message = null;

		if (code != null) {
			if (code.equals(Error.FAIL.ordinal()) ) {
				message = "Error(" + code + "): fail";
			}
			else if (code.equals(Error.FAIL_INVALID_XML.ordinal()) ) {
				message = "Error(" + code + "): invalid xml";
			}
			else if (code.equals(Error.FAIL_BATCH_SEQ_ID_NOT_UDPATED.ordinal()) ) {
				message = "Error(" + code + "): batch sequence id not updated";
			}
			else if (code.equals(Error.FAIL_UNKNOWN_EXCEPTION.ordinal()) ) {
				message = "Error(" + code + "): unknown exception";
			}
			else if (code.equals(Error.EXCEPTION_WITHOUT_INPUT_PARAMETER.ordinal()) ) {
				message = "Error(" + code + "): without input parameter";
			}
			else if (code.equals(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS.ordinal())) {
				message = "Error(" + code + "): missing compulsory input parameters";
			}
			else if (code.equals(Error.ERROR_EXCEEDS_RECORDS_LIMIT.ordinal())) {
				message = "Error(" + code + "): exceeds records limit";
			}
			else if (code.equals(Error.WARNING_SERVICE_UNAVAILABLE.ordinal())) {
				message = "Warning(" + code + "): service unavailable";
			}
		}

		return message;
	}

}
