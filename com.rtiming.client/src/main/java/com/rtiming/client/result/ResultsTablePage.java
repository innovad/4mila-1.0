package com.rtiming.client.result;

import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.ISearchForm;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.results.IResultsOutlineService;
import com.rtiming.shared.results.ResultRowData;

public class ResultsTablePage extends AbstractPageWithTable<ResultsTablePage.Table> implements IHelpEnabledPage {

  private final Long m_clientNr;
  private final Long m_classUid;
  private final Long m_classTypeUid;
  private final Long m_clubNr;
  private final Long m_courseNr;

  public ResultsTablePage(Long clientNr, Long classUid, Long classTypeUid, Long courseNr, Long clubNr) {
    super(createUserPreferenceContext(clientNr, classUid, classTypeUid, courseNr, clubNr));
    m_clientNr = clientNr;
    m_classUid = classUid;
    m_classTypeUid = classTypeUid;
    m_courseNr = courseNr;
    m_clubNr = clubNr;
  }

  public ResultsTablePage(Long clientNr, Long classUid, Long courseNr, Long clubNr) {
    this(clientNr, classUid, null, courseNr, clubNr);
  }

  @Override
  protected void execInitPage() throws ProcessingException {
    if (ClassTypeCodeType.isRelayType(m_classTypeUid)) {
      getCellForUpdate().setText(TEXTS.get("LegResults"));
    }
    if (isClubResultList()) {
      getTable().getClubColumn().setInitialVisible(false);
      getTable().getClassNameColumn().setInitialVisible(true);
    }
    if (isCourseResultList()) {
      getTable().getClassNameColumn().setInitialVisible(true);
      getTable().getCityColumn().setInitialVisible(false); // save space
    }

    getTable().resetColumnVisibilities();
  }

  private boolean isClubResultList() {
    return m_clubNr != null;
  }

  private boolean isCourseResultList() {
    return m_courseNr != null;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  public Long getClassUid() {
    return m_classUid;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Results");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<ResultRowData> list = BEANS.get(IResultsOutlineService.class).getResultTableData(m_clientNr, m_classUid, m_courseNr, m_clubNr, filter);
    importTableData(ResultsTableUtility.convertListToObjectArray(list, getTable()));
  }

  @Order(10.0)
  public class Table extends AbstractResultsTable {

    @Override
    protected void execDecorateRow(ITableRow row) throws ProcessingException {
      if (isClubResultList()) {
        if (CompareUtility.equals(getRankColumn().getValue(row), 1L)) {
          row.setFont(FontSpec.parse("bold"));
        }
      }
    }

  }

  @Override
  protected Class<? extends ISearchForm> getConfiguredSearchForm() {
    return SingleEventSearchForm.class;
  }

}
