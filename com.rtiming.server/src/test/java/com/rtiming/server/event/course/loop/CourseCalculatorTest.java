package com.rtiming.server.event.course.loop;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.server.race.RaceSettings;
import com.rtiming.server.race.validation.RaceValidationResult;
import com.rtiming.server.race.validation.ValidationTestUtility;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.race.RaceStatusCodeType;

/**
 * @author amo
 */
public class CourseCalculatorTest {

  @Test
  public void testCalculateCourse1() throws Exception {
    List<List<CourseControlRowData>> result = CourseCalculator.calculateCourse(createSingleLoopCourse1());
    Assert.assertNotNull(result);
    Assert.assertEquals("2 courses", 2, result.size());
  }

  @Test
  public void testCalculateCourse2() throws Exception {
    List<List<CourseControlRowData>> result = CourseCalculator.calculateCourse(ValidationTestUtility.createControlDefList(ValidationTestUtility.createControlDef(31L, 1L, null, null)));
    Assert.assertNotNull(result);
    Assert.assertEquals("1 course", 1, result.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateAllVariantsNull1() throws ProcessingException {
    CourseCalculator.validateAllVariants(null, null, null, null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateAllVariantsNull2() throws ProcessingException {
    CourseCalculator.validateAllVariants(new ArrayList<RaceControlBean>(), new ArrayList<CourseControlRowData>(), null, null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateAllVariantsNull3() throws ProcessingException {
    CourseCalculator.validateAllVariants(null, new ArrayList<CourseControlRowData>(), null, null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateAllVariantsNull4() throws ProcessingException {
    CourseCalculator.validateAllVariants(new ArrayList<RaceControlBean>(), null, null, new RaceSettings(), null, null, null, null);
  }

  @Test(expected = VetoException.class)
  public void testValidateAllVariantsNoECardAssigned() throws ProcessingException {
    CourseCalculator.validateAllVariants(new ArrayList<RaceControlBean>(), new ArrayList<CourseControlRowData>(), new ArrayList<RaceControlBean>(), new RaceSettings(), null, null, null, null);
  }

  @Test
  public void testValidateAllVariants1() throws ProcessingException {
    RaceValidationResult result = CourseCalculator.validateAllVariants(new ArrayList<RaceControlBean>(), new ArrayList<CourseControlRowData>(), new ArrayList<RaceControlBean>(), new RaceSettings(), null, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.NoStartTimeCode.ID, result.getStatusUid());
  }

  @Test
  public void testValidateAllVariants2() throws ProcessingException {
    RaceValidationResult result = CourseCalculator.validateAllVariants(new ArrayList<RaceControlBean>(), new ArrayList<CourseControlRowData>(), new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
  }

  @Test
  public void testValidateAllVariantsSimpleCourseMissingPunch() throws ProcessingException {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(32L, 2L, null, null)
        );
    List<RaceControlBean> punches = new ArrayList<RaceControlBean>();
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 2, result.getControls().size());
    Assert.assertEquals("Controls", "31", result.getControls().get(0).getControlNo());
    Assert.assertEquals("Controls", "32", result.getControls().get(1).getControlNo());
  }

  @Test
  public void testValidateAllVariantsSimpleCourseOK() throws ProcessingException {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(32L, 2L, null, null)
        );
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "32");
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 2, result.getControls().size());
    Assert.assertEquals("Controls", "31", result.getControls().get(0).getControlNo());
    Assert.assertEquals("Controls", "32", result.getControls().get(1).getControlNo());
  }

  @Test
  public void testValidateAllVariantsManualStatus1() throws ProcessingException {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(32L, 2L, null, null)
        );
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "32");
    List<RaceControlBean> manualControls = ValidationTestUtility.buildControlList("32");
    manualControls.get(0).setManualStatus(true);
    manualControls.get(0).setCourseControlNr(32L);
    manualControls.get(0).setControlStatusUid(ControlStatusCodeType.OkCode.ID);
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, manualControls, new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 2, result.getControls().size());
    Assert.assertEquals("Controls", "31", result.getControls().get(0).getControlNo());
    Assert.assertEquals("Controls", "32", result.getControls().get(1).getControlNo());
  }

  @Test
  public void testValidateAllVariantsManualStatus2() throws ProcessingException {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(32L, 2L, null, null)
        );
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "32");
    List<RaceControlBean> manualControls = ValidationTestUtility.buildControlList("32");
    manualControls.get(0).setManualStatus(true);
    manualControls.get(0).setCourseControlNr(32L);
    manualControls.get(0).setControlStatusUid(ControlStatusCodeType.WrongCode.ID);
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, manualControls, new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 2, result.getControls().size());
    Assert.assertEquals("Controls", "31", result.getControls().get(0).getControlNo());
    Assert.assertEquals("Controls", "32", result.getControls().get(1).getControlNo());
    Assert.assertEquals("Status", ControlStatusCodeType.WrongCode.ID, result.getControls().get(1).getControlStatusUid().longValue());
  }

  @Test
  public void testValidateAllVariantsManualStatus3() throws ProcessingException {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(32L, 2L, null, null)
        );
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "32");
    List<RaceControlBean> manualControls = ValidationTestUtility.buildControlList("32");
    manualControls.get(0).setManualStatus(false);
    manualControls.get(0).setCourseControlNr(32L);
    manualControls.get(0).setControlStatusUid(ControlStatusCodeType.WrongCode.ID);
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, manualControls, new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 2, result.getControls().size());
    Assert.assertEquals("Controls", "31", result.getControls().get(0).getControlNo());
    Assert.assertEquals("Controls", "32", result.getControls().get(1).getControlNo());
  }

  @Test
  public void testValidateAllVariantsManualControl1() throws ProcessingException {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(32L, 2L, null, null)
        );
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "32");
    ArrayList<RaceControlBean> manualControls = new ArrayList<RaceControlBean>();
    RaceControlBean bean = new RaceControlBean();
    bean.setCourseControlNr(31L);
    bean.setManualStatus(true);
    bean.setControlStatusUid(ControlStatusCodeType.OkCode.ID);
    manualControls.add(bean);
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, manualControls, new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 2, result.getControls().size());
    Assert.assertEquals("Controls", "31", result.getControls().get(0).getControlNo());
    Assert.assertEquals("Controls", "32", result.getControls().get(1).getControlNo());
    Assert.assertTrue("Manual Status", result.getControls().get(0).isManualStatus());
  }

  @Test
  public void testValidateAllVariantsManualControl2() throws ProcessingException {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(32L, 2L, null, null)
        );
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("32");
    ArrayList<RaceControlBean> manualControls = new ArrayList<RaceControlBean>();
    RaceControlBean bean = new RaceControlBean();
    bean.setCourseControlNr(31L);
    bean.setManualStatus(true);
    bean.setControlStatusUid(ControlStatusCodeType.OkCode.ID);
    manualControls.add(bean);
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, manualControls, new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 2, result.getControls().size());
    Assert.assertEquals("Controls", "31", result.getControls().get(0).getControlNo());
    Assert.assertEquals("Controls", "32", result.getControls().get(1).getControlNo());
    Assert.assertTrue("Manual Status", result.getControls().get(0).isManualStatus());
  }

  @Test
  public void testValidateAllVariantsManualTime1() throws ProcessingException {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(1L, 1L, null, null),
        ValidationTestUtility.createControlDef(31L, 2L, null, null),
        ValidationTestUtility.createControlDef(32L, 3L, null, null),
        ValidationTestUtility.createControlDef(99L, 4L, null, null)
        );
    courseControls.get(0).setTypeUid(ControlTypeCodeType.StartCode.ID);
    courseControls.get(3).setTypeUid(ControlTypeCodeType.FinishCode.ID);
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "32");
    punches.get(0).setPunchTime(500L);
    List<RaceControlBean> manualControls = ValidationTestUtility.buildControlList("32");
    manualControls.get(0).setManualStatus(true);
    manualControls.get(0).setCourseControlNr(32L);
    manualControls.get(0).setControlStatusUid(ControlStatusCodeType.OkCode.ID);
    manualControls.get(0).setPunchTime(3333L);
    RaceSettings settings = new RaceSettings();
    settings.setUsingStartlist(false);
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, manualControls, settings, 1000L, 4000L, 1000L, 1L);
    assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    assertEquals("Controls", 4, result.getControls().size());
    assertEquals("Controls", "31", result.getControls().get(1).getControlNo());
    assertEquals("Controls", "32", result.getControls().get(2).getControlNo());
    assertEquals("Time", 500, result.getControls().get(1).getPunchTime().longValue());
    assertEquals("Time", 3333, result.getControls().get(2).getPunchTime().longValue());
  }

  @Test
  public void testValidateAllVariantsCourseWithLoopMissingPunch1() throws ProcessingException {
    List<CourseControlRowData> courseControls = createSingleLoopCourse1();
    // MP: 31 (the third time) is missing
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "50", "51", "31", "40", "41", "99");
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 8, result.getControls().size());

    assertControlNos(result.getControls(), "31", "50", "51", "31", "40", "41", "31", "99");
    assertControlStatusUids(result.getControls(),
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.MissingCode.ID,
        ControlStatusCodeType.OkCode.ID);
  }

  @Test
  public void testValidateAllVariantsCourseWithLoopMissingPunch2() throws ProcessingException {
    List<CourseControlRowData> courseControls = createSingleLoopCourse1();
    // MP: 51 and 50 are switched
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "51", "50", "31", "40", "41", "31", "99");
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 9, result.getControls().size());

    assertControlNos(result.getControls(), "31", "50", "51", "31", "40", "41", "31", "99", "50");
    assertControlStatusUids(result.getControls(),
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.MissingCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.WrongCode.ID);
  }

  @Test
  public void testValidateAllVariantsCourseWithLoopMissingPunch3() throws ProcessingException {
    List<CourseControlRowData> courseControls = createSingleLoopCourse1();
    // MP: 31 (the first time) is missing
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("40", "41", "31", "50", "51", "31", "99");
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 8, result.getControls().size());

    assertControlNos(result.getControls(), "31", "40", "41", "31", "50", "51", "31", "99");
    assertControlStatusUids(result.getControls(),
        ControlStatusCodeType.MissingCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID);
  }

  @Test
  public void testValidateAllVariantsFreeorder1() throws ProcessingException {
    List<CourseControlRowData> courseControls = createFreeorderLoopCourse1();
    // 40 and 41 punched in free order
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "41", "40", "31", "50", "51", "31", "99");
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 8, result.getControls().size());

    assertControlNos(result.getControls(), "31", "41", "40", "31", "50", "51", "31", "99");
    assertControlStatusUidsOk(result.getControls());
  }

  @Test
  public void testValidateAllVariantsFreeorder2() throws ProcessingException {
    List<CourseControlRowData> courseControls = createFreeorderLoopCourse1();
    // 40 and 41 punched in free order
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "50", "51", "31", "40", "41", "31", "99");
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 8, result.getControls().size());

    assertControlNos(result.getControls(), "31", "50", "51", "31", "40", "41", "31", "99");
    assertControlStatusUidsOk(result.getControls());
  }

  @Test
  public void testValidateAllVariantsFreeorder3() throws ProcessingException {
    List<CourseControlRowData> courseControls = createFreeorderLoopCourse1();
    // 40 and 41 punched in free order
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "50", "51", "31", "41", "40", "31", "99");
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 8, result.getControls().size());

    assertControlNos(result.getControls(), "31", "50", "51", "31", "41", "40", "31", "99");
    assertControlStatusUidsOk(result.getControls());
  }

  @Test
  public void testValidateAllVariantsFreeorder4() throws ProcessingException {
    List<CourseControlRowData> courseControls = createFreeorderLoopCourse2();
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "50", "51", "31", "41", "40", "31", "99");
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 8, result.getControls().size());

    assertControlNos(result.getControls(), "31", "50", "51", "31", "41", "40", "31", "99");
    assertControlStatusUidsOk(result.getControls());
  }

  @Test
  public void testValidateAllVariantsFreeorder5() throws ProcessingException {
    List<CourseControlRowData> courseControls = createFreeorderLoopCourse2();
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "51", "50", "31", "41", "40", "31", "99");
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 8, result.getControls().size());

    assertControlNos(result.getControls(), "31", "51", "50", "31", "41", "40", "31", "99");
    assertControlStatusUidsOk(result.getControls());
  }

  @Test
  public void testValidateAllVariantsFreeorderMissingPunch() throws ProcessingException {
    List<CourseControlRowData> courseControls = createFreeorderLoopCourse1();
    // 40 and 41 punched in free order, but 41 missing
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "50", "51", "31", "40", "31", "99");
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, new ArrayList<RaceControlBean>(), new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.MissingPunchCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 8, result.getControls().size());

    assertControlNos(result.getControls(), "31", "50", "51", "31", "40", "41", "31", "99");
    assertControlStatusUids(result.getControls(),
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.MissingCode.ID,
        ControlStatusCodeType.OkCode.ID,
        ControlStatusCodeType.OkCode.ID);
  }

  @Test
  public void testValidateAllVariantsShiftTime1() throws ProcessingException {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(32L, 2L, null, null)
        );
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "32");
    ArrayList<RaceControlBean> manualControls = new ArrayList<RaceControlBean>();
    RaceControlBean bean = new RaceControlBean();
    bean.setCourseControlNr(31L);
    bean.setManualStatus(false);
    bean.setShiftTime(7894L);
    manualControls.add(bean);
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, manualControls, new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 2, result.getControls().size());
    Assert.assertEquals("Controls", "31", result.getControls().get(0).getControlNo());
    Assert.assertEquals("Controls", "32", result.getControls().get(1).getControlNo());
    Assert.assertFalse("Manual Status", result.getControls().get(0).isManualStatus());
    Assert.assertEquals("Shift Time", 7894, result.getControls().get(0).getShiftTime().longValue());
  }

  @Test
  public void testValidateAllVariantsShiftTime2() throws ProcessingException {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(32L, 2L, null, null)
        );
    List<RaceControlBean> punches = ValidationTestUtility.buildControlList("31", "32");
    List<RaceControlBean> manualControls = ValidationTestUtility.buildControlList("31");
    manualControls.get(0).setManualStatus(false);
    manualControls.get(0).setCourseControlNr(31L);
    manualControls.get(0).setShiftTime(4444L);
    RaceValidationResult result = CourseCalculator.validateAllVariants(punches, courseControls, manualControls, new RaceSettings(), 1000L, null, null, 1L);
    Assert.assertEquals("Status", RaceStatusCodeType.OkCode.ID, result.getStatusUid());
    Assert.assertEquals("Controls", 2, result.getControls().size());
    Assert.assertEquals("Controls", "31", result.getControls().get(0).getControlNo());
    Assert.assertEquals("Controls", "32", result.getControls().get(1).getControlNo());
    Assert.assertEquals("Status", 4444, result.getControls().get(0).getShiftTime().longValue());
  }

  private List<CourseControlRowData> createSingleLoopCourse1() {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(40L, 2L, 31L, "A"),
        ValidationTestUtility.createControlDef(41L, 3L, 31L, "A"),
        ValidationTestUtility.createControlDef(50L, 4L, 31L, "B"),
        ValidationTestUtility.createControlDef(51L, 5L, 31L, "B"),
        ValidationTestUtility.createControlDef(99L, 6L, null, null));
    return courseControls;
  }

  private List<CourseControlRowData> createFreeorderLoopCourse1() {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(40L, 2L, 31L, "A"),
        ValidationTestUtility.createControlDef(41L, 2L, 31L, "A"),
        ValidationTestUtility.createControlDef(50L, 4L, 31L, "B"),
        ValidationTestUtility.createControlDef(51L, 5L, 31L, "B"),
        ValidationTestUtility.createControlDef(99L, 6L, null, null));
    return courseControls;
  }

  private List<CourseControlRowData> createFreeorderLoopCourse2() {
    List<CourseControlRowData> courseControls = ValidationTestUtility.createControlDefList(
        ValidationTestUtility.createControlDef(31L, 1L, null, null),
        ValidationTestUtility.createControlDef(40L, 2L, 31L, "A"),
        ValidationTestUtility.createControlDef(41L, 2L, 31L, "A"),
        ValidationTestUtility.createControlDef(50L, 3L, 31L, "B"),
        ValidationTestUtility.createControlDef(51L, 3L, 31L, "B"),
        ValidationTestUtility.createControlDef(99L, 4L, null, null));
    return courseControls;
  }

  private void assertControlStatusUidsOk(List<RaceControlBean> controls) {
    for (RaceControlBean control : controls) {
      Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, control.getControlStatusUid().longValue());
    }
  }

  private void assertControlNos(List<RaceControlBean> controls, String... controlNos) {
    Assert.assertEquals("Same size", controls.size(), controlNos.length);
    int k = 0;
    for (RaceControlBean control : controls) {
      Assert.assertEquals("Control No", controlNos[k], control.getControlNo());
      k++;
    }
  }

  private void assertControlStatusUids(List<RaceControlBean> controls, Long... controlStatusUids) {
    Assert.assertEquals("Same size", controls.size(), controlStatusUids.length);
    int k = 0;
    for (RaceControlBean control : controls) {
      Assert.assertEquals("Control Status", controlStatusUids[k], control.getControlStatusUid());
      k++;
    }
  }

}
