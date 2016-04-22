package com.rtiming.client.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.ProcessingStatus;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.TriState;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.ClientSession;
import com.rtiming.client.entry.EntryForm.MainBox.ClazzField;
import com.rtiming.client.entry.EntryForm.MainBox.SexField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.AdditionalInformationEntryBox.AdditionalInformationEntryField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.EventsBox.EventsField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.FeesBox;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.FeesBox.CurrencyUidField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.FeesBox.EvtEntryField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.FeesBox.FeesField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField;
import com.rtiming.client.entry.EntryForm.MainBox.YearField;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationField;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.entry.AgeUtility;
import com.rtiming.shared.entry.EntryFormData;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.entry.startlist.StartlistSettingRowData;
import com.rtiming.shared.entry.startlist.StartlistTypeCodeType;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationFieldData;
import com.rtiming.shared.settings.clazz.ClazzLookupCall;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.fee.FeeCalculator;
import com.rtiming.shared.settings.fee.FeeCalculator.AddInfoInput;
import com.rtiming.shared.settings.fee.FeeCalculator.FeeOutput;
import com.rtiming.shared.settings.fee.FeeCalculator.FeeResult;
import com.rtiming.shared.settings.fee.FeeCalculator.RaceInput;
import com.rtiming.shared.settings.fee.FeeFormData;

public final class EntryFormUtility {

  private EntryFormUtility() {
  }

  public static Long getDefaultParticipationClassUid(EventsField field) {
    Long defaultClassUid = null;
    if (field.getTable().getRowCount() > 0) {
      defaultClassUid = field.getTable().getEventClassColumn().getValue(0);
    }
    return defaultClassUid;
  }

  public static Long getDefaultParticipationEventNr(EventsField field) {
    Long defaultEventNr = null;
    if (field.getTable().getRowCount() > 0) {
      defaultEventNr = field.getTable().getEventNrColumn().getValue(0);
    }
    return defaultEventNr;
  }

  public static void validateRacesTable(RacesField.Table raceTable, List<Long> availableEvents, EventConfiguration configuration) throws ProcessingException {
    if (raceTable == null || availableEvents == null || configuration == null) {
      throw new IllegalArgumentException("Parameters should not be null");
    }
    for (int k = 0; k < raceTable.getRowCount(); k++) {
      Long eventNr = raceTable.getRaceEventColumn().getValue(k);
      if (eventNr != null) {
        // check if event is available in event tab
        if (!availableEvents.contains(eventNr)) {
          raceTable.getRaceEventColumn().setValue(k, null);
          throw new VetoException(TEXTS.get("EntryEventOnEventRequiredMessage"));
        }

        // set classUid = null when not in new event
        Long classUid = raceTable.getLegColumn().getValue(k);
        EventClassFormData eventClass = configuration.getEventClassInfo(eventNr, classUid);
        if (eventClass == null) {
          // the class does not match the event, therefore remove the class
          raceTable.getLegColumn().setValue(k, null);
        }
      }
    }
  }

  public static void validateTeamSize(Long eventNr, Long classUid, long count, EventConfiguration configuration) throws ProcessingException {
    if (configuration == null) {
      throw new IllegalArgumentException("Parameters should not be null");
    }
    EventClassFormData formData = configuration.getEventClassInfo(eventNr, classUid);
    if (formData == null) {
      throw new ProcessingException("Class configuration not found: eventNr=" + eventNr + ", classUid=" + classUid);
    }

    Long teamMin = formData.getTeamSizeMin().getValue();
    Long teamMax = formData.getTeamSizeMax().getValue();
    Long typeUid = formData.getType().getValue();
    if (teamMin == null || teamMax == null || typeUid == null) {
      throw new ProcessingException("No team configuration available");
    }

    if (ClassTypeCodeType.isIndividualClassType(typeUid) || ClassTypeCodeType.isTeamClassType(typeUid)) {
      if (teamMin == 1 && teamMax == 1 && count != 1) {
        String classText = FMilaUtility.getCodeText(ClassCodeType.class, classUid);
        throw new VetoException(TEXTS.get("EntryTeamSizeIndividualClassMessage", classText));
      }
      else if (count < teamMin || count > teamMax) {
        String classText = FMilaUtility.getCodeText(ClassCodeType.class, classUid);
        throw new VetoException(TEXTS.get("EntryTeamSizeValidationMessage", classText, StringUtility.emptyIfNull(teamMin), StringUtility.emptyIfNull(teamMax)));
      }
    }
  }

  /**
   * @param cachedClassTypeUid
   *          in case the classTypeUid is already loaded, pass it to the method, otherwise it will be reloaded
   * @throws ProcessingException
   */
  public static void validateClassField(EventConfiguration configuration, List<RtClassAge> ageSettings, YearField yearField, SexField sexField, ClazzField clazzField, EvtEntryField evtEntryField) throws ProcessingException {
    // clear errors
    yearField.clearErrorStatus();
    sexField.clearErrorStatus();
    clazzField.clearErrorStatus();
    evtEntryField.clearErrorStatus();

    // check for lowest level
    Long eventNr = ((ClazzLookupCall) clazzField.getLookupCall()).getEventNr();
    Long classUid = clazzField.getValue();
    if (classUid != null) {
      EventClassFormData cachedClassTypeUid = configuration.getEventClassInfo(eventNr, classUid);
      if (cachedClassTypeUid == null || cachedClassTypeUid.getType().getValue() == null) {
        clazzField.setErrorStatus(Texts.get("EntryClassNotAvailable"));
        classUid = null;
      }
      else if (!ClassTypeCodeType.isLegClassType(cachedClassTypeUid.getType().getValue())) {
        clazzField.setErrorStatus(Texts.get("EntryClassLowestLevelMessage"));
        classUid = null;
      }
    }
    if (clazzField.getErrorStatus() != null) {
      return;
    }

    // check age
    if (eventNr == null) {
      return;
    }

    if (yearField.getValue() == null) {
      yearField.setErrorStatus(new ProcessingStatus(Texts.get("EntryClassAgeSexCheckNotPossible"), ProcessingStatus.WARNING));
      evtEntryField.setErrorStatus(new ProcessingStatus(Texts.get("EntryFeeCalculationNotPossible"), ProcessingStatus.WARNING));
    }
    if (sexField.getValue() == null) {
      sexField.setErrorStatus(new ProcessingStatus(Texts.get("EntryClassAgeSexCheckNotPossible"), ProcessingStatus.WARNING));
    }
    if (clazzField.getValue() == null) {
      // nop
    }
    else if (yearField.getValue() != null || sexField.getValue() != null) {
      EventBean event = configuration.getEvent(eventNr);
      TriState isValid = AgeUtility.isRunnerValidForClassAge(event.getEvtZero(), clazzField.getValue(), ClientSession.get().getSessionClientNr(), sexField.getValue(), yearField.getValue(), ageSettings);
      if (isValid.equals(TriState.FALSE)) {
        clazzField.setErrorStatus(Texts.get("EntryClassNotAllowed"));
      }
      else if (isValid.equals(TriState.UNDEFINED)) {
        clazzField.setErrorStatus(new ProcessingStatus(Texts.get("EntryClassAgeSexCheckNotPossibleDefinitionMissing"), ProcessingStatus.WARNING));
      }
      else {
        // nop, class is ok
      }
    }
  }

  /**
   * Calculates Fees for Entry.
   * fee calculation logic in {@link FeeCalculator}
   * 
   * @throws ProcessingException
   */
  public static void validateFeesTable(List<FeeFormData> feeConfiguration, FeesField feesField, RacesField racesField, EvtEntryField evtEntryField, CurrencyUidField currencyField, AdditionalInformationEntryField addInfoEntryField, EventConfiguration configuration) throws ProcessingException {

    FeeCalculator calculator = new FeeCalculator(feeConfiguration, configuration);

    // entry date
    if (evtEntryField.getValue() == null) {
      evtEntryField.setValue(new Date());
    }
    Date evtEntry = evtEntryField.getValue();

    // delete existing data
    feesField.getTable().deleteAllRows();
    feesField.getTable().discardAllDeletedRows();

    Collection<RaceInput> races = new ArrayList<>();
    List<AddInfoInput> addInfos = new ArrayList<>();

    // runners
    for (int k = 0; k < racesField.getTable().getRowCount(); k++) {
      RunnerBean runner = racesField.getTable().getRaceBeanColumn().getValue(k).getRunner();
      Long classUid = racesField.getTable().getLegColumn().getValue(k);
      Long eventNr = racesField.getTable().getRaceEventColumn().getValue(k);

      RaceInput race = calculator.new RaceInput(racesField.getTable().getRaceNrColumn().getValue(k), classUid, eventNr, runner.getYear());
      races.add(race);
    }

    // additional informations
    com.rtiming.client.settings.addinfo.AbstractAdditionalInformationField.Table table = addInfoEntryField.getTable();
    for (int k = 0; k < table.getRowCount(); k++) {
      Long additionalInformationUid = table.getAdditionalInformationUidColumn().getValue(k);

      AddInfoInput addInfoInput = calculator.new AddInfoInput();
      addInfoInput.setAdditionalInformationUid(additionalInformationUid);
      addInfoInput.setDecimal(table.getDecimalColumn().getValue(k).doubleValue()); // TODO MIG
      addInfoInput.setInteger(table.getIntegerColumn().getValue(k).longValue()); // TODO MIG
      addInfoInput.setText(table.getTextColumn().getValue(k));
      addInfoInput.setTypeUid(table.getTypeColumn().getValue(k));
      addInfos.add(addInfoInput);
    }

    FeeResult result = calculator.calculateFees(races, addInfos, evtEntry, currencyField.getValue());
    for (FeeOutput feeOutput : result.getFees()) {
      ITableRow row = feesField.getTable().createRow();
      feesField.getTable().getCurrencyColumn().setValue(row, feeOutput.getCurrencyUid());
      feesField.getTable().getAmountColumn().setValue(row, feeOutput.getAmount());
      feesField.getTable().getCashPaymentOnRegistrationColumn().setValue(row, feeOutput.getCashPaymentOnRegistration());
      feesField.getTable().getNameColumn().setValue(row, feeOutput.getText());
      feesField.getTable().addRow(row);
    }
    double sum = result.getSum();

    // update title
    if (currencyField.getValue() != null) {
      FeesBox parent = (FeesBox) feesField.getParentGroupBox();
      String value = String.format("%1$.2f", sum); // format with 2 digits (all currencies are assumed to have 2 digits)
      parent.setLabel(parent.getConfiguredLabel() + ": " + value + " " + FMilaUtility.getCodeExtKey(CurrencyCodeType.class, currencyField.getValue()));
    }
  }

  public static AbstractAdditionalInformationFieldData validateAdditionalInformationEntry(AbstractAdditionalInformationFieldData formData, AdditionalInformationEntryField addInfoField, EventsField eventsField, Map<Long, long[]> config) throws ProcessingException {
    // update additional information
    if (addInfoField.getTable().getRowCount() > 0 || formData != null) {

      // Initially Load Form Data (all AIUids)
      if (formData == null) {
        formData = new EntryFormData().getAdditionalInformationEntry();
        addInfoField.exportFormFieldData(formData);
      }

      // Save Dialog Values to Form Data
      AbstractAdditionalInformationField.Table infoTable = addInfoField.getTable();
      for (int k = 0; k < infoTable.getRowCount(); k++) {
        long infoUid = infoTable.getAdditionalInformationUidColumn().getValue(k);
        for (int j = 0; j < formData.getRowCount(); j++) {
          if (formData.getRows()[j].getAdditionalInformationUid() == infoUid) {
            formData.getRows()[j].setInteger(infoTable.getIntegerColumn().getValue(k));
            formData.getRows()[j].setDecimal(infoTable.getDecimalColumn().getValue(k));
            formData.getRows()[j].setText(infoTable.getTextColumn().getValue(k));
          }
        }
      }

      // Delete and Clear Table on Dialog
      infoTable.deleteAllRows();
      infoTable.discardAllDeletedRows();

      // Import Form Data to Dialog
      addInfoField.importFormFieldData(formData, true);

      // Get List of Registered Events
      ArrayList<Long> registeredEvents = new ArrayList<Long>();
      for (int k = 0; k < eventsField.getTable().getRowCount(); k++) {
        if (eventsField.getTable().getEventNrColumn().getValue(k) != null && eventsField.getTable().getRow(k).getStatus() != ITableRow.STATUS_DELETED) {
          registeredEvents.add(eventsField.getTable().getEventNrColumn().getValue(k));
        }
      }

      // Remove Rows on Dialog for Non-selected Events
      ArrayList<Integer> rowsToKeep = new ArrayList<Integer>();
      for (int k = 0; k < infoTable.getRowCount(); k++) {
        long[] eventNrs = config.get(infoTable.getAdditionalInformationUidColumn().getValue(k));
        if (eventNrs != null) {
          boolean isAvailableOnEvent = false;
          for (Long event : registeredEvents) {
            Arrays.sort(eventNrs);
            if (Arrays.binarySearch(eventNrs, event) >= 0) {
              isAvailableOnEvent = true;
            }
          }
          if (isAvailableOnEvent) {
            rowsToKeep.add(k);
          }
        }
      }

      // Finally Delete Rows on Dialog
      ArrayList<ITableRow> rowsToDelete = new ArrayList<ITableRow>();
      for (int k = 0; k < infoTable.getRowCount(); k++) {
        if (!rowsToKeep.contains(k)) {
          rowsToDelete.add(infoTable.getRow(k));
        }
      }
      infoTable.deleteRows(rowsToDelete);
      infoTable.discardAllDeletedRows();
    }

    return formData;
  }

  public static void checkClassesNotEmpty(RacesField.Table racesTable) throws ProcessingException {
    if (racesTable == null) {
      throw new ProcessingException("Must provide RacesTable");
    }
    List<Long> classes = racesTable.getLegColumn().getValues();
    for (Long c : classes) {
      if (c == null) {
        throw new VetoException(Texts.get("ClassValidationErrorMessage"));
      }
    }
  }

  public static void updateRaceStartTimes(RacesField.Table racesTable, EventsField.Table eventsTable, EventConfiguration configuration) throws ProcessingException {
    Map<Long, Date> eventStartTimes = new HashMap<>();

    // fetch start times
    for (int k = 0; k < eventsTable.getRowCount(); k++) {
      Date startTime = eventsTable.getStartTimeColumn().getValue(k);
      Long eventNr = eventsTable.getEventNrColumn().getValue(k);
      eventStartTimes.put(eventNr, startTime);
    }
    // apply start times
    for (int k = 0; k < racesTable.getRowCount(); k++) {
      Long classUid = eventsTable.getEventClassColumn().getValue(k);
      Long eventNr = racesTable.getRaceEventColumn().getValue(k);
      EventClassFormData eventClassFormData = configuration.getEventClassInfo(eventNr, classUid);
      if (ClassTypeCodeType.isOneRaceType(eventClassFormData.getType().getValue())) {
        Date startTime = eventStartTimes.get(eventNr);
        // update race
        racesTable.getLegStartTimeColumn().setValue(k, startTime);
      }
    }
  }

  public static void updateParticipationStartTimes(RacesField.Table racesTable, EventsField.Table eventsTable, EventConfiguration configuration) throws ProcessingException {
    Map<Long, Date> eventStartTimes = new HashMap<>();
    // fetch start times
    for (int k = 0; k < racesTable.getRowCount(); k++) {
      Date startTime = racesTable.getLegStartTimeColumn().getValue(k);
      Long eventNr = racesTable.getRaceEventColumn().getValue(k);
      eventStartTimes.put(eventNr, startTime);
    }
    // apply start times
    for (int k = 0; k < eventsTable.getRowCount(); k++) {
      Long classUid = eventsTable.getEventClassColumn().getValue(k);
      Long eventNr = eventsTable.getEventNrColumn().getValue(k);
      EventClassFormData eventClassFormData = configuration.getEventClassInfo(eventNr, classUid);
      if (ClassTypeCodeType.isOneRaceType(eventClassFormData.getType().getValue())) {
        Date startTime = eventStartTimes.get(eventNr);
        // update participations
        eventsTable.getStartTimeColumn().setValue(k, startTime);
      }
    }
  }

  public static void updateMassStartTime(EventsField.Table eventsTable, RacesField.Table racesTable, EventConfiguration configuration) throws ProcessingException {
    for (int k = 0; k < eventsTable.getRowCount(); k++) {
      Long stEventClassUid = eventsTable.getEventClassColumn().getValue(k);
      Long stEventNr = eventsTable.getEventNrColumn().getValue(k);
      List<StartlistSettingRowData> configList = SharedCache.getStartlistConfiguration();
      for (StartlistSettingRowData config : configList) {
        if (CompareUtility.equals(config.getEventNr(), stEventNr) && CompareUtility.equals(config.getClazzUid(), stEventClassUid)) {
          if (CompareUtility.equals(config.getTypeUid(), StartlistTypeCodeType.MassStartCode.ID)) {
            eventsTable.getStartTimeColumn().setValue(k, config.getFirstStart());
            updateRaceStartTimes(racesTable, eventsTable, configuration);
          }
        }
      }
    }
  }

}
