//package com.rtiming.shared.common.database;
//
//import java.util.Date;
//
//public class NewBackupNotification extends AbstractClientNotification {
//
//  private static final long serialVersionUID = 1L;
//  private final Date lastBackup;
//
//  public NewBackupNotification(Date lastBackup) {
//    this.lastBackup = lastBackup;
//  }
//
//  @Override
//  public boolean coalesce(IClientNotification existingNotification) {
//    return existingNotification.getClass().equals(this.getClass());
//  }
//
//  public Date getLastBackup() {
//    return lastBackup;
//  }
//
//}
