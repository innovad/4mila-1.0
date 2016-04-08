package com.rtiming.client.common.ui.columns;

import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.shared.Texts;
import com.rtiming.shared.event.course.ClassCodeType;

/**
 * 
 */
public class AbstractClassNameColumn extends AbstractSmartColumn<Long> {

  @Override
  protected String getConfiguredHeaderText() {
    return Texts.get("Class");
  }

  @Override
  protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
    return ClassCodeType.class;
  }

  @Override
  protected int getConfiguredWidth() {
    return 100;
  }

}
