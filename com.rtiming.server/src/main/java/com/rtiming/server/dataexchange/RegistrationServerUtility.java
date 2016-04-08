package com.rtiming.server.dataexchange;

import com.rtiming.server.ServerSession;

public final class RegistrationServerUtility {

  private RegistrationServerUtility() {
  }

  public static String buildRegistrationNo(String identifier) {
    return "R" + ServerSession.get().getSessionClientNr() + "-" + identifier;
  }

}
