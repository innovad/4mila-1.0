package com.rtiming.client.club;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.ScoutTexts;

import com.rtiming.client.club.ClubForm.MainBox.CancelButton;
import com.rtiming.client.club.ClubForm.MainBox.ContactRunnerField;
import com.rtiming.client.club.ClubForm.MainBox.ExtKeyField;
import com.rtiming.client.club.ClubForm.MainBox.NameField;
import com.rtiming.client.club.ClubForm.MainBox.OkButton;
import com.rtiming.client.club.ClubForm.MainBox.ShortcutField;
import com.rtiming.client.club.ClubForm.MainBox.TabBox;
import com.rtiming.client.club.ClubForm.MainBox.TabBox.AdditionalInformationBox;
import com.rtiming.client.club.ClubForm.MainBox.TabBox.AdditionalInformationBox.AdditionalInformationField;
import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.runner.AbstractRunnerField;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationField;
import com.rtiming.shared.Texts;
import com.rtiming.shared.club.ClubFormData;
import com.rtiming.shared.club.IClubProcessService;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.security.permission.UpdateClubPermission;

@FormData(value = ClubFormData.class, sdkCommand = SdkCommand.CREATE)
public class ClubForm extends AbstractForm {

  private Long clubNr;

  public ClubForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Club");
  }

  public AdditionalInformationBox getAdditionalInformationBox() {
    return getFieldByClass(AdditionalInformationBox.class);
  }

  public AdditionalInformationField getAdditionalInformationField() {
    return getFieldByClass(AdditionalInformationField.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getClubNr() {
    return clubNr;
  }

  @FormData
  public void setClubNr(Long clubNr) {
    this.clubNr = clubNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public ContactRunnerField getContactRunnerField() {
    return getFieldByClass(ContactRunnerField.class);
  }

  public ExtKeyField getExtKeyField() {
    return getFieldByClass(ExtKeyField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public ShortcutField getShortcutField() {
    return getFieldByClass(ShortcutField.class);
  }

  public TabBox getTabBox() {
    return getFieldByClass(TabBox.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

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
    public class NameField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("Name");
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
    public class ContactRunnerField extends AbstractRunnerField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("ContactPerson");
      }

    }

    @Order(50.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      public class AdditionalInformationBox extends AbstractGroupBox {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("AdditionalInformation");
        }

        @Order(10.0)
        public class AdditionalInformationField extends AbstractAdditionalInformationField {

          @Override
          protected void handleEditCompleted(ITableRow row) throws ProcessingException {

          }

        }
      }
    }

    @Order(58.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(60.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(70.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  @Override
  protected boolean execValidate() throws ProcessingException {
    return getAdditionalInformationField().execValidate();
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IClubProcessService service = BEANS.get(IClubProcessService.class);
      ClubFormData formData = new ClubFormData();
      exportFormData(formData);
      formData = BeanUtility.clubBean2formData(service.load(BeanUtility.clubFormData2bean(formData)));
      importFormData(formData);
      setEnabledPermission(new UpdateClubPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IClubProcessService service = BEANS.get(IClubProcessService.class);
      ClubFormData formData = new ClubFormData();
      exportFormData(formData);
      formData = BeanUtility.clubBean2formData(service.store(BeanUtility.clubFormData2bean(formData)));
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IClubProcessService service = BEANS.get(IClubProcessService.class);
      ClubFormData formData = new ClubFormData();
      exportFormData(formData);
      formData = BeanUtility.clubBean2formData(service.prepareCreate(BeanUtility.clubFormData2bean(formData)));
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IClubProcessService service = BEANS.get(IClubProcessService.class);
      ClubFormData formData = new ClubFormData();
      exportFormData(formData);
      formData = BeanUtility.clubBean2formData(service.create(BeanUtility.clubFormData2bean(formData)));
      importFormData(formData);
    }
  }
}
