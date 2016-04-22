package com.rtiming.server.entry.startlist;

import java.util.LinkedList;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtStartlistSetting;
import com.rtiming.shared.dao.RtStartlistSettingKey;
import com.rtiming.shared.dao.RtStartlistSettingVacant;
import com.rtiming.shared.dao.RtStartlistSettingVacantKey;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData;
import com.rtiming.shared.entry.startlist.StartlistSettingVacantPositionCodeType;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class StartlistVacantUtilityTest {

  @Test
  public void testVacantEarly() throws Exception {
    LinkedList<StartlistParticipationBean> completeStartlist = createStartlist(1);
    StartlistSettingFormData startlistSetting = createStartlistSetting(10L, 50L, StartlistSettingVacantPositionCodeType.EarlyStartCode.ID);
    StartlistVacantUtility.addVacants(startlistSetting, completeStartlist);
    assertOrder(completeStartlist, true, true, true, true, true, true, true, true, true, true, false);
  }

  @Test
  public void testVacantLate() throws Exception {
    LinkedList<StartlistParticipationBean> completeStartlist = createStartlist(3);
    StartlistSettingFormData startlistSetting = createStartlistSetting(1L, 1L, StartlistSettingVacantPositionCodeType.LateStartCode.ID);
    StartlistVacantUtility.addVacants(startlistSetting, completeStartlist);
    assertOrder(completeStartlist, false, false, false, true);
  }

  @Test
  public void testVacantDrawing() throws Exception {
    LinkedList<StartlistParticipationBean> completeStartlist = createStartlist(0);
    StartlistSettingFormData startlistSetting = createStartlistSetting(3L, 3L, StartlistSettingVacantPositionCodeType.DrawingCode.ID);
    StartlistVacantUtility.addVacants(startlistSetting, completeStartlist);
    assertOrder(completeStartlist, true, true, true);
  }

  @Test
  public void testVacantSize1() throws ProcessingException {
    LinkedList<StartlistParticipationBean> completeStartlist = createStartlist(20);
    StartlistSettingFormData startlistSetting = createStartlistSetting(5L, 50L, StartlistSettingVacantPositionCodeType.DrawingCode.ID);
    StartlistVacantUtility.addVacants(startlistSetting, completeStartlist);
    assertSize(completeStartlist, 30, 10);
  }

  @Test
  public void testVacantSize2() throws ProcessingException {
    LinkedList<StartlistParticipationBean> completeStartlist = createStartlist(20);
    StartlistSettingFormData startlistSetting = createStartlistSetting(11L, 50L, StartlistSettingVacantPositionCodeType.DrawingCode.ID);
    StartlistVacantUtility.addVacants(startlistSetting, completeStartlist);
    assertSize(completeStartlist, 31, 11);
  }

  @Test
  public void testVacantSizeOnlyVacants() throws ProcessingException {
    LinkedList<StartlistParticipationBean> completeStartlist = createStartlist(0);
    StartlistSettingFormData startlistSetting = createStartlistSetting(6L, 100L, StartlistSettingVacantPositionCodeType.DrawingCode.ID);
    StartlistVacantUtility.addVacants(startlistSetting, completeStartlist);
    assertSize(completeStartlist, 6, 6);
  }

  @Test
  public void testVacantSizeNoVacants() throws ProcessingException {
    LinkedList<StartlistParticipationBean> completeStartlist = createStartlist(0);
    StartlistSettingFormData startlistSetting = createStartlistSetting(0L, 100L, StartlistSettingVacantPositionCodeType.DrawingCode.ID);
    StartlistVacantUtility.addVacants(startlistSetting, completeStartlist);
    assertSize(completeStartlist, 0, 0);
  }

  @Test
  public void testVacantSizeRounding() throws ProcessingException {
    LinkedList<StartlistParticipationBean> completeStartlist = createStartlist(3);
    StartlistSettingFormData startlistSetting = createStartlistSetting(0L, 50L, StartlistSettingVacantPositionCodeType.DrawingCode.ID);
    StartlistVacantUtility.addVacants(startlistSetting, completeStartlist);
    assertSize(completeStartlist, 5, 2);
  }

  private void assertOrder(LinkedList<StartlistParticipationBean> startlist, boolean... isVacants) {
    Assert.assertEquals("Length", startlist.size(), isVacants.length);

    for (StartlistParticipationBean bean : startlist) {
      Assert.assertEquals("Vacant", isVacants[startlist.indexOf(bean)], bean.isVacant());
    }
  }

  private void assertSize(LinkedList<StartlistParticipationBean> startlist, long totalSize, long vacantSize) {
    long realTotalSize = startlist.size();
    long realVacantSize = 0;
    for (StartlistParticipationBean bean : startlist) {
      if (bean.isVacant()) {
        realVacantSize++;
      }
    }
    Assert.assertEquals("Total", totalSize, realTotalSize);
    Assert.assertEquals("Vacant", vacantSize, realVacantSize);
  }

  private StartlistSettingFormData createStartlistSetting(Long absolute, Long percent, Long positionUid) {
    StartlistSettingFormData startlistSetting = new StartlistSettingFormData();
    startlistSetting.setStartlistSettingNr(1L);
    startlistSetting.getVacantAbsolute().setValue(absolute);
    startlistSetting.getVacantPercent().setValue(percent);
    startlistSetting.getVacantPositionGroup().setValue(positionUid);
    return startlistSetting;
  }

  private LinkedList<StartlistParticipationBean> createStartlist(long count) {
    LinkedList<StartlistParticipationBean> completeStartlist = new LinkedList<StartlistParticipationBean>();
    for (long k = 0; k < count; k++) {
      StartlistParticipationBean bean = new StartlistParticipationBean();
      bean.setStartlistSettingNr(1L);
      bean.setEntryNr(k);
      bean.setEventNr(1L);
      completeStartlist.add(bean);
    }
    return completeStartlist;
  }

  @Test
  public void testRemoveVacants1() throws Exception {
    StartlistVacantUtility.removeVacants();
  }

  @Test
  public void testRemoveVacants2() throws Exception {
    RtStartlistSetting setting = new RtStartlistSetting();
    setting.setId(RtStartlistSettingKey.create((Long) null));
    JPA.merge(setting);

    StartlistVacantUtility.removeVacants(setting.getId().getId());

    JPA.remove(setting);
  }

  @Test
  public void testRemoveVacants3() throws Exception {
    RtStartlistSetting setting = new RtStartlistSetting();
    setting.setId(RtStartlistSettingKey.create((Long) null));
    JPA.merge(setting);

    RtStartlistSettingVacant vacant = new RtStartlistSettingVacant();
    RtStartlistSettingVacantKey id = new RtStartlistSettingVacantKey();
    id.setBibNo("blubb");
    id.setClientNr(ServerSession.get().getSessionClientNr());
    id.setStartlistSettingNr(setting.getId().getId());
    id.setStartTime(777L);
    vacant.setId(id);
    JPA.merge(vacant);

    StartlistVacantUtility.removeVacants(setting.getId().getId());

    RtStartlistSettingVacant find = JPA.find(RtStartlistSettingVacant.class, vacant.getId());
    Assert.assertNull("deleted", find);

    JPA.remove(setting);
  }

}
