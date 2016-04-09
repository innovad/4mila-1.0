package com.rtiming.client.common.report.dynamic;

import static net.sf.dynamicreports.report.builder.DynamicReports.cmp;
import static net.sf.dynamicreports.report.builder.DynamicReports.col;
import static net.sf.dynamicreports.report.builder.DynamicReports.type;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.IOUtility;

import com.rtiming.client.common.report.DataSourceUtility;
import com.rtiming.shared.FMilaUtility;

import net.sf.dynamicreports.jasper.builder.JasperReportBuilder;
import net.sf.dynamicreports.report.base.expression.AbstractSimpleExpression;
import net.sf.dynamicreports.report.builder.DynamicReports;
import net.sf.dynamicreports.report.builder.column.ColumnBuilder;
import net.sf.dynamicreports.report.builder.column.TextColumnBuilder;
import net.sf.dynamicreports.report.builder.component.SubreportBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.builder.style.TemplateStylesBuilder;
import net.sf.dynamicreports.report.constant.SplitType;
import net.sf.dynamicreports.report.definition.ReportParameters;
import net.sf.dynamicreports.report.exception.DRException;
import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JREmptyDataSource;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRMapCollectionDataSource;

/**
 * 
 */
public class GenericTableDynamicReport {

  private final List<GenericTableReportContent> content;
  private final Map<String, Object> parameters;
  private final String headerFooterTemplatePath;
  private StyleBuilder tableLeft;
  private StyleBuilder tableRight;

  public GenericTableDynamicReport(List<GenericTableReportContent> content, Map<String, Object> parameters, String headerFooterTemplatePath) {
    this.content = content;
    this.parameters = parameters;
    this.headerFooterTemplatePath = headerFooterTemplatePath;
  }

  protected JasperPrint build() throws ProcessingException {
    if (content == null) {
      throw new IllegalArgumentException("content must be set");
    }
    if (headerFooterTemplatePath == null) {
      throw new IllegalArgumentException("Default Header/Footer Template is missing");
    }
    try {
      String reportDir = IOUtility.getFilePath(headerFooterTemplatePath);
      TemplateStylesBuilder styles = DynamicReports.stl.loadStyles(reportDir + FMilaUtility.FILE_SEPARATOR + "styles.jrtx");
      tableLeft = styles.getStyle("table.left");
      tableRight = styles.getStyle("table.right");
      int size = content.size();

      SubreportBuilder tableSubreport = cmp.subreport(new TableSubreportExpression()).setDataSource(new TableSubreportDataSourceExpression(content));

      JasperReportBuilder report = DynamicReports.report().setTemplate(DynamicTemplate.reportTemplate).setParameters(parameters).detail(tableSubreport, cmp.verticalGap(20)).setDataSource(new JREmptyDataSource(size));

      // Header Footer
      InputStream headerFooterTemplate = new FileInputStream(headerFooterTemplatePath);
      report.setTemplateDesign(headerFooterTemplate);

      return report.toJasperPrint();
    }
    catch (DRException | FileNotFoundException e) {
      throw new ProcessingException("Failed creating Report", e);
    }
  }

  private class TableSubreportExpression extends AbstractSimpleExpression<JasperReportBuilder> {
    private static final long serialVersionUID = 1L;

    @Override
    public JasperReportBuilder evaluate(ReportParameters reportParameters) {
      int masterRowNumber = reportParameters.getReportRowNumber();
      JasperReportBuilder report = DynamicReports.report();

      List<ColumnBuilder<?, ?>> list = new ArrayList<>();
      for (IColumn tableColumn : content.get(0).getTable().getColumnSet().getVisibleColumns()) {
        if (tableColumn.isDisplayable() && tableColumn.isVisible() && tableColumn.getWidth() > 0) {
          String headerText = tableColumn.getHeaderCell().getText();
          TextColumnBuilder<String> reportColumn = col.column(headerText, tableColumn.getColumnId(), type.stringType());
          list.add(reportColumn);
          if (tableColumn.getHorizontalAlignment() == 1) {
            reportColumn.setTitleStyle(tableRight);
            reportColumn.setStyle(tableRight);
            list.add(col.emptyColumn(false, true).setFixedWidth(3));
          }
          else {
            reportColumn.setTitleStyle(tableLeft);
            reportColumn.setStyle(tableLeft);
          }
          reportColumn.setWidth(Math.max(tableColumn.getWidth(), 60));
        }
      }

      ColumnBuilder<?, ?>[] columns = list.toArray(new ColumnBuilder<?, ?>[list.size()]);
      report.setTemplate(DynamicTemplate.reportTemplate).setParameters(content.get(masterRowNumber - 1).getParameters()).setTitleSplitType(SplitType.PREVENT).columns(columns);

      return report;
    }
  }

  private class TableSubreportDataSourceExpression extends AbstractSimpleExpression<JRDataSource> {
    private static final long serialVersionUID = 1L;
    private final List<GenericTableReportContent> contents;

    public TableSubreportDataSourceExpression(List<GenericTableReportContent> contents) {
      this.contents = contents;
    }

    @Override
    public JRDataSource evaluate(ReportParameters reportParameters) {
      int masterRowNumber = reportParameters.getReportRowNumber();
      GenericTableReportContent reportContent = contents.get(masterRowNumber - 1);
      ITable table = reportContent.getTable();
      JRMapCollectionDataSource ds = new JRMapCollectionDataSource(DataSourceUtility.createCollectionFromTable(table, true, false));
      return ds;
    }
  }

}
