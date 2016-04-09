package com.rtiming.shared.dataexchange.city;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.dataexchange.AbstractCSVDataBean;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.CSVElement;
import com.rtiming.shared.dataexchange.IDataExchangeService;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.ImportMessageList;

public class GeonamesCityBean extends AbstractCSVDataBean {

  // 1 CH
  // 2 5000
  // 3 Aarau
  // 4 Kanton Aargau
  // 5 AG
  // 6 Bezirk Aarau
  // 7 1901
  // 8 Aarau
  // 9 4001
  // 10 47.389
  // 11 8.0487
  // 12 6

  private static final long serialVersionUID = 1L;

  @CSVElement(value = 1, title = "Country", isMandatory = true)
  private String country;

  @CSVElement(value = 2, title = "ZIP")
  private String zipCode;

  @CSVElement(value = 3, title = "City", isMandatory = true)
  private String cityStr;

  @CSVElement(value = 4, ignore = true)
  private final String ignore1 = "";

  @CSVElement(value = 5, title = "Region")
  private String region;

  public GeonamesCityBean(Long primaryKeyNr) {
    super(primaryKeyNr);
  }

  @Override
  public void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {
  }

  @Override
  public void storeBeansToDatabase(List<AbstractDataBean> batch, IProgressMonitor monitor) throws ProcessingException {
    ImportMessageList result = BEANS.get(IDataExchangeService.class).storeGeonamesCity(batch);
    monitor.addErrors(result);
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {

  }

  @Override
  public List<Object> getData() {
    List<Object> result = new ArrayList<Object>();
    result.add(country);
    result.add(zipCode);
    result.add(cityStr);
    result.add(region);
    return result;
  }

  @Override
  public void setData(List<Object> data) {
    country = (String) data.get(0);
    zipCode = (String) data.get(1);
    cityStr = (String) data.get(2);
    region = (String) data.get(4);
  }

  public String getCountry() {
    return country;
  }

  public String getZipCode() {
    return zipCode;
  }

  public String getCityStr() {
    return cityStr;
  }

  public String getRegion() {
    return region;
  }

}
