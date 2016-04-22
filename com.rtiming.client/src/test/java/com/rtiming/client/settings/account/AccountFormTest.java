package com.rtiming.client.settings.account;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.EMailField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.PasswordField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.RepeatPasswordField;
import com.rtiming.client.settings.account.AccountForm.MainBox.AccountBox.UsernameField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.test.helper.ITestingJPAService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class AccountFormTest extends AbstractFormTest<AccountForm> {

  @Override
  public void setUpForm() throws ProcessingException {
    BEANS.get(ITestingJPAService.class).cleanupAccounts();
    super.setUpForm();
  }

  @Override
  protected AccountForm getStartedForm() throws ProcessingException {
    AccountForm form = new AccountForm();
    form.startNew();
    return form;
  }

  @Override
  protected AccountForm getModifyForm() throws ProcessingException {
    AccountForm form = new AccountForm();
    form.setAccountNr(getForm().getAccountNr());
    form.startModify();
    return form;
  }

  @Override
  protected List<FieldValue> getFixedValues() {
    List<FieldValue> result = new ArrayList<FieldValue>();
    Date date = new Date();
    String unique = "" + date.getTime();
    result.add(new FieldValue(PasswordField.class, unique));
    result.add(new FieldValue(RepeatPasswordField.class, unique));
    result.add(new FieldValue(UsernameField.class, unique));
    result.add(new FieldValue(EMailField.class, unique + "@w123456.com"));
    return result;
  }

  @Override
  protected List<String> getFieldIdsToIgnore() {
    List<String> list = new ArrayList<String>();
    list.add(getForm().getPasswordField().getFieldId());
    list.add(getForm().getRepeatPasswordField().getFieldId());
    return list;
  }

  @Override
  public void cleanup() throws ProcessingException {
    BEANS.get(ITestingJPAService.class).deleteAccount(getForm().getAccountNr());
  }

  @Test(expected = VetoException.class)
  public void testSamePassword() throws Exception {
    AccountForm form = new AccountForm();
    form.startNew();
    FormTestUtility.fillFormFields(form);
    form.getPasswordField().setValue("123456A");
    form.getRepeatPasswordField().setValue("123456B");
    form.doOk();
  }

  @Test
  public void testUsernameTooShort() throws Exception {
    AccountForm form = new AccountForm();
    form.startNew();
    FormTestUtility.fillFormFields(form);
    form.getPasswordField().setValue("ABCDEFG");
    form.getRepeatPasswordField().setValue("ABCDEFG");
    form.getUsernameField().setValue("12345");
    ScoutClientAssert.assertInvalid(form.getUsernameField());
    form.getUsernameField().setValue("123456");
    ScoutClientAssert.assertValid(form.getUsernameField());
    form.doClose();
  }

  @Test
  public void testPasswordTooShort() throws Exception {
    AccountForm form = new AccountForm();
    form.startNew();
    FormTestUtility.fillFormFields(form);
    form.getPasswordField().setValue("123456");
    ScoutClientAssert.assertInvalid(form.getPasswordField());
    form.getPasswordField().setValue("1234567");
    ScoutClientAssert.assertValid(form.getPasswordField());
    form.doClose();
  }

  @Test
  public void testEMailField() throws Exception {
    AccountForm form = new AccountForm();
    form.startNew();
    form.getEMailField().setValue("ABC");
    ScoutClientAssert.assertInvalid(form.getEMailField());
    form.getEMailField().setValue("adi.moser@gmail.com");
    ScoutClientAssert.assertValid(form.getEMailField());
  }

}
