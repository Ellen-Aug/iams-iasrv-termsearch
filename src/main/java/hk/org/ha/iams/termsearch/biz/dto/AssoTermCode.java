/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : AssoTermCode.java
 *
 *********************************************************************
 * VERSION         : 1.0.0
 * REF NO          : 
 * DATE CREATED    : May 21, 2012
 * CREATED BY      : Kenneth Pang, HAITS AP(AI3)1
 *********************************************************************
 */
package hk.org.ha.iams.termsearch.biz.dto;

import java.io.Serializable;


/**
 * Associated Terminology Code
 */
public class AssoTermCode implements Serializable {


	private static final long serialVersionUID = 9153732709424163227L;

	private Integer termId;
	private String code;
	private Integer extension;

	/**
	 * Constructs a new AssoTermCode object.
	 */
	public AssoTermCode() {
		super();
	}

	/**
	 * Constructs a new AssoTermCode object by the given code.
	 * 
	 * @param code code.
	 */
	public AssoTermCode(String code) {
		this.code = code;
	}

	/**
	 * Constructs a new AssoTermCode object by the given code and extension.
	 * 
	 * @param code code.
	 * @param extension extension.
	 */
	public AssoTermCode(String code, Integer extension) {
		this.code = code;
		this.extension = extension;
	}

	/**
	 * Constructs a new AssoTermCode object by the given term ID and code.
	 * 
	 * @param termId term ID.
	 * @param code code.
	 */
	public AssoTermCode(Integer termId, String code) {
		this.code = code;
		this.termId = termId;
	}

	/**
	 * Constructs a new AssoTermCode object by the given term ID, code and extension.
	 * 
	 * @param termId term id.
	 * @param code code.
	 * @param extension extension.
	 */
	public AssoTermCode(Integer termId, String code, Integer extension) {
		this.code = code;
		this.termId = termId;
		this.extension = extension;
	}

	/**
	 * Sets term ID.
	 * 
	 * @param termId term ID.
	 */
	public void setTermId(Integer termId) {
		this.termId = termId;
	}

	/**
	 * Gets term ID.
	 * 
	 * @return the given term ID.
	 */
	public Integer getTermId() {
		return termId;
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
}
