package hk.org.ha.iams.termsearch.biz;

import hk.org.ha.iams.termsearch.biz.dto.GetTermDescCriteria;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescResult;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchCriteria;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchResult;
import hk.org.ha.iams.tool.direct.DirectManager;

@DirectManager
public interface TermServiceManager {

    TermSrchResult searchTermCode(TermSrchCriteria criteria);

    GetTermDescResult getTermDesc(GetTermDescCriteria criteria);
}
