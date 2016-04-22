package com.rtiming.client.dataexchange.iof3;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.rtiming.client.dataexchange.ProgressMonitor;
import com.rtiming.client.dataexchange.iof3.IOF300CourseDataBean.ImportedCourseControl;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.iof3.xml.ClassCourseAssignment;
import com.rtiming.shared.dataexchange.iof3.xml.Control;
import com.rtiming.shared.dataexchange.iof3.xml.ControlType;
import com.rtiming.shared.dataexchange.iof3.xml.Course;
import com.rtiming.shared.dataexchange.iof3.xml.CourseControl;
import com.rtiming.shared.dataexchange.iof3.xml.CourseData;
import com.rtiming.shared.dataexchange.iof3.xml.RaceCourseData;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.IControlProcessService;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.event.course.ICourseProcessService;
import com.rtiming.shared.settings.CodeFormData;

public class IOF300CourseDataBeanTest {

  @Test
  public void testImportCourseControlNull1() throws Exception {
    Map<String, ControlFormData> controlCache = new HashMap<>();

    IControlProcessService controlService = Mockito.mock(IControlProcessService.class);
    ICourseControlProcessService courseControlService = Mockito.mock(ICourseControlProcessService.class);
    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, createAccess(5L, null, courseControlService, controlService));
    ImportedCourseControl impControl = bean.new ImportedCourseControl();

    CourseControlFormData result = doTestImportCourseControl(bean, impControl, controlCache);
    Assert.assertNull("No Course Control", result);
  }

  @Test
  public void testImportCourseControlNull2() throws Exception {
    Map<String, ControlFormData> controlCache = new HashMap<>();

    IControlProcessService controlService = Mockito.mock(IControlProcessService.class);
    ICourseControlProcessService courseControlService = Mockito.mock(ICourseControlProcessService.class);
    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, createAccess(5L, null, courseControlService, controlService));

    CourseControlFormData result = doTestImportCourseControl(bean, null, controlCache);
    Assert.assertNull("No Course Control", result);
  }

  @Test
  public void testImportCourseControl1() throws Exception {
    Map<String, ControlFormData> controlCache = new HashMap<>();

    IControlProcessService controlService = Mockito.mock(IControlProcessService.class);
    ICourseControlProcessService courseControlService = Mockito.mock(ICourseControlProcessService.class);
    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, createAccess(5L, null, courseControlService, controlService));
    ImportedCourseControl impControl = bean.new ImportedCourseControl();

    impControl.setControlNo("31");
    ControlFormData value = new ControlFormData();
    value.setControlNr(999L);
    controlCache.put("31", value);
    CourseControlFormData newCourseControlFormData = new CourseControlFormData();
    Mockito.when(courseControlService.find(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(newCourseControlFormData);

    CourseControlFormData result = doTestImportCourseControl(bean, impControl, controlCache);
    Assert.assertNotNull("Course Control", result);
    Assert.assertEquals("Result", newCourseControlFormData, result);
    Assert.assertEquals("Cache Size", 1, controlCache.size());
    Assert.assertEquals("Cache Content", 999, controlCache.get("31").getControlNr().longValue());
  }

  @Test
  public void testImportCourseControl2() throws Exception {
    Map<String, ControlFormData> controlCache = new HashMap<>();

    IControlProcessService controlService = Mockito.mock(IControlProcessService.class);
    ICourseControlProcessService courseControlService = Mockito.mock(ICourseControlProcessService.class);
    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, createAccess(5L, null, courseControlService, controlService));
    ImportedCourseControl impControl = bean.new ImportedCourseControl();

    impControl.setControlNo("31");
    CourseControlFormData newCourseControlFormData = new CourseControlFormData();
    Mockito.when(courseControlService.find(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(newCourseControlFormData);
    ControlFormData value = new ControlFormData();
    value.setControlNr(999L);
    Mockito.when(controlService.find("31", 5L)).thenReturn(value);
    Mockito.when(controlService.store(value)).thenReturn(value);

    CourseControlFormData result = doTestImportCourseControl(bean, impControl, controlCache);
    Assert.assertNotNull("Course Control", result);
    Assert.assertEquals("Result", newCourseControlFormData, result);
    Assert.assertEquals("Cache Size", 1, controlCache.size());
    Assert.assertEquals("Cache Content", 999, controlCache.get("31").getControlNr().longValue());
  }

  @Test
  public void testImportCourseControl3() throws Exception {
    Map<String, ControlFormData> controlCache = new HashMap<>();
    ControlFormData value = new ControlFormData();
    value.setControlNr(999L);
    controlCache.put("31", value);

    IControlProcessService controlService = Mockito.mock(IControlProcessService.class);
    ICourseControlProcessService courseControlService = Mockito.mock(ICourseControlProcessService.class);
    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, createAccess(5L, null, courseControlService, controlService));
    ImportedCourseControl impControl = bean.new ImportedCourseControl();

    impControl.setControlNo("31");
    CourseControlFormData newCourseControlFormData = new CourseControlFormData();
    Mockito.when(courseControlService.find(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(newCourseControlFormData);
    Mockito.when(controlService.find("31", 5L)).thenReturn(value);
    Mockito.when(controlService.store(value)).thenReturn(value);

    CourseControlFormData result = doTestImportCourseControl(bean, impControl, controlCache);
    Assert.assertNotNull("Course Control", result);
    Assert.assertEquals("Result", newCourseControlFormData, result);
    Assert.assertEquals("Cache Size", 1, controlCache.size());
    Assert.assertEquals("Cache Content", 999, controlCache.get("31").getControlNr().longValue());
  }

  @Test
  public void testImportCourseControl4() throws Exception {
    Map<String, ControlFormData> controlCache = new HashMap<>();
    ControlFormData value = new ControlFormData();
    controlCache.put("31", value);

    IControlProcessService controlService = Mockito.mock(IControlProcessService.class);
    ICourseControlProcessService courseControlService = Mockito.mock(ICourseControlProcessService.class);
    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, createAccess(5L, null, courseControlService, controlService));
    ImportedCourseControl impControl = bean.new ImportedCourseControl();

    impControl.setControlNo("31");
    CourseControlFormData newCourseControlFormData = new CourseControlFormData();
    Mockito.when(courseControlService.find(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(newCourseControlFormData);
    Mockito.when(controlService.find("31", 5L)).thenReturn(value);
    Mockito.when(controlService.store(value)).thenReturn(value);
    ControlFormData value2 = new ControlFormData();
    value2.setControlNr(888L);
    Mockito.when(controlService.create(value)).thenReturn(value2);

    CourseControlFormData result = doTestImportCourseControl(bean, impControl, controlCache);
    Assert.assertNotNull("Course Control", result);
    Assert.assertEquals("Result", newCourseControlFormData, result);
    Assert.assertEquals("Cache Size", 1, controlCache.size());
    Assert.assertEquals("Cache Content", 888, controlCache.get("31").getControlNr().longValue());
  }

  @Test
  public void testImportCourseControl5() throws Exception {
    Map<String, ControlFormData> controlCache = new HashMap<>();
    ControlFormData value = new ControlFormData();
    value.getType().setValue(ControlTypeCodeType.ControlCode.ID); // wrong type
    controlCache.put("S", value);

    IControlProcessService controlService = Mockito.mock(IControlProcessService.class);
    ICourseControlProcessService courseControlService = Mockito.mock(ICourseControlProcessService.class);
    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, createAccess(5L, null, courseControlService, controlService));
    ImportedCourseControl impControl = bean.new ImportedCourseControl();

    impControl.setControlNo("S");
    impControl.setTypeUid(ControlTypeCodeType.StartCode.ID); // correct type
    CourseControlFormData newCourseControlFormData = new CourseControlFormData();
    Mockito.when(courseControlService.find(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(newCourseControlFormData);
    Mockito.when(controlService.find("S", 5L)).thenReturn(value);
    Mockito.when(controlService.store(value)).thenReturn(value);
    ControlFormData value2 = new ControlFormData();
    value2.setControlNr(888L);
    value2.getType().setValue(ControlTypeCodeType.StartCode.ID); // correct type
    Mockito.when(controlService.create(value)).thenReturn(value2);

    CourseControlFormData result = doTestImportCourseControl(bean, impControl, controlCache);
    Assert.assertNotNull("Course Control", result);
    Assert.assertEquals("Result", newCourseControlFormData, result);
    Assert.assertEquals("Cache Size", 1, controlCache.size());
    Assert.assertEquals("Cache Content", 888, controlCache.get("S").getControlNr().longValue());
    Assert.assertEquals("Cache Content", ControlTypeCodeType.StartCode.ID, controlCache.get("S").getType().getValue().longValue());
  }

  private CourseControlFormData doTestImportCourseControl(IOF300CourseDataBean bean, ImportedCourseControl impControl, Map<String, ControlFormData> controlCache) throws ProcessingException {
    IProgressMonitor monitor = new ProgressMonitor(null);
    CourseControlFormData result = bean.importCourseControl(impControl, controlCache, monitor);
    return result;
  }

  @Test
  public void testImportCourse1() throws Exception {
    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, null);
    CourseFormData result = bean.importCourse(null, null);
    Assert.assertNull("Result null", result);
  }

  @Test
  public void testImportCourse2() throws Exception {
    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, null);
    CourseFormData result = bean.importCourse(new ArrayList<Course>(), null);
    Assert.assertNull("Result null", result);
  }

  @Test
  public void testImportCourse3() throws Exception {
    List<Course> courseList = new ArrayList<Course>();
    Course course = new Course();
    course.setCourseFamily("ABCFamily");
    courseList.add(course);

    ICourseProcessService courseService = Mockito.mock(ICourseProcessService.class);
    CourseFormData courseFormData = new CourseFormData();
    courseFormData.setCourseNr(999L);
    Mockito.when(courseService.find("ABCFamily", 5L)).thenReturn(courseFormData);

    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, createAccess(5L, courseService, null, null));
    CourseFormData result = bean.importCourse(courseList, new ProgressMonitor(null));
    Assert.assertNotNull("Result not null", result);
    Assert.assertNull("Climb", courseFormData.getClimb().getValue());
    Assert.assertNull("Length", courseFormData.getLength().getValue());
  }

  @Test
  public void testImportCourse4() throws Exception {
    List<Course> courseList = new ArrayList<Course>();
    Course course = new Course();
    course.setCourseFamily("ABCFamily");
    course.setClimb(500d);
    course.setLength(12100d);
    courseList.add(course);

    ICourseProcessService courseService = Mockito.mock(ICourseProcessService.class);
    CourseFormData courseFormData = new CourseFormData();
    courseFormData.setCourseNr(999L);
    Mockito.when(courseService.find("ABCFamily", 5L)).thenReturn(courseFormData);

    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, createAccess(5L, courseService, null, null));
    CourseFormData result = bean.importCourse(courseList, new ProgressMonitor(null));
    Assert.assertNotNull("Result not null", result);
    Assert.assertEquals("Climb", 500, courseFormData.getClimb().getValue().longValue());
    Assert.assertEquals("Length", 12100, courseFormData.getLength().getValue().longValue());
  }

  @Test
  public void testImportCourse5() throws Exception {
    List<Course> courseList = new ArrayList<Course>();
    Course course = new Course();
    course.setCourseFamily("ABCFamily");

    CourseControl control1 = new CourseControl();
    control1.setType(ControlType.CONTROL);
    control1.getControl().add("31");
    course.getCourseControl().add(control1);

    courseList.add(course);

    ICourseProcessService courseService = Mockito.mock(ICourseProcessService.class);
    CourseFormData courseFormData = new CourseFormData();
    courseFormData.setCourseNr(999L);
    Mockito.when(courseService.find("ABCFamily", 5L)).thenReturn(courseFormData);

    IControlProcessService controlService = Mockito.mock(IControlProcessService.class);
    ControlFormData controlFormData = new ControlFormData();
    controlFormData.setControlNr(777L);
    Mockito.when(controlService.find("31", 5L)).thenReturn(controlFormData);
    Mockito.when(controlService.store(controlFormData)).thenReturn(controlFormData);

    ICourseControlProcessService courseControlService = Mockito.mock(ICourseControlProcessService.class);
    CourseControlFormData courseControlFormData = new CourseControlFormData();
    courseControlFormData.setCourseControlNr(555L);
    Mockito.when(courseControlService.find(Mockito.anyLong(), Mockito.anyLong(), Mockito.anyLong())).thenReturn(courseControlFormData);

    IOF300CourseDataBean bean = new IOF300CourseDataBean(null, createAccess(5L, courseService, courseControlService, controlService));
    bean.importCourse(courseList, new ProgressMonitor(null));
  }

  private CourseDataAccess createAccess(Long eventNr, ICourseProcessService courseService, ICourseControlProcessService courseControlService, IControlProcessService controlService) {
    return new CourseDataAccess(eventNr, courseService, courseControlService, controlService, null, null, null, null);
  }

  @Test
  public void testGetPreviewRowData1() throws Exception {
    IOF300CourseDataBean bean = new IOF300CourseDataBean(0L, Mockito.mock(CourseDataAccess.class));
    CourseData courseData = new CourseData();
    bean.setCourseData(courseData);
    assertPreviewData(bean, "0", "0", "0", "0");
  }

  @Test
  public void testGetPreviewRowData2() throws Exception {
    IOF300CourseDataBean bean = new IOF300CourseDataBean(0L, Mockito.mock(CourseDataAccess.class));
    CourseData courseData = new CourseData();
    courseData.getRaceCourseData().add(new RaceCourseData());
    bean.setCourseData(courseData);
    assertPreviewData(bean, "0", "0", "0", "0");
  }

  @Test
  public void testGetPreviewRowData3() throws Exception {
    IOF300CourseDataBean bean = new IOF300CourseDataBean(0L, Mockito.mock(CourseDataAccess.class));
    bean.setCourseData(null);
    assertPreviewData(bean, "0", "0", "0", "0");
  }

  @Test
  public void testGetPreviewRowData4() throws Exception {
    IOF300CourseDataBean bean = new IOF300CourseDataBean(0L, Mockito.mock(CourseDataAccess.class));
    CourseData courseData = new CourseData();
    RaceCourseData raceCourseData = new RaceCourseData();
    raceCourseData.getControl().add(new Control());
    courseData.getRaceCourseData().add(raceCourseData);
    bean.setCourseData(courseData);
    assertPreviewData(bean, "1", "0", "0", "0");
  }

  @Test
  public void testGetPreviewRowData5() throws Exception {
    IOF300CourseDataBean bean = new IOF300CourseDataBean(0L, Mockito.mock(CourseDataAccess.class));
    CourseData courseData = new CourseData();
    RaceCourseData raceCourseData = new RaceCourseData();
    raceCourseData.getCourse().add(new Course());
    courseData.getRaceCourseData().add(raceCourseData);
    bean.setCourseData(courseData);
    assertPreviewData(bean, "0", "1", "0", "0");
  }

  @Test
  public void testGetPreviewRowData6() throws Exception {
    IOF300CourseDataBean bean = new IOF300CourseDataBean(0L, Mockito.mock(CourseDataAccess.class));
    CourseData courseData = new CourseData();
    RaceCourseData raceCourseData = new RaceCourseData();
    raceCourseData.getClassCourseAssignment().add(new ClassCourseAssignment());
    courseData.getRaceCourseData().add(raceCourseData);
    bean.setCourseData(courseData);
    assertPreviewData(bean, "0", "0", "1", "0");
  }

  @Test
  public void testGetPreviewRowData7() throws Exception {
    IOF300CourseDataBean bean = new IOF300CourseDataBean(0L, Mockito.mock(CourseDataAccess.class));
    CourseData courseData = new CourseData();
    RaceCourseData raceCourseData = new RaceCourseData();
    raceCourseData.getMap().add(new com.rtiming.shared.dataexchange.iof3.xml.Map());
    courseData.getRaceCourseData().add(raceCourseData);
    bean.setCourseData(courseData);
    assertPreviewData(bean, "0", "0", "0", "1");
  }

  private void assertPreviewData(IOF300CourseDataBean bean, String controlCount, String courseCount, String classCount, String mapCount) throws ProcessingException {
    List<String> result = bean.getPreviewRowData();
    Assert.assertEquals("Size", 4, result.size());
    Assert.assertEquals("Control", controlCount, result.get(0));
    Assert.assertEquals("Course", courseCount, result.get(1));
    Assert.assertEquals("Class", classCount, result.get(2));
    Assert.assertEquals("Map", mapCount, result.get(3));
  }

  @Test
  public void testImportClass1() throws Exception {
    IOF300CourseDataBean bean = new IOF300CourseDataBean(0L, Mockito.mock(CourseDataAccess.class));
    bean.importClasses(null, new ProgressMonitor(null));
  }

  @Test
  public void testImportClass2() throws Exception {
    CourseDataAccess access = Mockito.mock(CourseDataAccess.class);
    CodeFormData classCode = new CodeFormData();
    classCode.setCodeUid(12345L);
    Mockito.when(access.findClass("Class1")).thenReturn(classCode);
    Mockito.when(access.findCourse("Course1")).thenReturn(new CourseFormData());
    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getType().setValue(ClassTypeCodeType.IndividualEventCode.ID);
    Mockito.when(access.loadEventClass(Mockito.any(EventClassFormData.class))).thenReturn(eventClass);

    IOF300CourseDataBean bean = new IOF300CourseDataBean(0L, access);
    List<ClassCourseAssignment> list = new ArrayList<>();
    ClassCourseAssignment classCourse = new ClassCourseAssignment();
    classCourse.setCourseFamily("Course1");
    classCourse.setClassName("Class1");
    list.add(classCourse);
    bean.importClasses(list, new ProgressMonitor(null));
  }

}
