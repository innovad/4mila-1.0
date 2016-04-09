package com.rtiming.server.club;

import org.eclipse.scout.rt.platform.BEANS;
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
import com.rtiming.shared.club.IClubProcessService;
import com.rtiming.shared.common.database.sql.ClubBean;
import com.rtiming.shared.common.security.permission.CreateClubPermission;
import com.rtiming.shared.common.security.permission.ReadClubPermission;
import com.rtiming.shared.common.security.permission.UpdateClubPermission;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClubKey;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationProcessService;

public class ClubProcessService  implements IClubProcessService {

  @Override
  public ClubBean prepareCreate(ClubBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreateClubPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    BEANS.get(IAdditionalInformationProcessService.class).prepareCreate(bean.getAddInfo());

    return bean;
  }

  @Override
  public ClubBean create(ClubBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreateClubPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtClubKey key = RtClubKey.create((Long) null);
    RtClub club = new RtClub();
    club.setId(key);
    JPA.persist(club);

    bean.setClubNr(key.getId());
    bean = store(bean);

    return bean;
  }

  @Override
  public ClubBean load(ClubBean bean) throws ProcessingException {
    if (!ACCESS.check(new ReadClubPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT shortcut, name, contactRunnerNr, extKey " +
        "FROM RtClub " +
        "WHERE id.clubNr = :clubNr " +
        "AND id.clientNr = :sessionClientNr " +
        "INTO :shortcut, :name, :contactRunnerNr, :extKey ", bean);

    // RT_ADDITIONAL_INFORMATION
    bean.getAddInfo().setJoinNr(bean.getClubNr());
    bean.getAddInfo().setClientNr(bean.getClientNr());
    BEANS.get(IAdditionalInformationProcessService.class).load(bean.getAddInfo());

    return bean;
  }

  @Override
  public ClubBean store(ClubBean bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateClubPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    String queryString = "UPDATE RtClub C " +
        "SET shortcut = :shortcut, " +
        "name = :name, " +
        "rtRunner.id.runnerNr = :contactRunnerNr, " +
        "extKey = :extKey " +
        "WHERE C.id.clubNr = :clubNr " +
        "AND C.id.clientNr = :sessionClientNr ";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, bean);
    query.executeUpdate();

    // RT_ADDITIONAL_INFORMATION
    bean.getAddInfo().setJoinNr(bean.getClubNr());
    bean.getAddInfo().setClientNr(bean.getClientNr());
    BEANS.get(IAdditionalInformationProcessService.class).store(bean.getAddInfo());

    return bean;
  }

  @Override
  public ClubBean findClub(String name) throws ProcessingException {
    name = StringUtility.uppercase(name).trim();

    String queryString = "SELECT MAX(C.id.clubNr) " +
        "FROM RtClub C " +
        "WHERE UPPER(C.name) = :name " +
        "AND C.id.clientNr = :sessionClientNr ";

    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("name", name);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Long clubNr = query.getSingleResult();

    ClubBean club = new ClubBean();
    if (clubNr != null) {
      club.setClubNr(clubNr);
      club = load(club);
    }

    return club;
  }

  @Override
  public int delete(RtClubKey... keys) throws ProcessingException {
    int k = 0;
    for (RtClubKey key : keys) {
      RtClub club = JPA.find(RtClub.class, key);
      if (club != null) {
        JPA.remove(club);
        k++;
      }
    }
    return k;
  }

}
