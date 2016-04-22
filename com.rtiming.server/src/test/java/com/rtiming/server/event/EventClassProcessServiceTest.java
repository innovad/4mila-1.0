package com.rtiming.server.event;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.test.data.EventClassTestDataProvider;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.race.TimePrecisionCodeType;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class EventClassProcessServiceTest {

  private EventClassTestDataProvider testData;

  @Test
  public void testDelete1() throws ProcessingException {
    EventClassProcessService svc = new EventClassProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    EventClassProcessService svc = new EventClassProcessService();
    svc.delete(new EventClassFormData());
  }

  @Test
  public void testDelete3() throws ProcessingException {
    createEventClass();

    EventClassProcessService svc = new EventClassProcessService();
    EventClassFormData formData = new EventClassFormData();
    formData.getEvent().setValue(testData.getEvent().getId().getId());
    formData.getClazz().setValue(testData.getClazz().getId().getId());
    svc.delete(formData);

    RtEventClass find = JPA.find(RtEventClass.class, testData.getEventClass().getId());
    assertNull("deleted", find);
    testData.setEventClass(null);
  }

  @Test
  public void testLoad1() throws Exception {
    EventClassProcessService svc = new EventClassProcessService();
    EventClassFormData formData = new EventClassFormData();
    formData = svc.load(formData);
  }

  @Test
  public void testLoad2() throws Exception {
    createEventClass();
    EventClassProcessService svc = new EventClassProcessService();
    EventClassFormData formData = new EventClassFormData();
    formData.setClientNr(testData.getEventClass().getId().getClientNr());
    formData.getEvent().setValue(testData.getEventClass().getId().getEventNr());
    formData.getClazz().setValue(testData.getEventClass().getId().getClassUid());
    formData = svc.load(formData);
    assertEquals("loaded", 4L, formData.getTeamSizeMin().getValue().longValue());
  }

  @Test
  public void testStore1() throws Exception {
    createEventClass();

    EventClassProcessService svc = new EventClassProcessService();
    EventClassFormData formData = new EventClassFormData();
    formData.setClientNr(testData.getEventClass().getId().getClientNr());
    formData.getEvent().setValue(testData.getEventClass().getId().getEventNr());
    formData.getClazz().setValue(testData.getEventClass().getId().getClassUid());
    formData = svc.load(formData);
    formData.getType().setValue(ClassTypeCodeType.TeamCombinedCourseCode.ID);
    formData.getTeamSizeMax().setValue(999L);

    formData = svc.store(formData);
    formData = svc.load(formData);
    assertEquals("stored", 999L, formData.getTeamSizeMax().getValue().longValue());
  }

  @Test
  public void testPrepareCreate1() throws Exception {
    EventClassProcessService svc = new EventClassProcessService();
    EventClassFormData formData = new EventClassFormData();
    svc.prepareCreate(formData);
  }

  @Test
  public void testPrepareCreate2() throws Exception {
    createEventClass();
    EventClassProcessService svc = new EventClassProcessService();
    EventClassFormData formData = new EventClassFormData();
    formData = svc.prepareCreate(formData);
    assertEquals("sortcode", 1, formData.getSortCode().getValue().longValue());
  }

  @Test
  public void testPrepareCreate3() throws Exception {
    createEventClass();
    EventClassProcessService svc = new EventClassProcessService();
    EventClassFormData formData = new EventClassFormData();
    formData.getEvent().setValue(testData.getEvent().getId().getId());
    formData = svc.prepareCreate(formData);
    assertEquals("sortcode", 2, formData.getSortCode().getValue().longValue());
  }

  @Test
  public void testPrepareCreate4() throws Exception {
    createEventClass();
    EventClassProcessService svc = new EventClassProcessService();
    EventClassFormData formData = new EventClassFormData();
    formData.getEvent().setValue(testData.getEvent().getId().getId());
    formData.getParent().setValue(testData.getClazz().getId().getId());
    formData = svc.prepareCreate(formData);
    assertEquals("sortcode", 1, formData.getSortCode().getValue().longValue());
    assertEquals("time precision", TimePrecisionCodeType.Precision100sCode.ID, formData.getTimePrecision().getValue().longValue());
    assertEquals("course generation", CourseGenerationCodeType.CourseGenerationIndividualControlsCode.ID, formData.getCourseGenerationType().getValue());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreate1() throws Exception {
    EventClassProcessService svc = new EventClassProcessService();
    EventClassFormData formData = new EventClassFormData();
    svc.create(formData);
  }

  @Test
  public void testCreate2() throws Exception {
    createEventClass();
    JPA.remove(testData.getEventClass());
    EventClassProcessService svc = new EventClassProcessService();
    EventClassFormData formData = new EventClassFormData();
    formData.getEvent().setValue(testData.getEvent().getId().getId());
    formData.getClazz().setValue(testData.getClazz().getId().getId());
    svc.create(formData);
    RtEventClass find = JPA.find(RtEventClass.class, testData.getEventClass().getId());
    assertNotNull("recreated", find);
  }

  @Test
  public void testCreate3() throws Exception {
// TODO MIG    
//    ServerJob job = new ServerJob("test", ServerSession.get()) {
//      @Override
//      protected IStatus runTransaction(IProgressMonitor monitor) throws Exception {
//        createEventClass();
//        EventClassProcessService svc = new EventClassProcessService();
//        EventClassFormData formData = new EventClassFormData();
//        formData.getEvent().setValue(testData.getEvent().getId().getId());
//        formData.getClazz().setValue(testData.getClazz().getId().getId());
//        svc.create(formData);
//        return null;
//      }
//    };
//    IStatus result = job.runNow(null);
//    Assert.assertTrue("error message", result.getMessage().startsWith(TEXTS.get("DuplicateKeyMessage")));
  }

  private void createEventClass() throws ProcessingException {
    testData = new EventClassTestDataProvider();
  }

  @After
  public void after() throws ProcessingException {
    if (testData != null) {
      testData.remove();
    }
  }

}
