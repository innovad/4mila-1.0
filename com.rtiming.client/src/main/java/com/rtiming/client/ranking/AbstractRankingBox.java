package com.rtiming.client.ranking;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;

import com.rtiming.client.ranking.AbstractRankingBox.FormatBox.DecimalPlacesField;
import com.rtiming.client.ranking.AbstractRankingBox.FormatBox.FormatField;
import com.rtiming.client.ranking.AbstractRankingBox.FormatBox.SortingField;
import com.rtiming.client.ranking.AbstractRankingBox.FormatBox.TimePrecisionField;
import com.rtiming.client.ranking.AbstractRankingBox.FormulaBox.FormulaField;
import com.rtiming.client.ranking.AbstractRankingBox.FormulaBox.FormulaTypeField;
import com.rtiming.shared.entry.startlist.BibNoOrderCodeType;
import com.rtiming.shared.race.TimePrecisionCodeType;
import com.rtiming.shared.ranking.AbstractFormulaCode;
import com.rtiming.shared.ranking.AbstractFormulaCode.RankingType;
import com.rtiming.shared.ranking.AbstractRankingBoxData;
import com.rtiming.shared.ranking.FormulaUtility;
import com.rtiming.shared.ranking.RankingFormatCodeType;
import com.rtiming.shared.ranking.RankingFormulaTypeCodeType;

@FormData(value = AbstractRankingBoxData.class, sdkCommand = SdkCommand.CREATE, defaultSubtypeSdkCommand = FormData.DefaultSubtypeSdkCommand.CREATE)
public abstract class AbstractRankingBox extends AbstractGroupBox {

  protected abstract RankingType getConfiguredRankingType();

  @Override
  protected boolean getConfiguredBorderVisible() {
    return false;
  }

  @Order(10.0)
  public class FormatBox extends AbstractGroupBox {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Ranking");
    }

    @Order(10.0)
    public class FormatField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Format");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return RankingFormatCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        validateRankingFormat();
      }
    }

    @Order(20.0)
    public class DecimalPlacesField extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("DecimalPlaces");
      }

      @Override
      protected Long getConfiguredMaxValue() {
        return 5L;
      }

      @Override
      protected Long getConfiguredMinValue() {
        return 0L;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        if (!getTimePrecisionField().isValueChanging()) {
          getTimePrecisionField().setValue(FormulaUtility.decimalPlaces2timePrecision(getDecimalPlacesField().getValue()));
        }
      }
    }

    @Order(30.0)
    public class TimePrecisionField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("TimePrecision");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return TimePrecisionCodeType.class;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        if (!getDecimalPlacesField().isValueChanging()) {
          getDecimalPlacesField().setValue(FormulaUtility.timePrecision2decimalPlaces(getTimePrecisionField().getValue()));
        }
      }
    }

    @Order(40.0)
    public class SortingField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Sorting");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return BibNoOrderCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }
  }

  @Order(20.0)
  public class FormulaBox extends AbstractGroupBox {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("Formula");
    }

    @Order(10.0)
    public class FormulaTypeField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Formula");
      }

      @Override
      protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
        return RankingFormulaTypeCodeType.class;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected void execFilterLookupResult(ILookupCall<Long> call, List<ILookupRow<Long>> result) {
        RankingType type = getConfiguredRankingType();
        List<ILookupRow<Long>> filtered = new ArrayList<ILookupRow<Long>>();
        for (ILookupRow<Long> row : result) {
          ICode code = BEANS.get(RankingFormulaTypeCodeType.class).getCode(row.getKey());
          AbstractFormulaCode formulaCode = (AbstractFormulaCode) code;
          if (formulaCode.getRankingType() == null || formulaCode.getRankingType().equals(type)) {
            filtered.add(row);
          }
        }
        result.clear();
        result.addAll(filtered);
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        updateDefaultFormulaValues();
      }

    }

    @Order(20.0)
    public class FormulaField extends AbstractStringField {

      @Override
      protected int getConfiguredGridH() {
        return 8;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Formula");
      }

      @Override
      protected boolean getConfiguredLabelVisible() {
        return false;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 10000;
      }

      @Override
      protected boolean getConfiguredMultilineText() {
        return true;
      }

      @Override
      protected boolean getConfiguredWrapText() {
        return true;
      }
    }
  }

  public void updateDefaultFormulaValues() {
    ICode<?> code = BEANS.get(RankingFormulaTypeCodeType.class).getCode(getFormulaTypeField().getValue());
    if (code != null && code instanceof AbstractFormulaCode) {
      AbstractFormulaCode formulaCode = (AbstractFormulaCode) code;
      // Formula
      String formula = formulaCode.getFormula();
      if (!StringUtility.isNullOrEmpty(formula)) {
        // do not overwrite formula in case of changing to custom formula
        getFormulaField().setValue(formula);
      }
      // Format
      Long formatUid = formulaCode.getFormatUid();
      if (formatUid != null) {
        getFormatField().setValue(formatUid);
      }
      // Sorting
      Long sortingUid = formulaCode.getSortingUid();
      if (sortingUid != null) {
        getSortingField().setValue(sortingUid);
      }
      // Decimal Places
      Long decimalPlaces = formulaCode.getDecimalPlaces();
      if (decimalPlaces != null) {
        if (CompareUtility.equals(formatUid, RankingFormatCodeType.TimeCode.ID)) {
          getTimePrecisionField().setValue(FormulaUtility.decimalPlaces2timePrecision(decimalPlaces));
        }
        else {
          getDecimalPlacesField().setValue(decimalPlaces);
        }
      }
    }
  }

  public void validateRankingFormat() {
    boolean isTimeFormat = CompareUtility.equals(getFormatField().getValue(), RankingFormatCodeType.TimeCode.ID);

    getDecimalPlacesField().setVisible(!isTimeFormat);
    getDecimalPlacesField().setMandatory(!isTimeFormat);

    getTimePrecisionField().setVisible(isTimeFormat);
    getTimePrecisionField().setMandatory(isTimeFormat);
  }

  public FormatField getFormatField() {
    return getFieldByClass(FormatField.class);
  }

  public FormulaBox getFormulaBox() {
    return getFieldByClass(FormulaBox.class);
  }

  public TimePrecisionField getTimePrecisionField() {
    return getFieldByClass(TimePrecisionField.class);
  }

  public DecimalPlacesField getDecimalPlacesField() {
    return getFieldByClass(DecimalPlacesField.class);
  }

  public FormulaField getFormulaField() {
    return getFieldByClass(FormulaField.class);
  }

  public SortingField getSortingField() {
    return getFieldByClass(SortingField.class);
  }

  public FormulaTypeField getFormulaTypeField() {
    return getFieldByClass(FormulaTypeField.class);
  }

}
