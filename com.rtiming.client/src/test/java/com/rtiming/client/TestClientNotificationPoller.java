package com.rtiming.client;

import org.eclipse.scout.rt.client.clientnotification.ClientNotificationPoller;
import org.eclipse.scout.rt.platform.Replace;

@Replace
public class TestClientNotificationPoller extends ClientNotificationPoller {

  @Override
  public void start() {
    // disable for testing
  }

  @Override
  public void stop() {
    // disable for testing
  }

}
