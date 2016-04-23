package com.rtiming.client.entry.startblock;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.ui.fields.AbstractCodeBox;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.shared.common.AbstractCodeBoxData;
import com.rtiming.shared.entry.startblock.StartblockCodeType;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class StartblockFormTest extends AbstractFormTest<StartblockForm> {

  @Override
  protected StartblockForm getStartedForm() throws ProcessingException {
    StartblockForm form = new StartblockForm();
    form.startNew();
    return form;
  }

  @Override
  protected StartblockForm getModifyForm() throws ProcessingException {
    StartblockForm form = new StartblockForm();
    form.setStartblockUid(getForm().getStartblockUid());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    CodeFormData formData = new CodeFormData();
    formData.setCodeType(ClassCodeType.ID);
    formData.setCodeUid(getForm().getStartblockUid());
    BEANS.get(ICodeProcessService.class).deleteCodeBox(formData.getMainBox());
  }

  @Test
  public void testFind() throws ProcessingException {
    FormTestUtility.fillFormFields(getForm());
    getForm().getMainBox().getFieldByClass(AbstractCodeBox.ShortcutField.class).setValue("XYZ");
    getForm().doOk();

    CodeFormData formData = BEANS.get(ICodeProcessService.class).find("XYZ", StartblockCodeType.ID);
    Assert.assertEquals(getForm().getStartblockUid(), formData.getCodeUid());
    Assert.assertEquals("XYZ", formData.getMainBox().getFieldByClass(AbstractCodeBoxData.Shortcut.class).getValue());
  }
}
