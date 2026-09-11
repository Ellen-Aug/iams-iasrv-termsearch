package hk.org.ha.iams.termsearch.dao;

import java.util.List;

import hk.org.ha.iams.termsearch.biz.dto.Alias;
import hk.org.ha.iams.termsearch.biz.dto.AssoEntity;
import hk.org.ha.iams.termsearch.biz.dto.AssoTermCode;
import hk.org.ha.iams.termsearch.biz.dto.GetTermDescCode;
import hk.org.ha.iams.termsearch.biz.dto.Snomedct;
import hk.org.ha.iams.termsearch.biz.dto.TermDesc;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.CodeType;
import hk.org.ha.iams.termsearch.biz.dto.TermServiceConstants.RequestSystem;
import hk.org.ha.iams.termsearch.biz.dto.TermSrchCriteria;
import hk.org.ha.iams.termsearch.dao.vo.BasicTermDataVO;
import hk.org.ha.iams.termsearch.exception.ServiceException;
import hk.org.ha.iams.tool.direct.DirectDataAccess;

@DirectDataAccess
public interface TermServiceDataAccess {

    List<BasicTermDataVO> findTermBasicData(TermSrchCriteria criteria) throws ServiceException;

    List<Integer> getTermSystemUsed(Integer termKey);

    List<Alias> getTermAlias(Integer termKey);

    List<AssoEntity> getAssociatedEntities(Integer termKey);

    List<AssoTermCode> getAssoTermCode(CodeType codeType, Integer termKey);

    List<Snomedct> getSnomedctCode(Integer termKey);

    Integer getSortingScore(BasicTermDataVO basicTermData, String keyword, RequestSystem requestSystem);

    List<TermDesc> getTermDescByCode(List<GetTermDescCode> termCodes) throws ServiceException;

    List<TermDesc> getTermDescById(List<Integer> termIDs);
}
