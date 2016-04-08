package com.rtiming.client.ecard.download.processor;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.scout.rt.client.IClientSession;

import com.rtiming.client.ecard.download.util.ByteUtility;
import com.rtiming.client.ecard.download.util.SICardUtility;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.PunchFormData;

public class SICardV5Processor extends AbstractSICardProcessor {

  public SICardV5Processor(ECardStationFormData station, FMilaSerialPort port, Date currentEvtZero, long eventNr, IClientSession session) {
    super(station, port, currentEvtZero, eventNr, session);
  }

  @Override
  public long readSICardNrOfInsertedCard(byte[] data) {
    return ByteUtility.getLongFromBytes(data[5], data[6]) * 100000 + ByteUtility.getLongFromBytes(data[7], data[8]);
  }

  @Override
  protected String readSICardNoFromData(byte[] data) {
    long cardNr = ByteUtility.getLongFromBytes(data[4], data[5]);
    long nCardSeries = ByteUtility.getLongFromByte(data[6]);
    if (nCardSeries != 1) {
      cardNr += nCardSeries * 100000L;
    }
    return Long.toString(cardNr);
  }

  @Override
  protected byte[] getRequestDataCommand(int messageNr) {
    return new byte[]{(byte) 0x02, (byte) 0xb1, (byte) 0x00, (byte) 0xb1, (byte) 0x00, (byte) 0x03};
  }

  @Override
  public int getNumberOfDataMessages() {
    return 1;
  }

  @Override
  protected ArrayList<PunchFormData> readControlsFromData(byte[] data) {
    ArrayList<PunchFormData> punches = new ArrayList<PunchFormData>();

    int numPunches = (data[23] & 0xff) - 1;
    int numFullPunches = numPunches;

    /* x.y     - punching records
       x.1       control stations code number
       x.2, x.3  punching time in 12h format
       31-36   - punching records controls stations code number only */

    // 30 full punches
    if (numFullPunches > 30) {
      numFullPunches = 30;
    }
    for (int i = 0; i < numFullPunches; i++) {
      int add = (i / 5) * 16 + (i % 5) * 3;
      PunchFormData punch = SICardUtility.readV5Punch(data, add + 33, i + 1, getCurrentEvtZero());
      punches.add(punch);
    }

    // 6 punches without time
    if (numPunches > 30) {
      int numPartialPunches = numPunches - 30;
      if (numPartialPunches > 6) {
        numPartialPunches = 6;
      }
      for (int i = 0; i < numPartialPunches; i++) {
        int add = (i + 1) * 16;
        long code = ByteUtility.getLongFromByte(data[add]);
        PunchFormData punch = new PunchFormData();
        punch.getControlNo().setValue("" + code);
        punch.getSortCode().setValue(new Long(31 + i));
        punches.add(punch);
      }
    }
    return punches;
  }

  @Override
  protected Long readFinishTime(byte[] data) {
    PunchFormData punch = SICardUtility.readV5Punch(data, 20, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

  @Override
  protected Long readStartTime(byte[] data) {
    PunchFormData punch = SICardUtility.readV5Punch(data, 18, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

  @Override
  protected Long readCheckTime(byte[] data) {
    PunchFormData punch = SICardUtility.readV5Punch(data, 24, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

  @Override
  protected Long readClearTime(byte[] data) {
    PunchFormData punch = SICardUtility.readV5Punch(data, 24, 0, getCurrentEvtZero());
    return punch.getRawTime();
  }

}
