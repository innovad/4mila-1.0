package com.rtiming.server.entry.startlist;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TreeSet;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;
import com.rtiming.shared.entry.startlist.StartlistSettingUtility;

public final class StartlistRegistrationGroupingUtility {

  private static final Logger LOG = LoggerFactory.getLogger(StartlistRegistrationGroupingUtility.class);

  private StartlistRegistrationGroupingUtility() {
  }

  public static void applyGroupingRules(List<StartlistSettingBean> completeStartlists) throws ProcessingException {
    // create a map with StartlistSettingBean and Startlists to easily find them
    final Map<Long, StartlistSettingBean> settingsMap = new HashMap<Long, StartlistSettingBean>();
    Map<Long, LinkedList<StartlistParticipationBean>> singleStartlistMap = new HashMap<Long, LinkedList<StartlistParticipationBean>>();
    for (StartlistSettingBean bean : completeStartlists) {
      settingsMap.put(bean.getStartlistSettingNr(), bean);
      singleStartlistMap.put(bean.getStartlistSettingNr(), bean.getList());
    }

    List<StartlistParticipationBean> allParticipations = new ArrayList<StartlistParticipationBean>();
    for (StartlistSettingBean singleStartlist : completeStartlists) {
      allParticipations.addAll(singleStartlist.getList());
    }

    applyRule(allParticipations, StartlistSettingOptionCodeType.SplitRegistrationsCode.ID, settingsMap, singleStartlistMap);
    applyRule(allParticipations, StartlistSettingOptionCodeType.GroupRegistrationsCode.ID, settingsMap, singleStartlistMap);
  }

  private static void applyRule(List<StartlistParticipationBean> allParticipations, Long ruleUid, final Map<Long, StartlistSettingBean> settingsMap, Map<Long, LinkedList<StartlistParticipationBean>> singleStartlistMap) throws ProcessingException {
    Map<Long, RegistrationBean> map = new HashMap<Long, RegistrationBean>();
    for (StartlistParticipationBean bean : allParticipations) {
      List<Long> options = StartlistSettingUtility.getStartlistOptions(settingsMap.get(bean.getStartlistSettingNr()).getSettings().getOptions());
      if (bean.getStartTimeWish() == null && CompareUtility.equals(bean.getRegistrationStartlistSettingOptionUid(), ruleUid) && options.contains(ruleUid)) {
        // add participation to registration map
        RegistrationBean registration = null;
        if (!map.containsKey(bean.getRegistrationNr())) {
          registration = new RegistrationBean(bean.getRegistrationNr(), bean.getRegistrationStartlistSettingOptionUid());
          map.put(bean.getRegistrationNr(), registration);
        }
        registration = map.get(bean.getRegistrationNr());
        registration.addParticipation(bean);
      }
    }

    // filter registrations, only with 2 or more participations
    // grouping, splitting would make no sense for 1 participation
    List<RegistrationBean> registrationToChange = new ArrayList<RegistrationBean>();
    for (RegistrationBean bean : map.values()) {
      if (bean.getParticipations().size() > 1) {
        registrationToChange.add(bean);
      }
    }

    for (RegistrationBean registration : registrationToChange) {
      if (CompareUtility.equals(StartlistSettingOptionCodeType.SplitRegistrationsCode.ID, registration.getStartlistSettingOptionUid())) {
        splitRegistration(registration, settingsMap, singleStartlistMap);
      }
      else if (CompareUtility.equals(StartlistSettingOptionCodeType.GroupRegistrationsCode.ID, registration.getStartlistSettingOptionUid())) {
        groupRegistration(registration, settingsMap, singleStartlistMap);
      }
    }
  }

  private static void groupRegistration(RegistrationBean registration, final Map<Long, StartlistSettingBean> settingsMap, Map<Long, LinkedList<StartlistParticipationBean>> singleStartlistMap) throws ProcessingException {
    LOG.debug("Grouping: " + registration);

    // find latest first start and earliest last start
    Long latestFirstStart = null;
    Long earliestLastStart = null;

    List<Long> groupStartTimes = new ArrayList<Long>();
    for (StartlistParticipationBean bean : registration.getParticipations()) {
      StartlistSettingBean startlistSettings = settingsMap.get(bean.getStartlistSettingNr());
      latestFirstStart = max(latestFirstStart, startlistSettings.getFirstStart());
      earliestLastStart = min(earliestLastStart, startlistSettings.getLastStart());
      groupStartTimes.add(bean.getStartTime());
    }

    Long avg = calculateAverageStartTime(groupStartTimes);

    // now latestFirstStart to earliestLastStart is the possible time frame for grouping
    if (latestFirstStart == null || earliestLastStart == null) {
      throw new ProcessingException("Earliest and/or latest start could not be calculated.");
    }
    Long from = latestFirstStart;
    Long to = earliestLastStart;
    if (from > to) {
      // no perfect matching time frame, but the best values are still here
      from = earliestLastStart;
      to = latestFirstStart;
    }

    Long targetTime = null;
    if (from <= avg && avg <= to) {
      targetTime = avg;
    }
    else if (avg < from) {
      targetTime = latestFirstStart;
    }
    else /* to <= avg */ {
      targetTime = earliestLastStart;
    }

    // move participations to target time
    for (StartlistParticipationBean bean : registration.getParticipations()) {
      LinkedList<StartlistParticipationBean> singleStartlist = singleStartlistMap.get(bean.getStartlistSettingNr());
      StartlistSettingBean startlistSetting = settingsMap.get(bean.getStartlistSettingNr());
      if (startlistSetting.getLastStart() <= targetTime) {
        // add as last
        singleStartlist.remove(bean);
        singleStartlist.addLast(bean);
      }
      else if (startlistSetting.getFirstStart() >= targetTime) {
        singleStartlist.remove(bean);
        singleStartlist.addFirst(bean);
      }
      else {
        StartlistParticipationBean insertPosition = singleStartlist.getLast();
        for (StartlistParticipationBean row : singleStartlist) {
          if (row.getStartTime() != null && row.getStartTime() > targetTime) {
            insertPosition = row;
            break;
          }
        }
        // only insert if it is not already at correct position
        if (!bean.equals(insertPosition)) {
          singleStartlist.remove(bean);
          singleStartlist.add(singleStartlist.indexOf(insertPosition), bean);
        }
      }
      // update temporary starttimes (bib no does not matter here since it is only temporary)
      StartlistServiceUtility.setStartTimesAndBibNo(startlistSetting, startlistSetting.getFirstStart(), 0L);
    }
  }

  private static Long calculateAverageStartTime(List<Long> groupStartTimes) {
    double sum = 0;
    for (int i = 0; i < groupStartTimes.size(); i++) {
      sum += groupStartTimes.get(i);
    }
    Long avg = Math.round(sum / groupStartTimes.size());
    return avg;
  }

  private static Long max(Long a, Long b) {
    if (a == null) {
      return b;
    }
    if (b == null) {
      return a;
    }
    return Math.max(a, b);
  }

  private static Long min(Long a, Long b) {
    if (a == null) {
      return b;
    }
    if (b == null) {
      return a;
    }
    return Math.min(a, b);
  }

  private static void splitRegistration(RegistrationBean registration, final Map<Long, StartlistSettingBean> settingsMap, Map<Long, LinkedList<StartlistParticipationBean>> singleStartlistMap) {
    LOG.debug("Splitting: " + registration);

    Comparator<? super StartlistParticipationBean> orderEarliest = new Comparator<StartlistParticipationBean>() {
      @Override
      public int compare(StartlistParticipationBean o1, StartlistParticipationBean o2) {
        if (o1 == null || o2 == null) {
          return 0;
        }
        Long startTime1 = settingsMap.get(o1.getStartlistSettingNr()).getFirstStart();
        Long startTime2 = settingsMap.get(o2.getStartlistSettingNr()).getFirstStart();
        if (startTime1.compareTo(startTime2) == 0) {
          return o1.getEntryNr().compareTo(o2.getEntryNr());
        }
        return startTime1.compareTo(startTime2);
      }
    };
    TreeSet<StartlistParticipationBean> earliestFirst = new TreeSet<StartlistParticipationBean>(orderEarliest);
    earliestFirst.addAll(registration.getParticipations());

    Comparator<? super StartlistParticipationBean> orderLatest = new Comparator<StartlistParticipationBean>() {
      @Override
      public int compare(StartlistParticipationBean o1, StartlistParticipationBean o2) {
        if (o1 == null || o2 == null) {
          return 0;
        }
        Long startTime1 = settingsMap.get(o1.getStartlistSettingNr()).getLastStart();
        Long startTime2 = settingsMap.get(o2.getStartlistSettingNr()).getLastStart();
        if (startTime1.compareTo(startTime2) == 0) {
          return o1.getEntryNr().compareTo(o2.getEntryNr());
        }
        return startTime1.compareTo(startTime2);
      }
    };
    TreeSet<StartlistParticipationBean> latestFirst = new TreeSet<StartlistParticipationBean>(orderLatest);
    latestFirst.addAll(registration.getParticipations());

    // loop through all startlist settings, ordererd by earliest, latest, 2nd earliest, 2nd latest
    // move first, then move last, ...
    boolean toggleQueue = true;
    boolean toggleReorder = true;
    if (earliestFirst.size() > 0 && latestFirst.size() > 0) {
      // now we have to check if is is better to start with first or latest
      // check which difference is bigger
      Long earliestFirstLastStart = settingsMap.get(earliestFirst.first().getStartlistSettingNr()).getLastStart();
      Long earliestFirstFirstStart = settingsMap.get(earliestFirst.first().getStartlistSettingNr()).getFirstStart();
      Long latestFirstLastStart = settingsMap.get(latestFirst.first().getStartlistSettingNr()).getLastStart();
      Long latestFirstFirstStart = settingsMap.get(latestFirst.first().getStartlistSettingNr()).getFirstStart();
      Long diff1 = earliestFirstLastStart - latestFirstFirstStart;
      Long diff2 = earliestFirstFirstStart - latestFirstLastStart;
      if (diff1 > diff2) {
        toggleQueue = false;
      }
    }
    while (earliestFirst.size() > 0 || latestFirst.size() > 0) {
      // decide which next participation to take
      StartlistParticipationBean nextBean = null;
      if (toggleQueue) {
        nextBean = earliestFirst.first();
        toggleQueue = false;
      }
      if (nextBean == null) {
        nextBean = latestFirst.first();
        toggleQueue = true;
      }
      // reorder
      LinkedList<StartlistParticipationBean> singleList = singleStartlistMap.get(nextBean.getStartlistSettingNr());
      if (toggleReorder) {
        singleList.remove(nextBean);
        singleList.addFirst(nextBean);
        toggleReorder = false;
      }
      else {
        singleList.remove(nextBean);
        singleList.addLast(nextBean);
        toggleReorder = true;
      }
      // remove from queue
      earliestFirst.remove(nextBean);
      latestFirst.remove(nextBean);
    }
  }
}
