package com.rtiming.client.dataexchange.oe2003;

import java.util.ArrayList;
import java.util.Date;
import java.util.Hashtable;
import java.util.List;
import java.util.TreeSet;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.data.basic.table.SortSpec;

import com.rtiming.client.ClientSession;
import com.rtiming.client.dataexchange.AbstractCSVInterface;
import com.rtiming.client.entry.AbstractEntriesTablePage;
import com.rtiming.client.entry.EntriesTablePage;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.CourseFormData;
import com.rtiming.shared.event.course.ICourseProcessService;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;

public class OE2003StartListInterface extends AbstractCSVInterface<OE2003ResultBean> {

  private final Hashtable<Long, ITableRow> entryList;
  private final Hashtable<Long, EventClassFormData> clazzInfo;
  private final Hashtable<Long, CodeFormData> clazzCodeInfo;
  private final Hashtable<Long, CourseFormData> courseInfo;
  private final Hashtable<Long, Long> controlCount;
  private final Date evtZero;

  public OE2003StartListInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
    entryList = new Hashtable<Long, ITableRow>();
    clazzInfo = new Hashtable<Long, EventClassFormData>();
    clazzCodeInfo = new Hashtable<Long, CodeFormData>();
    courseInfo = new Hashtable<Long, CourseFormData>();
    controlCount = new Hashtable<Long, Long>();
    evtZero = BEANS.get(IEventProcessService.class).getZeroTime(getEventNr());
  }

  @Override
  public AbstractDataBean createNewBean(Long primaryKeyNr) {
    ITableRow row = null;
    EventClassFormData clazz = null;
    CourseFormData course = null;
    CodeFormData clazzCode = null;
    Long noOfControls = null;

    if (primaryKeyNr != null) {
      row = entryList.get(primaryKeyNr);
      Long classUid = ((AbstractEntriesTablePage.Table) row.getTable()).getClazzShortcutColumn().getValue(row);
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

    return new OE2003StartListBean(primaryKeyNr, row, info, getEventNr());
  }

  @Override
  public List<Long> getPrimaryKeyNrs() throws ProcessingException {

    EntriesTablePage entries = new EntriesTablePage(getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
    entries.nodeAddedNotify();
    SortSpec spec = sortByClassTimeName(entries);
    entries.getTable().getColumnSet().setSortSpec(spec);
    entries.loadChildren();

    TreeSet<Long> entryNrs = new TreeSet<Long>();

    // save all rows
    for (long k = 0; k < entries.getTable().getRowCount(); k++) {
      entryNrs.add(k);
      entryList.put(k, entries.getTable().getRow(NumberUtility.longToInt(k)));

      Long classUid = entries.getTable().getClazzShortcutColumn().getValue(NumberUtility.longToInt(k));

      if (clazzInfo.get(classUid) == null) {
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
      }

    }

    return new ArrayList<>(entryNrs);
  }

  private SortSpec sortByClassTimeName(AbstractEntriesTablePage entries) {
    int[] indices = new int[3];
    indices[0] = entries.getTable().getClazzShortcutColumn().getColumnIndex();
    indices[1] = entries.getTable().getRelativeStartTimeColumn().getColumnIndex();
    indices[2] = entries.getTable().getRunnerColumn().getColumnIndex();
    boolean[] sort = new boolean[3];
    sort[0] = true;
    sort[1] = true;
    sort[2] = true;
    SortSpec spec = new SortSpec(indices, sort);
    return spec;
  }

  @Override
  protected boolean getConfiguredTranslatedColumnHeaders() {
    return true;
  }

}
