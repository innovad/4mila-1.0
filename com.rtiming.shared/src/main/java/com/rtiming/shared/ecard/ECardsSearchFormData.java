package com.rtiming.shared.ecard;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;


public class ECardsSearchFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public ECardsSearchFormData() {
  }

  public ECardBox getECardBox() {
    return getFieldByClass(ECardBox.class);
  }

  public static class ECardBox extends AbstractECardSearchBoxData {
    private static final long serialVersionUID = 1L;

    public ECardBox() {
    }
  }
}
