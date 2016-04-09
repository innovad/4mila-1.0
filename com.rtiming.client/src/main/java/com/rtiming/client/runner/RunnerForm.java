package com.rtiming.client.runner;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.booleanfield.AbstractBooleanField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.datefield.AbstractDateField;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;

import com.rtiming.client.club.AbstractClubField;
import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractYearField;
import com.rtiming.client.ecard.AbstractECardField;
import com.rtiming.client.runner.RunnerForm.MainBox.ActiveField;
import com.rtiming.client.runner.RunnerForm.MainBox.BirthdateField;
import com.rtiming.client.runner.RunnerForm.MainBox.CancelButton;
import com.rtiming.client.runner.RunnerForm.MainBox.ClubField;
import com.rtiming.client.runner.RunnerForm.MainBox.DefaultClazzField;
import com.rtiming.client.runner.RunnerForm.MainBox.ECardField;
import com.rtiming.client.runner.RunnerForm.MainBox.ExtKeyField;
import com.rtiming.client.runner.RunnerForm.MainBox.FirstNameField;
import com.rtiming.client.runner.RunnerForm.MainBox.LastNameField;
import com.rtiming.client.runner.RunnerForm.MainBox.NationUidField;
import com.rtiming.client.runner.RunnerForm.MainBox.OkButton;
import com.rtiming.client.runner.RunnerForm.MainBox.SexField;
import com.rtiming.client.runner.RunnerForm.MainBox.TabBox;
import com.rtiming.client.runner.RunnerForm.MainBox.TabBox.AdditionalInformationBox;
import com.rtiming.client.runner.RunnerForm.MainBox.TabBox.AdditionalInformationBox.AdditionalInformationField;
import com.rtiming.client.runner.RunnerForm.MainBox.TabBox.AddressBox;
import com.rtiming.client.runner.RunnerForm.MainBox.YearField;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationField;
import com.rtiming.client.settings.city.AbstractCountryField;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.security.permission.UpdateRunnerPermission;
import com.rtiming.shared.event.course.ClassCodeType;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.runner.RunnerFormData;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.city.AbstractAddressBoxData;

@FormData(value = RunnerFormData.class, sdkCommand = SdkCommand.CREATE)
public class RunnerForm extends AbstractForm {

  private Long runnerNr;
  private Long addressNr;
  private Long m_clientNr;

  public RunnerForm() throws ProcessingException {
    super();
  }

  @Override
  protected boolean execValidate() throws ProcessingException {
    return getAdditionalInformationField().execValidate();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Runner");
  }

  @FormData
  public Long getAddressNr() {
    return addressNr;
  }

  @FormData
  public void setAddressNr(Long addressNr) {
    this.addressNr = addressNr;
  }

  @FormData
  public Long getRunnerNr() {
    return runnerNr;
  }

  @FormData
  public void setRunnerNr(Long runnerNr) {
    this.runnerNr = runnerNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public ActiveField getActiveField() {
    return getFieldByClass(ActiveField.class);
  }

  public AdditionalInformationBox getAdditionalInformationBox() {
    return getFieldByClass(AdditionalInformationBox.class);
  }

  public AddressBox getAddressBox() {
    return getFieldByClass(AddressBox.class);
  }

  public BirthdateField getBirthdateField() {
    return getFieldByClass(BirthdateField.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public ClubField getClubField() {
    return getFieldByClass(ClubField.class);
  }

  public DefaultClazzField getDefaultClazzField() {
    return getFieldByClass(DefaultClazzField.class);
  }

  public ECardField getECardField() {
    return getFieldByClass(ECardField.class);
  }

  public AdditionalInformationField getAdditionalInformationField() {
    return getFieldByClass(AdditionalInformationField.class);
  }

  public ExtKeyField getExtKeyField() {
    return getFieldByClass(ExtKeyField.class);
  }

  public FirstNameField getFirstNameField() {
    return getFieldByClass(FirstNameField.class);
  }

  public LastNameField getLastNameField() {
    return getFieldByClass(LastNameField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NationUidField getNationUidField() {
    return getFieldByClass(NationUidField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public SexField getSexField() {
    return getFieldByClass(SexField.class);
  }

  public TabBox getTabBox() {
    return getFieldByClass(TabBox.class);
  }

  public YearField getYearField() {
    return getFieldByClass(YearField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class FirstNameField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("FirstName");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }
    }

    @Order(20.0)
    public class LastNameField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("LastName");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }
    }

    @Order(30.0)
    public class ExtKeyField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Number");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 60;
      }
    }

    @Order(40.0)
    public class ECardField extends AbstractECardField {

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

    @Order(60.0)
    public class ClubField extends AbstractClubField {

    }

    @Order(70.0)
    public class NationUidField extends AbstractCountryField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Nation");
      }

    }

    @Order(80.0)
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

    @Order(90.0)
    public class BirthdateField extends AbstractDateField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Birthdate");
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        getYearField().setValue(FMilaUtility.getYearFromDate(getBirthdateField().getValue()));
        getYearField().setEnabled(getBirthdateField().getValue() == null);
      }
    }

    @Order(100.0)
    public class YearField extends AbstractYearField {
    }

    @Order(110.0)
    public class ActiveField extends AbstractBooleanField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Active");
      }
    }

    @Order(120.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      @FormData(value = AbstractAddressBoxData.class, sdkCommand = SdkCommand.CREATE)
      public class AddressBox extends AbstractAddressBox {

      }

      @Order(20.0)
      public class AdditionalInformationBox extends AbstractGroupBox {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("AdditionalInformation");
        }

        @Order(10.0)
        public class AdditionalInformationField extends AbstractAdditionalInformationField {

          @Override
          protected void handleEditCompleted(ITableRow row) throws ProcessingException {
            // nop
          }

        }
      }
    }

    @Order(128.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(130.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(140.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IRunnerProcessService service = BEANS.get(IRunnerProcessService.class);
      RunnerFormData formData = new RunnerFormData();
      exportFormData(formData);
      formData = BeanUtility.runnerBean2formData(service.load(BeanUtility.runnerFormData2bean(formData)));
      importFormData(formData);
      setEnabledPermission(new UpdateRunnerPermission());

      getYearField().setEnabled(getBirthdateField().getValue() == null);
    }

    @Override
    public void execStore() throws ProcessingException {
      IRunnerProcessService service = BEANS.get(IRunnerProcessService.class);
      RunnerFormData formData = new RunnerFormData();
      exportFormData(formData);
      formData = BeanUtility.runnerBean2formData(service.store(BeanUtility.runnerFormData2bean(formData)));
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IRunnerProcessService service = BEANS.get(IRunnerProcessService.class);
      RunnerFormData formData = new RunnerFormData();
      exportFormData(formData);
      formData = BeanUtility.runnerBean2formData(service.prepareCreate(BeanUtility.runnerFormData2bean(formData)));
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IRunnerProcessService service = BEANS.get(IRunnerProcessService.class);
      RunnerFormData formData = new RunnerFormData();
      exportFormData(formData);
      formData = BeanUtility.runnerBean2formData(service.create(BeanUtility.runnerFormData2bean(formData)));
      importFormData(formData);
    }
  }

  @FormData
  public Long getClientNr() {
    return m_clientNr;
  }

  @FormData
  public void setClientNr(Long clientNr) {
    m_clientNr = clientNr;
  }
}
