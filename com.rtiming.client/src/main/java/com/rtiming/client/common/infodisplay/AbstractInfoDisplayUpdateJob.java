package com.rtiming.client.common.infodisplay;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.FMilaClientSyncJob;

public abstract class AbstractInfoDisplayUpdateJob extends FMilaClientSyncJob {

  private final IClientSession session;

  public AbstractInfoDisplayUpdateJob(IClientSession session) {
    super("InfoWindowUpdateJob", session);
    this.session = session;
  }

  public IClientSession getSession() {
    return session;
  }

  protected abstract String prepareURL() throws ProcessingException;

  @Override
  protected final void runVoid() throws Exception {
    final String locationFinal = prepareURL();

    new FMilaClientSyncJob("UpdateInfoWindowContent", session) {
      @Override
      protected void runVoid() throws ProcessingException {
        if (InfoDisplayUtility.useBrowserInfoWindow()) {
          SafariMacUtility.openURLinSafari(locationFinal);
        }
        else {
          InfoDisplayForm infoForm = InfoDisplayUtility.getWindow();
          infoForm.getInfoField().setLocation(locationFinal);
        }
      }
    }.start();

  }
}
