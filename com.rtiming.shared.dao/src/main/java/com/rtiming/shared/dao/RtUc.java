package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.Index;

import com.rtiming.shared.dao.util.UploadConfiguration;

/**
 * The persistent class for the rt_uc database table.
 */
@Entity
@Table(name = "rt_uc")
@UploadConfiguration(uploadOrder = 10, cleanup = false)
public class RtUc implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtUcKey id;

  private Boolean active;

  @Column(name = "code_type")
  @Index(name = "rt_uc_ix_code_type")
  private Long codeType;

  @Index(name = "rt_uc_ix_shortcut")
  private String shortcut;

  //bi-directional many-to-one association to RtCurrency
  @OneToMany(mappedBy = "rtUc")
  private List<RtCurrency> rtCurrencies;

  //bi-directional many-to-one association to RtEventClass
  @OneToMany(mappedBy = "rtUc1")
  private List<RtEventClass> rtEventClasses1;

  //bi-directional many-to-one association to RtEventClass
  @OneToMany(mappedBy = "rtUc2")
  private List<RtEventClass> rtEventClasses2;

  //bi-directional many-to-one association to RtEventStartblock
  @OneToMany(mappedBy = "rtUc")
  private List<RtEventStartblock> rtEventStartblocks;

  //bi-directional many-to-one association to RtRace
  @OneToMany(mappedBy = "rtUc")
  private List<RtRace> rtRaces;

  //bi-directional many-to-one association to RtUcl
  @OneToMany(mappedBy = "rtUc")
  private List<RtUcl> rtUcls;

  //bi-directional many-to-one association to RtUcl
  @OneToMany(mappedBy = "rtUc")
  private List<RtCountry> rtCountries;

  public RtUc() {
  }

  public RtUcKey getId() {
    return this.id;
  }

  public void setId(RtUcKey id) {
    this.id = id;
  }

  public Boolean getActive() {
    return this.active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Long getCodeType() {
    return this.codeType;
  }

  public void setCodeType(Long codeType) {
    this.codeType = codeType;
  }

  public String getShortcut() {
    return this.shortcut;
  }

  public void setShortcut(String shortcut) {
    this.shortcut = shortcut;
  }

  public List<RtCurrency> getRtCurrencies() {
    return this.rtCurrencies;
  }

  public void setRtCurrencies(List<RtCurrency> rtCurrencies) {
    this.rtCurrencies = rtCurrencies;
  }

  public RtCurrency addRtCurrency(RtCurrency rtCurrency) {
    getRtCurrencies().add(rtCurrency);
    rtCurrency.setRtUc(this);

    return rtCurrency;
  }

  public RtCurrency removeRtCurrency(RtCurrency rtCurrency) {
    getRtCurrencies().remove(rtCurrency);
    rtCurrency.setRtUc(null);

    return rtCurrency;
  }

  public List<RtEventClass> getRtEventClasses1() {
    return this.rtEventClasses1;
  }

  public void setRtEventClasses1(List<RtEventClass> rtEventClasses1) {
    this.rtEventClasses1 = rtEventClasses1;
  }

  public RtEventClass addRtEventClasses1(RtEventClass eventClass) {
    getRtEventClasses1().add(eventClass);
    eventClass.setRtUc1(this);

    return eventClass;
  }

  public RtEventClass removeRtEventClasses1(RtEventClass eventClass) {
    getRtEventClasses1().remove(eventClass);
    eventClass.setRtUc1(null);

    return eventClass;
  }

  public List<RtEventClass> getRtEventClasses2() {
    return this.rtEventClasses2;
  }

  public void setRtEventClasses2(List<RtEventClass> rtEventClasses2) {
    this.rtEventClasses2 = rtEventClasses2;
  }

  public RtEventClass addRtEventClasses2(RtEventClass eventClass) {
    getRtEventClasses2().add(eventClass);
    eventClass.setRtUc2(this);

    return eventClass;
  }

  public RtEventClass removeRtEventClasses2(RtEventClass eventClass) {
    getRtEventClasses2().remove(eventClass);
    eventClass.setRtUc2(null);

    return eventClass;
  }

  public List<RtEventStartblock> getRtEventStartblocks() {
    return this.rtEventStartblocks;
  }

  public void setRtEventStartblocks(List<RtEventStartblock> rtEventStartblocks) {
    this.rtEventStartblocks = rtEventStartblocks;
  }

  public RtEventStartblock addRtEventStartblock(RtEventStartblock rtEventStartblock) {
    getRtEventStartblocks().add(rtEventStartblock);
    rtEventStartblock.setRtUc(this);

    return rtEventStartblock;
  }

  public RtEventStartblock removeRtEventStartblock(RtEventStartblock rtEventStartblock) {
    getRtEventStartblocks().remove(rtEventStartblock);
    rtEventStartblock.setRtUc(null);

    return rtEventStartblock;
  }

  public List<RtRace> getRtRaces() {
    return this.rtRaces;
  }

  public void setRtRaces(List<RtRace> rtRaces) {
    this.rtRaces = rtRaces;
  }

  public RtRace addRtRace(RtRace rtRace) {
    getRtRaces().add(rtRace);
    rtRace.setRtUc(this);

    return rtRace;
  }

  public RtRace removeRtRace(RtRace rtRace) {
    getRtRaces().remove(rtRace);
    rtRace.setRtUc(null);

    return rtRace;
  }

  public List<RtUcl> getRtUcls() {
    return this.rtUcls;
  }

  public void setRtUcls(List<RtUcl> rtUcls) {
    this.rtUcls = rtUcls;
  }

  public RtUcl addRtUcl(RtUcl rtUcl) {
    getRtUcls().add(rtUcl);
    rtUcl.setRtUc(this);

    return rtUcl;
  }

  public RtUcl removeRtUcl(RtUcl rtUcl) {
    getRtUcls().remove(rtUcl);
    rtUcl.setRtUc(null);

    return rtUcl;
  }

}
