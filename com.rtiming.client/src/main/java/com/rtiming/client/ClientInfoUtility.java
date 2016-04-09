package com.rtiming.client;

import java.util.Map;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.SharedInfoUtility;
import com.rtiming.shared.settings.ISettingsOutlineService;

public final class ClientInfoUtility {

  private ClientInfoUtility() {
  }

  private static final Logger LOG = LoggerFactory.getLogger(ClientInfoUtility.class);
  private static final String NEW_LINE = FMilaUtility.LINE_SEPARATOR;

  public static String buildInstallationInfo(Map<String, String> plainTextPasswords) throws ProcessingException {

    StringBuilder info = new StringBuilder();
    info.append(BEANS.get(ISettingsOutlineService.class).getInstallationInfo(plainTextPasswords));
    info.append(NEW_LINE);
    info.append(SharedInfoUtility.buildInstallationInfo("Client"));

    return info.toString();
  }
}
