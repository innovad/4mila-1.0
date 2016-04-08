package com.rtiming.shared.entry.startblock;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.common.AbstractCodeBoxData;

public class StartblockFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public StartblockFormData() {
  }

  public StartblockUidProperty getStartblockUidProperty() {
    return getPropertyByClass(StartblockUidProperty.class);
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

  public CodeBox getCodeBox() {
    return getFieldByClass(CodeBox.class);
  }

  public class StartblockUidProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public StartblockUidProperty() {
    }
  }

  public static class CodeBox extends AbstractCodeBoxData {
    private static final long serialVersionUID = 1L;

    public CodeBox() {
    }
  }
}
