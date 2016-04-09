package com.rtiming.client.ecard.download;

import java.io.IOException;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.client.common.infodisplay.InfoDisplayIdleJob;
import com.rtiming.client.common.infodisplay.InfoDisplayUtility;
import com.rtiming.client.ecard.download.job.StatusUpdaterJob;
import com.rtiming.client.ecard.download.util.ByteUtility;
import com.rtiming.client.ecard.download.util.CRCCalculator;
import com.rtiming.serial.FMilaSerialEventListener;
import com.rtiming.serial.FMilaSerialPort;

public class SISerialPortListener implements FMilaSerialEventListener {

  private static Logger logger = LoggerFactory.getLogger(SISerialPortListener.class);
  private final IClientSession session;
  private FMilaSerialPort port;
  private AbstractSISerialPortHandler handler;
  private byte[] lastPartialMessage;

  public SISerialPortListener(IClientSession session) {
    this.session = session;
  }

  public void installHandler(AbstractSISerialPortHandler serialPortHandler) {
    this.handler = serialPortHandler;
  }

  @Override
  public void serialEvent(byte[] data) throws IOException {
    StatusUpdaterJob.setText(TEXTS.get(session.getLocale(), "ActivityAtECardStation"), session);
    try {
      if (data.length > 137 && data.length % 137 == 0) {
        // message size 137 or multiples
        for (int k = 0; k < data.length; k = k + 137) {
          checkCRC(ByteUtility.getSubarray(data, k, 137));
        }
      }
      else if (data.length % 137 > 12 && data.length != 136) {
        // with SI-Cards 8/9 sometimes only partial messages are sent
        // if a message size is 'in between', we wait for the rest of the message
        if (lastPartialMessage != null) {
          data = ByteUtility.concatArrays(lastPartialMessage, data);
          lastPartialMessage = null;
          for (int k = 0; k < data.length; k = k + 137) {
            checkCRC(ByteUtility.getSubarray(data, k, 137));
          }
        }
        else {
          StatusUpdaterJob.setText("Partial Read, waiting for another message)", session);
          lastPartialMessage = data;
          return;
        }
      }
      else {
        // message size 12 or 136
        checkCRC(data);
      }
      handler.handleData(data);
    }
    catch (Exception e) {
      StatusUpdaterJob.setText("Error: " + e.getMessage(), session);
      logger.error("Exception", e);
    }
  }

  private void checkCRC(byte[] data) throws ProcessingException {
    if (data == null || data.length < 4) {
      if (data != null) {
        if (data.length == 1 && data[0] == 21) {
          showInfoWindowError();
          throw new ProcessingException(TEXTS.get("ReadOutError", "NAK"));
        }
      }
      showInfoWindowError();
      throw new ProcessingException(TEXTS.get("ReadOutError", "Message empty or too short"));
    }

    long crcCalculated = CRCCalculator.crc(ByteUtility.getSubarray(data, 1, data.length - 4));
    long crcRead = ByteUtility.getLongFromBytes(data[data.length - 3], data[data.length - 2]);

    if (!CompareUtility.equals(crcCalculated, crcRead)) {
      showInfoWindowError();
      throw new ProcessingException(TEXTS.get("ReadOutError", "CRC Error " + ByteUtility.dumpBytes(new byte[]{new Long(crcCalculated).byteValue()}) + " != " + ByteUtility.dumpBytes(new byte[]{new Long(crcRead).byteValue()})));
    }
  }

  private void showInfoWindowError() {
    if (InfoDisplayUtility.isActive()) {
      new InfoDisplayIdleJob(TEXTS.get(session.getLocale(), "ReadOutError").replace("({0})", ""), session).schedule();
    }
  }

  public FMilaSerialPort getPort() {
    return port;
  }

  public IClientSession getSession() {
    return session;
  }

}
