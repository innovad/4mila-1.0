package com.rtiming.client.settings.clazz;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtClassAgeKey;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.ISettingsOutlineService;
import com.rtiming.shared.settings.clazz.IAgeProcessService;

public class AgeTablePage extends AbstractPageWithTable<AgeTablePage.Table> implements IHelpEnabledPage {

  private final Long classUid;

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Age");
  }

  public AgeTablePage(Long classUid) {
    super();
    this.classUid = classUid;
  }

  public Long getClassUid() {
    return classUid;
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<RtClassAge> list = BEANS.get(ISettingsOutlineService.class).getAgeTableData(getClassUid());
    Object[][] table = new Object[list.size()][getTable().getColumnCount()];
    int k = 0;
    for (RtClassAge row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getClassAgeNrColumn().getColumnIndex()] = row.getId().getId();
      table[k][getTable().getFromColumn().getColumnIndex()] = row.getAgeFrom();
      table[k][getTable().getToColumn().getColumnIndex()] = row.getAgeTo();
      table[k][getTable().getSexColumn().getColumnIndex()] = row.getSexUid();
      k++;
    }
    importTableData(table);
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    public SexColumn getSexColumn() {
      return getColumnSet().getColumnByClass(SexColumn.class);
    }

    public FromColumn getFromColumn() {
      return getColumnSet().getColumnByClass(FromColumn.class);
    }

    public ToColumn getToColumn() {
      return getColumnSet().getColumnByClass(ToColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return AbstractIcons.CaretDown; // TODO MIG
    }

    public ClassAgeNrColumn getClassAgeNrColumn() {
      return getColumnSet().getColumnByClass(ClassAgeNrColumn.class);
    }

    @Order(10.0)
    public class ClassAgeNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(20.0)
    public class SexColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Sex");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return SexCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(30.0)
    public class FromColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("From");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }
    }

    @Order(40.0)
    public class ToColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("To");
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
        AgeForm form = new AgeForm();
        form.getClazzField().setValue(getClassUid());
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
        AgeForm form = new AgeForm();
        form.setKey(RtClassAgeKey.create(getTable().getClassAgeNrColumn().getSelectedValue()));
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(25.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(30.0)
    public class DeleteMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("DeleteMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Age"))) {
          RtClassAgeKey key = RtClassAgeKey.create(getTable().getClassAgeNrColumn().getSelectedValue());
          BEANS.get(IAgeProcessService.class).delete(key);
          reloadPage();
        }
      }
    }
  }
}
