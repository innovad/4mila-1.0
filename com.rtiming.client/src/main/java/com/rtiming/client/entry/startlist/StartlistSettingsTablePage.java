package com.rtiming.client.entry.startlist;

import java.math.BigInteger;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.ColorUtility;
import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.HeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.IHeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.basic.table.customizer.ITableCustomizer;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.common.ui.columns.AbstractTimeWithSecondsColumn;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.entry.IRegistrationsOutlineService;
import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.entry.startlist.IStartlistService;
import com.rtiming.shared.entry.startlist.IStartlistSettingProcessService;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData;
import com.rtiming.shared.entry.startlist.StartlistSettingRowData;
import com.rtiming.shared.entry.startlist.StartlistSettingUtility;
import com.rtiming.shared.entry.startlist.StartlistTypeCodeType;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;

public class StartlistSettingsTablePage extends AbstractPageWithTable<StartlistSettingsTablePage.Table> implements IHelpEnabledPage {

  private final Long eventNr;

  public StartlistSettingsTablePage(Long eventNr) {
    super();
    this.eventNr = eventNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("StartlistSettings");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<StartlistSettingRowData> list = BEANS.get(IRegistrationsOutlineService.class).getStartlistSettingTableData(getEventNr());
    Object[][] table = new Object[list.size()][getTable().getColumnCount()];

    int k = 0;
    for (StartlistSettingRowData row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getStartlistSettingNrColumn().getColumnIndex()] = row.getStartlistSettingNr();
      table[k][getTable().getBibNoOrderUidColumn().getColumnIndex()] = row.getBibNoOrderUid();
      table[k][getTable().getClazzUidColumn().getColumnIndex()] = row.getClazzUid();
      table[k][getTable().getClazzTypeColumn().getColumnIndex()] = row.getClazzTypeUid();
      table[k][getTable().getCourseColumn().getColumnIndex()] = row.getCourse();
      table[k][getTable().getCourseNrColumn().getColumnIndex()] = row.getCourseNr();
      table[k][getTable().getSameClassesColumn().getColumnIndex()] = row.getSameClasses();
      table[k][getTable().getTypeUidColumn().getColumnIndex()] = row.getTypeUid();
      table[k][getTable().getFirstStartColumn().getColumnIndex()] = row.getFirstStart();
      table[k][getTable().getLastStartColumn().getColumnIndex()] = row.getLastStart();
      table[k][getTable().getParticipationCountColumn().getColumnIndex()] = row.getParticipationCount();
      table[k][getTable().getIntervalColumn().getColumnIndex()] = row.getInterval();
      table[k][getTable().getVacantColumn().getColumnIndex()] = row.getVacant();
      k++;
    }
    importTableData(table);
  }

  public Long getEventNr() {
    return eventNr;
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    @Override
    protected void execDecorateRow(ITableRow row) throws ProcessingException {
      if (CompareUtility.equals(getTypeUidColumn().getValue(row), StartlistTypeCodeType.StartlistNoneCode.ID)) {
        row.setForegroundColor(FMilaUtility.COLOR_LIGHT_GREY);
      }
    }

    @Override
    protected ITableCustomizer createTableCustomizer() {
      return new StartlistTableCustomizer();
    }

    @Override
    protected void execContentChanged() throws ProcessingException {
      try {
        setTableChanging(true);

        // Analyze Settings
        Date minFirstStart = null;
        for (int k = 0; k < getTable().getRowCount(); k++) {
          if (getFirstStartColumn().getValue(k) != null) {
            if (minFirstStart == null || minFirstStart.after(getFirstStartColumn().getValue(k))) {
              minFirstStart = getFirstStartColumn().getValue(k);
            }
          }
        }
        if (minFirstStart == null) {
          minFirstStart = BEANS.get(IEventProcessService.class).getZeroTime(getEventNr());
        }

        // Calculate Interval GCD with BigInteger.gcd method
        BigInteger minStartInterval = BigInteger.ZERO;
        for (int k = 0; k < getTable().getRowCount(); k++) {
          if (getTypeUidColumn().getValue(k) != StartlistTypeCodeType.StartlistNoneCode.ID) {
            Long interval = getIntervalColumn().getValue(k);
            long gcd = 0;
            Date firstStart = getFirstStartColumn().getValue(k);
            if ((NumberUtility.nvl(interval, 0) != 0) && firstStart != null) {
              long delta = firstStart.getTime() - minFirstStart.getTime();
              delta = Math.abs(delta);
              gcd = delta % (interval * 1000);
              if (gcd == 0) {
                gcd = interval * 1000;
              }
            }
            minStartInterval = minStartInterval.gcd(BigInteger.valueOf(gcd));
          }
        }
        minStartInterval = minStartInterval.divide(BigInteger.valueOf(1000));

        // Set Header
        Date headerDate = minFirstStart;
        for (IColumn c : getColumns()) {
          if (c instanceof AbstractStartlistPreviewColumn) {
            // set header cell
            IHeaderCell h = c.getHeaderCell();
            ((HeaderCell) h).setText(DateUtility.format(headerDate, FMilaUtility.DEFAULT_TIME_FORMAT_HMS));
            ((AbstractStartlistPreviewColumn) c).setDate(headerDate);
            headerDate = FMilaUtility.addSeconds(headerDate, minStartInterval.intValue());
          }
        }

        Date lastStarttime = minFirstStart;
        for (ITableRow row : getTable().getRows()) {
          Date firstStart = getFirstStartColumn().getValue(row);
          Long interval = NumberUtility.nvl(getIntervalColumn().getValue(row), 0L);
          long startlistSettingParticipationCount = NumberUtility.nvl(getParticipationCountColumn().getValue(row), 0) + NumberUtility.nvl(getVacantColumn().getValue(row), 0);

          // write planned starts
          if (firstStart != null) {
            int k = 0;
            int participationCount = 0;
            int nextBibNo = 1;
            Long bibNo = getBibNoFromColumn().getValue(row); // first bib no.
            if (CompareUtility.equals(getBibNoOrderUidColumn().getValue(row), BibNoOrderCodeType.DescendingCode.ID)) {
              bibNo += Math.min(getParticipationCountColumn().getValue(row) - 1, 0);
              nextBibNo = -1;
            }
            long counter = 1;
            Long delta = firstStart.getTime() - minFirstStart.getTime();
            for (IColumn c : getColumns()) {
              if (c instanceof AbstractStartlistPreviewColumn && startlistSettingParticipationCount > participationCount) {
                Date columnStarttime = FMilaUtility.addSeconds(minFirstStart, minStartInterval.intValue() * k);
                if (columnStarttime.compareTo(firstStart) >= 0) {
                  if ((interval != 0 && ((minFirstStart.getTime() - columnStarttime.getTime()) + delta) % (interval * 1000) == 0)) {
                    participationCount++;
                    lastStarttime = DateUtility.max(columnStarttime, lastStarttime);
                    formatStartCell(c, row, columnStarttime, bibNo, counter++);
                    bibNo = bibNo != null ? bibNo + nextBibNo : null;
                  }
                  else if (interval == 0 && columnStarttime.equals(firstStart)) {
                    participationCount++;
                    lastStarttime = DateUtility.max(columnStarttime, lastStarttime);
                    formatStartCell(c, row, columnStarttime, bibNo, counter++);
                    bibNo = bibNo != null ? bibNo + nextBibNo : null;
                  }
                }
                k++;
              }
            }
          }
        }

        // hide last columns
        boolean rowHeightUpdated = false;
        for (IColumn c : getColumns()) {
          if (c instanceof AbstractStartlistPreviewColumn) {
            if (!rowHeightUpdated && ((AbstractStartlistPreviewColumn) c).getDate().equals(minFirstStart)) {
              rowHeightUpdated = true;
              for (int k = 0; k < getTable().getRowCount(); k++) {
                if (StringUtility.isNullOrEmpty(((AbstractStartlistPreviewColumn) c).getValue(k))) {
                  ((AbstractStartlistPreviewColumn) c).setValue(k, FMilaUtility.LINE_SEPARATOR + FMilaUtility.LINE_SEPARATOR);
                }
              }
            }
            if (lastStarttime.compareTo(((AbstractStartlistPreviewColumn) c).getDate()) >= 0 && minStartInterval.compareTo(BigInteger.ZERO) != 0) {
              c.setDisplayable(true);
              c.setVisible(true);
            }
            else {
              c.setDisplayable(false);
              c.setVisible(false);
            }
          }
        }

      }
      finally {
        setTableChanging(false);
      }
    }

    private void formatStartCell(IColumn c, ITableRow row, Date columnStarttime, Long bibNo, long counter) throws ProcessingException {
      String string = DateUtility.format(columnStarttime, FMilaUtility.DEFAULT_TIME_FORMAT_HMS);
      string += FMilaUtility.LINE_SEPARATOR + counter + (bibNo != null ? " (" + bibNo + ")" : "");
      ((AbstractStartlistPreviewColumn) c).setValue(row, string);
      ((Cell) getTable().getCell(row.getRowIndex(), c.getColumnIndex())).setBackgroundColor(ColorUtility.RED);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.TIMEFIELDTIME;
    }

    public ParticipationCountColumn getParticipationCountColumn() {
      return getColumnSet().getColumnByClass(ParticipationCountColumn.class);
    }

    public BibNoOrderUidColumn getBibNoOrderUidColumn() {
      return getColumnSet().getColumnByClass(BibNoOrderUidColumn.class);
    }

    public VacantColumn getVacantColumn() {
      return getColumnSet().getColumnByClass(VacantColumn.class);
    }

    public ClazzTypeColumn getClazzTypeColumn() {
      return getColumnSet().getColumnByClass(ClazzTypeColumn.class);
    }

    public SameClassesColumn getSameClassesColumn() {
      return getColumnSet().getColumnByClass(SameClassesColumn.class);
    }

    public BibNoFromColumn getBibNoFromColumn() {
      return getColumnSet().getColumnByClass(BibNoFromColumn.class);
    }

    public ClazzUidColumn getClazzUidColumn() {
      return getColumnSet().getColumnByClass(ClazzUidColumn.class);
    }

    public CourseColumn getCourseColumn() {
      return getColumnSet().getColumnByClass(CourseColumn.class);
    }

    public CourseNrColumn getCourseNrColumn() {
      return getColumnSet().getColumnByClass(CourseNrColumn.class);
    }

    public FirstStartColumn getFirstStartColumn() {
      return getColumnSet().getColumnByClass(FirstStartColumn.class);
    }

    public IntervalColumn getIntervalColumn() {
      return getColumnSet().getColumnByClass(IntervalColumn.class);
    }

    public LastStartColumn getLastStartColumn() {
      return getColumnSet().getColumnByClass(LastStartColumn.class);
    }

    public StartlistSettingNrColumn getStartlistSettingNrColumn() {
      return getColumnSet().getColumnByClass(StartlistSettingNrColumn.class);
    }

    public TypeUidColumn getTypeUidColumn() {
      return getColumnSet().getColumnByClass(TypeUidColumn.class);
    }

    @Order(10.0)
    public class StartlistSettingNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(20.0)
    public class BibNoOrderUidColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(30.0)
    public class ClazzUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ClassCodeType.class;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Class");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(40.0)
    public class ClazzTypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("ClassType");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ClassTypeCodeType.class;

      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(50.0)
    public class CourseColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Course");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(60.0)
    public class SameClassesColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Classes");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(70.0)
    public class CourseNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(80.0)
    public class TypeUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return StartlistTypeCodeType.class;

      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("StartlistType");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(90.0)
    public class FirstStartColumn extends AbstractTimeWithSecondsColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("FirstStart");
      }

    }

    @Order(100.0)
    public class LastStartColumn extends AbstractTimeWithSecondsColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("LastStart");
      }

    }

    @Order(110.0)
    public class ParticipationCountColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Entries");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(120.0)
    public class IntervalColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredEditable() {
        return true;
      }

// TODO MIG      
//      @Override
//      protected boolean execIsEditable(ITableRow row) throws ProcessingException {
//        super.execIsEditable(row);
//        return (getStartlistSettingNrColumn().getValue(row) != null && getTypeUidColumn().getValue(row) == StartlistTypeCodeType.DrawingCode.ID);
//      }

      @Override
      protected void execCompleteEdit(ITableRow row, IFormField editingField) throws ProcessingException {
        StartlistSettingFormData formData = new StartlistSettingFormData();
        formData.setStartlistSettingNr(getStartlistSettingNrColumn().getValue(row));
        formData = BEANS.get(IStartlistSettingProcessService.class).load(formData);
        formData.getStartInterval().setValue(((AbstractLongField) editingField).getValue());
        BEANS.get(IStartlistSettingProcessService.class).store(formData);
        reloadPage();
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Interval");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(130.0)
    public class BibNoFromColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("BibNumber");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(140.0)
    public class VacantColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Vacant");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
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
        StartlistSettingForm form = new StartlistSettingForm();
        if (getStartlistSettingNrColumn().getSelectedValue() != null) {
          // edit existing settings
          form.setStartlistSettingNr(getTable().getStartlistSettingNrColumn().getSelectedValue());
          form.setParticipationCount(NumberUtility.nvl(getParticipationCountColumn().getSelectedValue(), 0));
          form.setEventNr(getEventNr());
          form.startModify();
          form.waitFor();
        }
        else {
          // create new settings
          form.setNewClassUid(getClazzUidColumn().getSelectedValue());
          form.setParticipationCount(NumberUtility.nvl(getParticipationCountColumn().getSelectedValue(), 0));
          form.setEventNr(getEventNr());
          form.startNew();
          form.waitFor();
        }
        if (form.isFormStored()) {
          reloadPage();
        }
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        if (getStartlistSettingNrColumn().getSelectedValue() == null) {
          setText(ScoutTexts.get("NewButton"));
        }
        else {
          setText(ScoutTexts.get("EditMenu"));
        }
      }
    }

    @Order(20.0)
    public class ApplySameStartlistSettingMenu extends AbstractMenu {

      private Long masterStartlistSettingNr = null;

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("ApplySameStartlistSetting");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (masterStartlistSettingNr != null) {
          for (Long classUid : getTable().getClazzUidColumn().getSelectedValues()) {
            EventClassFormData clazz = new EventClassFormData();
            clazz.getEvent().setValue(getEventNr());
            clazz.getClazz().setValue(classUid);
            clazz = BEANS.get(IEventClassProcessService.class).load(clazz);
            clazz.setStartlistSettingNr(masterStartlistSettingNr);
            BEANS.get(IEventClassProcessService.class).store(clazz);
          }
        }
        reloadPage();
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(true);
        masterStartlistSettingNr = null;

        // check startlistSettingNr
        for (Long startlistSettingNr : getStartlistSettingNrColumn().getSelectedValues()) {
          if (masterStartlistSettingNr == null && startlistSettingNr != null) {
            // this is the first startlistSettingNr
            masterStartlistSettingNr = startlistSettingNr;
          }
          else if (masterStartlistSettingNr != null && startlistSettingNr != null && startlistSettingNr.longValue() != masterStartlistSettingNr.longValue()) {
            // more than one startlistSettingNr is selected, set disabled
            setEnabled(false);
          }
        }
        if (masterStartlistSettingNr == null) {
          // no startlistSettingNr is selected, set disabled
          setEnabled(false);
        }

        // check courseNr
        Long masterCourseNr = null;
        for (Long courseNr : getCourseNrColumn().getSelectedValues()) {
          if (courseNr != null && masterCourseNr == null) {
            // this is the first courseNr
            masterCourseNr = courseNr;
          }
          else if (courseNr != null && masterCourseNr != null && courseNr.longValue() != masterCourseNr.longValue()) {
            // more than one courseNr is selected, set disabled
            setEnabled(false);
          }
        }
        if (masterCourseNr == null) {
          setEnabled(false);
        }

        // check multi-select
        if (getCourseNrColumn().getSelectedValues().size() <= 1) {
          setEnabled(false);
        }
      }
    }

    @Order(30.0)
    public class SetFirstStarttimeMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return Texts.get("SetFirstStarttime");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (getContextColumn() instanceof AbstractStartlistPreviewColumn) {
          StartlistSettingFormData formData = new StartlistSettingFormData();
          formData.setStartlistSettingNr(getStartlistSettingNrColumn().getSelectedValue());
          formData = BEANS.get(IStartlistSettingProcessService.class).load(formData);
          formData.getFirstStart().setValue(((AbstractStartlistPreviewColumn) getContextColumn()).getDate());
          BEANS.get(IStartlistSettingProcessService.class).store(formData);
          reloadPage();
        }
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(true);
        if (getStartlistSettingNrColumn().getSelectedValue() == null) {
          setVisible(false);
        }
        else if (!(getContextColumn() instanceof AbstractStartlistPreviewColumn)) {
          setVisible(false);
        }
        else {
          setText(Texts.get("SetFirstStarttime", DateUtility.format(((AbstractStartlistPreviewColumn) getContextColumn()).getDate(), FMilaUtility.DEFAULT_TIME_FORMAT_HMS)));
        }
      }
    }

    @Order(40.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

    }

    @Order(50.0)
    public class CreateStartlistMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("CreateStartlist");
      }

      @Override
      protected void execAction() throws ProcessingException {
        Long[] selectedNrs = getStartlistSettingNrColumn().getSelectedValues().toArray(new Long[0]);
        IStartlistService startlistService = BEANS.get(IStartlistService.class);
        boolean exec = true;
        boolean allSelected = StartlistSettingUtility.checkIfAllExistingStartlistSettingNrsAreSelected(selectedNrs, getStartlistSettingNrColumn().getValues().toArray(new Long[0]));

        if (!allSelected) {
          boolean registrationOptionSelected = StartlistSettingUtility.checkIfStartlistSettingsContainRegistrationOption(selectedNrs);

          if (registrationOptionSelected) {
            IMessageBox mbox = FMilaClientUtility.createMessageBox(Texts.get("ApplicationName"), null, Texts.get("StartlistSettingsRegistrationOptionsWarning"), Texts.get("CreateStartlist"), null, ScoutTexts.get("CancelButton"));
            exec = mbox.show() == MessageBox.YES_OPTION;
          }
        }

        if (exec) {
          if (startlistService.existsRaceWithStartTime(selectedNrs)) {
            IMessageBox mbox = FMilaClientUtility.createMessageBox(Texts.get("ApplicationName"), null, Texts.get("StartlistSettingOverwriteWarning"), Texts.get("CreateStartlist"), null, ScoutTexts.get("CancelButton"));
            exec = mbox.show() == MessageBox.YES_OPTION;
          }
          if (exec) {
            startlistService.createStartlists(selectedNrs);
          }
        }
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(true);
        for (Long startlistSettingNr : getStartlistSettingNrColumn().getSelectedValues()) {
          if (startlistSettingNr == null) {
            setEnabled(false);
          }
        }
      }
    }

    @Order(60.0)
    public class Separator2Menu extends AbstractSeparatorMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

    }

    @Order(70.0)
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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("StartlistSettings"), getClazzUidColumn().getSelectedDisplayTexts())) {
          for (Long startlistSettingNr : getStartlistSettingNrColumn().getSelectedValues()) {
            StartlistSettingFormData formData = new StartlistSettingFormData();
            formData.setStartlistSettingNr(startlistSettingNr);
            BEANS.get(IStartlistSettingProcessService.class).delete(formData);
          }
          reloadPage();
        }
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(true);
        for (Long startlistSettingNr : getStartlistSettingNrColumn().getSelectedValues()) {
          if (startlistSettingNr == null) {
            setEnabled(false);
          }
        }
      }
    }

  }
}
