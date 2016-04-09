package com.rtiming.server.entry.startlist;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.ListIterator;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;

import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;

public final class StartlistWishUtility {

  private StartlistWishUtility() {
  }

  public static void applyEarlyStartTimeWishes(LinkedList<StartlistParticipationBean> completeStartlist) throws ProcessingException {
    // early start
    List<StartlistParticipationBean> earlyStarts = new ArrayList<StartlistParticipationBean>();

    ListIterator<StartlistParticipationBean> listIterator = completeStartlist.listIterator(completeStartlist.size());
    while (listIterator.hasPrevious()) {
      StartlistParticipationBean bean = listIterator.previous();
      if (CompareUtility.equals(bean.getStartTimeWish(), AdditionalInformationCodeType.StartTimeWishEarlyStartCode.ID)) {
        earlyStarts.add(bean);
      }
    }

    for (StartlistParticipationBean earlyStart : earlyStarts) {
      completeStartlist.remove(earlyStart);
      completeStartlist.addFirst(earlyStart);
    }
  }

  public static void applyLateStartTimeWishes(LinkedList<StartlistParticipationBean> completeStartlist) throws ProcessingException {
    // Late Start
    List<StartlistParticipationBean> lateStarts = new ArrayList<StartlistParticipationBean>();

    ListIterator<StartlistParticipationBean> listIterator = completeStartlist.listIterator();
    while (listIterator.hasNext()) {
      StartlistParticipationBean bean = listIterator.next();
      if (CompareUtility.equals(bean.getStartTimeWish(), AdditionalInformationCodeType.StartTimeWishLateStartCode.ID)) {
        lateStarts.add(bean);
      }
    }

    for (StartlistParticipationBean lateStart : lateStarts) {
      completeStartlist.remove(lateStart);
      completeStartlist.addLast(lateStart);
    }

  }

}
