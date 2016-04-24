package com.rtiming.client.settings.addinfo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.EntityField;
import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.data.AdditionalInformationAdministrationTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationLookupCall;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class AdditionalInformationLookupCallTest extends AbstractDefaultLookupCallTest {

  private AdditionalInformationAdministrationTestDataProvider addInfo;

  @Override
  protected LookupCall createLookupCall() throws ProcessingException {
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.EntryCode.ID));
    addInfo = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    AdditionalInformationLookupCall lookupCall = new AdditionalInformationLookupCall();
    lookupCall.setEntityUid(addInfo.getForm().getEntityField().getValue());
    return lookupCall;
  }

  @Override
  public void deleteTestRow() throws ProcessingException {
    addInfo.remove();
  }

}
