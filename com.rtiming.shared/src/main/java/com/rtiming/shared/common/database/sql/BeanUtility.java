package com.rtiming.shared.common.database.sql;

import java.util.Date;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.holders.ITableHolder;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.club.ClubFormData;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.report.template.ReportTemplateFormData;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtMap;
import com.rtiming.shared.dao.RtRanking;
import com.rtiming.shared.dao.RtRankingEvent;
import com.rtiming.shared.dao.RtReportTemplate;
import com.rtiming.shared.ecard.ECardFormData;
import com.rtiming.shared.entry.EntryFormData;
import com.rtiming.shared.entry.EntryFormData.Events.EventsRowData;
import com.rtiming.shared.entry.EntryFormData.Races.RacesRowData;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.event.EventFormData;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.map.MapFormData;
import com.rtiming.shared.race.RaceFormData;
import com.rtiming.shared.ranking.FormulaUtility;
import com.rtiming.shared.ranking.RankingEventFormData;
import com.rtiming.shared.ranking.RankingFormData;
import com.rtiming.shared.ranking.RankingFormatCodeType;
import com.rtiming.shared.runner.RunnerFormData;
import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationFieldData;
import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationFieldData.AbstractAdditionalInformationRowData;
import com.rtiming.shared.settings.city.AbstractAddressBoxData;
import com.rtiming.shared.settings.clazz.AgeFormData;

public final class BeanUtility {

  private BeanUtility() {
  }

  public static RtClassAge ageFormData2bean(AgeFormData formData) {
    RtClassAge bean = new RtClassAge();
    bean.setAgeFrom(formData.getFrom().getValue());
    bean.setAgeTo(formData.getTo().getValue());
    bean.setId(formData.getKey());
    bean.setClassUid(formData.getClazz().getValue());
    bean.setSexUid(formData.getSex().getValue());
    return bean;
  }

  public static AgeFormData ageBean2FormData(RtClassAge bean) {
    AgeFormData formData = new AgeFormData();
    formData.getFrom().setValue(bean.getAgeFrom());
    formData.getTo().setValue(bean.getAgeTo());
    formData.setKey(bean.getId());
    formData.getClazz().setValue(bean.getClassUid());
    formData.getSex().setValue(bean.getSexUid());
    return formData;
  }

  public static RtReportTemplate reportTemplateFormData2bean(ReportTemplateFormData formData) {
    RtReportTemplate bean = new RtReportTemplate();
    bean.setActive(formData.getActive().getValue());
    bean.setEventNr(formData.getEvent().getValue());
    bean.setId(formData.getKey());
    bean.setTypeUid(formData.getReportType().getValue());
    bean.setTemplateFiles(formData.getTemplateFiles());
    bean.setShortcut(formData.getShortcut().getValue());
    return bean;
  }

  public static ReportTemplateFormData reportTemplateBean2FormData(RtReportTemplate bean) {
    ReportTemplateFormData formData = new ReportTemplateFormData();
    formData.getActive().setValue(bean.getActive());
    formData.getEvent().setValue(bean.getEventNr());
    formData.setKey(bean.getId());
    formData.getReportType().setValue(bean.getTypeUid());
    formData.setTemplateFiles(bean.getTemplateFiles());
    formData.getShortcut().setValue(bean.getShortcut());
    return formData;
  }

  public static RtEcard eCardFormData2bean(ECardFormData formData) {
    RtEcard bean = new RtEcard();
    bean.setEcardNo(formData.getNumber().getValue());
    bean.setKey(formData.getECardKey());
    bean.setRentalCard(formData.getRentalCard().getValue());
    bean.setTypeUid(formData.getECardType().getValue());
    return bean;
  }

  public static ECardFormData eCardBean2FormData(RtEcard bean) {
    ECardFormData formData = new ECardFormData();
    formData.getNumber().setValue(bean.getEcardNo());
    formData.setECardKey(bean.getKey());
    formData.getRentalCard().setValue(bean.getRentalCard());
    formData.getECardType().setValue(bean.getTypeUid());
    return formData;
  }

  public static RtMap mapFormData2bean(MapFormData formData) {
    RtMap bean = new RtMap();
    bean.setEvtFileLastUpdate(formData.getEvtFileLastUpdate());
    bean.setFormat(formData.getFormat());
    bean.setH(formData.getHeight().getValue());
    bean.setId(formData.getMapKey());
    bean.setIsFileChanged(formData.isFileChanged());
    bean.setMapData(formData.getMapData());
    bean.setName(formData.getName().getValue());
    bean.setNewEventNr(formData.getNewEventNr());
    bean.setNeX(NumberUtility.toDouble(formData.getNECornerBox().getX().getValue()));
    bean.setNeY(NumberUtility.toDouble(formData.getNECornerBox().getY().getValue()));
    bean.setNwX(NumberUtility.toDouble(formData.getNWCornerBox().getX().getValue()));
    bean.setNwY(NumberUtility.toDouble(formData.getNWCornerBox().getY().getValue()));
    bean.setSeX(NumberUtility.toDouble(formData.getSECornerBox().getX().getValue()));
    bean.setSeY(NumberUtility.toDouble(formData.getSECornerBox().getY().getValue()));
    bean.setSwX(NumberUtility.toDouble(formData.getSWCornerBox().getX().getValue()));
    bean.setSwY(NumberUtility.toDouble(formData.getSWCornerBox().getY().getValue()));
    bean.setOriginX(NumberUtility.toDouble(formData.getOriginX().getValue()));
    bean.setOriginY(NumberUtility.toDouble(formData.getOriginY().getValue()));
    bean.setResolution(NumberUtility.toDouble(formData.getResolution().getValue()));
    bean.setScale(formData.getScale().getValue());
    bean.setW(formData.getWidth().getValue());
    return bean;
  }

  public static MapFormData mapBean2FormData(RtMap bean) {
    MapFormData formData = new MapFormData();
    formData.setEvtFileLastUpdate(bean.getEvtFileLastUpdate());
    formData.setFormat(bean.getFormat());
    formData.getHeight().setValue(bean.getH());
    formData.setMapKey(bean.getId());
    formData.setFileChanged(bean.getIsFileChanged());
    formData.setMapData(bean.getMapData());
    formData.getName().setValue(bean.getName());
    formData.setNewEventNr(bean.getNewEventNr());
    formData.getNECornerBox().getX().setValue(bean.getNeX());
    formData.getNECornerBox().getY().setValue(bean.getNeY());
    formData.getNWCornerBox().getX().setValue(bean.getNwX());
    formData.getNWCornerBox().getY().setValue(bean.getNwY());
    formData.getSECornerBox().getX().setValue(bean.getSeX());
    formData.getSECornerBox().getY().setValue(bean.getSeY());
    formData.getSWCornerBox().getX().setValue(bean.getSwX());
    formData.getSWCornerBox().getY().setValue(bean.getSwY());
    formData.getOriginX().setValue(bean.getOriginX());
    formData.getOriginY().setValue(bean.getOriginY());
    formData.getResolution().setValue(bean.getResolution());
    formData.getScale().setValue(bean.getScale());
    formData.getWidth().setValue(bean.getW());
    return formData;
  }

  public static RtRanking rankingFormData2bean(RankingFormData formData) {
    RtRanking bean = new RtRanking();
    bean.setDecimalPlaces(formData.getRankingBox().getDecimalPlaces().getValue());
    bean.setFormatUid(formData.getRankingBox().getFormat().getValue());
    bean.setFormula(formData.getRankingBox().getFormula().getValue());
    bean.setFormulaTypeUid(formData.getRankingBox().getFormulaType().getValue());
    bean.setTimePrecisionUid(formData.getRankingBox().getTimePrecision().getValue());
    bean.setId(formData.getKey());
    bean.setName(formData.getName().getValue());
    bean.setSortingUid(formData.getRankingBox().getSorting().getValue());
    return bean;
  }

  public static RankingFormData rankingBean2FormData(RtRanking bean) {
    RankingFormData formData = new RankingFormData();
    formData.getRankingBox().getDecimalPlaces().setValue(bean.getDecimalPlaces());
    formData.getRankingBox().getFormat().setValue(bean.getFormatUid());
    formData.getRankingBox().getFormula().setValue(bean.getFormula());
    formData.getRankingBox().getFormulaType().setValue(bean.getFormulaTypeUid());
    formData.getRankingBox().getTimePrecision().setValue(bean.getTimePrecisionUid());
    formData.setKey(bean.getId());
    formData.getName().setValue(bean.getName());
    formData.getRankingBox().getSorting().setValue(bean.getSortingUid());

    if (CompareUtility.equals(bean.getFormatUid(), RankingFormatCodeType.TimeCode.ID)) {
      formData.getRankingBox().getDecimalPlaces().setValue((FormulaUtility.timePrecision2decimalPlaces(formData.getRankingBox().getTimePrecision().getValue())));
    }
    return formData;
  }

  public static RtRankingEvent rankingEventFormData2bean(RankingEventFormData formData) {
    RtRankingEvent bean = new RtRankingEvent();
    bean.setDecimalPlaces(formData.getRankingBox().getDecimalPlaces().getValue());
    bean.setFormatUid(formData.getRankingBox().getFormat().getValue());
    bean.setFormula(formData.getRankingBox().getFormula().getValue());
    bean.setFormulaTypeUid(formData.getRankingBox().getFormulaType().getValue());
    bean.setTimePrecisionUid(formData.getRankingBox().getTimePrecision().getValue());
    bean.setId(formData.getKey());
    if (bean.getId() != null && formData.getEvent().getValue() != null) {
      bean.getId().setEventNr(formData.getEvent().getValue());
    }
    bean.setSortingUid(formData.getRankingBox().getSorting().getValue());
    bean.setSortcode(formData.getSortCode().getValue());
    return bean;
  }

  public static RankingEventFormData rankingEventBean2FormData(RtRankingEvent bean) {
    RankingEventFormData formData = new RankingEventFormData();
    formData.getRankingBox().getDecimalPlaces().setValue(bean.getDecimalPlaces());
    formData.getRankingBox().getFormat().setValue(bean.getFormatUid());
    formData.getRankingBox().getFormula().setValue(bean.getFormula());
    formData.getRankingBox().getFormulaType().setValue(bean.getFormulaTypeUid());
    formData.getRankingBox().getTimePrecision().setValue(bean.getTimePrecisionUid());
    formData.setKey(bean.getId());
    formData.getRankingBox().getSorting().setValue(bean.getSortingUid());
    if (bean.getId() != null && bean.getId().getEventNr() != null) {
      formData.getEvent().setValue(bean.getId().getEventNr());
    }
    formData.getSortCode().setValue(bean.getSortcode());

    if (CompareUtility.equals(bean.getFormatUid(), RankingFormatCodeType.TimeCode.ID)) {
      formData.getRankingBox().getDecimalPlaces().setValue((FormulaUtility.timePrecision2decimalPlaces(formData.getRankingBox().getTimePrecision().getValue())));
    }
    return formData;
  }

  public static EventBean eventFormData2bean(EventFormData formData) {
    EventBean event = new EventBean();
    event.setClientNr(formData.getClientNr());
    event.setEventNr(formData.getEventNr());
    event.setEvtFinish(formData.getFinishTime().getValue());
    event.setEvtLastUpload(formData.getEvtLastUpload());
    event.setEvtZero(formData.getZeroTime().getValue());
    event.setFormat(formData.getFormat());
    event.setLocation(formData.getLocation().getValue());
    event.setLogoData(formData.getLogoData());
    event.setMap(formData.getMapp().getValue());
    event.setName(formData.getName().getValue());
    event.setPunchingSystemUid(formData.getPunchingSystem().getValue());
    event.setTimezone(formData.getTimezone().getValue());
    event.setTypeUid(formData.getEventType().getValue());
    return event;
  }

  public static EventFormData eventBean2formData(EventBean bean) {
    EventFormData formData = new EventFormData();
    formData.setClientNr(bean.getClientNr());
    formData.setEventNr(bean.getEventNr());
    formData.getFinishTime().setValue(bean.getEvtFinish());
    formData.setEvtLastUpload(bean.getEvtLastUpload());
    formData.getZeroTime().setValue(bean.getEvtZero());
    formData.setFormat(bean.getFormat());
    formData.getLocation().setValue(bean.getLocation());
    formData.setLogoData(bean.getLogoData());
    formData.getMapp().setValue(bean.getMap());
    formData.getName().setValue(bean.getName());
    formData.getPunchingSystem().setValue(bean.getPunchingSystemUid());
    formData.getTimezone().setValue(bean.getTimezone());
    formData.getEventType().setValue(bean.getTypeUid());
    return formData;
  }

  public static ClubBean clubFormData2bean(ClubFormData formData) {
    ClubBean bean = new ClubBean();
    bean.setClubNr(formData.getClubNr());
    bean.setContactRunnerNr(formData.getContactRunner().getValue());
    bean.setExtKey(formData.getExtKey().getValue());
    bean.setName(formData.getName().getValue());
    bean.setShortcut(formData.getShortcut().getValue());

    addInfoFormData2Bean(formData.getAdditionalInformation(), bean.getAddInfo(), EntityCodeType.ClubCode.ID, formData.getClubNr(), null);

    return bean;
  }

  public static ClubFormData clubBean2formData(ClubBean bean) {
    ClubFormData formData = new ClubFormData();
    formData.setClubNr(bean.getClubNr());
    formData.getContactRunner().setValue(bean.getContactRunnerNr());
    formData.getExtKey().setValue(bean.getExtKey());
    formData.getName().setValue(bean.getName());
    formData.getShortcut().setValue(bean.getShortcut());

    addInfoBean2FormData(bean.getAddInfo(), formData.getAdditionalInformation());

    return formData;
  }

  public static RunnerBean runnerFormData2bean(RunnerFormData formData) {
    RunnerBean bean = new RunnerBean();
    bean.setRunnerNr(formData.getRunnerNr());
    bean.setActive(BooleanUtility.nvl(formData.getActive().getValue()));
    bean.setClientNr(formData.getClientNr());
    bean.setClubNr(formData.getClub().getValue());
    bean.setDefaultClassUid(formData.getDefaultClazz().getValue());
    bean.setECardNr(formData.getECard().getValue());
    bean.setEvtBirth(formData.getBirthdate().getValue());
    bean.setExtKey(formData.getExtKey().getValue());
    bean.setFirstName(formData.getFirstName().getValue());
    bean.setLastName(formData.getLastName().getValue());
    bean.setNationUid(formData.getNationUid().getValue());
    bean.setSexUid(formData.getSex().getValue());
    bean.setYear(formData.getYear().getValue());

    addressFormData2Bean(formData.getAbstractAddressBoxData(), bean.getAddress(), formData.getAddressNr(), formData.getClientNr());
    addInfoFormData2Bean(formData.getAdditionalInformation(), bean.getAddInfo(), EntityCodeType.RunnerCode.ID, formData.getRunnerNr(), formData.getClientNr());

    return bean;
  }

  public static RunnerFormData runnerBean2formData(RunnerBean bean) {
    RunnerFormData formData = new RunnerFormData();
    formData.getActive().setValue(bean.isActive());
    formData.setAddressNr(bean.getAddress().getAddressNr());
    formData.getBirthdate().setValue(bean.getEvtBirth());
    formData.setClientNr(bean.getClientNr());
    formData.getClub().setValue(bean.getClubNr());
    formData.getDefaultClazz().setValue(bean.getDefaultClassUid());
    formData.getECard().setValue(bean.getECardNr());
    formData.getExtKey().setValue(bean.getExtKey());
    formData.getFirstName().setValue(bean.getFirstName());
    formData.getLastName().setValue(bean.getLastName());
    formData.getNationUid().setValue(bean.getNationUid());
    formData.setRunnerNr(bean.getRunnerNr());
    formData.getSex().setValue(bean.getSexUid());
    formData.getYear().setValue(bean.getYear());

    addressBean2formData(bean.getAddress(), formData.getAbstractAddressBoxData());
    addInfoBean2FormData(bean.getAddInfo(), formData.getAdditionalInformation());

    return formData;
  }

  public static EntryBean entryFormData2bean(EntryFormData formData, EventConfiguration configuration) {
    EntryBean bean = new EntryBean();
    bean.setEntryNr(formData.getEntryNr());
    bean.setCurrencyUid(formData.getCurrencyUid().getValue());
    bean.setEvtEntry(formData.getEvtEntry().getValue());
    bean.setRegistrationNr(formData.getRegistration().getValue());

    for (int row = 0; row < formData.getEvents().getRowCount(); row++) {
      if (formData.getEvents().getRows()[row].getRowState() != ITableHolder.STATUS_DELETED) {
        Long eventNr = formData.getEvents().getRows()[row].getEventNr();
        ParticipationBean participation = formData.getEvents().getRows()[row].getParticipationBean();
        if (participation == null) {
          participation = new ParticipationBean();
        }
        bean.addParticipation(participation);
        participation.setClassUid(formData.getEvents().getRows()[row].getEventClass());
        participation.setEntryNr(formData.getEntryNr());
        participation.setEventNr(eventNr);
        participation.setStartTime(FMilaUtility.getDateDifferenceInMilliSeconds(configuration.getEvent(eventNr).getEvtZero(), formData.getEvents().getRows()[row].getStartTime()));
      }
    }

    for (int row = 0; row < formData.getRaces().getRowCount(); row++) {
      if (formData.getRaces().getRows()[row].getRowState() != ITableHolder.STATUS_DELETED) {
        RaceBean race = formData.getRaces().getRows()[row].getRaceBean();
        if (race == null) {
          race = new RaceBean();
        }
        bean.addRace(race);

        Long eventNr = formData.getRaces().getRows()[row].getRaceEvent();
        race.setBibNo(formData.getRaces().getRows()[row].getBibNumber());
        race.setClubNr(formData.getRaces().getRows()[row].getClubNr());
        race.setECardNr(formData.getRaces().getRows()[row].getECard());
        race.setEntryNr(formData.getEntryNr());
        race.setEventNr(eventNr);
        race.setLegClassUid(formData.getRaces().getRows()[row].getLeg());
        race.setLegStartTime(FMilaUtility.getDateDifferenceInMilliSeconds(configuration.getEvent(eventNr).getEvtZero(), formData.getRaces().getRows()[row].getLegStartTime()));
        race.setNationUid(formData.getRaces().getRows()[row].getNation());
        race.setRaceNr(formData.getRaces().getRows()[row].getRaceNr());
        race.setRunnerNr(formData.getRaces().getRows()[row].getRunnerNr());
      }
    }

    for (int row = 0; row < formData.getFees().getRowCount(); row++) {
      if (formData.getFees().getRows()[row].getRowState() != ITableHolder.STATUS_DELETED) {
        FeeBean fee = new FeeBean();
        bean.addFee(fee);
        fee.setAmount(NumberUtility.toDouble(formData.getFees().getRows()[row].getAmount()));
        fee.setCashPaymentOnRegistration(formData.getFees().getRows()[row].getCashPaymentOnRegistration());
        fee.setCurrencyUid(formData.getFees().getRows()[row].getCurrency());
      }
    }

    addInfoFormData2Bean(formData.getAdditionalInformationEntry(), bean.getAddInfo(), EntityCodeType.EntryCode.ID, formData.getEntryNr(), null);

    return bean;
  }

  public static EntryFormData entryBean2formData(EntryBean bean, EventConfiguration configuration) {
    EntryFormData formData = new EntryFormData();
    formData.setEntryNr(bean.getEntryNr());
    formData.getCurrencyUid().setValue(bean.getCurrencyUid());
    formData.getEvtEntry().setValue(bean.getEvtEntry());
    formData.getRegistration().setValue(bean.getRegistrationNr());

    for (ParticipationBean participation : bean.getParticipations()) {
      EventsRowData row = formData.getEvents().addRow();
      row.setParticipationBean(participation);
      row.setEventClass(participation.getClassUid());
      row.setEventNr(participation.getEventNr());
      if (participation.getStartTime() != null) {
        row.setStartTime(FMilaUtility.addMilliSeconds(configuration.getEvent(participation.getEventNr()).getEvtZero(), participation.getStartTime()));
      }
    }

    for (RaceBean race : bean.getRaces()) {
      RacesRowData row = formData.getRaces().addRow();
      row.setRaceBean(race);
      row.setBibNumber(race.getBibNo());
      row.setClubNr(race.getClubNr());
      row.setECard(race.getECardNr());
      row.setLeg(race.getLegClassUid());
      if (race.getLegStartTime() != null) {
        row.setLegStartTime(FMilaUtility.addMilliSeconds(configuration.getEvent(race.getEventNr()).getEvtZero(), race.getLegStartTime()));
      }
      row.setFirstName(race.getRunner().getFirstName());
      row.setLastName(race.getRunner().getLastName());
      row.setNation(race.getNationUid());
      row.setRaceEvent(race.getEventNr());
      row.setRaceNr(race.getRaceNr());
      row.setRunnerNr(race.getRunnerNr());
    }

    addInfoBean2FormData(bean.getAddInfo(), formData.getAdditionalInformationEntry());

    return formData;
  }

  public static RaceBean raceFormData2bean(RaceFormData formData) throws ProcessingException {
    Date zeroTime = null;
    Long legStartTime = formData.getRawLegStartTime();
    Long legTime = formData.getRawLegTime();

    if (formData.getLegStartTime().getValue() != null) {
      zeroTime = getZeroTime(formData, zeroTime);
      legStartTime = FMilaUtility.getDateDifferenceInMilliSeconds(zeroTime, formData.getLegStartTime().getValue());
    }
    if (formData.getLegFinishTime().getValue() != null && legStartTime != null) {
      zeroTime = getZeroTime(formData, zeroTime);
      Long legFinishTime = FMilaUtility.getDateDifferenceInMilliSeconds(zeroTime, formData.getLegFinishTime().getValue());
      legTime = legFinishTime - legStartTime;
    }

    RaceBean bean = new RaceBean();
    bean.setRaceNr(formData.getRaceNr());
    bean.setBibNo(formData.getBibNumber().getValue());
    bean.setClientNr(formData.getClientNr());
    bean.setClubNr(formData.getClub().getValue());
    bean.setECardNr(formData.getECardNr().getValue());
    bean.setEntryNr(formData.getEntryNr());
    bean.setEventNr(formData.getEventNr().getValue());
    bean.setLegClassUid(formData.getLegClassUid().getValue());
    bean.setLegStartTime(legStartTime);
    bean.setLegTime(legTime);
    bean.setManualStatus(formData.getManualStatus().getValue());
    bean.setNationUid(formData.getNation().getValue());
    bean.setRunnerNr(formData.getRunnerNr().getValue());
    bean.setRunner(formData.getRunner());
    bean.setStatusUid(formData.getRaceStatus().getValue());

    addressFormData2Bean(formData.getAbstractAddressBoxData(), bean.getAddress(), formData.getAddressNr(), formData.getClientNr());

    return bean;
  }

  public static RaceFormData raceBean2formData(RaceBean bean) {
    RaceFormData formData = new RaceFormData();
    formData.setRaceNr(bean.getRaceNr());
    formData.getBibNumber().setValue(bean.getBibNo());
    formData.setAddressNr(bean.getAddress().getAddressNr());
    formData.setClientNr(bean.getClientNr());
    formData.getClub().setValue(bean.getClubNr());
    formData.getECardNr().setValue(bean.getECardNr());
    formData.setEntryNr(bean.getEntryNr());
    formData.getEventNr().setValue(bean.getEventNr());
    formData.getLegClassUid().setValue(bean.getLegClassUid());
    formData.setRawLegStartTime(bean.getLegStartTime());
    formData.setRawLegTime(bean.getLegTime());
    formData.getManualStatus().setValue(bean.isManualStatus());
    formData.getNation().setValue(bean.getNationUid());
    formData.getRunnerNr().setValue(bean.getRunnerNr());
    formData.setRunner(bean.getRunner());
    formData.getRaceStatus().setValue(bean.getStatusUid());

    addressBean2formData(bean.getAddress(), formData.getAbstractAddressBoxData());

    return formData;
  }

  public static AbstractAdditionalInformationFieldData addInfoBean2FormData(AdditionalInformationBean bean) {
    AbstractAdditionalInformationFieldData formData = new RunnerFormData().getAdditionalInformation();
    return addInfoBean2FormData(bean, formData);
  }

  private static AbstractAdditionalInformationFieldData addInfoBean2FormData(AdditionalInformationBean bean, AbstractAdditionalInformationFieldData formData) {
    formData.clearRows();
    for (AdditionalInformationValueBean value : bean.getValues()) {
      AbstractAdditionalInformationRowData row = formData.addRow();
      row.setAdditionalInformationUid(value.getAdditionalInformationUid());
      row.setDecimal(value.getValueDouble());
      row.setDefaultValueDecimal(value.getDefaultDecimal());
      row.setDefaultValueInteger(value.getDefaultInteger());
      row.setDefaultValueText(value.getDefaultText());
      row.setFeeGroup(value.getFeeGroupNr());
      row.setInteger(value.getValueInteger());
      row.setMandatory(value.isMandatory());
      row.setMaximum(value.getValueMax());
      row.setMinimum(value.getValueMin());
      row.setText(value.getValueString());
      row.setType(value.getTypeUid());
    }
    return formData;
  }

  public static AdditionalInformationBean addInfoFormData2Bean(AbstractAdditionalInformationFieldData formData, AdditionalInformationBean bean, Long entityUid, Long joinNr, Long clientNr) {
    bean.getValues().clear();
    bean.setEntityUid(entityUid);
    bean.setJoinNr(joinNr);
    bean.setClientNr(clientNr);
    for (int k = 0; k < formData.getRowCount(); k++) {
      AdditionalInformationValueBean value = new AdditionalInformationValueBean();
      value.setAdditionalInformationUid(formData.getRows()[k].getAdditionalInformationUid());
      value.setValueDouble(formData.getRows()[k].getDecimal() == null ? null : formData.getRows()[k].getDecimal().doubleValue());
      value.setDefaultDecimal(formData.getRows()[k].getDefaultValueDecimal() == null ? null : formData.getRows()[k].getDefaultValueDecimal().doubleValue());
      value.setDefaultInteger(formData.getRows()[k].getDefaultValueInteger());
      value.setDefaultText(formData.getRows()[k].getDefaultValueText());
      value.setFeeGroupNr(formData.getRows()[k].getFeeGroup());
      value.setValueInteger(formData.getRows()[k].getInteger());
      value.setMandatory(formData.getRows()[k].getMandatory());
      value.setValueMax(formData.getRows()[k].getMaximum() == null ? null : formData.getRows()[k].getMaximum().doubleValue());
      value.setValueMin(formData.getRows()[k].getMinimum() == null ? null : formData.getRows()[k].getMinimum().doubleValue());
      value.setValueString(formData.getRows()[k].getText());
      value.setTypeUid(formData.getRows()[k].getType());
      bean.addValue(value);
    }
    return bean;
  }

  public static void addressBean2formData(AddressBean bean, AbstractAddressBoxData formData) {
    formData.getCity().setValue(bean.getCityNr());
    formData.getEMail().setValue(bean.getEmail());
    formData.getFax().setValue(bean.getFax());
    formData.getMobile().setValue(bean.getMobile());
    formData.getPhone().setValue(bean.getPhone());
    formData.getStreet().setValue(bean.getStreet());
    formData.getUrl().setValue(bean.getWww());
  }

  public static void addressFormData2Bean(AbstractAddressBoxData formData, AddressBean bean, Long addressNr, Long clientNr) {
    bean.setAddressNr(addressNr);
    bean.setCityNr(formData.getCity().getValue());
    bean.setClientNr(clientNr);
    bean.setEmail(formData.getEMail().getValue());
    bean.setFax(formData.getFax().getValue());
    bean.setMobile(formData.getMobile().getValue());
    bean.setPhone(formData.getPhone().getValue());
    bean.setStreet(formData.getStreet().getValue());
    bean.setWww(formData.getUrl().getValue());
  }

  private static Date getZeroTime(RaceFormData formData, Date zeroTime) throws ProcessingException {
    if (zeroTime == null) {
      zeroTime = BEANS.get(IEventProcessService.class).getZeroTime(formData.getEventNr().getValue());
    }
    return zeroTime;
  }

}
