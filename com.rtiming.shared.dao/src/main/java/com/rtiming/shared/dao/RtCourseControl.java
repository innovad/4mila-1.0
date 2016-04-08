package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.rtiming.shared.dao.util.UploadConfiguration;

/**
 * The persistent class for the rt_course_control database table.
 */
@Entity
@Table(name = "rt_course_control")
@UploadConfiguration(uploadOrder = 230)
public class RtCourseControl implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtCourseControlKey id;

  @Column(name = "count_leg")
  private Boolean countLeg;

  private Boolean mandatory;

  @Column(name = "sortcode")
  private Long sortcode;

  @Column(name = "course_nr")
  private Long courseNr;

  @Column(name = "control_nr")
  private Long controlNr;

  @Column(name = "fork_type_uid")
  private Long forkTypeUid;

  @Column(name = "fork_master_course_control_nr")
  private Long forkMasterCourseControlNr;

  @Column(name = "fork_variant_code")
  private String forkVariantCode;

  //bi-directional many-to-one association to RtControl
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "control_nr", referencedColumnName = "control_nr", insertable = false, updatable = false)
  })
  private RtControl rtControl;

  //bi-directional many-to-one association to RtControl
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "fork_master_course_control_nr", referencedColumnName = "course_control_nr", insertable = false, updatable = false)
  })
  private RtCourseControl rtMasterCourseControl;

  //bi-directional many-to-one association to RtCourse
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "course_nr", referencedColumnName = "course_nr", insertable = false, updatable = false)
  })
  private RtCourse rtCourse;

  public RtCourseControl() {
  }

  public RtCourseControlKey getId() {
    return this.id;
  }

  public void setId(RtCourseControlKey id) {
    this.id = id;
  }

  public Boolean getCountLeg() {
    return this.countLeg;
  }

  public void setCountLeg(Boolean countLeg) {
    this.countLeg = countLeg;
  }

  public Boolean getMandatory() {
    return this.mandatory;
  }

  public void setMandatory(Boolean mandatory) {
    this.mandatory = mandatory;
  }

  public RtControl getRtControl() {
    return this.rtControl;
  }

  public void setRtControl(RtControl rtControl) {
    this.rtControl = rtControl;
  }

  public RtCourse getRtCourse() {
    return this.rtCourse;
  }

  public void setRtCourse(RtCourse rtCourse) {
    this.rtCourse = rtCourse;
  }

  public Long getSortcode() {
    return sortcode;
  }

  public void setSortcode(Long sortcode) {
    this.sortcode = sortcode;
  }

  public void setCourseNr(Long courseNr) {
    this.courseNr = courseNr;
  }

  public void setControlNr(Long controlNr) {
    this.controlNr = controlNr;
  }

}
