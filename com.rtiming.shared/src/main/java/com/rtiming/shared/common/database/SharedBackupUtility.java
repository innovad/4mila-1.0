package com.rtiming.shared.common.database;

public final class SharedBackupUtility {

  public static boolean backupIsActive(Long interval) {
    if (interval != null && interval > 0) {
      return true;
    }
    return false;
  }

}
