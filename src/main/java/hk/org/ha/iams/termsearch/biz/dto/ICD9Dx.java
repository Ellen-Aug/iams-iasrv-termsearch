/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : ICD9Dx.java
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
 * ICD9Dx 
 */
public class ICD9Dx extends TermCode implements Serializable {


	private static final long serialVersionUID = -6582780636988140562L;

	private Integer extension;

	/**
	 * Constructs a new ICD9Dx object.
	 */
	public ICD9Dx() {
		super();
	}

	/**
	 * Constructs a new ICD9Dx object by the given code and extension.
	 * 
	 * @param code code.
	 * @param extension extension.
	 */
	public ICD9Dx(String code, Integer extension) {
		super(code);

		this.extension = extension;
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
