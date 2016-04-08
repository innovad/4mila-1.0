package com.rtiming.client;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.client.context.ClientRunContexts;
import org.eclipse.scout.rt.client.job.ModelJobs;
import org.eclipse.scout.rt.platform.util.concurrent.IRunnable;

public abstract class FMilaClientSyncJob {

  private final IClientSession session;
  private final String name;

  public FMilaClientSyncJob(String name, IClientSession session) {
    this.session = session;
    this.name = name;
  }

  public void start() {
    ModelJobs.schedule(new IRunnable() {

      @Override
      public void run() throws Exception {
        runVoid();
      }
    }, ModelJobs.newInput(ClientRunContexts.copyCurrent().withSession(session, true)).withName(name));
  }

  public IClientSession getClientSession() {
    return session;
  }

  @Deprecated
  public void schedule() {
    this.start();
  }

  protected abstract void runVoid() throws Exception;

}
