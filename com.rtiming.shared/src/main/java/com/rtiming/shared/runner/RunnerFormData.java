package com.rtiming.shared.runner;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationFieldData;
import com.rtiming.shared.settings.city.AbstractAddressBoxData;

public class RunnerFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public RunnerFormData() {
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

  public RunnerNrProperty getRunnerNrProperty() {
    return getPropertyByClass(RunnerNrProperty.class);
  }

  /**
   * access method for property RunnerNr.
   */
  public Long getRunnerNr() {
    return getRunnerNrProperty().getValue();
  }

  /**
   * access method for property RunnerNr.
   */
  public void setRunnerNr(Long runnerNr) {
    getRunnerNrProperty().setValue(runnerNr);
  }

  public Active getActive() {
    return getFieldByClass(Active.class);
  }

  public AdditionalInformation getAdditionalInformation() {
    return getFieldByClass(AdditionalInformation.class);
  }

  public AddressBox getAddressBox() {
    return getFieldByClass(AddressBox.class);
  }

  public Birthdate getBirthdate() {
    return getFieldByClass(Birthdate.class);
  }

  public Club getClub() {
    return getFieldByClass(Club.class);
  }

  public DefaultClazz getDefaultClazz() {
    return getFieldByClass(DefaultClazz.class);
  }

  public ECard getECard() {
    return getFieldByClass(ECard.class);
  }

  public ExtKey getExtKey() {
    return getFieldByClass(ExtKey.class);
  }

  public FirstName getFirstName() {
    return getFieldByClass(FirstName.class);
  }

  public LastName getLastName() {
    return getFieldByClass(LastName.class);
  }

  public NationUid getNationUid() {
    return getFieldByClass(NationUid.class);
  }

  public Sex getSex() {
    return getFieldByClass(Sex.class);
  }

  public Year getYear() {
    return getFieldByClass(Year.class);
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

  public class RunnerNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public RunnerNrProperty() {
    }
  }

  public static class Active extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public Active() {
    }
  }

  public static class AdditionalInformation extends AbstractAdditionalInformationFieldData {
    private static final long serialVersionUID = 1L;

    public AdditionalInformation() {
    }
  }

  public static class AddressBox extends AbstractAddressBoxData {
    private static final long serialVersionUID = 1L;

    public AddressBox() {
    }
  }

  public static class Birthdate extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public Birthdate() {
    }
  }

  public static class Club extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Club() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class DefaultClazz extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public DefaultClazz() {
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

  public static class ExtKey extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public ExtKey() {
    }

    /**
     * list of derived validation rules.
     */
     
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

  public static class NationUid extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public NationUid() {
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
