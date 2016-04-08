package com.rtiming.shared.dao;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import com.rtiming.shared.dao.util.AbstractKey;

@Embeddable
public class RtReportTemplateFileKey extends AbstractKey<RtReportTemplateFileKey> implements Serializable {
  private static final long serialVersionUID = 1L;

  @Column(name = "report_template_file_nr")
  private Long reportTemplateFileNr;

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
    return reportTemplateFileNr;
  }

  @Override
  public void setId(Long id) {
    this.reportTemplateFileNr = id;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result
        + ((clientNr == null) ? 0 : clientNr.hashCode());
    result = prime * result
        + ((reportTemplateFileNr == null) ? 0 : reportTemplateFileNr.hashCode());
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
    RtReportTemplateFileKey other = (RtReportTemplateFileKey) obj;
    if (clientNr == null) {
      if (other.clientNr != null) {
        return false;
      }
    }
    else if (!clientNr.equals(other.clientNr)) {
      return false;
    }
    if (reportTemplateFileNr == null) {
      if (other.reportTemplateFileNr != null) {
        return false;
      }
    }
    else if (!reportTemplateFileNr.equals(other.reportTemplateFileNr)) {
      return false;
    }
    return true;
  }

  public static RtReportTemplateFileKey create(RtReportTemplateFileKey key) {
    if (key == null) {
      key = new RtReportTemplateFileKey();
    }
    return (RtReportTemplateFileKey) createKeyInternal(key);
  }

}
