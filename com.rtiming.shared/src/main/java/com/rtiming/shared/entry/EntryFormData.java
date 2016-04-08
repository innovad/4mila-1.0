package com.rtiming.shared.entry;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.tablefield.AbstractTableFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.common.database.sql.ParticipationBean;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationFieldData;

public class EntryFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public EntryFormData() {
  }

  public DownloadStationEntryProperty getDownloadStationEntryProperty() {
    return getPropertyByClass(DownloadStationEntryProperty.class);
  }

  /**
   * access method for property DownloadStationEntry.
   */
  public boolean isDownloadStationEntry() {
    return (getDownloadStationEntryProperty().getValue() == null) ? (false) : (getDownloadStationEntryProperty().getValue());
  }

  /**
   * access method for property DownloadStationEntry.
   */
  public void setDownloadStationEntry(boolean downloadStationEntry) {
    getDownloadStationEntryProperty().setValue(downloadStationEntry);
  }

  public EntryNrProperty getEntryNrProperty() {
    return getPropertyByClass(EntryNrProperty.class);
  }

  /**
   * access method for property EntryNr.
   */
  public Long getEntryNr() {
    return getEntryNrProperty().getValue();
  }

  /**
   * access method for property EntryNr.
   */
  public void setEntryNr(Long entryNr) {
    getEntryNrProperty().setValue(entryNr);
  }

  public AdditionalInformationEntry getAdditionalInformationEntry() {
    return getFieldByClass(AdditionalInformationEntry.class);
  }

  public AdditionalInformationRunner getAdditionalInformationRunner() {
    return getFieldByClass(AdditionalInformationRunner.class);
  }

  public City getCity() {
    return getFieldByClass(City.class);
  }

  public Clazz getClazz() {
    return getFieldByClass(Clazz.class);
  }

  public Club getClub() {
    return getFieldByClass(Club.class);
  }

  public CurrencyUid getCurrencyUid() {
    return getFieldByClass(CurrencyUid.class);
  }

  public ECard getECard() {
    return getFieldByClass(ECard.class);
  }

  public Events getEvents() {
    return getFieldByClass(Events.class);
  }

  public EvtEntry getEvtEntry() {
    return getFieldByClass(EvtEntry.class);
  }

  public Fees getFees() {
    return getFieldByClass(Fees.class);
  }

  public FirstName getFirstName() {
    return getFieldByClass(FirstName.class);
  }

  public LastName getLastName() {
    return getFieldByClass(LastName.class);
  }

  public Races getRaces() {
    return getFieldByClass(Races.class);
  }

  public Registration getRegistration() {
    return getFieldByClass(Registration.class);
  }

  public Runner getRunner() {
    return getFieldByClass(Runner.class);
  }

  public Sex getSex() {
    return getFieldByClass(Sex.class);
  }

  public Year getYear() {
    return getFieldByClass(Year.class);
  }

  public class DownloadStationEntryProperty extends AbstractPropertyData<Boolean> {
    private static final long serialVersionUID = 1L;

    public DownloadStationEntryProperty() {
    }
  }

  public class EntryNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public EntryNrProperty() {
    }
  }

  public static class AdditionalInformationEntry extends AbstractAdditionalInformationFieldData {
    private static final long serialVersionUID = 1L;

    public AdditionalInformationEntry() {
    }
  }

  public static class AdditionalInformationRunner extends AbstractAdditionalInformationFieldData {
    private static final long serialVersionUID = 1L;

    public AdditionalInformationRunner() {
    }
  }

  public static class City extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public City() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Clazz extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Clazz() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class Club extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Club() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class CurrencyUid extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public CurrencyUid() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class ECard extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ECard() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Events extends AbstractTableFieldData {
    private static final long serialVersionUID = 1L;

    public Events() {
    }

    public static final int PARTICIPATION_BEAN_COLUMN_ID = 0;
    public static final int EVENT_NR_COLUMN_ID = 1;
    public static final int EVENT_CLASS_COLUMN_ID = 2;
    public static final int START_TIME_COLUMN_ID = 3;

    public void setParticipationBean(int row, ParticipationBean participationBean) {
      setValueInternal(row, PARTICIPATION_BEAN_COLUMN_ID, participationBean);
    }

    public ParticipationBean getParticipationBean(int row) {
      return (ParticipationBean) getValueInternal(row, PARTICIPATION_BEAN_COLUMN_ID);
    }

    public void setEventNr(int row, Long eventNr) {
      setValueInternal(row, EVENT_NR_COLUMN_ID, eventNr);
    }

    public Long getEventNr(int row) {
      return (Long) getValueInternal(row, EVENT_NR_COLUMN_ID);
    }

    public void setEventClass(int row, Long eventClass) {
      setValueInternal(row, EVENT_CLASS_COLUMN_ID, eventClass);
    }

    public Long getEventClass(int row) {
      return (Long) getValueInternal(row, EVENT_CLASS_COLUMN_ID);
    }

    public void setStartTime(int row, Date startTime) {
      setValueInternal(row, START_TIME_COLUMN_ID, startTime);
    }

    public Date getStartTime(int row) {
      return (Date) getValueInternal(row, START_TIME_COLUMN_ID);
    }

    @Override
    public int getColumnCount() {
      return 4;
    }

    @Override
    public Object getValueAt(int row, int column) {
      switch (column) {
        case PARTICIPATION_BEAN_COLUMN_ID:
          return getParticipationBean(row);
        case EVENT_NR_COLUMN_ID:
          return getEventNr(row);
        case EVENT_CLASS_COLUMN_ID:
          return getEventClass(row);
        case START_TIME_COLUMN_ID:
          return getStartTime(row);
        default:
          return null;
      }
    }

    @Override
    public void setValueAt(int row, int column, Object value) {
      switch (column) {
        case PARTICIPATION_BEAN_COLUMN_ID:
          setParticipationBean(row, (ParticipationBean) value);
          break;
        case EVENT_NR_COLUMN_ID:
          setEventNr(row, (Long) value);
          break;
        case EVENT_CLASS_COLUMN_ID:
          setEventClass(row, (Long) value);
          break;
        case START_TIME_COLUMN_ID:
          setStartTime(row, (Date) value);
          break;
      }
    }
  }

  public static class EvtEntry extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public EvtEntry() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Fees extends AbstractTableFieldData {
    private static final long serialVersionUID = 1L;

    public Fees() {
    }

    public static final int NAME_COLUMN_ID = 0;
    public static final int AMOUNT_COLUMN_ID = 1;
    public static final int CURRENCY_COLUMN_ID = 2;
    public static final int CASH_PAYMENT_ON_REGISTRATION_COLUMN_ID = 3;

    public void setName(int row, String name) {
      setValueInternal(row, NAME_COLUMN_ID, name);
    }

    public String getName(int row) {
      return (String) getValueInternal(row, NAME_COLUMN_ID);
    }

    public void setAmount(int row, Double amount) {
      setValueInternal(row, AMOUNT_COLUMN_ID, amount);
    }

    public Double getAmount(int row) {
      return (Double) getValueInternal(row, AMOUNT_COLUMN_ID);
    }

    public void setCurrency(int row, Long currency) {
      setValueInternal(row, CURRENCY_COLUMN_ID, currency);
    }

    public Long getCurrency(int row) {
      return (Long) getValueInternal(row, CURRENCY_COLUMN_ID);
    }

    public void setCashPaymentOnRegistration(int row, Boolean cashPaymentOnRegistration) {
      setValueInternal(row, CASH_PAYMENT_ON_REGISTRATION_COLUMN_ID, cashPaymentOnRegistration);
    }

    public Boolean getCashPaymentOnRegistration(int row) {
      return (Boolean) getValueInternal(row, CASH_PAYMENT_ON_REGISTRATION_COLUMN_ID);
    }

    @Override
    public int getColumnCount() {
      return 4;
    }

    @Override
    public Object getValueAt(int row, int column) {
      switch (column) {
        case NAME_COLUMN_ID:
          return getName(row);
        case AMOUNT_COLUMN_ID:
          return getAmount(row);
        case CURRENCY_COLUMN_ID:
          return getCurrency(row);
        case CASH_PAYMENT_ON_REGISTRATION_COLUMN_ID:
          return getCashPaymentOnRegistration(row);
        default:
          return null;
      }
    }

    @Override
    public void setValueAt(int row, int column, Object value) {
      switch (column) {
        case NAME_COLUMN_ID:
          setName(row, (String) value);
          break;
        case AMOUNT_COLUMN_ID:
          setAmount(row, (Double) value);
          break;
        case CURRENCY_COLUMN_ID:
          setCurrency(row, (Long) value);
          break;
        case CASH_PAYMENT_ON_REGISTRATION_COLUMN_ID:
          setCashPaymentOnRegistration(row, (Boolean) value);
          break;
      }
    }
  }

  public static class FirstName extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public FirstName() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class LastName extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public LastName() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Races extends AbstractTableFieldData {
    private static final long serialVersionUID = 1L;

    public Races() {
    }

    public static final int RACE_NR_COLUMN_ID = 0;
    public static final int RUNNER_NR_COLUMN_ID = 1;
    public static final int CLUB_NR_COLUMN_ID = 2;
    public static final int LAST_NAME_COLUMN_ID = 3;
    public static final int FIRST_NAME_COLUMN_ID = 4;
    public static final int E_CARD_COLUMN_ID = 5;
    public static final int BIB_NUMBER_COLUMN_ID = 6;
    public static final int LEG_START_TIME_COLUMN_ID = 7;
    public static final int LEG_COLUMN_ID = 8;
    public static final int NATION_COLUMN_ID = 9;
    public static final int RACE_EVENT_COLUMN_ID = 10;
    public static final int RACE_FORM_BUTTON_COLUMN_ID = 11;
    public static final int RACE_BEAN_COLUMN_ID = 12;

    public void setRaceNr(int row, Long raceNr) {
      setValueInternal(row, RACE_NR_COLUMN_ID, raceNr);
    }

    public Long getRaceNr(int row) {
      return (Long) getValueInternal(row, RACE_NR_COLUMN_ID);
    }

    public void setRunnerNr(int row, Long runnerNr) {
      setValueInternal(row, RUNNER_NR_COLUMN_ID, runnerNr);
    }

    public Long getRunnerNr(int row) {
      return (Long) getValueInternal(row, RUNNER_NR_COLUMN_ID);
    }

    public void setClubNr(int row, Long clubNr) {
      setValueInternal(row, CLUB_NR_COLUMN_ID, clubNr);
    }

    public Long getClubNr(int row) {
      return (Long) getValueInternal(row, CLUB_NR_COLUMN_ID);
    }

    public void setLastName(int row, String lastName) {
      setValueInternal(row, LAST_NAME_COLUMN_ID, lastName);
    }

    public String getLastName(int row) {
      return (String) getValueInternal(row, LAST_NAME_COLUMN_ID);
    }

    public void setFirstName(int row, String firstName) {
      setValueInternal(row, FIRST_NAME_COLUMN_ID, firstName);
    }

    public String getFirstName(int row) {
      return (String) getValueInternal(row, FIRST_NAME_COLUMN_ID);
    }

    public void setECard(int row, Long eCard) {
      setValueInternal(row, E_CARD_COLUMN_ID, eCard);
    }

    public Long getECard(int row) {
      return (Long) getValueInternal(row, E_CARD_COLUMN_ID);
    }

    public void setBibNumber(int row, String bibNumber) {
      setValueInternal(row, BIB_NUMBER_COLUMN_ID, bibNumber);
    }

    public String getBibNumber(int row) {
      return (String) getValueInternal(row, BIB_NUMBER_COLUMN_ID);
    }

    public void setLegStartTime(int row, Date legStartTime) {
      setValueInternal(row, LEG_START_TIME_COLUMN_ID, legStartTime);
    }

    public Date getLegStartTime(int row) {
      return (Date) getValueInternal(row, LEG_START_TIME_COLUMN_ID);
    }

    public void setLeg(int row, Long leg) {
      setValueInternal(row, LEG_COLUMN_ID, leg);
    }

    public Long getLeg(int row) {
      return (Long) getValueInternal(row, LEG_COLUMN_ID);
    }

    public void setNation(int row, Long nation) {
      setValueInternal(row, NATION_COLUMN_ID, nation);
    }

    public Long getNation(int row) {
      return (Long) getValueInternal(row, NATION_COLUMN_ID);
    }

    public void setRaceEvent(int row, Long raceEvent) {
      setValueInternal(row, RACE_EVENT_COLUMN_ID, raceEvent);
    }

    public Long getRaceEvent(int row) {
      return (Long) getValueInternal(row, RACE_EVENT_COLUMN_ID);
    }

    public void setRaceFormButton(int row, String raceFormButton) {
      setValueInternal(row, RACE_FORM_BUTTON_COLUMN_ID, raceFormButton);
    }

    public String getRaceFormButton(int row) {
      return (String) getValueInternal(row, RACE_FORM_BUTTON_COLUMN_ID);
    }

    public void setRaceBean(int row, RaceBean raceBean) {
      setValueInternal(row, RACE_BEAN_COLUMN_ID, raceBean);
    }

    public RaceBean getRaceBean(int row) {
      return (RaceBean) getValueInternal(row, RACE_BEAN_COLUMN_ID);
    }

    @Override
    public int getColumnCount() {
      return 13;
    }

    @Override
    public Object getValueAt(int row, int column) {
      switch (column) {
        case RACE_NR_COLUMN_ID:
          return getRaceNr(row);
        case RUNNER_NR_COLUMN_ID:
          return getRunnerNr(row);
        case CLUB_NR_COLUMN_ID:
          return getClubNr(row);
        case LAST_NAME_COLUMN_ID:
          return getLastName(row);
        case FIRST_NAME_COLUMN_ID:
          return getFirstName(row);
        case E_CARD_COLUMN_ID:
          return getECard(row);
        case BIB_NUMBER_COLUMN_ID:
          return getBibNumber(row);
        case LEG_START_TIME_COLUMN_ID:
          return getLegStartTime(row);
        case LEG_COLUMN_ID:
          return getLeg(row);
        case NATION_COLUMN_ID:
          return getNation(row);
        case RACE_EVENT_COLUMN_ID:
          return getRaceEvent(row);
        case RACE_FORM_BUTTON_COLUMN_ID:
          return getRaceFormButton(row);
        case RACE_BEAN_COLUMN_ID:
          return getRaceBean(row);
        default:
          return null;
      }
    }

    @Override
    public void setValueAt(int row, int column, Object value) {
      switch (column) {
        case RACE_NR_COLUMN_ID:
          setRaceNr(row, (Long) value);
          break;
        case RUNNER_NR_COLUMN_ID:
          setRunnerNr(row, (Long) value);
          break;
        case CLUB_NR_COLUMN_ID:
          setClubNr(row, (Long) value);
          break;
        case LAST_NAME_COLUMN_ID:
          setLastName(row, (String) value);
          break;
        case FIRST_NAME_COLUMN_ID:
          setFirstName(row, (String) value);
          break;
        case E_CARD_COLUMN_ID:
          setECard(row, (Long) value);
          break;
        case BIB_NUMBER_COLUMN_ID:
          setBibNumber(row, (String) value);
          break;
        case LEG_START_TIME_COLUMN_ID:
          setLegStartTime(row, (Date) value);
          break;
        case LEG_COLUMN_ID:
          setLeg(row, (Long) value);
          break;
        case NATION_COLUMN_ID:
          setNation(row, (Long) value);
          break;
        case RACE_EVENT_COLUMN_ID:
          setRaceEvent(row, (Long) value);
          break;
        case RACE_FORM_BUTTON_COLUMN_ID:
          setRaceFormButton(row, (String) value);
          break;
        case RACE_BEAN_COLUMN_ID:
          setRaceBean(row, (RaceBean) value);
          break;
      }
    }
  }

  public static class Registration extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Registration() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Runner extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Runner() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Sex extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Sex() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Year extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Year() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
