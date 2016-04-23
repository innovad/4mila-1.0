package com.rtiming.client.event;

import java.awt.Toolkit;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.imageio.ImageIO;

import org.eclipse.scout.rt.client.ui.basic.filechooser.IFileChooser;
import org.eclipse.scout.rt.client.ui.desktop.DesktopEvent;
import org.eclipse.scout.rt.client.ui.desktop.DesktopListener;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.event.EventForm.MainBox.FinishTimeField;
import com.rtiming.client.event.EventForm.MainBox.ZeroTimeField;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.FormTestUtility.OrderedFieldPair;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.event.IEventProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class EventFormTest extends AbstractFormTest<EventForm> {

  @Override
  protected EventForm getStartedForm() throws ProcessingException {
    EventForm form = new EventForm();
    form.startNew();
    return form;
  }

  @Override
  protected EventForm getModifyForm() throws ProcessingException {
    EventForm form = new EventForm();
    form.setEventNr(getForm().getEventNr());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    BEANS.get(IEventProcessService.class).delete(getForm().getEventNr(), true);
  }

  @Override
  protected ArrayList<OrderedFieldPair> getOrderedFieldPairs() {
    ArrayList<OrderedFieldPair> result = new ArrayList<OrderedFieldPair>();
    OrderedFieldPair pair = new OrderedFieldPair(ZeroTimeField.class, FinishTimeField.class);
    result.add(pair);
    return result;
  }

  @Test
  public void testLogo() throws Exception {
    URL url = FMilaUtility.findFileLocation("resources//logo//logo.png", "");
    BinaryResource binaryResource = FMilaUtility.createBinaryResource(url.getPath());

    DesktopListener listener = new DesktopListener() {
      @Override
      public void desktopChanged(DesktopEvent e) {
        if (e.getType() == DesktopEvent.TYPE_FILE_CHOOSER_SHOW) {
          IFileChooser chooser = e.getFileChooser();
          chooser.setFiles(Arrays.asList(new BinaryResource[]{binaryResource}));
        }
      }
    };
    ClientSession.get().getDesktop().addDesktopListener(listener);

    EventForm event = getForm();
    event.getLogoField().getFileChooserButton().doClick();

    ClientSession.get().getDesktop().removeDesktopListener(listener);

    Assert.assertEquals("png", event.getLogoField().getImageFormat());
    byte[] original = binaryResource.getContent();
    Assert.assertEquals(original.length, ((byte[]) event.getLogoField().getImageField().getImage()).length);
  }

  @Test
  public void testClipboardFile() throws Exception {
    URL url = FMilaUtility.findFileLocation("resources//logo//logo.png", "");
    final File file = new File(url.getPath());

    Transferable contents = new Transferable() {
      @Override
      public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.javaFileListFlavor.equals(flavor);
      }

      @Override
      public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.javaFileListFlavor};
      }

      @Override
      public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        ArrayList<File> list = new ArrayList<File>();
        list.add(file);
        return list;
      }
    };

    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents, null);

    EventForm event = getForm();
    event.getLogoField().getPasteFromClipboardButton().doClick();

    Assert.assertEquals("png", event.getLogoField().getImageFormat());
    byte[] original = IOUtility.getContent(file.getAbsolutePath());
    Assert.assertEquals(original.length, ((byte[]) event.getLogoField().getImageField().getImage()).length);
  }

  @Test
  public void testClipboardImage() throws Exception {
    URL url = FMilaUtility.findFileLocation("resources//logo//logo.png", "");
    final File file = new File(url.getPath());

    Transferable contents = new Transferable() {
      @Override
      public boolean isDataFlavorSupported(DataFlavor flavor) {
        return DataFlavor.imageFlavor.equals(flavor);
      }

      @Override
      public DataFlavor[] getTransferDataFlavors() {
        return new DataFlavor[]{DataFlavor.imageFlavor};
      }

      @Override
      public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        if (DataFlavor.imageFlavor.equals(flavor)) {
          return ImageIO.read(file);
        }
        throw new UnsupportedFlavorException(flavor);
      }
    };

    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents, null);

    EventForm event = getForm();
    event.getLogoField().getPasteFromClipboardButton().doClick();

    Assert.assertEquals("png", event.getLogoField().getImageFormat());
    byte[] original = IOUtility.getContent(file.getAbsolutePath());
    Assert.assertEquals(original.length / 1000, ((byte[]) event.getLogoField().getImageField().getImage()).length / 1000);
  }

  @Test
  public void testDnDFile() throws Exception {
// TODO MIG    
//    URL url = FMilaUtility.findFileLocation("resources//logo//logo.png", "");
//    final File file = new File(url.getPath());
//    TransferObject t = new FileListTransferObject(new File[]{file});
//
//    EventForm event = getForm();
//    event.getLogoField().getImageField().drop(t);
//
//    Assert.assertEquals("png", event.getLogoField().getImageFormat());
//    byte[] original = IOUtility.getContent(file.getAbsolutePath());
//    Assert.assertEquals(original.length, ((byte[]) event.getLogoField().getImageField().getImage()).length);
  }

  @Test(expected = VetoException.class)
  public void testDnDFileNotFound() throws Exception {
    final File file = new File("XYZ");
    Assert.assertFalse(file.exists());
// TODO MIG    
//    TransferObject t = new FileListTransferObject(new File[]{file});
//
//    EventForm event = getForm();
//    event.getLogoField().getImageField().drop(t);
  }

  @Test(expected = VetoException.class)
  public void testDnDFileWrongFormat() throws Exception {
    URL url = FMilaUtility.findFileLocation("resources//logo//empty.txt", "");
    final File file = new File(url.getPath());
    Assert.assertTrue(file.exists());
// TODO MIG    
//    TransferObject t = new FileListTransferObject(new File[]{file});
//
//    EventForm event = getForm();
//    event.getLogoField().getImageField().drop(t);
  }

  @Test
  public void testDnDImage() throws Exception {
    URL url = FMilaUtility.findFileLocation("resources//logo//logo.png", "");
    final File file = new File(url.getPath());
// TODO MIG    
//    TransferObject t = new ImageTransferObject(ImageIO.read(file));
//
//    EventForm event = getForm();
//    event.getLogoField().getImageField().drop(t);

//    Assert.assertEquals("jpeg", event.getLogoField().getImageFormat());
//    byte[] original = IOUtility.getContent(file.getAbsolutePath());
//    Assert.assertEquals(original.length / 1000, ((byte[]) event.getLogoField().getImageField().getImage()).length / 1000);
  }

}
