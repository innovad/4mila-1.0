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
 * The persistent class for the rt_country database table.
 */
@Entity
@Table(name = "rt_country")
@UploadConfiguration(uploadOrder = 260)
public class RtCountry implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtCountryKey id;

  @Column(name = "country_code", unique = true)
  @Index(name = "rt_country_ix_country_code")
  private String countryCode;

  @Column(name = "nation", unique = true)
  @Index(name = "rt_country_ix_nation")
  private String nation;

  //bi-directional many-to-one association to RtCity
  @OneToMany(mappedBy = "rtCountry")
  private List<RtCity> rtCities;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "country_uid", referencedColumnName = "uc_uid", insertable = false, updatable = false)
  })
  private RtUc rtUc;

  public RtCountry() {
  }

  public RtCountryKey getId() {
    return id;
  }

  public void setId(RtCountryKey id) {
    this.id = id;
  }

  public String getCountryCode() {
    return this.countryCode;
  }

  public void setCountryCode(String countryCode) {
    this.countryCode = countryCode;
  }

  public String getNation() {
    return this.nation;
  }

  public void setNation(String nation) {
    this.nation = nation;
  }

  public List<RtCity> getRtCities() {
    return this.rtCities;
  }

  public void setRtCities(List<RtCity> rtCities) {
    this.rtCities = rtCities;
  }

  public RtCity addRtCity(RtCity rtCity) {
    getRtCities().add(rtCity);
    rtCity.setRtCountry(this);

    return rtCity;
  }

  public RtCity removeRtCity(RtCity rtCity) {
    getRtCities().remove(rtCity);
    rtCity.setRtCountry(null);

    return rtCity;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

}
