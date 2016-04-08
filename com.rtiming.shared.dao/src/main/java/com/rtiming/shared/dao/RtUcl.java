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

import org.hibernate.annotations.Index;

import com.rtiming.shared.dao.util.UploadConfiguration;

/**
 * The persistent class for the rt_ucl database table.
 */
@Entity
@Table(name = "rt_ucl")
@UploadConfiguration(uploadOrder = 20, cleanup = false)
public class RtUcl implements Serializable {
  private static final long serialVersionUID = 1L;

  @EmbeddedId
  private RtUclKey id;

  @Column(name = "code_name")
  @Index(name = "rt_ucl_ix_code_name")
  private String codeName;

  //bi-directional many-to-one association to RtUc
  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumns({
      @JoinColumn(name = "client_nr", referencedColumnName = "client_nr", insertable = false, updatable = false),
      @JoinColumn(name = "uc_uid", referencedColumnName = "uc_uid", insertable = false, updatable = false)
  })
  private RtUc rtUc;

  public RtUcl() {
  }

  public RtUclKey getId() {
    return this.id;
  }

  public void setId(RtUclKey id) {
    this.id = id;
  }

  public String getCodeName() {
    return this.codeName;
  }

  public void setCodeName(String codeName) {
    this.codeName = codeName;
  }

  public RtUc getRtUc() {
    return this.rtUc;
  }

  public void setRtUc(RtUc rtUc) {
    this.rtUc = rtUc;
  }

}
