package com.rtiming.client.event.course.loop;

import static com.rtiming.client.event.course.loop.CourseLoopCalculatorTestUtility.buildControl;
import static com.rtiming.client.event.course.loop.CourseLoopCalculatorTestUtility.buildCourse;
import static com.rtiming.client.event.course.loop.CourseLoopCalculatorTestUtility.buildPunch;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.race.RaceControlsTablePage;
import com.rtiming.client.test.data.ControlTestData;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.services.code.CourseForkTypeCodeType;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class CourseLoopCalculatorClientTest {

  private EventWithIndividualValidatedRaceTestDataProvider event;

  @Test
  public void testSimpleCourse() throws Exception {
    List<ControlTestData> controls = buildCourse(
        buildControl("31", 1L, null, null, null, null),
        buildControl("32", 2L, null, null, null, null)
        );
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);

    assertRaceTable(new String[]{"31", "32"}, new Long[]{ControlStatusCodeType.OkCode.ID, ControlStatusCodeType.OkCode.ID});
  }

  @Test
  public void testLoop1AB() throws Exception {
    List<ControlTestData> controls = buildLoop1();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("32"),
        buildPunch("34"),
        buildPunch("32"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testLoop1BA() throws Exception {
    List<ControlTestData> controls = buildLoop1();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("34"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("32"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testLoop2ABXY() throws Exception {
    List<ControlTestData> controls = buildLoop2();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("34"),
        buildPunch("40"),
        buildPunch("34"),
        buildPunch("41"),
        buildPunch("34"),
        buildPunch("32"),
        buildPunch("50"),
        buildPunch("51"),
        buildPunch("32"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testLoop2BAXY() throws Exception {
    List<ControlTestData> controls = buildLoop2();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("50"),
        buildPunch("51"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("34"),
        buildPunch("40"),
        buildPunch("34"),
        buildPunch("41"),
        buildPunch("34"),
        buildPunch("32"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testLoop2ABYX() throws Exception {
    List<ControlTestData> controls = buildLoop2();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("34"),
        buildPunch("41"),
        buildPunch("34"),
        buildPunch("40"),
        buildPunch("34"),
        buildPunch("32"),
        buildPunch("50"),
        buildPunch("51"),
        buildPunch("32"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testLoop2BAYX() throws Exception {
    List<ControlTestData> controls = buildLoop2();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("50"),
        buildPunch("51"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("34"),
        buildPunch("41"),
        buildPunch("34"),
        buildPunch("40"),
        buildPunch("34"),
        buildPunch("32"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60, 65}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testFreeorderLoop1() throws Exception {
    List<ControlTestData> controls = buildFreeorderLoop1();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("40"),
        buildPunch("41"),
        buildPunch("32"),
        buildPunch("50"),
        buildPunch("51"),
        buildPunch("32"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35, 40, 45}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testFreeorderLoop2() throws Exception {
    List<ControlTestData> controls = buildFreeorderLoop1();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("41"),
        buildPunch("40"),
        buildPunch("32"),
        buildPunch("50"),
        buildPunch("51"),
        buildPunch("32"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35, 40, 45}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testFreeorderLoop3() throws Exception {
    List<ControlTestData> controls = buildFreeorderLoop1();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("50"),
        buildPunch("51"),
        buildPunch("32"),
        buildPunch("40"),
        buildPunch("41"),
        buildPunch("32"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35, 40, 45}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testFreeorderLoop4() throws Exception {
    List<ControlTestData> controls = buildFreeorderLoop2();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("50"),
        buildPunch("51"),
        buildPunch("32"),
        buildPunch("40"),
        buildPunch("41"),
        buildPunch("32"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35, 40, 45}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testFreeorderLoop5() throws Exception {
    List<ControlTestData> controls = buildFreeorderLoop2();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("51"),
        buildPunch("50"),
        buildPunch("32"),
        buildPunch("41"),
        buildPunch("40"),
        buildPunch("32"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35, 40, 45}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testFreeorderLoopMissingPunch() throws Exception {
    List<ControlTestData> controls = buildFreeorderLoop1();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("41"),
        buildPunch("40"),
        buildPunch("32"),
        buildPunch("50"),
        buildPunch("51"),
        buildPunch("35")
        );
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, new Integer[]{5, 10, 15, 20, 25, 30, 35, 40, 45}, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTable(new String[]{"31", "32", "41", "40", "32", "50", "51", "32", "35"},
        new Long[]{
            ControlStatusCodeType.OkCode.ID,
            ControlStatusCodeType.OkCode.ID,
            ControlStatusCodeType.OkCode.ID,
            ControlStatusCodeType.OkCode.ID,
            ControlStatusCodeType.OkCode.ID,
            ControlStatusCodeType.OkCode.ID,
            ControlStatusCodeType.OkCode.ID,
            ControlStatusCodeType.MissingCode.ID,
            ControlStatusCodeType.OkCode.ID
        });
  }

  @Test
  public void testRegiomila2014A1() throws Exception {
    List<ControlTestData> controls = buildRegiomila2014A();
    List<ControlTestData> punches = buildCourse(
        buildPunch("37"),
        buildPunch("38"),
        buildPunch("39"),
        buildPunch("36"),
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("34"),
        buildPunch("35"),
        buildPunch("40"),
        buildPunch("50"),
        // Phi-Loop
        buildPunch("49"),
        buildPunch("42"),
        buildPunch("41"),
        buildPunch("48"),
        buildPunch("44"),
        buildPunch("46"),
        buildPunch("47"),
        buildPunch("50"),
        buildPunch("48"),
        buildPunch("43"),
        buildPunch("44"),
        buildPunch("45"),
        // Phi-Loop End
        buildPunch("48"),
        buildPunch("51"),
        buildPunch("52"),
        buildPunch("53"),
        buildPunch("54"),
        buildPunch("56"),
        buildPunch("57"),
        buildPunch("58"),
        buildPunch("103"),
        buildPunch("102"),
        buildPunch("105"),
        buildPunch("107"),
        buildPunch("109"),
        buildPunch("108"),
        buildPunch("106"),
        buildPunch("110"),
        buildPunch("105"),
        buildPunch("104"),
        buildPunch("103"),
        buildPunch("101"),
        buildPunch("59"),
        buildPunch("60"));
    Integer[] times = buildTimes(punches);
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, times, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
    System.out.println("A: Event: " + event.getEventNr() + "Course: " + event.getCourseNr());
  }

  @Test
  public void testRegiomila2014A2() throws Exception {
    List<ControlTestData> controls = buildRegiomila2014A();
    List<ControlTestData> punches = buildCourse(
        buildPunch("37"),
        buildPunch("38"),
        buildPunch("39"),
        buildPunch("36"),
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("34"),
        buildPunch("35"),
        buildPunch("40"),
        buildPunch("50"),
        // Phi-Loop
        buildPunch("49"),
        buildPunch("42"),
        buildPunch("41"),
        buildPunch("48"),
        buildPunch("43"),
        buildPunch("44"),
        buildPunch("45"),
        buildPunch("48"),
        buildPunch("44"),
        buildPunch("46"),
        buildPunch("47"),
        buildPunch("50"),
        // Phi-Loop End
        buildPunch("48"),
        buildPunch("51"),
        buildPunch("52"),
        buildPunch("53"),
        buildPunch("54"),
        buildPunch("56"),
        buildPunch("57"),
        buildPunch("58"),
        buildPunch("103"),
        buildPunch("102"),
        buildPunch("105"),
        buildPunch("107"),
        buildPunch("109"),
        buildPunch("108"),
        buildPunch("106"),
        buildPunch("110"),
        buildPunch("105"),
        buildPunch("104"),
        buildPunch("103"),
        buildPunch("101"),
        buildPunch("59"),
        buildPunch("60"));
    Integer[] times = buildTimes(punches);
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, times, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testRegiomila2014A3() throws Exception {
    List<ControlTestData> controls = buildRegiomila2014A();
    List<ControlTestData> punches = buildCourse(
        buildPunch("37"),
        buildPunch("38"),
        buildPunch("39"),
        buildPunch("36"),
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("34"),
        buildPunch("35"),
        buildPunch("40"),
        buildPunch("50"),
        // Phi-Loop
        buildPunch("48"),
        buildPunch("43"),
        buildPunch("44"),
        buildPunch("45"),
        buildPunch("48"),
        buildPunch("44"),
        buildPunch("46"),
        buildPunch("47"),
        buildPunch("50"),
        buildPunch("49"),
        buildPunch("42"),
        buildPunch("41"),
        // Phi-Loop End
        buildPunch("48"),
        buildPunch("51"),
        buildPunch("52"),
        buildPunch("53"),
        buildPunch("54"),
        buildPunch("56"),
        buildPunch("57"),
        buildPunch("58"),
        buildPunch("103"),
        buildPunch("102"),
        buildPunch("105"),
        buildPunch("107"),
        buildPunch("109"),
        buildPunch("108"),
        buildPunch("106"),
        buildPunch("110"),
        buildPunch("105"),
        buildPunch("104"),
        buildPunch("103"),
        buildPunch("101"),
        buildPunch("59"),
        buildPunch("60"));
    Integer[] times = buildTimes(punches);
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, times, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testRegiomila2014A4() throws Exception {
    List<ControlTestData> controls = buildRegiomila2014A();
    List<ControlTestData> punches = buildCourse(
        buildPunch("37"),
        buildPunch("38"),
        buildPunch("39"),
        buildPunch("36"),
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("34"),
        buildPunch("35"),
        buildPunch("40"),
        buildPunch("50"),
        // Phi-Loop
        buildPunch("48"),
        buildPunch("44"),
        buildPunch("46"),
        buildPunch("47"),
        buildPunch("50"),
        buildPunch("49"),
        buildPunch("42"),
        buildPunch("41"),
        buildPunch("48"),
        buildPunch("43"),
        buildPunch("44"),
        buildPunch("45"),
        // Phi-Loop End
        buildPunch("48"),
        buildPunch("51"),
        buildPunch("52"),
        buildPunch("53"),
        buildPunch("54"),
        buildPunch("56"),
        buildPunch("57"),
        buildPunch("58"),
        buildPunch("103"),
        buildPunch("102"),
        buildPunch("105"),
        buildPunch("107"),
        buildPunch("109"),
        buildPunch("108"),
        buildPunch("106"),
        buildPunch("110"),
        buildPunch("105"),
        buildPunch("104"),
        buildPunch("103"),
        buildPunch("101"),
        buildPunch("59"),
        buildPunch("60"));
    Integer[] times = buildTimes(punches);
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, times, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testRegiomila2014B1() throws Exception {
    List<ControlTestData> controls = buildRegiomila2014B();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("34"),
        buildPunch("35"),
        buildPunch("40"),
        buildPunch("50"),
        // Phi-Loop Start
        buildPunch("49"),
        buildPunch("42"),
        buildPunch("41"),
        buildPunch("48"),
        buildPunch("44"),
        buildPunch("46"),
        buildPunch("47"),
        buildPunch("50"),
        // Phi-Loop End
        buildPunch("48"),
        buildPunch("51"),
        buildPunch("52"),
        buildPunch("56"),
        buildPunch("57"),
        buildPunch("58"),
        buildPunch("103"),
        buildPunch("102"),
        buildPunch("105"),
        buildPunch("107"),
        buildPunch("106"),
        buildPunch("110"),
        buildPunch("105"),
        buildPunch("104"),
        buildPunch("103"),
        buildPunch("60"));
    Integer[] times = buildTimes(punches);
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, times, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
    System.out.println("B: Event: " + event.getEventNr() + "Course: " + event.getCourseNr());
  }

  @Test
  public void testRegiomila2014B2() throws Exception {
    List<ControlTestData> controls = buildRegiomila2014B();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("33"),
        buildPunch("34"),
        buildPunch("35"),
        buildPunch("40"),
        buildPunch("50"),
        // Phi-Loop Start
        buildPunch("48"),
        buildPunch("44"),
        buildPunch("46"),
        buildPunch("47"),
        buildPunch("50"),
        buildPunch("49"),
        buildPunch("42"),
        buildPunch("41"),
        // Phi-Loop End
        buildPunch("48"),
        buildPunch("51"),
        buildPunch("52"),
        buildPunch("56"),
        buildPunch("57"),
        buildPunch("58"),
        buildPunch("103"),
        buildPunch("102"),
        buildPunch("105"),
        buildPunch("107"),
        buildPunch("106"),
        buildPunch("110"),
        buildPunch("105"),
        buildPunch("104"),
        buildPunch("103"),
        buildPunch("60"));
    Integer[] times = buildTimes(punches);
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, times, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testRegiomila2014C1() throws Exception {
    List<ControlTestData> controls = buildRegiomila2014C();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("34"),
        buildPunch("35"),
        buildPunch("40"),
        buildPunch("50"),
        // Phi-Loop Start
        buildPunch("48"),
        buildPunch("44"),
        buildPunch("46"),
        buildPunch("47"),
        buildPunch("50"),
        buildPunch("49"),
        buildPunch("42"),
        buildPunch("41"),
        // Phi-Loop End
        buildPunch("48"),
        buildPunch("51"),
        buildPunch("52"),
        buildPunch("56"),
        buildPunch("57"),
        buildPunch("58"),
        buildPunch("103"),
        buildPunch("102"),
        buildPunch("104"),
        buildPunch("103"),
        buildPunch("101"),
        buildPunch("59"),
        buildPunch("60"));
    Integer[] times = buildTimes(punches);
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, times, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
    System.out.println("C: Event: " + event.getEventNr() + "Course: " + event.getCourseNr());
  }

  @Test
  public void testRegiomila2014C2() throws Exception {
    List<ControlTestData> controls = buildRegiomila2014C();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("34"),
        buildPunch("35"),
        buildPunch("40"),
        buildPunch("50"),
        // Phi-Loop Start
        buildPunch("49"),
        buildPunch("42"),
        buildPunch("41"),
        buildPunch("48"),
        buildPunch("44"),
        buildPunch("46"),
        buildPunch("47"),
        buildPunch("50"),
        // Phi-Loop End
        buildPunch("48"),
        buildPunch("51"),
        buildPunch("52"),
        buildPunch("56"),
        buildPunch("57"),
        buildPunch("58"),
        buildPunch("103"),
        buildPunch("102"),
        buildPunch("104"),
        buildPunch("103"),
        buildPunch("101"),
        buildPunch("59"),
        buildPunch("60"));
    Integer[] times = buildTimes(punches);
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, times, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
  }

  @Test
  public void testRegiomila2014D() throws Exception {
    List<ControlTestData> controls = buildRegiomila2014D();
    List<ControlTestData> punches = buildCourse(
        buildPunch("31"),
        buildPunch("32"),
        buildPunch("34"),
        buildPunch("35"),
        buildPunch("40"),
        buildPunch("50"),
        buildPunch("47"),
        buildPunch("52"),
        buildPunch("56"),
        buildPunch("57"),
        buildPunch("58"),
        buildPunch("101"),
        buildPunch("103"),
        buildPunch("60"));
    Integer[] times = buildTimes(punches);
    event = new EventWithIndividualValidatedRaceTestDataProvider(controls, punches, 0, 1000, times, CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);
    assertRaceTableOk(punches);
    System.out.println("D: Event: " + event.getEventNr() + "Course: " + event.getCourseNr());
  }

  private Integer[] buildTimes(List<ControlTestData> punches) {
    Integer[] times = new Integer[punches.size()];
    for (int k = 0; k < punches.size(); k++) {
      times[k] = k * 10;
    }
    return times;
  }

  private List<ControlTestData> buildLoop1() {
    return buildCourse(
        buildControl("31", 1L, null, null, null, null),
        buildControl("32", 2L, CourseForkTypeCodeType.ButterflyCode.ID, null, null, null),
        buildControl("33", 3L, null, "32", 2L, "A"),
        buildControl("34", 4L, null, "32", 2L, "B"),
        buildControl("35", 5L, null, null, null, null));
  }

  private List<ControlTestData> buildLoop2() {
    return buildCourse(
        buildControl("31", 1L, null, null, null, null),
        buildControl("32", 2L, CourseForkTypeCodeType.ButterflyCode.ID, null, null, null),
        buildControl("33", 3L, null, "32", 2L, "A"),
        buildControl("34", 4L, CourseForkTypeCodeType.ButterflyCode.ID, "32", 2L, "A"),
        buildControl("40", 5L, null, "34", 4L, "X"),
        buildControl("41", 6L, null, "34", 4L, "Y"),
        buildControl("50", 7L, null, "32", 2L, "B"),
        buildControl("51", 8L, null, "32", 2L, "B"),
        buildControl("35", 9L, null, null, null, null));
  }

  private List<ControlTestData> buildFreeorderLoop1() {
    return buildCourse(
        buildControl("31", 1L, null, null, null, null),
        buildControl("32", 2L, CourseForkTypeCodeType.ButterflyCode.ID, null, null, null),
        buildControl("40", 3L, null, "32", 2L, "A"),
        buildControl("41", 3L, null, "32", 2L, "A"),
        buildControl("50", 4L, null, "32", 2L, "B"),
        buildControl("51", 5L, null, "32", 2L, "B"),
        buildControl("35", 6L, null, null, null, null));
  }

  private List<ControlTestData> buildFreeorderLoop2() {
    return buildCourse(
        buildControl("31", 1L, null, null, null, null),
        buildControl("32", 2L, CourseForkTypeCodeType.ButterflyCode.ID, null, null, null),
        buildControl("40", 3L, null, "32", 2L, "A"),
        buildControl("41", 3L, null, "32", 2L, "A"),
        buildControl("50", 4L, null, "32", 2L, "B"),
        buildControl("51", 4L, null, "32", 2L, "B"),
        buildControl("35", 5L, null, null, null, null));
  }

  private List<ControlTestData> buildRegiomila2014A() {
    return buildCourse(
        buildControl("37", 1L, null, null, null, null),
        buildControl("38", 2L, null, null, null, null),
        buildControl("39", 3L, null, null, null, null),
        buildControl("36", 4L, null, null, null, null),
        buildControl("31", 5L, null, null, null, null),
        buildControl("32", 6L, null, null, null, null),
        buildControl("33", 7L, null, null, null, null),
        buildControl("34", 8L, null, null, null, null),
        buildControl("35", 9L, null, null, null, null),
        buildControl("40", 10L, null, null, null, null),
        buildControl("50", 11L, CourseForkTypeCodeType.ForkCode.ID, null, null, null),
        // Phi-Loop Variant A
        buildControl("49", 12L, null, "50", 11L, "A"),
        buildControl("42", 13L, null, "50", 11L, "A"),
        buildControl("41", 14L, null, "50", 11L, "A"),
        buildControl("48", 15L, null, "50", 11L, "A"),
        buildControl("44", 16L, null, "50", 11L, "A"),
        buildControl("46", 17L, null, "50", 11L, "A"),
        buildControl("47", 18L, null, "50", 11L, "A"),
        buildControl("50", 19L, null, "50", 11L, "A"),
        buildControl("48", 20L, null, "50", 11L, "A"),
        buildControl("43", 21L, null, "50", 11L, "A"),
        buildControl("44", 22L, null, "50", 11L, "A"),
        buildControl("45", 23L, null, "50", 11L, "A"),
        // Phi-Loop Variant B
        buildControl("48", 25L, null, "50", 11L, "B"),
        buildControl("44", 26L, null, "50", 11L, "B"),
        buildControl("46", 27L, null, "50", 11L, "B"),
        buildControl("47", 28L, null, "50", 11L, "B"),
        buildControl("50", 29L, null, "50", 11L, "B"),
        buildControl("49", 30L, null, "50", 11L, "B"),
        buildControl("42", 31L, null, "50", 11L, "B"),
        buildControl("41", 32L, null, "50", 11L, "B"),
        buildControl("48", 33L, null, "50", 11L, "B"),
        buildControl("43", 34L, null, "50", 11L, "B"),
        buildControl("44", 35L, null, "50", 11L, "B"),
        buildControl("45", 36L, null, "50", 11L, "B"),
        // Phi-Loop Variant C
        buildControl("48", 40L, null, "50", 11L, "C"),
        buildControl("43", 41L, null, "50", 11L, "C"),
        buildControl("44", 42L, null, "50", 11L, "C"),
        buildControl("45", 43L, null, "50", 11L, "C"),
        buildControl("48", 44L, null, "50", 11L, "C"),
        buildControl("44", 45L, null, "50", 11L, "C"),
        buildControl("46", 46L, null, "50", 11L, "C"),
        buildControl("47", 47L, null, "50", 11L, "C"),
        buildControl("50", 48L, null, "50", 11L, "C"),
        buildControl("49", 49L, null, "50", 11L, "C"),
        buildControl("42", 50L, null, "50", 11L, "C"),
        buildControl("41", 51L, null, "50", 11L, "C"),
        // Phi-Loop Variant D
        buildControl("49", 55L, null, "50", 11L, "D"),
        buildControl("42", 56L, null, "50", 11L, "D"),
        buildControl("41", 57L, null, "50", 11L, "D"),
        buildControl("48", 58L, null, "50", 11L, "D"),
        buildControl("43", 59L, null, "50", 11L, "D"),
        buildControl("44", 60L, null, "50", 11L, "D"),
        buildControl("45", 61L, null, "50", 11L, "D"),
        buildControl("48", 62L, null, "50", 11L, "D"),
        buildControl("44", 63L, null, "50", 11L, "D"),
        buildControl("46", 64L, null, "50", 11L, "D"),
        buildControl("47", 65L, null, "50", 11L, "D"),
        buildControl("50", 66L, null, "50", 11L, "D"),
        // End Phi-Loops
        buildControl("48", 70L, null, null, null, null),
        buildControl("51", 75L, null, null, null, null),
        buildControl("52", 80L, null, null, null, null),
        buildControl("53", 90L, null, null, null, null),
        buildControl("54", 100L, null, null, null, null),
        buildControl("56", 110L, null, null, null, null),
        buildControl("57", 120L, null, null, null, null),
        buildControl("58", 130L, null, null, null, null),
        buildControl("103", 140L, null, null, null, null),
        buildControl("102", 150L, null, null, null, null),
        buildControl("105", 160L, null, null, null, null),
        buildControl("107", 170L, null, null, null, null),
        buildControl("109", 180L, null, null, null, null),
        buildControl("108", 190L, null, null, null, null),
        buildControl("106", 200L, null, null, null, null),
        buildControl("110", 210L, null, null, null, null),
        buildControl("105", 220L, null, null, null, null),
        buildControl("104", 230L, null, null, null, null),
        buildControl("103", 240L, null, null, null, null),
        buildControl("101", 250L, null, null, null, null),
        buildControl("59", 260L, null, null, null, null),
        buildControl("60", 270L, null, null, null, null));
  }

  private List<ControlTestData> buildRegiomila2014B() {
    return buildCourse(
        buildControl("31", 1L, null, null, null, null),
        buildControl("32", 2L, null, null, null, null),
        buildControl("33", 3L, null, null, null, null),
        buildControl("34", 4L, null, null, null, null),
        buildControl("35", 5L, null, null, null, null),
        buildControl("40", 6L, null, null, null, null),
        buildControl("50", 10L, CourseForkTypeCodeType.ForkCode.ID, null, null, null),
        // Phi Loop Variant A
        buildControl("49", 20L, null, "50", 10L, "A"),
        buildControl("42", 30L, null, "50", 10L, "A"),
        buildControl("41", 40L, null, "50", 10L, "A"),
        buildControl("48", 50L, null, "50", 10L, "A"),
        buildControl("44", 60L, null, "50", 10L, "A"),
        buildControl("46", 70L, null, "50", 10L, "A"),
        buildControl("47", 80L, null, "50", 10L, "A"),
        buildControl("50", 90L, null, "50", 10L, "A"),
        // Phi Loop Variant B
        buildControl("48", 91L, null, "50", 10L, "B"),
        buildControl("44", 92L, null, "50", 10L, "B"),
        buildControl("46", 93L, null, "50", 10L, "B"),
        buildControl("47", 94L, null, "50", 10L, "B"),
        buildControl("50", 95L, null, "50", 10L, "B"),
        buildControl("49", 96L, null, "50", 10L, "B"),
        buildControl("42", 97L, null, "50", 10L, "B"),
        buildControl("41", 98L, null, "50", 10L, "B"),
        // End Phi Loop
        buildControl("48", 100L, null, null, null, null),
        buildControl("51", 110L, null, null, null, null),
        buildControl("52", 120L, null, null, null, null),
        buildControl("56", 130L, null, null, null, null),
        buildControl("57", 140L, null, null, null, null),
        buildControl("58", 150L, null, null, null, null),
        buildControl("103", 160L, null, null, null, null),
        buildControl("102", 170L, null, null, null, null),
        buildControl("105", 180L, null, null, null, null),
        buildControl("107", 190L, null, null, null, null),
        buildControl("106", 200L, null, null, null, null),
        buildControl("110", 210L, null, null, null, null),
        buildControl("105", 220L, null, null, null, null),
        buildControl("104", 230L, null, null, null, null),
        buildControl("103", 240L, null, null, null, null),
        buildControl("60", 250L, null, null, null, null));
  }

  private List<ControlTestData> buildRegiomila2014C() {
    return buildCourse(
        buildControl("31", 1L, null, null, null, null),
        buildControl("32", 2L, null, null, null, null),
        buildControl("34", 3L, null, null, null, null),
        buildControl("35", 4L, null, null, null, null),
        buildControl("40", 5L, null, null, null, null),
        buildControl("50", 10L, CourseForkTypeCodeType.ForkCode.ID, null, null, null),
        // Phi Loop Variant A
        buildControl("49", 20L, null, "50", 10L, "A"),
        buildControl("42", 30L, null, "50", 10L, "A"),
        buildControl("41", 40L, null, "50", 10L, "A"),
        buildControl("48", 50L, null, "50", 10L, "A"),
        buildControl("44", 60L, null, "50", 10L, "A"),
        buildControl("46", 70L, null, "50", 10L, "A"),
        buildControl("47", 80L, null, "50", 10L, "A"),
        buildControl("50", 90L, null, "50", 10L, "A"),
        // Phi Loop Variant B
        buildControl("48", 91L, null, "50", 10L, "B"),
        buildControl("44", 92L, null, "50", 10L, "B"),
        buildControl("46", 93L, null, "50", 10L, "B"),
        buildControl("47", 94L, null, "50", 10L, "B"),
        buildControl("50", 95L, null, "50", 10L, "B"),
        buildControl("49", 96L, null, "50", 10L, "B"),
        buildControl("42", 97L, null, "50", 10L, "B"),
        buildControl("41", 98L, null, "50", 10L, "B"),
        // End Phi Loop
        buildControl("48", 100L, null, null, null, null),
        buildControl("51", 110L, null, null, null, null),
        buildControl("52", 120L, null, null, null, null),
        buildControl("56", 130L, null, null, null, null),
        buildControl("57", 140L, null, null, null, null),
        buildControl("58", 150L, null, null, null, null),
        buildControl("103", 160L, null, null, null, null),
        buildControl("102", 170L, null, null, null, null),
        buildControl("104", 180L, null, null, null, null),
        buildControl("103", 190L, null, null, null, null),
        buildControl("101", 200L, null, null, null, null),
        buildControl("59", 210L, null, null, null, null),
        buildControl("60", 220L, null, null, null, null));
  }

  private List<ControlTestData> buildRegiomila2014D() {
    return buildCourse(
        buildControl("31", 1L, null, null, null, null),
        buildControl("32", 2L, null, null, null, null),
        buildControl("34", 3L, null, null, null, null),
        buildControl("35", 4L, null, null, null, null),
        buildControl("40", 5L, null, null, null, null),
        buildControl("50", 6L, null, null, null, null),
        buildControl("47", 7L, null, null, null, null),
        buildControl("52", 8L, null, null, null, null),
        buildControl("56", 9L, null, null, null, null),
        buildControl("57", 10L, null, null, null, null),
        buildControl("58", 20L, null, null, null, null),
        buildControl("101", 30L, null, null, null, null),
        buildControl("103", 40L, null, null, null, null),
        buildControl("60", 50L, null, null, null, null));
  }

  private void assertRaceTableOk(List<ControlTestData> punches) throws ProcessingException {
    List<String> controlNos = new ArrayList<>();
    List<Long> statusUids = new ArrayList<>();
    for (ControlTestData punch : punches) {
      controlNos.add(punch.getControlNo());
      statusUids.add(ControlStatusCodeType.OkCode.ID);
    }
    assertRaceTable(controlNos.toArray(new String[controlNos.size()]), statusUids.toArray(new Long[statusUids.size()]));
  }

  private void assertRaceTable(String[] controlNos, Long[] statusUids) throws ProcessingException {
    RaceControlsTablePage page = new RaceControlsTablePage(event.getRaceNr());
    page.nodeAddedNotify();
    page.loadChildren();

    Assert.assertEquals("Same size", controlNos.length, statusUids.length);
    Assert.assertEquals("Row count", controlNos.length, page.getTable().getRowCount());
    Assert.assertEquals("Row count", statusUids.length, page.getTable().getRowCount());
    for (int k = 0; k < controlNos.length; k++) {
      Assert.assertEquals("Control No", controlNos[k], page.getTable().getControlColumn().getValue(k));
      Assert.assertEquals("Status", statusUids[k], page.getTable().getControlStatusColumn().getValue(k));
    }
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
  }

}
