package com.rtiming.client.common.excel;

import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.util.WorkbookUtil;
import org.eclipse.scout.commons.IOUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.OpenUriAction;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.FMilaUtility;

public final class ExcelUtility {

  private ExcelUtility() {
  }

  public static void export(ITable table) throws ProcessingException {
    Object[][] data;
    if (table.getSelectedRowCount() >= 2) {
      data = table.exportTableRowsAsCSV(table.getSelectedRows(), table.getColumnSet().getVisibleColumns(), true, false, false);
    }
    else {
      data = table.exportTableRowsAsCSV(table.getFilteredRows(), table.getColumnSet().getVisibleColumns(), true, false, false);
    }

    HSSFWorkbook wb = new HSSFWorkbook();
    String filename = IOUtility.getTempFileName(".xls");

    String safeName = WorkbookUtil.createSafeSheetName("Export");
    HSSFSheet sheet = wb.createSheet(safeName);

    short rows = 0;
    int cols = 0;

    for (Object[] line : data) {
      HSSFRow row = sheet.createRow(rows);

      cols = 0;
      for (Object value : line) {
        HSSFCell cell = row.createCell(cols);
        cell.getCellStyle().setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);

        // title
        if (rows == 0) {
          setBold(wb, cell);
        }

        setCellValue(wb, cell, value, table.getColumnSet().getVisibleColumn(cols));

        cols++;
      }
      rows++;
    }

    // fit columns
    for (int i = 0; i < cols; i++) {
      sheet.autoSizeColumn(i);
    }

    try {
      FileOutputStream fileOut = new FileOutputStream(filename);
      wb.write(fileOut);
      fileOut.close();
    }
    catch (Exception e) {
      throw new ProcessingException("POI", e);
    }

    ClientSessionProvider.currentSession().getDesktop().openUri(filename, OpenUriAction.DOWNLOAD);
    ClientSession.get().getDesktop().setStatusText(TEXTS.get("ExcelFileCreated"));
  }

  private static void setBold(HSSFWorkbook workbook, HSSFCell cell) {
    HSSFCellStyle boldStyle = workbook.createCellStyle();
    HSSFFont boldFont = workbook.createFont();
    boldFont.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
    boldStyle.setFont(boldFont);
    cell.setCellStyle(boldStyle);
  }

  private static void setCellValue(HSSFWorkbook workbook, HSSFCell cell, Object value, IColumn<?> column) throws ProcessingException {
    // null
    if (value == null) {
      cell.setCellType(Cell.CELL_TYPE_BLANK);
    }
    // number
    else if (value instanceof Number) {
      cell.setCellType(Cell.CELL_TYPE_NUMERIC);
      String format;
      if (value instanceof Float || value instanceof Double || value instanceof BigDecimal) {
        format = "#,##0.00";
      }
      else {
        format = "#,##0";
      }
      HSSFCellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
      cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(format));
      cell.setCellStyle(cellStyle);
      cell.setCellValue(((Number) value).doubleValue());
    }
    // date
    else if (value instanceof Date) {
      String string = "DD.MM.YYYY";
      if (column instanceof AbstractDateColumn) {
        if (((AbstractDateColumn) column).isHasTime()) {
          string = "DD.MM.YYYY hh:mm:ss";
        }
      }
      HSSFCellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
      cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat(string));
      cell.setCellStyle(cellStyle);

      Calendar cal = Calendar.getInstance();
      cal.setTime((Date) value);
      cell.setCellValue(cal);
    }
    // boolean
    else if (value instanceof Boolean) {
      cell.setCellType(Cell.CELL_TYPE_BOOLEAN);
      cell.setCellValue((Boolean) value);
    }
    // percent
    else if (value instanceof String && ((String) value).endsWith("%")) {
      HSSFCellStyle cellStyle = workbook.createCellStyle();
      cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
      cellStyle.setDataFormat(workbook.getCreationHelper().createDataFormat().getFormat("0.00%"));
      cell.setCellStyle(cellStyle);

      String s = (String) value;
      try {
        cell.setCellValue(Double.valueOf(s));
      }
      catch (Exception e) {
        if (e instanceof InterruptedException) {
          throw new ProcessingException("interrupted", e);
        }
        cell.setCellValue("'" + s);
      }
    }
    // text
    else {
      String s = "" + value;
      if (s.contains(FMilaUtility.LINE_SEPARATOR) || s.contains("\n")) {
        HSSFCellStyle cellStyle = workbook.createCellStyle();
        cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_TOP);
        cellStyle.setWrapText(true);
        cell.setCellStyle(cellStyle);
      }
      cell.setCellType(Cell.CELL_TYPE_STRING);
      cell.setCellValue(s);
    }
  }
}
