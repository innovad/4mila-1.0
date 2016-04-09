package com.rtiming.server.ranking;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateRankingEventPermission;
import com.rtiming.shared.common.security.permission.ReadRankingEventPermission;
import com.rtiming.shared.common.security.permission.UpdateRankingEventPermission;
import com.rtiming.shared.dao.RtRankingEvent;
import com.rtiming.shared.dao.RtRankingEventKey;
import com.rtiming.shared.dao.RtRankingEventKey_;
import com.rtiming.shared.dao.RtRankingEvent_;
import com.rtiming.shared.ranking.FormulaUtility;
import com.rtiming.shared.ranking.IRankingEventProcessService;
import com.rtiming.shared.ranking.RankingFormatCodeType;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

public class RankingEventProcessService  implements IRankingEventProcessService {

  @Override
  public RtRankingEvent prepareCreate(RtRankingEvent bean) throws ProcessingException {
    if (!ACCESS.check(new CreateRankingEventPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }
    if (bean == null) {
      return null;
    }

    bean.setFormulaTypeUid(RankingFormulaTypeCodeType.TimeCode.ID);

    // calculate next sortcode
    long maxSortCode = 0;
    if (bean.getId() != null && bean.getId().getRankingNr() != null) {
      CriteriaBuilder b = JPA.getCriteriaBuilder();
      CriteriaQuery<RtRankingEvent> select = b.createQuery(RtRankingEvent.class);
      Root<RtRankingEvent> rtrankingevent = select.from(RtRankingEvent.class);

      select.where(
          b.and(
              b.equal(rtrankingevent.get(RtRankingEvent_.id).get(RtRankingEventKey_.clientNr), ServerSession.get().getSessionClientNr()),
              b.equal(rtrankingevent.get(RtRankingEvent_.id).get(RtRankingEventKey_.rankingNr), bean.getId().getRankingNr()))
          );
      List<RtRankingEvent> rankingevents = JPA.createQuery(select).getResultList();

      for (RtRankingEvent re : rankingevents) {
        maxSortCode = Math.max(NumberUtility.nvl(re.getSortcode(), 0L), maxSortCode);
      }
    }
    bean.setSortcode(maxSortCode + 1);

    return bean;
  }

  @Override
  public RtRankingEvent create(RtRankingEvent bean) throws ProcessingException {
    if (!ACCESS.check(new CreateRankingEventPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    RtRankingEvent alreadyExists = JPA.find(RtRankingEvent.class, bean.getId());
    if (alreadyExists != null) {
      throw new VetoException(Texts.get("DuplicateKeyMessage"));
    }

    bean = store(bean);
    return bean;
  }

  @Override
  public RtRankingEvent load(RtRankingEventKey key) throws ProcessingException {
    if (!ACCESS.check(new ReadRankingEventPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }
    if (key == null) {
      return null;
    }

    RtRankingEvent bean = JPA.find(RtRankingEvent.class, key);

    bean.setFormulaTypeUid(RankingServerUtility.analyzeFormulaType(bean.getFormula()));
    bean.setTimePrecisionUid(FormulaUtility.decimalPlaces2timePrecision(bean.getDecimalPlaces()));

    return bean;
  }

  @Override
  public RtRankingEvent store(RtRankingEvent bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateRankingEventPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    if (CompareUtility.equals(bean.getFormatUid(), RankingFormatCodeType.TimeCode.ID)) {
      bean.setDecimalPlaces(FormulaUtility.timePrecision2decimalPlaces(bean.getTimePrecisionUid()));
    }

    JPA.merge(bean);

    return bean;
  }

  @Override
  public void delete(RtRankingEventKey key) throws ProcessingException {
    if (key != null) {
      RtRankingEvent rankingEvent = load(key);
      if (rankingEvent != null) {
        JPA.remove(rankingEvent);
      }
    }
  }

}
