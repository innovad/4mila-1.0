package com.rtiming.shared.ecard.download;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class DownloadedECardFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public DownloadedECardFormData() {
  }

  public PunchSessionNrProperty getPunchSessionNrProperty() {
    return getPropertyByClass(PunchSessionNrProperty.class);
  }

  /**
   * access method for property PunchSessionNr.
   */
  public Long getPunchSessionNr() {
    return getPunchSessionNrProperty().getValue();
  }

  /**
   * access method for property PunchSessionNr.
   */
  public void setPunchSessionNr(Long punchSessionNr) {
    getPunchSessionNrProperty().setValue(punchSessionNr);
  }

  public RawCheckProperty getRawCheckProperty() {
    return getPropertyByClass(RawCheckProperty.class);
  }

  /**
   * access method for property RawCheck.
   */
  public Long getRawCheck() {
    return getRawCheckProperty().getValue();
  }

  /**
   * access method for property RawCheck.
   */
  public void setRawCheck(Long rawCheck) {
    getRawCheckProperty().setValue(rawCheck);
  }

  public RawClearProperty getRawClearProperty() {
    return getPropertyByClass(RawClearProperty.class);
  }

  /**
   * access method for property RawClear.
   */
  public Long getRawClear() {
    return getRawClearProperty().getValue();
  }

  /**
   * access method for property RawClear.
   */
  public void setRawClear(Long rawClear) {
    getRawClearProperty().setValue(rawClear);
  }

  public RawFinishProperty getRawFinishProperty() {
    return getPropertyByClass(RawFinishProperty.class);
  }

  /**
   * access method for property RawFinish.
   */
  public Long getRawFinish() {
    return getRawFinishProperty().getValue();
  }

  /**
   * access method for property RawFinish.
   */
  public void setRawFinish(Long rawFinish) {
    getRawFinishProperty().setValue(rawFinish);
  }

  public RawStartProperty getRawStartProperty() {
    return getPropertyByClass(RawStartProperty.class);
  }

  /**
   * access method for property RawStart.
   */
  public Long getRawStart() {
    return getRawStartProperty().getValue();
  }

  /**
   * access method for property RawStart.
   */
  public void setRawStart(Long rawStart) {
    getRawStartProperty().setValue(rawStart);
  }

  public Check getCheck() {
    return getFieldByClass(Check.class);
  }

  public Clear getClear() {
    return getFieldByClass(Clear.class);
  }

  public ECard getECard() {
    return getFieldByClass(ECard.class);
  }

  public ECardStation getECardStation() {
    return getFieldByClass(ECardStation.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public EvtDownload getEvtDownload() {
    return getFieldByClass(EvtDownload.class);
  }

  public Finish getFinish() {
    return getFieldByClass(Finish.class);
  }

  public Race getRace() {
    return getFieldByClass(Race.class);
  }

  public RawData getRawData() {
    return getFieldByClass(RawData.class);
  }

  public Start getStart() {
    return getFieldByClass(Start.class);
  }

  public class PunchSessionNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public PunchSessionNrProperty() {
    }
  }

  public class RawCheckProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RawCheckProperty() {
    }
  }

  public class RawClearProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RawClearProperty() {
    }
  }

  public class RawFinishProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RawFinishProperty() {
    }
  }

  public class RawStartProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RawStartProperty() {
    }
  }

  public static class Check extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public Check() {
    }
  }

  public static class Clear extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public Clear() {
    }
  }

  public static class ECard extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ECard() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class ECardStation extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ECardStation() {
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

  public static class EvtDownload extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public EvtDownload() {
    }
  }

  public static class Finish extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public Finish() {
    }
  }

  public static class Race extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Race() {
    }

    /**
     * list of derived validation rules.
     */
  }

  public static class RawData extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public RawData() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class Start extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public Start() {
    }
  }
}
