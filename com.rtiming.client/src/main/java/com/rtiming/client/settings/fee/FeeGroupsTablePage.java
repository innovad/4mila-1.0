package com.rtiming.client.settings.fee;

import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.settings.ISettingsOutlineService;
import com.rtiming.shared.settings.fee.FeeGroupFormData;
import com.rtiming.shared.settings.fee.IFeeGroupProcessService;

public class FeeGroupsTablePage extends AbstractPageWithTable<FeeGroupsTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("FeeGroups");
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    return new FeeTablePage(getTable().getFeeGroupNrColumn().getValue(row));
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(ISettingsOutlineService.class).getFeeGroupTableData());
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.FEE_GROUP;
    }

    public CountColumn getCountColumn() {
      return getColumnSet().getColumnByClass(CountColumn.class);
    }

    public CashPaymentOnRegistrationColumn getCashPaymentOnRegistrationColumn() {
      return getColumnSet().getColumnByClass(CashPaymentOnRegistrationColumn.class);
    }

    public ClientNrColumn getClientNrColumn() {
      return getColumnSet().getColumnByClass(ClientNrColumn.class);
    }

    public FeeGroupNrColumn getFeeGroupNrColumn() {
      return getColumnSet().getColumnByClass(FeeGroupNrColumn.class);
    }

    @Order(10.0)
    public class FeeGroupNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(20.0)
    public class ClientNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(30.0)
    public class NameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("FeeGroup");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(40.0)
    public class CashPaymentOnRegistrationColumn extends AbstractBooleanColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("CashPaymentOnRegistration");
      }

      @Override
      protected int getConfiguredWidth() {
        return 160;
      }
    }

    @Order(50.0)
    public class CountColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Count");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(10.0)
    public class NewMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("NewButton");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() throws ProcessingException {
        FeeGroupForm form = new FeeGroupForm();
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(20.0)
    public class EditMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("EditMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        FeeGroupForm form = new FeeGroupForm();
        form.setFeeGroupNr(getFeeGroupNrColumn().getSelectedValue());
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(30.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(40.0)
    public class DeleteMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("FeeGroups"), getNameColumn().getSelectedValues())) {
          for (Long feeGroupNr : getFeeGroupNrColumn().getSelectedValues()) {
            FeeGroupFormData formData = new FeeGroupFormData();
            formData.setFeeGroupNr(feeGroupNr);
            BEANS.get(IFeeGroupProcessService.class).delete(formData);
          }
          reloadPage();
        }
      }
    }
  }
}
