package com.rtiming.server.result;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPACriteriaUtility;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.ecard.download.JPADownloadedECardsSearchFormDataStatementBuilder;
import com.rtiming.server.settings.addinfo.AdditionalInformationDatabaseUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.report.template.IReportParameters;
import com.rtiming.shared.dao.RtAddress;
import com.rtiming.shared.dao.RtAddress_;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtCity_;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClubKey_;
import com.rtiming.shared.dao.RtClub_;
import com.rtiming.shared.dao.RtCountry;
import com.rtiming.shared.dao.RtCountry_;
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourseKey_;
import com.rtiming.shared.dao.RtCourse_;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey_;
import com.rtiming.shared.dao.RtEcardStation;
import com.rtiming.shared.dao.RtEcardStationKey_;
import com.rtiming.shared.dao.RtEcardStation_;
import com.rtiming.shared.dao.RtEcard_;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClass_;
import com.rtiming.shared.dao.RtEventKey_;
import com.rtiming.shared.dao.RtEvent_;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey_;
import com.rtiming.shared.dao.RtPunchSession_;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceKey_;
import com.rtiming.shared.dao.RtRace_;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunnerKey_;
import com.rtiming.shared.dao.RtRunner_;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUc_;
import com.rtiming.shared.ecard.download.DownloadedECards;
import com.rtiming.shared.ecard.download.DownloadedECardsSearchFormData;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.results.IResultsOutlineService;
import com.rtiming.shared.results.ResultClazzRowData;
import com.rtiming.shared.results.ResultRowData;
import com.rtiming.shared.results.SingleEventSearchFormData;
import com.rtiming.shared.results.SplitTimeReportData;

public class ResultsOutlineService implements IResultsOutlineService {

  @Override
  public List<ResultRowData> getResultTableData(Long clientNr, Long classUid, Long courseNr, Long clubNr, SearchFilter filter) throws ProcessingException {
    Long eventNr = null;

    // Search
    if (filter != null && filter.getFormData() != null) {
      eventNr = ((SingleEventSearchFormData) filter.getFormData()).getEvent().getValue();
    }

    List<ResultRowData> result = null;
    if (clubNr != null) {
      // fetch all classes results and build club result list
      result = getResultsClubTableDataInternal(clientNr, clubNr, eventNr);
    }
    else {
      // standard class or course results
      result = getResultsTableDataInternal(clientNr, classUid, courseNr, clubNr, eventNr);
      result = ResultUtility.calculateRanks(result);
    }

    return result;
  }

  private List<ResultRowData> getResultsClubTableDataInternal(Long clientNr, Long clubNr, Long eventNr) throws ProcessingException {
    // fetch result list with all club members
    List<ResultRowData> allClubMembers = getResultsTableDataInternal(clientNr, null, null, clubNr, eventNr);
    List<ResultRowData> resultList = new ArrayList<ResultRowData>();
    // get all involved race numbers
    ArrayList<Long> raceNrs = new ArrayList<Long>();
    for (ResultRowData row : allClubMembers) {
      Long raceNr = row.getRaceNr();
      raceNrs.add(raceNr);
    }

    HashSet<Long> processedClassUids = new HashSet<Long>();
    for (ResultRowData row : allClubMembers) {
      Long memberClassUid = row.getClassShortcut();
      if (!processedClassUids.contains(memberClassUid)) {
        List<ResultRowData> bestTime = getResultsTableDataInternal(clientNr, memberClassUid, null, null, eventNr);
        bestTime = ResultUtility.calculateRanks(bestTime);
        processedClassUids.add(memberClassUid);
        int c = 0;
        for (ResultRowData bestTimeRow : bestTime) {
          Long memberRaceNr = bestTimeRow.getRaceNr();
          if (c == 0) {
            resultList.add(bestTimeRow);
          }
          else if (raceNrs.contains(memberRaceNr)) {
            resultList.add(bestTimeRow);
          }
          c++;
        }
      }
    }
    return resultList;
  }

  protected List<ResultRowData> getResultsTableDataInternal(Long clientNr, Long classUid, Long courseNr, Long clubNr, Long eventNr) throws ProcessingException {

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRace> race = selectQuery.from(RtRace.class);
    Join<RtRace, RtCountry> joinNation = race.join(RtRace_.rtCountry, JoinType.LEFT);
    Join<RtRace, RtRunner> joinRunner = race.join(RtRace_.rtRunner, JoinType.LEFT);
    Join<RtRace, RtEcard> joinECard = race.join(RtRace_.rtEcard, JoinType.LEFT);
    Join<RtRace, RtClub> joinClub = race.join(RtRace_.rtClub, JoinType.LEFT);
    Join<RtRace, RtAddress> joinAddress = race.join(RtRace_.rtAddress, JoinType.LEFT);
    Join<RtAddress, RtCity> joinCity = joinAddress.join(RtAddress_.rtCity, JoinType.LEFT);
    Join<RtRace, RtEventClass> joinEventClass = race.join(RtRace_.rtEventClass, JoinType.INNER);
    Join<RtEventClass, RtCourse> joinCourse = joinEventClass.join(RtEventClass_.rtCourse, JoinType.LEFT);

    selectQuery.select(b.array(race.get(RtRace_.legTime), // must be first
    race.get(RtRace_.id).get(RtRaceKey_.raceNr), race.get(RtRace_.entryNr), joinRunner.get(RtRunner_.id).get(RtRunnerKey_.runnerNr), race.get(RtRace_.legClassUid), joinCourse.get(RtCourse_.shortcut), race.get(RtRace_.bibNo), joinRunner.get(RtRunner_.extKey), JPACriteriaUtility.runnerNameJPA(joinRunner), joinRunner.get(RtRunner_.lastName), joinRunner.get(RtRunner_.firstName), joinECard.get(RtEcard_.ecardNo), joinECard.get(RtEcard_.rentalCard), joinClub.get(RtClub_.shortcut), joinClub.get(RtClub_.extKey), joinClub.get(RtClub_.name), joinNation.get(RtCountry_.nation), joinNation.get(RtCountry_.countryCode), joinRunner.get(RtRunner_.sexUid), joinRunner.get(RtRunner_.evtBirth), joinRunner.get(RtRunner_.year), joinAddress.get(RtAddress_.street), joinCity.get(RtCity_.zip), joinCity.get(RtCity_.name), joinCity.get(RtCity_.areaUid), joinCity.get(RtCity_.region), joinCity.get(RtCity_.countryUid), joinAddress.get(RtAddress_.phone), joinAddress.get(RtAddress_.fax), joinAddress.get(RtAddress_.mobile), joinAddress.get(RtAddress_.email), joinAddress.get(RtAddress_.www), race.get(RtRace_.statusUid), race.get(RtRace_.legStartTime), joinEventClass.get(RtEventClass_.timePrecisionUid))).where(b.and(b.equal(race.get(RtRace_.id).get(RtRaceKey_.clientNr), ServerSession.get().getSessionClientNr()), eventNr != null ? b.equal(race.get(RtRace_.eventNr), eventNr) : b.conjunction(), classUid != null ? b.equal(race.get(RtRace_.legClassUid), classUid) : b.conjunction(), courseNr != null ? b.equal(joinCourse.get(RtCourse_.id).get(RtCourseKey_.courseNr), courseNr) : b.conjunction(), clubNr != null ? b.equal(joinClub.get(RtClub_.id).get(RtClubKey_.clubNr), clubNr) : b.conjunction(), b.isNotNull(race.get(RtRace_.statusUid)), b.notEqual(race.get(RtRace_.statusUid), RaceStatusCodeType.DidNotStartCode.ID)));
    List<Order> order = new ArrayList<>();
    if (clubNr != null) {
      order.add(b.asc(race.get(RtRace_.legClassUid)));
    }
    /* Status OK is lowest */
    order.add(b.asc(race.get(RtRace_.statusUid)));
    order.add(b.asc(race.get(RtRace_.legTime)));
    /* Sort Teams, lowest race nr (with ranking) first */
    order.add(b.asc(race.get(RtRace_.id).get(RtRaceKey_.raceNr)));
    /* finally order by name */
    order.add(b.asc(JPACriteriaUtility.runnerNameJPA(joinRunner)));
    selectQuery.orderBy(order);

    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    // fetch winning time if list > 0
    Long winningTime = null;
    if (resultList.size() > 0) {
      winningTime = TypeCastUtility.castValue(resultList.get(0)[0], Long.class);
    }

    // load additional values
    Set<Long> entryPkNrs = AdditionalInformationDatabaseUtility.convertToPrimaryKeyList(resultList, 2);
    Map<Long, Object[]> entryAdditionalValues = AdditionalInformationDatabaseUtility.selectTableData(EntityCodeType.EntryCode.ID, entryPkNrs);

    Set<Long> runnerPkNrs = AdditionalInformationDatabaseUtility.convertToPrimaryKeyList(resultList, 3);
    Map<Long, Object[]> runnerAdditionalValues = AdditionalInformationDatabaseUtility.selectTableData(EntityCodeType.RunnerCode.ID, runnerPkNrs);

    List<ResultRowData> result = new ArrayList<>();
    for (Object[] row : resultList) {
      int k = 0;
      ResultRowData r = new ResultRowData();
      Long legTime = TypeCastUtility.castValue(row[k++], Long.class);
      r.setRaceNr(TypeCastUtility.castValue(row[k++], Long.class));
      r.setEntryNr(TypeCastUtility.castValue(row[k++], Long.class));
      r.setRunnerNr(TypeCastUtility.castValue(row[k++], Long.class));
      Long legClassUid = TypeCastUtility.castValue(row[k++], Long.class);
      r.setClassShortcut(legClassUid);
      r.setClassName(FMilaUtility.getCodeText(ClassTypeCodeType.class, legClassUid));
      r.setCourseShortcut(TypeCastUtility.castValue(row[k++], String.class));
      r.setBibNumber(TypeCastUtility.castValue(row[k++], String.class));
      r.setExtKey(TypeCastUtility.castValue(row[k++], String.class));
      r.setRunner(TypeCastUtility.castValue(row[k++], String.class));
      r.setLastName(TypeCastUtility.castValue(row[k++], String.class));
      r.setFirstName(TypeCastUtility.castValue(row[k++], String.class));
      r.seteCard(TypeCastUtility.castValue(row[k++], String.class));
      r.setRentalCard(BooleanUtility.nvl(TypeCastUtility.castValue(row[k++], Boolean.class)));
      r.setClubShortcut(TypeCastUtility.castValue(row[k++], String.class));
      r.setClubExtKey(TypeCastUtility.castValue(row[k++], String.class));
      r.setClub(TypeCastUtility.castValue(row[k++], String.class));
      r.setNation(TypeCastUtility.castValue(row[k++], String.class));
      r.setNationCountryCode(TypeCastUtility.castValue(row[k++], String.class));
      r.setSex(TypeCastUtility.castValue(row[k++], Long.class));
      r.setBirthdate(TypeCastUtility.castValue(row[k++], Date.class));
      r.setYear(TypeCastUtility.castValue(row[k++], Long.class));
      r.setStreet(TypeCastUtility.castValue(row[k++], String.class));
      r.setZip(TypeCastUtility.castValue(row[k++], String.class));
      r.setCity(TypeCastUtility.castValue(row[k++], String.class));
      r.setArea(TypeCastUtility.castValue(row[k++], Long.class));
      r.setRegion(TypeCastUtility.castValue(row[k++], String.class));
      r.setCountry(TypeCastUtility.castValue(row[k++], String.class));
      r.setPhone(TypeCastUtility.castValue(row[k++], String.class));
      r.setFax(TypeCastUtility.castValue(row[k++], String.class));
      r.setMobilePhone(TypeCastUtility.castValue(row[k++], String.class));
      r.seteMail(TypeCastUtility.castValue(row[k++], String.class));
      r.setURL(TypeCastUtility.castValue(row[k++], String.class));
      Long raceStatusUid = TypeCastUtility.castValue(row[k++], Long.class);
      r.setRaceStatus(raceStatusUid);
      r.setLegStartTime(TypeCastUtility.castValue(row[k++], Long.class));
      r.setLegTime(legTime);
      Long timePrecisionUid = TypeCastUtility.castValue(row[k++], Long.class);
      if (CompareUtility.equals(raceStatusUid, RaceStatusCodeType.OkCode.ID)) {
        if (winningTime != null && legTime != null) {
          r.setTimeBehind(FMilaUtility.formatTime(legTime - winningTime, timePrecisionUid));
          r.setPercent(((Double.valueOf(legTime) - Double.valueOf(winningTime)) / Double.valueOf(winningTime)) * 100);
        }
        r.setTime(FMilaUtility.formatTime(legTime, timePrecisionUid));
      }
      else {
        r.setTime(FMilaUtility.getCodeText(RaceStatusCodeType.class, raceStatusUid));
      }
      r.setEntryAdditionalValues(entryAdditionalValues.get(r.getEntryNr()));
      r.setRunnerAdditionalValues(runnerAdditionalValues.get(r.getRunnerNr()));
      result.add(r);
    }
    return result;
  }

  @Override
  public Object[][] getDownloadedECardTableData(DownloadedECards presentationType, DownloadedECardsSearchFormData searchFormData) throws ProcessingException {

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtPunchSession> allPunchSessions = b.createQuery(RtPunchSession.class);
    Root<RtPunchSession> allPunchSessionsRoot = allPunchSessions.from(RtPunchSession.class);
    allPunchSessions.where(b.and(b.equal(allPunchSessionsRoot.get(RtPunchSession_.eventNr), searchFormData.getEvent().getValue()), b.equal(allPunchSessionsRoot.get(RtPunchSession_.id).get(RtPunchSessionKey_.clientNr), ServerSession.get().getSessionClientNr())));
    List<RtPunchSession> punchSessionList = JPA.createQuery(allPunchSessions).getResultList();
    Map<Long, Long> eCardPunchSessionCount = new HashMap<>();
    for (RtPunchSession session : punchSessionList) {
      if (session.geteCardNr() != null) {
        if (eCardPunchSessionCount.get(session.geteCardNr()) == null) {
          eCardPunchSessionCount.put(session.geteCardNr(), 0L);
        }
        eCardPunchSessionCount.put(session.geteCardNr(), Math.min(1, 1 + eCardPunchSessionCount.get(session.geteCardNr())));
      }
    }

    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtPunchSession> punchSession = selectQuery.from(RtPunchSession.class);
    Join<RtPunchSession, RtEcard> joinECard = punchSession.join(RtPunchSession_.rtEcard, JoinType.LEFT);
    Join<RtPunchSession, RtRace> joinRace = punchSession.join(RtPunchSession_.rtRace, JoinType.LEFT);
    Join<RtRace, RtRunner> joinRunner = joinRace.join(RtRace_.rtRunner, JoinType.LEFT);
    Join<RtRace, RtEventClass> joinEventClass = joinRace.join(RtRace_.rtEventClass, JoinType.LEFT);
    Join<RtRace, RtEvent> joinEvent = joinRace.join(RtRace_.rtEvent, JoinType.LEFT);
    Join<RtEventClass, RtCourse> joinCourse = joinEventClass.join(RtEventClass_.rtCourse, JoinType.LEFT);

    JPADownloadedECardsSearchFormDataStatementBuilder builder = new JPADownloadedECardsSearchFormDataStatementBuilder(punchSession, joinRunner, joinECard, joinRace, joinEvent, joinCourse);
    builder.build(searchFormData);

    selectQuery.select(b.array(punchSession.get(RtPunchSession_.id).get(RtPunchSessionKey_.punchSessionNr), punchSession.get(RtPunchSession_.eventNr), joinRace.get(RtRace_.entryNr), punchSession.get(RtPunchSession_.raceNr), joinECard.get(RtEcard_.ecardNo), joinECard.get(RtEcard_.id).get(RtEcardKey_.eCardNr), JPACriteriaUtility.runnerNameJPA(joinRunner), punchSession.get(RtPunchSession_.evtDownload), joinRace.get(RtRace_.bibNo), joinRace.get(RtRace_.legStartTime), joinCourse.get(RtCourse_.shortcut), joinRace.get(RtRace_.legClassUid), joinEventClass.get(RtEventClass_.timePrecisionUid), joinRace.get(RtRace_.legTime), joinRace.get(RtRace_.statusUid), punchSession.get(RtPunchSession_.ecardClear), punchSession.get(RtPunchSession_.ecardCheck), punchSession.get(RtPunchSession_.start), punchSession.get(RtPunchSession_.finish), joinEvent.get(RtEvent_.evtZero))).where(b.and(b.equal(punchSession.get(RtPunchSession_.id).get(RtPunchSessionKey_.clientNr), ServerSession.get().getSessionClientNr()), builder.getPredicate())).orderBy(b.desc(punchSession.get(RtPunchSession_.evtDownload)));

    if (CompareUtility.equals(presentationType, DownloadedECards.DUPLICATE)) {
      Subquery<Long> subquery = selectQuery.subquery(Long.class);
      Root<RtPunchSession> subroot = subquery.from(RtPunchSession.class);

      subquery.select(subroot.get(RtPunchSession_.id).get(RtPunchSessionKey_.punchSessionNr)).where(b.and(b.notEqual(subroot.get(RtPunchSession_.id).get(RtPunchSessionKey_.punchSessionNr), punchSession.get(RtPunchSession_.id).get(RtPunchSessionKey_.punchSessionNr)), b.equal(subroot.get(RtPunchSession_.eventNr), punchSession.get(RtPunchSession_.eventNr)), b.equal(subroot.get(RtPunchSession_.eCardNr), punchSession.get(RtPunchSession_.eCardNr))));

      selectQuery.getRestriction().getExpressions().add(b.exists(subquery));
    }

    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();
    List<Object[]> result = new ArrayList<>();

    for (Object[] row : resultList) {
      int k = 0;
      Long punchSessionNr = TypeCastUtility.castValue(row[k++], Long.class);
      Long eventNr = TypeCastUtility.castValue(row[k++], Long.class);
      Long entryNr = TypeCastUtility.castValue(row[k++], Long.class);
      Long raceNr = TypeCastUtility.castValue(row[k++], Long.class);
      String eCard = TypeCastUtility.castValue(row[k++], String.class);
      Long eCardNr = TypeCastUtility.castValue(row[k++], Long.class);
      String runnerName = TypeCastUtility.castValue(row[k++], String.class);
      Date evtDownload = TypeCastUtility.castValue(row[k++], Date.class);
      String bibNo = TypeCastUtility.castValue(row[k++], String.class);
      Long legStartTime = TypeCastUtility.castValue(row[k++], Long.class);
      String courseShortcut = TypeCastUtility.castValue(row[k++], String.class);
      Long legClassUid = TypeCastUtility.castValue(row[k++], Long.class);
      Long timePrecisionUid = TypeCastUtility.castValue(row[k++], Long.class);
      Long legTime = TypeCastUtility.castValue(row[k++], Long.class);
      Long statusUid = TypeCastUtility.castValue(row[k++], Long.class);
      Long ecardClear = TypeCastUtility.castValue(row[k++], Long.class);
      Long ecardCheck = TypeCastUtility.castValue(row[k++], Long.class);
      Long ecardStart = TypeCastUtility.castValue(row[k++], Long.class);
      Long ecardFinish = TypeCastUtility.castValue(row[k++], Long.class);
      Date evtZero = TypeCastUtility.castValue(row[k++], Date.class);

      Object[] resultRow = new Object[19];
      k = 0;
      resultRow[k++] = punchSessionNr;
      resultRow[k++] = eventNr;
      resultRow[k++] = entryNr;
      resultRow[k++] = raceNr;
      resultRow[k++] = eCard + (StringUtility.isNullOrEmpty(runnerName) ? "" : " (" + runnerName + ")");
      resultRow[k++] = eCard;
      resultRow[k++] = evtDownload;
      resultRow[k++] = bibNo;
      resultRow[k++] = FMilaUtility.addMilliSeconds(evtZero, legStartTime);
      resultRow[k++] = runnerName;
      resultRow[k++] = courseShortcut;
      resultRow[k++] = legClassUid;
      resultRow[k++] = FMilaUtility.formatTime(legTime, timePrecisionUid);
      resultRow[k++] = statusUid;
      resultRow[k++] = FMilaUtility.addMilliSeconds(evtZero, ecardClear);
      resultRow[k++] = FMilaUtility.addMilliSeconds(evtZero, ecardCheck);
      resultRow[k++] = FMilaUtility.addMilliSeconds(evtZero, ecardStart);
      resultRow[k++] = FMilaUtility.addMilliSeconds(evtZero, ecardFinish);
      resultRow[k++] = NumberUtility.nvl(eCardPunchSessionCount.get(eCardNr), 0) > 1;
      result.add(resultRow);
    }
    return JPAUtility.convertList2Array(result);
  }

  @Override
  public List<RtEcardStation> getECardStationTableData() throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtEcardStation> selectQuery = b.createQuery(RtEcardStation.class);
    Root<RtEcardStation> eCardStation = selectQuery.from(RtEcardStation.class);

    selectQuery.where(b.and(b.equal(eCardStation.get(RtEcardStation_.id).get(RtEcardStationKey_.clientNr), ServerSession.get().getSessionClientNr())));

    List<RtEcardStation> resultList = JPA.createQuery(selectQuery).getResultList();
    return resultList;
  }

  @Override
  public Object[][] getPunchTableData(Long punchSessionNr) throws ProcessingException {
    String queryString = "SELECT P.id.sortcode, P.controlNo, E.evtZero, P.time " + "FROM RtPunch P " + "INNER JOIN P.rtPunchSession PS " + "INNER JOIN PS.rtEvent E " + "WHERE (P.id.punchSessionNr = :punchSessionNr OR :punchSessionNr IS NULL)";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("punchSessionNr", punchSessionNr);
    Object[][] array = JPAUtility.convertList2Array(query.getResultList());
    for (Object[] row : array) {
      row[2] = FMilaUtility.addMilliSeconds(TypeCastUtility.castValue(row[2], Date.class), TypeCastUtility.castValue(row[3], Long.class));
    }
    return array;
  }

  @Override
  public SplitTimeReportData getSplitTimesReportData(long raceNr) throws ProcessingException {

    SplitTimeReportData reportData = new SplitTimeReportData();

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRace> race = selectQuery.from(RtRace.class);
    Join<RtRace, RtRunner> joinRunner = race.join(RtRace_.rtRunner, JoinType.INNER);
    Join<RtRace, RtClub> joinClub = race.join(RtRace_.rtClub, JoinType.LEFT);
    Join<RtRace, RtAddress> joinAddress = race.join(RtRace_.rtAddress, JoinType.LEFT);
    Join<RtAddress, RtCity> joinCity = joinAddress.join(RtAddress_.rtCity, JoinType.LEFT);
    Join<RtRace, RtEcard> joinECard = race.join(RtRace_.rtEcard, JoinType.LEFT);
    Join<RtRace, RtEvent> joinEvent = race.join(RtRace_.rtEvent, JoinType.INNER);
    Join<RtRace, RtEventClass> joinEventClass = race.join(RtRace_.rtEventClass, JoinType.INNER);
    Join<RtEventClass, RtUc> joinUc = joinEventClass.join(RtEventClass_.rtUc1, JoinType.INNER);

    selectQuery.select(b.array(JPACriteriaUtility.runnerNameJPA(joinRunner), joinClub.get(RtClub_.name), joinCity.get(RtCity_.name), joinECard.get(RtEcard_.ecardNo), joinUc.get(RtUc_.shortcut), joinEventClass.get(RtEventClass_.timePrecisionUid), race.get(RtRace_.legTime), joinEvent.get(RtEvent_.name), joinEvent.get(RtEvent_.id).get(RtEventKey_.eventNr), joinEvent.get(RtEvent_.format), joinEvent.get(RtEvent_.evtZero), race.get(RtRace_.statusUid))).where(b.and(b.equal(race.get(RtRace_.id).get(RtRaceKey_.clientNr), ServerSession.get().getSessionClientNr()), b.equal(race.get(RtRace_.id).get(RtRaceKey_.raceNr), raceNr)));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    if (resultList.size() == 0) {
      throw new VetoException(TEXTS.get("RaceNotValidatedMessage"));
    }
    else {
      Object[] data = resultList.get(0);
      String runnerName = TypeCastUtility.castValue(data[0], String.class);
      String clubName = TypeCastUtility.castValue(data[1], String.class);
      String cityName = TypeCastUtility.castValue(data[2], String.class);
      String eCardNo = TypeCastUtility.castValue(data[3], String.class);
      String shortcut = TypeCastUtility.castValue(data[4], String.class);
      Long timePrecisionUid = TypeCastUtility.castValue(data[5], Long.class);
      Long legTime = TypeCastUtility.castValue(data[6], Long.class);
      String eventName = TypeCastUtility.castValue(data[7], String.class);
      Long eventNr = TypeCastUtility.castValue(data[8], Long.class);
      String eventFormat = TypeCastUtility.castValue(data[9], String.class);
      Date evtZero = TypeCastUtility.castValue(data[10], Date.class);
      Long raceStatusUid = TypeCastUtility.castValue(data[11], Long.class);

      if (raceStatusUid == null) {
        throw new VetoException(TEXTS.get("RaceNotValidatedMessage"));
      }

      HashMap<String, String> parameters = new HashMap<>();
      parameters.put(IReportParameters.RUNNER_NAME, runnerName);
      parameters.put(IReportParameters.RUNNER_CLUB, clubName);
      parameters.put(IReportParameters.RUNNER_CITY, cityName);
      parameters.put(IReportParameters.RUNNER_ECARD_NO, eCardNo);
      parameters.put(IReportParameters.RACE_CLASS, shortcut);
      if (CompareUtility.equals(raceStatusUid, RaceStatusCodeType.OkCode.ID)) {
        parameters.put(IReportParameters.RACE_TIME, FMilaUtility.formatTime(legTime, timePrecisionUid));
      }
      else {
        parameters.put(IReportParameters.RACE_TIME, CODES.getCodeType(RaceStatusCodeType.class).getCode(raceStatusUid).getText());
      }
      parameters.put(IReportParameters.EVENT_NAME, eventName);
      // logo
      if (!StringUtility.isNullOrEmpty(eventFormat)) {
        parameters.put(IReportParameters.JASPER_LOGO_PARAMETER, "logo/" + ServerSession.get().getSessionClientNr() + "/" + eventNr + "." + eventFormat);
      }
      parameters.put(IReportParameters.EVENT_DATE_FORMATTED, DateUtility.formatDate(evtZero));
      reportData.setEventDate(evtZero);
      parameters.put(IReportParameters.APPLICATION_NAME, Texts.get("ApplicationName") + " " + FMilaUtility.getVersion());

      reportData.setEventNr(eventNr);
      reportData.setParameters(parameters);
    }

    return reportData;
  }

  @Override
  public Object[][] getResultClubTableData(SearchFilter filter) throws ProcessingException {
    Long eventNr = ((SingleEventSearchFormData) filter.getFormData()).getEvent().getValue();
    String queryString = "SELECT C.id.clubNr, " + "C.name, " + "COUNT(DISTINCT RA.entryNr), " + "COUNT(DISTINCT RA.id.raceNr), " + "COUNT(RA.statusUid), " + "COUNT(DISTINCT RA.id.raceNr) - COUNT(RA.statusUid) " + "FROM RtClub C " + "INNER JOIN C.rtRaces RA " + "WHERE C.id.clientNr = :sessionClientNr " + (eventNr == null ? "" : "AND RA.eventNr = :eventNr ") + "GROUP BY C.id.clubNr, C.name ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    if (eventNr != null) {
      query.setParameter("eventNr", eventNr);
    }
    return JPAUtility.convertList2Array(query.getResultList());
  }

  @Override
  public List<ResultClazzRowData> getResultClassTableData(Long clientNr, SearchFilter filter) throws ProcessingException {
    Long eventNr = ((SingleEventSearchFormData) filter.getFormData()).getEvent().getValue();
    String queryString = "SELECT " + "EC.parentUid, " + // Parent/Relay Uid
    "EC.id.classUid, " + // Class/Leg Uid
    "EC.typeUid, " + // Class Type
    "EC.sortcode, " + // Order
    "COUNT(DISTINCT RA.entryNr), " + "COUNT(DISTINCT RA.id.raceNr), " + "COUNT(RA.statusUid), " + "COUNT(DISTINCT RA.id.raceNr) - COUNT(RA.statusUid) " + "FROM RtEventClass EC " + "LEFT JOIN EC.rtRaces RA " + "WHERE (EC.id.eventNr = :eventNr OR :eventNr IS NULL) " + "AND EC.id.clientNr = :clientNr " + "AND EC.typeUid != " + ClassTypeCodeType.RelayCode.ID + " " + "GROUP BY EC.id.classUid, EC.parentUid, EC.typeUid, EC.sortcode ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("clientNr", clientNr);
    query.setParameter("eventNr", eventNr);
    List data = query.getResultList();

    List<ResultClazzRowData> result = new ArrayList<>();
    for (Object rowObject : data) {
      Object[] row = (Object[]) rowObject;
      int k = 0;
      ResultClazzRowData r = new ResultClazzRowData();
      r.setParentUid(TypeCastUtility.castValue(row[k++], Long.class));
      r.setClazzUid(TypeCastUtility.castValue(row[k++], Long.class));
      r.setClazzTypeUid(TypeCastUtility.castValue(row[k++], Long.class));
      r.setSortCode(TypeCastUtility.castValue(row[k++], Long.class));
      r.setEntries(TypeCastUtility.castValue(row[k++], Long.class));
      r.setRunners(TypeCastUtility.castValue(row[k++], Long.class));
      r.setProcessed(TypeCastUtility.castValue(row[k++], Long.class));
      r.setMissing(TypeCastUtility.castValue(row[k++], Long.class));
      // set text columns
      if (r.getParentUid() == null) {
        r.setOutline(FMilaUtility.getCodeText(ClassCodeType.class, r.getClazzUid()));
        r.setParent(FMilaUtility.getCodeText(ClassCodeType.class, r.getClazzUid()));
      }
      else {
        r.setOutline(FMilaUtility.getCodeText(ClassCodeType.class, r.getParentUid()) + ", " + FMilaUtility.getCodeText(ClassCodeType.class, r.getClazzUid()));
        r.setParent(FMilaUtility.getCodeText(ClassCodeType.class, r.getParentUid()));
      }
      r.setClazz(FMilaUtility.getCodeText(ClassCodeType.class, r.getClazzUid()));
      result.add(r);
    }
    return result;
  }

  @Override
  public Object[][] getResultCourseTableData(SearchFilter filter) throws ProcessingException {
    Long eventNr = ((SingleEventSearchFormData) filter.getFormData()).getEvent().getValue();
    String queryString = "SELECT 0, " + "C.id.courseNr, " + "C.shortcut, " + "COUNT(DISTINCT RA.entryNr), " + "COUNT(DISTINCT RA.id.raceNr), " + "COUNT(RA.statusUid), " + "COUNT(DISTINCT RA.id.raceNr) - COUNT(RA.statusUid) " + "FROM RtCourse C " + "LEFT OUTER JOIN C.rtEventClasses EC " + "LEFT OUTER JOIN EC.rtRaces RA " + "WHERE (C.eventNr IS NULL OR :eventNr = C.eventNr) " + "AND C.id.clientNr = :clientNr " + "GROUP BY C.id.courseNr, C.shortcut ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("eventNr", eventNr);
    return JPAUtility.convertList2Array(query.getResultList());
  }
}
