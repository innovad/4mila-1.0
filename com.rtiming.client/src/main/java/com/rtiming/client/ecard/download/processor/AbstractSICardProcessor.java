package com.rtiming.client.ecard.download.processor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.FMilaClientSyncJob;
import com.rtiming.client.ecard.download.job.CourseAndClassFromECardJob;
import com.rtiming.client.ecard.download.job.SICardDownloadJob;
import com.rtiming.client.ecard.download.job.SICardEntryJob;
import com.rtiming.client.ecard.download.job.StatusUpdaterJob;
import com.rtiming.client.ecard.download.util.ByteUtility;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.PunchFormData;

public abstract class AbstractSICardProcessor {

  private final IClientSession session;
  private final FMilaSerialPort port;
  private final long mode;
  private final Date currentEvtZero;
  private final long eventNr;
  private final ECardStationFormData station;

  private String eCardNo;
  private ArrayList<PunchFormData> controlData; // punches with control no. and time
  private Long clearTime; // clear time if available
  private Long checkTime; // check time if available
  private Long startTime; // start time if available
  private Long finishTime; // finish time if available
  private byte[] rawData; // raw data in blocks
  private int dataMessagesCounter = 0;

  public AbstractSICardProcessor(ECardStationFormData station, FMilaSerialPort port, Date currentEvtZero, long eventNr, IClientSession session) {
    super();
    this.station = station;
    this.port = port;
    this.mode = station.getModus().getValue();
    this.session = session;
    this.currentEvtZero = currentEvtZero;
    this.eventNr = eventNr;
  }

  /**
   * SI Card was inserted into station
   * 
   * @param data
   * @throws IOException
   */
  public final void handleCardInserted(byte[] data) throws IOException, ProcessingException {
    long cardNr = readSICardNrOfInsertedCard(data);
    String cardNo = String.valueOf(cardNr);
    setECardNo(cardNo);
    StatusUpdaterJob.setText(TEXTS.get(session.getLocale(), "ReadECard", cardNo), session);

    if (CompareUtility.equals(mode, ECardStationDownloadModusCodeType.EntryCode.ID)) {
      SICardEntryJob job = new SICardEntryJob(session, port, "" + cardNr);
      job.start();
    }
    else {
      // request more data
      StatusUpdaterJob.setText(TEXTS.get(session.getLocale(), "RequestECardData"), session);
      port.write(getRequestDataCommand(dataMessagesCounter + 1));
    }
  }

  /**
   * SI Card data is processed (in case of newer cards, this step may be repeated)
   * 
   * @param data
   * @throws IOException
   */
  public final void handleProcessData(byte[] data) throws IOException, ProcessingException {
    if (getRawData() == null) {
      setRawData(data);
    }
    else {
      data = ByteUtility.concatArrays(getRawData(), data);
      setRawData(data);
    }
    dataMessagesCounter++;
    // block 0
    if (dataMessagesCounter == getNumberOfDataMessages()) {
      StatusUpdaterJob.setText(TEXTS.get(session.getLocale(), "ProcessData"), session);
      // read
      setECardNo(readSICardNoFromData(data));
      setControlData(readControlsFromData(data));
      setClearTime(readClearTime(data));
      setCheckTime(readCheckTime(data));
      setStartTime(readStartTime(data));
      setFinishTime(readFinishTime(data));
      if (mode == ECardStationDownloadModusCodeType.CourseAndClassFromECardCode.ID) {
        FMilaClientSyncJob job = new CourseAndClassFromECardJob(session, port, this);
        job.start();
      }
      else {
        removeSICard();
        FMilaClientSyncJob job = new SICardDownloadJob(station, session, port, mode, getCurrentEvtZero(), eventNr, this);
        job.start();
      }
    }
    // request next block
    else {
      byte[] requestDataCommand = getRequestDataCommand(dataMessagesCounter + 1);
      if (requestDataCommand != null) {
        port.write(requestDataCommand);
      }
    }
  }

  private void removeSICard() throws IOException {
    port.write((byte) 6);
    StatusUpdaterJob.setText(TEXTS.get(session.getLocale(), "RemoveECardMessage"), session);
  }

  /**
   * SI Card was removed from the station
   * 
   * @param data
   */
  public final void handleCardRemoved(byte[] data) {
    StatusUpdaterJob.setText(TEXTS.get(session.getLocale(), "ECardRemovedMessage"), session);
  }

  protected abstract long readSICardNrOfInsertedCard(byte[] data);

  protected abstract String readSICardNoFromData(byte[] data);

  protected abstract Long readClearTime(byte[] data) throws ProcessingException;

  protected abstract Long readCheckTime(byte[] data) throws ProcessingException;

  protected abstract Long readStartTime(byte[] data) throws ProcessingException;

  protected abstract Long readFinishTime(byte[] data) throws ProcessingException;

  protected abstract byte[] getRequestDataCommand(int messageNr) throws ProcessingException;

  public abstract int getNumberOfDataMessages() throws ProcessingException;

  protected abstract ArrayList<PunchFormData> readControlsFromData(byte[] data) throws ProcessingException;

  public String getECardNo() {
    return this.eCardNo;
  }

  public void setECardNo(String number) {
    this.eCardNo = number;
  }

  public ArrayList<PunchFormData> getControlData() {
    return controlData;
  }

  public void setControlData(ArrayList<PunchFormData> controlData) {
    this.controlData = controlData;
  }

  public Long getStartTime() {
    return startTime;
  }

  public void setStartTime(Long startTime) {
    this.startTime = startTime;
  }

  public Long getFinishTime() {
    return finishTime;
  }

  public void setFinishTime(Long finishTime) {
    this.finishTime = finishTime;
  }

  public byte[] getRawData() {
    return rawData;
  }

  public void setRawData(byte[] rawData) {
    this.rawData = rawData;
  }

  public Date getCurrentEvtZero() {
    return currentEvtZero;
  }

  public Long getClearTime() {
    return clearTime;
  }

  public void setClearTime(Long clearTime) {
    this.clearTime = clearTime;
  }

  public Long getCheckTime() {
    return checkTime;
  }

  public void setCheckTime(Long checkTime) {
    this.checkTime = checkTime;
  }

}
