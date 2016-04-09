package com.rtiming.client.result;

import java.util.List;
import java.util.Set;

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
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ColorUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.common.ui.columns.AbstractDateTimeWithSecondsColumn;
import com.rtiming.client.common.ui.columns.AbstractTimeDifferenceColumn;
import com.rtiming.client.race.RaceControlForm;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.RaceControlRowData;
import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.race.IRaceControlProcessService;
import com.rtiming.shared.race.IRaceService;

public abstract class AbstractRaceControlTable extends AbstractTable {

  @Override
  protected String getConfiguredDefaultIconId() {
    return Icons.TIMEFIELDTIME;
  }

  public abstract void reloadTable() throws ProcessingException;

  public abstract boolean getConfiguredIsDeleteAllowed();

  protected abstract Long[] getRaceNrs();

  @Override
  protected Class<? extends IMenu> getConfiguredDefaultMenu() {
    return EditMenu.class;
  }

  @Override
  protected void execDecorateRow(ITableRow row) throws ProcessingException {
    if (getControlTypeColumn().getValue(row) != null) {
      if (getControlTypeColumn().getValue(row) == ControlTypeCodeType.ControlCode.ID) {
        // normal
      }
      else {
        row.setFont(FontSpec.parse("bold"));
      }

      if (getControlStatusColumn().getValue(row) == ControlStatusCodeType.OkCode.ID) {
        // normal
      }
      else if (getControlStatusColumn().getValue(row) == ControlStatusCodeType.InitialStatusCode.ID) {
        row.setForegroundColor(FMilaUtility.COLOR_LIGHT_GREY);
      }
      else if (getControlStatusColumn().getValue(row) == ControlStatusCodeType.AdditionalCode.ID) {
        row.setForegroundColor(ColorUtility.BLUE);
      }
      else {
        row.setForegroundColor(ColorUtility.RED);
      }
    }
  }

  public ControlColumn getControlColumn() {
    return getColumnSet().getColumnByClass(ControlColumn.class);
  }

  public ControlStatusColumn getControlStatusColumn() {
    return getColumnSet().getColumnByClass(ControlStatusColumn.class);
  }

  public ControlTypeColumn getControlTypeColumn() {
    return getColumnSet().getColumnByClass(ControlTypeColumn.class);
  }

  public CountLegColumn getCountLegColumn() {
    return getColumnSet().getColumnByClass(CountLegColumn.class);
  }

  public LegTimeColumn getLegTimeColumn() {
    return getColumnSet().getColumnByClass(LegTimeColumn.class);
  }

  public LegTimeRawColumn getLegTimeRawColumn() {
    return getColumnSet().getColumnByClass(LegTimeRawColumn.class);
  }

  public OverallTimeColumn getOverallTimeColumn() {
    return getColumnSet().getColumnByClass(OverallTimeColumn.class);
  }

  public OverallTimeRawColumn getOverallTimeRawColumn() {
    return getColumnSet().getColumnByClass(OverallTimeRawColumn.class);
  }

  public RaceControlNrColumn getRaceControlNrColumn() {
    return getColumnSet().getColumnByClass(RaceControlNrColumn.class);
  }

  public CourseControlNrColumn getCourseControlNrColumn() {
    return getColumnSet().getColumnByClass(CourseControlNrColumn.class);
  }

  public RaceNrColumn getRaceNrColumn() {
    return getColumnSet().getColumnByClass(RaceNrColumn.class);
  }

  public RelativeTimeColumn getRelativeTimeColumn() {
    return getColumnSet().getColumnByClass(RelativeTimeColumn.class);
  }

  public SortCodeColumn getSortCodeColumn() {
    return getColumnSet().getColumnByClass(SortCodeColumn.class);
  }

  public ShiftTimeColumn getShiftTimeColumn() {
    return getColumnSet().getColumnByClass(ShiftTimeColumn.class);
  }

  public ManualStatusColumn getManualStatusColumn() {
    return getColumnSet().getColumnByClass(ManualStatusColumn.class);
  }

  @Order(10.0)
  public class RaceNrColumn extends AbstractLongColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }

    @Override
    protected int getConfiguredSortIndex() {
      return 1;
    }
  }

  @Order(20.0)
  public class RaceControlNrColumn extends AbstractLongColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(25.0)
  public class CourseControlNrColumn extends AbstractLongColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(30.0)
  public class ControlTypeColumn extends AbstractLongColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(40.0)
  public class ControlColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Control");
    }

    @Override
    protected int getConfiguredWidth() {
      return 80;
    }
  }

  @Order(50.0)
  public class SortCodeColumn extends AbstractLongColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("SortCode");
    }

    @Override
    protected int getConfiguredSortIndex() {
      return 2;
    }

    @Override
    protected int getConfiguredWidth() {
      return 80;
    }
  }

  @Order(60.0)
  public class ControlStatusColumn extends AbstractSmartColumn<Long> {

    @Override
    protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
      return ControlStatusCodeType.class;

    }

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("ControlStatus");
    }

    @Override
    protected int getConfiguredWidth() {
      return 140;
    }
  }

  @Order(70.0)
  public class OverallTimeColumn extends AbstractDateTimeWithSecondsColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Time");
    }

    @Override
    protected int getConfiguredWidth() {
      return 160;
    }

    @Override
    protected int getConfiguredSortIndex() {
      return 3;
    }

  }

  @Order(80.0)
  public class OverallTimeRawColumn extends AbstractLongColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(90.0)
  public class RelativeTimeColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("Time");
    }

    @Override
    protected int getConfiguredHorizontalAlignment() {
      return 1;
    }

    @Override
    protected int getConfiguredWidth() {
      return 90;
    }
  }

  @Order(100.0)
  public class LegTimeColumn extends AbstractStringColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("LegTime");
    }

    @Override
    protected int getConfiguredHorizontalAlignment() {
      return 1;
    }

    @Override
    protected int getConfiguredWidth() {
      return 90;
    }
  }

  @Order(110.0)
  public class LegTimeRawColumn extends AbstractLongColumn {

    @Override
    protected boolean getConfiguredDisplayable() {
      return false;
    }
  }

  @Order(120.0)
  public class CountLegColumn extends AbstractBooleanColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("CountLeg");
    }

    @Override
    protected int getConfiguredWidth() {
      return 120;
    }
  }

  @Order(130.0)
  public class ManualStatusColumn extends AbstractBooleanColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("ManualControlStatus");
    }

    @Override
    protected int getConfiguredWidth() {
      return 120;
    }
  }

  @Order(140.0)
  public class ShiftTimeColumn extends AbstractTimeDifferenceColumn {

    @Override
    protected String getConfiguredHeaderText() {
      return Texts.get("ShiftTime");
    }

    @Override
    protected int getConfiguredWidth() {
      return 120;
    }

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

  }

  @Order(10.0)
  public class EditMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return ScoutTexts.get("EditMenu");
    }

    @Override
    protected void execAction() throws ProcessingException {
      RaceControlForm form = new RaceControlForm();
      Long raceControlNr = getRaceControlNrColumn().getSelectedValue();
      form.setRaceControlNr(raceControlNr);
      form.startModify();
      form.waitFor();
      if (form.isFormStored()) {
        BEANS.get(IRaceService.class).validateAndPersistRace(form.getRaceField().getValue());
        // reload outer page and this table together
        ClientSession.get().getDesktop().getOutline().getActivePage().reloadPage();
      }
      // workaround for selection bug (always the first selected stays and is never cleared when using this menu)
      deselectAllRows();
    }
  }

  @Order(20.0)
  public class SeparatorMenu extends AbstractSeparatorMenu {
  }

  @Order(60.0)
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
    protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
      setEnabled(getConfiguredIsDeleteAllowed());
    }

    @Override
    protected void execAction() throws ProcessingException {
      if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("RaceControl"), getControlColumn().getSelectedValues())) {
        BEANS.get(IRaceControlProcessService.class).delete(getRaceControlNrColumn().getSelectedValues().toArray(new Long[0]));
        reloadTable();
      }
    }
  }

  public Object[][] list2data(List<RaceControlRowData> list) {
    Object[][] table = new Object[list.size()][this.getColumnCount()];
    int k = 0;
    for (RaceControlRowData row : list) {
      table[k] = new Object[this.getColumnCount()];
      table[k][this.getControlColumn().getColumnIndex()] = row.getControlNo();
      table[k][this.getCourseControlNrColumn().getColumnIndex()] = row.getCourseControlNr();
      table[k][this.getLegTimeColumn().getColumnIndex()] = row.getLegTime();
      table[k][this.getLegTimeRawColumn().getColumnIndex()] = row.getLegTimeRaw();
      table[k][this.getOverallTimeColumn().getColumnIndex()] = row.getOverallTime();
      table[k][this.getOverallTimeRawColumn().getColumnIndex()] = row.getOverallTimeRaw();
      table[k][this.getRaceControlNrColumn().getColumnIndex()] = row.getRaceControlNr();
      table[k][this.getRaceNrColumn().getColumnIndex()] = row.getRaceNr();
      table[k][this.getShiftTimeColumn().getColumnIndex()] = row.getShiftTime();
      table[k][this.getSortCodeColumn().getColumnIndex()] = row.getSortCode();
      table[k][this.getControlStatusColumn().getColumnIndex()] = row.getStatusUid();
      table[k][this.getRelativeTimeColumn().getColumnIndex()] = row.getRelativeTime();
      table[k][this.getControlTypeColumn().getColumnIndex()] = row.getTypeUid();
      table[k][this.getCountLegColumn().getColumnIndex()] = row.isCountLeg();
      table[k][this.getManualStatusColumn().getColumnIndex()] = row.isManualStatus();
      k++;
    }
    return table;
  }

}
