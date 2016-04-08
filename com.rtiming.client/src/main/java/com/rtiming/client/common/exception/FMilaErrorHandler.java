//package com.rtiming.client.common.exception;
//
//import org.eclipse.scout.commons.StringUtility;
//import org.eclipse.scout.commons.exception.ProcessingException;
//import org.eclipse.scout.commons.logger.IScoutLogger;
//import org.eclipse.scout.commons.logger.ScoutLogManager;
//
//public class FMilaErrorHandler extends ErrorHandler {
//  private static final IScoutLogger LOG = ScoutLogManager.getLogger(FMilaErrorHandler.class);
//  private final Throwable throwable;
//
//  public FMilaErrorHandler(Throwable throwable) {
//    super(throwable);
//    this.throwable = throwable;
//  }
//
//  @Override
//  public void showMessageBox() {
//    try {
//      String title = getTitle();
//
//      ExceptionForm form = new ExceptionForm();
//      form.setTitle(StringUtility.nvl(title, "Error"));
//      form.setMessageText(getCopyPasteText());
//      form.startDefault();
//      form.waitFor();
//    }
//    catch (ProcessingException e) {
//      LOG.error("could not open exception dialog", e);
//      super.showMessageBox();
//    }
//  }
//}
