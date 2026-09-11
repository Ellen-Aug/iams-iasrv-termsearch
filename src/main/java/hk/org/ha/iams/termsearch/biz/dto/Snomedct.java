/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : Snomedct.java
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

import java.util.Date;


/**
 * SNOMED CT
 */
public class Snomedct implements Serializable {

	private static final long serialVersionUID = -7225723647626348263L;

	private String code;
	private String version;
	private Date versionDate;

	/**
	 * Constructs a new Snomedct object.
	 */
	public Snomedct() {
		super();
	}

	/**
	 * Constructs a new Snomedct object by the given code, version and version date.
	 * 
	 * @param code code.
	 * @param version version.
	 * @param versionDate version date.
	 */
	public Snomedct(String code, String version, Date versionDate) {
		this.code = code;
		this.version = version;
		this.versionDate = versionDate;
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
	 * Sets version.
	 * 
	 * @param version version.
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * Gets version.
	 * 
	 * @return the given version.
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * Sets version date.
	 * 
	 * @param versionDate version date.
	 */
	public void setVersionDate(Date versionDate) {
		this.versionDate = versionDate;
	}

	/**
	 * Gets version date.
	 * 
	 * @return the given version date.
	 */
	public Date getVersionDate() {
		return versionDate;
	}
}
