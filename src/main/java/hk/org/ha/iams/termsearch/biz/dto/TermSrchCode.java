/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : TermSrchCode.java
 *
 *********************************************************************
 * VERSION         : 1.0.0
 * REF NO          : 
 * DATE CREATED    : May 21, 2012
 * CREATED BY      : Kenneth Pang, HAITS AP(AI3)1
 *********************************************************************
 */
package hk.org.ha.iams.termsearch.biz.dto;

import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.CodeType;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.SearchOption;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.SearchType;

import java.io.Serializable;

public class TermSrchCode implements Serializable {


	private static final long serialVersionUID = -5446313705257907279L;
	private CodeType codeType;
	private SearchType searchType;
	private SearchOption searchOption;
	private String code;
	private Integer extension;
	private String codeFrom;
	private String codeTo;

	/**
	 * Constructs a new TermSrchCode for code search by code type, search option and code.
	 * 
	 * @param codeType code type.
	 * @param searchOption search option.
	 * @param code code.
	 */
	public TermSrchCode(CodeType codeType, SearchOption searchOption, String code) {
		this.codeType = codeType;
		this.searchType = SearchType.CODE;
		this.searchOption = searchOption;
		this.code = code;
	}

	/**
	 * Constructs a new TermSrchCode for code + extension search by code type, code and extension.
	 * 
	 * @param codeType code type.
	 * @param code code.
	 * @param extension extension.
	 */
	public TermSrchCode(CodeType codeType, String code, Integer extension) {
		this.codeType = codeType;
		this.searchType = SearchType.CODE_EXTENSION;
		this.searchOption = SearchOption.EXACT_MATCH;
		this.code = code;
		this.extension = extension;
	}

	/**
	 * Constructs a new TermSrchCode for code range search by code type, beginning code and ending code.
	 * 
	 * @param codeType code type.
	 * @param codeFrom beginning code.
	 * @param codeTo ending code.
	 */
	public TermSrchCode(CodeType codeType, String codeFrom, String codeTo) {
		this.codeType = codeType;
		this.searchType = SearchType.CODE_RANGE;
		this.searchOption = SearchOption.BEGIN_WITH;
		this.codeFrom = codeFrom;
		this.codeTo = codeTo;
	}

	/**
	 * Gets code type.
	 * 
	 * @return the given code type.
	 */
	public CodeType getCodeType() {
		return codeType;
	}

	/**
	 * Gets search type.
	 * 
	 * @return the search type defined by constructor.
	 */
	public SearchType getSearchType() {
		return searchType;
	}

	/**
	 * Gets search option.
	 * 
	 * @return the given search option.
	 */
	public SearchOption getSearchOption() {
		return searchOption;
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
	 * Gets extension.
	 * 
	 * @return the given extension.
	 */
	public Integer getExtension() {
		return extension;
	}

	/**
	 * Get ending code.
	 * 
	 * @return the given ending code.
	 */
	public String getCodeFrom() {
		return codeFrom;
	}

	/**
	 * Gets beginning code.
	 * 
	 * @return the given beginning code.
	 */
	public String getCodeTo() {
		return codeTo;
	}
}
