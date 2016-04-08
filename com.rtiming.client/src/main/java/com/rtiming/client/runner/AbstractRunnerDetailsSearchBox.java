package com.rtiming.client.runner;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.DefaultSubtypeSdkCommand;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.runner.AbstractRunnerDetailsSearchBox.YearBox.YearFrom;
import com.rtiming.client.runner.AbstractRunnerDetailsSearchBox.YearBox.YearTo;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.runner.AbstractRunnerDetailsSearchBoxData;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.city.CountryCodeType;

@FormData(value = AbstractRunnerDetailsSearchBoxData.class, defaultSubtypeSdkCommand = DefaultSubtypeSdkCommand.CREATE, sdkCommand = SdkCommand.CREATE)
public abstract class AbstractRunnerDetailsSearchBox extends AbstractGroupBox {

  public YearFrom getYearFrom() {
    return getFieldByClass(YearFrom.class);
  }

  public YearTo getYearTo() {
    return getFieldByClass(YearTo.class);
  }

  @Override
  protected String getConfiguredLabel() {
    return ScoutTexts.get("Details");
  }

  @Order(10.0)
  public class NationUidField extends AbstractSmartField<Long> {

    @Override
    protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
      return CountryCodeType.class;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Nation");
    }
  }

  @Order(20.0)
  public class SexField extends AbstractSmartField<Long> {

    @Override
    protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
      return SexCodeType.class;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Sex");
    }
  }

  @Order(30.0)
  public class BirthdateBox extends AbstractSequenceBox {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Birthdate");
    }

    @Order(10.0)
    public class BirthdateFrom extends AbstractDateField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("from");
      }
    }

    @Order(20.0)
    public class BirthdateTo extends AbstractDateField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("to");
      }
    }
  }

  @Order(40.0)
  public class YearBox extends AbstractSequenceBox {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Year");
    }

    @Order(10.0)
    public class YearFrom extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("from");
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        getYearTo().setValue(getYearFrom().getValue());
      }
    }

    @Order(20.0)
    public class YearTo extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("to");
      }
    }
  }

  @Order(50.0)
  public class DefaultClazzField extends AbstractSmartField<Long> {

    @Override
    protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
      return ClassCodeType.class;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("DefaultClazz");
    }
  }
}
