package com.rtiming.server.entry.startlist;

import java.util.LinkedList;

import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.entry.startlist.StartlistSeparationUtility.Separation;

import org.junit.Assert;

@RunWith(ServerTestRunner.class) @RunWithSubject("admin") @RunWithServerSession(ServerSession.class)
public class StartlistSeparationUtilityTest {

  @Test
  public void testSeparationEmpty() throws Exception {
    LinkedList<StartlistParticipationBean> completeStartlist = new LinkedList<StartlistParticipationBean>();
    StartlistSeparationUtility.separateParticipations(completeStartlist, Separation.CLUB);
    Assert.assertEquals("0 Participations", 0, completeStartlist.size());
  }

  @Test
  public void testSeparationClubNull() throws Exception {
    LinkedList<StartlistParticipationBean> completeStartlist = new LinkedList<StartlistParticipationBean>();
    completeStartlist.add(createParticipation(10L, null, Separation.CLUB));

    StartlistSeparationUtility.separateParticipations(completeStartlist, Separation.CLUB);
    Assert.assertEquals("1 Participation", 1, completeStartlist.size());
  }

  @Test
  public void testSeparation2DifferentEntries() throws Exception {
    doTest(new Long[]{100L, 101L}, new Integer[]{1, 2});
  }

  @Test
  public void testSeparation3EntriesCandidateBefore() throws Exception {
    doTest(new Long[]{100L, 101L, 101L}, new Integer[]{2, 1, 3});
  }

  @Test
  public void testSeparation3EntriesCandidateAfter() throws Exception {
    doTest(new Long[]{101L, 101L, 100L}, new Integer[]{1, 3, 2});
  }

  @Test
  public void testSeparation5EntriesCandidateBefore() throws Exception {
    doTest(new Long[]{100L, 101L, 100L, 101L, 101L}, new Integer[]{2, 3, 4, 1, 5});
  }

  @Test
  public void testSeparation5EntriesCandidateAfter() throws Exception {
    doTest(new Long[]{101L, 101L, 100L, 101L, 100L}, new Integer[]{1, 5, 2, 3, 4});
  }

  @Test
  public void testSeparation5EntriesTriple1() throws Exception {
    doTest(new Long[]{101L, 101L, 101L, 100L, 100L}, new Integer[]{1, 4, 2, 5, 3});
  }

  @Test
  public void testSeparation5EntriesTriple2() throws Exception {
    doTest(new Long[]{100L, 101L, 101L, 101L, 100L}, new Integer[]{2, 1, 3, 5, 4});
  }

  @Test
  public void testSeparation5EntriesTriple3Club() throws Exception {
    doTest(new Long[]{100L, 100L, 101L, 101L, 101L}, new Integer[]{3, 2, 4, 1, 5});
  }

  @Test
  public void testSeparation5EntriesTriple3Nation() throws Exception {
    doTest(new Long[]{100L, 100L, 101L, 101L, 101L}, new Integer[]{3, 2, 4, 1, 5}, Separation.NATION);
  }

  @Test
  public void testSeparationNeighbourDistance() throws Exception {
    doTest(new Long[]{100L, 101L, 100L, 101L, 101L, 100L, 100L}, new Integer[]{1, 2, 3, 4, 6, 5, 7});
  }

  @Test
  public void testNoSolution() throws Exception {
    doTest(new Long[]{100L, 100L, 100L}, new Integer[]{1, 2, 3});
  }

  private void doTest(Long[] randomOrder, Integer[] expectedSeparatedOrder) {
    doTest(randomOrder, expectedSeparatedOrder, Separation.CLUB);
  }

  private void doTest(Long[] randomOrder, Integer[] expectedSeparatedOrder, Separation type) {
    LinkedList<StartlistParticipationBean> completeStartlist = new LinkedList<StartlistParticipationBean>();

    // build startlist
    long participationCount = 0;
    for (Long entityNr : randomOrder) {
      StartlistParticipationBean participation = createParticipation(participationCount, entityNr, type);
      completeStartlist.add(participation);
      participationCount++;
    }
    LinkedList<StartlistParticipationBean> original = new LinkedList<StartlistParticipationBean>();
    original.addAll(completeStartlist);

    StartlistSeparationUtility.separateParticipations(completeStartlist, type);
    Assert.assertEquals(participationCount + " Participations", participationCount, completeStartlist.size());

    int x = 0;
    for (Integer order : expectedSeparatedOrder) {
      Assert.assertEquals("Expected Sort Order", original.get(order - 1), completeStartlist.get(x));
      x++;
    }

  }

  private StartlistParticipationBean createParticipation(Long entryNr, Long entityNr, Separation type) {
    StartlistParticipationBean participation = new StartlistParticipationBean();
    participation.setEventNr(1L);
    participation.setEntryNr(entryNr);
    if (Separation.CLUB.equals(type)) {
      participation.setClubNr(entityNr);
    }
    else if (Separation.NATION.equals(type)) {
      participation.setNationUid(entityNr);
    }
    return participation;
  }

}
