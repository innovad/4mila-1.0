package com.rtiming.shared.dataexchange;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

public class DataExchangeStartFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public DataExchangeStartFormData() {
  }

  public ImportNrProperty getImportNrProperty() {
    return getPropertyByClass(ImportNrProperty.class);
  }

  /**
   * access method for property ImportNr.
   */
  public Long getImportNr() {
    return getImportNrProperty().getValue();
  }

  /**
   * access method for property ImportNr.
   */
  public void setImportNr(Long importNr) {
    getImportNrProperty().setValue(importNr);
  }

  public CharacterSet getCharacterSet() {
    return getFieldByClass(CharacterSet.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public FieldSeparator getFieldSeparator() {
    return getFieldByClass(FieldSeparator.class);
  }

  public File getFile() {
    return getFieldByClass(File.class);
  }

  public Format getFormat() {
    return getFieldByClass(Format.class);
  }

  public IgnoreHeaderRow getIgnoreHeaderRow() {
    return getFieldByClass(IgnoreHeaderRow.class);
  }

  public ImportExportGroup getImportExportGroup() {
    return getFieldByClass(ImportExportGroup.class);
  }

  public Language getLanguage() {
    return getFieldByClass(Language.class);
  }

  public TextEnclosing getTextEnclosing() {
    return getFieldByClass(TextEnclosing.class);
  }

  public class ImportNrProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public ImportNrProperty() {
    }
  }

  public static class CharacterSet extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public CharacterSet() {
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

  public static class FieldSeparator extends AbstractValueFieldData<Character> {
    private static final long serialVersionUID = 1L;

    public FieldSeparator() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class File extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public File() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class Format extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Format() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class IgnoreHeaderRow extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public IgnoreHeaderRow() {
    }
  }

  public static class ImportExportGroup extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public ImportExportGroup() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class Language extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Language() {
    }

    /**
     * list of derived validation rules.
     */

  }

  public static class TextEnclosing extends AbstractValueFieldData<Character> {
    private static final long serialVersionUID = 1L;

    public TextEnclosing() {
    }

    /**
     * list of derived validation rules.
     */
  }
}
