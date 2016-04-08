package com.rtiming.client.dataexchange.oe2003;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.client.ClientSession;
import com.rtiming.client.dataexchange.AbstractCSVInterface;
import com.rtiming.client.race.RaceControlsTablePage;
import com.rtiming.client.result.ResultsClassesTablePage;
import com.rtiming.client.result.ResultsTablePage;
import com.rtiming.client.result.SingleEventSearchForm;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.ICourseProcessService;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;

public class OE2003ResultListInterface extends AbstractCSVInterface<OE2003ResultBean> {

  private final Hashtable<Long, ResultsTablePage> resultsTables;
  private final Hashtable<Long, RaceControlsTablePage> splitsTables;
  private final Hashtable<Long, Long> rowToClassUidMapper;
  private final Hashtable<Long, Long> classUidFirstKey;
  private final Hashtable<Long, EventClassFormData> clazzInfo;
  private final Hashtable<Long, CodeFormData> clazzCodeInfo;
  private final Hashtable<Long, CourseFormData> courseInfo;
  private final Hashtable<Long, Long> controlCount;

  public OE2003ResultListInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
    resultsTables = new Hashtable<Long, ResultsTablePage>();
    splitsTables = new Hashtable<Long, RaceControlsTablePage>();
    rowToClassUidMapper = new Hashtable<Long, Long>();
    classUidFirstKey = new Hashtable<Long, Long>();
    clazzInfo = new Hashtable<Long, EventClassFormData>();
    clazzCodeInfo = new Hashtable<Long, CodeFormData>();
    courseInfo = new Hashtable<Long, CourseFormData>();
    controlCount = new Hashtable<Long, Long>();
  }

  @Override
  public AbstractDataBean createNewBean(Long primaryKeyNr) {
    ITableRow row = null;
    List<ITableRow> splits = new ArrayList<ITableRow>();
    EventClassFormData clazz = null;
    CourseFormData course = null;
    CodeFormData clazzCode = null;
    Long noOfControls = null;
    if (primaryKeyNr != null) {
      Long classUid = rowToClassUidMapper.get(primaryKeyNr);
      ResultsTablePage results = resultsTables.get(classUid);
      Long firstKey = classUidFirstKey.get(classUid);
      Integer rowNr = NumberUtility.longToInt(primaryKeyNr - firstKey);
      RaceControlsTablePage splitTable = splitsTables.get(classUid);
      row = results.getTable().getRow(rowNr);
      Long raceNr = ((ResultsTablePage.Table) row.getTable()).getRaceNrColumn().getValue(row);
      for (int i = 0; i < splitTable.getTable().getRowCount(); i++) {
        if (CompareUtility.equals(splitTable.getTable().getRaceNrColumn().getValue(i), raceNr)) {
          splits.add(splitTable.getTable().getRow(i));
        }
      }
      clazz = clazzInfo.get(classUid);
      clazzCode = clazzCodeInfo.get(classUid);
      course = courseInfo.get(classUid);
      noOfControls = controlCount.get(classUid);
    }
    OE2003ClassInfo info = new OE2003ClassInfo();
    info.setClazz(clazz);
    info.setClazzCode(clazzCode);
    info.setCourse(course);
    info.setControlCount(noOfControls);
    info.setLanguageUid(getLanguageUid());
    return new OE2003ResultBean(primaryKeyNr, row, splits, info, getEventNr());
  }

  @Override
  public List<Long> getPrimaryKeyNrs() throws ProcessingException {

    ResultsClassesTablePage classes = new ResultsClassesTablePage(ClientSession.get().getSessionClientNr());
    SingleEventSearchForm search = (SingleEventSearchForm) classes.getSearchFormInternal();
    search.getEventField().setValue(getEventNr());
    search.resetSearchFilter();
    classes.loadChildren();

    ArrayList<Long> classUids = new ArrayList<Long>();
    for (int k = 0; k < classes.getTable().getRowCount(); k++) {
      if (NumberUtility.nvl(classes.getTable().getProcessedColumn().getValue(k), 0) > 0 && classes.getTable().getClassUidColumn().getValue(k) != null) {
        classUids.add(classes.getTable().getClassUidColumn().getValue(k));
      }
    }

    long rowCounter = 0;
    for (Long classUid : classUids) {
      // Load Result List
      ResultsTablePage results = new ResultsTablePage(ClientSession.get().getSessionClientNr(), classUid, null, null);
      SingleEventSearchForm eventSearch = (SingleEventSearchForm) results.getSearchFormInternal();
      eventSearch.getEventField().setValue(getEventNr());
      eventSearch.resetSearchFilter();
      results.loadChildren();

      resultsTables.put(classUid, results);
      classUidFirstKey.put(classUid, rowCounter);
      for (long k = 0; k < results.getTable().getRowCount(); k++) {
        rowToClassUidMapper.put(rowCounter, classUid);
        rowCounter++;
      }

      // Load Splits for all Races
      RaceControlsTablePage splits = new RaceControlsTablePage(ClientSession.get().getSessionClientNr(), results.getTable().getRaceNrColumn().getValues().toArray(new Long[0]));
      splits.loadChildren();

      splitsTables.put(classUid, splits);

      // Load Class and Course Info
      EventClassFormData clazz = new EventClassFormData();
      clazz.getClazz().setValue(classUid);
      clazz.getEvent().setValue(getEventNr());
      clazz = BEANS.get(IEventClassProcessService.class).load(clazz);
      clazzInfo.put(classUid, clazz);

      CourseFormData course = new CourseFormData();
      if (clazz.getCourse().getValue() != null) {
        course.setCourseNr(clazz.getCourse().getValue());
        course = BEANS.get(ICourseProcessService.class).load(course);
        controlCount.put(classUid, BEANS.get(ICourseProcessService.class).getControlCount(course.getCourseNr()));
      }
      courseInfo.put(classUid, course);

      // Clazz CodeFormData for Translation / Ext Key
      CodeFormData clazzCode = new CodeFormData();
      clazzCode.setCodeType(ClassCodeType.ID);
      clazzCode.setCodeUid(classUid);
      clazzCode = BEANS.get(ICodeProcessService.class).load(clazzCode);
      clazzCodeInfo.put(classUid, clazzCode);

      controlCount.put(classUid, BEANS.get(ICourseProcessService.class).getControlCount(course.getCourseNr()));
    }

    List<Long> rows = new ArrayList<Long>();
    for (long k = 0; k < rowCounter; k++) {
      rows.add(k);
    }

    return rows;
  }

  @Override
  protected boolean getConfiguredTranslatedColumnHeaders() {
    return true;
  }

}
