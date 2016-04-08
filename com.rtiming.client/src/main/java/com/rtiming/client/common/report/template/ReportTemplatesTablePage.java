package com.rtiming.client.common.report.template;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
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
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.report.template.IReportTemplateProcessService;
import com.rtiming.shared.dao.RtReportTemplate;
import com.rtiming.shared.dao.RtReportTemplateFile;
import com.rtiming.shared.dao.RtReportTemplateKey;
import com.rtiming.shared.event.EventLookupCall;
import com.rtiming.shared.services.code.ReportTypeCodeType;
import com.rtiming.shared.settings.ISettingsOutlineService;

public class ReportTemplatesTablePage extends AbstractPageWithTable<ReportTemplatesTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("ReportTemplates");
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    return new ReportTemplateColumnsTablePage(getTable().getReportTypeColumn().getValue(row));
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<RtReportTemplate> list = BEANS.get(ISettingsOutlineService.class).getReportTemplateTableData();
    Object[][] table = new Object[list.size()][getTable().getColumnCount()];
    int k = 0;
    for (RtReportTemplate row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getActiveColumn().getColumnIndex()] = row.getActive();
      table[k][getTable().getEventColumn().getColumnIndex()] = row.getEventNr();
      table[k][getTable().getReportTemplateNrColumn().getColumnIndex()] = row.getId().getId();
      Collection<Object> fileNames = new HashSet<>();
      for (RtReportTemplateFile file : row.getTemplateFiles()) {
        if (!StringUtility.isNullOrEmpty(file.getTemplateFileName())) {
          fileNames.add(file.getTemplateFileName());
        }
      }
      table[k][getTable().getReportTemplateColumn().getColumnIndex()] = StringUtility.collectionToString(fileNames);
      table[k][getTable().getReportTypeColumn().getColumnIndex()] = row.getTypeUid();
      table[k][getTable().getShortcutColumn().getColumnIndex()] = row.getShortcut();
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

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.FILE;
    }

    public EventColumn getEventColumn() {
      return getColumnSet().getColumnByClass(EventColumn.class);
    }

    public ReportTemplateNrColumn getReportTemplateNrColumn() {
      return getColumnSet().getColumnByClass(ReportTemplateNrColumn.class);
    }

    public ReportTemplateColumn getReportTemplateColumn() {
      return getColumnSet().getColumnByClass(ReportTemplateColumn.class);
    }

    public ShortcutColumn getShortcutColumn() {
      return getColumnSet().getColumnByClass(ShortcutColumn.class);
    }

    public ActiveColumn getActiveColumn() {
      return getColumnSet().getColumnByClass(ActiveColumn.class);
    }

    public ReportTypeColumn getReportTypeColumn() {
      return getColumnSet().getColumnByClass(ReportTypeColumn.class);
    }

    @Order(10.0)
    public class ReportTemplateNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("ReportTemplate");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(20.0)
    public class ShortcutColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Shortcut");
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
    public class ReportTemplateColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("ReportTemplate");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(40.0)
    public class ReportTypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("ReportType");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ReportTypeCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(50.0)
    public class EventColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Event");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return EventLookupCall.class;
      }

    }

    @Order(60.0)
    public class ActiveColumn extends AbstractBooleanColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Active");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(10.0)
    public class NewMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("NewButton");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() throws ProcessingException {
        ReportTemplateForm form = new ReportTemplateForm();
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
        return TEXTS.get("EditMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        ReportTemplateForm form = new ReportTemplateForm();
        form.setKey(RtReportTemplateKey.create(getReportTemplateNrColumn().getSelectedValue()));
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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("ReportTemplates"), getReportTemplateColumn().getSelectedValues())) {
          for (Long reportTemplateNr : getReportTemplateNrColumn().getSelectedValues()) {
            RtReportTemplateKey key = RtReportTemplateKey.create(reportTemplateNr);
            BEANS.get(IReportTemplateProcessService.class).delete(key);
          }
          reloadPage();
        }
      }

    }
  }
}
