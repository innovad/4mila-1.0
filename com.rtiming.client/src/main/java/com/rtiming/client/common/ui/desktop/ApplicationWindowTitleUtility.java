package com.rtiming.client.common.ui.desktop;

import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Platform;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;

public final class ApplicationWindowTitleUtility {

  private ApplicationWindowTitleUtility() {
  }

  public static String getTitle(String configuredTitle, Date lastBackup) throws ProcessingException {
    StringBuffer title = new StringBuffer();
    // User Id
    title.append(ClientSession.get().getUserId().toLowerCase());
    // Default Event
    Long defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    if (defaultEventNr != null) {
      EventBean event = new EventBean();
      event.setEventNr(defaultEventNr);
      event = BEANS.get(IEventProcessService.class).load(event);
      title.append("@");
      title.append(event.getName());
    }
    // Expiry Date
    title.append(" (");
    title.append(configuredTitle);
    title.append(" ");
    title.append(FMilaUtility.getVersion());
    if (FMilaUtility.isRichClient()) {
      title.append(", ");
      title.append(TEXTS.get("Expires"));
      title.append(" ");
      title.append(DateUtility.formatDate(ExpirationManager.calculateExpiryDate()));
    }
    title.append(") ");
    // Client Nr
    if (Platform.get().inDevelopmentMode()) {
      title.append(" ");
      title.append("ClientNr=");
      title.append(ClientSession.get().getSessionClientNr());
    }
    // Backup
    title.append(" ");
    if (lastBackup != null) {
      title.append(TEXTS.get("LastBackup"));
      title.append(": ");
      title.append(DateUtility.formatDateTime(lastBackup));
    }
    else {
      title.append(TEXTS.get("NoBackup"));
    }
    return title.toString();
  }
}
