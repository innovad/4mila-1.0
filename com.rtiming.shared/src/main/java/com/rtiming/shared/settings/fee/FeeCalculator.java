package com.rtiming.shared.settings.fee;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;

public class FeeCalculator {

  private final List<FeeFormData> feeConfiguration;
  private final EventConfiguration eventConfiguration;

  public FeeCalculator(List<FeeFormData> feeConfiguration, EventConfiguration eventConfiguration) {
    super();
    if (feeConfiguration == null || eventConfiguration == null) {
      throw new IllegalArgumentException("arguments mandatory");
    }
    this.feeConfiguration = feeConfiguration;
    this.eventConfiguration = eventConfiguration;
  }

  // calculates the fee for one entry? registration?
  public FeeResult calculateFees(Collection<RaceInput> races, List<AddInfoInput> addInfos, Date evtEntry, Long currencyUid) throws ProcessingException {
    FeeResult result = new FeeResult();
    if (races == null) {
      races = new ArrayList<>();
    }
    if (addInfos == null) {
      addInfos = new ArrayList<>();
    }

    // runners
    for (RaceInput race : races) {
      Long runnerYear = race.getRunnerYear();
      Long classUid = race.getLegClassUid();
      Long eventNr = race.getEventNr();

      if (eventNr != null) {

        // evt zero
        if (eventConfiguration.getEvent(eventNr) == null) {
          throw new IllegalArgumentException("Fee Calculation: Event Configuration is missing for event " + eventNr);
        }
        Date evtZero = eventConfiguration.getEvent(eventNr).getEvtZero();
        if (evtZero == null) {
          throw new IllegalArgumentException("Fee Calculation: Event Zero Time not available for event " + eventNr);
        }
        GregorianCalendar greg = new GregorianCalendar();
        greg.setTime(evtZero);
        int yearOfEvent = greg.get(Calendar.YEAR);

        // calculate fee for runners
        for (FeeFormData fee : feeConfiguration) {
          if (CompareUtility.equals(fee.getEventNr(), eventNr) &&
              CompareUtility.equals(fee.getClassUid(), classUid)) {
            Long year = NumberUtility.nvl(runnerYear, yearOfEvent + 1);
            if (CompareUtility.equals(currencyUid, fee.getCurrency().getValue())) {
              Long runnerAge = yearOfEvent - year;
              if (runnerAge >= NumberUtility.nvl(fee.getAgeFrom().getValue(), Long.MIN_VALUE) &&
                  runnerAge <= NumberUtility.nvl(fee.getAgeTo().getValue(), Long.MAX_VALUE) &&
                  (fee.getDateFrom().getValue() == null || evtEntry.compareTo(fee.getDateFrom().getValue()) >= 0) &&
                  (fee.getDateTo().getValue() == null || evtEntry.compareTo(fee.getDateTo().getValue()) <= 0)) {
                FeeOutput feeOutput = new FeeOutput();
                feeOutput.setAmount(fee.getFee().getValue());
                feeOutput.setCurrencyUid(fee.getCurrency().getValue());

                String displayText = Texts.get("Entry") + " " + FMilaUtility.getCodeText(ClassCodeType.class, classUid) + ", " + Texts.get("Age") + " " + runnerAge;
                feeOutput.setText(displayText);
                feeOutput.setCashPaymentOnRegistration(fee.isCashPaymentOnRegistration());
                result.getFees().add(feeOutput);

                result.setSum(result.getSum() + fee.getFee().getValue());
              }
            }
            else {
              // nop - year not yet defined
            }
          }
        }
      }
    }

    // additional informations
    for (AddInfoInput addInfo : addInfos) {
      Long additionalInformationUid = addInfo.getAdditionalInformationUid();

      // calculate fee for infos
      Long typeUid = addInfo.getTypeUid();
      if (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.SmartfieldCode.ID)) {
        additionalInformationUid = addInfo.getInteger();
      }
      if ((CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.BooleanCode.ID) && NumberUtility.nvl(addInfo.getInteger(), 0) == 1) ||
          (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.IntegerCode.ID) && NumberUtility.nvl(addInfo.getInteger(), 0) != 0) ||
          (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.DoubleCode.ID) && NumberUtility.nvl(addInfo.getDecimal(), 0D) != 0) ||
          (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.TextCode.ID) && !StringUtility.isNullOrEmpty(addInfo.getText())) ||
          (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.SmartfieldCode.ID) && addInfo.getInteger() != null)) {
        for (FeeFormData fee : feeConfiguration) {
          if (CompareUtility.equals(currencyUid, fee.getCurrency().getValue()) &&
              CompareUtility.equals(fee.getAdditionalInformationUid(), additionalInformationUid)) {
            if ((fee.getDateFrom().getValue() == null || evtEntry.compareTo(fee.getDateFrom().getValue()) >= 0) &&
                (fee.getDateTo().getValue() == null || evtEntry.compareTo(fee.getDateTo().getValue()) <= 0)) {

              Double factor = 1d;
              if (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.DoubleCode.ID)) {
                factor = addInfo.getDecimal();
              }
              else if (CompareUtility.equals(typeUid, AdditionalInformationTypeCodeType.IntegerCode.ID)) {
                factor = addInfo.getInteger().doubleValue();
              }

              FeeOutput feeOutput = new FeeOutput();
              feeOutput.setAmount(fee.getFee().getValue() * factor);
              feeOutput.setCurrencyUid(fee.getCurrency().getValue());

              String displayText = FMilaUtility.getCodeText(AdditionalInformationCodeType.class, additionalInformationUid);
              feeOutput.setText(displayText);
              feeOutput.setCashPaymentOnRegistration(fee.isCashPaymentOnRegistration());
              result.getFees().add(feeOutput);

              result.setSum(result.getSum() + (fee.getFee().getValue() * factor));
            }
          }
        }
      }
    }

    return result;
  }

  public class RaceInput {

    private final Long raceNr;
    private final Long legClassUid;
    private final Long eventNr;
    private final Long runnerYear;

    public RaceInput(Long raceNr, Long legClassUid, Long eventNr, Long runnerYear) {
      super();
      this.raceNr = raceNr;
      this.legClassUid = legClassUid;
      this.eventNr = eventNr;
      this.runnerYear = runnerYear;
    }

    public Long getRaceNr() {
      return raceNr;
    }

    public Long getLegClassUid() {
      return legClassUid;
    }

    public Long getEventNr() {
      return eventNr;
    }

    public Long getRunnerYear() {
      return runnerYear;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((raceNr == null) ? 0 : raceNr.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof RaceInput)) {
        return false;
      }
      RaceInput other = (RaceInput) obj;
      if (!getOuterType().equals(other.getOuterType())) {
        return false;
      }
      if (raceNr == null) {
        if (other.raceNr != null) {
          return false;
        }
      }
      else if (!raceNr.equals(other.raceNr)) {
        return false;
      }
      return true;
    }

    private FeeCalculator getOuterType() {
      return FeeCalculator.this;
    }

  }

  public class AddInfoInput {

    private Long additionalInformationUid;
    private Long typeUid;
    private Long integer;
    private Double decimal;
    private String text;

    public Long getAdditionalInformationUid() {
      return additionalInformationUid;
    }

    public Long getTypeUid() {
      return typeUid;
    }

    public String getText() {
      return text;
    }

    public Double getDecimal() {
      return decimal;
    }

    public Long getInteger() {
      return integer;
    }

    public void setAdditionalInformationUid(Long additionalInformationUid) {
      this.additionalInformationUid = additionalInformationUid;
    }

    public void setTypeUid(Long typeUid) {
      this.typeUid = typeUid;
    }

    public void setInteger(Long integer) {
      this.integer = integer;
    }

    public void setDecimal(Double decimal) {
      this.decimal = decimal;
    }

    public void setText(String text) {
      this.text = text;
    }

  }

  public class FeeResult {

    private double sum = 0d;
    private final List<FeeOutput> fees;

    public FeeResult() {
      fees = new ArrayList<>();
    }

    public List<FeeOutput> getFees() {
      return fees;
    }

    public void setSum(double sum) {
      this.sum = sum;
    }

    public double getSum() {
      return sum;
    }

  }

  public class FeeOutput {

    private Double amount;
    private Long currencyUid;
    private String text;
    private boolean cashPaymentOnRegistration;

    public Double getAmount() {
      return amount;
    }

    public void setAmount(Double amount) {
      this.amount = amount;
    }

    public Long getCurrencyUid() {
      return currencyUid;
    }

    public void setCurrencyUid(Long currencyUid) {
      this.currencyUid = currencyUid;
    }

    public String getText() {
      return text;
    }

    public void setText(String text) {
      this.text = text;
    }

    public boolean getCashPaymentOnRegistration() {
      return cashPaymentOnRegistration;
    }

    public void setCashPaymentOnRegistration(boolean cashPaymentOnRegistration) {
      this.cashPaymentOnRegistration = cashPaymentOnRegistration;
    }

  }

}
