package com.rtiming.client.entry.startlist;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.basic.table.customizer.AbstractTableCustomizer;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;

public class StartlistTableCustomizer extends AbstractTableCustomizer {

  @Override
  public void injectCustomColumns(OrderedCollection<IColumn<?>> columnList) {

    for (int i = 0; i < 1000; i++) {
      columnList.addLast(new StartlistColumn(i));
    }

  }

  @Order(99999.9)
  public class StartlistColumn extends AbstractStartlistPreviewColumn {

    private final int id;

    public StartlistColumn(int id) {
      super();
      this.id = id;
    }

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }

    @Override
    public String getColumnId() {
      return super.getColumnId() + id;
    }

  }

  @Override
  public String getPreferencesKey() {
    return null; // TODO MIG
  }

}
