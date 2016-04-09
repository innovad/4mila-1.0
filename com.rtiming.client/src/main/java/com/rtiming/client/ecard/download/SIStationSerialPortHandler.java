package com.rtiming.client.ecard.download;

import java.io.IOException;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.client.ecard.download.job.StatusUpdaterJob;
import com.rtiming.client.ecard.download.util.ByteUtility;
import com.rtiming.client.ecard.download.util.CRCCalculator;
import com.rtiming.serial.FMilaSerialPort;

public final class SIStationSerialPortHandler extends AbstractSISerialPortHandler {

  private final Object lock;
  private boolean isInitialized = false;
  private static Logger logger = LoggerFactory.getLogger(SIStationSerialPortHandler.class);

  public SIStationSerialPortHandler(IClientSession session, Object lock, FMilaSerialPort port) {
    super(session, port);
    this.lock = lock;
  }

  @Override
  protected void handleData(byte[] data) throws IOException, ProcessingException {

    if (data.length == 9 && (data[1] == -16) && (data[5] == 77)) {
      // complete answer is [2, -16, 3, 2, 69, 77, -128, -118, 3]
      // request station info
      int crc = CRCCalculator.crc(new byte[]{(byte) 0x83, (byte) 0x02, (byte) 0x74, (byte) 0x01});
      byte[] requestStationInfo = {(byte) 0x02, (byte) 0x83, (byte) 0x02, (byte) 0x74, (byte) 0x01, (byte) (crc >> 8 & 0xff), (byte) (crc & 0xff), (byte) 0x03};
      getPort().write(requestStationInfo);
    }
    else if (data.length == 10 && data[1] == -125) {
      // station info

      /* CN1, CN0 2 bytes stations code number 1...999
      0x74 1 byte data address in the system memory
      CPC 1 byte protocol configuration, bit mask value
      xxxxxxx1b   extended protocol
      xxxxxx1xb   auto send out
      xxxxx1xxb   handshake (only valid for card readout)
      xxx1xxxxb   access with password only
      1xxxxxxxb   read   out   SI-card   after   punch   (only  for   punch
      modes; depends on bit 2: auto send out or
      handshake)
      CRC1, CRC0 2 bytes 16 bit CRC value, computed including command byte and LEN
      (0x04, 0x14 for the request) */

      Long stationNr = ByteUtility.getLongFromBytes(data[3], data[4]);

      byte cpc = data[6];
      boolean extendedProtocol = (cpc & (1 << 0)) != 0;
      boolean autoSend = (cpc & (1 << 1)) != 0;
      boolean handShake = (cpc & (1 << 2)) != 0;
      boolean password = (cpc & (1 << 4)) != 0;
      boolean readOutAfterPunch = (cpc & (1 << 7)) != 0;
      logger.info("Checking SI-Station, Nr: " + stationNr + ", Extended Protocol: " + extendedProtocol + ", Password: " + password + ", Read-Out After Punch: " + readOutAfterPunch);

      if (!extendedProtocol) {
        throw new ProcessingException("Extended Protocol required. Please set SI Station to extended Protocol.");
      }
      if (autoSend) {
        throw new ProcessingException("AutoSend not supported. Please turn off AutoSend on SI Station.");
      }
      if (!handShake) {
        throw new ProcessingException("Handshake required. Please turn on Handshake on SI Station.");
      }

      synchronized (lock) {
        //set ready flag to true (so isInitialized() returns true)
        isInitialized = true;
        lock.notifyAll();
      }

      StatusUpdaterJob.setText(TEXTS.get(getSession().getLocale(), "ECardStationReady", String.valueOf(stationNr)), getSession());
    }
    else {
      // unknown
      throw new ProcessingException("Unsupported Operation: " + ByteUtility.dumpBytes(data));
    }

  }

  public boolean isInitialized() {
    return isInitialized;
  }

}
