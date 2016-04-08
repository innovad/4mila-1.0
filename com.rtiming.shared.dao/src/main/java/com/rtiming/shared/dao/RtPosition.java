package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 * The persistent class for the rt_position database table.
 */
@Entity
@Table(name = "rt_position")
public class RtPosition implements Serializable {
  private static final long serialVersionUID = 1L;

  @Id
  @SequenceGenerator(name = "RT_POSITION_POSITIONNR_GENERATOR", sequenceName = "rt_position_position_nr_seq")
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "RT_POSITION_POSITIONNR_GENERATOR")
  @Column(name = "position_nr")
  private Long positionNr;

  private double accuracy;

  private double altitude;

  @Column(name = "altitude_accuracy")
  private double altitudeAccuracy;

  private double heading;

  private double latitude;

  private double longitude;

  private double speed;

  @Column(name = "update_time")
  private Date updateTime;

  @Column(name = "web_session_nr")
  private Long webSessionNr;

  @Column(name = "race_nr")
  private Long raceNr;

  @Column(name = "client_nr")
  private Long clientNr;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtRace
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "race_nr", referencedColumnName = "race_nr", insertable = false, updatable = false)
  })
  private RtRace rtRace;

  //bi-directional many-to-one association to RtWebSession
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "web_session_nr", insertable = false, updatable = false)
  private RtWebSession rtWebSession;

  public RtPosition() {
  }

  public Long getPositionNr() {
    return this.positionNr;
  }

  public void setPositionNr(Long positionNr) {
    this.positionNr = positionNr;
  }

  public double getAccuracy() {
    return this.accuracy;
  }

  public void setAccuracy(double accuracy) {
    this.accuracy = accuracy;
  }

  public double getAltitude() {
    return this.altitude;
  }

  public void setAltitude(double altitude) {
    this.altitude = altitude;
  }

  public double getAltitudeAccuracy() {
    return this.altitudeAccuracy;
  }

  public void setAltitudeAccuracy(double altitudeAccuracy) {
    this.altitudeAccuracy = altitudeAccuracy;
  }

  public double getHeading() {
    return this.heading;
  }

  public void setHeading(double heading) {
    this.heading = heading;
  }

  public double getLatitude() {
    return this.latitude;
  }

  public void setLatitude(double latitude) {
    this.latitude = latitude;
  }

  public double getLongitude() {
    return this.longitude;
  }

  public void setLongitude(double longitude) {
    this.longitude = longitude;
  }

  public double getSpeed() {
    return this.speed;
  }

  public void setSpeed(double speed) {
    this.speed = speed;
  }

  public Date getUpdateTime() {
    return this.updateTime;
  }

  public void setUpdateTime(Date updateTime) {
    this.updateTime = updateTime;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public RtRace getRtRace() {
    return this.rtRace;
  }

  public void setRtRace(RtRace rtRace) {
    this.rtRace = rtRace;
  }

  public RtWebSession getRtWebSession() {
    return this.rtWebSession;
  }

  public void setRtWebSession(RtWebSession rtWebSession) {
    this.rtWebSession = rtWebSession;
  }

  public void setWebSessionNr(Long webSessionNr) {
    this.webSessionNr = webSessionNr;
  }

  public void setRaceNr(Long raceNr) {
    this.raceNr = raceNr;
  }

  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

}
