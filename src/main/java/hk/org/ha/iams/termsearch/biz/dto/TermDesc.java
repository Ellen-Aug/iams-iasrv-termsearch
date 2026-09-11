/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : TermDesc.java
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
 * Terminology Description
 */
public class TermDesc implements Serializable {

	private static final long serialVersionUID = -8579863558068499449L;

	private String code;
	private Integer extension;
	private GetDescCodeType codeType;

	private Integer termId;

	private boolean isFound;

	private String fullDescription;
	private String shortDescription;
	private String status;

	/**
	 * Constructs a new TermDesc object.
	 */
	public TermDesc() {
		super();
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
	 * Set code type.
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
	 * Specifies whether or not the record is found.
	 * 
	 * @param isFound true if the record is found; false otherwise.
	 */
	public void setIsFound(boolean isFound) {
		this.isFound = isFound;
	}

	/**
	 * Tells whether the record is found.
	 * 
	 * @return true if the record is found; false otherwise.
	 */
	public boolean isFound() {
		return isFound;
	}

	/**
	 * Sets full description.
	 * 
	 * @param fullDescription full description.
	 */
	public void setFullDescription(String fullDescription) {
		this.fullDescription = fullDescription;
	}

	/**
	 * Gets full description.
	 * 
	 * @return the given full description.
	 */
	public String getFullDescription() {
		return fullDescription;
	}

	/**
	 * Sets short description.
	 * 
	 * @param shortDescription short description.
	 */
	public void setShortDescription(String shortDescription) {
		this.shortDescription = shortDescription;
	}

	/**
	 * Gets short description.
	 * 
	 * @return the given short description.
	 */
	public String getShortDescription() {
		return shortDescription;
	}

	/**
	 * Sets the search result status.
	 * 
	 * @param status the search result status.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets the search result status.
	 * 
	 * @return the given search result status.
	 */
	public String getStatus() {
		return status;
	}
}
