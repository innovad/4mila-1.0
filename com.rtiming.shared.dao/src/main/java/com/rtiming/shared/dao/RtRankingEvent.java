package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the rt_ranking_event database table.
 */
@Entity
@Table(name = "rt_ranking_event")
public class RtRankingEvent implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtRankingEventKey id;

  @Column(name = "decimal_places")
  private Long decimalPlaces;

  @Column(name = "format_uid")
  private Long formatUid;

  @Column(name = "formula", columnDefinition = "clob")
  private String formula;

  private Long sortcode;

  @Column(name = "sorting_uid")
  private Long sortingUid;

  @Transient
  private Long formulaTypeUid;

  @Transient
  private Long timePrecisionUid;

  //bi-directional many-to-one association to RtEvent
  @ManyToOne
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "event_nr", referencedColumnName = "event_nr", insertable = false, updatable = false)
  })
  private RtEvent rtEvent;

  //bi-directional many-to-one association to RtRanking
  @ManyToOne
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "ranking_nr", referencedColumnName = "ranking_nr", insertable = false, updatable = false)
  })
  private RtRanking rtRanking;

  public RtRankingEvent() {
  }

  public RtRankingEventKey getId() {
    return this.id;
  }

  public void setId(RtRankingEventKey id) {
    this.id = id;
  }

  public Long getDecimalPlaces() {
    return this.decimalPlaces;
  }

  public void setDecimalPlaces(Long decimalPlaces) {
    this.decimalPlaces = decimalPlaces;
  }

  public Long getFormatUid() {
    return this.formatUid;
  }

  public void setFormatUid(Long formatUid) {
    this.formatUid = formatUid;
  }

  public String getFormula() {
    return this.formula;
  }

  public void setFormula(String formula) {
    this.formula = formula;
  }

  public Long getSortcode() {
    return this.sortcode;
  }

  public void setSortcode(Long sortcode) {
    this.sortcode = sortcode;
  }

  public Long getSortingUid() {
    return this.sortingUid;
  }

  public void setSortingUid(Long sortingUid) {
    this.sortingUid = sortingUid;
  }

  public RtEvent getRtEvent() {
    return this.rtEvent;
  }

  public void setRtEvent(RtEvent rtEvent) {
    this.rtEvent = rtEvent;
  }

  public RtRanking getRtRanking() {
    return this.rtRanking;
  }

  public void setRtRanking(RtRanking rtRanking) {
    this.rtRanking = rtRanking;
  }

  public Long getFormulaTypeUid() {
    return formulaTypeUid;
  }

  public void setFormulaTypeUid(Long formulaTypeUid) {
    this.formulaTypeUid = formulaTypeUid;
  }

  public Long getTimePrecisionUid() {
    return timePrecisionUid;
  }

  public void setTimePrecisionUid(Long timePrecisionUid) {
    this.timePrecisionUid = timePrecisionUid;
  }

}
