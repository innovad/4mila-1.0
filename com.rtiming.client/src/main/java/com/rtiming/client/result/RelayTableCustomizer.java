package com.rtiming.client.result;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.basic.table.customizer.AbstractTableCustomizer;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.shared.Texts;

public class RelayTableCustomizer extends AbstractTableCustomizer {

  private final RelayAfterLegResultsTablePage page;

  public RelayTableCustomizer(RelayAfterLegResultsTablePage page) {
    this.page = page;
  }

  @Override
  public void injectCustomColumns(OrderedCollection<IColumn<?>> columnList) {
    columnList.addFirst(new RelayRankColumn()); // add at begin
    columnList.addLast(new RelayTimeBehindColumn()); // add at end
    columnList.addLast(new RelayPercentColumn()); // add at end
    columnList.addLast(new RelayTimeColumn()); // add at end
  }

  @Order(1.0)
  public class RelayRankColumn extends AbstractLongColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Rank") + " " + TEXTS.get("Relay");
    }

    @Override
    protected int getConfiguredWidth() {
      return 80;
    }
  }

  @Order(999999997.0)
  public class RelayTimeBehindColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("TimeBehind") + " " + TEXTS.get("Relay");
    }

    @Override
    protected int getConfiguredHorizontalAlignment() {
      return 1;
    }

    @Override
    protected int getConfiguredWidth() {
      return 120;
    }
  }

  @Order(999999998.0)
  public class RelayPercentColumn extends AbstractBigDecimalColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Percent") + " " + TEXTS.get("Relay");
    }

    @Override
    protected int getConfiguredMaxFractionDigits() {
      return 1;
    }

    @Override
    protected int getConfiguredMinFractionDigits() {
      return 1;
    }

    @Override
    protected int getConfiguredWidth() {
      return 80;
    }
  }

  @Order(999999999.0)
  public class RelayTimeColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Time") + " " + TEXTS.get("Relay");
    }

    @Override
    protected int getConfiguredHorizontalAlignment() {
      return 1;
    }

    @Override
    protected int getConfiguredWidth() {
      return 120;
    }
  }

  @Override
  public String getPreferencesKey() {
    return null; // TODO MIG
  }

}
