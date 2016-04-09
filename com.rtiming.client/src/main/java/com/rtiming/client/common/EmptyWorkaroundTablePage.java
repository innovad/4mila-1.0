package com.rtiming.client.common;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.Order;

public class EmptyWorkaroundTablePage extends AbstractPageWithTable<EmptyWorkaroundTablePage.Table> {

  @Order(10.0)
  public class Table extends AbstractTable {
  }
}
