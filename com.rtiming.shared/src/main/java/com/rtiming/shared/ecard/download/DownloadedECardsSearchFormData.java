package com.rtiming.shared.ecard.download;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class DownloadedECardsSearchFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public DownloadedECardsSearchFormData() {
  }

  public PresentationTypeProperty getPresentationTypeProperty() {
    return getPropertyByClass(PresentationTypeProperty.class);
  }

  /**
   * access method for property PresentationType.
   */
  public DownloadedECards getPresentationType() {
    return getPresentationTypeProperty().getValue();
  }

  /**
   * access method for property PresentationType.
   */
  public void setPresentationType(DownloadedECards presentationType) {
    getPresentationTypeProperty().setValue(presentationType);
  }

  public Clazz getClazz() {
    return getFieldByClass(Clazz.class);
  }

  public Course getCourse() {
    return getFieldByClass(Course.class);
  }

  public DownloadedOnFrom getDownloadedOnFrom() {
    return getFieldByClass(DownloadedOnFrom.class);
  }

  public DownloadedOnTo getDownloadedOnTo() {
    return getFieldByClass(DownloadedOnTo.class);
  }

  public ECard getECard() {
    return getFieldByClass(ECard.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public RaceStatus getRaceStatus() {
    return getFieldByClass(RaceStatus.class);
  }

  public Runner getRunner() {
    return getFieldByClass(Runner.class);
  }

  public RunnerAssignedGroup getRunnerAssignedGroup() {
    return getFieldByClass(RunnerAssignedGroup.class);
  }

  public class PresentationTypeProperty extends AbstractPropertyData<DownloadedECards> {
    private static final long serialVersionUID = 1L;

    public PresentationTypeProperty() {
    }
  }

  public static class Clazz extends AbstractValueFieldData<Long[]> {
    private static final long serialVersionUID = 1L;

    public Clazz() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Course extends AbstractValueFieldData<Long[]> {
    private static final long serialVersionUID = 1L;

    public Course() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class DownloadedOnFrom extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public DownloadedOnFrom() {
    }
  }

  public static class DownloadedOnTo extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public DownloadedOnTo() {
    }
  }

  public static class ECard extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public ECard() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Event extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Event() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class RaceStatus extends AbstractValueFieldData<Long[]> {
    private static final long serialVersionUID = 1L;

    public RaceStatus() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Runner extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Runner() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class RunnerAssignedGroup extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public RunnerAssignedGroup() {
    }
  }
}
