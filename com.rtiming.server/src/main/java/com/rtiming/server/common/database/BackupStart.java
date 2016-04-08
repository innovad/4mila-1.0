package com.rtiming.server.common.database;

import javax.security.auth.Subject;

import org.eclipse.scout.rt.server.IServerSession;

/**
 * 
 */
public class BackupStart {

  public BackupStart(IServerSession serverSession, Subject subject) {
    // TODO MIG super("4mila Backup Start", serverSession, subject);
    // this.setSubject(subject);
  }

//  @Override
//  protected IStatus runTransaction(IProgressMonitor monitor) throws Exception {
//    BEANS.get(IDatabaseService.class).setScheduledBackupStatus(true, !FMilaServerUtility.isTestEnvironment());
//    return null;
//  }

}
