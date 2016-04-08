package com.rtiming.shared.settings;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class DefaultCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1250L;

  public DefaultCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("Settings");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class DefaultEventCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1251L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("DefaultEvent");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public static class DefaultCurrencyCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1252L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("DefaultCurrency");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public static class DefaultCountryCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1253L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("DefaultCountry");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(40.0)
  public static class BackupDirectoryCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 1254L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("BackupDirectory");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(50.0)
  public static class BackupIntervalCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 1255L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("BackupInterval");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(60.0)
  public static class BackupMaxNumberCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 1256L;

    @Override
    protected String getConfiguredText() {  return TEXTS.get("BackupMaxNumber");}

    @Override
    public Long getId() {
      return ID;
    }
  }

}
