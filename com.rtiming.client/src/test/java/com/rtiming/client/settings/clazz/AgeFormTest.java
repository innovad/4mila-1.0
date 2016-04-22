package com.rtiming.client.settings.clazz;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.settings.CodeForm;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.clazz.IAgeProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class AgeFormTest extends AbstractFormTest<AgeForm> {

  private CodeForm clazz;

  @Override
  public void setUpForm() throws ProcessingException {
    clazz = new CodeForm();
    clazz.setCodeType(ClassCodeType.ID);
    clazz.startNew();
    FormTestUtility.fillFormFields(clazz, new MaxFormFieldValueProvider());
    clazz.doOk();
    super.setUpForm();
  }

  @Override
  protected AgeForm getStartedForm() throws ProcessingException {
    AgeForm form = new AgeForm();
    form.getClazzField().setValue(clazz.getCodeUid());
    form.startNew();
    return form;
  }

  @Override
  protected AgeForm getModifyForm() throws ProcessingException {
    AgeForm form = new AgeForm();
    form.getClazzField().setValue(clazz.getCodeUid());
    form.setKey(getForm().getKey());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    if (getForm().getKey() != null) {
      BEANS.get(IAgeProcessService.class).delete(getForm().getKey());
    }

    CodeFormData formData = new CodeFormData();
    formData.setCodeUid(clazz.getCodeUid());
    formData.setCodeType(ClassCodeType.ID);
    BEANS.get(ICodeProcessService.class).deleteCodeBox(formData.getMainBox());
  }

}
