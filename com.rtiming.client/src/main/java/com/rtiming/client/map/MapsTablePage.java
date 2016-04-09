package com.rtiming.client.map;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
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
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dao.RtMap;
import com.rtiming.shared.dao.RtMapKey;
import com.rtiming.shared.event.EventMapFormData;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.map.IEventMapProcessService;
import com.rtiming.shared.map.IMapProcessService;

public class MapsTablePage extends AbstractPageWithTable<MapsTablePage.Table> implements IHelpEnabledPage {

  private final Long eventNr;

  public MapsTablePage(Long eventNr) {
    super();
    this.eventNr = eventNr;
  }

  public MapsTablePage() {
    super();
    this.eventNr = null;
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<RtMap> list = BEANS.get(IEventsOutlineService.class).getMapTableData(getEventNr());

    Object[][] table = new Object[list.size()][getTable().getColumnCount()];
    int k = 0;
    for (RtMap row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getMapNrColumn().getColumnIndex()] = row.getId().getId();
      table[k][getTable().getNameColumn().getColumnIndex()] = row.getName();
      if (row.getW() != null && row.getH() != null) {
        table[k][getTable().getPixelColumn().getColumnIndex()] = row.getW() + "x" + row.getH();
      }
      table[k][getTable().getScaleColumn().getColumnIndex()] = row.getScale();
      table[k][getTable().getX0Column().getColumnIndex()] = row.getNwX();
      table[k][getTable().getY0Column().getColumnIndex()] = row.getNwY();
      k++;
    }
    importTableData(table);
  }

  public Long getEventNr() {
    return eventNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Maps");
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    @Override
    protected Class<? extends IMenu> getConfiguredDefaultMenu() {
      return EditMenu.class;
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.COUNTRY;
    }

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
    }

    public ScaleColumn getScaleColumn() {
      return getColumnSet().getColumnByClass(ScaleColumn.class);
    }

    public X0Column getX0Column() {
      return getColumnSet().getColumnByClass(X0Column.class);
    }

    public Y0Column getY0Column() {
      return getColumnSet().getColumnByClass(Y0Column.class);
    }

    public PixelColumn getPixelColumn() {
      return getColumnSet().getColumnByClass(PixelColumn.class);
    }

    public MapNrColumn getMapNrColumn() {
      return getColumnSet().getColumnByClass(MapNrColumn.class);
    }

    @Order(10.0)
    public class MapNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(20.0)
    public class NameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return ScoutTexts.get("Name");
      }

      @Override
      protected int getConfiguredWidth() {
        return 200;
      }
    }

    @Order(30.0)
    public class ScaleColumn extends AbstractLongColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Scale");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(40.0)
    public class X0Column extends AbstractBigDecimalColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Longitude");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(50.0)
    public class Y0Column extends AbstractBigDecimalColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Latitude");
      }

      @Override
      protected int getConfiguredWidth() {
        return 100;
      }
    }

    @Order(60.0)
    public class PixelColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Pixel");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
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
        MapForm form = new MapForm();
        form.setNewEventNr(getEventNr());
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(20.0)
    public class AddMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return Texts.get("AddMenu");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() throws ProcessingException {
        EventMapForm form = new EventMapForm();
        form.getEventField().setValue(getEventNr());
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(getEventNr() != null);
      }
    }

    @Order(30.0)
    public class EditMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("EditMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        MapForm form = new MapForm();
        form.setMapKey(RtMapKey.create(getMapNrColumn().getSelectedValue()));
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          reloadPage();
        }
      }
    }

    @Order(35.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(40.0)
    public class RemoveMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return Texts.get("Remove");
      }

      @Override
      protected void execAction() throws ProcessingException {
        EventMapFormData formData = new EventMapFormData();
        formData.getEvent().setValue(getEventNr());
        formData.getMap().setValue(getMapNrColumn().getSelectedValue());
        BEANS.get(IEventMapProcessService.class).delete(formData);
        reloadPage();
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(getEventNr() != null);
      }
    }

    @Order(50.0)
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
        if (MessageBoxes.showDeleteConfirmationMessage(Texts.get("Maps"), getNameColumn().getSelectedValues())) {
          for (Long mapNr : getMapNrColumn().getSelectedValues()) {
            RtMapKey key = RtMapKey.create(mapNr);
            BEANS.get(IMapProcessService.class).delete(key);
          }
          reloadPage();
        }
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setVisible(getEventNr() == null);
      }

    }

  }
}
