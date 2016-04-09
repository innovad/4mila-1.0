package com.rtiming.shared.settings.user;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

public class LanguageCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1150L;

  public LanguageCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return ScoutTexts.get("Language");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public class English extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1151L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("English");
    }

    @Override
    public String getExtKey() {
      return "en";
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public class German extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1152L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("German");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getExtKey() {
      return "de";
    }

  }

  @Order(30.0)
  public class French extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1153L;

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("French");
    }

    @Override
    public String getExtKey() {
      return "fr";
    }

  }

}
