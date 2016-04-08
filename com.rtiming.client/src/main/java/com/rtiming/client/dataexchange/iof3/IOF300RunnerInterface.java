package com.rtiming.client.dataexchange.iof3;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ClientSession;
import com.rtiming.client.dataexchange.AbstractXMLInterface;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.cache.CityDataCacher;
import com.rtiming.shared.dataexchange.cache.ClazzDataCacher;
import com.rtiming.shared.dataexchange.cache.ClubDataCacher;
import com.rtiming.shared.dataexchange.cache.DataCacher;
import com.rtiming.shared.dataexchange.cache.NationDataCacher;
import com.rtiming.shared.dataexchange.iof3.IOF300RunnerDataBean;
import com.rtiming.shared.dataexchange.iof3.IOF300Utility;
import com.rtiming.shared.dataexchange.iof3.xml.Competitor;
import com.rtiming.shared.dataexchange.iof3.xml.CompetitorList;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.runner.RunnerRowData;
import com.rtiming.shared.runner.RunnersSearchFormData;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.city.ICountryProcessService;

public class IOF300RunnerInterface extends AbstractXMLInterface<IOF300RunnerDataBean> {

  private final Hashtable<Long, RunnerRowData> runnerList;
  private final CityDataCacher cityCache;
  private final NationDataCacher nationCache;
  private final ClubDataCacher clubCache;
  private final ClazzDataCacher clazzCache;
  private final String defaultNation;

  public IOF300RunnerInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
    this.setJaxbObject(new CompetitorList());
    runnerList = new Hashtable<Long, RunnerRowData>();
    cityCache = new CityDataCacher();
    nationCache = new NationDataCacher();
    clubCache = new ClubDataCacher();
    clazzCache = new ClazzDataCacher();
    defaultNation = getDefaultNation();
  }

  private String getDefaultNation() throws ProcessingException {
    Long countryUid = BEANS.get(IDefaultProcessService.class).getDefaultCountryUid();
    CountryFormData country = new CountryFormData();
    country.setCountryUid(countryUid);
    country = BEANS.get(ICountryProcessService.class).load(country);
    return country.getNation().getValue();
  }

  @Override
  public Object createXMLRootObject() throws ProcessingException {
    CompetitorList competitorList = new CompetitorList();
    competitorList.setIofVersion(IOF300Utility.getIOFVersion());
    competitorList.setCreateTime(IOF300Utility.getCurrentModifyDateTime());
    competitorList.setCreator(IOF300Utility.getCreator());
    return competitorList;
  }

  @Override
  public AbstractDataBean createNewBean(Long primaryKeyNr) {
    RunnerRowData row = runnerList.get(primaryKeyNr);
    return new IOF300RunnerDataBean(primaryKeyNr, row, DataCacher.get(ClientSession.get().getUserId()), defaultNation);
  }

  @Override
  protected ArrayList<? extends IOF300RunnerDataBean> createBeansFromXMLObject(Object xmlData) throws ProcessingException {
    ArrayList<IOF300RunnerDataBean> list = new ArrayList<IOF300RunnerDataBean>();

    CompetitorList competitorList = (CompetitorList) xmlData;
    for (Competitor competitor : competitorList.getCompetitor()) {
      IOF300RunnerDataBean runner = new IOF300RunnerDataBean(getEventNr(), null, DataCacher.get(ClientSession.get().getUserId()), defaultNation);
      runner.setCompetitor(competitor);
      list.add(runner);
    }

    return list;
  }

  @Override
  public List<Long> getPrimaryKeyNrs() throws ProcessingException {
    List<RunnerRowData> list = BEANS.get(IEventsOutlineService.class).getRunnerTableData(new RunnersSearchFormData());

    List<Long> nrs = new ArrayList<>();
    for (RunnerRowData row : list) {
      runnerList.put(row.getRunnerNr(), row);
      nrs.add(row.getRunnerNr());
    }

    return nrs;
  }

  @Override
  public ArrayList getColumnHeaders() {
    ArrayList<String> list = new ArrayList<String>();
    if (isImport()) {
      list.add(TEXTS.get("Replace"));
    }
    list.add(TEXTS.get("Number"));
    list.add(TEXTS.get("LastName"));
    list.add(TEXTS.get("FirstName"));
    list.add(TEXTS.get("ECard"));
    return list;
  }

  @Override
  protected boolean isUseXmlFilter() {
    return true;
  }

}
