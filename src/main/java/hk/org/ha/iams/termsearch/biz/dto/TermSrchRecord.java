/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : TermSrchRecord.java
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
import java.util.Date;
import java.util.List;


/**
 * Terminology Search Record
 * 
 * <p>The following output fields are mandatory in every searched term which must not be empty or null.</p>
 * <table cellspacing="2" cellpadding="2" border="0" summary="Terminology search record mandatory fields">
 *   <tr>
 *     <td style="padding-left: 15px;"><b>Field</b></td>
 *     <td style="padding-left: 15px;"><b>Corresponding Method</b></td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Display Sequence</td>
 *     <td style="padding-left: 15px;">getDisplaySeq</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Full Description</td>
 *     <td style="padding-left: 15px;">getFullDesc</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Status</td>
 *     <td style="padding-left: 15px;">getStatus</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Term ID</td>
 *     <td style="padding-left: 15px;">getTermId</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Exact Match Indicator</td>
 *     <td style="padding-left: 15px;">isExactMatch</td>
 *   </tr>
 * </table>
 * <p>Other fields can be NULL.</p>
 * <p>Optional Data Group:</p>
 * <p>IAMS Information:</p>
 * <table cellspacing="2" cellpadding="2" border="0" summary="Optional data group">
 *   <tr>
 *     <td style="padding-left: 15px;"><b>Field</b></td>
 *     <td style="padding-left: 15px;"><b>Corresponding Method</b></td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Remarks</td>
 *     <td style="padding-left: 15px;">getRemarks</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Requested By</td>
 *     <td style="padding-left: 15px;">getRequestUser</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Requested Hospital</td>
 *     <td style="padding-left: 15px;">getRequestHospCd</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Created By</td>
 *     <td style="padding-left: 15px;">getCreateUser</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Created Date/Time</td>
 *     <td style="padding-left: 15px;">getCreateDtm</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Updated By</td>
 *     <td style="padding-left: 15px;">getUpdateUser</td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">Updated Date/Time</td>
 *     <td style="padding-left: 15px;">getUpdateDtm</td>
 *   </tr>
 * </table>
 * <p>Alias:</p>
 * <table cellspacing="2" cellpadding="2" border="0" summary="Alias">
 *   <tr>
 *     <td style="padding-left: 15px;"><b>Field</b></td>
 *     <td style="padding-left: 15px;"><b>Corresponding Method</b></td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">{@link hk.org.ha.iams.termsearch.biz.dto.Alias}</td>
 *     <td style="padding-left: 15px;">getAliases</td>
 *   </tr>
 * </table>
 * <p>Associated Entity:</p>
 * <table cellspacing="2" cellpadding="2" border="0" summary="Associated entity">
 *   <tr>
 *     <td style="padding-left: 15px;"><b>Field</b></td>
 *     <td style="padding-left: 15px;"><b>Corresponding Method</b></td>
 *   </tr>
 *   <tr>
 *     <td style="padding-left: 15px;">{@link hk.org.ha.iams.termsearch.biz.dto.AssoEntity}</td>
 *     <td style="padding-left: 15px;">getAssoEntities</td>
 *   </tr>
 * </table>
 */
public class TermSrchRecord implements Serializable {

	private static final long serialVersionUID = -2810810545190957418L;

	private Integer displaySeq;
	private Integer termId;

	private Integer clinicalDataId;
	private String diagOrProc;
	private String nature;
	private boolean exactMatch;
	private String fullDesc;
	private String shortDesc;
	private String status;
	private String sex;
	private String age;
	private String principalCdFlag;
	private List<String> sysUseds;
	private String remarks;
	private String requestUser;
	private String requestHospCd;

	private String createUser;
	private Date createDtm;    
	private String updateUser;
	private Date updateDtm;    

	private List<Alias> aliases;   
	private List<AssoEntity> assoEntities;
	private ICD9Dx icd9Dx;
	private ICD9Px icd9Px;
	//private ICD10Dx icd10Dx;
	private ICD102010MBD icd102010MBD;
	private List<Snomedct> snomedcts;
	private ICPC2 icpc2;

	/**
	 * Constructs a new TermSrchRecord object.
	 */
	public TermSrchRecord() {
		super();
	}

	/**
	 * Sets display sequence.
	 * 
	 * @param displaySeq display sequence.
	 */
	public void setDisplaySeq(Integer displaySeq) {
		this.displaySeq = displaySeq;
	}

	/**
	 * Gets display sequence.
	 * 
	 * @return the given display sequence.
	 */
	public Integer getDisplaySeq() {
		return displaySeq;
	}

	/**
	 * Sets Term ID.
	 * 
	 * @param termId Term ID.
	 */
	public void setTermId(Integer termId) {
		this.termId = termId;
	}

	/**
	 * Gets Term ID.
	 * 
	 * @return the given Term ID.
	 */
	public Integer getTermId() {
		return termId;
	}

	/**
	 * Sets clinical data ID.
	 * 
	 * @param clinicalDataId clinical data ID.
	 */
	public void setClinicalDataId(Integer clinicalDataId) {
		this.clinicalDataId = clinicalDataId;
	}

	/**
	 * Gets clinical data ID.
	 * 
	 * @return the given clinical data ID.
	 */
	public Integer getClinicalDataId() {
		return clinicalDataId;
	}

	/**
	 * Sets diag/proc indicator.
	 * 
	 * @param diagOrProc diag/proc indicator.
	 */
	public void setDiagOrProc(String diagOrProc) {
		this.diagOrProc = diagOrProc;
	}

	/**
	 * Gets diag/proc indicator.
	 * 
	 * @return the given diag/proc indicator.
	 */
	public String getDiagOrProc() {
		return diagOrProc;
	}

	/**
	 * Sets exact match flag.
	 * 
	 * @param exactMatch exact match flag.
	 */
	public void setExactMatch(boolean exactMatch) {
		this.exactMatch = exactMatch;
	}

	/**
	 * Gets exact match flag.
	 * 
	 * @return the given exact match flag.
	 */
	public boolean isExactMatch() {
		return exactMatch;
	}

	/**
	 * Sets terminology nature.
	 * 
	 * @param nature terminology nature.
	 */
	public void setNature(String nature) {
		this.nature = nature;
	}

	/**
	 * Gets terminology nature. Possible value: "Dx", "Px", "Organism", "AntiB" and "Alert".
	 * 
	 * @return the given terminology nature.
	 */
	public String getNature() {
		return nature;
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
	 * Sets short description.
	 * 
	 * @param shortDesc short description.
	 */
	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	/**
	 * Gets short description.
	 * 
	 * @return the given short description.
	 */
	public String getShortDesc() {
		return shortDesc;
	}

	/**
	 * Sets terminology status.
	 * 
	 * @param status terminology status.
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * Gets terminology status. Possible value: "A" and "I".
	 * 
	 * @return the given terminology status.
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * Sets sex validation flag.
	 * 
	 * @param sex sex validation flag.
	 */
	public void setSex(String sex) {
		this.sex = sex;
	}

	/**
	 * Gets sex validation flag. Possible value: "M" and "F".
	 * 
	 * @return the given sex validation flag.
	 */
	public String getSex() {
		return sex;
	}

	/**
	 * Sets age validation flag.
	 * 
	 * @param age Age
	 */
	public void setAge(String age) {
		this.age = age;
	}

	/**
	 * Gets age validation flag.
	 * <p>Possible value:</p>
	 * <table cellspacing="2" cellpadding="2" border="0" summary="Age possible value">
	 *   <tr>
	 *     <td><b>Value</b></td>
	 *     <td><b>Description</b></td>
	 *   </tr>
	 *   <tr>
	 *     <td>B</td>
	 *     <td>< 1 year</td>
	 *   </tr>
	 *   <tr>
	 *     <td>P</td>
	 *     <td>< 18 years</td>
	 *   </tr>
	 *   <tr>
	 *     <td>M</td>
	 *     <td>12 - 55 years</td>
	 *   </tr>
	 *   <tr>
	 *     <td>A</td>
	 *     <td>> 14 years</td>
	 *   </tr>
	 * </table>
	 * 
	 * @return the given age validation flag.
	 */
	public String getAge() {
		return age;
	}

	/**
	 * Sets principal code flag.
	 * 
	 * @param principalCdFlag principal code flag.
	 */
	public void setPrincipalCdFlag(String principalCdFlag) {
		this.principalCdFlag = principalCdFlag;
	}

	/**
	 * Gets principal code flag. Possible value: "Y" and "N".
	 * 
	 * @return the given principal code flag.
	 */
	public String getPrincipalCdFlag() {
		return principalCdFlag;
	}

	/**
	 * Sets a list of system used.
	 * 
	 * @param sysUseds System used
	 */
	public void setSysUseds(List<String> sysUseds) {
		this.sysUseds = sysUseds;
	}

	/**
	 * Gets a list of system used. Possible value: "PSY", "OBS" and "NNF"
	 * 
	 * @return the given list of system used.
	 */
	public List<String> getSysUseds() {
		return sysUseds;
	}

	/**
	 * Sets remarks.
	 * 
	 * @param remarks remarks.
	 */
	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	/**
	 * Gets remarks.
	 * 
	 * @return the given remarks.
	 */
	public String getRemarks() {
		return remarks;
	}

	/**
	 * Sets request user.
	 * 
	 * @param requestUser request user.
	 */
	public void setRequestUser(String requestUser) {
		this.requestUser = requestUser;
	}

	/**
	 * Gets request user.
	 * 
	 * @return the given request user.
	 */
	public String getRequestUser() {
		return requestUser;
	}

	/**
	 * Sets request hospital code.
	 * 
	 * @param requestHospCd request hospital code.
	 */
	public void setRequestHospCd(String requestHospCd) {
		this.requestHospCd = requestHospCd;
	}

	/**
	 * Gets request hospital code.
	 * 
	 * @return the given request hospital code.
	 */
	public String getRequestHospCd() {
		return requestHospCd;
	}

	/**
	 * Sets terminology create user.
	 * 
	 * @param createUser terminology create user.
	 */
	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	/**
	 * Gets terminology create user.
	 * 
	 * @return the given terminology create user.
	 */
	public String getCreateUser() {
		return createUser;
	}

	/**
	 * Sets terminology create date.
	 * 
	 * @param createDtm terminology create date.
	 */
	public void setCreateDtm(Date createDtm) {
		this.createDtm = createDtm;
	}

	/**
	 * Gets terminology create date.
	 * 
	 * @return the given terminology create date.
	 */
	public Date getCreateDtm() {
		return createDtm;
	}

	/**
	 * Sets update user.
	 * 
	 * @param updateUser update user.
	 */
	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	/**
	 * Gets update user.
	 * 
	 * @return the given update user.
	 */
	public String getUpdateUser() {
		return updateUser;
	}

	/**
	 * Sets update date.
	 * 
	 * @param updateDtm update date.
	 */
	public void setUpdateDtm(Date updateDtm) {
		this.updateDtm = updateDtm;
	}

	/**
	 * Gets update date.
	 * 
	 * @return the given update date.
	 */
	public Date getUpdateDtm() {
		return updateDtm;
	}

	/**
	 * Sets a list of alias objects.
	 * 
	 * @param aliases a list of alias objects.
	 */
	public void setAliases(List<Alias> aliases) {
		this.aliases = aliases;
	}

	/**
	 * Gets a list of alias objects.
	 * 
	 * @return the given list of alias objects.
	 */
	public List<Alias> getAliases() {
		return aliases;
	}

	/**
	 * Sets a list of associated entity objects.
	 * 
	 * @param assoEntities a list of associated entity objects.
	 */
	public void setAssoEntities(List<AssoEntity> assoEntities) {
		this.assoEntities = assoEntities;
	}

	/**
	 * Gets a list of associated entity objects.
	 * 
	 * @return the given list of associated entity objects.
	 */
	public List<AssoEntity> getAssoEntities() {
		return assoEntities;
	}

	/**
	 * Sets ICD9Dx object.
	 * 
	 * @param icd9Dx ICD9Dx object.
	 */
	public void setIcd9Dx(ICD9Dx icd9Dx) {
		this.icd9Dx = icd9Dx;
	}

	/**
	 * Gets ICD9Dx object.
	 * 
	 * @return the given ICD9Dx object.
	 */
	public ICD9Dx getIcd9Dx() {
		return icd9Dx;
	}

	/**
	 * Sets ICD9Px object.
	 * 
	 * @param icd9Px ICD9Px object.
	 */
	public void setIcd9Px(ICD9Px icd9Px) {
		this.icd9Px = icd9Px;
	}

	/**
	 * Gets ICD9Px object.
	 * 
	 * @return the given ICD9Px object.
	 */
	public ICD9Px getIcd9Px() {
		return icd9Px;
	}

	//	 /**
	//	  * Sets ICD10Dx object.
	//	  * 
	//	  * @param icd10Dx ICD10Dx object.
	//	  */
	//	 public void setIcd10Dx(ICD10Dx icd10Dx) {
	//		 this.icd10Dx = icd10Dx;
	//	 }
	//
	//	 /**
	//	  * Gets ICD10Dx object.
	//	  * 
	//	  * @return the given ICD10Dx object.
	//	  */
	//	 public ICD10Dx getIcd10Dx() {
	//		 return icd10Dx;
	//	 }

	/**
	 * Gets ICD102010MBD object.
	 * 
	 * @return the given ICD102010MBD object.
	 */
	public ICD102010MBD getIcd102010MBD() {
		return icd102010MBD;
	}

	/**
	 * Sets ICD102010MBD object.
	 * 
	 * @param icd102010MBD ICD102010MBD object.
	 */
	public void setIcd102010MBD(ICD102010MBD icd102010MBD) {
		this.icd102010MBD = icd102010MBD;
	}

	/**
	 * Sets a list of SNOMED CT objects.
	 * 
	 * @param snomedcts a list of SNOMED CT objects.
	 */
	public void setSnomedcts(List<Snomedct> snomedcts) {
		this.snomedcts = snomedcts;
	}


	/**
	 * Gets a list of SNOMED CT objects.
	 * 
	 * @return the given list of SNOMED CT objects.
	 */
	public List<Snomedct> getSnomedcts() {
		return snomedcts;
	}

	/**
	 * Gets ICPC2 object.
	 * 
	 * @return the given icpc2 object.
	 */
	public ICPC2 getIcpc2() {
		return icpc2;
	}

	/**
	 * Sets ICPC2 object.
	 * 
	 * @param icpc2 ICPC2 object.
	 */
	public void setIcpc2(ICPC2 icpc2) {
		this.icpc2 = icpc2;
	}

}
