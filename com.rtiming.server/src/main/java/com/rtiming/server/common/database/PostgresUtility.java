package com.rtiming.server.common.database;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ProcessBuilder.Redirect;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.shared.FMilaUtility;

public class PostgresUtility {

  public static void executePostgresCommand(String command, String[] arguments, boolean readOutput) throws ProcessingException {
    executePostgresCommand("fmila", command, arguments, readOutput);
  }

  public static void executePostgresCommand(String database, String command, String[] arguments, boolean readOutput) throws ProcessingException {
    try {
      // Path to pgsql bin
      String pgdumpPath = getPostgresBinDirectory();

      List<String> commands = new ArrayList<String>();
      commands.add(pgdumpPath + FMilaUtility.FILE_SEPARATOR + command);
      commands.addAll(Arrays.asList(arguments));

      ProcessBuilder pb = new ProcessBuilder();
      pb.environment().clear();
      pb.environment().put("PGUSER", "fmila");
      if (!StringUtility.isNullOrEmpty(database)) {
        pb.environment().put("PGDATABASE", database);
      }
      pb.environment().put("PGPASSWORD", "");
      pb.environment().put("PGHOST", "localhost");
      if (readOutput) {
        pb.redirectErrorStream(true);
      }
      else {
        pb.redirectError(Redirect.INHERIT);
      }

      // Start process
      pb.command(commands);
      Process p = pb.start();

      // Read
      if (readOutput) {
        InputStream is = p.getInputStream();
        InputStreamReader reader = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(reader);
        String line;
        while ((line = br.readLine()) != null) {
          System.out.println(line);
        }
      }
    }
    catch (IOException e) {
      throw new ProcessingException("Postgres command failed: " + e.getMessage(), e);
    }
  }

  private static String getPostgresBinDirectory() throws ProcessingException {
    // String pathStr = Activator.getDefault().getBundle().getBundleContext().getProperty("postgresql.bin.dir");
    String pathStr = ""; // TODO MIG

    if (StringUtility.isNullOrEmpty(pathStr)) {
      throw new ProcessingException("postgresql.bin.dir in config.ini must be set");
    }

    File path = new File(pathStr);
    if (!path.exists()) {
      throw new ProcessingException("postgresql.bin.dir, cannot access directory: " + path.getAbsolutePath());
    }

    if (!path.canExecute()) {
      throw new ProcessingException("postgresql.bin.dir, no program execution rights: " + path.getAbsolutePath());
    }

    return pathStr;
  }

}
