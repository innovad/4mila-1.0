package com.rtiming.client.entry;

import java.net.URL;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.ValueFieldMenuType;
import org.eclipse.scout.rt.client.ui.basic.cell.Cell;
import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractBooleanColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractLongColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractSmartColumn;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.client.ui.form.fields.tablefield.AbstractTableField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.ColorUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.client.AbstractDoubleColumn;
import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientSyncJob;
import com.rtiming.client.club.AbstractClubField;
import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.infodisplay.EntryInfoDisplayUpdateJob;
import com.rtiming.client.common.infodisplay.InfoDisplayIdleJob;
import com.rtiming.client.common.infodisplay.InfoDisplayUtility;
import com.rtiming.client.common.ui.action.AbstractSeparatorMenu;
import com.rtiming.client.common.ui.columns.AbstractBeanColumn;
import com.rtiming.client.common.ui.columns.AbstractDateTimeWithSecondsColumn;
import com.rtiming.client.common.ui.fields.AbstractDefaultDateTimeField;
import com.rtiming.client.common.ui.fields.AbstractYearField;
import com.rtiming.client.ecard.AbstractECardField;
import com.rtiming.client.entry.EntryForm.MainBox.AddEventButton;
import com.rtiming.client.entry.EntryForm.MainBox.AddRunnerButton;
import com.rtiming.client.entry.EntryForm.MainBox.CancelAndNextButton;
import com.rtiming.client.entry.EntryForm.MainBox.CancelButton;
import com.rtiming.client.entry.EntryForm.MainBox.CityField;
import com.rtiming.client.entry.EntryForm.MainBox.ClazzField;
import com.rtiming.client.entry.EntryForm.MainBox.ClubField;
import com.rtiming.client.entry.EntryForm.MainBox.ECardField;
import com.rtiming.client.entry.EntryForm.MainBox.FirstNameField;
import com.rtiming.client.entry.EntryForm.MainBox.LastNameField;
import com.rtiming.client.entry.EntryForm.MainBox.OkButton;
import com.rtiming.client.entry.EntryForm.MainBox.RegistrationField;
import com.rtiming.client.entry.EntryForm.MainBox.RunnerField;
import com.rtiming.client.entry.EntryForm.MainBox.SaveAndNextButton;
import com.rtiming.client.entry.EntryForm.MainBox.SexField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.AdditionalInformationEntryBox;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.AdditionalInformationEntryBox.AdditionalInformationEntryField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.AdditionalInformationRunnerBox;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.AdditionalInformationRunnerBox.AdditionalInformationRunnerField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.EventsBox;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.EventsBox.EventsField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.FeesBox;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.FeesBox.CurrencyUidField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.FeesBox.EvtEntryField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.FeesBox.FeesField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table;
import com.rtiming.client.entry.EntryForm.MainBox.YearField;
import com.rtiming.client.race.RaceForm;
import com.rtiming.client.settings.addinfo.AbstractAdditionalInformationField;
import com.rtiming.client.settings.city.AbstractCityField;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.AdditionalInformationBean;
import com.rtiming.shared.common.database.sql.AddressBean;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.database.sql.EntryBean;
import com.rtiming.shared.common.database.sql.ParticipationBean;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.common.database.sql.RunnerBean;
import com.rtiming.shared.common.security.permission.UpdateEntryPermission;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.ecard.ECardLookupCall;
import com.rtiming.shared.entry.EntryFormData;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.entry.IEntryProcessService;
import com.rtiming.shared.entry.RegistrationLookupCall;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.EventLookupCall;
import com.rtiming.shared.event.course.ClassTypeCodeType;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.runner.RunnerFormData;
import com.rtiming.shared.runner.RunnerFormData.AdditionalInformation;
import com.rtiming.shared.runner.RunnerLookupCall;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationFieldData;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationProcessService;
import com.rtiming.shared.settings.city.CountryCodeType;
import com.rtiming.shared.settings.clazz.ClazzLookupCall;
import com.rtiming.shared.settings.currency.CurrencyCodeType;
import com.rtiming.shared.settings.fee.FeeFormData;

@FormData(value = EntryFormData.class, sdkCommand = SdkCommand.CREATE)
public class EntryForm extends AbstractForm {

  private Long entryNr;
  private boolean m_isDownloadStationEntry = false;
  private AbstractAdditionalInformationFieldData additionalInformationEntryFormData;

  private Map<Long, long[]> eventAdditionalInformationConfiguration;
  private EventConfiguration eventConfiguration;
  private List<RtClassAge> ageConfiguration;
  private List<FeeFormData> feeConfiguration;

  public EntryForm() throws ProcessingException {
    super();
  }

  @Override
  protected boolean getConfiguredCacheBounds() {
    return true;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Entry");
  }

  @Override
  protected void execInitForm() throws ProcessingException {
    eventAdditionalInformationConfiguration = SharedCache.getEventAdditionalInformationConfiguration();

    // Load Event Settings
    eventConfiguration = SharedCache.getEventConfiguration();

    // Load Age Settings
    ageConfiguration = SharedCache.getAgeConfiguration();

    // Fee Configuration
    feeConfiguration = SharedCache.getFeeConfiguration();
  }

  @Override
  protected void execDisposeForm() throws ProcessingException {
    idleInfoWindow();
  }

  @Override
  protected boolean execValidate() throws ProcessingException {

    // Classes
    EntryFormUtility.checkClassesNotEmpty(getRacesField().getTable());

    // Events
    if (Arrays.asList(getEventsField().getTable().getEventNrColumn().getValues()).contains(null)) {
      throw new VetoException(TEXTS.get("EntryEventOnEventRequiredMessage"));
    }

    // Additional Information
    getAdditionalInformationRunnerField().execValidate();
    getAdditionalInformationEntryField().execValidate();

    // Team Size
    for (int i = 0; i < getEventsField().getTable().getRowCount(); i++) {
      Long eventNr = getEventsField().getTable().getEventNrColumn().getValue(i);
      Long classUid = getEventsField().getTable().getEventClassColumn().getValue(i);

      List<ITableRow> rows = getRacesField().getTable().getLegColumn().findRows(classUid);
      long count = 0L;
      if (rows != null) {
        count = rows.size();
      }
      EntryFormUtility.validateTeamSize(eventNr, classUid, count, eventConfiguration);
    }

    return true;
  }

  @FormData
  public boolean isDownloadStationEntry() {
    return m_isDownloadStationEntry;
  }

  @FormData
  public void setDownloadStationEntry(boolean isDownloadStationEntry) {
    m_isDownloadStationEntry = isDownloadStationEntry;
  }

  @FormData
  public Long getEntryNr() {
    return entryNr;
  }

  @FormData
  public void setEntryNr(Long entryNr) {
    this.entryNr = entryNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public AddEventButton getAddEventButton() {
    return getFieldByClass(AddEventButton.class);
  }

  public AddRunnerButton getAddRunnerButton() {
    return getFieldByClass(AddRunnerButton.class);
  }

  public AdditionalInformationEntryBox getAdditionalInformationEntryBox() {
    return getFieldByClass(AdditionalInformationEntryBox.class);
  }

  public AdditionalInformationEntryField getAdditionalInformationEntryField() {
    return getFieldByClass(AdditionalInformationEntryField.class);
  }

  public AdditionalInformationRunnerBox getAdditionalInformationRunnerBox() {
    return getFieldByClass(AdditionalInformationRunnerBox.class);
  }

  public AdditionalInformationRunnerField getAdditionalInformationRunnerField() {
    return getFieldByClass(AdditionalInformationRunnerField.class);
  }

  public CancelAndNextButton getCancelAndNextButton() {
    return getFieldByClass(CancelAndNextButton.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public CityField getCityField() {
    return getFieldByClass(CityField.class);
  }

  public ClazzField getClazzField() {
    return getFieldByClass(ClazzField.class);
  }

  public ClubField getClubField() {
    return getFieldByClass(ClubField.class);
  }

  public ECardField getECardField() {
    return getFieldByClass(ECardField.class);
  }

  public EventsBox getEventsBox() {
    return getFieldByClass(EventsBox.class);
  }

  public EventsField getEventsField() {
    return getFieldByClass(EventsField.class);
  }

  public EvtEntryField getEvtEntryField() {
    return getFieldByClass(EvtEntryField.class);
  }

  public FeesField getFeesField() {
    return getFieldByClass(FeesField.class);
  }

  public CurrencyUidField getCurrencyField() {
    return getFieldByClass(CurrencyUidField.class);
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

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public FeesBox getFeesBox() {
    return getFieldByClass(FeesBox.class);
  }

  public RegistrationField getRegistrationField() {
    return getFieldByClass(RegistrationField.class);
  }

  public RunnerField getRunnerField() {
    return getFieldByClass(RunnerField.class);
  }

  public SaveAndNextButton getSaveAndNextButton() {
    return getFieldByClass(SaveAndNextButton.class);
  }

  public SexField getSexField() {
    return getFieldByClass(SexField.class);
  }

  public TabBox getTabBox() {
    return getFieldByClass(TabBox.class);
  }

  public RacesBox getRacesBox() {
    return getFieldByClass(RacesBox.class);
  }

  public RacesField getRacesField() {
    return getFieldByClass(RacesField.class);
  }

  public YearField getYearField() {
    return getFieldByClass(YearField.class);
  }

  private void addNewRunnerRow(boolean changeFocus) throws ProcessingException {
    getRacesField().getTable().deselectAllRows();
    getTabBox().setSelectedTab(getRacesBox());
    ITableRow newRow = getRacesField().getTable().createRow();

    // Event
    Long defaultEventNr = EntryFormUtility.getDefaultParticipationEventNr(getEventsField());
    getRacesField().getTable().getRaceEventColumn().setValue(newRow, defaultEventNr);
    ((ClazzLookupCall) getClazzField().getLookupCall()).setEventNr(defaultEventNr);

    // Additional Information
    AdditionalInformationBean addInfo = new AdditionalInformationBean();
    addInfo.setEntityUid(EntityCodeType.RunnerCode.ID);
    addInfo = BEANS.get(IAdditionalInformationProcessService.class).prepareCreate(addInfo);
    updateRunnerAdditionalInformation(addInfo);

    // Runner
    getRacesField().getTable().getRaceBeanColumn().setValue(newRow, new RaceBean());
    newRow = getRacesField().getTable().addRow(newRow);
    clearRunnerFields();
    try {
      getRunnerField().setValueChangeTriggerEnabled(false);
      getRunnerField().setValue(null);
    }
    finally {
      getRunnerField().setValueChangeTriggerEnabled(true);
    }
    getRacesField().getTable().selectRow(newRow.getRowIndex());
    if (changeFocus) {
      getRunnerField().requestFocus();
    }
  }

  private void clearRunnerFields() {
    try {
      setValueChangeTriggerOnFormFieldsEnabled(false);

      if (!isDownloadStationEntry()) {
        getECardField().setValue(null);
      }
      setFieldNull(getLastNameField());
      setFieldNull(getFirstNameField());
      setFieldNull(getClazzField());
      setFieldNull(getSexField());
      setFieldNull(getYearField());
      setFieldNull(getClubField());
      setFieldNull(getCityField());
    }
    finally {
      setValueChangeTriggerOnFormFieldsEnabled(true);
    }

  }

  private void setFieldNull(IValueField<?> field) {
    if (!field.isFieldChanging()) {
      field.setValue(null);
    }
  }

  private void setValueChangeTriggerOnFormFieldsEnabled(boolean enabled) {
    getECardField().setValueChangeTriggerEnabled(enabled);
    getLastNameField().setValueChangeTriggerEnabled(enabled);
    getFirstNameField().setValueChangeTriggerEnabled(enabled);
    getClubField().setValueChangeTriggerEnabled(enabled);
    getSexField().setValueChangeTriggerEnabled(enabled);
    getYearField().setValueChangeTriggerEnabled(enabled);
    getCityField().setValueChangeTriggerEnabled(enabled);
    getClazzField().setValueChangeTriggerEnabled(enabled);
  }

  private void modifyRunner() throws ProcessingException {
    if (!getRunnerField().isValueChanging()) {
      if (getRacesField().getTable().getSelectedRowCount() == 0) {
        addNewRunnerRow(false);
      }

      RunnerBean runnerBean = getRacesField().getTable().getRaceBeanColumn().getSelectedValue().getRunner();

      runnerBean.setECardNr(getECardField().getValue());
      runnerBean.setLastName(getLastNameField().getValue());
      runnerBean.setFirstName(getFirstNameField().getValue());
      runnerBean.setSexUid(getSexField().getValue());
      runnerBean.setYear(getYearField().getValue());
      runnerBean.setClubNr(getClubField().getValue());
      runnerBean.getAddress().setCityNr(getCityField().getValue());

      AdditionalInformation additionalInformationRunner = new RunnerFormData().getAdditionalInformation();
      getAdditionalInformationRunnerField().getTable().discardAllDeletedRows();
      getAdditionalInformationRunnerField().exportFormFieldData(additionalInformationRunner);
      BeanUtility.addInfoFormData2Bean(additionalInformationRunner, runnerBean.getAddInfo(), EntityCodeType.RunnerCode.ID, runnerBean.getRunnerNr(), runnerBean.getClientNr());

      updateRunnerRowFromBean(getRacesField().getTable().getSelectedRow(), runnerBean, true);

      updateInfoWindow();
    }
  }

  private void updateRunnerRowFromBean(ITableRow row, RunnerBean runner, boolean updateECard) throws ProcessingException {
    Table races = getRacesField().getTable();
    races.getRunnerNrColumn().setValue(row, runner.getRunnerNr());
    races.getFirstNameColumn().setValue(row, runner.getFirstName());
    races.getLastNameColumn().setValue(row, runner.getLastName());
    races.getClubNrColumn().setValue(row, runner.getClubNr());
    races.getNationColumn().setValue(row, runner.getNationUid());
    if (updateECard) {
      races.getECardColumn().setValue(row, runner.getECardNr());
    }
    updateRaceBeanColumn(row, runner);
    updateRunnerAdditionalInformation(runner.getAddInfo());
  }

  private void updateRaceBeanColumn(ITableRow row, RunnerBean runner) throws ProcessingException {
    getRacesField().getTable().getRaceBeanColumn().getValue(row).setRunner(runner);
    getRacesField().getTable().getRaceBeanColumn().getValue(row).setAddress(runner.getAddress().copy());
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class RunnerField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Runner");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return RunnerLookupCall.class;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        // clean fields
        clearRunnerFields();

        // get default event
        Long defaultEventNr = EntryFormUtility.getDefaultParticipationEventNr(getEventsField());

        // load runner
        RunnerBean runner = new RunnerBean();
        runner.setRunnerNr(getRunnerField().getValue());
        if (runner.getRunnerNr() != null) {
          runner = BEANS.get(IRunnerProcessService.class).load(runner);
        }

        // add/update runner
        ITableRow row;
        boolean isNewRow = getRacesField().getTable().getSelectedRow() == null;
        if (isNewRow) {
          row = getRacesField().getTable().createRow();
          getRacesField().getTable().getRaceEventColumn().setValue(row, defaultEventNr);
          ((ClazzLookupCall) getClazzField().getLookupCall()).setEventNr(defaultEventNr);
          getRacesField().getTable().getRaceBeanColumn().setValue(row, new RaceBean());
          updateRaceBeanColumn(row, runner);
        }
        else {
          row = getRacesField().getTable().getSelectedRow();
          updateRaceBeanColumn(row, runner);
        }

        // update fields, avoid row update
        try {
          setValueChangeTriggerOnFormFieldsEnabled(false);

          // data fields
          if (getECardField().getValue() == null && !isDownloadStationEntry()) {
            // e-card could be set from outside (download station)
            getECardField().setValue(runner.getECardNr());
          }
          getLastNameField().setValue(runner.getLastName());
          getFirstNameField().setValue(runner.getFirstName());
          getClubField().setValue(runner.getClubNr());
          getCityField().setValue(runner.getAddress().getCityNr());
          getSexField().setValue(runner.getSexUid());
          getYearField().setValue(runner.getYear());
          updateRunnerAdditionalInformation(runner.getAddInfo());
        }
        finally {
          setValueChangeTriggerOnFormFieldsEnabled(true);
        }

        // update row
        updateRunnerRowFromBean(row, runner, !isDownloadStationEntry());

        // select
        if (isNewRow) {
          getRacesField().getTable().addRow(row);
          getRacesField().getTable().selectFirstRow();
        }

        // class
        if (EntryFormUtility.getDefaultParticipationClassUid(getEventsField()) == null) {
          getClazzField().setValue(runner.getDefaultClassUid());
        }
        else {
          getClazzField().setValue(EntryFormUtility.getDefaultParticipationClassUid(getEventsField()));
        }

        ClientSession clientSession = ClientSession.get();
        if (clientSession != null) {
          FMilaClientSyncJob job = new FMilaClientSyncJob("Focus", clientSession) {
            @Override
            protected void runVoid() throws Exception {
              getClazzField().requestFocus();
            }
          };
          job.schedule();
        }

        updateInfoWindow();
      }
    }

    @Order(20.0)
    public class ClazzField extends AbstractSmartField<Long> {

      @Override
      protected boolean getConfiguredBrowseHierarchy() {
        return true;
      }

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Class");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return ClazzLookupCall.class;
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        Long eventNr = ((ClazzLookupCall) getClazzField().getLookupCall()).getEventNr();
        Long classUid = getClazzField().getValue();
        EventClassFormData eventClass = null;

        if (eventNr != null && classUid != null) {
          eventClass = eventConfiguration.getEventClassInfo(eventNr, classUid);
        }

        // check class and age
        EntryFormUtility.validateClassField(eventConfiguration, ageConfiguration, getYearField(), getSexField(), getClazzField(), getEvtEntryField());

        // update class
        ITableRow row;
        if (getRacesField().getTable().getSelectedRow() == null) {
          // nop
        }
        else {
          row = getRacesField().getTable().getSelectedRow();
          getRacesField().getTable().getLegColumn().setValue(row, classUid);
          for (int k = 0; k < getEventsField().getTable().getRowCount(); k++) {
            if (eventClass != null && CompareUtility.equals(eventNr, getEventsField().getTable().getEventNrColumn().getValue(k))) {
              Long eventClassTypeUid = eventClass.getType().getValue();
              if (CompareUtility.equals(eventClassTypeUid, ClassTypeCodeType.RelayCode.ID)) {
                // in case of relay class don't copy anything to the team tab
              }
              else if (CompareUtility.equals(eventClassTypeUid, ClassTypeCodeType.RelayLegCode.ID)) {
                // relay leg: load parent class
                EventClassFormData eventClassParent = eventConfiguration.getEventClassInfo(getEventsField().getTable().getEventNrColumn().getValue(k), classUid);
                getEventsField().getTable().getEventClassColumn().setValue(k, eventClassParent.getParent().getValue());
              }
              else {
                // set same class
                getEventsField().getTable().getEventClassColumn().setValue(k, classUid);
              }
            }
          }
        }

        // update start time for mass start
        if (getHandler() instanceof NewHandler) {
          EntryFormUtility.updateMassStartTime(getEventsField().getTable(), getRacesField().getTable(), eventConfiguration);
        }

        // update fees
        updateFeesField();

        // update info window
        updateInfoWindow();
      }

      @Override
      protected void execPrepareLookup(ILookupCall call) throws ProcessingException {
        ((ClazzLookupCall) call).setShowShortcutOnly(true);
        if (getRacesField().getTable().getSelectedRowCount() == 0) {
          ((ClazzLookupCall) call).setEventNr(EntryFormUtility.getDefaultParticipationEventNr(getEventsField()));
          addNewRunnerRow(false);
        }
      }
    }

    @Order(30.0)
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
      protected void execChangedValue() throws ProcessingException {
        modifyRunner();
      }
    }

    @Order(40.0)
    public class FirstNameField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("FirstName");
      }

      @Override
      protected void execChangedValue() throws ProcessingException {
        modifyRunner();
      }
    }

    @Order(50.0)
    public class ECardField extends AbstractECardField {

      @Override
      protected void execChangedValue() throws ProcessingException {
        modifyRunner();
      }
    }

    @Order(60.0)
    public class RegistrationField extends AbstractSmartField<Long> {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Registration");
      }

      @Override
      protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
        return RegistrationLookupCall.class;
      }

    }

    @Order(70.0)
    public class ClubField extends AbstractClubField {

      @Override
      protected void execChangedValue() throws ProcessingException {
        modifyRunner();
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

      @Override
      protected void execChangedValue() throws ProcessingException {
        EntryFormUtility.validateClassField(eventConfiguration, ageConfiguration, getYearField(), getSexField(), getClazzField(), getEvtEntryField());
        modifyRunner();
      }
    }

    @Order(90.0)
    public class YearField extends AbstractYearField {

      @Override
      protected void execChangedValue() throws ProcessingException {
        EntryFormUtility.validateClassField(eventConfiguration, ageConfiguration, getYearField(), getSexField(), getClazzField(), getEvtEntryField());
        modifyRunner();
        updateFeesField();
      }

    }

    @Order(100.0)
    public class CityField extends AbstractCityField {

      @Override
      protected void execChangedValue() throws ProcessingException {
        modifyRunner();
      }
    }

    @Order(110.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      public class RacesBox extends AbstractGroupBox {

        @Override
        protected boolean getConfiguredGridUseUiWidth() {
          return true;
        }

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("Team");
        }

        @Order(10.0)
        public class RacesField extends AbstractTableField<RacesField.Table> {

          @Override
          protected int getConfiguredGridH() {
            return 5;
          }

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Team");
          }

          @Override
          protected boolean getConfiguredLabelVisible() {
            return false;
          }

          @Order(10.0)
          public class Table extends AbstractTable {

            @Override
            protected boolean getConfiguredAutoResizeColumns() {
              return true;
            }

            @Override
            // TODO MIG @ConfigPropertyValue("true")
            protected boolean getConfiguredMultiSelect() {
              return false;
            }

            @Override
            protected String getConfiguredDefaultIconId() {
              return Icons.RUNNER;
            }

            @Override
            protected void execRowsSelected(List<? extends ITableRow> rows) throws ProcessingException {
              boolean isFieldChanging = false;
              for (IFormField field : getForm().getAllFields()) {
                if (field.isFieldChanging()) {
                  isFieldChanging = true;
                  break;
                }
              }
              if (rows.size() > 0 && !isFieldChanging) {
                updateFormFromRunnerRow(rows.get(0));
              }
              for (ITableRow row : getTable().getRows()) {
                updateModifyHtml(row);
              }
            }

            public LegStartTimeColumn getLegStartTimeColumn() {
              return getColumnSet().getColumnByClass(LegStartTimeColumn.class);
            }

            public ClubNrColumn getClubNrColumn() {
              return getColumnSet().getColumnByClass(ClubNrColumn.class);
            }

            public NationColumn getNationColumn() {
              return getColumnSet().getColumnByClass(NationColumn.class);
            }

            public BibNumberColumn getBibNumberColumn() {
              return getColumnSet().getColumnByClass(BibNumberColumn.class);
            }

            public ECardColumn getECardColumn() {
              return getColumnSet().getColumnByClass(ECardColumn.class);
            }

            public FirstNameColumn getFirstNameColumn() {
              return getColumnSet().getColumnByClass(FirstNameColumn.class);
            }

            public LastNameColumn getLastNameColumn() {
              return getColumnSet().getColumnByClass(LastNameColumn.class);
            }

            public LegColumn getLegColumn() {
              return getColumnSet().getColumnByClass(LegColumn.class);
            }

            public RaceEventColumn getRaceEventColumn() {
              return getColumnSet().getColumnByClass(RaceEventColumn.class);
            }

            public RaceNrColumn getRaceNrColumn() {
              return getColumnSet().getColumnByClass(RaceNrColumn.class);
            }

            public RaceBeanColumn getRaceBeanColumn() {
              return getColumnSet().getColumnByClass(RaceBeanColumn.class);
            }

            public RunnerNrColumn getRunnerNrColumn() {
              return getColumnSet().getColumnByClass(RunnerNrColumn.class);
            }

            public RaceFormButtonColumn getRaceFormButtonColumn() {
              return getColumnSet().getColumnByClass(RaceFormButtonColumn.class);
            }

            @Order(10.0)
            public class RaceNrColumn extends AbstractLongColumn {

              @Override
              protected boolean getConfiguredDisplayable() {
                return false;
              }
            }

            @Order(30.0)
            public class RunnerNrColumn extends AbstractSmartColumn<Long> {

              @Override
              protected boolean getConfiguredDisplayable() {
                return false;
              }

              @Override
              protected int getConfiguredWidth() {
                return 250;
              }

              @Override
              protected void execPrepareLookup(ILookupCall call, ITableRow row) {
                ((RunnerLookupCall) call).setShowNameOnly(true);
              }
            }

            @Order(40.0)
            public class ClubNrColumn extends AbstractLongColumn {

              @Override
              protected boolean getConfiguredDisplayable() {
                return false;
              }
            }

            @Order(50.0)
            public class LastNameColumn extends AbstractStringColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("LastName");
              }

              @Override
              protected int getConfiguredSortIndex() {
                return 3;
              }

              @Override
              protected int getConfiguredWidth() {
                return 120;
              }
            }

            @Order(60.0)
            public class FirstNameColumn extends AbstractStringColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("FirstName");
              }

              @Override
              protected int getConfiguredSortIndex() {
                return 4;
              }

              @Override
              protected int getConfiguredWidth() {
                return 120;
              }
            }

            @Order(70.0)
            public class ECardColumn extends AbstractSmartColumn<Long> {

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("ECard");
              }

              @Override
              protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
                return ECardLookupCall.class;

              }

              @Override
              protected int getConfiguredWidth() {
                return 50;
              }

              @Override
              protected boolean getConfiguredVisible() {
                return false;
              }

            }

            @Order(80.0)
            public class BibNumberColumn extends AbstractStringColumn {

              @Override
              protected boolean getConfiguredVisible() {
                return false;
              }

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("BibNumber");
              }

              @Override
              protected int getConfiguredWidth() {
                return 50;
              }
            }

            @Order(90.0)
            public class LegStartTimeColumn extends AbstractDateTimeWithSecondsColumn {

              @Override
              protected boolean getConfiguredVisible() {
                return false;
              }

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("StartTime");
              }

              @Override
              protected int getConfiguredWidth() {
                return 100;
              }

            }

            @Order(100.0)
            public class LegColumn extends AbstractSmartColumn<Long> {

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("Class");
              }

              @Override
              protected int getConfiguredHorizontalAlignment() {
                return 2;
              }

              @Override
              protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
                return ClazzLookupCall.class;
              }

              @Override
              protected int getConfiguredSortIndex() {
                return 2;
              }

              @Override
              protected int getConfiguredWidth() {
                return 55;
              }

              @Override
              protected void execPrepareLookup(ILookupCall call, ITableRow row) {
                ((ClazzLookupCall) call).setEventNr(getRaceEventColumn().getValue(row));
                ((ClazzLookupCall) call).setShowShortcutOnly(true);
              }
            }

            @Order(110.0)
            public class NationColumn extends AbstractSmartColumn<Long> {

              @Override
              protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
                return CountryCodeType.class;
              }

              @Override
              protected String getConfiguredHeaderText() {
                return TEXTS.get("Nation");
              }

              @Override
              protected boolean getConfiguredVisible() {
                return false;
              }
            }

            @Order(120.0)
            public class RaceEventColumn extends AbstractSmartColumn<Long> {

              @Override
              protected boolean getConfiguredEditable() {
                return true;
              }

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("Event");
              }

              @Override
              protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
                return EventLookupCall.class;
              }

              @Override
              protected void execPrepareLookup(ILookupCall call, ITableRow row) {
                ((EventLookupCall) call).setRestrictToEventNrs(getEventsField().getTable().getEventNrColumn().getValues());
              }

              @Override
              protected int getConfiguredSortIndex() {
                return 1;
              }

              @Override
              protected int getConfiguredWidth() {
                return 65;
              }

              @Override
              protected void execCompleteEdit(ITableRow row, IFormField editingField) throws ProcessingException {
                super.execCompleteEdit(row, editingField);
                List<Long> availableEvents = getEventsField().getTable().getEventNrColumn().getValues();
                EntryFormUtility.validateRacesTable(getRacesField().getTable(), availableEvents, eventConfiguration);
                updateFormFromRunnerRow(row);
              }

            }

            @Order(125.0)
            public class RaceFormButtonColumn extends AbstractStringColumn {

              @Override
              protected void execDecorateCell(Cell cell, ITableRow row) throws ProcessingException {
                updateModifyHtml(row);
              }

            }

            @Override
            protected void execHyperlinkAction(URL url, String path, boolean local) throws ProcessingException {
              if (path != null) {
                getTable().runMenu(EditMenu.class);
              }
            }

            @Order(130.0)
            public class RaceBeanColumn extends AbstractBeanColumn<RaceBean> {

              @Override
              protected boolean getConfiguredDisplayable() {
                return false;
              }
            }

            @Override
            protected Class<? extends IMenu> getConfiguredDefaultMenu() {
              return EditMenu.class;
            }

            private void updateModifyHtml(ITableRow row) {
              String color = row.isSelected() ? ColorUtility.WHITE : ColorUtility.BLACK;
              String html = "<html><body><font color=\"#" + color + "\"><a href=\"rowIndex=" + row.getRowIndex() + "\">" + TEXTS.get("EditMenu") + "</a></font></body></html>";
              Cell cell = (Cell) row.getCell(getRaceFormButtonColumn());
              if (!StringUtility.equalsIgnoreCase(cell.getText(), html)) {
                cell.setText(html);
              }
            }

            @Order(10.0)
            public class AddRunnerMenu extends AbstractMenu {

              @Override
              protected String getConfiguredKeyStroke() {
                return FMilaUtility.NEW_ENTRY_KEY_STROKE;
              }

              @Override
              protected Set<? extends IMenuType> getConfiguredMenuTypes() {
                return CollectionUtility.<IMenuType> hashSet(ValueFieldMenuType.Null);
              }

              @Override
              protected String getConfiguredText() {
                return Texts.get("AddRunner");
              }

              @Override
              protected void execAction() throws ProcessingException {
                addNewRunnerRow(true);
              }
            }

            @Order(20.0)
            public class EditMenu extends AbstractMenu {

              @Override
              protected String getConfiguredText() {
                return TEXTS.get("EditEntryAndRace");
              }

              @Override
              protected void execAction() throws ProcessingException {
                ITableRow row = getTable().getSelectedRow();
                RaceForm form = new RaceForm();

                // Export EntryForm and convert to EntryBean
                EntryFormData formData = new EntryFormData();
                exportFormData(formData);
                EntryBean entryBean = BeanUtility.entryFormData2bean(formData, eventConfiguration);

                // Get RaceBean from EntryBean
                RaceBean raceBean = entryBean.getRaces().get(row.getRowIndex());
                form.setRaceBean(raceBean);
                form.startModify(false);

                form.waitFor();
                if (form.isFormStored()) {
                  // Replace RaceBean with modified RaceBean
                  int index = entryBean.getRaces().indexOf(raceBean);
                  entryBean.getRaces().remove(index);
                  entryBean.getRaces().add(index, form.getRaceBean());

                  // Convert EntryBean back to FormData and import
                  formData = BeanUtility.entryBean2formData(entryBean, eventConfiguration);
                  importFormData(formData);

                  updateRunnerRowFromBean(row, form.getRaceBean().getRunner(), true);
                  updateFormFromRunnerRow(getTable().getRow(row.getRowIndex()));

                  EntryFormUtility.updateParticipationStartTimes(getRacesField().getTable(), getEventsField().getTable(), eventConfiguration);
                  selectRow(getTable().getRow(row.getRowIndex()));
                  touch();
                }
              }
            }

            @Order(30.0)
            public class SeparatorMenu extends AbstractSeparatorMenu {
            }

            @Order(40.0)
            public class DeleteRunnerMenu extends AbstractMenu {

              @Override
              protected String getConfiguredText() {
                return ScoutTexts.get("DeleteMenu");
              }

              @Override
              protected void execAction() throws ProcessingException {
                getRacesField().getTable().deleteRow(getRacesField().getTable().getSelectedRow());
                clearRunnerFields();
                getRacesField().getTable().selectFirstRow();
              }
            }
          }
        }
      }

      @Order(20.0)
      public class EventsBox extends AbstractGroupBox {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("Events");
        }

        @Order(10.0)
        public class EventsField extends AbstractTableField<EventsField.Table> {

          @Override
          protected int getConfiguredGridH() {
            return 5;
          }

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Events");
          }

          @Override
          protected boolean getConfiguredLabelVisible() {
            return false;
          }

          @Order(10.0)
          public class Table extends AbstractTable {

            @Override
            protected boolean getConfiguredAutoResizeColumns() {
              return true;
            }

            @Override
            protected String getConfiguredDefaultIconId() {
              return Icons.EVENT;
            }

            public EventClassColumn getEventClassColumn() {
              return getColumnSet().getColumnByClass(EventClassColumn.class);
            }

            public EventNrColumn getEventNrColumn() {
              return getColumnSet().getColumnByClass(EventNrColumn.class);
            }

            public ParticipationBeanColumn getParticipationBeanColumn() {
              return getColumnSet().getColumnByClass(ParticipationBeanColumn.class);
            }

            public StartTimeColumn getStartTimeColumn() {
              return getColumnSet().getColumnByClass(StartTimeColumn.class);
            }

            @Order(5.0)
            public class ParticipationBeanColumn extends AbstractBeanColumn<ParticipationBean> {

              @Override
              protected boolean getConfiguredDisplayable() {
                return false;
              }
            }

            @Order(10.0)
            public class EventNrColumn extends AbstractSmartColumn<Long> {

              @Override
              protected boolean getConfiguredEditable() {
                return true;
              }

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("Event");
              }

              @Override
              protected Class<? extends ILookupCall<Long>> getConfiguredLookupCall() {
                return EventLookupCall.class;
              }

              @Override
              protected boolean getConfiguredPrimaryKey() {
                return true;
              }

              @Override
              protected void execCompleteEdit(ITableRow row, IFormField editingField) throws ProcessingException {
                super.execCompleteEdit(row, editingField);
                additionalInformationEntryFormData = EntryFormUtility.validateAdditionalInformationEntry(additionalInformationEntryFormData, getAdditionalInformationEntryField(), getEventsField(), eventAdditionalInformationConfiguration);
                getEventsField().getTable().requestFocusInCell(getEventClassColumn(), row);
              }

            }

            @Order(20.0)
            public class EventClassColumn extends AbstractSmartColumn<Long> {

              @Override
              protected boolean getConfiguredEditable() {
                return true;
              }

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("Class");
              }

              @Override
              protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
                return ClazzLookupCall.class;
              }

              @Override
              protected int getConfiguredWidth() {
                return 120;
              }

              @Override
              protected void execPrepareLookup(ILookupCall<Long> call, ITableRow row) {
                ((ClazzLookupCall) call).setEventNr(getEventNrColumn().getValue(row));
                ((ClazzLookupCall) call).setShowClassesOnly(true);
                super.execPrepareLookup(call, row);
              }

            }

            @Order(25.0)
            public class StartTimeColumn extends AbstractDateTimeWithSecondsColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("StartTime");
              }

              @Override
              protected int getConfiguredWidth() {
                return 65;
              }

              @Override
              protected boolean getConfiguredEditable() {
                return true;
              }

              @SuppressWarnings("unchecked")
              @Override
              protected IFormField execPrepareEdit(ITableRow row) throws ProcessingException {
                IFormField field = super.execPrepareEdit(row);
                if (getStartTimeColumn().getValue(row) == null) {
                  Long eventNr = getEventNrColumn().getValue(row);
                  if (eventNr != null) {
                    Date proposalDate = eventConfiguration.getEvent(eventNr).getEvtZero();
                    proposalDate = DateUtility.nvl(proposalDate, new Date());
                    if (field instanceof IValueField<?>) {
                      ((IValueField<Date>) field).setValue(proposalDate);
                    }
                  }
                }
                return field;
              }

              @Override
              protected void execCompleteEdit(ITableRow row, IFormField editingField) throws ProcessingException {
                super.execCompleteEdit(row, editingField);
                // update Races and Race Bean
                EntryFormUtility.updateRaceStartTimes(getRacesField().getTable(), getEventsField().getTable(), eventConfiguration);
              }

            }

            @Order(10.0)
            public class AddEventMenu extends AbstractMenu {

              @Override
              protected String getConfiguredKeyStroke() {
                return "f6";
              }

              @Override
              protected Set<? extends IMenuType> getConfiguredMenuTypes() {
                return CollectionUtility.<IMenuType> hashSet(ValueFieldMenuType.Null);
              }

              @Override
              protected String getConfiguredText() {
                return Texts.get("AddEvent");
              }

              @Override
              protected void execAction() throws ProcessingException {
                addNewEvent();
              }
            }

            @Order(20.0)
            public class DeleteEventMenu extends AbstractMenu {

              @Override
              protected String getConfiguredText() {
                return ScoutTexts.get("DeleteMenu");
              }

              @Override
              protected void execAction() throws ProcessingException {
                getEventsField().getTable().deleteRow(getEventsField().getTable().getSelectedRow());
              }
            }
          }
        }
      }

      @Order(30.0)
      public class FeesBox extends AbstractGroupBox {

        @Override
        public String getConfiguredLabel() {
          return Texts.get("Fees");
        }

        @Order(10.0)
        public class EvtEntryField extends AbstractDefaultDateTimeField {

          @Override
          protected void execChangedValue() throws ProcessingException {
            updateFeesField();
          }

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Date");
          }

          @Override
          protected boolean getConfiguredMandatory() {
            return true;
          }
        }

        @Order(20.0)
        public class CurrencyUidField extends AbstractSmartField<Long> {

          @Override
          protected void execChangedValue() throws ProcessingException {
            updateFeesField();
          }

          @Override
          protected Class<? extends ICodeType<?, Long>> getConfiguredCodeType() {
            return CurrencyCodeType.class;
          }

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Currency");
          }

          @Override
          protected boolean getConfiguredMandatory() {
            return true;
          }
        }

        @Order(40.0)
        public class FeesField extends AbstractTableField<FeesField.Table> {

          @Override
          protected int getConfiguredGridH() {
            return 4;
          }

          @Override
          protected int getConfiguredGridW() {
            return 2;
          }

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Fees");
          }

          @Override
          protected boolean getConfiguredLabelVisible() {
            return false;
          }

          @Order(10.0)
          public class Table extends AbstractTable {

            @Override
            protected boolean getConfiguredAutoResizeColumns() {
              return true;
            }

            public CashPaymentOnRegistrationColumn getCashPaymentOnRegistrationColumn() {
              return getColumnSet().getColumnByClass(CashPaymentOnRegistrationColumn.class);
            }

            public AmountColumn getAmountColumn() {
              return getColumnSet().getColumnByClass(AmountColumn.class);
            }

            public CurrencyColumn getCurrencyColumn() {
              return getColumnSet().getColumnByClass(CurrencyColumn.class);
            }

            public NameColumn getNameColumn() {
              return getColumnSet().getColumnByClass(NameColumn.class);
            }

            @Order(10.0)
            public class NameColumn extends AbstractStringColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return ScoutTexts.get("Name");
              }
            }

            @Order(20.0)
            public class AmountColumn extends AbstractDoubleColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("Amount");
              }
            }

            @Order(30.0)
            public class CurrencyColumn extends AbstractSmartColumn<Long> {

              @Override
              protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
                return CurrencyCodeType.class;

              }

              @Override
              protected boolean getConfiguredDisplayable() {
                return false;
              }

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("Currency");
              }
            }

            @Order(40.0)
            public class CashPaymentOnRegistrationColumn extends AbstractBooleanColumn {

              @Override
              protected String getConfiguredHeaderText() {
                return Texts.get("CashPaymentOnRegistration");
              }
            }
          }
        }
      }

      @Order(40.0)
      public class AdditionalInformationRunnerBox extends AbstractGroupBox {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("AdditionalInformationRunner");
        }

        @Order(10.0)
        public class AdditionalInformationRunnerField extends AbstractAdditionalInformationField {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("AdditionalInformationRunner");
          }

          @Override
          protected void handleEditCompleted(ITableRow row) throws ProcessingException {
            modifyRunner();
          }
        }
      }

      @Order(50.0)
      public class AdditionalInformationEntryBox extends AbstractGroupBox {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("AdditionalInformationEntry");
        }

        @Order(10.0)
        public class AdditionalInformationEntryField extends AbstractAdditionalInformationField {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("AdditionalInformationEntry");
          }

          @Override
          protected void handleEditCompleted(ITableRow row) throws ProcessingException {
            updateFeesField();
          }
        }
      }

    }

    @Order(118.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(120.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(130.0)
    public class CancelButton extends AbstractCancelButton {
    }

    @Order(140.0)
    public class AddRunnerButton extends AbstractLinkButton {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("AddRunner");
      }

      @Override
      protected void execInitField() throws ProcessingException {
        setLabel(getConfiguredLabel() + " (" + StringUtility.uppercase(FMilaUtility.NEW_ENTRY_KEY_STROKE) + ")");
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        addNewRunnerRow(true);
      }
    }

    @Order(150.0)
    public class AddEventButton extends AbstractLinkButton {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("AddEvent");
      }

      @Override
      protected void execInitField() throws ProcessingException {
        setLabel(getConfiguredLabel() + " (F7)");
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        addNewEvent();
      }

    }

    @Order(160.0)
    public class SaveAndNextButton extends AbstractLinkButton {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("SaveAndNext");
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        doOk();
        getDesktop().getOutline().getActivePage().reloadPage();
        EntryForm form = new EntryForm();
        form.startNew();
      }

      @Override
      protected void execInitField() throws ProcessingException {
        setLabel(getConfiguredLabel() + " (F9)");
      }

    }

    @Order(170.0)
    public class CancelAndNextButton extends AbstractLinkButton {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("CancelAndNext");
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        doCancel();
        EntryForm form = new EntryForm();
        form.startNew();
      }

      @Override
      protected void execInitField() throws ProcessingException {
        setLabel(getConfiguredLabel() + " (F10)");
      }

    }

    @Order(10.0)
    public class AddNewRunnerKeyStroke extends AbstractKeyStroke {

      @Override
      protected String getConfiguredKeyStroke() {
        return FMilaUtility.NEW_ENTRY_KEY_STROKE;
      }

      @Override
      protected void execAction() throws ProcessingException {
        getAddRunnerButton().doClick();
      }
    }

    @Order(20.0)
    public class AddNewEventKeyStroke extends AbstractKeyStroke {

      @Override
      protected String getConfiguredKeyStroke() {
        return "f7";
      }

      @Override
      protected void execAction() throws ProcessingException {
        getAddEventButton().doClick();
      }

    }

    @Order(30.0)
    public class SaveAndNextKeyStroke extends AbstractKeyStroke {

      @Override
      protected String getConfiguredKeyStroke() {
        return "f9";
      }

      @Override
      protected void execAction() throws ProcessingException {
        getSaveAndNextButton().doClick();
      }
    }

    @Order(40.0)
    public class CancelAndNextKeyStroke extends AbstractKeyStroke {

      @Override
      protected String getConfiguredKeyStroke() {
        return "f10";
      }

      @Override
      protected void execAction() throws ProcessingException {
        getCancelAndNextButton().doClick();
      }
    }

  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IEntryProcessService service = BEANS.get(IEntryProcessService.class);
      EntryFormData formData = new EntryFormData();
      exportFormData(formData);
      formData = BeanUtility.entryBean2formData(service.load(BeanUtility.entryFormData2bean(formData, eventConfiguration)), eventConfiguration);
      importFormData(formData);
      setEnabledPermission(new UpdateEntryPermission());

      doInit(false);

      if (getRacesField().getTable().getRowCount() > 0) {
        getRacesField().getTable().selectFirstRow();
        updateFormFromRunnerRow(getRacesField().getTable().getRow(0));
      }
    }

    @Override
    public void execStore() throws ProcessingException {
      IEntryProcessService service = BEANS.get(IEntryProcessService.class);
      EntryFormData formData = new EntryFormData();
      exportFormData(formData);
      formData = BeanUtility.entryBean2formData(service.store(BeanUtility.entryFormData2bean(formData, eventConfiguration)), eventConfiguration);
    }
  }

  private void doInit(boolean isNewHandler) throws ProcessingException {
    // Load Additional Info For Events
    additionalInformationEntryFormData = EntryFormUtility.validateAdditionalInformationEntry(additionalInformationEntryFormData, getAdditionalInformationEntryField(), getEventsField(), eventAdditionalInformationConfiguration);

    // Set <New Registration> if entry is not part of registration
    if (getRegistrationField().getValue() == null) {
      getRegistrationField().setValue(0L);
    }

    if (!isNewHandler) {
      // Calculate Fees
      updateFeesField();
    }

  }

  private void startInfoWindow() {
    if (InfoDisplayUtility.isActive()) {
      new InfoDisplayIdleJob(TEXTS.get("Welcome"), ClientSession.get()).schedule();
    }
  }

  private void updateInfoWindow() throws ProcessingException {
    if (InfoDisplayUtility.isActive()) {
      new EntryInfoDisplayUpdateJob(this, ClientSession.get()).schedule();
    }
  }

  private void idleInfoWindow() throws ProcessingException {
    if (InfoDisplayUtility.isActive()) {
      new InfoDisplayIdleJob(ClientSession.get()).schedule();
    }
  }

  private void updateFeesField() throws ProcessingException {
    EntryFormUtility.validateFeesTable(feeConfiguration, getFeesField(), getRacesField(), getEvtEntryField(), getCurrencyField(), getAdditionalInformationEntryField(), eventConfiguration);
  }

  private void updateFormFromRunnerRow(ITableRow row) throws ProcessingException {
    if (!getRunnerField().isValueParsing()) {
      try {
        // disable runner trigger
        getRunnerField().setValueChangeTriggerEnabled(false);
        setValueChangeTriggerOnFormFieldsEnabled(false);

        RacesField.Table teamTable = getRacesField().getTable();

        // apply rows to upper fields of dialog
        getRunnerField().setValue(teamTable.getRunnerNrColumn().getValue(row));
        getECardField().setValue(teamTable.getECardColumn().getValue(row));
        getLastNameField().setValue(teamTable.getLastNameColumn().getValue(row));
        getFirstNameField().setValue(teamTable.getFirstNameColumn().getValue(row));
        ((ClazzLookupCall) getClazzField().getLookupCall()).setEventNr(teamTable.getRaceEventColumn().getValue(row));
        getClazzField().setValue(teamTable.getLegColumn().getValue(row));

        // runner
        if (teamTable.getRaceBeanColumn().getValue(row) != null) {
          RunnerBean runner = teamTable.getRaceBeanColumn().getValue(row).getRunner();
          AddressBean address = teamTable.getRaceBeanColumn().getValue(row).getAddress();
          getSexField().setValue(runner.getSexUid());
          getYearField().setValue(runner.getYear());
          getClubField().setValue(runner.getClubNr());
          getCityField().setValue(address.getCityNr());
          updateRunnerAdditionalInformation(runner.getAddInfo());
        }
      }
      finally {
        getRunnerField().setValueChangeTriggerEnabled(true);
        setValueChangeTriggerOnFormFieldsEnabled(true);
      }

      updateInfoWindow();
    }
  }

  private void updateRunnerAdditionalInformation(AdditionalInformationBean addInfoBean) throws ProcessingException {
    getAdditionalInformationRunnerField().getTable().deleteAllRows();
    getAdditionalInformationRunnerField().getTable().discardAllDeletedRows();
    getAdditionalInformationRunnerField().importFormFieldData(BeanUtility.addInfoBean2FormData(addInfoBean), true);
  }

  private void addNewEvent() throws ProcessingException {
    getEventsField().getTable().deselectAllRows();
    getTabBox().setSelectedTab(getEventsBox());

    ITableRow newRow = getEventsField().getTable().createRow();
    newRow = getEventsField().getTable().addRow(newRow);

    getEventsField().getTable().selectRow(newRow.getRowIndex());
    getEventsField().getTable().requestFocusInCell(getEventsField().getTable().getEventNrColumn(), newRow);
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IEntryProcessService service = BEANS.get(IEntryProcessService.class);
      EntryFormData formData = new EntryFormData();
      exportFormData(formData);
      formData = BeanUtility.entryBean2formData(service.prepareCreate(BeanUtility.entryFormData2bean(formData, eventConfiguration)), eventConfiguration);
      importFormData(formData);

      doInit(true);
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      startInfoWindow();
    }

    @Override
    public void execStore() throws ProcessingException {
      IEntryProcessService service = BEANS.get(IEntryProcessService.class);
      EntryFormData formData = new EntryFormData();
      exportFormData(formData);
      formData = BeanUtility.entryBean2formData(service.create(BeanUtility.entryFormData2bean(formData, eventConfiguration)), eventConfiguration);
      importFormData(formData);
    }
  }
}
