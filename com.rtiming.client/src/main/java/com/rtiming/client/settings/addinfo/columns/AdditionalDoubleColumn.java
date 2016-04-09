package com.rtiming.client.settings.addinfo.columns;

import org.eclipse.scout.rt.client.ui.basic.table.HeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

@Order(value = Double.MAX_VALUE)
public class AdditionalDoubleColumn extends AbstractBigDecimalColumn implements IAdditionalColumn {

  private final Long additionalInformationUid;
  private final String id;
  private final String text;

  public AdditionalDoubleColumn(Long additionalInformationUid, String id, String text) {
    super();
    this.additionalInformationUid = additionalInformationUid;
    this.id = id;
    this.text = text;
  }

  @Override
  protected void execInitColumn() throws ProcessingException {
    super.execInitColumn();
    ((HeaderCell) getHeaderCell()).setText(text);
  }

  @Override
  public String getColumnId() {
    return id;
  }

  @Override
  protected int getConfiguredWidth() {
    return IAdditionalColumn.defaultWidth;
  }

  @Override
  public Long getAdditionalInformationUid() {
    return additionalInformationUid;
  }

}
