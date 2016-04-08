package com.rtiming.server.ecard.download;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.commons.holders.LongHolder;
import org.eclipse.scout.commons.holders.NVPair;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateDownloadedECardPermission;
import com.rtiming.shared.common.security.permission.DeleteDownloadedECardPermission;
import com.rtiming.shared.common.security.permission.ReadDownloadedECardPermission;
import com.rtiming.shared.common.security.permission.UpdateDownloadedECardPermission;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey;
import com.rtiming.shared.ecard.download.DownloadedECardFormData;
import com.rtiming.shared.ecard.download.IDownloadedECardProcessService;
import com.rtiming.shared.ecard.download.IPunchProcessService;
import com.rtiming.shared.ecard.download.PunchFormData;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.event.course.ICourseControlProcessService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.settings.IDefaultProcessService;

public class DownloadedECardProcessService  implements IDownloadedECardProcessService {

  @Override
  public DownloadedECardFormData prepareCreate(DownloadedECardFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateDownloadedECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getEvent().setValue(BEANS.get(IDefaultProcessService.class).getDefaultEventNr());
    formData.getEvtDownload().setValue(new Date());

    return formData;
  }

  @Override
  public DownloadedECardFormData create(DownloadedECardFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateDownloadedECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtPunchSessionKey key = RtPunchSessionKey.create((Long) null);
    RtPunchSession punchSession = new RtPunchSession();
    punchSession.setId(key);
    JPA.persist(punchSession);

    formData.setPunchSessionNr(punchSession.getId().getId());
    formData = store(formData);

    return formData;
  }

  @Override
  public DownloadedECardFormData load(DownloadedECardFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadDownloadedECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    LongHolder diffClear = new LongHolder();
    LongHolder diffCheck = new LongHolder();
    LongHolder diffStart = new LongHolder();
    LongHolder diffFinish = new LongHolder();

    JPAUtility.select("SELECT " +
        "PS.eventNr, " +
        "PS.eCardNr, " +
        "PS.stationNr, " +
        "PS.raceNr, " +
        "PS.evtDownload, " +
        "PS.ecardClear, " +
        "PS.ecardCheck, " +
        "PS.start, " +
        "PS.finish, " +
        "PS.rawData " +
        "FROM RtPunchSession PS " +
        "INNER JOIN PS.rtEvent " +
        "WHERE PS.id.punchSessionNr = :punchSessionNr " +
        "AND PS.id.clientNr = :sessionClientNr " +
        "INTO " +
        ":event, " +
        ":eCard, " +
        ":eCardStation, " +
        ":race, " +
        ":evtDownload, " +
        ":diffClear, " +
        ":diffCheck, " +
        ":diffStart, " +
        ":diffFinish," +
        ":rawData "
        , formData
        , new NVPair("diffClear", diffClear)
        , new NVPair("diffCheck", diffCheck)
        , new NVPair("diffStart", diffStart)
        , new NVPair("diffFinish", diffFinish)
        , formData
        );

    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(formData.getEvent().getValue());
    formData.getClear().setValue(FMilaUtility.addMilliSeconds(evtZero, diffClear.getValue()));
    formData.getCheck().setValue(FMilaUtility.addMilliSeconds(evtZero, diffCheck.getValue()));
    formData.getStart().setValue(FMilaUtility.addMilliSeconds(evtZero, diffStart.getValue()));
    formData.getFinish().setValue(FMilaUtility.addMilliSeconds(evtZero, diffFinish.getValue()));

    return formData;
  }

  @Override
  public DownloadedECardFormData store(DownloadedECardFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateDownloadedECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null) {
      return null;
    }

    Date zeroTime = null;

    Long diffClear = formData.getRawClear();
    Long diffCheck = formData.getRawCheck();
    Long diffStart = formData.getRawStart();
    Long diffFinish = formData.getRawFinish();

    if (isGUIClearTime(formData)) {
      zeroTime = getZeroTime(formData, zeroTime);
      diffClear = FMilaUtility.getDateDifferenceInMilliSeconds(zeroTime, formData.getClear().getValue());
    }
    if (isGUICheckTime(formData)) {
      zeroTime = getZeroTime(formData, zeroTime);
      diffCheck = FMilaUtility.getDateDifferenceInMilliSeconds(zeroTime, formData.getCheck().getValue());
    }
    if (isGUIStartTime(formData)) {
      zeroTime = getZeroTime(formData, zeroTime);
      diffStart = FMilaUtility.getDateDifferenceInMilliSeconds(zeroTime, formData.getStart().getValue());
    }
    if (isGUIFinishTime(formData)) {
      zeroTime = getZeroTime(formData, zeroTime);
      diffFinish = FMilaUtility.getDateDifferenceInMilliSeconds(zeroTime, formData.getFinish().getValue());
    }

    String queryString = "UPDATE RtPunchSession PS " +
        "SET eventNr = :event, " +
        "eCardNr = :eCardNr, " +
        "stationNr = :stationNr, " +
        "raceNr = :race, " +
        "evtDownload = :evtDownload, " +
        "ecardClear = :diffClear, " +
        "ecardCheck = :diffCheck, " +
        "start = :diffStart, " +
        "finish = :diffFinish, " +
        "rawData = :rawData " +
        "WHERE id.punchSessionNr = :punchSessionNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("diffClear", diffClear);
    query.setParameter("diffCheck", diffCheck);
    query.setParameter("diffStart", diffStart);
    query.setParameter("diffFinish", diffFinish);
    query.setParameter("eCardNr", formData.getECard().getValue());
    query.setParameter("stationNr", formData.getECardStation().getValue());
    query.executeUpdate();

    return formData;
  }

  @Override
  public DownloadedECardFormData delete(DownloadedECardFormData formData) throws ProcessingException {
    if (!ACCESS.check(new DeleteDownloadedECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null || formData.getPunchSessionNr() == null) {
      return formData;
    }

    formData = load(formData);

    BEANS.get(IPunchProcessService.class).delete(formData.getPunchSessionNr(), null);

    RtPunchSession punchSession = new RtPunchSession();
    punchSession.setId(RtPunchSessionKey.create(formData.getPunchSessionNr()));
    JPA.remove(punchSession);

    Long raceNr = formData.getRace().getValue();
    if (raceNr != null) {
      BEANS.get(IRaceService.class).reset(raceNr);
    }

    return formData;
  }

  private Date getZeroTime(DownloadedECardFormData formData, Date zeroTime) throws ProcessingException {
    if (zeroTime == null) {
      zeroTime = BEANS.get(IEventProcessService.class).getZeroTime(formData.getEvent().getValue());
    }
    return zeroTime;
  }

  private boolean isGUIClearTime(DownloadedECardFormData formData) {
    return (formData.getRawClear() == null && formData.getClear().getValue() != null);
  }

  private boolean isGUICheckTime(DownloadedECardFormData formData) {
    return (formData.getRawCheck() == null && formData.getCheck().getValue() != null);
  }

  private boolean isGUIStartTime(DownloadedECardFormData formData) {
    return (formData.getRawStart() == null && formData.getStart().getValue() != null);
  }

  private boolean isGUIFinishTime(DownloadedECardFormData formData) {
    return (formData.getRawFinish() == null && formData.getFinish().getValue() != null);
  }

  /**
   * find the best-matching class for given punches, event, sex and year of birth
   */
  @Override
  public Long[] matchClass(long punchSessionNr, List<PunchFormData> punches, long eventNr, Date evtZero, Long sexUid, Long year) throws ProcessingException {
    Set<EventClassFormData> clazzes = ClassMatchUtility.matchClasses(eventNr, sexUid, year, SharedCache.getEventConfiguration(), SharedCache.getAgeConfiguration());
    List<ClazzMatchCandidate> candidates = new ArrayList<>();
    for (EventClassFormData eventClazz : clazzes) {
      // course data
      Long courseNr = eventClazz.getCourse().getValue();
      List<List<CourseControlRowData>> courseData = BEANS.get(ICourseControlProcessService.class).getCourses(courseNr);

      // compare with punch data
      candidates.add(new ClazzMatchCandidate(eventClazz.getClazz().getValue(), eventClazz.getCourse().getValue(), courseData));
    }
    ClazzMatchCandidate winner = ClassMatchUtility.match(candidates, punches);

    if (winner == null) {
      return new Long[]{};
    }
    return new Long[]{winner.getClassUid()};
  }

}
