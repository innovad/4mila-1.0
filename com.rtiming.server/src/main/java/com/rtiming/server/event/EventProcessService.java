package com.rtiming.server.event;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.TimeZone;

import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com._4mila._4mila.jaxws.online.FileData;
import com._4mila._4mila.jaxws.online.OnlineServiceSoap;
import com._4mila._4mila.jaxws.online.Result;
import com._4mila._4mila.jaxws.online.TableDataList;
import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.common.web.UploadUtility;
import com.rtiming.server.entry.SharedCacheServerUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.common.file.IFileProcessService;
import com.rtiming.shared.common.security.permission.CreateEventPermission;
import com.rtiming.shared.common.security.permission.ReadEventPermission;
import com.rtiming.shared.common.security.permission.UpdateEventPermission;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtMap;
import com.rtiming.shared.dao.util.UploadConfiguration;
import com.rtiming.shared.ecard.download.PunchingSystemCodeType;
import com.rtiming.shared.entry.startblock.StartblockCodeType;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.map.IMapProcessService;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.settings.DefaultCodeType;
import com.rtiming.shared.settings.DefaultFormData;
import com.rtiming.shared.settings.IDefaultProcessService;

public class EventProcessService implements IEventProcessService {

  private static final String SERVER_LOGO_DIR = "logo";

  @Override
  public EventBean prepareCreate(EventBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreateEventPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    bean.setPunchingSystemUid(PunchingSystemCodeType.SportIdentCode.ID);
    Date date = new Date();
    date = FMilaUtility.truncDateToHour(date);
    bean.setEvtZero(date);
    bean.setEvtFinish(DateUtility.addHours(date, 12));
    bean.setTimezone(TimeZone.getDefault().getOffset(date.getTime()));

    return bean;
  }

  @Override
  public EventBean create(EventBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreateEventPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtEventKey key = RtEventKey.create((Long) null);
    RtEvent event = new RtEvent();
    event.setId(key);
    JPA.persist(event);
    bean.setEventNr(event.getId().getId());
    bean = store(bean);

    return bean;
  }

  @Override
  public EventBean load(EventBean bean) throws ProcessingException {
    if (!ACCESS.check(new ReadEventPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT name, location, map, typeUid, evtZero, evtFinish, timezoneOffset, punchingSystemUid, format, evtLastUpload, id.clientNr " + "FROM RtEvent " + "WHERE id.eventNr = :eventNr " + "AND id.clientNr = COALESCE(:clientNr,:sessionClientNr) " + "INTO :name, :location, :map, :typeUid, :evtZero, :evtFinish, :timezone, :punchingSystemUid, :format, :evtLastUpload, :clientNr ", bean);

    // load logo
    bean.setLogoData(BEANS.get(IFileProcessService.class).loadFile(bean.getEventNr(), NumberUtility.nvl(bean.getClientNr(), ServerSession.get().getSessionClientNr()), bean.getFormat(), SERVER_LOGO_DIR));

    return bean;
  }

  @Override
  public EventBean store(EventBean bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateEventPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    bean.setEvtZero(DateUtility.truncDateToMinute(bean.getEvtZero()));
    bean.setEvtFinish(DateUtility.truncDateToMinute(bean.getEvtFinish()));

    String queryString = "UPDATE RtEvent " + "SET name = :name, " + "location = :location, " + "map = :map, " + "typeUid = :typeUid, " + "evtZero = :evtZero, " + "evtFinish = :evtFinish, " + "timezoneOffset = :timezone, " + "punchingSystemUid = :punchingSystemUid, " + "format = :format, " + "evtLastUpload = :evtLastUpload " + "WHERE id.eventNr = :eventNr " + "AND id.clientNr = :clientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, bean);
    query.setParameter("clientNr", NumberUtility.nvl(bean.getClientNr(), ServerSession.get().getSessionClientNr()));
    query.executeUpdate();

    // save logo
    BEANS.get(IFileProcessService.class).writeDataToFile(bean.getEventNr(), NumberUtility.nvl(bean.getClientNr(), ServerSession.get().getSessionClientNr()), bean.getFormat(), bean.getLogoData(), SERVER_LOGO_DIR);

    // set default if none
    if (BEANS.get(IDefaultProcessService.class).getDefaultEventNr() == null) {
      DefaultFormData defaultEvent = new DefaultFormData();
      defaultEvent.getValueInteger().setValue(bean.getEventNr());
      defaultEvent.getDefaultUid().setValue(DefaultCodeType.DefaultEventCode.ID);
      BEANS.get(IDefaultProcessService.class).create(defaultEvent);
    }

    SharedCacheServerUtility.notifyClients();

    return bean;
  }

  @Override
  public Date getZeroTime(long eventNr) throws ProcessingException {
    String queryString = "SELECT MAX(E.evtZero) " + "FROM RtEvent E " + "WHERE E.id.eventNr = :eventNr " + "AND E.id.clientNr = :clientNr ";

    FMilaTypedQuery<Date> query = JPA.createQuery(queryString, Date.class);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("eventNr", eventNr);
    Date evtZero = query.getSingleResult();

    return evtZero;
  }

  @Override
  public long getRunnerStartedCount(long eventNr, Long classUid, Long courseNr) throws ProcessingException {
    String queryString = "SELECT COUNT(RA.id.raceNr) " + "FROM RtRace RA " + "INNER JOIN RA.rtEventClass EC " + "WHERE RA.eventNr = :eventNr " + (classUid != null ? "AND RA.legClassUid = :classUid " : "") + (courseNr != null ? "AND EC.courseNr = :courseNr " : "") + "AND COALESCE(RA.statusUid,0) != " + RaceStatusCodeType.DidNotStartCode.ID;
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("eventNr", eventNr);
    if (classUid != null) {
      query.setParameter("classUid", classUid);
    }
    if (courseNr != null) {
      query.setParameter("courseNr", courseNr);
    }
    Long count = query.getSingleResult();

    return NumberUtility.nvl(count, 0);
  }

  @Override
  public void syncWithOnline(long eventNr) throws ProcessingException {

    OnlineServiceSoap onlineService = null; // BEANS.get(OnlineServiceClient.class).getPortType(); // TODO MIG
    TableDataList uploadData = new TableDataList();

    EventBean event = new EventBean();
    event.setEventNr(eventNr);
    event = load(event);

    // upload all tables
    Set<EntityType<?>> entities = JPA.currentEntityManager().getMetamodel().getEntities();
    List<Class<?>> orderedEntities = new ArrayList<Class<?>>();
    for (EntityType<?> entity : entities) {
      if (entity.getJavaType().getAnnotation(UploadConfiguration.class) != null) {
        orderedEntities.add(entity.getJavaType());
      }
    }

    Comparator<? super Class<?>> comparator = new Comparator<Object>() {
      @Override
      public int compare(Object o1, Object o2) {
        if (o1 != null && o1 instanceof Class && o2 != null && o2 instanceof Class) {
          Class<?> c1 = (Class) o1;
          Class<?> c2 = (Class) o2;
          long s1 = c1.getAnnotation(UploadConfiguration.class).uploadOrder();
          long s2 = c2.getAnnotation(UploadConfiguration.class).uploadOrder();
          return Long.valueOf(s1).compareTo(Long.valueOf(s2));
        }
        return 0;
      }
    };
    Collections.sort(orderedEntities, comparator);

    for (Class<?> entity : orderedEntities) {
      String tableName = entity.getAnnotation(Table.class).name();
      String[] filter = entity.getAnnotation(UploadConfiguration.class).filteredColumns();
      boolean cleanup = entity.getAnnotation(UploadConfiguration.class).cleanup();
      uploadData.getTables().add(UploadUtility.createUploadTable(tableName, filter, eventNr, cleanup));
    }

    // add map files
    RtMap map = BEANS.get(IMapProcessService.class).findMap(eventNr, ServerSession.get().getSessionClientNr());
    if (map.getId() != null && map.getId().getId() != null && map.getEvtFileLastUpdate() != null) {
      FileData fileData = new FileData();
      fileData.setClientNr(map.getId().getClientNr());
      if (event.getEvtLastUpload() == null || map.getEvtFileLastUpdate().after(event.getEvtLastUpload())) {
        fileData.setContent(map.getMapData());
      }
      fileData.setFormat(map.getFormat());
      fileData.setPath(IMapProcessService.SERVER_MAP_DIR);
      fileData.setPkNr(map.getId().getId());
      uploadData.getFiles().add(fileData);
    }

    try {
      Result result = onlineService.upload("username", "pw", ServerSession.get().getSessionClientNr(), eventNr, uploadData);
      if (result.getStatusNr() != 0) {
        throw new ProcessingException("Upload failed: " + result.getStatusMessage());
      }

      // update last update date
      updateEvtLastUpload(eventNr, ServerSession.get().getSessionClientNr());
    }
    catch (Exception e) {
      throw new VetoException(TEXTS.get("GlobalServerConnectionFailure", e.getMessage()));
    }

  }

  private void updateEvtLastUpload(Long eventNr, Long clientNr) throws ProcessingException {
    EventBean event = new EventBean();
    event.setEventNr(eventNr);
    event.setClientNr(clientNr);
    event = load(event);

    event.setEvtLastUpload(new Date());
    store(event);
  }

  @Override
  public void delete(Long eventNr, boolean cleanup) throws ProcessingException {
    if (eventNr == null) {
      return;
    }

    String queryString = "DELETE FROM RtPunch P " + "WHERE P.id.punchSessionNr IN (SELECT PS.id.punchSessionNr " + "                           FROM RtPunchSession PS " + "                           WHERE PS.raceNr IN (SELECT R.id.raceNr " + "                                               FROM RtRace R " + "                                               WHERE R.eventNr = :eventNr " + "                                               AND R.id.clientNr = :sessionClientNr) " + "                           AND PS.id.clientNr = :sessionClientNr) " + "AND P.id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtPunchSession PS WHERE PS.raceNr IN (SELECT R.id.raceNr " + "                                                      FROM RtRace R " + "                                                      WHERE R.eventNr = :eventNr" + "                                                      AND R.id.clientNr = :sessionClientNr) " + "AND PS.id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtPunchSession PS WHERE PS.eventNr  = :eventNr " + "AND PS.id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtRaceControl RC WHERE RC.raceNr IN (SELECT R.id.raceNr " + "                                                     FROM RtRace R " + "                                                     WHERE R.eventNr = :eventNr" + "                                                     AND R.id.clientNr = :sessionClientNr) " + "AND CLIENT_NR = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtRace R WHERE R.eventNr = :eventNr AND R.id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtParticipation P WHERE P.id.eventNr = :eventNr AND P.id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtEventClass EC WHERE EC.id.eventNr = :eventNr AND EC.id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtEventStartblock ES WHERE ES.id.eventNr = :eventNr AND ES.id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtEventAdditionalInformation EAI WHERE EAI.id.eventNr = :eventNr AND EAI.id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtCourseControl CC WHERE CC.controlNr IN (SELECT C.id.controlNr FROM RtControl C WHERE C.eventNr = :eventNr AND C.id.clientNr = :sessionClientNr) " + " AND CLIENT_NR = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtCourse C WHERE C.eventNr = :eventNr AND C.id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtControlReplacement WHERE id.controlNr IN (SELECT id.controlNr " + "                                                               FROM RtControl " + "                                                               WHERE eventNr = :eventNr" + "                                                               AND id.clientNr = :sessionClientNr) " + " AND CLIENT_NR = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtControl WHERE eventNr = :eventNr AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtEventMap WHERE id.eventNr = :eventNr AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtStartlistSettingVacant WHERE id.startlistSettingNr IN (SELECT id.startlistSettingNr " + "                                                                              FROM RtStartlistSetting " + "                                                                              WHERE eventNr = :eventNr " + "                                                                              AND id.clientNr = :sessionClientNr) " + " AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtStartlistSettingOption WHERE id.startlistSettingNr IN (SELECT id.startlistSettingNr " + "                                                                              FROM RtStartlistSetting " + "                                                                              WHERE eventNr = :eventNr " + "                                                                              AND id.clientNr = :sessionClientNr) " + " AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtStartlistSetting WHERE eventNr = :eventNr AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtEvent WHERE id.eventNr = :eventNr AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    queryString = "DELETE FROM RtDefault WHERE valueInteger = :eventNr " + "AND id.defaultUid = " + DefaultCodeType.DefaultEventCode.ID + " " + "AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString, eventNr);

    if (cleanup) {
      cleanupDatabase();
    }

    SharedCacheServerUtility.notifyClients();
  }

  private void executeDeleteQuery(String queryString) throws ProcessingException {
    executeDeleteQuery(queryString, null);
  }

  private void executeDeleteQuery(String queryString, Long eventNr) throws ProcessingException {
    FMilaQuery query = JPA.createQuery(queryString);
    if (eventNr != null) {
      query.setParameter("eventNr", eventNr);
    }
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();
  }

  private void cleanupDatabase() throws ProcessingException {
    String queryString = "DELETE FROM RtRace WHERE entryNr IN (SELECT E.id.entryNr " + "                                              FROM RtEntry E WHERE 0 = (SELECT COUNT(P.id.entryNr) " + "                                                                         FROM RtParticipation P " + "                                                                         WHERE P.id.entryNr = E.id.entryNr " + "                                                                         AND id.clientNr = :sessionClientNr)" + "                                              AND id.clientNr = :sessionClientNr) " + "AND id.clientNr = :sessionClientNr ";
    executeDeleteQuery(queryString);

    queryString = "DELETE FROM RtEntry WHERE id.entryNr IN (SELECT E.id.entryNr " + "                                               FROM RtEntry E WHERE 0 = (SELECT COUNT(P.id.entryNr) " + "                                                                          FROM RtParticipation P " + "                                                                          WHERE P.id.entryNr = E.id.entryNr " + "                                                                          AND id.clientNr = :sessionClientNr)" + "                                               AND id.clientNr = :sessionClientNr) " + "AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString);

    queryString = "DELETE FROM RtPayment WHERE registrationNr IN (SELECT id.registrationNr " + "                                                        FROM RtRegistration R " + "                                                        WHERE NOT EXISTS (SELECT 1 " + "                                                                          FROM RtEntry E " + "                                                                          WHERE E.registrationNr = R.id.registrationNr" + "                                                                          AND id.clientNr = :sessionClientNr)" + "                                                        AND id.clientNr = :sessionClientNr) " + "AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString);

    queryString = "DELETE FROM RtRegistration R " + "WHERE NOT EXISTS (SELECT 1 " + "                  FROM RtEntry E " + "                  WHERE E.registrationNr = R.id.registrationNr" + "                  AND id.clientNr = :sessionClientNr) " + "AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString);

    queryString = "DELETE FROM RtRunner " + "WHERE id.runnerNr NOT IN (SELECT COALESCE(runnerNr,0) " + "                        FROM RtRace" + "                        WHERE id.clientNr = :sessionClientNr) " + "AND id.runnerNr NOT IN (SELECT COALESCE(contactRunnerNr,0) " + "                        FROM RtClub" + "                        WHERE id.clientNr = :sessionClientNr) " + "AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString);

    queryString = "DELETE FROM RtAddress " + "WHERE id.addressNr NOT IN (SELECT COALESCE(addressNr,0) " + "                         FROM RtRunner " + "                         WHERE id.clientNr = :sessionClientNr) " + "AND id.addressNr NOT IN (SELECT COALESCE(addressNr,0) " + "                         FROM RtRace " + "                         WHERE id.clientNr = :sessionClientNr) " + "AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString);

    queryString = "DELETE FROM RtEcard WHERE id.eCardNr NOT IN (SELECT COALESCE(R.eCardNr,0) FROM RtRunner R " + "                                                   WHERE id.clientNr = :sessionClientNr)" + "AND id.eCardNr NOT IN ( " + "                                                   SELECT COALESCE(RA.eCardNr,0) FROM RtRace RA " + "                                                   WHERE id.clientNr = :sessionClientNr) " + "AND id.eCardNr NOT IN ( " + "                                                   SELECT COALESCE(PS.eCardNr,0) FROM RtPunchSession PS " + "                                                   WHERE id.clientNr = :sessionClientNr" + "                                                   ) " + "AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString);

    queryString = "DELETE FROM RtClub " + "WHERE id.clubNr NOT IN (SELECT COALESCE(clubNr,0) " + "                      FROM RtRunner " + "                      WHERE id.clientNr = :sessionClientNr) " + "AND id.clubNr NOT IN (SELECT COALESCE(clubNr,0) " + "                      FROM RtRace" + "                      WHERE id.clientNr = :sessionClientNr) " + "AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString);

    queryString = "DELETE FROM RtUcl " + "WHERE id.ucUid IN (SELECT id.ucUid " + "                 FROM RtUc " + "                 WHERE codeType = " + StartblockCodeType.ID + " " + "                 AND id.clientNr = :sessionClientNr " + "                 AND UC_UID NOT IN (SELECT id.startblockUid " + "                                    FROM RtEventStartblock " + "                                    WHERE id.clientNr = :sessionClientNr)" + "                )" + "AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString);

    queryString = "DELETE FROM RtUc " + "       WHERE codeType = " + StartblockCodeType.ID + " " + "       AND id.ucUid NOT IN (SELECT id.startblockUid " + "                          FROM RtEventStartblock" + "                          WHERE id.clientNr = :sessionClientNr)" + "       AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString);

    queryString = "DELETE FROM RtFee " + "WHERE feeGroupNr IN (SELECT id.feeGroupNr " + "                       FROM RtFeeGroup " + "                       WHERE id.feeGroupNr NOT IN (SELECT COALESCE(feeGroupNr,0) " + "                                                  FROM RtEventClass " + "                                                  WHERE id.clientNr = :sessionClientNr) " + "                       AND id.feeGroupNr NOT IN ( " + "                                                  SELECT COALESCE(feeGroupNr,0) " + "                                                  FROM RtAdditionalInformationDef " + "                                                  WHERE id.clientNr = :sessionClientNr) " + "                       AND id.clientNr = :sessionClientNr) " + "AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString);

    queryString = "DELETE FROM RtFeeGroup " + "WHERE id.feeGroupNr NOT IN (SELECT COALESCE(feeGroupNr,0) " + "                           FROM RtEventClass " + "                           WHERE id.clientNr = :sessionClientNr) " + "AND id.feeGroupNr NOT IN ( " + "                           SELECT COALESCE(feeGroupNr,0) " + "                           FROM RtAdditionalInformationDef " + "                           WHERE id.clientNr = :sessionClientNr) " + "AND id.clientNr = :sessionClientNr";
    executeDeleteQuery(queryString);
  }
}
