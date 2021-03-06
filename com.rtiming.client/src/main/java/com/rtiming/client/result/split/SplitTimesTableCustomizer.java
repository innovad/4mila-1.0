package com.rtiming.client.result.split;

import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.basic.table.customizer.AbstractTableCustomizer;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;

import com.rtiming.client.result.AbstractControlColumn;

public class SplitTimesTableCustomizer extends AbstractTableCustomizer {

  @Override
  public void injectCustomColumns(OrderedCollection<IColumn<?>> columnList) {

    for (int i = 0; i < 100; i++) {
      columnList.addLast(new ControlColumn(i));
    }

  }

  @Order(8.0)
  public class ControlColumn extends AbstractControlColumn {

    private final int id;

    public ControlColumn(int id) {
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
