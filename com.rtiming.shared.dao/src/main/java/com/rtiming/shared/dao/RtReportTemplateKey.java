package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

@Embeddable
public class RtReportTemplateKey extends AbstractKey<RtReportTemplateKey> implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(name = "report_template_nr")
  private Long reportTemplateNr;

  @Column(name = "client_nr")
  private Long clientNr;

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
    return reportTemplateNr;
  }

  @Override
  public void setId(Long id) {
    this.reportTemplateNr = id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((clientNr == null) ? 0 : clientNr.hashCode());
    result = prime * result
        + ((reportTemplateNr == null) ? 0 : reportTemplateNr.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    RtReportTemplateKey other = (RtReportTemplateKey) obj;
    if (clientNr == null) {
      if (other.clientNr != null) {
        return false;
      }
    }
    else if (!clientNr.equals(other.clientNr)) {
      return false;
    }
    if (reportTemplateNr == null) {
      if (other.reportTemplateNr != null) {
        return false;
      }
    }
    else if (!reportTemplateNr.equals(other.reportTemplateNr)) {
      return false;
    }
    return true;
  }

  public static RtReportTemplateKey create(RtReportTemplateKey key) {
    if (key == null) {
      key = new RtReportTemplateKey();
    }
    return (RtReportTemplateKey) createKeyInternal(key);
  }

  public static RtReportTemplateKey create(Long id) {
    RtReportTemplateKey key = new RtReportTemplateKey();
    key.setId(id);
    return create(key);
  }

}
