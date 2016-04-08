package com.rtiming.server.entry;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.clientnotification.IClientNotificationService;

import com.rtiming.shared.entry.SharedCache;

/**
 * 
 */
public final class SharedCacheServerUtility {

  public static void notifyClients() throws ProcessingException {
    SharedCache.resetCache();
    IClientNotificationService notifyService = BEANS.get(IClientNotificationService.class);
    // TODO MIG notifyService.putNotification(new SharedCacheChangedClientNotification(), new AllUserFilter(30000L));
  }

}
