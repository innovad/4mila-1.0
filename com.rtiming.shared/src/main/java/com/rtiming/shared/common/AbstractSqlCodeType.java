package com.rtiming.shared.common;

import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.code.AbstractCodeType;
import org.eclipse.scout.rt.shared.services.common.code.ICodeRow;

import com.rtiming.shared.settings.ISettingsOutlineService;

public abstract class AbstractSqlCodeType extends AbstractCodeType<Long, Long> {
  private static final long serialVersionUID = 1L;

  private boolean m_prependShortcut;

  public boolean getConfiguredPrependShortcut() {
    return true;
  }

  public boolean isPrependShortcut() {
    return m_prependShortcut;
  }

  public void setPrependShortcut(boolean prependShortcut) {
    m_prependShortcut = prependShortcut;
  }

  @Override
  protected void initConfig() {
    setPrependShortcut(getConfiguredPrependShortcut());
    super.initConfig();
  }

  /**
   * Object key
   * String text
   * String iconId
   * String tooltipText
   * String backgroundColor
   * String foregroundColor
   * String font
   * Long active (0 or 1)
   * Object parentKey
   * extKey
   * calcValue
   * enabled
   * partitionId
   */

  @Override
  public List<? extends ICodeRow<Long>> execLoadCodes(Class<? extends ICodeRow<Long>> codeRowType) {
    Object[][] data = BEANS.get(ISettingsOutlineService.class).loadCodes(this);
    // TODO MIG return createCodeRowArray(data);
    return null;
  }
}
