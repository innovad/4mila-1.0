package com.rtiming.server.event.course;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateReplacementControlPermission;
import com.rtiming.shared.common.security.permission.ReadReplacementControlPermission;
import com.rtiming.shared.common.security.permission.UpdateReplacementControlPermission;
import com.rtiming.shared.dao.RtControlReplacement;
import com.rtiming.shared.dao.RtControlReplacementKey;
import com.rtiming.shared.event.course.IReplacementControlProcessService;
import com.rtiming.shared.event.course.ReplacementControlFormData;

public class ReplacementControlProcessService  implements IReplacementControlProcessService {

  @Override
  public ReplacementControlFormData prepareCreate(ReplacementControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateReplacementControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public ReplacementControlFormData create(ReplacementControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateReplacementControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtControlReplacement replacement = new RtControlReplacement();
    RtControlReplacementKey key = new RtControlReplacementKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setControlNr(formData.getControl().getValue());
    key.setReplacementControlNr(formData.getReplacementControl().getValue());
    replacement.setId(key);
    JPA.persist(replacement);

    ServerCache.resetCache();
    return formData;
  }

  @Override
  public ReplacementControlFormData load(ReplacementControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadReplacementControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public ReplacementControlFormData store(ReplacementControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateReplacementControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public ReplacementControlFormData delete(ReplacementControlFormData formData) throws ProcessingException {
    if (formData == null) {
      return null;
    }

    String queryString = "DELETE FROM RtControlReplacement " +
        "WHERE id.controlNr = :control " +
        "AND id.replacementControlNr = :replacementControl " +
        "AND id.clientNr = :sessionClientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    ServerCache.resetCache();
    return formData;
  }

}
