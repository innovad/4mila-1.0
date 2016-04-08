package com.rtiming.server.settings.clazz;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.entry.SharedCacheServerUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateAgePermission;
import com.rtiming.shared.common.security.permission.DeleteAgePermission;
import com.rtiming.shared.common.security.permission.ReadAgePermission;
import com.rtiming.shared.common.security.permission.UpdateAgePermission;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtClassAgeKey;
import com.rtiming.shared.dao.RtClassAgeKey_;
import com.rtiming.shared.dao.RtClassAge_;
import com.rtiming.shared.settings.clazz.IAgeProcessService;

public class AgeProcessService implements IAgeProcessService {

  @Override
  public RtClassAge prepareCreate(RtClassAge bean) throws ProcessingException {
    if (!ACCESS.check(new CreateAgePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    return bean;
  }

  @Override
  public RtClassAge create(RtClassAge bean) throws ProcessingException {
    if (!ACCESS.check(new CreateAgePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // new key
    bean.setId(RtClassAgeKey.create(bean.getId()));

    bean = store(bean);

    return bean;
  }

  @Override
  public RtClassAge load(RtClassAgeKey key) throws ProcessingException {
    if (!ACCESS.check(new ReadAgePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    key = RtClassAgeKey.create(key);
    return JPA.find(RtClassAge.class, key);
  }

  @Override
  public RtClassAge store(RtClassAge bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateAgePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return storeInternal(bean);
  }

  protected RtClassAge storeInternal(RtClassAge bean) throws ProcessingException {
    if (bean.getSexUid() == null && bean.getAgeFrom() == null && bean.getAgeTo() == null) {
      throw new VetoException(TEXTS.get("AgeFormConditionValidation"));
    }

    JPA.merge(bean);
    SharedCacheServerUtility.notifyClients();
    return bean;
  }

  @Override
  public RtClassAge delete(RtClassAgeKey key) throws ProcessingException {
    if (!ACCESS.check(new DeleteAgePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtClassAge bean = load(key);
    JPA.remove(bean);
    SharedCacheServerUtility.notifyClients();
    return bean;
  }

  @Override
  public List<RtClassAge> loadAgeConfiguration() throws ProcessingException {
    if (!ACCESS.check(new ReadAgePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtClassAge> q = b.createQuery(RtClassAge.class);

    Root<RtClassAge> root = q.from(RtClassAge.class);
    q.where(b.equal(root.get(RtClassAge_.id).get(RtClassAgeKey_.clientNr), ServerSession.get().getSessionClientNr()));

    return JPA.createQuery(q).getResultList();
  }
}
