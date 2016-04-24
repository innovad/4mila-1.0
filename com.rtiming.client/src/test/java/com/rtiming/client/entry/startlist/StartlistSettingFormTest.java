package com.rtiming.client.entry.startlist;

import java.util.Arrays;
import java.util.Date;
import java.util.HashSet;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;
import com.rtiming.shared.entry.startlist.StartlistSettingVacantPositionCodeType;
import com.rtiming.shared.entry.startlist.StartlistTypeCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestClientSession.class)
public class StartlistSettingFormTest {

  private EventWithIndividualClassTestDataProvider event;

  @Before
  public void before() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
  }

  @Test
  public void testNew() throws ProcessingException {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    form.getTypeUidField().setValue(StartlistTypeCodeType.DrawingCode.ID);
    ScoutClientAssert.assertEnabled(form.getFirstStartField());
    ScoutClientAssert.assertEnabled(form.getLastStartField());
    ScoutClientAssert.assertEnabled(form.getStartIntervalField());
    ScoutClientAssert.assertEnabled(form.getVacantAbsoluteField());
    ScoutClientAssert.assertEnabled(form.getVacantPercentField());
    ScoutClientAssert.assertEnabled(form.getBibNoFromField());

    ScoutClientAssert.assertDisabled(form.getLastBibNoField());
    ScoutClientAssert.assertDisabled(form.getBibNoOrderUidField());

    ScoutClientAssert.assertVisible(form.getOptionsBox());
    ScoutClientAssert.assertVisible(form.getVacantPositionGroup());

    form.getTypeUidField().setValue(StartlistTypeCodeType.MassStartCode.ID);
    ScoutClientAssert.assertInvisible(form.getOptionsBox());
    ScoutClientAssert.assertInvisible(form.getVacantPositionGroup());

    // set back to drawing
    form.getTypeUidField().setValue(StartlistTypeCodeType.DrawingCode.ID);
    ScoutClientAssert.assertEnabled(form.getFirstStartField());
    ScoutClientAssert.assertEnabled(form.getLastStartField());
    ScoutClientAssert.assertEnabled(form.getStartIntervalField());
    ScoutClientAssert.assertEnabled(form.getVacantAbsoluteField());
    ScoutClientAssert.assertEnabled(form.getVacantPercentField());
    ScoutClientAssert.assertEnabled(form.getBibNoFromField());

    ScoutClientAssert.assertDisabled(form.getLastBibNoField());
    ScoutClientAssert.assertDisabled(form.getBibNoOrderUidField());

    ScoutClientAssert.assertVisible(form.getOptionsBox());
    ScoutClientAssert.assertVisible(form.getVacantPositionGroup());

    form.doClose();
  }

  @Test
  public void testBibNoBox() throws ProcessingException {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    ScoutClientAssert.assertEnabled(form.getBibNoFromField());
    ScoutClientAssert.assertDisabled(form.getLastBibNoField());
    ScoutClientAssert.assertDisabled(form.getBibNoOrderUidField());

    form.getBibNoFromField().setValue(88L);

    ScoutClientAssert.assertEnabled(form.getBibNoFromField());
    ScoutClientAssert.assertDisabled(form.getLastBibNoField());
    ScoutClientAssert.assertEnabled(form.getBibNoOrderUidField());
    Assert.assertEquals(BibNoOrderCodeType.AscendingCode.ID, form.getBibNoOrderUidField().getValue().longValue());

    // 88 init value + 7 participations => 94 last bib
    Assert.assertEquals(94L, form.getLastBibNoField().getValue().longValue());

    // 88 init value - 7 participations => 82 last bib
    form.getBibNoOrderUidField().setValue(BibNoOrderCodeType.DescendingCode.ID);
    Assert.assertEquals(82L, form.getLastBibNoField().getValue().longValue());

    form.doClose();
  }

  @Test
  public void testLastStartCalculation() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    form.getTypeUidField().setValue(StartlistTypeCodeType.DrawingCode.ID);

    Date firstStart = form.getFirstStartField().getValue();
    Date lastStart = form.getLastStartField().getValue();

    long diff = lastStart.getTime() - firstStart.getTime();
    Assert.assertEquals((7 - 1) * 120 * 1000, diff);

    form.getLastStartField().setValue(DateUtility.addDays(lastStart, 24));
    Assert.assertEquals((7 - 1) * 120 * 1000, diff);

    form.getFirstStartField().setValue(DateUtility.addDays(firstStart, -50));
    Assert.assertEquals((7 - 1) * 120 * 1000, diff);

    form.doClose();
  }

  @Test
  public void testFirstStartNull() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    form.getTypeUidField().setValue(StartlistTypeCodeType.DrawingCode.ID);
    form.getFirstStartField().setValue(null);
    Assert.assertNull(form.getLastStartField().getValue());

    form.doClose();
  }

  @Test
  public void testLastStartNull() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    form.getTypeUidField().setValue(StartlistTypeCodeType.DrawingCode.ID);
    form.getLastStartField().setValue(null);
    Assert.assertNull(form.getFirstStartField().getValue());

    form.doClose();
  }

  @Test
  public void testIntervalNull() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    form.getTypeUidField().setValue(StartlistTypeCodeType.DrawingCode.ID);
    form.getStartIntervalField().setValue(null);
    Assert.assertNull(form.getLastStartField().getValue());

    form.doClose();
  }

  @Test
  public void testParticipationCountNull() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(null);
    form.setEventNr(event.getEventNr());
    form.startNew();

    form.getTypeUidField().setValue(StartlistTypeCodeType.DrawingCode.ID);
    Assert.assertNull(form.getLastStartField().getValue());

    form.doClose();
  }

  @Test
  public void testIntervalCalculation() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    form.getTypeUidField().setValue(StartlistTypeCodeType.DrawingCode.ID);

    form.getStartIntervalField().setValue(99L);

    Date firstStart = form.getFirstStartField().getValue();
    Date lastStart = form.getLastStartField().getValue();
    long diff = lastStart.getTime() - firstStart.getTime();
    Assert.assertEquals((7 - 1) * 99 * 1000, diff);

    form.getStartIntervalField().setValue(11L);

    firstStart = form.getFirstStartField().getValue();
    lastStart = form.getLastStartField().getValue();
    diff = lastStart.getTime() - firstStart.getTime();
    Assert.assertEquals((7 - 1) * 11 * 1000, diff);
  }

  @Test
  public void testStore() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    form.getTypeUidField().setValue(StartlistTypeCodeType.DrawingCode.ID);
    form.doOk();
    Assert.assertNotNull(form.getStartlistSettingNr());

    StartlistSettingForm modify = new StartlistSettingForm();
    modify.setStartlistSettingNr(form.getStartlistSettingNr());
    modify.setParticipationCount(7L);
    modify.setEventNr(event.getEventNr());
    modify.startModify();

    Assert.assertEquals(form.getTypeUidField().getValue(), modify.getTypeUidField().getValue());
    Assert.assertEquals(form.getBibNoFromField().getValue(), modify.getBibNoFromField().getValue());
    Assert.assertEquals(form.getBibNoOrderUidField().getValue(), modify.getBibNoOrderUidField().getValue());
    Assert.assertEquals(form.getFirstStartField().getValue(), modify.getFirstStartField().getValue());
    Assert.assertEquals(form.getLastStartField().getValue(), modify.getLastStartField().getValue());
    Assert.assertEquals(form.getStartIntervalField().getValue(), modify.getStartIntervalField().getValue());

    modify.getBibNoFromField().setValue(99L);

    modify.doOk();
  }

  @Test
  public void testOptionsStore() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    form.getTypeUidField().setValue(StartlistTypeCodeType.DrawingCode.ID);
    form.getOptionsField().setValue(new HashSet<Long>(Arrays.asList(new Long[]{StartlistSettingOptionCodeType.SeparateNationsCode.ID})));
    form.doOk();
    Assert.assertNotNull(form.getStartlistSettingNr());

    StartlistSettingForm modify = new StartlistSettingForm();
    modify.setStartlistSettingNr(form.getStartlistSettingNr());
    modify.setParticipationCount(7L);
    modify.setEventNr(event.getEventNr());
    modify.startModify();

    Assert.assertEquals("Options", 1, form.getOptionsField().getValue().size());
    Assert.assertEquals("Options", StartlistSettingOptionCodeType.SeparateNationsCode.ID, form.getOptionsField().getValue().toArray(new Long[0])[0]);

    modify.doOk();
  }

  @Test(expected = VetoException.class)
  public void testOptionsSeparation() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    form.getOptionsField().setValue(new HashSet<Long>(Arrays.asList(new Long[]{StartlistSettingOptionCodeType.SeparateNationsCode.ID, StartlistSettingOptionCodeType.SeparateClubsCode.ID})));

    form.doOk();
  }

  @Test
  public void testLastStartChange() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    Date firstStart = form.getFirstStartField().getValue();
    Date lastStart = form.getLastStartField().getValue();

    long diff = lastStart.getTime() - firstStart.getTime();
    Assert.assertEquals((7 - 1) * 120 * 1000, diff);

    form.getLastStartField().setValue(FMilaUtility.addSeconds(lastStart, 333));
    Assert.assertEquals("First Start Changed", FMilaUtility.addSeconds(firstStart, 333), form.getFirstStartField().getValue());
  }

  @Test
  public void testFirstStartChange() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    Date firstStart = form.getFirstStartField().getValue();
    Date lastStart = form.getLastStartField().getValue();

    long diff = lastStart.getTime() - firstStart.getTime();
    Assert.assertEquals((7 - 1) * 120 * 1000, diff);

    form.getFirstStartField().setValue(FMilaUtility.addSeconds(firstStart, 333));
    Assert.assertEquals("Last Start Changed", FMilaUtility.addSeconds(lastStart, 333), form.getLastStartField().getValue());
  }

  @Test
  public void testVacantDefault() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    Assert.assertEquals("Default value", StartlistSettingVacantPositionCodeType.EarlyStartCode.ID, form.getVacantPositionGroup().getValue());
    form.doClose();
  }

  @Test
  public void testVacantPosition() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(7L);
    form.setEventNr(event.getEventNr());
    form.startNew();

    Assert.assertEquals("Default value", StartlistSettingVacantPositionCodeType.EarlyStartCode.ID, form.getVacantPositionGroup().getValue());

    form.getVacantPositionGroup().setValue(StartlistSettingVacantPositionCodeType.LateStartCode.ID);
    form.getTypeUidField().setValue(StartlistTypeCodeType.MassStartCode.ID);

    Assert.assertEquals("Default value reset", StartlistSettingVacantPositionCodeType.EarlyStartCode.ID, form.getVacantPositionGroup().getValue());

    form.doClose();
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

}
