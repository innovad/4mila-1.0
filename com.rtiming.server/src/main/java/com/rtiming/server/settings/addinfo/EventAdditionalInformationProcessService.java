package com.rtiming.server.settings.addinfo;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.entry.SharedCacheServerUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateEventAdditionalInformationPermission;
import com.rtiming.shared.common.security.permission.DeleteEventAdditionalInformationPermission;
import com.rtiming.shared.common.security.permission.ReadEventAdditionalInformationPermission;
import com.rtiming.shared.common.security.permission.UpdateEventAdditionalInformationPermission;
import com.rtiming.shared.dao.RtEventAdditionalInformation;
import com.rtiming.shared.dao.RtEventAdditionalInformationKey;
import com.rtiming.shared.settings.addinfo.EventAdditionalInformationFormData;
import com.rtiming.shared.settings.addinfo.IEventAdditionalInformationProcessService;

public class EventAdditionalInformationProcessService  implements IEventAdditionalInformationProcessService {

  @Override
  public EventAdditionalInformationFormData prepareCreate(EventAdditionalInformationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateEventAdditionalInformationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public EventAdditionalInformationFormData create(EventAdditionalInformationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateEventAdditionalInformationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtEventAdditionalInformation eventAdditionalInformation = new RtEventAdditionalInformation();
    RtEventAdditionalInformationKey key = new RtEventAdditionalInformationKey();
    key.setAdditionalInformationUid(formData.getAdditionalInformation().getValue());
    key.setEventNr(formData.getEvent().getValue());
    key.setClientNr(ServerSession.get().getSessionClientNr());
    eventAdditionalInformation.setId(key);
    JPA.merge(eventAdditionalInformation);

    formData = store(formData);

    return formData;
  }

  @Override
  public EventAdditionalInformationFormData load(EventAdditionalInformationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadEventAdditionalInformationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public EventAdditionalInformationFormData store(EventAdditionalInformationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateEventAdditionalInformationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    SharedCacheServerUtility.notifyClients();

    return formData;
  }

  @Override
  public EventAdditionalInformationFormData delete(EventAdditionalInformationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new DeleteEventAdditionalInformationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null) {
      return formData;
    }

    String queryString = "DELETE FROM RtEventAdditionalInformation WHERE id.eventNr = :event " +
        "AND id.additionalInformationUid = :additionalInformation " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    SharedCacheServerUtility.notifyClients();

    return formData;
  }

  @Override
  public HashMap<Long, long[]> loadEventAdditionalInformationConfiguration() throws ProcessingException {

    String queryString = "SELECT id.additionalInformationUid, id.eventNr " +
        "FROM RtEventAdditionalInformation " +
        "WHERE id.clientNr = :sessionClientNr " +
        "ORDER BY id.additionalInformationUid ASC ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    List list = query.getResultList();

    Long[] additionalInformationUids = new Long[list.size()];
    Long[] eventNrs = new Long[list.size()];
    for (int k = 0; k < list.size(); k++) {
      Object[] row = (Object[]) list.get(k);
      additionalInformationUids[k] = (Long) row[0];
      eventNrs[k] = (Long) row[1];
    }

    HashMap<Long, long[]> result = new HashMap<Long, long[]>();
    for (int k = 0; k < eventNrs.length; k++) {
      if (result.get(additionalInformationUids[k]) == null) {
        result.put(additionalInformationUids[k], new long[]{eventNrs[k]});
      }
      else {
        long[] array = result.get(additionalInformationUids[k]);
        array = Arrays.copyOf(array, array.length + 1);
        array[array.length - 1] = eventNrs[k];
        result.put(additionalInformationUids[k], array);
      }
    }

    return result;
  }

}
