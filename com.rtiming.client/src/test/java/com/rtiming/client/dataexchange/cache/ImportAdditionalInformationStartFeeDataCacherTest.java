package com.rtiming.client.dataexchange.cache;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.dataexchange.cache.DefaultAdditionalInformationStartFeeDataCacher;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class ImportAdditionalInformationStartFeeDataCacherTest {

  private EventTestDataProvider event;

  @Before
  public void before() throws ProcessingException {
    event = new EventTestDataProvider();
  }

  @Test
  public void testStartFeeCodeType() throws Exception {
    AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
    formData.setAdditionalInformationUid(AdditionalInformationCodeType.IndividualStartFeeCode.ID);
    BEANS.get(IAdditionalInformationAdministrationProcessService.class).delete(formData);

    // test code creation
    DefaultAdditionalInformationStartFeeDataCacher cacher = new DefaultAdditionalInformationStartFeeDataCacher(event.getEventNr());
    cacher.get("TEST");

    // now the code is created
    DefaultAdditionalInformationStartFeeDataCacher cacher2 = new DefaultAdditionalInformationStartFeeDataCacher(event.getEventNr());
    cacher2.get("TEST");
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

}
