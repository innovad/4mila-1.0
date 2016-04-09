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

public class SwissPostCityBean extends AbstractCSVDataBean {

  // 1 104
  // 2 20
  // 3 1000
  // 4 0
  // 5 Lausanne
  // 6 Lausanne
  // 7 VD
  // 8 2
  // 9 0
  // 10 130
  // 11 5586
  // 12 19880301

  private static final long serialVersionUID = 1L;

  @CSVElement(value = 1, ignore = true)
  private String ignore1;

  @CSVElement(value = 2, ignore = true)
  private String ignore2;

  @CSVElement(value = 3, isMandatory = true, maxLength = 4, minLength = 4, title = "PLZ")
  private String zip;

  @CSVElement(value = 4, ignore = true)
  private String ignore3;

  @CSVElement(value = 5, isMandatory = true, title = "Ort")
  private String city;

  @CSVElement(value = 6, ignore = true)
  private String ignore4;

  @CSVElement(value = 7, isMandatory = true, title = "Kanton")
  private String region;

  public SwissPostCityBean(Long primaryKeyNr) {
    super(primaryKeyNr);
  }

  @Override
  public void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {
  }

  @Override
  public void storeBeansToDatabase(List<AbstractDataBean> batch, IProgressMonitor monitor) throws ProcessingException {
    ImportMessageList result = BEANS.get(IDataExchangeService.class).storeSwissPostCity(batch);
    monitor.addErrors(result);
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {

  }

  @Override
  public List<Object> getData() {
    List<Object> result = new ArrayList<Object>();
    result.add(zip);
    result.add(city);
    result.add(region);
    return result;
  }

  @Override
  public void setData(List<Object> data) {
    zip = (String) data.get(2);
    city = (String) data.get(4);
    region = (String) data.get(6);
  }

  public String getZip() {
    return zip;
  }

  public String getCity() {
    return city;
  }

  public String getRegion() {
    return region;
  }

}
