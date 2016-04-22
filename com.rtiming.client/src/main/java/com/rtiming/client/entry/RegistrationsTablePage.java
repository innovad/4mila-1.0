package com.rtiming.client.entry;

import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.AbstractDoubleColumn;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.entry.IRegistrationProcessService;
import com.rtiming.shared.entry.IRegistrationsOutlineService;
import com.rtiming.shared.entry.RegistrationFormData;
import com.rtiming.shared.entry.RegistrationsSearchFormData;

public class RegistrationsTablePage extends AbstractPageWithTable<RegistrationsTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return RegistrationsSearchForm.class;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Registrations");
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) throws ProcessingException {
    return new RegistrationNodePage(getTable().getRegistrationNrColumn().getValue(row));
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IRegistrationsOutlineService.class).getRegistrationTableData((RegistrationsSearchFormData) getSearchFilter().getFormData()));
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public RunnersColumn getRunnersColumn() {
      return getColumnSet().getColumnByClass(RunnersColumn.class);
    }

    public FeeColumn getFeeColumn() {
      return getColumnSet().getColumnByClass(FeeColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.REGISTRATION;
    }

    public RunnerCountColumn getRunnerCountColumn() {
      return getColumnSet().getColumnByClass(RunnerCountColumn.class);
    }

    public PaymentsColumn getPaymentsColumn() {
      return getColumnSet().getColumnByClass(PaymentsColumn.class);
    }

    public ClubsColumn getClubsColumn() {
      return getColumnSet().getColumnByClass(ClubsColumn.class);
    }

    public DateColumn getDateColumn() {
      return getColumnSet().getColumnByClass(DateColumn.class);
    }

    public EntryCountColumn getEntryCountColumn() {
      return getColumnSet().getColumnByClass(EntryCountColumn.class);
    }

    public EventsColumn getEventsColumn() {
      return getColumnSet().getColumnByClass(EventsColumn.class);
    }

    public RegistrationNoColumn getRegistrationNoColumn() {
      return getColumnSet().getColumnByClass(RegistrationNoColumn.class);
    }

    public RegistrationNrColumn getRegistrationNrColumn() {
      return getColumnSet().getColumnByClass(RegistrationNrColumn.class);
    }

    @Order(10.0)
    public class RegistrationNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

    @Order(20.0)
    public class RegistrationNoColumn extends AbstractStringColumn {

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
    public class RunnersColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredTextWrap() {
        return true;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Runners");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(40.0)
    public class ClubsColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Clubs");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(50.0)
    public class EventsColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Events");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(60.0)
    public class EntryCountColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Entries");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(70.0)
    public class RunnerCountColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Runners");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(80.0)
    public class FeeColumn extends AbstractDoubleColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Fee");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(90.0)
    public class PaymentsColumn extends AbstractDoubleColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Payments");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(100.0)
    public class DateColumn extends AbstractDateColumn {

      @Override
      protected boolean getConfiguredHasTime() {
        return true;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Date");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(10.0)
    public class RegistrationMenu extends AbstractMenu {

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
        RegistrationForm form = new RegistrationForm();
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
        RegistrationForm form = new RegistrationForm();
        form.setRegistrationNr(getTable().getRegistrationNrColumn().getSelectedValue());
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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Registrations"), getRegistrationNoColumn().getSelectedDisplayTexts())) {
          for (Long registrationNr : getRegistrationNrColumn().getSelectedValues()) {
            RegistrationFormData formData = new RegistrationFormData();
            formData.setRegistrationNr(registrationNr);
            BEANS.get(IRegistrationProcessService.class).delete(formData);
          }
          reloadPage();
        }
      }
    }
  }
}
