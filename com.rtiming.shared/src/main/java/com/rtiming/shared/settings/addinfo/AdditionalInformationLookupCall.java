package com.rtiming.shared.settings.addinfo;

import org.eclipse.scout.commons.annotations.FormData;
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

  @FormData
  public Long getParentUid() {
    return m_parentUid;
  }

  @FormData
  public void setParentUid(Long parentUid) {
    m_parentUid = parentUid;
  }

  @FormData
  public Long getEntityUid() {
    return m_entityUid;
  }

  @FormData
  public void setEntityUid(Long typeUid) {
    m_entityUid = typeUid;
  }
}
