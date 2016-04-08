package com.rtiming.shared.dataexchange;

import java.io.Serializable;

public class ImportMessage implements Serializable {

  private static final long serialVersionUID = 1L;

  private String item;
  private String message;
  private int status;

  public ImportMessage(String item, String message, int status) {
    super();
    this.item = item;
    this.message = message;
    this.status = status;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  public String getItem() {
    return item;
  }

  public void setItem(String item) {
    this.item = item;
  }

}
