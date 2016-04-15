package com.rtiming.shared.club;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.club.AbstractClubSearchBox", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public abstract class AbstractClubSearchBoxData extends AbstractFormFieldData {

  private static final long serialVersionUID = 1L;

  public Club getClub() {
    return getFieldByClass(Club.class);
  }

  public ContactPerson getContactPerson() {
    return getFieldByClass(ContactPerson.class);
  }

  public Name getName() {
    return getFieldByClass(Name.class);
  }

  public Shortcut getShortcut() {
    return getFieldByClass(Shortcut.class);
  }

  public static class Club extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class ContactPerson extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class Name extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class Shortcut extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }
}
