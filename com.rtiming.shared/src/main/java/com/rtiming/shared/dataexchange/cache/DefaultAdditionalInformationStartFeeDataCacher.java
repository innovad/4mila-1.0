package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.shared.AdditionalInformationUtility;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;

public class DefaultAdditionalInformationStartFeeDataCacher extends AbstractDefaultDataCacher<AdditionalInformationAdministrationFormData> {

  private final Long eventNr;

  public DefaultAdditionalInformationStartFeeDataCacher(Long eventNr) {
    this.eventNr = eventNr;
  }

  @Override
  protected Long getPrimaryKey(AdditionalInformationAdministrationFormData formData) {
    return formData.getAdditionalInformationUid();
  }

  @Override
  protected AdditionalInformationAdministrationFormData createDefaultValue() throws ProcessingException {
    return AdditionalInformationUtility.createIndividualStartFee(eventNr);
  }

}
