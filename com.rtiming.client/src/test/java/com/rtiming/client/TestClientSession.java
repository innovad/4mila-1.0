package com.rtiming.client;

import org.eclipse.scout.rt.platform.util.StringUtility;

public class TestClientSession extends ClientSession {

  public TestClientSession() {
    super(true);
  }

  @Override
  public String getUserId() {
    String userId = super.getUserId();
    if (StringUtility.isNullOrEmpty(userId)) {
      userId = "admin";
    }
    return userId;
  }

}
