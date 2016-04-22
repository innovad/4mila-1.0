package com.rtiming.server.race.validation;

import static com.rtiming.server.race.validation.ValidationTestUtility.buildControlList;
import static com.rtiming.server.race.validation.ValidationTestUtility.buildFreeOrderControlList;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.rtiming.server.race.RaceControlBean;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.race.RaceStatusCodeType;

import org.junit.Assert;

/**
 * @author amo
 */
public class FreeOrderUtilityTest {

  @Test(expected = IllegalArgumentException.class)
  public void testFreeOrder1() throws Exception {
    FreeOrderUtility.validateFreeOrderGroup(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFreeOrder2() throws Exception {
    List<RaceControlBean> freeOrderGroup = new ArrayList<RaceControlBean>();
    FreeOrderUtility.validateFreeOrderGroup(freeOrderGroup, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testFreeOrder3() throws Exception {
    List<RaceControlBean> punchedControls = new ArrayList<RaceControlBean>();
    FreeOrderUtility.validateFreeOrderGroup(null, punchedControls);
  }

  @Test
  public void testFreeOrder4() throws Exception {
    List<RaceControlBean> freeOrderGroup = buildFreeOrderControlList("31", "32");
    List<RaceControlBean> punchedControls = buildControlList("31", "32");

    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderGroup, punchedControls);
    Assert.assertEquals("Free Order", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
    Assert.assertEquals("Punched Controls not deleted", 2, punchedControls.size());
  }

  @Test
  public void testFreeOrder5() throws Exception {
    List<RaceControlBean> freeOrderGroup = buildFreeOrderControlList("31", "32");
    List<RaceControlBean> punchedControls = buildControlList("31", "32", "33");

    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderGroup, punchedControls);
    Assert.assertEquals("Free Order", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
    Assert.assertEquals("Punched Controls not deleted", 3, punchedControls.size());
    Assert.assertEquals("31", "31", punchedControls.get(0).getControlNo());
    Assert.assertEquals("32", "32", punchedControls.get(1).getControlNo());
    Assert.assertEquals("33", "33", punchedControls.get(2).getControlNo());
  }

  @Test
  public void testFreeOrder6() throws Exception {
    List<RaceControlBean> freeOrderGroup = buildFreeOrderControlList("31", "32");
    List<RaceControlBean> punchedControls = buildControlList("32");
    RaceControlBean control31 = freeOrderGroup.get(0);
    RaceControlBean control32 = freeOrderGroup.get(1);

    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderGroup, punchedControls);
    Assert.assertEquals("Free Order", RaceStatusCodeType.MissingPunchCode.ID, result.getRaceStatusUid());
    Assert.assertEquals("Punched Controls not deleted", 1, punchedControls.size());
    Assert.assertEquals("31 MP", ControlStatusCodeType.MissingCode.ID, control31.getControlStatusUid().longValue());
    Assert.assertEquals("32 OK", ControlStatusCodeType.OkCode.ID, control32.getControlStatusUid().longValue());
  }

  @Test
  public void testFreeOrder7() throws Exception {
    List<RaceControlBean> freeOrderGroup = buildFreeOrderControlList("31", "32");
    List<RaceControlBean> punchedControls = buildControlList("31");
    RaceControlBean control31 = freeOrderGroup.get(0);
    RaceControlBean control32 = freeOrderGroup.get(1);

    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderGroup, punchedControls);
    Assert.assertEquals("Free Order", RaceStatusCodeType.MissingPunchCode.ID, result.getRaceStatusUid());
    Assert.assertEquals("Punched Controls not deleted", 1, punchedControls.size());
    Assert.assertEquals("31 MP", ControlStatusCodeType.OkCode.ID, control31.getControlStatusUid().longValue());
    Assert.assertEquals("32 OK", ControlStatusCodeType.MissingCode.ID, control32.getControlStatusUid().longValue());
  }

  @Test
  public void testFreeOrder8() throws Exception {
    List<RaceControlBean> freeOrderGroup = buildFreeOrderControlList("31", "32");
    List<RaceControlBean> punchedControls = buildControlList("31", "31", "32");

    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderGroup, punchedControls);
    Assert.assertEquals("Free Order", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
    Assert.assertEquals("Punched Controls not deleted", 3, punchedControls.size());
    Assert.assertEquals("31", "31", punchedControls.get(0).getControlNo());
    Assert.assertEquals("31", "31", punchedControls.get(1).getControlNo());
    Assert.assertEquals("31", "32", punchedControls.get(2).getControlNo());
  }

  @Test
  public void testFreeOrderClearAndSize() throws Exception {
    List<RaceControlBean> freeOrderGroup = new ArrayList<RaceControlBean>();
    freeOrderGroup.add(new RaceControlBean());
    List<RaceControlBean> punchedControls = new ArrayList<RaceControlBean>();
    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderGroup, punchedControls);

    Assert.assertEquals("FreeOrderGroup cleared", 0, freeOrderGroup.size());
    Assert.assertEquals("Free Order", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testSort1() throws Exception {
    FreeOrderUtility.sortControlsBySortCodeAndTime(null);
  }

  @Test
  public void testSort2() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);
    Assert.assertEquals("0 size", 0, freeOrderControls.size());
  }

  @Test
  public void testSort3() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32");
    freeOrderControls.get(0).setPunchTime(2L);
    freeOrderControls.get(1).setPunchTime(1L);

    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);

    Assert.assertEquals("2 size", 2, freeOrderControls.size());
    Assert.assertEquals("Order 1", "32", freeOrderControls.get(0).getControlNo());
    Assert.assertEquals("Order 2", "31", freeOrderControls.get(1).getControlNo());
  }

  @Test
  public void testSort4() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32");
    freeOrderControls.get(1).setPunchTime(1L);

    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);

    Assert.assertEquals("2 size", 2, freeOrderControls.size());
    Assert.assertEquals("Order 1", "32", freeOrderControls.get(0).getControlNo());
    Assert.assertEquals("Order 2", "31", freeOrderControls.get(1).getControlNo());
  }

  @Test
  public void testSort5() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32");
    freeOrderControls.get(0).setPunchTime(1L);

    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);

    Assert.assertEquals("2 size", 2, freeOrderControls.size());
    Assert.assertEquals("Order 1", "31", freeOrderControls.get(0).getControlNo());
    Assert.assertEquals("Order 2", "32", freeOrderControls.get(1).getControlNo());
  }

  @Test
  public void testSort6() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32");

    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);

    Assert.assertEquals("2 size", 2, freeOrderControls.size());
    Assert.assertEquals("Order 1", "31", freeOrderControls.get(0).getControlNo());
    Assert.assertEquals("Order 2", "32", freeOrderControls.get(1).getControlNo());
  }

  @Test
  public void testSort7() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32");
    freeOrderControls.get(0).setSortcode(null);

    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);

    Assert.assertEquals("2 size", 2, freeOrderControls.size());
    Assert.assertEquals("Order 1", "32", freeOrderControls.get(0).getControlNo());
    Assert.assertEquals("Order 2", "31", freeOrderControls.get(1).getControlNo());
  }

  @Test
  public void testSort8() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32");
    freeOrderControls.get(0).setSortcode(null);
    freeOrderControls.get(1).setPunchTime(1L);

    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);

    Assert.assertEquals("2 size", 2, freeOrderControls.size());
    Assert.assertEquals("Order 1", "32", freeOrderControls.get(0).getControlNo());
    Assert.assertEquals("Order 2", "31", freeOrderControls.get(1).getControlNo());
  }

  @Test
  public void testSort9() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31");
    freeOrderControls.add(null);

    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);

    Assert.assertEquals("2 size", 2, freeOrderControls.size());
    Assert.assertEquals("Order 1", "31", freeOrderControls.get(0).getControlNo());
  }

  @Test
  public void testSort10() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    freeOrderControls.add(null);
    List<RaceControlBean> freeOrderControls2 = ValidationTestUtility.buildFreeOrderControlList("31");
    freeOrderControls.addAll(freeOrderControls2);

    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);

    Assert.assertEquals("2 size", 2, freeOrderControls.size());
    Assert.assertEquals("Order 1", "31", freeOrderControls.get(0).getControlNo());
  }

  @Test
  public void testSort11() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    freeOrderControls.add(null);
    freeOrderControls.add(new RaceControlBean());

    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);

    Assert.assertEquals("2 size", 2, freeOrderControls.size());
  }

  @Test
  public void testSort12() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    freeOrderControls.add(new RaceControlBean());
    freeOrderControls.add(null);

    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);

    Assert.assertEquals("2 size", 2, freeOrderControls.size());
  }

  @Test
  public void testSort13() throws Exception {
    List<RaceControlBean> freeOrderControls = new ArrayList<RaceControlBean>();
    freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32", "33", "34");
    freeOrderControls.get(0).setPunchTime(3L);
    freeOrderControls.get(1).setPunchTime(3L);
    freeOrderControls.get(2).setPunchTime(4L);
    freeOrderControls.get(3).setPunchTime(1L);

    FreeOrderUtility.sortControlsBySortCodeAndTime(freeOrderControls);

    Assert.assertEquals("4 size", 4, freeOrderControls.size());
    Assert.assertEquals("Order 1", "34", freeOrderControls.get(0).getControlNo());
    Assert.assertEquals("Order 2", "31", freeOrderControls.get(1).getControlNo());
    Assert.assertEquals("Order 3", "32", freeOrderControls.get(2).getControlNo());
    Assert.assertEquals("Order 4", "33", freeOrderControls.get(3).getControlNo());

    Assert.assertEquals("Time 1", 1, freeOrderControls.get(0).getPunchTime().longValue());
    Assert.assertEquals("Time 2", 3, freeOrderControls.get(1).getPunchTime().longValue());
    Assert.assertEquals("Time 3", 3, freeOrderControls.get(2).getPunchTime().longValue());
    Assert.assertEquals("Time 4", 4, freeOrderControls.get(3).getPunchTime().longValue());
  }

  @Test
  public void testMandatory1() throws Exception {
    List<RaceControlBean> freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32");
    RaceControlBean control31 = freeOrderControls.get(0);
    RaceControlBean control32 = freeOrderControls.get(1);
    control31.setMandatory(false);
    control32.setMandatory(false);
    List<RaceControlBean> punchedControls = ValidationTestUtility.buildControlList();

    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderControls, punchedControls);

    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
    Assert.assertEquals("Control Status", ControlStatusCodeType.InitialStatusCode.ID, control31.getControlStatusUid().longValue());
    Assert.assertEquals("Control Status", ControlStatusCodeType.InitialStatusCode.ID, control32.getControlStatusUid().longValue());
  }

  @Test
  public void testMandatory2() throws Exception {
    List<RaceControlBean> freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32");
    RaceControlBean control31 = freeOrderControls.get(0);
    RaceControlBean control32 = freeOrderControls.get(1);
    control31.setMandatory(true);
    control32.setMandatory(true);
    List<RaceControlBean> punchedControls = ValidationTestUtility.buildControlList();

    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderControls, punchedControls);

    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, result.getRaceStatusUid());
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, control31.getControlStatusUid().longValue());
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, control32.getControlStatusUid().longValue());
  }

  @Test
  public void testMandatory3() throws Exception {
    List<RaceControlBean> freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32");
    RaceControlBean control31 = freeOrderControls.get(0);
    RaceControlBean control32 = freeOrderControls.get(1);
    control31.setMandatory(false);
    control32.setMandatory(true);
    List<RaceControlBean> punchedControls = ValidationTestUtility.buildControlList("32");

    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderControls, punchedControls);

    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
    Assert.assertEquals("Control Status", ControlStatusCodeType.InitialStatusCode.ID, control31.getControlStatusUid().longValue());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, control32.getControlStatusUid().longValue());
  }

  @Test
  public void testMandatory4() throws Exception {
    List<RaceControlBean> freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32");
    RaceControlBean control31 = freeOrderControls.get(0);
    RaceControlBean control32 = freeOrderControls.get(1);
    control31.setMandatory(false);
    control32.setMandatory(true);
    List<RaceControlBean> punchedControls = ValidationTestUtility.buildControlList("31");

    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderControls, punchedControls);

    Assert.assertEquals("Race Status", RaceStatusCodeType.MissingPunchCode.ID, result.getRaceStatusUid());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, control31.getControlStatusUid().longValue());
    Assert.assertEquals("Control Status", ControlStatusCodeType.MissingCode.ID, control32.getControlStatusUid().longValue());
  }

  @Test
  public void testMandatory5() throws Exception {
    List<RaceControlBean> freeOrderControls = ValidationTestUtility.buildFreeOrderControlList("31", "32");
    RaceControlBean control31 = freeOrderControls.get(0);
    RaceControlBean control32 = freeOrderControls.get(1);
    control31.setMandatory(false);
    control32.setMandatory(true);
    List<RaceControlBean> punchedControls = ValidationTestUtility.buildControlList("32");

    FreeOrderResult result = FreeOrderUtility.validateFreeOrderGroup(freeOrderControls, punchedControls);

    Assert.assertEquals("Race Status", RaceStatusCodeType.OkCode.ID, result.getRaceStatusUid());
    Assert.assertEquals("Control Status", ControlStatusCodeType.InitialStatusCode.ID, control31.getControlStatusUid().longValue());
    Assert.assertEquals("Control Status", ControlStatusCodeType.OkCode.ID, control32.getControlStatusUid().longValue());
  }

}
