package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the rt_account database table.
 */
@Entity
@Table(name = "rt_account")
public class RtAccount implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "rt_account", sequenceName = "rt_account_account_nr_seq")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rt_account")
  @Column(name = "account_nr")
  private Long accountNr;

  private String email;

  @Column(name = "first_name")
  private String firstName;

  @Column(name = "last_name")
  private String lastName;

  private String password;

  private String username;

  @Column(name = "global_client_nr")
  private Long globalClientNr;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "global_client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtRunner
  @OneToMany(mappedBy = "rtAccount")
  private List<RtRunner> rtRunners;

  //bi-directional many-to-one association to RtWebSession
  @OneToMany(mappedBy = "rtAccount")
  private List<RtWebSession> rtWebSessions;

  public RtAccount() {
  }

  public Long getAccountNr() {
    return this.accountNr;
  }

  public void setAccountNr(Long accountNr) {
    this.accountNr = accountNr;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
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

  public String getPassword() {
    return this.password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getUsername() {
    return this.username;
  }

  public void setUsername(String username) {
    this.username = username;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public List<RtRunner> getRtRunners() {
    return this.rtRunners;
  }

  public void setRtRunners(List<RtRunner> rtRunners) {
    this.rtRunners = rtRunners;
  }

  public RtRunner addRtRunner(RtRunner rtRunner) {
    getRtRunners().add(rtRunner);
    rtRunner.setRtAccount(this);

    return rtRunner;
  }

  public RtRunner removeRtRunner(RtRunner rtRunner) {
    getRtRunners().remove(rtRunner);
    rtRunner.setRtAccount(null);

    return rtRunner;
  }

  public List<RtWebSession> getRtWebSessions() {
    return this.rtWebSessions;
  }

  public void setRtWebSessions(List<RtWebSession> rtWebSessions) {
    this.rtWebSessions = rtWebSessions;
  }

  public RtWebSession addRtWebSession(RtWebSession rtWebSession) {
    getRtWebSessions().add(rtWebSession);
    rtWebSession.setRtAccount(this);

    return rtWebSession;
  }

  public RtWebSession removeRtWebSession(RtWebSession rtWebSession) {
    getRtWebSessions().remove(rtWebSession);
    rtWebSession.setRtAccount(null);

    return rtWebSession;
  }

  public Long getGlobalClientNr() {
    return globalClientNr;
  }

}
