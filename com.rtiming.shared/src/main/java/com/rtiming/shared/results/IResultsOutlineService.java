package com.rtiming.shared.results;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.shared.dao.RtEcardStation;
import com.rtiming.shared.ecard.download.DownloadedECards;
import com.rtiming.shared.ecard.download.DownloadedECardsSearchFormData;

@TunnelToServer
public interface IResultsOutlineService extends IService {

  List<ResultRowData> getResultTableData(Long clientNr, Long classUid, Long courseNr, Long clubNr, SearchFilter filter) throws ProcessingException;

  Object[][] getDownloadedECardTableData(DownloadedECards presentationType, DownloadedECardsSearchFormData searchFormData) throws ProcessingException;

  List<RtEcardStation> getECardStationTableData() throws ProcessingException;

  Object[][] getPunchTableData(Long punchSessionNr) throws ProcessingException;

  SplitTimeReportData getSplitTimesReportData(long raceNr) throws ProcessingException;

  Object[][] getResultClubTableData(SearchFilter filter) throws ProcessingException;

  List<ResultClazzRowData> getResultClassTableData(Long clientNr, SearchFilter filter) throws ProcessingException;

  Object[][] getResultCourseTableData(SearchFilter filter) throws ProcessingException;
}
