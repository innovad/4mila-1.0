package com.rtiming.server.race;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.TypeCastUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.AbstractJPALookupService;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPACriteriaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcard_;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey_;
import com.rtiming.shared.dao.RtPunchSession_;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceKey_;
import com.rtiming.shared.dao.RtRace_;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUc_;
import com.rtiming.shared.race.IRaceLookupService;
import com.rtiming.shared.race.RaceLookupCall;

public class RaceLookupService extends AbstractJPALookupService implements IRaceLookupService {

  CriteriaBuilder b = JPA.getCriteriaBuilder();
  CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
  Root<RtRace> race = selectQuery.from(RtRace.class);
  Join<RtRace, RtRunner> joinRunner = race.join(RtRace_.rtRunner, JoinType.LEFT);
  Join<RtRace, RtEcard> joinEcard = race.join(RtRace_.rtEcard, JoinType.LEFT);
  Join<RtRace, RtUc> joinLegClass = race.join(RtRace_.rtLegClassUc, JoinType.LEFT);
  Join<RtRace, RtPunchSession> joinPunchSession = race.join(RtRace_.rtPunchSessions, JoinType.LEFT);

  Expression<String> displayString = b.concat(b.concat(b.concat(b.concat(b.coalesce(b.concat(race.get(RtRace_.bibNo), " "), ""), JPACriteriaUtility.runnerNameJPA(joinRunner)), b.concat(" - ", b.coalesce(joinEcard.get(RtEcard_.ecardNo), "<" + TEXTS.get("NoECard") + ">"))), b.coalesce(b.concat(" (", b.concat(joinLegClass.get(RtUc_.shortcut), ")")), "")), b.concat(", ", getDownloadedStatusString()));

  Expression<String> searchText = JPACriteriaUtility.removeDefaultTokens(b.concat(displayString, JPACriteriaUtility.runnerNameTokenPlain(joinRunner)));

  protected Expression<String> getDownloadedStatusString() {
    String notDownloadedText = TEXTS.get("NotDownloaded");
    String downloadedText = TEXTS.get("Downloaded");

    return b.selectCase().when(b.isNull(joinPunchSession.get(RtPunchSession_.id).get(RtPunchSessionKey_.punchSessionNr)), notDownloadedText).otherwise(downloadedText).as(String.class);
  }

  @Override
  protected Predicate getKeyWhere(Object key) throws ProcessingException {
    return b.equal(race.get(RtRace_.id).get(RtRaceKey_.raceNr), key);
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
    Long eventNr = ((RaceLookupCall) call).getEventNr();
    /* if master event field is set, restrict to races of event */
    Object master = ((RaceLookupCall) call).getMaster();
    if (master != null) {
      eventNr = TypeCastUtility.castValue(master, Long.class);
    }

    selectQuery.select(b.array(race.get(RtRace_.id).get(RtRaceKey_.raceNr), displayString, b.literal(""), b.literal(""), b.literal(""), b.literal(""), joinPunchSession.get(RtPunchSession_.id).get(RtPunchSessionKey_.punchSessionNr))).where(b.and(b.equal(race.get(RtRace_.id).get(RtRaceKey_.clientNr), ServerSession.get().getSessionClientNr()), eventNr == null ? b.conjunction() : b.equal(race.get(RtRace_.eventNr), eventNr), additionalPredicate == null ? b.conjunction() : additionalPredicate)).orderBy(b.desc(joinPunchSession.get(RtPunchSession_.id).get(RtPunchSessionKey_.punchSessionNr)), b.asc(displayString));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    for (Object[] row : resultList) {
      Long punchSessionNr = TypeCastUtility.castValue(row[6], Long.class);
      row[6] = punchSessionNr == null ? "bold" : "normal";
    }
    return resultList;
  }

  @Override
  protected int getConfiguredSortColumn() {
    return Integer.MIN_VALUE;
  }

}
