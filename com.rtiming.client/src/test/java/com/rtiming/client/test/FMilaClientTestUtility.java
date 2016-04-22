package com.rtiming.client.test;

import java.lang.reflect.Method;
import java.util.List;

import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractColumn;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.button.IButton;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.Assert;

import com.rtiming.client.FMilaClientSyncJob;
import com.rtiming.client.ecard.download.ECardStationForm;
import com.rtiming.client.event.EventClassForm;
import com.rtiming.client.event.course.ControlForm;
import com.rtiming.client.event.course.CourseControlForm;
import com.rtiming.client.event.course.CourseForm;
import com.rtiming.client.runner.RunnerForm;
import com.rtiming.client.settings.CodeForm;
import com.rtiming.client.test.data.EventClassTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.database.sql.EntryBean;
import com.rtiming.shared.common.database.sql.ParticipationBean;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.entry.EntryFormData;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.entry.IEntryProcessService;
import com.rtiming.shared.entry.IEntryService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.runner.IRunnerProcessService;

/**
 * @author amo
 */
public final class FMilaClientTestUtility {

  private FMilaClientTestUtility() {
  }

  public static CodeForm createClass() throws ProcessingException {
    CodeForm clazz = new CodeForm();
    clazz.setCodeType(ClassCodeType.ID);
    clazz.startNew();
    FormTestUtility.fillFormFields(clazz);
    clazz.doOk();
    return clazz;
  }

  public static CourseForm createCourse(Long eventNr) throws ProcessingException {
    Assert.assertNotNull(eventNr);
    CourseForm course = new CourseForm();
    course.getEventField().setValue(eventNr);
    course.startNew();
    FormTestUtility.fillFormFields(course, new FieldValue(CourseForm.MainBox.EventField.class, eventNr));
    course.doOk();
    Assert.assertEquals(course.getEventField().getValue(), eventNr);
    return course;
  }

  public static CourseControlForm createCourseControl(Long eventNr, Long courseNr, Long typeUid, Long sortCode) throws ProcessingException {
    Assert.assertNotNull(eventNr);
    Assert.assertNotNull(courseNr);
    Assert.assertNotNull(typeUid);

    // Control
    ControlForm start = new ControlForm();
    start.getEventField().setValue(eventNr);
    start.startNew();
    FormTestUtility.fillFormFields(start);
    start.getTypeField().setValue(typeUid);
    start.doOk();

    // Start Course
    CourseControlForm startCourse = new CourseControlForm();
    startCourse.startNew();
    startCourse.setEventNr(eventNr);
    startCourse.getCourseField().setValue(courseNr);
    startCourse.getControlField().setValue(start.getControlNr());
    startCourse.getSortCodeField().setValue(sortCode);
    startCourse.doOk();

    return startCourse;
  }

  public static EventClassForm createEventClass(Long eventNr, Long classUid, Long courseNr, Long typeUid, Long courseGenerationUid) throws ProcessingException {
    return createEventClassLeg(eventNr, classUid, courseNr, typeUid, null, null, courseGenerationUid);
  }

  public static EventClassForm createEventClassLeg(Long eventNr, Long classUid, Long courseNr, Long typeUid, Long parentUid, Long sortCode, Long courseGenerationUid) throws ProcessingException {
    Assert.assertNotNull(eventNr);
    Assert.assertNotNull(classUid);

    EventClassTestDataProvider eventClass = new EventClassTestDataProvider(eventNr, classUid, courseNr, typeUid, parentUid, sortCode, courseGenerationUid);
    return eventClass.getForm();
  }

  public static EntryFormData createIndividualEntry(Long eventNr, Long classUid) throws ProcessingException {
    Assert.assertNotNull(eventNr);
    Assert.assertNotNull(classUid);

    // Runner
    RunnerForm runner = new RunnerForm();
    runner.startNew();
    FormTestUtility.fillFormFields(runner);
    runner.doOk();

    RunnerBean runnerFormData = new RunnerBean();
    runnerFormData.setRunnerNr(runner.getRunnerNr());
    runnerFormData = BEANS.get(IRunnerProcessService.class).load(runnerFormData);

    // Entry
    EntryBean entry = new EntryBean();
    entry = BEANS.get(IEntryProcessService.class).prepareCreate(entry);

    RaceBean race = new RaceBean();
    entry.addRace(race);
    race.setRunnerNr(runner.getRunnerNr());
    race.setRunner(runnerFormData);
    race.setEventNr(eventNr);
    race.setECardNr(runner.getECardField().getValue());
    race.setLegClassUid(classUid);
    race.setLegStartTime(0L);

    ParticipationBean participation = entry.getParticipations().get(0);
    participation.setEventNr(eventNr);
    participation.setClassUid(classUid);

    EventConfiguration configuration = BEANS.get(IEntryService.class).loadEventConfiguration();
    return BeanUtility.entryBean2formData(BEANS.get(IEntryProcessService.class).create(entry), configuration);
  }

  public static ECardStationForm getECardStation() throws ProcessingException {
    ECardStationForm form = new ECardStationForm();
    MaxFormFieldValueProvider provider = new MaxFormFieldValueProvider();
    form.getIdentifierField().setEnabled(true);
    form.getBaudField().setEnabled(true);
    form.getClientAddressField().setEnabled(true);
    form.getPortField().setEnabled(true);
    provider.fillValueField(form.getIdentifierField(), null);
    provider.fillValueField(form.getBaudField(), null);
    provider.fillValueField(form.getClientAddressField(), null);
    provider.fillValueField(form.getPortField(), null);
    form.getIdentifierField().setEnabled(false);
    form.getBaudField().setEnabled(false);
    form.getClientAddressField().setEnabled(false);
    form.getPortField().setEnabled(false);
    form.startNew();
    return form;
  }

  public static void testFormFields(IForm form) {
    Method[] methods = form.getClass().getMethods();

    for (IFormField field : form.getAllFields()) {
      for (Method m : methods) {
        if (m.getName().equalsIgnoreCase("get" + field.getClass().getSimpleName())) {
          Assert.assertEquals(0, m.getParameterTypes().length);
          try {
            Object o = m.invoke(form);
            Assert.assertEquals(field.getClass(), o.getClass());
          }
          catch (Exception e) {
            Assert.fail(e.getMessage());
          }
        }
      }
    }
  }

  /**
   * If clicking a button leads to a lock because execClickAction contains waitFor() no code can be executed until the
   * lock is released.
   * With this function it is possible to execute code just before the lock is installed.
   * <p>
   * The caller must ensure that the lock gets released (f.e. by canceling the form at the and of the runnable).
   * </p>
   * 
   * @param button
   * @param runnableAfterButtonClick
   * @throws ProcessingException
   */
  public static void clickBlockingButton(IButton button, Runnable runnableAfterButtonClick) throws ProcessingException {
// TODO MIG    
//    TestingExceptionHandlerService exceptionHandler = new TestingExceptionHandlerService();
//    @SuppressWarnings("rawtypes")
//    List<ServiceRegistration> regs = TestingUtility.registerServices(Activator.getDefault().getBundle(), 1000, exceptionHandler);
//
//    WaitForListener waitForListener = new WaitForListener(runnableAfterButtonClick);
//    ClientJob currentJob = (ClientJob) ClientJob.getJobManager().currentJob();
//    currentJob.addJobChangeListenerEx(waitForListener);
//    try {
//      //click button which opens the form and blocks with waitFor()
//      button.doClick();
//      exceptionHandler.assertNoException();
//      exceptionHandler.clear();
//    }
//    finally {
//      currentJob.removeJobChangeListenerEx(waitForListener);
//      TestingUtility.unregisterServices(regs);
//    }

  }

  public static void runBlockingMenu(ITable table, Class<? extends IMenu> menuType, Runnable runnableAfterMenuExecution) throws ProcessingException {
// TODO MIG    
//    TestingExceptionHandlerService exceptionHandler = new TestingExceptionHandlerService();
//    @SuppressWarnings("rawtypes")
//    List<ServiceRegistration> regs = TestingUtility.registerServices(Activator.getDefault().getBundle(), 1000, exceptionHandler);
//
//    WaitForListener waitForListener = new WaitForListener(runnableAfterMenuExecution);
//    ClientJob currentJob = (ClientJob) ClientJob.getJobManager().currentJob();
//    currentJob.addJobChangeListenerEx(waitForListener);
//    try {
//      //run menu which opens a form and blocks with waitFor()
//      boolean run = table.runMenu(menuType);
//      Assert.assertTrue(run);
//      exceptionHandler.assertNoException();
//      exceptionHandler.clear();
//    }
//    finally {
//      currentJob.removeJobChangeListenerEx(waitForListener);
//      TestingUtility.unregisterServices(regs);
//    }
//
  }

  public static void runBlockingJob(FMilaClientSyncJob job, Runnable runnableAfterWaitFor) throws ProcessingException {
// TODO MIG    
//    TestingExceptionHandlerService exceptionHandler = new TestingExceptionHandlerService();
//    @SuppressWarnings("rawtypes")
//    List<ServiceRegistration> regs = TestingUtility.registerServices(Activator.getDefault().getBundle(), 1000, exceptionHandler);
//
//    WaitForListener waitForListener = new WaitForListener(runnableAfterWaitFor);
//    ClientJob currentJob = (ClientJob) ClientJob.getJobManager().currentJob();
//    currentJob.addJobChangeListenerEx(waitForListener);
//    try {
//      // run job which opens the form and blocks with waitFor()
//      job.runNow(new NullProgressMonitor());
//      exceptionHandler.assertNoException();
//      exceptionHandler.clear();
//    }
//    finally {
//      currentJob.removeJobChangeListenerEx(waitForListener);
//      TestingUtility.unregisterServices(regs);
//    }
  }

  public static <T extends IForm> T findLastAddedForm(Class<T> formType) {
    List<T> forms = IDesktop.CURRENT.get().findForms(formType);
    if (forms == null || forms.size() == 0) {
      return null;
    }

    return forms.get(forms.size() - 1);
  }

  private static class WaitForListener /* extends JobChangeAdapterEx */ {
    private final Runnable runnable;

    public WaitForListener(Runnable runnable) {
      this.runnable = runnable;
    }

// TODO MIG    
//    @Override
//    public void blockingConditionStart(IJobChangeEvent event) {
//      try {
//        runnable.run();
//      }
//      catch (Throwable t) {
//        //Exception would be catched by the listener so it is necessary to use the exception service in order to make the test fail.
//        BEANS.get(TestingExceptionHandlerService.class).handleException(new ProcessingException("", t));
//      }
//    }
  }

  @SuppressWarnings({"rawtypes", "unchecked"})
  public static void doColumnEdit(AbstractColumn<?> col, int rowId, Object newValue) throws ProcessingException {
    if (!col.isEditable()) {
      Assert.fail("Column is not editable");
    }
    ITableRow row = col.getTable().getRow(rowId);
    IFormField field = col.prepareEdit(row);
    if (field instanceof IValueField<?>) {
      IValueField valueField = (IValueField<?>) field;
      valueField.setValue(newValue);
    }
    else {
      Assert.fail("Column Editing Field is no Value Field");
    }
    col.completeEdit(row, field);
  }

}
