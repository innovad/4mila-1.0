package com.rtiming.server.race;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.BooleanUtility;
import org.eclipse.scout.commons.TypeCastUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateRaceControlPermission;
import com.rtiming.shared.common.security.permission.ReadRaceControlPermission;
import com.rtiming.shared.common.security.permission.UpdateRaceControlPermission;
import com.rtiming.shared.dao.RtCourseControl;
import com.rtiming.shared.dao.RtCourseControl_;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEvent_;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceControl;
import com.rtiming.shared.dao.RtRaceControlKey;
import com.rtiming.shared.dao.RtRaceControlKey_;
import com.rtiming.shared.dao.RtRaceControl_;
import com.rtiming.shared.dao.RtRaceKey;
import com.rtiming.shared.dao.RtRace_;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.race.IRaceControlProcessService;
import com.rtiming.shared.race.RaceControlFormData;

public class RaceControlProcessService  implements IRaceControlProcessService {

  @Override
  public RaceControlFormData prepareCreate(RaceControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateRaceControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    RtRace race = JPA.find(RtRace.class, RtRaceKey.create(formData.getRace().getValue()));
    if (race != null) {
      formData.getZeroTime().setValue(BEANS.get(IEventProcessService.class).getZeroTime(race.getEventNr()));
      formData.setLegStartTime(race.getLegStartTime());
    }

    return formData;
  }

  @Override
  public RaceControlFormData create(RaceControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateRaceControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtRaceControlKey key = RtRaceControlKey.create((Long) null);
    RtRaceControl raceControl = new RtRaceControl();
    raceControl.setId(key);
    raceControl.setManualStatus(formData.getManualStatus().getValue());
    JPA.persist(raceControl);

    formData.setRaceControlNr(raceControl.getId().getId());
    formData = store(formData);

    return formData;
  }

  @Override
  public RaceControlFormData load(RaceControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadRaceControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtRaceControl> raceControl = selectQuery.from(RtRaceControl.class);
    Join<RtRaceControl, RtRace> joinRace = raceControl.join(RtRaceControl_.rtRace, JoinType.LEFT);
    Join<RtRace, RtEvent> joinEvent = joinRace.join(RtRace_.rtEvent, JoinType.LEFT);
    Join<RtRaceControl, RtCourseControl> joinCourseControl = raceControl.join(RtRaceControl_.rtCourseControl, JoinType.LEFT);

    selectQuery.select(b.array(
        raceControl.get(RtRaceControl_.raceNr),
        joinCourseControl.get(RtCourseControl_.controlNr),
        raceControl.get(RtRaceControl_.courseControlNr),
        raceControl.get(RtRaceControl_.sortcode),
        raceControl.get(RtRaceControl_.statusUid),
        raceControl.get(RtRaceControl_.manualStatus),
        joinEvent.get(RtEvent_.evtZero),
        joinRace.get(RtRace_.legStartTime),
        raceControl.get(RtRaceControl_.overallTime),
        raceControl.get(RtRaceControl_.shiftTime)
        ))
        .where(
            b.and(
                b.equal(raceControl.get(RtRaceControl_.id).get(RtRaceControlKey_.raceControlNr), formData.getRaceControlNr()),
                b.equal(raceControl.get(RtRaceControl_.id).get(RtRaceControlKey_.clientNr), ServerSession.get().getSessionClientNr()))
        );
    List<Object[]> result = JPA.createQuery(selectQuery).getResultList();

    if (result != null && result.size() >= 0) {
      Object[] row = result.get(0);
      formData.getRace().setValue(TypeCastUtility.castValue(row[0], Long.class));
      formData.getControl().setValue(TypeCastUtility.castValue(row[1], Long.class));
      formData.setCourseControlNr(TypeCastUtility.castValue(row[2], Long.class));
      formData.getSortCode().setValue(TypeCastUtility.castValue(row[3], Long.class));
      formData.getControlStatus().setValue(TypeCastUtility.castValue(row[4], Long.class));
      formData.getManualStatus().setValue(TypeCastUtility.castValue(row[5], Boolean.class));
      formData.getZeroTime().setValue(TypeCastUtility.castValue(row[6], Date.class));
      Long legStartTime = TypeCastUtility.castValue(row[7], Long.class);
      Long overallTime = TypeCastUtility.castValue(row[8], Long.class);
      if (formData.getZeroTime().getValue() != null && legStartTime != null && overallTime != null) {
        Date time = FMilaUtility.addMilliSeconds(formData.getZeroTime().getValue(), legStartTime + overallTime);
        formData.getTime().setValue(time);
      }
      formData.getShiftTime().setValue(TypeCastUtility.castValue(row[9], Long.class));
      formData.setLegStartTime(legStartTime);
    }

    return formData;
  }

  @Override
  public RaceControlFormData store(RaceControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateRaceControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    Long timeDiff = null;
    if (formData.getZeroTime().getValue() != null && formData.getTime().getValue() != null && formData.getLegStartTime() != null) {
      timeDiff = FMilaUtility.getDateDifferenceInMilliSeconds(formData.getZeroTime().getValue(), formData.getTime().getValue());
      timeDiff = timeDiff - formData.getLegStartTime();
    }

    String queryString = "UPDATE RtRaceControl SET raceNr = :race, " +
        "courseControlNr = :courseControlNr, " +
        "controlNr = :control, " +
        "sortcode = :sortCode, " +
        "statusUid = :controlStatus, " +
        "manualStatus = :manualStatus, " +
        "overallTime = :timeDiff, " +
        "shiftTime = :shiftTime " +
        "WHERE id.raceControlNr = :raceControlNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.setParameter("timeDiff", timeDiff);
    query.setParameter("manualStatus", BooleanUtility.nvl(formData.getManualStatus().getValue()));
    query.executeUpdate();

    return formData;
  }

  @Override
  public void delete(Long... raceControlNrs) throws ProcessingException {
    if (raceControlNrs != null && raceControlNrs.length > 0) {
      String queryString = "DELETE FROM RtRaceControl " +
          "WHERE id.raceControlNr IN :raceControlNrs " +
          "AND id.clientNr = :sessionClientNr ";

      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("raceControlNrs", Arrays.asList(raceControlNrs));
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();
    }
  }
}
