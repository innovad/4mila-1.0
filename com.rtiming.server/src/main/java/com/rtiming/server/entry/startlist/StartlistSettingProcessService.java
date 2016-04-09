package com.rtiming.server.entry.startlist;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.entry.SharedCacheServerUtility;
import com.rtiming.shared.AdditionalInformationUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateStartlistSettingPermission;
import com.rtiming.shared.common.security.permission.ReadStartlistSettingPermission;
import com.rtiming.shared.common.security.permission.UpdateStartlistSettingPermission;
import com.rtiming.shared.dao.RtStartlistSetting;
import com.rtiming.shared.dao.RtStartlistSettingKey;
import com.rtiming.shared.dao.RtStartlistSettingOption;
import com.rtiming.shared.dao.RtStartlistSettingOptionKey;
import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.entry.startlist.IStartlistSettingProcessService;
import com.rtiming.shared.entry.startlist.StartlistSettingFormData;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;
import com.rtiming.shared.entry.startlist.StartlistSettingVacantPositionCodeType;
import com.rtiming.shared.entry.startlist.StartlistTypeCodeType;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.event.IEventProcessService;

public class StartlistSettingProcessService implements IStartlistSettingProcessService {

  @Override
  public StartlistSettingFormData prepareCreate(StartlistSettingFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateStartlistSettingPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getTypeUid().setValue(StartlistTypeCodeType.DrawingCode.ID);

    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(formData.getEventNr());
    formData.getFirstStart().setValue(evtZero);

    formData.getStartInterval().setValue(120L); // 2mins default

    formData.getVacantAbsolute().setValue(0L);
    formData.getVacantPercent().setValue(0L);
    formData.getVacantPositionGroup().setValue(StartlistSettingVacantPositionCodeType.EarlyStartCode.ID);

    formData.getBibNoOrderUid().setValue(BibNoOrderCodeType.AscendingCode.ID);

    return formData;
  }

  @Override
  public StartlistSettingFormData create(StartlistSettingFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateStartlistSettingPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtStartlistSettingKey key = RtStartlistSettingKey.create((Long) null);
    RtStartlistSetting startlistSetting = new RtStartlistSetting();
    startlistSetting.setId(key);
    JPA.persist(startlistSetting);
    formData.setStartlistSettingNr(startlistSetting.getId().getId());

    formData = store(formData);

    // set startlistSettingNr on class
    if (formData.getNewClassUid() != null) {
      EventClassFormData clazz = new EventClassFormData();
      clazz.getEvent().setValue(formData.getEventNr());
      clazz.getClazz().setValue(formData.getNewClassUid());
      clazz = BEANS.get(IEventClassProcessService.class).load(clazz);
      clazz.setStartlistSettingNr(formData.getStartlistSettingNr());
      BEANS.get(IEventClassProcessService.class).store(clazz);
    }

    return formData;
  }

  @Override
  public StartlistSettingFormData load(StartlistSettingFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadStartlistSettingPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtStartlistSettingKey key = RtStartlistSettingKey.create(formData.getStartlistSettingNr());
    RtStartlistSetting setting = JPA.find(RtStartlistSetting.class, key);
    formData.getTypeUid().setValue(setting.getTypeUid());
    formData.setEventNr(setting.getEventNr());
    formData.getStartInterval().setValue(setting.getStartInterval());
    formData.getVacantPercent().setValue(setting.getVacantPercent());
    formData.getVacantAbsolute().setValue(setting.getVacantAbsolute());
    formData.getVacantPositionGroup().setValue(setting.getVacantPositionUid());
    formData.getBibNoFrom().setValue(setting.getBibNoFrom());
    formData.getBibNoOrderUid().setValue(setting.getBibNoOrderUid());
    // date
    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(setting.getEventNr());
    formData.getFirstStart().setValue(FMilaUtility.addMilliSeconds(evtZero, setting.getFirstStarttime()));

    String queryString = "SELECT " + "id.optionUid " + "FROM RtStartlistSettingOption STO " + "WHERE STO.id.startlistSettingNr = :startlistSettingNr " + "AND STO.id.clientNr = :sessionClientNr ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("startlistSettingNr", formData.getStartlistSettingNr());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    List<Long> options = query.getResultList();
    formData.getOptions().setValue(new HashSet<>(options));

    return formData;
  }

  @Override
  public StartlistSettingFormData store(StartlistSettingFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateStartlistSettingPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // check
    if (formData.getOptions().getValue() != null) {
      Set<Long> values = formData.getOptions().getValue();
      if (values.contains(StartlistSettingOptionCodeType.SeparateClubsCode.ID) && values.contains(StartlistSettingOptionCodeType.SeparateNationsCode.ID)) {
        throw new VetoException(TEXTS.get("StartlistOptionsSeparationOnlyOneAttributeWarning"));
      }
    }

    Date evtZero = BEANS.get(IEventProcessService.class).getZeroTime(formData.getEventNr());
    Long firstStartDiff = FMilaUtility.getDateDifferenceInMilliSeconds(evtZero, formData.getFirstStart().getValue());

    String queryString = "UPDATE RtStartlistSetting " + "SET typeUid = :typeUid, " + "eventNr = :eventNr, " + "firstStarttime = :firstStartDiff, " + "startInterval = :startInterval, " + "vacantPercent = :vacantPercent, " + "vacantAbsolute = :vacantAbsolute, " + "vacantPositionUid = :vacantPositionGroup, " + "bibNoFrom = :bibNoFrom, " + "bibNoOrderUid = :bibNoOrderUid " + "WHERE id.startlistSettingNr = :startlistSettingNr " + "AND id.clientNr = :sessionClientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.setParameter("firstStartDiff", firstStartDiff);
    query.executeUpdate();

    deleteOptions(formData.getStartlistSettingNr());

    if (formData.getOptions().getValue() != null) {
      for (Long optionUid : formData.getOptions().getValue()) {
        RtStartlistSettingOption option = new RtStartlistSettingOption();
        RtStartlistSettingOptionKey key = new RtStartlistSettingOptionKey();
        key.setStartlistSettingNr(formData.getStartlistSettingNr());
        key.setClientNr(ServerSession.get().getSessionClientNr());
        key.setOptionUid(optionUid);
        option.setId(key);
        JPA.persist(option);

        if (CompareUtility.equals(optionUid, StartlistSettingOptionCodeType.AllowStarttimeWishesCode.ID)) {
          AdditionalInformationUtility.createStartTimeWish(formData.getEventNr());
        }
      }

    }

    SharedCacheServerUtility.notifyClients();
    return formData;
  }

  private void deleteOptions(Long startlistSettingNr) throws ProcessingException {
    String queryString = "DELETE FROM RtStartlistSettingOption " + "WHERE id.startlistSettingNr = :startlistSettingNr " + "AND id.clientNr = :sessionClientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("startlistSettingNr", startlistSettingNr);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();
  }

  @Override
  public StartlistSettingFormData delete(StartlistSettingFormData formData) throws ProcessingException {
    if (formData == null || formData.getStartlistSettingNr() == null) {
      return formData;
    }

    String queryString = "UPDATE RtEventClass " + "SET startlistSettingNr = NULL " + "WHERE startlistSettingNr = :startlistSettingNr " + "AND :startlistSettingNr IS NOT NULL " + "AND id.clientNr = :sessionClientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("startlistSettingNr", formData.getStartlistSettingNr());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();

    deleteOptions(formData.getStartlistSettingNr());

    StartlistVacantUtility.removeVacants(formData.getStartlistSettingNr());

    queryString = "DELETE FROM RtStartlistSetting " + "WHERE id.startlistSettingNr = :startlistSettingNr " + "AND id.clientNr = :sessionClientNr";
    query = JPA.createQuery(queryString);
    query.setParameter("startlistSettingNr", formData.getStartlistSettingNr());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();

    SharedCacheServerUtility.notifyClients();
    return formData;
  }

}
