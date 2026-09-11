/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : Alias.java
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
 * Alias
 */
public class Alias implements Serializable {


	private static final long serialVersionUID = -2552486848946841433L;
	private Integer aliasId;
	private String aliasDesc;

	/**
	 * Constructs a new Alias object.
	 */
	public Alias() {
		super();
	}

	/**
	 * Constructs a new Alias object by the given alias id and alias description.
	 * 
	 * @param aliasId alias ID.
	 * @param aliasDesc alias description.
	 */
	public Alias(Integer aliasId, String aliasDesc) {
		this.aliasId = aliasId;
		this.aliasDesc = aliasDesc;
	}

	/**
	 * Sets alias ID.
	 * 
	 * @param aliasId alias ID.
	 */
	public void setAliasId(Integer aliasId) {
		this.aliasId = aliasId;
	}

	/**
	 * Gets alias ID.
	 * 
	 * @return the given alias ID.
	 */
	public Integer getAliasId() {
		return aliasId;
	}

	/**
	 * Sets alias description.
	 * 
	 * @param aliasDesc alias description.
	 */
	public void setAliasDesc(String aliasDesc) {
		this.aliasDesc = aliasDesc;
	}

	/**
	 * Gets alias description.
	 * 
	 * @return the given alias description.
	 */
	public String getAliasDesc() {
		return aliasDesc;
	}
}
