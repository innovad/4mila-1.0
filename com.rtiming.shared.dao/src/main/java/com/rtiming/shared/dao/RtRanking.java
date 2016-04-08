package com.rtiming.shared.dao;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * The persistent class for the rt_ranking database table.
 */
@Entity
@Table(name = "rt_ranking")
public class RtRanking implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtRankingKey id;

  @Column(name = "decimal_places")
  private Long decimalPlaces;

  @Column(name = "format_uid")
  private Long formatUid;

  @Column(name = "formula", columnDefinition = "clob")
  private String formula;

  private String name;

  @Column(name = "sorting_uid")
  private Long sortingUid;

  @Transient
  private Long formulaTypeUid;

  @Transient
  private Long timePrecisionUid;

  //bi-directional many-to-one association to RtRankingEvent
  @OneToMany(mappedBy = "rtRanking")
  private List<RtRankingEvent> rtRankingEvents;

  public RtRanking() {
  }

  public RtRankingKey getId() {
    return this.id;
  }

  public void setId(RtRankingKey id) {
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

  public String getName() {
    return this.name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getSortingUid() {
    return this.sortingUid;
  }

  public void setSortingUid(Long sortingUid) {
    this.sortingUid = sortingUid;
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

  public List<RtRankingEvent> getRtRankingEvents() {
    return this.rtRankingEvents;
  }

  public void setRtRankingEvents(List<RtRankingEvent> rtRankingEvents) {
    this.rtRankingEvents = rtRankingEvents;
  }

  public RtRankingEvent addRtRankingEvent(RtRankingEvent rtRankingEvent) {
    getRtRankingEvents().add(rtRankingEvent);
    rtRankingEvent.setRtRanking(this);

    return rtRankingEvent;
  }

  public RtRankingEvent removeRtRankingEvent(RtRankingEvent rtRankingEvent) {
    getRtRankingEvents().remove(rtRankingEvent);
    rtRankingEvent.setRtRanking(null);

    return rtRankingEvent;
  }

}
