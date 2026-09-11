/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : TermServiceUtils.java
 *
 *********************************************************************
 * VERSION         : 1.0.0
 * REF NO          : 
 * DATE CREATED    : May 21, 2012
 * CREATED BY      : Kenneth Pang, HAITS AP(AI3)1
 *********************************************************************
 */
package hk.org.ha.iams.termsearch.util;

import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.CodeType;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchCriteria;

import java.util.ArrayList;
import java.util.List;

public class TermServiceUtils {

	private TermServiceUtils(){
		super();
	}

	public static List<CodeType> getOutputCodeTypes(TermSrchCriteria criteria) {
		List<CodeType> outputCodeTypes = new ArrayList<CodeType>();

		if (!TermCollections.isEmpty(criteria.getOut_codeTypes())) {
			for (String outputCodeType : criteria.getOut_codeTypes()) {
				if (outputCodeType.toUpperCase().equals("ALL")) {
					for (CodeType codeType: CodeType.values()) {
						if (!outputCodeTypes.contains(codeType)) {
							outputCodeTypes.add(codeType);
						}
					}
				}
				else {
					CodeType codeType = CodeType.getByCodeType(outputCodeType);

					if (codeType != null && !outputCodeTypes.contains(codeType)) {
						outputCodeTypes.add(codeType);
					}
				}
			}
		}

		return outputCodeTypes;
	}
}
