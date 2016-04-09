package com.rtiming.client.ecard.download;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.labelfield.AbstractLabelField;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ecard.download.ECardStationStatusForm.MainBox.ComPortField;
import com.rtiming.client.ecard.download.ECardStationStatusForm.MainBox.ModusField;
import com.rtiming.client.ecard.download.ECardStationStatusForm.MainBox.RestartButton;
import com.rtiming.client.ecard.download.ECardStationStatusForm.MainBox.SettingsButton;
import com.rtiming.client.ecard.download.ECardStationStatusForm.MainBox.StatusField;
import com.rtiming.client.ecard.download.util.CRCCalculator;
import com.rtiming.client.result.pos.PosPrinterManager;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.serial.SerialUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.ECardStationStatusFormData;
import com.rtiming.shared.ecard.download.IECardStationProcessService;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;

@FormData(value = ECardStationStatusFormData.class, sdkCommand = SdkCommand.CREATE)
public class ECardStationStatusForm extends AbstractForm {

  private final HashMap<String, FMilaSerialPort> clientSerialPorts = new HashMap<String, FMilaSerialPort>();
  private Long currentECardStationNr;
  private Date currentEvtZero;
  private FMilaSerialPort serialPort;
  private StationMode currentMode = StationMode.DISCONNECTED;

  private enum StationMode {
    CONNECTED, DISCONNECTED
  }

  public ECardStationStatusForm() throws ProcessingException {
    super();
  }

  public StatusField getStatusField() {
    return getFieldByClass(StatusField.class);
  }

  public Long getCurrentECardStationNr() {
    return currentECardStationNr;
  }

  public void setCurrentECardStationNr(Long currentECardStationNr) {
    this.currentECardStationNr = currentECardStationNr;
  }

  @Override
  protected int getConfiguredDisplayHint() {
    return DISPLAY_HINT_VIEW;
  }

  @Override
  protected String getConfiguredDisplayViewId() {
    return IForm.VIEW_ID_SW;
  }

  @Override
  protected int getConfiguredModalityHint() {
    return IForm.MODALITY_HINT_MODELESS;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("ECardStation");
  }

  public void startForm() throws ProcessingException {
    startInternal(new FormHandler());
  }

  public ComPortField getComPortField() {
    return getFieldByClass(ComPortField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public ModusField getModusField() {
    return getFieldByClass(ModusField.class);
  }

  public RestartButton getRestartButton() {
    return getFieldByClass(RestartButton.class);
  }

  public SettingsButton getSettingsButton() {
    return getFieldByClass(SettingsButton.class);
  }

  /**
   * @throws ProcessingException
   */
  private void handleModusOrComPortChange() throws ProcessingException {
    if (StationMode.CONNECTED.equals(currentMode)) {
      getRestartButton().execClickAction();
    }
    if (getComPortField().getValue() != null && getModusField().getValue() != null) {
      getRestartButton().execClickAction();
    }
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class ComPortField extends AbstractSmartField<String> {

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
        getDesktop().setStatusText(TEXTS.get("SearchingDownloadStations"));
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("ECardStation");
      }

      @Override
      protected String getConfiguredTooltipText() {
        return ScoutTexts.get("ECardStation");
      }

      @Override
      protected int getConfiguredLabelPosition() {
        return LABEL_POSITION_ON_FIELD;
      }

      @Override
      protected Class<? extends LookupCall<String>> getConfiguredLookupCall() {
        return ComPortLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        handleModusOrComPortChange();
      }

      @Override
      protected void execFilterLookupResult(ILookupCall<String> call, List<ILookupRow<String>> result) {
        super.execFilterLookupResult(call, result);
        getDesktop().setStatusText(null);
      }

    }

    @Order(30.0)
    public class ModusField extends AbstractSmartField<Long> {

      @Override
      protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
        return ECardStationDownloadModusCodeType.class;
      }

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("Style");
      }

      @Override
      protected int getConfiguredLabelPosition() {
        return LABEL_POSITION_ON_FIELD;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected String getConfiguredTooltipText() {
        return ScoutTexts.get("Style");
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        handleModusOrComPortChange();
      }
    }

    @Order(40.0)
    public class StatusField extends AbstractLabelField {

      private static final String RED = "CC0000";
      private static final String GREEN = "009933";

      @Override
      protected int getConfiguredGridW() {
        return 2;
      }

      @Override
      protected boolean getConfiguredLabelVisible() {
        return false;
      }

      public void setMode(StationMode mode) {
        if (StationMode.CONNECTED.equals(mode)) {
          setBackgroundColor(GREEN);
        }
        else {
          setBackgroundColor(RED);
        }
      }

    }

    @Order(50.0)
    public class RestartButton extends AbstractButton {

      public void execInitButton() {
        if (StationMode.CONNECTED.equals(currentMode)) {
          setLabel(Texts.get("Disconnect"));
          getStatusField().setMode(StationMode.CONNECTED);
        }
        else if (StationMode.DISCONNECTED.equals(currentMode)) {
          setLabel(Texts.get("Connect"));
          getStatusField().setMode(StationMode.DISCONNECTED);
        }
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Connect");
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        if (StationMode.DISCONNECTED.equals(currentMode)) {
          if (getComPortField().getValue() == null || getModusField().getValue() == null) {
            throw new VetoException(TEXTS.get("ChoosePortAndMode"));
          }
          getDesktop().setStatusText(TEXTS.get("ConnectToECardStation"));
          init(getModusField().getValue(), getComPortField().getValue());
        }
        else if (StationMode.CONNECTED.equals(currentMode)) {
          if (serialPort != null) {
            serialPort.close();
          }
          currentMode = StationMode.DISCONNECTED;
          execInitButton();
          getDesktop().setStatusText(TEXTS.get("ECardStationCanBeRemovedMessage"));

          // close all POS printer devices
          PosPrinterManager.closeAll();
        }
      }
    }

    @Order(60.0)
    public class SettingsButton extends AbstractButton {

      @Override
      protected int getConfiguredHorizontalAlignment() {
        return 1;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Settings") + "...";
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        ECardStationForm stationForm = new ECardStationForm();
        stationForm.setECardStationNr(getCurrentECardStationNr());
        stationForm.startModify();
        stationForm.waitFor();
        if (stationForm.isFormStored()) {
          init(stationForm.getModusField().getValue(), getComPortField().getValue());
        }
      }

      @Override
      protected void execInitField() throws ProcessingException {
        setEnabled(getCurrentECardStationNr() != null);
      }

    }
  }

  public class FormHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() throws ProcessingException {
      List<? extends ILookupRow<String>> rows = getComPortField().getLookupCall().getDataByAll();
      for (ILookupRow r : rows) {
        if (r.getText().toLowerCase().contains("sportident")) {
          getComPortField().setValue(r.getKey().toString());
          break;
        }
      }
      if (!StringUtility.isNullOrEmpty(getComPortField().getValue())) {
        init(null, getComPortField().getValue());
      }
      getDesktop().setStatusText(null);
    }

  }

  public void init(Long mode, String port) throws ProcessingException {

    serialPort = clientSerialPorts.get(port);
    setCurrentECardStationNr(null);
    currentMode = StationMode.DISCONNECTED;

    // evt zero
    IEventProcessService eventService = BEANS.get(IEventProcessService.class);
    Long defaultEventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
    if (defaultEventNr == null) {
      if (this.isShowing()) {
        throw new VetoException(TEXTS.get("DefaultEventRequired"));
      }
      else {
        return;
      }
    }
    currentEvtZero = eventService.getZeroTime(defaultEventNr);

    // find station if already used
    IECardStationProcessService svc = BEANS.get(IECardStationProcessService.class);
    ECardStationFormData station = svc.find(port, FMilaUtility.getHostAddress());

    // get default readout mode
    if (mode == null) {
      if (station.getECardStationNr() != null && station.getModus().getValue() != null) {
        mode = station.getModus().getValue();
      }
      else {
        mode = ECardStationDownloadModusCodeType.EntryCode.ID;
      }
    }

    try {

      // try to close
      if (serialPort != null) {
        serialPort.close();
      }

      int speed = 38400;

      // open
      serialPort = SerialUtility.getPort(speed, port);

      // update database
      station.getBaud().setValue(new Long(speed));
      station.getIdentifier().setValue(getComPortField().getDisplayText());
      station.getModus().setValue(mode);
      if (station.getECardStationNr() == null) {
        station = svc.create(station);
      }
      else {
        station = svc.store(station);
      }
      setCurrentECardStationNr(station.getECardStationNr());

      // check
      if (currentEvtZero == null || defaultEventNr == null || getCurrentECardStationNr() == null) {
        throw new ProcessingException("Default data for opening station missing.");
      }

      // station init
      Object lock = new Object();
      SIStationSerialPortHandler stationHandler = new SIStationSerialPortHandler(ClientSession.get(), lock, serialPort);
      SISerialPortListener serialPortListener = new SISerialPortListener(ClientSession.get());
      serialPortListener.installHandler(stationHandler);

      // add single serial port listener (options are done by handlers)
      serialPort.addEventListener(serialPortListener);

      // request direct communication with readout station
      int crc = CRCCalculator.crc(new byte[]{(byte) 0xF0, (byte) 0x01, (byte) 0x4D});
      byte[] message = {(byte) 0x02, (byte) 0xF0, (byte) 0x01, (byte) 0x4D, (byte) (crc >> 8 & 0xff), (byte) (crc & 0xff), (byte) 0x03};
      serialPort.write(message);

      // wait 4 * 0.5sec for initialize
      synchronized (lock) {
        int counter = 0;
        while (!stationHandler.isInitialized() && counter <= 4) {
          lock.wait(500);
          counter++;
        }
      }
      if (!stationHandler.isInitialized()) {
        throw new ProcessingException("Timeout occured, failed initializing station");
      }

      // station is initialized, now listen to cards
      SICardSerialPortHandler cardHandler = new SICardSerialPortHandler(station, currentEvtZero, defaultEventNr, ClientSession.get(), serialPort);
      serialPortListener.installHandler(cardHandler);
      currentMode = StationMode.CONNECTED;
      getDesktop().setStatusText(TEXTS.get("DownloadStationReady"));
    }
    catch (Exception e) {
      setCurrentECardStationNr(null);
      currentMode = StationMode.DISCONNECTED;
      if (serialPort != null) {
        serialPort.close();
      }
      e.printStackTrace(); // temp
      System.out.println(e.getMessage()); // temp
      ClientSession.get().getDesktop().setStatusText(TEXTS.get("FailedOpeningPort", e.getMessage()));
    }
    finally {
      clientSerialPorts.put(port, serialPort);
      getSettingsButton().execInitField();
      getRestartButton().execInitButton();
      if (!getModusField().isValueChanging()) {
        getModusField().setValue(mode);
      }
    }

  }

}
