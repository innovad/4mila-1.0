package com.rtiming.client.ecard.download.processor;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;

import com.rtiming.client.ecard.download.util.ByteUtility;
import com.rtiming.client.ecard.download.util.SICardUtility;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.PunchFormData;

public class SICardV6Processor extends AbstractSICardProcessor {

  public SICardV6Processor(ECardStationFormData station, FMilaSerialPort port, Date currentEvtZero, long eventNr, IClientSession session) {
    super(station, port, currentEvtZero, eventNr, session);
  }

  private int numberOfDataMessages = 0;
  private MODEL model;

  public enum MODEL {
    SIX, SIXSTAR
  }

  public void setModel(MODEL type) {
    this.model = type;
  }

  public MODEL getModel() {
    return model;
  }

  @Override
  protected long readSICardNrOfInsertedCard(byte[] data) {
    return ByteUtility.getLongFromBytes(data[5], data[6], data[7], data[8]);
  }

  @Override
  public int getNumberOfDataMessages() {
    return numberOfDataMessages;
  }

  public void setNumberOfDataMessages(int numberOfDataMessages) {
    this.numberOfDataMessages = numberOfDataMessages;
  }

  @Override
  protected byte[] getRequestDataCommand(int messageNr) {
    if (messageNr <= 0 || messageNr > 8) {
      throw new IllegalArgumentException("messageNr must be between 1 and 8: " + messageNr);
    }
    if (messageNr == 1) {
      // this is always the get data configuration command (check if 6 or 6*)
      return new byte[]{(byte) 0x02, (byte) 0x83, (byte) 0x02, (byte) 0x33, (byte) 0x01, (byte) 0x16, (byte) 0x11, (byte) 0x03}; // request data configuration
    }
    else if (messageNr == 2) {
      // request data 0 (for both 6 and 6*)
      // data block 0 contains start, finish, clear, check, card id etc.
      return new byte[]{(byte) 0x02, (byte) 0xe1, (byte) 0x01, (byte) 0x00, (byte) 0x46, (byte) 0x0A, (byte) 0x03}; // 0
    }
    // data block 1 contains user address information, this is not supported so far
    else if (getModel() == null) {
      throw new IllegalArgumentException("Model must be set");
    }
    else if (messageNr == 3 && getModel().equals(MODEL.SIXSTAR)) {
      // request data 2 (for 6*)
      return new byte[]{(byte) 0x02, (byte) 0xe1, (byte) 0x01, (byte) 0x2, (byte) 0x46, (byte) 0x0A, (byte) 0x03}; // 2
    }
    else if (messageNr == 4 && getModel().equals(MODEL.SIXSTAR)) {
      // request data 3 (for 6*)
      return new byte[]{(byte) 0x02, (byte) 0xe1, (byte) 0x01, (byte) 0x3, (byte) 0x46, (byte) 0x0A, (byte) 0x03}; // 3
    }
    else if (messageNr == 5 && getModel().equals(MODEL.SIXSTAR)) {
      // request data 4 (for 6*)
      return new byte[]{(byte) 0x02, (byte) 0xe1, (byte) 0x01, (byte) 0x4, (byte) 0x46, (byte) 0x0A, (byte) 0x03}; // 4
    }
    else if (messageNr == 6 && getModel().equals(MODEL.SIXSTAR)) {
      // request data 5 (for 6*)
      return new byte[]{(byte) 0x02, (byte) 0xe1, (byte) 0x01, (byte) 0x5, (byte) 0x46, (byte) 0x0A, (byte) 0x03}; // 5
    }
    else if ((messageNr == 3 && getModel().equals(MODEL.SIX)) || (messageNr == 7 && getModel().equals(MODEL.SIXSTAR))) {
      // request data 6
      return new byte[]{(byte) 0x02, (byte) 0xe1, (byte) 0x01, (byte) 0x6, (byte) 0x40, (byte) 0x0A, (byte) 0x03}; // 6
    }
    else if ((messageNr == 4 && getModel().equals(MODEL.SIX)) || (messageNr == 8 && getModel().equals(MODEL.SIXSTAR))) {
      // request data 7
      return new byte[]{(byte) 0x02, (byte) 0xe1, (byte) 0x01, (byte) 0x7, (byte) 0x41, (byte) 0x0A, (byte) 0x03}; // 7
    }
    throw new IllegalArgumentException("Invalid message nr: " + messageNr);
  }

  @Override
  protected Long readCheckTime(byte[] data) throws ProcessingException {
    PunchFormData punch = SICardUtility.readV6Punch(data, 28, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

  @Override
  protected Long readClearTime(byte[] data) throws ProcessingException {
    PunchFormData punch = SICardUtility.readV6Punch(data, 32, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

  @Override
  protected ArrayList<PunchFormData> readControlsFromData(byte[] data) throws ProcessingException {
    ArrayList<PunchFormData> punches = new ArrayList<PunchFormData>();

    long numberOfPunches = getNumberOfPunches(data);
    for (int i = 0; i < numberOfPunches; i++) {
      PunchFormData punch = SICardUtility.readV6Punch(data, 128 + i * 4, i, getCurrentEvtZero());
      punches.add(punch);
    }

    return punches;
  }

  public long getNumberOfPunches(byte[] data) throws ProcessingException {
    if (data == null || data.length < 128) {
      throw new IllegalArgumentException("SICard6 data block expected, but was null or too short");
    }
    long result = ByteUtility.getLongFromByte(data[18]);
    if (CompareUtility.equals(MODEL.SIX, getModel())) {
      if (result < 0 || result > 64) {
        throw new ProcessingException("Illegal number of controls for SICard 6: " + result);
      }
    }
    else if (CompareUtility.equals(MODEL.SIXSTAR, getModel())) {
      if (result < 0 || result > 192) {
        throw new ProcessingException("Illegal number of controls for SICard 6*: " + result);
      }
    }
    else {
      throw new ProcessingException("Cannot check number of controls, model not set");
    }
    return result;
  }

  @Override
  protected Long readFinishTime(byte[] data) throws ProcessingException {
    PunchFormData punch = SICardUtility.readV6Punch(data, 20, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

  @Override
  protected String readSICardNoFromData(byte[] data) {
    return Long.toString(ByteUtility.getLongFromBytes(data[10], data[11], data[12], data[13]));
  }

  @Override
  protected Long readStartTime(byte[] data) throws ProcessingException {
    PunchFormData punch = SICardUtility.readV6Punch(data, 24, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

}
