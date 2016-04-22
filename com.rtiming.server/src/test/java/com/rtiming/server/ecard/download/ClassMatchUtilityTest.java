package com.rtiming.server.ecard.download;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Test;

import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtClassAgeKey;
import com.rtiming.shared.ecard.download.PunchFormData;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.runner.SexCodeType;

public class ClassMatchUtilityTest {

  @Test(expected = IllegalArgumentException.class)
  public void testMatchClassesArguments1() throws Exception {
    ClassMatchUtility.matchClasses(null, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMatchClassesArguments2() throws Exception {
    ClassMatchUtility.matchClasses(1L, null, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMatchClassesArguments3() throws Exception {
    ClassMatchUtility.matchClasses(null, null, null, new EventConfiguration(), new ArrayList<RtClassAge>());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMatchClassesArguments4() throws Exception {
    ClassMatchUtility.matchClasses(null, null, null, new EventConfiguration(), null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMatchClassesArguments5() throws Exception {
    ClassMatchUtility.matchClasses(null, null, null, null, new ArrayList<RtClassAge>());
  }

  @Test
  public void testMatchClasses1() throws Exception {
    Set<EventClassFormData> result = ClassMatchUtility.matchClasses(3L, null, null, new EventConfiguration(), new ArrayList<RtClassAge>());
    assertTrue("no classes found", result.isEmpty());
  }

  @Test
  public void testMatchClasses2() throws Exception {
    EventConfiguration config = createSingleEvent();

    Set<EventClassFormData> result = ClassMatchUtility.matchClasses(3L, null, null, config, new ArrayList<RtClassAge>());
    assertTrue("no classes found", result.isEmpty());
  }

  @Test
  public void testMatchClasses3() throws Exception {
    EventConfiguration config = createSingleEvent();
    EventClassFormData clazz = createClazz(1L);
    config.addClass(clazz);

    Set<EventClassFormData> result = ClassMatchUtility.matchClasses(3L, null, null, config, new ArrayList<RtClassAge>());
    assertEquals("1 clazz found", 1, result.size());
    assertTrue("class", result.toArray(new EventClassFormData[result.size()])[0].getClazz().getValue() == 77);
  }

  @Test
  public void testMatchClasses4() throws Exception {
    EventConfiguration config = createSingleEvent();
    EventClassFormData clazz = createClazz(1L);
    config.addClass(clazz);

    List<RtClassAge> ageConfig = createAgeConfig(1L, SexCodeType.ManCode.ID, null, null);

    Set<EventClassFormData> result = ClassMatchUtility.matchClasses(3L, SexCodeType.WomanCode.ID, null, config, ageConfig);
    assertEquals("0 clazz found", 0, result.size());
  }

  @Test
  public void testMatchClasses5() throws Exception {
    EventConfiguration config = createSingleEvent();
    EventClassFormData clazz = createClazz(1L);
    config.addClass(clazz);

    List<RtClassAge> ageConfig = createAgeConfig(1L, SexCodeType.WomanCode.ID, null, null);

    Set<EventClassFormData> result = ClassMatchUtility.matchClasses(3L, SexCodeType.WomanCode.ID, null, config, ageConfig);
    assertEquals("1 clazz found", 1, result.size());
    assertTrue("class", result.toArray(new EventClassFormData[result.size()])[0].getClazz().getValue() == 77);
  }

  @Test
  public void testMatchClasses6() throws Exception {
    EventConfiguration config = createSingleEvent();
    EventClassFormData clazz = createClazz(1L);
    config.addClass(clazz);

    List<RtClassAge> ageConfig = createAgeConfig(1L, SexCodeType.WomanCode.ID, 0L, 99L);

    Set<EventClassFormData> result = ClassMatchUtility.matchClasses(3L, SexCodeType.WomanCode.ID, null, config, ageConfig);
    assertEquals("1 clazz found", 1, result.size());
    assertTrue("class", result.toArray(new EventClassFormData[result.size()])[0].getClazz().getValue() == 77);
  }

  @Test
  public void testMatchClasses7() throws Exception {
    EventConfiguration config = createSingleEvent();
    EventClassFormData clazz = createClazz(1L);
    config.addClass(clazz);

    List<RtClassAge> ageConfig = createAgeConfig(1L, SexCodeType.WomanCode.ID, 80L, 99L);

    Set<EventClassFormData> result = ClassMatchUtility.matchClasses(3L, SexCodeType.WomanCode.ID, 1975L, config, ageConfig);
    assertEquals("0 clazz found", 0, result.size());
  }

  @Test
  public void testMatchClasses8() throws Exception {
    EventConfiguration config = createSingleEvent();
    EventClassFormData clazz = createClazz(2L); // different clientNr
    config.addClass(clazz);

    List<RtClassAge> ageConfig = createAgeConfig(2L, SexCodeType.WomanCode.ID, 0L, 99L); // different clientNr 

    Set<EventClassFormData> result = ClassMatchUtility.matchClasses(3L, SexCodeType.WomanCode.ID, null, config, ageConfig);
    assertEquals("0 clazz found", 0, result.size());
  }

  @Test
  public void testMatch1() throws Exception {
    ClazzMatchCandidate result = ClassMatchUtility.match(null, null);
    assertNull("empty", result);
  }

  @Test
  public void testMatch2() throws Exception {
    ClazzMatchCandidate result = ClassMatchUtility.match(new ArrayList<ClazzMatchCandidate>(), null);
    assertNull("empty", result);
  }

  @Test
  public void testMatch3() throws Exception {
    List<ClazzMatchCandidate> candidates = new ArrayList<ClazzMatchCandidate>();
    candidates.add(createCandidate(77L, "31"));

    List<PunchFormData> punches = createPunches("31");

    ClazzMatchCandidate result = ClassMatchUtility.match(candidates, punches);
    assertNotNull("empty", result);
    assertEquals("Clazz", 77L, result.getClassUid().longValue());
  }

  @Test
  public void testMatch4() throws Exception {
    List<ClazzMatchCandidate> candidates = new ArrayList<ClazzMatchCandidate>();
    candidates.add(createCandidate(77L, "31", "32", "33"));

    List<PunchFormData> punches = createPunches("31");

    ClazzMatchCandidate result = ClassMatchUtility.match(candidates, punches);
    assertNotNull("empty", result);
    assertEquals("Clazz", 77L, result.getClassUid().longValue());
  }

  @Test
  public void testMatch5() throws Exception {
    List<ClazzMatchCandidate> candidates = new ArrayList<ClazzMatchCandidate>();
    candidates.add(createCandidate(77L, "31", "34", "35"));
    candidates.add(createCandidate(78L, "31", "32", "34"));

    List<PunchFormData> punches = createPunches("31", "32", "33");

    ClazzMatchCandidate result = ClassMatchUtility.match(candidates, punches);
    assertNotNull("empty", result);
    assertEquals("Clazz", 78L, result.getClassUid().longValue());
  }

  @Test
  public void testMatch6() throws Exception {
    List<ClazzMatchCandidate> candidates = new ArrayList<ClazzMatchCandidate>();
    candidates.add(createCandidate(77L, "31", "34", "35"));
    candidates.add(createCandidate(78L, "31", "32", "33"));

    List<PunchFormData> punches = createPunches("31", "32", "33");

    ClazzMatchCandidate result = ClassMatchUtility.match(candidates, punches);
    assertNotNull("empty", result);
    assertEquals("Clazz", 78L, result.getClassUid().longValue());
  }

  @Test
  public void testMatch7() throws Exception {
    List<ClazzMatchCandidate> candidates = new ArrayList<ClazzMatchCandidate>();
    candidates.add(createCandidate(77L, "31", "34", "35"));

    List<List<CourseControlRowData>> courses = new ArrayList<>();
    courses.add(createCourse("33", "32", "31"));
    courses.add(createCourse("31", "88", "99"));
    courses.add(createCourse("31", "99", "33"));
    ClazzMatchCandidate candidate = new ClazzMatchCandidate(78L, 99L, courses);
    candidates.add(candidate);

    List<PunchFormData> punches = createPunches("31", "32", "33");

    ClazzMatchCandidate result = ClassMatchUtility.match(candidates, punches);
    assertNotNull("empty", result);
    assertEquals("Clazz", 78L, result.getClassUid().longValue());
  }

  private ClazzMatchCandidate createCandidate(Long classUid, String... controlNos) {
    List<List<CourseControlRowData>> variants = new ArrayList<>();
    List<CourseControlRowData> course = createCourse(controlNos);
    variants.add(course);

    return new ClazzMatchCandidate(classUid, 88L, variants);
  }

  private List<PunchFormData> createPunches(String... controlNos) {
    List<PunchFormData> punches = new ArrayList<PunchFormData>();
    for (String controlNo : controlNos) {
      PunchFormData punch = new PunchFormData();
      punch.getControlNo().setValue(controlNo);
      punches.add(punch);
    }
    return punches;
  }

  private List<CourseControlRowData> createCourse(String... controlNos) {
    List<CourseControlRowData> course = new ArrayList<>();
    long k = 0;
    for (String controlNo : controlNos) {
      CourseControlRowData control = new CourseControlRowData();
      control.setControlNo(controlNo);
      control.setControlNr(k);
      control.setCourseControlNr(k);
      control.setSortCode(k);
      control.setMandatory(true);
      control.setCountLeg(true);
      control.setTypeUid(ControlTypeCodeType.ControlCode.ID);
      course.add(control);
      k++;
    }
    return course;
  }

  private List<RtClassAge> createAgeConfig(Long clientNr, Long sexUid, Long ageFrom, Long ageTo) {
    List<RtClassAge> ageConfig = new ArrayList<RtClassAge>();
    RtClassAge clazzAge = new RtClassAge();
    clazzAge.setClassUid(77L);
    RtClassAgeKey key = new RtClassAgeKey();
    key.setClientNr(clientNr);
    clazzAge.setId(key);
    clazzAge.setSexUid(sexUid);
    clazzAge.setAgeFrom(ageFrom);
    clazzAge.setAgeTo(ageTo);
    ageConfig.add(clazzAge);
    return ageConfig;
  }

  private EventClassFormData createClazz(Long clientNr) {
    EventClassFormData clazz = new EventClassFormData();
    clazz.setClientNr(clientNr);
    clazz.getEvent().setValue(3L);
    clazz.getClazz().setValue(77L);
    return clazz;
  }

  private EventConfiguration createSingleEvent() {
    EventConfiguration config = new EventConfiguration();
    EventBean event = new EventBean();
    event.setEventNr(3L);
    event.setClientNr(1L);
    event.setEvtZero(DateUtility.parse("25092014", "ddMMyyyy"));
    config.addEvents(event);
    return config;
  }

}
