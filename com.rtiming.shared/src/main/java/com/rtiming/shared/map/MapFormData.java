package com.rtiming.shared.map;

import java.math.BigDecimal;
import java.util.Date;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.dao.RtMapKey;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.map.MapForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class MapFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  /**
   * access method for property EvtFileLastUpdate.
   */
  public Date getEvtFileLastUpdate() {
    return getEvtFileLastUpdateProperty().getValue();
  }

  /**
   * access method for property EvtFileLastUpdate.
   */
  public void setEvtFileLastUpdate(Date evtFileLastUpdate) {
    getEvtFileLastUpdateProperty().setValue(evtFileLastUpdate);
  }

  public EvtFileLastUpdateProperty getEvtFileLastUpdateProperty() {
    return getPropertyByClass(EvtFileLastUpdateProperty.class);
  }

  /**
   * access method for property FileChanged.
   */
  public boolean isFileChanged() {
    return getFileChangedProperty().getValue() == null ? false : getFileChangedProperty().getValue();
  }

  /**
   * access method for property FileChanged.
   */
  public void setFileChanged(boolean fileChanged) {
    getFileChangedProperty().setValue(fileChanged);
  }

  public FileChangedProperty getFileChangedProperty() {
    return getPropertyByClass(FileChangedProperty.class);
  }

  /**
   * access method for property Format.
   */
  public String getFormat() {
    return getFormatProperty().getValue();
  }

  /**
   * access method for property Format.
   */
  public void setFormat(String format) {
    getFormatProperty().setValue(format);
  }

  public FormatProperty getFormatProperty() {
    return getPropertyByClass(FormatProperty.class);
  }

  public Height getHeight() {
    return getFieldByClass(Height.class);
  }

  /**
   * access method for property MapData.
   */
  public byte[] getMapData() {
    return getMapDataProperty().getValue();
  }

  /**
   * access method for property MapData.
   */
  public void setMapData(byte[] mapData) {
    getMapDataProperty().setValue(mapData);
  }

  public MapDataProperty getMapDataProperty() {
    return getPropertyByClass(MapDataProperty.class);
  }

  /**
   * access method for property MapKey.
   */
  public RtMapKey getMapKey() {
    return getMapKeyProperty().getValue();
  }

  /**
   * access method for property MapKey.
   */
  public void setMapKey(RtMapKey mapKey) {
    getMapKeyProperty().setValue(mapKey);
  }

  public MapKeyProperty getMapKeyProperty() {
    return getPropertyByClass(MapKeyProperty.class);
  }

  public NECornerBox getNECornerBox() {
    return getFieldByClass(NECornerBox.class);
  }

  public NWCornerBox getNWCornerBox() {
    return getFieldByClass(NWCornerBox.class);
  }

  public Name getName() {
    return getFieldByClass(Name.class);
  }

  /**
   * access method for property NewEventNr.
   */
  public Long getNewEventNr() {
    return getNewEventNrProperty().getValue();
  }

  /**
   * access method for property NewEventNr.
   */
  public void setNewEventNr(Long newEventNr) {
    getNewEventNrProperty().setValue(newEventNr);
  }

  public NewEventNrProperty getNewEventNrProperty() {
    return getPropertyByClass(NewEventNrProperty.class);
  }

  public OriginX getOriginX() {
    return getFieldByClass(OriginX.class);
  }

  public OriginY getOriginY() {
    return getFieldByClass(OriginY.class);
  }

  public Resolution getResolution() {
    return getFieldByClass(Resolution.class);
  }

  public SECornerBox getSECornerBox() {
    return getFieldByClass(SECornerBox.class);
  }

  public SWCornerBox getSWCornerBox() {
    return getFieldByClass(SWCornerBox.class);
  }

  public Scale getScale() {
    return getFieldByClass(Scale.class);
  }

  public Width getWidth() {
    return getFieldByClass(Width.class);
  }

  public static class EvtFileLastUpdateProperty extends AbstractPropertyData<Date> {

    private static final long serialVersionUID = 1L;
  }

  public static class FileChangedProperty extends AbstractPropertyData<Boolean> {

    private static final long serialVersionUID = 1L;
  }

  public static class FormatProperty extends AbstractPropertyData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class Height extends AbstractValueFieldData<Integer> {

    private static final long serialVersionUID = 1L;
  }

  public static class MapDataProperty extends AbstractPropertyData<byte[]> {

    private static final long serialVersionUID = 1L;
  }

  public static class MapKeyProperty extends AbstractPropertyData<RtMapKey> {

    private static final long serialVersionUID = 1L;
  }

  public static class NECornerBox extends AbstractPositionBoxData {

    private static final long serialVersionUID = 1L;
  }

  public static class NWCornerBox extends AbstractPositionBoxData {

    private static final long serialVersionUID = 1L;
  }

  public static class Name extends AbstractValueFieldData<String> {

    private static final long serialVersionUID = 1L;
  }

  public static class NewEventNrProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class OriginX extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class OriginY extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class Resolution extends AbstractValueFieldData<BigDecimal> {

    private static final long serialVersionUID = 1L;
  }

  public static class SECornerBox extends AbstractPositionBoxData {

    private static final long serialVersionUID = 1L;
  }

  public static class SWCornerBox extends AbstractPositionBoxData {

    private static final long serialVersionUID = 1L;
  }

  public static class Scale extends AbstractValueFieldData<Long> {

    private static final long serialVersionUID = 1L;
  }

  public static class Width extends AbstractValueFieldData<Integer> {

    private static final long serialVersionUID = 1L;
  }
}
