package hk.org.ha.iams.termsearch.controller.impl;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import hk.org.ha.iams.termsearch.biz.TermServiceManager;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescCriteria;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescResult;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchCriteria;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchResult;
import hk.org.ha.iams.termsearch.controller.TermServiceApi;

@RestController
@RequestMapping("/iams/api/termsearch")
public class TermServiceApiPlaceholder implements TermServiceApi {

    private final TermServiceManager termServiceManager;

    public TermServiceApiPlaceholder(TermServiceManager termServiceManager) {
        this.termServiceManager = termServiceManager;
    }

    @Override
    public TermSrchResult searchTermCode(TermSrchCriteria criteria) {
        return termServiceManager.searchTermCode(criteria);
    }

    @Override
    public GetTermDescResult getTermDesc(GetTermDescCriteria criteria) {
        return termServiceManager.getTermDesc(criteria);
    }
}
