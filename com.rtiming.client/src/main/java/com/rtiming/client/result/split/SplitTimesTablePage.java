package com.rtiming.client.result.split;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.HeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.basic.table.customizer.ITableCustomizer;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.ColorUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.result.AbstractControlColumn;
import com.rtiming.client.result.AbstractResultsTable;
import com.rtiming.client.result.ResultsTableUtility;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.race.TimePrecisionCodeType;
import com.rtiming.shared.results.IResultsOutlineService;
import com.rtiming.shared.results.ResultRowData;

public class SplitTimesTablePage extends AbstractPageWithTable<SplitTimesTablePage.Table> implements IHelpEnabledPage {

  private static final int SPLIT_INFO_SIZE = 3;

  private final Long m_clientNr;
  private final Long m_classUid;
  private final Long m_clubNr;
  private final Long m_courseNr;

  public SplitTimesTablePage(Long clientNr, Long classUid, Long courseNr, Long clubNr) {
    m_clientNr = clientNr;
    m_classUid = classUid;
    m_courseNr = courseNr;
    m_clubNr = clubNr;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("SplitTimes");
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Order(10.0)
  public class Table extends AbstractResultsTable {

    @Override
    protected ITableCustomizer createTableCustomizer() {
      return new SplitTimesTableCustomizer();
    }

    @Override
    protected void execInitTable() throws ProcessingException {
      super.execInitTable();
      setSplitTimesVisibility();
    }

    @Override
    protected void execResetColumns(Set<String> set) throws ProcessingException {
      super.execResetColumns(set);
      setSplitTimesVisibility();
    }

    private void setSplitTimesVisibility() {
      for (IColumn<?> c : getColumns()) {
        if (!(c instanceof RankColumn || c instanceof RunnerColumn || c instanceof TimeColumn)) {
          c.setVisible(false);
        }
      }
    }

    @Override
    protected void execContentChanged() throws ProcessingException {
      Long courseNr = SplitTimesTablePage.this.m_courseNr;
      Long timePrecisionUid = TimePrecisionCodeType.Precision1sCode.ID;
      if (courseNr == null && m_classUid != null) {
        EventClassFormData eventClass = new EventClassFormData();
        eventClass.getEvent().setValue(getEventNr());
        eventClass.getClazz().setValue(m_classUid);
        eventClass = BEANS.get(IEventClassProcessService.class).load(eventClass);
        courseNr = eventClass.getCourse().getValue();
        timePrecisionUid = eventClass.getTimePrecision().getValue();
      }
      // add split columns
      if (courseNr != null) {
        // load splits and controls for all races/runner
        Long winnerRaceNr = null;
        if (getTable().getRowCount() > 0 && CompareUtility.equals(getTable().getRaceStatusColumn().getValue(0), RaceStatusCodeType.OkCode.ID)) {
          winnerRaceNr = getTable().getRaceNrColumn().getValue(0);
        }
        Map<Long /* raceNr */, List<SplitTime>> data = SplitCalculator.calculateSplits(winnerRaceNr, timePrecisionUid, getTable().getRaceNrColumn().getValues().toArray(new Long[0]));

        // get winning course
        List<Long> winnerCourseControlNrs = new ArrayList<>();
        if (data.get(winnerRaceNr) != null) {
          for (SplitTime split : data.get(winnerRaceNr)) {
            Long courseControlNr = split.getControl().getCourseControlNr();
            if (courseControlNr != null) {
              winnerCourseControlNrs.add(courseControlNr);
            }
          }
        }

        // get all courses
        List<CourseControlRowData> winnerCourse = null;
        List<List<CourseControlRowData>> courses = BEANS.get(ICourseControlProcessService.class).getCourses(courseNr);
        for (List<CourseControlRowData> course : courses) {
          // compare courses
          List<Long> courseLongList = new ArrayList<>();
          for (CourseControlRowData control : course) {
            courseLongList.add(control.getCourseControlNr());
          }
          if (courseLongList.equals(winnerCourseControlNrs)) {
            winnerCourse = course;
            break;
          }
        }
        if (winnerCourse == null) {
          winnerCourse = courses.get(0); // default first if there is no winner
        }

        int firstControlCol = 0;
        // analyze location of first control column
        for (int k = 0; k < getTable().getColumnCount(); k++) {
          if (getTable().getColumns().get(k) instanceof AbstractControlColumn) {
            firstControlCol = k;
            break;
          }
        }
        // add one additional column for finish, control (not start)
        int controlCount = 1;
        String lastControlCount = null;
        for (int i = 0; i < winnerCourse.size(); i++) {
          String controlNo = winnerCourse.get(i).getControlNo();
          if (CompareUtility.equals(ControlTypeCodeType.StartCode.ID, winnerCourse.get(i).getTypeUid())) {
            lastControlCount = controlNo;
          }
          else {
            getTable().getColumns().get(firstControlCol + i).setDisplayable(true);
            getTable().getColumns().get(firstControlCol + i).setVisible(true);
            String text;
            if (CompareUtility.equals(winnerCourse.get(i).getTypeUid(), ControlTypeCodeType.ControlCode.ID)) {
              text = lastControlCount + "-" + controlCount + " (" + controlNo + ")";
              lastControlCount = String.valueOf(controlCount);
              controlCount++;
            }
            else {
              text = lastControlCount + "-" + controlNo;
            }
            ((HeaderCell) getTable().getHeaderCell(firstControlCol + i)).setText(text);
          }
        }

        // loop over all races/runners
        for (int k = 0; k < getTable().getRowCount(); k += SPLIT_INFO_SIZE) {
          Long raceNr = getTable().getRaceNrColumn().getValue(k);
          // background colors
          String color = "ffffff";
          if (k % 2 == 0) {
            color = "eeeeee";
          }
          getTable().getRow(k).setBackgroundColor(color);
          getTable().getRow(k + 1).setBackgroundColor(color);
          getTable().getRow(k + 2).setBackgroundColor(color);

          // for each runner, loop over its controls and split times
          for (int m = 0; m < winnerCourse.size(); m++) {
            if (data.get(raceNr) == null || m >= data.get(raceNr).size()) {
              continue;
            }
            SplitTime splitTime = data.get(raceNr).get(m);
            String legTime = splitTime.getLegTime().getLegTime();
            String overallTime = splitTime.getOverallTime().getOverallTime();
            AbstractControlColumn col = (AbstractControlColumn) getTable().getColumns().get(firstControlCol + m);
            // leg time
            String leg = legTime;
            if (splitTime.getLegTime().getLegRank() != null) {
              leg += " (" + splitTime.getLegTime().getLegRank() + ")";
            }
            col.setValue(k, leg);
            if (CompareUtility.equals(1L, splitTime.getLegTime().getLegRank())) {
              setCellBold(k, col);
            }

            // overall time
            String overall = overallTime;
            if (splitTime.getOverallTime().getOverallRank() != null) {
              overall += " (" + splitTime.getOverallTime().getOverallRank() + ")";
            }
            col.setValue(k + 1, overall);
            if (CompareUtility.equals(1L, splitTime.getOverallTime().getOverallRank())) {
              setCellBold(k + 1, col);
            }

            // third row
            String behind = "";
            if (splitTime.getLegTime().getTimeBehind() != null) {
              // data
              Long millis = splitTime.getLegTime().getTimeBehind();
              behind = String.format("%d:%02d", TimeUnit.MILLISECONDS.toMinutes(millis), TimeUnit.MILLISECONDS.toSeconds(millis) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millis)));

              // color
              if (splitTime.getLegTime().getTimeBehind() != null) {
                String htmlColor = calculateTimeBehindColor(splitTime.getLegTime().getTimeBehind(), splitTime.getLegTime().getLegTimeRaw() - splitTime.getLegTime().getTimeBehind());
                setCellColor(htmlColor, k, col);
              }
            }
            col.setValue(k + 2, behind);
          }
        }
      }
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<ResultRowData> list = BEANS.get(IResultsOutlineService.class).getResultTableData(m_clientNr, m_classUid, m_courseNr, m_clubNr, filter);
    Object[][] data = ResultsTableUtility.convertListToObjectArray(list, getTable());
    Object[][] expandedData = new Object[data.length * SPLIT_INFO_SIZE][];
    int k = 0;
    for (Object[] row : data) {
      expandedData[k] = row;
      expandedData[k + 1] = new Object[row.length];
      expandedData[k + 2] = new Object[row.length];
      k += SPLIT_INFO_SIZE;
    }
    importTableData(expandedData);
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return SingleEventSearchForm.class;
  }

  private Long getEventNr() {
    return ((SingleEventSearchForm) getSearchFormInternal()).getEventField().getValue();
  }

  private void setCellColor(String color, int row, AbstractControlColumn col) {
    Cell cell = (Cell) getTable().getCell(getTable().getRow(row), col);
    cell.setObserver(null); // required, otherwise Scout calls execContentChanged again and again
    cell.setBackgroundColor(color);

    cell = (Cell) getTable().getCell(getTable().getRow(row + 1), col);
    cell.setObserver(null); // required, otherwise Scout calls execContentChanged again and again
    cell.setBackgroundColor(color);

    cell = (Cell) getTable().getCell(getTable().getRow(row + 2), col);
    cell.setObserver(null); // required, otherwise Scout calls execContentChanged again and again
    cell.setBackgroundColor(color);
  }

  private String calculateTimeBehindColor(float timeBehind, long fastestTime) {
    float max = fastestTime * 1.5f; // fastest time plus 50%
    float green = 120; // hsb, red = 0;

    timeBehind = Math.min(timeBehind, max);
    float hsv = (green - ((timeBehind / max) * green)) / 360;

    int rgb = ColorUtility.hsb2rgb(hsv, 0.5f, 1f);
    Color rgbColor = new Color(rgb);
    String c = Integer.toHexString(rgbColor.getRGB() & 0x00ffffff); // filter alpha channel
    return c;
  }

  private void setCellBold(int row, AbstractControlColumn col) {
    Cell cell = (Cell) getTable().getCell(getTable().getRow(row), col);
    cell.setObserver(null); // required, otherwise Scout calls execContentChanged again and again
    cell.setFont(FontSpec.parse("bold"));
  }

}
