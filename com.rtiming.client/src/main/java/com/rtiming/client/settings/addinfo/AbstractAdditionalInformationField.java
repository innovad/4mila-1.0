package com.rtiming.client.settings.addinfo;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBigDecimalColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.data.basic.FontSpec;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.BooleanCodeType;
import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationFieldData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationLookupCall;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;

@Order(10.0)
@FormData(value = AbstractAdditionalInformationFieldData.class, sdkCommand = SdkCommand.CREATE)
public abstract class AbstractAdditionalInformationField extends AbstractTableField<AbstractAdditionalInformationField.Table> {

  /**
   * this method must be called from the implementing dialog in its validation method
   * 
   * @return true if validation successful
   * @throws ProcessingException
   */
  public boolean execValidate() throws ProcessingException {

    ArrayList<String> mandatoryTexts = new ArrayList<String>();

    for (int k = 0; k < getTable().getRowCount(); k++) {
      if (BooleanUtility.nvl(getTable().getMandatoryColumn().getValue(k), false) && StringUtility.isNullOrEmpty(getTable().getValueColumn().getValue(k))) {
        ICode code = CODES.getCodeType(AdditionalInformationCodeType.class).getCode(getTable().getAdditionalInformationUidColumn().getValue(k));
        mandatoryTexts.add(code != null ? code.getText() : getTable().getAdditionalInformationUidColumn().getValue(k).toString());
      }
    }

    if (mandatoryTexts.size() > 0) {
      StringBuffer buf = new StringBuffer();
      buf.append(ScoutTexts.get("FormEmptyMandatoryFieldsMessage") + "\n\n");
      for (Iterator it = mandatoryTexts.iterator(); it.hasNext();) {
        buf.append("- " + it.next() + "\n");
      }
      buf.append("\n");
      String introText = ScoutTexts.get("FormIncompleteIntro");
      throw new VetoException(introText, buf.toString());
    }

    return true;
  }

  @Override
  protected int getConfiguredGridH() {
    return 5;
  }

  protected abstract void handleEditCompleted(ITableRow row) throws ProcessingException;

  @Override
  protected int getConfiguredGridW() {
    return 2;
  }

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("AdditionalInformation");
  }

  @Override
  protected boolean getConfiguredLabelVisible() {
    return false;
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public ValueColumn getValueColumn() {
      return getColumnSet().getColumnByClass(ValueColumn.class);
    }

    @Override
    protected String getConfiguredDefaultIconId() {
      return Icons.ADDITIONAL_INFORMATION;
    }

    @Override
    protected boolean getConfiguredAutoResizeColumns() {
      return true;
    }

    public MinimumColumn getMinimumColumn() {
      return getColumnSet().getColumnByClass(MinimumColumn.class);
    }

    public MaximumColumn getMaximumColumn() {
      return getColumnSet().getColumnByClass(MaximumColumn.class);
    }

    public FeeGroupColumn getFeeGroupColumn() {
      return getColumnSet().getColumnByClass(FeeGroupColumn.class);
    }

    public TypeColumn getTypeColumn() {
      return getColumnSet().getColumnByClass(TypeColumn.class);
    }

    public IntegerColumn getIntegerColumn() {
      return getColumnSet().getColumnByClass(IntegerColumn.class);
    }

    public DefaultValueIntegerColumn getDefaultValueIntegerColumn() {
      return getColumnSet().getColumnByClass(DefaultValueIntegerColumn.class);
    }

    public DecimalColumn getDecimalColumn() {
      return getColumnSet().getColumnByClass(DecimalColumn.class);
    }

    public TextColumn getTextColumn() {
      return getColumnSet().getColumnByClass(TextColumn.class);
    }

    public AdditionalInformationUidColumn getAdditionalInformationUidColumn() {
      return getColumnSet().getColumnByClass(AdditionalInformationUidColumn.class);
    }

    public MandatoryColumn getMandatoryColumn() {
      return getColumnSet().getColumnByClass(MandatoryColumn.class);
    }

    @Order(10.0)
    public class AdditionalInformationUidColumn extends AbstractSmartColumn<Long> {

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return AdditionalInformationCodeType.class;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return ScoutTexts.get("Info");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }
    }

    @Order(20.0)
    public class TypeColumn extends AbstractSmartColumn<Long> {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Type");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return AdditionalInformationTypeCodeType.class;

      }
    }

    @Order(50.0)
    public class ValueColumn extends AbstractStringColumn {

      public class BooleanSmartfield extends AbstractSmartField<Boolean> {
        @Override
        protected Class<? extends ICodeType<Long, Boolean>> getConfiguredCodeType() {
          return BooleanCodeType.class;
        }
      }

      public class TextField extends AbstractStringField {

      }

      public class IntegerField extends AbstractLongField {

      }

      public class DoubleField extends AbstractBigDecimalField {

      }

      public class SmartField extends AbstractSmartField<Long> {

        @Override
        protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
          return AdditionalInformationLookupCall.class;
        }

      }

      @Override
      protected boolean getConfiguredEditable() {
        return true;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return ScoutTexts.get("Value");
      }

      @Override
      protected int getConfiguredWidth() {
        return 150;
      }

      @Override
      protected IFormField execPrepareEdit(ITableRow row) throws ProcessingException {
        IFormField field = null;
        if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.BooleanCode.ID)) {
          // Boolean
          BooleanSmartfield booleanSmartfield = new BooleanSmartfield();
          booleanSmartfield.setValue(CompareUtility.equals(1L, getIntegerColumn().getValue(row)));
          field = booleanSmartfield;
        }
        else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.IntegerCode.ID)) {
          // Number
          IntegerField integerField = new IntegerField();
          if (getMaximumColumn().getValue(row) != null) {
            integerField.setMaxValue(Math.round(getMaximumColumn().getValue(row).doubleValue()));
          }
          if (getMinimumColumn().getValue(row) != null) {
            integerField.setMinValue(Math.round(getMinimumColumn().getValue(row).doubleValue()));
          }
          integerField.setValue(getIntegerColumn().getValue(row));
          field = integerField;
        }
        else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.DoubleCode.ID)) {
          // Decimal
          DoubleField doubleField = new DoubleField();
          doubleField.setMaxValue(getMaximumColumn().getValue(row));
          doubleField.setMinValue(getMinimumColumn().getValue(row));
          doubleField.setValue(getDecimalColumn().getValue(row));
          field = doubleField;
        }
        else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.TextCode.ID)) {
          // Text
          TextField textField = new TextField();
          if (getMaximumColumn().getValue(row) != null) {
            textField.setMaxLength((int) Math.round(getMaximumColumn().getValue(row).doubleValue()));
          }
          if (textField.getMaxLength() > 250) {
            textField.setMaxLength(250);
          }
          textField.setValue(getTextColumn().getValue(row));
          field = textField;
        }
        else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.SmartfieldCode.ID)) {
          // Smartfield
          SmartField smartField = new SmartField();
          ILookupCall call = smartField.getLookupCall();
          ((AdditionalInformationLookupCall) call).setParentUid(getAdditionalInformationUidColumn().getValue(row));
          smartField.setValue(getIntegerColumn().getValue(row));
          field = smartField;
        }
        else {
          throw new ProcessingException("Undefined AdditionalInformationTypeCodeType");
        }

        // Mandatory
        if (BooleanUtility.nvl(getMandatoryColumn().getValue(row), false)) {
          field.setMandatory(true);
        }

        return field;
      }

      @Override
      protected void execCompleteEdit(ITableRow row, IFormField editingField) throws ProcessingException {
        if (!editingField.isContentValid()) {
          if (editingField.getErrorStatus() != null) {
            throw new VetoException(editingField.getErrorStatus().getMessage());
          }
          else if (editingField.isMandatory()) {
            throw new VetoException(Texts.get("MandatoryField"));
          }
        }

        if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.BooleanCode.ID)) {
          Boolean value = ((BooleanSmartfield) editingField).getValue();
          if (value == null) {
            getIntegerColumn().setValue(row, null);
          }
          else {
            getIntegerColumn().setValue(row, value ? 1L : 0L);
          }
        }
        else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.IntegerCode.ID)) {
          getIntegerColumn().setValue(row, ((IntegerField) editingField).getValue());
        }
        else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.DoubleCode.ID)) {
          getDecimalColumn().setValue(row, ((DoubleField) editingField).getValue());
        }
        else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.TextCode.ID)) {
          getTextColumn().setValue(row, ((TextField) editingField).getValue());
        }
        else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.SmartfieldCode.ID)) {
          Long value = ((SmartField) editingField).getValue();
          getIntegerColumn().setValue(row, value);
        }
        else {
          throw new ProcessingException("Undefined AdditionalInformationTypeCodeType");
        }

        handleEditCompleted(row);
      }

    }

    @Override
    protected void execDecorateRow(ITableRow row) throws ProcessingException {
      // Mandatory
      if (BooleanUtility.nvl(getMandatoryColumn().getValue(row), false)) {
        row.setFont(FontSpec.parse("bold"));
      }

      // Value
      if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.BooleanCode.ID)) {
        Long value = getIntegerColumn().getValue(row);
        if (value == null) {
          getValueColumn().setValue(row, null);
        }
        else {
          getValueColumn().setValue(row, CODES.getCodeType(BooleanCodeType.class).getCode(value == 1).getText());
        }
      }
      else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.IntegerCode.ID)) {
        Long value = getIntegerColumn().getValue(row);
        if (value == null) {
          getValueColumn().setValue(row, null);
        }
        else {
          getValueColumn().setValue(row, NumberUtility.format(value));
        }
      }
      else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.DoubleCode.ID)) {
        BigDecimal value = getDecimalColumn().getValue(row);
        if (value == null) {
          getValueColumn().setValue(row, null);
        }
        else {
          getValueColumn().setValue(row, NumberUtility.format(value));
        }
      }
      else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.TextCode.ID)) {
        getValueColumn().setValue(row, getTextColumn().getValue(row));
      }
      else if (CompareUtility.equals(getTypeColumn().getValue(row), AdditionalInformationTypeCodeType.SmartfieldCode.ID)) {
        Long value = getIntegerColumn().getValue(row);
        if (value == null) {
          getValueColumn().setValue(row, null);
        }
        else {
          ICode code = CODES.getCodeType(AdditionalInformationCodeType.class).getCode(value);
          getValueColumn().setValue(row, code != null ? code.getText() : String.valueOf(value));
        }
      }
      else {
        throw new ProcessingException("Undefined AdditionalInformationTypeCodeType");
      }
    }

    @Order(60.0)
    public class IntegerColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Integer");
      }
    }

    @Order(70.0)
    public class DecimalColumn extends AbstractBigDecimalColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Double");
      }
    }

    @Order(80.0)
    public class TextColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Text");
      }
    }

    @Order(90.0)
    public class MinimumColumn extends AbstractBigDecimalColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Minimum");
      }
    }

    @Order(100.0)
    public class MaximumColumn extends AbstractBigDecimalColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("Maximum");
      }
    }

    @Order(110.0)
    public class FeeGroupColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

      @Override
      protected String getConfiguredHeaderText() {
        return Texts.get("FeeGroup");
      }
    }

    @Order(120.0)
    public class DefaultValueIntegerColumn extends AbstractLongColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

    @Order(130.0)
    public class DefaultValueDecimalColumn extends AbstractBigDecimalColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

    @Order(140.0)
    public class DefaultValueTextColumn extends AbstractStringColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

    @Order(150.0)
    public class MandatoryColumn extends AbstractBooleanColumn {

      @Override
      protected boolean getConfiguredDisplayable() {
        return false;
      }

    }

  }
}
