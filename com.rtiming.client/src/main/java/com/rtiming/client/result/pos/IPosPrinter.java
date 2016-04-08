package com.rtiming.client.result.pos;

import org.eclipse.scout.commons.exception.ProcessingException;

public interface IPosPrinter {

  int getLineWidth() throws ProcessingException;

  void print(String data) throws ProcessingException;

  void close() throws ProcessingException;

}
