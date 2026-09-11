/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : TermCode.java
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

import java.util.List;


/**
 * Terminology Code
 */
public abstract class TermCode implements Serializable {

	private static final long serialVersionUID = -2668327355491379949L;

	private String code;
	private List<AssoTermCode> assoTermCodes;
	private boolean hasAssociatedCode;

	/**
	 * Constructs a new TermCode object.
	 */
	protected TermCode() {
		super();
	}

	/**
	 * Constructs a new TermCode object by the given code.
	 * 
	 * @param code code.
	 */
	protected TermCode(String code) {
		this.code = code;
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
	 * Sets a list of associated terminology codes.
	 * 
	 * @param assoTermCodes a list of associated terminology codes.
	 */
	public void setAssoTermCodes(List<AssoTermCode> assoTermCodes) {
		this.assoTermCodes = assoTermCodes;
	}

	/**
	 * Gets a list of associated terminology codes.
	 * If a ICD10 associated code is matched to more than one term, all matched in-use term id will be returned in this associated code list.
	 * 
	 * @return the given list of associated terminology codes.
	 */
	public List<AssoTermCode> getAssoTermCodes() {
		return assoTermCodes;
	}

	/**
	 * Sets has associated code flag.
	 * 
	 * @param hasAssociatedCode associated code flag.
	 */
	public void setHasAssociatedCode(boolean hasAssociatedCode) {
		this.hasAssociatedCode = hasAssociatedCode;
	}

	/**
	 * Gets has associated code flag.
	 * 
	 * @return the given has associated code flag.
	 */
	public boolean isHasAssociatedCode() {
		return hasAssociatedCode;
	}
}
