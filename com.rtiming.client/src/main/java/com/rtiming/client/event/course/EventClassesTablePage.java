package com.rtiming.client.event.course;

import java.util.Set;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.HeaderCell;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.EmptyWorkaroundTablePage;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.event.EventClassForm;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

public class EventClassesTablePage extends AbstractPageWithTable<EventClassesTablePage.Table> implements IHelpEnabledPage {

  private final Long m_eventNr;
  private final Long m_parentClassUid;

  public EventClassesTablePage(Long eventNr, Long parentClassUid) {
    super();
    m_eventNr = eventNr;
    m_parentClassUid = parentClassUid;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Classes");
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      if (CompareUtility.notEquals(getTypeColumn().getSelectedValue(), ClassTypeCodeType.RelayCode.ID)) {
        return EditMenu.class;
      }
      return super.getConfiguredDefaultMenu();
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.CLAZZ;
    }

    public CourseColumn getCourseColumn() {
      return getColumnSet().getColumnByClass(CourseColumn.class);
    }

    public SortCodeColumn getSortCodeColumn() {
      return getColumnSet().getColumnByClass(SortCodeColumn.class);
    }

    public FeeGroupColumn getFeeGroupColumn() {
      return getColumnSet().getColumnByClass(FeeGroupColumn.class);
    }

    public CourseGenerationTypeColumn getCourseGenerationTypeColumn() {
      return getColumnSet().getColumnByClass(CourseGenerationTypeColumn.class);
    }

    public ClazzColumn getClazzColumn() {
      return getColumnSet().getColumnByClass(ClazzColumn.class);
    }

    public ParentColumn getParentColumn() {
      return getColumnSet().getColumnByClass(ParentColumn.class);
    }

    public TypeColumn getTypeColumn() {
      return getColumnSet().getColumnByClass(TypeColumn.class);
    }

    @Order(10.0)
    public class ParentColumn extends AbstractSmartColumn<Long> {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

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
        return 250;
      }
    }

    @Order(20.0)
    public class ClazzColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Class");
      }

      @Override
      protected void execDecorateHeaderCell(HeaderCell cell) throws ProcessingException {
        if (NumberUtility.nvl(getParentClassUid(), 0) == 0) {
          cell.setText(Texts.get("Class"));
        }
        else {
          cell.setText(Texts.get("Leg"));
        }
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ClassCodeType.class;

      }

      @Override
      protected int getConfiguredSortIndex() {
        return 2;
      }

      @Override
      protected boolean getConfiguredSummary() {
        return true;
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(30.0)
    public class TypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ClassTypeCodeType.class;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Type");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(40.0)
    public class CourseColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Course");
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }
    }

    @Order(50.0)
    public class FeeGroupColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("FeeGroup");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(60.0)
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
        return 150;
      }
    }

    @Order(70.0)
    public class CourseGenerationTypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("CourseSetting");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return CourseGenerationCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
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
        EventClassForm form = new EventClassForm();
        form.getEventField().setValue(getEventNr());
        form.getParentField().setValue(getParentClassUid());
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
        EventClassForm form = new EventClassForm();
        form.getEventField().setValue(getEventNr());
        form.getClazzField().setValue(getClazzColumn().getSelectedValue());
        form.getParentField().setValue(getParentClassUid());
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(40.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(50.0)
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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Classes"), getClazzColumn().getSelectedDisplayTexts())) {
          for (Long classUid : getClazzColumn().getSelectedValues()) {
            EventClassFormData formData = new EventClassFormData();
            formData.getEvent().setValue(getEventNr());
            formData.getClazz().setValue(classUid);
            BEANS.get(IEventClassProcessService.class).delete(formData);
          }
          reloadPage();
        }
      }
    }
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    if (getParentClassUid() == null && !ClassTypeCodeType.isLegClassType(getTable().getTypeColumn().getValue(row))) {
      return new EventClassesTablePage(getEventNr(), getTable().getClazzColumn().getValue(row));
    }
    else {
      // workaround since Scout cannot handle null
      return new EmptyWorkaroundTablePage();
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IEventsOutlineService.class).getEventClassTableData(getEventNr(), getParentClassUid()));
  }

  @FormData
  public Long getEventNr() {
    return m_eventNr;
  }

  @FormData
  public Long getParentClassUid() {
    return m_parentClassUid;
  }

}
