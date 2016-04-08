package com.rtiming.client.settings.addinfo.columns;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.HeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;

@Order(value = Double.MAX_VALUE)
public class AdditionalSmartColumn extends AbstractSmartColumn<Long> implements IAdditionalColumn {

  private final Long additionalInformationUid;
  private final String id;
  private final String text;

  public AdditionalSmartColumn(Long additionalInformationUid, String id, String text) {
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
  protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
    return AdditionalInformationCodeType.class;
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
