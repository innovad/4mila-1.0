package com.rtiming.shared.dataexchange;

import java.io.Serializable;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;

public abstract class AbstractDataBean implements Serializable {

  private static final long serialVersionUID = 1L;
  private Long primaryKeyNr;
  private String errorMessage;

  public AbstractDataBean(Long primaryKeyNr) {
    this.primaryKeyNr = primaryKeyNr;
  }

  public abstract void loadBeanFromDatabase() throws ProcessingException;

  public void storeBeansToDatabase(List<AbstractDataBean> batch, IProgressMonitor monitor) throws ProcessingException {
    for (AbstractDataBean bean : batch) {
      bean.storeBeanToDatabase(monitor);
    }
  }

  protected void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {
  }

  public Long getPrimaryKeyNr() {
    return primaryKeyNr;
  }

  public void setPrimaryKeyNr(Long primaryKeyNr) {
    this.primaryKeyNr = primaryKeyNr;
  }

  public String getErrorMessage() {
    return errorMessage;
  }

  public void setErrorMessage(String errorMessage) {
    this.errorMessage = errorMessage;
  }

}
