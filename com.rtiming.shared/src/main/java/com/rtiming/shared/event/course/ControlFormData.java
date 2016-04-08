package com.rtiming.shared.event.course;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class ControlFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public ControlFormData() {
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

  public ControlNrProperty getControlNrProperty() {
    return getPropertyByClass(ControlNrProperty.class);
  }

  /**
   * access method for property ControlNr.
   */
  public Long getControlNr() {
    return getControlNrProperty().getValue();
  }

  /**
   * access method for property ControlNr.
   */
  public void setControlNr(Long controlNr) {
    getControlNrProperty().setValue(controlNr);
  }

  public Active getActive() {
    return getFieldByClass(Active.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public GlobalX getGlobalX() {
    return getFieldByClass(GlobalX.class);
  }

  public GlobalY getGlobalY() {
    return getFieldByClass(GlobalY.class);
  }

  public Number getNumber() {
    return getFieldByClass(Number.class);
  }

  public PositionX getPositionX() {
    return getFieldByClass(PositionX.class);
  }

  public PositionY getPositionY() {
    return getFieldByClass(PositionY.class);
  }

  public Type getType() {
    return getFieldByClass(Type.class);
  }

  public class ClientNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public ClientNrProperty() {
    }
  }

  public class ControlNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public ControlNrProperty() {
    }
  }

  public static class Active extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public Active() {
    }
  }

  public static class Event extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Event() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class GlobalX extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public GlobalX() {
    }
  }

  public static class GlobalY extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public GlobalY() {
    }
  }

  public static class Number extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Number() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class PositionX extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public PositionX() {
    }
  }

  public static class PositionY extends AbstractValueFieldData<Double> {
    private static final long serialVersionUID = 1L;

    public PositionY() {
    }
  }

  public static class Type extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Type() {
    }

    /**
     * list of derived validation rules.
     */
 
  }
}
