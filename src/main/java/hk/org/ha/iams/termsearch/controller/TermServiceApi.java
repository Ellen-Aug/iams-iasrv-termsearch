package hk.org.ha.iams.termsearch.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import hk.org.ha.iams.termsearch.biz.dto.GetTermDescCriteria;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescResult;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchCriteria;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchResult;
import hk.org.ha.iams.tool.direct.DirectApi;
import io.swagger.v3.oas.annotations.tags.Tag;

@DirectApi
@Tag(name = "termsearch")
@RequestMapping("/iams/api/termsearch")
public interface TermServiceApi {

    @PostMapping("/searchTermCode")
    TermSrchResult searchTermCode(@RequestBody TermSrchCriteria criteria);

    @PostMapping("/getTermDesc")
    GetTermDescResult getTermDesc(@RequestBody GetTermDescCriteria criteria);
}
