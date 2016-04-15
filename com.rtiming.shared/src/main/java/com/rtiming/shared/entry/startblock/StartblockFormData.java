package com.rtiming.shared.entry.startblock;

import javax.annotation.Generated;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.common.AbstractCodeBoxData;

/**
 * <b>NOTE:</b><br>
 * This class is auto generated by the Scout SDK. No manual modifications recommended.
 */
@Generated(value = "com.rtiming.client.entry.startblock.StartblockForm", comments = "This class is auto generated by the Scout SDK. No manual modifications recommended.")
public class StartblockFormData extends AbstractFormData {

  private static final long serialVersionUID = 1L;

  public CodeBox getCodeBox() {
    return getFieldByClass(CodeBox.class);
  }

  /**
   * access method for property StartblockUid.
   */
  public Long getStartblockUid() {
    return getStartblockUidProperty().getValue();
  }

  /**
   * access method for property StartblockUid.
   */
  public void setStartblockUid(Long startblockUid) {
    getStartblockUidProperty().setValue(startblockUid);
  }

  public StartblockUidProperty getStartblockUidProperty() {
    return getPropertyByClass(StartblockUidProperty.class);
  }

  public static class CodeBox extends AbstractCodeBoxData {

    private static final long serialVersionUID = 1L;
  }

  public static class StartblockUidProperty extends AbstractPropertyData<Long> {

    private static final long serialVersionUID = 1L;
  }
}
