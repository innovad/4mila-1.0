package com.rtiming.client.ecard.download;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class DownloadedECardsTablePageDetailFormTest {

  private static EventWithIndividualValidatedRaceTestDataProvider event;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());
  }

  @Test
  public void test1() throws Exception {
    AbstractDownloadedECardsTablePage page = new DownloadedECardsAllTablePage();
    DownloadedECardsSearchForm downloadedECardsSearchForm = (DownloadedECardsSearchForm) page.getSearchFormInternal();
    downloadedECardsSearchForm.getResetButton().doClick();
    Assert.assertEquals("Event on Search Form", downloadedECardsSearchForm.getEventField().getValue(), event.getEventNr());
    page.nodeAddedNotify();
    page.getSearchFormInternal().start();
    page.loadChildren();

    RaceControlsTableForm form = (RaceControlsTableForm) page.getDetailForm();

    for (int k = 0; k < page.getTable().getRowCount(); k++) {
      System.out.println("EventNr " + k + ": " + page.getTable().getEventNrColumn().getValue(k));
      System.out.println("RaceNr " + k + ": " + page.getTable().getRaceNrColumn().getValue(k));
      System.out.println("PunchSessionNr " + k + ": " + page.getTable().getPunchSessionNrColumn().getValue(k));
    }

    Assert.assertEquals("1 Row", 1, page.getTable().getRowCount());
    Assert.assertEquals("0 Rows", 0, form.getRaceControlField().getTable().getRowCount());
    page.getTable().selectFirstRow();
    Assert.assertEquals("3 Rows", 3, form.getRaceControlField().getTable().getRowCount());
    Assert.assertEquals("RaceNr", event.getRaceNr(), form.getRaceControlField().getTable().getRaceNrColumn().getValue(0));
    page.getTable().deselectAllRows();
    Assert.assertEquals("0 Rows", 0, form.getRaceControlField().getTable().getRowCount());
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    event.remove();
  }

}
