package com.rtiming.client.event.course;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.FormData;
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
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlFormData;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.services.code.CourseForkTypeCodeType;

public class CourseControlsTablePage extends AbstractPageWithTable<CourseControlsTablePage.Table> implements IHelpEnabledPage {

  private Long m_courseNr;
  private final List<CourseControlRowData> courseControls;

  public CourseControlsTablePage(Long courseNr, List<CourseControlRowData> courseControls) {
    super();
    m_courseNr = courseNr;
    this.courseControls = courseControls;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Controls");
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    public ControlNrColumn getControlNrColumn() {
      return getColumnSet().getColumnByClass(ControlNrColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.CHECKBOXNO;
    }

    public CountLegColumn getCountLegColumn() {
      return getColumnSet().getColumnByClass(CountLegColumn.class);
    }

    public ControlTypeColumn getControlTypeColumn() {
      return getColumnSet().getColumnByClass(ControlTypeColumn.class);
    }

    public MandatoryControlColumn getMandatoryControlColumn() {
      return getColumnSet().getColumnByClass(MandatoryControlColumn.class);
    }

    public ForkMasterCourseControlNrColumn getForkMasterCourseControlNrColumn() {
      return getColumnSet().getColumnByClass(ForkMasterCourseControlNrColumn.class);
    }

    public ForkMasterCourseControlColumn getForkMasterCourseControlColumn() {
      return getColumnSet().getColumnByClass(ForkMasterCourseControlColumn.class);
    }

    public ForkVariantCodeColumn getForkVariantCodeColumn() {
      return getColumnSet().getColumnByClass(ForkVariantCodeColumn.class);
    }

    public SortCodeColumn getSortCodeColumn() {
      return getColumnSet().getColumnByClass(SortCodeColumn.class);
    }

    public CourseControlNrColumn getCourseControlNrColumn() {
      return getColumnSet().getColumnByClass(CourseControlNrColumn.class);
    }

    public ForkTypeColumn getForkTypeColumn() {
      return getColumnSet().getColumnByClass(ForkTypeColumn.class);
    }

    public ControlColumn getControlColumn() {
      return getColumnSet().getColumnByClass(ControlColumn.class);
    }

    @Order(10.0)
    public class CourseControlNrColumn extends AbstractLongColumn {
      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(20.0)
    public class ControlNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(30.0)
    public class ControlColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Control");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(40.0)
    public class SortCodeColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("SortCode");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(50.0)
    public class ControlTypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ControlTypeCodeType.class;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("ControlType");
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

    @Order(60.0)
    public class ForkTypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Fork");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return CourseForkTypeCodeType.class;
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(70.0)
    public class ForkMasterCourseControlNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

    @Order(80.0)
    public class ForkMasterCourseControlColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("LoopMasterControl");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(90.0)
    public class ForkVariantCodeColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Variant");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(100.0)
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

    @Order(110.0)
    public class MandatoryControlColumn extends AbstractBooleanColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("MandatoryControl");
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
        CourseControlForm form = new CourseControlForm();
        form.getCourseField().setValue(getCourseNr());
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
        CourseControlForm form = new CourseControlForm();
        form.setCourseControlNr(getCourseControlNrColumn().getSelectedValue());
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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Controls"), getControlColumn().getSelectedDisplayTexts())) {
          for (ITableRow row : getTable().getSelectedRows()) {
            CourseControlFormData formData = new CourseControlFormData();
            formData.setCourseControlNr(getCourseControlNrColumn().getValue(row));
            BEANS.get(ICourseControlProcessService.class).delete(formData);
          }
        }
        reloadPage();
      }
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<CourseControlRowData> list = null;
    if (courseControls == null) {
      list = BEANS.get(IEventsOutlineService.class).getCourseControlTableData(getCourseNr());
    }
    else {
      list = courseControls;
    }

    Object[][] table = new Object[list.size()][getTable().getColumnCount()];
    int k = 0;
    for (CourseControlRowData row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getCourseControlNrColumn().getColumnIndex()] = row.getCourseControlNr();
      table[k][getTable().getControlColumn().getColumnIndex()] = row.getControlNo();
      table[k][getTable().getControlNrColumn().getColumnIndex()] = row.getControlNr();
      table[k][getTable().getSortCodeColumn().getColumnIndex()] = row.getSortCode();
      table[k][getTable().getControlTypeColumn().getColumnIndex()] = row.getTypeUid();
      String sortCode = "";
      for (CourseControlRowData master : list) {
        if (CompareUtility.equals(master.getCourseControlNr(), row.getForkMasterCourseControlNr())) {
          sortCode = " - " + master.getSortCode();
          break;
        }
      }
      table[k][getTable().getForkTypeColumn().getColumnIndex()] = row.getLoopTypeUid();
      table[k][getTable().getForkMasterCourseControlColumn().getColumnIndex()] = StringUtility.emptyIfNull(row.getForkMasterCourseControlNo()) + sortCode;
      table[k][getTable().getForkMasterCourseControlNrColumn().getColumnIndex()] = row.getForkMasterCourseControlNr();
      table[k][getTable().getForkVariantCodeColumn().getColumnIndex()] = row.getForkVariantCode();
      table[k][getTable().getCountLegColumn().getColumnIndex()] = row.isCountLeg();
      table[k][getTable().getMandatoryControlColumn().getColumnIndex()] = row.isMandatory();
      k++;
    }
    importTableData(table);
  }

  @FormData
  public Long getCourseNr() {
    return m_courseNr;
  }

  @FormData
  public void setCourseNr(Long courseNr) {
    m_courseNr = courseNr;
  }
}
