package com.rtiming.client.ecard.download.processor;

import java.util.ArrayList;
import java.util.Date;

import org.eclipse.scout.rt.client.IClientSession;

import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.PunchFormData;

public class TestSICardProcessor extends AbstractSICardProcessor {

  private String eCardNo;
  private ArrayList<PunchFormData> punches = new ArrayList<PunchFormData>();

  public TestSICardProcessor(String eCardNo, String[] controlNos, ECardStationFormData station, FMilaSerialPort port, Date currentEvtZero, long eventNr, IClientSession session) {
    super(station, port, currentEvtZero, eventNr, session);
    this.eCardNo = eCardNo;
    long order = 0;
    for (String controlNo : controlNos) {
      PunchFormData punch = new PunchFormData();
      punch.getControlNo().setValue(controlNo);
      punch.getSortCode().setValue(order++);
      punches.add(punch);
    }
  }

  @Override
  protected long readSICardNrOfInsertedCard(byte[] data) {
    return Long.parseLong(eCardNo);
  }

  @Override
  protected String readSICardNoFromData(byte[] data) {
    return eCardNo;
  }

  @Override
  public String getECardNo() {
    return eCardNo;
  }

  @Override
  public ArrayList<PunchFormData> getControlData() {
    return punches;
  }

  @Override
  protected Long readClearTime(byte[] data) {
    return 1000L;
  }

  @Override
  protected Long readCheckTime(byte[] data) {
    return 1010L;
  }

  @Override
  protected Long readStartTime(byte[] data) {
    return 200L;
  }

  @Override
  protected Long readFinishTime(byte[] data) {
    return 5000L;
  }

  @Override
  protected byte[] getRequestDataCommand(int messageNr) {
    return new byte[]{1};
  }

  @Override
  public int getNumberOfDataMessages() {
    return 1;
  }

  @Override
  protected ArrayList<PunchFormData> readControlsFromData(byte[] data) {
    return punches;
  }

}
