package com.rtiming.client.common.ui.action;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;

public abstract class AbstractSeparatorMenu extends AbstractMenu {

  @Override
  protected boolean getConfiguredSeparator() {
    return true;
  }

}
