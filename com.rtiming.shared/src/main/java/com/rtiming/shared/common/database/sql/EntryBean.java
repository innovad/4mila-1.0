package com.rtiming.shared.common.database.sql;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.rtiming.shared.common.EntityCodeType;

public class EntryBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long entryNr;
  private Long clientNr;
  private Long statusUid;
  private Long registrationNr;
  private Date evtEntry;
  private Long currencyUid;

  private final AdditionalInformationBean addInfo;

  private final List<RaceBean> races;
  private final List<ParticipationBean> participations;
  private final List<PaymentBean> payments;
  private final List<FeeBean> fees;

  public EntryBean() {
    races = new ArrayList<RaceBean>();
    participations = new ArrayList<ParticipationBean>();
    payments = new ArrayList<PaymentBean>();
    fees = new ArrayList<FeeBean>();

    addInfo = new AdditionalInformationBean();
    addInfo.setEntityUid(EntityCodeType.EntryCode.ID);
  }

  public Long getEntryNr() {
    return entryNr;
  }

  public void setEntryNr(Long entryNr) {
    this.entryNr = entryNr;
  }

  public Long getClientNr() {
    return clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  public Long getStatusUid() {
    return statusUid;
  }

  public void setStatusUid(Long statusUid) {
    this.statusUid = statusUid;
  }

  public Long getRegistrationNr() {
    return registrationNr;
  }

  public void setRegistrationNr(Long registrationNr) {
    this.registrationNr = registrationNr;
  }

  public Date getEvtEntry() {
    return evtEntry;
  }

  public void setEvtEntry(Date evtEntry) {
    this.evtEntry = evtEntry;
  }

  public Long getCurrencyUid() {
    return currencyUid;
  }

  public void setCurrencyUid(Long currencyUid) {
    this.currencyUid = currencyUid;
  }

  public List<RaceBean> getRaces() {
    return races;
  }

  public List<ParticipationBean> getParticipations() {
    return participations;
  }

  public List<PaymentBean> getPayments() {
    return payments;
  }

  public List<FeeBean> getFees() {
    return fees;
  }

  public void addParticipation(ParticipationBean participation) {
    participations.add(participation);
  }

  public void addRace(RaceBean race) {
    races.add(race);
  }

  public void addFee(FeeBean fee) {
    fees.add(fee);
  }

  public AdditionalInformationBean getAddInfo() {
    return addInfo;
  }

  @Override
  public String toString() {
    return "EntryBean [entryNr=" + entryNr + ", addInfo=" + addInfo + ", races=" + races + ", participations=" + participations + ", payments=" + payments + ", fees=" + fees + "]";
  }

}
