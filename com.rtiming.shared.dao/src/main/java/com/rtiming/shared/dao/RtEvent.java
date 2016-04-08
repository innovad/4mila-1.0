package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import com.rtiming.shared.dao.util.UploadConfiguration;

/**
 * The persistent class for the rt_event database table.
 */
@Entity
@Table(name = "rt_event")
@UploadConfiguration(uploadOrder = 30)
public class RtEvent implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtEventKey id;

  @Column(name = "evt_finish")
  private Date evtFinish;

  @Column(name = "evt_last_upload")
  private Date evtLastUpload;

  @Column(name = "evt_zero")
  private Date evtZero;

  private String format;

  private String location;

  private String map;

  private String name;

  @Column(name = "punching_system_uid")
  private Long punchingSystemUid;

  @Column(name = "timezone_offset")
  private Integer timezoneOffset;

  @Column(name = "type_uid")
  private Long typeUid;

  //bi-directional many-to-one association to RtEventClass
  @OneToMany
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "event_nr", insertable = false, updatable = false)
  })
  private List<RtEventClass> rtEventClasses;

  public RtEvent() {
  }

  public RtEventKey getId() {
    return this.id;
  }

  public void setId(RtEventKey id) {
    this.id = id;
  }

  public Date getEvtFinish() {
    return this.evtFinish;
  }

  public void setEvtFinish(Date evtFinish) {
    this.evtFinish = evtFinish;
  }

  public Date getEvtLastUpload() {
    return this.evtLastUpload;
  }

  public void setEvtLastUpload(Date evtLastUpload) {
    this.evtLastUpload = evtLastUpload;
  }

  public Date getEvtZero() {
    return this.evtZero;
  }

  public void setEvtZero(Date evtZero) {
    this.evtZero = evtZero;
  }

  public String getFormat() {
    return this.format;
  }

  public void setFormat(String format) {
    this.format = format;
  }

  public String getLocation() {
    return this.location;
  }

  public void setLocation(String location) {
    this.location = location;
  }

  public String getMap() {
    return this.map;
  }

  public void setMap(String map) {
    this.map = map;
  }

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getPunchingSystemUid() {
    return this.punchingSystemUid;
  }

  public void setPunchingSystemUid(Long punchingSystemUid) {
    this.punchingSystemUid = punchingSystemUid;
  }

  public Integer getTimezoneOffset() {
    return this.timezoneOffset;
  }

  public void setTimezoneOffset(Integer timezoneOffset) {
    this.timezoneOffset = timezoneOffset;
  }

  public Long getTypeUid() {
    return this.typeUid;
  }

  public void setTypeUid(Long typeUid) {
    this.typeUid = typeUid;
  }

}
