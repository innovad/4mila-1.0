package com.rtiming.server.settings;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.event.EventProcessService;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClubKey;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunnerKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.ecard.ECardTypeCodeType;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.entry.IEntryService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.runner.SexCodeType;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class TestDataProcessServiceTest {

  private RtEvent event;
  private RtUc clazz;
  private RtEventClass eventClass;
  private RtRunner runner;
  private RtClub club;
  private RtEcard ecard;

  @Test
  public void testCreateTestData() throws ProcessingException {
    createEvent();

    TestDataProcessService svc = new TestDataProcessService();
    EventConfiguration configuration = BEANS.get(IEntryService.class).loadEventConfiguration();
    svc.createTestData(event.getId().getId(), clazz.getId().getId(), configuration);
  }

  private void createEvent() throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    clazz = new RtUc();
    clazz.setActive(true);
    clazz.setCodeType(ClassCodeType.ID);
    clazz.setId(RtUcKey.create((Long) null));
    JPA.merge(clazz);

    eventClass = new RtEventClass();
    RtEventClassKey id = new RtEventClassKey();
    id.setClassUid(clazz.getId().getId());
    id.setEventNr(event.getId().getId());
    id.setClientNr(ServerSession.get().getSessionClientNr());
    eventClass.setTeamSizeMin(1L);
    eventClass.setTeamSizeMax(1L);
    eventClass.setId(id);
    eventClass.setTypeUid(ClassTypeCodeType.IndividualEventCode.ID);
    JPA.merge(eventClass);

    club = new RtClub();
    club.setId(RtClubKey.create((Long) null));
    JPA.merge(club);

    ecard = new RtEcard();
    ecard.setKey(RtEcardKey.create((Long) null));
    ecard.setEcardNo("ABCDEF");
    ecard.setTypeUid(ECardTypeCodeType.SICard10Code.ID);
    JPA.merge(ecard);

    runner = new RtRunner();
    runner.setId(RtRunnerKey.create((Long) null));
    runner.setActive(true);
    runner.setDefaultClassUid(clazz.getId().getId());
    runner.setSexUid(SexCodeType.ManCode.ID);
    runner.setYear(2012L);
    runner.setFirstName("FirstName");
    runner.setLastName("LastName");
    runner.setClubNr(club.getId().getId());
    runner.setEcardNr(ecard.getKey().getId());
    JPA.merge(runner);
  }

  @After
  public void after() throws ProcessingException {
    if (event != null && JPA.find(RtEvent.class, event.getId()) != null) {
      EventProcessService svc = new EventProcessService();
      svc.delete(event.getId().getId(), true);
    }
  }

}
