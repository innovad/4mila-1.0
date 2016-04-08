package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.List;

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
 * The persistent class for the rt_event_startblock database table.
 */
@Entity
@Table(name = "rt_event_startblock")
@UploadConfiguration(uploadOrder = 519)
public class RtEventStartblock implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtEventStartblockKey id;

  private Long sortcode;

  //bi-directional many-to-one association to RtClient
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "client_nr", insertable = false, updatable = false)
  private RtClient rtClient;

  //bi-directional many-to-one association to RtEvent
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "event_nr", insertable = false, updatable = false)
  })
  private RtEvent rtEvent;

  //bi-directional many-to-one association to RtUc
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "startblock_uid", referencedColumnName = "uc_uid", insertable = false, updatable = false)
  })
  private RtUc rtUc;

  //bi-directional many-to-one association to RtParticipation
  @OneToMany(mappedBy = "rtEventStartblock")
  private List<RtParticipation> rtParticipations;

  public RtEventStartblock() {
  }

  public RtEventStartblockKey getId() {
    return this.id;
  }

  public void setId(RtEventStartblockKey id) {
    this.id = id;
  }

  public Long getSortcode() {
    return this.sortcode;
  }

  public void setSortcode(Long sortcode) {
    this.sortcode = sortcode;
  }

  public RtClient getRtClient() {
    return this.rtClient;
  }

  public void setRtClient(RtClient rtClient) {
    this.rtClient = rtClient;
  }

  public RtEvent getRtEvent() {
    return this.rtEvent;
  }

  public void setRtEvent(RtEvent rtEvent) {
    this.rtEvent = rtEvent;
  }

  public RtUc getRtUc() {
    return this.rtUc;
  }

  public void setRtUc(RtUc rtUc) {
    this.rtUc = rtUc;
  }

  public List<RtParticipation> getRtParticipations() {
    return this.rtParticipations;
  }

  public void setRtParticipations(List<RtParticipation> rtParticipations) {
    this.rtParticipations = rtParticipations;
  }

  public RtParticipation addRtParticipation(RtParticipation rtParticipation) {
    getRtParticipations().add(rtParticipation);
    rtParticipation.setRtEventStartblock(this);

    return rtParticipation;
  }

  public RtParticipation removeRtParticipation(RtParticipation rtParticipation) {
    getRtParticipations().remove(rtParticipation);
    rtParticipation.setRtEventStartblock(null);

    return rtParticipation;
  }

}
