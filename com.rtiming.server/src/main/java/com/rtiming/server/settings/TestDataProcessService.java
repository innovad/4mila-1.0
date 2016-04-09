package com.rtiming.server.settings;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.holders.ITableHolder;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.common.security.permission.CreateTestDataPermission;
import com.rtiming.shared.common.security.permission.ReadTestDataPermission;
import com.rtiming.shared.common.security.permission.UpdateTestDataPermission;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.ecard.download.DownloadedECardFormData;
import com.rtiming.shared.ecard.download.IDownloadedECardProcessService;
import com.rtiming.shared.ecard.download.IPunchProcessService;
import com.rtiming.shared.entry.EntryFormData;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.entry.IEntryProcessService;
import com.rtiming.shared.entry.IEntryService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.ISettingsOutlineService;
import com.rtiming.shared.settings.ITestDataProcessService;
import com.rtiming.shared.settings.TestDataFormData;

public class TestDataProcessService  implements ITestDataProcessService {

  @Override
  public TestDataFormData prepareCreate(TestDataFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateTestDataPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getEvent().setValue(BEANS.get(IDefaultProcessService.class).getDefaultEventNr());

    return formData;
  }

  @Override
  public TestDataFormData create(TestDataFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateTestDataPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    EventConfiguration configuration = BEANS.get(IEntryService.class).loadEventConfiguration();

    for (int i = 0; i < formData.getCount().getValue(); i++) {
      createTestData(formData.getEvent().getValue(), formData.getClazz().getValue(), configuration);
    }

    return formData;
  }

  @Override
  public TestDataFormData load(TestDataFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadTestDataPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    return formData;
  }

  @Override
  public TestDataFormData store(TestDataFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateTestDataPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    return formData;
  }

  protected void createTestData(long eventNr, long classUid, EventConfiguration configuration) throws ProcessingException {
    // select required sex and age
    Long sexUid = null;
    Long yearFrom = null;
    Long yearTo = null;
    RtEvent event = JPA.find(RtEvent.class, RtEventKey.create(eventNr));
    ISettingsOutlineService svc = BEANS.get(ISettingsOutlineService.class);
    List<RtClassAge> classAgeList = svc.getAgeTableData(classUid);
    if (classAgeList != null && classAgeList.size() > 0) {
      GregorianCalendar greg = new GregorianCalendar();
      greg.setTime(event.getEvtZero());
      Long eventYear = (long) greg.get(Calendar.YEAR);
      RtClassAge age = classAgeList.get(0);
      sexUid = age.getSexUid();
      if (age.getAgeTo() != null) {
        yearFrom = eventYear - age.getAgeTo();
      }
      if (age.getAgeFrom() != null) {
        yearTo = eventYear - age.getAgeFrom();
      }
    }
    sexUid = NumberUtility.nvl(sexUid, SexCodeType.ManCode.ID);
    yearFrom = NumberUtility.nvl(yearFrom, Long.MIN_VALUE);
    yearTo = NumberUtility.nvl(yearTo, Long.MAX_VALUE);

    // select random runner
    String queryString = "SELECT MAX(id.runnerNr) " +
        "FROM RtRunner R " +
        "WHERE NOT EXISTS (SELECT 1 FROM RtRace RA WHERE RA.runnerNr = R.id.runnerNr AND RA.eventNr = :eventNr) " +
        "AND firstName IS NOT NULL " +
        "AND clubNr IS NOT NULL " +
        "AND eCardNr IS NOT NULL " +
        "AND year >= :yearFrom " +
        "AND year <= :yearTo " +
        "AND sexUid = :sexUid " +
        "AND R.id.clientNr = :sessionClientNr ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("eventNr", eventNr);
    query.setParameter("sexUid", sexUid);
    query.setParameter("yearFrom", yearFrom);
    query.setParameter("yearTo", yearTo);
    Long runnerNr = query.getSingleResult();

    if (runnerNr == null) {
      throw new VetoException("Could not create test data, no matching runners found.");
    }

    // select ecard station
    queryString = "SELECT MAX(id.stationNr) FROM RtEcardStation WHERE id.clientNr = :clientNr";
    query = JPA.createQuery(queryString, Long.class);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    Long stationNr = query.getSingleResult();

    if (stationNr == null) {
      throw new VetoException("At least one registered download station must exist. Please connect a download station.");
    }

    // load runner
    RunnerBean runner = new RunnerBean();
    runner.setRunnerNr(runnerNr);
    runner = BEANS.get(IRunnerProcessService.class).load(runner);

    // create entry
    EntryFormData entry = new EntryFormData();

    entry = BeanUtility.entryBean2formData(BEANS.get(IEntryProcessService.class).prepareCreate(BeanUtility.entryFormData2bean(entry, configuration)), configuration);

    int newRowId = entry.getRaces().addRow();
    entry.getRaces().setRowState(newRowId, ITableHolder.STATUS_INSERTED);
    entry.getRaces().setRunnerNr(newRowId, runner.getRunnerNr());
    RaceBean raceBean = new RaceBean();
    raceBean.setRunner(runner);
    raceBean.setAddress(runner.getAddress().copy());
    raceBean.setNationUid(runner.getNationUid());
    raceBean.setClubNr(runner.getClubNr());
    entry.getRaces().setRaceBean(newRowId, raceBean);
    entry.getRaces().getRaceBean(newRowId).setRunner(runner);
    entry.getRaces().setRaceEvent(newRowId, eventNr);
    entry.getRaces().setECard(newRowId, runner.getECardNr());
    entry.getRaces().setLastName(newRowId, runner.getLastName());
    entry.getRaces().setFirstName(newRowId, runner.getFirstName());
    entry.getRaces().setLeg(newRowId, classUid);
    entry.getRaces().setClubNr(newRowId, runner.getClubNr());
    entry.getRaces().setNation(newRowId, runner.getNationUid());

    int newEventRowId = 0; // create in prepareCreate
    entry.getRaces().setRowState(newEventRowId, ITableHolder.STATUS_INSERTED);
    entry.getEvents().setEventNr(newEventRowId, eventNr);
    entry.getEvents().setEventClass(newEventRowId, classUid);

    entry = BeanUtility.entryBean2formData(BEANS.get(IEntryProcessService.class).create(BeanUtility.entryFormData2bean(entry, configuration)), configuration);

    // create punch session
    DownloadedECardFormData punchSession = new DownloadedECardFormData();
    Long raceNr = entry.getRaces().getRaceNr(newRowId);
    punchSession.getRace().setValue(raceNr);
    punchSession.getECard().setValue(entry.getRaces().getECard(newRowId));
    punchSession.getEvent().setValue(eventNr);
    punchSession.getEvtDownload().setValue(new Date());
    punchSession.getECardStation().setValue(stationNr);
    punchSession = BEANS.get(IDownloadedECardProcessService.class).create(punchSession);

    // create punch test data
    BEANS.get(IPunchProcessService.class).createTestData(punchSession.getPunchSessionNr(), raceNr, eventNr, classUid);

    // validate race
    BEANS.get(IRaceService.class).validateAndPersistRace(punchSession.getRace().getValue());
  }
}
