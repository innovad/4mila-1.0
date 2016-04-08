package com.rtiming.server.map;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateEventMapPermission;
import com.rtiming.shared.common.security.permission.ReadEventMapPermission;
import com.rtiming.shared.common.security.permission.UpdateEventMapPermission;
import com.rtiming.shared.dao.RtEventMap;
import com.rtiming.shared.dao.RtEventMapKey;
import com.rtiming.shared.event.EventMapFormData;
import com.rtiming.shared.map.IEventMapProcessService;

public class EventMapProcessService  implements IEventMapProcessService {

  @Override
  public EventMapFormData prepareCreate(EventMapFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateEventMapPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public EventMapFormData create(EventMapFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateEventMapPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtEventMap eventMap = new RtEventMap();
    RtEventMapKey key = new RtEventMapKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setEventNr(formData.getEvent().getValue());
    key.setMapNr(formData.getMap().getValue());
    eventMap.setId(key);
    JPA.persist(eventMap);

    return formData;
  }

  @Override
  public EventMapFormData load(EventMapFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadEventMapPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public EventMapFormData store(EventMapFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateEventMapPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public EventMapFormData delete(EventMapFormData formData) throws ProcessingException {
    if (formData == null) {
      return null;
    }

    String queryString = "DELETE FROM RtEventMap WHERE id.eventNr = :event AND id.mapNr = :map";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("map", formData.getMap().getValue());
    query.setParameter("event", formData.getEvent().getValue());
    query.executeUpdate();

    return formData;
  }
}
