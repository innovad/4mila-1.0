package com.rtiming.client.entry;

import java.util.List;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.DefaultSubtypeSdkCommand;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.shared.Texts;
import com.rtiming.shared.entry.AbstractRegistrationSearchBoxData;

@FormData(value = AbstractRegistrationSearchBoxData.class, defaultSubtypeSdkCommand = DefaultSubtypeSdkCommand.CREATE, sdkCommand = SdkCommand.CREATE)
public abstract class AbstractRegistrationSearchBox extends AbstractGroupBox {

  @Override
  public String getConfiguredLabel() {
    return Texts.get("Registrations");
  }

  @Order(20.0)
  public class NumberField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return ScoutTexts.get("Number");
    }
  }

  @Order(30.0)
  public class EvtRegistrationBox extends AbstractSequenceBox {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Date");
    }

    @Order(10.0)
    public class EvtRegistrationFrom extends AbstractDateField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("from");
      }
    }

    @Order(20.0)
    public class EvtRegistrationTo extends AbstractDateField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("to");
      }
    }
  }

  @Order(40.0)
  public class StartlistSettingOptionGroup extends AbstractStartlistSettingOptionGroupBox {

    @Override
    protected void execInitField() throws ProcessingException {
      super.execInitField();
      setValue(0L);
    }

    @Override
    protected void execFilterLookupResult(ILookupCall<Long> call, List<ILookupRow<Long>> result) {
      super.execFilterLookupResult(call, result);
      result.add(0, new LookupRow(0L, Texts.get("All")));
    }

  }
}
