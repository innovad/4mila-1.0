package com.rtiming.server.runner;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.common.database.sql.AddressBean;
import com.rtiming.shared.common.security.permission.ReadAddressPermission;
import com.rtiming.shared.common.security.permission.UpdateAddressPermission;
import com.rtiming.shared.runner.IAddressProcessService;

public class AddressProcessService  implements IAddressProcessService {

  @Override
  public AddressBean load(AddressBean formData) throws ProcessingException {
    if (!ACCESS.check(new ReadAddressPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT street, phone, fax, mobile, email, www, cityNr FROM RtAddress " +
        "WHERE id.addressNr = :addressNr " +
        "AND id.clientNr = COALESCE(:clientNr,:sessionClientNr) " + // use RT_RUNNER clientNr
        "INTO :street, :phone, :fax, :mobile, :email, :www, :cityNr "
        , formData
        );

    return formData;
  }

  @Override
  public AddressBean store(AddressBean bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateAddressPermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }
    if (bean == null) {
      return null;
    }

    String queryString = "UPDATE RtAddress A " +
        "SET A.street = :street, " +
        "A.phone = :phone, " +
        "A.fax = :fax, " +
        "A.mobile = :mobile, " +
        "A.email = :email, " +
        "A.www = :www, " +
        "A.rtCity.id.cityNr = :cityNr " +
        "WHERE A.id.addressNr = :addressNr " +
        "AND A.id.clientNr = :addressClientNr ";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, bean);
    query.setParameter("addressClientNr", NumberUtility.nvl(bean.getClientNr(), ServerSession.get().getSessionClientNr()));
    query.executeUpdate();

    return bean;
  }
}
