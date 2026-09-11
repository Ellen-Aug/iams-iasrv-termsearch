/*********************************************************************
 * Copyright (c) Electronic Health Record & Hospital Authority 2010. 
 * All Rights Reserved.
 *********************************************************************
 * PROJECT NAME    : IAMS
 *
 * MODULE NAME     : IAMS Service Term
 *
 * PROGRAM NAME    : TermServiceDataAccessPOJO.java
 *
 *********************************************************************
 * VERSION         : 1.0.0
 * REF NO          : 
 * DATE CREATED    : May 21, 2012
 * CREATED BY      : Kenneth Pang, HAITS AP(AI3)1
 *********************************************************************
 */
package hk.org.ha.iams.termsearch.dao.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import hk.org.ha.iams.termsearch.dao.vo.LookupValueDTO;
import hk.org.ha.iams.termsearch.dao.LookupValueCache;
import hk.org.ha.iams.termsearch.util.CommonDictionaryUtils;
import hk.org.ha.iams.termsearch.exception.IamsDataAccessException;
import hk.org.ha.iams.termsearch.exception.IamsExceptionHelper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import hk.org.ha.iams.termsearch.util.TermCollections;
import hk.org.ha.iams.termsearch.util.TermDates;
import hk.org.ha.iams.termsearch.util.TermNumbers;
import hk.org.ha.iams.termsearch.util.TermStrings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import hk.org.ha.iams.termsearch.constant.TermServiceLookupValueConstants;
import hk.org.ha.iams.termsearch.constant.TermServiceMessages;
import hk.org.ha.iams.termsearch.dao.TermServiceDataAccess;
import hk.org.ha.iams.termsearch.dao.vo.BasicTermDataVO;
import hk.org.ha.iams.termsearch.biz.dto.Alias;
import hk.org.ha.iams.termsearch.biz.dto.AssoEntity;
import hk.org.ha.iams.termsearch.biz.dto.AssoTermCode;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescCode;
import hk.org.ha.iams.termsearch.biz.dto.Snomedct;
import hk.org.ha.iams.termsearch.biz.dto.TermDesc;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.CodeType;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.GetDescCodeType;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.RequestSystem;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.SearchOption;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.SearchType;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchCode;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchCriteria;
import hk.org.ha.iams.termsearch.exception.ServiceException;
import hk.org.ha.iams.termsearch.util.BasicTermDataComparator;
import hk.org.ha.iams.termsearch.util.BasicTermDataComparator.Nature;
import hk.org.ha.iams.termsearch.util.TermServiceUtils;
import hk.org.ha.iams.termsearch.validation.ReturnMessage.Error;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.stereotype.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Repository
public class TermServiceDataAccessPOJO implements TermServiceDataAccess {


	private static final Logger logger = LoggerFactory.getLogger(TermServiceDataAccessPOJO.class);
	private static final Character MAX_CHARACTER = (char)('z' + 1);

	private final NamedParameterJdbcTemplate jdbcTemplate;
	private final LookupValueCache lookupValueCache;

	public TermServiceDataAccessPOJO(ObjectProvider<DataSource> dataSources, LookupValueCache lookupValueCache) {
		DataSource dataSource = dataSources.getIfAvailable();
		this.jdbcTemplate = dataSource == null ? null : new NamedParameterJdbcTemplate(dataSource);
		this.lookupValueCache = lookupValueCache;
	}

	private boolean noDatabase() {
		return jdbcTemplate == null;
	}

	public NamedParameterJdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}

	@Override
	public List<TermDesc> getTermDescById(List<Integer> termIDs) throws IamsDataAccessException {
		List<TermDesc> termDescs = new ArrayList<TermDesc>();

		if (noDatabase() || termIDs == null) {
			if (termIDs != null) {
				for (Integer termId : termIDs) {
					TermDesc termDesc = new TermDesc();
					termDesc.setIsFound(false);
					termDesc.setTermId(termId);
					termDescs.add(termDesc);
				}
			}
			return termDescs;
		}

		try {

			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			Date startGetDescByIDDate = TermDates.getCurrentDate();
			logger.debug("I@@@@@@@@@@ Start getTermDescById at (" + startGetDescByIDDate + ")");        
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

			Map<Integer, TermDesc> sqlResults = new HashMap<Integer, TermDesc>();
			Map<String, Object> parameterMap = new HashMap<String, Object>();

			List<String> whereClauses = new ArrayList<String>();
			int i = 1;
			for (Integer termId : termIDs) {

				whereClauses.add("(cl.term_id = " +":termId_"+i  + " )");
				parameterMap.put("termId_"+i, termId);

				i+=1;
			}

			if (!TermCollections.isEmpty(whereClauses)) {
				StringBuilder sqlBuilder = new StringBuilder();

				sqlBuilder.append("SELECT c.term_id, c.full_desc, c.short_desc, cha.status ");
				sqlBuilder.append("FROM iams_concept c, iams_concept_latest cl, iams_concept_ha cha ");
				sqlBuilder.append("WHERE cha.term_key = c.term_key ");
				sqlBuilder.append("AND cl.term_key = c.term_key ");
				sqlBuilder.append("AND c.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED +" AND (");
				sqlBuilder.append(TermStrings.join(whereClauses, " OR "));
				sqlBuilder.append(")");

				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				logger.debug("I@@@@@@@@@@ SQL: " + sqlBuilder.toString());
				logger.debug("I@@@@@@@@@@ parameter: " + parameterMap);
				Date startSQLDate = TermDates.getCurrentDate();
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

				SqlRowSet rs = getJdbcTemplate().queryForRowSet(sqlBuilder.toString(), parameterMap);

				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				Date endSQLDate = TermDates.getCurrentDate();
				logger.debug("I@@@@@@@@@@ Total SQL time: " + (endSQLDate.getTime() - startSQLDate.getTime()));
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

				while (rs.next()) {
					TermDesc termDesc = new TermDesc();

					termDesc.setIsFound(true);
					termDesc.setTermId(rs.getInt("term_id"));
					termDesc.setFullDescription(rs.getString("full_desc"));
					termDesc.setShortDescription(rs.getString("short_desc"));
					termDesc.setStatus(lookupValueCache.getValueByValueKey(rs.getInt("status")).getDataValue());

					sqlResults.put(termDesc.getTermId(), termDesc);
				}
			}

			for (Integer termId : termIDs) {
				TermDesc termDesc = sqlResults.get(termId);

				if (termDesc == null) {
					termDesc = new TermDesc();

					termDesc.setIsFound(false);
					termDesc.setTermId(termId);
				}

				termDescs.add(termDesc);
			}

			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			Date endGetDescByIDDate = TermDates.getCurrentDate();
			logger.debug("I@@@@@@@@@@ Total getTermDescById time: " + (endGetDescByIDDate.getTime() - startGetDescByIDDate.getTime()));
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		}
		catch (Exception e) {
			IamsExceptionHelper.throwsIAMSException("Failure in getting term description by id", e);
		}

		return termDescs;
	}

	@Override
	public List<TermDesc> getTermDescByCode(List<GetTermDescCode> termCodes) throws ServiceException {
		List<TermDesc> termDescs = new ArrayList<TermDesc>();

		if (noDatabase() || termCodes == null) {
			if (termCodes != null) {
				for (GetTermDescCode termDescCode : termCodes) {
					TermDesc termDesc = new TermDesc();
					termDesc.setIsFound(false);
					termDesc.setCode(termDescCode.getCode());
					termDesc.setExtension(termDescCode.getExtension());
					termDesc.setCodeType(termDescCode.getCodeType());
					termDescs.add(termDesc);
				}
			}
			return termDescs;
		}

		try {
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			Date startGetDescByCodeDate = TermDates.getCurrentDate();
			logger.debug("C@@@@@@@@@@ Start getTermDescByCode at (" + startGetDescByCodeDate + ")");
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

			Map<String, TermDesc> sqlResults = new HashMap<String, TermDesc>();
			Map<String, Object> parameterMap = new HashMap<String, Object>();

			List<String> dxWhereClauses = new ArrayList<String>();
			List<String> pxWhereClauses = new ArrayList<String>();

			int i = 1;
			for (GetTermDescCode termDescCode : termCodes) {
				if (termDescCode.getCodeType().equals(GetDescCodeType.ICD9Dx) || termDescCode.getCodeType().equals(GetDescCodeType.ICD9Px)) {
					if (termDescCode.getCode() == null) {
						throw new ServiceException(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS.ordinal(), "Code is compulsory for code + extension search.");
					}
					else if (termDescCode.getExtension() == null) {
						throw new ServiceException(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS.ordinal(), "Extension is compulsory for code + extension search with code type ICD9Dx and ICD9Px.");
					}
					else {
						if (termDescCode.getCodeType().equals(GetDescCodeType.ICD9Dx)) {
							dxWhereClauses.add("(d.code = :dxCode_" + i + " AND d.extension = :dxExtension_" + i + " )");
							parameterMap.put("dxCode_"+i, termDescCode.getCode().toUpperCase());
							parameterMap.put("dxExtension_"+i, termDescCode.getExtension());
						}
						else if (termDescCode.getCodeType().equals(GetDescCodeType.ICD9Px)) {
							pxWhereClauses.add("(p.code = :pxCode_" + i + " AND p.extension = :pxExtension_" + i + ")");
							parameterMap.put("pxCode_"+i, termDescCode.getCode().toUpperCase());
							parameterMap.put("pxExtension_"+i, termDescCode.getExtension());
						}
					}

					i+=1;
				}
			}

			if (!TermCollections.isEmpty(dxWhereClauses) || !TermCollections.isEmpty(pxWhereClauses)) {
				StringBuilder sqlBuilder = new StringBuilder();

				if (!TermCollections.isEmpty(dxWhereClauses)) {
					sqlBuilder.append("SELECT c.term_id, c.full_desc, c.short_desc, cha.status, d.code, d.extension, 'D' AS code_type ");
					sqlBuilder.append("FROM iams_concept c, iams_concept_latest cl, iams_concept_ha cha, iams_concept_icd9dx d ");
					sqlBuilder.append("WHERE cha.term_key = c.term_key ");
					sqlBuilder.append("AND cl.term_key = c.term_key ");
					sqlBuilder.append("AND c.term_key = d.term_key AND c.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED + " AND (");
					sqlBuilder.append(TermStrings.join(dxWhereClauses, " OR "));
					sqlBuilder.append(")");
				}

				if (!TermCollections.isEmpty(pxWhereClauses)) {
					if (sqlBuilder.length() > 0) {
						sqlBuilder.append(" UNION ALL ");
					}

					sqlBuilder.append("SELECT c.term_id, c.full_desc, c.short_desc, cha.status, p.code, p.extension, 'P' AS code_type ");
					sqlBuilder.append("FROM iams_concept c, iams_concept_latest cl, iams_concept_ha cha, iams_concept_icd9px p ");
					sqlBuilder.append("WHERE cha.term_key = c.term_key ");
					sqlBuilder.append("AND cl.term_key = c.term_key ");
					sqlBuilder.append("AND c.term_key = p.term_key AND c.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED + " AND (");
					sqlBuilder.append(TermStrings.join(pxWhereClauses, " OR "));
					sqlBuilder.append(")");
				}

				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				logger.debug("C@@@@@@@@@@ SQL: " + sqlBuilder.toString());
				logger.debug("C@@@@@@@@@@ parameter: " + parameterMap);
				Date startSQLDate = TermDates.getCurrentDate();
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

				SqlRowSet rs = getJdbcTemplate().queryForRowSet(sqlBuilder.toString(), parameterMap);

				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				Date endSQLDate = TermDates.getCurrentDate();
				logger.debug("C@@@@@@@@@@ Total SQL time: " + (endSQLDate.getTime() - startSQLDate.getTime()));
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

				while (rs.next()) {
					TermDesc termDesc = new TermDesc();

					termDesc.setIsFound(true);
					termDesc.setTermId(rs.getInt("term_id"));
					termDesc.setCode(rs.getString("code"));
					termDesc.setExtension(rs.getInt("extension"));                
					termDesc.setFullDescription(rs.getString("full_desc"));
					termDesc.setShortDescription(rs.getString("short_desc"));
					termDesc.setStatus(lookupValueCache.getValueByValueKey(rs.getInt("status")).getDataValue());

					if (rs.getString("code_type").equals("D")) {
						termDesc.setCodeType(GetDescCodeType.ICD9Dx);
					}
					else if (rs.getString("code_type").equals("P")) {
						termDesc.setCodeType(GetDescCodeType.ICD9Px);
					}

					sqlResults.put(termDesc.getCodeType().codeType() + "-" + termDesc.getCode() + "-" + termDesc.getExtension(), termDesc);
				}

				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				Date endRSDate = TermDates.getCurrentDate();
				logger.debug("C@@@@@@@@@@ Total RS time: " + (endRSDate.getTime() - endSQLDate.getTime()));
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			}

			for (GetTermDescCode termDescCode : termCodes) {
				String tempKey = termDescCode.getCodeType().codeType() + "-" + termDescCode.getCode() + "-" + termDescCode.getExtension();
				TermDesc termDesc = sqlResults.get(tempKey);

				if (termDesc == null) {
					termDesc = new TermDesc();

					termDesc.setIsFound(false);
					termDesc.setCode(termDescCode.getCode());
					termDesc.setExtension(termDescCode.getExtension());
					termDesc.setCodeType(termDescCode.getCodeType());
				}

				termDescs.add(termDesc);
			}

			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			Date endGetDescByCodeDate = TermDates.getCurrentDate();
			logger.debug("C@@@@@@@@@@ Total getTermDescByCode time: " + (endGetDescByCodeDate.getTime() - startGetDescByCodeDate.getTime()));
			// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
		}
		catch (Exception e) {
			if (e instanceof ServiceException) {
				throw (ServiceException) e;
			}
			else {
				IamsExceptionHelper.throwsIAMSException("Failure in getting term description by code", e);
			}
		}

		return termDescs;

	}

	@Override
	public List<BasicTermDataVO> findTermBasicData(TermSrchCriteria criteria) throws ServiceException {
		List<BasicTermDataVO> basicTermDataList = new ArrayList<BasicTermDataVO>();

		if (noDatabase()) {
			return basicTermDataList;
		}

		try {
			boolean isKeywordSearch = false;
			boolean isCodeSearch = false;
			boolean hasKeyword = true;

			List<String> countFromClauseElements = new ArrayList<String>();
			List<String> countWhereClauseElements = new ArrayList<String>();

			List<String> selectClauseElements = new ArrayList<String>();
			List<String> fromClauseElements = new ArrayList<String>();
			List<String> whereClauseElements = new ArrayList<String>();

			Map<String , Object> parameterMap = new HashMap<String, Object>();
			selectClauseElements.add("c.term_key, c.term_id, c.nature, c.full_desc, c.short_desc, cha.status, c.sex, c.age, c.principal_cd_flag, (SELECT COUNT(suc.system_used) FROM iams_concept_ha_sys_used suc WHERE suc.term_key = cl.term_key) AS system_used_count");
			fromClauseElements.add("iams_concept_latest cl, iams_concept_ha cha ,iams_concept c");

			countFromClauseElements.add("iams_concept_latest cl, iams_concept_ha cha, iams_concept c ");

			// Optional fields
			if (criteria.isOut_iamsInfo()) {
				selectClauseElements.add("c.remarks, " +
						"c.request_user, "+
						"c.request_inst_cd, "+ 
						"(SELECT cu.name FROM iams_am_user cu WHERE cu.user_key = c.create_user) AS create_user, "+
						"CAST(c.create_dtm AS TIMESTAMP) AS create_dtm, " +
						//"c.create_dtm, " +
						"(SELECT uu.name FROM iams_am_user uu WHERE uu.user_key = c.update_user) AS update_user, " +
						"CAST(c.update_dtm AS TIMESTAMP) AS update_dtm");
				//"c.update_dtm");

			}

			// Code
			List<CodeType> outputCodeTypes = TermServiceUtils.getOutputCodeTypes(criteria);

			if (criteria.isIn_CDF_OverrideIndicator() || criteria.isIn_Top40_OverrideIndicator()) {
				if (!outputCodeTypes.contains(CodeType.ICD9Dx)) {
					outputCodeTypes.add(CodeType.ICD9Dx);
				}

				if (!outputCodeTypes.contains(CodeType.ICD9Px)) {
					outputCodeTypes.add(CodeType.ICD9Px);
				}
			}

			for (CodeType outputCodeType : outputCodeTypes) {
				if (outputCodeType.equals(CodeType.ICD9Dx)) {
					selectClauseElements.add("cicd9dx.code, cicd9dx.extension, (SELECT COUNT(cicd9dxasso.code) FROM iams_concept_icd9dx_asso cicd9dxasso WHERE cicd9dxasso.term_key = cl.term_key) AS icd9dx_asso_count");

					fromClauseElements.add("LEFT OUTER JOIN iams_concept_icd9dx cicd9dx ON (cicd9dx.term_key = c.term_key)");
				}
				else if (outputCodeType.equals(CodeType.ICD9Px)) {
					selectClauseElements.add("cicd9px.code, cicd9px.extension, (SELECT pxwt.px_wt FROM icd9_proc_ext pxwt WHERE pxwt.term_id = cl.term_id) AS px_wt, (SELECT COUNT(cicd9pxasso.code) FROM iams_concept_icd9px_asso cicd9pxasso WHERE cicd9pxasso.term_key = cl.term_key) AS icd9px_asso_count");

					fromClauseElements.add("LEFT OUTER JOIN iams_concept_icd9px cicd9px ON (cicd9px.term_key = c.term_key)");
				}
				/*else if (outputCodeType.equals(CodeType.ICD10Dx)) {
					selectClauseElements.add("cicd10.code, (SELECT COUNT(cicd10asso.code) FROM iams_concept_icd10_asso cicd10asso WHERE cicd10asso.term_key = cl.term_key) AS icd10dx_asso_count");

					fromClauseElements.add("LEFT OUTER JOIN iams_concept_icd10 cicd10 ON (cicd10.term_key = c.term_key)");
				}*/
				else if (outputCodeType.equals(CodeType.ICD102010MBD)) {
					selectClauseElements.add("cicd102010.code, (SELECT COUNT(cicd102010asso.code) FROM iams_concept_icd10_2010_asso cicd102010asso WHERE cicd102010asso.term_key = cl.term_key) AS icd102010_asso_count");

					fromClauseElements.add("LEFT OUTER JOIN iams_concept_icd10_2010 cicd102010 ON (cicd102010.term_key = c.term_key)");
				}
				else if (outputCodeType.equals(CodeType.SNOMEDCT)) {
					selectClauseElements.add("(SELECT COUNT(csct.code) FROM iams_concept_sct csct WHERE csct.term_key = c.term_key) AS sct_count");
				}
				else if (outputCodeType.equals(CodeType.ICPC2)) {
					selectClauseElements.add("cicpc2.code, (SELECT COUNT(cicpc2asso.code) FROM iams_concept_icpc2_asso cicpc2asso WHERE cicpc2asso.term_key = cl.term_key) AS icpc2_asso_count");

					fromClauseElements.add("LEFT OUTER JOIN iams_concept_icpc2 cicpc2 ON (cicpc2.term_key = c.term_key)");

				}
			}

			// Keyword Search
			if (!TermStrings.isBlank(criteria.getIn_keyword())) {
				isKeywordSearch = true;
				//Set<Integer> targetTermKeys = getTermKeyByKeyword(criteria.getIn_keyword());

				ArrayList<String> wordList = new ArrayList<String>();
				wordList.add(criteria.getIn_keyword());
				List<String> keywordTokens = CommonDictionaryUtils.tokenizeWords(wordList, false);

				selectClauseElements.add("(SELECT COUNT(ca.term_key) FROM iams_concept_alias ca WHERE UPPER(ca.alias) = :alias AND ca.term_key = cl.term_key) AS matched_alias_count");
				parameterMap.put("alias", criteria.getIn_keyword().toUpperCase());

				// Main Keyword Search
				if (!TermCollections.isEmpty(keywordTokens)) {
					int keywordIndex = 0;
					StringBuilder keywordClause = new StringBuilder("EXISTS (select 1 FROM ");
					StringBuilder tableClause = new StringBuilder();
					StringBuilder whereJoinClause = new StringBuilder(" WHERE d0.term_key = cl.term_key ");
					StringBuilder whereLikeClause = new StringBuilder();

					for (String keyword : keywordTokens) {
						tableClause.append("iams_concept_dict d"+keywordIndex);
						if (keywordIndex < keywordTokens.size()-1){
							tableClause.append(", ");

							whereJoinClause.append("AND d"+keywordIndex +".term_key = d"+(keywordIndex+1)+".term_key ");
							whereJoinClause.append("AND d"+keywordIndex +".alias_id = d"+(keywordIndex+1)+".alias_id ");
						}

						whereLikeClause.append("AND d"+keywordIndex+".word like :keyword_"+keywordIndex+" ");
						parameterMap.put("keyword_"+keywordIndex, keyword+"%");
						keywordIndex++;
					}
					keywordClause.append(tableClause);
					keywordClause.append(whereJoinClause);
					keywordClause.append(whereLikeClause + ")");
					whereClauseElements.add(keywordClause.toString());
					countWhereClauseElements.add(keywordClause.toString());
				}  else {
					hasKeyword = false; 
				}

				// Code Types
				if (!TermCollections.isEmpty(criteria.getIn_keywordCodeTypes())) {
					List<String> codeTypeWhereClauses = new ArrayList<String>();

					for (CodeType codeType : criteria.getIn_keywordCodeTypes()) {
						if (CodeType.ICD9Dx.equals(codeType)) {
							codeTypeWhereClauses.add("EXISTS (SELECT icd9dx.term_key FROM iams_concept_icd9dx icd9dx WHERE icd9dx.term_key = cl.term_key)");
						}
						else if (CodeType.ICD9Px.equals(codeType)) {
							codeTypeWhereClauses.add("EXISTS (SELECT icd9px.term_key FROM iams_concept_icd9px icd9px WHERE icd9px.term_key = cl.term_key)");
						}
						/*	else if (CodeType.ICD10Dx.equals(codeType)) {
							codeTypeWhereClauses.add("EXISTS (SELECT icd10.term_key FROM iams_concept_icd10 icd10 WHERE icd10.term_key = cl.term_key)");
						}*/
						else if (CodeType.ICD102010MBD.equals(codeType)) {
							codeTypeWhereClauses.add("EXISTS (SELECT icd102010.term_key FROM iams_concept_icd10_2010 icd102010 WHERE icd102010.term_key = cl.term_key)");
						}
						else if (CodeType.SNOMEDCT.equals(codeType)) {
							codeTypeWhereClauses.add("EXISTS (SELECT sct.term_key FROM iams_concept_sct sct WHERE sct.term_key = cl.term_key)");
						}
						else if (CodeType.ICPC2.equals(codeType)) {
							codeTypeWhereClauses.add("EXISTS (SELECT icpc2.term_key FROM iams_concept_icpc2 icpc2 WHERE icpc2.term_key = cl.term_key)");
						}
					}

					if (!TermCollections.isEmpty(codeTypeWhereClauses)) {
						whereClauseElements.add("(" + TermStrings.join(codeTypeWhereClauses, " OR ") + ")");
						countWhereClauseElements.add("(" + TermStrings.join(codeTypeWhereClauses, " OR ") + ")");
					}
				}
			}
			// Code Section Search (By Code)
			else if (!TermCollections.isEmpty(criteria.getIn_termCodes())) {
				isCodeSearch = true;

				Map<SearchType, Map<CodeType, List<TermSrchCode>>> groupedCodeSections = new EnumMap<SearchType, Map<CodeType, List<TermSrchCode>>>(SearchType.class);

				for (TermSrchCode termSrchCode: criteria.getIn_termCodes()) {
					SearchType searchType = termSrchCode.getSearchType();
					CodeType codeType = termSrchCode.getCodeType();

					Map<CodeType, List<TermSrchCode>> groupedCodeTypeMap = groupedCodeSections.get(searchType);

					if (groupedCodeTypeMap == null) {
						groupedCodeTypeMap = new EnumMap<CodeType, List<TermSrchCode>>(CodeType.class);
					}

					List<TermSrchCode> searchCodes = groupedCodeTypeMap.get(codeType);

					if (searchCodes == null) {
						searchCodes = new ArrayList<TermSrchCode>();
					}

					searchCodes.add(termSrchCode);
					groupedCodeTypeMap.put(codeType, searchCodes);
					groupedCodeSections.put(searchType, groupedCodeTypeMap);
				}

				List<String> codeWhereClauses = new ArrayList<String>();
				int codeTableAliasIndex = 0;
				int codeIndex = 0;
				int codeExtIndex = 0;
				int codeFromIndex = 0;
				int codeToIndex = 0;

				for (SearchType searchType : groupedCodeSections.keySet()) {
					Map<CodeType, List<TermSrchCode>> groupedCodeTypeMap = groupedCodeSections.get(searchType);

					for (CodeType codeType : groupedCodeTypeMap.keySet()) {
						List<TermSrchCode> searchCodes = groupedCodeTypeMap.get(codeType);
						List<String> codeSectionWhereClauses = new ArrayList<String>();
						String codeTableAlias = "ct" + codeTableAliasIndex;

						if (SearchType.CODE.equals(searchType)) {
							for (TermSrchCode termSrchCode : searchCodes) {
								String code = termSrchCode.getCode();
								SearchOption searchOption = termSrchCode.getSearchOption();

								if (!TermStrings.isBlank(code)) {
									if (searchOption.equals(SearchOption.BEGIN_WITH)) {
										codeSectionWhereClauses.add("(UPPER(" + codeTableAlias + ".code) >= :codeFrom_" + codeFromIndex + " AND UPPER("+codeTableAlias + ".code) <= :codeTo_"+codeToIndex+")");
										parameterMap.put("codeFrom_"+codeFromIndex, code.toUpperCase());
										parameterMap.put("codeTo_"+codeToIndex, code.toUpperCase()+MAX_CHARACTER);
										codeFromIndex++;
										codeToIndex++;
									}
									else if (searchOption.equals(SearchOption.EXACT_MATCH)) {
										codeSectionWhereClauses.add("(UPPER(" + codeTableAlias + ".code) = :code_" + codeIndex + ")");
										parameterMap.put("code_"+codeIndex, code.toUpperCase());
										codeIndex++;
										codeExtIndex++;
									}
								}
								else {
									throw new ServiceException(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS.ordinal(), TermServiceMessages.ERROR_MISSING_CODE);
								}

							}
						}
						else if (SearchType.CODE_EXTENSION.equals(searchType)) {
							for (TermSrchCode termSrchCode : searchCodes) {
								String code = termSrchCode.getCode();
								Integer extension = termSrchCode.getExtension();

								if (!TermStrings.isBlank(code)) {
									if (CodeType.ICD9Dx.equals(codeType) || CodeType.ICD9Px.equals(codeType)) {
										if (extension != null) {
											codeSectionWhereClauses.add("(UPPER(" + codeTableAlias + ".code) = :code_" + codeIndex + " AND " + codeTableAlias + ".extension = :codeExt_" + codeExtIndex + ")");
											parameterMap.put("code_"+codeIndex, code.toUpperCase());
											parameterMap.put("codeExt_"+codeExtIndex, extension);
										}
										else {
											throw new ServiceException(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS.ordinal(), TermServiceMessages.ERROR_MISSING_CODE_EXTENSION_ICD9);
										}
									}
									else {
										codeSectionWhereClauses.add("(UPPER(" + codeTableAlias + ".code) = :code_" + codeIndex + ")");
										parameterMap.put("code_"+codeIndex, code.toUpperCase());
									}
								}
								else {
									throw new ServiceException(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS.ordinal(), TermServiceMessages.ERROR_MISSING_CODE_EXTENSION);
								}
								codeIndex++;
								codeExtIndex++;
							}
						}
						else if (SearchType.CODE_RANGE.equals(searchType)) {
							for (TermSrchCode termSrchCode : searchCodes) {
								String codeFrom = termSrchCode.getCodeFrom();
								String codeTo = termSrchCode.getCodeTo();

								if (!TermStrings.isBlank(codeFrom)&& !TermStrings.isBlank(codeTo)) {
									codeSectionWhereClauses.add("(UPPER(" + codeTableAlias + ".code) >= :codeForm_" + codeFromIndex + " AND UPPER(" + codeTableAlias + ".code) <= :codeTo_" + codeToIndex + ")");
									parameterMap.put("codeForm_"+codeFromIndex, codeFrom.toUpperCase());
									parameterMap.put("codeTo_"+codeFromIndex, codeTo.toUpperCase() + MAX_CHARACTER);
								}
								else {
									throw new ServiceException(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS.ordinal(), "Code range is compulsory for code range search.");
								}
								codeFromIndex++;
								codeToIndex++;
							}

						}

						if (!TermCollections.isEmpty(codeSectionWhereClauses)) {
							if (CodeType.ICD9Dx.equals(codeType)) {
								codeWhereClauses.add("EXISTS (SELECT " + codeTableAlias + ".term_key FROM iams_concept_icd9dx " + codeTableAlias + " WHERE " + codeTableAlias + ".term_key = cl.term_key AND " +
										"(" + TermStrings.join(codeSectionWhereClauses, " OR ") + "))");
							}
							else if (CodeType.ICD9Px.equals(codeType)) {
								codeWhereClauses.add("EXISTS (SELECT " + codeTableAlias + ".term_key FROM iams_concept_icd9px " + codeTableAlias + " WHERE " + codeTableAlias + ".term_key = cl.term_key AND " +
										"(" + TermStrings.join(codeSectionWhereClauses, " OR ") + "))");
							}
							/*else if (CodeType.ICD10Dx.equals(codeType)) {
								codeWhereClauses.add("EXISTS (SELECT " + codeTableAlias + ".term_key FROM iams_concept_icd10 " + codeTableAlias + " WHERE " + codeTableAlias + ".term_key = cl.term_key AND " +
										"(" + TermStrings.join(codeSectionWhereClauses, " OR ") + "))");
							}*/
							else if (CodeType.ICD102010MBD.equals(codeType)) {
								codeWhereClauses.add("EXISTS (SELECT " + codeTableAlias + ".term_key FROM iams_concept_icd10_2010 " + codeTableAlias + " WHERE " + codeTableAlias + ".term_key = cl.term_key AND " +
										"(" + TermStrings.join(codeSectionWhereClauses, " OR ") + "))");
							}
							else if (CodeType.SNOMEDCT.equals(codeType)) {
								codeWhereClauses.add("EXISTS (SELECT " + codeTableAlias + ".term_key FROM iams_concept_sct " + codeTableAlias + " WHERE " + codeTableAlias + ".term_key = cl.term_key AND " +
										"(" + TermStrings.join(codeSectionWhereClauses, " OR ") + "))");
							}
							else if (CodeType.ICPC2.equals(codeType)) {
								codeWhereClauses.add("EXISTS (SELECT " + codeTableAlias + ".term_key FROM iams_concept_icpc2 " + codeTableAlias + " WHERE " + codeTableAlias + ".term_key = cl.term_key AND " +
										"(" + TermStrings.join(codeSectionWhereClauses, " OR ") + "))");
							}
						}
					}
				}

				if (!TermCollections.isEmpty(codeWhereClauses)) {
					whereClauseElements.add("(" + TermStrings.join(codeWhereClauses, " OR ") + ")");
				}
			}
			// Code Section Search (By Term ID)
			else if (!TermCollections.isEmpty(criteria.getIn_termIds())) {
				whereClauseElements.add("c.term_id IN (:termId)");
				parameterMap.put("termId", criteria.getIn_termIds());
			}

			// CDF Override and Top40 Override
			if (criteria.isIn_CDF_OverrideIndicator() || criteria.isIn_Top40_OverrideIndicator()) {
				selectClauseElements.add("cdf.clinical_data_id, cdf.clinical_data_type_id, cdf.description, cdf.reference_id, cdf.valid_sex, cdf.valid_age, cdf.status, ctm.icd9_code_type");

				if (isKeywordSearch) {
					selectClauseElements.add("(SELECT COUNT(ca.clinical_data_id) FROM cdf_alias ca WHERE UPPER(ca.description) = :keyword AND ca.clinical_data_id = cdf.clinical_data_id) AS matched_cdf_alias_count");
					parameterMap.put("keyword", criteria.getIn_keyword().toUpperCase());
				}

				fromClauseElements.add("LEFT OUTER JOIN cdf_term_mapping ctm ON (c.term_Id = ctm.term_Id and ctm.code_to_cdf='Y')");
				fromClauseElements.add("LEFT OUTER JOIN cdf_clinical_data cdf ON (cdf.clinical_data_id = ctm.clinical_data_id");

				//Enhance to bypass cdf if in specialties list is equal reference id for psy user 
				//This query will result for preparing override case records if true 
				if(!TermCollections.isEmpty(criteria.getIn_CDF_Specialties())){

					fromClauseElements.add(" and NOT EXISTS (SELECT NULL from cdf_clinical_ref r WHERE r.clinical_data_id = cdf.clinical_data_id "
							+ "and r.reference_id IN (:specialties))");

					parameterMap.put("specialties", criteria.getIn_CDF_Specialties());

					fromClauseElements.add(" and ( NOT EXISTS (SELECT NULL from cdf_specialty_check sc WHERE sc.clinical_data_id = cdf.clinical_data_id) "
							+ "or EXISTS (SELECT NULL from cdf_specialty_check sc WHERE sc.clinical_data_id = cdf.clinical_data_id and sc.specialty_value IN (:specialties_check)))");

					parameterMap.put("specialties_check", criteria.getIn_CDF_Specialties());

					fromClauseElements.add(")");
				}
				else if(!TermCollections.isEmpty(criteria.getIn_Top40_Specialties()))
				{
					fromClauseElements.add(" and NOT EXISTS (SELECT NULL from cdf_clinical_ref r WHERE r.clinical_data_id = cdf.clinical_data_id "
							+ "and r.reference_id IN (:specialties))");

					parameterMap.put("specialties", criteria.getIn_Top40_Specialties());

					fromClauseElements.add(" and ( NOT EXISTS (SELECT NULL from cdf_specialty_check sc WHERE sc.clinical_data_id = cdf.clinical_data_id) "
							+ "or EXISTS (SELECT NULL from cdf_specialty_check sc WHERE sc.clinical_data_id = cdf.clinical_data_id and sc.specialty_value IN (:specialties_check)))");

					parameterMap.put("specialties_check", criteria.getIn_Top40_Specialties());

					fromClauseElements.add(")");
				}
				else{
					fromClauseElements.add(" and ( NOT EXISTS (SELECT NULL from cdf_specialty_check sc WHERE sc.clinical_data_id = cdf.clinical_data_id) "
							+ "or EXISTS (SELECT NULL from cdf_specialty_check sc WHERE sc.clinical_data_id = cdf.clinical_data_id and sc.specialty_value IS NULL))");

					fromClauseElements.add(")");
				}
			}

			// Request Hospital Codes
			if (criteria.getIn_requestHospitalCode() != null && !criteria.getIn_requestHospitalCode().toUpperCase().equals("ALL")) {

				LookupValueDTO hosptialLookupValue = lookupValueCache.getValueByDataValue(TermServiceLookupValueConstants.INSTITUTION_LIST, criteria.getIn_requestHospitalCode());
				if (hosptialLookupValue != null) {
					whereClauseElements.add("c.request_inst_cd = :insitution ");
					countWhereClauseElements.add("c.request_inst_cd = :insitution ");
					parameterMap.put("insitution", hosptialLookupValue.getValueKey());

				}
			}

			// System Used (if both system uesd and exclude system used are entered, only system used will be act as criteria)
			if (!TermCollections.isEmpty(criteria.getIn_systemUseds()) || !TermCollections.isEmpty(criteria.getIn_excludeSystemUseds())) {
				String operator = "EXISTS";
				List<String> targetSystemUseds = null;
				List<Integer> systemUsedIds = new ArrayList<Integer>();

				if (!TermCollections.isEmpty(criteria.getIn_systemUseds())) {
					targetSystemUseds = criteria.getIn_systemUseds();
				}
				else if (!TermCollections.isEmpty(criteria.getIn_excludeSystemUseds())) {
					targetSystemUseds = criteria.getIn_excludeSystemUseds();
					operator = "NOT EXISTS";
				}

				for (String systemUsed : targetSystemUseds) {

					LookupValueDTO systemUseDTO = lookupValueCache.getValueByDataValue(TermServiceLookupValueConstants.SYSTEM_USED_LIST, systemUsed);

					if (systemUseDTO != null) {
						systemUsedIds.add(systemUseDTO.getValueKey());
					}
				}

				if (!TermCollections.isEmpty(systemUsedIds)) {
					whereClauseElements.add(operator + " (SELECT su.system_used FROM iams_concept_ha_sys_used su WHERE su.term_key = cl.term_key AND su.system_used IN (:systemUsed))");
					parameterMap.put("systemUsed", systemUsedIds);
				}
			}

			// Nature
			if (!TermCollections.isEmpty(criteria.getIn_natures())) {
				if (criteria.getIn_natures().size() == 1) {

					LookupValueDTO natureDTO = lookupValueCache.getValueByDataValue(TermServiceLookupValueConstants.NATURE_LIST, criteria.getIn_natures().get(0));
					if (natureDTO != null) {
						whereClauseElements.add("c.nature = :nature");
						countWhereClauseElements.add("c.nature = :nature");
						parameterMap.put("nature", natureDTO.getValueKey());
					}
				}
				else {
					List<Integer> natureIds = new ArrayList<Integer>();

					for (String nature : criteria.getIn_natures()) {

						LookupValueDTO natureDTO = lookupValueCache.getValueByDataValue(TermServiceLookupValueConstants.NATURE_LIST, nature);

						if (natureDTO != null) {
							natureIds.add(natureDTO.getValueKey());
						}
					}

					whereClauseElements.add("c.nature IN (:nature)");
					countWhereClauseElements.add("c.nature IN (:nature)");
					parameterMap.put("nature", natureIds);
				}
			}

			// Stage

			whereClauseElements.add("c.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED);
			countWhereClauseElements.add("c.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED);

			// Status
			if (!TermStrings.isBlank(criteria.getIn_status()) && !criteria.getIn_status().toUpperCase().equals("ALL")) {

				LookupValueDTO statusDTO = lookupValueCache.getValueByDataValue(TermServiceLookupValueConstants.STATUS_LIST, criteria.getIn_status());

				if (isCodeSearch && criteria.getIn_status().toUpperCase().equals("A") && criteria.isIn_inActiveTerm_associatedOverride()) {
					List<String> statusCheckWhereClauses = new ArrayList<String>();

					statusCheckWhereClauses.add("cha.status = :status");
					parameterMap.put("status", TermServiceLookupValueConstants.STATUS_ACTIVE);
					statusCheckWhereClauses.add("EXISTS (SELECT casso.term_id FROM iams_concept_icd9dx c9dx, iams_concept_icd9dx_asso c9dxasso, iams_concept casso WHERE cl.term_key = c9dx.term_key AND UPPER(c9dx.code) = UPPER(c9dxasso.code) AND c9dx.extension = c9dxasso.extension AND c9dxasso.term_key = casso.term_key AND casso.status = :status AND casso.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED + ")");
					statusCheckWhereClauses.add("EXISTS (SELECT casso.term_id FROM iams_concept_icd9px c9px, iams_concept_icd9px_asso c9pxasso, iams_concept casso WHERE cl.term_key = c9px.term_key AND UPPER(c9px.code) = UPPER(c9pxasso.code) AND c9px.extension = c9pxasso.extension AND c9pxasso.term_key = casso.term_key AND casso.status = :status AND casso.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED + ")");

					whereClauseElements.add("(" + TermStrings.join(statusCheckWhereClauses, " OR ") + ")");
				}
				else if (statusDTO != null) {
					whereClauseElements.add("cha.status = :status");
					parameterMap.put("status", statusDTO.getValueKey());
				}

				if (statusDTO != null) {
					countWhereClauseElements.add("cha.status = :status");
					parameterMap.put("status", statusDTO.getValueKey());
				}
			}

			whereClauseElements.add("cha.term_key = cl.term_key");
			whereClauseElements.add("cl.term_key = c.term_key");
			countWhereClauseElements.add("cha.term_key = cl.term_key");
			countWhereClauseElements.add("cl.term_key = c.term_key");

			if(hasKeyword){

				// If it is keyword search and records limit is set, count the result
				if (isKeywordSearch && criteria.getOut_recordsLimit() != null && criteria.getOut_recordsLimit() > 0) {
					boolean isExceedsLimit = false;
					StringBuilder countSQL = new StringBuilder();

					countSQL.append("SELECT COUNT(*) ");
					countSQL.append("FROM ").append(TermStrings.join(countFromClauseElements, " ")).append(" ");
					countSQL.append("WHERE ").append(TermStrings.join(countWhereClauseElements, " AND ")).append(" ");

					// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
					Date startCountSQLDate = TermDates.getCurrentDate();
					// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

					logger.debug("COUNT SQL: " + countSQL.toString());
					logger.debug("COUNT SQL para: " + parameterMap);

					int recordsCount = getJdbcTemplate().queryForObject(countSQL.toString(), parameterMap, Integer.class);
					logger.debug("total record count : "+recordsCount);

					// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
					Date endCountSQLDate = TermDates.getCurrentDate();
					logger.debug("@@@@@@@@@@ Total COUNT SQL time: " + (endCountSQLDate.getTime() - startCountSQLDate.getTime()));
					// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

					if (recordsCount >= criteria.getOut_recordsLimit()) {
						isExceedsLimit = true;
					}

					if (isExceedsLimit) {
						throw new ServiceException(Error.ERROR_EXCEEDS_RECORDS_LIMIT.ordinal(), "Exceeds the records limit " + criteria.getOut_recordsLimit());
					}
				}

				StringBuilder sql = new StringBuilder();

				sql.append("SELECT ").append(TermStrings.join(selectClauseElements, ", ")).append(" ");
				sql.append("FROM ").append(TermStrings.join(fromClauseElements, " ")).append(" ");
				sql.append("WHERE ").append(TermStrings.join(whereClauseElements, " AND ")).append(" ");

				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				Date startSQLDate =TermDates.getCurrentDate();
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

				logger.debug("SQL: " + sql.toString());
				logger.debug("parameter: "+parameterMap);

				SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql.toString(), parameterMap);

				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				Date endSQLDate = TermDates.getCurrentDate();
				logger.debug("@@@@@@@@@@ Total SQL time: " + (endSQLDate.getTime() - startSQLDate.getTime()));

				long sortingTime = 0;
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

				Map<Integer, Boolean> addedCDFData = new HashMap<Integer, Boolean>();

				while (rs.next()) {
					int fieldIndex = 1;
					boolean isAdd = false;

					Integer termKey = TermNumbers.toInteger(rs.getString(fieldIndex++));
					Integer termId = TermNumbers.toInteger(rs.getString(fieldIndex++));
					Integer nature = TermNumbers.toInteger(rs.getString(fieldIndex++));
					String fullDesc = rs.getString(fieldIndex++);
					String shortDesc = rs.getString(fieldIndex++);
					Integer status = TermNumbers.toInteger(rs.getString(fieldIndex++));
					Integer sex = TermNumbers.toInteger(rs.getString(fieldIndex++));
					Integer age = TermNumbers.toInteger(rs.getString(fieldIndex++));
					Integer principalCdFlag = TermNumbers.toInteger(rs.getString(fieldIndex++));
					Integer systemUsedCount = TermNumbers.toInteger(rs.getString(fieldIndex++));

					BasicTermDataVO basicTermData = new BasicTermDataVO();

					basicTermData.setTermKey(termKey);
					basicTermData.setTermId(termId);
					basicTermData.setNature(lookupValueCache.getValueByValueKey(nature).getDataValue());
					basicTermData.setFullDesc(fullDesc);
					basicTermData.setShortDesc(shortDesc);
					basicTermData.setStatus(lookupValueCache.getValueByValueKey(status).getDataValue());
					basicTermData.setSex(lookupValueCache.getValueByValueKey(sex).getDataValue());
					basicTermData.setAge(lookupValueCache.getValueByValueKey(age).getDataValue());
					basicTermData.setPrincipalCdFlag(lookupValueCache.getValueByValueKey(principalCdFlag).getDataValue());
					basicTermData.setSystemUsedCount(systemUsedCount);

					if (criteria.isOut_iamsInfo()) {
						String remarks = rs.getString(fieldIndex++);
						String requestUser = rs.getString(fieldIndex++);
						Integer requestHospCd = TermNumbers.toInteger(rs.getString(fieldIndex++));
						String createUser = rs.getString(fieldIndex++);
						Date createDtm = TermDates.toDate(rs.getTimestamp(fieldIndex++));
						String updateUser = rs.getString(fieldIndex++);
						Date updateDtm = TermDates.toDate(rs.getTimestamp(fieldIndex++));

						basicTermData.setRemarks(remarks);
						basicTermData.setRequestUser(requestUser);

						basicTermData.setRequestHospCd(lookupValueCache.getValueByValueKey(requestHospCd).getDataValue());
						basicTermData.setCreateUser(createUser);
						basicTermData.setCreateDtm(createDtm);
						basicTermData.setUpdateUser(updateUser);
						basicTermData.setUpdateDtm(updateDtm);
					}

					for (CodeType outputCodeType : outputCodeTypes) {
						if (outputCodeType.equals(CodeType.ICD9Dx)) {
							String icd9dxCode = rs.getString(fieldIndex++);
							Integer icd9dxExtension = TermNumbers.toInteger(rs.getString(fieldIndex++));
							Integer icd9dxAssoCount = TermNumbers.toInteger(rs.getString(fieldIndex++));

							basicTermData.setIcd9dxCode(icd9dxCode);
							basicTermData.setIcd9dxExtension(icd9dxExtension);
							basicTermData.setAssoIcd9dxCount(icd9dxAssoCount);
						}
						else if (outputCodeType.equals(CodeType.ICD9Px)) {
							String icd9pxCode = rs.getString(fieldIndex++);
							Integer icdpdxExtension = TermNumbers.toInteger(rs.getString(fieldIndex++));
							String icd9pxWeight = rs.getString(fieldIndex++);
							if(icd9pxWeight != null){
								icd9pxWeight = TermNumbers.formatNumber(TermNumbers.toDouble(icd9pxWeight), "0.00");
							}

							Integer icd9pxAssoCount = TermNumbers.toInteger(rs.getString(fieldIndex++));
							basicTermData.setIcd9pxCode(icd9pxCode);
							basicTermData.setIcd9pxExtension(icdpdxExtension);
							basicTermData.setIcd9pxWeight(icd9pxWeight);
							basicTermData.setAssoIcd9pxCount(icd9pxAssoCount);
						}
						//						else if (outputCodeType.equals(CodeType.ICD10Dx)) {
						//							String icd10dxCode = rs.getString(fieldIndex++);
						//							Integer icd10dxAssoCount = TermNumbers.toInteger(rs.getString(fieldIndex++));
						//
						//							basicTermData.setIcd10dxCode(icd10dxCode);
						//							basicTermData.setAssoIcd10dxCount(icd10dxAssoCount);
						//						}
						else if (outputCodeType.equals(CodeType.ICD102010MBD)) {
							String icd102010Code = rs.getString(fieldIndex++);
							Integer icd102010AssoCount = TermNumbers.toInteger(rs.getString(fieldIndex++));

							basicTermData.setIcd102010MBDCode(icd102010Code);
							basicTermData.setAssoIcd102010MBDCount(icd102010AssoCount);
						}
						else if (outputCodeType.equals(CodeType.SNOMEDCT)) {
							Integer sctCount = TermNumbers.toInteger(rs.getString(fieldIndex++));

							basicTermData.setSnomedctCount(sctCount);
						}
						else if (outputCodeType.equals(CodeType.ICPC2)) {
							String icpc2Code = rs.getString(fieldIndex++);
							Integer icpc2AssoCount = TermNumbers.toInteger(rs.getString(fieldIndex++));

							basicTermData.setIcpc2Code(icpc2Code);
							basicTermData.setAssoIcpc2Count(icpc2AssoCount);
						}
					}

					if (isKeywordSearch) {
						Integer matchedAliasCount = TermNumbers.toInteger(rs.getString(fieldIndex++));

						basicTermData.setMatchedAliasCount(matchedAliasCount);
					}

					if (criteria.isIn_CDF_OverrideIndicator() || criteria.isIn_Top40_OverrideIndicator()) {
						Integer clinicalDataId = TermNumbers.toInteger(rs.getString(fieldIndex++));

						basicTermData.setClinicalDataId(clinicalDataId);

						if (clinicalDataId != null) {

							String cdfDataType = rs.getString(fieldIndex++);
							String cdfDescription = rs.getString(fieldIndex++);
							String cdfReferenceId = rs.getString(fieldIndex++);
							String cdfSex = rs.getString(fieldIndex++);
							String cdfAge = rs.getString(fieldIndex++);
							String cdfStatus = rs.getString(fieldIndex++);
							String icd9CodeType = rs.getString(fieldIndex++);

							String cdfNature = null;
							if( "Dx9".equals(icd9CodeType)){
								cdfNature = "D";
							} else if("Px9".equals(icd9CodeType)){
								cdfNature = "P";
							}

							basicTermData.setCdfDataType(cdfDataType);
							basicTermData.setCdfDescription(cdfDescription);
							basicTermData.setCdfReferenceId(cdfReferenceId);
							basicTermData.setCdfSex(cdfSex);
							basicTermData.setCdfAge(cdfAge);
							basicTermData.setCdfStatus(cdfStatus);
							basicTermData.setCdfNature(cdfNature);

							if (isKeywordSearch) {
								Integer cdfAliasMatchedCount = TermNumbers.toInteger(rs.getString(fieldIndex++));

								basicTermData.setCdfAliasMatchedCount(cdfAliasMatchedCount);
							}

							boolean isOverride = false;

							if (criteria.isIn_CDF_OverrideIndicator() && (cdfDataType.equals("DI") || cdfDataType.equals("PR"))) {							
								isOverride = true;
							}

							if (criteria.isIn_Top40_OverrideIndicator() && cdfDataType.equals("DT")) {						
								isOverride = true;
							}

							basicTermData.setCdfOverride(isOverride);

							if(isOverride){
								if (!addedCDFData.containsKey(clinicalDataId)) {

									addedCDFData.put(clinicalDataId, Boolean.TRUE);

									isAdd = true;
								}
							} else {
								isAdd = true;
							}
						}
						else {
							isAdd = true;
						}
					}
					else {
						isAdd = true;
					}

					if (isAdd) {
						if (isKeywordSearch) {
							// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
							Date startGetSortingScoreDate = TermDates.getCurrentDate();
							// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

							basicTermData.setSortingScore(getSortingScore(basicTermData, criteria.getIn_keyword(), criteria.getIn_requestSystem()));

							// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
							Date endGetSortingScoreDate = TermDates.getCurrentDate();

							sortingTime += endGetSortingScoreDate.getTime() - startGetSortingScoreDate.getTime();
							// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
						}

						basicTermDataList.add(basicTermData);
					}
				}

				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				Date startSortingDate =  TermDates.getCurrentDate();
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++

				// Sorting result
				CodeType soringCodeType = null;

				if (isKeywordSearch) {
					if (!TermCollections.isEmpty(criteria.getIn_keywordCodeTypes())) {
						soringCodeType = criteria.getIn_keywordCodeTypes().get(0);
					}
				}
				else {
					if (!TermCollections.isEmpty(criteria.getIn_termCodes())) {
						TermSrchCode codeSearchType = criteria.getIn_termCodes().get(0);

						if (codeSearchType.getCodeType() != null) {
							soringCodeType = codeSearchType.getCodeType();
						}
					}
				}
				// default sort by ICD9DX
				if (soringCodeType == null) {
					soringCodeType = CodeType.ICD9Dx;
				}

				Collections.sort(basicTermDataList, new BasicTermDataComparator(isKeywordSearch, criteria.getIn_requestSystem(), soringCodeType));

				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
				Date endSortingDate = new Date(System.currentTimeMillis());

				logger.debug("@@@@@@@@@@ Collections sort time: " + (endSortingDate.getTime() - startSortingDate.getTime()));
				logger.debug("@@@@@@@@@@ Total sorting time: " + (endSortingDate.getTime() - startSortingDate.getTime() + sortingTime));        
				logger.debug("@@@@@@@@@@ Total get basic term data (without SQL, sorting) time: " + (endSortingDate.getTime() - endSQLDate.getTime() - (endSortingDate.getTime() - startSortingDate.getTime() + sortingTime)));
				// ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
			}
		}
		catch (Exception e) {
			if (e instanceof ServiceException) {
				throw (ServiceException) e;
			}
			else {
				IamsExceptionHelper.throwsIAMSException("Failure in getting basic term data", e);
			}
		}

		return basicTermDataList;
	}

	@Override
	public List<Integer> getTermSystemUsed(Integer termKey) throws IamsDataAccessException {
		List<Integer> systemUseds = new ArrayList<Integer>();
		if (noDatabase()) {
			return systemUseds;
		}

		Map<String, Object> parameterMap = new HashMap<String, Object>();

		try {    
			String sql = "SELECT s.system_used FROM iams_concept_ha_sys_used s WHERE s.term_key = :termKey";

			parameterMap.put("termKey", termKey);

			SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql, parameterMap);

			while (rs.next()) {
				systemUseds.add(rs.getInt("system_used"));
			}
		}
		catch (Exception e) {
			IamsExceptionHelper.throwsIAMSException("Failure in getting concept system used. Term key: " + termKey, e);
		}

		return systemUseds;
	}

	@Override
	public List<Alias> getTermAlias(Integer termKey) throws IamsDataAccessException {
		List<Alias> aliases = new ArrayList<Alias>();
		if (noDatabase()) {
			return aliases;
		}


		try {    
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			String sql = "SELECT a.alias_id, a.alias FROM iams_concept_alias a WHERE a.term_key = :termKey";
			parameterMap.put("termKey", termKey);

			SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql, parameterMap);

			while (rs.next()) {
				Alias alias = new Alias();

				alias.setAliasId(rs.getInt("alias_id"));
				alias.setAliasDesc(rs.getString("alias"));

				aliases.add(alias);
			}
		}
		catch (Exception e) {
			IamsExceptionHelper.throwsIAMSException("Failure in getting concept aliases. Term key: " + termKey, e);
		}

		return aliases;

	}

	@Override
	public List<AssoEntity> getAssociatedEntities(Integer termKey) throws IamsDataAccessException {
		List<AssoEntity> entities = new ArrayList<AssoEntity>();
		if (noDatabase()) {
			return entities;
		}

		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();

			String sql = "SELECT e.entity_id, e.full_desc, e.epr_desc " +
					"FROM iams_entity e, iams_entity_concept ce " +
					"WHERE e.entity_key = ce.entity_key " +
					"AND e.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED + " "+
					"AND ce.term_key = :termKey";

			parameterMap.put("termKey", termKey);

			SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql, parameterMap);

			while (rs.next()) {
				AssoEntity assoEntity = new AssoEntity();

				assoEntity.setEntityId(rs.getInt("entity_id"));
				assoEntity.setFullDesc(rs.getString("full_desc"));
				assoEntity.setEprDesc(rs.getString("epr_desc"));

				entities.add(assoEntity);
			}
		}
		catch (Exception e) {
			IamsExceptionHelper.throwsIAMSException("Failure in getting concept asso entities. Term key: " + termKey, e);
		}

		return entities;

	}

	@Override
	public List<AssoTermCode> getAssoTermCode(CodeType codeType, Integer termKey)  throws IamsDataAccessException {
		List<AssoTermCode> assoTermCodes = new ArrayList<AssoTermCode>();
		if (noDatabase()) {
			return assoTermCodes;
		}

		try {
			Map<String, Object> parameterMap = new HashMap<String, Object>();

			String sql = null;

			if (codeType.equals(CodeType.ICD9Dx)) {
				sql = "SELECT DISTINCT a.code, a.extension, c.term_id " +
						"FROM iams_concept_icd9dx_asso a " +
						"LEFT JOIN iams_concept_icd9dx d ON d.code = a.code AND d.extension = a.extension " +
						"LEFT JOIN iams_concept c ON d.term_key = c.term_key AND c.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED + " "+
						"WHERE a.term_key = :termKey " +
						"ORDER BY a.code, a.extension";

				parameterMap.put("termKey", termKey);
			}        
			else if (codeType.equals(CodeType.ICD9Px)) {
				sql = "SELECT DISTINCT a.code, a.extension, c.term_id " +
						"FROM iams_concept_icd9px_asso a " +
						"LEFT JOIN iams_concept_icd9px d ON d.code = a.code AND d.extension = a.extension " +
						"LEFT JOIN iams_concept c ON d.term_key = c.term_key AND c.stage = "  + TermServiceLookupValueConstants.STAGE_IN_USED + " "+
						"WHERE a.term_key = :termKey " +
						"ORDER BY a.code, a.extension";

				parameterMap.put("termKey", termKey);
			}        
			/*else if (codeType.equals(CodeType.ICD10Dx)) {
				sql = "SELECT DISTINCT a.code, NULL AS extension, c.term_id " +
						"FROM iams_concept_icd10_asso a " +
						"LEFT JOIN iams_concept_icd10 d ON d.code = a.code " +
						"LEFT JOIN iams_concept c ON d.term_key = c.term_key AND c.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED + " "+
						"WHERE a.term_key = :termKey " +
						"ORDER BY a.code, extension";

				parameterMap.put("termKey", termKey);
			}*/
			else if (codeType.equals(CodeType.ICD102010MBD)) {
				sql = "SELECT DISTINCT a.code, NULL AS extension, c.term_id " +
						"FROM iams_concept_icd10_2010_asso a " +
						"LEFT JOIN iams_concept_icd10_2010 d ON d.code = a.code " +
						"LEFT JOIN iams_concept c ON d.term_key = c.term_key AND c.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED + " "+
						"WHERE a.term_key = :termKey " +
						"ORDER BY a.code, extension";

				parameterMap.put("termKey", termKey);
			}
			else if (codeType.equals(CodeType.ICPC2)) {
				sql = "SELECT DISTINCT a.code, NULL AS extension, c.term_id " +
						"FROM iams_concept_icpc2_asso a " +
						"LEFT JOIN iams_concept_icpc2 d ON d.code = a.code " +
						"LEFT JOIN iams_concept c ON d.term_key = c.term_key AND c.stage = " + TermServiceLookupValueConstants.STAGE_IN_USED + " "+
						"WHERE a.term_key = :termKey " +
						"ORDER BY a.code, extension";

				parameterMap.put("termKey", termKey);
			}

			SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql, parameterMap);

			String lastCodeExtStr = null;
			AssoTermCode lastAssoTermCode = null;

			while (rs.next()) {
				String code = rs.getString("code");
				Integer extension = TermNumbers.toInteger(rs.getString("extension"));
				Integer termId = TermNumbers.toInteger(rs.getString("term_id"));

				String codeExtStr = code + "|||" + extension;

				if (lastCodeExtStr == null || !codeExtStr.equals(lastCodeExtStr)) {
					lastCodeExtStr = codeExtStr;
					lastAssoTermCode = new AssoTermCode();

					lastAssoTermCode.setCode(code);
					lastAssoTermCode.setExtension(extension);

					assoTermCodes.add(lastAssoTermCode);
				}

				if (termId != null) {
					lastAssoTermCode.setTermId(termId);
				}
			}
		}
		catch (Exception e) {
			IamsExceptionHelper.throwsIAMSException("Failure in getting concept asso term code. Term key: " + termKey, e);
		}

		return assoTermCodes;
	}


	@Override
	public List<Snomedct> getSnomedctCode(Integer termKey) throws IamsDataAccessException {
		List<Snomedct> snomedcts = new ArrayList<Snomedct>();
		if (noDatabase()) {
			return snomedcts;
		}

		try {

			Map<String, Object> parameterMap = new HashMap<String, Object>();

			String sql = "SELECT s.code, s.code_version, CAST(s.code_version_date AS TIMESTAMP) AS code_version_date FROM iams_concept_sct s WHERE s.term_key = :termKey ";

			parameterMap.put("termKey", termKey);
			SqlRowSet rs = getJdbcTemplate().queryForRowSet(sql, parameterMap);

			while (rs.next()) {
				Snomedct snomedct = new Snomedct();

				snomedct.setCode(rs.getString("code"));
				snomedct.setVersion(rs.getString("code_version"));
				snomedct.setVersionDate(rs.getTimestamp("code_version_date"));

				snomedcts.add(snomedct);
			}
		}
		catch (Exception e) {
			IamsExceptionHelper.throwsIAMSException("Failure in getting concept Snomed CT. Term key: " + termKey, e);
		}

		return snomedcts;

	}

	// Obsolete because do not need to use description token to match
	/*
	private boolean matchDesc(String description,List<String> keywordTokens) throws IamsDataAccessException {
    // To match the Description with input keywords, to check the partial match
		ArrayList<String> wordList = new ArrayList<String>();
		wordList.add(description);
		List<String> descTokens = CommonDictionaryUtils.tokenizeWords(wordList, false);
		boolean isMatch = false;

		// Match the Description and Keyword
		for(String keyword : keywordTokens){
			for (String desc : descTokens)
			{
				//if (desc.equals(keyword)){
				if (desc.indexOf(keyword) == 0){
					isMatch = true;
				}
			}
		}
		return isMatch;
	}
	 */

	@Override
	public Integer getSortingScore(BasicTermDataVO basicTermData, String keyword, RequestSystem requestSystem) throws IamsDataAccessException {
		Integer score = 0; // Initial score : 000000000
		int totalPriorityLevel = 9;

		String upperCaseKeyword = keyword.toUpperCase();

		Nature iamsNature = Nature.getNatureByIamsNature(basicTermData.getNature());
		Nature cdfNature = Nature.getNatureByCdfNature(basicTermData.getCdfNature());

		//Initial ExactMatch field 
		// 0 : partial match ;  1 : exact match
		basicTermData.setExactMatch(0);

		if (!requestSystem.equals(RequestSystem.FM)) {

			// 1st Priority - Dx  
			if ((basicTermData.isCdfOverride() && cdfNature != null && cdfNature.equals(Nature.DX)) || (!basicTermData.isCdfOverride() && iamsNature != null && iamsNature.equals(Nature.DX))) {
				score |= 1 << (totalPriorityLevel - 1);
			}

			// 2nd Priority = Px 
			if ((basicTermData.isCdfOverride() && cdfNature != null && cdfNature.equals(Nature.PX)) || (!basicTermData.isCdfOverride() && iamsNature != null && iamsNature.equals(Nature.PX))) {
				score |= 1 << (totalPriorityLevel - 2);
			}

			// 3rd Priority - Exact match of CDF name
			if (basicTermData.isCdfOverride() && basicTermData.getCdfDescription() != null && basicTermData.getCdfDescription().toUpperCase().equals(upperCaseKeyword)) {
				score |= 1 << (totalPriorityLevel - 3);
				basicTermData.setExactMatch(1);
			}

			// 4th Priority - Exact match of CDF alias
			if (basicTermData.isCdfOverride() && basicTermData.getCdfAliasMatchedCount() != null && basicTermData.getCdfAliasMatchedCount() > 0) {
				score |= 1 << (totalPriorityLevel - 4);
				basicTermData.setExactMatch(1);
			}

			// 5th Priority - Exact match of IAMS full description
			if (!basicTermData.isCdfOverride() && basicTermData.getFullDesc() != null && basicTermData.getFullDesc().toUpperCase().equals(upperCaseKeyword)) {
				score |= 1 << (totalPriorityLevel - 5);
				basicTermData.setExactMatch(1);
			}

			// 6th Priority - Exact match of IAMS alias
			if (!basicTermData.isCdfOverride() && basicTermData.getMatchedAliasCount() != null && basicTermData.getMatchedAliasCount() > 0) {
				score |= 1 << (totalPriorityLevel - 6);
				basicTermData.setExactMatch(1);
			}

			// 7th Priority - Exact match of IAMS short description 
			if (!basicTermData.isCdfOverride() && basicTermData.getShortDesc() != null && basicTermData.getShortDesc().toUpperCase().equals(upperCaseKeyword)) {
				score |= 1 << (totalPriorityLevel - 7);
				basicTermData.setExactMatch(1);
			}


			// 8th Priority - Partial match of CDF name (Modified at 28 Sep 2012)
			if (basicTermData.isCdfOverride() && basicTermData.getClinicalDataId() != null ) {
				score |= 1 << (totalPriorityLevel - 8);
			}

			// Delete for condition change : 8th Priority - Partial match of CDF name
			//if (basicTermData.isCdfOverride() && basicTermData.getCdfDescription() != null && !basicTermData.getCdfDescription().toUpperCase().equals(upperCaseKeyword)) {
			//	score |= 1 << (totalPriorityLevel - 8);
			//}

			// Deleted at 27 Sep 2012, this condition change
			// 9th Priority - Partial match of IAMS full description
			//if (!basicTermData.isCdfOverride() && basicTermData.getFullDesc() != null && !basicTermData.getFullDesc().toUpperCase().equals(upperCaseKeyword)) {
			//	score |= 1 << (totalPriorityLevel - 9);
			//}

		} 
		else { // FM
			// Initial score : 0000000000
			totalPriorityLevel = 10;

			// 1st Priority - Dx  
			if ((basicTermData.isCdfOverride() && cdfNature != null && cdfNature.equals(Nature.DX)) || (!basicTermData.isCdfOverride() && iamsNature != null && iamsNature.equals(Nature.DX))) {
				score |= 1 << (totalPriorityLevel - 1);
			}

			// 2nd Priority = Px 
			if ((basicTermData.isCdfOverride() && cdfNature != null && cdfNature.equals(Nature.PX)) || (!basicTermData.isCdfOverride() && iamsNature != null && iamsNature.equals(Nature.PX))) {
				score |= 1 << (totalPriorityLevel - 2);
			}

			// 3rd Priority - Exact match of CDF name
			if (basicTermData.isCdfOverride() && basicTermData.getCdfDescription() != null && basicTermData.getCdfDescription().toUpperCase().equals(upperCaseKeyword)) {
				score |= 1 << (totalPriorityLevel - 3);
				basicTermData.setExactMatch(1);
			}

			// 4th Priority - Exact match of CDF alias
			if (basicTermData.isCdfOverride() && basicTermData.getCdfAliasMatchedCount() != null && basicTermData.getCdfAliasMatchedCount() > 0) {
				score |= 1 << (totalPriorityLevel - 4);
				basicTermData.setExactMatch(1);
			}

			// 5th Priority - ICPC2 code exist
			if (!basicTermData.isCdfOverride() && basicTermData.getIcpc2Code() != null) {
				score |= 1 << (totalPriorityLevel - 5);
			}

			// 6th Priority - Exact match of IAMS full description
			if (!basicTermData.isCdfOverride() && basicTermData.getFullDesc() != null && basicTermData.getFullDesc().toUpperCase().equals(upperCaseKeyword)) {
				score |= 1 << (totalPriorityLevel - 6);
				basicTermData.setExactMatch(1);
			}

			// 7th Priority - Exact match of IAMS alias
			if (!basicTermData.isCdfOverride() && basicTermData.getMatchedAliasCount() != null && basicTermData.getMatchedAliasCount() > 0) {
				score |= 1 << (totalPriorityLevel - 7);
				basicTermData.setExactMatch(1);
			}

			// 7th Priority - Exact match of IAMS short description 
			if (!basicTermData.isCdfOverride() && basicTermData.getShortDesc() != null && basicTermData.getShortDesc().toUpperCase().equals(upperCaseKeyword)) {
				score |= 1 << (totalPriorityLevel - 8);
				basicTermData.setExactMatch(1);
			}

			// 8th Priority - Partial match of CDF name (Modified at 28 Sep 2012)
			if (basicTermData.isCdfOverride() && basicTermData.getClinicalDataId() != null ) {
				score |= 1 << (totalPriorityLevel - 9);
			}

		}
		return score;

	}

}
