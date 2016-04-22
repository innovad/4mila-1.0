package com.rtiming.client.result;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
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
import com.rtiming.client.race.RaceForm;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.ClubTestDataProvider;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class ResultsTablePageTest extends AbstractTablePageTest<ResultsTablePage> {

  private static EventWithIndividualValidatedRaceTestDataProvider event;
  private static EntryTestDataProvider entry2;
  private static EntryTestDataProvider entry3;
  private static ClubTestDataProvider club;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    String[] controlNos = new String[]{"31"};
    String[] punchNos = new String[]{"31"};
    club = new ClubTestDataProvider();
    event = new EventWithIndividualValidatedRaceTestDataProvider(controlNos, punchNos);
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());
    entry2 = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    // make sure entry2 is same club
    RaceForm race2 = new RaceForm();
    race2.setRaceNr(entry2.getRaceNr());
    race2.startModify();
    race2.getClubField().setValue(event.getClubNr());
    race2.getManualStatusField().setValue(true);
    race2.getRaceStatusField().setValue(RaceStatusCodeType.DisqualifiedCode.ID);
    race2.doOk();
    entry3 = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    // make sure entry3 is NOT same club
    RaceForm race3 = new RaceForm();
    race3.setRaceNr(entry3.getRaceNr());
    race3.startModify();
    race3.getClubField().setValue(null);
    race3.getManualStatusField().setValue(true);
    race3.getRaceStatusField().setValue(RaceStatusCodeType.DisqualifiedCode.ID);
    race3.doOk();
  }

  @Override
  protected ResultsTablePage getTablePage() {
    return new ResultsTablePage(ClientSession.get().getSessionClientNr(), event.getClassUid(), null, null);
  }

  @Test
  public void testClubResults() throws Exception {
    Long clubNr = event.getClubNr();
    Assert.assertNotNull("Club should be set", clubNr);
    ResultsTablePage page = new ResultsTablePage(ClientSession.get().getSessionClientNr(), null, null, clubNr);
    page.nodeAddedNotify();
    page.loadChildren();
    Assert.assertEquals("2 Rows", 2, page.getTable().getRowCount());
    Assert.assertEquals("Rank 1", 1, page.getTable().getRankColumn().getValue(0).longValue());
    Assert.assertNull("Rank 2", page.getTable().getRankColumn().getValue(1));
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    event.remove();
    club.remove();
  }

}
