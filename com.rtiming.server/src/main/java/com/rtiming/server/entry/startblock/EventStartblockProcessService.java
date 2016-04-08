package com.rtiming.server.entry.startblock;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateEventStartblockPermission;
import com.rtiming.shared.common.security.permission.ReadEventStartblockPermission;
import com.rtiming.shared.common.security.permission.UpdateEventStartblockPermission;
import com.rtiming.shared.dao.RtEventStartblock;
import com.rtiming.shared.dao.RtEventStartblockKey;
import com.rtiming.shared.entry.startblock.EventStartblockFormData;
import com.rtiming.shared.entry.startblock.IEventStartblockProcessService;

public class EventStartblockProcessService  implements IEventStartblockProcessService {

  @Override
  public EventStartblockFormData prepareCreate(EventStartblockFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateEventStartblockPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    String queryString = "SELECT COALESCE(MAX(ES.sortcode)+1,1) FROM RtEventStartblock ES " +
        "WHERE ES.id.eventNr = :eventNr " +
        "AND ES.id.clientNr = :sessionClientNr ";

    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    JPAUtility.setAutoParameters(query, queryString, formData);
    Long maxSortcode = query.getSingleResult();
    formData.getSortCode().setValue(maxSortcode);

    return formData;
  }

  @Override
  public EventStartblockFormData create(EventStartblockFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateEventStartblockPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtEventStartblock startblock = new RtEventStartblock();
    RtEventStartblockKey key = new RtEventStartblockKey();
    key.setEventNr(formData.getEventNr());
    key.setId(formData.getStartblockUid().getValue());
    key.setClientNr(ServerSession.get().getSessionClientNr());
    startblock.setId(key);
    JPA.persist(startblock);

    formData = store(formData);

    return formData;
  }

  @Override
  public EventStartblockFormData load(EventStartblockFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadEventStartblockPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT sortcode FROM RtEventStartblock " +
        "WHERE id.startblockUid = :startblockUid " +
        "AND id.eventNr = :eventNr " +
        "AND id.clientNr = :sessionClientNr " +
        "INTO :sortCode", formData);

    return formData;
  }

  @Override
  public EventStartblockFormData store(EventStartblockFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateEventStartblockPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    String queryString = "UPDATE RtEventStartblock ES " +
        "SET ES.sortcode = :sortCode " +
        "WHERE ES.id.startblockUid = :startblockUid " +
        "AND ES.id.eventNr = :eventNr " +
        "AND ES.id.clientNr = :sessionClientNr ";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    return formData;
  }

  @Override
  public EventStartblockFormData delete(EventStartblockFormData formData) throws ProcessingException {
    if (formData != null && formData.getEventNr() != null) {
      RtEventStartblock es = new RtEventStartblock();
      RtEventStartblockKey key = new RtEventStartblockKey();
      key.setEventNr(formData.getEventNr());
      key.setId(formData.getStartblockUid().getValue());
      es.setId(RtEventStartblockKey.create(key));
      JPA.remove(es);
    }
    return formData;
  }
}
