/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 ********************************************************************/
package hk.org.ha.iams.termsearch.biz.dto;
import java.io.Serializable;

import java.util.List;

public class GetTermDescResult implements Serializable {

	private static final long serialVersionUID = 980818355261931910L;

	private GetTermDescReturnStatus status;
	private List<TermDesc> termDescs;

	public GetTermDescResult() {
		super();
	}

	public void setStatus(GetTermDescReturnStatus status) {
		this.status = status;
	}

	public GetTermDescReturnStatus getStatus() {
		return status;
	}

	public void setTermDescs(List<TermDesc> termDescs) {
		this.termDescs = termDescs;
	}

	public List<TermDesc> getTermDescs() {
		return termDescs;
	}
}
