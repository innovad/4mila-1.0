package com.rtiming.shared.settings;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.common.AbstractCodeBoxData;

public class CodeFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public CodeFormData() {
  }

  public CodeTypeProperty getCodeTypeProperty() {
    return getPropertyByClass(CodeTypeProperty.class);
  }

  /**
   * access method for property CodeType.
   */
  public Long getCodeType() {
    return getCodeTypeProperty().getValue();
  }

  /**
   * access method for property CodeType.
   */
  public void setCodeType(Long codeType) {
    getCodeTypeProperty().setValue(codeType);
  }

  public CodeUidProperty getCodeUidProperty() {
    return getPropertyByClass(CodeUidProperty.class);
  }

  /**
   * access method for property CodeUid.
   */
  public Long getCodeUid() {
    return getCodeUidProperty().getValue();
  }

  /**
   * access method for property CodeUid.
   */
  public void setCodeUid(Long codeUid) {
    getCodeUidProperty().setValue(codeUid);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public class CodeTypeProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public CodeTypeProperty() {
    }
  }

  public class CodeUidProperty extends AbstractPropertyData<Long> {
    private static final long serialVersionUID = 1L;

    public CodeUidProperty() {
    }
  }

  public static class MainBox extends AbstractCodeBoxData {
    private static final long serialVersionUID = 1L;

    public MainBox() {
    }
  }
}
