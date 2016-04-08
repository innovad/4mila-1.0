package com.rtiming.server.common.database.setup;

import java.io.File;
import java.nio.charset.Charset;
import java.sql.SQLException;

import org.eclipse.scout.commons.IOUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.h2.tools.DeleteDbFiles;
import org.h2.tools.Restore;
import org.h2.tools.RunScript;
import org.h2.tools.Server;

public class CreateH2Schema extends AbstractCreateSchema {

  public CreateH2Schema(JPASetup setup, boolean recreate, boolean isDevMode) {
    super(setup, recreate, isDevMode);
  }

  private String parseDBName() {
    return "dev"; // TODO MIG
//    JPASetup setup = getSetup();
//    String mappingName = setup.getJdbcMappingName();
//    String[] parts = mappingName.split("\\/");
//    return StringUtility.trim(parts[parts.length - 1]);
  }

  @Override
  public void drop() {
    DeleteDbFiles.execute(System.getProperty("user.home"), parseDBName(), false);
  }

  @Override
  public void runScript(String path) {
    try {
      RunScript.execute(getSetup().getJdbcMappingName(), getSetup().getHibernateConnectionUsername(), getSetup().getHibernateConnectionPassword(), path, Charset.forName("UTF8"), true);
    }
    catch (SQLException e) {
      throw new RuntimeException("Failed creating H2 database", e);
    }
  }

  @Override
  public void createBackup(String backupFile) throws ProcessingException {
    String script = "BACKUP TO '" + backupFile + "'";
    File tempScript = IOUtility.createTempFile(IOUtility.getTempFileName(".sql"), script.getBytes());
    runScript(tempScript.getAbsolutePath());
  }

  @Override
  public void restoreBackup(String backupFile) throws ProcessingException {
    String script = "SHUTDOWN";
    File tempScript = IOUtility.createTempFile(IOUtility.getTempFileName(".sql"), script.getBytes());
    runScript(tempScript.getAbsolutePath());
    Restore.execute(backupFile, System.getProperty("user.home"), parseDBName());
  }

  @Override
  public void postRun() {
    super.postRun();
    try {
      Server.createTcpServer(new String[]{"-tcpPort", "9123", "-tcpAllowOthers"}).start();
      Server.createWebServer(new String[]{"-webPort", "8085", "-ifExists"}).start();
    }
    catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
