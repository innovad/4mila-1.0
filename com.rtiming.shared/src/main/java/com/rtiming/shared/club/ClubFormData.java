package com.rtiming.shared.club;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationFieldData;

public class ClubFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public ClubFormData() {
  }

  public ClubNrProperty getClubNrProperty() {
    return getPropertyByClass(ClubNrProperty.class);
  }

  /**
   * access method for property ClubNr.
   */
  public Long getClubNr() {
    return getClubNrProperty().getValue();
  }

  /**
   * access method for property ClubNr.
   */
  public void setClubNr(Long clubNr) {
    getClubNrProperty().setValue(clubNr);
  }

  public AdditionalInformation getAdditionalInformation() {
    return getFieldByClass(AdditionalInformation.class);
  }

  public ContactRunner getContactRunner() {
    return getFieldByClass(ContactRunner.class);
  }

  public ExtKey getExtKey() {
    return getFieldByClass(ExtKey.class);
  }

  public Name getName() {
    return getFieldByClass(Name.class);
  }

  public Shortcut getShortcut() {
    return getFieldByClass(Shortcut.class);
  }

  public class ClubNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public ClubNrProperty() {
    }
  }

  public static class AdditionalInformation extends AbstractAdditionalInformationFieldData {
    private static final long serialVersionUID = 1L;

    public AdditionalInformation() {
    }
  }

  public static class ContactRunner extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ContactRunner() {
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

  public static class Name extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Name() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class Shortcut extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Shortcut() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
