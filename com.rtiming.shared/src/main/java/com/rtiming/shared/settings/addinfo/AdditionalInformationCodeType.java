package com.rtiming.shared.settings.addinfo;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;

import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractSqlCodeType;

public class AdditionalInformationCodeType extends AbstractSqlCodeType {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1700L;

  public AdditionalInformationCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.ADDITIONAL_INFORMATION;
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("AdditionalInformation");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class IndividualStartFeeCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1701L;

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }

    @Override
    protected String getConfiguredText() {
      return Texts.get("IndividualStartFee");
    }

    @Override
    public String getExtKey() {
      return "individualstartfee";
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public static class SwissOrienteeringAntiDopingCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 1702L;

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("SwissOrienteeringAntiDoping");
    }

    @Override
    public String getExtKey() {
      return "swissorienteeringantidpoing";
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public static class StartTimeWishCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 1703L;

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("StartTimeWish");
    }

    @Override
    public String getExtKey() {
      return "starttimewish";
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(40.0)
  public static class StartTimeWishEarlyStartCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 1704L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("EarlyStart");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }

  }

  @Order(50.0)
  public static class StartTimeWishLateStartCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 1705L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("LateStart");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }

  }
}
