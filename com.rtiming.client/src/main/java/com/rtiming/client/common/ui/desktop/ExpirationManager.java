package com.rtiming.client.common.ui.desktop;

import java.util.Date;

import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.security.IUpdateService;
import com.rtiming.shared.common.security.UpdateInfo;
import com.rtiming.shared.settings.account.IAccountProcessService;

public class ExpirationManager {

  private static Logger logger = LoggerFactory.getLogger(ExpirationManager.class);

  public void checkExpiration() throws ProcessingException {
    if (FMilaUtility.isRichClient() && isExpired()) {
      String expiryString = DateUtility.formatDateTime(calculateExpiryDate());
      IMessageBox box = FMilaClientUtility.createMessageBox(null, null, TEXTS.get("ApplicationExpiredMessage", expiryString), TEXTS.get("Download"), TEXTS.get("Exit"), null);

      int answer = box.show();
      if (MessageBox.YES_OPTION == answer) {
        String updateURL = "http://www.4mila.com/";
        try {
          UpdateInfo update = BEANS.get(IUpdateService.class).checkForUpdate();
          if (!StringUtility.isNullOrEmpty(update.getDownloadLink())) {
            updateURL = update.getDownloadLink();
          }
        }
        catch (Exception e) {
          logger.warn("Could not check for update", e);
        }
        FMilaClientUtility.openDocument(updateURL);
      }
      ClientSession.get().stop();
    }
  }

  private boolean isExpired() throws ProcessingException {
    Date serverTime = BEANS.get(IAccountProcessService.class).getServerTime();
    Date clientTime = new Date();
    Date expiryDate = calculateExpiryDate();

    return (serverTime.after(expiryDate) || clientTime.after(expiryDate));
  }

  public static Date calculateExpiryDate() {
    String expiryString = "31.12.2016";
    Date expiryDate = DateUtility.truncDate(DateUtility.parse(expiryString, "dd.MM.yyyy"));
    expiryDate = DateUtility.addDays(expiryDate, 1);
    expiryDate = FMilaUtility.addSeconds(expiryDate, -1);
    return expiryDate;
  }

}
