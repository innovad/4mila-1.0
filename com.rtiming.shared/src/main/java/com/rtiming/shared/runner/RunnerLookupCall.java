package com.rtiming.shared.runner;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

public class RunnerLookupCall extends LookupCall<Long> {

  private static final long serialVersionUID = 1L;
  private boolean m_showNameOnly;

  @Override
  protected Class<? extends ILookupService<Long>> getConfiguredService() {
    return IRunnerLookupService.class;
  }

  public boolean isShowNameOnly() {
    return m_showNameOnly;
  }

  public void setShowNameOnly(boolean showFullInfo) {
    m_showNameOnly = showFullInfo;
  }
}
