package com.rtiming.client.ecard;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.BooleanUtility;
import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.ColorUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.ecard.ECardTypeCodeType;
import com.rtiming.shared.ecard.ECardsSearchFormData;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.event.IEventsOutlineService;

public class ECardsTablePage extends AbstractPageWithTable<ECardsTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("ECards");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<RtEcard> list = BEANS.get(IEventsOutlineService.class).getECardTableData((ECardsSearchFormData) filter.getFormData());

    Object[][] table = new Object[list.size()][getTable().getColumnCount()];
    int k = 0;
    for (RtEcard row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getECardNrColumn().getColumnIndex()] = row.getKey().getId();
      table[k][getTable().getECardTypeColumn().getColumnIndex()] = row.getTypeUid();
      table[k][getTable().getRentalCardColumn().getColumnIndex()] = row.getRentalCard();
      table[k][getTable().getNumberColumn().getColumnIndex()] = row.getEcardNo();
      k++;
    }
    importTableData(table);
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.ECARD;
    }

    public RentalCardColumn getRentalCardColumn() {
      return getColumnSet().getColumnByClass(RentalCardColumn.class);
    }

    @Override
    protected void execDecorateRow(ITableRow row) throws ProcessingException {
      if (BooleanUtility.nvl(getRentalCardColumn().getValue(row))) {
        row.setForegroundColor(ColorUtility.BLUE);
      }
    }

    public ECardNrColumn getECardNrColumn() {
      return getColumnSet().getColumnByClass(ECardNrColumn.class);
    }

    public ECardTypeColumn getECardTypeColumn() {
      return getColumnSet().getColumnByClass(ECardTypeColumn.class);
    }

    public NumberColumn getNumberColumn() {
      return getColumnSet().getColumnByClass(NumberColumn.class);
    }

    @Order(10.0)
    public class ECardNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected boolean getConfiguredPrimaryKey() {
        return true;
      }
    }

    @Order(20.0)
    public class NumberColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Number");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(30.0)
    public class ECardTypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("ECardType");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ECardTypeCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(40.0)
    public class RentalCardColumn extends AbstractBooleanColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("RentalCard");
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
        ECardForm form = new ECardForm();
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
        ECardForm form = new ECardForm();
        form.setECardKey(RtEcardKey.create(getECardNrColumn().getSelectedValue()));
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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("ECards"), getNumberColumn().getSelectedValues())) {
          for (Long runnerNr : getECardNrColumn().getSelectedValues()) {
            RtEcardKey key = RtEcardKey.create(runnerNr);
            BEANS.get(IECardProcessService.class).delete(key);
          }
          reloadPage();
        }
      }

    }
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return ECardsSearchForm.class;
  }
}
