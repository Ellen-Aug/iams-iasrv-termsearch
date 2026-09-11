/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : TermServiceLookupValueConstants.java
 *
 *********************************************************************
 * VERSION         : 1.0.0
 * REF NO          : 
 * DATE CREATED    : May 21, 2012
 * CREATED BY      : Kenneth Pang, HAITS AP(AI3)1
 *********************************************************************
 */
package hk.org.ha.iams.termsearch.constant;

public class TermServiceLookupValueConstants {

	public static String INSTITUTION_LIST = "service.term.list.institution";
	public static String SYSTEM_USED_LIST = "service.term.list.system.used";
	public static String NATURE_LIST = "service.term.list.nature";
	public static String STATUS_LIST = "service.term.list.status";

	public static final int STAGE_IN_USED = 31;
	public static final int STATUS_ACTIVE = 15;

	private TermServiceLookupValueConstants() {
		super();
	}

}
