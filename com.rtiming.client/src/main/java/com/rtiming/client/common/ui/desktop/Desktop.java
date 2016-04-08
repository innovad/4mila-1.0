package com.rtiming.client.common.ui.desktop;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.client.services.common.bookmark.IBookmarkService;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.client.ui.basic.table.ITable;
import org.eclipse.scout.rt.client.ui.desktop.AbstractDesktop;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.desktop.OpenUriAction;
import org.eclipse.scout.rt.client.ui.desktop.bookmark.menu.AbstractBookmarkMenu;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutlineViewButton;
import org.eclipse.scout.rt.client.ui.desktop.outline.IOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.form.ScoutInfoForm;
import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.bookmark.Bookmark;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.FMilaShortcutBookmarkUtility;
import com.rtiming.client.common.infodisplay.InfoDisplayIdleJob;
import com.rtiming.client.common.infodisplay.InfoDisplayUtility;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.dataexchange.DataExchangeWizard;
import com.rtiming.client.ecard.download.ECardStationStatusForm;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.entry.RegistrationOutline;
import com.rtiming.client.event.EventsOutline;
import com.rtiming.client.result.ResultsOutline;
import com.rtiming.client.result.pos.PosPrinterManager;
import com.rtiming.client.settings.SettingsOutline;
import com.rtiming.client.setup.SetupWizard;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.FMilaUtility.OperatingSystem;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.IDatabaseService;
import com.rtiming.shared.common.security.FMilaVersion;
import com.rtiming.shared.common.security.IUpdateService;
import com.rtiming.shared.common.security.UpdateInfo;
import com.rtiming.shared.common.security.permission.CreateDataExchangePermission;
import com.rtiming.shared.common.security.permission.ReadDownloadedECardPermission;
import com.rtiming.shared.settings.ISettingsOutlineService;

public class Desktop extends AbstractDesktop implements IDesktop {
  private static IScoutLogger logger = ScoutLogManager.getLogger(Desktop.class);

  private LinkForm linkForm;
  private MenuTableListener menuTableListener;

  public Desktop() {
  }

  @SuppressWarnings("unchecked")
  @Override
  protected List<Class<? extends IOutline>> getConfiguredOutlines() {
    return Arrays.asList(new Class[]{EventsOutline.class, RegistrationOutline.class, ResultsOutline.class, SettingsOutline.class});
  }

  @Order(10.0f)
  public class EventsOutlineViewButton extends AbstractOutlineViewButton {

    public EventsOutlineViewButton() {
      super(Desktop.this, EventsOutline.class);
    }
  }

  @Order(20.0f)
  public class EntryOutlineViewButton extends AbstractOutlineViewButton {

    public EntryOutlineViewButton() {
      super(Desktop.this, RegistrationOutline.class);
    }
  }

  @Order(30.0f)
  public class ResultsOutlineViewButton extends AbstractOutlineViewButton {

    public ResultsOutlineViewButton() {
      super(Desktop.this, ResultsOutline.class);
    }
  }

  @Order(40.0f)
  public class SettingsOutlineViewButton extends AbstractOutlineViewButton {

    public SettingsOutlineViewButton() {
      super(Desktop.this, SettingsOutline.class);
    }
  }

  @Override
  public String getConfiguredTitle() {
    return Texts.get("ApplicationName");
  }

  @Override
  protected void execClosing() throws ProcessingException {
    PosPrinterManager.closeAll();
  }

  @Override
  protected void execInit() throws ProcessingException {
    if (FMilaUtility.isStandalone()) {
      BEANS.get(IDatabaseService.class).setupApplication();
    }
    updateApplicationWindowTitle();
  }

  public void updateApplicationWindowTitle() throws ProcessingException {
    Date date = BEANS.get(IDatabaseService.class).getLastBackup();
    updateApplicationWindowTitle(date);
  }

  public void updateApplicationWindowTitle(Date lastBackup) throws ProcessingException {
    setTitle(ApplicationWindowTitleUtility.getTitle(getConfiguredTitle(), lastBackup));
  }

  @Override
  protected void execPageDetailTableChanged(ITable oldTable, ITable newTable) throws ProcessingException {
    super.execPageDetailTableChanged(oldTable, newTable);
    if (oldTable != null) {
      oldTable.removeTableListener(menuTableListener);
    }
    if (newTable != null) {
      if (linkForm != null) {
        linkForm.buildLinks();
        newTable.addTableListener(menuTableListener);
      }
    }
  }

  @Override
  protected void execGuiAttached() throws ProcessingException {
    ExpirationManager manager = new ExpirationManager();
    manager.checkExpiration();
  }

  @Override
  protected void execOpened() throws ProcessingException {
    //outline form
    // TODO MIG
//    DefaultOutlineTreeForm treeForm = new DefaultOutlineTreeForm();
//    treeForm.setIconId(Icons.ECLIPSE_SCOUT);
//    treeForm.startView();
//    //outline table
//    DefaultOutlineTableForm tableForm = new DefaultOutlineTableForm();
//    tableForm.setIconId(Icons.ECLIPSE_SCOUT);
//    tableForm.startView();
    // ecard station status
    if (FMilaUtility.isRichClient()) {
      ECardStationStatusForm status = new ECardStationStatusForm();
      status.startForm();
    }
    // context menu alternative: link form
    linkForm = new LinkForm();
    linkForm.startForm();

    ToolsForm toolsForm = new ToolsForm();
    toolsForm.startForm();

    menuTableListener = new MenuTableListener(linkForm);

    //load startup bookmark
    IBookmarkService bookmarkService = BEANS.get(IBookmarkService.class);
    bookmarkService.loadBookmarks();
    Bookmark bm = bookmarkService.getStartBookmark();
    if (bm != null) {
      bookmarkService.activate(bm);
    }
    else {
      if (getAvailableOutlines().size() > 0) {
        for (IOutline outline : getAvailableOutlines()) {
          if (outline.isVisible() && outline.isEnabled()) {
            setOutline(outline);
            break;
          }
        }
      }
    }

    //handle first login
    if (!FMilaUtility.isWebClient()) {
      Object[][] users = BEANS.get(ISettingsOutlineService.class).getUserTableData();
      if (users == null || users.length == 0) {
        SetupWizard setup = new SetupWizard();
        setup.start();
      }
    }

    // TODO MIG
    // Install Client Notification Listeners
    // IClientNotificationConsumerService cncService = BEANS.get(IClientNotificationConsumerService.class);
    // cncService.addGlobalClientNotificationConsumerListener(new NewBackupClientNotificationConsumerListener());
    // cncService.addGlobalClientNotificationConsumerListener(new SharedCacheChangedClientNotificationConsumerListener());
  }

  @Order(10.0)
  public class FileMenu extends AbstractMenu {

    @Override
    protected void execInitAction() throws ProcessingException {
      setVisible(FMilaUtility.isRichClient());
      if (OperatingSystem.MACOSX.equals(FMilaUtility.getPlatform())) {
        setText(Texts.get("FileMenuMac"));
      }
      super.execInitAction();
    }

    @Override
    public String getConfiguredText() {
      return Texts.get("FileMenu");
    }

    @Order(10.0)
    public class RefreshMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return Texts.get("Refresh");
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (getOutline() != null) {
          IPage page = getOutline().getActivePage();
          if (page != null) {
            page.reloadPage();
          }
        }
      }

      @Override
      public String getConfiguredKeyStroke() {
        return "f5";
      }
    }

    @Order(20.0)
    public class DataExchangeMenu extends AbstractMenu {

      @Override
      protected void execInitAction() throws ProcessingException {
        setVisible(FMilaUtility.isRichClient() && ACCESS.check(new CreateDataExchangePermission()));
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("DataExchangeMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        DataExchangeWizard wiz = new DataExchangeWizard();
        wiz.start();
      }
    }

    @Order(30.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(40.0)
    public class ExitMenu extends AbstractMenu {

      @Override
      protected void execInitAction() throws ProcessingException {
        setVisible(FMilaUtility.isRichClient());
      }

      @Override
      public String getConfiguredText() {
        return Texts.get("ExitMenu");
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        // Mac OS X provides its own exit menu
        setVisible(!FMilaUtility.getPlatform().equals(FMilaUtility.OperatingSystem.MACOSX));
      }

      @Override
      public void execAction() throws ProcessingException {
        ClientSessionProvider.currentSession().stop();
      }
    }
  }

  @Order(20.0)
  public class ToolsMenu extends AbstractMenu {

    @Override
    protected void execInitAction() throws ProcessingException {
      setVisible(FMilaUtility.isRichClient());
    }

    @Override
    public String getConfiguredText() {
      return Texts.get("ToolsMenu");
    }

    @Order(10.0)
    public class EntryMenu extends AbstractMenu {

      @Override
      protected void execInitAction() throws ProcessingException {
        setVisible(FMilaUtility.isRichClient());
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("NewEntryMenu");
      }

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
      }

      @Override
      protected void execAction() throws ProcessingException {
        EntryForm form = new EntryForm();
        form.startNew();
        form.waitFor();
        if (form.isFormStored()) {
          getOutline().getActivePage().reloadPage();
        }
      }

      @Override
      protected String getConfiguredKeyStroke() {
        return FMilaUtility.NEW_ENTRY_KEY_STROKE;
      }
    }

    @Order(20.0)
    public class EntriesTablePageMenu extends AbstractMenu {

      @Override
      protected void execInitAction() throws ProcessingException {
        setVisible(FMilaUtility.isRichClient() && ACCESS.check(new ReadDownloadedECardPermission()));
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("Entries");
      }

      @Override
      protected String getConfiguredKeyStroke() {
        return "f7";
      }

      @Override
      protected void execAction() throws ProcessingException {
        FMilaShortcutBookmarkUtility.activateEntriesTablePage();
      }
    }

    @Order(30.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(40.0)
    public class InfoDisplayMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("StartInfoDisplay");
      }

      @Override
      protected String getConfiguredKeyStroke() {
        return "f8";
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (!InfoDisplayUtility.isActive()) {
          setText(TEXTS.get("StopInfoDisplay"));
          InfoDisplayUtility.setActive(true);
          if (InfoDisplayUtility.useBrowserInfoWindow()) {
            InfoDisplayIdleJob job = new InfoDisplayIdleJob(ClientSession.get());
            job.schedule();
          }
          else {
            InfoDisplayUtility.getWindow();
          }
        }
        else {
          setText(TEXTS.get("StartInfoDisplay"));
          InfoDisplayUtility.setActive(false);
          if (!InfoDisplayUtility.useBrowserInfoWindow()) {
            InfoDisplayUtility.closeWindow();
          }
        }
      }

    }

    @Order(45.0)
    public class InfoDisplayIdleMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("ResetInfoDisplay");
      }

      @Override
      protected String getConfiguredKeyStroke() {
        return "f9";
      }

      @Override
      protected void execAction() throws ProcessingException {
        if (InfoDisplayUtility.isActive()) {
          new InfoDisplayIdleJob(ClientSession.get()).schedule();
        }
      }

    }

    @Order(50.0)
    public class Separator2Menu extends AbstractSeparatorMenu {
    }

    @Order(60.0)
    public class DownloadedECardsTablePageMenu extends AbstractMenu {

      @Override
      protected void execInitAction() throws ProcessingException {
        setVisible(FMilaUtility.isRichClient() && ACCESS.check(new ReadDownloadedECardPermission()));
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("DownloadedECards");
      }

      @Override
      protected String getConfiguredKeyStroke() {
        return "f12";
      }

      @Override
      protected void execAction() throws ProcessingException {
        FMilaShortcutBookmarkUtility.activateDownloadedECardsTablePage();
      }
    }
  }

  @Order(30.0)
  public class DevelopmentMenu extends FMilaDevelopmentMenu {

  }

  @Order(40.0)
  public class BookmarkMenu extends AbstractBookmarkMenu {
    public BookmarkMenu() {
      super(Desktop.this);
    }
  }

  @Order(50.0)
  public class HelpMenu extends AbstractMenu {

    @Override
    public String getConfiguredText() {
      return Texts.get("HelpMenu");
    }

    @Override
    protected void execInitAction() throws ProcessingException {
      if (OperatingSystem.MACOSX.equals(FMilaUtility.getPlatform())) {
        setText(Texts.get("HelpMenuMac"));
      }
      super.execInitAction();
    }

    @Order(10.0)
    public class UserGuideMenu extends AbstractMenu {

      @Override
      protected void execAction() throws ProcessingException {
        FMilaClientUtility.openDocument("http://www.4mila.com/#!userguide");
      }

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("UserGuide");
      }
    }

    @Order(20.0)
    public class UrlMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return "www.4mila.com";
      }

      @Override
      protected void execAction() throws ProcessingException {
        FMilaClientUtility.openDocument("http://www.4mila.com/");
      }
    }

    @Order(30.0)
    public class SeparatorMenu extends AbstractSeparatorMenu {
    }

    @Order(40.0)
    public class CheckForUpdateMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return TEXTS.get("CheckForUpdateMenu");
      }

      @Override
      protected void execInitAction() throws ProcessingException {
        setVisible(!FMilaUtility.isWebClient());
      }

      @Override
      protected void execAction() throws ProcessingException {
        UpdateInfo info = BEANS.get(IUpdateService.class).checkForUpdate();
        FMilaVersion serverVersion = new FMilaVersion(info.getServerVersion());
        if (StringUtility.isNullOrEmpty(info.getDownloadLink())) {
          FMilaClientUtility.showOkMessage(TEXTS.get("ApplicationName"), null, TEXTS.get("InstallationUpToDateMessage", serverVersion.toString()));
        }
        else {
          FMilaVersion newVersion = new FMilaVersion(info.getNewVersion());
          IMessageBox box = FMilaClientUtility.createMessageBox(TEXTS.get("UpdateAvailable"), null, TEXTS.get("UpdateMessage", newVersion.toString(), serverVersion.toString()), TEXTS.get("Download"), null, TEXTS.get("Cancel"));
          if (box.show() == IMessageBox.YES_OPTION) {
            // start download
            ClientSessionProvider.currentSession().getDesktop().openUri(info.getDownloadLink(), OpenUriAction.DOWNLOAD);
          }
        }
      }
    }

    @Order(50.0)
    public class AboutMenu extends AbstractMenu {

      @Override
      public String getConfiguredText() {
        return Texts.get("AboutMenu");
      }

      @Override
      public void execAction() throws ProcessingException {
        ScoutInfoForm form = new ScoutInfoForm();
        form.startModify();
      }
    }

  }

}
