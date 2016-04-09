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

public class GeneralCityBean extends AbstractCSVDataBean {

  // Zip
  // City
  // Area 
  // Region
  // Country

  private static final long serialVersionUID = 1L;

  @CSVElement(value = 1, title = "ZIP")
  private String zipCode;

  @CSVElement(value = 2, title = "City", isMandatory = true)
  private String cityStr;

  @CSVElement(value = 3, title = "Area")
  private String area;

  @CSVElement(value = 4, title = "Region")
  private String region;

  @CSVElement(value = 5, title = "Country", isMandatory = true)
  private String country;

  public GeneralCityBean(Long primaryKeyNr) {
    super(primaryKeyNr);
  }

  @Override
  public void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {
  }

  @Override
  public void storeBeansToDatabase(List<AbstractDataBean> batch, IProgressMonitor monitor) throws ProcessingException {
    ImportMessageList result = BEANS.get(IDataExchangeService.class).storeGeneralCity(batch);
    monitor.addErrors(result);
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {

  }

  @Override
  public List<Object> getData() {
    List<Object> result = new ArrayList<Object>();
    result.add(zipCode);
    result.add(cityStr);
    result.add(area);
    result.add(region);
    result.add(country);
    return result;
  }

  @Override
  public void setData(List<Object> data) {
    zipCode = (String) data.get(0);
    cityStr = (String) data.get(1);
    area = (String) data.get(2);
    region = (String) data.get(3);
    country = (String) data.get(4);
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

  public String getArea() {
    return area;
  }

}
