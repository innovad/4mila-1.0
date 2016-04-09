package com.rtiming.server.ecard.download;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.event.course.loop.CourseCalculator;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreatePunchPermission;
import com.rtiming.shared.common.security.permission.ReadPunchPermission;
import com.rtiming.shared.common.security.permission.UpdatePunchPermission;
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControl_;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEvent_;
import com.rtiming.shared.dao.RtPunch;
import com.rtiming.shared.dao.RtPunchKey;
import com.rtiming.shared.dao.RtPunchKey_;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey;
import com.rtiming.shared.dao.RtPunchSession_;
import com.rtiming.shared.dao.RtPunch_;
import com.rtiming.shared.dao.RtRaceControl;
import com.rtiming.shared.dao.RtRaceControlKey_;
import com.rtiming.shared.dao.RtRaceControl_;
import com.rtiming.shared.ecard.download.IPunchProcessService;
import com.rtiming.shared.ecard.download.PunchFormData;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

public class PunchProcessService  implements IPunchProcessService {

  @Override
  public PunchFormData prepareCreate(PunchFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreatePunchPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    if (formData != null) {
      RtPunchSession session = JPA.find(RtPunchSession.class, RtPunchSessionKey.create(formData.getPunchSession().getValue()));
      if (session != null) {
        formData.getEvent().setValue(session.getEventNr());
      }
    }

    return formData;
  }

  @Override
  public PunchFormData create(PunchFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreatePunchPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtPunch punch = new RtPunch();
    RtPunchKey key = new RtPunchKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setId(formData.getPunchSession().getValue());
    key.setSortcode(formData.getSortCode().getValue());
    punch.setId(key);
    JPA.persist(punch);

    formData = store(formData);

    return formData;
  }

  @Override
  public PunchFormData load(PunchFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadPunchPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtPunch> punch = selectQuery.from(RtPunch.class);
    Join<RtPunch, RtPunchSession> joinPunchSession = punch.join(RtPunch_.rtPunchSession, JoinType.INNER);
    Join<RtPunchSession, RtEvent> joinEvent = joinPunchSession.join(RtPunchSession_.rtEvent, JoinType.INNER);

    selectQuery.select(b.array(
        punch.get(RtPunch_.controlNo),
        punch.get(RtPunch_.time),
        joinEvent.get(RtEvent_.evtZero),
        joinPunchSession.get(RtPunchSession_.eventNr)
        ))
        .where(
            b.and(
                b.equal(punch.get(RtPunch_.id).get(RtPunchKey_.punchSessionNr), formData.getPunchSession().getValue()),
                b.equal(punch.get(RtPunch_.id).get(RtPunchKey_.sortcode), formData.getSortCode().getValue()))
        );
    List<Object[]> result = JPA.createQuery(selectQuery).getResultList();
    if (result.size() > 0) {
      Object[] row = result.get(0);
      formData.getControlNo().setValue(TypeCastUtility.castValue(row[0], String.class));
      Long time = TypeCastUtility.castValue(row[1], Long.class);
      Date evtZero = TypeCastUtility.castValue(row[2], Date.class);
      formData.getTime().setValue(FMilaUtility.addMilliSeconds(evtZero, time));
      formData.getEvent().setValue(TypeCastUtility.castValue(row[3], Long.class));
    }

    return formData;
  }

  @Override
  public PunchFormData store(PunchFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdatePunchPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    Long relativeTime = formData.getRawTime();
    if (formData.getRawTime() == null && formData.getTime().getValue() != null) {
      // Time set in GUI
      Date zeroTime = BEANS.get(IEventProcessService.class).getZeroTime(formData.getEvent().getValue());
      relativeTime = FMilaUtility.getDateDifferenceInMilliSeconds(zeroTime, formData.getTime().getValue());
    }

    String queryString = "UPDATE RtPunch P " +
        "SET P.controlNo = :controlNo, " +
        "P.time = :relativeTime " +
        "WHERE P.id.punchSessionNr = :punchSession " +
        "AND P.id.sortcode = :sortCode " +
        "AND P.id.clientNr = :sessionClientNr ";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.setParameter("relativeTime", relativeTime);
    query.executeUpdate();

    return formData;
  }

  @Override
  public void delete(long punchSessionNr, Long[] sortCodes) throws ProcessingException {
    boolean restrictSortcodes = sortCodes != null && sortCodes.length > 0;

    String queryString = "DELETE FROM RtPunch R " +
        "WHERE :punchSessionNr = R.id.punchSessionNr " +
        (restrictSortcodes ? "AND R.id.sortcode in :sortCodes " : "");

    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("punchSessionNr", punchSessionNr);
    if (restrictSortcodes) {
      query.setParameter("sortCodes", Arrays.asList(sortCodes));
    }
    query.executeUpdate();

  }

  @Override
  public void createTestData(long punchSessionNr, Long raceNr, Long eventNr, Long classUid) throws ProcessingException {

    // settings
    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(eventNr);
    eventClass.getClazz().setValue(classUid);
    eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);

    // start time in 4 hours
    long start = (long) (Math.floor(Math.floor(NumberUtility.randomDouble() * 1000 * 60 * 60 * 4) / 1000 / 60) * 1000 * 60);

    // Speed Factor
    Double factor = NumberUtility.randomDouble() + 0.5;
    Long maxTime = Long.MIN_VALUE;

    if (CompareUtility.equals(eventClass.getCourseGenerationType().getValue(), CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID)) {
      List<CourseControlRowData> definitions = BEANS.get(IEventsOutlineService.class).getCourseControlTableData(eventClass.getCourse().getValue());
      List<List<CourseControlRowData>> courses = CourseCalculator.calculateCourse(definitions);
      if (courses.isEmpty()) {
        throw new VetoException("Could not create course variants");
      }
      List<CourseControlRowData> course = courses.get((int) NumberUtility.randomInt(courses.size()));
      long k = 0;
      for (CourseControlRowData control : course) {
        if (CompareUtility.equals(control.getTypeUid(), ControlTypeCodeType.ControlCode.ID)) {
          PunchFormData formData = new PunchFormData();
          formData.getPunchSession().setValue(punchSessionNr);
          formData.getControlNo().setValue(control.getControlNo());
          formData.getSortCode().setValue(k);
          formData.setRawTime(NumberUtility.toLong(start + (factor * k * 1000 * 225) + (120 * 1000 - NumberUtility.randomInt(60 * 1000))));
          maxTime = Math.max(maxTime, formData.getRawTime());
          BEANS.get(IPunchProcessService.class).create(formData);
          k++;
        }
      }
    }
    else {
      CriteriaBuilder b = JPA.getCriteriaBuilder();
      CriteriaQuery<String> selectQuery = b.createQuery(String.class);
      Root<RtRaceControl> root = selectQuery.from(RtRaceControl.class);
      Join<RtRaceControl, RtControl> joinControl = root.join(RtRaceControl_.rtControl, JoinType.INNER);

      selectQuery.select(joinControl.get(RtControl_.controlNo))
          .where(
              b.and(
                  b.equal(root.get(RtRaceControl_.id).get(RtRaceControlKey_.clientNr), ServerSession.get().getSessionClientNr()),
                  b.equal(root.get(RtRaceControl_.raceNr), raceNr)
                  )
          ).
          orderBy(b.asc(root.get(RtRaceControl_.sortcode)));

      List<String> raceControls = JPA.createQuery(selectQuery).getResultList();
      Long sortcode = 1L;
      for (String controlNo : raceControls) {
        RtPunch punch = new RtPunch();
        RtPunchKey key = new RtPunchKey();
        key.setClientNr(ServerSession.get().getSessionClientNr());
        key.setId(punchSessionNr);
        key.setSortcode(sortcode);
        punch.setId(key);
        punch.setControlNo(controlNo);
        punch.setTime(NumberUtility.toLong(start + sortcode * 1000 * 225 * factor + Math.floor(Math.random() - 1) * 120 * 1000));
        JPA.persist(punch);
        maxTime = Math.max(maxTime, punch.getTime());
        sortcode++;
      }

//      SQL.insert("INSERT INTO RT_PUNCH (PUNCH_SESSION_NR, CLIENT_NR, SORTCODE, CONTROL_NO, TIME) " +
//          "SELECT :punchSessionNr, RC.CLIENT_NR, ROW_NUMBER() OVER (ORDER BY 1) AS SORTCODE, C.CONTROL_NO, " + start + " + RC.SORTCODE * 1000 * 225 * :factor + FLOOR((RANDOM()-1)*120*1000) " +
//          "FROM RT_RACE_CONTROL RC " +
//          "INNER JOIN RT_PUNCH_SESSION PS ON RC.RACE_NR = PS.RACE_NR " +
//          "INNER JOIN RT_CONTROL C ON C.CONTROL_NR = RC.CONTROL_NR " +
//          "WHERE PS.PUNCH_SESSION_NR = :punchSessionNr " +
//          "AND RC.CLIENT_NR = :sessionClientNr " +
//          "AND C.TYPE_UID = :controlTypeUid " +
//          "ORDER BY RC.SORTCODE ASC "
//          , new NVPair("controlTypeUid", ControlTypeCodeType.ControlCode.ID)
//          , new NVPair("punchSessionNr", punchSessionNr)
//          , new NVPair("factor", factor)
//          );
    }

    RtPunchSession session = JPA.find(RtPunchSession.class, RtPunchSessionKey.create(punchSessionNr));
    session.setFinish(5000 + NumberUtility.toLong(maxTime + Math.floor(Math.random() * 10000)));
    session.setStart(start);

//    SQL.update("UPDATE RT_PUNCH_SESSION PS " +
//        "SET FINISH = (SELECT MAX(TIME) + FLOOR(RANDOM()*100000) FROM RT_PUNCH P WHERE P.PUNCH_SESSION_NR = PS.PUNCH_SESSION_NR), " +
//        "START = " + start + " " +
//        "WHERE PS.PUNCH_SESSION_NR = :punchSessionNr "
//        , new NVPair("punchSessionNr", punchSessionNr)
//        );

  }
}
