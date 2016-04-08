package com.rtiming.server;

import org.eclipse.scout.rt.platform.IPlatform.State;
import org.eclipse.scout.rt.platform.IPlatformListener;
import org.eclipse.scout.rt.platform.PlatformEvent;

import com.rtiming.server.common.database.setup.JPASetup;

/**
 * 
 */
public class ServerStartup implements IPlatformListener {

  @Override
  public void stateChanged(PlatformEvent event) {
    if (event.getState().equals(State.BeanManagerValid)) {
      JPASetup.get().initDatabase();
    }
  }

}
