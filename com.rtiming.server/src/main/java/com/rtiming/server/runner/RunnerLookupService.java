package com.rtiming.server.runner;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.AbstractJPALookupService;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPACriteriaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClub_;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcard_;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunnerKey_;
import com.rtiming.shared.dao.RtRunner_;
import com.rtiming.shared.runner.IRunnerLookupService;
import com.rtiming.shared.runner.RunnerLookupCall;

public class RunnerLookupService extends AbstractJPALookupService implements IRunnerLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtRunner> runner = selectQuery.from(RtRunner.class);
  Join<RtRunner, RtClub> joinClub = runner.join(RtRunner_.rtClub, JoinType.LEFT);
  Join<RtRunner, RtEcard> joinEcard = runner.join(RtRunner_.rtEcard, JoinType.LEFT);

  Expression<String> name = JPACriteriaUtility.runnerNameJPA(runner);
  Expression<String> displayText = b.concat(b.concat(name, b.coalesce(b.concat(" (", b.concat(joinClub.get(RtClub_.name), ")")), "")), b.coalesce(b.concat(" - ", joinEcard.get(RtEcard_.ecardNo)), ""));
  Expression<String> searchText = JPACriteriaUtility.removeDefaultTokens(b.concat(displayText, JPACriteriaUtility.runnerNameTokenPlain(runner)));

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(runner.get(RtRunner_.id).get(RtRunnerKey_.runnerNr), key);
  }

  @Override
  protected Predicate getTextWhere(String text, ILookupCall call) throws ProcessingException {
    text = JPACriteriaUtility.removeDefaultTokens(text);
    return b.like(b.upper(searchText), text);
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.RUNNER;
  }

  @Override
  protected List<Object[]> fetchLookupRows(Predicate additionalPredicate, ILookupCall call) {
    boolean showNameOnly = ((RunnerLookupCall) call).isShowNameOnly();

    selectQuery.select(b.array(runner.get(RtRunner_.id).get(RtRunnerKey_.runnerNr), showNameOnly ? name : displayText)).where(b.and(b.equal(runner.get(RtRunner_.id).get(RtRunnerKey_.clientNr), ServerSession.get().getSessionClientNr()), additionalPredicate == null ? b.conjunction() : additionalPredicate));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

}
