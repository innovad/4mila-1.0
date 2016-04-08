package com.rtiming.client.ecard.download;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dao.RtEcardStation;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.IECardStationProcessService;
import com.rtiming.shared.results.IResultsOutlineService;

public class ECardStationsTablePage extends AbstractPageWithTable<ECardStationsTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("ECardStations");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<RtEcardStation> list = BEANS.get(IResultsOutlineService.class).getECardStationTableData();
    Object[][] table = new Object[list.size()][getTable().getColumnCount()];

    int k = 0;
    for (RtEcardStation row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getBaudColumn().getColumnIndex()] = row.getBaud();
      table[k][getTable().getAddressColumn().getColumnIndex()] = row.getClientAddress();
      table[k][getTable().getECardStationNrColumn().getColumnIndex()] = row.getId().getId();
      table[k][getTable().getIdentifierColumn().getColumnIndex()] = row.getIdentifier();
      table[k][getTable().getModusColumn().getColumnIndex()] = row.getModusUid();
      table[k][getTable().getPortColumn().getColumnIndex()] = row.getPort();
      table[k][getTable().getPosPrinterColumn().getColumnIndex()] = row.getPosPrinter();
      table[k][getTable().getPrinterColumn().getColumnIndex()] = row.getPrinter();
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

    public IdentifierColumn getIdentifierColumn() {
      return getColumnSet().getColumnByClass(IdentifierColumn.class);
    }

    public PortColumn getPortColumn() {
      return getColumnSet().getColumnByClass(PortColumn.class);
    }

    public BaudColumn getBaudColumn() {
      return getColumnSet().getColumnByClass(BaudColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.ECARD_STATION;
    }

    public ModusColumn getModusColumn() {
      return getColumnSet().getColumnByClass(ModusColumn.class);
    }

    public PosPrinterColumn getPosPrinterColumn() {
      return getColumnSet().getColumnByClass(PosPrinterColumn.class);
    }

    public PrinterColumn getPrinterColumn() {
      return getColumnSet().getColumnByClass(PrinterColumn.class);
    }

    public AddressColumn getAddressColumn() {
      return getColumnSet().getColumnByClass(AddressColumn.class);
    }

    public ECardStationNrColumn getECardStationNrColumn() {
      return getColumnSet().getColumnByClass(ECardStationNrColumn.class);
    }

    @Order(10.0)
    public class ECardStationNrColumn extends AbstractLongColumn {

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
    public class IdentifierColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return ScoutTexts.get("Name");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(30.0)
    public class AddressColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Address");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(40.0)
    public class PortColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return ScoutTexts.get("Port");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(50.0)
    public class ModusColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Mode");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ECardStationDownloadModusCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(60.0)
    public class BaudColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Speed");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(70.0)
    public class PrinterColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("OperatingSystemPrinter");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(80.0)
    public class PosPrinterColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("PosPrinter");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(10.0)
    public class NewButtonMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("NewButton");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected void execAction() throws ProcessingException {
        ECardStationForm form = new ECardStationForm();
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
        ECardStationForm form = new ECardStationForm();
        form.setECardStationNr(getECardStationNrColumn().getSelectedValue());
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
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        String myAddress = FMilaUtility.getHostAddress();
        setEnabled(StringUtility.equalsIgnoreCase(myAddress, getAddressColumn().getSelectedValue()));
      }

      @Override
      protected void execAction() throws ProcessingException {
        ECardStationStatusForm statusForm = ClientSession.get().getDesktop().findForm(ECardStationStatusForm.class);
        if (CompareUtility.equals(statusForm.getCurrentECardStationNr(), getECardStationNrColumn().getSelectedValue())) {
          throw new VetoException(TEXTS.get("ECardStationInUseDeletionError"));
        }
        else if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("ECardStations"), getIdentifierColumn().getSelectedValues())) {
          for (Long eCardStationNr : getTable().getECardStationNrColumn().getSelectedValues()) {
            ECardStationFormData formData = new ECardStationFormData();
            formData.setECardStationNr(eCardStationNr);
            BEANS.get(IECardStationProcessService.class).delete(formData, false);
          }
          reloadPage();
        }
      }
    }
  }
}
