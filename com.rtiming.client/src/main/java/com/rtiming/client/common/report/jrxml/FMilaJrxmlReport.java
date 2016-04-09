package com.rtiming.client.common.report.jrxml;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.common.report.FMilaReport;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 * 
 */
public class FMilaJrxmlReport extends FMilaReport {

  private ReportDataSource list;

  public void setData(ReportDataSource list) {
    this.list = list;
  }

  @Override
  protected JasperPrint createJasperPrint() throws ProcessingException {
    JasperPrint result = null;
    JRMapCollectionDataSource ds = new JRMapCollectionDataSource(list);

    try {
      // fill
      result = JasperFillManager.fillReport(getTemplateFilename(), parameters, ds);
    }
    catch (JRException e) {
      throw new ProcessingException("Failed creating template-based report", e);
    }
    return result;
  }
}
