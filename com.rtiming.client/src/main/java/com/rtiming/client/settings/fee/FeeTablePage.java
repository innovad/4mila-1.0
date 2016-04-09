package com.rtiming.client.settings.fee;

import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.settings.ISettingsOutlineService;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.fee.FeeFormData;
import com.rtiming.shared.settings.fee.IFeeProcessService;

public class FeeTablePage extends AbstractPageWithTable<FeeTablePage.Table> implements IHelpEnabledPage {

  private final Long feeGroupNr;

  public FeeTablePage(Long feeGroupNr) {
    super();
    this.feeGroupNr = feeGroupNr;
  }

  public Long getFeeGroupNr() {
    return feeGroupNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Fee");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(ISettingsOutlineService.class).getFeeTableData(getFeeGroupNr()));
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    public FeeColumn getFeeColumn() {
      return getColumnSet().getColumnByClass(FeeColumn.class);
    }

    public EvtFromColumn getEvtFromColumn() {
      return getColumnSet().getColumnByClass(EvtFromColumn.class);
    }

    public EvtToColumn getEvtToColumn() {
      return getColumnSet().getColumnByClass(EvtToColumn.class);
    }

    public AgeToColumn getAgeToColumn() {
      return getColumnSet().getColumnByClass(AgeToColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.PAYMENT;
    }

    public CurrencyColumn getCurrencyColumn() {
      return getColumnSet().getColumnByClass(CurrencyColumn.class);
    }

    public AgeFromColumn getAgeFromColumn() {
      return getColumnSet().getColumnByClass(AgeFromColumn.class);
    }

    public ClientNrColumn getClientNrColumn() {
      return getColumnSet().getColumnByClass(ClientNrColumn.class);
    }

    public FeeNrColumn getFeeNrColumn() {
      return getColumnSet().getColumnByClass(FeeNrColumn.class);
    }

    @Order(10.0)
    public class FeeNrColumn extends AbstractLongColumn {

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
    public class FeeColumn extends AbstractBigDecimalColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Fee");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(40.0)
    public class CurrencyColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Currency");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return CurrencyCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(50.0)
    public class EvtFromColumn extends AbstractDateColumn {

      @Override
      protected boolean getConfiguredHasTime() {
        return true;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("From");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(60.0)
    public class EvtToColumn extends AbstractDateColumn {

      @Override
      protected boolean getConfiguredHasTime() {
        return true;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("To");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(70.0)
    public class AgeFromColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("From");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(80.0)
    public class AgeToColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("To");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
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
        FeeForm form = new FeeForm();
        form.setFeeGroupNr(getFeeGroupNr());
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
        FeeForm form = new FeeForm();
        form.setFeeNr(getFeeNrColumn().getSelectedValue());
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
        return ScoutTexts.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Fee"), getFeeColumn().getSelectedDisplayTexts())) {
          for (Long feeNr : getTable().getFeeNrColumn().getSelectedValues()) {
            FeeFormData formData = new FeeFormData();
            formData.setFeeNr(feeNr);
            BEANS.get(IFeeProcessService.class).delete(formData);
          }
          reloadPage();
        }
      }

    }
  }
}
