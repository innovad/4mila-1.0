package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_course_control database table.
 */
@Embeddable
public class RtCourseControlKey extends AbstractKey<RtCourseControlKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "course_control_nr")
  private Long courseControlNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtCourseControlKey() {
  }

  @Override
  public Long getClientNr() {
    return clientNr;
  }

  @Override
  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  @Override
  public Long getId() {
    return courseControlNr;
  }

  @Override
  public void setId(Long courseControlNr) {
    this.courseControlNr = courseControlNr;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof RtCourseControlKey)) {
      return false;
    }
    RtCourseControlKey castOther = (RtCourseControlKey) other;
    return this.courseControlNr.equals(castOther.courseControlNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.courseControlNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtCourseControlKey create(RtCourseControlKey key) {
    if (key == null) {
      key = new RtCourseControlKey();
    }
    return (RtCourseControlKey) createKeyInternal(key);
  }

  public static RtCourseControlKey create(Long id) {
    RtCourseControlKey key = new RtCourseControlKey();
    key.setId(id);
    return create(key);
  }

}
