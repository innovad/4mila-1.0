package com.rtiming.client.dataexchange.iof;

import java.util.ArrayList;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.dataexchange.AbstractXMLInterface;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.iof203.xml.CourseData;

public class IOF203CourseImporter extends AbstractXMLInterface {

  public IOF203CourseImporter(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
    this.setJaxbObject(new CourseData());
  }

  @Override
  public ArrayList<IOF203CourseDataBean> createBeansFromXMLObject(Object xmlData) throws ProcessingException {

    ArrayList<IOF203CourseDataBean> list = new ArrayList<IOF203CourseDataBean>();
    IOF203CourseDataBean bean = new IOF203CourseDataBean(getEventNr(), getEventNr());

    CourseData courseData = (CourseData) xmlData;

    bean.setControlsList(courseData.getControl());
    bean.setFinishPointList(courseData.getFinishPoint());
    bean.setStartPointList(courseData.getStartPoint());
    bean.setCourseList(courseData.getCourse());
    bean.setMap(courseData.getMap());

    list.add(bean);

    return list;

  }

  @Override
  public ArrayList<String> getColumnHeaders() {
    ArrayList<String> list = new ArrayList<String>();
    list.add(TEXTS.get("Start"));
    list.add(TEXTS.get("Control"));
    list.add(TEXTS.get("Finish"));
    list.add(TEXTS.get("Courses"));
    return list;
  }

  @Override
  public AbstractDataBean createNewBean(Long primaryKeyNr) {
    return new IOF203CourseDataBean(primaryKeyNr, getEventNr());
  }

}
