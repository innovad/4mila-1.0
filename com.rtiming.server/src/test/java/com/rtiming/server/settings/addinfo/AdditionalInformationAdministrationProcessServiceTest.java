package com.rtiming.server.settings.addinfo;

import static org.junit.Assert.assertNull;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtAdditionalInformationDef;
import com.rtiming.shared.dao.RtAdditionalInformationDefKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class AdditionalInformationAdministrationProcessServiceTest {

  @Test
  public void testDelete1() throws ProcessingException {
    AdditionalInformationAdministrationProcessService svc = new AdditionalInformationAdministrationProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    AdditionalInformationAdministrationProcessService svc = new AdditionalInformationAdministrationProcessService();
    svc.delete(new AdditionalInformationAdministrationFormData());
  }

  @Test
  public void testDelete3() throws ProcessingException {
    RtAdditionalInformationDef def = new RtAdditionalInformationDef();
    def.setId(RtAdditionalInformationDefKey.create((Long) null));
    JPA.merge(def);

    AdditionalInformationAdministrationProcessService svc = new AdditionalInformationAdministrationProcessService();
    AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
    formData.setAdditionalInformationUid(def.getId().getId());
    svc.delete(formData);

    RtAdditionalInformationDef find = JPA.find(RtAdditionalInformationDef.class, def.getId());
    assertNull("deleted", find);
  }

  @Test
  public void testLoad1() throws Exception {
    AdditionalInformationAdministrationProcessService svc = new AdditionalInformationAdministrationProcessService();
    AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
    svc.load(formData);
  }

  @Test
  public void testStore1() throws Exception {
    RtUc uc = new RtUc();
    uc.setCodeType(AdditionalInformationCodeType.ID);
    uc.setActive(true);
    uc.setId(RtUcKey.create((Long) null));
    JPA.merge(uc);

    RtAdditionalInformationDef def = new RtAdditionalInformationDef();
    def.setId(RtAdditionalInformationDefKey.create(uc.getId().getId()));
    JPA.merge(def);

    AdditionalInformationAdministrationProcessService svc = new AdditionalInformationAdministrationProcessService();
    AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
    formData.setAdditionalInformationUid(def.getId().getId());
    formData = svc.load(formData);
    svc.store(formData);

    JPA.remove(def);
    JPA.remove(uc);
  }

}
