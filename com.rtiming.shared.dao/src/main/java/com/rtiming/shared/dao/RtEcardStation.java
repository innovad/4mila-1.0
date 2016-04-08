package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;

/**
 * The persistent class for the rt_ecard_station database table.
 */
@Entity
@Table(name = "rt_ecard_station")
public class RtEcardStation implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtEcardStationKey id;

  private Long baud;

  @Column(name = "client_address")
  private String clientAddress;

  private String identifier;

  @Column(name = "modus_uid")
  private Long modusUid;

  private String port;

  @Column(name = "pos_printer")
  private String posPrinter;

  private String printer;

  //bi-directional many-to-one association to RtPunchSession
  @OneToMany(mappedBy = "rtEcardStation")
  private List<RtPunchSession> rtPunchSessions;

  public RtEcardStation() {
  }

  public RtEcardStationKey getId() {
    return id;
  }

  public void setId(RtEcardStationKey id) {
    this.id = id;
  }

  public Long getBaud() {
    return this.baud;
  }

  public void setBaud(Long baud) {
    this.baud = baud;
  }

  public String getClientAddress() {
    return this.clientAddress;
  }

  public void setClientAddress(String clientAddress) {
    this.clientAddress = clientAddress;
  }

  public String getIdentifier() {
    return this.identifier;
  }

  public void setIdentifier(String identifier) {
    this.identifier = identifier;
  }

  public Long getModusUid() {
    return this.modusUid;
  }

  public void setModusUid(Long modusUid) {
    this.modusUid = modusUid;
  }

  public String getPort() {
    return this.port;
  }

  public void setPort(String port) {
    this.port = port;
  }

  public String getPosPrinter() {
    return this.posPrinter;
  }

  public void setPosPrinter(String posPrinter) {
    this.posPrinter = posPrinter;
  }

  public String getPrinter() {
    return this.printer;
  }

  public void setPrinter(String printer) {
    this.printer = printer;
  }

  public List<RtPunchSession> getRtPunchSessions() {
    return this.rtPunchSessions;
  }

  public void setRtPunchSessions(List<RtPunchSession> rtPunchSessions) {
    this.rtPunchSessions = rtPunchSessions;
  }

  public RtPunchSession addRtPunchSession(RtPunchSession rtPunchSession) {
    getRtPunchSessions().add(rtPunchSession);
    rtPunchSession.setRtEcardStation(this);

    return rtPunchSession;
  }

  public RtPunchSession removeRtPunchSession(RtPunchSession rtPunchSession) {
    getRtPunchSessions().remove(rtPunchSession);
    rtPunchSession.setRtEcardStation(null);

    return rtPunchSession;
  }

}
