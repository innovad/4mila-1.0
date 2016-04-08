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

import org.hibernate.annotations.Index;

import com.rtiming.shared.dao.util.UploadConfiguration;

/**
 * The persistent class for the rt_city database table.
 */
@Entity
@Table(name = "rt_city")
@UploadConfiguration(uploadOrder = 400, cleanup = false)
public class RtCity implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtCityKey id;

  @Column(name = "area_uid")
  private Long areaUid;

  @Index(name = "rt_city_ix_name_region_zip")
  private String name;

  @Index(name = "rt_city_ix_name_region_zip")
  private String region;

  @Index(name = "rt_city_ix_name_region_zip")
  private String zip;

  @Column(name = "country_uid")
  private Long countryUid;

  //bi-directional many-to-one association to RtAddress
  @OneToMany(mappedBy = "rtCity")
  private List<RtAddress> rtAddresses;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtCountry
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({@JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false), @JoinColumn(name = "country_uid", referencedColumnName = "country_uid", insertable = false, updatable = false)
  })
  private RtCountry rtCountry;

  public RtCity() {
  }

  public RtCityKey getId() {
    return this.id;
  }

  public void setId(RtCityKey id) {
    this.id = id;
  }

  public Long getAreaUid() {
    return this.areaUid;
  }

  public void setAreaUid(Long areaUid) {
    this.areaUid = areaUid;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getRegion() {
    return this.region;
  }

  public void setRegion(String region) {
    this.region = region;
  }

  public String getZip() {
    return this.zip;
  }

  public void setZip(String zip) {
    this.zip = zip;
  }

  public List<RtAddress> getRtAddresses() {
    return this.rtAddresses;
  }

  public void setRtAddresses(List<RtAddress> rtAddresses) {
    this.rtAddresses = rtAddresses;
  }

  public RtAddress addRtAddress(RtAddress rtAddress) {
    getRtAddresses().add(rtAddress);
    rtAddress.setRtCity(this);

    return rtAddress;
  }

  public RtAddress removeRtAddress(RtAddress rtAddress) {
    getRtAddresses().remove(rtAddress);
    rtAddress.setRtCity(null);

    return rtAddress;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public RtCountry getRtCountry() {
    return this.rtCountry;
  }

  public void setRtCountry(RtCountry rtCountry) {
    this.rtCountry = rtCountry;
  }

}
