package com.rtiming.server.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.shared.TEXTS;
import org.junit.Test;

import com.rtiming.shared.dao.RtClassAge;

public class ClassAgeUtilityTest {

  @Test
  public void testCalculateClassAge1() throws Exception {
    List<String> result = ClassAgeUtility.calculateAgeText(null);
    assertEquals("1 row", 1, result.size());
    assertNotNull("text", result.get(0));
    assertTrue(result.get(0).contains(TEXTS.get("NoAgeLimit")));
  }

  @Test
  public void testCalculateClassAge2() throws Exception {
    List<RtClassAge> list = new ArrayList<>();
    List<String> result = ClassAgeUtility.calculateAgeText(list);
    assertEquals("1 row", 1, result.size());
    assertNotNull("text", result.get(0));
    assertTrue(result.get(0).contains(TEXTS.get("NoAgeLimit")));
  }

  @Test
  public void testCalculateClassAge3() throws Exception {
    List<RtClassAge> list = new ArrayList<>();
    list.add(new RtClassAge());
    List<String> result = ClassAgeUtility.calculateAgeText(list);
    assertEquals("1 row", 1, result.size());
    assertNotNull("text", result.get(0));
    assertTrue(result.get(0).contains(TEXTS.get("NoAgeLimit")));
  }

  @Test
  public void testCalculateClassAge4() throws Exception {
    List<RtClassAge> list = new ArrayList<>();
    RtClassAge classAge = new RtClassAge();
    classAge.setAgeFrom(15L);
    classAge.setAgeTo(77L);
    list.add(classAge);
    List<String> result = ClassAgeUtility.calculateAgeText(list);
    assertEquals("1 row", 1, result.size());
    assertNotNull("text", result.get(0));
    assertFalse(result.get(0).contains(TEXTS.get("NoAgeLimit")));
    assertTrue(result.get(0).contains("15 - 77"));
  }
}
