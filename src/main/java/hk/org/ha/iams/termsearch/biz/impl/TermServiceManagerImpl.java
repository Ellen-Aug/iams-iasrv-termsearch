package hk.org.ha.iams.termsearch.biz.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import hk.org.ha.iams.server.AlsNotFoundLog;
import hk.org.ha.iams.termsearch.biz.TermServiceCallAuditLog;
import hk.org.ha.iams.termsearch.biz.TermServiceManager;
import hk.org.ha.iams.termsearch.biz.dto.Alias;
import hk.org.ha.iams.termsearch.biz.dto.AssoEntity;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescCriteria;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescResult;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescReturnStatus;
import hk.org.ha.iams.termsearch.biz.dto.ICD102010MBD;
import hk.org.ha.iams.termsearch.biz.dto.ICD9Dx;
import hk.org.ha.iams.termsearch.biz.dto.ICD9Px;
import hk.org.ha.iams.termsearch.biz.dto.ICPC2;
import hk.org.ha.iams.termsearch.biz.dto.Snomedct;
import hk.org.ha.iams.termsearch.biz.dto.TermDesc;
import hk.org.ha.iams.termsearch.biz.dto.TermReturnStatus;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.CodeType;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchCriteria;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchRecord;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchResult;
import hk.org.ha.iams.termsearch.constant.TermServiceLookupValueConstants;
import hk.org.ha.iams.termsearch.constant.TermServiceMessages;
import hk.org.ha.iams.termsearch.dao.LookupValueCache;
import hk.org.ha.iams.termsearch.dao.TermServiceDataAccess;
import hk.org.ha.iams.termsearch.dao.vo.BasicTermDataVO;
import hk.org.ha.iams.termsearch.exception.ServiceException;
import hk.org.ha.iams.termsearch.util.TermCollections;
import hk.org.ha.iams.termsearch.util.TermServiceUtils;
import hk.org.ha.iams.termsearch.util.TermStrings;
import hk.org.ha.iams.termsearch.validation.ReturnMessage.Error;
import hk.org.ha.iams.termsearch.validation.ValidateResult;

/**
 * REST manager. Same return codes as the EJB bean. Validation/not-found stay HTTP 200
 * in the JSON body (no SOAP fault). Search is not {@code @Transactional}.
 */
@Service
public class TermServiceManagerImpl implements TermServiceManager {

    private static final Logger logger = LoggerFactory.getLogger(TermServiceManagerImpl.class);

    private final TermServiceDataAccess termDataAccess;
    private final LookupValueCache lookupValueCache;
    private final AlsNotFoundLog alsNotFoundLog;
    private final TermServiceCallAuditLog auditLog;
    private final boolean serviceEnabled;

    public TermServiceManagerImpl(TermServiceDataAccess termDataAccess,
            LookupValueCache lookupValueCache,
            AlsNotFoundLog alsNotFoundLog,
            TermServiceCallAuditLog auditLog,
            @Value("${termsearch.enabled:true}") boolean serviceEnabled) {
        this.termDataAccess = termDataAccess;
        this.lookupValueCache = lookupValueCache;
        this.alsNotFoundLog = alsNotFoundLog;
        this.auditLog = auditLog;
        this.serviceEnabled = serviceEnabled;
    }

    @Override
    public GetTermDescResult getTermDesc(GetTermDescCriteria criteria) {
        GetTermDescResult result = new GetTermDescResult();
        GetTermDescReturnStatus returnStatus = new GetTermDescReturnStatus();
        List<TermDesc> termDescs = null;
        ServiceException serviceException = null;
        String auditSession = auditLog.newSession();
        auditLog.getDescRequest(auditSession, criteria);

        try {
            if (!serviceEnabled) {
                returnStatus.setReturnCode(Error.WARNING_SERVICE_UNAVAILABLE.ordinal());
                returnStatus.setReturnMessage(TermServiceMessages.ERROR_SERVICE_UNAVAILABLE);
                returnStatus.setCount(0);
                returnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
            } else {
                ValidateResult validateResult = isValidGetTermDescCriteria(criteria);
                if (validateResult.isValid()) {
                    if (!TermCollections.isEmpty(criteria.getInTermIDs())) {
                        termDescs = termDataAccess.getTermDescById(criteria.getInTermIDs());
                    } else if (!TermCollections.isEmpty(criteria.getInTermCodes())) {
                        termDescs = termDataAccess.getTermDescByCode(criteria.getInTermCodes());
                    }

                    boolean isAllFound = true;
                    if (termDescs != null) {
                        for (TermDesc termDesc : termDescs) {
                            if (!termDesc.isFound()) {
                                isAllFound = false;
                                break;
                            }
                        }
                    }

                    if (isAllFound) {
                        returnStatus.setReturnCode(Error.SUCCESS.ordinal());
                        returnStatus.setReturnMessage(TermServiceMessages.SUCCESS);
                    } else {
                        returnStatus.setReturnCode(Error.ERROR_SOME_RECORDS_NOT_FOUND.ordinal());
                        returnStatus.setReturnMessage(TermServiceMessages.ERROR_MISSING_RECORD);
                        alsNotFoundLog.warnHttp404("getTermDesc some records not found");
                    }
                    returnStatus.setCount(termDescs == null ? 0 : termDescs.size());
                    returnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
                } else {
                    returnStatus.setReturnCode(validateResult.getError().ordinal());
                    returnStatus.setReturnMessage(validateResult.getErrorMessage());
                    returnStatus.setCount(0);
                    returnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
                }
            }
        } catch (ServiceException e) {
            serviceException = e;
            returnStatus.setReturnCode(e.getCode() == null
                    ? Error.FAIL_UNKNOWN_EXCEPTION.ordinal()
                    : e.getCode());
            returnStatus.setReturnMessage(e.getMessage());
            returnStatus.setCount(0);
            returnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
        } catch (Exception e) {
            logger.error("Failure in getting term description", e);
            serviceException = new ServiceException(Error.FAIL_UNKNOWN_EXCEPTION.ordinal(),
                    TermServiceMessages.ERROR_EXCEPTION);
            returnStatus.setReturnCode(Error.FAIL_UNKNOWN_EXCEPTION.ordinal());
            returnStatus.setReturnMessage(TermServiceMessages.ERROR_EXCEPTION);
            returnStatus.setCount(0);
            returnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
        }

        result.setStatus(returnStatus);
        result.setTermDescs(termDescs);
        auditLog.getDescResponse(auditSession, criteria, result, serviceException);
        return result;
    }

    @Override
    public TermSrchResult searchTermCode(TermSrchCriteria termSrchCriteria) {
        TermSrchResult termSrchResult = new TermSrchResult();
        TermReturnStatus termReturnStatus = new TermReturnStatus();
        List<TermSrchRecord> termSrchRecords = null;
        ServiceException serviceException = null;
        String auditSession = auditLog.newSession();
        auditLog.searchRequest(auditSession, termSrchCriteria);

        try {
            if (!serviceEnabled) {
                termReturnStatus.setReturnCode(Error.WARNING_SERVICE_UNAVAILABLE.ordinal());
                termReturnStatus.setReturnMessage(TermServiceMessages.ERROR_SERVICE_UNAVAILABLE);
                termReturnStatus.setCount(0);
                termReturnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
            } else {
                ValidateResult validateResult = isValidTermSrchCriteria(termSrchCriteria);
                if (validateResult.isValid()) {
                    List<BasicTermDataVO> basicTermDataList = termDataAccess.findTermBasicData(termSrchCriteria);

                    if (!TermCollections.isEmpty(basicTermDataList)) {
                        termSrchRecords = mapRecords(termSrchCriteria, basicTermDataList);
                        termReturnStatus.setReturnCode(Error.SUCCESS.ordinal());
                        termReturnStatus.setReturnMessage(TermServiceMessages.SUCCESS);
                        termReturnStatus.setCount(termSrchRecords.size());
                        termReturnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
                    } else {
                        termReturnStatus.setReturnCode(Error.ERROR_NO_RECORD_FOUND.ordinal());
                        termReturnStatus.setReturnMessage(TermServiceMessages.ERROR_NO_RECORD);
                        termReturnStatus.setCount(0);
                        termReturnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
                    }
                } else {
                    termReturnStatus.setReturnCode(validateResult.getError().ordinal());
                    termReturnStatus.setReturnMessage(validateResult.getErrorMessage());
                    termReturnStatus.setCount(0);
                    termReturnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
                }
            }
        } catch (ServiceException e) {
            serviceException = e;
            if (e.getCode() != null && e.getCode().equals(Error.ERROR_EXCEEDS_RECORDS_LIMIT.ordinal())) {
                termReturnStatus.setReturnCode(e.getCode());
                termReturnStatus.setReturnMessage(e.getMessage());
                termReturnStatus.setCount(0);
                termReturnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
            } else {
                termReturnStatus.setReturnCode(e.getCode() == null
                        ? Error.FAIL_UNKNOWN_EXCEPTION.ordinal()
                        : e.getCode());
                termReturnStatus.setReturnMessage(e.getMessage());
                termReturnStatus.setCount(0);
                termReturnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
            }
        } catch (Exception e) {
            logger.error("Failure in searching term code", e);
            serviceException = new ServiceException(Error.FAIL_UNKNOWN_EXCEPTION.ordinal(),
                    TermServiceMessages.ERROR_EXCEPTION);
            termReturnStatus.setReturnCode(Error.FAIL_UNKNOWN_EXCEPTION.ordinal());
            termReturnStatus.setReturnMessage(TermServiceMessages.ERROR_EXCEPTION);
            termReturnStatus.setCount(0);
            termReturnStatus.setTimestamp(new Timestamp(System.currentTimeMillis()));
        }

        termSrchResult.setTermStatus(termReturnStatus);
        termSrchResult.setTermSrchRecords(termSrchRecords);
        auditLog.searchResponse(auditSession, termSrchCriteria, termSrchResult, serviceException);
        return termSrchResult;
    }

    private List<TermSrchRecord> mapRecords(TermSrchCriteria termSrchCriteria, List<BasicTermDataVO> basicTermDataList)
            throws ServiceException {
        List<TermSrchRecord> termSrchRecords = new ArrayList<>();
        int recordCount = 0;
        for (BasicTermDataVO basicTermData : basicTermDataList) {
            TermSrchRecord termSrchRecord = new TermSrchRecord();
            termSrchRecord.setDisplaySeq(recordCount++);

            if (!basicTermData.isCdfOverride()) {
                termSrchRecord.setTermId(basicTermData.getTermId());
                termSrchRecord.setNature(basicTermData.getNature());
                termSrchRecord.setFullDesc(basicTermData.getFullDesc());
                termSrchRecord.setShortDesc(basicTermData.getShortDesc());
                termSrchRecord.setStatus(basicTermData.getStatus());
                termSrchRecord.setSex(basicTermData.getSex());
                termSrchRecord.setAge(basicTermData.getAge());
                termSrchRecord.setPrincipalCdFlag(basicTermData.getPrincipalCdFlag());

                if (basicTermData.getSystemUsedCount() != null && basicTermData.getSystemUsedCount() > 0) {
                    List<Integer> sysUseds = termDataAccess.getTermSystemUsed(basicTermData.getTermKey());
                    List<String> sysUsedDescs = null;
                    if (sysUseds != null && !sysUseds.isEmpty()) {
                        sysUsedDescs = new ArrayList<>();
                        for (Integer sysUsed : sysUseds) {
                            sysUsedDescs.add(lookupValueCache.getValueByValueKey(sysUsed).getDataValue());
                        }
                    }
                    termSrchRecord.setSysUseds(sysUsedDescs);
                }
            } else {
                termSrchRecord.setClinicalDataId(basicTermData.getClinicalDataId());
                termSrchRecord.setDiagOrProc(basicTermData.getCdfNature());
                termSrchRecord.setFullDesc(basicTermData.getCdfDescription());
                termSrchRecord.setShortDesc(basicTermData.getCdfDescription());
                termSrchRecord.setSex(basicTermData.getCdfSex());
                termSrchRecord.setAge(basicTermData.getCdfAge());
                termSrchRecord.setStatus(lookupValueCache.getValueByValueKey(
                        TermServiceLookupValueConstants.STATUS_ACTIVE).getDataValue());
                if (basicTermData.getCdfStatus() != null) {
                    termSrchRecord.setStatus(basicTermData.getCdfStatus());
                }
            }

            if (termSrchCriteria.getIn_keyword() != null && termSrchRecord.getFullDesc() != null
                    && termSrchCriteria.getIn_keyword().equalsIgnoreCase(termSrchRecord.getFullDesc())) {
                termSrchRecord.setExactMatch(true);
            }

            List<CodeType> outputCodeTypes = TermServiceUtils.getOutputCodeTypes(termSrchCriteria);
            if (!basicTermData.isCdfOverride() && !TermCollections.isEmpty(outputCodeTypes)) {
                for (CodeType codeType : outputCodeTypes) {
                    if (codeType.equals(CodeType.ICD9Dx) && basicTermData.getIcd9dxCode() != null) {
                        ICD9Dx icd9dx = new ICD9Dx(basicTermData.getIcd9dxCode(), basicTermData.getIcd9dxExtension());
                        if (basicTermData.getAssoIcd9dxCount() != null && basicTermData.getAssoIcd9dxCount() > 0) {
                            icd9dx.setHasAssociatedCode(true);
                            if (termSrchCriteria.isOut_associatedCode()) {
                                icd9dx.setAssoTermCodes(termDataAccess.getAssoTermCode(codeType, basicTermData.getTermKey()));
                            }
                        }
                        termSrchRecord.setIcd9Dx(icd9dx);
                    } else if (codeType.equals(CodeType.ICD9Px) && basicTermData.getIcd9pxCode() != null) {
                        ICD9Px icd9px = new ICD9Px(basicTermData.getIcd9pxCode(), basicTermData.getIcd9pxExtension(),
                                basicTermData.getIcd9pxWeight());
                        if (basicTermData.getAssoIcd9pxCount() != null && basicTermData.getAssoIcd9pxCount() > 0) {
                            icd9px.setHasAssociatedCode(true);
                            if (termSrchCriteria.isOut_associatedCode()) {
                                icd9px.setAssoTermCodes(termDataAccess.getAssoTermCode(codeType, basicTermData.getTermKey()));
                            }
                        }
                        termSrchRecord.setIcd9Px(icd9px);
                    } else if (codeType.equals(CodeType.ICD102010MBD) && basicTermData.getIcd102010MBDCode() != null) {
                        ICD102010MBD icd102010MBD = new ICD102010MBD(basicTermData.getIcd102010MBDCode());
                        if (basicTermData.getAssoIcd102010MBDCount() != null
                                && basicTermData.getAssoIcd102010MBDCount() > 0) {
                            icd102010MBD.setHasAssociatedCode(true);
                            if (termSrchCriteria.isOut_associatedCode()) {
                                icd102010MBD.setAssoTermCodes(
                                        termDataAccess.getAssoTermCode(codeType, basicTermData.getTermKey()));
                            }
                        }
                        termSrchRecord.setIcd102010MBD(icd102010MBD);
                    } else if (codeType.equals(CodeType.SNOMEDCT)
                            && basicTermData.getSnomedctCount() != null && basicTermData.getSnomedctCount() > 0) {
                        List<Snomedct> snomedcts = termDataAccess.getSnomedctCode(basicTermData.getTermKey());
                        termSrchRecord.setSnomedcts(snomedcts);
                    } else if (codeType.equals(CodeType.ICPC2) && basicTermData.getIcpc2Code() != null) {
                        ICPC2 icpc2 = new ICPC2(basicTermData.getIcpc2Code());
                        if (basicTermData.getAssoIcpc2Count() != null && basicTermData.getAssoIcpc2Count() > 0) {
                            icpc2.setHasAssociatedCode(true);
                            if (termSrchCriteria.isOut_associatedCode()) {
                                icpc2.setAssoTermCodes(termDataAccess.getAssoTermCode(codeType, basicTermData.getTermKey()));
                            }
                        }
                        termSrchRecord.setIcpc2(icpc2);
                    }
                }
            }

            if (!basicTermData.isCdfOverride() && termSrchCriteria.isOut_iamsInfo()) {
                termSrchRecord.setRemarks(basicTermData.getRemarks());
                termSrchRecord.setRequestUser(basicTermData.getRequestUser());
                termSrchRecord.setRequestHospCd(basicTermData.getRequestHospCd());
                termSrchRecord.setCreateUser(basicTermData.getCreateUser());
                termSrchRecord.setCreateDtm(basicTermData.getCreateDtm());
                termSrchRecord.setUpdateUser(basicTermData.getUpdateUser());
                termSrchRecord.setUpdateDtm(basicTermData.getUpdateDtm());
            }

            if (!basicTermData.isCdfOverride() && termSrchCriteria.isOut_alias()) {
                List<Alias> aliases = termDataAccess.getTermAlias(basicTermData.getTermKey());
                if (!TermCollections.isEmpty(aliases)) {
                    termSrchRecord.setAliases(aliases);
                }
            }

            if (!basicTermData.isCdfOverride() && termSrchCriteria.isOut_associatedEntity()) {
                List<AssoEntity> associatedEntities = termDataAccess.getAssociatedEntities(basicTermData.getTermKey());
                if (!TermCollections.isEmpty(associatedEntities)) {
                    termSrchRecord.setAssoEntities(associatedEntities);
                }
            }

            termSrchRecords.add(termSrchRecord);
        }
        return termSrchRecords;
    }

    private ValidateResult isValidGetTermDescCriteria(GetTermDescCriteria criteria) {
        ValidateResult validateResult = new ValidateResult();
        if (criteria != null) {
            if (TermCollections.isEmpty(criteria.getInTermCodes()) && TermCollections.isEmpty(criteria.getInTermIDs())) {
                validateResult.setError(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS);
                validateResult.setErrorMessage("inTermCodes, inTermIDs are compulsory");
            } else {
                validateResult.setValid(true);
            }
        } else {
            validateResult.setError(Error.EXCEPTION_WITHOUT_INPUT_PARAMETER);
            validateResult.setErrorMessage("criteria object is null");
        }
        return validateResult;
    }

    private ValidateResult isValidTermSrchCriteria(TermSrchCriteria termSrchCriteria) {
        ValidateResult validateResult = new ValidateResult();
        if (termSrchCriteria != null) {
            if (termSrchCriteria.getIn_requestSystem() == null) {
                validateResult.setError(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS);
                validateResult.setErrorMessage(TermServiceMessages.ERROR_MISSING_REQUEST_SYSTEM);
            } else if (TermStrings.isBlank(termSrchCriteria.getIn_keyword())
                    && TermCollections.isEmpty(termSrchCriteria.getIn_termCodes())
                    && TermCollections.isEmpty(termSrchCriteria.getIn_termIds())) {
                validateResult.setError(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS);
                validateResult.setErrorMessage(TermServiceMessages.ERROR_MISSING_KEYWORD_CODES_TERMID);
            } else if (TermCollections.isEmpty(termSrchCriteria.getIn_natures())) {
                validateResult.setError(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS);
                validateResult.setErrorMessage(TermServiceMessages.ERROR_MISSING_NATURE);
            } else if (termSrchCriteria.getOut_refTerm() == null) {
                validateResult.setError(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS);
                validateResult.setErrorMessage(TermServiceMessages.ERROR_MISSING_REFTERM);
            } else if (TermCollections.isEmpty(termSrchCriteria.getOut_codeTypes())) {
                validateResult.setError(Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS);
                validateResult.setErrorMessage(TermServiceMessages.ERROR_MISSING_CODE_TYPE);
            } else {
                validateResult.setValid(true);
            }
        } else {
            validateResult.setError(Error.EXCEPTION_WITHOUT_INPUT_PARAMETER);
            validateResult.setErrorMessage("criteria object is null");
        }
        return validateResult;
    }
}
