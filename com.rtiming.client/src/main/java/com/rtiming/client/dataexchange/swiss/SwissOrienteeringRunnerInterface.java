package com.rtiming.client.dataexchange.swiss;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.client.dataexchange.AbstractCSVInterface;
import com.rtiming.client.runner.RunnersSearchForm;
import com.rtiming.client.runner.RunnersTablePage;
import com.rtiming.shared.AdditionalInformationUtility;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.swiss.SwissOrienteeringRunnerBean;

public class SwissOrienteeringRunnerInterface extends AbstractCSVInterface<SwissOrienteeringRunnerBean> {

  public SwissOrienteeringRunnerInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);

    // Check if Anti Doping additional info exists
    AdditionalInformationUtility.createSwissOrienteeringAntiDoping();
  }

  @Override
  public List<Long> getPrimaryKeyNrs() throws ProcessingException {
    RunnersTablePage runners = new RunnersTablePage();
    runners.setSearchForm(new RunnersSearchForm());
    runners.getSearchFormInternal().start();
    runners.loadChildren();
    return runners.getTable().getRunnerNrColumn().getValues();
  }

  @Override
  public SwissOrienteeringRunnerBean createNewBean(Long primaryKeyNr) {
    return new SwissOrienteeringRunnerBean(primaryKeyNr);
  }

}
