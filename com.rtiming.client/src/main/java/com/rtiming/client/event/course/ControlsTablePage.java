package com.rtiming.client.event.course;

import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
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
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.ControlsSearchFormData;
import com.rtiming.shared.event.course.IControlProcessService;

public class ControlsTablePage extends AbstractPageWithTable<ControlsTablePage.Table> implements IHelpEnabledPage {

  private final Long eventNr;

  public ControlsTablePage(Long eventNr) {
    super();
    this.eventNr = eventNr;
  }

  public Long getEventNr() {
    return eventNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return ControlsSearchForm.class;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Controls");
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    return new ReplacementControlsTablePage(getTable().getControlNrColumn().getValue(row), getEventNr());
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    importTableData(BEANS.get(IEventsOutlineService.class).getControlTableData(getEventNr(), (ControlsSearchFormData) filter.getFormData()));
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public ControlNrColumn getControlNrColumn() {
      return getColumnSet().getColumnByClass(ControlNrColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.CHECKBOXNO;
    }

    public TypeColumn getTypeColumn() {
      return getColumnSet().getColumnByClass(TypeColumn.class);
    }

    public PositionXColumn getPositionXColumn() {
      return getColumnSet().getColumnByClass(PositionXColumn.class);
    }

    public PositionYColumn getPositionYColumn() {
      return getColumnSet().getColumnByClass(PositionYColumn.class);
    }

    public ReplacementControlsColumn getReplacementControlsColumn() {
      return getColumnSet().getColumnByClass(ReplacementControlsColumn.class);
    }

    public LongitudeColumn getLongitudeColumn() {
      return getColumnSet().getColumnByClass(LongitudeColumn.class);
    }

    public LatitudeColumn getLatitudeColumn() {
      return getColumnSet().getColumnByClass(LatitudeColumn.class);
    }

    public ControlColumn getControlColumn() {
      return getColumnSet().getColumnByClass(ControlColumn.class);
    }

    @Order(10.0)
    public class ControlNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(20.0)
    public class ControlColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Control");
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

      @Override
      protected int getConfiguredWidth() {
        return 70;
      }
    }

    @Order(30.0)
    public class TypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Type");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return ControlTypeCodeType.class;

      }

      @Override
      protected int getConfiguredWidth() {
        return 65;
      }
    }

    @Order(40.0)
    public class PositionXColumn extends AbstractBigDecimalColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("MapPositionX");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(50.0)
    public class PositionYColumn extends AbstractBigDecimalColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("MapPositionY");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(60.0)
    public class LongitudeColumn extends AbstractBigDecimalColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("GeographicLongitude");
      }

      @Override
      protected int getConfiguredMaxFractionDigits() {
        return 6;
      }

      @Override
      protected int getConfiguredMinFractionDigits() {
        return 6;
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(70.0)
    public class LatitudeColumn extends AbstractBigDecimalColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("GeographicLatitude");
      }

      @Override
      protected int getConfiguredMaxFractionDigits() {
        return 6;
      }

      @Override
      protected int getConfiguredMinFractionDigits() {
        return 6;
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(80.0)
    public class ReplacementControlsColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("ReplacementControls");
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
        ControlForm form = new ControlForm();
        form.getEventField().setValue(eventNr);
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
        ControlForm form = new ControlForm();
        form.setControlNr(getControlNrColumn().getSelectedValue());
        form.getEventField().setValue(eventNr);
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
    public class CalculateLongitudeLatitudeFromLocalPositionMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("CalculateLongitudeLatitudeFromLocalPosition");
      }

      @Override
      protected void execAction() throws ProcessingException {
        BEANS.get(IControlProcessService.class).georeferenceFromLocalPosition(getTable().getControlNrColumn().getSelectedValues().toArray(new Long[0]), eventNr);
        reloadPage();
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
          for (Long controlNr : getControlNrColumn().getSelectedValues()) {
            ControlFormData control = new ControlFormData();
            control.setControlNr(controlNr);
            control.getEvent().setValue(getEventNr());
            BEANS.get(IControlProcessService.class).delete(control);
          }
        }
        reloadPage();
      }
    }
  }
}
