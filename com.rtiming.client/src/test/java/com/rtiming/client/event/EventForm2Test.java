package com.rtiming.client.event;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.ClipboardOwner;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.BeanMetaData;
import org.eclipse.scout.rt.platform.IBean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.shared.services.common.code.ICodeService;
import org.eclipse.scout.rt.testing.shared.TestingUtility;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.ecard.download.PunchingSystemCodeType;
import com.rtiming.shared.event.IEventProcessService;

/**
 * @author amo
 */
public class EventForm2Test {

  @SuppressWarnings("rawtypes")
  private List<IBean<?>> registration = null;
  private IEventProcessService eventService;

  @Before
  public void before() throws ProcessingException {
    // TODO MIG IExceptionHandlerService exceptionService = Mockito.mock(IExceptionHandlerService.class);
    ICodeService codeService = Mockito.mock(ICodeService.class);
    IClientSession session = Mockito.mock(IClientSession.class);
    eventService = Mockito.mock(IEventProcessService.class);
    registration = TestingUtility.registerBeans(new BeanMetaData(IEventProcessService.class, eventService), new BeanMetaData(ICodeService.class, codeService));
    // TODO MIG ClientSessionThreadLocal.set(session);
  }

  @Test
  public void testFormDefault() throws Exception {
    EventBean eventBean = new EventBean();
    eventBean.setPunchingSystemUid(PunchingSystemCodeType.SportIdentCode.ID);
    Mockito.when(eventService.prepareCreate(Mockito.any(EventBean.class))).thenReturn(eventBean);

    EventForm form = new EventForm();
    form.startNew();

    Assert.assertEquals("E-Punching Default", PunchingSystemCodeType.SportIdentCode.ID, form.getPunchingSystemField().getValue().longValue());
  }

  @Test
  public void testPasteFromClipboard() throws Exception {
    EventBean eventBean = new EventBean();
    Mockito.when(eventService.prepareCreate(Mockito.any(EventBean.class))).thenReturn(eventBean);

    URL url = null;
    try {
      url = FMilaUtility.findFileLocation("resources//logo//logo.png", "");
    }
    catch (ProcessingException e) {
      Assert.fail(e.getMessage());
      e.printStackTrace();
    }
    final URL finalUrl = url;

    EventForm form = new EventForm();
    form.startNew();

    Transferable contents = new Transferable() {

      @Override
      public boolean isDataFlavorSupported(DataFlavor flavor) {
        return true;
      }

      @Override
      public DataFlavor[] getTransferDataFlavors() {
        return null;
      }

      @Override
      public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException {
        List<File> list = new ArrayList<File>();
        File image = new File(finalUrl.getFile());
        list.add(image);
        return list;
      }
    };
    ClipboardOwner owner = new ClipboardOwner() {
      @Override
      public void lostOwnership(Clipboard clipboard, Transferable contents) {
      }
    };
    Toolkit.getDefaultToolkit().getSystemClipboard().setContents(contents, owner);

    form.getLogoField().getPasteFromClipboardButton().doClick();
    Assert.assertTrue(form.getLogoField().getImageField().getImage() instanceof byte[]);
    Assert.assertEquals(((byte[]) form.getLogoField().getImageField().getImage()).length, IOUtility.getContent(finalUrl.getFile()).length);
  }

  @After
  public void after() throws ProcessingException {
    TestingUtility.unregisterBeans(registration);
    // TODO MIG ClientSessionThreadLocal.set(null);
  }

}
