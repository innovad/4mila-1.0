package com.rtiming.shared.settings.clazz;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

public class ClazzLookupCall extends LookupCall<Long> {

  private static final long serialVersionUID = 1L;
  private Long m_eventNr;
  private Long m_classNr;
  private boolean m_showShortcutOnly;
  private boolean m_showLegsOnly;
  private boolean m_showClassesOnly;

  @Override
  protected Class<? extends ILookupService<Long>> getConfiguredService() {
    return IClazzLookupService.class;
  }

  public Long getEventNr() {
    return m_eventNr;
  }

  public void setEventNr(Long eventNr) {
    m_eventNr = eventNr;
  }

  public Long getClassNr() {
    return m_classNr;
  }

  public void setClassNr(Long classNr) {
    m_classNr = classNr;
  }

  public boolean isShowShortcutOnly() {
    return m_showShortcutOnly;
  }

  public void setShowShortcutOnly(boolean showShortcutOnly) {
    m_showShortcutOnly = showShortcutOnly;
  }

  public boolean isShowLegsOnly() {
    return m_showLegsOnly;
  }

  public void setShowLegsOnly(boolean showLegsOnly) {
    m_showLegsOnly = showLegsOnly;
  }

  public boolean isShowClassesOnly() {
    return m_showClassesOnly;
  }

  public void setShowClassesOnly(boolean showClassesOnly) {
    m_showClassesOnly = showClassesOnly;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((m_classNr == null) ? 0 : m_classNr.hashCode());
    result = prime * result + ((m_eventNr == null) ? 0 : m_eventNr.hashCode());
    result = prime * result + (m_showClassesOnly ? 1231 : 1237);
    result = prime * result + (m_showLegsOnly ? 1231 : 1237);
    result = prime * result + (m_showShortcutOnly ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    ClazzLookupCall other = (ClazzLookupCall) obj;
    if (m_classNr == null) {
      if (other.m_classNr != null) {
        return false;
      }
    }
    else if (!m_classNr.equals(other.m_classNr)) {
      return false;
    }
    if (m_eventNr == null) {
      if (other.m_eventNr != null) {
        return false;
      }
    }
    else if (!m_eventNr.equals(other.m_eventNr)) {
      return false;
    }
    if (m_showClassesOnly != other.m_showClassesOnly) {
      return false;
    }
    if (m_showLegsOnly != other.m_showLegsOnly) {
      return false;
    }
    if (m_showShortcutOnly != other.m_showShortcutOnly) {
      return false;
    }
    return true;
  }

}
