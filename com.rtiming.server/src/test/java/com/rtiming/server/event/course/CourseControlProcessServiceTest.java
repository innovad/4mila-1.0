package com.rtiming.server.event.course;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.event.EventProcessService;
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControlKey;
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourseControl;
import com.rtiming.shared.dao.RtCourseControlKey;
import com.rtiming.shared.dao.RtCourseKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.event.course.CourseControlFormData;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class CourseControlProcessServiceTest {

  private RtEvent event;
  private RtCourse course;
  private RtControl control;
  private RtCourseControl courseControl;

  private void createEventWithCourse(boolean withCourseControl) throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    course = new RtCourse();
    course.setId(RtCourseKey.create((Long) null));
    course.setEventNr(event.getId().getId());
    JPA.merge(course);

    control = new RtControl();
    control.setId(RtControlKey.create((Long) null));
    control.setEventNr(event.getId().getId());
    control.setActive(true);
    JPA.merge(control);

    if (withCourseControl) {
      courseControl = new RtCourseControl();
      courseControl.setId(RtCourseControlKey.create((Long) null));
      courseControl.setCourseNr(course.getId().getId());
      courseControl.setSortcode(1L);
      courseControl.setCountLeg(true);
      courseControl.setMandatory(true);
      courseControl.setControlNr(control.getId().getId());
      JPA.merge(courseControl);
    }
  }

  @Test
  public void testPrepareCreate1() throws Exception {
    CourseControlProcessService svc = new CourseControlProcessService();
    svc.prepareCreate(new CourseControlFormData());
  }

  @Test
  public void testPrepareCreate2() throws Exception {
    createEventWithCourse(false);
    CourseControlProcessService svc = new CourseControlProcessService();
    CourseControlFormData formData = new CourseControlFormData();
    formData.getCourse().setValue(course.getId().getId());
    formData = svc.prepareCreate(formData);

    assertEquals("next sortcode", 1, formData.getSortCode().getValue().longValue());
  }

  @Test
  public void testPrepareCreate3() throws Exception {
    createEventWithCourse(true);
    CourseControlProcessService svc = new CourseControlProcessService();
    CourseControlFormData formData = new CourseControlFormData();
    formData.getCourse().setValue(course.getId().getId());
    formData = svc.prepareCreate(formData);

    assertEquals("next sortcode", 2, formData.getSortCode().getValue().longValue());
  }

  @Test
  public void testDelete1() throws ProcessingException {
    CourseControlProcessService svc = new CourseControlProcessService();
    svc.delete(null);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    CourseControlProcessService svc = new CourseControlProcessService();
    svc.delete(new CourseControlFormData());
  }

  @Test
  public void testDelete3() throws ProcessingException {
    RtCourseControl control2 = new RtCourseControl();
    control2.setId(RtCourseControlKey.create((Long) null));
    control2.setSortcode(1L);
    control2.setCountLeg(true);
    control2.setMandatory(true);
    JPA.persist(control2);

    CourseControlProcessService svc = new CourseControlProcessService();
    CourseControlFormData formData = new CourseControlFormData();
    formData.setCourseControlNr(control2.getId().getId());
    svc.delete(formData);

    RtCourseControl find = JPA.find(RtCourseControl.class, control2.getId());
    Assert.assertNull("deleted", find);
  }

  @Test
  public void testFind1() throws Exception {
    CourseControlProcessService svc = new CourseControlProcessService();
    CourseControlFormData find = svc.find(null, null, null);
    assertNull("not found", find.getCourseControlNr());
  }

  @Test
  public void testFind2() throws Exception {
    createEventWithCourse(true);
    CourseControlProcessService svc = new CourseControlProcessService();
    CourseControlFormData find = svc.find(course.getId().getId(), control.getId().getId(), courseControl.getSortcode());
    assertNotNull("found", find.getCourseControlNr());
    assertEquals("found", courseControl.getId().getId(), find.getCourseControlNr());
  }

  @Test
  public void testStore1() throws Exception {
    CourseControlProcessService svc = new CourseControlProcessService();
    svc.store(new CourseControlFormData());
  }

  @Test
  public void testStore2() throws Exception {
    createEventWithCourse(true);
    CourseControlProcessService svc = new CourseControlProcessService();
    CourseControlFormData formData = new CourseControlFormData();
    formData.setCourseControlNr(courseControl.getId().getId());
    formData = svc.load(formData);
    formData.getCountLeg().setValue(false);
    svc.store(formData);

    RtCourseControl find = JPA.find(RtCourseControl.class, courseControl.getId());
    assertFalse("updated", find.getCountLeg());
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      EventProcessService svc = new EventProcessService();
      svc.delete(event.getId().getId(), false);
    }
  }

}
