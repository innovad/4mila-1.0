package com.rtiming.client.ecard.download;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.ecard.download.ECardStationForm.MainBox.BaudField;
import com.rtiming.client.ecard.download.ECardStationForm.MainBox.CancelButton;
import com.rtiming.client.ecard.download.ECardStationForm.MainBox.ClientAddressField;
import com.rtiming.client.ecard.download.ECardStationForm.MainBox.IdentifierField;
import com.rtiming.client.ecard.download.ECardStationForm.MainBox.ModusField;
import com.rtiming.client.ecard.download.ECardStationForm.MainBox.OkButton;
import com.rtiming.client.ecard.download.ECardStationForm.MainBox.PortField;
import com.rtiming.client.ecard.download.ECardStationForm.MainBox.PrinterBox;
import com.rtiming.client.ecard.download.ECardStationForm.MainBox.PrinterBox.PosPrinterField;
import com.rtiming.client.ecard.download.ECardStationForm.MainBox.PrinterBox.PrinterField;
import com.rtiming.client.result.pos.PosPrinterLookupCall;
import com.rtiming.client.result.pos.PosPrinterUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.UpdateECardStationPermission;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.IECardStationProcessService;

@FormData(value = ECardStationFormData.class, sdkCommand = SdkCommand.CREATE)
public class ECardStationForm extends AbstractForm {

  private Long eCardStationNr;

  public ECardStationForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("ECardStation");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public Long getECardStationNr() {
    return eCardStationNr;
  }

  @FormData
  public void setECardStationNr(Long stationNr) {
    this.eCardStationNr = stationNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public ClientAddressField getClientAddressField() {
    return getFieldByClass(ClientAddressField.class);
  }

  public IdentifierField getIdentifierField() {
    return getFieldByClass(IdentifierField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public ModusField getModusField() {
    return getFieldByClass(ModusField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public PosPrinterField getPosPrinterField() {
    return getFieldByClass(PosPrinterField.class);
  }

  public PortField getPortField() {
    return getFieldByClass(PortField.class);
  }

  public BaudField getBaudField() {
    return getFieldByClass(BaudField.class);
  }

  public PrinterBox getPrinterBox() {
    return getFieldByClass(PrinterBox.class);
  }

  public PrinterField getPrinterField() {
    return getFieldByClass(PrinterField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class IdentifierField extends AbstractStringField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

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
        return 60;
      }
    }

    @Order(20.0)
    public class ClientAddressField extends AbstractStringField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Address");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 60;
      }
    }

    @Order(30.0)
    public class PortField extends AbstractStringField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("Port");
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }
    }

    @Order(40.0)
    public class ModusField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Mode");
      }

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return ECardStationDownloadModusCodeType.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(50.0)
    public class BaudField extends AbstractLongField {

      @Override
      protected boolean getConfiguredEnabled() {
        return false;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Speed");
      }

      @Override
      protected Long getConfiguredMaxValue() {
        return 999999999L;
      }

      @Override
      protected Long getConfiguredMinValue() {
        return 0L;
      }
    }

    @Order(60.0)
    public class PrinterBox extends AbstractGroupBox {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Printer");
      }

      @Order(10.0)
      public class PrinterField extends AbstractSmartField<String> {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("OperatingSystemPrinter");
        }

        @Override
        protected Class<? extends LookupCall<String>> getConfiguredLookupCall() {
          return PrinterLookupCall.class;
        }
      }

      @Order(20.0)
      public class PosPrinterField extends AbstractSmartField<String> {

        @Override
        protected String getConfiguredLabel() {
          return TEXTS.get("PosPrinter");
        }

        @Override
        protected Class<? extends LookupCall<String>> getConfiguredLookupCall() {
          return PosPrinterLookupCall.class;
        }

        @Order(10.0)
        public class InformationMenu extends AbstractMenu {

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("Information");
          }

          @Override
          protected void execInitAction() throws ProcessingException {
            setText(getConfiguredText() + "...");
          }

          @Override
          protected void execAction() throws ProcessingException {
            PosPrinterUtility.posInfo(getValue());
          }
        }

        @Order(20.0)
        public class PrintTestMenu extends AbstractMenu {

          @Override
          protected String getConfiguredText() {
            return TEXTS.get("PrintTest");
          }

          @Override
          protected void execAction() throws ProcessingException {
            PosPrinterUtility.printTest(getValue());
          }
        }
      }
    }

    @Order(68.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(70.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(80.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IECardStationProcessService service = BEANS.get(IECardStationProcessService.class);
      ECardStationFormData formData = new ECardStationFormData();
      exportFormData(formData);
      formData = service.load(formData);
      importFormData(formData);

      String myAddress = FMilaUtility.getHostAddress();
      boolean editable = StringUtility.equalsIgnoreCase(myAddress, getClientAddressField().getValue());
      if (!editable) {
        setAllEnabled(false);
        getCancelButton().setEnabled(true);
      }

      setEnabledPermission(new UpdateECardStationPermission());
    }

    @Override
    public void execStore() throws ProcessingException {
      IECardStationProcessService service = BEANS.get(IECardStationProcessService.class);
      ECardStationFormData formData = new ECardStationFormData();
      exportFormData(formData);
      formData = service.store(formData);
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IECardStationProcessService service = BEANS.get(IECardStationProcessService.class);
      ECardStationFormData formData = new ECardStationFormData();
      exportFormData(formData);
      formData = service.prepareCreate(formData);
      importFormData(formData);
    }

    @Override
    public void execStore() throws ProcessingException {
      IECardStationProcessService service = BEANS.get(IECardStationProcessService.class);
      ECardStationFormData formData = new ECardStationFormData();
      exportFormData(formData);
      formData = service.create(formData);
      importFormData(formData);
    }
  }
}
