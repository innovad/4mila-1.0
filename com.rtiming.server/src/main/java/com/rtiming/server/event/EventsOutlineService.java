package com.rtiming.server.event;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;

import com.rtiming.server.ServerSession;
import com.rtiming.server.club.JPAClubsSearchFormDataStatementBuilder;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPACriteriaUtility;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.ecard.JPAECardBoxSearchFormDataStatementBuilder;
import com.rtiming.server.event.course.JPAControlSearchFormDataStatementBuilder;
import com.rtiming.server.runner.JPARunnersSearchFormDataStatementBuilder;
import com.rtiming.server.settings.addinfo.AdditionalInformationDatabaseUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.club.ClubRowData;
import com.rtiming.shared.club.ClubsSearchFormData;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.dao.RtAddress;
import com.rtiming.shared.dao.RtAddress_;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtCity_;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClubKey_;
import com.rtiming.shared.dao.RtClub_;
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControlKey_;
import com.rtiming.shared.dao.RtControl_;
import com.rtiming.shared.dao.RtCountry;
import com.rtiming.shared.dao.RtCountry_;
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourseControl;
import com.rtiming.shared.dao.RtCourseControlKey_;
import com.rtiming.shared.dao.RtCourseControl_;
import com.rtiming.shared.dao.RtCourse_;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey_;
import com.rtiming.shared.dao.RtEcard_;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey_;
import com.rtiming.shared.dao.RtEventClass_;
import com.rtiming.shared.dao.RtEventKey_;
import com.rtiming.shared.dao.RtEventMap;
import com.rtiming.shared.dao.RtEventMapKey_;
import com.rtiming.shared.dao.RtEventMap_;
import com.rtiming.shared.dao.RtEvent_;
import com.rtiming.shared.dao.RtFeeGroup;
import com.rtiming.shared.dao.RtFeeGroup_;
import com.rtiming.shared.dao.RtMap;
import com.rtiming.shared.dao.RtMapKey_;
import com.rtiming.shared.dao.RtMap_;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceControl;
import com.rtiming.shared.dao.RtRaceControlKey_;
import com.rtiming.shared.dao.RtRaceControl_;
import com.rtiming.shared.dao.RtRace_;
import com.rtiming.shared.dao.RtRankingEvent;
import com.rtiming.shared.dao.RtRankingEventKey_;
import com.rtiming.shared.dao.RtRankingEvent_;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunnerKey_;
import com.rtiming.shared.dao.RtRunner_;
import com.rtiming.shared.ecard.ECardsSearchFormData;
import com.rtiming.shared.event.EventRowData;
import com.rtiming.shared.event.EventsSearchFormData;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.RaceControlRowData;
import com.rtiming.shared.event.course.ControlsSearchFormData;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.event.course.ICourseProcessService;
import com.rtiming.shared.event.course.ReplacementControlRowData;
import com.rtiming.shared.runner.RunnerRowData;
import com.rtiming.shared.runner.RunnersSearchFormData;
import com.rtiming.shared.settings.IDefaultProcessService;

public class EventsOutlineService implements IEventsOutlineService {

  @Override
  public List<RtEcard> getECardTableData(ECardsSearchFormData formData) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtEcard> select = b.createQuery(RtEcard.class);
    Root<RtEcard> rtecard = select.from(RtEcard.class);

    JPAECardBoxSearchFormDataStatementBuilder builder = new JPAECardBoxSearchFormDataStatementBuilder(rtecard);
    builder.build(formData.getECardBox());

    select.where(b.and(b.equal(rtecard.get(RtEcard_.id).get(RtEcardKey_.clientNr), ServerSession.get().getSessionClientNr()), builder.getPredicate()));

    return JPA.createQuery(select).getResultList();
  }

  @Override
  public List<ClubRowData> getClubTableData(ClubsSearchFormData formData) throws ProcessingException {

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtClub> club = selectQuery.from(RtClub.class);
    Join<RtClub, RtRunner> joinContactRunner = club.join(RtClub_.rtRunner, JoinType.LEFT);

    JPAClubsSearchFormDataStatementBuilder builder = new JPAClubsSearchFormDataStatementBuilder(selectQuery, club);
    builder.build(formData);

    selectQuery.select(b.array(club.get(RtClub_.id).get(RtClubKey_.clubNr), club.get(RtClub_.shortcut), club.get(RtClub_.name), club.get(RtClub_.extKey), JPACriteriaUtility.runnerNameJPA(joinContactRunner))).where(b.and(b.equal(club.get(RtClub_.id).get(RtClubKey_.clientNr), ServerSession.get().getSessionClientNr()), builder.getPredicate()));

    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    // load additional values
    Set<Long> pkNrs = AdditionalInformationDatabaseUtility.convertToPrimaryKeyList(resultList, 0);
    Map<Long, Object[]> additionalValues = AdditionalInformationDatabaseUtility.selectTableData(EntityCodeType.ClubCode.ID, pkNrs);

    List<ClubRowData> result = new ArrayList<>();
    for (Object[] row : resultList) {
      ClubRowData r = new ClubRowData();
      r.setClubNr(TypeCastUtility.castValue(row[0], Long.class));
      r.setShortcut(TypeCastUtility.castValue(row[1], String.class));
      r.setName(TypeCastUtility.castValue(row[2], String.class));
      r.setExtKey(TypeCastUtility.castValue(row[3], String.class));
      r.setContactPerson(TypeCastUtility.castValue(row[4], String.class));
      r.setAdditionalValues(additionalValues.get(r.getClubNr()));
      result.add(r);
    }

    return result;
  }

  @Override
  public List<RunnerRowData> getRunnerTableData(RunnersSearchFormData formData) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRunner> runner = selectQuery.from(RtRunner.class);
    Join<RtRunner, RtEcard> joinECard = runner.join(RtRunner_.rtEcard, JoinType.LEFT);
    Join<RtRunner, RtClub> joinClub = runner.join(RtRunner_.rtClub, JoinType.LEFT);
    Join<RtRunner, RtCountry> joinCountry = runner.join(RtRunner_.rtCountry, JoinType.LEFT);
    Join<RtRunner, RtAddress> joinAddress = runner.join(RtRunner_.rtAddress, JoinType.LEFT);
    Join<RtAddress, RtCity> joinCity = joinAddress.join(RtAddress_.rtCity, JoinType.LEFT);

    JPARunnersSearchFormDataStatementBuilder builder = new JPARunnersSearchFormDataStatementBuilder(selectQuery, runner, joinClub, joinCity, joinECard);
    builder.build(formData);

    selectQuery.select(b.array(runner.get(RtRunner_.id).get(RtRunnerKey_.runnerNr), runner.get(RtRunner_.extKey), JPACriteriaUtility.runnerNameJPA(runner), // Name
    runner.get(RtRunner_.lastName), // Last Name
    runner.get(RtRunner_.firstName), // First Name
    joinECard.get(RtEcard_.ecardNo), runner.get(RtRunner_.defaultClassUid), joinClub.get(RtClub_.name), joinCountry.get(RtCountry_.nation), // Nation
    joinCountry.get(RtCountry_.countryCode), // Nation Country Code (flag)
    runner.get(RtRunner_.sexUid), runner.get(RtRunner_.evtBirth), runner.get(RtRunner_.year), joinAddress.get(RtAddress_.street), joinCity.get(RtCity_.zip), joinCity.get(RtCity_.name), joinCity.get(RtCity_.areaUid), joinCity.get(RtCity_.region), joinCity.get(RtCity_.countryUid), joinAddress.get(RtAddress_.phone), joinAddress.get(RtAddress_.fax), joinAddress.get(RtAddress_.mobile), joinAddress.get(RtAddress_.email), joinAddress.get(RtAddress_.www))).where(b.and(b.equal(runner.get(RtRunner_.id).get(RtRunnerKey_.clientNr), ServerSession.get().getSessionClientNr()), builder.getPredicate()));

    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    // load additional values
    Set<Long> pkNrs = AdditionalInformationDatabaseUtility.convertToPrimaryKeyList(resultList, 0);
    Map<Long, Object[]> additionalValues = AdditionalInformationDatabaseUtility.selectTableData(EntityCodeType.RunnerCode.ID, pkNrs);

    List<RunnerRowData> result = new ArrayList<>();
    for (Object[] row : resultList) {
      RunnerRowData r = new RunnerRowData();
      r.setRunnerNr(TypeCastUtility.castValue(row[0], Long.class));
      r.setExtKey(TypeCastUtility.castValue(row[1], String.class));
      r.setName(TypeCastUtility.castValue(row[2], String.class));
      r.setLastName(TypeCastUtility.castValue(row[3], String.class));
      r.setFirstName(TypeCastUtility.castValue(row[4], String.class));
      r.seteCard(TypeCastUtility.castValue(row[5], String.class));
      r.setDefaultClazz(TypeCastUtility.castValue(row[6], Long.class));
      r.setClub(TypeCastUtility.castValue(row[7], String.class));
      r.setNation(TypeCastUtility.castValue(row[8], String.class));
      r.setNationCountryCode(TypeCastUtility.castValue(row[9], String.class));
      r.setSexUid(TypeCastUtility.castValue(row[10], Long.class));
      r.setEvtBirthdate(TypeCastUtility.castValue(row[11], Date.class));
      r.setYear(TypeCastUtility.castValue(row[12], Long.class));
      r.setStreet(TypeCastUtility.castValue(row[13], String.class));
      r.setZip(TypeCastUtility.castValue(row[14], String.class));
      r.setCity(TypeCastUtility.castValue(row[15], String.class));
      r.setAreaUid(TypeCastUtility.castValue(row[16], Long.class));
      r.setRegion(TypeCastUtility.castValue(row[17], String.class));
      r.setCountryUid(TypeCastUtility.castValue(row[18], Long.class));
      r.setPhone(TypeCastUtility.castValue(row[19], String.class));
      r.setFax(TypeCastUtility.castValue(row[20], String.class));
      r.setMobile(TypeCastUtility.castValue(row[21], String.class));
      r.setEmail(TypeCastUtility.castValue(row[22], String.class));
      r.setWww(TypeCastUtility.castValue(row[23], String.class));
      r.setAdditionalValues(additionalValues.get(r.getRunnerNr()));
      result.add(r);
    }

    return result;
  }

  @Override
  public List<EventRowData> getEventTableData(Long clientNr, EventsSearchFormData formData) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtEvent> selectQuery = b.createQuery(RtEvent.class);
    Root<RtEvent> event = selectQuery.from(RtEvent.class);

    JPAEventsSearchFormDataStatementBuilder builder = new JPAEventsSearchFormDataStatementBuilder(event);
    builder.build(formData);

    selectQuery.where(b.and(clientNr == null ? b.conjunction() : b.equal(event.get(RtEvent_.id).get(RtEventKey_.clientNr), clientNr), builder.getPredicate()));

    List<RtEvent> list = JPA.createQuery(selectQuery).getResultList();

    Long defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();

    List<EventRowData> result = new ArrayList<>();
    for (RtEvent row : list) {
      EventRowData r = new EventRowData();
      r.setEventNr(row.getId().getId());
      r.setClientNr(row.getId().getClientNr());
      r.setDefaultEventNr(defaultEventNr);
      r.setName(row.getName());
      r.setLocation(row.getLocation());
      r.setMap(row.getMap());
      r.setTypeUid(row.getTypeUid());
      r.setEvtZero(row.getEvtZero());
      r.setEvtFinish(row.getEvtFinish());
      r.setTimeZone(row.getTimezoneOffset());
      r.setEvtLastUpload(row.getEvtLastUpload());
      result.add(r);
    }
    return result;
  }

  @Override
  public Object[][] getEventClassTableData(Long eventNr, Long parentClassUid) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtEventClass> eventClass = selectQuery.from(RtEventClass.class);
    Join<RtEventClass, RtCourse> joinCourse = eventClass.join(RtEventClass_.rtCourse, JoinType.LEFT);
    Join<RtEventClass, RtFeeGroup> joinFeeGroup = eventClass.join(RtEventClass_.rtFeeGroup, JoinType.LEFT);

    selectQuery.select(b.array(eventClass.get(RtEventClass_.parentUid), eventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid), eventClass.get(RtEventClass_.typeUid), joinCourse.get(RtCourse_.shortcut), joinFeeGroup.get(RtFeeGroup_.name), eventClass.get(RtEventClass_.sortcode), eventClass.get(RtEventClass_.courseGenerationTypeUid))).where(b.and(b.equal(eventClass.get(RtEventClass_.id).get(RtEventClassKey_.eventNr), eventNr), b.equal(eventClass.get(RtEventClass_.id).get(RtEventClassKey_.clientNr), ServerSession.get().getSessionClientNr()), (parentClassUid == null ? b.isNull(eventClass.get(RtEventClass_.parentUid)) : b.equal(eventClass.get(RtEventClass_.parentUid), parentClassUid))));
    return JPAUtility.convertList2Array(JPA.createQuery(selectQuery).getResultList());
  }

  @Override
  public Object[][] getCourseTableData(Long eventNr) throws ProcessingException {
    String queryString = "SELECT " + "C.id.courseNr, " + "C.rtEvent.id.eventNr, " + "C.shortcut, " + "C.length, " + "C.climb, " + "999, " + // calculated below
    "'abc' " + // calculated below
    "FROM RtCourse C " + "WHERE C.rtEvent.id.eventNr = :eventNr " + "AND C.id.clientNr = :clientNr ";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("eventNr", eventNr);
    Object[][] data = JPAUtility.convertList2Array(query.getResultList());

    for (Object[] row : data) {
      Long courseNr = TypeCastUtility.castValue(row[0], Long.class);

      // control count
      Long maxControlCount = BEANS.get(ICourseProcessService.class).getControlCount(courseNr);
      row[5] = maxControlCount;

      // classes
      queryString = "SELECT rtUc1.shortcut FROM RtEventClass EC WHERE EC.courseNr = :courseNr order by 1";
      query = JPA.createQuery(queryString, String.class);
      query.setParameter("courseNr", courseNr);
      List<?> shortcuts = query.getResultList();
      String shortcutString = CollectionUtility.format(shortcuts, ", ");
      row[6] = shortcutString;
    }

    return data;
  }

  @Override
  public List<CourseControlRowData> getCourseControlTableData(Long courseNr) throws ProcessingException {

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtCourseControl> courseControl = selectQuery.from(RtCourseControl.class);
    Join<RtCourseControl, RtControl> joinControl = courseControl.join(RtCourseControl_.rtControl, JoinType.INNER);
    Join<RtCourseControl, RtCourseControl> joinMasterCourseControl = courseControl.join(RtCourseControl_.rtMasterCourseControl, JoinType.LEFT);
    Join<RtCourseControl, RtControl> joinMasterControl = joinMasterCourseControl.join(RtCourseControl_.rtControl, JoinType.LEFT);

    selectQuery.select(b.array(courseControl.get(RtCourseControl_.id).get(RtCourseControlKey_.courseControlNr), courseControl.get(RtCourseControl_.controlNr), joinControl.get(RtControl_.controlNo), courseControl.get(RtCourseControl_.forkTypeUid), courseControl.get(RtCourseControl_.forkMasterCourseControlNr), joinMasterControl.get(RtControl_.controlNo), courseControl.get(RtCourseControl_.forkVariantCode), joinControl.get(RtControl_.typeUid), courseControl.get(RtCourseControl_.sortcode), courseControl.get(RtCourseControl_.countLeg), courseControl.get(RtCourseControl_.mandatory))).where(b.and(b.equal(courseControl.get(RtCourseControl_.id).get(RtCourseControlKey_.clientNr), ServerSession.get().getSessionClientNr()), b.equal(courseControl.get(RtCourseControl_.courseNr), courseNr))).orderBy(b.asc(courseControl.get(RtCourseControl_.sortcode)));

    Object[][] data = JPAUtility.convertList2Array(JPA.createQuery(selectQuery).getResultList());

    List<CourseControlRowData> result = new ArrayList<>();
    for (Object[] row : data) {
      CourseControlRowData r = new CourseControlRowData();
      r.setCourseControlNr(TypeCastUtility.castValue(row[0], Long.class));
      r.setControlNr(TypeCastUtility.castValue(row[1], Long.class));
      r.setControlNo(TypeCastUtility.castValue(row[2], String.class));
      r.setForkTypeUid(TypeCastUtility.castValue(row[3], Long.class));
      r.setForkMasterCourseControlNr(TypeCastUtility.castValue(row[4], Long.class));
      r.setForkMasterCourseControlNo(TypeCastUtility.castValue(row[5], String.class));
      r.setForkVariantCode(TypeCastUtility.castValue(row[6], String.class));
      r.setTypeUid(TypeCastUtility.castValue(row[7], Long.class));
      r.setSortCode(TypeCastUtility.castValue(row[8], Long.class));
      r.setCountLeg(BooleanUtility.nvl(TypeCastUtility.castValue(row[9], Boolean.class)));
      r.setMandatory(BooleanUtility.nvl(TypeCastUtility.castValue(row[10], Boolean.class)));
      result.add(r);
    }
    return result;
  }

  @Override
  public Object[][] getControlTableData(Long eventNr, ControlsSearchFormData formData) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtControl> select = b.createQuery(RtControl.class);
    Root<RtControl> rtcontrol = select.from(RtControl.class);

    JPAControlSearchFormDataStatementBuilder builder = new JPAControlSearchFormDataStatementBuilder(rtcontrol);
    builder.build(formData);

    select.where(b.and(b.equal(rtcontrol.get(RtControl_.id).get(RtControlKey_.clientNr), ServerSession.get().getSessionClientNr()), b.equal(rtcontrol.get(RtControl_.eventNr), eventNr), builder.getPredicate()));
    List<RtControl> result = JPA.createQuery(select).getResultList();

    // format data and load replacement controls
    Object[][] array = new Object[result.size()][8];
    int k = 0;
    for (RtControl control : result) {
      List<ReplacementControlRowData> replacementControls = getReplacementControlTableData(control.getId().getId());
      List<String> replacementControlList = new ArrayList<>();
      for (ReplacementControlRowData row : replacementControls) {
        replacementControlList.add(row.getReplacementControlNo());
      }
      Collections.sort(replacementControlList);
      array[k][0] = control.getId().getId();
      array[k][1] = control.getControlNo();
      array[k][2] = control.getTypeUid();
      array[k][3] = control.getLocalx();
      array[k][4] = control.getLocaly();
      array[k][5] = control.getGlobalx();
      array[k][6] = control.getGlobaly();
      array[k][7] = CollectionUtility.format(replacementControlList);
      k++;
    }

    return array;
  }

  @Override
  public List<RaceControlRowData> getRaceControlTableData(Long clientNr, Long... raceNr) throws ProcessingException {

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRaceControl> raceControl = selectQuery.from(RtRaceControl.class);
    Join<RtRaceControl, RtRace> joinRace = raceControl.join(RtRaceControl_.rtRace, JoinType.LEFT);
    Join<RtRaceControl, RtControl> joinControl = raceControl.join(RtRaceControl_.rtControl, JoinType.LEFT);
    Join<RtRace, RtEvent> joinEvent = joinRace.join(RtRace_.rtEvent, JoinType.LEFT);
    Join<RtRace, RtEventClass> joinEventClass = joinRace.join(RtRace_.rtEventClass, JoinType.LEFT);
    Join<RtRaceControl, RtCourseControl> joinCourseControl = raceControl.join(RtRaceControl_.rtCourseControl, JoinType.LEFT);

    selectQuery.select(b.array(raceControl.get(RtRaceControl_.raceNr), raceControl.get(RtRaceControl_.id).get(RtRaceControlKey_.raceControlNr), raceControl.get(RtRaceControl_.courseControlNr), joinControl.get(RtControl_.typeUid), joinControl.get(RtControl_.controlNo), raceControl.get(RtRaceControl_.sortcode), raceControl.get(RtRaceControl_.statusUid), joinEvent.get(RtEvent_.evtZero), joinRace.get(RtRace_.legStartTime), raceControl.get(RtRaceControl_.overallTime), joinEventClass.get(RtEventClass_.timePrecisionUid), joinCourseControl.get(RtCourseControl_.countLeg), raceControl.get(RtRaceControl_.legTime), raceControl.get(RtRaceControl_.manualStatus), raceControl.get(RtRaceControl_.shiftTime))).where(b.and((raceNr == null || raceNr.length == 0) ? b.conjunction() : raceControl.get(RtRaceControl_.raceNr).in(Arrays.asList(raceNr)), b.equal(raceControl.get(RtRaceControl_.id).get(RtRaceControlKey_.clientNr), ServerSession.get().getSessionClientNr()))).orderBy(b.asc(raceControl.get(RtRaceControl_.raceNr)), b.asc(raceControl.get(RtRaceControl_.sortcode))); // important for multiple race nrs
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    List<RaceControlRowData> result = new ArrayList<>();
    for (Object[] row : resultList) {
      RaceControlRowData r = new RaceControlRowData();
      r.setRaceNr(TypeCastUtility.castValue(row[0], Long.class));
      r.setRaceControlNr(TypeCastUtility.castValue(row[1], Long.class));
      r.setCourseControlNr(TypeCastUtility.castValue(row[2], Long.class));
      r.setTypeUid(TypeCastUtility.castValue(row[3], Long.class));
      r.setControlNo(TypeCastUtility.castValue(row[4], String.class));
      r.setSortCode(TypeCastUtility.castValue(row[5], Long.class));
      r.setStatusUid(TypeCastUtility.castValue(row[6], Long.class));
      Date evtZero = TypeCastUtility.castValue(row[7], Date.class);
      Long legStartTime = TypeCastUtility.castValue(row[8], Long.class);
      Long overallTime = TypeCastUtility.castValue(row[9], Long.class);
      Long precisionUid = TypeCastUtility.castValue(row[10], Long.class);
      boolean countLeg = BooleanUtility.nvl(TypeCastUtility.castValue(row[11], Boolean.class));
      Long legTime = TypeCastUtility.castValue(row[12], Long.class);
      r.setManualStatus(TypeCastUtility.castValue(row[13], Boolean.class));
      r.setShiftTime(TypeCastUtility.castValue(row[14], Long.class));

      // overallTime
      if (evtZero != null && legStartTime != null && overallTime != null) {
        Date overallDate = FMilaUtility.addMilliSeconds(evtZero, legStartTime + overallTime);
        r.setOverallTime(overallDate);
      }
      r.setOverallTimeRaw(overallTime);

      // relative Time
      if (overallTime != null) {
        r.setRelativeTime(FMilaUtility.formatTime(overallTime, precisionUid));
      }

      // legTime
      String legTimeString = FMilaUtility.formatTime(legTime, precisionUid);
      if (!StringUtility.isNullOrEmpty(legTimeString)) {
        r.setLegTime(countLeg ? legTimeString : StringUtility.box("(", legTimeString, ")"));
      }
      r.setLegTimeRaw(legTime);

      // countLeg
      r.setCountLeg(countLeg);
      result.add(r);
    }
    return result;
  }

  @Override
  public List<ReplacementControlRowData> getReplacementControlTableData(long controlNr) throws ProcessingException {
    String queryString = "SELECT CR.id.controlNr, CR.id.replacementControlNr, C.controlNo, R.controlNo " + "FROM RtControlReplacement CR " + "INNER JOIN CR.rtControl1 C " + "INNER JOIN CR.rtControl2 R " + "WHERE CR.id.controlNr = :controlNr " + "AND CR.id.clientNr = :clientNr";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("controlNr", controlNr);
    List list = query.getResultList();
    List<ReplacementControlRowData> result = new ArrayList<>();
    for (Object rowObject : list) {
      Object[] row = (Object[]) rowObject;
      ReplacementControlRowData resultRow = new ReplacementControlRowData();
      resultRow.setControlNr(TypeCastUtility.castValue(row[0], Long.class));
      resultRow.setReplacementControlNr(TypeCastUtility.castValue(row[1], Long.class));
      resultRow.setControlNo(TypeCastUtility.castValue(row[2], String.class));
      resultRow.setReplacementControlNo(TypeCastUtility.castValue(row[3], String.class));
      result.add(resultRow);
    }
    return result;
  }

  @Override
  public List<RtMap> getMapTableData(Long eventNr) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtMap> select = b.createQuery(RtMap.class);
    Root<RtMap> rtmap = select.from(RtMap.class);

    List<Predicate> list = new ArrayList<>();
    Predicate restriction1 = b.equal(rtmap.get(RtMap_.key).get(RtMapKey_.clientNr), ServerSession.get().getSessionClientNr());
    list.add(restriction1);
    if (eventNr != null) {
      Join<RtMap, RtEventMap> joinEventMap = rtmap.join(RtMap_.rtEventMaps, JoinType.LEFT);
      Predicate restriction2 = b.equal(joinEventMap.get(RtEventMap_.id).get(RtEventMapKey_.eventNr), eventNr);
      list.add(restriction2);
    }

    select.where(b.and(list.toArray(new Predicate[list.size()])));

    return JPA.createQuery(select).getResultList();
  }

  @Override
  public Object[][] getEventAdditionalInformationTableData(long eventNr) throws ProcessingException {
    String queryString = "SELECT EA.id.additionalInformationUid " + "FROM RtEventAdditionalInformation EA " + "WHERE EA.id.eventNr = :eventNr " + "AND EA.id.clientNr = :clientNr";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("eventNr", eventNr);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    return JPAUtility.convertList2Array(query.getResultList());
  }

  @Override
  public Object[][] getRankingTableData() throws ProcessingException {
    String queryString = "SELECT " + "RK.id.rankingNr, " + "RK.name, " + "COUNT(RE.id.eventNr), " + "RK.formatUid, " + "RK.sortingUid, " + "RK.formula " + "FROM RtRanking RK " + "LEFT JOIN RK.rtRankingEvents RE " + "WHERE RK.id.clientNr = :clientNr " + "GROUP BY RK.id.rankingNr, RK.name, RK.formatUid, RK.sortingUid, RK.formula ";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());

    return JPAUtility.convertList2Array(query.getResultList());
  }

  @Override
  public Object[][] getRankingEventTableData(Long rankingNr) throws ProcessingException {
    String queryString = "SELECT " + "RE.id.eventNr, " + "E.name, " + "RE.formatUid, " + "RE.sortingUid, " + "RE.sortcode, " + "RE.formula " + "FROM RtRankingEvent RE " + "INNER JOIN RE.rtEvent E " + "WHERE RE.id.rankingNr = :rankingNr AND RE.id.clientNr = :clientNr";

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("rankingNr", rankingNr);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());

    return JPAUtility.convertList2Array(query.getResultList());
  }

  @Override
  public Object[][] getRankingClassesTableData(Long rankingNr) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRankingEvent> rankingEvent = selectQuery.from(RtRankingEvent.class);
    Join<RtRankingEvent, RtEvent> joinEvent = rankingEvent.join(RtRankingEvent_.rtEvent, JoinType.INNER);
    Join<RtEvent, RtEventClass> joinEventClass = joinEvent.join(RtEvent_.rtEventClasses, JoinType.INNER);

    Subquery<Long> numberOfEventsSubselect = selectQuery.subquery(Long.class);
    Root<RtRankingEvent> subroot = numberOfEventsSubselect.from(RtRankingEvent.class);
    numberOfEventsSubselect.select(b.count(subroot.get(RtRankingEvent_.id).get(RtRankingEventKey_.rankingNr))).where(b.and(b.equal(subroot.get(RtRankingEvent_.id).get(RtRankingEventKey_.rankingNr), rankingNr), b.equal(subroot.get(RtRankingEvent_.id).get(RtRankingEventKey_.clientNr), ServerSession.get().getSessionClientNr())));

    selectQuery.select(b.array(joinEventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid), b.count(rankingEvent.get(RtRankingEvent_.id).get(RtRankingEventKey_.eventNr)))).where(b.and(b.equal(rankingEvent.get(RtRankingEvent_.id).get(RtRankingEventKey_.rankingNr), rankingNr), b.equal(rankingEvent.get(RtRankingEvent_.id).get(RtRankingEventKey_.clientNr), ServerSession.get().getSessionClientNr()))).groupBy(joinEventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid)).having(b.equal(b.count(rankingEvent.get(RtRankingEvent_.id).get(RtRankingEventKey_.eventNr)), numberOfEventsSubselect));

    return JPAUtility.convertList2Array(JPA.createQuery(selectQuery).getResultList());
  }
}
