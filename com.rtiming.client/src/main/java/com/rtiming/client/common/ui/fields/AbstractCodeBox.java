package com.rtiming.client.common.ui.fields;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.DefaultSubtypeSdkCommand;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractCodeBoxData;
import com.rtiming.shared.settings.user.LanguageCodeType;

@FormData(value = AbstractCodeBoxData.class, sdkCommand = SdkCommand.CREATE, defaultSubtypeSdkCommand = DefaultSubtypeSdkCommand.CREATE)
public abstract class AbstractCodeBox extends AbstractGroupBox {

  @Override
  public String getConfiguredLabel() {
    return Texts.get("Code");
  }

  @Override
  public int getConfiguredGridH() {
    return 5;
  }

  public ShortcutField getShortcutField() {
    return getFieldByClass(ShortcutField.class);
  }

  @Order(1.0)
  public class CodeUidField extends AbstractLongField {

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

  }

  @Order(2.0)
  public class CodeTypeUidField extends AbstractLongField {

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

  }

  @Order(10.0)
  public class ShortcutField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Shortcut");
    }

    @Override
    protected int getConfiguredMaxLength() {
      return 60;
    }

  }

  @Order(20.0)
  public class ActiveField extends AbstractBooleanField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Active");
    }
  }

  @Order(30.0)
  public class LanguageField extends AbstractTableField<LanguageField.Table> {

    @Override
    protected int getConfiguredGridH() {
      return 4;
    }

    @Override
    protected int getConfiguredGridW() {
      return 2;
    }

    @Override
    protected String getConfiguredLabel() {
      return ScoutTexts.get("Language");
    }

    @Override
    protected boolean getConfiguredLabelVisible() {
      return false;
    }

    @Order(10.0)
    public class Table extends AbstractTable {

      @Override
      protected boolean getConfiguredSortEnabled() {
        return false;
      }

      @Override
      protected boolean getConfiguredAutoResizeColumns() {
        return true;
      }

      public LanguageColumn getLanguageColumn() {
        return getColumnSet().getColumnByClass(LanguageColumn.class);
      }

      public TranslationColumn getTranslationColumn() {
        return getColumnSet().getColumnByClass(TranslationColumn.class);
      }

      @Order(10.0)
      public class LanguageColumn extends AbstractSmartColumn<Long> {

        @Override
        protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
          return LanguageCodeType.class;
        }

        @Override
        protected String getConfiguredHeaderText() {
          return ScoutTexts.get("Language");
        }

        @Override
        protected int getConfiguredWidth() {
          return 150;
        }
      }

      @Order(20.0)
      public class TranslationColumn extends AbstractStringColumn {

        public class TextField extends AbstractStringField {
          @Override
          protected int getConfiguredMaxLength() {
            return 250;
          }

          @Override
          protected boolean getConfiguredMandatory() {
            return true;
          }
        }

        @Override
        protected String getConfiguredHeaderText() {
          return Texts.get("Translation");
        }

        @Override
        protected int getConfiguredWidth() {
          return 150;
        }

        @Override
        protected boolean getConfiguredEditable() {
          return true;
        }

// TODO MIG        
//        @Override
//        protected boolean execIsEditable(ITableRow row) throws ProcessingException {
//          return true;
//        }

        @Override
        protected IFormField execPrepareEdit(ITableRow row) throws ProcessingException {
          TextField textField = new TextField();
          textField.setValue(getTranslationColumn().getValue(row));
          return textField;
        }

        @Override
        protected void execCompleteEdit(ITableRow row, IFormField editingField) throws ProcessingException {
          getTranslationColumn().setValue(row, ((TextField) editingField).getValue());
        }

      }
    }
  }

}
