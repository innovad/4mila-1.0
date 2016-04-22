package com.rtiming.client.settings.addinfo;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.DefaultSubtypeSdkCommand;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.AbstractDoubleField;
import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractCodeBox;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.CancelButton;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.CodeBox;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.DefaultValueBooleanField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.DefaultValueDecimalField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.DefaultValueIntegerField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.DefaultValueSmartfieldField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.DefaultValueTextField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.EntityField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.FeeGroupField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MandatoryField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MaximumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MinimumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.OkButton;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.SmartfieldField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.TypeField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.BooleanCodeType;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.security.permission.UpdateAdditionalInformationAdministrationPermission;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationLookupCall;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;
import com.rtiming.shared.settings.fee.FeeGroupLookupCall;

@FormData(value = AdditionalInformationAdministrationFormData.class, sdkCommand = SdkCommand.CREATE, defaultSubtypeSdkCommand = DefaultSubtypeSdkCommand.CREATE)
public class AdditionalInformationAdministrationForm extends AbstractForm {

  private Long m_additionalInformationUid;

  public AdditionalInformationAdministrationForm() throws ProcessingException {
    super();
  }

  @Override
  protected void execInitForm() throws ProcessingException {
    if (getSmartfieldField().getValue() == null) {
      // standard type
      getSmartfieldField().setVisible(false);
      getSmartfieldField().setMandatory(false);
      getCodeBox().getShortcutField().setMandatory(true);
      getCodeBox().getShortcutField().setMaxLength(20);
      getCodeBox().getShortcutField().setFormatLower();
    }
    else {
      // child of smartfield type
      getSmartfieldField().setEnabled(false);
      getTypeField().setValue(AdditionalInformationTypeCodeType.SmartfieldCode.ID);
      getTypeField().setEnabled(false);
      getMaximumField().setVisible(false);
      getMinimumField().setVisible(false);
      getEntityField().setMandatory(false);
      getEntityField().setVisible(false);
      getMandatoryField().setVisible(false);
    }
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("AdditionalInformation");
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public CodeBox getCodeBox() {
    return getFieldByClass(CodeBox.class);
  }

  public MandatoryField getMandatoryField() {
    return getFieldByClass(MandatoryField.class);
  }

  public MaximumField getMaximumField() {
    return getFieldByClass(MaximumField.class);
  }

  public MinimumField getMinimumField() {
    return getFieldByClass(MinimumField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public SmartfieldField getSmartfieldField() {
    return getFieldByClass(SmartfieldField.class);
  }

  public TypeField getTypeField() {
    return getFieldByClass(TypeField.class);
  }

  public DefaultValueBooleanField getDefaultValueBooleanField() {
    return getFieldByClass(DefaultValueBooleanField.class);
  }

  public DefaultValueDecimalField getDefaultValueDecimalField() {
    return getFieldByClass(DefaultValueDecimalField.class);
  }

  public DefaultValueIntegerField getDefaultValueIntegerField() {
    return getFieldByClass(DefaultValueIntegerField.class);
  }

  public DefaultValueTextField getDefaultValueTextField() {
    return getFieldByClass(DefaultValueTextField.class);
  }

  public DefaultValueSmartfieldField getDefaultValueSmartfieldField() {
    return getFieldByClass(DefaultValueSmartfieldField.class);
  }

  public EntityField getEntityField() {
    return getFieldByClass(EntityField.class);
  }

  public FeeGroupField getFeeGroupField() {
    return getFieldByClass(FeeGroupField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class EntityField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Entity");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return EntityCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(20.0)
    public class TypeField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Type");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return AdditionalInformationTypeCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        if (getSmartfieldField().getValue() == null) {
          // default values
          getDefaultValueIntegerField().setVisible(CompareUtility.equals(getValue(), AdditionalInformationTypeCodeType.IntegerCode.ID));
          getDefaultValueDecimalField().setVisible(CompareUtility.equals(getValue(), AdditionalInformationTypeCodeType.DoubleCode.ID));
          getDefaultValueTextField().setVisible(CompareUtility.equals(getValue(), AdditionalInformationTypeCodeType.TextCode.ID));
          getDefaultValueBooleanField().setVisible(CompareUtility.equals(getValue(), AdditionalInformationTypeCodeType.BooleanCode.ID));
          boolean isSmartfieldType = CompareUtility.equals(getValue(), AdditionalInformationTypeCodeType.SmartfieldCode.ID);
          getDefaultValueSmartfieldField().setVisible(isSmartfieldType);

          // special handling for smartfield: default values are not possible in new handler
          // (no default values yet, all values would be shown)
          boolean isNewAndSmartfield = isSmartfieldType && getAdditionalInformationUid() == null;
          if (isNewAndSmartfield) {
            getDefaultValueSmartfieldField().setEnabled(false);
          }

          // min, max
          boolean isParentSmartfieldType = CompareUtility.equals(getTypeField().getValue(), AdditionalInformationTypeCodeType.SmartfieldCode.ID);
          boolean isBooleanType = CompareUtility.equals(getTypeField().getValue(), AdditionalInformationTypeCodeType.BooleanCode.ID);
          boolean enabled = !isParentSmartfieldType && !isBooleanType;
          getMinimumField().setEnabled(enabled);
          getMaximumField().setEnabled(enabled);

          // fee group
          getFeeGroupField().setEnabled(!isParentSmartfieldType);
          if (!getFeeGroupField().isEnabled()) {
            getFeeGroupField().setValue(null);
          }
        }
      }

    }

    @Order(30.0)
    public class SmartfieldField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Smartfield");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return AdditionalInformationCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(40.0)
    public class DefaultValueIntegerField extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("DefaultValue");
      }

      @Override
      protected Long getConfiguredMaxValue() {
        return 999999999L;
      }

      @Override
      protected Long getConfiguredMinValue() {
        return -999999999L;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(50.0)
    public class DefaultValueDecimalField extends AbstractDoubleField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("DefaultValue");
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(60.0)
    public class DefaultValueTextField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("DefaultValue");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(70.0)
    public class DefaultValueBooleanField extends AbstractSmartField<Boolean> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("DefaultValue");
      }

      @Override
      protected Class<? extends ICodeType<Long, Boolean>> getConfiguredCodeType() {
        return BooleanCodeType.class;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }
    }

    @Order(80.0)
    public class DefaultValueSmartfieldField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("DefaultValue");
      }

      @Override
      protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
        ((AdditionalInformationLookupCall) call).setParentUid(getAdditionalInformationUid());
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return AdditionalInformationLookupCall.class;
      }

      @Override
      protected boolean getConfiguredVisible() {
        return false;
      }

    }

    @Order(90.0)
    public class FeeGroupField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("FeeGroup");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return FeeGroupLookupCall.class;
      }
    }

    @Order(100.0)
    public class MinimumField extends AbstractDoubleField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Minimum");
      }

    }

    @Order(110.0)
    public class MaximumField extends AbstractDoubleField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Maximum");
      }

    }

    @Order(120.0)
    public class MandatoryField extends AbstractBooleanField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Mandatory");
      }
    }

    @Order(130.0)
    public class CodeBox extends AbstractCodeBox {

      @Override
      protected boolean getConfiguredBorderVisible() {
        return false;
      }

    }

    @Order(138.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(140.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(150.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IAdditionalInformationAdministrationProcessService service = BEANS.get(IAdditionalInformationAdministrationProcessService.class);
      AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);
      setEnabledPermission(new UpdateAdditionalInformationAdministrationPermission());

      getEntityField().setEnabled(false);
      getTypeField().setEnabled(false);
      getTypeField().execChangedValue();
    }

    @Override
    public void execStore() throws ProcessingException {
      IAdditionalInformationAdministrationProcessService service = BEANS.get(IAdditionalInformationAdministrationProcessService.class);
      AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IAdditionalInformationAdministrationProcessService service = BEANS.get(IAdditionalInformationAdministrationProcessService.class);
      AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IAdditionalInformationAdministrationProcessService service = BEANS.get(IAdditionalInformationAdministrationProcessService.class);
      AdditionalInformationAdministrationFormData formData = new AdditionalInformationAdministrationFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }

  @FormData
  public Long getAdditionalInformationUid() {
    return m_additionalInformationUid;
  }

  @FormData
  public void setAdditionalInformationUid(Long additionalInformationUid) {
    m_additionalInformationUid = additionalInformationUid;
  }
}
