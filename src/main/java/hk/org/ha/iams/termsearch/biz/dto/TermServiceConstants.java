/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : TermServiceConstants.java
 *
 *********************************************************************
 * VERSION         : 1.0.0
 * REF NO          : 
 * DATE CREATED    : May 21, 2012
 * CREATED BY      : Kenneth Pang, HAITS AP(AI3)1
 *********************************************************************
 */
package hk.org.ha.iams.termsearch.biz.dto;

public class TermServiceConstants {

	private TermServiceConstants(){
		super();
	}

	/**
	 * Code type.
	 */
	public enum CodeType {
		/**
		 * ICD9Dx.
		 */
		ICD9Dx("ICD9Dx"),
		/**
		 * ICD9Px.
		 */
		ICD9Px("ICD9Px"),
		/**
		 * ICD10Dx.
		 */
		//ICD10Dx("ICD10Dx"),
		/**
		 * ICD102010MBD.
		 */
		ICD102010MBD("ICD102010MBD"),
		/**
		 *  ICPC2
		 */
		ICPC2("ICPC2"),
		/**
		 * Snomed CT.
		 */
		SNOMEDCT("SNOMEDCT");

		private final String codeType;

		CodeType(String codeType) {
			this.codeType = codeType;
		}

		public String codeType() {
			return codeType;
		}

		public String toString() {
			return codeType;
		}

		/**
		 * Returns the enum constant of this type with the specified code type string.
		 * 
		 * @param codeType code type string.
		 * @return the enum constant with the specified code type string.
		 */
		public static CodeType getByCodeType(String codeType) {
			CodeType correspondingCodeType = null;

			for (CodeType codeTypeConstant : values()) {
				if (codeTypeConstant.codeType().equals(codeType)) {
					correspondingCodeType = codeTypeConstant;
					break;
				}
			}

			return correspondingCodeType;
		}
	}

	/**
	 * Search type for code search.
	 */
	public enum SearchType {
		/**
		 * Search by code.
		 */
		CODE,
		/**
		 * Search by code + extension.
		 */
		CODE_EXTENSION,
		/**
		 * Search by code range.
		 */
		CODE_RANGE
	}

	/**
	 * Search option for code search.
	 */
	public enum SearchOption {
		/**
		 * Search by begin with a code.
		 */
		BEGIN_WITH,
		/**
		 * Search by exact match a code.
		 */
		EXACT_MATCH
	}

	public enum GetDescCodeType {
		/**
		 * ICD9Dx.
		 */
		ICD9Dx("ICD9Dx"),
		/**
		 * ICD9Px.
		 */
		ICD9Px("ICD9Px");

		private final String codeType;

		GetDescCodeType(String codeType) {
			this.codeType = codeType;
		}

		public String codeType() {
			return codeType;
		}

		@Override
		public String toString() {
			return codeType;
		}

		/**
		 * Returns the enum constant of this type with the specified code type string.
		 * 
		 * @param codeType code type string.
		 * @return the enum constant with the specified code type string.
		 */
		public static GetDescCodeType getByCodeType(String codeType) {
			GetDescCodeType correspondingCodeType = null;

			for (GetDescCodeType codeTypeConstant : values()) {
				if (codeTypeConstant.codeType().equals(codeType)) {
					correspondingCodeType = codeTypeConstant;
					break;
				}
			}

			return correspondingCodeType;
		}
	}

	public enum RefTerm {
		/**
		 * HA
		 */
		HA
	}

	public enum RequestSystem {
		/**
		 * CMS_CORE
		 */
		CMS_CORE,
		/**
		 * FM
		 */
		FM,
		/**
		 * OT
		 */
		OT,
		/**
		 * ERS
		 */
		ERS;
	}
}
