package com.rtiming.server.ecard.download;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateECardStationPermission;
import com.rtiming.shared.common.security.permission.DeleteECardStationPermission;
import com.rtiming.shared.common.security.permission.ReadECardStationPermission;
import com.rtiming.shared.common.security.permission.UpdateECardStationPermission;
import com.rtiming.shared.dao.RtEcardStation;
import com.rtiming.shared.dao.RtEcardStationKey;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.IECardStationProcessService;

public class ECardStationProcessService  implements IECardStationProcessService {

  @Override
  public ECardStationFormData prepareCreate(ECardStationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateECardStationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    return formData;
  }

  @Override
  public ECardStationFormData create(ECardStationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateECardStationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtEcardStationKey key = RtEcardStationKey.create((Long) null);
    RtEcardStation station = new RtEcardStation();
    station.setId(key);
    JPA.persist(station);

    formData.setECardStationNr(station.getId().getId());
    formData = store(formData);

    return formData;
  }

  @Override
  public ECardStationFormData load(ECardStationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadECardStationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT identifier, clientAddress, port, baud, modusUid, printer, posPrinter " +
        "FROM RtEcardStation " +
        "WHERE id.stationNr = :eCardStationNr " +
        "AND id.clientNr = :sessionClientNr " +
        "INTO :identifier, :clientAddress, :port, :baud, :modus, :printer, :posPrinter ", formData);

    return formData;
  }

  @Override
  public ECardStationFormData store(ECardStationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateECardStationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null || formData.getECardStationNr() == null) {
      return formData;
    }
    formData.getIdentifier().setValue(StringUtility.substring(formData.getIdentifier().getValue(), 0, 60));

    String queryString = "UPDATE RtEcardStation " +
        "SET identifier = :identifier, " +
        "clientAddress = :clientAddress, " +
        "port = :port, " +
        "baud = :baud, " +
        "modusUid = :modus, " +
        "printer = :printer, " +
        "posPrinter = :posPrinter " +
        "WHERE id.stationNr = :stationNr " +
        "AND id.clientNr = :sessionClientNr ";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.setParameter("stationNr", formData.getECardStationNr());
    query.executeUpdate();

    return formData;
  }

  @Override
  public ECardStationFormData find(String port, String clientAddress) throws ProcessingException {
    port = StringUtility.uppercase(port).trim();
    clientAddress = StringUtility.uppercase(clientAddress).trim();

    String queryString = "SELECT MAX(id.stationNr) FROM RtEcardStation " +
        "WHERE UPPER(clientAddress) = :clientAddress " +
        "AND UPPER(port) = :port " +
        "AND id.clientNr = :sessionClientNr ";

    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("clientAddress", clientAddress);
    query.setParameter("port", port);
    Long stationNr = query.getSingleResult();

    ECardStationFormData station = new ECardStationFormData();
    if (stationNr != null) {
      station.setECardStationNr(stationNr);
      station = load(station);
    }
    else {
      station.getPort().setValue(port);
      station.getClientAddress().setValue(clientAddress);
    }

    return station;
  }

  @Override
  public void delete(ECardStationFormData formData, boolean deletePunches) throws ProcessingException {
    if (!ACCESS.check(new DeleteECardStationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null || formData.getECardStationNr() == null) {
      return;
    }

    if (deletePunches) {
      String queryString = "DELETE FROM RtPunch WHERE id.punchSessionNr IN (SELECT id.punchSessionNr FROM RtPunchSession WHERE stationNr = :eCardStationNr AND id.clientNr = :clientNr) AND id.clientNr = :clientNr";
      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("eCardStationNr", formData.getECardStationNr());
      query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();

      queryString = "DELETE FROM RtPunchSession WHERE stationNr = :eCardStationNr AND id.clientNr = :clientNr";
      query = JPA.createQuery(queryString);
      query.setParameter("eCardStationNr", formData.getECardStationNr());
      query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
      query.executeUpdate();
    }

    RtEcardStation station = new RtEcardStation();
    station.setId(RtEcardStationKey.create(formData.getECardStationNr()));
    JPA.remove(station);
  }
}
