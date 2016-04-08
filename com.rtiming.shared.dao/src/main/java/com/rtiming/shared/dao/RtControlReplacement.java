package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * The persistent class for the rt_control_replacement database table.
 */
@Entity
@Table(name = "rt_control_replacement")
public class RtControlReplacement implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtControlReplacementKey id;

  //bi-directional many-to-one association to RtControl
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "control_nr", referencedColumnName = "control_nr", insertable = false, updatable = false)
  })
  private RtControl rtControl1;

  //bi-directional many-to-one association to RtControl
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "replacement_control_nr", referencedColumnName = "control_nr", insertable = false, updatable = false)
  })
  private RtControl rtControl2;

  public RtControlReplacement() {
  }

  public RtControlReplacementKey getId() {
    return this.id;
  }

  public void setId(RtControlReplacementKey id) {
    this.id = id;
  }

  public RtControl getRtControl1() {
    return this.rtControl1;
  }

  public void setRtControl1(RtControl rtControl1) {
    this.rtControl1 = rtControl1;
  }

  public RtControl getRtControl2() {
    return this.rtControl2;
  }

  public void setRtControl2(RtControl rtControl2) {
    this.rtControl2 = rtControl2;
  }

}
