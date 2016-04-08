package com.rtiming.client.dataexchange.iof3;

import java.util.ArrayList;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.dataexchange.AbstractXMLInterface;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.iof3.xml.CourseData;

public class IOF300CourseImporter extends AbstractXMLInterface {

  public IOF300CourseImporter(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
    this.setJaxbObject(new CourseData());
  }

  @Override
  public ArrayList<IOF300CourseDataBean> createBeansFromXMLObject(Object xmlData) throws ProcessingException {

    ArrayList<IOF300CourseDataBean> list = new ArrayList<IOF300CourseDataBean>();
    IOF300CourseDataBean bean = new IOF300CourseDataBean(getEventNr(), new CourseDataAccess(getEventNr()));

    CourseData courseData = (CourseData) xmlData;
    bean.setCourseData(courseData);
    list.add(bean);

    return list;

  }

  @Override
  public ArrayList<String> getColumnHeaders() {
    ArrayList<String> list = new ArrayList<String>();
    list.add(TEXTS.get("Control"));
    list.add(TEXTS.get("Course"));
    list.add(TEXTS.get("Class"));
    list.add(TEXTS.get("Map"));
    return list;
  }

  @Override
  public AbstractDataBean createNewBean(Long primaryKeyNr) {
    return new IOF300CourseDataBean(primaryKeyNr, new CourseDataAccess(getEventNr()));
  }

  @Override
  protected boolean isUseXmlFilter() {
    return true;
  }

}
