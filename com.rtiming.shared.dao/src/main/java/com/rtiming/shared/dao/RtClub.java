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
 * The persistent class for the rt_club database table.
 */
@Entity
@Table(name = "rt_club")
@UploadConfiguration(uploadOrder = 420, filteredColumns = {"contact_runner_nr"}, cleanup = false)
public class RtClub implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtClubKey id;

  @Column(name = "ext_key")
  private String extKey;

  private String name;

  private String shortcut;

  @Column(name = "contact_runner_nr")
  private Long contactRunnerNr;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtRunner
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "contact_runner_nr", referencedColumnName = "runner_nr", insertable = false, updatable = false)
  })
  private RtRunner rtRunner;

  //bi-directional many-to-one association to RtRace
  @OneToMany(mappedBy = "rtClub")
  private List<RtRace> rtRaces;

  //bi-directional many-to-one association to RtRunner
  @OneToMany(mappedBy = "rtClub")
  private List<RtRunner> rtRunners;

  public RtClub() {
  }

  public RtClubKey getId() {
    return this.id;
  }

  public void setId(RtClubKey id) {
    this.id = id;
  }

  public String getExtKey() {
    return this.extKey;
  }

  public void setExtKey(String extKey) {
    this.extKey = extKey;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getShortcut() {
    return this.shortcut;
  }

  public void setShortcut(String shortcut) {
    this.shortcut = shortcut;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public RtRunner getRtRunner() {
    return this.rtRunner;
  }

  public void setRtRunner(RtRunner rtRunner) {
    this.rtRunner = rtRunner;
  }

  public List<RtRace> getRtRaces() {
    return this.rtRaces;
  }

  public void setRtRaces(List<RtRace> rtRaces) {
    this.rtRaces = rtRaces;
  }

  public RtRace addRtRace(RtRace rtRace) {
    getRtRaces().add(rtRace);
    rtRace.setRtClub(this);

    return rtRace;
  }

  public RtRace removeRtRace(RtRace rtRace) {
    getRtRaces().remove(rtRace);
    rtRace.setRtClub(null);

    return rtRace;
  }

  public List<RtRunner> getRtRunners() {
    return this.rtRunners;
  }

  public void setRtRunners(List<RtRunner> rtRunners) {
    this.rtRunners = rtRunners;
  }

  public RtRunner addRtRunner(RtRunner runner) {
    getRtRunners().add(runner);
    runner.setRtClub(this);

    return runner;
  }

  public RtRunner removeRtRunner(RtRunner runner) {
    getRtRunners().remove(runner);
    runner.setRtClub(null);

    return runner;
  }

}
