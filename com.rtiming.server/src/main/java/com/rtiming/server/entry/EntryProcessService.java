package com.rtiming.server.entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.EntryBean;
import com.rtiming.shared.common.database.sql.FeeBean;
import com.rtiming.shared.common.database.sql.ParticipationBean;
import com.rtiming.shared.common.database.sql.PaymentBean;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.common.security.permission.CreateEntryPermission;
import com.rtiming.shared.common.security.permission.DeleteEntryPermission;
import com.rtiming.shared.common.security.permission.ReadEntryPermission;
import com.rtiming.shared.common.security.permission.UpdateEntryPermission;
import com.rtiming.shared.dao.RtAddress;
import com.rtiming.shared.dao.RtAddressKey;
import com.rtiming.shared.dao.RtEntry;
import com.rtiming.shared.dao.RtEntryKey;
import com.rtiming.shared.dao.RtParticipation;
import com.rtiming.shared.dao.RtParticipationKey;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceKey;
import com.rtiming.shared.entry.IEntryProcessService;
import com.rtiming.shared.entry.IRegistrationProcessService;
import com.rtiming.shared.entry.RegistrationFormData;
import com.rtiming.shared.entry.payment.IPaymentProcessService;
import com.rtiming.shared.entry.payment.PaymentTypeCodeType;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationProcessService;

public class EntryProcessService  implements IEntryProcessService {

  @Override
  public EntryBean prepareCreate(EntryBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreateEntryPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // Default Event
    ParticipationBean participation = new ParticipationBean();
    participation.setEventNr(BEANS.get(IDefaultProcessService.class).getDefaultEventNr());
    bean.addParticipation(participation);

    bean.setEvtEntry(new Date());
    bean.setCurrencyUid(BEANS.get(IDefaultProcessService.class).getDefaultCurrencyUid());

    BEANS.get(IAdditionalInformationProcessService.class).prepareCreate(bean.getAddInfo());

    return bean;
  }

  @Override
  public EntryBean create(EntryBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreateEntryPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // RT_ENTRY
    RtEntryKey key = RtEntryKey.create((Long) null);
    RtEntry entry = new RtEntry();
    entry.setId(key);
    JPA.persist(entry);

    bean.setEntryNr(entry.getId().getId());
    bean = store(bean);

    return bean;
  }

  @Override
  public EntryBean load(EntryBean bean) throws ProcessingException {
    if (!ACCESS.check(new ReadEntryPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // RT_ENTRY
    JPAUtility.select("SELECT registrationNr, " +
        "evtEntry, " +
        "currencyUid " +
        "FROM RtEntry E " +
        "WHERE E.id.entryNr = :entryNr " +
        "AND E.id.clientNr = :sessionClientNr " +
        "INTO :registrationNr, :evtEntry, :currencyUid ",
        bean
        );

    // RT_PARTICIPATION
    String queryString = "SELECT P.id.eventNr, " +
        "P.classUid, " +
        "P.startTime, " +
        "P.startblockUid " +
        "FROM RtParticipation P " +
        "INNER JOIN P.rtEvent " +
        "WHERE P.id.entryNr = :entryNr " +
        "AND P.id.clientNr = :clientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("entryNr", bean.getEntryNr());
    query.setParameter("clientNr", NumberUtility.nvl(bean.getClientNr(), ServerSession.get().getSessionClientNr()));
    List result = query.getResultList();
    bean.getParticipations().clear();
    for (Object rowObject : result) {
      Object[] row = (Object[]) rowObject;
      ParticipationBean participation = new ParticipationBean();
      participation.setEventNr(TypeCastUtility.castValue(row[0], Long.class));
      participation.setClassUid(TypeCastUtility.castValue(row[1], Long.class));
      participation.setStartTime(TypeCastUtility.castValue(row[2], Long.class));
      participation.setStartblockUid(TypeCastUtility.castValue(row[3], Long.class));
      bean.addParticipation(participation);
    }

    // RT_RACE
    queryString = "SELECT " +
        "RA.id.raceNr " +
        "FROM RtRace RA " +
        "WHERE entryNr = :entryNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaTypedQuery<Long> typedQuery = JPA.createQuery(queryString, Long.class);
    typedQuery.setParameter("entryNr", bean.getEntryNr());
    typedQuery.setParameter("sessionClientNr", NumberUtility.nvl(bean.getClientNr(), ServerSession.get().getSessionClientNr()));
    List<Long> raceNrs = typedQuery.getResultList();
    for (Long raceNr : raceNrs) {
      RaceBean race = new RaceBean();
      race.setRaceNr(raceNr);
      race = BEANS.get(IRaceProcessService.class).load(race);
      bean.addRace(race);
    }

    // RT_RUNNER
    for (RaceBean race : bean.getRaces()) {
      RunnerBean runner = new RunnerBean();
      runner.setRunnerNr(race.getRunnerNr());
      runner = BEANS.get(IRunnerProcessService.class).load(runner);
      // patch nation and club since we need the race nation and club
      runner.setClubNr(race.getClubNr());
      runner.setNationUid(race.getNationUid());
      race.setRunner(runner);
    }

    // RT_ADDITIONAL_INFORMATION (Entry)
    bean.getAddInfo().setJoinNr(bean.getEntryNr());
    bean.getAddInfo().setClientNr(bean.getClientNr());
    BEANS.get(IAdditionalInformationProcessService.class).load(bean.getAddInfo());

    return bean;
  }

  @Override
  public EntryBean store(EntryBean bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateEntryPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // RT_REGISTRATION (create registration on new entry)
    if (NumberUtility.nvl(bean.getRegistrationNr(), 0L) == 0L) {
      createRegistration(bean);
    }

    // RT_ENTRY
    storeEntry(bean);

    // RT_RUNNER
    storeRunners(bean);

    // RT_PARTICIPATION (Events)
    ArrayList<Long> eventNrs = new ArrayList<Long>();
    Long entryClassUid = validateEvents(bean, eventNrs);
    bean = storeParticipation(bean, eventNrs);

    // RT_RACE (Team, Races)
    validateRaces(bean, entryClassUid);
    List<Long> raceNrs = storeRaces(bean);

    // RT_ADDITIONAL_INFORMATION (Entry)
    bean.getAddInfo().setJoinNr(bean.getEntryNr());
    bean.getAddInfo().setClientNr(bean.getClientNr());
    BEANS.get(IAdditionalInformationProcessService.class).store(bean.getAddInfo());

    // RT_RACE_CONTROL (inflate)
    BEANS.get(IRaceService.class).inflateRaceControls(CollectionUtility.toArray(raceNrs, Long.class));
    return bean;
  }

  protected void storeEntry(EntryBean bean) throws ProcessingException {
    if (bean == null) {
      return;
    }
    String queryString = "UPDATE RtEntry " +
        "SET statusUid = 0, " +
        "registrationNr = :registrationNr, " +
        "evtEntry = :evtEntry, " +
        "currencyUid = :currencyUid " +
        "WHERE id.entryNr = :entryNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, bean);
    query.executeUpdate();
  }

  protected EntryBean storeParticipation(EntryBean bean, List<Long> eventNrs) throws ProcessingException {
    if (bean == null) {
      return null;
    }
    if (eventNrs != null && eventNrs.size() > 0) {
      String queryString = "DELETE FROM RtParticipation WHERE id.entryNr = :entryNr " +
          "AND id.eventNr NOT IN :eventNrs " +
          "AND id.clientNr = :sessionClientNr";
      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("eventNrs", eventNrs);
      query.setParameter("entryNr", bean.getEntryNr());
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();
    }

    for (ParticipationBean participation : bean.getParticipations()) {
      RtParticipationKey key = new RtParticipationKey();
      key.setClientNr(ServerSession.get().getSessionClientNr());
      key.setEntryNr(bean.getEntryNr());
      key.setEventNr(participation.getEventNr());
      RtParticipation p = new RtParticipation();
      p.setId(key);
      if (JPA.find(RtParticipation.class, key) == null) {
        JPA.merge(p);
      }

      String queryString = "UPDATE RtParticipation SET classUid = :classUid," +
          "startTime = :startTime, " +
          "startblockUid = :startblockUid " +
          "WHERE id.eventNr = :eventNr " +
          "AND id.entryNr = :entryNr " +
          "AND id.clientNr = :sessionClientNr";
      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("classUid", participation.getClassUid());
      query.setParameter("startTime", participation.getStartTime());
      query.setParameter("startblockUid", participation.getStartblockUid());
      query.setParameter("eventNr", participation.getEventNr());
      query.setParameter("entryNr", bean.getEntryNr());
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();
    }

    return bean;
  }

  protected List<Long> storeRaces(EntryBean bean) throws ProcessingException {
    List<Long> raceNrs = new ArrayList<Long>();

    for (RaceBean race : bean.getRaces()) {
      race.setEntryNr(bean.getEntryNr());
      if (race.getRaceNr() == null) {
        RtRaceKey key = RtRaceKey.create((Long) null);
        RtRace raceBean = new RtRace();
        raceBean.setId(key);
        raceBean.setManualStatus(false);
        JPA.persist(raceBean);
        race.setRaceNr(raceBean.getId().getId());
      }
      if (race.getAddress().getAddressNr() == null) {
        RtAddressKey key = RtAddressKey.create((Long) null);
        RtAddress address = new RtAddress();
        address.setId(key);
        JPA.persist(address);
        race.getAddress().setAddressNr(address.getId().getId());
      }
      race = BEANS.get(IRaceProcessService.class).store(race);

      // save raceNr
      raceNrs.add(race.getRaceNr());
    }

    // delete
    String queryString = "SELECT id.raceNr FROM RtRace R " +
        "WHERE R.entryNr = :entryNr " +
        "AND id.clientNr = :sessionClientNr " +
        "AND R.id.raceNr NOT IN :raceNrs ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("entryNr", bean.getEntryNr());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("raceNrs", raceNrs);
    List<Long> raceNrsToBeDeleted = query.getResultList();
    for (Long raceNr : raceNrsToBeDeleted) {
      BEANS.get(IRaceProcessService.class).delete(raceNr);
    }

    return raceNrs;
  }

  private void validateRaces(EntryBean bean, Long entryClassUid) throws ProcessingException {
    for (RaceBean race : bean.getRaces()) {

      // validate class
      // all races must have the same class except in case of relay

      // load cached class settings
      EventClassFormData mainClass = findEventClassSettings(bean, race.getEventNr(), entryClassUid);

      // check class
      if (race.getLegClassUid() == null) {
        throw new VetoException(Texts.get("EntryClassOnRaceRequiredMessage"));
      }
      if (race.getEventNr() == null) {
        throw new VetoException(TEXTS.get("EntryEventOnRaceRequiredMessage"));
      }
      // check leg class
      if (CompareUtility.equals(ClassTypeCodeType.RelayCode.ID, mainClass.getType().getValue())) {
        // relay
        EventClassFormData legClass = new EventClassFormData();
        legClass.getEvent().setValue(race.getEventNr());
        legClass.getClazz().setValue(race.getLegClassUid());
        legClass = BEANS.get(IEventClassProcessService.class).load(legClass);
        if (CompareUtility.notEquals(legClass.getParent().getValue(), entryClassUid)) {
          throw new VetoException(TEXTS.get("RelayEntryClassValidationMessage",
              FMilaUtility.getCodeText(ClassCodeType.class, entryClassUid),
              FMilaUtility.getCodeText(ClassCodeType.class, legClass.getParent().getValue())));
        }
      }
      else {
        // non-relay
        if (CompareUtility.notEquals(race.getLegClassUid(), entryClassUid)) {
          throw new VetoException(TEXTS.get("IndividualEntryClassValidationMessage",
              FMilaUtility.getCodeText(ClassCodeType.class, entryClassUid),
              FMilaUtility.getCodeText(ClassCodeType.class, race.getLegClassUid())));
        }
      }

      // check last name
      if (StringUtility.isNullOrEmpty(race.getRunner().getLastName())) {
        throw new VetoException(Texts.get("RunnerLastNameRequiredMessage"));
      }
    }
  }

  private EventClassFormData findEventClassSettings(EntryBean entry, Long eventNr, Long classUid) throws ProcessingException {
    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(eventNr);
    eventClass.getClazz().setValue(classUid);
    eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);
    if (eventClass.getType().getValue() == null) {
      throw new ProcessingException("No event class information found.");
    }
    return eventClass;
  }

  private Long validateEvents(EntryBean bean, ArrayList<Long> eventNrs) throws VetoException {
    Long entryClassUid = null;
    for (ParticipationBean participation : bean.getParticipations()) {
      // validate events
      // class must exist, it should not be a leg class
      if (participation.getClassUid() == null) {
        throw new VetoException(Texts.get("EntryClassOnParticipationRequiredMessage"));
      }
      if (entryClassUid == null) {
        entryClassUid = participation.getClassUid();
      }
      // in an entry, the class uid must be the same for all events (multi day)
      if (CompareUtility.notEquals(entryClassUid, participation.getClassUid())) {
        throw new VetoException(Texts.get("EntryClassSameClassForMultiDayResults"));
      }
      if (participation.getEventNr() == null) {
        throw new VetoException(TEXTS.get("EntryEventOnEventRequiredMessage"));
      }

      eventNrs.add(participation.getEventNr());
    }
    if (eventNrs.size() == 0) {
      throw new VetoException(TEXTS.get("EntryEventRequiredMessage"));
    }
    return entryClassUid;
  }

  private void storeRunners(EntryBean entryBean) throws ProcessingException {
    for (RaceBean race : entryBean.getRaces()) {
      RunnerBean runnerBean = race.getRunner();
      IRunnerProcessService runnerService = BEANS.get(IRunnerProcessService.class);
      if (runnerBean.getRunnerNr() == null) {
        runnerBean = runnerService.create(runnerBean);
      }
      else {
        runnerBean = runnerService.store(runnerBean);
      }
      race.setRunnerNr(runnerBean.getRunnerNr());
    }
  }

  private void createRegistration(EntryBean bean) throws ProcessingException {
    // Registration
    RegistrationFormData registration = new RegistrationFormData();
    registration = BEANS.get(IRegistrationProcessService.class).create(registration);
    bean.setRegistrationNr(registration.getRegistrationNr());

    // Cash Payment on Registration
    for (FeeBean fee : bean.getFees()) {
      int k = 0;
      if (BooleanUtility.nvl(fee.isCashPaymentOnRegistration())) {
        PaymentBean cash = new PaymentBean();
        cash.setAmount(fee.getAmount());
        cash.setCurrencyUid(fee.getCurrencyUid());
        cash.setTypeUid(PaymentTypeCodeType.CashPaymentCode.ID);
        cash.setPaymentNo(registration.getRegistrationNo().getValue() + "-" + k);
        cash.setRegistrationNr(registration.getRegistrationNr());
        cash.setEvtPayment(bean.getEvtEntry());
        BEANS.get(IPaymentProcessService.class).create(cash);
        k++;
      }
    }
  }

  @Override
  public void delete(EntryBean bean) throws ProcessingException {
    if (!ACCESS.check(new DeleteEntryPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (bean == null || bean.getEntryNr() == null) {
      return;
    }

    // RT_RACE
    for (RaceBean race : bean.getRaces()) {
      String queryString = "DELETE FROM RtPunch " +
          "WHERE id.punchSessionNr IN (SELECT P.id.punchSessionNr " +
          "                           FROM RtPunchSession P " +
          "                           WHERE P.raceNr = :raceNr " +
          "                           AND P.id.clientNr = :sessionClientNr) " +
          "AND id.clientNr = :sessionClientNr";
      executeDeleteQuery(queryString, race.getRaceNr());

      queryString = "DELETE FROM RtPunchSession " +
          "WHERE raceNr = :raceNr " +
          "AND id.clientNr = :sessionClientNr";
      executeDeleteQuery(queryString, race.getRaceNr());

      queryString = "DELETE FROM RtRaceControl " +
          "WHERE raceNr = :raceNr " +
          "AND id.clientNr = :sessionClientNr";
      executeDeleteQuery(queryString, race.getRaceNr());

      queryString = "DELETE FROM RtRace " +
          "WHERE id.raceNr = :raceNr " +
          "AND id.clientNr = :sessionClientNr";
      executeDeleteQuery(queryString, race.getRaceNr());

      queryString = "DELETE FROM RtAddress " +
          "WHERE id.addressNr = :addressNr " +
          "AND id.clientNr = :sessionClientNr";
      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("addressNr", race.getAddress().getAddressNr());
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();
    }

    // RT_PARTICIPATION
    for (ParticipationBean participation : bean.getParticipations()) {
      String queryString = "DELETE FROM RtParticipation " +
          "WHERE id.eventNr = :eventNr " +
          "AND id.entryNr = :entryNr " +
          "AND id.clientNr = :sessionClientNr ";
      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("eventNr", participation.getEventNr());
      query.setParameter("entryNr", bean.getEntryNr());
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();
    }

    // RT_ENTRY
    String queryString = "DELETE FROM RtEntry " +
        "WHERE id.entryNr = :entryNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("entryNr", bean.getEntryNr());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();

  }

  private void executeDeleteQuery(String queryString, Long raceNr) throws ProcessingException {
    FMilaQuery query = JPA.createQuery(queryString);
    if (raceNr != null) {
      query.setParameter("raceNr", raceNr);
    }
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();
  }

}
