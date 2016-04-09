package com.rtiming.client.ecard.download.processor;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.ecard.download.util.ByteUtility;
import com.rtiming.client.ecard.download.util.SICardUtility;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.ecard.ECardTypeCodeType;
import com.rtiming.shared.ecard.ECardUtility;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.PunchFormData;

public class SICardV8FamilyProcessor extends AbstractSICardProcessor {

  private static final byte[] COMMAND_REQUEST_DATAV8_0 = new byte[]{(byte) 0x02, (byte) 0xef, (byte) 0x01, (byte) 0x00, (byte) 0xe2, (byte) 0x09, (byte) 0x03};
  private static final byte[] COMMAND_REQUEST_DATAV8_1 = new byte[]{(byte) 0x02, (byte) 0xef, (byte) 0x01, (byte) 0x01, (byte) 0xe3, (byte) 0x09, (byte) 0x03};
  private static final byte[] COMMAND_REQUEST_DATAV10_04567 = new byte[]{(byte) 0x02, (byte) 0xef, (byte) 0x01, (byte) 0x08, (byte) 0xea, (byte) 0x09, (byte) 0x03};

  public SICardV8FamilyProcessor(ECardStationFormData station, FMilaSerialPort port, Date currentEvtZero, long eventNr, IClientSession session) {
    super(station, port, currentEvtZero, eventNr, session);
  }

  @Override
  public long readSICardNrOfInsertedCard(byte[] data) {
    return ByteUtility.getLongFromBytes(data[5], data[6], data[7], data[8]) & 16777215L; // hidden secrets :-)
  }

  @Override
  protected byte[] getRequestDataCommand(int messageNr) throws ProcessingException {
    if (isType8or9()) {
      if (messageNr == 1) {
        return COMMAND_REQUEST_DATAV8_0;
      }
      return COMMAND_REQUEST_DATAV8_1;
    }
    else {
      if (messageNr == 1) {
        return COMMAND_REQUEST_DATAV10_04567;
      }
      return null;
    }
  }

  private boolean isType8or9() throws ProcessingException {
    return ECardUtility.getType(getECardNo()) == ECardTypeCodeType.SICard8Code.ID || ECardUtility.getType(getECardNo()) == ECardTypeCodeType.SICard9Code.ID;
  }

  @Override
  protected String readSICardNoFromData(byte[] data) {
    return Long.toString(ByteUtility.getLongFromBytes(data[24], data[25], data[26], data[27]) & 16777215L); // hidden secrets :-)
  }

  @Override
  public int getNumberOfDataMessages() throws ProcessingException {
    if (isType8or9()) {
      return 2;
    }
    return 5;
  }

  @Override
  protected ArrayList<PunchFormData> readControlsFromData(byte[] data) throws ProcessingException {
    ArrayList<PunchFormData> punches = new ArrayList<PunchFormData>();

    int cardType = data[24] & 0xff;
    int numberOfPunches = data[22] & 0xff;
    int firstPunch = 56;

    if (cardType == 15) {
      // SI Card 10/11/SIAC
      firstPunch = 128;
    }
    else if (cardType == 2) {
      // SI Card 8 30 Records
      firstPunch = 136;
    }

    for (int i = 0; i < numberOfPunches; i++) {
      // same punch structure as V6
      punches.add(SICardUtility.readV6Punch(data, firstPunch + i * 4, i + 1, getCurrentEvtZero()));
    }

    return punches;
  }

  @Override
  protected Long readFinishTime(byte[] data) throws ProcessingException {
    // same punch structure as V6
    PunchFormData punch = SICardUtility.readV6Punch(data, 16, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

  @Override
  protected Long readStartTime(byte[] data) throws ProcessingException {
    // same punch structure as V6
    PunchFormData punch = SICardUtility.readV6Punch(data, 12, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

  @Override
  protected Long readCheckTime(byte[] data) throws ProcessingException {
    // same punch structure as V6
    PunchFormData punch = SICardUtility.readV6Punch(data, 8, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

  @Override
  protected Long readClearTime(byte[] data) throws ProcessingException {
    // same punch structure as V6
    PunchFormData punch = SICardUtility.readV6Punch(data, 8, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

}
