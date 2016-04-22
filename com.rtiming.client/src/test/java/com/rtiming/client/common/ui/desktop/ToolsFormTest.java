package com.rtiming.client.common.ui.desktop;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.test.FMilaClientTestUtility;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class ToolsFormTest {

  @Test
  public void testForm() throws Exception {
    ToolsForm form = new ToolsForm();
    form.startForm();
    form.doClose();
  }

  @Test
  public void testGetters() throws Exception {
    FMilaClientTestUtility.testFormFields(new ToolsForm());
  }

  @Test
  public void testEntryLink() throws Exception {
    ToolsForm form = new ToolsForm();
    form.startForm();

    Runnable runnableAfterButtonClick = new Runnable() {
      @Override
      public void run() {
        EntryForm form = FMilaClientTestUtility.findLastAddedForm(EntryForm.class);
        try {
          form.doCancel();
        }
        catch (ProcessingException e) {
          e.printStackTrace();
        }
      }
    };
    FMilaClientTestUtility.clickBlockingButton(form.getNewEntryButton(), runnableAfterButtonClick);

    form.doClose();
  }

}
