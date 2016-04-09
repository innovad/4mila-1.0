package com.rtiming.shared.settings.addinfo;

import org.eclipse.scout.rt.shared.services.lookup.ILookupService;

import com.rtiming.shared.common.AbstractBasicLookupCall;

public class AdditionalInformationLookupCall extends AbstractBasicLookupCall {

  private static final long serialVersionUID = 1L;
  private Long m_parentUid;
  private Long m_entityUid;

  @Override
  protected Class<? extends ILookupService<Long>> getConfiguredService() {
    return IAdditionalInformationLookupService.class;
  }

  public Long getParentUid() {
    return m_parentUid;
  }

  public void setParentUid(Long parentUid) {
    m_parentUid = parentUid;
  }

  public Long getEntityUid() {
    return m_entityUid;
  }

  public void setEntityUid(Long typeUid) {
    m_entityUid = typeUid;
  }
}
