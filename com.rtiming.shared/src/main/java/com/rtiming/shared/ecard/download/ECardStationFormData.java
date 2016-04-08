package com.rtiming.shared.ecard.download;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class ECardStationFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public ECardStationFormData() {
  }

  public ECardStationNrProperty getECardStationNrProperty() {
    return getPropertyByClass(ECardStationNrProperty.class);
  }

  /**
   * access method for property ECardStationNr.
   */
  public Long getECardStationNr() {
    return getECardStationNrProperty().getValue();
  }

  /**
   * access method for property ECardStationNr.
   */
  public void setECardStationNr(Long eCardStationNr) {
    getECardStationNrProperty().setValue(eCardStationNr);
  }

  public Baud getBaud() {
    return getFieldByClass(Baud.class);
  }

  public ClientAddress getClientAddress() {
    return getFieldByClass(ClientAddress.class);
  }

  public Identifier getIdentifier() {
    return getFieldByClass(Identifier.class);
  }

  public Modus getModus() {
    return getFieldByClass(Modus.class);
  }

  public Port getPort() {
    return getFieldByClass(Port.class);
  }

  public PosPrinter getPosPrinter() {
    return getFieldByClass(PosPrinter.class);
  }

  public Printer getPrinter() {
    return getFieldByClass(Printer.class);
  }

  public class ECardStationNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public ECardStationNrProperty() {
    }
  }

  public static class Baud extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Baud() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class ClientAddress extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public ClientAddress() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class Identifier extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Identifier() {
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

  public static class Port extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Port() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class PosPrinter extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public PosPrinter() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class Printer extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Printer() {
    }

    /**
     * list of derived validation rules.
     */
  }
}
