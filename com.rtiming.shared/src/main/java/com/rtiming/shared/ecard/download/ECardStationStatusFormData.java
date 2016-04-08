package com.rtiming.shared.ecard.download;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public class ECardStationStatusFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public ECardStationStatusFormData() {
  }

  public ComPort getComPort() {
    return getFieldByClass(ComPort.class);
  }

  public Modus getModus() {
    return getFieldByClass(Modus.class);
  }

  public Status getStatus() {
    return getFieldByClass(Status.class);
  }

  public static class ComPort extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public ComPort() {
    }

    /**
     * list of derived validation rules.
     */
  }

  public static class Modus extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Modus() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class Status extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Status() {
    }
  }
}
