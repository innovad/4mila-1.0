package com.rtiming.client.dataexchange;

public class Preview {

  private Object[][] data;
  private String[][] errors;

  public final Object[][] getData() {
    return data;
  }

  public final void setData(Object[][] data) {
    this.data = data;
  }

  public final String[][] getErrors() {
    return errors;
  }

  public final void setErrors(String[][] errors) {
    this.errors = errors;
  }

}
