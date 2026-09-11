/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : GetTermDescReturnStatus.java
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
import java.sql.Timestamp;


/**
 * Get Terminology Description Return Status
 * 
 * <p>Possible return code:</p>
 * <table cellspacing="2" cellpadding="2" border="0" summary="Get terminology description return status possible return code">
 *   <tr>
 *     <td><b>Return Code</b></td>
 *     <td><b>Description</b></td>
 *   </tr>
 *   <tr>
 *     <td>0</td>
 *     <td>Success</td>
 *   </tr>
 *   <tr>
 *     <td>4</td>
 *     <td>Fail - Unknown Exception</td>
 *   </tr>
 *   <tr>
 *     <td>5</td>
 *     <td>Error - Without Input Parameter</td>
 *   </tr>
 *   <tr>
 *     <td>6</td>
 *     <td>Error - Missing Compulsory Input Parameters</td>
 *   </tr>
 *   <tr>
 *     <td>9</td>
 *     <td>Error - Some Records Not Found</td>
 *   </tr>
 * </table>
 */
public class GetTermDescReturnStatus implements Serializable {

	private static final long serialVersionUID = -5171950570903066017L;

	private Integer returnCode;
	private String returnMessage;
	private Timestamp timestamp;

	private Integer count;

	/**
	 * Constructs a new GetTermDescReturnStatus object.
	 */
	public GetTermDescReturnStatus() {
		super();
	}

	/**
	 * Sets the total number of searched term.
	 * 
	 * @param count the total number of searched term.
	 */
	public void setCount(Integer count) {
		this.count = count;
	}

	/**
	 * Gets total number of searched term.
	 * 
	 * @return the given total number of searched term.
	 */
	public Integer getCount() {
		return count;
	}

	/**
	 * Sets return code.
	 * 
	 * @param returnCode return code.
	 */
	public void setReturnCode(Integer returnCode) {
		this.returnCode = returnCode;
	}

	/**
	 * Gets return code.
	 * 
	 * @return the given return code.
	 */
	public Integer getReturnCode() {
		return returnCode;
	}

	/**
	 * Sets return message.
	 * 
	 * @param returnMessage return message.
	 */
	public void setReturnMessage(String returnMessage) {
		this.returnMessage = returnMessage;
	}

	/**
	 * Gets return message.
	 * 
	 * @return the given return message.
	 */
	public String getReturnMessage() {
		return returnMessage;
	}

	/**
	 * Sets return timestamp.
	 * 
	 * @param timestamp return timestamp.
	 */
	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	/**
	 * Gets return timestamp.
	 * 
	 * @return the given return timestamp.
	 */
	public Timestamp getTimestamp() {
		return timestamp;
	}
}
