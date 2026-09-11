/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : GetTermDescCriteria.java
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
 * <p>If both list of term IDs and term codes are inputted, it will process the term ID search only.</p>
 */
public class GetTermDescCriteria implements Serializable {

	private static final long serialVersionUID = -887668581845074065L;

	private String inRequestKey;
	private List<GetTermDescCode> inTermCodes;
	private List<Integer> inTermIDs;

	/**
	 * Constructs a new GetTermDescCriteria object.
	 */
	public GetTermDescCriteria() {
		super();
	}

	/**
	 * Sets a list of term codes.
	 * 
	 * @param inTermCodes a list of term codes.
	 */
	public void setInTermCodes(List<GetTermDescCode> inTermCodes) {
		this.inTermCodes = inTermCodes;
	}

	/**
	 * Gets a list of term codes.
	 * 
	 * @return the given list of term codes.
	 */
	public List<GetTermDescCode> getInTermCodes() {
		return inTermCodes;
	}

	/**
	 * Sets request key.
	 * 
	 * @param inRequestKey request key.
	 */
	public void setInRequestKey(String inRequestKey) {
		this.inRequestKey = inRequestKey;
	}

	/**
	 * Gets request key.
	 * 
	 * @return the given request key.
	 */
	public String getInRequestKey() {
		return inRequestKey;
	}

	/**
	 * Sets a list of term IDs.
	 * 
	 * @param inTermIDs a list of term IDs.
	 */
	public void setInTermIDs(List<Integer> inTermIDs) {
		this.inTermIDs = inTermIDs;
	}

	/**
	 * Gets a list of term IDs.
	 * 
	 * @return the given list of term IDs.
	 */
	public List<Integer> getInTermIDs() {
		return inTermIDs;
	}
}
