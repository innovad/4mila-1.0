package com.rtiming.server.race;

import java.util.ArrayList;
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
import org.eclipse.scout.rt.platform.holders.LongHolder;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPACriteriaUtility;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.common.security.permission.CreateRacePermission;
import com.rtiming.shared.common.security.permission.ReadRacePermission;
import com.rtiming.shared.common.security.permission.UpdateRacePermission;
import com.rtiming.shared.dao.RtAddress;
import com.rtiming.shared.dao.RtAddressKey;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceKey;
import com.rtiming.shared.dao.RtRace_;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunner_;
import com.rtiming.shared.event.EventRowData;
import com.rtiming.shared.event.EventsSearchFormData;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.runner.IAddressProcessService;

public class RaceProcessService implements IRaceProcessService {

  @Override
  public RaceBean prepareCreate(RaceBean formData) throws ProcessingException {
    if (!ACCESS.check(new CreateRacePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    return formData;
  }

  @Override
  public RaceBean create(RaceBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreateRacePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtRaceKey key = RtRaceKey.create((Long) null);
    RtRace race = new RtRace();
    race.setId(key);
    race.setManualStatus(bean.isManualStatus());
    JPA.persist(race);
    bean.setRaceNr(race.getId().getId());

    RtAddressKey key2 = RtAddressKey.create((Long) null);
    RtAddress address = new RtAddress();
    address.setId(key2);
    JPA.persist(address);
    bean.getAddress().setAddressNr(address.getId().getId());

    bean = store(bean);

    return bean;
  }

  @Override
  public RaceBean load(RaceBean bean) throws ProcessingException {
    if (!ACCESS.check(new ReadRacePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    LongHolder addressNr = new LongHolder();
    JPAUtility.select("SELECT " + "addressNr, " + "id.clientNr, " + "entryNr, " + "legTime, " + "legStartTime, " + "eventNr, " + "runnerNr, " + "eCardNr, " + "legClassUid, " + "statusUid, " + "COALESCE(manualStatus,FALSE), " + "bibNo, " + "nationUid," + "clubNr " + "FROM RtRace R " + "WHERE R.id.raceNr = :raceNr " + "AND R.id.clientNr = COALESCE(:clientNr, :sessionClientNr) " + "INTO " + ":addressNr, " + ":clientNr, " + ":entryNr, " + ":legTime, " + ":legStartTime," + ":eventNr, " + ":runnerNr, " + ":eCardNr, " + ":legClassUid,  " + ":statusUid, " + ":manualStatus, " + ":bibNo, " + ":nationUid, " + ":clubNr ", bean, new NVPair("addressNr", addressNr));

    bean.getAddress().setAddressNr(addressNr.getValue());
    BEANS.get(IAddressProcessService.class).load(bean.getAddress());

    return bean;
  }

  @Override
  public RaceBean store(RaceBean bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateRacePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    String queryString = "UPDATE RtRace SET " + "eventNr = :eventNr, " + "entryNr = :entryNr, " + "runnerNr = :runnerNr," + "eCardNr = :ECardNr," + "legStartTime = :start," + "legTime = :time," + "legClassUid = :legClassUid," + "bibNo = :bibNo, " + "nationUid = :nationUid, " + "clubNr = :clubNr, " + "addressNr = :addressNr, " + (bean.getStatusUid() != null ? "statusUid = :statusUid, " : "") + "manualStatus = :manualStatus " + "WHERE id.raceNr = :raceNr " + "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("start", bean.getLegStartTime());
    query.setParameter("time", bean.getLegTime());
    query.setParameter("addressNr", bean.getAddress().getAddressNr());
    JPAUtility.setAutoParameters(query, queryString, bean);
    query.executeUpdate();

    BEANS.get(IAddressProcessService.class).store(bean.getAddress());

    if (bean.getStatusUid() != null) {
      BEANS.get(IRaceService.class).validateAndPersistRace(bean.getRaceNr());
    }

    return bean;
  }

  @Override
  public Long findRaceNr(long eventNr, String eCardNo) throws ProcessingException {
    if (eCardNo == null) {
      return null;
    }
    eCardNo = StringUtility.uppercase(eCardNo).trim();

    String queryString = "SELECT MAX(RA.id.raceNr) FROM RtRace RA " + "INNER JOIN RA.rtEcard E " + "LEFT JOIN RA.rtPunchSessions PS " + "WHERE UPPER(E.ecardNo) = :eCardNo " + "AND PS.id.punchSessionNr IS NULL " + // only races without punch session
        "AND RA.eventNr = :eventNr " + "AND RA.id.clientNr = :clientNr";

    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("eventNr", eventNr);
    query.setParameter("eCardNo", eCardNo);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    Long result = query.getSingleResult();

    return result;
  }

  @Override
  public List<RaceBean> findByAccountNr(Long accountNr) throws ProcessingException {
    List<EventRowData> events = BEANS.get(IEventsOutlineService.class).getEventTableData(null, new EventsSearchFormData());
    List<EventRowData> currentEvents = new ArrayList<>();
    Date now = new Date();
    for (EventRowData event : events) {
      Integer timezoneOffset = event.getTimeZone();
      Date evtZero = FMilaUtility.addMilliSeconds(event.getEvtZero(), NumberUtility.toLong(timezoneOffset));
      Date evtFinish = FMilaUtility.addMilliSeconds(event.getEvtFinish(), NumberUtility.toLong(timezoneOffset));
      if (DateUtility.isInDateRange(evtZero, now, evtFinish)) {
        currentEvents.add(event);
      }
    }

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtRace> selectQuery = b.createQuery(RtRace.class);
    Root<RtRace> race = selectQuery.from(RtRace.class);
    Join<RtRace, RtRunner> joinRunner = race.join(RtRace_.rtRunner, JoinType.INNER);
    selectQuery.where(b.and(accountNr == null ? b.conjunction() : b.equal(joinRunner.get(RtRunner_.accountNr), accountNr))).orderBy(b.asc(JPACriteriaUtility.runnerNameJPA(joinRunner)));

    List<RtRace> raceList = JPA.createQuery(selectQuery).getResultList();
    List<RtRace> filteredRaceList = new ArrayList<>();
    for (RtRace raceBean : raceList) {
      for (EventRowData event : events) {
        if (CompareUtility.equals(event.getEventNr(), raceBean.getEventNr()) && CompareUtility.equals(event.getClientNr(), raceBean.getId().getClientNr())) {
          filteredRaceList.add(raceBean);
        }
      }
    }

    ArrayList<RaceBean> result = new ArrayList<RaceBean>();
    for (RtRace raceBean : filteredRaceList) {
      RaceBean resultBean = new RaceBean();
      resultBean.setRaceNr(raceBean.getId().getId());
      resultBean.setClientNr(raceBean.getId().getClientNr());
      resultBean = load(resultBean);
      result.add(resultBean);
    }

    return result;
  }

  @Override
  public void delete(Long... raceNrs) throws ProcessingException {
    if (raceNrs != null && raceNrs.length > 0) {
      String queryString = "UPDATE RtPunchSession PS " + "SET PS.raceNr = NULL " + "WHERE PS.raceNr IN :raceNrs " + "AND PS.id.clientNr = :sessionClientNr";

      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("raceNrs", Arrays.asList(raceNrs));
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();

      queryString = "DELETE FROM RtRaceControl RC " + "WHERE RC.raceNr = :raceNrs " + "AND RC.id.clientNr = :sessionClientNr ";

      query = JPA.createQuery(queryString);
      query.setParameter("raceNrs", Arrays.asList(raceNrs));
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();

      queryString = "DELETE FROM RtRace R " + "WHERE R.id.raceNr = :raceNrs " + "AND R.id.clientNr = :sessionClientNr ";

      query = JPA.createQuery(queryString);
      query.setParameter("raceNrs", Arrays.asList(raceNrs));
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();
    }
  }

}
