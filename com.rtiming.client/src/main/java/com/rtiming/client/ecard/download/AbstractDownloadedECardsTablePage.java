package com.rtiming.client.ecard.download;

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
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ColorUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.report.jrxml.SplitTimesReport;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.common.ui.columns.AbstractDateTimeWithSecondsColumn;
import com.rtiming.client.common.ui.columns.AbstractTimeWithSecondsColumn;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField;
import com.rtiming.client.result.pos.IPosPrinter;
import com.rtiming.client.result.pos.PosPrinterManager;
import com.rtiming.client.result.pos.SplitTimesPosPrinter;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.ecard.download.DownloadedECardFormData;
import com.rtiming.shared.ecard.download.DownloadedECards;
import com.rtiming.shared.ecard.download.DownloadedECardsSearchFormData;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.IDownloadedECardProcessService;
import com.rtiming.shared.ecard.download.IECardStationProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.results.IResultsOutlineService;

public abstract class AbstractDownloadedECardsTablePage extends AbstractPageWithTable<AbstractDownloadedECardsTablePage.Table> {

  private final DownloadedECards presentationType;

  protected AbstractDownloadedECardsTablePage(DownloadedECards presentationType) {
    this.presentationType = presentationType;
  }

  public DownloadedECards getPresentationType() {
    return presentationType;
  }

  @Override
  protected void execInitSearchForm() throws ProcessingException {
    ((DownloadedECardsSearchForm) getSearchFormInternal()).setPresentationType(getPresentationType());
  }

  @Override
  protected void execInitPage() throws ProcessingException {
    RaceControlsTableForm form = new RaceControlsTableForm();
    setDetailForm(form);

    switch (getPresentationType()) {
      case ALL:
        getCellForUpdate().setText(TEXTS.get("All"));
        break;
      case DUPLICATE:
        getCellForUpdate().setText(TEXTS.get("Multiple"));
        break;
      case MISSING_PUNCH:
        getCellForUpdate().setText(TEXTS.get("MissingControls"));
        break;
      case ENTRY_NOT_FOUND:
        getCellForUpdate().setText(TEXTS.get("NoEntryAssigned"));
        break;
      default:
        break;
    }

    getTable().setSortEnabled(false);
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("DownloadedECards");
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) throws ProcessingException {
    return new PunchesTablePage(getTable().getPunchSessionNrColumn().getValue(row));
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IResultsOutlineService.class).getDownloadedECardTableData(getPresentationType(), (DownloadedECardsSearchFormData) filter.getFormData()));
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      if (getRaceNrColumn().getSelectedValue() != null) {
        return EditRaceMenu.class;
      }
      else {
        return EditDownloadedECardMenu.class;
      }
    }

    @Override
    protected boolean getConfiguredScrollToSelection() {
      return true;
    }

    @Override
    protected void execDecorateRow(ITableRow row) throws ProcessingException {
      if (getMultipleDownloadColumn().getValue(row) && getRaceStatusColumn().getValue(row) == null) {
        row.setBackgroundColor(FMilaUtility.COLOR_ORANGE);
        row.setFont(FontSpec.parse("bold"));
      }
      else if (getRaceStatusColumn().getValue(row) == null) {
        row.setBackgroundColor(ColorUtility.RED);
        row.setFont(FontSpec.parse("bold"));
      }
      else if (CompareUtility.notEquals(getRaceStatusColumn().getValue(row), RaceStatusCodeType.OkCode.ID)) {
        row.setBackgroundColor(FMilaUtility.COLOR_LIGHT_BLUE);
        row.setFont(FontSpec.parse("italic"));
      }
    }

    @Override
    protected void execRowsSelected(List<? extends ITableRow> rows) throws ProcessingException {
      RaceControlsTableForm form = (RaceControlsTableForm) getDetailForm();
      if (rows != null && rows.size() == 1) {
        form.setRaceNrs(new Long[]{getRaceNrColumn().getSelectedValue()});
        form.getRaceControlField().getTable().reloadTable();
      }
      else {
        form.getRaceControlField().getTable().clearTable();
      }
    }

    public RunnerColumn getRunnerColumn() {
      return getColumnSet().getColumnByClass(RunnerColumn.class);
    }

    public TimeColumn getTimeColumn() {
      return getColumnSet().getColumnByClass(TimeColumn.class);
    }

    public RaceStatusColumn getRaceStatusColumn() {
      return getColumnSet().getColumnByClass(RaceStatusColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.ECARD;
    }

    public EventNrColumn getEventNrColumn() {
      return getColumnSet().getColumnByClass(EventNrColumn.class);
    }

    public RaceNrColumn getRaceNrColumn() {
      return getColumnSet().getColumnByClass(RaceNrColumn.class);
    }

    public MultipleDownloadColumn getMultipleDownloadColumn() {
      return getColumnSet().getColumnByClass(MultipleDownloadColumn.class);
    }

    public ClearColumn getClearColumn() {
      return getColumnSet().getColumnByClass(ClearColumn.class);
    }

    public StartColumn getStartColumn() {
      return getColumnSet().getColumnByClass(StartColumn.class);
    }

    public FinishColumn getFinishColumn() {
      return getColumnSet().getColumnByClass(FinishColumn.class);
    }

    public OutlineColumn getOutlineColumn() {
      return getColumnSet().getColumnByClass(OutlineColumn.class);
    }

    public StartTimeColumn getStartTimeColumn() {
      return getColumnSet().getColumnByClass(StartTimeColumn.class);
    }

    public BibNumberColumn getBibNumberColumn() {
      return getColumnSet().getColumnByClass(BibNumberColumn.class);
    }

    public CheckColumn getCheckColumn() {
      return getColumnSet().getColumnByClass(CheckColumn.class);
    }

    public ClazzColumn getClazzColumn() {
      return getColumnSet().getColumnByClass(ClazzColumn.class);
    }

    public CourseColumn getCourseColumn() {
      return getColumnSet().getColumnByClass(CourseColumn.class);
    }

    public DownloadedOnColumn getDownloadedOnColumn() {
      return getColumnSet().getColumnByClass(DownloadedOnColumn.class);
    }

    public ECardColumn getECardColumn() {
      return getColumnSet().getColumnByClass(ECardColumn.class);
    }

    public PunchSessionNrColumn getPunchSessionNrColumn() {
      return getColumnSet().getColumnByClass(PunchSessionNrColumn.class);
    }

    public EntryNrColumn getEntryNrColumn() {
      return getColumnSet().getColumnByClass(EntryNrColumn.class);
    }

    @Order(10.0)
    public class PunchSessionNrColumn extends AbstractLongColumn {

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
    public class EventNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(25.0)
    public class EntryNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(30.0)
    public class RaceNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(40.0)
    public class OutlineColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("DownloadedECard");
      }

      @Override
      protected boolean getConfiguredSummary() {
        return true;
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(50.0)
    public class ECardColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("ECard");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(60.0)
    public class DownloadedOnColumn extends AbstractDateTimeWithSecondsColumn {

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected boolean getConfiguredSortAscending() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("DownloadedOn");
      }

      @Override
      protected int getConfiguredWidth() {
        return 130;
      }
    }

    @Order(70.0)
    public class BibNumberColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("BibNumber");
      }

      @Override
      protected int getConfiguredWidth() {
        return 90;
      }
    }

    @Order(80.0)
    public class StartTimeColumn extends AbstractTimeWithSecondsColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("StartTime");
      }

    }

    @Order(90.0)
    public class RunnerColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Runner");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(100.0)
    public class CourseColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Course");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(110.0)
    public class ClazzColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Class");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ClassCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(120.0)
    public class TimeColumn extends AbstractStringColumn {

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
        return 80;
      }
    }

    @Order(130.0)
    public class RaceStatusColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("ControlStatus");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return RaceStatusCodeType.class;
      }

      @Override
      protected int getConfiguredWidth() {
        return 90;
      }
    }

    @Order(140.0)
    public class ClearColumn extends AbstractTimeWithSecondsColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Clear");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 75;
      }
    }

    @Order(150.0)
    public class CheckColumn extends AbstractTimeWithSecondsColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Check");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 75;
      }
    }

    @Order(160.0)
    public class StartColumn extends AbstractTimeWithSecondsColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Start");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 75;
      }
    }

    @Order(170.0)
    public class FinishColumn extends AbstractTimeWithSecondsColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Finish");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 75;
      }
    }

    @Order(180.0)
    public class MultipleDownloadColumn extends AbstractBooleanColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("MultipleDownload");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(20.0)
    public class EditRaceMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("EditEntryAndRace");
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(getRaceNrColumn().getSelectedValue() != null);
      }

      @Override
      protected void execAction() throws ProcessingException {
        EntryForm entry = new EntryForm();
        entry.setEntryNr(getTable().getEntryNrColumn().getSelectedValue());
        entry.startModify();
        for (ITableRow row : entry.getRacesField().getTable().getRows()) {
          Long currentRaceNr = entry.getRacesField().getTable().getRaceNrColumn().getValue(row);
          Long raceNr = getTable().getRaceNrColumn().getSelectedValue();
          if (CompareUtility.equals(currentRaceNr, raceNr)) {
            entry.getRacesField().getTable().selectRow(row);
            entry.getRacesField().getTable().runMenu(RacesField.Table.EditMenu.class);
          }
        }
        entry.waitFor();
        if (entry.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(30.0)
    public class EditDownloadedECardMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("EditDownloadedECardMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        DownloadedECardForm form = new DownloadedECardForm();
        form.setPunchSessionNr(getPunchSessionNrColumn().getSelectedValue());
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(40.0)
    public class Separator1Menu extends AbstractSeparatorMenu {
    }

    @Order(70.0)
    public class PrintSplitTimesDirectMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("PrintSplitTimesDirect");
      }

      @Override
      protected void execAction() throws ProcessingException {
        ECardStationStatusForm station = ClientSession.get().getDesktop().findForm(ECardStationStatusForm.class);
        if (station != null) {
          Long eCardStationNr = station.getCurrentECardStationNr();
          ECardStationFormData formData = new ECardStationFormData();
          formData.setECardStationNr(eCardStationNr);
          formData = BEANS.get(IECardStationProcessService.class).load(formData);
          if (!StringUtility.isNullOrEmpty(formData.getPosPrinter().getValue())) {
            IPosPrinter printer = PosPrinterManager.get(formData.getPosPrinter().getValue());
            for (Long raceNr : getRaceNrColumn().getSelectedValues()) {
              SplitTimesPosPrinter.printSplitTimesReport(raceNr, printer);
            }
            return;
          }
          else if (!StringUtility.isNullOrEmpty(formData.getPrinter().getValue())) {
            SplitTimesReport.printSplitTimesReport(getRaceNrColumn().getSelectedValues().toArray(new Long[0]), formData.getPrinter().getValue());
            return;
          }
        }
        throw new VetoException(TEXTS.get("NoPrinterSetOnDownloadStation"));
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(true);
        for (Long raceNr : getRaceNrColumn().getSelectedValues()) {
          if (raceNr == null) {
            setEnabled(false);
            break;
          }
        }
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }
    }

    @Order(80.0)
    public class ValidateRaceMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("ValidateRace");
      }

      @Override
      protected void execAction() throws ProcessingException {
        boolean unassignedDownloadedECardExists = false;
        for (Long raceNr : getRaceNrColumn().getSelectedValues()) {
          if (raceNr == null) {
            unassignedDownloadedECardExists = true;
          }
        }
        if (unassignedDownloadedECardExists) {
          FMilaClientUtility.showOkMessage(TEXTS.get("ApplicationName"), null, TEXTS.get("DownloadedECardsWithoutEntrySelected"));
        }
        for (Long raceNr : getRaceNrColumn().getSelectedValues()) {
          if (raceNr != null) {
            BEANS.get(IRaceService.class).validateAndPersistRace(raceNr);
          }
        }
        reloadPage();
      }
    }

    @Order(50.0)
    public class CreatePDFSplitTimesMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return Texts.get("CreatePDFMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        SplitTimesReport.openSplitTimesReport(getRaceNrColumn().getSelectedValues().toArray(new Long[0]));
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(true);
        for (Long raceNr : getRaceNrColumn().getSelectedValues()) {
          if (raceNr == null) {
            setEnabled(false);
            break;
          }
        }
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }
    }

    @Order(60.0)
    public class PrintSplitTimesWithPrinterChooserMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("PrintSplitTimesWithPrinterChooser");
      }

      @Override
      protected void execAction() throws ProcessingException {
        SplitTimesReport.printSplitTimesReport(getRaceNrColumn().getSelectedValues().toArray(new Long[0]), null);
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(true);
        for (Long raceNr : getRaceNrColumn().getSelectedValues()) {
          if (raceNr == null) {
            setEnabled(false);
            break;
          }
        }
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }
    }

    @Order(90.0)
    public class Separator2Menu extends AbstractSeparatorMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

    }

    @Order(100.0)
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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("DownloadedECards"), getECardColumn().getSelectedValues())) {
          DownloadedECardFormData formData = new DownloadedECardFormData();
          for (Long punchSessionNr : getTable().getPunchSessionNrColumn().getSelectedValues()) {
            formData.setPunchSessionNr(punchSessionNr);
            BEANS.get(IDownloadedECardProcessService.class).delete(formData);
          }
          reloadPage();
        }
      }
    }
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return DownloadedECardsSearchForm.class;
  }
}
