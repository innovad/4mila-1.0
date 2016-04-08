package com.rtiming.server.entry.startlist;

public class BadPair {

  private final StartlistParticipationBean participation1;
  private final StartlistParticipationBean participation2;
  private final int position;

  public BadPair(StartlistParticipationBean participation1, StartlistParticipationBean participation2, int position) {
    super();
    this.participation1 = participation1;
    this.participation2 = participation2;
    this.position = position;
  }

  public StartlistParticipationBean getParticipation1() {
    return participation1;
  }

  public StartlistParticipationBean getParticipation2() {
    return participation2;
  }

  public int getPosition() {
    return position;
  }

  @Override
  public String toString() {
    return "BadPair [participation1=" + participation1 + ", participation2=" + participation2 + ", position=" + position + "]";
  }

}
