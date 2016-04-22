package com.rtiming.client.entry;

import java.security.Permission;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.desktop.AbstractDesktop;
import org.eclipse.scout.rt.platform.BeanMetaData;
import org.eclipse.scout.rt.platform.IBean;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.ICodeService;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.security.IAccessControlService;
import org.eclipse.scout.rt.shared.services.lookup.IBatchLookupService;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.shared.TestingUtility;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.rtiming.client.ClientSession;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.EventsBox.EventsField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table.EditMenu;
import com.rtiming.client.race.RaceForm;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.TestingRunnable;
import com.rtiming.shared.club.IClubLookupService;
import com.rtiming.shared.common.database.sql.EntryBean;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.common.database.sql.ParticipationBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.ecard.IECardLookupService;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.entry.IEntryProcessService;
import com.rtiming.shared.entry.IEntryService;
import com.rtiming.shared.entry.IRegistrationLookupService;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.IEventLookupService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.runner.IRunnerLookupService;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.addinfo.IEventAdditionalInformationProcessService;
import com.rtiming.shared.settings.city.ICityLookupService;
import com.rtiming.shared.settings.clazz.IAgeProcessService;
import com.rtiming.shared.settings.clazz.IClazzLookupService;
import com.rtiming.shared.settings.fee.IFeeProcessService;

/**
 * @author amo
 */
@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class EntryFormTest {

  @SuppressWarnings("rawtypes")
  private List<IBean<?>> registration = null;
  private ClientSession session;
  private IEntryProcessService entryProcessService;
  private IEventAdditionalInformationProcessService eventAddInfoService;
  private IEntryService entryService;
  private IFeeProcessService feeProcessSerivce;
  private IRunnerLookupService runnerLookupService;
  private IClazzLookupService clazzLookupService;
  private IECardLookupService ecardLookupService;
  private IRegistrationLookupService registrationLookupService;
  private IClubLookupService clubLookupService;
  private ICityLookupService cityLookupService;
  private IEventLookupService eventLookupService;
  private IRunnerProcessService runnerProcessService;
  private IBatchLookupService batchLookupService;
  private IAccessControlService accessControlService;
  private IEventProcessService eventProcessService;
  private IEventClassProcessService eventClassProcessService;
  private IDefaultProcessService defaultProcessService;
  private IAgeProcessService ageProcessService;

  @SuppressWarnings("unchecked")
  @Before
  public void before() throws ProcessingException {
    // TODO MIG IExceptionHandlerService exceptionService = Mockito.mock(IExceptionHandlerService.class);
    ICodeService codeService = Mockito.mock(ICodeService.class);
    Mockito.when(codeService.getCodeType(Mockito.any(Class.class))).thenReturn(Mockito.mock(ICodeType.class));
    entryProcessService = Mockito.mock(IEntryProcessService.class);
    eventAddInfoService = Mockito.mock(IEventAdditionalInformationProcessService.class);
    entryService = Mockito.mock(IEntryService.class);
    Mockito.when(entryProcessService.prepareCreate(Mockito.any(EntryBean.class))).thenReturn(new EntryBean());
    feeProcessSerivce = Mockito.mock(IFeeProcessService.class);
    runnerProcessService = Mockito.mock(IRunnerProcessService.class);
    accessControlService = Mockito.mock(IAccessControlService.class);
    Mockito.when(accessControlService.checkPermission(Mockito.any(Permission.class))).thenReturn(true);
    eventProcessService = Mockito.mock(IEventProcessService.class);
    eventClassProcessService = Mockito.mock(IEventClassProcessService.class);
    defaultProcessService = Mockito.mock(IDefaultProcessService.class);
    ageProcessService = Mockito.mock(IAgeProcessService.class);

    // Lookups
    List<List<ILookupRow<?>>> lookupRows = new ArrayList<>();
    List<ILookupRow<?>> row = new ArrayList<>();
    row.add(new LookupRow(0L, "Bla", null));
    lookupRows.add(row);
    lookupRows.add(row);
    batchLookupService = Mockito.mock(IBatchLookupService.class);
    // TODO MIG Mockito.when(batchLookupService.getBatchDataByKey(Mockito.any(BatchLookupCall.class))).thenReturn(lookupRows);
    runnerLookupService = Mockito.mock(IRunnerLookupService.class);
    // TODO MIG Mockito.when(runnerLookupService.getDataByKey(Mockito.any(LookupCall.class))).thenReturn(row);
    clazzLookupService = Mockito.mock(IClazzLookupService.class);
    // TODO MIG Mockito.when(clazzLookupService.getDataByKey(Mockito.any(LookupCall.class))).thenReturn(row);
    ecardLookupService = Mockito.mock(IECardLookupService.class);
    // TODO MIG Mockito.when(ecardLookupService.getDataByKey(Mockito.any(LookupCall.class))).thenReturn(row);
    registrationLookupService = Mockito.mock(IRegistrationLookupService.class);
    // TODO MIG Mockito.when(registrationLookupService.getDataByKey(Mockito.any(LookupCall.class))).thenReturn(row);
    clubLookupService = Mockito.mock(IClubLookupService.class);
    // TODO MIG Mockito.when(clubLookupService.getDataByKey(Mockito.any(LookupCall.class))).thenReturn(row);
    cityLookupService = Mockito.mock(ICityLookupService.class);
    // TODO MIG Mockito.when(cityLookupService.getDataByKey(Mockito.any(LookupCall.class))).thenReturn(row);
    eventLookupService = Mockito.mock(IEventLookupService.class);
    // TODO MIG Mockito.when(eventLookupService.getDataByKey(Mockito.any(LookupCall.class))).thenReturn(row);

    registration = TestingUtility.registerBeans(/* exceptionService TODO MIG */ new BeanMetaData(IEntryProcessService.class, entryProcessService), new BeanMetaData(ICodeService.class, codeService), new BeanMetaData(IEntryService.class, entryService), new BeanMetaData(IEventAdditionalInformationProcessService.class, eventAddInfoService), new BeanMetaData(IFeeProcessService.class, feeProcessSerivce), new BeanMetaData(IRunnerLookupService.class, runnerLookupService), new BeanMetaData(IClazzLookupService.class, clazzLookupService), new BeanMetaData(IECardLookupService.class, ecardLookupService), new BeanMetaData(IRegistrationLookupService.class, registrationLookupService), new BeanMetaData(IClubLookupService.class, clubLookupService), new BeanMetaData(ICityLookupService.class, cityLookupService), new BeanMetaData(IRunnerProcessService.class, runnerProcessService), new BeanMetaData(IEventLookupService.class, eventLookupService), new BeanMetaData(IBatchLookupService.class, batchLookupService), new BeanMetaData(IAccessControlService.class, accessControlService), new BeanMetaData(IEventProcessService.class, eventProcessService), new BeanMetaData(IEventClassProcessService.class, eventClassProcessService), new BeanMetaData(IDefaultProcessService.class, defaultProcessService), new BeanMetaData(IAgeProcessService.class, ageProcessService));

    session = Mockito.mock(ClientSession.class);
    // TODO MIG ClientSessionThreadLocal.set(session);
    Mockito.when(session.getDesktop()).thenReturn(new SimpleDesktop());
  }

  @Test
  public void testPrepare() throws Exception {
    // Prepare
    prepareDefaultEvent();

    EntryForm form = new EntryForm();
    form.startNew();

    EventsField.Table eventsTable = form.getEventsField().getTable();
    Assert.assertEquals("1 Rows", 1, eventsTable.getRowCount());
    Assert.assertEquals("Default Event", 33L, eventsTable.getEventNrColumn().getValue(0).longValue());
  }

  @Test
  public void testRunnerSmartfield() throws Exception {
    EntryForm form = new EntryForm();
    form.startNew();

    // Runner
    RunnerBean runner = createRunnerForSmartfield();

    Table racesTable = form.getRacesField().getTable();
    Assert.assertEquals("0 Rows", 0, racesTable.getRowCount());
    form.getRunnerField().setValue(runner.getRunnerNr());

    // Table
    Assert.assertEquals("1 Rows", 1, racesTable.getRowCount());
    Assert.assertTrue("Row selected", racesTable.getRow(0).isSelected());

    // Form
    Assert.assertEquals("Last Name Field", runner.getLastName(), form.getLastNameField().getValue());
    Assert.assertEquals("First Name Field", runner.getFirstName(), form.getFirstNameField().getValue());
    Assert.assertEquals("Year Field", runner.getYear(), form.getYearField().getValue());

    // Columns
    Assert.assertEquals("Runner Bean", runner, racesTable.getRaceBeanColumn().getValue(0).getRunner());
  }

  @Test
  public void testModifyRace1() throws Exception {
    EventBean event = prepareDefaultEvent();

    EntryForm form = new EntryForm();
    form.startNew();

    final RunnerBean runner = createRunnerForSmartfield();
    form.getRunnerField().setValue(runner.getRunnerNr());

    Mockito.when(eventProcessService.load(Mockito.any(EventBean.class))).thenReturn(event);
    Mockito.when(eventClassProcessService.load(Mockito.any(EventClassFormData.class))).thenReturn(new EventClassFormData());

    Runnable runnableAfterMenu = new TestingRunnable() {

      @Override
      protected void runTest() throws ProcessingException {
        // TODO MIG ClientSessionThreadLocal.set(session);
        RaceForm raceForm = FMilaClientTestUtility.findLastAddedForm(RaceForm.class);
        Assert.assertNotNull("RaceForm exists", raceForm);
        Assert.assertEquals("Runner Nr", runner.getRunnerNr(), raceForm.getRunnerNrField().getValue());
        Assert.assertEquals("Nation", runner.getNationUid(), raceForm.getNationField().getValue());
        Assert.assertEquals("Club", runner.getClubNr(), raceForm.getClubField().getValue());
        raceForm.doClose();
      }
    };
    FMilaClientTestUtility.runBlockingMenu(form.getRacesField().getTable(), EditMenu.class, runnableAfterMenu);
  }

  @Test
  public void testModifyRace2() throws Exception {
    EventBean event = prepareDefaultEvent();

    EntryForm form = new EntryForm();
    form.startNew();

    final RunnerBean runner = createRunnerForSmartfield();
    form.getRunnerField().setValue(runner.getRunnerNr());
    // form.getClazzField().setValue(123L);

    Mockito.when(eventProcessService.load(Mockito.any(EventBean.class))).thenReturn(event);
    Mockito.when(eventClassProcessService.load(Mockito.any(EventClassFormData.class))).thenReturn(new EventClassFormData());

    Runnable runnableAfterMenu = new TestingRunnable() {

      @Override
      protected void runTest() throws ProcessingException {
        // TODO MIG ClientSessionThreadLocal.set(session);
        RaceForm raceForm = FMilaClientTestUtility.findLastAddedForm(RaceForm.class);
        raceForm.getBibNumberField().setValue("12345");
        // raceForm.doOk();
        raceForm.doClose();
      }
    };
    FMilaClientTestUtility.runBlockingMenu(form.getRacesField().getTable(), EditMenu.class, runnableAfterMenu);
    // Assert.assertEquals("Bib no changed", "12345", form.getRacesField().getTable().getBibNumberColumn().getValue(0));
  }

  private EventBean prepareDefaultEvent() throws ProcessingException {
    EntryBean entryBean = new EntryBean();
    ParticipationBean participation = new ParticipationBean();
    participation.setEventNr(33L);
    entryBean.addParticipation(participation);
    Mockito.when(entryProcessService.prepareCreate(Mockito.any(EntryBean.class))).thenReturn(entryBean);

    EventConfiguration configuration = new EventConfiguration();
    EventBean event = new EventBean();
    event.setEventNr(33L);
    event.setEvtZero(new Date());
    configuration.addEvents(event);

    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(33L);
    eventClass.getClazz().setValue(123L);
    configuration.addClass(eventClass);

    Mockito.when(entryService.loadEventConfiguration()).thenReturn(configuration);

    return event;
  }

  private RunnerBean createRunnerForSmartfield() throws ProcessingException {
    RunnerBean runner = new RunnerBean();
    runner.setRunnerNr(1000L);
    runner.setFirstName("Jonathan Felix");
    runner.setLastName("Latscha");
    runner.setYear(2012L);
    runner.setNationUid(444L);
    runner.setClubNr(333L);
    runner.setDefaultClassUid(null);
    Mockito.when(runnerProcessService.load(Mockito.any(RunnerBean.class))).thenReturn(runner);
    return runner;
  }

  @After
  public void after() throws ProcessingException {
    TestingUtility.unregisterBeans(registration);
    // TODO MIG ClientSessionThreadLocal.set(null);
  }

  class SimpleDesktop extends AbstractDesktop {
    @Override
    public boolean isOpened() {
      return true;
    }
  }

}
