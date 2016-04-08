package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * The persistent class for the rt_event_additional_information database table.
 */
@Entity
@Table(name = "rt_event_additional_information")
public class RtEventAdditionalInformation implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtEventAdditionalInformationKey id;

  public RtEventAdditionalInformation() {
  }

  public RtEventAdditionalInformationKey getId() {
    return this.id;
  }

  public void setId(RtEventAdditionalInformationKey id) {
    this.id = id;
  }

}
