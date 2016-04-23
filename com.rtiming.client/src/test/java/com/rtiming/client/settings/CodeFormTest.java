package com.rtiming.client.settings;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class CodeFormTest extends AbstractFormTest<CodeForm> {

  @Override
  protected CodeForm getStartedForm() throws ProcessingException {
    CodeForm form = new CodeForm();
    form.setCodeType(ClassCodeType.ID);
    form.startNew();
    return form;
  }

  @Override
  protected CodeForm getModifyForm() throws ProcessingException {
    CodeForm form = new CodeForm();
    form.setCodeUid(getForm().getCodeUid());
    form.setCodeType(ClassCodeType.ID);
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    CodeFormData formData = new CodeFormData();
    formData.setCodeType(ClassCodeType.ID);
    formData.setCodeUid(getForm().getCodeUid());
    BEANS.get(ICodeProcessService.class).deleteCodeBox(formData.getMainBox());
  }

}
