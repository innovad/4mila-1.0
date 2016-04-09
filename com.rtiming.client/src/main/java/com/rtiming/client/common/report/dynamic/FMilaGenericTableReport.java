package com.rtiming.client.common.report.dynamic;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.common.report.FMilaReport;

import net.sf.jasperreports.engine.JasperPrint;

public class FMilaGenericTableReport extends FMilaReport {

  private List<GenericTableReportContent> content;
  private String headerFooterTemplateName;
  private String subtitleTemplateName;

  public FMilaGenericTableReport() {
    super();
  }

  public void setContent(List<GenericTableReportContent> content) {
    this.content = content;
  }

  @Override
  protected JasperPrint createJasperPrint() throws ProcessingException {
    if (content == null) {
      throw new IllegalArgumentException("No table was set for generic table report.");
    }

    GenericTableDynamicReport report = new GenericTableDynamicReport(content, parameters, getTemplateFilename());
    JasperPrint jasperPrint = report.build();

    return jasperPrint;
  }

}
