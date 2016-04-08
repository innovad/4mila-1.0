package com.rtiming.client;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.client.session.ClientSessionProvider;
import org.eclipse.scout.rt.client.ui.desktop.OpenUriAction;
import org.eclipse.scout.rt.client.ui.messagebox.IMessageBox;
import org.eclipse.scout.rt.client.ui.messagebox.MessageBoxes;

import com.rtiming.shared.Texts;

public final class FMilaClientUtility {

  private static final IScoutLogger LOG = ScoutLogManager.getLogger(FMilaClientUtility.class);

  private FMilaClientUtility() {
  }

  public static void openDocument(String path) throws ProcessingException {
    ClientSessionProvider.currentSession().getDesktop().openUri(path, OpenUriAction.DOWNLOAD);
    ClientSession.get().getDesktop().setStatusText(Texts.get("FileOpened"));
  }

  public static boolean isTestEnvironment() {
    // TODO MIG
//    Bundle server = Platform.getBundle(com.rtiming.client.Activator.PLUGIN_ID);
//    Bundle[] fragments = Platform.getFragments(server);
//    if (fragments != null) {
//      for (Bundle fragment : fragments) {
//        if (fragment.getSymbolicName().contains("test")) {
//          return true;
//        }
//      }
//    }
    return false;
  }

  public static boolean isAdminUser() {
    return "admin".equalsIgnoreCase(ClientSession.get().getUserId());
  }

  public static IMessageBox createMessageBox(String text1, String header, String body, String yes, String no, String cancel) {
    return MessageBoxes.create().withHeader(header).withBody(body).withYesButtonText(yes).withNoButtonText(no).withCancelButtonText(cancel);
  }

  public static IMessageBox showOkMessage(String title, String header, String content) {
    return MessageBoxes.createOk().withHeader(title).withBody(content);
  }

  public static IMessageBox showYesNoMessage(String title, String header, String content) {
    return MessageBoxes.createYesNo().withHeader(title).withBody(content);
  }

}
