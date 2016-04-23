package com.rtiming.client.common.exception;

import java.io.File;
import java.io.FileReader;
import java.util.Arrays;

import org.eclipse.scout.rt.client.ui.basic.filechooser.IFileChooser;
import org.eclipse.scout.rt.client.ui.desktop.DesktopEvent;
import org.eclipse.scout.rt.client.ui.desktop.DesktopListener;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.shared.FMilaUtility;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class ExceptionFormTest {

  @Test
  public void testGetters() throws Exception {
    FMilaClientTestUtility.testFormFields(new ExceptionForm());
  }

  @Test
  public void testForm() throws Exception {
    ExceptionForm form = new ExceptionForm();
    form.startDefault();
    form.doClose();
  }

  @Test
  public void testExport() throws Exception {
    final String file = IOUtility.getTempFileName(".txt");

    DesktopListener listener = new DesktopListener() {
      @Override
      public void desktopChanged(DesktopEvent e) {
        if (e.getType() == DesktopEvent.TYPE_FILE_CHOOSER_SHOW) {
          IFileChooser chooser = e.getFileChooser();
          chooser.setFiles(Arrays.asList(new BinaryResource[]{FMilaUtility.createBinaryResource(file)}));
        }
      }
    };
    ClientSession.get().getDesktop().addDesktopListener(listener);

    ExceptionForm form = new ExceptionForm();
    form.startDefault();
    form.getMessageField().setValue("ABC");
    form.getSaveButton().doClick();
    form.doClose();

    String result = IOUtility.getContent(new FileReader(new File(file)));
    Assert.assertEquals("Export correct", "ABC", result);

    ClientSession.get().getDesktop().removeDesktopListener(listener);
  }

}
