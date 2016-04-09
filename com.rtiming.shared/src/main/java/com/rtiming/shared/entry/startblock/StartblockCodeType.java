package com.rtiming.shared.entry.startblock;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.AbstractIcons;

import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractSqlCodeType;

public class StartblockCodeType extends AbstractSqlCodeType {

  private static final long serialVersionUID = 1L;
  public static final long ID = 2050L;

  public StartblockCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Star;
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("Startblock");
  }

  @Override
  public Long getId() {
    return ID;
  }
}
