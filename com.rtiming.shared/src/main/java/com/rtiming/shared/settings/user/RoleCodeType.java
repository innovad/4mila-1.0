package com.rtiming.shared.settings.user;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractRoleCode;

public class RoleCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 2150L;

  public RoleCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("Role");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public class AdministratorCode extends AbstractRoleCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 2151L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Administrator");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getDefaultSetupUsername() {
      return FMilaUtility.ADMIN_USER;
    }

  }

  @Order(20.0)
  public class RegistrationCode extends AbstractRoleCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 2152L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Registration");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getDefaultSetupUsername() {
      return "reg";
    }

  }

  @Order(30.0)
  public class ECardDownloadCode extends AbstractRoleCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 2153L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("ECardDownload");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getDefaultSetupUsername() {
      return "ecard";
    }

  }

  @Order(40.0)
  public class SpeakerCode extends AbstractRoleCode {

    private static final long serialVersionUID = 1L;
    public static final long ID = 2154L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Speaker");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    public String getDefaultSetupUsername() {
      return "speaker";
    }

  }
}
