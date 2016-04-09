package com.rtiming.shared.settings.city;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractSqlCodeType;

public class CountryCodeType extends AbstractSqlCodeType {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1100L;

  public CountryCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("Country");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.COUNTRY;
  }

}
