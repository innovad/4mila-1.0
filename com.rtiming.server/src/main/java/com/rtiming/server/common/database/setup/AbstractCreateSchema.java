package com.rtiming.server.common.database.setup;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.persistence.Table;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.platform.Platform;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dao.RtEvent;

public abstract class AbstractCreateSchema {

  private static IScoutLogger logger = ScoutLogManager.getLogger(AbstractCreateSchema.class);

  private final JPASetup setup;
  private final boolean recreate;
  private final boolean devMode;

  public AbstractCreateSchema(JPASetup setup, boolean recreate, boolean devMode) {
    super();
    this.setup = setup;
    this.recreate = recreate;
    this.devMode = devMode;
  }

  public final void run() {
    if (true) { // TODO MIG recreate
      System.out.println("Dropping database...");
      drop();
    }
    if (!schemaExists()) {
      System.out.println("Create schema...");
      create();
      if (Platform.get().inDevelopmentMode()) { // TODO MIG devMode
        System.out.println("Init dev data...");
        initDevData();
      }
    }
    postRun();
  }

  public void postRun() {
  }

  protected abstract void drop();

  protected abstract void runScript(String path);

  protected boolean schemaExists() {
    // security hint - never drop global production
//    if (getSetup().getJdbcMappingName().contains("/global")) { // TODO MIG
//      return true;
//    }
    // check existing database
    boolean exists = false;
    Connection conn = null;
    ResultSet result = null;
    try {
      DriverManager.registerDriver((Driver) Class.forName(getSetup().getJdbcDriverName()).newInstance());
      conn = DriverManager.getConnection("jdbc:h2:~/dev", "fmila", "");
      // TODO MIG conn = DriverManager.getConnection(getSetup().getJdbcMappingName(), getSetup().getHibernateConnectionUsername(), getSetup().getHibernateConnectionPassword());
      String tableName = RtEvent.class.getAnnotation(Table.class).name().toUpperCase();
      result = conn.getMetaData().getTables(null, null, tableName, new String[]{"TABLE"});
      exists = result.first();
      if (!exists) {
        result = conn.getMetaData().getTables(null, null, tableName.toLowerCase(), new String[]{"TABLE"});
        exists = result.first();
      }
    }
    catch (SQLException | InstantiationException | IllegalAccessException | ClassNotFoundException e) {
      logger.error("unable to check if schema exists", e);
    }
    finally {
      if (result != null) {
        try {
          result.close();
        }
        catch (SQLException e) {
          logger.error("unable to close result set", e);
        }
      }
      if (conn != null) {
        try {
          conn.close();
        }
        catch (SQLException e) {
          logger.error("unable to close connection", e);
        }
      }
    }
    return exists;
  }

  private void create() {
    runScript(findScriptPath("schema.sql"));
  }

  private void initDevData() {
    runScript(findScriptPath("devdata.sql"));
  }

  private String findScriptPath(String scriptName) throws RuntimeException {
    String path = null;
    try {
      URL url = FMilaUtility.findFileLocation(scriptName, ""); // TODO MIG
      path = url.getFile();
      File convertToPath = new File(path);
      path = convertToPath.getAbsolutePath();
    }
    catch (ProcessingException e) {
      throw new RuntimeException("unable to run script", e);
    }
    return path;
  }

  protected JPASetup getSetup() {
    return setup;
  }

  protected boolean isDevMode() {
    return devMode;
  }

  protected boolean isRecreate() {
    return recreate;
  }

  public abstract void createBackup(String backupFile) throws ProcessingException;

  public abstract void restoreBackup(String backupFile) throws ProcessingException;

}
