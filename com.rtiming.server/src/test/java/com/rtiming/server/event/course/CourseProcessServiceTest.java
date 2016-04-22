package com.rtiming.server.event.course;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControlKey;
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourseControl;
import com.rtiming.shared.dao.RtCourseControlKey;
import com.rtiming.shared.dao.RtCourseKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.CourseFormData;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class CourseProcessServiceTest {

  private RtEvent event;
  private RtCourse course;
  private RtControl control;
  private RtControl control2;
  private RtCourseControl courseControl;
  private RtCourseControl courseControl2;

  @Test
  public void testDelete1() throws ProcessingException {
    CourseProcessService svc = new CourseProcessService();
    svc.delete(null, true);
  }

  @Test
  public void testDelete2() throws ProcessingException {
    CourseProcessService svc = new CourseProcessService();
    svc.delete(new CourseFormData(), true);
  }

  @Test
  public void testDelete3() throws ProcessingException {
    createCourseWithControl(false);

    CourseProcessService svc = new CourseProcessService();
    CourseFormData formData = new CourseFormData();
    formData.setCourseNr(course.getId().getId());
    svc.delete(formData, true);

    RtCourse findCourse = JPA.find(RtCourse.class, course.getId());
    assertNull("deleted", findCourse);

    RtCourseControl findControl = JPA.find(RtCourseControl.class, courseControl.getId());
    assertNull("deleted", findControl);
  }

  @Test
  public void testDelete4() throws ProcessingException {
    createCourseWithControl(false);

    CourseProcessService svc = new CourseProcessService();
    CourseFormData formData = new CourseFormData();
    formData.setCourseNr(course.getId().getId());
    svc.delete(formData, false);

    RtCourseControl findControl = JPA.find(RtCourseControl.class, courseControl.getId());
    assertNull("deleted", findControl);

    RtCourse findCourse = JPA.find(RtCourse.class, course.getId());
    assertNotNull("NOT deleted", findCourse);
    JPA.remove(findCourse);
  }

  @Test
  public void testFind1() throws Exception {
    createCourseWithControl(false);
    CourseProcessService svc = new CourseProcessService();
    CourseFormData find = svc.find("TESTX" + System.currentTimeMillis(), event.getId().getId());
    assertNull("not found", find.getCourseNr());
  }

  @Test
  public void testFind2() throws Exception {
    createCourseWithControl(false);
    CourseProcessService svc = new CourseProcessService();
    CourseFormData find = svc.find(course.getShortcut(), event.getId().getId());
    assertNotNull("found", find.getCourseNr());
    assertEquals("found", find.getCourseNr(), course.getId().getId());
  }

  @Test
  public void testFind3() throws Exception {
    CourseProcessService svc = new CourseProcessService();
    svc.find(null, 0L);
  }

  @Test
  public void testUpdate1() throws Exception {
    CourseProcessService svc = new CourseProcessService();
    CourseFormData formData = new CourseFormData();
    svc.store(formData);
  }

  @Test
  public void testUpdate2() throws Exception {
    createCourseWithControl(false);
    CourseProcessService svc = new CourseProcessService();
    CourseFormData formData = new CourseFormData();
    formData.setCourseNr(course.getId().getId());
    formData = svc.load(formData);
    formData.getClimb().setValue(1234L);
    svc.store(formData);

    RtCourse find = JPA.find(RtCourse.class, course.getId());
    assertEquals("updated", 1234L, find.getClimb().longValue());
  }

  @Test
  public void testLoad() throws Exception {
    createCourseWithControl(false);
    CourseProcessService svc = new CourseProcessService();
    CourseFormData formData = new CourseFormData();
    formData.setCourseNr(course.getId().getId());
    formData = svc.load(formData);
    assertTrue("loaded", formData.getShortcut().getValue().startsWith("COURSE"));
  }

  @Test
  public void testLoadControls1() throws Exception {
    createCourseWithControl(false);
    CourseProcessService svc = new CourseProcessService();
    List<ControlFormData> list = svc.loadControls(course.getId().getId(), course.getId().getClientNr());
    assertEquals("size", 1, list.size());
    assertEquals("control no", "44", list.get(0).getNumber().getValue());
  }

  @Test
  public void testLoadControls2() throws Exception {
    createCourseWithControl(true);
    CourseProcessService svc = new CourseProcessService();
    List<ControlFormData> list = svc.loadControls(course.getId().getId(), course.getId().getClientNr());
    assertEquals("size", 2, list.size());
    assertEquals("control no", "31", list.get(0).getNumber().getValue());
    assertEquals("control no", "44", list.get(1).getNumber().getValue());
  }

  private void createCourseWithControl(boolean createSecondControl) throws ProcessingException {
    event = new RtEvent();
    event.setId(RtEventKey.create((Long) null));
    JPA.merge(event);

    course = new RtCourse();
    course.setId(RtCourseKey.create((Long) null));
    course.setShortcut("COURSE" + System.currentTimeMillis());
    course.setEventNr(event.getId().getId());
    JPA.merge(course);

    control = new RtControl();
    control.setId(RtControlKey.create((Long) null));
    control.setControlNo("44");
    control.setActive(true);
    JPA.merge(control);

    courseControl = new RtCourseControl();
    courseControl.setCourseNr(course.getId().getId());
    courseControl.setId(RtCourseControlKey.create((Long) null));
    courseControl.setControlNr(control.getId().getId());
    courseControl.setSortcode(2L);
    courseControl.setCountLeg(true);
    courseControl.setMandatory(true);
    JPA.merge(courseControl);

    if (createSecondControl) {
      control2 = new RtControl();
      control2.setId(RtControlKey.create((Long) null));
      control2.setControlNo("31");
      control2.setActive(true);
      JPA.merge(control2);

      courseControl2 = new RtCourseControl();
      courseControl2.setCourseNr(course.getId().getId());
      courseControl2.setId(RtCourseControlKey.create((Long) null));
      courseControl2.setControlNr(control2.getId().getId());
      courseControl2.setSortcode(1L);
      courseControl2.setCountLeg(true);
      courseControl2.setMandatory(true);
      JPA.merge(courseControl2);
    }
  }

  @After
  public void after() throws ProcessingException {
    if (courseControl != null && JPA.find(RtCourseControl.class, courseControl.getId()) != null) {
      JPA.remove(courseControl);
    }
    if (control != null && JPA.find(RtControl.class, control.getId()) != null) {
      JPA.remove(control);
    }
    if (courseControl2 != null && JPA.find(RtCourseControl.class, courseControl2.getId()) != null) {
      JPA.remove(courseControl2);
    }
    if (control2 != null && JPA.find(RtControl.class, control2.getId()) != null) {
      JPA.remove(control2);
    }
    if (course != null && JPA.find(RtCourse.class, course.getId()) != null) {
      JPA.remove(course);
    }
    if (event != null && JPA.find(RtEvent.class, event.getId()) != null) {
      JPA.remove(event);
    }
  }

}
