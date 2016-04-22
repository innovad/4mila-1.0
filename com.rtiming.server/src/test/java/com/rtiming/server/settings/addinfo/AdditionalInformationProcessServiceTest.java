package com.rtiming.server.settings.addinfo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.AdditionalInformationBean;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.dao.RtAdditionalInformationDef;
import com.rtiming.shared.dao.RtAdditionalInformationDefKey;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClubKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class AdditionalInformationProcessServiceTest {

  private RtUc uc;
  private RtAdditionalInformationDef def;
  private RtClub club;
  private String testColumnShortcut;

  @Test
  public void testLoad1() throws ProcessingException {
    AdditionalInformationProcessService svc = new AdditionalInformationProcessService();
    svc.load(new AdditionalInformationBean());
  }

  @Test
  public void testLoad2() throws ProcessingException {
    createSampleData();

    AdditionalInformationProcessService svc = new AdditionalInformationProcessService();
    AdditionalInformationBean bean = new AdditionalInformationBean();
    bean.setClientNr(ServerSession.get().getSessionClientNr());
    bean.setEntityUid(EntityCodeType.ClubCode.ID);
    bean.setJoinNr(club.getId().getId());
    bean = svc.load(bean);

    assertEquals("1 row", 1, bean.getValues().size());
    assertEquals("loaded", 12345, bean.getValues().get(0).getValueInteger().longValue());
  }

  @Test
  public void testStore1() throws Exception {
    createSampleData();

    AdditionalInformationProcessService svc = new AdditionalInformationProcessService();
    AdditionalInformationBean bean = new AdditionalInformationBean();
    bean.setClientNr(ServerSession.get().getSessionClientNr());
    bean.setEntityUid(EntityCodeType.ClubCode.ID);
    bean.setJoinNr(club.getId().getId());
    bean = svc.load(bean);
    assertEquals("1 row", 1, bean.getValues().size());
    bean.getValues().get(0).setValueInteger(777L);
    bean = svc.store(bean);

    bean = svc.load(bean);
    assertEquals("1 row", 1, bean.getValues().size());
    assertEquals("updated", 777, bean.getValues().get(0).getValueInteger().longValue());
  }

  @Test
  public void testPrepareCreate1() throws Exception {
    AdditionalInformationProcessService svc = new AdditionalInformationProcessService();
    AdditionalInformationBean bean = new AdditionalInformationBean();
    bean = svc.prepareCreate(bean);
    assertEquals("no addinfo", 0, bean.getValues().size());
  }

  @Test
  public void testPrepareCreate2() throws Exception {
    createSampleData();
    AdditionalInformationProcessService svc = new AdditionalInformationProcessService();
    AdditionalInformationBean bean = new AdditionalInformationBean();
    bean.setClientNr(ServerSession.get().getSessionClientNr());
    bean.setEntityUid(def.getEntityUid());
    bean.setJoinNr(club.getId().getId());
    bean = svc.prepareCreate(bean);
    assertTrue("min. 1 addinfo", bean.getValues().size() >= 1);
    boolean found = false;
    for (AdditionalInformationValueBean value : bean.getValues()) {
      if (value.getDefaultInteger() == def.getDefaultLong()) {
        found = true;
        break;
      }
    }
    assertTrue("addinfo found", found);
  }

  private void createSampleData() throws ProcessingException {
    uc = new RtUc();
    uc.setId(RtUcKey.create((Long) null));
    uc.setCodeType(AdditionalInformationCodeType.ID);
    uc.setActive(true);
    uc.setShortcut(String.valueOf(uc.getId().getId()));
    JPA.persist(uc);

    def = new RtAdditionalInformationDef();
    def.setId(RtAdditionalInformationDefKey.create(uc.getId().getId()));
    def.setEntityUid(EntityCodeType.ClubCode.ID);
    def.setTypeUid(AdditionalInformationTypeCodeType.IntegerCode.ID);
    def.setDefaultLong(123L);
    JPA.persist(def);

    testColumnShortcut = String.valueOf(def.getId().getId());
    AdditionalInformationDatabaseUtility.createColumn(EntityCodeType.ClubCode.ID, AdditionalInformationTypeCodeType.IntegerCode.ID, testColumnShortcut);

    club = new RtClub();
    club.setId(RtClubKey.create((Long) null));
    JPA.persist(club);

    AdditionalInformationValueBean value = new AdditionalInformationValueBean();
    value.setValueInteger(12345L);
    value.setAdditionalInformationUid(def.getId().getId());
    AdditionalInformationDatabaseUtility.updateValue(def.getEntityUid(), club.getId().getId(), ServerSession.get().getSessionClientNr(), value);

    SharedCache.resetCache();
  }

  @After
  public void after() throws ProcessingException {
    if (!StringUtility.isNullOrEmpty(testColumnShortcut)) {
      JPA.commit();
      AdditionalInformationDatabaseUtility.dropColumn(EntityCodeType.ClubCode.ID, testColumnShortcut);
    }
    if (def != null) {
      JPA.remove(def);
    }
    if (uc != null) {
      JPA.remove(uc);
    }
    if (club != null) {
      JPA.remove(club);
    }
    SharedCache.resetCache();
  }

}
