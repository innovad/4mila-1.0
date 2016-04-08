package com.rtiming.client.common.report.template;

import java.io.File;

import com.rtiming.shared.services.code.AbstractReportTypeCode.ReportTemplateType;

/**
 * 
 */
public interface ITemplateField {

  boolean isVisible();

  void setValue(String absolutePath);

  void setDirectory(File localTemporaryReportDir);

  void setServerLastModified(long lastModified);

  long getServerLastModified();

  void setLocalLastModified(long lastModified);

  void setVisible(boolean typeMatch);

  void setMandatory(boolean typeMatch);

  ReportTemplateType getTemplateType();

  long getLocalLastModified();

  File getDirectory();

  String getFileName();

  boolean isMaster();

  String getValue();

}
