package hk.org.ha.iams.termsearch.biz;

import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

import hk.org.ha.iams.termsearch.biz.dto.GetTermDescCriteria;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescResult;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchCriteria;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchResult;
import hk.org.ha.iams.termsearch.exception.ServiceException;
import hk.org.ha.iams.termsearch.util.TermCollections;
import hk.org.ha.iams.termsearch.validation.ReturnMessage.Error;

/**
 * Cloud stand-in for EJB {@code addTermSrchAuditLog} / {@code addGetTermDescAuditLog}.
 * No IAMS audit JAR / WebLogic ECID. Writes structured SLF4J lines.
 */
@Component
public class TermServiceCallAuditLog {

    public static final String METHOD_TERM_SRCH = "TerminologySearch";
    public static final String METHOD_GET_DESC = "GetTerminologyDesc";

    private static final Logger LOG = LoggerFactory.getLogger(TermServiceCallAuditLog.class);

    public String newSession() {
        return UUID.randomUUID().toString();
    }

    public void searchRequest(String session, TermSrchCriteria criteria) {
        String requestKey = criteria == null || criteria.getIn_requestKey() == null
                ? "nil" : criteria.getIn_requestKey();
        putMdc(session, requestKey, METHOD_TERM_SRCH);
        int codeCount = criteria == null || TermCollections.isEmpty(criteria.getIn_termCodes())
                ? 0 : criteria.getIn_termCodes().size();
        int idCount = criteria == null || TermCollections.isEmpty(criteria.getIn_termIds())
                ? 0 : criteria.getIn_termIds().size();
        boolean hasKeyword = criteria != null && criteria.getIn_keyword() != null && !criteria.getIn_keyword().isBlank();
        LOG.info("AUDIT Request service={} session={} requestKey={} requestSystem={} keyword={} termCodes={} termIds={}",
                METHOD_TERM_SRCH, session, requestKey,
                criteria == null ? null : criteria.getIn_requestSystem(),
                hasKeyword, codeCount, idCount);
    }

    public void searchResponse(String session, TermSrchCriteria criteria, TermSrchResult result,
            ServiceException serviceException) {
        String requestKey = criteria == null || criteria.getIn_requestKey() == null
                ? "nil" : criteria.getIn_requestKey();
        Integer returnCode = serviceException != null && serviceException.getCode() != null
                ? serviceException.getCode()
                : (result == null || result.getTermStatus() == null ? null : result.getTermStatus().getReturnCode());
        Integer count = result == null || result.getTermStatus() == null ? 0 : result.getTermStatus().getCount();
        LOG.info("AUDIT Response service={} session={} requestKey={} status={} returnCode={} count={} detail={}",
                METHOD_TERM_SRCH, session, requestKey, auditStatus(returnCode), returnCode, count,
                serviceException == null ? (result == null || result.getTermStatus() == null
                        ? null : result.getTermStatus().getReturnMessage())
                        : serviceException.getMessage());
        clearMdc();
    }

    public void getDescRequest(String session, GetTermDescCriteria criteria) {
        String requestKey = criteria == null || criteria.getInRequestKey() == null
                ? "nil" : criteria.getInRequestKey();
        putMdc(session, requestKey, METHOD_GET_DESC);
        int codeCount = criteria == null || TermCollections.isEmpty(criteria.getInTermCodes())
                ? 0 : criteria.getInTermCodes().size();
        int idCount = criteria == null || TermCollections.isEmpty(criteria.getInTermIDs())
                ? 0 : criteria.getInTermIDs().size();
        LOG.info("AUDIT Request service={} session={} requestKey={} termCodes={} termIds={}",
                METHOD_GET_DESC, session, requestKey, codeCount, idCount);
    }

    public void getDescResponse(String session, GetTermDescCriteria criteria, GetTermDescResult result,
            ServiceException serviceException) {
        String requestKey = criteria == null || criteria.getInRequestKey() == null
                ? "nil" : criteria.getInRequestKey();
        Integer returnCode = serviceException != null && serviceException.getCode() != null
                ? serviceException.getCode()
                : (result == null || result.getStatus() == null ? null : result.getStatus().getReturnCode());
        Integer count = result == null || result.getStatus() == null ? 0 : result.getStatus().getCount();
        LOG.info("AUDIT Response service={} session={} requestKey={} status={} returnCode={} count={} detail={}",
                METHOD_GET_DESC, session, requestKey, auditStatus(returnCode), returnCode, count,
                serviceException == null ? (result == null || result.getStatus() == null
                        ? null : result.getStatus().getReturnMessage())
                        : serviceException.getMessage());
        clearMdc();
    }

    private static String auditStatus(Integer returnCode) {
        if (returnCode == null) {
            return "FAIL";
        }
        if (returnCode == Error.SUCCESS.ordinal()
                || returnCode == Error.ERROR_NO_RECORD_FOUND.ordinal()
                || returnCode == Error.ERROR_SOME_RECORDS_NOT_FOUND.ordinal()
                || returnCode == Error.ERROR_EXCEEDS_RECORDS_LIMIT.ordinal()
                || returnCode == Error.EXCEPTION_MISSING_COMPULSORY_INPUT_PARAMETERS.ordinal()
                || returnCode == Error.EXCEPTION_WITHOUT_INPUT_PARAMETER.ordinal()
                || returnCode == Error.WARNING_SERVICE_UNAVAILABLE.ordinal()) {
            return "SUCCESS";
        }
        return "FAIL";
    }

    private static void putMdc(String session, String requestKey, String service) {
        MDC.put("auditSession", session);
        MDC.put("requestKey", requestKey);
        MDC.put("serviceName", service);
    }

    private static void clearMdc() {
        MDC.remove("auditSession");
        MDC.remove("requestKey");
        MDC.remove("serviceName");
    }
}
