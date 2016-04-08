package com.rtiming.server.common.database;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.server.scheduler.AbstractSchedulerJobWithFormula;
import org.eclipse.scout.rt.server.scheduler.IScheduler;
import org.eclipse.scout.rt.server.scheduler.TickSignal;

/**
 * 
 */
public class BackupJob extends AbstractSchedulerJobWithFormula {

  public static final String ID = "Backup";

  public BackupJob(Long interval) throws ProcessingException {
    super(ID, ID, "minute % " + interval + "==0");
  }

  @Override
  public void run(IScheduler scheduler, TickSignal signal) throws ProcessingException {
    ServerBackupUtility.backup();
  }

}
