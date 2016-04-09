package com.rtiming.client.dataexchange.swiss;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.dataexchange.AbstractCSVInterface;
import com.rtiming.shared.AdditionalInformationUtility;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.swiss.GO2OLEntryBean;

public class GO2OLEntriesInterface extends AbstractCSVInterface<GO2OLEntryBean> {

  /**
   * @param interfaceConfig
   * @throws ProcessingException
   */
  public GO2OLEntriesInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);

    // Check if Anti Doping additional info exists
    AdditionalInformationUtility.createSwissOrienteeringAntiDoping();

    // Check if Start Time Wishes additional info exists
    AdditionalInformationUtility.createStartTimeWish(interfaceConfig.getEvent().getValue());
  }

  @Override
  public AbstractDataBean createNewBean(Long primaryKeyNr) {
    return new GO2OLEntryBean(primaryKeyNr, getEventNr());
  }

  @Override
  public List<Long> getPrimaryKeyNrs() throws ProcessingException {
    return null;
  }

}
