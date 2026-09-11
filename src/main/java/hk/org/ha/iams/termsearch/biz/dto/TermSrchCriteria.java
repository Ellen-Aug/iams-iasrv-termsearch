/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : TermSrchCriteria.java
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

import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.CodeType;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.RefTerm;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.RequestSystem;

/**
 * <p>There are a lot of input criteria required in the generic search service. The mandatory input parameters include:</p>
 * <table cellspacing="2" cellpadding="2" border="0" summary="Generic search mandatory parameter">
 *   <tr>
 *     <td style="padding-left: 15px;"><b>Parameter</b></td>
 *     <td style="padding-left: 15px;"><b>Corresponding Method</b></td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Request System</td>
 *     <td style="padding-left: 15px;">setIn_requestSystem</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Keyword Section*</td>
 *     <td style="padding-left: 15px;">setIn_keyword</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Code Section*</td>
 *     <td style="padding-left: 15px;">setIn_termCodes</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Term ID Section*</td>
 *     <td style="padding-left: 15px;">setIn_termIds</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Natures</td>
 *     <td style="padding-left: 15px;">setIn_natures</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Output Code Type</td>
 *     <td style="padding-left: 15px;">setOut_codeTypes</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Output Reference Terminology Type</td>
 *     <td style="padding-left: 15px;">setOut_refTerm</td>
 *   </tr>
 * </table>
 * <p>* If all keyword, term code and term id are inputted, it will process the keyword search only. For those parameters which data type is boolean, it will default to be false for handling. Service Exception will be thrown if any mandatory fields are not inputted.</p>
 * <p>If both system used and exclude system used are present, only system used will be act as criteria.</p>
 * <p>Besides the input parameters, there are some output control parameters:</p>
 * <table cellspacing="2" cellpadding="2" border="0" summary="Optional parameters">
 *   <tr>
 *     <td style="padding-left: 15px;"><b>Parameter</b></td>
 *     <td style="padding-left: 15px;"><b>Corresponding Method</b></td>
 *     <td style="padding-left: 15px;"><b>Information will be show when set</b></td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;" valign="top">IAMS Information</td>
 *     <td style="padding-left: 15px;" valign="top">setOut_iamsInfo</td>
 *     <td style="padding-left: 15px;" valign="top">Remarks<br>Requested By<br>Requested Hospital<br>Created By<br>Create Date/Time<br>Updated By<br>Update Date/Time</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;" valign="top">Alias</td>
 *     <td style="padding-left: 15px;" valign="top">setOut_alias</td>
 *     <td style="padding-left: 15px;" valign="top">Alias Section (Multiple):<br>Alias ID<br>Alias Description</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;" valign="top">Associated Code</td>
 *     <td style="padding-left: 15px;" valign="top">setOut_associatedCode</td>
 *     <td style="padding-left: 15px;" valign="top">Associated Code of ICD9Dx, ICD9Px and ICD102010MBD Code</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;" valign="top">Associated Entity</td>
 *     <td style="padding-left: 15px;" valign="top">setOut_associatedEntity</td>
 *     <td style="padding-left: 15px;" valign="top">Associated Entity Section (Multiple):<br>Entity Id<br>Entity Description (HA)<br>Entity Description (ePR)</td>
 *   </tr>
 * </table>
 * <p>For the detail of return result structure, please see {@link hk.org.ha.iams.termsearch.biz.dto.TermSrchRecord}</p>
 */

public class TermSrchCriteria implements Serializable {

	private static final long serialVersionUID = 6975313244391231835L;

	private List<TermSrchCode> in_termCodes;
	private List<Integer> in_termIds;
	private String in_keyword;
	private List<CodeType> in_keywordCodeTypes;
	private List<String> in_natures;
	private String in_status;
	private boolean in_inActiveTerm_associatedOverride;
	private boolean in_CDF_OverrideIndicator;
	private List<String> in_CDF_Specialties;
	private boolean in_Top40_OverrideIndicator;
	private List<String> in_Top40_Specialties;
	private List<String> in_systemUseds;
	private List<String> in_excludeSystemUseds;
	private String in_requestHospitalCode;
	private String in_requestKey;

	private RequestSystem in_requestSystem;

	private RefTerm out_refTerm;
	private List<String> out_codeTypes;
	private boolean out_associatedCode;
	private boolean out_iamsInfo;
	private boolean out_alias;
	private boolean out_associatedEntity;
	private Integer out_recordsLimit;

	/**
	 * Constructs a new TermSrchCriteria object.
	 */
	public TermSrchCriteria() {
		super();
	}

	/**
	 * Sets the term codes criteria for term codes search.
	 * 
	 * @param in_termCodes term codes criteria.
	 */
	public void setIn_termCodes(List<TermSrchCode> in_termCodes) {
		this.in_termCodes = in_termCodes;
	}

	/**
	 * Gets the term codes criteria for term codes search.
	 * 
	 * @return the given term codes criteria.
	 */
	public List<TermSrchCode> getIn_termCodes() {
		return in_termCodes;
	}

	/**
	 * Sets the term ids criteria for term ids search.
	 * 
	 * @param in_termIds the term ids criteria.
	 */
	public void setIn_termIds(List<Integer> in_termIds) {
		this.in_termIds = in_termIds;
	}

	/**
	 * Gets the term ids criteria.
	 * 
	 * @return the given term ids criteria.
	 */
	public List<Integer> getIn_termIds() {
		return in_termIds;
	}

	/**
	 * Sets the keyword, keyword code type and output records limit for keyword search criteria.
	 * 
	 * @param in_keyword keyword.
	 * @param in_keywordCodeTypes keyword code type.
	 * @param out_recordsLimit records limit. If the value is 0 or null, it will consider as unlimited.
	 */
	public void setIn_keyword(String in_keyword, List<CodeType> in_keywordCodeTypes, Integer out_recordsLimit) {
		this.in_keyword = in_keyword;
		this.in_keywordCodeTypes = in_keywordCodeTypes;
		this.out_recordsLimit = out_recordsLimit;
	}

	/**
	 * Gets keyword.
	 * 
	 * @return the given keyword.
	 */
	public String getIn_keyword() {
		return in_keyword;
	}

	/**
	 * Gets keyword code type.
	 * 
	 * @return the given keyword code type.
	 */
	public List<CodeType> getIn_keywordCodeTypes() {
		return in_keywordCodeTypes;
	}

	/**
	 * Gets records limit.
	 * 
	 * @return the given records limit.
	 */
	public Integer getOut_recordsLimit() {
		return out_recordsLimit;
	}

	/**
	 * Sets terminology natures.
	 * 
	 * @param in_natures the given terminology natures. Example: "Dx", "Px", "Organism", "Lab. Test", "Alert" and "Specimen".
	 */
	public void setIn_natures(List<String> in_natures) {
		this.in_natures = in_natures;
	}

	/**
	 * Gets terminology natures.
	 * 
	 * @return the terminology natures.
	 */
	public List<String> getIn_natures() {
		return in_natures;
	}

	/**
	 * Sets terminology status.
	 * 
	 * @param in_status terminology status. Possible value: "All", "A" and "I".
	 */
	public void setIn_status(String in_status) {
		this.in_status = in_status;
	}

	/**
	 * Gets terminology status.
	 * 
	 * @return the given terminology status.
	 */
	public String getIn_status() {
		return in_status;
	}

	/**
	 * Specifies whether or not the result should includes the inactive term which containing a code that is associated with other active term when searching criteria status is set to ACTIVE.
	 * 
	 * @param in_inActiveTerm_associatedOverride true if the checking is to be turned on; false if it is to be turned off.
	 */
	public void setIn_inActiveTerm_associatedOverride(boolean in_inActiveTerm_associatedOverride) {
		this.in_inActiveTerm_associatedOverride = in_inActiveTerm_associatedOverride;
	}

	/**
	 * Tells whether the inactive term checking is to be turned on.
	 * 
	 * @return true if the inactive term checking is turned on; false otherwise.
	 */
	public boolean isIn_inActiveTerm_associatedOverride() {
		return in_inActiveTerm_associatedOverride;
	}

	/**
	 * Specifies whether or not the CDF override is to be turned on.
	 * 
	 * @param in_CDF_OverrideIndicator true if the override is to be turned on; false if it is to be turned off.
	 * @param in_CDF_Specialties list of specialties for bypass CDF override.
	 */
	public void setIn_CDF_OverrideIndicator(boolean in_CDF_OverrideIndicator, List<String> in_CDF_Specialties) {
		this.in_CDF_OverrideIndicator = in_CDF_OverrideIndicator;
		this.in_CDF_Specialties = in_CDF_Specialties;
	}

	/**
	 * Tells whether CDF override is to be turned on.
	 * 
	 * @return true if the CDF override is turned on; false otherwise.
	 */
	public boolean isIn_CDF_OverrideIndicator() {
		return in_CDF_OverrideIndicator;
	}

	/**
	 * Gets list of specialties for bypass CDF override.
	 * 
	 * @return list of specialties for bypass CDF override.
	 */
	public List<String> getIn_CDF_Specialties() {
		return in_CDF_Specialties;
	}

	/**
	 * Specifies whether or not the TOP40 override is to be turned on.
	 * 
	 * @param in_Top40_OverrideIndicator true if the override is to be turned on; false if it is to be turned off.
	 * @param in_Top40_Specialties list of specialties for bypass Top40 override.
	 */
	public void setIn_Top40_OverrideIndicator(boolean in_Top40_OverrideIndicator, List<String> in_Top40_Specialties) {
		this.in_Top40_OverrideIndicator = in_Top40_OverrideIndicator;
		this.in_Top40_Specialties = in_Top40_Specialties;
	}

	/**
	 * Tells whether TOP40 override is to be turned on.
	 * 
	 * @return true if the TOP40 override is turned on; false otherwise.
	 */
	public boolean isIn_Top40_OverrideIndicator() {
		return in_Top40_OverrideIndicator;
	}

	/**
	 * Gets list of specialties for bypass Top40 override.
	 * 
	 * @return list of specialties for bypass Top40 override.
	 */
	public List<String> getIn_Top40_Specialties() {
		return in_Top40_Specialties;
	}

	/**
	 * Sets what the systems should be included.
	 * 
	 * @param in_systemUseds the given systems should be included. Possible value: "PSY", "OBS" and "NNF".
	 */
	public void setIn_systemUseds(List<String> in_systemUseds) {
		this.in_systemUseds = in_systemUseds;
	}

	/**
	 * Gets what the systems should be included.
	 * 
	 * @return the systems should be included.
	 */
	public List<String> getIn_systemUseds() {
		return in_systemUseds;
	}

	/**
	 * Sets what the systems should be excluded.
	 * 
	 * @param in_excludeSystemUseds the given systems should be excluded. Possible value: "PSY", "OBS" and "NNF".
	 */
	public void setIn_excludeSystemUseds(List<String> in_excludeSystemUseds) {
		this.in_excludeSystemUseds = in_excludeSystemUseds;
	}

	/**
	 * Gets what the systems should be excluded.
	 * 
	 * @return the systems should be excluded.
	 */
	public List<String> getIn_excludeSystemUseds() {
		return in_excludeSystemUseds;
	}

	/**
	 * Sets request hospital code.
	 * 
	 * @param in_requestHospitalCode request hospital code. "All" and null value for search all hospital.
	 */
	public void setIn_requestHospitalCode(String in_requestHospitalCode) {
		this.in_requestHospitalCode = in_requestHospitalCode;
	}

	/**
	 * Gets request hospital code.
	 * 
	 * @return the given request hospital code.
	 */
	public String getIn_requestHospitalCode() {
		return in_requestHospitalCode;
	}

	/**
	 * Sets reference terminology type.
	 * 
	 * @param out_refTerm reference terminology type.
	 */
	public void setOut_refTerm(RefTerm out_refTerm) {
		this.out_refTerm = out_refTerm;
	}

	/**
	 * Gets reference terminology type.
	 * 
	 * @return the given reference terminology type.
	 */
	public RefTerm getOut_refTerm() {
		return out_refTerm;
	}

	/**
	 * Sets which types of code should be include in the search result. Possible value: "ICD9Dx", "ICD9Px", "ICD102010MBD" and "SCT".
	 * 
	 * @param out_codeTypes types of code.
	 */
	public void setOut_codeTypes(List<String> out_codeTypes) {
		this.out_codeTypes = out_codeTypes;
	}

	/**
	 * Gets the types of code which will be include in the search result.
	 * 
	 * @return the given types of code.
	 */
	public List<String> getOut_codeTypes() {
		return out_codeTypes;
	}

	/**
	 * Specifies whether or not the associated code is include in the search result.
	 * 
	 * @param out_associatedCode true if the associated code is include in the search result; false otherwise.
	 */
	public void setOut_associatedCode(boolean out_associatedCode) {
		this.out_associatedCode = out_associatedCode;
	}

	/**
	 * Tells whether the associated code is include in the search result.
	 * 
	 * @return true if the associated code is include in the search result; false otherwise.
	 */
	public boolean isOut_associatedCode() {
		return out_associatedCode;
	}

	/**
	 * Specifies whether or not the IAMS information is include in the search result.
	 * 
	 * @param out_iamsInfo true if the IAMS information is include in the search result; false otherwise.
	 */
	public void setOut_iamsInfo(boolean out_iamsInfo) {
		this.out_iamsInfo = out_iamsInfo;
	}

	/**
	 * Tells whether the IAMS information is include in the search result.
	 * 
	 * @return true if the IAMS information is include in the search result; false otherwise.
	 */
	public boolean isOut_iamsInfo() {
		return out_iamsInfo;
	}

	/**
	 * Specifies whether or not the IAMS aliases is include in the search result.
	 * 
	 * @param out_alias true if the IAMS aliases is include in the search result; false otherwise.
	 */
	public void setOut_alias(boolean out_alias) {
		this.out_alias = out_alias;
	}

	/**
	 * Tells whether the IAMS aliases is include in the search result.
	 * 
	 * @return true if the IAMS aliases is include in the search result; false otherwise.
	 */
	public boolean isOut_alias() {
		return out_alias;
	}

	/**
	 * Specifies whether or not the associated entity is include in the search result.
	 * 
	 * @param out_associatedEntity true if the associated entity is include in the search result; false otherwise.
	 */
	public void setOut_associatedEntity(boolean out_associatedEntity) {
		this.out_associatedEntity = out_associatedEntity;
	}

	/**
	 * Tells whether the associated entity is include in the search result.
	 * 
	 * @return true if the associated entity is include in the search result; false otherwise.
	 */
	public boolean isOut_associatedEntity() {
		return out_associatedEntity;
	}

	/**
	 * Sets request key.
	 * 
	 * @param in_requestKey request key.
	 */
	public void setIn_requestKey(String in_requestKey) {
		this.in_requestKey = in_requestKey;
	}

	/**
	 * Gets request key.
	 * 
	 * @return the given request key.
	 */
	public String getIn_requestKey() {
		return in_requestKey;
	}

	/**
	 * Gets request system in searching.
	 * 
	 * @return the given request system.
	 */
	public RequestSystem getIn_requestSystem() {
		return in_requestSystem;
	}

	/**
	 * Sets request system in searching.
	 * 
	 * @param in_requestSystem request system.
	 */
	public void setIn_requestSystem(RequestSystem in_requestSystem) {
		this.in_requestSystem = in_requestSystem;
	}


}
