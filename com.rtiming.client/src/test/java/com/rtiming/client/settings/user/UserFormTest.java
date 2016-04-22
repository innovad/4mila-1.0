package com.rtiming.client.settings.user;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.settings.user.UserForm.MainBox.PasswordField;
import com.rtiming.client.settings.user.UserForm.MainBox.RepeatPasswordField;
import com.rtiming.client.settings.user.UserForm.MainBox.UsernameField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.dao.RtUserKey;
import com.rtiming.shared.settings.user.IUserProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class UserFormTest extends AbstractFormTest<UserForm> {

  @Override
  protected UserForm getStartedForm() throws ProcessingException {
    UserForm form = new UserForm();
    form.startNew();
    return form;
  }

  @Override
  protected UserForm getModifyForm() throws ProcessingException {
    UserForm form = new UserForm();
    form.setUserNr(getForm().getUserNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    BEANS.get(IUserProcessService.class).delete(RtUserKey.create(getForm().getUserNr()));
  }

  @Override
  protected List<FieldValue> getFixedValues() {
    List<FieldValue> result = new ArrayList<FieldValue>();
    result.add(new FieldValue(PasswordField.class, "123456"));
    result.add(new FieldValue(RepeatPasswordField.class, "123456"));
    result.add(new FieldValue(UsernameField.class, "123"));
    return result;
  }

  @Override
  protected List<String> getFieldIdsToIgnore() {
    List<String> list = new ArrayList<String>();
    list.add(getForm().getPasswordField().getFieldId());
    list.add(getForm().getRepeatPasswordField().getFieldId());
    return list;
  }

  @Test
  public void testDefaultValue() throws Exception {
    UserForm user = new UserForm();
    user.startNew();

    Long languageUid = null;
    String language = Locale.getDefault().getLanguage();
    for (ICode<?> code : CODES.getCodeType(LanguageCodeType.class).getCodes()) {
      if (language.equalsIgnoreCase(code.getExtKey())) {
        languageUid = TypeCastUtility.castValue(code.getId(), Long.class);
      }
    }

    Assert.assertEquals(languageUid.longValue(), user.getLanguageField().getValue().longValue());
    user.doClose();
  }

  @Test(expected = VetoException.class)
  public void testRoleMandatory() throws Exception {
    UserForm form = getForm();
    form.getRolesField().uncheckAllKeys();
    form.touch();
    form.doOk();
  }

  @Test
  public void testUsernameTooShort() throws Exception {
    UserForm form = new UserForm();
    form.startNew();
    FormTestUtility.fillFormFields(form);
    form.getPasswordField().setValue("ABCDEF");
    form.getRepeatPasswordField().setValue("ABCDEF");
    form.getUsernameField().setValue("12");
    ScoutClientAssert.assertInvalid(form.getUsernameField());
    form.getUsernameField().setValue("123");
    ScoutClientAssert.assertValid(form.getUsernameField());
    form.doClose();
  }

  @Test
  public void testPasswordTooShort() throws Exception {
    UserForm form = new UserForm();
    form.startNew();
    FormTestUtility.fillFormFields(form);
    form.getPasswordField().setValue("12");
    ScoutClientAssert.assertInvalid(form.getPasswordField());
    form.getPasswordField().setValue("123");
    ScoutClientAssert.assertValid(form.getPasswordField());
    form.doClose();
  }

}
