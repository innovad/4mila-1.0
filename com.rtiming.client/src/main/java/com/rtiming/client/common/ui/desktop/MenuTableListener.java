package com.rtiming.client.common.ui.desktop;

import org.eclipse.scout.rt.client.ui.basic.table.TableAdapter;
import org.eclipse.scout.rt.client.ui.basic.table.TableEvent;


public class MenuTableListener extends TableAdapter {

  private final LinkForm linkForm;

  public MenuTableListener(LinkForm linkForm) {
    this.linkForm = linkForm;
  }

  @Override
  public void tableChanged(TableEvent e) {
    linkForm.buildLinks();
  }

}
