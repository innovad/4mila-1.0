package com.rtiming.shared.common;

import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.tablefield.AbstractTableFieldData;

public abstract class AbstractCodeBoxData extends AbstractFormFieldData {
  private static final long serialVersionUID = 1L;

  public AbstractCodeBoxData() {
  }

  public Active getActive() {
    return getFieldByClass(Active.class);
  }

  public CodeTypeUid getCodeTypeUid() {
    return getFieldByClass(CodeTypeUid.class);
  }

  public CodeUid getCodeUid() {
    return getFieldByClass(CodeUid.class);
  }

  public Language getLanguage() {
    return getFieldByClass(Language.class);
  }

  public Shortcut getShortcut() {
    return getFieldByClass(Shortcut.class);
  }

  public static class Active extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public Active() {
    }
  }

  public static class CodeTypeUid extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public CodeTypeUid() {
    }
  }

  public static class CodeUid extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public CodeUid() {
    }
  }

  public static class Language extends AbstractTableFieldData {
    private static final long serialVersionUID = 1L;

    public Language() {
    }

    public static final int LANGUAGE_COLUMN_ID = 0;
    public static final int TRANSLATION_COLUMN_ID = 1;

    public void setLanguage(int row, Long language) {
      setValueInternal(row, LANGUAGE_COLUMN_ID, language);
    }

    public Long getLanguage(int row) {
      return (Long) getValueInternal(row, LANGUAGE_COLUMN_ID);
    }

    public void setTranslation(int row, String translation) {
      setValueInternal(row, TRANSLATION_COLUMN_ID, translation);
    }

    public String getTranslation(int row) {
      return (String) getValueInternal(row, TRANSLATION_COLUMN_ID);
    }

    @Override
    public int getColumnCount() {
      return 2;
    }

    @Override
    public Object getValueAt(int row, int column) {
      switch (column) {
        case LANGUAGE_COLUMN_ID:
          return getLanguage(row);
        case TRANSLATION_COLUMN_ID:
          return getTranslation(row);
        default:
          return null;
      }
    }

    @Override
    public void setValueAt(int row, int column, Object value) {
      switch (column) {
        case LANGUAGE_COLUMN_ID:
          setLanguage(row, (Long) value);
          break;
        case TRANSLATION_COLUMN_ID:
          setTranslation(row, (String) value);
          break;
      }
    }
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
