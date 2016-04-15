package com.rtiming.client.common.report;

import java.io.File;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.print.attribute.HashPrintRequestAttributeSet;
import javax.print.attribute.HashPrintServiceAttributeSet;
import javax.print.attribute.PrintRequestAttributeSet;
import javax.print.attribute.PrintServiceAttributeSet;
import javax.print.attribute.standard.MediaSizeName;
import javax.print.attribute.standard.PrinterName;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.file.IRemoteFileService;
import org.eclipse.scout.rt.shared.services.common.file.RemoteFile;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.common.report.template.IReportParameters;
import com.rtiming.shared.dao.RtReportTemplateFile;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.services.code.AbstractReportTypeCode;
import com.rtiming.shared.services.code.ReportTypeCodeType;

import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JRParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRHtmlExporter;
import net.sf.jasperreports.engine.export.JRHtmlExporterParameter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporter;
import net.sf.jasperreports.engine.export.JRPrintServiceExporterParameter;
import net.sf.jasperreports.engine.export.JRRtfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.engine.export.JRXlsExporterParameter;

public abstract class FMilaReport {

  public static final String LOCAL_REPORT_DIR = "resources/reports/";

  protected final Map<String, Object> parameters;
  private String templateFilename;

  public FMilaReport() {
    super();
    parameters = new HashMap<String, Object>();
    putParameterValue(IReportParameters.GENERAL_PAGE, Texts.get("Page"));
    putParameterValue(IReportParameters.APPLICATION_NAME, Texts.get("ApplicationName") + " " + FMilaUtility.getVersion());
    putParameterValue(IReportParameters.GENERAL_TOTAL, Texts.get("Total"));
    putParameterValue(IReportParameters.GENERAL_COUNT, Texts.get("Count"));
  }

  protected String getTemplateFilename() {
    return templateFilename;
  }

  public Map<String, Object> getParameters() {
    return parameters;
  }

  protected abstract JasperPrint createJasperPrint() throws ProcessingException;

  public final void setTemplate(Long reportTypeUid, Long eventNr) throws ProcessingException {
    Set<RtReportTemplateFile> files = TemplateSelectionUtility.getTemplateFiles(reportTypeUid, eventNr, SharedCache.getTemplateConfiguration());

    AbstractReportTypeCode report = (AbstractReportTypeCode) BEANS.get(ReportTypeCodeType.class).getCode(reportTypeUid);
    if (files != null && files.size() > 0) {
      templateFilename = TemplateSelectionUtility.loadFilesIntoLocalDir(files, TemplateSelectionUtility.getCustomTemplateLoader(), report.getConfiguredCompilationRequired());
      setSubreportDirectory(IOUtility.getFilePath(templateFilename) + FMilaUtility.FILE_SEPARATOR);
    }
    else {
      // no custom template, use defaults
      templateFilename = TemplateSelectionUtility.loadFilesIntoLocalDir(report.getConfiguredDefaultTemplates(), TemplateSelectionUtility.getDefaultTemplateLoader(), report.getConfiguredCompilationRequired());
      setSubreportDirectory(IOUtility.getFilePath(templateFilename) + FMilaUtility.FILE_SEPARATOR);
    }

    // set event data if event nr available
    if (eventNr != null) {
      EventBean event = new EventBean();
      event.setEventNr(eventNr);
      event = BEANS.get(IEventProcessService.class).load(event);

      // Common
      if (!StringUtility.isNullOrEmpty(event.getFormat())) {
        putParameterValue(IReportParameters.JASPER_LOGO_PARAMETER, "logo/" + event.getClientNr() + "/" + eventNr + "." + event.getFormat());
      }
      putParameterValue(IReportParameters.EVENT_NAME, StringUtility.emptyIfNull(event.getName()));
      putParameterValue(IReportParameters.EVENT_MAP, StringUtility.emptyIfNull(event.getMap()));
      putParameterValue(IReportParameters.EVENT_LOCATION, StringUtility.emptyIfNull(event.getLocation()));
      putParameterValue(IReportParameters.EVENT_DATE, event.getEvtZero());
    }

  }

  protected void putParameterValue(String key, String value) {
    parameters.put(key, value);
  }

  protected void putParameterValue(String key, Date value) {
    parameters.put(key, value);
  }

  protected void putParameterValue(String key, Boolean value) {
    parameters.put(key, value);
  }

  public void setSubreportDirectory(String path) {
    parameters.put("SUBREPORT_DIR", path);
  }

  public String getSubreportDirectory() {
    return (String) parameters.get("SUBREPORT_DIR");
  }

  public final String createReport(ReportType reportType, boolean print, String printer, boolean openFile) throws ProcessingException {
    String reportFilePath = null;

    try {
      // add server URL to logo.path
      if (parameters.get(IReportParameters.JASPER_LOGO_PARAMETER) != null) {
        RemoteFile spec = new RemoteFile(parameters.get(IReportParameters.JASPER_LOGO_PARAMETER).toString(), 0);
        RemoteFile rf = BEANS.get(IRemoteFileService.class).getRemoteFile(spec);
        if (rf.exists()) {
          File file = IOUtility.createTempFile(rf.getName(), rf.extractData());
          parameters.put(IReportParameters.JASPER_LOGO_PARAMETER, file.getAbsolutePath());
        }
        else {
          throw new VetoException(Texts.get("FileReadException") + " (" + rf.getPath() + ")");
        }
      }
      else {
        // TODO MIG
//        URL file = FMilaUtility.findFileLocation("resources/images/report_logo.png", Activator.getDefault().getBundle().getSymbolicName());
//        parameters.put(IReportParameters.JASPER_LOGO_PARAMETER, file.getPath());
      }

      // Default Subreport Directory
      if (getSubreportDirectory() == null) {
        // TODO MIG
//        URL subreportDir = FMilaUtility.findFileLocation(LOCAL_REPORT_DIR, Activator.getDefault().getBundle().getSymbolicName());
//        setSubreportDirectory(subreportDir.getPath());
      }

      // type dependent parameters
      if (reportType == ReportType.HTML) {
        putParameterValue(JRParameter.IS_IGNORE_PAGINATION, Boolean.TRUE);
      }

      JasperPrint jasperPrint = createJasperPrint();

      // pdf
      if (reportType == ReportType.PDF) {
        reportFilePath = IOUtility.getTempFileName(".pdf");
        JasperExportManager.exportReportToPdfFile(jasperPrint, reportFilePath);
      }

      // rtf
      if (reportType == ReportType.RTF) {
        reportFilePath = IOUtility.getTempFileName(".rtf");
        File destFile = new File(reportFilePath);

        JRRtfExporter rtfExporter = new JRRtfExporter();

        rtfExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        rtfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());

        rtfExporter.exportReport();
      }

      if (reportType == ReportType.XLS) {
        reportFilePath = IOUtility.getTempFileName(".xls");
        File destFile = new File(reportFilePath);

        JRXlsExporter xlsExporter = new JRXlsExporter();

        xlsExporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        xlsExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, destFile.toString());
        xlsExporter.setParameter(JRXlsExporterParameter.IS_ONE_PAGE_PER_SHEET, Boolean.FALSE);

        xlsExporter.exportReport();
      }

      if (reportType == ReportType.HTML) {
        reportFilePath = IOUtility.getTempFileName(".html");

        JRHtmlExporter html = new JRHtmlExporter();
        html.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        html.setParameter(JRExporterParameter.IGNORE_PAGE_MARGINS, Boolean.TRUE);
        html.setParameter(JRHtmlExporterParameter.IS_USING_IMAGES_TO_ALIGN, Boolean.FALSE);
        html.setParameter(JRHtmlExporterParameter.IS_REMOVE_EMPTY_SPACE_BETWEEN_ROWS, Boolean.TRUE);
        html.setParameter(JRHtmlExporterParameter.BETWEEN_PAGES_HTML, "");
        html.setParameter(JRHtmlExporterParameter.ZOOM_RATIO, 2f);
        File htmlFile = new File(reportFilePath);
        html.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, htmlFile.toString());
        html.exportReport();
      }

      // print
      if (print) {
        boolean showPrintDialog = StringUtility.isNullOrEmpty(printer);

        PrintRequestAttributeSet printRequestAttributeSet = new HashPrintRequestAttributeSet();
        printRequestAttributeSet.add(MediaSizeName.ISO_A4);

        PrintServiceAttributeSet printServiceAttributeSet = new HashPrintServiceAttributeSet();
        if (!showPrintDialog) {
          printServiceAttributeSet.add(new PrinterName(printer, null));
        }

        JRPrintServiceExporter exporter = new JRPrintServiceExporter();

        exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_REQUEST_ATTRIBUTE_SET, printRequestAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.PRINT_SERVICE_ATTRIBUTE_SET, printServiceAttributeSet);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PAGE_DIALOG, Boolean.FALSE);
        exporter.setParameter(JRPrintServiceExporterParameter.DISPLAY_PRINT_DIALOG, showPrintDialog);

        exporter.exportReport();
      }

      // open pdf
      if (openFile) {
        FMilaClientUtility.openDocument(reportFilePath);
      }

    }
    catch (IllegalArgumentException e) {
      throw e;
    }
    catch (Exception e) {
      throw new ProcessingException("Could not create Report", e);
    }

    return reportFilePath;
  }

}
