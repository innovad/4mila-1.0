package com.rtiming.client.entry.startlist;

import java.util.Arrays;
import java.util.HashSet;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.desktop.DesktopEvent;
import org.eclipse.scout.rt.client.ui.desktop.DesktopListener;
import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.entry.startlist.StartlistSettingsTablePage.Table.DeleteMenu;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.entry.startlist.IStartlistService;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class StartlistSettingsTablePageDeletionTest {

  private EventWithIndividualClassTestDataProvider event;

  @Test
  public void testDelete() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();

    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    form.getOptionsField().setValue(new HashSet<>(Arrays.asList(new Long[]{StartlistSettingOptionCodeType.AllowStarttimeWishesCode.ID})));
    form.getVacantAbsoluteField().setValue(10L);
    form.doOk();

    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("1 Row", 1, page.getTable().getRowCount());
    BEANS.get(IStartlistService.class).createStartlists(new Long[]{page.getTable().getStartlistSettingNrColumn().getValue(0)});

    DesktopListener listener = new DesktopListener() {
      @Override
      public void desktopChanged(DesktopEvent e) {
        if (e.getType() == DesktopEvent.TYPE_MESSAGE_BOX_SHOW) {
          IMessageBox box = e.getMessageBox();
          box.getUIFacade().setResultFromUI(IMessageBox.YES_OPTION);
        }
      }
    };
    ClientSession.get().getDesktop().addDesktopListener(listener);

    Assert.assertEquals("Class", event.getClassUid(), page.getTable().getClazzUidColumn().getValue(0));
    page.getTable().selectFirstRow();
    boolean run = page.getTable().runMenu(DeleteMenu.class);
    Assert.assertTrue(run);
    ClientSession.get().getDesktop().removeDesktopListener(listener);
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
  }

}
