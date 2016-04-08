package com.rtiming.shared.common.database.sql;

import java.io.Serializable;

public class AdditionalInformationValueBean implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long additionalInformationUid;
  private Long valueInteger;
  private Double valueDouble;
  private String valueString;

  private Long typeUid;
  private Double valueMin;
  private Double valueMax;
  private Long defaultInteger;
  private Double defaultDecimal;
  private String defaultText;
  private boolean mandatory;
  private Long feeGroupNr;

  public Long getAdditionalInformationUid() {
    return additionalInformationUid;
  }

  public void setAdditionalInformationUid(Long additionalInformationUid) {
    this.additionalInformationUid = additionalInformationUid;
  }

  public Long getValueInteger() {
    return valueInteger;
  }

  public void setValueInteger(Long valueInteger) {
    this.valueInteger = valueInteger;
  }

  public Double getValueDouble() {
    return valueDouble;
  }

  public void setValueDouble(Double valueDouble) {
    this.valueDouble = valueDouble;
  }

  public String getValueString() {
    return valueString;
  }

  public void setValueString(String valueString) {
    this.valueString = valueString;
  }

  public Long getTypeUid() {
    return typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

  public Double getValueMin() {
    return valueMin;
  }

  public void setValueMin(Double valueMin) {
    this.valueMin = valueMin;
  }

  public Double getValueMax() {
    return valueMax;
  }

  public void setValueMax(Double valueMax) {
    this.valueMax = valueMax;
  }

  public Long getDefaultInteger() {
    return defaultInteger;
  }

  public void setDefaultInteger(Long defaultInteger) {
    this.defaultInteger = defaultInteger;
  }

  public Double getDefaultDecimal() {
    return defaultDecimal;
  }

  public void setDefaultDecimal(Double defaultDecimal) {
    this.defaultDecimal = defaultDecimal;
  }

  public String getDefaultText() {
    return defaultText;
  }

  public void setDefaultText(String defaultText) {
    this.defaultText = defaultText;
  }

  public boolean isMandatory() {
    return mandatory;
  }

  public void setMandatory(boolean mandatory) {
    this.mandatory = mandatory;
  }

  public Long getFeeGroupNr() {
    return feeGroupNr;
  }

  public void setFeeGroupNr(Long feeGroupNr) {
    this.feeGroupNr = feeGroupNr;
  }

}
