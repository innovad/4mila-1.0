package com.rtiming.server.common.database.setup;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.server.common.database.PostgresUtility;

public class CreatePostgresqlSchema extends AbstractCreateSchema {

  public CreatePostgresqlSchema(JPASetup setup, boolean recreate, boolean isDevMode) {
    super(setup, recreate, isDevMode);
  }

  @Override
  public void drop() {
    String dbName = parseDBName();
    dropDb(dbName);
    createDb(dbName);
  }

  private void dropDb(String dbName) {
    try {
      String[] arguments = new String[]{dbName};
      PostgresUtility.executePostgresCommand(null, "dropdb", arguments, true);
    }
    catch (ProcessingException e) {
      System.out.println("Failed dropping database: " + e.getLocalizedMessage());
      e.printStackTrace();
      // nop
    }
  }

  private void createDb(String dbName) {
    try {
      String[] arguments = new String[]{"--template", "template0", "--encoding", "UTF8", dbName};
      PostgresUtility.executePostgresCommand(null, "createdb", arguments, true);
    }
    catch (ProcessingException e) {
      throw new RuntimeException("failed running command" + dbName, e);
    }
  }

  @Override
  public void runScript(String path) {
    try {
      String[] arguments = new String[]{"--dbname=" + parseDBName(), "--file", path};
      PostgresUtility.executePostgresCommand("psql", arguments, true);
    }
    catch (ProcessingException e) {
      throw new RuntimeException("failed running script" + path, e);
    }
  }

  private String parseDBName() {
    JPASetup setup = getSetup();
    String mappingName = setup.getJdbcMappingName();
    String[] parts = mappingName.split("\\/");
    String[] secondParts = parts[parts.length - 1].split("\\?");
    return StringUtility.trim(secondParts[0]);
  }

  @Override
  public void createBackup(String backupFile) throws ProcessingException {
    String[] arguments = new String[]{"-f", backupFile, "--clean", "--format=c", "--compress=9", "--verbose"};
    PostgresUtility.executePostgresCommand("pg_dump", arguments, true);
  }

  @Override
  public void restoreBackup(String backupFile) throws ProcessingException {
    String[] arguments = new String[]{"--dbname=fmila", "--no-password", "--clean", "--single-transaction", "--verbose", backupFile};
    PostgresUtility.executePostgresCommand("pg_restore", arguments, false);
  }

}
