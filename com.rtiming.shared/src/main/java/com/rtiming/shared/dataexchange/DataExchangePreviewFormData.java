package com.rtiming.shared.dataexchange;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.tablefield.AbstractTableFieldBeanData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.dataexchange.DataExchangePreviewForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class DataExchangePreviewFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public PreviewData getPreviewData() {
    return getFieldByClass(PreviewData.class);
  }

  public PreviewInfo getPreviewInfo() {
    return getFieldByClass(PreviewInfo.class);
  }

  public static class PreviewData extends AbstractTableFieldBeanData {

    private static final long serialVersionUID = 1L;

    @Override
    public PreviewRowData addRow() {
      return (PreviewRowData) super.addRow();
    }

    @Override
    public PreviewRowData addRow(int rowState) {
      return (PreviewRowData) super.addRow(rowState);
    }

    @Override
    public PreviewRowData createRow() {
      return new PreviewRowData();
    }

    @Override
    public Class<? extends AbstractTableRowData> getRowType() {
      return PreviewRowData.class;
    }

    @Override
    public PreviewRowData[] getRows() {
      return (PreviewRowData[]) super.getRows();
    }

    @Override
    public PreviewRowData rowAt(int index) {
      return (PreviewRowData) super.rowAt(index);
    }

    public void setRows(PreviewRowData[] rows) {
      super.setRows(rows);
    }

    public static class PreviewRowData extends AbstractTableRowData {

      private static final long serialVersionUID = 1L;
    }
  }

  public static class PreviewInfo extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }
}
