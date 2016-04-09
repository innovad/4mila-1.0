package com.rtiming.shared.dataexchange;

import java.util.ArrayList;

import org.eclipse.scout.rt.platform.exception.ProcessingException;


public abstract class AbstractXMLDataBean extends AbstractDataBean {

  private static final long serialVersionUID = 1L;

  public AbstractXMLDataBean(Long primaryKeyNr) {
    super(primaryKeyNr);
  }

  public abstract Object createXMLObject(Object main) throws ProcessingException;

  public abstract ArrayList<String> getPreviewRowData() throws ProcessingException;

}
