package com.rtiming.shared.services.code;

import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCode;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;

public class CourseGenerationCodeType extends AbstractCodeType<Long, Long> {

  private static final long serialVersionUID = 1L;
  public static final Long ID = 2500L;

  public CourseGenerationCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredText() {
    return TEXTS.get("CourseSetting");
  }

  @Override
  public Long getId() {
    return ID;
  }

  @Order(10.0)
  public static class CourseGenerationIndividualControlsCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2501L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("CourseGenerationIndividualControls");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }

  @Order(20.0)
  public static class CourseGenerationUseCourseTemplateCode extends AbstractCode<Long> {

    private static final long serialVersionUID = 1L;
    public static final Long ID = 2502L;

    @Override
    protected String getConfiguredText() {
      return TEXTS.get("CourseGenerationUseCourseTemplate");
    }

    @Override
    public Long getId() {
      return ID;
    }
  }
}
