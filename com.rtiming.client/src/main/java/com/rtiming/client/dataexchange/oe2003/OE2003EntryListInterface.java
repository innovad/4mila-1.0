package com.rtiming.client.dataexchange.oe2003;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.client.dataexchange.AbstractCSVInterface;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.oe2003.OE2003EntryBean;

public class OE2003EntryListInterface extends AbstractCSVInterface<OE2003EntryBean> {

  public OE2003EntryListInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
  }

  @Override
  public List<Long> getPrimaryKeyNrs() throws ProcessingException {
    return null;
  }

  @Override
  public OE2003EntryBean createNewBean(Long primaryKeyNr) {
    return new OE2003EntryBean(primaryKeyNr, getEventNr());
  }

  @Override
  protected boolean getConfiguredTranslatedColumnHeaders() {
    return true;
  }

}
