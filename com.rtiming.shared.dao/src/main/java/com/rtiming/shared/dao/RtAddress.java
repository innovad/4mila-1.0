package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.rtiming.shared.dao.util.UploadConfiguration;

/**
 * The persistent class for the rt_address database table.
 */
@Entity
@Table(name = "rt_address")
@UploadConfiguration(uploadOrder = 410, cleanup = false)
public class RtAddress implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtAddressKey id;

  private String email;

  private String fax;

  private String mobile;

  private String phone;

  private String street;

  private String www;

  @Column(name = "city_nr")
  private Long cityNr;

  //bi-directional many-to-one association to RtCity
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "city_nr", referencedColumnName = "city_nr", insertable = false, updatable = false),
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false)
  })
  private RtCity rtCity;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtRace
  @OneToMany(mappedBy = "rtAddress")
  private List<RtRace> rtRaces;

  //bi-directional many-to-one association to RtRunner
  @OneToMany(mappedBy = "rtAddress")
  private List<RtRunner> rtRunners;

  public RtAddress() {
  }

  public RtAddressKey getId() {
    return this.id;
  }

  public void setId(RtAddressKey id) {
    this.id = id;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getFax() {
    return this.fax;
  }

  public void setFax(String fax) {
    this.fax = fax;
  }

  public String getMobile() {
    return this.mobile;
  }

  public void setMobile(String mobile) {
    this.mobile = mobile;
  }

  public String getPhone() {
    return this.phone;
  }

  public void setPhone(String phone) {
    this.phone = phone;
  }

  public String getStreet() {
    return this.street;
  }

  public void setStreet(String street) {
    this.street = street;
  }

  public String getWww() {
    return this.www;
  }

  public void setWww(String www) {
    this.www = www;
  }

  public RtCity getRtCity() {
    return this.rtCity;
  }

  public void setRtCity(RtCity rtCity) {
    this.rtCity = rtCity;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public List<RtRace> getRtRaces() {
    return this.rtRaces;
  }

  public void setRtRaces(List<RtRace> rtRaces) {
    this.rtRaces = rtRaces;
  }

  public RtRace addRtRace(RtRace rtRace) {
    getRtRaces().add(rtRace);
    rtRace.setRtAddress(this);

    return rtRace;
  }

  public RtRace removeRtRace(RtRace rtRace) {
    getRtRaces().remove(rtRace);
    rtRace.setRtAddress(null);

    return rtRace;
  }

  public List<RtRunner> getRtRunners() {
    return this.rtRunners;
  }

  public void setRtRunners(List<RtRunner> rtRunners) {
    this.rtRunners = rtRunners;
  }

  public RtRunner addRtRunner(RtRunner rtRunner) {
    getRtRunners().add(rtRunner);
    rtRunner.setRtAddress(this);

    return rtRunner;
  }

  public RtRunner removeRtRunner(RtRunner rtRunner) {
    getRtRunners().remove(rtRunner);
    rtRunner.setRtAddress(null);

    return rtRunner;
  }

}
