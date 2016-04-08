package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Index;

import com.rtiming.shared.dao.util.UploadConfiguration;

/**
 * The persistent class for the rt_runner database table.
 */
@Entity
@Table(name = "rt_runner")
@UploadConfiguration(uploadOrder = 430, filteredColumns = "account_nr", cleanup = false)
public class RtRunner implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtRunnerKey id;

  private Boolean active;

  @Column(name = "default_class_uid")
  private Long defaultClassUid;

  @Temporal(TemporalType.DATE)
  @Column(name = "evt_birth")
  private Date evtBirth;

  @Column(name = "ext_key")
  @Index(name = "rt_runner_ix_ext_key")
  private String extKey;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  @Column(name = "nation_uid")
  private Long nationUid;

  @Column(name = "sex_uid")
  private Long sexUid;

  @Column(name = "address_nr")
  private Long addressNr;

  @Column(name = "account_nr")
  private Long accountNr;

  @Column(name = "club_nr")
  private Long clubNr;

  @Column(name = "ecard_nr")
  private Long eCardNr;

  private Long year;

  //bi-directional many-to-one association to RtClub
  @OneToMany(mappedBy = "rtRunner")
  private List<RtClub> rtClubs;

  //bi-directional many-to-one association to RtRace
  @OneToMany(mappedBy = "rtRunner")
  private List<RtRace> rtRaces;

  //bi-directional many-to-one association to RtAccount
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_nr", insertable = false, updatable = false)
  private RtAccount rtAccount;

  //bi-directional many-to-one association to RtAddress
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "address_nr", referencedColumnName = "address_nr", insertable = false, updatable = false),
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false)
  })
  private RtAddress rtAddress;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtClub
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "club_nr", referencedColumnName = "club_nr", insertable = false, updatable = false)
  })
  private RtClub rtClub;

  //bi-directional many-to-one association to RtEcard
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "ecard_nr", referencedColumnName = "ecard_nr", insertable = false, updatable = false)
  })
  private RtEcard rtEcard;

  //bi-directional many-to-one association to RtCountry
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "nation_uid", referencedColumnName = "country_uid", insertable = false, updatable = false)
  })
  private RtCountry rtCountry;

  public RtRunner() {
  }

  public RtRunnerKey getId() {
    return this.id;
  }

  public void setId(RtRunnerKey id) {
    this.id = id;
  }

  public Boolean getActive() {
    return this.active;
  }

  public void setActive(Boolean active) {
    this.active = active;
  }

  public Long getDefaultClassUid() {
    return this.defaultClassUid;
  }

  public void setDefaultClassUid(Long defaultClassUid) {
    this.defaultClassUid = defaultClassUid;
  }

  public Date getEvtBirth() {
    return this.evtBirth;
  }

  public void setEvtBirth(Date evtBirth) {
    this.evtBirth = evtBirth;
  }

  public String getExtKey() {
    return this.extKey;
  }

  public void setExtKey(String extKey) {
    this.extKey = extKey;
  }

  public String getFirstName() {
    return this.firstName;
  }

  public void setFirstName(String firstName) {
    this.firstName = firstName;
  }

  public String getLastName() {
    return this.lastName;
  }

  public void setLastName(String lastName) {
    this.lastName = lastName;
  }

  public Long getNationUid() {
    return this.nationUid;
  }

  public void setNationUid(Long nationUid) {
    this.nationUid = nationUid;
  }

  public Long getSexUid() {
    return this.sexUid;
  }

  public void setSexUid(Long sexUid) {
    this.sexUid = sexUid;
  }

  public Long getYear() {
    return this.year;
  }

  public void setYear(Long year) {
    this.year = year;
  }

  public List<RtClub> getRtClubs() {
    return this.rtClubs;
  }

  public void setRtClubs(List<RtClub> rtClubs) {
    this.rtClubs = rtClubs;
  }

  public RtClub addRtClub(RtClub club) {
    getRtClubs().add(club);
    club.setRtRunner(this);

    return club;
  }

  public RtClub removeRtClub(RtClub club) {
    getRtClubs().remove(club);
    club.setRtRunner(null);

    return club;
  }

  public List<RtRace> getRtRaces() {
    return this.rtRaces;
  }

  public void setRtRaces(List<RtRace> rtRaces) {
    this.rtRaces = rtRaces;
  }

  public RtRace addRtRace(RtRace rtRace) {
    getRtRaces().add(rtRace);
    rtRace.setRtRunner(this);

    return rtRace;
  }

  public RtRace removeRtRace(RtRace rtRace) {
    getRtRaces().remove(rtRace);
    rtRace.setRtRunner(null);

    return rtRace;
  }

  public RtAccount getRtAccount() {
    return this.rtAccount;
  }

  public void setRtAccount(RtAccount rtAccount) {
    this.rtAccount = rtAccount;
  }

  public RtAddress getRtAddress() {
    return this.rtAddress;
  }

  public void setRtAddress(RtAddress rtAddress) {
    this.rtAddress = rtAddress;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public RtClub getRtClub() {
    return this.rtClub;
  }

  public void setRtClub(RtClub rtClub) {
    this.rtClub = rtClub;
  }

  public RtEcard getRtEcard() {
    return this.rtEcard;
  }

  public void setRtEcard(RtEcard rtEcard) {
    this.rtEcard = rtEcard;
  }

  public void setEcardNr(Long ecardNr) {
    this.eCardNr = ecardNr;
  }

  public void setClubNr(Long clubNr) {
    this.clubNr = clubNr;
  }

  public Long getAddressNr() {
    return addressNr;
  }

}
