package com.rtiming.client.dataexchange;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.basic.table.customizer.AbstractTableCustomizer;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;

import com.rtiming.shared.Texts;

public class DataExchangePreviewTableCustomizer extends AbstractTableCustomizer {

  private final int columnCount;

  public DataExchangePreviewTableCustomizer(int columnCount) {
    super();
    this.columnCount = columnCount;
  }

  @Override
  public void addColumn(IColumn<?> insertAfterColumn) throws ProcessingException {

  }

  @Override
  public void injectCustomColumns(OrderedCollection<IColumn<?>> columnList) {
    for (int i = 0; i < columnCount; i++) {
      columnList.addLast(new PreviewColumn(i));
    }
  }

  public class PreviewColumn extends AbstractStringColumn {

    private final int id;

    public PreviewColumn(int id) {
      super();
      this.id = id;
    }

    @Override
    public String getColumnId() {
      return super.getColumnId() + id;
    }

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Preview");
    }

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }

    @Override
    protected int getConfiguredWidth() {
      return 65;
    }

  }

  @Override
  public String getPreferencesKey() {
    return null; // TOOD MIG
  }

}
