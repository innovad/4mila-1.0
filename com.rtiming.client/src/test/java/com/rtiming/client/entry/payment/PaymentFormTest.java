package com.rtiming.client.entry.payment;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.entry.RegistrationForm;
import com.rtiming.client.entry.payment.PaymentForm.MainBox.CurrencyUidField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;
import com.rtiming.shared.common.database.sql.PaymentBean;
import com.rtiming.shared.entry.IRegistrationProcessService;
import com.rtiming.shared.entry.RegistrationFormData;
import com.rtiming.shared.entry.payment.IPaymentProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class PaymentFormTest extends AbstractFormTest<PaymentForm> {

  private RegistrationForm registration;
  private static CurrencyTestDataProvider currency;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    currency = new CurrencyTestDataProvider();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    currency.remove();
  }

  @Override
  protected List<FieldValue> getFixedValues() throws ProcessingException {
    List<FieldValue> list = new ArrayList<FieldValue>();
    list.add(new FieldValue(CurrencyUidField.class, currency.getCurrencyUid()));
    return list;
  }

  @Override
  public void setUpForm() throws ProcessingException {
    registration = new RegistrationForm();
    registration.startNew();
    FormTestUtility.fillFormFields(registration, new MaxFormFieldValueProvider());
    registration.doOk();
    super.setUpForm();
  }

  @Override
  protected PaymentForm getStartedForm() throws ProcessingException {
    PaymentForm form = new PaymentForm();
    form.getRegistrationField().setValue(registration.getRegistrationNr());
    form.startNew();
    return form;
  }

  @Override
  protected PaymentForm getModifyForm() throws ProcessingException {
    PaymentForm form = new PaymentForm();
    form.setPaymentNr(getForm().getPaymentNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    PaymentBean bean = new PaymentBean();
    bean.setPaymentNr(getForm().getPaymentNr());
    BEANS.get(IPaymentProcessService.class).delete(bean);

    RegistrationFormData formData = new RegistrationFormData();
    formData.setRegistrationNr(registration.getRegistrationNr());
    BEANS.get(IRegistrationProcessService.class).delete(formData);
  }

}
