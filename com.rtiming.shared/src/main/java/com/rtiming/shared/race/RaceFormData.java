package com.rtiming.shared.race;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.settings.city.AbstractAddressBoxData;

public class RaceFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public RaceFormData() {
  }

  public AddressNrProperty getAddressNrProperty() {
    return getPropertyByClass(AddressNrProperty.class);
  }

  /**
   * access method for property AddressNr.
   */
  public Long getAddressNr() {
    return getAddressNrProperty().getValue();
  }

  /**
   * access method for property AddressNr.
   */
  public void setAddressNr(Long addressNr) {
    getAddressNrProperty().setValue(addressNr);
  }

  public ClientNrProperty getClientNrProperty() {
    return getPropertyByClass(ClientNrProperty.class);
  }

  /**
   * access method for property ClientNr.
   */
  public Long getClientNr() {
    return getClientNrProperty().getValue();
  }

  /**
   * access method for property ClientNr.
   */
  public void setClientNr(Long clientNr) {
    getClientNrProperty().setValue(clientNr);
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

  public RaceNrProperty getRaceNrProperty() {
    return getPropertyByClass(RaceNrProperty.class);
  }

  /**
   * access method for property RaceNr.
   */
  public Long getRaceNr() {
    return getRaceNrProperty().getValue();
  }

  /**
   * access method for property RaceNr.
   */
  public void setRaceNr(Long raceNr) {
    getRaceNrProperty().setValue(raceNr);
  }

  public RawLegStartTimeProperty getRawLegStartTimeProperty() {
    return getPropertyByClass(RawLegStartTimeProperty.class);
  }

  /**
   * access method for property RawLegStartTime.
   */
  public Long getRawLegStartTime() {
    return getRawLegStartTimeProperty().getValue();
  }

  /**
   * access method for property RawLegStartTime.
   */
  public void setRawLegStartTime(Long rawLegStartTime) {
    getRawLegStartTimeProperty().setValue(rawLegStartTime);
  }

  public RawLegTimeProperty getRawLegTimeProperty() {
    return getPropertyByClass(RawLegTimeProperty.class);
  }

  /**
   * access method for property RawLegTime.
   */
  public Long getRawLegTime() {
    return getRawLegTimeProperty().getValue();
  }

  /**
   * access method for property RawLegTime.
   */
  public void setRawLegTime(Long rawLegTime) {
    getRawLegTimeProperty().setValue(rawLegTime);
  }

  public RunnerProperty getRunnerProperty() {
    return getPropertyByClass(RunnerProperty.class);
  }

  /**
   * access method for property Runner.
   */
  public RunnerBean getRunner() {
    return getRunnerProperty().getValue();
  }

  /**
   * access method for property Runner.
   */
  public void setRunner(RunnerBean runner) {
    getRunnerProperty().setValue(runner);
  }

  public AddressBox getAddressBox() {
    return getFieldByClass(AddressBox.class);
  }

  public BibNumber getBibNumber() {
    return getFieldByClass(BibNumber.class);
  }

  public Club getClub() {
    return getFieldByClass(Club.class);
  }

  public ECardNr getECardNr() {
    return getFieldByClass(ECardNr.class);
  }

  public EventNr getEventNr() {
    return getFieldByClass(EventNr.class);
  }

  public LegClassUid getLegClassUid() {
    return getFieldByClass(LegClassUid.class);
  }

  public LegFinishTime getLegFinishTime() {
    return getFieldByClass(LegFinishTime.class);
  }

  public LegStartTime getLegStartTime() {
    return getFieldByClass(LegStartTime.class);
  }

  public LegTime getLegTime() {
    return getFieldByClass(LegTime.class);
  }

  public ManualStatus getManualStatus() {
    return getFieldByClass(ManualStatus.class);
  }

  public Nation getNation() {
    return getFieldByClass(Nation.class);
  }

  public RaceStatus getRaceStatus() {
    return getFieldByClass(RaceStatus.class);
  }

  public RunnerNr getRunnerNr() {
    return getFieldByClass(RunnerNr.class);
  }

  public class AddressNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public AddressNrProperty() {
    }
  }

  public class ClientNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public ClientNrProperty() {
    }
  }

  public class EntryNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public EntryNrProperty() {
    }
  }

  public class RaceNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RaceNrProperty() {
    }
  }

  public class RawLegStartTimeProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RawLegStartTimeProperty() {
    }
  }

  public class RawLegTimeProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RawLegTimeProperty() {
    }
  }

  public class RunnerProperty extends AbstractPropertyData<RunnerBean> {
    private static final long serialVersionUID = 1L;

    public RunnerProperty() {
    }
  }

  public static class AddressBox extends AbstractAddressBoxData {
    private static final long serialVersionUID = 1L;

    public AddressBox() {
    }
  }

  public static class BibNumber extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public BibNumber() {
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

  public static class ECardNr extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ECardNr() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class EventNr extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public EventNr() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class LegClassUid extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public LegClassUid() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class LegFinishTime extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public LegFinishTime() {
    }
  }

  public static class LegStartTime extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public LegStartTime() {
    }
  }

  public static class LegTime extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public LegTime() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class ManualStatus extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public ManualStatus() {
    }
  }

  public static class Nation extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Nation() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class RaceStatus extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public RaceStatus() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class RunnerNr extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public RunnerNr() {
    }

    /**
     * list of derived validation rules.
     */
 
  }
}
