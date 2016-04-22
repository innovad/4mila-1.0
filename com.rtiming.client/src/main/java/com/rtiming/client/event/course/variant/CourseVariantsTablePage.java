package com.rtiming.client.event.course.variant;

import java.util.List;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractIntegerColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.event.course.CourseControlsTablePage;
import com.rtiming.shared.Icons;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.event.course.ICourseControlProcessService;

public class CourseVariantsTablePage extends AbstractPageWithTable<CourseVariantsTablePage.Table> implements IHelpEnabledPage {

  private final Long courseNr;

  public CourseVariantsTablePage(Long courseNr) {
    super();
    this.courseNr = courseNr;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Variants");
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.COURSE;
    }

    public ControlsColumn getControlsColumn() {
      return getColumnSet().getColumnByClass(ControlsColumn.class);
    }

    public VariantColumn getVariantColumn() {
      return getColumnSet().getColumnByClass(VariantColumn.class);
    }

    public VariantCountNrColumn getVariantCountNrColumn() {
      return getColumnSet().getColumnByClass(VariantCountNrColumn.class);
    }

    @Order(10.0)
    public class VariantCountNrColumn extends AbstractIntegerColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Variant");
      }
    }

    @Order(20.0)
    public class VariantColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Variant");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(30.0)
    public class ControlsColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Controls");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<List<CourseControlRowData>> list = BEANS.get(ICourseControlProcessService.class).getCourses(courseNr);
    Object[][] data = new Object[list.size()][3];
    int i = 0;
    for (List<CourseControlRowData> row : list) {
      data[i][0] = list.indexOf(row);
      data[i][1] = TEXTS.get("Variant") + " " + (list.indexOf(row) + 1);
      data[i][2] = row.size();
      i++;
    }
    importTableData(data);
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) throws ProcessingException {
    List<List<CourseControlRowData>> list = BEANS.get(ICourseControlProcessService.class).getCourses(courseNr);
    return new CourseControlsTablePage(courseNr, list.get(getTable().getVariantCountNrColumn().getValue(row)));
  }

}
