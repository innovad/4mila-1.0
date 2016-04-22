package com.rtiming.server.event.course.loop;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.server.race.validation.ValidationTestUtility;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.services.code.CourseForkTypeCodeType;

public class CourseLoopCalculatorTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNull() throws Exception {
    CourseLoopCalculator.calculateCourse(null, null);
  }

  @Test
  public void testEmpty() throws Exception {
    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(new ArrayList<CourseControlRowData>(), CourseForkTypeCodeType.ButterflyCode.ID);
    Assert.assertNotNull("Result not empty", result);
    Assert.assertEquals("1 Row", 1, result.size());
    Assert.assertEquals("0 Controls", 0, result.get(0).size());
  }

  @Test
  public void testSimpleCourse1() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(31L, 1L, null, null));

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);

    assertCourseVariants(new Long[][]{{31L}}, result);
  }

  @Test
  public void testSimpleCourse2() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(32L, 2L, null, null), createControlDef(33L, 3L, null, null));

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);

    assertCourseVariants(new Long[][]{{31L, 32L, 33L}}, result);
  }

  @Test
  public void testSimpleCourseFreeOrder() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(32L, 2L, null, null), createControlDef(33L, 2L, null, null), createControlDef(34L, 3L, null, null));

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);

    assertCourseVariants(new Long[][]{{31L, 32L, 33L, 34L}}, result);
  }

  @Test
  public void testDummyLoopWithOneVariant() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(1L, 31L, 1L, null, null), createControlDef(2L, 32L, 2L, 1L, "X"), createControlDef(3L, 33L, 2L, 1L, "X"), createControlDef(4L, 34L, 3L, null, null));

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);

    assertCourseVariants(new Long[][]{{31L, 32L, 33L, 31L, 34L}}, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMissingMasterControl() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(32L, 2L, 99L, "X"), createControlDef(33L, 3L, null, null));

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);

    assertCourseVariants(new Long[][]{{31L, 33L}}, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMasterControl1() throws Exception {
    CourseLoopCalculator.calculateCourse(createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(32L, 2L, 32L, "X"), createControlDef(33L, 3L, null, null)), CourseForkTypeCodeType.ButterflyCode.ID);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMasterControl2() throws Exception {
    CourseLoopCalculator.calculateCourse(createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(32L, 2L, 32L, "X"), createControlDef(32L, 3L, 32L, "Y"), createControlDef(33L, 4L, null, null)), CourseForkTypeCodeType.ButterflyCode.ID);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidMasterControl3() throws Exception {
    CourseLoopCalculator.calculateCourse(createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(32L, 2L, 32L, null), createControlDef(33L, 3L, null, null)), CourseForkTypeCodeType.ButterflyCode.ID);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmptyVariant() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(32L, 2L, 31L, null), createControlDef(33L, 3L, null, null));
    CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);
  }

  @Test
  public void testSingleLoop1() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(32L, 2L, 31L, "A"), createControlDef(33L, 3L, 31L, "B"), createControlDef(34L, 4L, null, null));

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);

    assertCourseVariants(new Long[][]{{31L, 33L, 31L, 32L, 31L, 34L}, {31L, 32L, 31L, 33L, 31L, 34L}}, result);
    assertCourseVariants(new Long[][]{{31L, 32L, 31L, 33L, 31L, 34L}, {31L, 33L, 31L, 32L, 31L, 34L}}, result);
  }

  @Test
  public void testSingleLoop2() throws Exception {
    List<CourseControlRowData> definition = createSingleLoopCourse1();

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);

    assertCourseVariants(new Long[][]{{31L, 40L, 41L, 31L, 50L, 51L, 31L, 99L}, {31L, 50L, 51L, 31L, 40L, 41L, 31L, 99L}
    }, result);
  }

  @Test
  public void testSingleLoop3() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(40L, 2L, 31L, "A"), createControlDef(41L, 3L, 31L, "A"), createControlDef(50L, 4L, 31L, "B"), createControlDef(51L, 5L, 31L, "B"), createControlDef(60L, 6L, 31L, "C"), createControlDef(61L, 7L, 31L, "C"), createControlDef(99L, 8L, null, null));

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);

    assertCourseVariants(new Long[][]{{31L, 40L, 41L, 31L, 50L, 51L, 31L, 60L, 61L, 31L, 99L}, {31L, 50L, 51L, 31L, 40L, 41L, 31L, 60L, 61L, 31L, 99L}, {31L, 40L, 41L, 31L, 60L, 61L, 31L, 50L, 51L, 31L, 99L}, {31L, 50L, 51L, 31L, 60L, 61L, 31L, 40L, 41L, 31L, 99L}, {31L, 60L, 61L, 31L, 50L, 51L, 31L, 40L, 41L, 31L, 99L}, {31L, 60L, 61L, 31L, 40L, 41L, 31L, 50L, 51L, 31L, 99L}
    }, result);
  }

  @Test
  public void testSingleLoopMultipleMaster1() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(1L, 31L, 1L, null, null), createControlDef(2L, 31L, 2L, null, null), createControlDef(3L, 32L, 3L, 2L, "A"), createControlDef(4L, 33L, 4L, 2L, "B"), createControlDef(5L, 34L, 5L, null, null));

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);

    assertCourseVariants(new Long[][]{{31L, 31L, 33L, 31L, 32L, 31L, 34L}, {31L, 31L, 32L, 31L, 33L, 31L, 34L}}, result);
  }

  @Test
  public void testSingleLoopMultipleMaster2() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(1L, 31L, 1L, null, null), createControlDef(2L, 31L, 2L, null, null), createControlDef(3L, 32L, 3L, 2L, "A"), createControlDef(4L, 33L, 4L, 2L, "B"), createControlDef(5L, 31L, 5L, null, null));

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);

    assertCourseVariants(new Long[][]{{31L, 31L, 33L, 31L, 32L, 31L, 31L}, {31L, 31L, 32L, 31L, 33L, 31L, 31L}}, result);
  }

  @Test
  public void testDoubleLoop() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(32L, 2L, 31L, "A"), createControlDef(33L, 3L, 31L, "A"), createControlDef(34L, 4L, 31L, "B"), createControlDef(35L, 5L, 31L, "B"), createControlDef(88L, 6L, null, null), createControlDef(40L, 7L, 88L, "A"), createControlDef(41L, 8L, 88L, "B"), createControlDef(99L, 9L, null, null));

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);

    assertCourseVariants(new Long[][]{{31L, 34L, 35L, 31L, 32L, 33L, 31L, 88L, 40L, 88L, 41L, 88L, 99L}, {31L, 34L, 35L, 31L, 32L, 33L, 31L, 88L, 41L, 88L, 40L, 88L, 99L}, {31L, 32L, 33L, 31L, 34L, 35L, 31L, 88L, 40L, 88L, 41L, 88L, 99L}, {31L, 32L, 33L, 31L, 34L, 35L, 31L, 88L, 41L, 88L, 40L, 88L, 99L}
    }, result);
  }

  @Test
  public void testNestedLoops() throws Exception {
    List<CourseControlRowData> definition = createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(32L, 2L, 31L, "A"), createControlDef(33L, 3L, 31L, "B"), createControlDef(34L, 4L, 32L, "X"), createControlDef(35L, 5L, 32L, "Y"));

    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(definition, CourseForkTypeCodeType.ButterflyCode.ID);
    assertCourseVariants(new Long[][]{{31L, 32L, 35L, 32L, 34L, 32L, 31L, 33L, 31L}, {31L, 32L, 34L, 32L, 35L, 32L, 31L, 33L, 31L}, {31L, 33L, 31L, 32L, 34L, 32L, 35L, 32L, 31L}, {31L, 33L, 31L, 32L, 35L, 32L, 34L, 32L, 31L}
    }, result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConvertToRaceControls1() throws Exception {
    CourseLoopCalculator.convertToRaceControls(null, null);
  }

  @Test
  public void testConvertToRaceControls2() throws Exception {
    List<RaceControlBean> result = CourseLoopCalculator.convertToRaceControls(new ArrayList<CourseControlRowData>(), null);
    Assert.assertEquals("0 Controls", 0, result.size());
  }

  @Test
  public void testConvertToRaceControls3() throws Exception {
    CourseControlRowData row1 = new CourseControlRowData();
    List<CourseControlRowData> input = createControlDefList(row1);
    List<RaceControlBean> result = CourseLoopCalculator.convertToRaceControls(input, null);
    Assert.assertEquals("1 Control", 1, result.size());
    Assert.assertEquals("Sort Order", 1, result.get(0).getSortcode().longValue());
  }

  @Test
  public void testConvertToRaceControls4() throws Exception {
    CourseControlRowData row1 = new CourseControlRowData();
    row1.setTypeUid(ControlTypeCodeType.StartCode.ID);
    List<CourseControlRowData> input = createControlDefList(row1);
    List<RaceControlBean> result = CourseLoopCalculator.convertToRaceControls(input, null);
    Assert.assertEquals("1 Control", 1, result.size());
    Assert.assertEquals("Sort Order Start", 0, result.get(0).getSortcode().longValue());
  }

  @Test
  public void testConvertToRaceControls5() throws Exception {
    CourseControlRowData row1 = new CourseControlRowData();
    row1.setTypeUid(ControlTypeCodeType.ControlCode.ID);
    List<CourseControlRowData> input = createControlDefList(row1);
    List<RaceControlBean> result = CourseLoopCalculator.convertToRaceControls(input, null);
    Assert.assertEquals("1 Control", 1, result.size());
    Assert.assertEquals("Sort Order Control", 1, result.get(0).getSortcode().longValue());
  }

  @Test
  public void testConvertToRaceControls6() throws Exception {
    // Free Order Sort Codes
    List<CourseControlRowData> courseControls = createControlDefList(createControlDef(31L, 3L, null, null), createControlDef(32L, 3L, null, null));
    List<RaceControlBean> result = CourseLoopCalculator.convertToRaceControls(courseControls, null);
    Assert.assertEquals("2 Controls", 2, result.size());
    Assert.assertEquals("Sort Order Control", 1, result.get(0).getSortcode().longValue());
    Assert.assertEquals("Sort Order Control", 1, result.get(1).getSortcode().longValue());
  }

  @Test
  public void testConvertToRaceControls7() throws Exception {
    // Normal Sort Codes
    List<CourseControlRowData> courseControls = createControlDefList(createControlDef(31L, 3L, null, null), createControlDef(32L, 5L, null, null));
    List<RaceControlBean> result = CourseLoopCalculator.convertToRaceControls(courseControls, null);
    Assert.assertEquals("2 Controls", 2, result.size());
    Assert.assertEquals("Sort Order Control", 1, result.get(0).getSortcode().longValue());
    Assert.assertEquals("Sort Order Control", 2, result.get(1).getSortcode().longValue());
  }

  @Test
  public void testCalculateForks1() throws Exception {
    CourseLoopCalculator.calculateCourse(ValidationTestUtility.createControlDefList(), CourseForkTypeCodeType.ForkCode.ID);
  }

  @Test
  public void testCalculateForks2() throws Exception {
    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(ValidationTestUtility.createControlDefList(createControlDef(1L, 31L, 1L, null, null)), CourseForkTypeCodeType.ForkCode.ID);
    Assert.assertEquals("Result", 1, result.size());
  }

  @Test
  public void testCalculateForks3() throws Exception {
    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(ValidationTestUtility.createControlDefList(createControlDef(1L, 31L, 1L, null, null, CourseForkTypeCodeType.ForkCode.ID), createControlDef(2L, 32L, 2L, 1L, "A", null), createControlDef(3L, 33L, 3L, 1L, "B", null), createControlDef(4L, 34L, 4L, null, null, null)), CourseForkTypeCodeType.ForkCode.ID);
    Assert.assertEquals("Result", 2, result.size());
    Assert.assertEquals("3 Controls", 3, result.get(0).size());
    Assert.assertEquals("3 Controls", 3, result.get(1).size());
    assertCourseVariants(new Long[][]{{31L, 32L, 34L}, {31L, 33L, 34L}}, result);
  }

  @Test
  public void testCalculateForks4() throws Exception {
    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(ValidationTestUtility.createControlDefList(createControlDef(1L, 31L, 1L, null, null, CourseForkTypeCodeType.ForkCode.ID), createControlDef(2L, 32L, 2L, 1L, "A", null), createControlDef(3L, 33L, 3L, 1L, "B", null), createControlDef(4L, 34L, 4L, 1L, "C", null), createControlDef(5L, 35L, 5L, null, null, null)), CourseForkTypeCodeType.ForkCode.ID);
    Assert.assertEquals("Result", 3, result.size());
    assertCourseVariants(new Long[][]{{31L, 32L, 35L}, {31L, 33L, 35L}, {31L, 34L, 35L}
    }, result);
  }

  @Test
  public void testCalculateForks5() throws Exception {
    List<List<CourseControlRowData>> result = CourseLoopCalculator.calculateCourse(ValidationTestUtility.createControlDefList(createControlDef(1L, 31L, 1L, null, null, CourseForkTypeCodeType.ForkCode.ID), createControlDef(2L, 32L, 2L, 1L, "A", null), createControlDef(3L, 33L, 3L, 1L, "B", null), createControlDef(4L, 34L, 4L, 1L, "C", null), createControlDef(5L, 35L, 5L, null, null, null), createControlDef(6L, 36L, 6L, null, null, CourseForkTypeCodeType.ForkCode.ID), createControlDef(7L, 40L, 7L, 6L, "D", null), createControlDef(8L, 41L, 8L, 6L, "E", null)), CourseForkTypeCodeType.ForkCode.ID);
    Assert.assertEquals("Result", 6, result.size());
    assertCourseVariants(new Long[][]{{31L, 32L, 35L, 36L, 40L}, {31L, 33L, 35L, 36L, 40L}, {31L, 34L, 35L, 36L, 40L}, {31L, 32L, 35L, 36L, 41L}, {31L, 33L, 35L, 36L, 41L}, {31L, 34L, 35L, 36L, 41L}
    }, result);
  }

  private List<CourseControlRowData> createSingleLoopCourse1() {
    List<CourseControlRowData> courseControls = createControlDefList(createControlDef(31L, 1L, null, null), createControlDef(40L, 2L, 31L, "A"), createControlDef(41L, 3L, 31L, "A"), createControlDef(50L, 4L, 31L, "B"), createControlDef(51L, 5L, 31L, "B"), createControlDef(99L, 6L, null, null));
    return courseControls;
  }

  private CourseControlRowData createControlDef(Long controlNr, Long sortCode, Long masterControlNr, String variant) {
    return createControlDef(controlNr, controlNr, sortCode, masterControlNr, variant);
  }

  private CourseControlRowData createControlDef(Long courseControlNr, Long controlNr, Long sortCode, Long masterControlNr, String variant) {
    return createControlDef(courseControlNr, controlNr, sortCode, masterControlNr, variant, CourseForkTypeCodeType.ButterflyCode.ID);
  }

  private CourseControlRowData createControlDef(Long courseControlNr, Long controlNr, Long sortCode, Long masterControlNr, String variant, Long loopTypeUid) {
    CourseControlRowData control = new CourseControlRowData();
    control.setControlNo(String.valueOf(controlNr));
    control.setCourseControlNr(courseControlNr);
    control.setControlNr(controlNr);
    control.setSortCode(sortCode);
    control.setTypeUid(ControlTypeCodeType.ControlCode.ID);
    control.setForkMasterCourseControlNr(masterControlNr);
    control.setForkVariantCode(variant);
    control.setForkTypeUid(loopTypeUid);
    control.setMandatory(true);
    control.setCountLeg(true);
    return control;
  }

  private List<CourseControlRowData> createControlDefList(CourseControlRowData... controls) {
    List<CourseControlRowData> definition = new ArrayList<CourseControlRowData>();
    for (CourseControlRowData control : controls) {
      definition.add(control);
    }
    return definition;
  }

  private void assertCourseVariants(Long[][] expected, List<List<CourseControlRowData>> actual) {
    // test variant count
    Assert.assertEquals("Number of variants", expected.length, actual.size());

    // test control count
    int k = 0;
    for (Long[] expectedCourse : expected) {
      Assert.assertEquals("Number of controls", expectedCourse.length, actual.get(k).size());
      k++;
    }

    // test controls
    List<List<Long>> actualList = new ArrayList<>();
    for (List<CourseControlRowData> course : actual) {
      ArrayList<Long> list = new ArrayList<Long>();
      for (CourseControlRowData control : course) {
        list.add(control.getControlNr());
      }
      actualList.add(list);
    }
    Assert.assertEquals("Converted size", actualList.size(), actual.size());

    for (Long[] expectedCourse : expected) {
      for (List<Long> actualLong : actualList) {
        if (CompareUtility.equals(actualLong.toArray(new Long[actualLong.size()]), expectedCourse)) {
          boolean removed = actualList.remove(actualLong);
          Assert.assertTrue("Removed", removed);
          break;
        }
      }
    }
    Assert.assertEquals("0 courses remaining", 0, actualList.size());
  }

}
