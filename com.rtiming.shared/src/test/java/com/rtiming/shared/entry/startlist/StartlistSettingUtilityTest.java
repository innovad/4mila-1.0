package com.rtiming.shared.entry.startlist;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.FMilaUtility;

public class StartlistSettingUtilityTest {

  @Test
  public void testCheckIfAllExistingStartlistSettingNrsAreSelectedEmpty() throws Exception {
    Long[] selectedNrs = new Long[]{};
    Long[] allNrs = new Long[]{};
    boolean result = StartlistSettingUtility.checkIfAllExistingStartlistSettingNrsAreSelected(selectedNrs, allNrs);

    Assert.assertTrue("Check", result);
  }

  @Test
  public void testCheckIfAllExistingStartlistSettingNrsAreSelectedSimple() throws Exception {
    Long[] selectedNrs = new Long[]{1L, 2L};
    Long[] allNrs = new Long[]{1L, 2L};
    boolean result = StartlistSettingUtility.checkIfAllExistingStartlistSettingNrsAreSelected(selectedNrs, allNrs);

    Assert.assertTrue("Check", result);
  }

  @Test
  public void testCheckIfAllExistingStartlistSettingNrsAreSelectedNull() throws Exception {
    Long[] selectedNrs = new Long[]{1L, 2L};
    Long[] allNrs = new Long[]{1L, 2L, null, null};
    boolean result = StartlistSettingUtility.checkIfAllExistingStartlistSettingNrsAreSelected(selectedNrs, allNrs);

    Assert.assertTrue("Check", result);
  }

  @Test
  public void testCheckIfAllExistingStartlistSettingNrsAreSelectedFalse() throws Exception {
    Long[] selectedNrs = new Long[]{1L};
    Long[] allNrs = new Long[]{1L, 2L};
    boolean result = StartlistSettingUtility.checkIfAllExistingStartlistSettingNrsAreSelected(selectedNrs, allNrs);

    Assert.assertFalse("Check", result);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckIfAllExistingStartlistSettingNrsAreSelectedException1() throws Exception {
    StartlistSettingUtility.checkIfAllExistingStartlistSettingNrsAreSelected(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckIfAllExistingStartlistSettingNrsAreSelectedException2() throws Exception {
    StartlistSettingUtility.checkIfAllExistingStartlistSettingNrsAreSelected(new Long[]{1L}, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCheckIfStartlistSettingsContainRegistrationOption() throws Exception {
    StartlistSettingUtility.checkIfStartlistSettingsContainRegistrationOption(null);
  }

  @Test
  public void testGetStartlistOptions() throws Exception {
    StartlistSettingFormData formData = new StartlistSettingFormData();
    Set<Long> set = new HashSet<>();
    set.add(33L);
    set.add(34L);
    formData.getOptions().setValue(set);

    List<Long> result = StartlistSettingUtility.getStartlistOptions(formData.getOptions());

    Assert.assertTrue("Nr is in List", result.contains(33L));
    Assert.assertTrue("Nr is in List", result.contains(34L));
    Assert.assertEquals("Count", 2, result.size());
  }

  @Test
  public void testGetStartlistOptionsNull1() throws Exception {
    List<Long> result = StartlistSettingUtility.getStartlistOptions(null);
    Assert.assertEquals("Count", 0, result.size());
  }

  @Test
  public void testGetStartlistOptionsNull2() throws Exception {
    StartlistSettingFormData formData = new StartlistSettingFormData();
    List<Long> result = StartlistSettingUtility.getStartlistOptions(formData.getOptions());
    Assert.assertEquals("Count", 0, result.size());
  }

  @Test
  public void testFirstStart() throws Exception {
    Date lastStart = new Date();
    Long interval = 180L;
    Long entriesCount = 2L;
    Long vacantPercent = 0L;
    Long vacantAbsolute = 0L;
    Date firstStart = StartlistSettingUtility.calculateFirstStart(lastStart, interval, entriesCount, vacantPercent, vacantAbsolute);

    Assert.assertEquals("First Start", FMilaUtility.addSeconds(lastStart, -1 * 180), firstStart);
  }

  @Test
  public void testFirstStartWithVacants() throws Exception {
    Date lastStart = new Date();
    Long interval = 180L;
    Long entriesCount = 2L;
    Long vacantPercent = 0L;
    Long vacantAbsolute = 3L;
    Date firstStart = StartlistSettingUtility.calculateFirstStart(lastStart, interval, entriesCount, vacantPercent, vacantAbsolute);

    Assert.assertEquals("First Start", FMilaUtility.addSeconds(lastStart, -4 * 180), firstStart);
  }

  @Test
  public void testFirstStartNull() throws Exception {
    Date firstStart = StartlistSettingUtility.calculateFirstStart(null, null, null, null, null);
    Assert.assertNull("First Start", firstStart);
  }

  @Test
  public void testLastStart() throws Exception {
    Date lastStart = new Date();
    Long interval = 180L;
    Long entriesCount = 4L;
    Long vacantPercent = 0L;
    Long vacantAbsolute = 0L;
    Date firstStart = StartlistSettingUtility.calculateLastStart(lastStart, interval, entriesCount, vacantPercent, vacantAbsolute);

    Assert.assertEquals("Last Start", FMilaUtility.addSeconds(lastStart, 3 * 180), firstStart);
  }

  @Test
  public void testLastStartWithVacants() throws Exception {
    Date lastStart = new Date();
    Long interval = 180L;
    Long entriesCount = 4L;
    Long vacantPercent = 50L;
    Long vacantAbsolute = 0L;
    Date firstStart = StartlistSettingUtility.calculateLastStart(lastStart, interval, entriesCount, vacantPercent, vacantAbsolute);

    Assert.assertEquals("Last Start", FMilaUtility.addSeconds(lastStart, 5 * 180), firstStart);
  }

  @Test
  public void testLastStartNull() throws Exception {
    Date lastStart = StartlistSettingUtility.calculateLastStart(null, null, null, null, null);
    Assert.assertNull("First Start", lastStart);
  }

  @Test
  public void testVacantCountPercent() throws Exception {
    Long entriesCount = 100L;
    Long vacantPercentIn = 77L;
    Long vacantAbsoluteIn = 76L;
    long result = StartlistSettingUtility.calculateVacantCount(entriesCount, vacantPercentIn, vacantAbsoluteIn);

    Assert.assertEquals("Vacant calculation", 77L, result);
  }

  @Test
  public void testVacantCountAbsolute() throws Exception {
    Long entriesCount = 100L;
    Long vacantPercentIn = 33L;
    Long vacantAbsoluteIn = 34L;
    long result = StartlistSettingUtility.calculateVacantCount(entriesCount, vacantPercentIn, vacantAbsoluteIn);

    Assert.assertEquals("Vacant calculation", 34L, result);
  }

  @Test
  public void testVacantNull() throws Exception {
    long result = StartlistSettingUtility.calculateVacantCount(null, null, null);
    Assert.assertEquals("Vacant calculation", 0L, result);
  }

}
