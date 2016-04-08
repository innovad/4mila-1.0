package com.rtiming.shared.event;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.club.ClubRowData;
import com.rtiming.shared.club.ClubsSearchFormData;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtMap;
import com.rtiming.shared.ecard.ECardsSearchFormData;
import com.rtiming.shared.event.course.ControlsSearchFormData;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.event.course.ReplacementControlRowData;
import com.rtiming.shared.runner.RunnerRowData;
import com.rtiming.shared.runner.RunnersSearchFormData;

@TunnelToServer
public interface IEventsOutlineService extends IService {

  List<RtEcard> getECardTableData(ECardsSearchFormData formData) throws ProcessingException;

  List<ClubRowData> getClubTableData(ClubsSearchFormData formData) throws ProcessingException;

  List<RunnerRowData> getRunnerTableData(RunnersSearchFormData formData) throws ProcessingException;

  List<EventRowData> getEventTableData(Long clientNr, EventsSearchFormData formData) throws ProcessingException;

  Object[][] getEventClassTableData(Long eventNr, Long parentClassUid) throws ProcessingException;

  Object[][] getCourseTableData(Long eventNr) throws ProcessingException;

  List<CourseControlRowData> getCourseControlTableData(Long courseNr) throws ProcessingException;

  Object[][] getControlTableData(Long eventNr, ControlsSearchFormData formData) throws ProcessingException;

  List<RaceControlRowData> getRaceControlTableData(Long clientNr, Long... raceNr) throws ProcessingException;

  List<ReplacementControlRowData> getReplacementControlTableData(long controlNr) throws ProcessingException;

  List<RtMap> getMapTableData(Long eventNr) throws ProcessingException;

  Object[][] getEventAdditionalInformationTableData(long eventNr) throws ProcessingException;

  Object[][] getRankingTableData() throws ProcessingException;

  Object[][] getRankingEventTableData(Long rankingNr) throws ProcessingException;

  Object[][] getRankingClassesTableData(Long rankingNr) throws ProcessingException;

}
