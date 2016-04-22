package com.rtiming.server.settings.fee;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.util.List;

import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtAdditionalInformationDef;
import com.rtiming.shared.dao.RtAdditionalInformationDefKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtFee;
import com.rtiming.shared.dao.RtFeeGroup;
import com.rtiming.shared.dao.RtFeeGroupKey;
import com.rtiming.shared.dao.RtFeeKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.fee.FeeFormData;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class FeeProcessServiceTest {

  private RtFeeGroup feeGroup;
  private RtFee fee;
  private RtAdditionalInformationDef def;
  private RtUc uc;
  private RtEventClass eventClass;
  private RtEvent event;
  private RtUc clazz;

  @Test
  public void testStore1() throws ProcessingException {
    FeeProcessService svc = new FeeProcessService();
    FeeFormData formData = new FeeFormData();
    formData.setFeeGroupNr(-555L);
    svc.store(formData);
  }

  @Test
  public void testStore2() throws ProcessingException {
    FeeProcessService svc = new FeeProcessService();
    FeeFormData formData = new FeeFormData();
    formData.setFeeGroupNr(null);
    svc.store(formData);
  }

  @Test
  public void testDelete() throws ProcessingException {
    fee = new RtFee();
    RtFeeKey key = RtFeeKey.create((Long) null);
    fee.setId(key);
    JPA.merge(fee);
    assertNotNull("created", fee.getId().getId());

    FeeProcessService svc = new FeeProcessService();
    FeeFormData formData = new FeeFormData();
    formData.setFeeNr(fee.getId().getId());
    formData = svc.load(formData);
    svc.delete(formData);

    RtFee result = JPA.find(RtFee.class, key);
    assertNull("deleted", result);
    fee = null;
  }

  @Test
  public void testLoadFeeConfiguration1() throws Exception {
    FeeProcessService svc = new FeeProcessService();
    List<FeeFormData> result = svc.loadFeeConfiguration();
    assertTrue("empty or filled", result.size() >= 0);
  }

  @Test
  @SuppressWarnings("null")
  public void testLoadFeeConfigurationAddInfo() throws Exception {
    createTestData(false, true);
    FeeProcessService svc = new FeeProcessService();
    List<FeeFormData> result = svc.loadFeeConfigurationAddInfo();
    assertTrue("Size", result.size() > 0);
    FeeFormData testDataFound = null;
    for (FeeFormData formData : result) {
      if (CompareUtility.equals(formData.getFeeNr(), fee.getId().getId())) {
        testDataFound = formData;
        break;
      }
    }
    assertNotNull("fee must exist in configuration", testDataFound);
    assertEquals(feeGroup.getCashPaymentOnRegistration(), testDataFound.getCashPaymentOnRegistrationProperty().getValue());
  }

  @Test
  @SuppressWarnings("null")
  public void testLoadFeeConfigurationEventClass() throws Exception {
    createTestData(true, false);
    FeeProcessService svc = new FeeProcessService();
    List<FeeFormData> result = svc.loadFeeConfigurationEventClass();
    assertTrue("Size", result.size() > 0);
    FeeFormData testDataFound = null;
    for (FeeFormData formData : result) {
      if (CompareUtility.equals(formData.getFeeNr(), fee.getId().getId())) {
        testDataFound = formData;
        break;
      }
    }
    assertNotNull("fee must exist in configuration", testDataFound);
    assertEquals(feeGroup.getCashPaymentOnRegistration(), testDataFound.getCashPaymentOnRegistrationProperty().getValue());
  }

  private void createTestData(boolean createEventClass, boolean createAddInfo) throws ProcessingException {
    feeGroup = new RtFeeGroup();
    feeGroup.setId(RtFeeGroupKey.create((Long) null));
    feeGroup.setCashPaymentOnRegistration(true);
    JPA.merge(feeGroup);

    fee = new RtFee();
    fee.setId(RtFeeKey.create((Long) null));
    fee.setFeeGroupNr(feeGroup.getId().getId());
    JPA.merge(fee);

    uc = new RtUc();
    uc.setCodeType(AdditionalInformationCodeType.ID);
    uc.setActive(true);
    uc.setId(RtUcKey.create((Long) null));
    JPA.merge(uc);

    if (createAddInfo) {
      def = new RtAdditionalInformationDef();
      def.setId(RtAdditionalInformationDefKey.create(uc.getId().getId()));
      def.setFeeGroupNr(feeGroup.getId().getId());
      JPA.merge(def);
    }

    if (createEventClass) {
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
      eventClass.setId(id);
      eventClass.setFeeGroupNr(feeGroup.getId().getId());
      JPA.merge(eventClass);
    }
  }

  @After
  public void after() throws ProcessingException {
    if (eventClass != null) {
      JPA.remove(eventClass);
    }
    if (clazz != null) {
      JPA.remove(clazz);
    }
    if (event != null) {
      JPA.remove(event);
    }
    if (fee != null) {
      JPA.remove(fee);
    }
    if (def != null) {
      JPA.remove(def);
    }
    if (uc != null) {
      JPA.remove(uc);
    }
    if (feeGroup != null) {
      JPA.remove(feeGroup);
    }
  }

}
