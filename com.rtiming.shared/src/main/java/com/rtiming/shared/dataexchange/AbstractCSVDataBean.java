package com.rtiming.shared.dataexchange;

import java.util.List;

public abstract class AbstractCSVDataBean extends AbstractDataBean {

  private static final long serialVersionUID = 1L;

  public AbstractCSVDataBean(Long primaryKeyNr) {
    super(primaryKeyNr);
  }

  public abstract void setData(List<Object> data);

  public abstract List<Object> getData();

}
