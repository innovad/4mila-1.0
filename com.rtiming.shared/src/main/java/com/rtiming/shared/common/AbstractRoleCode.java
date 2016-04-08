package com.rtiming.shared.common;

import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;

public abstract class AbstractRoleCode extends AbstractCode<Long> {

  private static final long serialVersionUID = 1L;

  public abstract String getDefaultSetupUsername();

}
