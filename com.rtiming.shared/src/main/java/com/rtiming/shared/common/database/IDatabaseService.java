package com.rtiming.shared.common.database;

import java.util.Date;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

@TunnelToServer
public interface IDatabaseService extends IService {

  List<String> getTables() throws ProcessingException;

  Object[][] getBackupsTableData() throws ProcessingException;

  void createBackup() throws ProcessingException;

  void restoreBackup(String backupFileName) throws ProcessingException;

  void deleteBackup(String... files) throws ProcessingException;

  String getDataDirectory() throws ProcessingException;

  void setScheduledBackupStatus(boolean force, boolean throwException) throws ProcessingException;

  void setupApplication() throws ProcessingException;

  Date getLastBackup() throws ProcessingException;

  DatabaseInfoBean getDatabaseInfo() throws ProcessingException;

  TableInfoBean getColumns(String tableId) throws ProcessingException;

  String getGlobalWebRootURL() throws ProcessingException;

}
