package com.rtiming.server.race;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.event.course.ServerCache;
import com.rtiming.server.event.course.loop.CourseCalculator;
import com.rtiming.server.race.validation.RaceValidationResult;
import com.rtiming.server.race.validation.RaceValidationUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControl_;
import com.rtiming.shared.dao.RtCourseControl;
import com.rtiming.shared.dao.RtCourseControl_;
import com.rtiming.shared.dao.RtEventClassKey;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey_;
import com.rtiming.shared.dao.RtPunchSession_;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceControl;
import com.rtiming.shared.dao.RtRaceControlKey_;
import com.rtiming.shared.dao.RtRaceControl_;
import com.rtiming.shared.dao.RtRaceKey_;
import com.rtiming.shared.dao.RtRace_;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.event.course.IControlProcessService;
import com.rtiming.shared.race.IRaceControlProcessService;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.race.RaceControlFormData;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

public class RaceService  implements IRaceService {

  @Override
  public void inflateRaceControls(Long[] raceNrs) throws ProcessingException {
    if (raceNrs == null || raceNrs.length == 0) {
      return;
    }
    String queryString = "DELETE FROM RtRaceControl " +
        "WHERE statusUid = " + ControlStatusCodeType.InitialStatusCode.ID + " " +
        "AND raceNr IN :raceNrs " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("raceNrs", Arrays.asList(raceNrs));
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();

    HashMap<RtEventClassKey, List<List<CourseControlRowData>>> courseCache = new HashMap<>();
    HashMap<RtEventClassKey, RaceSettings> settingsCache = new HashMap<>();

    for (Long raceNr : raceNrs) {
      RaceBean race = new RaceBean();
      race.setRaceNr(raceNr);
      race = BEANS.get(IRaceProcessService.class).load(race);

      // cache key
      RtEventClassKey pk = new RtEventClassKey();
      pk.setEventNr(race.getEventNr());
      pk.setClassUid(race.getLegClassUid());
      pk.setClientNr(race.getClientNr());

      RaceSettings settings = settingsCache.get(pk);
      if (settings == null) {
        settings = loadSettings(race.getEventNr(), race.getLegClassUid());
        settingsCache.put(pk, settings);
      }

      if (CompareUtility.equals(CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID, settings.getCourseGenerationTypeUid())) {
        // get cached course variants
        List<List<CourseControlRowData>> result = courseCache.get(pk);
        if (result == null) {
          List<CourseControlRowData> courseControls = BEANS.get(IEventsOutlineService.class).getCourseControlTableData(settings.getCourseNr());
          result = CourseCalculator.calculateCourse(courseControls);
          courseCache.put(pk, result);
        }

        List<CourseControlRowData> chosenCourse = result.get((int) NumberUtility.randomInt(result.size()));
        List<RaceControlFormData> raceControls = new ArrayList<>();
        long sortCode = 1;
        for (CourseControlRowData control : chosenCourse) {
          RaceControlFormData raceControl = new RaceControlFormData();
          raceControl.getRace().setValue(raceNr);
          raceControl.setCourseControlNr(control.getCourseControlNr());
          raceControl.getControl().setValue(control.getControlNr());
          raceControl.getSortCode().setValue(sortCode);
          raceControl.getControlStatus().setValue(ControlStatusCodeType.InitialStatusCode.ID);
          raceControl.getManualStatus().setValue(false);
          raceControls.add(raceControl);
          sortCode++;
        }

        for (RaceControlFormData raceControl : raceControls) {
          BEANS.get(IRaceControlProcessService.class).create(raceControl);
        }
      }
    }

  }

  @Override
  public void validateAndPersistRace(long raceNr) throws ProcessingException {

    // load race
    RaceBean race = loadRace(raceNr);

    // check if automated validation is enabled
    if (BooleanUtility.nvl(race.isManualStatus())) {
      return;
    }

    // load settings
    RaceSettings settings = loadSettings(race.getEventNr(), race.getLegClassUid());

    // reset all controls
    resetControls(raceNr, false);

    RaceValidationResult result = validateRaceInternal(race, settings);

    // store race control data
    for (RaceControlBean raceControl : result.getControls()) {
      if (raceControl.getRaceControlNr() != null) {
        storeRaceControl(raceControl);
      }
      else {
        createRaceControl(raceControl, race.getEventNr(), raceNr);
      }
    }

    // update race status and race time
    updateRace(race,
        result.getStartTime(),
        result.getLegTime(),
        result.getStatusUid(),
        true,
        settings.isOneRaceClassTypeUid());

    // update participation status and summary time
    updateParticipation(race.getEntryNr(),
        race.getEventNr(),
        result.getStatusUid(),
        settings.isSummaryTimeIsMaxTime());
  }

  @Override
  public RaceBean validateRace(long raceNr) throws ProcessingException {
    // load race
    RaceBean race = loadRace(raceNr);

    // load settings
    RaceSettings settings = loadSettings(race.getEventNr(), race.getLegClassUid());

    RaceValidationResult result = validateRaceInternal(race, settings);
    race.setLegStartTime(result.getStartTime());
    race.setLegTime(result.getLegTime());
    race.setStatusUid(result.getStatusUid());

    return race;
  }

  protected RaceValidationResult validateRaceInternal(RaceBean race, RaceSettings settings) throws ProcessingException, VetoException {
    // load all punches of punch sessions pointing to race
    List<RaceControlBean> punchedControls = loadPunchedControls(race.getRaceNr());

    // load punch session
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtPunchSession> selectQuery = b.createQuery(RtPunchSession.class);
    Root<RtPunchSession> punchSession = selectQuery.from(RtPunchSession.class);

    selectQuery.where(
        b.and(
            b.equal(punchSession.get(RtPunchSession_.raceNr), race.getRaceNr()),
            b.equal(punchSession.get(RtPunchSession_.id).get(RtPunchSessionKey_.clientNr), ServerSession.get().getSessionClientNr()))
        );
    List<RtPunchSession> resultList = JPA.createQuery(selectQuery).getResultList();
    if (resultList.size() > 1) {
      throw new VetoException(TEXTS.get("MultipleDownloadedECardsMessage"));
    }
    else if (resultList.size() != 1) {
      throw new VetoException(TEXTS.get("NoECardAssignedMessage"));
    }

    Long punchStartTime = resultList.get(0).getStart();
    Long finishTime = resultList.get(0).getFinish();
    Long punchSessionNr = resultList.get(0).getId().getId();
    Long legStartTime = race.getLegStartTime();

    RaceValidationResult validationResult = null;

    // load all race controls (start, controls, finish) of race
    List<RaceControlBean> plannedControls = loadPlannedControls(race.getRaceNr());
    if (CompareUtility.equals(settings.getCourseGenerationTypeUid(), CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID)) {
      validationResult = RaceValidationUtility.validateRace(punchSessionNr, plannedControls, punchedControls, punchStartTime, legStartTime, finishTime, settings);
    }
    else {
      List<CourseControlRowData> courseControls = BEANS.get(IEventsOutlineService.class).getCourseControlTableData(settings.getCourseNr());
      validationResult = CourseCalculator.validateAllVariants(punchedControls, courseControls, plannedControls, settings, punchStartTime, finishTime, legStartTime, punchSessionNr);
      if (plannedControls.size() > 0) {
        resetControls(race.getRaceNr(), true);
      }
    }

    return validationResult;
  }

  private RaceBean loadRace(long raceNr) throws ProcessingException {
    RaceBean race = new RaceBean();
    race.setRaceNr(raceNr);
    race = BEANS.get(IRaceProcessService.class).load(race);
    return race;
  }

  protected List<RaceControlBean> loadPlannedControls(long raceNr) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRaceControl> raceControl = selectQuery.from(RtRaceControl.class);
    Join<RtRaceControl, RtRace> joinRace = raceControl.join(RtRaceControl_.rtRace, JoinType.INNER);
    Join<RtRaceControl, RtCourseControl> joinCourseControl = raceControl.join(RtRaceControl_.rtCourseControl, JoinType.INNER);
    Join<RtCourseControl, RtControl> joinControl = joinCourseControl.join(RtCourseControl_.rtControl, JoinType.INNER);

    selectQuery.select(b.array(
        raceControl.get(RtRaceControl_.sortcode),
        joinControl.get(RtControl_.controlNo),
        raceControl.get(RtRaceControl_.controlNr),
        raceControl.get(RtRaceControl_.id).get(RtRaceControlKey_.raceControlNr),
        raceControl.get(RtRaceControl_.courseControlNr),
        joinCourseControl.get(RtCourseControl_.countLeg),
        joinCourseControl.get(RtCourseControl_.mandatory),
        joinControl.get(RtControl_.typeUid),
        raceControl.get(RtRaceControl_.shiftTime),
        raceControl.get(RtRaceControl_.manualStatus),
        raceControl.get(RtRaceControl_.statusUid),
        joinRace.get(RtRace_.legStartTime),
        raceControl.get(RtRaceControl_.overallTime),
        joinRace.get(RtRace_.eventNr)
        ))
        .where(
            b.and(
                b.equal(joinRace.get(RtRace_.id).get(RtRaceKey_.raceNr), raceNr),
                b.equal(joinRace.get(RtRace_.id).get(RtRaceKey_.clientNr), ServerSession.get().getSessionClientNr()))
        )
        .orderBy(b.asc(raceControl.get(RtRaceControl_.raceNr)), b.asc(raceControl.get(RtRaceControl_.sortcode))
        );
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    List<RaceControlBean> result = new ArrayList<>();
    for (Object[] row : resultList) {
      RaceControlBean bean = new RaceControlBean();
      bean.setSortcode(TypeCastUtility.castValue(row[0], Long.class));
      bean.setControlNo(TypeCastUtility.castValue(row[1], String.class));
      bean.setControlNr(TypeCastUtility.castValue(row[2], Long.class));
      bean.setRaceControlNr(TypeCastUtility.castValue(row[3], Long.class));
      bean.setCourseControlNr(TypeCastUtility.castValue(row[4], Long.class));
      bean.setCountLeg(BooleanUtility.nvl(TypeCastUtility.castValue(row[5], Boolean.class)));
      bean.setMandatory(BooleanUtility.nvl(TypeCastUtility.castValue(row[6], Boolean.class)));
      bean.setTypeUid(TypeCastUtility.castValue(row[7], Long.class));
      bean.setShiftTime(TypeCastUtility.castValue(row[8], Long.class));
      bean.setManualStatus(BooleanUtility.nvl(TypeCastUtility.castValue(row[9], Boolean.class)));
      bean.setControlStatusUid(TypeCastUtility.castValue(row[10], Long.class));

      // manual punch time
      Long legStartTime = TypeCastUtility.castValue(row[11], Long.class);
      Long overallTime = TypeCastUtility.castValue(row[12], Long.class);
      if (bean.isManualStatus() && legStartTime != null && overallTime != null) {
        bean.setPunchTime(legStartTime + overallTime);
      }

      // fetch replacement controls from cache
      Long eventNr = TypeCastUtility.castValue(row[13], Long.class);
      Map<Long, List<String>> replacementControls = ServerCache.getReplacementControls(eventNr);
      bean.setReplacementControlsByList(replacementControls.get(bean.getControlNr()));
      result.add(bean);
    }
    return result;
  }

  protected List<RaceControlBean> loadPunchedControls(long raceNr) throws ProcessingException {
    String queryString = "SELECT " +
        "P.id.sortcode, " +
        "P.controlNo, " +
        "P.time " +
        "FROM RtPunch P " +
        "INNER JOIN P.rtPunchSession PS " +
        "WHERE PS.raceNr = :raceNr " +
        "AND PS.id.clientNr = :sessionClientNr " +
        "ORDER BY P.id.sortcode ASC ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("raceNr", raceNr);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    List result = query.getResultList();
    List<RaceControlBean> list = new ArrayList<>();
    for (Object rowObject : result) {
      Object[] row = (Object[]) rowObject;
      RaceControlBean bean = new RaceControlBean();
      bean.setSortcode(TypeCastUtility.castValue(row[0], Long.class));
      bean.setControlNo(TypeCastUtility.castValue(row[1], String.class));
      bean.setPunchTime(TypeCastUtility.castValue(row[2], Long.class));
      bean.setTypeUid(ControlTypeCodeType.ControlCode.ID);
      list.add(bean);
    }
    return list;
  }

  protected void updateRace(RaceBean race, Long startTime, Long legTime, Long statusUid, boolean resetLegStartTime, boolean allRacesOfEntry) throws ProcessingException {
    String queryString = "UPDATE RtRace " +
        "SET statusUid = :statusUid, " +
        "legTime = :legTime " +
        (resetLegStartTime ? ", legStartTime = :startTime " : "") +
        "WHERE entryNr = :entryNr " +
        (allRacesOfEntry ? "" : "AND id.raceNr = :raceNr ") +
        "AND eventNr = :eventNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    if (resetLegStartTime) {
      query.setParameter("startTime", startTime);
    }
    query.setParameter("legTime", legTime);
    if (!allRacesOfEntry) {
      query.setParameter("raceNr", race.getRaceNr());
    }
    query.setParameter("entryNr", race.getEntryNr());
    query.setParameter("eventNr", race.getEventNr());
    query.setParameter("statusUid", statusUid);
    query.executeUpdate();
  }

  protected void updateParticipation(Long entryNr, Long eventNr, Long statusUid, boolean summaryTimeIsMaxTime) throws ProcessingException {
    String queryString = "UPDATE RtParticipation P " +
        "SET " +
        "statusUid = :statusUid, " +
        (summaryTimeIsMaxTime ?
            // individual race and combined teams
            "summaryTime = (SELECT MAX(legTime) " +
                "            FROM RtRace X " +
                "            WHERE X.entryNr = P.id.entryNr " +
                "            AND X.eventNr = P.id.eventNr), " :
            // relays, score, etc.
            // TODO this is not correct for team relays
            "summaryTime = (SELECT SUM(legTime) " +
                "            FROM RtRace X " +
                "            WHERE X.entryNr = P.id.entryNr " +
                "            AND X.eventNr = P.id.eventNr), "
        ) +
        // for all types, the participation start is the minimal leg start
        "startTime = (SELECT MIN(legStartTime) " +
        "              FROM RtRace X " +
        "              WHERE X.entryNr = P.id.entryNr " +
        "              AND X.eventNr = P.id.eventNr) " +
        "WHERE id.entryNr = :entryNr " +
        "AND id.eventNr = :eventNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("entryNr", entryNr);
    query.setParameter("eventNr", eventNr);
    query.setParameter("statusUid", statusUid);
    query.executeUpdate();
  }

  protected void resetControls(long raceNr, boolean removeAll) throws ProcessingException {
    if (removeAll) {
      // delete all controls
      String queryString = "DELETE FROM RtRaceControl " +
          "WHERE raceNr = :raceNr " +
          "AND id.clientNr = :sessionClientNr ";
      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("raceNr", raceNr);
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();
    }
    else {
      // reset all controls
      String queryString = "DELETE FROM RtRaceControl " +
          "WHERE raceNr = :raceNr " +
          "AND id.clientNr = :sessionClientNr " +
          "AND statusUid IN (" + ControlStatusCodeType.AdditionalCode.ID + "," + ControlStatusCodeType.WrongCode.ID + ") ";
      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("raceNr", raceNr);
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();

      queryString = "UPDATE RtRaceControl " +
          "SET statusUid = CASE WHEN manualStatus = TRUE THEN statusUid ELSE :statusUid END, " + // do not reset if manual
          "overallTime = CASE WHEN manualStatus = TRUE THEN overallTime ELSE NULL END, " + // do not reset if manual
          "legTime = NULL " +
          "WHERE raceNr = :raceNr " +
          "AND id.clientNr = :sessionClientNr ";
      query = JPA.createQuery(queryString);
      query.setParameter("raceNr", raceNr);
      query.setParameter("statusUid", ControlStatusCodeType.InitialStatusCode.ID);
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();
    }
  }

  protected void storeRaceControl(RaceControlBean raceControl) throws ProcessingException {
    String queryString = "UPDATE RtRaceControl SET statusUid = :statusUid, " +
        "overallTime = :overallTime, " +
        "legTime = :legTime " +
        "WHERE id.raceControlNr = :raceControlNr " +
        "AND id.clientNr = :clientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("raceControlNr", raceControl.getRaceControlNr());
    query.setParameter("statusUid", raceControl.getControlStatusUid());
    query.setParameter("overallTime", raceControl.getOverallTime());
    query.setParameter("legTime", raceControl.getLegTime());
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();
  }

  private void createRaceControl(RaceControlBean raceControl, Long eventNr, Long raceNr) throws ProcessingException {
    RaceControlFormData formData = new RaceControlFormData();
    formData.getRace().setValue(raceNr);
    formData.setCourseControlNr(raceControl.getCourseControlNr());
    formData.getControl().setValue(raceControl.getControlNr());
    formData.getControlStatus().setValue(raceControl.getControlStatusUid());
    formData.getManualStatus().setValue(raceControl.isManualStatus());
    formData.getShiftTime().setValue(raceControl.getShiftTime());
    if (formData.getCourseControlNr() == null) {
      formData.getSortCode().setValue(FMilaUtility.MAX_SORTCODE);
    }
    else {
      formData.getSortCode().setValue(raceControl.getSortcode());
    }

    if (raceControl.getControlNr() == null) {
      // find control (with string and eventNr) and create new race control
      ControlFormData control = BEANS.get(IControlProcessService.class).find(raceControl.getControlNo(), eventNr);

      // could not even find control
      if (control.getControlNr() == null) {
        control.getType().setValue(ControlTypeCodeType.ControlCode.ID);
        control.getActive().setValue(false);
        control = BEANS.get(IControlProcessService.class).create(control);
      }

      formData.getControl().setValue(control.getControlNr());
    }

    formData = BEANS.get(IRaceControlProcessService.class).create(formData);
    // update time (cannot do this on form data, since it uses date)
    raceControl.setRaceControlNr(formData.getRaceControlNr());
    storeRaceControl(raceControl);
  }

  protected RaceSettings loadSettings(Long eventNr, Long classUid) throws ProcessingException {
    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(eventNr);
    eventClass.getClazz().setValue(classUid);
    eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);
    boolean isUsingStartlist = eventClass.getStartlistSettingNr() != null;
    // max time: max race time = summary time, otherwise: sum of all race times = summary time
    boolean summaryTimeIsMaxTime = CompareUtility.equals(eventClass.getType().getValue(), ClassTypeCodeType.IndividualEventCode.ID) ||
        CompareUtility.equals(eventClass.getType().getValue(), ClassTypeCodeType.TeamCombinedCourseCode.ID);
    boolean isOneRaceClassTypeUid = ClassTypeCodeType.isOneRaceType(eventClass.getType().getValue());
    Long timePrecisionUid = eventClass.getTimePrecision().getValue();

    RaceSettings settings = new RaceSettings();
    settings.setUsingStartlist(isUsingStartlist);
    settings.setSummaryTimeIsMaxTime(summaryTimeIsMaxTime);
    settings.setOneRaceClassTypeUid(isOneRaceClassTypeUid);
    settings.setTimePrecisionUid(timePrecisionUid);
    settings.setCourseNr(eventClass.getCourse().getValue());
    settings.setCourseGenerationTypeUid(eventClass.getCourseGenerationType().getValue());
    settings.setEventNr(eventNr);

    return settings;
  }

  @Override
  public void reset(long raceNr) throws ProcessingException {
    RaceBean race = loadRace(raceNr);
    RaceSettings settings = loadSettings(race.getEventNr(), race.getLegClassUid());
    resetControls(raceNr, false);
    updateRace(race, null, null, null, !settings.isUsingStartlist(), settings.isOneRaceClassTypeUid());
    updateParticipation(race.getEntryNr(), race.getEventNr(), null, settings.isSummaryTimeIsMaxTime());
  }

}
