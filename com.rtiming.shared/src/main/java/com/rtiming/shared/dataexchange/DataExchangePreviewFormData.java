package com.rtiming.shared.dataexchange;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.tablefield.AbstractTableFieldData;

public class DataExchangePreviewFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public DataExchangePreviewFormData() {
  }

  public PreviewData getPreviewData() {
    return getFieldByClass(PreviewData.class);
  }

  public PreviewInfo getPreviewInfo() {
    return getFieldByClass(PreviewInfo.class);
  }

  public static class PreviewData extends AbstractTableFieldData {
    private static final long serialVersionUID = 1L;

    public PreviewData() {
    }
  }

  public static class PreviewInfo extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public PreviewInfo() {
    }
  }
}
