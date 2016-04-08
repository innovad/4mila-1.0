package com.rtiming.server.entry.startlist;

import java.util.LinkedList;
import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;

public final class StartlistSeparationUtility {

  private static IScoutLogger logger = ScoutLogManager.getLogger(StartlistSeparationUtility.class);

  private StartlistSeparationUtility() {
  }

  public enum Separation {
    CLUB,
    NATION,
    CLUB_AND_NATION
  }

  private static boolean isSimilar(StartlistParticipationBean bean1, StartlistParticipationBean bean2, Separation type) {
    if (bean1 != null && bean2 != null) {
      if (Separation.CLUB.equals(type)) {
        return CompareUtility.equals(bean1.getClubNr(), bean2.getClubNr());
      }
      else if (Separation.NATION.equals(type)) {
        return CompareUtility.equals(bean1.getNationUid(), bean2.getNationUid());
      }
    }
    return false;
  }

  /**
   * @param completeStartlist
   * @param type
   */
  public static void separateParticipations(LinkedList<StartlistParticipationBean> completeStartlist, Separation type) {

    if (logger.isDebugEnabled()) {
      logger.debug("** BEFORE **");
      for (StartlistParticipationBean part : completeStartlist) {
        logger.debug(completeStartlist.indexOf(part) + ", " + part);
      }
      logger.debug("*****");
    }

    BadPair nextBadPair = findNextBadPair(completeStartlist, 0, type);

    int iterationCounter = 0;
    while (nextBadPair != null && iterationCounter < 10) {
      logger.debug("Bad Pair: " + nextBadPair);
      StartlistParticipationBean neighbour = findClosestNeighbour(completeStartlist, nextBadPair, type);

      // swap
      if (neighbour != null) {
        logger.debug("Neighbour: " + neighbour);
        StartlistParticipationBean badBean1 = nextBadPair.getParticipation1();
        // remove neighbour
        completeStartlist.remove(neighbour);
        // insert neighbour between bad pair
        int pos = completeStartlist.indexOf(badBean1);
        completeStartlist.add(pos + 1, neighbour);
        logger.debug("Insert " + neighbour + " after " + badBean1);

        logger.debug("** INTER **" + iterationCounter);
        for (StartlistParticipationBean part : completeStartlist) {
          logger.debug(completeStartlist.indexOf(part) + ", " + part);
        }
        logger.debug("*****");

      }

      nextBadPair = findNextBadPair(completeStartlist, nextBadPair.getPosition() + 2, type);
      iterationCounter++;
    }

    if (logger.isDebugEnabled()) {
      logger.debug("** AFTER **");
      for (StartlistParticipationBean part : completeStartlist) {
        logger.debug(completeStartlist.indexOf(part) + ", " + part);
      }
      logger.debug("*****");
    }

  }

  private static BadPair findNextBadPair(List<StartlistParticipationBean> completeStartlist, int startPos, Separation type) {
    StartlistParticipationBean previousParticipation = null;
    int count = 0;
    for (StartlistParticipationBean bean : completeStartlist) {
      if (count >= (startPos - 1) && StartlistSeparationUtility.isSimilar(previousParticipation, bean, type)) {
        return new BadPair(previousParticipation, bean, completeStartlist.indexOf(previousParticipation));
      }
      previousParticipation = bean;
      count++;
    }
    return null;
  }

  private static StartlistParticipationBean getIfExists(List<StartlistParticipationBean> list, int k) {
    if (k >= 0 && k < list.size()) {
      return list.get(k);
    }
    return null;
  }

  private static StartlistParticipationBean findClosestNeighbour(List<StartlistParticipationBean> completeStartlist, BadPair badPair, Separation type) {
    int pos = badPair.getPosition();

    // search previous
    StartlistParticipationBean previousNeighbour = null;
    Integer previousNeighbourDistance = null;
    for (int i = pos; i >= 0; i--) {
      StartlistParticipationBean candidateBefore = getIfExists(completeStartlist, i - 1);
      StartlistParticipationBean candidate = getIfExists(completeStartlist, i);
      StartlistParticipationBean candidateAfter = getIfExists(completeStartlist, i + 1);
      if (isCandidate(badPair.getParticipation1(), candidate, candidateBefore, candidateAfter, type)) {
        previousNeighbour = candidate;
        previousNeighbourDistance = pos - i;
        break;
      }
    }

    // search following
    StartlistParticipationBean followingNeighbour = null;
    Integer followingNeighbourDistance = null;
    for (int i = pos; i < completeStartlist.size(); i++) {
      StartlistParticipationBean candidateBefore = getIfExists(completeStartlist, i - 1);
      StartlistParticipationBean candidate = getIfExists(completeStartlist, i);
      StartlistParticipationBean candidateAfter = getIfExists(completeStartlist, i + 1);
      if (isCandidate(badPair.getParticipation2(), candidate, candidateAfter, candidateBefore, type)) {
        followingNeighbour = candidate;
        followingNeighbourDistance = i - pos;
        break;
      }
    }

    // return closest neighbour, previous neighbour if same distance, otherwise null
    if (previousNeighbourDistance != null && followingNeighbourDistance != null) {
      if (followingNeighbourDistance < previousNeighbourDistance) {
        return followingNeighbour;
      }
      else {
        return previousNeighbour;
      }
    }
    if (previousNeighbour != null) {
      return previousNeighbour;
    }
    else if (followingNeighbour != null) {
      return followingNeighbour;
    }
    return null;
  }

  private static boolean isCandidate(StartlistParticipationBean bad, StartlistParticipationBean candidate, StartlistParticipationBean bean1, StartlistParticipationBean bean2, Separation type) {
    return candidate != null &&
        !StartlistSeparationUtility.isSimilar(candidate, bad, type) &&
        !StartlistSeparationUtility.isSimilar(bean1, bean2, type);
  }

}
