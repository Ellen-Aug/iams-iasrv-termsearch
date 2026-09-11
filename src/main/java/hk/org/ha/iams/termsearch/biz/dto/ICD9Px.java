/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : ICD9Px.java
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
 * ICD9Px
 */
public class ICD9Px extends TermCode implements Serializable {


	private static final long serialVersionUID = 716966237572433355L;

	private String pxWeight;
	private Integer extension;

	/**
	 * Constructs a new ICD9Px object.
	 */
	public ICD9Px() {
		super();
	}

	/**
	 * Constructs a new ICD9Px object by the given code, extension and PX weight.
	 * 
	 * @param code code.
	 * @param extension extension.
	 * @param pxWeight PX weight.
	 */
	public ICD9Px(String code, Integer extension, String pxWeight) {
		super(code);

		this.extension = extension;
		this.pxWeight = pxWeight;
	}

	/**
	 * Sets PX weight.
	 * 
	 * @param pxWeight PX weight.
	 */
	public void setPxWeight(String pxWeight) {
		this.pxWeight = pxWeight;
	}

	/**
	 * Gets PX weight.
	 * 
	 * @return the given PX weight.
	 */
	public String getPxWeight() {
		return pxWeight;
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
