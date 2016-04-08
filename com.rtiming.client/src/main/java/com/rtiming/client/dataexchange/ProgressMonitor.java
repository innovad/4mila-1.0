package com.rtiming.client.dataexchange;

import org.eclipse.scout.commons.exception.IProcessingStatus;
import org.eclipse.scout.rt.client.ui.form.fields.IValueField;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.ImportMessage;
import com.rtiming.shared.dataexchange.ImportMessageList;

public class ProgressMonitor implements IProgressMonitor {

  private final ImportMessageList importMessages = new ImportMessageList();
  private int errorCount = 0;

  public ProgressMonitor(IValueField htmlInfo) {
    this.htmlInfo = htmlInfo;
  }

  private final IValueField htmlInfo;

  public void addError(AbstractDataBean bean, String error) {
    ImportMessage message = new ImportMessage(bean.toString(), error, IProcessingStatus.ERROR);
    importMessages.getErrors().add(message);
    errorCount++;
  }

  @Override
  public void addErrors(ImportMessageList list) {
    importMessages.getErrors().addAll(list.getErrors());
    errorCount = errorCount + list.getErrors().size();
  }

  @Override
  public void addInfo(String msg, String info) {
    ImportMessage message = new ImportMessage(msg, info, IProcessingStatus.INFO);
    importMessages.getErrors().add(message);
  }

  @Override
  public void addInfoCreated(String msg, String info) {
    addInfo(msg + " " + TEXTS.get("DataCreated"), info);
  }

  @Override
  public void addInfoUpdated(String msg, String info) {
    addInfo(msg + " " + TEXTS.get("DataUpdated"), info);
  }

  @Override
  public void addInfoDeleted(String msg, String info) {
    addInfo(msg + " " + TEXTS.get("DataDeleted"), info);
  }

  @Override
  public void update(int counter, int length) {
    setHtmlInfo(createStatusHtml(counter, length));
  }

  /**
   * @param counter
   * @param length
   * @return
   */
  private String createStatusHtml(int counter, int length) {
    return "" +
        Texts.get("Done") +
        ": " + Math.min(counter, length) + "/" + length + "<br>" +
        Texts.get("Successful") +
        ": " + (Math.min(counter, length) - errorCount) +
        "<br>" +
        Texts.get("Error") +
        ": " + errorCount +
        "<br>" +
        "<br>" +
        importMessages.toHtml() +
        "";
  }

  @SuppressWarnings("unchecked")
  private void setHtmlInfo(String htmlInfo) {
    this.htmlInfo.setValue(htmlInfo);
  }

}
