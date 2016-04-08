package com.rtiming.server.ecard;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateECardPermission;
import com.rtiming.shared.common.security.permission.ReadECardPermission;
import com.rtiming.shared.common.security.permission.UpdateECardPermission;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.dao.RtEcardKey_;
import com.rtiming.shared.dao.RtEcard_;
import com.rtiming.shared.ecard.ECardFormData;
import com.rtiming.shared.ecard.ECardUtility;
import com.rtiming.shared.ecard.IECardProcessService;

public class ECardProcessService  implements IECardProcessService {

  @Override
  public ECardFormData prepareCreate(ECardFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getRentalCard().setValue(false);

    return formData;
  }

  @Override
  public RtEcard create(RtEcard bean) throws ProcessingException {
    if (!ACCESS.check(new CreateECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // new key
    bean.setKey(RtEcardKey.create(bean.getKey()));

    bean = store(bean);

    return bean;
  }

  @Override
  public RtEcard load(RtEcardKey key) throws ProcessingException {
    if (!ACCESS.check(new ReadECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    key = RtEcardKey.create(key);
    return JPA.find(RtEcard.class, key);
  }

  @Override
  public RtEcard store(RtEcard bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateECardPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPA.merge(bean);

    return bean;
  }

  @Override
  public RtEcard findECard(String eCardNo) throws ProcessingException {
    eCardNo = StringUtility.uppercase(eCardNo).trim();

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtEcard> q = b.createQuery(RtEcard.class);

    Root<RtEcard> root = q.from(RtEcard.class);
    q.where(
        b.equal(root.get(RtEcard_.id).get(RtEcardKey_.clientNr), ServerSession.get().getSessionClientNr()),
        b.like(b.upper(root.get(RtEcard_.ecardNo)), eCardNo)
        );

    List<RtEcard> result = JPA.createQuery(q).getResultList();

    RtEcard eCard = new RtEcard();
    if (result.size() > 0) {
      eCard = result.get(0);
    }
    else {
      eCard.setEcardNo(eCardNo);
      eCard.setTypeUid(ECardUtility.getType(eCardNo));
    }

    return eCard;
  }

  @Override
  public int delete(RtEcardKey... keys) throws ProcessingException {
    int k = 0;
    for (RtEcardKey key : keys) {
      RtEcard ecard = load(key);
      if (ecard != null) {
        JPA.remove(ecard);
        k++;
      }
    }
    return k;
  }

}
