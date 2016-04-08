package com.rtiming.shared.club;

import java.io.Serializable;

public class ClubRowData implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long clubNr;
  private String shortcut;
  private String name;
  private String extKey;
  private String contactPerson;
  private Object[] additionalValues;

  public Long getClubNr() {
    return clubNr;
  }

  public void setClubNr(Long clubNr) {
    this.clubNr = clubNr;
  }

  public String getShortcut() {
    return shortcut;
  }

  public void setShortcut(String shortcut) {
    this.shortcut = shortcut;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getExtKey() {
    return extKey;
  }

  public void setExtKey(String extKey) {
    this.extKey = extKey;
  }

  public String getContactPerson() {
    return contactPerson;
  }

  public void setContactPerson(String contactPerson) {
    this.contactPerson = contactPerson;
  }

  public Object[] getAdditionalValues() {
    return additionalValues;
  }

  public void setAdditionalValues(Object[] additionalValues) {
    this.additionalValues = additionalValues;
  }

}
