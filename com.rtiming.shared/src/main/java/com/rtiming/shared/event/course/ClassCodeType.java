package com.rtiming.shared.event.course;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractSqlCodeType;

public class ClassCodeType extends AbstractSqlCodeType {

  private static final long serialVersionUID = 1L;
  public static final long ID = 1200L;

  public ClassCodeType() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredIconId() {
    return Icons.CLAZZ;
  }

  @Override
  protected boolean getConfiguredIsHierarchy() {
    return true;
  }

  @Override
  protected String getConfiguredText() {
    return Texts.get("Class");
  }

  @Override
  public Long getId() {
    return ID;
  }
}
