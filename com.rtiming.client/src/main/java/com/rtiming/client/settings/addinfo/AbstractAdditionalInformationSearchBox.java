package com.rtiming.client.settings.addinfo;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.DefaultSubtypeSdkCommand;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.labelfield.AbstractLabelField;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.sequencebox.AbstractSequenceBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationSearchBox.DecimalBox.DecimalFrom;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationSearchBox.DecimalBox.DecimalTo;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationSearchBox.IntegerBox.IntegerFrom;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationSearchBox.IntegerBox.IntegerTo;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.BooleanCodeType;
import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationSearchBoxData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationLookupCall;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;

@FormData(value = AbstractAdditionalInformationSearchBoxData.class, defaultSubtypeSdkCommand = DefaultSubtypeSdkCommand.CREATE, sdkCommand = SdkCommand.CREATE)
public abstract class AbstractAdditionalInformationSearchBox extends AbstractGroupBox {

  public AdditionalInformationField getAdditionalInformationField() {
    return getFieldByClass(AdditionalInformationField.class);
  }

  public BooleanField getBooleanField() {
    return getFieldByClass(BooleanField.class);
  }

  public DecimalBox getDecimalBox() {
    return getFieldByClass(DecimalBox.class);
  }

  public DecimalFrom getDecimalFrom() {
    return getFieldByClass(DecimalFrom.class);
  }

  public DecimalTo getDecimalTo() {
    return getFieldByClass(DecimalTo.class);
  }

  public EmptyField getEmptyField() {
    return getFieldByClass(EmptyField.class);
  }

  public IntegerBox getIntegerBox() {
    return getFieldByClass(IntegerBox.class);
  }

  public IntegerFrom getIntegerFrom() {
    return getFieldByClass(IntegerFrom.class);
  }

  public IntegerTo getIntegerTo() {
    return getFieldByClass(IntegerTo.class);
  }

  public SmartfieldField getSmartfieldField() {
    return getFieldByClass(SmartfieldField.class);
  }

  public TextField getTextField() {
    return getFieldByClass(TextField.class);
  }

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("AdditionalInformation");
  }

  protected abstract Long getEntityUid();

  @Order(10.0)
  public class AdditionalInformationField extends AbstractSmartField<Long> {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("AdditionalInformation");
    }

    @Override
    protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
      return AdditionalInformationLookupCall.class;
    }

    @Override
    protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
      ((AdditionalInformationLookupCall) call).setEntityUid(getEntityUid());
    }

    @Override
    protected void execChangedValue() throws ProcessingException {

      // hide all fields
      getIntegerBox().setVisible(false);
      getDecimalBox().setVisible(false);
      getTextField().setVisible(false);
      getSmartfieldField().setVisible(false);
      getBooleanField().setVisible(false);

      if (getValue() != null) {
        AdditionalInformationAdministrationFormData config = new AdditionalInformationAdministrationFormData();
        config.setAdditionalInformationUid(getValue());
        config = BEANS.get(IAdditionalInformationAdministrationProcessService.class).load(config);

        if (config.getType().getValue() == AdditionalInformationTypeCodeType.IntegerCode.ID) {
          getIntegerBox().setVisible(true);
        }
        else if (config.getType().getValue() == AdditionalInformationTypeCodeType.DoubleCode.ID) {
          getDecimalBox().setVisible(true);
        }
        else if (config.getType().getValue() == AdditionalInformationTypeCodeType.TextCode.ID) {
          getTextField().setVisible(true);
        }
        else if (config.getType().getValue() == AdditionalInformationTypeCodeType.SmartfieldCode.ID) {
          getSmartfieldField().setVisible(true);
          ((AdditionalInformationLookupCall) getSmartfieldField().getLookupCall()).setParentUid(getValue());
        }
        else if (config.getType().getValue() == AdditionalInformationTypeCodeType.BooleanCode.ID) {
          getBooleanField().setVisible(true);
        }
      }
    }

  }

  @Order(20.0)
  public class EmptyField extends AbstractLabelField {
  }

  @Order(30.0)
  public class IntegerBox extends AbstractSequenceBox {

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Integer");
    }

    @Order(10.0)
    public class IntegerFrom extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("from");
      }
    }

    @Order(20.0)
    public class IntegerTo extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("to");
      }
    }
  }

  @Order(40.0)
  public class DecimalBox extends AbstractSequenceBox {

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Double");
    }

    @Order(10.0)
    public class DecimalFrom extends AbstractBigDecimalField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("from");
      }
    }

    @Order(20.0)
    public class DecimalTo extends AbstractBigDecimalField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("to");
      }
    }
  }

  @Order(50.0)
  public class TextField extends AbstractStringField {

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Text");
    }
  }

  @Order(60.0)
  public class SmartfieldField extends AbstractSmartField<Long> {

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Smartfield");
    }

    @Override
    protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
      return AdditionalInformationLookupCall.class;
    }
  }

  @Order(70.0)
  public class BooleanField extends AbstractSmartField<Boolean> {

    @Override
    protected boolean getConfiguredVisible() {
      return false;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Boolean");
    }

    @Override
    protected Class<? extends ICodeType<Long, Boolean>> getConfiguredCodeType() {
      return BooleanCodeType.class;
    }
  }
}
