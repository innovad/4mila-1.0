package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

/**
 * The primary key class for the rt_course database table.
 */
@Embeddable
public class RtCourseKey extends AbstractKey<RtCourseKey> implements Serializable {
  //default serial version id, required for serializable classes.
  private static final long serialVersionUID = 1L;

  @Column(name = "course_nr")
  private Long courseNr;

  @Column(name = "client_nr")
  private Long clientNr;

  public RtCourseKey() {
  }

  @Override
  public Long getId() {
    return this.courseNr;
  }

  @Override
  public void setId(Long courseNr) {
    this.courseNr = courseNr;
  }

  @Override
  public Long getClientNr() {
    return this.clientNr;
  }

  @Override
  public void setClientNr(Long clientNr) {
    this.clientNr = clientNr;
  }

  @Override
  public boolean equals(Object other) {
    if (this == other) {
      return true;
    }
    if (!(other instanceof RtCourseKey)) {
      return false;
    }
    RtCourseKey castOther = (RtCourseKey) other;
    return this.courseNr.equals(castOther.courseNr)
        && this.clientNr.equals(castOther.clientNr);
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int hash = 17;
    hash = hash * prime + this.courseNr.hashCode();
    hash = hash * prime + this.clientNr.hashCode();

    return hash;
  }

  public static RtCourseKey create(RtCourseKey key) {
    if (key == null) {
      key = new RtCourseKey();
    }
    return (RtCourseKey) createKeyInternal(key);
  }

  public static RtCourseKey create(Long id) {
    RtCourseKey key = new RtCourseKey();
    key.setId(id);
    return create(key);
  }

}
