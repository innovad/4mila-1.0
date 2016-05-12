package com.rtiming.server.test.helper;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.test.helper.ITestingJPAService;

public class TestingJPAService implements ITestingJPAService {

  @Override
  public Long getMaxCityNr() throws ProcessingException {
    String queryString = "SELECT MAX(id.cityNr) FROM RtCity WHERE id.clientNr = :clientNr";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    Long result = query.getSingleResult();
    return result;
  }

  @Override
  public Long getCityCount() throws ProcessingException {
    String queryString = "SELECT COUNT(id.cityNr) FROM RtCity WHERE id.clientNr = :clientNr";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    Long result = query.getSingleResult();
    return result;
  }

  @Override
  public void deleteCities(Long countryUid) throws ProcessingException {
    String queryString = "DELETE FROM RtCity WHERE countryUid = :countryUid AND id.clientNr = :clientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("countryUid", countryUid);
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();
  }

  @Override
  public List<Long> getPunchSessionsForStation(Long stationNr) throws ProcessingException {
    String queryString = "SELECT id.punchSessionNr " + "FROM RtPunchSession " + "WHERE id.clientNr = :clientNr " + (stationNr != null ? "AND stationNr = :stationNr " : "");
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    if (stationNr != null) {
      query.setParameter("stationNr", stationNr);
    }
    query.setParameter("clientNr", ServerSession.get().getSessionClientNr());

    return query.getResultList();
  }

  @Override
  public void cleanupAccounts() throws ProcessingException {
    String queryString = "UPDATE RtAccount SET username = NULL WHERE username = '123456'";
    FMilaQuery query = JPA.createQuery(queryString);
    query.executeUpdate();

    queryString = "UPDATE RtAccount SET email = NULL WHERE email = 'test@w123456.com'";
    query = JPA.createQuery(queryString);
    query.executeUpdate();
  }

  @Override
  public void cleanupCountries() throws ProcessingException {
    String queryString = "UPDATE RtCountry SET countryCode = NULL, nation = NULL";
    FMilaQuery query = JPA.createQuery(queryString);
    query.executeUpdate();
  }

  @Override
  public void deleteAccount(Long accountNr) throws ProcessingException {
    String queryString = "DELETE FROM RtAccount WHERE id.accountNr = :accountNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("accountNr", accountNr);
    query.executeUpdate();
  }

  @Override
  public void cleanupRunner(Long eCardNr) throws ProcessingException {
    String queryString = "UPDATE RtRunner SET eCardNr = NULL WHERE eCardNr = :eCardNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("eCardNr", eCardNr);
    query.executeUpdate();
  }

}
