/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : AssoEntity.java
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
 * Associated Entity 
 */
public class AssoEntity implements Serializable {


	private static final long serialVersionUID = 3900519912251249956L;

	private Integer entityId;
	private String fullDesc;
	private String eprDesc;

	/**
	 * Constructs a new AssoEntity object.
	 */
	public AssoEntity() {
		super();
	}

	/**
	 * Constructs a new AssoEntity object by the given entity id, full description and ePR description.
	 * 
	 * @param entityId entity id.
	 * @param fullDesc full description.
	 * @param eprDesc ePR description.
	 */
	public AssoEntity(Integer entityId, String fullDesc, String eprDesc) {
		this.entityId = entityId;
		this.fullDesc = fullDesc;
		this.eprDesc = eprDesc;
	}

	/**
	 * Sets entity id.
	 * 
	 * @param entityId entity id.
	 */
	public void setEntityId(Integer entityId) {
		this.entityId = entityId;
	}

	/**
	 * Gets entity id.
	 * 
	 * @return the given entity id.
	 */
	public Integer getEntityId() {
		return entityId;
	}

	/**
	 * Sets full description.
	 * 
	 * @param fullDesc full description.
	 */
	public void setFullDesc(String fullDesc) {
		this.fullDesc = fullDesc;
	}

	/**
	 * Gets full description.
	 * 
	 * @return the given full description.
	 */
	public String getFullDesc() {
		return fullDesc;
	}

	/**
	 * Sets ePR description.
	 * 
	 * @param eprDesc ePR description.
	 */
	public void setEprDesc(String eprDesc) {
		this.eprDesc = eprDesc;
	}

	/**
	 * Gets ePR description.
	 * 
	 * @return the given ePR description.
	 */
	public String getEprDesc() {
		return eprDesc;
	}
}
