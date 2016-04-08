package com.rtiming.server.entry.startlist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.TypeCastUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.entry.startlist.StartlistSeparationUtility.Separation;
import com.rtiming.server.settings.addinfo.AdditionalInformationDatabaseUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourse_;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClass_;
import com.rtiming.shared.dao.RtParticipation;
import com.rtiming.shared.dao.RtParticipationKey_;
import com.rtiming.shared.dao.RtParticipation_;
import com.rtiming.shared.dao.RtStartlistSettingVacant;
import com.rtiming.shared.dao.RtStartlistSettingVacantKey;
import com.rtiming.shared.entry.startlist.IStartlistService;
import com.rtiming.shared.entry.startlist.IStartlistSettingProcessService;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;
import com.rtiming.shared.entry.startlist.StartlistSettingUtility;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;

public class StartlistService  implements IStartlistService {

  @Override
  public void createStartlists(Long[] startlistSettingNrs) throws ProcessingException {
    // remove existing vacants for all selected classes/courses
    StartlistVacantUtility.removeVacants(startlistSettingNrs);

    List<StartlistSettingBean> completeStartlists = new ArrayList<StartlistSettingBean>();

    // load participations and shuffle blocks
    for (Long startlistSettingNr : startlistSettingNrs) {
      StartlistSettingBean startlist = createSingleStartlist(startlistSettingNr);
      completeStartlists.add(startlist);
    }

    // handle global splitting / grouping by registration
    setStartTimesAndBibNo(completeStartlists); // 1st assignment, only used to calculate grouping, splitting
    StartlistRegistrationGroupingUtility.applyGroupingRules(completeStartlists);

    // apply vacants and separation
    applySeparationsAndVacants(completeStartlists);

    // set startimes and bib numbers
    setStartTimesAndBibNo(completeStartlists); // 2nd and final assignment

    // store starttimes and bib no to database
    storeStarttimes(completeStartlists);
  }

  @Override
  public boolean existsRaceWithStartTime(Long[] startlistSettingNrs) throws ProcessingException {
    if (startlistSettingNrs == null || startlistSettingNrs.length == 0) {
      return false;
    }

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Long> selectQuery = b.createQuery(Long.class);
    Root<RtParticipation> participation = selectQuery.from(RtParticipation.class);
    Join<RtParticipation, RtEventClass> joinEventClass = participation.join(RtParticipation_.rtEventClass, JoinType.INNER);
    Join<RtEventClass, RtCourse> joinCourse = joinEventClass.join(RtEventClass_.rtCourse, JoinType.LEFT);

    selectQuery.select(b.count(participation.get(RtParticipation_.id).get(RtParticipationKey_.clientNr)))
        .where(
            b.and(
                b.isNotNull(participation.get(RtParticipation_.startTime)),
                b.equal(participation.get(RtParticipation_.id).get(RtParticipationKey_.clientNr), ServerSession.get().getSessionClientNr()),
                b.or(joinCourse.get(RtCourse_.startlistSettingNr).in(Arrays.asList(startlistSettingNrs)),
                    joinEventClass.get(RtEventClass_.startlistSettingNr).in(Arrays.asList(startlistSettingNrs)))
                ));
    List<Long> result = JPA.createQuery(selectQuery).getResultList();
    if (result == null || result.isEmpty()) {
      return false;
    }
    return result.get(0) > 0;
  }

  private void applySeparationsAndVacants(List<StartlistSettingBean> completeStartlists) throws ProcessingException {
    for (StartlistSettingBean singleStartlistBean : completeStartlists) {
      LinkedList<StartlistParticipationBean> singleStartlist = singleStartlistBean.getList();
      List<Long> options = StartlistSettingUtility.getStartlistOptions(singleStartlistBean.getSettings().getOptions());

      // separation
      Separation separation = null;
      if (options.contains(StartlistSettingOptionCodeType.SeparateClubsCode.ID)) {
        separation = Separation.CLUB;
      }
      else if (options.contains(StartlistSettingOptionCodeType.SeparateNationsCode.ID)) {
        separation = Separation.NATION;
      }
      if (separation != null) {
        StartlistSeparationUtility.separateParticipations(singleStartlist, separation);
      }

      // add vacants
      StartlistVacantUtility.addVacants(singleStartlistBean.getSettings(), singleStartlist);
    }
  }

  private StartlistSettingBean createSingleStartlist(Long startlistSettingNr) throws ProcessingException {
    StartlistSettingFormData startlistSetting = new StartlistSettingFormData();
    startlistSetting.setStartlistSettingNr(startlistSettingNr);
    startlistSetting = BEANS.get(IStartlistSettingProcessService.class).load(startlistSetting);
    List<Long> options = StartlistSettingUtility.getStartlistOptions(startlistSetting.getOptions());

    // Loop over start blocks
    Long eventNr = startlistSetting.getEventNr();
    Long[] startblockUids = getOrderedStartblocksForEvent(eventNr);

    // unassigned with NULL as startblockUid
    LinkedList<StartlistParticipationBean> completeStartlist = new LinkedList<StartlistParticipationBean>();
    List<StartlistParticipationBean> blockStartlist = drawStartlistBlock(null, startlistSetting);
    completeStartlist.addAll(blockStartlist);

    // startblocks
    for (Long startblockUid : startblockUids) {
      blockStartlist = drawStartlistBlock(startblockUid, startlistSetting);
      completeStartlist.addAll(blockStartlist);
    }

    // start time wishes
    if (options.contains(StartlistSettingOptionCodeType.AllowStarttimeWishesCode.ID)) {
      StartlistWishUtility.applyEarlyStartTimeWishes(completeStartlist);
      StartlistWishUtility.applyLateStartTimeWishes(completeStartlist);
    }

    StartlistSettingBean result = new StartlistSettingBean(startlistSettingNr, eventNr, startlistSetting, completeStartlist);
    return result;
  }

  private void setStartTimesAndBibNo(List<StartlistSettingBean> allStartlist) throws ProcessingException {
    List<StartlistSettingBean> completedStartlists = new ArrayList<>();
    for (StartlistSettingBean bean : allStartlist) {
      StartlistSettingFormData startlistSetting = bean.getSettings();

      // Calculate First Start Time
      Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(bean.getEventNr());
      Long nextStart = FMilaUtility.getDateDifferenceInMilliSeconds(evtZero, startlistSetting.getFirstStart().getValue());
      bean.setFirstStart(nextStart);

      // Calculate First Bib No
      Long nextBibNo = startlistSetting.getBibNoFrom().getValue();
      if (nextBibNo == null) {
        nextBibNo = getMaxBibNoFromDatabase(bean.getEventNr(), nextBibNo, allStartlist);
        nextBibNo = getMaxBibNoFromCurrentStartlists(nextBibNo, completedStartlists);
      }

      // Startzeiten in Bean schreiben
      nextBibNo = StartlistServiceUtility.setStartTimesAndBibNo(bean, nextStart, nextBibNo);
      completedStartlists.add(bean);
    }
  }

  protected Long getMaxBibNoFromCurrentStartlists(Long nextBibNo, List<StartlistSettingBean> completedStartlists) {
    if (completedStartlists != null) {
      for (StartlistSettingBean completedStartlist : completedStartlists) {
        for (StartlistParticipationBean race : completedStartlist.getList()) {
          nextBibNo = Math.max(NumberUtility.nvl(race.getBibNo(), 0), NumberUtility.nvl(nextBibNo, 0));
        }
      }
    }
    return NumberUtility.nvl(nextBibNo, 0) + 1;
  }

  /**
   * @param eventNr
   * @param bibNo
   * @param excludedStartlists
   * @return
   * @throws ProcessingException
   */
  protected Long getMaxBibNoFromDatabase(Long eventNr, Long bibNo, List<StartlistSettingBean> excludedStartlists) throws ProcessingException {
    if (excludedStartlists == null) {
      throw new IllegalArgumentException("arguments required");
    }
    Set<Long> startlistSettingNrs = new HashSet<>();
    for (StartlistSettingBean bean : excludedStartlists) {
      startlistSettingNrs.add(bean.getStartlistSettingNr());
    }
    startlistSettingNrs.add(-1L);
    String queryString = "SELECT MAX(R.bibNo) FROM RtRace R " +
        "LEFT JOIN R.rtEventClass EC " +
        "LEFT JOIN EC.rtCourse C " +
        "WHERE R.eventNr = :eventNr " +
        "AND R.id.clientNr = :sessionClientNr " +
        "AND COALESCE(EC.startlistSettingNr,0) NOT IN (:startlistSettingNrs) " +
        "AND COALESCE(C.startlistSettingNr,0) NOT IN (:startlistSettingNrs) ";
    FMilaTypedQuery<String> query = JPA.createQuery(queryString, String.class);
    query.setParameter("eventNr", eventNr);
    query.setParameter("startlistSettingNrs", startlistSettingNrs);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    String maxBibNo = query.getSingleResult();

    try {
      bibNo = Long.parseLong(maxBibNo);
    }
    catch (NumberFormatException e) {
      bibNo = 0L;
    }
    return bibNo;
  }

  /**
   * @param startblockUid
   * @param startlistSetting
   * @param firstStarttime
   *          the first starttime for this block
   * @throws ProcessingException
   */
  protected List<StartlistParticipationBean> drawStartlistBlock(Long startblockUid, StartlistSettingFormData startlistSetting) throws ProcessingException {
    if (startlistSetting == null) {
      return new ArrayList<>();
    }

    // Read start time wishes
    List<AdditionalInformationValueBean> values = new ArrayList<>();
    AdditionalInformationValueBean value = new AdditionalInformationValueBean();
    value.setAdditionalInformationUid(AdditionalInformationCodeType.StartTimeWishCode.ID);
    values.add(value);
    List<?> starttimeWishesList = AdditionalInformationDatabaseUtility.selectValues(EntityCodeType.EntryCode.ID, true, null, ServerSession.get().getSessionClientNr(), values);

    Map<Long, Long> starttimeWishes = new HashMap<>();
    for (Object object : starttimeWishesList) {
      Object[] row = (Object[]) object;
      Long wishUid = TypeCastUtility.castValue(row[1], Long.class);
      if (NumberUtility.nvl(wishUid, 0) != 0) {
        starttimeWishes.put(TypeCastUtility.castValue(row[0], Long.class), wishUid);
      }
    }

    // Participations lesen und in Liste schreiben
    String queryString = "SELECT " +
        "P.id.entryNr, " + // Entry Nr
        "P.id.eventNr, " + // Event Nr
        "MIN(R.id.registrationNr), " + // Registration Nr
        "MIN(RA.nationUid), " + // Nation
        "MIN(RA.clubNr), " + // Club
        "MIN(R.startlistSettingOptionUid) " + // Group Option (Split or Group or Null)
        "FROM RtParticipation P " +
        "LEFT JOIN P.rtRaces RA " +
        "LEFT JOIN RA.rtEntry E " +
        "LEFT JOIN E.rtRegistration R " +
        "WHERE P.id.eventNr = :startListEventNr " +
        "AND P.id.clientNr = :sessionClientNr " +
        "AND COALESCE(P.startblockUid,-1) = COALESCE(:startblockUid,-1) " +
        "AND P.classUid IN (SELECT EC.id.classUid FROM RtEventClass EC" +
        "                  WHERE EC.id.eventNr = :startListEventNr " +
        "                  AND EC.id.clientNr = :sessionClientNr" +
        "                  AND EC.startlistSettingNr = :startlistSettingNrIn) " +
        "GROUP BY P.id.entryNr, P.id.eventNr " +
        "ORDER BY 1 ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("startlistSettingNrIn", startlistSetting.getStartlistSettingNr());
    query.setParameter("startListEventNr", startlistSetting.getEventNr());
    query.setParameter("startblockUid", NumberUtility.nvl(startblockUid, -1L));
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    List result = query.getResultList();
    List<StartlistParticipationBean> participations = new ArrayList<>();
    for (Object rowObject : result) {
      Object[] row = (Object[]) rowObject;
      StartlistParticipationBean bean = new StartlistParticipationBean();
      bean.setStartlistSettingNr(startlistSetting.getStartlistSettingNr());
      bean.setEntryNr(TypeCastUtility.castValue(row[0], Long.class));
      bean.setEventNr(TypeCastUtility.castValue(row[1], Long.class));
      bean.setRegistrationNr(TypeCastUtility.castValue(row[2], Long.class));
      bean.setNationUid(TypeCastUtility.castValue(row[3], Long.class));
      bean.setClubNr(TypeCastUtility.castValue(row[4], Long.class));
      bean.setRegistrationStartlistSettingOptionUid(TypeCastUtility.castValue(row[5], Long.class));
      // lookup wish
      bean.setStartTimeWish(starttimeWishes.get(bean.getEntryNr()));
      participations.add(bean);
    }

    // Reihenfolge auslosen
    Collections.shuffle(participations);

    return participations;
  }

  protected void storeStarttimes(List<StartlistSettingBean> allStartlists) throws ProcessingException {
    if (allStartlists == null) {
      return;
    }
    for (StartlistSettingBean singleStartlist : allStartlists) {
      for (StartlistParticipationBean participation : singleStartlist.getList()) {

        if (participation.isVacant()) {
          RtStartlistSettingVacantKey key = new RtStartlistSettingVacantKey();
          key.setStartlistSettingNr(participation.getStartlistSettingNr());
          key.setClientNr(ServerSession.get().getSessionClientNr());
          key.setBibNo(StringUtility.emptyIfNull(participation.getBibNo()));
          key.setStartTime(participation.getStartTime());
          RtStartlistSettingVacant vacant = new RtStartlistSettingVacant();
          vacant.setId(key);
          JPA.persist(vacant);
        }
        else {
          String queryString = "UPDATE RtParticipation " +
              "SET startTime = :starttime " +
              "WHERE id.entryNr = :entryNr " +
              "AND id.eventNr = :eventNr " +
              "AND id.clientNr = :sessionClientNr ";
          FMilaQuery query = JPA.createQuery(queryString);
          query.setParameter("starttime", participation.getStartTime());
          query.setParameter("entryNr", participation.getEntryNr());
          query.setParameter("eventNr", participation.getEventNr());
          query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
          query.executeUpdate();

          queryString = "UPDATE RtRace " +
              "SET bibNo = :bibNo, " +
              "legStartTime = :starttime " + // TODO: Relay - write only first race
              "WHERE entryNr = :entryNr " +
              "AND eventNr = :eventNr " +
              "AND id.clientNr = :sessionClientNr ";
          query = JPA.createQuery(queryString);
          query.setParameter("starttime", participation.getStartTime());
          query.setParameter("bibNo", StringUtility.emptyIfNull(participation.getBibNo()));
          query.setParameter("entryNr", participation.getEntryNr());
          query.setParameter("eventNr", participation.getEventNr());
          query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
          query.executeUpdate();
        }
      }
    }
  }

  /**
   * @param startblockUids
   * @param eventNr
   * @throws ProcessingException
   */
  protected Long[] getOrderedStartblocksForEvent(Long eventNr) throws ProcessingException {
    String queryString = "SELECT id.startblockUid " +
        "FROM RtEventStartblock SB " +
        "WHERE id.eventNr = :eventNr " +
        "AND id.clientNr = :sessionClientNr " +
        "ORDER BY sortcode ASC ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("eventNr", eventNr);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    List<Long> startblockUids = query.getResultList();

    return startblockUids.toArray(new Long[startblockUids.size()]);
  }
}
