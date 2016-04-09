package com.rtiming.server.event;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.entry.SharedCacheServerUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateEventClassPermission;
import com.rtiming.shared.common.security.permission.ReadEventClassPermission;
import com.rtiming.shared.common.security.permission.UpdateEventClassPermission;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.race.TimePrecisionCodeType;
import com.rtiming.shared.services.code.CourseGenerationCodeType;

public class EventClassProcessService  implements IEventClassProcessService {

  @Override
  public EventClassFormData prepareCreate(EventClassFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateEventClassPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getTimePrecision().setValue(TimePrecisionCodeType.Precision1sCode.ID);
    formData.getType().setValue(ClassTypeCodeType.IndividualEventCode.ID);
    formData.getCourseGenerationType().setValue(CourseGenerationCodeType.CourseGenerationUseCourseTemplateCode.ID);

    // generate continuing sortcode
    String queryString = "SELECT MAX(sortcode) " +
        "FROM RtEventClass EC " +
        "WHERE EC.id.eventNr = :eventNr " +
        "AND COALESCE(EC.parentUid,0) = :parentUid " +
        "AND EC.id.clientNr = :sessionClientNr ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("eventNr", formData.getEvent().getValue());
    query.setParameter("parentUid", NumberUtility.nvl(formData.getParent().getValue(), 0L));
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Long nextSortCode = query.getSingleResult();
    formData.getSortCode().setValue(NumberUtility.nvl(nextSortCode, 0) + 1);

    // in case of leg, set TimePrecision from Parent
    if (formData.getParent().getValue() != null) {
      JPAUtility.select("SELECT " +
          "timePrecisionUid, " +
          "courseGenerationTypeUid, " +
          "feeGroupNr " +
          "FROM RtEventClass " +
          "WHERE id.eventNr = :event " +
          "AND id.classUid = :parent " +
          "AND id.clientNr = :sessionClientNr " +
          "INTO " +
          ":timePrecision, " +
          ":courseGenerationType, " +
          ":feeGroup ", formData);

      formData.getType().setValue(ClassTypeCodeType.RelayLegCode.ID);
    }

    return formData;
  }

  @Override
  public EventClassFormData create(EventClassFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateEventClassPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null || formData.getEvent().getValue() == null || formData.getClazz().getValue() == null) {
      throw new IllegalArgumentException("arguments must not be null");
    }

    RtEventClass eventClass = new RtEventClass();
    RtEventClassKey key = new RtEventClassKey();
    key.setClassUid(formData.getClazz().getValue());
    key.setEventNr(formData.getEvent().getValue());
    key.setClientNr(ServerSession.get().getSessionClientNr());
    eventClass.setId(key);
    JPA.persist(eventClass);

    formData = store(formData);
    return formData;
  }

  @Override
  public EventClassFormData load(EventClassFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadEventClassPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT typeUid, " +
        "courseNr, " +
        "teamSizeMin, " +
        "teamSizeMax, " +
        "sortcode, " +
        "parentUid, " +
        "startlistSettingNr, " +
        "timePrecisionUid, " +
        "courseGenerationTypeUid, " +
        "feeGroupNr " +
        "FROM RtEventClass " +
        "WHERE id.eventNr = :event " +
        "AND id.classUid = :clazz " +
        "AND id.clientNr = COALESCE(:clientNr, :sessionClientNr) " +
        "INTO :type, " +
        ":course, " +
        ":teamSizeMin, " +
        ":teamSizeMax, " +
        ":sortCode, " +
        ":parent, " +
        ":startlistSettingNr, " +
        ":timePrecision, " +
        ":courseGenerationType, " +
        ":feeGroup ", formData);

    return formData;
  }

  @Override
  public EventClassFormData store(EventClassFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateEventClassPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (!ClassTypeCodeType.isTeamClassType(formData.getType().getValue())) {
      formData.getTeamSizeMax().setValue(1L);
      formData.getTeamSizeMin().setValue(1L);
    }
    if (formData.getTeamSizeMax().getValue() == null ||
        formData.getTeamSizeMin().getValue() == null ||
        formData.getTeamSizeMin().getValue() > formData.getTeamSizeMax().getValue()) {
      throw new ProcessingException("Team size error");
    }

    String queryString = "UPDATE RtEventClass " +
        "SET typeUid = :type, " +
        "courseNr = :course, " +
        "teamSizeMin = :teamSizeMin, " +
        "teamSizeMax = :teamSizeMax, " +
        "sortcode = :sortCode, " +
        "parentUid = :parent, " +
        "startlistSettingNr = :startlistSettingNr, " +
        "timePrecisionUid = :timePrecision, " +
        "courseGenerationTypeUid = :courseGenerationType, " +
        "feeGroupNr = :feeGroup " +
        "WHERE id.eventNr = :event " +
        "AND id.classUid = :clazz " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    // propagate time precision to child classes
    queryString = "UPDATE RtEventClass " +
        "SET startlistSettingNr = NULL, " +
        "timePrecisionUid = :timePrecision, " +
        "courseGenerationTypeUid = :courseGenerationType, " +
        "feeGroupNr = :feeGroup " +
        "WHERE parentUid = :clazz " +
        "AND id.eventNr = :event " +
        "AND id.clientNr = :sessionClientNr ";
    query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    SharedCacheServerUtility.notifyClients();

    return formData;
  }

  @Override
  public EventClassFormData delete(EventClassFormData formData) throws ProcessingException {
    if (formData == null) {
      return null;
    }

    String queryString = "DELETE " +
        "FROM RtEventClass " +
        "WHERE :clazz = id.classUid " +
        "AND :event = id.eventNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("event", formData.getEvent().getValue());
    query.setParameter("clazz", formData.getClazz().getValue());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();

    SharedCacheServerUtility.notifyClients();

    return formData;
  }

}
