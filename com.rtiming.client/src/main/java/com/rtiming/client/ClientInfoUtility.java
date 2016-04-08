package com.rtiming.client;

import java.util.Map;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.SharedInfoUtility;
import com.rtiming.shared.settings.ISettingsOutlineService;

public final class ClientInfoUtility {

  private ClientInfoUtility() {
  }

  private static final IScoutLogger LOG = ScoutLogManager.getLogger(ClientInfoUtility.class);
  private static final String NEW_LINE = FMilaUtility.LINE_SEPARATOR;

  public static String buildInstallationInfo(Map<String, String> plainTextPasswords) throws ProcessingException {

    StringBuilder info = new StringBuilder();
    info.append(BEANS.get(ISettingsOutlineService.class).getInstallationInfo(plainTextPasswords));
    info.append(NEW_LINE);
    info.append(SharedInfoUtility.buildInstallationInfo("Client"));

    return info.toString();
  }
}
