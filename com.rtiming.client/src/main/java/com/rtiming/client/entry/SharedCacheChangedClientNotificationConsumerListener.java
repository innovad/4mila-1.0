//package com.rtiming.client.entry;
//
//import org.eclipse.scout.commons.exception.ProcessingException;
//import org.eclipse.scout.rt.platform.BEANS;
//import org.eclipse.scout.rt.platform.exception.ExceptionHandler;
//
//import com.rtiming.shared.entry.SharedCache;
//import com.rtiming.shared.entry.SharedCacheChangedClientNotification;
//
///**
// * 
// */
//public class SharedCacheChangedClientNotificationConsumerListener implements IClientNotificationConsumerListener {
//
//  @Override
//  public void handleEvent(ClientNotificationConsumerEvent e, boolean sync) {
//    if (e.getClientNotification() instanceof SharedCacheChangedClientNotification) {
//      try {
//        SharedCache.resetCache();
//      }
//      catch (ProcessingException ex) {
//        BEANS.get(ExceptionHandler.class).handle(ex);
//      }
//    }
//  }
//
//}
