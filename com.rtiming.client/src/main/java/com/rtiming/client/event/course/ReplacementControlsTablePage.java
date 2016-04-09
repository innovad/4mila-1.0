package com.rtiming.client.event.course;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.course.IReplacementControlProcessService;
import com.rtiming.shared.event.course.ReplacementControlFormData;
import com.rtiming.shared.event.course.ReplacementControlRowData;

public class ReplacementControlsTablePage extends AbstractPageWithTable<ReplacementControlsTablePage.Table> implements IHelpEnabledPage {

  private final Long m_controlNr;
  private final Long m_eventNr;

  public ReplacementControlsTablePage(Long controlNr, Long eventNr) {
    super();
    m_controlNr = controlNr;
    m_eventNr = eventNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("ReplacementControl");
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public ReplacementControlNrColumn getReplacementControlNrColumn() {
      return getColumnSet().getColumnByClass(ReplacementControlNrColumn.class);
    }

    public ReplacementControlColumn getReplacementControlColumn() {
      return getColumnSet().getColumnByClass(ReplacementControlColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.CHECKBOXNO;
    }

    public ControlColumn getControlColumn() {
      return getColumnSet().getColumnByClass(ControlColumn.class);
    }

    public ControlNrColumn getControlNrColumn() {
      return getColumnSet().getColumnByClass(ControlNrColumn.class);
    }

    @Order(10.0)
    public class ControlNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(20.0)
    public class ReplacementControlNrColumn extends AbstractLongColumn {

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
    public class ReplacementControlColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("ReplacementControl");
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
        ReplacementControlForm form = new ReplacementControlForm();
        form.getEventField().setValue(getEventNr());
        form.getControlField().setValue(getControlNr());
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(20.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(30.0)
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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("ReplacementControl"), getReplacementControlColumn().getSelectedValues())) {
          ReplacementControlFormData formData = new ReplacementControlFormData();
          formData.getEvent().setValue(getEventNr());
          formData.getControl().setValue(getControlNr());
          for (int i = 0; i < getTable().getSelectedRowCount(); i++) {
            formData.getReplacementControl().setValue(getTable().getReplacementControlNrColumn().getValue(i));
            BEANS.get(IReplacementControlProcessService.class).delete(formData);
          }
          reloadPage();
        }
      }
    }
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<ReplacementControlRowData> list = BEANS.get(IEventsOutlineService.class).getReplacementControlTableData(getControlNr());

    Object[][] result = new Object[list.size()][getTable().getColumnCount()];
    int k = 0;
    for (ReplacementControlRowData row : list) {
      result[k] = new Object[getTable().getColumnCount()];
      result[k][getTable().getControlNrColumn().getColumnIndex()] = row.getControlNr();
      result[k][getTable().getReplacementControlNrColumn().getColumnIndex()] = row.getReplacementControlNr();
      result[k][getTable().getControlColumn().getColumnIndex()] = row.getControlNo();
      result[k][getTable().getReplacementControlColumn().getColumnIndex()] = row.getReplacementControlNr();
      k++;
    }
    importTableData(result);
  }

  @FormData
  public Long getControlNr() {
    return m_controlNr;
  }

  @FormData
  public Long getEventNr() {
    return m_eventNr;
  }

}
