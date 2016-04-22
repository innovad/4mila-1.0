package com.rtiming.server.entry.startlist;

import java.util.LinkedList;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType.StartTimeWishEarlyStartCode;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType.StartTimeWishLateStartCode;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class StartlistWishUtilityTest {

  @Test
  public void testEarlyStartTimeWishEmpty() throws Exception {
    LinkedList<StartlistParticipationBean> completeStartlist = new LinkedList<StartlistParticipationBean>();
    StartlistWishUtility.applyEarlyStartTimeWishes(completeStartlist);
  }

  @Test
  public void testEarlyStartTimeNoWish() throws Exception {
    doTest(new Long[]{null, null}, new Integer[]{1, 2});
  }

  @Test
  public void testEarlyStartTimeWish() throws Exception {
    doTest(new Long[]{null, StartTimeWishEarlyStartCode.ID}, new Integer[]{2, 1});
  }

  @Test
  public void testLateStartTimeWish() throws Exception {
    doTest(new Long[]{StartTimeWishLateStartCode.ID, null}, new Integer[]{2, 1});
  }

  @Test
  public void testBothStartTimeWish() throws Exception {
    doTest(new Long[]{StartTimeWishLateStartCode.ID, StartTimeWishEarlyStartCode.ID}, new Integer[]{2, 1});
  }

  @Test
  public void testMultipleEarlyStartTimeWishes() throws Exception {
    doTest(new Long[]{null, StartTimeWishEarlyStartCode.ID, StartTimeWishEarlyStartCode.ID}, new Integer[]{2, 3, 1});
  }

  @Test
  public void testMultipleLateStartTimeWishes() throws Exception {
    doTest(new Long[]{StartTimeWishLateStartCode.ID, StartTimeWishLateStartCode.ID, null}, new Integer[]{3, 1, 2});
  }

  @Test
  public void testTooManyLateStartTimeWishes() throws Exception {
    doTest(new Long[]{StartTimeWishLateStartCode.ID, StartTimeWishLateStartCode.ID, StartTimeWishLateStartCode.ID}, new Integer[]{1, 2, 3});
  }

  private void doTest(Long[] wishUids, Integer[] expectedResult) throws ProcessingException {
    LinkedList<StartlistParticipationBean> completeStartlist = new LinkedList<StartlistParticipationBean>();

    // build startlist
    long participationCount = 0;
    for (Long entityNr : wishUids) {
      StartlistParticipationBean participation = createParticipation(participationCount, entityNr);
      completeStartlist.add(participation);
      participationCount++;
    }
    LinkedList<StartlistParticipationBean> original = new LinkedList<StartlistParticipationBean>();
    original.addAll(completeStartlist);

    StartlistWishUtility.applyEarlyStartTimeWishes(completeStartlist);
    StartlistWishUtility.applyLateStartTimeWishes(completeStartlist);
    Assert.assertEquals(participationCount + " Participations", participationCount, completeStartlist.size());

    int x = 0;
    for (Integer order : expectedResult) {
      Assert.assertEquals("Expected Sort Order", original.get(order - 1), completeStartlist.get(x));
      x++;
    }

  }

  private StartlistParticipationBean createParticipation(Long entryNr, Long wishUid) {
    StartlistParticipationBean participation = new StartlistParticipationBean();
    participation.setEventNr(1L);
    participation.setEntryNr(entryNr);
    participation.setStartTimeWish(wishUid);
    return participation;
  }

}
