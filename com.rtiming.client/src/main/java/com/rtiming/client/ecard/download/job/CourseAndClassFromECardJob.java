package com.rtiming.client.ecard.download.job;

import java.io.IOException;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.FMilaClientSyncJob;
import com.rtiming.client.ecard.download.CourseAndClassFromECardForm;
import com.rtiming.client.ecard.download.processor.AbstractSICardProcessor;
import com.rtiming.serial.FMilaSerialPort;

public class CourseAndClassFromECardJob extends FMilaClientSyncJob {

  private final AbstractSICardProcessor processor;
  private final IClientSession session;
  private final FMilaSerialPort port;

  public CourseAndClassFromECardJob(IClientSession session, FMilaSerialPort port, AbstractSICardProcessor processor) {
    super("CourseAndClassFromECardJob", session);
    this.session = session;
    this.processor = processor;
    this.port = port;
  }

  @Override
  protected void runVoid() throws Exception {
    CourseAndClassFromECardForm form = new CourseAndClassFromECardForm();
    form.setControls(processor.getControlData());
    form.startNew();

    // Beep
    doRemoveECard();
  }

  private void doRemoveECard() throws IOException {
    port.write((byte) 6);
    StatusUpdaterJob.setText(TEXTS.get(session.getLocale(), "RemoveECardMessage"), session);
  }
}
