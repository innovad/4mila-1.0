package com.rtiming.server.dataexchange;

import java.util.Date;
import java.util.List;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.IProcessingStatus;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.club.ClubFormData;
import com.rtiming.shared.common.database.sql.EntryBean;
import com.rtiming.shared.common.database.sql.ParticipationBean;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.DataExchangeUtility;
import com.rtiming.shared.dataexchange.IDataExchangeService;
import com.rtiming.shared.dataexchange.ImportMessage;
import com.rtiming.shared.dataexchange.ImportMessageList;
import com.rtiming.shared.dataexchange.cache.AreaDataCacher;
import com.rtiming.shared.dataexchange.cache.CityDataCacher;
import com.rtiming.shared.dataexchange.cache.CityRegionCacheKey;
import com.rtiming.shared.dataexchange.cache.CityRegionDataCacher;
import com.rtiming.shared.dataexchange.cache.ClazzDataCacher;
import com.rtiming.shared.dataexchange.cache.ClubDataCacher;
import com.rtiming.shared.dataexchange.cache.CurrencyDataCacher;
import com.rtiming.shared.dataexchange.cache.DataCacher;
import com.rtiming.shared.dataexchange.cache.DefaultAdditionalInformationStartFeeDataCacher;
import com.rtiming.shared.dataexchange.cache.DefaultCountryDataCacher;
import com.rtiming.shared.dataexchange.cache.DefaultCurrencyDataCacher;
import com.rtiming.shared.dataexchange.cache.DefaultEventDataCacher;
import com.rtiming.shared.dataexchange.cache.NationDataCacher;
import com.rtiming.shared.dataexchange.cache.RegistrationDataCacher;
import com.rtiming.shared.dataexchange.cache.StartblockDataCacher;
import com.rtiming.shared.dataexchange.city.GeneralCityBean;
import com.rtiming.shared.dataexchange.city.GeonamesCityBean;
import com.rtiming.shared.dataexchange.city.SwissPostCityBean;
import com.rtiming.shared.dataexchange.iof3.IOF300RunnerDataBean;
import com.rtiming.shared.dataexchange.oe2003.OE2003EntryBean;
import com.rtiming.shared.dataexchange.swiss.GO2OLEntryBean;
import com.rtiming.shared.dataexchange.swiss.SwissOrienteeringRunnerBean;
import com.rtiming.shared.dataexchange.swiss.SwissOrienteeringUtility;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.entry.IEntryProcessService;
import com.rtiming.shared.entry.RegistrationFormData;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.ICityProcessService;
import com.rtiming.shared.settings.currency.CurrencyFormData;

public class DataExchangeService  implements IDataExchangeService {

  @Override
  public void clearCaches() {
    DataCacher.get(ServerSession.get().getUserId()).clear();
  }

  @Override
  public ImportMessageList storeSwissOrienteeringRunner(List<AbstractDataBean> runner) throws ProcessingException {
    ImportMessageList list = new ImportMessageList();
    for (AbstractDataBean bean : runner) {
      try {
        storeSwissOrienteeringRunner((SwissOrienteeringRunnerBean) bean);
      }
      catch (Exception e) {
        ImportMessage message = new ImportMessage(bean.toString(), e.getMessage(), IProcessingStatus.ERROR);
        list.getErrors().add(message);
      }
    }
    return list;
  }

  private void storeSwissOrienteeringRunner(SwissOrienteeringRunnerBean bean) throws ProcessingException {

    // Caches
    ClubDataCacher clubCache = DataCacher.get(ServerSession.get().getUserId()).getCache(ClubDataCacher.class);
    AreaDataCacher areaCache = DataCacher.get(ServerSession.get().getUserId()).getCache(AreaDataCacher.class);
    CityRegionDataCacher cityCache = DataCacher.get(ServerSession.get().getUserId()).getCache(CityRegionDataCacher.class);
    NationDataCacher nationCache = DataCacher.get(ServerSession.get().getUserId()).getCache(NationDataCacher.class);
    ClazzDataCacher clazzCache = DataCacher.get(ServerSession.get().getUserId()).getCache(ClazzDataCacher.class);

    // Runner
    RunnerBean runner = new RunnerBean();
    IRunnerProcessService runnerService = BEANS.get(IRunnerProcessService.class);

    Long runnerNr = runnerService.findRunner(bean.getSwissOrienteeringDatabaseCode());
    if (runnerNr != null) {
      runner.setRunnerNr(runnerNr);
      if (runner.getRunnerNr() != null) {
        runner = runnerService.load(runner);
      }
    }
    runner.setExtKey(bean.getSwissOrienteeringDatabaseCode());
    runner.setLastName(bean.getLastName());
    runner.setFirstName(bean.getFirstName());
    runner.setYear(DataExchangeUtility.parseYear(bean.getYearOfBirth()));
    runner.setSexUid(DataExchangeUtility.parseSex(bean.getSex()));
    runner.getAddress().setStreet(bean.getStreet());
    runner.getAddress().setEmail(bean.getEmail());

    // Swiss Orienteering Anti Doping
    boolean antiDopingSigned = DataExchangeUtility.parseBoolean(bean.getDopStat());
    ImportUtility.importSwissOrienteeringAntiDopingSigned(antiDopingSigned, runner.getAddInfo());

    // Nation
    if (!SwissOrienteeringUtility.isNullOrEmptyOrDash(bean.getNationStr())) {
      runner.setNationUid(ImportUtility.importNation(bean.getNationStr(), nationCache));
    }

    // City
    String cityStr = bean.getCityStr();
    String zipStr = bean.getZipCode();
    String regionStr = bean.getCanton();
    String areaStr = bean.getRegion();
    String country = "CH";
    if (!StringUtility.isNullOrEmpty(bean.getCountry())) {
      country = bean.getCountry();
    }

    CityRegionCacheKey key = new CityRegionCacheKey(cityStr, zipStr, regionStr, country, null, null);
    CityFormData city = ImportUtility.importCityWithRegion(key, areaStr, cityCache, areaCache);
    runner.getAddress().setCityNr(city.getCityNr());

    // Club
    String clubStr = bean.getClubName();
    if (!StringUtility.isNullOrEmpty(clubStr)) {
      ClubFormData club = clubCache.get(clubStr);
      if (club.getClubNr() == null) {
        club.getName().setValue(clubStr);
        club = clubCache.put(clubStr, club);
      }
      runner.setClubNr(club.getClubNr());
    }

    // E-Card
    IECardProcessService eCardService = BEANS.get(IECardProcessService.class);
    String eCardNoStr = bean.geteCardNumber();
    if (!(StringUtility.isNullOrEmpty(eCardNoStr) || "0".equals(eCardNoStr))) {
      RtEcard eCard = eCardService.findECard(eCardNoStr);
      if (eCard.getKey() == null) {
        eCard.setRentalCard(false);
        eCard = eCardService.create(eCard);
      }
      runner.setECardNr(eCard.getKey().getId());
    }

    // Class
    Long classUid = null;
    if (!SwissOrienteeringUtility.isNullOrEmptyOrDash(bean.getClazzCode())) {
      CodeFormData clazz = clazzCache.get(bean.getClazzCode());
      if (clazz.getCodeUid() == null) {
        for (int k = 0; k < clazz.getMainBox().getLanguage().getRowCount(); k++) {
          clazz.getMainBox().getLanguage().setTranslation(k, bean.getClazzCode());
        }
        clazz = clazzCache.put(bean.getClazzCode(), clazz);
      }
      classUid = clazz.getCodeUid();
    }
    runner.setDefaultClassUid(classUid);

    // store Runner
    runner.setActive(true);
    if (runner.getRunnerNr() == null) {
      runner = runnerService.create(runner);
    }
    else {
      runner = runnerService.store(runner);
    }
  }

  @Override
  public ImportMessageList storeOE2003Entry(List<AbstractDataBean> entry, Long eventNr) throws ProcessingException {
    ImportMessageList list = new ImportMessageList();
    for (AbstractDataBean bean : entry) {
      try {
        storeOE2003Entry((OE2003EntryBean) bean, eventNr);
      }
      catch (Exception e) {
        ImportMessage message = new ImportMessage(bean.toString(), e.getMessage(), IProcessingStatus.ERROR);
        list.getErrors().add(message);
      }
    }
    return list;
  }

  private void storeOE2003Entry(OE2003EntryBean bean, Long eventNr) throws ProcessingException {

    // Caches
    ClubDataCacher clubCache = DataCacher.get(ServerSession.get().getUserId()).getCache(ClubDataCacher.class);
    CityDataCacher cityCache = DataCacher.get(ServerSession.get().getUserId()).getCache(CityDataCacher.class);
    DefaultCountryDataCacher countryCache = DataCacher.get(ServerSession.get().getUserId()).getCache(DefaultCountryDataCacher.class);
    DefaultCurrencyDataCacher currencyCache = DataCacher.get(ServerSession.get().getUserId()).addCache(new DefaultCurrencyDataCacher(eventNr));
    ClazzDataCacher clazzCache = DataCacher.get(ServerSession.get().getUserId()).getCache(ClazzDataCacher.class);
    StartblockDataCacher startblockCache = DataCacher.get(ServerSession.get().getUserId()).getCache(StartblockDataCacher.class);
    DefaultAdditionalInformationStartFeeDataCacher startFeeCache = DataCacher.get(ServerSession.get().getUserId()).addCache(new DefaultAdditionalInformationStartFeeDataCacher(eventNr));
    DefaultEventDataCacher eventCache = DataCacher.get(ServerSession.get().getUserId()).addCache(new DefaultEventDataCacher(eventNr));
    NationDataCacher nationCache = DataCacher.get(ServerSession.get().getUserId()).getCache(NationDataCacher.class);

    // E-Card
    String eCardNoStr = bean.getECardNumber();
    boolean rentalCard = DataExchangeUtility.parseBoolean(bean.getGemietet());
    RtEcard eCard = ImportUtility.importECard(eCardNoStr, rentalCard);

    // Club
    String clubStr = StringUtility.emptyIfNull(bean.getAbk()) +
        (StringUtility.isNullOrEmpty(bean.getAbk()) ? "" : " ") +
        StringUtility.emptyIfNull(bean.getOrt());
    ClubFormData club = null;
    if (!StringUtility.isNullOrEmpty(clubStr)) {
      club = ImportUtility.importClub(clubStr, clubCache);
    }

    // City
    String cityStr = bean.getWohnort();
    String zip = bean.getPLZ();
    CityFormData city = null;
    if (!StringUtility.isNullOrEmpty(cityStr) && !StringUtility.isNullOrEmpty(zip)) {
      String countryCode = countryCache.getDefault().getCountryCode().getValue();
      city = ImportUtility.importCity(cityStr, zip, countryCode, cityCache);
    }

    // Runner
    RunnerBean runner = ImportUtility.importRunner(bean.getDatenbankId());
    runner.setFirstName(bean.getVName());
    runner.setLastName(bean.getNName());
    if (!StringUtility.isNullOrEmpty(bean.getJg())) {
      runner.setYear(FMilaUtility.validateYear(DataExchangeUtility.parseLong(bean.getJg())));
    }
    if (!StringUtility.isNullOrEmpty(bean.getGeschlecht())) {
      runner.setSexUid(DataExchangeUtility.parseSex(bean.getGeschlecht()));
    }
    runner.setECardNr(eCard == null ? null : eCard.getKey().getId());
    if (club != null) {
      runner.setClubNr(club.getClubNr());
    }
    if (!StringUtility.isNullOrEmpty(bean.getEmail())) {
      runner.getAddress().setEmail(bean.getEMail());
    }
    if (!StringUtility.isNullOrEmpty(bean.getPhone())) {
      runner.getAddress().setPhone(bean.getPhone());
    }
    if (!StringUtility.isNullOrEmpty(bean.getFax())) {
      runner.getAddress().setFax(bean.getFax());
    }
    // street and city are always together updated
    if (city != null) {
      runner.getAddress().setCityNr(city.getCityNr());
      String street = ImportUtility.importStreet(bean.getStreet(), bean.getStreet2ndLine());
      if (!StringUtility.isNullOrEmpty(street)) {
        runner.getAddress().setStreet(street);
      }
    }
    runner.setExtKey(bean.getDatenbankId());
    if (!StringUtility.isNullOrEmpty(bean.getNation())) {
      runner.setNationUid(ImportUtility.importNation(bean.getNation(), nationCache));
    }

    // Class
    String kurz = bean.getKurz();
    String lang = bean.getLang();
    CodeFormData clazz = ImportUtility.importClass(kurz, lang, eventNr, clazzCache);

    // Entry
    EntryBean entry = new EntryBean();
    RaceBean race = new RaceBean();
    entry.addRace(race);
    race.setRunner(runner);
    race.setLegClassUid(clazz.getCodeUid());
    race.setEventNr(eventNr);
    race.setRunnerNr(runner.getRunnerNr());
    race.setECardNr(eCard == null ? null : eCard.getKey().getId());
    race.setBibNo(bean.getStartNumber());
    if (club != null) {
      race.setClubNr(club.getClubNr());
    }
    else {
      race.setClubNr(runner.getClubNr());
    }
    race.setAddress(runner.getAddress().copy());
    race.setNationUid(runner.getNationUid());

    entry.setCurrencyUid(currencyCache.getDefault().getCurrencyUid());

    ParticipationBean participation = new ParticipationBean();
    entry.addParticipation(participation);
    participation.setEventNr(eventNr);
    participation.setClassUid(clazz.getCodeUid());
    Date zeroTime = eventCache.getDefault().getZeroTime().getValue();
    participation.setStartTime(FMilaUtility.getDateDifferenceInMilliSeconds(zeroTime, DataExchangeUtility.parseDate(zeroTime, bean.getStart())));

    // Fees
    Double fee = DataExchangeUtility.parseDouble(bean.getStartgeld());
    ImportUtility.importStartFee(fee, entry, startFeeCache);

    // Startblock
    Long startblockUid = ImportUtility.importStartblock(bean.getBlock(), entry.getEntryNr(), eventNr, startblockCache);
    participation.setStartblockUid(startblockUid);

    entry = BEANS.get(IEntryProcessService.class).create(entry);

    // Payment
    if (DataExchangeUtility.parseBoolean(bean.getBezahlt()) && fee != null) {
      ImportUtility.importPayment(fee, entry.getRegistrationNr(), currencyCache.getDefault().getCurrencyUid());
    }
  }

  @Override
  public ImportMessageList storeGeneralCity(List<AbstractDataBean> entry) throws ProcessingException {
    ImportMessageList list = new ImportMessageList();
    for (AbstractDataBean bean : entry) {
      try {
        GeneralCityBean cityBean = (GeneralCityBean) bean;
        storeCity(cityBean.getCityStr(), cityBean.getZipCode(), cityBean.getRegion(), cityBean.getArea(), cityBean.getCountry());
      }
      catch (Exception e) {
        ImportMessage message = new ImportMessage(bean.toString(), e.getMessage(), IProcessingStatus.ERROR);
        list.getErrors().add(message);
      }
    }
    return list;
  }

  @Override
  public ImportMessageList storeGeonamesCity(List<AbstractDataBean> city) throws ProcessingException {
    ImportMessageList list = new ImportMessageList();
    for (AbstractDataBean bean : city) {
      try {
        GeonamesCityBean cityBean = (GeonamesCityBean) bean;
        storeCity(cityBean.getCityStr(), cityBean.getZipCode(), cityBean.getRegion(), null, cityBean.getCountry());
      }
      catch (Exception e) {
        ImportMessage message = new ImportMessage(bean.toString(), e.getMessage(), IProcessingStatus.ERROR);
        list.getErrors().add(message);
      }
    }
    return list;
  }

  @Override
  public ImportMessageList storeSwissPostCity(List<AbstractDataBean> entry) throws ProcessingException {
    ImportMessageList list = new ImportMessageList();
    for (AbstractDataBean bean : entry) {
      try {
        SwissPostCityBean cityBean = (SwissPostCityBean) bean;
        storeCity(cityBean.getCity(), cityBean.getZip(), cityBean.getRegion(), null, "CH");
      }
      catch (Exception e) {
        ImportMessage message = new ImportMessage(bean.toString(), e.getMessage(), IProcessingStatus.ERROR);
        list.getErrors().add(message);
      }
    }
    return list;
  }

  private void storeCity(String cityStr, String zip, String region, String area, String country) throws ProcessingException {
    ICityProcessService cityService = BEANS.get(ICityProcessService.class);
    CityRegionDataCacher cityCache = DataCacher.get(ServerSession.get().getUserId()).getCache(CityRegionDataCacher.class);
    AreaDataCacher areaCache = DataCacher.get(ServerSession.get().getUserId()).getCache(AreaDataCacher.class);

    String countryName = null;
    String countryCode = null;
    if (StringUtility.length(country) <= 3) {
      countryCode = country;
    }
    else {
      countryName = country;
    }

    CityRegionCacheKey key = new CityRegionCacheKey(cityStr, zip, region, countryCode, null, countryName);
    CityFormData city = ImportUtility.importCityWithRegion(key, area, cityCache, areaCache);
    if (city.getCityNr() == null) {
      city = cityService.create(city);
    }
    else {
      city = cityService.store(city);
    }
  }

  @Override
  public ImportMessageList storeGO2OLEntry(List<AbstractDataBean> entry, Long eventNr) throws ProcessingException {
    ImportMessageList list = new ImportMessageList();
    for (AbstractDataBean bean : entry) {
      try {
        storeGO2OLEntry((GO2OLEntryBean) bean, eventNr);
      }
      catch (Exception e) {
        ImportMessage message = new ImportMessage(bean.toString(), e.getMessage(), IProcessingStatus.ERROR);
        list.getErrors().add(message);
      }
    }
    return list;
  }

  private void storeGO2OLEntry(GO2OLEntryBean bean, Long eventNr) throws ProcessingException {

    // Caches
    ClubDataCacher clubCache = DataCacher.get(ServerSession.get().getUserId()).getCache(ClubDataCacher.class);
    CityRegionDataCacher cityRegionCache = DataCacher.get(ServerSession.get().getUserId()).getCache(CityRegionDataCacher.class);
    AreaDataCacher areaCache = DataCacher.get(ServerSession.get().getUserId()).getCache(AreaDataCacher.class);
    CurrencyDataCacher currencyCache = DataCacher.get(ServerSession.get().getUserId()).addCache(new CurrencyDataCacher(eventNr));
    ClazzDataCacher clazzCache = DataCacher.get(ServerSession.get().getUserId()).getCache(ClazzDataCacher.class);
    StartblockDataCacher startblockCache = DataCacher.get(ServerSession.get().getUserId()).getCache(StartblockDataCacher.class);
    DefaultAdditionalInformationStartFeeDataCacher startFeeCache = DataCacher.get(ServerSession.get().getUserId()).addCache(new DefaultAdditionalInformationStartFeeDataCacher(eventNr));
    DefaultEventDataCacher eventCache = DataCacher.get(ServerSession.get().getUserId()).addCache(new DefaultEventDataCacher(eventNr));
    NationDataCacher nationCache = DataCacher.get(ServerSession.get().getUserId()).getCache(NationDataCacher.class);
    RegistrationDataCacher registrationCache = DataCacher.get(ServerSession.get().getUserId()).getCache(RegistrationDataCacher.class);
    DefaultCountryDataCacher defaultCountry = DataCacher.get(ServerSession.get().getUserId()).getCache(DefaultCountryDataCacher.class);

    // E-Card
    RtEcard eCard = ImportUtility.importECard(bean.getCardnr(), false);

    // Club
    ClubFormData club = ImportUtility.importClub(bean.getClub(), clubCache);

    // City
    String countryCode = bean.getCountry();
    if (StringUtility.isNullOrEmpty(countryCode)) {
      countryCode = defaultCountry.getDefault().getCountryCode().getValue();
    }
    CityRegionCacheKey key = new CityRegionCacheKey(bean.getTown(), bean.getZip(), bean.getCanton(), countryCode, null, null);
    CityFormData city = ImportUtility.importCityWithRegion(key, bean.getRegion(), cityRegionCache, areaCache);

    // Runner
    RunnerBean runner = ImportUtility.importRunner(bean.getFednr());
    runner.setFirstName(bean.getFirstname());
    runner.setLastName(bean.getFamilyname());
    runner.setYear(DataExchangeUtility.parseLong(bean.getYear4()));
    runner.setSexUid(DataExchangeUtility.parseSex(bean.getSex()));
    runner.setECardNr(eCard == null ? null : eCard.getKey().getId());
    runner.setClubNr(club.getClubNr());
    runner.getAddress().setStreet(ImportUtility.importStreet(bean.getAdr1(), bean.getAdr2()));
    runner.getAddress().setEmail(bean.getEmail());
    runner.getAddress().setMobile(bean.getMobile());
    runner.getAddress().setCityNr(city.getCityNr());
    runner.setExtKey(bean.getFednr());

    // Swiss Orienteering Anti Doping
    boolean antiDopingSigned = DataExchangeUtility.parseBoolean(bean.getDopstat());
    ImportUtility.importSwissOrienteeringAntiDopingSigned(antiDopingSigned, runner.getAddInfo());

    // Nation
    runner.setNationUid(ImportUtility.importNation(bean.getNationstartedfor(), nationCache));

    // Class
    CodeFormData clazz = ImportUtility.importClass(bean.getClassshort(), bean.getClassshort(), eventNr, clazzCache);

    // Registration and Child Option
    String registrationNo = RegistrationServerUtility.buildRegistrationNo(bean.getGroupsort());
    boolean isChildOption = DataExchangeUtility.parseBoolean(bean.getChild());
    Long settingOptionUid = null;
    if (isChildOption) {
      registrationNo += "-S";
      settingOptionUid = StartlistSettingOptionCodeType.SplitRegistrationsCode.ID;
    }
    else {
      registrationNo += "-G";
      settingOptionUid = StartlistSettingOptionCodeType.GroupRegistrationsCode.ID;
    }
    Long registrationNr = null;
    RegistrationFormData registration = registrationCache.get(registrationNo);
    if (registration.getRegistrationNr() == null) {
      registration.getRegistrationNo().setValue(registrationNo);
      registration.getStartlistSettingOptionGroupBox().setValue(settingOptionUid);
      registration = registrationCache.put(registrationNo, registration);
    }
    registrationNr = registration.getRegistrationNr();

    // Currency
    CurrencyFormData currency = ImportUtility.importCurrency(bean.getCurrency(), currencyCache);

    // Entry
    EntryBean entry = new EntryBean();
    entry.setRegistrationNr(registrationNr);
    entry.setCurrencyUid(currency.getCurrencyUid());

    RaceBean race = new RaceBean();
    entry.addRace(race);
    race.setRunner(runner);
    race.setLegClassUid(clazz.getCodeUid());
    race.setEventNr(eventNr);
    race.setRunnerNr(runner.getRunnerNr());
    race.setECardNr(eCard == null ? null : eCard.getKey().getId());
    race.setBibNo(bean.getStartnr());
    race.setClubNr(NumberUtility.nvl(club.getClubNr(), runner.getClubNr())); // copy from runner if import data is empty
    race.setAddress(runner.getAddress().copy());
    race.setNationUid(runner.getNationUid());

    ParticipationBean participation = new ParticipationBean();
    entry.addParticipation(participation);
    participation.setEventNr(eventNr);
    participation.setClassUid(clazz.getCodeUid());
    Date zeroTime = eventCache.getDefault().getZeroTime().getValue();
    participation.setStartTime(FMilaUtility.getDateDifferenceInMilliSeconds(zeroTime, DataExchangeUtility.parseDate(zeroTime, bean.getStarttime())));

    // Start Time Wish
    if (DataExchangeUtility.parseBoolean(bean.getStartearly())) {
      ImportUtility.importStartTimeWish(AdditionalInformationCodeType.StartTimeWishEarlyStartCode.ID, entry.getAddInfo());
    }
    else if (DataExchangeUtility.parseBoolean(bean.getStartlate())) {
      ImportUtility.importStartTimeWish(AdditionalInformationCodeType.StartTimeWishLateStartCode.ID, entry.getAddInfo());
    }

    // Fees
    ImportUtility.importStartFee(DataExchangeUtility.parseDouble(bean.getFeeowed()), entry, startFeeCache);

    // Startblock
    Long startblockUid = ImportUtility.importStartblock(bean.getStartblock(), entry.getEntryNr(), eventNr, startblockCache);
    participation.setStartblockUid(startblockUid);

    // Store Entry
    entry = BEANS.get(IEntryProcessService.class).create(entry);

    // Payment
    Double paid = DataExchangeUtility.parseDouble(bean.getFeepayed());
    if (paid != null) {
      ImportUtility.importPayment(paid, entry.getRegistrationNr(), currency.getCurrencyUid());
    }

  }

  @Override
  public ImportMessageList storeIOF300Runner(List<AbstractDataBean> entry) throws ProcessingException {
    ImportMessageList list = new ImportMessageList();
    for (AbstractDataBean bean : entry) {
      try {
        storeIOF300Runner((IOF300RunnerDataBean) bean);
      }
      catch (Exception e) {
        ImportMessage message = new ImportMessage(bean.toString(), e.getMessage(), IProcessingStatus.ERROR);
        list.getErrors().add(message);
      }
    }
    return list;
  }

  private void storeIOF300Runner(IOF300RunnerDataBean bean) throws ProcessingException {
    RtEcard ecard = bean.getEcard();
    RunnerBean runner = bean.getRunner();

    if (ecard != null) {
      if (ecard.getKey() == null) {
        ecard = BEANS.get(IECardProcessService.class).create(ecard);
      }
      else {
        ecard = BEANS.get(IECardProcessService.class).store(ecard);
      }
    }
    if (runner != null) {
      if (ecard != null) {
        runner.setECardNr(ecard.getKey().getId());
      }
      if (runner.getRunnerNr() == null) {
        runner = BEANS.get(IRunnerProcessService.class).create(runner);
      }
      else {
        runner = BEANS.get(IRunnerProcessService.class).store(runner);
      }
    }
  }

}
