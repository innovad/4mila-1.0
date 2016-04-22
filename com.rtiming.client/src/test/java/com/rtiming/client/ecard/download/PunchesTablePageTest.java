package com.rtiming.client.ecard.download;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.desktop.DesktopEvent;
import org.eclipse.scout.rt.client.ui.desktop.DesktopListener;
import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ecard.download.PunchesTablePage.Table.DeleteMenu;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class PunchesTablePageTest extends AbstractTablePageTest<PunchesTablePage> {

  private EventWithIndividualValidatedRaceTestDataProvider event;

  @Before
  public void before() throws ProcessingException {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31", "32", "33"}, new String[]{"31", "32", "33"});
  }

  @Override
  protected PunchesTablePage getTablePage() throws ProcessingException {
    return new PunchesTablePage(event.getPunchSessionNr());
  }

  @Test
  public void testDeleteMenuMultiSelect() throws Exception {
    PunchesTablePage page = new PunchesTablePage(event.getPunchSessionNr());
    page.loadChildren();

    Assert.assertEquals(3, page.getTable().getRowCount());

    page.getTable().selectRow(page.getTable().getRow(1));
    page.getTable().selectRow(page.getTable().getRow(2), true);

    testDeleteMenu(page);

    page.loadChildren();
    Assert.assertEquals(1, page.getTable().getRowCount());
  }

  @Test
  public void testDeleteMenuSingleSelect() throws Exception {
    PunchesTablePage page = new PunchesTablePage(event.getPunchSessionNr());
    page.loadChildren();

    Assert.assertEquals(3, page.getTable().getRowCount());

    page.getTable().selectRow(page.getTable().getRow(1));

    testDeleteMenu(page);

    page.loadChildren();
    Assert.assertEquals(2, page.getTable().getRowCount());
  }

  private void testDeleteMenu(PunchesTablePage page) throws ProcessingException {
    DesktopListener listener = new DesktopListener() {
      @Override
      public void desktopChanged(DesktopEvent e) {
        if (e.getType() == DesktopEvent.TYPE_MESSAGE_BOX_ADDED) {
          IMessageBox box = e.getMessageBox();
          box.getUIFacade().setResultFromUI(IMessageBox.YES_OPTION);
        }
      }
    };
    ClientSession.get().getDesktop().addDesktopListener(listener);

    boolean run = page.getTable().runMenu(DeleteMenu.class);
    Assert.assertTrue(run);

    ClientSession.get().getDesktop().removeDesktopListener(listener);
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

}
