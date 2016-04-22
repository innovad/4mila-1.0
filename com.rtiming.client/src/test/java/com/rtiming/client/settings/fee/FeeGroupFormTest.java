package com.rtiming.client.settings.fee;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.shared.settings.fee.FeeGroupFormData;
import com.rtiming.shared.settings.fee.IFeeGroupProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class FeeGroupFormTest extends AbstractFormTest<FeeGroupForm> {

  @Override
  protected FeeGroupForm getStartedForm() throws ProcessingException {
    FeeGroupForm form = new FeeGroupForm();
    form.startNew();
    return form;
  }

  @Override
  protected FeeGroupForm getModifyForm() throws ProcessingException {
    FeeGroupForm form = new FeeGroupForm();
    form.setFeeGroupNr(getForm().getFeeGroupNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    FeeGroupFormData formData = new FeeGroupFormData();
    formData.setFeeGroupNr(getForm().getFeeGroupNr());
    BEANS.get(IFeeGroupProcessService.class).delete(formData);
  }

}
