package com.rtiming.client.entry.startlist;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.desktop.DesktopEvent;
import org.eclipse.scout.rt.client.ui.desktop.DesktopListener;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.data.basic.table.SortSpec;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.entry.EntriesSearchForm;
import com.rtiming.client.entry.EntriesTablePage;
import com.rtiming.client.entry.startlist.StartlistSettingsTablePage.Table.ApplySameStartlistSettingMenu;
import com.rtiming.client.entry.startlist.StartlistSettingsTablePage.Table.CreateStartlistMenu;
import com.rtiming.client.entry.startlist.StartlistSettingsTablePage.Table.DeleteMenu;
import com.rtiming.client.event.course.CourseForm;
import com.rtiming.client.settings.CodeForm;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.shared.AdditionalInformationUtility;
import com.rtiming.shared.entry.startlist.IStartlistService;
import com.rtiming.shared.entry.startlist.IStartlistSettingProcessService;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;
import com.rtiming.shared.entry.startlist.StartlistSettingUtility;
import com.rtiming.shared.entry.startlist.StartlistTypeCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.services.code.CourseGenerationCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class StartlistSettingsTablePageTest extends AbstractTablePageTest<StartlistSettingsTablePage> {

  private EventWithIndividualClassTestDataProvider event;
  private EntryTestDataProvider entry;
  private EntryTestDataProvider entry2;

  @Before
  public void before() throws ProcessingException {
    event = new EventWithIndividualClassTestDataProvider();
    AdditionalInformationUtility.createStartTimeWish(event.getEventNr());
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(event.getEventNr());
  }

  @Override
  protected StartlistSettingsTablePage getTablePage() throws ProcessingException {
    return new StartlistSettingsTablePage(event.getEventNr());
  }

  @Test
  public void testData() throws Exception {
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    Assert.assertNotNull(entry.getEntryNr());

    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.loadChildren();
    Assert.assertEquals(1, page.getTable().getRowCount());
    Assert.assertEquals(1, page.getTable().getParticipationCountColumn().getValue(0).longValue());
  }

  @Test
  public void testTwoEntries() throws Exception {
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    entry2 = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    Assert.assertNotNull(entry.getEntryNr());
    Assert.assertNotNull(entry2.getEntryNr());

    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.loadChildren();
    Assert.assertEquals(1, page.getTable().getRowCount());
    Assert.assertEquals(2, page.getTable().getParticipationCountColumn().getValue(0).longValue());

    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(page.getTable().getClazzUidColumn().getValue(0));
    form.setParticipationCount(page.getTable().getParticipationCountColumn().getValue(0));
    form.setEventNr(event.getEventNr());
    form.startNew();
    form.doOk();

    page.loadChildren();

    int lastCol = page.getTable().getVacantColumn().getColumnIndex();
    Assert.assertTrue(page.getTable().getColumns().get(lastCol + 1).isVisible()); // slot 1
    Assert.assertTrue(page.getTable().getColumns().get(lastCol + 2).isVisible()); // slot 2
    Assert.assertFalse(page.getTable().getColumns().get(lastCol + 3).isVisible()); // invisible

    ITableRow row = page.getTable().getRow(0);
    String firstStart = page.getTable().getFirstStartColumn().getDisplayText(row);
    String lastStart = page.getTable().getLastStartColumn().getDisplayText(row);
    Assert.assertTrue(page.getTable().getColumns().get(lastCol + 1).getValue(row).toString().startsWith(firstStart));
    Assert.assertTrue(page.getTable().getColumns().get(lastCol + 2).getValue(row).toString().startsWith(lastStart));

    IFormField field = page.getTable().getIntervalColumn().prepareEdit(row);
    ((AbstractLongField) field).setValue(77L);
    page.getTable().getIntervalColumn().completeEdit(row, field);
    page.loadChildren();

    lastStart = page.getTable().getLastStartColumn().getDisplayText(row);
    Assert.assertTrue(page.getTable().getColumns().get(lastCol + 1).getValue(row).toString().startsWith(firstStart));
    Assert.assertTrue(page.getTable().getColumns().get(lastCol + 2).getValue(row).toString().startsWith(lastStart));

    form = new StartlistSettingForm();
    form.setStartlistSettingNr(page.getTable().getStartlistSettingNrColumn().getValue(0));
    form.setParticipationCount(2L);
    form.setEventNr(event.getEventNr());
    form.startModify();

    Assert.assertEquals(page.getTable().getFirstStartColumn().getValue(0), form.getFirstStartField().getValue());
    Assert.assertEquals(page.getTable().getLastStartColumn().getValue(0), form.getLastStartField().getValue());
    Assert.assertEquals(77L, form.getStartIntervalField().getValue().longValue());

    form.doClose();
  }

  @Test
  public void testSingleMenusOnNewRow() throws Exception {
    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.loadChildren();
    Assert.assertEquals(1, page.getTable().getRowCount());

    page.getTable().selectFirstRow();
    boolean done = page.getTable().runMenu(ApplySameStartlistSettingMenu.class);
    Assert.assertFalse(done);

    done = page.getTable().runMenu(CreateStartlistMenu.class);
    Assert.assertFalse(done);

    done = page.getTable().runMenu(DeleteMenu.class);
    Assert.assertFalse(done);
  }

  @Test
  public void testSingleMenusOnCreatedRow() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(0L);
    form.setEventNr(event.getEventNr());
    form.startNew();
    form.doOk();

    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.loadChildren();
    Assert.assertEquals(1, page.getTable().getRowCount());

    page.getTable().selectFirstRow();
    boolean done = page.getTable().runMenu(ApplySameStartlistSettingMenu.class);
    Assert.assertFalse(done);

    done = page.getTable().runMenu(CreateStartlistMenu.class);
    Assert.assertTrue(done);

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

    done = page.getTable().runMenu(DeleteMenu.class);
    Assert.assertTrue(done);

    ClientSession.get().getDesktop().removeDesktopListener(listener);

    page.loadChildren();
    page.getTable().selectFirstRow();

    done = page.getTable().runMenu(DeleteMenu.class);
    Assert.assertFalse(done);

    done = page.getTable().runMenu(CreateStartlistMenu.class);
    Assert.assertFalse(done);

    done = page.getTable().runMenu(DeleteMenu.class);
    Assert.assertFalse(done);
  }

  @Test
  public void testMultiMenusOnNewRow() throws Exception {
    // add a 2nd class with course
    CodeForm clazz = FMilaClientTestUtility.createClass();
    CourseForm course = FMilaClientTestUtility.createCourse(event.getEventNr());
    FMilaClientTestUtility.createEventClass(event.getEventNr(), clazz.getCodeUid(), course.getCourseNr(), ClassTypeCodeType.IndividualEventCode.ID, CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);

    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.loadChildren();
    Assert.assertEquals(2, page.getTable().getRowCount());

    page.getTable().selectAllRows();
    boolean done = page.getTable().runMenu(ApplySameStartlistSettingMenu.class);
    Assert.assertFalse(done);

    done = page.getTable().runMenu(CreateStartlistMenu.class);
    Assert.assertFalse(done);

    done = page.getTable().runMenu(DeleteMenu.class);
    Assert.assertFalse(done);
  }

  @Test
  public void testMultiMenusSameCourse() throws Exception {
    // add a 2nd class with course
    CodeForm clazz = FMilaClientTestUtility.createClass();
    FMilaClientTestUtility.createEventClass(event.getEventNr(), clazz.getCodeUid(), event.getCourseNr(), ClassTypeCodeType.IndividualEventCode.ID, CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);

    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(0L);
    form.setEventNr(event.getEventNr());
    form.startNew();
    form.doOk();

    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.loadChildren();
    Assert.assertEquals(2, page.getTable().getRowCount());

    page.getTable().selectAllRows();

    boolean done = page.getTable().runMenu(CreateStartlistMenu.class);
    Assert.assertFalse(done);

    done = page.getTable().runMenu(DeleteMenu.class);
    Assert.assertFalse(done);

    done = page.getTable().runMenu(ApplySameStartlistSettingMenu.class);
    Assert.assertTrue(done);
  }

  @Test
  public void testMultiMenusDifferentCourse() throws Exception {
    // add a 2nd class with course
    CodeForm clazz = FMilaClientTestUtility.createClass();
    CourseForm course = FMilaClientTestUtility.createCourse(event.getEventNr());
    FMilaClientTestUtility.createEventClass(event.getEventNr(), clazz.getCodeUid(), course.getCourseNr(), ClassTypeCodeType.IndividualEventCode.ID, CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);

    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(0L);
    form.setEventNr(event.getEventNr());
    form.startNew();
    form.doOk();

    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.loadChildren();
    Assert.assertEquals(2, page.getTable().getRowCount());

    page.getTable().selectAllRows();

    boolean done = page.getTable().runMenu(CreateStartlistMenu.class);
    Assert.assertFalse(done);

    done = page.getTable().runMenu(DeleteMenu.class);
    Assert.assertFalse(done);

    done = page.getTable().runMenu(ApplySameStartlistSettingMenu.class);
    Assert.assertFalse(done);
  }

  @Test
  public void testTwoClasses() throws Exception {
    // add a 2nd class with course
    CodeForm clazz = FMilaClientTestUtility.createClass();
    CourseForm course = FMilaClientTestUtility.createCourse(event.getEventNr());
    FMilaClientTestUtility.createEventClass(event.getEventNr(), clazz.getCodeUid(), course.getCourseNr(), ClassTypeCodeType.IndividualEventCode.ID, CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID);

    // add entries
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    entry2 = new EntryTestDataProvider(event.getEventNr(), clazz.getCodeUid());

    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.loadChildren();
    Assert.assertEquals(2, page.getTable().getRowCount());
    Assert.assertEquals(1, page.getTable().getParticipationCountColumn().getValue(0).longValue());
    Assert.assertEquals(1, page.getTable().getParticipationCountColumn().getValue(1).longValue());

    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(page.getTable().getClazzUidColumn().getValue(0));
    form.setParticipationCount(page.getTable().getParticipationCountColumn().getValue(0));
    form.setEventNr(event.getEventNr());
    form.startNew();
    form.doOk();

    page.loadChildren();

    Assert.assertEquals(1, page.getTable().getParticipationCountColumn().getValue(0).longValue());
    Assert.assertEquals(1, page.getTable().getParticipationCountColumn().getValue(1).longValue());
  }

  @Test
  public void testDrawing() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(2L);
    form.setEventNr(event.getEventNr());
    form.startNew();
    Assert.assertEquals("Drawing", form.getTypeUidField().getValue().longValue(), StartlistTypeCodeType.DrawingCode.ID);
    form.doOk();

    // add entries
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    entry2 = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.loadChildren();
    Assert.assertEquals(1, page.getTable().getRowCount());
    Assert.assertEquals(2, page.getTable().getParticipationCountColumn().getValue(0).longValue());

    page.getTable().selectFirstRow();
    boolean run = page.getTable().runMenu(CreateStartlistMenu.class);
    Assert.assertTrue(run);

    EntriesTablePage startlist = new EntriesTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, event.getClassUid(), null, null);
    startlist.nodeAddedNotify();
    startlist.getTable().getStartTimeColumn().setInitialSortIndex(Integer.MAX_VALUE);
    EntriesSearchForm entriesSearchForm = (EntriesSearchForm) startlist.getSearchFormInternal();
    entriesSearchForm.doReset();
    entriesSearchForm.getEventField().setValue(event.getEventNr());
    entriesSearchForm.getStartTimeFrom().setValue(null);
    entriesSearchForm.getStartTimeTo().setValue(null);
    startlist.loadChildren();

    Assert.assertEquals(2, startlist.getTable().getRowCount());
    Assert.assertEquals(form.getFirstStartField().getValue(), startlist.getTable().getStartTimeColumn().getValue(0));
    Assert.assertEquals(form.getLastStartField().getValue(), startlist.getTable().getStartTimeColumn().getValue(1));
  }

  @Test
  public void testMassStart() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(2L);
    form.setEventNr(event.getEventNr());
    form.startNew();
    form.getTypeUidField().setValue(StartlistTypeCodeType.MassStartCode.ID);
    form.doOk();

    // add entries
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    entry2 = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    StartlistSettingsTablePage page = new StartlistSettingsTablePage(event.getEventNr());
    page.loadChildren();
    Assert.assertEquals(1, page.getTable().getRowCount());
    Assert.assertEquals(2, page.getTable().getParticipationCountColumn().getValue(0).longValue());

    page.getTable().selectFirstRow();
    boolean run = page.getTable().runMenu(CreateStartlistMenu.class);
    Assert.assertTrue(run);

    EntriesTablePage startlist = new EntriesTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, event.getClassUid(), null, null);
    startlist.nodeAddedNotify();
    startlist.getTable().getStartTimeColumn().setInitialSortIndex(Integer.MAX_VALUE);
    EntriesSearchForm entriesSearchForm = (EntriesSearchForm) startlist.getSearchFormInternal();
    entriesSearchForm.doReset();
    entriesSearchForm.getEventField().setValue(event.getEventNr());
    entriesSearchForm.getStartTimeFrom().setValue(null);
    entriesSearchForm.getStartTimeTo().setValue(null);
    startlist.loadChildren();

    Assert.assertEquals(2, startlist.getTable().getRowCount());
    Assert.assertEquals(form.getFirstStartField().getValue(), form.getLastStartField().getValue());
    Assert.assertEquals(form.getFirstStartField().getValue(), startlist.getTable().getStartTimeColumn().getValue(0));
    Assert.assertEquals(form.getLastStartField().getValue(), startlist.getTable().getStartTimeColumn().getValue(1));
  }

  @Test
  public void testBibNo() throws Exception {
    StartlistSettingForm form = new StartlistSettingForm();
    form.setNewClassUid(event.getClassUid());
    form.setParticipationCount(2L);
    form.setEventNr(event.getEventNr());
    form.startNew();
    form.getTypeUidField().setValue(StartlistTypeCodeType.MassStartCode.ID);
    form.doOk();

    // add entries
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    entry2 = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());

    // Iteration 1
    BEANS.get(IStartlistService.class).createStartlists(new Long[]{form.getStartlistSettingNr()});
    EntriesTablePage startlist = loadEntriesSortedByBibNo();
    Assert.assertEquals(2, startlist.getTable().getRowCount());
    Assert.assertEquals("1", startlist.getTable().getBibNumberColumn().getValue(0));
    Assert.assertEquals("2", startlist.getTable().getBibNumberColumn().getValue(1));

    // Iteration 2 - same bib no expected
    BEANS.get(IStartlistService.class).createStartlists(new Long[]{form.getStartlistSettingNr()});
    startlist = loadEntriesSortedByBibNo();
    Assert.assertEquals(2, startlist.getTable().getRowCount());
    Assert.assertEquals("1", startlist.getTable().getBibNumberColumn().getValue(0));
    Assert.assertEquals("2", startlist.getTable().getBibNumberColumn().getValue(1));
  }

  private EntriesTablePage loadEntriesSortedByBibNo() throws ProcessingException {
    EntriesTablePage startlist = new EntriesTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, event.getClassUid(), null, null);
    startlist.nodeAddedNotify();
    SortSpec spec = new SortSpec(startlist.getTable().getBibNumberColumn().getColumnIndex(), true);
    startlist.getTable().getColumnSet().setSortSpec(spec);
    EntriesSearchForm entriesSearchForm = (EntriesSearchForm) startlist.getSearchFormInternal();
    entriesSearchForm.doReset();
    entriesSearchForm.getEventField().setValue(event.getEventNr());
    entriesSearchForm.getStartTimeFrom().setValue(null);
    entriesSearchForm.getStartTimeTo().setValue(null);
    startlist.loadChildren();
    return startlist;
  }

  @Test
  public void testCheckIfStartlistSettingsContainRegistrationOptionFalse1() throws Exception {
    StartlistSettingFormData settings1 = new StartlistSettingFormData();
    settings1.setEventNr(event.getEventNr());
    settings1.getOptions().setValue(null);
    settings1 = BEANS.get(IStartlistSettingProcessService.class).create(settings1);

    Long[] selectedNrs = new Long[]{settings1.getStartlistSettingNr()};
    boolean result = StartlistSettingUtility.checkIfStartlistSettingsContainRegistrationOption(selectedNrs);

    Assert.assertFalse("No options are set", result);
  }

  @Test
  public void testCheckIfStartlistSettingsContainRegistrationOptionFalse2() throws Exception {
    StartlistSettingFormData settings1 = new StartlistSettingFormData();
    settings1.setEventNr(event.getEventNr());
    settings1.getOptions().getValue().add(StartlistSettingOptionCodeType.AllowStarttimeWishesCode.ID);
    settings1.getOptions().getValue().add(StartlistSettingOptionCodeType.SeparateNationsCode.ID);
    settings1 = BEANS.get(IStartlistSettingProcessService.class).create(settings1);

    Long[] selectedNrs = new Long[]{settings1.getStartlistSettingNr()};
    boolean result = StartlistSettingUtility.checkIfStartlistSettingsContainRegistrationOption(selectedNrs);

    Assert.assertFalse("No options are set", result);
  }

  @Test
  public void testCheckIfStartlistSettingsContainRegistrationOptionTrue1() throws Exception {
    StartlistSettingFormData settings1 = new StartlistSettingFormData();
    settings1.setEventNr(event.getEventNr());
    settings1.getOptions().getValue().add(StartlistSettingOptionCodeType.GroupRegistrationsCode.ID);
    settings1 = BEANS.get(IStartlistSettingProcessService.class).create(settings1);

    Long[] selectedNrs = new Long[]{settings1.getStartlistSettingNr()};
    boolean result = StartlistSettingUtility.checkIfStartlistSettingsContainRegistrationOption(selectedNrs);

    Assert.assertTrue("Options are set", result);
  }

  @Test
  public void testCheckIfStartlistSettingsContainRegistrationOptionTrue2() throws Exception {
    StartlistSettingFormData settings1 = new StartlistSettingFormData();
    settings1.setEventNr(event.getEventNr());
    settings1.getOptions().getValue().add(StartlistSettingOptionCodeType.SplitRegistrationsCode.ID);
    settings1 = BEANS.get(IStartlistSettingProcessService.class).create(settings1);

    Long[] selectedNrs = new Long[]{settings1.getStartlistSettingNr()};
    boolean result = StartlistSettingUtility.checkIfStartlistSettingsContainRegistrationOption(selectedNrs);

    Assert.assertTrue("Options are set", result);
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
  }

}
