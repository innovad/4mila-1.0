package com.rtiming.client.common.report.template;

import java.util.HashMap;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPageWithTable;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.ClientSession;
import com.rtiming.client.entry.EntriesTablePage;
import com.rtiming.client.ranking.RankingEventResultsTablePage;
import com.rtiming.client.result.ResultsTablePage;
import com.rtiming.shared.services.code.ReportTypeCodeType.RankingsCode;
import com.rtiming.shared.services.code.ReportTypeCodeType.ResultsCode;
import com.rtiming.shared.services.code.ReportTypeCodeType.StartListCode;

/**
 * 
 */
public class ReportTemplateTablePageMapping {

  private static HashMap<Long, IPageWithTable<?>> map;

  private ReportTemplateTablePageMapping() throws ProcessingException {
  }

  private static void initCache() throws ProcessingException {
    map = new HashMap<>();
    map.put(StartListCode.ID, new EntriesTablePage(0L, ClientSession.get().getSessionClientNr(), 0L, 0L, 0L, 0L));
    map.put(ResultsCode.ID, new ResultsTablePage(ClientSession.get().getSessionClientNr(), 0L, 0L, 0L));
    map.put(RankingsCode.ID, new RankingEventResultsTablePage(null, null, null));
  }

  public static IPageWithTable<?> getTableForReportType(Long id) throws ProcessingException {
    if (map == null) {
      initCache();
    }
    return map.get(id);
  }
}
