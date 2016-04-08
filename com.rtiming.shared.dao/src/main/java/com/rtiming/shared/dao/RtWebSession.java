package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.Date;
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
 * The persistent class for the rt_web_session database table.
 */
@Entity
@Table(name = "rt_web_session")
public class RtWebSession implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "rt_web_session", sequenceName = "rt_web_session_web_session_nr_seq")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "rt_web_session")
  @Column(name = "web_session_nr")
  private Long webSessionNr;

  @Column(name = "client_ip")
  private String clientIp;

  @Column(name = "evt_last_login")
  private Date evtLastLogin;

  @Column(name = "session_key")
  private String sessionKey;

  @Column(name = "user_agent")
  private String userAgent;

  @Column(name = "account_nr")
  private Long accountNr;

  @Column(name = "client_nr")
  private Long clientNr;

  //bi-directional many-to-one association to RtPosition
  @OneToMany(mappedBy = "rtWebSession")
  private List<RtPosition> rtPositions;

  //bi-directional many-to-one association to RtAccount
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "account_nr", insertable = false, updatable = false)
  private RtAccount rtAccount;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  public RtWebSession() {
  }

  public Long getWebSessionNr() {
    return this.webSessionNr;
  }

  public void setWebSessionNr(Long webSessionNr) {
    this.webSessionNr = webSessionNr;
  }

  public String getClientIp() {
    return this.clientIp;
  }

  public void setClientIp(String clientIp) {
    this.clientIp = clientIp;
  }

  public Date getEvtLastLogin() {
    return this.evtLastLogin;
  }

  public void setEvtLastLogin(Date evtLastLogin) {
    this.evtLastLogin = evtLastLogin;
  }

  public String getSessionKey() {
    return this.sessionKey;
  }

  public void setSessionKey(String sessionKey) {
    this.sessionKey = sessionKey;
  }

  public String getUserAgent() {
    return this.userAgent;
  }

  public void setUserAgent(String userAgent) {
    this.userAgent = userAgent;
  }

  public List<RtPosition> getRtPositions() {
    return this.rtPositions;
  }

  public void setRtPositions(List<RtPosition> rtPositions) {
    this.rtPositions = rtPositions;
  }

  public RtPosition addRtPosition(RtPosition rtPosition) {
    getRtPositions().add(rtPosition);
    rtPosition.setRtWebSession(this);

    return rtPosition;
  }

  public RtPosition removeRtPosition(RtPosition rtPosition) {
    getRtPositions().remove(rtPosition);
    rtPosition.setRtWebSession(null);

    return rtPosition;
  }

  public RtAccount getRtAccount() {
    return this.rtAccount;
  }

  public void setRtAccount(RtAccount rtAccount) {
    this.rtAccount = rtAccount;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public void setAccountNr(Long accountNr) {
    this.accountNr = accountNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

}
