package com.rtiming.shared.common.database.sql;

import java.io.Serializable;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

public class AddressBean implements Serializable, Cloneable {

  private static final long serialVersionUID = 1L;

  private Long addressNr;
  private Long clientNr;
  private String street;
  private String phone;
  private String fax;
  private String mobile;
  private String email;
  private String www;
  private Long cityNr;

  public Long getAddressNr() {
    return addressNr;
  }

  public void setAddressNr(Long addressNr) {
    this.addressNr = addressNr;
  }

  public Long getClientNr() {
    return clientNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  public String getStreet() {
    return street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getPhone() {
    return phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getFax() {
    return fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getMobile() {
    return mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getWww() {
    return www;
  }

  public void setWww(String www) {
    this.www = www;
  }

  public Long getCityNr() {
    return cityNr;
  }

  public void setCityNr(Long cityNr) {
    this.cityNr = cityNr;
  }

  public AddressBean copy() throws ProcessingException {
    try {
      return clone();
    }
    catch (CloneNotSupportedException e) {
      throw new ProcessingException(e.getMessage(), e);
    }
  }

  @Override
  protected AddressBean clone() throws CloneNotSupportedException {
    Object clone = super.clone();
    if (clone instanceof AddressBean) {
      ((AddressBean) clone).setAddressNr(null);
      return ((AddressBean) clone);
    }
    return null;
  }

}
