package com.rtiming.client.result;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.data.basic.table.SortSpec;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.event.course.EventClassesTablePage;
import com.rtiming.client.result.RelayTableCustomizer.RelayTimeColumn;
import com.rtiming.client.test.data.EventWithRelayValidatedRaceTestDataProvider;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class RelayAfterLegResultsTablePageDataTest {

  private static EventWithRelayValidatedRaceTestDataProvider relay;
  private static EventClassesTablePage eventClass;

  @BeforeClass
  public static void before() throws ProcessingException {
    String[][] controlNos = new String[][]{
        new String[]{"31"},
        new String[]{"31"},
        new String[]{"31"}
        };
    String[][] punchNos = new String[][]{
        new String[]{"31"},
        new String[]{"31"},
        new String[]{"31"}
        };
    Integer[] start = new Integer[]{
        0,
        3600,
        7200
        };
    Integer[] finish = new Integer[]{
        3600,
        7200,
        10800
        };
    Integer[][] legTimes = new Integer[][]{
        new Integer[]{
            1800,
            1800,
            1800},
        new Integer[]{
            1800,
            1800,
            1800},
        new Integer[]{
            1800,
            1800,
            1800}
        };
    relay = new EventWithRelayValidatedRaceTestDataProvider(controlNos, punchNos, start, finish, legTimes);

    eventClass = new EventClassesTablePage(relay.getEventNr(), relay.getParentUid());
    SortSpec spec = new SortSpec(eventClass.getTable().getSortCodeColumn().getColumnIndex(), true);
    eventClass.getTable().getColumnSet().setSortSpec(spec);
    eventClass.loadChildren();
  }

  @Test
  public void testFirstStageResults() throws Exception {
    RelayAfterLegResultsTablePage page = new RelayAfterLegResultsTablePage(relay.getEventNr(), relay.getParentUid(), relay.getLegUid(0));
    page.nodeAddedNotify();
    page.loadChildren();

    ITableRow row1 = relay.getEntry().getForm().getRacesField().getTable().getLegColumn().findRow(eventClass.getTable().getClazzColumn().getValue(0));

    Assert.assertEquals("2 Rows", 2, page.getTable().getRowCount());
    Assert.assertNull("Summary Row: No Race", page.getTable().getRaceNrColumn().getValue(0));
    Assert.assertEquals("Race on Leg Row", relay.getEntry().getForm().getRacesField().getTable().getRaceNrColumn().getValue(row1), page.getTable().getRaceNrColumn().getValue(1));
    Assert.assertEquals("Leg 1 Time", "1:00:00", page.getTable().getTimeColumn().getValue(1));
    Assert.assertEquals("Relay Time", "1:00:00", page.getTable().getColumnSet().getColumnByClass(RelayTimeColumn.class).getValue(0));
  }

  @Test
  public void testSecondStageResults() throws Exception {
    RelayAfterLegResultsTablePage page = new RelayAfterLegResultsTablePage(relay.getEventNr(), relay.getParentUid(), relay.getLegUid(1));
    page.nodeAddedNotify();
    page.loadChildren();

    ITableRow row1 = relay.getEntry().getForm().getRacesField().getTable().getLegColumn().findRow(eventClass.getTable().getClazzColumn().getValue(0));
    ITableRow row2 = relay.getEntry().getForm().getRacesField().getTable().getLegColumn().findRow(eventClass.getTable().getClazzColumn().getValue(1));

    Assert.assertEquals("3 Rows", 3, page.getTable().getRowCount());
    Assert.assertNull("Summary Row: No Race", page.getTable().getRaceNrColumn().getValue(0));
    Assert.assertEquals("Race on Leg Row", relay.getEntry().getForm().getRacesField().getTable().getRaceNrColumn().getValue(row1), page.getTable().getRaceNrColumn().getValue(1));
    Assert.assertEquals("Race on Leg Row", relay.getEntry().getForm().getRacesField().getTable().getRaceNrColumn().getValue(row2), page.getTable().getRaceNrColumn().getValue(2));
    Assert.assertEquals("Leg 1 Time", "1:00:00", page.getTable().getTimeColumn().getValue(1));
    Assert.assertEquals("Leg 2 Time", "1:00:00", page.getTable().getTimeColumn().getValue(2));
    Assert.assertEquals("Relay Time", "2:00:00", page.getTable().getColumnSet().getColumnByClass(RelayTimeColumn.class).getValue(0));
  }

  @Test
  public void testThirdStageResults() throws Exception {
    RelayAfterLegResultsTablePage page = new RelayAfterLegResultsTablePage(relay.getEventNr(), relay.getParentUid(), relay.getLegUid(2));
    page.nodeAddedNotify();
    page.loadChildren();

    ITableRow row1 = relay.getEntry().getForm().getRacesField().getTable().getLegColumn().findRow(eventClass.getTable().getClazzColumn().getValue(0));
    ITableRow row2 = relay.getEntry().getForm().getRacesField().getTable().getLegColumn().findRow(eventClass.getTable().getClazzColumn().getValue(1));
    ITableRow row3 = relay.getEntry().getForm().getRacesField().getTable().getLegColumn().findRow(eventClass.getTable().getClazzColumn().getValue(2));

    Assert.assertEquals("4 Rows", 4, page.getTable().getRowCount());
    Assert.assertNull("Summary Row: No Race", page.getTable().getRaceNrColumn().getValue(0));
    Assert.assertEquals("Race on Leg Row", relay.getEntry().getForm().getRacesField().getTable().getRaceNrColumn().getValue(row1), page.getTable().getRaceNrColumn().getValue(1));
    Assert.assertEquals("Race on Leg Row", relay.getEntry().getForm().getRacesField().getTable().getRaceNrColumn().getValue(row2), page.getTable().getRaceNrColumn().getValue(2));
    Assert.assertEquals("Race on Leg Row", relay.getEntry().getForm().getRacesField().getTable().getRaceNrColumn().getValue(row3), page.getTable().getRaceNrColumn().getValue(3));
    Assert.assertEquals("Leg 1 Time", "1:00:00", page.getTable().getTimeColumn().getValue(1));
    Assert.assertEquals("Leg 2 Time", "1:00:00", page.getTable().getTimeColumn().getValue(2));
    Assert.assertEquals("Leg 3 Time", "1:00:00", page.getTable().getTimeColumn().getValue(3));
    Assert.assertEquals("Relay Time", "3:00:00", page.getTable().getColumnSet().getColumnByClass(RelayTimeColumn.class).getValue(0));
  }

  @AfterClass
  public static void after() throws ProcessingException {
    if (relay != null) {
      relay.remove();
    }
  }

}
