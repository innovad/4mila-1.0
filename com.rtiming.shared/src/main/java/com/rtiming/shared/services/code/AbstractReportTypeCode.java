package com.rtiming.shared.services.code;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;

import com.rtiming.shared.dao.RtReportTemplateFile;

/**
 * 
 */
public abstract class AbstractReportTypeCode extends AbstractCode<Long> {

  private static final long serialVersionUID = 1L;

  public enum ReportTemplateType {
    GENERIC_TABLE, CUSTOM_TEMPLATE
  }

  public abstract ReportTemplateType getConfiguredReportTemplateType();

  public abstract List<RtReportTemplateFile> getConfiguredDefaultTemplates();

  public abstract boolean getConfiguredCompilationRequired();

  public static List<RtReportTemplateFile> buildDefaultTemplateList(Long ID, String... filenames) {
    boolean masterDone = false;
    List<RtReportTemplateFile> result = new ArrayList<RtReportTemplateFile>();
    for (String filename : filenames) {
      RtReportTemplateFile file = new RtReportTemplateFile();
      file.setTemplateFileName(filename);
      file.setMaster(!masterDone);
      file.setReportTemplateNr(-1 * ID); // workaround, use negative ID as identifier to get unique local folder name
      result.add(file);
      masterDone = true;
    }
    return result;
  }

}
