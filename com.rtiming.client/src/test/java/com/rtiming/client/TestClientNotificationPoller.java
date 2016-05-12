package com.rtiming.client;

import java.util.List;

import org.eclipse.scout.rt.client.clientnotification.ClientNotificationPoller;
import org.eclipse.scout.rt.platform.Replace;
import org.eclipse.scout.rt.shared.clientnotification.ClientNotificationMessage;

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

  @Override
  protected void handleMessagesReceived(List<ClientNotificationMessage> notifications) {
    // disable for testing
  }

}
