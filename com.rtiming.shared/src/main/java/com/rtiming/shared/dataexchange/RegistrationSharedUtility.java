package com.rtiming.shared.dataexchange;

import org.eclipse.scout.rt.shared.TEXTS;

public final class RegistrationSharedUtility {

  private RegistrationSharedUtility() {
  }

  public static String getNewRegistrationNoText() {
    return "<" + TEXTS.get("NewRegistration") + ">";
  }

}
