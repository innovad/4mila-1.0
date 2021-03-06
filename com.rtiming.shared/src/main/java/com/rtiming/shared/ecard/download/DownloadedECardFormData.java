package com.rtiming.shared.ecard.download;

import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.ecard.download.DownloadedECardForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class DownloadedECardFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public Check getCheck() {
    return getFieldByClass(Check.class);
  }

  public Clear getClear() {
    return getFieldByClass(Clear.class);
  }

  public ECard getECard() {
    return getFieldByClass(ECard.class);
  }

  public ECardStation getECardStation() {
    return getFieldByClass(ECardStation.class);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public EvtDownload getEvtDownload() {
    return getFieldByClass(EvtDownload.class);
  }

  public Finish getFinish() {
    return getFieldByClass(Finish.class);
  }

  /**
   * access method for property PunchSessionNr.
   */
  public Long getPunchSessionNr() {
    return getPunchSessionNrProperty().getValue();
  }

  /**
   * access method for property PunchSessionNr.
   */
  public void setPunchSessionNr(Long punchSessionNr) {
    getPunchSessionNrProperty().setValue(punchSessionNr);
  }

  public PunchSessionNrProperty getPunchSessionNrProperty() {
    return getPropertyByClass(PunchSessionNrProperty.class);
  }

  public Race getRace() {
    return getFieldByClass(Race.class);
  }

  /**
   * access method for property RawCheck.
   */
  public Long getRawCheck() {
    return getRawCheckProperty().getValue();
  }

  /**
   * access method for property RawCheck.
   */
  public void setRawCheck(Long rawCheck) {
    getRawCheckProperty().setValue(rawCheck);
  }

  public RawCheckProperty getRawCheckProperty() {
    return getPropertyByClass(RawCheckProperty.class);
  }

  /**
   * access method for property RawClear.
   */
  public Long getRawClear() {
    return getRawClearProperty().getValue();
  }

  /**
   * access method for property RawClear.
   */
  public void setRawClear(Long rawClear) {
    getRawClearProperty().setValue(rawClear);
  }

  public RawClearProperty getRawClearProperty() {
    return getPropertyByClass(RawClearProperty.class);
  }

  public RawData getRawData() {
    return getFieldByClass(RawData.class);
  }

  /**
   * access method for property RawFinish.
   */
  public Long getRawFinish() {
    return getRawFinishProperty().getValue();
  }

  /**
   * access method for property RawFinish.
   */
  public void setRawFinish(Long rawFinish) {
    getRawFinishProperty().setValue(rawFinish);
  }

  public RawFinishProperty getRawFinishProperty() {
    return getPropertyByClass(RawFinishProperty.class);
  }

  /**
   * access method for property RawStart.
   */
  public Long getRawStart() {
    return getRawStartProperty().getValue();
  }

  /**
   * access method for property RawStart.
   */
  public void setRawStart(Long rawStart) {
    getRawStartProperty().setValue(rawStart);
  }

  public RawStartProperty getRawStartProperty() {
    return getPropertyByClass(RawStartProperty.class);
  }

  public Start getStart() {
    return getFieldByClass(Start.class);
  }

  public static class Check extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class Clear extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class ECard extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class ECardStation extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class Event extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class EvtDownload extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class Finish extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class PunchSessionNrProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class Race extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class RawCheckProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class RawClearProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class RawData extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class RawFinishProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class RawStartProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class Start extends AbstractValueFieldData<Date> {

    private static final long serialVersionUID = 1L;
  }
}
