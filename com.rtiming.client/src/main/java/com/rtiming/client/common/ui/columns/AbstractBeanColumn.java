package com.rtiming.client.common.ui.columns;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractColumn;

public abstract class AbstractBeanColumn<T extends Object> extends AbstractColumn<T> {

  public AbstractBeanColumn() {
    super();
  }

  @SuppressWarnings("unchecked")
  @Override
  public int compareTableRows(ITableRow r1, ITableRow r2) {
    int c;
    Object o1 = getValue(r1);
    Object o2 = getValue(r2);

    if (o1 == null && o2 == null) {
      c = 0;
    }
    else if (o1 == null) {
      c = -1;
    }
    else if (o2 == null) {
      c = 1;
    }
    else if ((o1 instanceof Comparable) && (o2 instanceof Comparable) && o1.getClass().isAssignableFrom(o2.getClass())) {
      c = ((Comparable) o1).compareTo(o2);
    }
    else {
      c = StringUtility.compareIgnoreCase(o1.toString(), o2.toString());
    }
    return c;
  }

}
