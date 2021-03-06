package com.rtiming.shared.common;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractFormFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.fields.tablefield.AbstractTableFieldBeanData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.common.ui.fields.AbstractCodeBox", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public abstract class AbstractCodeBoxData extends AbstractFormFieldData {

  private static final long serialVersionUID = 1L;

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
  }

  public static class CodeTypeUid extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class CodeUid extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class Language extends AbstractTableFieldBeanData {

    private static final long serialVersionUID = 1L;

    @Override
    public LanguageRowData addRow() {
      return (LanguageRowData) super.addRow();
    }

    @Override
    public LanguageRowData addRow(int rowState) {
      return (LanguageRowData) super.addRow(rowState);
    }

    @Override
    public LanguageRowData createRow() {
      return new LanguageRowData();
    }

    @Override
    public Class<? extends AbstractTableRowData> getRowType() {
      return LanguageRowData.class;
    }

    @Override
    public LanguageRowData[] getRows() {
      return (LanguageRowData[]) super.getRows();
    }

    @Override
    public LanguageRowData rowAt(int index) {
      return (LanguageRowData) super.rowAt(index);
    }

    public void setRows(LanguageRowData[] rows) {
      super.setRows(rows);
    }

    public static class LanguageRowData extends AbstractTableRowData {

      private static final long serialVersionUID = 1L;
      public static final String language = "language";
      public static final String translation = "translation";
      private Long m_language;
      private String m_translation;

      public Long getLanguage() {
        return m_language;
      }

      public void setLanguage(Long newLanguage) {
        m_language = newLanguage;
      }

      public String getTranslation() {
        return m_translation;
      }

      public void setTranslation(String newTranslation) {
        m_translation = newTranslation;
      }
    }
  }

  public static class Shortcut extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }
}
