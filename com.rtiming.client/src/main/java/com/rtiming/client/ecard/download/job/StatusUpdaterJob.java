package com.rtiming.client.ecard.download.job;

import org.eclipse.scout.rt.client.IClientSession;

import com.rtiming.client.FMilaClientSyncJob;

public class StatusUpdaterJob extends FMilaClientSyncJob {

  private final IClientSession session;
  private final String statusText;

  public StatusUpdaterJob(IClientSession session, String statusText) {
    super("StatusUpdaterJob", session);
    this.session = session;
    this.statusText = statusText;
  }

  @Override
  protected void runVoid() throws Exception {
    if (session != null && session.getDesktop() != null) {
      session.getDesktop().setStatusText(statusText);
    }
  }

  public static void setText(String text, IClientSession session) {
    StatusUpdaterJob job = new StatusUpdaterJob(session, text);
    job.start();
  }

}
