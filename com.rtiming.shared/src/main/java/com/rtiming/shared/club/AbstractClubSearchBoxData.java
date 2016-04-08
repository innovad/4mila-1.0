package com.rtiming.shared.club;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

public abstract class AbstractClubSearchBoxData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractClubSearchBoxData() {
  }

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

    public Club() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class ContactPerson extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public ContactPerson() {
    }

  }

  public static class Name extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Name() {
    }

  }

  public static class Shortcut extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Shortcut() {
    }

  }
}
