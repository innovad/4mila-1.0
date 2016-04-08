package com.rtiming.server.entry.payment;

import java.util.Date;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.dataexchange.RegistrationServerUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateRegistrationPermission;
import com.rtiming.shared.common.security.permission.ReadRegistrationPermission;
import com.rtiming.shared.common.security.permission.UpdateRegistrationPermission;
import com.rtiming.shared.dao.RtRegistration;
import com.rtiming.shared.dao.RtRegistrationKey;
import com.rtiming.shared.dataexchange.RegistrationSharedUtility;
import com.rtiming.shared.entry.IRegistrationProcessService;
import com.rtiming.shared.entry.RegistrationFormData;

public class RegistrationProcessService  implements IRegistrationProcessService {

  @Override
  public RegistrationFormData prepareCreate(RegistrationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateRegistrationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getRegistrationNo().setValue(RegistrationSharedUtility.getNewRegistrationNoText());
    formData.getEvtRegistration().setValue(new Date());
    formData.getStartlistSettingOptionGroupBox().setValue(null);

    return formData;
  }

  @Override
  public RegistrationFormData create(RegistrationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateRegistrationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // make sure date is set (no need to call prepare create)
    if (formData.getEvtRegistration().getValue() == null) {
      formData.getEvtRegistration().setValue(new Date());
    }

    // create primary key
    RtRegistrationKey key = RtRegistrationKey.create((Long) null);
    RtRegistration registration = new RtRegistration();
    registration.setId(key);
    JPA.persist(registration);
    formData.setRegistrationNr(registration.getId().getId());

    // set registration no (should be unique for all 4mila installations)
    if (StringUtility.isNullOrEmpty(formData.getRegistrationNo().getValue()) || StringUtility.equalsIgnoreCase(RegistrationSharedUtility.getNewRegistrationNoText(), formData.getRegistrationNo().getValue())) {
      formData.getRegistrationNo().setValue(RegistrationServerUtility.buildRegistrationNo(String.valueOf(formData.getRegistrationNr())));
    }

    formData = store(formData);

    return formData;
  }

  @Override
  public RegistrationFormData load(RegistrationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadRegistrationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT R.registrationNo, R.evtRegistration, R.startlistSettingOptionUid " +
        "FROM RtRegistration R " +
        "WHERE R.id.registrationNr = :registrationNr " +
        "AND R.id.clientNr = :sessionClientNr " +
        "INTO :registrationNo, :evtRegistration, :startlistSettingOptionGroupBox ", formData);

    return formData;
  }

  @Override
  public RegistrationFormData store(RegistrationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateRegistrationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    String queryString = "UPDATE RtRegistration R " +
        "SET R.registrationNo = :registrationNo, " +
        "R.evtRegistration = :evtRegistration, " +
        "R.startlistSettingOptionUid = :startlistSettingOptionGroupBox " +
        "WHERE R.id.registrationNr = :registrationNr " +
        "AND R.id.clientNr = :sessionClientNr";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    return formData;
  }

  @Override
  public RegistrationFormData find(String no) throws ProcessingException {
    no = StringUtility.uppercase(no).trim();

    String queryString = "SELECT MAX(R.id.registrationNr) " +
        "FROM RtRegistration R " +
        "WHERE UPPER(R.registrationNo) = :no " +
        "AND R.id.clientNr = :sessionClientNr ";

    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("no", no);
    Long registrationNr = query.getSingleResult();

    RegistrationFormData registration = new RegistrationFormData();
    if (registrationNr != null) {
      registration.setRegistrationNr(registrationNr);
      registration = load(registration);
    }

    return registration;
  }

  @Override
  public void delete(RegistrationFormData formData) throws ProcessingException {
    String queryString = "DELETE FROM RtRegistration R " +
        "WHERE R.id.registrationNr = :registrationNr " +
        "AND R.id.clientNr = :sessionClientNr";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();
  }

}
