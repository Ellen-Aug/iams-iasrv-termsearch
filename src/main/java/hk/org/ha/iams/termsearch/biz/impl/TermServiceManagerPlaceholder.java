package hk.org.ha.iams.termsearch.biz.impl;

import java.sql.Timestamp;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import hk.org.ha.iams.server.AlsNotFoundLog;
import hk.org.ha.iams.termsearch.biz.TermServiceManager;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescCriteria;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescResult;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescReturnStatus;
import hk.org.ha.iams.termsearch.biz.dto.TermReturnStatus;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchCriteria;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchResult;
import hk.org.ha.iams.termsearch.dao.TermServiceDataAccess;

/**
 * Direct-generated-style placeholder. Business SQL/port is the next drop.
 * HTTP contract is always 200; business outcome lives in TermReturnStatus.
 * Search methods are intentionally not {@code @Transactional} (old NOT_SUPPORTED).
 */
@Primary
@Service
public class TermServiceManagerPlaceholder implements TermServiceManager {

    public static final int RETURN_NO_RECORD = 7;

    private final TermServiceDataAccess termServiceDataAccess;
    private final AlsNotFoundLog alsNotFoundLog;

    public TermServiceManagerPlaceholder(TermServiceDataAccess termServiceDataAccess,
            AlsNotFoundLog alsNotFoundLog) {
        this.termServiceDataAccess = termServiceDataAccess;
        this.alsNotFoundLog = alsNotFoundLog;
    }

    @Override
    public TermSrchResult searchTermCode(TermSrchCriteria criteria) {
        TermSrchResult result = new TermSrchResult();
        TermReturnStatus status = new TermReturnStatus();
        status.setReturnCode(RETURN_NO_RECORD);
        status.setReturnMessage("Not implemented: search SQL port pending");
        status.setTimestamp(new Timestamp(System.currentTimeMillis()));
        status.setCount(0);
        result.setTermStatus(status);
        return result;
    }

    @Override
    public GetTermDescResult getTermDesc(GetTermDescCriteria criteria) {
        alsNotFoundLog.warnHttp404("getTermDesc stub — DAO not ported yet");
        GetTermDescResult result = new GetTermDescResult();
        GetTermDescReturnStatus status = new GetTermDescReturnStatus();
        status.setReturnCode(RETURN_NO_RECORD);
        status.setReturnMessage("Not implemented: getTermDesc SQL port pending");
        status.setTimestamp(new Timestamp(System.currentTimeMillis()));
        status.setCount(0);
        result.setStatus(status);
        return result;
    }
}
