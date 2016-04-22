package com.rtiming.shared;

import java.util.Locale;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType.IndividualStartFeeCode;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType.StartTimeWishCode;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType.SwissOrienteeringAntiDopingCode;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.addinfo.EventAdditionalInformationFormData;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;
import com.rtiming.shared.settings.addinfo.IEventAdditionalInformationProcessService;
import com.rtiming.shared.settings.fee.FeeFormData;
import com.rtiming.shared.settings.fee.FeeGroupFormData;
import com.rtiming.shared.settings.fee.IFeeGroupProcessService;
import com.rtiming.shared.settings.fee.IFeeProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;

public final class AdditionalInformationUtility {

  private AdditionalInformationUtility() {
  }

  public static AdditionalInformationAdministrationFormData create(Long addInfoUid, Long parentUid, Long typeUid, Long entityUid, String textKey, String shortcut, Long feeGroupNr) throws ProcessingException {
    AdditionalInformationAdministrationFormData ai = new AdditionalInformationAdministrationFormData();
    ai.setAdditionalInformationUid(addInfoUid);
    ai = BEANS.get(IAdditionalInformationAdministrationProcessService.class).load(ai);

    if (ai.getEntity().getValue() == null) {
      ai = new AdditionalInformationAdministrationFormData();
      ai = BEANS.get(IAdditionalInformationAdministrationProcessService.class).prepareCreate(ai);
      for (int i = 0; i < ai.getCodeBox().getLanguage().getRowCount(); i++) {
        String language = BEANS.get(LanguageCodeType.class).getCode(ai.getCodeBox().getLanguage().rowAt(i).getLanguage()).getExtKey();
        Locale locale = new Locale(language);
        ai.getCodeBox().getLanguage().rowAt(i).setTranslation(TEXTS.get(locale, textKey));
      }
      ai.getEntity().setValue(entityUid);
      ai.getSmartfield().setValue(parentUid);
      ai.getType().setValue(typeUid);
      ai.getFeeGroup().setValue(feeGroupNr);
      ai.setAdditionalInformationUid(addInfoUid);
      ai.getCodeBox().getCodeTypeUid().setValue(AdditionalInformationCodeType.ID);
      ai.getCodeBox().getCodeUid().setValue(addInfoUid);
      ai.getCodeBox().getShortcut().setValue(shortcut);
      ai = BEANS.get(IAdditionalInformationAdministrationProcessService.class).create(ai);
    }

    return ai;
  }

  public static void createSwissOrienteeringAntiDoping() throws ProcessingException {
    create(AdditionalInformationCodeType.SwissOrienteeringAntiDopingCode.ID, null, AdditionalInformationTypeCodeType.BooleanCode.ID, EntityCodeType.RunnerCode.ID, "SwissOrienteeringAntiDoping", new SwissOrienteeringAntiDopingCode().getExtKey(), null);
  }

  public static void createStartTimeWish(Long eventNr) throws ProcessingException {
    create(AdditionalInformationCodeType.StartTimeWishCode.ID, null, AdditionalInformationTypeCodeType.SmartfieldCode.ID, EntityCodeType.EntryCode.ID, "StartTimeWish", new StartTimeWishCode().getExtKey(), null);
    create(AdditionalInformationCodeType.StartTimeWishEarlyStartCode.ID, AdditionalInformationCodeType.StartTimeWishCode.ID, AdditionalInformationTypeCodeType.SmartfieldCode.ID, EntityCodeType.EntryCode.ID, "EarlyStart", null, null);
    create(AdditionalInformationCodeType.StartTimeWishLateStartCode.ID, AdditionalInformationCodeType.StartTimeWishCode.ID, AdditionalInformationTypeCodeType.SmartfieldCode.ID, EntityCodeType.EntryCode.ID, "LateStart", null, null);

    if (eventNr != null) {
      EventAdditionalInformationFormData eventAddInfo = new EventAdditionalInformationFormData();
      eventAddInfo.getEvent().setValue(eventNr);
      eventAddInfo.getAdditionalInformation().setValue(AdditionalInformationCodeType.StartTimeWishCode.ID);
      BEANS.get(IEventAdditionalInformationProcessService.class).create(eventAddInfo);
    }
  }

  public static AdditionalInformationAdministrationFormData createIndividualStartFee(Long eventNr) throws ProcessingException {
    // Check if Indivudual Fee additional info exists
    AdditionalInformationAdministrationFormData ai = new AdditionalInformationAdministrationFormData();
    ai.setAdditionalInformationUid(AdditionalInformationCodeType.IndividualStartFeeCode.ID);
    ai = BEANS.get(IAdditionalInformationAdministrationProcessService.class).load(ai);

    if (ai.getEntity().getValue() == null) {

      // Fee Group
      FeeGroupFormData fg = new FeeGroupFormData();
      fg.getName().setValue(Texts.get("IndividualStartFee"));
      fg.getCashPaymentOnRegistration().setValue(false);
      fg = BEANS.get(IFeeGroupProcessService.class).create(fg);

      // Additional Information
      ai = create(AdditionalInformationCodeType.IndividualStartFeeCode.ID, null, AdditionalInformationTypeCodeType.DoubleCode.ID, EntityCodeType.EntryCode.ID, "IndividualStartFee", new IndividualStartFeeCode().getExtKey(), fg.getFeeGroupNr());
    }

    // Fee
    Long[] missingCurrencies = BEANS.get(IFeeGroupProcessService.class).getMissingCurrencies(ai.getFeeGroup().getValue());
    Long feeGroupNr = ai.getFeeGroup().getValue();
    for (Long currencyUid : missingCurrencies) {
      addIndividualStartFeeForCurrency(feeGroupNr, currencyUid);
    }

    // Attach Additional Information to Event
    EventAdditionalInformationFormData eai = new EventAdditionalInformationFormData();
    eai.getEvent().setValue(eventNr);
    eai.getAdditionalInformation().setValue(ai.getAdditionalInformationUid());
    BEANS.get(IEventAdditionalInformationProcessService.class).create(eai);

    return ai;
  }

  private static void addIndividualStartFeeForCurrency(Long feeGroupNr, Long currencyUid) throws ProcessingException {
    FeeFormData fee = new FeeFormData();
    fee.setFeeGroupNr(feeGroupNr);
    fee.getFee().setValue(1d);
    fee.getCurrency().setValue(currencyUid);
    fee = BEANS.get(IFeeProcessService.class).create(fee);
  }

}
