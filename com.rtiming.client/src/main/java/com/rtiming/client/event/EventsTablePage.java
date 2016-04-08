package com.rtiming.client.event;

import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractDateColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Platform;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.common.infodisplay.InfoDisplayIdleJob;
import com.rtiming.client.common.infodisplay.InfoDisplayUtility;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.common.ui.columns.AbstractDateTimeWithSecondsColumn;
import com.rtiming.client.common.ui.desktop.Desktop;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.IDatabaseService;
import com.rtiming.shared.event.EventRowData;
import com.rtiming.shared.event.EventTypeCodeType;
import com.rtiming.shared.event.EventsSearchFormData;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.settings.IDefaultProcessService;

public class EventsTablePage extends AbstractPageWithTable<EventsTablePage.Table> implements IHelpEnabledPage {

  private final Long clientNr;

  public EventsTablePage(Long clientNr) {
    this.clientNr = clientNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Events");
  }

  @Override
  protected IPage execCreateChildPage(ITableRow row) throws ProcessingException {
    EventNodePage childPage = new EventNodePage(getTable().getEventNrColumn().getValue(row));
    return childPage;
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<EventRowData> list = BEANS.get(IEventsOutlineService.class).getEventTableData(clientNr, (EventsSearchFormData) getSearchFilter().getFormData());
    Object[][] table = new Object[list.size()][getTable().getColumnCount()];

    int k = 0;
    for (EventRowData row : list) {
      table[k] = new Object[getTable().getColumnCount()];
      table[k][getTable().getEventNrColumn().getColumnIndex()] = row.getEventNr();
      table[k][getTable().getClientNrColumn().getColumnIndex()] = row.getClientNr();
      table[k][getTable().getDefaultEventNrColumn().getColumnIndex()] = row.getDefaultEventNr();
      table[k][getTable().getNameColumn().getColumnIndex()] = row.getName();
      table[k][getTable().getLocationColumn().getColumnIndex()] = row.getLocation();
      table[k][getTable().getMapColumn().getColumnIndex()] = row.getMap();
      table[k][getTable().getTypeColumn().getColumnIndex()] = row.getTypeUid();
      table[k][getTable().getZeroTimeColumn().getColumnIndex()] = row.getEvtZero();
      table[k][getTable().getFinishTimeColumn().getColumnIndex()] = row.getEvtFinish();
      table[k][getTable().getTimezoneColumn().getColumnIndex()] = row.getTimeZone();
      table[k][getTable().getEvtLastUploadColumn().getColumnIndex()] = row.getEvtLastUpload();
      k++;
    }
    importTableData(table);
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public NameColumn getNameColumn() {
      return getColumnSet().getColumnByClass(NameColumn.class);
    }

    public LocationColumn getLocationColumn() {
      return getColumnSet().getColumnByClass(LocationColumn.class);
    }

    public MapColumn getMapColumn() {
      return getColumnSet().getColumnByClass(MapColumn.class);
    }

    public ZeroTimeColumn getZeroTimeColumn() {
      return getColumnSet().getColumnByClass(ZeroTimeColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.EVENT;
    }

    @Override
    protected void execDecorateRow(ITableRow row) throws ProcessingException {
      if (CompareUtility.equals(getEventNrColumn().getValue(row), getDefaultEventNrColumn().getValue(row))) {
        FontSpec font = FontSpec.parse("bold");
        row.setFont(font);
      }
    }

    public TypeColumn getTypeColumn() {
      return getColumnSet().getColumnByClass(TypeColumn.class);
    }

    public TimezoneColumn getTimezoneColumn() {
      return getColumnSet().getColumnByClass(TimezoneColumn.class);
    }

    public FinishTimeColumn getFinishTimeColumn() {
      return getColumnSet().getColumnByClass(FinishTimeColumn.class);
    }

    public EvtLastUploadColumn getEvtLastUploadColumn() {
      return getColumnSet().getColumnByClass(EvtLastUploadColumn.class);
    }

    public ClientNrColumn getClientNrColumn() {
      return getColumnSet().getColumnByClass(ClientNrColumn.class);
    }

    public DefaultEventNrColumn getDefaultEventNrColumn() {
      return getColumnSet().getColumnByClass(DefaultEventNrColumn.class);
    }

    public EventNrColumn getEventNrColumn() {
      return getColumnSet().getColumnByClass(EventNrColumn.class);
    }

    @Order(10.0)
    public class EventNrColumn extends AbstractLongColumn {

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
    public class ClientNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }
    }

    @Order(30.0)
    public class DefaultEventNrColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("DefaultEvent");
      }
    }

    @Order(40.0)
    public class NameColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return ScoutTexts.get("Name");
      }

      @Override
      protected int getConfiguredWidth() {
        return 220;
      }
    }

    @Order(50.0)
    public class LocationColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Location");
      }

      @Override
      protected int getConfiguredWidth() {
        return 220;
      }
    }

    @Order(60.0)
    public class MapColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Map");
      }

      @Override
      protected int getConfiguredWidth() {
        return 220;
      }
    }

    @Order(70.0)
    public class TypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Type");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return EventTypeCodeType.class;
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }

    }

    @Order(80.0)
    public class ZeroTimeColumn extends AbstractDateColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("ZeroTime");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }

      @Override
      protected boolean getConfiguredHasTime() {
        return true;
      }
    }

    @Order(90.0)
    public class FinishTimeColumn extends AbstractDateColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("FinishClosing");
      }

      @Override
      protected int getConfiguredWidth() {
        return 120;
      }

      @Override
      protected boolean getConfiguredHasTime() {
        return true;
      }

    }

    @Order(100.0)
    public class TimezoneColumn extends AbstractSmartColumn<Integer> {

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Timezone");
      }

      @Override
      protected void execPrepareLookup(ILookupCall call, ITableRow row) {
        ((TimezoneLookupCall) call).setDate(getZeroTimeColumn().getValue(row));
        ((TimezoneLookupCall) call).setLongText(false);
      }

      @Override
      protected Class<? extends LookupCall<Integer>> getConfiguredLookupCall() {
        return TimezoneLookupCall.class;
      }

      @Override
      protected int getConfiguredWidth() {
        return 80;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(110.0)
    public class EvtLastUploadColumn extends AbstractDateTimeWithSecondsColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("LastSyncTo4mila");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
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
        EventForm form = new EventForm();
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          ((Desktop) ClientSession.get().getDesktop()).updateApplicationWindowTitle();
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
        EventForm form = new EventForm();
        form.setEventNr(getEventNrColumn().getSelectedValue());
        form.startModify();
        form.waitFor();
        if (form.isFormStored()) {
          ((Desktop) ClientSession.get().getDesktop()).updateApplicationWindowTitle();
          reloadPage();
        }
      }
    }

    @Order(30.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(40.0)
    public class EventSetDefaultEventMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return Texts.get("EventSetDefaultEvent");
      }

      @Override
      protected void execAction() throws ProcessingException {
        BEANS.get(IDefaultProcessService.class).setDefaultEventNr(getEventNrColumn().getSelectedValue());
        ((Desktop) ClientSession.get().getDesktop()).updateApplicationWindowTitle();
        if (InfoDisplayUtility.isActive()) {
          InfoDisplayIdleJob job = new InfoDisplayIdleJob(ClientSession.get());
          job.schedule();
        }
        reloadPage();
      }
    }

    @Order(50.0)
    public class Separator2Menu extends AbstractSeparatorMenu {
    }

    @Order(55.0)
    public class OpenLiveScreenMenu extends AbstractMenu {

      @Override
      protected void execInitAction() throws ProcessingException {
        setVisible(FMilaUtility.isRichClient());
      }

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("OpenLiveScreen");
      }

      @Override
      protected void execAction() throws ProcessingException {
        // TODO MIG
//        Long eventClientNr = getClientNrColumn().getSelectedValue();
//        Long eventNr = getEventNrColumn().getSelectedValue();
//        String serverUrl = Activator.getDefault().getBundle().getBundleContext().getProperty("server.url");
//        if (StringUtility.isNullOrEmpty(serverUrl)) {
//          serverUrl = "http://localhost:8084/4mila/process";
//        }
//        serverUrl = StringUtility.replace(serverUrl, "process", "web/screen.html?clientNr=" + eventClientNr + "&eventNr=" + eventNr);
//        FMilaClientUtility.openDocument(serverUrl);
      }
    }

    @Order(60.0)
    public class SyncWith4milaMenu extends AbstractMenu {

      @Override
      protected void execInitAction() throws ProcessingException {
        setVisible(FMilaUtility.isRichClient());
        setVisible(Platform.get().inDevelopmentMode()); // for release 1.0.x only
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("Sync");
      }

      @Override
      protected void execAction() throws ProcessingException {
        BEANS.get(IEventProcessService.class).syncWithOnline(getEventNrColumn().getSelectedValue());
        ClientSession.get().getDesktop().setStatusText(TEXTS.get("EventSynced"));
        reloadPage();
      }
    }

    @Order(70.0)
    public class OpenURLMenu extends AbstractMenu {

      @Override
      protected void execInitAction() throws ProcessingException {
        setVisible(FMilaUtility.isRichClient());
        setVisible(false); // for release 1.0.10 only
      }

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("OpenEventOn4mila");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (getEvtLastUploadColumn().getSelectedValue() == null) {
          FMilaClientUtility.showOkMessage(TEXTS.get("ApplicationName"), null, TEXTS.get("EventNotSyncedYet"));
          return;
        }
        Long eventClientNr = getClientNrColumn().getSelectedValue();
        Long eventNr = getEventNrColumn().getSelectedValue();
        String url = BEANS.get(IDatabaseService.class).getGlobalWebRootURL();
        FMilaClientUtility.openDocument(url + "index.html#!events." + eventClientNr + "." + eventNr + "");
      }
    }

    @Order(90.0)
    public class Separator3Menu extends AbstractSeparatorMenu {
    }

    @Order(100.0)
    public class DeleteMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return ScoutTexts.get("DeleteMenu");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.MultiSelection, TableMenuType.SingleSelection);
      }

      @Override
      protected void execAction() throws ProcessingException {
        boolean askDelete = MessageBoxes.showDeleteConfirmationMessage(Texts.get("Events"), getNameColumn().getSelectedValues());
        if (askDelete) {
          IMessageBox mbox = FMilaClientUtility.createMessageBox(ScoutTexts.get("DeleteConfirmationTitle"), null, TEXTS.get("CleanupDatabase"), TEXTS.get("NoCleanup"), TEXTS.get("Cleanup"), ScoutTexts.get("CancelButton"));
          int askCleanup = mbox.show();
          if (askCleanup == IMessageBox.YES_OPTION || askCleanup == IMessageBox.NO_OPTION) {
            boolean doCleanup = (askCleanup == IMessageBox.NO_OPTION);
            for (Long eventNr : getEventNrColumn().getSelectedValues()) {
              BEANS.get(IEventProcessService.class).delete(eventNr, doCleanup);
            }
          }
          reloadPage();
        }
      }
    }
  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return EventsSearchForm.class;
  }
}
