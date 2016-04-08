package com.rtiming.shared.common.database.sql;

import java.io.Serializable;

import com.rtiming.shared.common.EntityCodeType;

public class ClubBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long clubNr;
  private Long clientNr;
  private String name;
  private String shortcut;
  private String extKey;
  private Long contactRunnerNr;
  private final AdditionalInformationBean addInfo;

  public ClubBean() {
    addInfo = new AdditionalInformationBean();
    addInfo.setEntityUid(EntityCodeType.ClubCode.ID);
  }

  public Long getClubNr() {
    return clubNr;
  }

  public void setClubNr(Long clubNr) {
    this.clubNr = clubNr;
  }

  public Long getClientNr() {
    return clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getShortcut() {
    return shortcut;
  }

  public void setShortcut(String shortcut) {
    this.shortcut = shortcut;
  }

  public String getExtKey() {
    return extKey;
  }

  public void setExtKey(String extKey) {
    this.extKey = extKey;
  }

  public Long getContactRunnerNr() {
    return contactRunnerNr;
  }

  public void setContactRunnerNr(Long contactRunnerNr) {
    this.contactRunnerNr = contactRunnerNr;
  }

  public AdditionalInformationBean getAddInfo() {
    return addInfo;
  }

}
