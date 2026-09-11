/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 ********************************************************************/
package hk.org.ha.iams.termsearch.biz.dto;
import java.io.Serializable;

import java.util.List;

public class TermSrchResult implements Serializable {

	private static final long serialVersionUID = 6482021561854386562L;

	private TermReturnStatus termStatus;
	private List<TermSrchRecord> termSrchRecords;

	public TermSrchResult() {
		super();
	}

	public void setTermStatus(TermReturnStatus termStatus) {
		this.termStatus = termStatus;
	}

	public TermReturnStatus getTermStatus() {
		return termStatus;
	}

	public void setTermSrchRecords(List<TermSrchRecord> termSrchRecords) {
		this.termSrchRecords = termSrchRecords;
	}

	public List<TermSrchRecord> getTermSrchRecords() {
		return termSrchRecords;
	}
}
