package com.rtiming.client.settings.addinfo.columns;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.HeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;

@Order(value = Double.MAX_VALUE)
public class AdditionalStringColumn extends AbstractStringColumn implements IAdditionalColumn {

  private final Long additionalInformationUid;
  private final String id;
  private final String text;

  public AdditionalStringColumn(Long additionalInformationUid, String id, String text) {
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
