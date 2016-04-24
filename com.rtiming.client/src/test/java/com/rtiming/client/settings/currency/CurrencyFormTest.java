package com.rtiming.client.settings.currency;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class CurrencyFormTest extends AbstractFormTest<CurrencyForm> {

  @Override
  protected CurrencyForm getStartedForm() throws ProcessingException {
    CurrencyForm form = new CurrencyForm();
    form.startNew();
    return form;
  }

  @Override
  protected CurrencyForm getModifyForm() throws ProcessingException {
    CurrencyForm form = new CurrencyForm();
    form.setCurrencyUid(getForm().getCurrencyUid());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    CurrencyFormData formData = new CurrencyFormData();
    formData.setCurrencyUid(getForm().getCurrencyUid());
    BEANS.get(ICurrencyProcessService.class).delete(formData);
  }

}
