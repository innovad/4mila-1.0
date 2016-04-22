package com.rtiming.server.event.course;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.junit.Test;

import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.services.code.CourseForkTypeCodeType;

import org.junit.Assert;

/**
 * @since 1.0.7
 */
public class CourseCopyUtilityTest {

  @Test(expected = IllegalArgumentException.class)
  public void testCreateVariantsFromAllCourses1() throws Exception {
    CourseCopyUtility.createVariantsFromAllCourses(1L, 10L, 101L, 102L, null);
  }

  @Test
  public void testCreateVariantsFromAllCourses2() throws Exception {
    List<List<CourseControlRowData>> allCourseControlVariants = new ArrayList<>();
    List<CourseControlFormData> result = CourseCopyUtility.createVariantsFromAllCourses(1L, 10L, 101L, 102L, allCourseControlVariants);
    Assert.assertEquals("0 Items", 0, result.size());
  }

  @Test(expected = VetoException.class)
  public void testCreateVariantsFromAllCourses3() throws Exception {
    List<List<CourseControlRowData>> allCourseControlVariants = new ArrayList<>();
    allCourseControlVariants.add(new ArrayList<CourseControlRowData>());
    List<CourseControlFormData> result = CourseCopyUtility.createVariantsFromAllCourses(1L, 10L, 101L, 102L, allCourseControlVariants);
    Assert.assertEquals("0 Items", 0, result.size());
  }

  @Test
  public void testCreateVariantsFromAllCourses4() throws Exception {
    List<List<CourseControlRowData>> allCourseControlVariants = new ArrayList<>();
    List<CourseControlRowData> course = new ArrayList<CourseControlRowData>();
    CourseControlRowData control1 = new CourseControlRowData();
    control1.setControlNr(31L);
    course.add(control1);
    CourseControlRowData control2 = new CourseControlRowData();
    control2.setControlNr(32L);
    course.add(control2);
    allCourseControlVariants.add(course);
    List<CourseControlFormData> result = CourseCopyUtility.createVariantsFromAllCourses(1L, 10L, 101L, 31L, allCourseControlVariants);
    Assert.assertEquals("1 Item", 1, result.size());
    Assert.assertEquals("Control", 32, result.get(0).getControl().getValue().longValue());
  }

  @Test(expected = VetoException.class)
  public void testCreateVariantsFromAllCourses5() throws Exception {
    List<List<CourseControlRowData>> allCourseControlVariants = new ArrayList<>();
    List<CourseControlRowData> course = new ArrayList<CourseControlRowData>();
    CourseControlRowData control1 = new CourseControlRowData();
    control1.setControlNr(32L);
    course.add(control1);
    allCourseControlVariants.add(course);
    CourseCopyUtility.createVariantsFromAllCourses(1L, 10L, 101L, 31L, allCourseControlVariants);
  }

  @Test(expected = VetoException.class)
  public void testGetFirstControlOfFirstCourse1() throws Exception {
    CourseCopyUtility.getFirstControlOfFirstCourse(null);
  }

  @Test(expected = VetoException.class)
  public void testGetFirstControlOfFirstCourse2() throws Exception {
    CourseCopyUtility.getFirstControlOfFirstCourse(new ArrayList<List<CourseControlRowData>>());
  }

  @Test(expected = VetoException.class)
  public void testGetFirstControlOfFirstCourse3() throws Exception {
    List<List<CourseControlRowData>> list = new ArrayList<List<CourseControlRowData>>();
    list.add(new ArrayList<CourseControlRowData>());
    CourseCopyUtility.getFirstControlOfFirstCourse(list);
  }

  @Test
  public void testGetFirstControlOfFirstCourse4() throws Exception {
    List<List<CourseControlRowData>> list = new ArrayList<List<CourseControlRowData>>();
    List<CourseControlRowData> innerList = new ArrayList<CourseControlRowData>();
    CourseControlRowData control = new CourseControlRowData();
    innerList.add(control);
    list.add(innerList);
    CourseControlRowData result = CourseCopyUtility.getFirstControlOfFirstCourse(list);
    Assert.assertEquals("First Control", control, result);
  }

  @Test
  public void testRow2FormData1() throws Exception {
    CourseControlRowData row = new CourseControlRowData();
    row.setMandatory(false);
    row.setCountLeg(true);
    CourseControlFormData control = CourseCopyUtility.row2formData(row, 1L, 10L, CourseForkTypeCodeType.ButterflyCode.ID, 100L, "1000", 10000L);
    Assert.assertEquals("Event", 1L, control.getEventNr().longValue());
    Assert.assertEquals("Course", 10L, control.getCourse().getValue().longValue());
    Assert.assertEquals("Fork Type", CourseForkTypeCodeType.ButterflyCode.ID, control.getForkType().getValue());
    Assert.assertEquals("Master", 100L, control.getForkMasterCourseControl().getValue().longValue());
    Assert.assertEquals("Variant", "1000", control.getForkVariantCode().getValue());
    Assert.assertEquals("SortCode", 10000L, control.getSortCode().getValue().longValue());
    Assert.assertEquals("Count Leg", row.isCountLeg(), control.getCountLeg().getValue().booleanValue());
    Assert.assertEquals("Mandatory", row.isMandatory(), control.getMandatory().getValue().booleanValue());
  }

  @Test
  public void testRow2FormData2() throws Exception {
    CourseControlRowData row = new CourseControlRowData();
    row.setMandatory(false);
    row.setCountLeg(true);
    row.setControlNr(777L);
    CourseControlFormData control = CourseCopyUtility.row2formData(row, 1L, 10L, CourseForkTypeCodeType.ButterflyCode.ID, 100L, "1000", 10000L);
    for (AbstractFormFieldData field : control.getFields()) {
      Assert.assertTrue(field.isValueSet());
      Assert.assertNotNull("field is empty: " + field.getFieldId(), ((AbstractValueFieldData<?>) field).getValue());
    }
  }
}
