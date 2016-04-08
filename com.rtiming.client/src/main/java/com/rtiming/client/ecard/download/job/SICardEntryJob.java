package com.rtiming.client.ecard.download.job;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.FMilaClientSyncJob;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.runner.IRunnerProcessService;

/**
 *
 */
public class SICardEntryJob extends FMilaClientSyncJob {

  private final String eCardNo;
  private final IClientSession session;
  private final FMilaSerialPort port;

  public SICardEntryJob(IClientSession session, FMilaSerialPort port, String eCardNo) {
    super("SICardEntryJob", session);
    this.session = session;
    this.eCardNo = eCardNo;
    this.port = port;
  }

  @Override
  protected void runVoid() throws Exception {
    IECardProcessService eCardService = BEANS.get(IECardProcessService.class);

    Long runnerNr = BEANS.get(IRunnerProcessService.class).findRunnerByECard(eCardNo);
    RtEcard ecard = eCardService.findECard(eCardNo);

    // Create ECard if it is not in the database
    if (ecard.getKey() == null) {
      ecard.setRentalCard(false);
      ecard = eCardService.create(ecard);
    }

    EntryForm form = session.getDesktop().findForm(EntryForm.class);
    boolean formStarted = false;
    if (form != null) {
      if (form.getRunnerField().getValue() == null && form.getECardField().getValue() == null) {
        setRunnerAndOrECard(form, runnerNr, ecard.getKey().getId());
      }
      else {
        if (FMilaClientUtility.showYesNoMessage(null, null, TEXTS.get("OverwriteQuestion")).getSeverity() == MessageBox.YES_OPTION) {
          setRunnerAndOrECard(form, runnerNr, ecard.getKey().getId());
        }
      }
    }
    else {
      form = new EntryForm();
      form.startNew();
      setRunnerAndOrECard(form, runnerNr, ecard.getKey().getId());
      formStarted = true;
    }

    // Beep
    port.write((byte) 6);
    StatusUpdaterJob.setText(TEXTS.get(session.getLocale(), "RemoveECardMessage"), session);

    if (formStarted) {
      form.waitFor();
      if (form.isFormStored()) {
        IPage activePage = session.getDesktop().getOutline().getActivePage();
        if (activePage != null) {
          activePage.reloadPage();
        }
      }
    }
  }

  private void setRunnerAndOrECard(EntryForm form, Long runnerNr, Long eCardNr) {
    form.setDownloadStationEntry(true);
    if (runnerNr != null) {
      form.getRunnerField().setValue(runnerNr);
    }
    if (eCardNr != null) {
      form.getECardField().setValue(eCardNr);
    }
  }

}
