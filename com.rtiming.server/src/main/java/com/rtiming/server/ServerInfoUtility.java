package com.rtiming.server;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.server.services.common.file.RemoteFileService;

import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.setup.JPASetup;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.SharedInfoUtility;

public final class ServerInfoUtility {

  private ServerInfoUtility() {
  }

  private static final String NEW_LINE = FMilaUtility.LINE_SEPARATOR;
  private static final IScoutLogger LOG = ScoutLogManager.getLogger(ServerInfoUtility.class);

  public static String buildInstallationInfo(Map<String, String> plainTextPasswords) throws ProcessingException {
    StringBuilder info = new StringBuilder();

    // Current Date
    info = SharedInfoUtility.appendInfo(info, "Report created", DateUtility.formatDateTime(new Date()));
    info.append(NEW_LINE);

    // Versions
    info.append("*** General Info ***");
    info.append(NEW_LINE);
    info = SharedInfoUtility.appendInfo(info, "Server Version", FMilaUtility.getVersion());

    // Database
    String jdbc = JPASetup.get().getJdbcMappingName();
    info = SharedInfoUtility.appendInfo(info, "Database", jdbc);

    String dbVersion = "unknown";
    String dbProduct = "unknown";
    try {
      Connection connection = JPA.getConnection();
      DatabaseMetaData data = connection.getMetaData();
      dbVersion = data.getDatabaseProductVersion();
      dbProduct = data.getDatabaseProductName();
    }
    catch (SQLException e) {
      LOG.error("Could not read IP address", e);
    }
    info = SharedInfoUtility.appendInfo(info, "Version", dbVersion);
    info = SharedInfoUtility.appendInfo(info, "Product", dbProduct);

    // IP and Hostname
    String hostname = "unknown";
    String ip = "unknown";
    try {
      InetAddress addr = InetAddress.getLocalHost();
      hostname = addr.getHostName();
      ip = addr.getHostAddress();
    }
    catch (UnknownHostException e) {
      LOG.error("Could not read IP address", e);
    }
    info = SharedInfoUtility.appendInfo(info, "Hostname", hostname);
    info = SharedInfoUtility.appendInfo(info, "IP", ip);

    // File Root
    String fileRoot = BEANS.get(RemoteFileService.class).getRootPath();
    info = SharedInfoUtility.appendInfo(info, "File Store Root", fileRoot);
    info.append(NEW_LINE);

    // User Info
    if (plainTextPasswords != null && plainTextPasswords.size() > 0) {
      info.append("*** Users ***");
      info.append(NEW_LINE);
      for (String user : plainTextPasswords.keySet()) {
        info.append(user);
        info.append("/");
        info.append(plainTextPasswords.get(user));
        info.append(NEW_LINE);
      }
      info.append(NEW_LINE);
    }

    // Server Properties
    info.append(SharedInfoUtility.buildInstallationInfo("Server"));

    return info.toString();
  }

}
