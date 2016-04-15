package com.rtiming.client.dataexchange;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;

import com.rtiming.client.ClientSession;
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm.MainBox.InfoField;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;
import com.rtiming.shared.dataexchange.IDataExchangeService;
import com.rtiming.shared.dataexchange.cache.DataCacher;

public abstract class AbstractInterface<Bean extends AbstractDataBean> {

  private static final int STEP_SIZE = 100;
  private ArrayList<Bean> data;
  private final Preview preview = new Preview();

  private final String fullPathName;
  private final Long eventNr;
  private final boolean isImport;
  private final String characterSet;
  private final Long languageUid;
  private InfoField infoField;

  public AbstractInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    this.fullPathName = interfaceConfig.getFile().getValue().getFilename();
    this.eventNr = interfaceConfig.getEvent().getValue();
    this.isImport = interfaceConfig.getImportExportGroup().getValue();
    this.characterSet = interfaceConfig.getCharacterSet().getValue();
    this.languageUid = NumberUtility.nvl(interfaceConfig.getLanguage().getValue(), ClientSession.get().getLanguageUid());
  }

  public abstract void fileToPreview() throws ProcessingException;

  public final void databaseToPreview() throws ProcessingException {
    for (Long primaryKeyNr : getPrimaryKeyNrs()) {
      Bean bean = createNewBean(primaryKeyNr);
      bean.loadBeanFromDatabase();
      addBean(bean);
    }
  }

  public void addBean(Bean bean) {
    if (getData() == null) {
      setData(new ArrayList<Bean>());
    }
    getData().add(bean);
  }

  public abstract Bean createNewBean(Long primaryKeyNr);

  public final void previewToDatabase(ProgressMonitor monitor) throws ProcessingException {
    int counter = 0;

    // clear server and client caches
    BEANS.get(IDataExchangeService.class).clearCaches();
    DataCacher.get(ClientSession.get().getUserId()).clear();

    // loop over beans and store batches to database
    List<AbstractDataBean> batch = new ArrayList<AbstractDataBean>();
    for (Bean b : data) {
      try {
        counter++;
        if (counter % STEP_SIZE == 0) {
          b.storeBeansToDatabase(batch, monitor);
          batch.clear();
        }
        if (b.getErrorMessage() == null) {
          batch.add(b);
        }
        else {
          monitor.addError(b, b.getErrorMessage());
        }
      }
      catch (ProcessingException e) {
        monitor.addError(b, e.getMessage());
      }

      // User Feedback
      int length = getPreview().getData().length;
      if (counter == 1 || counter % STEP_SIZE == 0 || counter == length) {
        monitor.update(counter, length);
      }
    }
    if (batch.size() > 0) {
      batch.get(0).storeBeansToDatabase(batch, monitor);
      monitor.update(getPreview().getData().length, getPreview().getData().length);
    }
  }

  public abstract void previewToFile(ProgressMonitor monitor) throws ProcessingException;

  public abstract List<Long> getPrimaryKeyNrs() throws ProcessingException;

  public String getFullPathName() {
    return fullPathName;
  }

  public Long getEventNr() {
    return eventNr;
  }

  public boolean isImport() {
    return isImport;
  }

  public String getCharacterSet() {
    return characterSet;
  }

  public Long getLanguageUid() {
    return languageUid;
  }

  public Preview getPreview() {
    return preview;
  }

  public abstract void createPreviewData() throws ProcessingException;

  public abstract ArrayList<String> getColumnHeaders() throws ProcessingException;

  public ArrayList<Bean> getData() {
    return data;
  }

  public void setData(ArrayList<Bean> data) {
    this.data = data;
  }

}
