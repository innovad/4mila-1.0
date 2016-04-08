package com.rtiming.client.common.ui.desktop;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.placeholder.AbstractPlaceholderField;
import org.eclipse.scout.rt.platform.util.collection.OrderedCollection;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.desktop.LinkForm.MainBox.LinkBox;

public class LinkForm extends AbstractForm {

  private List<Link> linkList;

  public LinkForm() throws ProcessingException {
    super();
  }

  public class FormHandler extends AbstractFormHandler {
  }

  public void startForm() throws ProcessingException {
    startInternal(new FormHandler());
  }

  public void buildLinks() {
    IPage page = ((Desktop) ClientSession.get().getDesktop()).getOutline().getActivePage();
    for (Link menu : linkList) {
      menu.setVisible(false);
    }
    boolean isPageWithTable = page instanceof AbstractPageWithTable<?>;
    if (isPageWithTable) {
      AbstractPageWithTable<?> pageWithTable = (AbstractPageWithTable<?>) page;
      List<IMenu> menues = pageWithTable.getTable().getMenus();
      int i = 0;
      for (IMenu menu : menues) {
        // TODO MIG
//        menu.prepareAction();
//        if (menu.isVisible() && !menu.isSeparator()) {
//          Link currentMenu = linkList.get(i);
//          currentMenu.setMenu(menu);
//          if (!menu.isEmptySpaceAction()) {
//            if (pageWithTable.getTable().getSelectedRowCount() == 0) {
//              if (menu.isSingleSelectionAction() || menu.isMultiSelectionAction()) {
//                currentMenu.setEnabled(false);
//              }
//            }
//            else if (pageWithTable.getTable().getSelectedRowCount() > 1) {
//              if (!menu.isMultiSelectionAction()) {
//                currentMenu.setEnabled(false);
//              }
//            }
//          }
//          i++;
//        }
      }
    }

    // Help Menu
    boolean isHelpVisible = isPageWithTable && (page instanceof IHelpEnabledPage);
    getFieldByClass(PlaceholderField.class).setVisible(isHelpVisible);
    getFieldByClass(HelpLinkButton.class).setVisible(isHelpVisible);
  }

  @Order(1000.0)
  public class Link extends AbstractLinkButton {

    private IMenu menu;

    public Link() {
    }

    @Override
    protected boolean getConfiguredProcessButton() {
      return false;
    }

    public void setMenu(IMenu menu) {
      this.menu = menu;
      setVisible(menu.isVisible());
      setEnabled(menu.isEnabled());
      setLabel(menu.getText());
    }

    @Override
    protected void execClickAction() throws ProcessingException {
      menu.doAction();
    }

  }

  @Order(1.0)
  public class HelpLinkButton extends AbstractHelpLinkButton {

    @Override
    protected boolean getConfiguredProcessButton() {
      return false;
    }

  }

  @Order(2.0)
  public class PlaceholderField extends AbstractPlaceholderField {
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return DISPLAY_HINT_VIEW;
  }

  @Override
  protected String getConfiguredDisplayViewId() {
    return VIEW_ID_E;
  }

  @Override
  protected int getConfiguredModalityHint() {
    return IForm.MODALITY_HINT_MODELESS;
  }

  public LinkBox getLinkBox() {
    return getFieldByClass(LinkBox.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected int getConfiguredGridColumnCount() {
      return 1;
    }

    @Order(10.0)
    public class LinkBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredBorderDecoration() {
        return BORDER_DECORATION_EMPTY;
      }

      @Override
      protected void injectFieldsInternal(OrderedCollection<IFormField> fieldList) {
        linkList = new ArrayList<Link>();

        // Help Link
        fieldList.addLast(new HelpLinkButton());
        fieldList.addLast(new PlaceholderField());

        // Action Links
        for (int i = 0; i < 20; i++) {
          Link menu = new Link();
          fieldList.addLast(menu);
          linkList.add(menu);
        }
      }

    }

  }
}
