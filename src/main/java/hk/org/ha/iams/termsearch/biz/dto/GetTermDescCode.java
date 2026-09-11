/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : GetTermDescCode.java
 *
 *********************************************************************
 * VERSION         : 1.0.0
 * REF NO          : 
 * DATE CREATED    : May 21, 2012
 * CREATED BY      : Kenneth Pang, HAITS AP(AI3)1
 *********************************************************************
 */
package hk.org.ha.iams.termsearch.biz.dto;

import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.GetDescCodeType;

import java.io.Serializable;

/**
 * Get Terminology Description Code
 */
public class GetTermDescCode implements Serializable {

	private static final long serialVersionUID = -6756649975560718759L;

	private String code;
	private Integer extension;
	private GetDescCodeType codeType;

	/**
	 * Jackson / REST. EJB clients used the typed constructor below.
	 */
	public GetTermDescCode() {
		super();
	}

	/**
	 * Constructs a new GetTermDescCode object by the given code type, code and extension.
	 * 
	 * @param codeType code type.
	 * @param code code.
	 * @param extension extension.
	 */
	public GetTermDescCode(GetDescCodeType codeType, String code, Integer extension) {
		super();

		this.codeType = codeType;
		this.code = code;
		this.extension = extension;
	}

	/**
	 * Sets code.
	 * 
	 * @param code code.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * Gets code.
	 * 
	 * @return the given code.
	 */
	public String getCode() {
		return code;
	}

	/**
	 * Sets extension.
	 * 
	 * @param extension extension.
	 */
	public void setExtension(Integer extension) {
		this.extension = extension;
	}

	/**
	 * Gets extension.
	 * 
	 * @return the given extension.
	 */
	public Integer getExtension() {
		return extension;
	}

	/**
	 * Sets code type.
	 * 
	 * @param codeType code type.
	 */
	public void setCodeType(GetDescCodeType codeType) {
		this.codeType = codeType;
	}

	/**
	 * Gets code type.
	 * 
	 * @return the given code type.
	 */
	public GetDescCodeType getCodeType() {
		return codeType;
	}
}
