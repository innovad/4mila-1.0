package com.rtiming.server.ranking;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.common.security.permission.CreateRankingPermission;
import com.rtiming.shared.common.security.permission.ReadRankingPermission;
import com.rtiming.shared.common.security.permission.UpdateRankingPermission;
import com.rtiming.shared.dao.RtRanking;
import com.rtiming.shared.dao.RtRankingEvent;
import com.rtiming.shared.dao.RtRankingEventKey_;
import com.rtiming.shared.dao.RtRankingEvent_;
import com.rtiming.shared.dao.RtRankingKey;
import com.rtiming.shared.ranking.FormulaUtility;
import com.rtiming.shared.ranking.IRankingProcessService;
import com.rtiming.shared.ranking.RankingFormatCodeType;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

public class RankingProcessService  implements IRankingProcessService {

  @Override
  public RtRanking prepareCreate(RtRanking bean) throws ProcessingException {
    if (!ACCESS.check(new CreateRankingPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }
    if (bean == null) {
      return null;
    }

    bean.setFormulaTypeUid(RankingFormulaTypeCodeType.SumTimeCode.ID);
    return bean;
  }

  @Override
  public RtRanking create(RtRanking bean) throws ProcessingException {
    if (!ACCESS.check(new CreateRankingPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    // new key
    bean.setId(RtRankingKey.create(bean.getId()));

    bean = store(bean);

    return bean;
  }

  @Override
  public RtRanking load(RtRankingKey key) throws ProcessingException {
    if (!ACCESS.check(new ReadRankingPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }
    if (key == null) {
      return null;
    }

    key = RtRankingKey.create(key);
    RtRanking bean = JPA.find(RtRanking.class, key);

    bean.setFormulaTypeUid(RankingServerUtility.analyzeFormulaType(bean.getFormula()));
    bean.setTimePrecisionUid(FormulaUtility.decimalPlaces2timePrecision(bean.getDecimalPlaces()));
    return bean;
  }

  @Override
  public RtRanking store(RtRanking bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateRankingPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    if (CompareUtility.equals(bean.getFormatUid(), RankingFormatCodeType.TimeCode.ID)) {
      bean.setDecimalPlaces(FormulaUtility.timePrecision2decimalPlaces(bean.getTimePrecisionUid()));
    }

    JPA.merge(bean);

    return bean;
  }

  @Override
  public void delete(RtRankingKey key) throws ProcessingException {
    if (key != null) {
      CriteriaBuilder b = JPA.getCriteriaBuilder();
      CriteriaQuery<RtRankingEvent> select = b.createQuery(RtRankingEvent.class);
      Root<RtRankingEvent> rtrankingevent = select.from(RtRankingEvent.class);

      select.where(
          b.and(
              b.equal(rtrankingevent.get(RtRankingEvent_.id).get(RtRankingEventKey_.clientNr), ServerSession.get().getSessionClientNr()),
              b.equal(rtrankingevent.get(RtRankingEvent_.id).get(RtRankingEventKey_.rankingNr), key.getId()))
          );
      List<RtRankingEvent> rankingevents = JPA.createQuery(select).getResultList();

      for (RtRankingEvent re : rankingevents) {
        JPA.remove(re);
      }

      RtRanking map = load(key);
      if (map != null) {
        JPA.remove(map);
      }
    }
  }
}
