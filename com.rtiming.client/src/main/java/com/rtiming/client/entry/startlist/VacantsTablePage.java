package com.rtiming.client.entry.startlist;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.columns.AbstractTimeWithSecondsColumn;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table.AddRunnerMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.entry.IRegistrationsOutlineService;
import com.rtiming.shared.event.course.ClassCodeType;

public class VacantsTablePage extends AbstractPageWithTable<VacantsTablePage.Table> implements IHelpEnabledPage {

  private final Long eventNr;

  public VacantsTablePage(Long eventNr) {
    super();
    this.eventNr = eventNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Vacants");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IRegistrationsOutlineService.class).getVacantsTableData(getEventNr()));
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.TIMEFIELDTIME;
    }

    public CourseColumn getCourseColumn() {
      return getColumnSet().getColumnByClass(CourseColumn.class);
    }

    public StartTimeColumn getStartTimeColumn() {
      return getColumnSet().getColumnByClass(StartTimeColumn.class);
    }

    public RunnerColumn getRunnerColumn() {
      return getColumnSet().getColumnByClass(RunnerColumn.class);
    }

    public StartlistSettingNrColumn getStartlistSettingNrColumn() {
      return getColumnSet().getColumnByClass(StartlistSettingNrColumn.class);
    }

    public BibNumberColumn getBibNumberColumn() {
      return getColumnSet().getColumnByClass(BibNumberColumn.class);
    }

    public ClazzColumn getClazzColumn() {
      return getColumnSet().getColumnByClass(ClazzColumn.class);
    }

    @Order(10.0)
    public class StartlistSettingNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(20.0)
    public class ClazzColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ClassCodeType.class;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Class");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 195;
      }
    }

    @Order(30.0)
    public class CourseColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Course");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(40.0)
    public class StartTimeColumn extends AbstractTimeWithSecondsColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("StartTime");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 2;
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(50.0)
    public class BibNumberColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("BibNumber");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(60.0)
    public class RunnerColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Runner");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }

      @Override
      protected boolean getConfiguredTextWrap() {
        return true;
      }
    }

    @Order(10.0)
    public class NewEntryMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("NewEntryMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        EntryForm form = new EntryForm();
        form.startNew();
        form.getEventsField().getTable().getEventNrColumn().fill(getEventNr());
        // add vacant info
        form.getRacesField().getTable().runMenu(AddRunnerMenu.class);
        form.getClazzField().setValue(getClazzColumn().getSelectedValue());
        form.getRacesField().getTable().getLegStartTimeColumn().setValue(0, getStartTimeColumn().getSelectedValue());
        form.getRacesField().getTable().getBibNumberColumn().setValue(0, getBibNumberColumn().getSelectedValue());
        form.getEventsField().getTable().getStartTimeColumn().setValue(0, getStartTimeColumn().getSelectedValue());
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(StringUtility.isNullOrEmpty(getRunnerColumn().getSelectedValue()));
      }
    }
  }

  public Long getEventNr() {
    return eventNr;
  }

}
