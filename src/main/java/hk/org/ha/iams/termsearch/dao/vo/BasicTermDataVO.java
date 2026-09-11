/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : BasicTermDataVO.java
 *
 *********************************************************************
 * VERSION         : 1.0.0
 * REF NO          : 
 * DATE CREATED    : May 21, 2012
 * CREATED BY      : Kenneth Pang, HAITS AP(AI3)1
 *********************************************************************
 */
package hk.org.ha.iams.termsearch.dao.vo;

import java.io.Serializable;
import java.util.Date;

public class BasicTermDataVO implements Serializable {

	private static final long serialVersionUID = -1742792656225971782L;

	private Integer termKey;
	private Integer termId;
	private String nature;
	private String fullDesc;
	private String shortDesc;
	private String status;
	private String sex;
	private String age;
	private String principalCdFlag;
	private Integer clinicalDataId;

	private Integer systemUsedCount;
	private Integer matchedAliasCount;

	// IAMS Info.
	private String remarks;
	private String requestUser;
	private String requestHospCd;
	private String createUser;
	private Date createDtm;
	private String updateUser;
	private Date updateDtm;

	// Code
	private String icd9dxCode;
	private Integer icd9dxExtension;
	private String icd9pxCode;
	private Integer icd9pxExtension;
	private String icd9pxWeight;
	//private String icd10dxCode;

	private String icd102010MBDCode;
	private String icpc2Code;

	// CDF Data
	private String cdfDataType;
	private String cdfDescription;
	private String cdfReferenceId;
	private String cdfSex;
	private String cdfAge;
	private String cdfStatus;
	private boolean cdfOverride;
	private String cdfNature;
	private Integer cdfAliasMatchedCount;

	//Code Summary
	private Integer snomedctCount;
	private Integer assoIcd9dxCount;
	private Integer assoIcd9pxCount;
	//private Integer assoIcd10dxCount;

	private Integer assoIcd102010MBDCount;
	private Integer assoIcpc2Count;

	private Integer sortingScore;
	private Integer exactMatch;

	public BasicTermDataVO() {
		super();

		sortingScore = 0;
	}

	public Integer getTermKey() {
		return termKey;
	}

	public void setTermKey(Integer termKey) {
		this.termKey = termKey;
	}

	public Integer getTermId() {
		return termId;
	}

	public void setTermId(Integer termId) {
		this.termId = termId;
	}

	public String getNature() {
		return nature;
	}

	public void setNature(String nature) {
		this.nature = nature;
	}

	public String getFullDesc() {
		return fullDesc;
	}

	public void setFullDesc(String fullDesc) {
		this.fullDesc = fullDesc;
	}

	public String getShortDesc() {
		return shortDesc;
	}

	public void setShortDesc(String shortDesc) {
		this.shortDesc = shortDesc;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getAge() {
		return age;
	}

	public void setAge(String age) {
		this.age = age;
	}

	public String getPrincipalCdFlag() {
		return principalCdFlag;
	}

	public void setPrincipalCdFlag(String principalCdFlag) {
		this.principalCdFlag = principalCdFlag;
	}

	public Integer getClinicalDataId() {
		return clinicalDataId;
	}

	public void setClinicalDataId(Integer clinicalDataId) {
		this.clinicalDataId = clinicalDataId;
	}

	public Integer getSystemUsedCount() {
		return systemUsedCount;
	}

	public void setSystemUsedCount(Integer systemUsedCount) {
		this.systemUsedCount = systemUsedCount;
	}

	public Integer getMatchedAliasCount() {
		return matchedAliasCount;
	}

	public void setMatchedAliasCount(Integer matchedAliasCount) {
		this.matchedAliasCount = matchedAliasCount;
	}

	public String getRemarks() {
		return remarks;
	}

	public void setRemarks(String remarks) {
		this.remarks = remarks;
	}

	public String getRequestUser() {
		return requestUser;
	}

	public void setRequestUser(String requestUser) {
		this.requestUser = requestUser;
	}

	public String getRequestHospCd() {
		return requestHospCd;
	}

	public void setRequestHospCd(String requestHospCd) {
		this.requestHospCd = requestHospCd;
	}

	public String getCreateUser() {
		return createUser;
	}

	public void setCreateUser(String createUser) {
		this.createUser = createUser;
	}

	public Date getCreateDtm() {
		return createDtm;
	}

	public void setCreateDtm(Date createDtm) {
		this.createDtm = createDtm;
	}

	public String getUpdateUser() {
		return updateUser;
	}

	public void setUpdateUser(String updateUser) {
		this.updateUser = updateUser;
	}

	public Date getUpdateDtm() {
		return updateDtm;
	}

	public void setUpdateDtm(Date updateDtm) {
		this.updateDtm = updateDtm;
	}

	public String getIcd9dxCode() {
		return icd9dxCode;
	}

	public void setIcd9dxCode(String icd9dxCode) {
		this.icd9dxCode = icd9dxCode;
	}

	public Integer getIcd9dxExtension() {
		return icd9dxExtension;
	}

	public void setIcd9dxExtension(Integer icd9dxExtension) {
		this.icd9dxExtension = icd9dxExtension;
	}

	public String getIcd9pxCode() {
		return icd9pxCode;
	}

	public void setIcd9pxCode(String icd9pxCode) {
		this.icd9pxCode = icd9pxCode;
	}

	public Integer getIcd9pxExtension() {
		return icd9pxExtension;
	}

	public void setIcd9pxExtension(Integer icd9pxExtension) {
		this.icd9pxExtension = icd9pxExtension;
	}

	public String getIcd9pxWeight() {
		return icd9pxWeight;
	}

	public void setIcd9pxWeight(String icd9pxWeight) {
		this.icd9pxWeight = icd9pxWeight;
	}

	/*	public String getIcd10dxCode() {
		return icd10dxCode;
	}

	public void setIcd10dxCode(String icd10dxCode) {
		this.icd10dxCode = icd10dxCode;
	}*/

	public String getCdfDataType() {
		return cdfDataType;
	}

	public String getIcd102010MBDCode() {
		return icd102010MBDCode;
	}

	public void setIcd102010MBDCode(String icd102010mbdCode) {
		icd102010MBDCode = icd102010mbdCode;
	}

	public String getIcpc2Code() {
		return icpc2Code;
	}

	public void setIcpc2Code(String icpc2Code) {
		this.icpc2Code = icpc2Code;
	}

	public void setCdfDataType(String cdfDataType) {
		this.cdfDataType = cdfDataType;
	}

	public String getCdfDescription() {
		return cdfDescription;
	}

	public void setCdfDescription(String cdfDescription) {
		this.cdfDescription = cdfDescription;
	}

	public String getCdfReferenceId() {
		return cdfReferenceId;
	}

	public void setCdfReferenceId(String cdfReferenceId) {
		this.cdfReferenceId = cdfReferenceId;
	}

	public String getCdfSex() {
		return cdfSex;
	}

	public void setCdfSex(String cdfSex) {
		this.cdfSex = cdfSex;
	}

	public String getCdfAge() {
		return cdfAge;
	}

	public void setCdfAge(String cdfAge) {
		this.cdfAge = cdfAge;
	}

	public String getCdfStatus() {
		return cdfStatus;
	}

	public void setCdfStatus(String cdfStatus) {
		this.cdfStatus = cdfStatus;
	}

	public boolean isCdfOverride() {
		return cdfOverride;
	}

	public void setCdfOverride(boolean cdfOverride) {
		this.cdfOverride = cdfOverride;
	}

	public String getCdfNature() {
		return cdfNature;
	}

	public void setCdfNature(String cdfNature) {
		this.cdfNature = cdfNature;
	}

	public Integer getCdfAliasMatchedCount() {
		return cdfAliasMatchedCount;
	}

	public void setCdfAliasMatchedCount(Integer cdfAliasMatchedCount) {
		this.cdfAliasMatchedCount = cdfAliasMatchedCount;
	}

	public Integer getSnomedctCount() {
		return snomedctCount;
	}

	public void setSnomedctCount(Integer snomedctCount) {
		this.snomedctCount = snomedctCount;
	}

	public Integer getAssoIcd9dxCount() {
		return assoIcd9dxCount;
	}

	public void setAssoIcd9dxCount(Integer assoIcd9dxCount) {
		this.assoIcd9dxCount = assoIcd9dxCount;
	}

	public Integer getAssoIcd9pxCount() {
		return assoIcd9pxCount;
	}

	public void setAssoIcd9pxCount(Integer assoIcd9pxCount) {
		this.assoIcd9pxCount = assoIcd9pxCount;
	}

	/*	public Integer getAssoIcd10dxCount() {
		return assoIcd10dxCount;
	}

	public void setAssoIcd10dxCount(Integer assoIcd10dxCount) {
		this.assoIcd10dxCount = assoIcd10dxCount;
	}*/

	public Integer getSortingScore() {
		return sortingScore;
	}

	public Integer getAssoIcd102010MBDCount() {
		return assoIcd102010MBDCount;
	}

	public void setAssoIcd102010MBDCount(Integer assoIcd102010MBDCount) {
		this.assoIcd102010MBDCount = assoIcd102010MBDCount;
	}

	public Integer getAssoIcpc2Count() {
		return assoIcpc2Count;
	}

	public void setAssoIcpc2Count(Integer assoIcpc2Count) {
		this.assoIcpc2Count = assoIcpc2Count;
	}

	public void setSortingScore(Integer sortingScore) {
		this.sortingScore = sortingScore;
	}

	public Integer getExactMatch() {
		return exactMatch;
	}

	public void setExactMatch(Integer exactMatch) {
		this.exactMatch = exactMatch;
	}
}
