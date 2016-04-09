package com.rtiming.shared.settings.city;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractSqlCodeType;

public class AreaCodeType extends AbstractSqlCodeType {

  private static final long serialVersionUID = 1L;
  public static final long ID = 2100L;

  public AreaCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.COUNTRY;
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("Area");
  }

  @Override
  public Long getId() {
    return ID;
  }
}
