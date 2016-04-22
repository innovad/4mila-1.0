package com.rtiming.client.ranking;

import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlStatusCodeType.AdditionalCode;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.race.RaceStatusCodeType.DidNotStartCode;

public class RankingUtilityTest {

  @Test
  public void testRaceExtKey1() throws Exception {
    String extKey = RankingUtility.raceStatus2ExtKey(RaceStatusCodeType.DidNotStartCode.ID);
    String expected = new RaceStatusCodeType().getCode(DidNotStartCode.ID).getExtKey();
    Assert.assertEquals("Ext Key", expected, extKey);
  }

  @Test
  public void testRaceExtKey2() throws Exception {
    String extKey = RankingUtility.raceStatus2ExtKey(RaceStatusCodeType.OkCode.ID);
    String expected = new RaceStatusCodeType().getCode(RaceStatusCodeType.OkCode.ID).getExtKey();
    Assert.assertEquals("Ext Key", expected, extKey);
  }

  @Test
  public void testRaceExtKeyNull() throws Exception {
    String extKey = RankingUtility.raceStatus2ExtKey(null);
    Assert.assertEquals("Ext Key", "", extKey);
  }

  @Test
  public void testControlExtKey1() throws Exception {
    String extKey = RankingUtility.controlStatus2ExtKey(ControlStatusCodeType.AdditionalCode.ID);
    String expected = new ControlStatusCodeType().getCode(AdditionalCode.ID).getExtKey();
    Assert.assertEquals("Ext Key", expected, extKey);
  }

  @Test
  public void testControlExtKey2() throws Exception {
    String extKey = RankingUtility.controlStatus2ExtKey(ControlStatusCodeType.OkCode.ID);
    String expected = new ControlStatusCodeType().getCode(ControlStatusCodeType.OkCode.ID).getExtKey();
    Assert.assertEquals("Ext Key", expected, extKey);
  }

  @Test
  public void testControlExtKeyNull() throws Exception {
    String extKey = RankingUtility.controlStatus2ExtKey(null);
    Assert.assertEquals("Ext Key", "", extKey);
  }

}
