package com.rtiming.client.settings.clazz;

import java.util.Set;

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
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.settings.CodeForm;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.ISettingsOutlineService;

public class ClassesTablePage extends AbstractPageWithTable<ClassesTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Classes");
  }

  @Override
  protected IPage<?> execCreateChildPage(ITableRow row) throws ProcessingException {
    AgeTablePage childPage = new AgeTablePage(getTable().getClassUidColumn().getValue(row));
    return childPage;
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(ISettingsOutlineService.class).getClassTableData());
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public ShortcutColumn getShortcutColumn() {
      return getColumnSet().getColumnByClass(ShortcutColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.CLAZZ;
    }

    public AgeColumn getAgeColumn() {
      return getColumnSet().getColumnByClass(AgeColumn.class);
    }

    public ClassColumn getClassColumn() {
      return getColumnSet().getColumnByClass(ClassColumn.class);
    }

    public ClassUidColumn getClassUidColumn() {
      return getColumnSet().getColumnByClass(ClassUidColumn.class);
    }

    @Order(10.0)
    public class ClassUidColumn extends AbstractLongColumn {

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
    public class ClassColumn extends AbstractStringColumn {

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
        return 250;
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
        return 120;
      }
    }

    @Order(40.0)
    public class AgeColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Age");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
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
        CodeForm form = new CodeForm();
        form.setCodeType(ClassCodeType.ID);
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
        CodeForm form = new CodeForm();
        form.setCodeUid(getClassUidColumn().getSelectedValue());
        form.setCodeType(ClassCodeType.ID);
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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Classes"), getClassColumn().getSelectedValues())) {
          for (Long classUid : getClassUidColumn().getSelectedValues()) {
            CodeFormData formData = new CodeFormData();
            formData.setCodeUid(classUid);
            formData = BEANS.get(ICodeProcessService.class).load(formData);
            BEANS.get(ICodeProcessService.class).deleteCodeBox(formData.getMainBox());
          }
          reloadPage();
        }
      }
    }
  }
}
