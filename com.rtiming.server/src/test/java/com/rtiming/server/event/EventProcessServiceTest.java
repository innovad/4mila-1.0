package com.rtiming.server.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Date;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.dao.RtAdditionalInformationDef;
import com.rtiming.shared.dao.RtAdditionalInformationDefKey;
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControlKey;
import com.rtiming.shared.dao.RtControlReplacement;
import com.rtiming.shared.dao.RtControlReplacementKey;
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourseControl;
import com.rtiming.shared.dao.RtCourseControlKey;
import com.rtiming.shared.dao.RtCourseKey;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.dao.RtEntry;
import com.rtiming.shared.dao.RtEntryKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventAdditionalInformation;
import com.rtiming.shared.dao.RtEventAdditionalInformationKey;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtFee;
import com.rtiming.shared.dao.RtFeeGroup;
import com.rtiming.shared.dao.RtFeeGroupKey;
import com.rtiming.shared.dao.RtFeeKey;
import com.rtiming.shared.dao.RtParticipation;
import com.rtiming.shared.dao.RtParticipationKey;
import com.rtiming.shared.dao.RtPunch;
import com.rtiming.shared.dao.RtPunchKey;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceControl;
import com.rtiming.shared.dao.RtRaceControlKey;
import com.rtiming.shared.dao.RtRaceKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.ecard.ECardTypeCodeType;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class EventProcessServiceTest {

  private RtEvent event;
  private RtRace race;
  private RtRaceControl raceControl;
  private RtEcard ecard;
  private RtParticipation participation;
  private RtEntry entry;
  private RtUc additionalInformation;
  private RtAdditionalInformationDef additionalInformationDef;
  private RtEventAdditionalInformation eventAdditionalInformation;
  private RtPunchSession session;
  private RtPunchSession session2;
  private RtPunch punch;
  private RtCourse course;
  private RtCourseControl courseControl;
  private RtControl control;
  private RtControlReplacement controlReplacement;
  private RtUc clazz;
  private RtEventClass eventClass;
  private RtFeeGroup feeGroup;
  private RtFee fee;

  @Test
  public void testDelete1() throws ProcessingException {
    EventProcessService svc = new EventProcessService();
    svc.delete(0L, true);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    EventProcessService svc = new EventProcessService();
    svc.delete(0L, false);
  }

  @Test
  public void testDelete3() throws ProcessingException {
    boolean cleanup = true;
    doDeleteTest(cleanup);
  }

  @Test
  public void testDelete4() throws ProcessingException {
    boolean cleanup = false;
    doDeleteTest(cleanup);
  }

  private void doDeleteTest(boolean cleanup) throws ProcessingException {
    createEventWithData();
    EventProcessService svc = new EventProcessService();
    svc.delete(event.getId().getId(), cleanup);

    RtEvent findEvent = JPA.find(RtEvent.class, event.getId());
    assertNull("deleted", findEvent);

    RtRace findRace = JPA.find(RtRace.class, race.getId());
    assertNull("deleted", findRace);

    RtRaceControl findRaceControl = JPA.find(RtRaceControl.class, raceControl.getId());
    assertNull("deleted", findRaceControl);

    RtEntry findEntry = JPA.find(RtEntry.class, entry.getId());
    if (cleanup) {
      assertNull("deleted", findEntry);
    }
    else {
      assertNotNull("deleted", findEntry);
    }

    RtParticipation findParticipation = JPA.find(RtParticipation.class, participation.getId());
    assertNull("deleted", findParticipation);

    RtEventAdditionalInformation findEventAddInfo = JPA.find(RtEventAdditionalInformation.class, eventAdditionalInformation.getId());
    assertNull("deleted", findEventAddInfo);

    RtPunchSession findSession = JPA.find(RtPunchSession.class, session.getId());
    assertNull("deleted", findSession);

    RtPunchSession findSession2 = JPA.find(RtPunchSession.class, session2.getId());
    assertNull("deleted", findSession2);

    RtPunch findPunch = JPA.find(RtPunch.class, punch.getId());
    assertNull("deleted", findPunch);

    RtControlReplacement findControlReplacement = JPA.find(RtControlReplacement.class, controlReplacement.getId());
    assertNull("deleted", findControlReplacement);

    RtAdditionalInformationDef findAddInfoDef = JPA.find(RtAdditionalInformationDef.class, additionalInformationDef.getId());
    if (cleanup) {
      assertNotNull("deleted", findAddInfoDef);
    }
    else {
      assertNotNull("NOT deleted", findAddInfoDef);
    }

    RtEcard findEcard = JPA.find(RtEcard.class, ecard.getKey());
    if (cleanup) {
      assertNull("deleted", findEcard);
    }
    else {
      assertNotNull("NOT deleted", findEcard);
    }

    RtFeeGroup findFeeGroup = JPA.find(RtFeeGroup.class, feeGroup.getId());
    if (cleanup) {
      assertNull("deleted", findFeeGroup);
    }
    else {
      assertNotNull("NOT deleted", findFeeGroup);
    }

    RtFee findFee = JPA.find(RtFee.class, fee.getId());
    if (cleanup) {
      assertNull("deleted", findFee);
    }
    else {
      assertNotNull("NOT deleted", findFee);
    }
  }

  @Test
  public void testGetZeroTime1() throws Exception {
    EventProcessService svc = new EventProcessService();
    Date result = svc.getZeroTime(0L);
    assertNull("no date", result);
  }

  @Test
  public void testGetZeroTime2() throws Exception {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    Date evtZero = new Date();
    event.setEvtZero(evtZero);
    JPA.merge(event);

    EventProcessService svc = new EventProcessService();
    Date result = svc.getZeroTime(event.getId().getId());
    Assert.assertEquals("no date", evtZero, result);

    JPA.remove(event);
  }

  private void createEventWithData() throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    ecard = new RtEcard();
    ecard.setKey(RtEcardKey.create((Long) null));
    ecard.setEcardNo("ABCDEF");
    ecard.setTypeUid(ECardTypeCodeType.SICard10Code.ID);
    JPA.merge(ecard);

    clazz = new RtUc();
    clazz.setActive(true);
    clazz.setCodeType(ClassCodeType.ID);
    clazz.setId(RtUcKey.create((Long) null));
    JPA.merge(clazz);

    course = new RtCourse();
    course.setId(RtCourseKey.create((Long) null));
    course.setEventNr(event.getId().getId());
    JPA.merge(course);

    feeGroup = new RtFeeGroup();
    feeGroup.setId(RtFeeGroupKey.create((Long) null));
    JPA.merge(feeGroup);

    fee = new RtFee();
    fee.setId(RtFeeKey.create((Long) null));
    fee.setFeeGroupNr(feeGroup.getId().getId());
    JPA.merge(fee);

    eventClass = new RtEventClass();
    RtEventClassKey id = new RtEventClassKey();
    id.setClassUid(clazz.getId().getId());
    id.setEventNr(event.getId().getId());
    id.setClientNr(ServerSession.get().getSessionClientNr());
    eventClass.setId(id);
    eventClass.setCourseNr(course.getId().getId());
    eventClass.setFeeGroupNr(feeGroup.getId().getId());
    JPA.merge(eventClass);

    race = new RtRace();
    race.setId(RtRaceKey.create((Long) null));
    race.setEventNr(event.getId().getId());
    race.setEcardNr(ecard.getKey().getId());
    race.setManualStatus(false);
    race.setLegClassUid(clazz.getId().getId());
    JPA.merge(race);

    raceControl = new RtRaceControl();
    raceControl.setId(RtRaceControlKey.create((Long) null));
    raceControl.setRaceNr(race.getId().getId());
    raceControl.setManualStatus(false);
    JPA.merge(raceControl);

    entry = new RtEntry();
    entry.setId(RtEntryKey.create((Long) null));
    JPA.merge(entry);

    participation = new RtParticipation();
    RtParticipationKey id2 = new RtParticipationKey();
    id2.setClientNr(ServerSession.get().getSessionClientNr());
    id2.setEntryNr(entry.getId().getId());
    id2.setEventNr(event.getId().getId());
    participation.setId(id2);
    JPA.merge(participation);

    additionalInformation = new RtUc();
    additionalInformation.setCodeType(AdditionalInformationCodeType.ID);
    additionalInformation.setActive(true);
    additionalInformation.setId(RtUcKey.create((Long) null));
    JPA.merge(additionalInformation);

    additionalInformationDef = new RtAdditionalInformationDef();
    RtAdditionalInformationDefKey key = new RtAdditionalInformationDefKey();
    key.setId(additionalInformation.getId().getId());
    key.setClientNr(ServerSession.get().getSessionClientNr());
    additionalInformationDef.setId(key);
    JPA.merge(additionalInformationDef);

    eventAdditionalInformation = new RtEventAdditionalInformation();
    RtEventAdditionalInformationKey key2 = new RtEventAdditionalInformationKey();
    key2.setAdditionalInformationUid(additionalInformation.getId().getId());
    key2.setEventNr(event.getId().getId());
    key2.setClientNr(ServerSession.get().getSessionClientNr());
    eventAdditionalInformation.setId(key2);

    session = new RtPunchSession();
    session.setId(RtPunchSessionKey.create((Long) null));
    session.setEventNr(event.getId().getId());
    session.setRaceNr(race.getId().getId());
    JPA.merge(session);

    session2 = new RtPunchSession();
    session2.setId(RtPunchSessionKey.create((Long) null));
    session2.setEventNr(event.getId().getId());
    JPA.merge(session2);

    punch = new RtPunch();
    RtPunchKey key3 = new RtPunchKey();
    key3.setId(session.getId().getId());
    key3.setSortcode(1L);
    key3.setClientNr(ServerSession.get().getSessionClientNr());
    punch.setId(key3);
    JPA.merge(punch);

    control = new RtControl();
    control.setId(RtControlKey.create((Long) null));
    control.setEventNr(event.getId().getId());
    control.setActive(true);
    JPA.merge(control);

    courseControl = new RtCourseControl();
    courseControl.setId(RtCourseControlKey.create((Long) null));
    courseControl.setCourseNr(course.getId().getId());
    courseControl.setSortcode(1L);
    courseControl.setCountLeg(true);
    courseControl.setMandatory(true);
    courseControl.setControlNr(control.getId().getId());
    JPA.merge(courseControl);

    controlReplacement = new RtControlReplacement();
    RtControlReplacementKey key5 = new RtControlReplacementKey();
    key5.setControlNr(control.getId().getId());
    key5.setReplacementControlNr(control.getId().getId());
    key5.setClientNr(ServerSession.get().getSessionClientNr());
    controlReplacement.setId(key5);
    JPA.merge(controlReplacement);
  }

  @Test
  public void testStore() throws Exception {
    createEventWithData();

    EventProcessService svc = new EventProcessService();
    EventBean bean = new EventBean();
    bean.setEventNr(event.getId().getId());
    bean = svc.load(bean);
    bean.setName("isabel");
    svc.store(bean);

    RtEvent find = JPA.find(RtEvent.class, event.getId());
    Assert.assertEquals("updated", "isabel", find.getName());
  }

  @Test
  public void testGetRunnerStartedCount1() throws Exception {
    EventProcessService svc = new EventProcessService();
    long result = svc.getRunnerStartedCount(0L, null, null);
    assertEquals("result", 0, result);
  }

  @Test
  public void testGetRunnerStartedCount2() throws Exception {
    createEventWithData();
    EventProcessService svc = new EventProcessService();
    long result = svc.getRunnerStartedCount(event.getId().getId(), clazz.getId().getId(), null);
    assertEquals("result", 1, result);
  }

  @Test
  public void testGetRunnerStartedCount3() throws Exception {
    createEventWithData();
    EventProcessService svc = new EventProcessService();
    long result = svc.getRunnerStartedCount(event.getId().getId(), clazz.getId().getId(), course.getId().getId());
    assertEquals("result", 1, result);
  }

  @Test
  public void testGetRunnerStartedCount4() throws Exception {
    createEventWithData();
    EventProcessService svc = new EventProcessService();
    long result = svc.getRunnerStartedCount(event.getId().getId(), null, course.getId().getId());
    assertEquals("result", 1, result);
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      EventProcessService svc = new EventProcessService();
      svc.delete(event.getId().getId(), false);
    }
    if (ecard != null) {
      JPA.remove(ecard);
    }
    if (additionalInformation != null) {
      JPA.remove(additionalInformation);
    }
    if (additionalInformationDef != null) {
      JPA.remove(additionalInformationDef);
    }
  }

}
