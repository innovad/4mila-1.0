package com.rtiming.client.race;

import static com.rtiming.client.event.course.loop.CourseLoopCalculatorTestUtility.buildControl;
import static com.rtiming.client.event.course.loop.CourseLoopCalculatorTestUtility.buildCourse;
import static com.rtiming.client.event.course.loop.CourseLoopCalculatorTestUtility.buildPunch;

import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.TestClientSession;
import com.rtiming.client.event.course.ReplacementControlForm;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.ControlTestData;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.IControlProcessService;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestClientSession.class)
public class RaceControlsTablePageTest extends AbstractTablePageTest<RaceControlsTablePage> {

  private EventWithIndividualValidatedRaceTestDataProvider event;
  private static CurrencyTestDataProvider currency;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    currency = new CurrencyTestDataProvider();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    currency.remove();
  }

  @Override
  protected RaceControlsTablePage getTablePage() {
    return new RaceControlsTablePage(ClientSession.get().getSessionClientNr(), (Long[]) null);
  }

  @Test
  public void testTimeFormatting() throws Exception {
    String[] controlNos = new String[]{"31", "32", "33", "34"};
    Integer[] legTimes = new Integer[]{1, 61, 3600, 7200};
    event = new EventWithIndividualValidatedRaceTestDataProvider(controlNos, controlNos, 0, 7250, legTimes);

    RaceControlsTablePage page = new RaceControlsTablePage(event.getRaceNr());
    page.getTable().resetColumnSortOrder();
    page.loadChildren();

    // no
    Assert.assertEquals("31", page.getTable().getControlColumn().getValue(1));
    Assert.assertEquals("32", page.getTable().getControlColumn().getValue(2));
    Assert.assertEquals("33", page.getTable().getControlColumn().getValue(3));
    Assert.assertEquals("34", page.getTable().getControlColumn().getValue(4));

    // leg times
    Assert.assertEquals("0:00", page.getTable().getLegTimeColumn().getValue(0));
    Assert.assertEquals("0:01", page.getTable().getLegTimeColumn().getValue(1));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(2));
    Assert.assertEquals("58:59", page.getTable().getLegTimeColumn().getValue(3));
    Assert.assertEquals("1:00:00", page.getTable().getLegTimeColumn().getValue(4));
    Assert.assertEquals("0:50", page.getTable().getLegTimeColumn().getValue(5));

    // times
    Assert.assertEquals("0:00", page.getTable().getRelativeTimeColumn().getValue(0));
    Assert.assertEquals("0:01", page.getTable().getRelativeTimeColumn().getValue(1));
    Assert.assertEquals("1:01", page.getTable().getRelativeTimeColumn().getValue(2));
    Assert.assertEquals("1:00:00", page.getTable().getRelativeTimeColumn().getValue(3));
    Assert.assertEquals("2:00:00", page.getTable().getRelativeTimeColumn().getValue(4));
    Assert.assertEquals("2:00:50", page.getTable().getRelativeTimeColumn().getValue(5));

    // status
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(0).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(1).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(2).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(3).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(4).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(5).longValue());

    assertRaceStatus(event.getRaceNr(), RaceStatusCodeType.OkCode.ID);
  }

  @Test
  public void testReplacementControlIndividualControls() throws Exception {
    String[] controlNos = new String[]{"31", "32"};
    String[] punchNos = new String[]{"31", "33"};
    Integer[] legTimes = new Integer[]{1, 61};
    event = new EventWithIndividualValidatedRaceTestDataProvider(controlNos, punchNos, 0, 7250, legTimes);
    doReplacmentControlTest();
  }

  @Test
  public void testReplacementControlCourseTemplate() throws Exception {
    List<ControlTestData> course = buildCourse(
        buildControl("S", 1L, null, null, null, null),
        buildControl("31", 1L, null, null, null, null),
        buildControl("32", 2L, null, null, null, null),
        buildControl("Z", 3L, null, null, null, null)
        );
    List<ControlTestData> punch = buildCourse(
        buildPunch("S"),
        buildPunch("31"),
        buildPunch("33"),
        buildPunch("Z")
        );
    Integer[] legTimes = new Integer[]{0, 1, 61, 500};
    event = new EventWithIndividualValidatedRaceTestDataProvider(course, punch, 0, 7250, legTimes, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    doReplacmentControlTest();
  }

  private void doReplacmentControlTest() throws ProcessingException {
    RaceControlsTablePage page = new RaceControlsTablePage(event.getRaceNr());
    page.getTable().resetColumnSortOrder();
    page.loadChildren();

    // no
    Assert.assertEquals("S", page.getTable().getControlColumn().getValue(0));
    Assert.assertEquals("31", page.getTable().getControlColumn().getValue(1));
    Assert.assertEquals("32", page.getTable().getControlColumn().getValue(2));
    Assert.assertEquals("Z", page.getTable().getControlColumn().getValue(3));
    Assert.assertEquals("33", page.getTable().getControlColumn().getValue(4));

    // status
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(0).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(1).longValue());
    Assert.assertEquals(ControlStatusCodeType.MissingCode.ID, page.getTable().getControlStatusColumn().getValue(2).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(3).longValue());
    Assert.assertEquals(ControlStatusCodeType.WrongCode.ID, page.getTable().getControlStatusColumn().getValue(4).longValue());

    assertRaceStatus(event.getRaceNr(), RaceStatusCodeType.MissingPunchCode.ID);

    ControlFormData control32 = BEANS.get(IControlProcessService.class).find("32", event.getEventNr());
    ControlFormData control33 = BEANS.get(IControlProcessService.class).find("33", event.getEventNr());

    ReplacementControlForm replacement = new ReplacementControlForm();
    replacement.getEventField().setValue(event.getEventNr());
    replacement.getControlField().setValue(control32.getControlNr());
    replacement.startNew();
    replacement.getReplacementControlField().setValue(control33.getControlNr());
    replacement.doOk();

    BEANS.get(IRaceService.class).validateAndPersistRace(event.getRaceNr());

    page = new RaceControlsTablePage(event.getRaceNr());
    page.nodeAddedNotify();
    page.getTable().resetColumnSortOrder();
    page.loadChildren();

    // no
    Assert.assertEquals("S", page.getTable().getControlColumn().getValue(0));
    Assert.assertEquals("31", page.getTable().getControlColumn().getValue(1));
    Assert.assertEquals("32", page.getTable().getControlColumn().getValue(2));
    Assert.assertEquals("Z", page.getTable().getControlColumn().getValue(3));

    // status
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(0).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(1).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(2).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(3).longValue());

    assertRaceStatus(event.getRaceNr(), RaceStatusCodeType.OkCode.ID);
  }

  @Test
  public void testOver24h() throws Exception {
    String[] controlNos = new String[]{"31"};
    Integer[] legTimes = new Integer[]{3600 * 50 + 3};
    event = new EventWithIndividualValidatedRaceTestDataProvider(controlNos, controlNos, 3600 * 24, 3600 * 99 + 4, legTimes);

    RaceControlsTablePage page = new RaceControlsTablePage(event.getRaceNr());
    page.getTable().resetColumnSortOrder();
    page.loadChildren();

    // no
    Assert.assertEquals("31", page.getTable().getControlColumn().getValue(1));

    // leg times
    Assert.assertEquals("0:00", page.getTable().getLegTimeColumn().getValue(0));
    Assert.assertEquals("50:00:03", page.getTable().getLegTimeColumn().getValue(1));
    Assert.assertEquals("25:00:01", page.getTable().getLegTimeColumn().getValue(2));

    // times
    Assert.assertEquals("0:00", page.getTable().getRelativeTimeColumn().getValue(0));
    Assert.assertEquals("50:00:03", page.getTable().getRelativeTimeColumn().getValue(1));
    Assert.assertEquals("75:00:04", page.getTable().getRelativeTimeColumn().getValue(2));

    // status
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(0).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(1).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(2).longValue());

    assertRaceStatus(event.getRaceNr(), RaceStatusCodeType.OkCode.ID);
  }

  @Test
  public void testControlWithWrongTime() throws Exception {
    String[] controlNos = new String[]{"31", "32", "33", "34"};
    Integer[] legTimes = new Integer[]{60, 120, 90, 240};
    event = new EventWithIndividualValidatedRaceTestDataProvider(controlNos, controlNos, 0, 300, legTimes);

    RaceControlsTablePage page = new RaceControlsTablePage(event.getRaceNr());
    page.getTable().resetColumnSortOrder();
    page.loadChildren();

    // leg times
    Assert.assertEquals("0:00", page.getTable().getLegTimeColumn().getValue(0));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(1)); // 60 - 0
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(2)); // 120 - 60
    Assert.assertNull(page.getTable().getLegTimeColumn().getValue(3)); // to time because it would be negative: 90 - 120
    Assert.assertEquals("2:30", page.getTable().getLegTimeColumn().getValue(4)); // 240 - 90
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(5)); // 300 - 240

    // times
    Assert.assertEquals("0:00", page.getTable().getRelativeTimeColumn().getValue(0)); // 0
    Assert.assertEquals("1:00", page.getTable().getRelativeTimeColumn().getValue(1)); // 60
    Assert.assertEquals("2:00", page.getTable().getRelativeTimeColumn().getValue(2)); // 120
    Assert.assertEquals("1:30", page.getTable().getRelativeTimeColumn().getValue(3)); // 90
    Assert.assertEquals("4:00", page.getTable().getRelativeTimeColumn().getValue(4)); // 240
    Assert.assertEquals("5:00", page.getTable().getRelativeTimeColumn().getValue(5)); // 300

    assertRaceStatus(event.getRaceNr(), RaceStatusCodeType.OkCode.ID);
  }

  @Test
  public void testMissingControl() throws Exception {
    String[] controlNos = new String[]{"31", "32", "33", "34"};
    String[] punchNos = new String[]{"31", "32", "34"};
    Integer[] legTimes = new Integer[]{60, 120, 180};
    event = new EventWithIndividualValidatedRaceTestDataProvider(controlNos, punchNos, 0, 240, legTimes);

    RaceControlsTablePage page = new RaceControlsTablePage(event.getRaceNr());
    page.getTable().resetColumnSortOrder();
    page.loadChildren();

    // leg times
    Assert.assertEquals("0:00", page.getTable().getLegTimeColumn().getValue(0));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(1));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(2));
    Assert.assertNull(page.getTable().getLegTimeColumn().getValue(3));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(4));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(5));

    // times
    Assert.assertEquals("0:00", page.getTable().getRelativeTimeColumn().getValue(0));
    Assert.assertEquals("1:00", page.getTable().getRelativeTimeColumn().getValue(1));
    Assert.assertEquals("2:00", page.getTable().getRelativeTimeColumn().getValue(2));
    Assert.assertNull(page.getTable().getRelativeTimeColumn().getValue(3));
    Assert.assertEquals("3:00", page.getTable().getRelativeTimeColumn().getValue(4));
    Assert.assertEquals("4:00", page.getTable().getRelativeTimeColumn().getValue(5));

    // status
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(0).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(1).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(2).longValue());
    Assert.assertEquals(ControlStatusCodeType.MissingCode.ID, page.getTable().getControlStatusColumn().getValue(3).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(4).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(5).longValue());

    assertRaceStatus(event.getRaceNr(), RaceStatusCodeType.MissingPunchCode.ID);
  }

  @Test
  public void testWrongOrder() throws Exception {
    String[] controlNos = new String[]{"31", "32", "33", "34"};
    String[] punchNos = new String[]{"31", "33", "32", "34"};
    Integer[] legTimes = new Integer[]{60, 120, 180, 240};
    event = new EventWithIndividualValidatedRaceTestDataProvider(controlNos, punchNos, 0, 300, legTimes);

    RaceControlsTablePage page = new RaceControlsTablePage(event.getRaceNr());
    page.getTable().resetColumnSortOrder();
    page.loadChildren();

    // leg times
    Assert.assertEquals("0:00", page.getTable().getLegTimeColumn().getValue(0));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(1));
    Assert.assertNull(page.getTable().getLegTimeColumn().getValue(2));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(3));
    Assert.assertEquals("2:00", page.getTable().getLegTimeColumn().getValue(4));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(5));

    // times
    Assert.assertEquals("0:00", page.getTable().getRelativeTimeColumn().getValue(0));
    Assert.assertEquals("1:00", page.getTable().getRelativeTimeColumn().getValue(1));
    Assert.assertEquals(null, page.getTable().getRelativeTimeColumn().getValue(2));
    Assert.assertEquals("2:00", page.getTable().getRelativeTimeColumn().getValue(3));
    Assert.assertEquals("4:00", page.getTable().getRelativeTimeColumn().getValue(4));
    Assert.assertEquals("5:00", page.getTable().getRelativeTimeColumn().getValue(5));

    // status
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(0).longValue()); // S
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(1).longValue()); // 31
    Assert.assertEquals(ControlStatusCodeType.MissingCode.ID, page.getTable().getControlStatusColumn().getValue(2).longValue()); // 32
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(3).longValue()); // 33
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(4).longValue()); // 34
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(5).longValue()); // Z
    Assert.assertEquals(ControlStatusCodeType.WrongCode.ID, page.getTable().getControlStatusColumn().getValue(6).longValue()); // 32

    assertRaceStatus(event.getRaceNr(), RaceStatusCodeType.MissingPunchCode.ID);
  }

  @Test
  public void testAdditionalControl() throws Exception {
    String[] controlNos = new String[]{"99", "100", "101"};
    String[] punchNos = new String[]{"99", "88", "100", "101"};
    Integer[] legTimes = new Integer[]{60, 90, 120, 180};
    event = new EventWithIndividualValidatedRaceTestDataProvider(controlNos, punchNos, 0, 240, legTimes);

    RaceControlsTablePage page = new RaceControlsTablePage(event.getRaceNr());
    page.getTable().resetColumnSortOrder();
    page.loadChildren();

    // leg times
    Assert.assertEquals("0:00", page.getTable().getLegTimeColumn().getValue(0));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(1));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(2));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(3));
    Assert.assertEquals("1:00", page.getTable().getLegTimeColumn().getValue(4));
    Assert.assertNull(page.getTable().getLegTimeColumn().getValue(5));

    // times
    Assert.assertEquals("0:00", page.getTable().getRelativeTimeColumn().getValue(0));
    Assert.assertEquals("1:00", page.getTable().getRelativeTimeColumn().getValue(1));
    Assert.assertEquals("2:00", page.getTable().getRelativeTimeColumn().getValue(2));
    Assert.assertEquals("3:00", page.getTable().getRelativeTimeColumn().getValue(3));
    Assert.assertEquals("4:00", page.getTable().getRelativeTimeColumn().getValue(4));
    Assert.assertEquals("1:30", page.getTable().getRelativeTimeColumn().getValue(5));

    // status
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(0).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(1).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(2).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(3).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(4).longValue());
    Assert.assertEquals(ControlStatusCodeType.AdditionalCode.ID, page.getTable().getControlStatusColumn().getValue(5).longValue());

    // additional control
    Assert.assertEquals("88", page.getTable().getControlColumn().getValue(5));

    assertRaceStatus(event.getRaceNr(), RaceStatusCodeType.OkCode.ID);
  }

  @Test
  public void testClassWithoutControls() throws Exception {
    String[] controlNos = new String[]{};
    String[] punchNos = new String[]{"31"};
    Integer[] legTimes = new Integer[]{11};
    event = new EventWithIndividualValidatedRaceTestDataProvider(controlNos, punchNos, 0, 240, legTimes);

    RaceControlsTablePage page = new RaceControlsTablePage(event.getRaceNr());
    page.getTable().resetColumnSortOrder();
    page.loadChildren();

    Assert.assertEquals("Start, 1 Control, Finish", 3, page.getTable().getRowCount());

    // times
    Assert.assertEquals("0:00", page.getTable().getRelativeTimeColumn().getValue(0));
    Assert.assertEquals("4:00", page.getTable().getRelativeTimeColumn().getValue(1));
    Assert.assertEquals("0:11", page.getTable().getRelativeTimeColumn().getValue(2));

    // status
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(0).longValue());
    Assert.assertEquals(ControlStatusCodeType.OkCode.ID, page.getTable().getControlStatusColumn().getValue(1).longValue());
    Assert.assertEquals(ControlStatusCodeType.AdditionalCode.ID, page.getTable().getControlStatusColumn().getValue(2).longValue());
  }

  @Test
  public void testClassWithoutControlsStartAndFinish() throws Exception {
    String[] controlNos = new String[]{};
    String[] punchNos = new String[]{"31"};
    Integer[] legTimes = new Integer[]{11};
    event = new EventWithIndividualValidatedRaceTestDataProvider(controlNos, punchNos, null, null, legTimes);

    RaceControlsTablePage page = new RaceControlsTablePage(event.getRaceNr());
    page.getTable().resetColumnSortOrder();
    page.loadChildren();

    Assert.assertEquals("One additional control", 1, page.getTable().getRowCount());
    Assert.assertEquals(null, page.getTable().getOverallTimeColumn().getValue(0));
    Assert.assertEquals(null, page.getTable().getRelativeTimeColumn().getValue(0));
    Assert.assertEquals(null, page.getTable().getLegTimeColumn().getValue(0));
    Assert.assertEquals(ControlStatusCodeType.AdditionalCode.ID, page.getTable().getControlStatusColumn().getValue(0).longValue());
  }

  private void assertRaceStatus(long raceNr, long expected) throws ProcessingException {
    RaceBean race = new RaceBean();
    race.setRaceNr(raceNr);
    race = BEANS.get(IRaceProcessService.class).load(race);
    Assert.assertEquals("Race Status", expected, race.getStatusUid().longValue());
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
  }

}
