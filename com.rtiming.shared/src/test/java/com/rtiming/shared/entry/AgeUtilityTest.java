package com.rtiming.shared.entry;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Test;

import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtClassAgeKey;
import com.rtiming.shared.runner.SexCodeType;

import junit.framework.Assert;

/**
 * @author amo
 */
public class AgeUtilityTest {

  @Test(expected = IllegalArgumentException.class)
  public void testIsRunnerValid1() throws Exception {
    AgeUtility.isRunnerValidForClassAge(null, null, null, null, null, null);
  }

  @Test
  public void testIsRunnerValid2() throws Exception {
    Date date = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = new ArrayList<>();
    TriState result = AgeUtility.isRunnerValidForClassAge(date, null, null, null, null, settings);
    Assert.assertEquals("Age Validation", TriState.UNDEFINED, result);
  }

  @Test
  public void testIsRunnerValid3() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, SexCodeType.ManCode.ID, 2002L, settings);
    Assert.assertEquals("Age Validation", TriState.TRUE, result);
  }

  @Test
  public void testIsRunnerValid4() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, SexCodeType.ManCode.ID, 1992L, settings);
    Assert.assertEquals("Age Validation", TriState.TRUE, result);
  }

  @Test
  public void testIsRunnerValid5() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, SexCodeType.ManCode.ID, 2003L, settings);
    Assert.assertEquals("Age Validation", TriState.FALSE, result);
  }

  @Test
  public void testIsRunnerValid6() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, SexCodeType.ManCode.ID, 1991L, settings);
    Assert.assertEquals("Age Validation", TriState.FALSE, result);
  }

  @Test
  public void testIsRunnerValid7() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, null, 1998L, settings);
    Assert.assertEquals("Age Validation", TriState.TRUE, result);
  }

  @Test
  public void testIsRunnerValid8() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, null, 1991L, settings);
    Assert.assertEquals("Age Validation", TriState.FALSE, result);
  }

  @Test
  public void testIsRunnerValid9() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, SexCodeType.ManCode.ID, null, settings);
    Assert.assertEquals("Age Validation", TriState.TRUE, result);
  }

  @Test
  public void testIsRunnerValid10() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, SexCodeType.WomanCode.ID, 1995L, settings);
    Assert.assertEquals("Age Validation", TriState.FALSE, result);
  }

  @Test
  public void testIsRunnerValid11() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, SexCodeType.WomanCode.ID, null, settings);
    Assert.assertEquals("Age Validation", TriState.FALSE, result);
  }

  @Test
  public void testIsRunnerValid12() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, null, null, settings);
    Assert.assertEquals("Age Validation", TriState.TRUE, result);
  }

  @Test
  public void testIsRunnerValid13() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, null, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, SexCodeType.WomanCode.ID, 1995L, settings);
    Assert.assertEquals("Age Validation", TriState.TRUE, result);
  }

  @Test
  public void testIsRunnerValid14() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(554L, null, 10L, 20L);

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, SexCodeType.WomanCode.ID, 1995L, settings);
    Assert.assertEquals("Age Validation", TriState.UNDEFINED, result);
  }

  @Test
  public void testIsRunnerValid15() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, null, null);
    settings.add(createSettings(555L, SexCodeType.WomanCode.ID, 10L, 20L));

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, SexCodeType.ManCode.ID, 2010L, settings);
    Assert.assertEquals("Age Validation", TriState.TRUE, result);
  }

  @Test
  public void testIsRunnerValid16() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, null, null);
    settings.add(createSettings(555L, SexCodeType.WomanCode.ID, 10L, 20L));

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 555L, 88L, SexCodeType.WomanCode.ID, 2010L, settings);
    Assert.assertEquals("Age Validation", TriState.FALSE, result);
  }

  @Test
  public void testIsRunnerValid17() throws Exception {
    Date evtZero = DateUtility.parse("02072012", "ddMMyyyy");
    List<RtClassAge> settings = createSimpleSettings(555L, SexCodeType.ManCode.ID, null, null);
    settings.add(createSettings(555L, SexCodeType.WomanCode.ID, 10L, 20L));

    TriState result = AgeUtility.isRunnerValidForClassAge(evtZero, 556L, 88L, SexCodeType.WomanCode.ID, 2010L, settings);
    Assert.assertEquals("Age Validation", TriState.UNDEFINED, result);
  }

  private List<RtClassAge> createSimpleSettings(Long classUid, Long sexUid, Long ageFrom, Long ageTo) {
    List<RtClassAge> settings = new ArrayList<>();
    RtClassAge ageBean1 = createSettings(classUid, sexUid, ageFrom, ageTo);
    settings.add(ageBean1);
    return settings;
  }

  private RtClassAge createSettings(Long classUid, Long sexUid, Long ageFrom, Long ageTo) {
    RtClassAge ageBean1 = new RtClassAge();
    ageBean1.setClassUid(classUid);
    RtClassAgeKey id = new RtClassAgeKey();
    id.setClientNr(88L);
    ageBean1.setId(id);
    ageBean1.setSexUid(sexUid);
    ageBean1.setAgeFrom(ageFrom);
    ageBean1.setAgeTo(ageTo);
    return ageBean1;
  }

}
