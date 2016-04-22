package com.rtiming.server.entry.startlist;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType.StartTimeWishEarlyStartCode;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class StartlistRegistrationGroupingUtilityTest {

  private static Long splitUid = StartlistSettingOptionCodeType.SplitRegistrationsCode.ID;
  private static Long groupUid = StartlistSettingOptionCodeType.GroupRegistrationsCode.ID;
  private long counter = 0;

  @Test
  public void testGroupingRulesEmpty() throws Exception {
    List<StartlistSettingBean> completeStartlists = new ArrayList<StartlistSettingBean>();
    StartlistRegistrationGroupingUtility.applyGroupingRules(completeStartlists);
  }

  @Test
  public void testGroupingRulesEmptyList() throws Exception {
    List<StartlistSettingBean> completeStartlists = new ArrayList<StartlistSettingBean>();

    StartlistSettingFormData settings = new StartlistSettingFormData();
    LinkedList<StartlistParticipationBean> list = new LinkedList<StartlistParticipationBean>();
    StartlistSettingBean startlistSettingBean = new StartlistSettingBean(1L, 10L, settings, list);

    completeStartlists.add(startlistSettingBean);
    StartlistRegistrationGroupingUtility.applyGroupingRules(completeStartlists);
  }

  @Test
  public void testGroupingRulesSplitOneStartlistWishStrongerThanSplit() throws Exception {
    LinkedList<StartlistParticipationBean> list = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1000L, splitUid, list);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, splitUid, StartTimeWishEarlyStartCode.ID, 10000L, list);
    StartlistParticipationBean b3 = addParticipation(1L, 1001L, splitUid, list);

    // create startlist setting
    StartlistSettingBean startlistSettingBean = createStartlistSettingBean(1L, 0L, 50000L, list);

    // analyze complete startlists
    LinkedList<StartlistParticipationBean> result = doStartlistTest(startlistSettingBean).get(0).getList();

    assertOrder(result, b1, b2, b3); // no change because we apply grouping only (and no wishes)
  }

  @Test
  public void testGroupingRulesSplitOneStartlistOff() throws Exception {
    LinkedList<StartlistParticipationBean> list = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1000L, splitUid, list);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, splitUid, list);
    StartlistParticipationBean b3 = addParticipation(1L, 1001L, splitUid, list);

    // create startlist setting
    StartlistSettingBean startlistSettingBean = createStartlistSettingBean(1L, 0L, 50000L, list, true, false);

    // analyze complete startlists
    LinkedList<StartlistParticipationBean> result = doStartlistTest(startlistSettingBean).get(0).getList();

    assertOrder(result, b1, b2, b3);
  }

  @Test
  public void testGroupingRulesSplitOneStartlist1() throws Exception {
    LinkedList<StartlistParticipationBean> list = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1000L, splitUid, list);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, splitUid, list);
    StartlistParticipationBean b3 = addParticipation(1L, 1001L, splitUid, list);

    // create startlist setting
    StartlistSettingBean startlistSettingBean = createStartlistSettingBean(1L, 0L, 50000L, list);

    // analyze complete startlists
    LinkedList<StartlistParticipationBean> result = doStartlistTest(startlistSettingBean).get(0).getList();

    assertOrder(result, b1, b3, b2);
  }

  @Test
  public void testGroupingRulesSplitOneStartlist2() throws Exception {
    LinkedList<StartlistParticipationBean> list = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1000L, splitUid, list);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, splitUid, list);
    StartlistParticipationBean b3 = addParticipation(1L, 1001L, null, list);

    // create startlist setting
    StartlistSettingBean startlistSettingBean = createStartlistSettingBean(1L, 0L, 50000L, list);

    // analyze complete startlists
    LinkedList<StartlistParticipationBean> result = doStartlistTest(startlistSettingBean).get(0).getList();

    assertOrder(result, b1, b3, b2);
  }

  @Test
  public void testGroupingRulesSplitOneStartlist3() throws Exception {
    LinkedList<StartlistParticipationBean> list = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1001L, null, list);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, splitUid, list);
    StartlistParticipationBean b3 = addParticipation(1L, 1000L, splitUid, list);
    StartlistParticipationBean b4 = addParticipation(1L, 1000L, splitUid, list);
    StartlistParticipationBean b5 = addParticipation(1L, 1001L, null, list);

    // create startlist setting
    StartlistSettingBean startlistSettingBean = createStartlistSettingBean(1L, 0L, 50000L, list);

    // analyze complete startlists
    LinkedList<StartlistParticipationBean> result = doStartlistTest(startlistSettingBean).get(0).getList();

    assertOrder(result, b4, b2, b1, b5, b3);
  }

  @Test
  public void testGroupingRulesSplitTwoStartlists1() throws Exception {
    LinkedList<StartlistParticipationBean> list1 = new LinkedList<StartlistParticipationBean>();
    LinkedList<StartlistParticipationBean> list2 = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1001L, splitUid, list1);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, null, list1);
    StartlistParticipationBean b3 = addParticipation(2L, 1001L, splitUid, list2);
    StartlistParticipationBean b4 = addParticipation(2L, 1000L, null, list2);

    // create startlist setting
    StartlistSettingBean startlistSettingBean1 = createStartlistSettingBean(1L, 5000L, 50000L, list1);
    StartlistSettingBean startlistSettingBean2 = createStartlistSettingBean(2L, 1000L, 40000L, list2);

    // analyze complete startlists
    List<StartlistSettingBean> testResult = doStartlistTest(startlistSettingBean1, startlistSettingBean2);
    LinkedList<StartlistParticipationBean> result1 = testResult.get(0).getList();
    LinkedList<StartlistParticipationBean> result2 = testResult.get(1).getList();

    assertOrder(result1, b2, b1);
    assertOrder(result2, b3, b4);
  }

  @Test
  public void testGroupingRulesSplitTwoStartlists2() throws Exception {
    LinkedList<StartlistParticipationBean> list1 = new LinkedList<StartlistParticipationBean>();
    LinkedList<StartlistParticipationBean> list2 = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1001L, splitUid, list1);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, null, list1);
    StartlistParticipationBean b3 = addParticipation(2L, 1001L, splitUid, list2);
    StartlistParticipationBean b4 = addParticipation(2L, 1000L, null, list2);

    // create startlist setting
    StartlistSettingBean startlistSettingBean1 = createStartlistSettingBean(1L, 3000L, 10000000L, list1);
    StartlistSettingBean startlistSettingBean2 = createStartlistSettingBean(2L, 5000L, 40000L, list2);

    // analyze complete startlists
    List<StartlistSettingBean> testResult = doStartlistTest(startlistSettingBean1, startlistSettingBean2);
    LinkedList<StartlistParticipationBean> result1 = testResult.get(0).getList();
    LinkedList<StartlistParticipationBean> result2 = testResult.get(1).getList();

    assertOrder(result1, b2, b1);
    assertOrder(result2, b3, b4);
  }

  @Test
  public void testGroupingRulesSplitTwoStartlists3() throws Exception {
    LinkedList<StartlistParticipationBean> list1 = new LinkedList<StartlistParticipationBean>();
    LinkedList<StartlistParticipationBean> list2 = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1001L, splitUid, list1);
    StartlistParticipationBean b2 = addParticipation(1L, 1002L, splitUid, list1);
    StartlistParticipationBean b3 = addParticipation(1L, 1000L, null, list1);
    StartlistParticipationBean b4 = addParticipation(2L, 1001L, splitUid, list2);
    StartlistParticipationBean b5 = addParticipation(2L, 1002L, splitUid, list2);
    StartlistParticipationBean b6 = addParticipation(2L, 1000L, null, list2);

    // create startlist setting
    StartlistSettingBean startlistSettingBean1 = createStartlistSettingBean(1L, 5000L, 50000L, list1);
    StartlistSettingBean startlistSettingBean2 = createStartlistSettingBean(2L, 1000L, 40000L, list2);

    // analyze complete startlists
    List<StartlistSettingBean> testResult = doStartlistTest(startlistSettingBean1, startlistSettingBean2);
    LinkedList<StartlistParticipationBean> result1 = testResult.get(0).getList();
    LinkedList<StartlistParticipationBean> result2 = testResult.get(1).getList();

    assertOrder(result1, b3, b1, b2);
    assertOrder(result2, b5, b4, b6);
  }

  @Test
  public void testGroupingRulesSplitTwoStartlists4() throws Exception {
    LinkedList<StartlistParticipationBean> list1 = new LinkedList<StartlistParticipationBean>();
    LinkedList<StartlistParticipationBean> list2 = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1001L, splitUid, list1);
    StartlistParticipationBean b2 = addParticipation(1L, 1002L, splitUid, list1);
    StartlistParticipationBean b3 = addParticipation(1L, 1000L, null, list1);
    StartlistParticipationBean b4 = addParticipation(2L, 1001L, splitUid, list2);
    StartlistParticipationBean b5 = addParticipation(2L, 1002L, splitUid, list2);
    StartlistParticipationBean b6 = addParticipation(2L, 1000L, null, list2);

    // create startlist setting
    StartlistSettingBean startlistSettingBean1 = createStartlistSettingBean(1L, 5000L, 50000L, list1);
    StartlistSettingBean startlistSettingBean2 = createStartlistSettingBean(2L, 5000L, 50000L, list2);

    // analyze complete startlists
    List<StartlistSettingBean> testResult = doStartlistTest(startlistSettingBean1, startlistSettingBean2);
    LinkedList<StartlistParticipationBean> result1 = testResult.get(0).getList();
    LinkedList<StartlistParticipationBean> result2 = testResult.get(1).getList();

    assertOrder(result1, b2, b1, b3);
    assertOrder(result2, b6, b4, b5);
  }

  @Test
  public void testGroupingRulesGroupOneStartlistSimple() throws Exception {
    LinkedList<StartlistParticipationBean> list = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1000L, groupUid, null, 5000L, list);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, groupUid, null, 15000L, list);

    // create startlist setting
    StartlistSettingBean startlistSettingBean = createStartlistSettingBean(1L, 5000L, 15000L, list);

    // analyze complete startlists
    LinkedList<StartlistParticipationBean> result = doStartlistTest(startlistSettingBean).get(0).getList();

    assertOrder(result, b1, b2);
  }

  @Test
  public void testGroupingRulesGroupOneStartlistOff() throws Exception {
    LinkedList<StartlistParticipationBean> list = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1000L, groupUid, null, 5000L, list);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, null, null, 10000L, list);
    StartlistParticipationBean b3 = addParticipation(1L, 1000L, groupUid, null, 15000L, list);

    // create startlist setting
    StartlistSettingBean startlistSettingBean = createStartlistSettingBean(1L, 5000L, 15000L, list, false, true);

    // analyze complete startlists
    LinkedList<StartlistParticipationBean> result = doStartlistTest(startlistSettingBean).get(0).getList();

    assertOrder(result, b1, b2, b3);
  }

  @Test
  public void testGroupingRulesGroupOneStartlist() throws Exception {
    LinkedList<StartlistParticipationBean> list = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1000L, groupUid, null, 5000L, list);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, null, null, 10000L, list);
    StartlistParticipationBean b3 = addParticipation(1L, 1000L, groupUid, null, 15000L, list);

    // create startlist setting
    StartlistSettingBean startlistSettingBean = createStartlistSettingBean(1L, 5000L, 15000L, list);

    // analyze complete startlists
    LinkedList<StartlistParticipationBean> result = doStartlistTest(startlistSettingBean).get(0).getList();

    assertOrder(result, b2, b1, b3);
  }

  @Test
  public void testGroupingRulesGroupTwoStartlists1() throws Exception {
    LinkedList<StartlistParticipationBean> list1 = new LinkedList<StartlistParticipationBean>();
    LinkedList<StartlistParticipationBean> list2 = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1001L, groupUid, null, 1000L, list1);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, null, null, 2000L, list1);
    StartlistParticipationBean b3 = addParticipation(2L, 1000L, null, null, 5000L, list2);
    StartlistParticipationBean b4 = addParticipation(2L, 1001L, groupUid, null, 6000L, list2);

    // create startlist setting
    StartlistSettingBean startlistSettingBean1 = createStartlistSettingBean(1L, 1000L, 2000L, list1);
    StartlistSettingBean startlistSettingBean2 = createStartlistSettingBean(2L, 5000L, 6000L, list2);

    // analyze complete startlists
    List<StartlistSettingBean> testResult = doStartlistTest(startlistSettingBean1, startlistSettingBean2);
    LinkedList<StartlistParticipationBean> result1 = testResult.get(0).getList();
    LinkedList<StartlistParticipationBean> result2 = testResult.get(1).getList();

    assertOrder(result1, b2, b1);
    assertOrder(result2, b4, b3);
  }

  @Test
  public void testGroupingRulesGroupTwoStartlists2() throws Exception {
    LinkedList<StartlistParticipationBean> list1 = new LinkedList<StartlistParticipationBean>();
    LinkedList<StartlistParticipationBean> list2 = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1000L, null, null, 2000L, list1);
    StartlistParticipationBean b2 = addParticipation(1L, 1001L, groupUid, null, 3000L, list1);
    StartlistParticipationBean b3 = addParticipation(1L, 1000L, null, null, 4000L, list1);
    StartlistParticipationBean b4 = addParticipation(2L, 1000L, null, null, 3500L, list2);
    StartlistParticipationBean b5 = addParticipation(2L, 1000L, null, null, 4500L, list2);
    StartlistParticipationBean b6 = addParticipation(2L, 1001L, groupUid, null, 5500L, list2);

    // create startlist setting
    StartlistSettingBean startlistSettingBean1 = createStartlistSettingBean(1L, 2000L, 4000L, list1);
    StartlistSettingBean startlistSettingBean2 = createStartlistSettingBean(2L, 3500L, 5500L, list2);

    // analyze complete startlists
    List<StartlistSettingBean> testResult = doStartlistTest(startlistSettingBean1, startlistSettingBean2);
    LinkedList<StartlistParticipationBean> result1 = testResult.get(0).getList();
    LinkedList<StartlistParticipationBean> result2 = testResult.get(1).getList();

    assertOrder(result1, b1, b3, b2);
    assertOrder(result2, b4, b6, b5);
  }

  @Test
  public void testGroupingRulesGroupTwoStartlists3() throws Exception {
    LinkedList<StartlistParticipationBean> list1 = new LinkedList<StartlistParticipationBean>();
    LinkedList<StartlistParticipationBean> list2 = new LinkedList<StartlistParticipationBean>();

    // add participations
    StartlistParticipationBean b1 = addParticipation(1L, 1001L, groupUid, null, 1000L, list1);
    StartlistParticipationBean b2 = addParticipation(1L, 1000L, null, null, 1500L, list1);
    StartlistParticipationBean b3 = addParticipation(1L, 1000L, null, null, 5000L, list1);
    StartlistParticipationBean b4 = addParticipation(2L, 1001L, groupUid, null, 4000L, list2);
    StartlistParticipationBean b5 = addParticipation(2L, 1000L, null, null, 6000L, list2);
    StartlistParticipationBean b6 = addParticipation(2L, 1000L, null, null, 8000L, list2);

    // create startlist setting
    StartlistSettingBean startlistSettingBean1 = createStartlistSettingBean(1L, 1000L, 5000L, list1);
    StartlistSettingBean startlistSettingBean2 = createStartlistSettingBean(2L, 4000L, 8000L, list2);

    // analyze complete startlists
    List<StartlistSettingBean> testResult = doStartlistTest(startlistSettingBean1, startlistSettingBean2);
    LinkedList<StartlistParticipationBean> result1 = testResult.get(0).getList();
    LinkedList<StartlistParticipationBean> result2 = testResult.get(1).getList();

    assertOrder(result1, b2, b1, b3);
    assertOrder(result2, b4, b5, b6);
  }

  private void assertOrder(LinkedList<StartlistParticipationBean> result, StartlistParticipationBean... bean) {
    Assert.assertEquals("Size", result.size(), bean.length);
    for (int i = 0; i < result.size(); i++) {
      Assert.assertEquals("Order after grouping", bean[i], result.get(i));
    }
  }

  private List<StartlistSettingBean> doStartlistTest(StartlistSettingBean... startlistSettingBeans) throws ProcessingException {
    List<StartlistSettingBean> completeStartlists = new ArrayList<StartlistSettingBean>();
    for (StartlistSettingBean bean : startlistSettingBeans) {
      completeStartlists.add(bean);
    }
    StartlistRegistrationGroupingUtility.applyGroupingRules(completeStartlists);

    return completeStartlists;
  }

  private StartlistSettingBean createStartlistSettingBean(Long nr, Long firstStart, Long lastStart, LinkedList<StartlistParticipationBean> list) {
    return createStartlistSettingBean(nr, firstStart, lastStart, list, true, true);
  }

  private StartlistSettingBean createStartlistSettingBean(Long nr, Long firstStart, Long lastStart, LinkedList<StartlistParticipationBean> list, boolean group, boolean split) {
    StartlistSettingFormData settings = new StartlistSettingFormData();
    List<Long> options = new ArrayList<Long>();
    if (split) {
      options.add(StartlistSettingOptionCodeType.SplitRegistrationsCode.ID);
    }
    if (group) {
      options.add(StartlistSettingOptionCodeType.GroupRegistrationsCode.ID);
    }
    settings.getOptions().setValue(new HashSet<>(options));

    StartlistSettingBean startlistSettingBean = new StartlistSettingBean(nr, 10L, settings, list);
    startlistSettingBean.setFirstStart(firstStart);
    startlistSettingBean.setLastStart(lastStart);

    return startlistSettingBean;
  }

  private StartlistParticipationBean addParticipation(Long startlistSettingNr, Long registrationNr, Long optionUid, LinkedList<StartlistParticipationBean> list) {
    return addParticipation(startlistSettingNr, registrationNr, optionUid, null, 10000L, list);
  }

  private StartlistParticipationBean addParticipation(Long startlistSettingNr, Long registrationNr, Long optionUid, Long startTimeWish, Long startTime, LinkedList<StartlistParticipationBean> list) {
    StartlistParticipationBean participation = new StartlistParticipationBean();
    participation.setStartlistSettingNr(startlistSettingNr);
    participation.setEventNr(10L);
    participation.setEntryNr(counter);
    participation.setRegistrationNr(registrationNr);
    participation.setRegistrationStartlistSettingOptionUid(optionUid);
    participation.setStartTime(startTime);
    participation.setStartTimeWish(startTimeWish);
    list.add(participation);
    counter++;
    return participation;
  }

}
