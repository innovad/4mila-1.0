package com.rtiming.client.result.pos;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

public class TestPosPrinter implements IPosPrinter {

  private StringBuilder builder = new StringBuilder();

  @Override
  public int getLineWidth() throws ProcessingException {
    return 30;
  }

  @Override
  public void print(String data) throws ProcessingException {
    builder.append(data);
  }

  @Override
  public void close() throws ProcessingException {
    // nop
  }

  public String getPrintout() throws ProcessingException {
    return builder.toString();
  }

}
