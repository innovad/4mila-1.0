package com.rtiming.client.common.report.dynamic;

import static net.sf.dynamicreports.report.builder.DynamicReports.stl;
import static net.sf.dynamicreports.report.builder.DynamicReports.template;

import java.awt.Color;
import java.util.Locale;

import net.sf.dynamicreports.report.builder.ReportTemplateBuilder;
import net.sf.dynamicreports.report.builder.style.StyleBuilder;
import net.sf.dynamicreports.report.constant.HorizontalAlignment;
import net.sf.dynamicreports.report.constant.VerticalAlignment;

public class DynamicTemplate {
  public static final StyleBuilder rootStyle;
  public static final StyleBuilder columnStyle;
  public static final StyleBuilder columnTitleStyle;

  public static final ReportTemplateBuilder reportTemplate;

  static {
    rootStyle = stl.style().setPadding(1);
    columnStyle = stl.style(rootStyle).setVerticalAlignment(VerticalAlignment.MIDDLE);
    columnTitleStyle = stl.style(columnStyle).setBorder(stl.pen1Point()).setHorizontalAlignment(HorizontalAlignment.CENTER).setBackgroundColor(Color.LIGHT_GRAY).bold();

    reportTemplate = template().setLocale(Locale.ENGLISH).setColumnStyle(columnStyle).setColumnTitleStyle(columnTitleStyle).highlightDetailEvenRows();
  }

}
