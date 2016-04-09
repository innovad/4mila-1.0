package com.rtiming.server.runner;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.holders.LongHolder;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.AddressBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.common.security.permission.CreateRunnerPermission;
import com.rtiming.shared.common.security.permission.ReadRunnerPermission;
import com.rtiming.shared.common.security.permission.UpdateRunnerPermission;
import com.rtiming.shared.dao.RtAddress;
import com.rtiming.shared.dao.RtAddressKey;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunnerKey;
import com.rtiming.shared.runner.IAddressProcessService;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationProcessService;

public class RunnerProcessService  implements IRunnerProcessService {

  @Override
  public RunnerBean prepareCreate(RunnerBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreateRunnerPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    bean.setActive(true);
    BEANS.get(IAdditionalInformationProcessService.class).prepareCreate(bean.getAddInfo());

    return bean;
  }

  @Override
  public RunnerBean create(RunnerBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreateRunnerPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // runner
    RtRunnerKey key = RtRunnerKey.create((Long) null);
    RtRunner runner = new RtRunner();
    runner.setId(key);
    runner.setActive(bean.isActive());
    JPA.persist(runner);
    bean.setRunnerNr(runner.getId().getId());

    // address
    bean = insertAddress(bean);

    bean = store(bean);

    return bean;
  }

  private RunnerBean insertAddress(RunnerBean bean) throws ProcessingException {
    RtAddressKey key = RtAddressKey.create((Long) null);
    RtAddress adress = new RtAddress();
    adress.setId(key);
    JPA.persist(adress);

    bean.getAddress().setAddressNr(adress.getId().getId());
    return bean;
  }

  @Override
  public RunnerBean load(RunnerBean bean) throws ProcessingException {
    if (!ACCESS.check(new ReadRunnerPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (bean == null) {
      return null;
    }

    LongHolder addressNr = new LongHolder();

    JPAUtility.select("SELECT " +
        "id.clientNr, " +
        "firstName, " +
        "lastName, " +
        "sexUid, " +
        "evtBirth, " +
        "year, " +
        "extKey, " +
        "clubNr, " +
        "nationUid, " +
        "rtEcard.id.eCardNr, " +
        "defaultClassUid, " +
        "active, " +
        "addressNr " +
        "FROM RtRunner " +
        "WHERE id.runnerNr = :runnerNr " +
        "AND id.clientNr = COALESCE(:clientNr,:sessionClientNr) " +
        "INTO " +
        ":clientNr, " +
        ":firstName, " +
        ":lastName, " +
        ":sexUid, " +
        ":evtBirth, " +
        ":year, " +
        ":extKey, " +
        ":clubNr, " +
        ":nationUid, " +
        ":eCardNr," +
        ":defaultClassUid, " +
        ":active, " +
        ":addressNr "
        , bean
        , new NVPair("addressNr", addressNr)
        );

    // if an address nr is already set, use this one (use for entries)
    if (bean.getAddress().getAddressNr() == null) {
      bean.getAddress().setAddressNr(addressNr.getValue());
    }
    AddressBean addressBean = BEANS.get(IAddressProcessService.class).load(bean.getAddress());
    bean.setAddress(addressBean);

    // RT_ADDITIONAL_INFORMATION
    bean.getAddInfo().setJoinNr(bean.getRunnerNr());
    bean.getAddInfo().setClientNr(bean.getClientNr());
    BEANS.get(IAdditionalInformationProcessService.class).load(bean.getAddInfo());

    return bean;
  }

  @Override
  public RunnerBean store(RunnerBean bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateRunnerPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    if (bean.getAddress() == null || bean.getAddress().getAddressNr() == null) {
      bean = insertAddress(bean);
    }
    if (StringUtility.isNullOrEmpty(bean.getLastName())) {
      throw new VetoException(TEXTS.get("RunnerLastNameRequiredMessage"));
    }

    String queryString = "UPDATE RtRunner " +
        "SET firstName = :firstName, " +
        "lastName = :lastName, " +
        "sexUid = :sexUid, " +
        "evtBirth = :evtBirth, " +
        "year = :year, " +
        "extKey = :extKey, " +
        "clubNr = :clubNr, " +
        "nationUid = :nationUid, " +
        "eCardNr = :eCardNr," +
        "defaultClassUid = :defaultClassUid, " +
        "active = :active, " +
        "addressNr = :addressNr " +
        "WHERE id.runnerNr = :runnerNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("addressNr", bean.getAddress().getAddressNr());
    query.setParameter("eCardNr", bean.getECardNr());
    JPAUtility.setAutoParameters(query, queryString, bean);
    query.executeUpdate();

    BEANS.get(IAddressProcessService.class).store(bean.getAddress());

    // RT_ADDITIONAL_INFORMATION
    bean.getAddInfo().setJoinNr(bean.getRunnerNr());
    bean.getAddInfo().setClientNr(bean.getClientNr());
    BEANS.get(IAdditionalInformationProcessService.class).store(bean.getAddInfo());

    // Update ACCOUNT_NR on global database
    if (FMilaUtility.isWebClient()) {
      updateOnlineAccountRunnerLinks(null, ServerSession.get().getSessionClientNr(), bean.getRunnerNr());
    }

    return bean;
  }

  @Override
  public Long findRunner(String extKey) throws ProcessingException {
    if (StringUtility.isNullOrEmpty(extKey)) {
      return null;
    }
    extKey = StringUtility.uppercase(extKey).trim();

    String queryString = "SELECT MAX(id.runnerNr) FROM RtRunner WHERE UPPER(extKey) = :extKey " +
        "AND id.clientNr = :sessionClientNr ";

    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("extKey", extKey);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Long runnerNr = query.getSingleResult();

    return runnerNr;
  }

  @Override
  public Long findRunnerByECard(String eCardNo) throws ProcessingException {
    if (StringUtility.isNullOrEmpty(eCardNo)) {
      return null;
    }
    eCardNo = StringUtility.uppercase(eCardNo).trim();

    String queryString = "SELECT MAX(R.id.runnerNr) FROM RtRunner R " +
        "INNER JOIN R.rtEcard E " +
        "WHERE UPPER(E.ecardNo) = :eCardNo " +
        "AND R.id.clientNr = :sessionClientNr ";

    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("eCardNo", eCardNo);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Long runnerNr = query.getSingleResult();

    return runnerNr;
  }

  @Override
  public RunnerBean delete(RunnerBean bean) throws ProcessingException {
    String queryString = "DELETE RtRunner R " +
        "WHERE R.id.runnerNr = :runnerNr " +
        "AND r.id.clientNr = :sessionClientNr";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, bean);
    query.executeUpdate();

    return bean;
  }

  @Override
  public void updateOnlineAccountRunnerLinks(Long accountNr, Long clientNr, Long runnerNr) throws ProcessingException {
    if (accountNr == null && clientNr == null) {
      throw new IllegalArgumentException("Either accountNr or clientNr must be set");
    }
    else if (accountNr != null && clientNr != null) {
      throw new IllegalArgumentException("Only one of both parameters should be set");
    }

    if (accountNr != null) {

      String queryString = "UPDATE RtRunner R " +
          "SET accountNr = :accountNr " +
          "WHERE R.addressNr IN ( " +
          /* select addresses with same e-mail address as account e-mail address */
          "                       SELECT A.id.addressNr FROM RtAddress A " +
          "                       WHERE A.id.addressNr = R.addressNr " +
          "                       AND A.id.clientNr = R.id.clientNr " +
          "                       AND LOWER(A.email) IN (" +
          /* account email address */
          "                                               SELECT LOWER(AC.email) " +
          "                                               FROM RtAccount AC " +
          "                                               WHERE AC.accountNr = :accountNr)" +
          ")";
      FMilaQuery query = JPA.createQuery(queryString);
      query.setParameter("accountNr", accountNr);
      query.executeUpdate();

    }
    else if (clientNr != null) {

      // update accountNr if a new accountNr could be found, otherwise accountNr should remain
      String queryString = "UPDATE RtRunner R " +
          "SET accountNr = COALESCE((SELECT MAX(AC.accountNr) FROM RtAccount AC" +
          "                    WHERE LOWER(AC.email) IN (SELECT LOWER(A.email) " +
          "                                              FROM RtAddress A " +
          "                                              WHERE A.id.addressNr = R.addressNr " +
          "                                              AND A.id.clientNr = R.id.clientNr) " +
          "), R.accountNr) " +
          "WHERE R.id.clientNr = :clientNr " +
          (runnerNr != null ? "AND R.id.runnerNr = :runnerNr " : "");
      FMilaQuery query = JPA.createQuery(queryString);
      if (runnerNr != null) {
        query.setParameter("runnerNr", runnerNr);
      }
      query.setParameter("clientNr", clientNr);
      query.executeUpdate();

    }

  }
}
