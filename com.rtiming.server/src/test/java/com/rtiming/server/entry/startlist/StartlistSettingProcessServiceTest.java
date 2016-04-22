package com.rtiming.server.entry.startlist;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtStartlistSetting;
import com.rtiming.shared.dao.RtStartlistSettingKey;
import com.rtiming.shared.dao.RtStartlistSettingOption;
import com.rtiming.shared.dao.RtStartlistSettingOptionKey;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;
import com.rtiming.shared.event.IEventProcessService;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class StartlistSettingProcessServiceTest {

  private RtEvent event;
  private RtStartlistSetting setting;

  @Test
  public void testDelete1() throws ProcessingException {
    StartlistSettingProcessService svc = new StartlistSettingProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    StartlistSettingProcessService svc = new StartlistSettingProcessService();
    svc.delete(new StartlistSettingFormData());
  }

  @Test
  public void testDelete3() throws ProcessingException {
    createStartlistSetting();

    StartlistSettingProcessService svc = new StartlistSettingProcessService();
    StartlistSettingFormData formData = new StartlistSettingFormData();
    formData.setStartlistSettingNr(setting.getId().getId());
    svc.delete(formData);

    RtStartlistSetting find = JPA.find(RtStartlistSetting.class, setting.getId());
    assertNull(find);
  }

  private void createStartlistSetting() throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    event.setEvtZero(DateUtility.parse("13082014", "ddMMyyyy"));
    JPA.merge(event);

    setting = new RtStartlistSetting();
    setting.setId(RtStartlistSettingKey.create((Long) null));
    setting.setEventNr(event.getId().getId());
    JPA.merge(setting);
  }

  @Test
  public void testStore1() throws Exception {
    createStartlistSetting();

    StartlistSettingProcessService svc = new StartlistSettingProcessService();
    StartlistSettingFormData formData = new StartlistSettingFormData();
    formData.setStartlistSettingNr(setting.getId().getId());
    formData = svc.load(formData);
    formData.getVacantAbsolute().setValue(123L);
    svc.store(formData);

    RtStartlistSetting find = JPA.find(RtStartlistSetting.class, setting.getId());
    assertEquals("updated", 123L, find.getVacantAbsolute().longValue());
  }

  @Test
  public void testStore2() throws Exception {
    createStartlistSetting();

    StartlistSettingProcessService svc = new StartlistSettingProcessService();
    StartlistSettingFormData formData = new StartlistSettingFormData();
    formData.setStartlistSettingNr(setting.getId().getId());
    formData = svc.load(formData);
    formData.getOptions().setValue(new HashSet<>(Arrays.asList(new Long[]{StartlistSettingOptionCodeType.SeparateClubsCode.ID})));
    svc.store(formData);

    RtStartlistSettingOptionKey key = new RtStartlistSettingOptionKey();
    key.setClientNr(setting.getId().getClientNr());
    key.setOptionUid(StartlistSettingOptionCodeType.SeparateClubsCode.ID);
    key.setStartlistSettingNr(setting.getId().getId());
    RtStartlistSettingOption find = JPA.find(RtStartlistSettingOption.class, key);
    assertNotNull("exists", find);
  }

  @Test
  public void testLoad1() throws Exception {
    Long[] options = new Long[]{StartlistSettingOptionCodeType.SeparateClubsCode.ID, StartlistSettingOptionCodeType.AllowStarttimeWishesCode.ID};
    doTestLoadOptions(options);
  }

  @Test
  public void testLoad2() throws Exception {
    Long[] options = new Long[]{StartlistSettingOptionCodeType.AllowStarttimeWishesCode.ID};
    doTestLoadOptions(options);
  }

  @Test
  public void testLoad3() throws Exception {
    Long[] options = new Long[]{};
    doTestLoadOptions(options);
  }

  @Test
  public void testLoad4() throws Exception {
    createStartlistSetting();

    StartlistSettingProcessService svc = new StartlistSettingProcessService();
    StartlistSettingFormData formData = new StartlistSettingFormData();
    formData.setStartlistSettingNr(setting.getId().getId());
    formData = svc.load(formData);
    assertEquals("event set", setting.getEventNr(), formData.getEventNr());
    formData.getFirstStart().setValue(DateUtility.parse("02072012", "ddMMyyyy"));
    formData = svc.store(formData);

    formData = new StartlistSettingFormData();
    formData.setStartlistSettingNr(setting.getId().getId());
    formData = svc.load(formData);

    assertEquals("event still set", setting.getEventNr(), formData.getEventNr());
    assertEquals("date", DateUtility.parse("02072012", "ddMMyyyy"), formData.getFirstStart().getValue());
  }

  private void doTestLoadOptions(Long[] options) throws ProcessingException {
    createStartlistSetting();

    StartlistSettingProcessService svc = new StartlistSettingProcessService();
    StartlistSettingFormData formData = new StartlistSettingFormData();
    formData.setStartlistSettingNr(setting.getId().getId());
    formData = svc.load(formData);
    formData.getOptions().setValue(new HashSet<>(Arrays.asList(options)));
    formData = svc.store(formData);

    formData = new StartlistSettingFormData();
    formData.setStartlistSettingNr(setting.getId().getId());
    formData = svc.load(formData);

    assertEquals("loaded", options.length, formData.getOptions().getValue().size());
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      BEANS.get(IEventProcessService.class).delete(event.getId().getId(), false);
    }
  }

}
