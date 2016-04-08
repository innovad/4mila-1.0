package com.rtiming.shared.event.course;

import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

import com.rtiming.shared.Texts;

public class ClassTypeCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1050L;

  public ClassTypeCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("ClassType");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public class IndividualEventCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1051L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("IndividualEvent");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public class RelayCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1052L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("Relay");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(30.0)
  public class TeamCombinedCourseCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1053L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("TeamCombinedCourse");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(40.0)
  public class TeamScoreEventCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1054L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("TeamScoreEvent");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    protected boolean getConfiguredActive() {
      // disable until implemented
      return false;
    }

  }

  @Order(50.0)
  public class IndividualScoreEventCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1055L;

    @Override
    protected String getConfiguredText() {
      return Texts.get("IndividualScoreEvent");
    }

    @Override
    public Long getId() {
      return ID;
    }

    @Override
    protected boolean getConfiguredActive() {
      // disable until implemented
      return false;
    }

  }

  public static boolean isTeamClassType(Long codeTypeId) {
    if (codeTypeId == null) {
      return false;
    }
    return (codeTypeId == TeamCombinedCourseCode.ID || codeTypeId == TeamScoreEventCode.ID);
  }

  public static boolean isOneRaceType(Long codeTypeId) {
    if (codeTypeId == null) {
      return false;
    }
    return (codeTypeId == TeamCombinedCourseCode.ID || codeTypeId == IndividualEventCode.ID || codeTypeId == IndividualScoreEventCode.ID);
  }

  public static boolean isIndividualClassType(Long codeTypeId) {
    if (codeTypeId == null) {
      return false;
    }
    return (codeTypeId == IndividualEventCode.ID ||
        codeTypeId == RelayLegCode.ID || codeTypeId == IndividualScoreEventCode.ID);
  }

  /**
   * true if type is relay or relay leg
   * 
   * @param codeTypeId
   * @return true/false
   */
  public static boolean isRelayType(Long codeTypeId) {
    if (codeTypeId == null) {
      return false;
    }
    return (codeTypeId == RelayCode.ID || codeTypeId == RelayLegCode.ID);
  }

  /**
   * true if type is single race (everything except main relay type)
   * 
   * @param codeTypeId
   * @return true/false
   */
  public static boolean isLegClassType(Long codeTypeId) {
    if (codeTypeId == null) {
      return false;
    }
    return (codeTypeId != RelayCode.ID); // everything except relay is possible on RT_RACE
  }

  @Order(60.0)
  public class RelayLegCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final long ID = 1046L;

    @Override
    protected boolean getConfiguredActive() {
      return false;
    }

    @Override
    protected String getConfiguredText() {
      return Texts.get("Leg");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
