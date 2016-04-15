package com.rtiming.server.dataexchange;

import java.math.BigDecimal;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.club.ClubFormData;
import com.rtiming.shared.common.database.sql.AdditionalInformationBean;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.common.database.sql.EntryBean;
import com.rtiming.shared.common.database.sql.PaymentBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dataexchange.cache.AreaDataCacher;
import com.rtiming.shared.dataexchange.cache.CityCacheKey;
import com.rtiming.shared.dataexchange.cache.CityDataCacher;
import com.rtiming.shared.dataexchange.cache.CityRegionCacheKey;
import com.rtiming.shared.dataexchange.cache.CityRegionDataCacher;
import com.rtiming.shared.dataexchange.cache.ClazzDataCacher;
import com.rtiming.shared.dataexchange.cache.ClubDataCacher;
import com.rtiming.shared.dataexchange.cache.CurrencyDataCacher;
import com.rtiming.shared.dataexchange.cache.DefaultAdditionalInformationStartFeeDataCacher;
import com.rtiming.shared.dataexchange.cache.NationDataCacher;
import com.rtiming.shared.dataexchange.cache.StartblockDataCacher;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.entry.payment.IPaymentProcessService;
import com.rtiming.shared.entry.payment.PaymentTypeCodeType;
import com.rtiming.shared.entry.startblock.EventStartblockFormData;
import com.rtiming.shared.entry.startblock.IEventStartblockProcessService;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.IEventClassProcessService;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.city.CityFormData;
import com.rtiming.shared.settings.city.CountryFormData;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;

public final class ImportUtility {

  private ImportUtility() {
  }

  public static RtEcard importECard(String eCardNoStr, boolean rentalCard) throws ProcessingException {
    RtEcard eCard = null;
    if (!(StringUtility.isNullOrEmpty(eCardNoStr) || "0".equals(eCardNoStr))) {
      IECardProcessService eCardService = BEANS.get(IECardProcessService.class);
      eCard = eCardService.findECard(eCardNoStr);
      boolean oldRental = BooleanUtility.nvl(eCard.getRentalCard());
      eCard.setRentalCard(rentalCard);
      if (eCard.getKey() == null) {
        eCard = eCardService.create(eCard);
      }
      else if (oldRental != rentalCard) {
        eCard = eCardService.store(eCard);
      }
    }
    return eCard;
  }

  public static ClubFormData importClub(String clubStr, ClubDataCacher clubCache) throws ProcessingException {
    ClubFormData club = new ClubFormData();
    if (!StringUtility.isNullOrEmpty(clubStr)) {
      club = clubCache.get(clubStr);
      if (club.getClubNr() == null) {
        club.getName().setValue(clubStr);
        club = clubCache.put(clubStr, club);
      }
    }
    return club;
  }

  public static CurrencyFormData importCurrency(String currencyStr, CurrencyDataCacher currencyCache) throws ProcessingException {
    CurrencyFormData currency = new CurrencyFormData();
    if (!StringUtility.isNullOrEmpty(currencyStr)) {
      currency = currencyCache.get(currencyStr);
      if (currency.getCurrencyUid() == null) {
        currency = BEANS.get(ICurrencyProcessService.class).prepareCreate(currency);
        currency.getExchangeRate().setValue(BigDecimal.ONE);
        currency.getCodeBox().getShortcut().setValue(currencyStr);
        for (int k = 0; k < currency.getCodeBox().getLanguage().getRowCount(); k++) {
          currency.getCodeBox().getLanguage().rowAt(k).setTranslation(currencyStr);
        }
        currency = currencyCache.put(currencyStr, currency);
      }
    }
    return currency;
  }

  public static RunnerBean importRunner(String extKey) throws ProcessingException {
    RunnerBean runner = new RunnerBean();
    if (!StringUtility.isNullOrEmpty(extKey)) {
      Long runnerNr = BEANS.get(IRunnerProcessService.class).findRunner(extKey);
      runner.setRunnerNr(runnerNr);
      if (runner.getRunnerNr() != null) {
        runner = BEANS.get(IRunnerProcessService.class).load(runner);
      }
    }
    return runner;
  }

  public static CodeFormData importClass(String kurz, String lang, Long eventNr, ClazzDataCacher clazzCache) throws ProcessingException {
    CodeFormData clazz = clazzCache.get(kurz);
    if (clazz.getCodeUid() == null) {
      for (int k = 0; k < clazz.getMainBox().getLanguage().getRowCount(); k++) {
        clazz.getMainBox().getLanguage().rowAt(k).setTranslation(lang);
      }
      clazz = clazzCache.put(kurz, clazz);
    }

    EventClassFormData eventClass = new EventClassFormData();
    eventClass.getEvent().setValue(eventNr);
    eventClass.getClazz().setValue(clazz.getCodeUid());
    IEventClassProcessService eventClassSvc = BEANS.get(IEventClassProcessService.class);
    eventClass = eventClassSvc.load(eventClass);
    if (eventClass.getType().getValue() == null) {
      eventClassSvc.prepareCreate(eventClass);
      eventClassSvc.create(eventClass);
    }
    return clazz;
  }

  public static CityFormData importCity(String cityStr, String zip, String countryCode, CityDataCacher cityCache) throws ProcessingException {
    CityCacheKey cityTerm = new CityCacheKey(cityStr, zip, countryCode);
    CityFormData city = cityCache.get(cityTerm);
    if (city.getCityNr() == null) {
      city = cityCache.put(cityTerm, city);
    }
    return city;
  }

  public static CityFormData importCityWithRegion(CityRegionCacheKey cityTerm, String areaStr, CityRegionDataCacher cityCache, AreaDataCacher areaCache) throws ProcessingException {
    CityFormData city = cityCache.get(cityTerm);

    // Area
    if (!StringUtility.isNullOrEmpty(areaStr)) {
      CodeFormData code = areaCache.get(areaStr);
      if (code.getCodeUid() == null) {
        code = areaCache.put(areaStr, code);
      }

      // City might have been updated: Area
      boolean areaChanged = CompareUtility.notEquals(city.getArea().getValue(), code.getCodeUid()) && city.getCityNr() != null;
      if (areaChanged || city.getCityNr() == null) {
        city.getArea().setValue(code.getCodeUid());
        city = cityCache.put(cityTerm, city);
      }
    }
    return city;
  }

  public static void importStartFee(Double fee, EntryBean entry, DefaultAdditionalInformationStartFeeDataCacher startFeeCache) throws ProcessingException {
    AdditionalInformationValueBean value = new AdditionalInformationValueBean();
    entry.getAddInfo().addValue(value);
    value.setValueDouble(fee);
    value.setAdditionalInformationUid(startFeeCache.getDefault().getAdditionalInformationUid());
    value.setTypeUid(AdditionalInformationTypeCodeType.DoubleCode.ID);
  }

  public static Long importStartblock(String block, Long entryNr, Long eventNr, StartblockDataCacher startblockCache) throws ProcessingException {
    if (!StringUtility.isNullOrEmpty(block)) {
      CodeFormData start = startblockCache.get(block);
      if (start.getCodeUid() == null) {
        start = startblockCache.put(block, start);
      }

      EventStartblockFormData eventStartblock = new EventStartblockFormData();
      eventStartblock.setEventNr(eventNr);
      eventStartblock.getStartblockUid().setValue(start.getCodeUid());
      IEventStartblockProcessService eventStartblockService = BEANS.get(IEventStartblockProcessService.class);

      eventStartblock = eventStartblockService.load(eventStartblock);
      if (eventStartblock.getSortCode().getValue() == null) {
        eventStartblock = eventStartblockService.prepareCreate(eventStartblock);
        eventStartblock = eventStartblockService.create(eventStartblock);
      }

      return start.getCodeUid();
    }
    return null;
  }

  public static void importSwissOrienteeringAntiDopingSigned(boolean signed, AdditionalInformationBean addInfo) {
    long antiDopingSigned = signed ? 1L : 0L;

    AdditionalInformationValueBean value = new AdditionalInformationValueBean();
    value.setAdditionalInformationUid(AdditionalInformationCodeType.SwissOrienteeringAntiDopingCode.ID);
    value.setTypeUid(AdditionalInformationTypeCodeType.BooleanCode.ID);
    value.setValueInteger(antiDopingSigned);
    addInfo.addValue(value);
  }

  public static void importStartTimeWish(Long wishUid, AdditionalInformationBean addInfo) {
    AdditionalInformationValueBean value = new AdditionalInformationValueBean();
    value.setAdditionalInformationUid(AdditionalInformationCodeType.StartTimeWishCode.ID);
    value.setTypeUid(AdditionalInformationTypeCodeType.SmartfieldCode.ID);
    value.setValueInteger(wishUid);
    addInfo.addValue(value);
  }

  public static Long importNation(String nationStr, NationDataCacher nationCache) throws ProcessingException {
    Long countryUid = null;
    if (!StringUtility.isNullOrEmpty(nationStr)) {
      CountryFormData nation = nationCache.get(nationStr);
      if (nation.getCountryUid() == null) {
        nationCache.put(nationStr, nation);
      }
      countryUid = nationCache.get(nationStr).getCountryUid();
    }
    return countryUid;
  }

  public static void importPayment(Double amount, Long registrationNr, Long currencyUid) throws ProcessingException {
    PaymentBean payment = new PaymentBean();
    payment = BEANS.get(IPaymentProcessService.class).prepareCreate(payment);
    payment.setAmount(amount);
    payment.setRegistrationNr(registrationNr);
    payment.setTypeUid(PaymentTypeCodeType.CashPaymentCode.ID);
    payment.setCurrencyUid(currencyUid);
    BEANS.get(IPaymentProcessService.class).create(payment);
  }

  public static String importStreet(String line1, String line2) {
    StringBuilder address = new StringBuilder();
    if (!StringUtility.isNullOrEmpty(line1)) {
      address.append(line1);
    }
    if (!StringUtility.isNullOrEmpty(line2)) {
      address.append(FMilaUtility.LINE_SEPARATOR);
      address.append(line2);
    }
    return address.toString();
  }

}
