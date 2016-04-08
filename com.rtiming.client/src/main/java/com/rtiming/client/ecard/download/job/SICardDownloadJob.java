package com.rtiming.client.ecard.download.job;

import java.io.IOException;
import java.util.Date;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.FMilaClientSyncJob;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.infodisplay.InfoDisplayDownloadJob;
import com.rtiming.client.common.infodisplay.InfoDisplayUtility;
import com.rtiming.client.common.report.jrxml.SplitTimesReport;
import com.rtiming.client.common.ui.desktop.Desktop;
import com.rtiming.client.ecard.download.AbstractDownloadedECardsTablePage;
import com.rtiming.client.ecard.download.DownloadedECardForm;
import com.rtiming.client.ecard.download.processor.AbstractSICardProcessor;
import com.rtiming.client.ecard.download.util.ByteUtility;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table;
import com.rtiming.client.result.pos.IPosPrinter;
import com.rtiming.client.result.pos.PosPrinterManager;
import com.rtiming.client.result.pos.SplitTimesPosPrinter;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.ecard.download.DownloadedECardFormData;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.IDownloadedECardProcessService;
import com.rtiming.shared.ecard.download.IPunchProcessService;
import com.rtiming.shared.ecard.download.PunchFormData;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.IRaceService;
import com.rtiming.shared.runner.IRunnerProcessService;

public class SICardDownloadJob extends FMilaClientSyncJob {

  private static IScoutLogger logger = ScoutLogManager.getLogger(Desktop.class);

  private final AbstractSICardProcessor processor;
  private final ECardStationFormData station;
  private final IClientSession session;
  private final FMilaSerialPort port;
  private final long eventNr;
  private final Date currentEvtZero;
  private final long downloadModeUid;

  public SICardDownloadJob(ECardStationFormData station, IClientSession session, FMilaSerialPort port, long downloadModeUid, Date currentEvtZero, long eventNr, AbstractSICardProcessor processor) {
    super("SICardDownloadJob", session);
    this.station = station;
    this.session = session;
    this.processor = processor;
    this.port = port;
    this.eventNr = eventNr;
    this.downloadModeUid = downloadModeUid;
    this.currentEvtZero = currentEvtZero;
  }

  @Override
  protected void runVoid() throws Exception {

    // Find Race and ECard
    Long raceNr = BEANS.get(IRaceProcessService.class).findRaceNr(eventNr, processor.getECardNo());
    IECardProcessService eCardService = BEANS.get(IECardProcessService.class);
    RtEcard ecard = eCardService.findECard(processor.getECardNo());

    // Create ECard if it is not in the database
    if (ecard.getKey() == null) {
      ecard.setRentalCard(false);
      ecard = eCardService.create(ecard);
    }

    // Punch Session
    DownloadedECardFormData ecardDownload = new DownloadedECardFormData();
    ecardDownload = BEANS.get(IDownloadedECardProcessService.class).prepareCreate(ecardDownload);
    ecardDownload.getECard().setValue(ecard.getKey().getId());
    ecardDownload.getRace().setValue(raceNr);
    ecardDownload.setRawClear(processor.getClearTime());
    ecardDownload.setRawCheck(processor.getCheckTime());
    ecardDownload.setRawStart(processor.getStartTime());
    ecardDownload.setRawFinish(processor.getFinishTime());
    ecardDownload.getECardStation().setValue(station.getECardStationNr());
    ecardDownload.getRawData().setValue(ByteUtility.dumpBytes(processor.getRawData()));
    ecardDownload = BEANS.get(IDownloadedECardProcessService.class).create(ecardDownload);

    // Punches
    for (PunchFormData punch : processor.getControlData()) {
      punch.getPunchSession().setValue(ecardDownload.getPunchSessionNr());
      BEANS.get(IPunchProcessService.class).create(punch);
    }

    // handle missing race
    if (raceNr == null && downloadModeUid == ECardStationDownloadModusCodeType.DownloadSplitTimesAndRegisterCode.ID) {
      // no race => show registration form
      Long runnerNr = BEANS.get(IRunnerProcessService.class).findRunnerByECard(processor.getECardNo());

      EntryForm form = new EntryForm();
      form.startNew();
      if (runnerNr != null) {
        form.getRunnerField().setValue(runnerNr);
        form.setDownloadStationEntry(true);
      }
      else {
        RtEcard eCard = BEANS.get(IECardProcessService.class).findECard(processor.getECardNo());
        if (eCard.getKey().getId() != null) {
          form.setDownloadStationEntry(true);
          form.getECardField().setValue(eCard.getKey().getId());
        }
      }
      Long[] classNrs = BEANS.get(IDownloadedECardProcessService.class).matchClass(ecardDownload.getPunchSessionNr(), processor.getControlData(), eventNr, currentEvtZero, form.getSexField().getValue(), form.getYearField().getValue());
      if (classNrs.length >= 1) {
        form.getClazzField().setValue(classNrs[0]);
      }
      form.waitFor();

      if (form.isFormStored()) {
        Table raceTable = form.getRacesField().getTable();
        for (int i = 0; i < raceTable.getRowCount(); i++) {
          boolean sameEvent = CompareUtility.equals(eventNr, raceTable.getRaceEventColumn().getValue(i));
          boolean sameECard = CompareUtility.equals(ecard.getKey().getId(), raceTable.getECardColumn().getValue(i));
          if (sameEvent && sameECard) {
            raceNr = raceTable.getRaceNrColumn().getValue(i);
            break;
          }
        }
        if (raceNr == null) {
          FMilaClientUtility.showOkMessage(TEXTS.get("ApplicationName"), null, TEXTS.get("ECardEntryCanceledMessage"));
          handleCanceledEntry(ecardDownload);
          return;
        }
        else {
          // write race nr to database
          ecardDownload.getRace().setValue(raceNr);
          BEANS.get(IDownloadedECardProcessService.class).store(ecardDownload);
        }
      }
      else {
        handleCanceledEntry(ecardDownload);
        return;
      }
    }
    else if (raceNr == null && downloadModeUid == ECardStationDownloadModusCodeType.DownloadSplitTimesAndAssignRaceCode.ID) {
      // no race => show downloaded e-card form for race selection
      DownloadedECardForm form = new DownloadedECardForm();
      form.setPunchSessionNr(ecardDownload.getPunchSessionNr());
      form.startModify();
      form.waitFor();
      // fetch new assigned race nr
      raceNr = form.getRaceField().getValue();
    }

    // Validate
    if (raceNr != null) {
      BEANS.get(IRaceService.class).validateAndPersistRace(raceNr);
    }

    // Reload Info Display
    if (InfoDisplayUtility.isActive()) {
      new InfoDisplayDownloadJob(raceNr, getClientSession()).schedule();
    }

    // Print
    try {
      if (raceNr != null && !StringUtility.isNullOrEmpty(station.getPrinter().getValue())) {
        SplitTimesReport.printSplitTimesReport(new Long[]{raceNr}, station.getPrinter().getValue());
      }
      else if (raceNr != null && !StringUtility.isNullOrEmpty(station.getPosPrinter().getValue())) {
        IPosPrinter printer = PosPrinterManager.get(station.getPosPrinter().getValue());
        SplitTimesPosPrinter.printSplitTimesReport(raceNr, printer);
      }
    }
    catch (final Exception e) {
      logger.error("Failed to print split times: " + e.getMessage(), e);
      new FMilaClientSyncJob("msgbox", session) {
        @Override
        protected void runVoid() throws Exception {
          FMilaClientUtility.showOkMessage(TEXTS.get("ApplicationName"), null, TEXTS.get("FailedPrintingSplitTimesMessage") + FMilaUtility.LINE_SEPARATOR + " (" + e.getMessage() + ")");
        }
      }.schedule();
    }

    // Reload GUI
    IPage activePage = session.getDesktop().getOutline().getActivePage();
    if (activePage != null) {
      activePage.reloadPage();
      if (activePage instanceof AbstractDownloadedECardsTablePage) {
        AbstractDownloadedECardsTablePage downloadedECardsTablePage = (AbstractDownloadedECardsTablePage) activePage;
        if (downloadedECardsTablePage.getTable().getRowCount() > 0 && CompareUtility.equals(raceNr, downloadedECardsTablePage.getTable().getRaceNrColumn().getValue(0))) {
          downloadedECardsTablePage.getTable().selectFirstRow();
        }
      }
    }

  }

  private void handleCanceledEntry(DownloadedECardFormData ecardDownload) throws ProcessingException, IOException {
    BEANS.get(IDownloadedECardProcessService.class).delete(ecardDownload);
  }

}
