package com.rtiming.client.dataexchange.city;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.dataexchange.AbstractCSVInterface;
import com.rtiming.client.settings.city.CitiesTablePage;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.city.SwissPostCityBean;

public class SwissPostCityInterface extends AbstractCSVInterface<SwissPostCityBean> {

  public SwissPostCityInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
  }

  @Override
  public List<Long> getPrimaryKeyNrs() throws ProcessingException {
    CitiesTablePage cities = new CitiesTablePage();
    cities.loadChildren();
    return cities.getTable().getCityNrColumn().getValues();
  }

  @Override
  public SwissPostCityBean createNewBean(Long primaryKeyNr) {
    return new SwissPostCityBean(primaryKeyNr);
  }

}
