//package com.rtiming.client.common.database;
//
//import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
//
//import com.rtiming.client.ClientSession;
//import com.rtiming.client.FMilaClientSyncJob;
//import com.rtiming.client.common.ui.desktop.Desktop;
//import com.rtiming.shared.common.database.NewBackupNotification;
//
//public class NewBackupClientNotificationConsumerListener implements IClientNotificationConsumerListener {
//
//  @Override
//  public void handleEvent(final ClientNotificationConsumerEvent e, boolean sync) {
//    if (e.getClientNotification() instanceof NewBackupNotification) {
//      new FMilaClientSyncJob("NewBackupNotification", ClientSession.get()) {
//        @Override
//        protected void runVoid() throws Exception {
//          NewBackupNotification notification = (NewBackupNotification) e.getClientNotification();
//          IDesktop desktop = ClientSession.get().getDesktop();
//          if (desktop instanceof Desktop) {
//            Desktop fmilaDesktop = (Desktop) desktop;
//            fmilaDesktop.updateApplicationWindowTitle(notification.getLastBackup());
//          }
//        }
//      }.schedule();
//    }
//  }
//}
