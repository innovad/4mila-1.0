//package com.rtiming.client.common.exception;
//
//import java.net.ConnectException;
//import java.util.List;
//
//import org.eclipse.scout.commons.exception.ProcessingException;
//import org.eclipse.scout.commons.exception.VetoException;
//import org.eclipse.scout.commons.logger.IScoutLogger;
//import org.eclipse.scout.commons.logger.ScoutLogManager;
//import org.eclipse.scout.rt.client.ui.form.IForm;
//
//import com.rtiming.client.ClientSession;
//
//@Priority(1)
//public class FMilaClientExceptionHandlerService extends ClientExceptionHandlerService {
//
//  private static final Logger LOG = LoggerFactory.getLogger(FMilaClientExceptionHandlerService.class);
//
//  @Override
//  protected void showExceptionInUI(ProcessingException pex) {
//    // avoid infinite dialog loops
//    if (ClientSession.get() != null) {
//      List<IForm> dialogStack = ClientSession.get().getDesktop().getDialogStack();
//      for (IForm form : dialogStack) {
//        if (form instanceof ExceptionForm) {
//          new ErrorHandler(pex).showMessageBox();
//          return;
//        }
//      }
//    }
//    // standard veto exception
//    if (pex instanceof VetoException || getCause(pex) instanceof ConnectException) {
//      new ErrorHandler(pex).showMessageBox();
//    }
//    // real error, show cause and log
//    else {
//      new FMilaErrorHandler(pex).showMessageBox();
//    }
//  }
//
//  private Throwable getCause(Throwable e) {
//    if (e.getCause() != null) {
//      return getCause(e.getCause());
//    }
//    return e;
//  }
//}
