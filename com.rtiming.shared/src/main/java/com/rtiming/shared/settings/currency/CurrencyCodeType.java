package com.rtiming.shared.settings.currency;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractSqlCodeType;

public class CurrencyCodeType extends AbstractSqlCodeType {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1850L;

  public CurrencyCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.PAYMENT;
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("Currency");
  }

  @Override
  public Long getId() {
    return ID;
  }
}
