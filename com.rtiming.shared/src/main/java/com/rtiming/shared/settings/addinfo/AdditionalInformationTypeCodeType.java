package com.rtiming.shared.settings.addinfo;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;

public class AdditionalInformationTypeCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1750L;

  public AdditionalInformationTypeCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.ADDITIONAL_INFORMATION;
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("AdditionalInformationType");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public class IntegerCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1751L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Integer");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public class DoubleCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1752L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Double");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public class BooleanCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1753L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Boolean");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(40.0)
  public class TextCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1754L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Text");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(50.0)
  public class SmartfieldCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1755L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Smartfield");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
