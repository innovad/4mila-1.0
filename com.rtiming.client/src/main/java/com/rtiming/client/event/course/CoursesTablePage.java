package com.rtiming.client.event.course;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.event.course.ICourseProcessService;

public class CoursesTablePage extends AbstractPageWithTable<CoursesTablePage.Table> implements IHelpEnabledPage {

  private Long m_eventNr;

  public CoursesTablePage(Long eventNr) {
    super();
    m_eventNr = eventNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Courses");
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.COURSE;
    }

    public EventNrColumn getEventNrColumn() {
      return getColumnSet().getColumnByClass(EventNrColumn.class);
    }

    public ShortcutColumn getShortcutColumn() {
      return getColumnSet().getColumnByClass(ShortcutColumn.class);
    }

    public LengthColumn getLengthColumn() {
      return getColumnSet().getColumnByClass(LengthColumn.class);
    }

    public ControlCountColumn getControlCountColumn() {
      return getColumnSet().getColumnByClass(ControlCountColumn.class);
    }

    public ClassesColumn getClassesColumn() {
      return getColumnSet().getColumnByClass(ClassesColumn.class);
    }

    public ClimbColumn getClimbColumn() {
      return getColumnSet().getColumnByClass(ClimbColumn.class);
    }

    public CourseNrColumn getCourseNrColumn() {
      return getColumnSet().getColumnByClass(CourseNrColumn.class);
    }

    @Order(10.0)
    public class CourseNrColumn extends AbstractLongColumn {

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

    @Order(30.0)
    public class ShortcutColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Shortcut");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(40.0)
    public class LengthColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Length");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(50.0)
    public class ClimbColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Climb");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(60.0)
    public class ControlCountColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Control");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(70.0)
    public class ClassesColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Classes");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
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
        CourseForm form = new CourseForm();
        form.setCourseNr(getCourseNrColumn().getSelectedValue());
        form.getEventField().setValue(getEventNr());
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
        CourseForm form = new CourseForm();
        form.setCourseNr(getCourseNrColumn().getSelectedValue());
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
    public class NewCoursesWithSelectedVariantsMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("NewCourseWithSelectedVariants");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() throws ProcessingException {
        CourseForm form = new CourseForm();
        form.getEventField().setValue(getEventNr());
        form.startNewCourseWithVariants(getCourseNrColumn().getSelectedValues().toArray(new Long[0]));
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(50.0)
    public class Separator2Menu extends AbstractSeparatorMenu {
      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }
    }

    @Order(60.0)
    public class DeleteMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("DeleteMenu");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Courses"), getShortcutColumn().getSelectedDisplayTexts())) {
          for (Long courseNr : getCourseNrColumn().getSelectedValues()) {
            CourseFormData course = new CourseFormData();
            course.setCourseNr(courseNr);
            BEANS.get(ICourseProcessService.class).delete(course, true);
          }
        }
        reloadPage();
      }
    }
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    Long courseNr = getTable().getCourseNrColumn().getValue(row);
    List<List<CourseControlRowData>> courses = BEANS.get(ICourseControlProcessService.class).getCourses(courseNr);
    if (courses.size() <= 1) {
      return new CourseControlsTablePage(courseNr, null);
    }
    else {
      return new CourseNodePage(courseNr);
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IEventsOutlineService.class).getCourseTableData(getEventNr()));
  }

  @FormData
  public Long getEventNr() {
    return m_eventNr;
  }

  @FormData
  public void setEventNr(Long eventNr) {
    m_eventNr = eventNr;
  }
}
