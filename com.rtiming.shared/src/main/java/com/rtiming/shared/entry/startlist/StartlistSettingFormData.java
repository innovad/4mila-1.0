package com.rtiming.shared.entry.startlist;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;


public class StartlistSettingFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public StartlistSettingFormData() {
  }

  public EventNrProperty getEventNrProperty() {
    return getPropertyByClass(EventNrProperty.class);
  }

  /**
   * access method for property EventNr.
   */
  public Long getEventNr() {
    return getEventNrProperty().getValue();
  }

  /**
   * access method for property EventNr.
   */
  public void setEventNr(Long eventNr) {
    getEventNrProperty().setValue(eventNr);
  }

  public NewClassUidProperty getNewClassUidProperty() {
    return getPropertyByClass(NewClassUidProperty.class);
  }

  /**
   * access method for property NewClassUid.
   */
  public Long getNewClassUid() {
    return getNewClassUidProperty().getValue();
  }

  /**
   * access method for property NewClassUid.
   */
  public void setNewClassUid(Long newClassUid) {
    getNewClassUidProperty().setValue(newClassUid);
  }

  public ParticipationCountProperty getParticipationCountProperty() {
    return getPropertyByClass(ParticipationCountProperty.class);
  }

  /**
   * access method for property ParticipationCount.
   */
  public Long getParticipationCount() {
    return getParticipationCountProperty().getValue();
  }

  /**
   * access method for property ParticipationCount.
   */
  public void setParticipationCount(Long participationCount) {
    getParticipationCountProperty().setValue(participationCount);
  }

  public StartlistSettingNrProperty getStartlistSettingNrProperty() {
    return getPropertyByClass(StartlistSettingNrProperty.class);
  }

  /**
   * access method for property StartlistSettingNr.
   */
  public Long getStartlistSettingNr() {
    return getStartlistSettingNrProperty().getValue();
  }

  /**
   * access method for property StartlistSettingNr.
   */
  public void setStartlistSettingNr(Long startlistSettingNr) {
    getStartlistSettingNrProperty().setValue(startlistSettingNr);
  }

  public BibNoFrom getBibNoFrom() {
    return getFieldByClass(BibNoFrom.class);
  }

  public BibNoOrderUid getBibNoOrderUid() {
    return getFieldByClass(BibNoOrderUid.class);
  }

  public FirstStart getFirstStart() {
    return getFieldByClass(FirstStart.class);
  }

  public LastBibNo getLastBibNo() {
    return getFieldByClass(LastBibNo.class);
  }

  public LastStart getLastStart() {
    return getFieldByClass(LastStart.class);
  }

  public Options getOptions() {
    return getFieldByClass(Options.class);
  }

  public StartInterval getStartInterval() {
    return getFieldByClass(StartInterval.class);
  }

  public TypeUid getTypeUid() {
    return getFieldByClass(TypeUid.class);
  }

  public VacantAbsolute getVacantAbsolute() {
    return getFieldByClass(VacantAbsolute.class);
  }

  public VacantPercent getVacantPercent() {
    return getFieldByClass(VacantPercent.class);
  }

  public VacantPositionGroup getVacantPositionGroup() {
    return getFieldByClass(VacantPositionGroup.class);
  }

  public class EventNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public EventNrProperty() {
    }
  }

  public class NewClassUidProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public NewClassUidProperty() {
    }
  }

  public class ParticipationCountProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public ParticipationCountProperty() {
    }
  }

  public class StartlistSettingNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public StartlistSettingNrProperty() {
    }
  }

  public static class BibNoFrom extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public BibNoFrom() {
    }
  }

  public static class BibNoOrderUid extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public BibNoOrderUid() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class FirstStart extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public FirstStart() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class LastBibNo extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public LastBibNo() {
    }
  }

  public static class LastStart extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public LastStart() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Options extends AbstractValueFieldData<Long[]> {
    private static final long serialVersionUID = 1L;

    public Options() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class StartInterval extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public StartInterval() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class TypeUid extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public TypeUid() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class VacantAbsolute extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public VacantAbsolute() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class VacantPercent extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public VacantPercent() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class VacantPositionGroup extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public VacantPositionGroup() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
