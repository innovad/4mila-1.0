package com.rtiming.client.common.help;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.IDesktop;
import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.platform.Platform;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;

import com.rtiming.client.ClientSession;
import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.ui.desktop.LinkForm;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.FMilaUtility.OperatingSystem;
import com.rtiming.shared.Texts;
import com.rtiming.shared.settings.user.LanguageCodeType;

/**
 * 
 */
public class AbstractHelpLinkButton extends AbstractLinkButton {

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Help");
  }

  @Override
  protected void execClickAction() throws ProcessingException {
    // Path
    // http://127.0.0.1:8887/doc/doku.php?id=controlform
    String path = "http://4mila.com/doc/doku.php?id=";
    if (Platform.get().inDevelopmentMode()) {
      path = "http://127.0.0.1/doc/doku.php?id=";
    }

    // Id
    IForm enclosingForm = getForm();
    String id;

    IDesktop desktop = ClientSession.get().getDesktop();
    if (enclosingForm instanceof LinkForm) {
      id = desktop.getOutline().getActivePage().getNodeId();
    }
    else {
      id = getForm().getFormId();
    }

    // Language
    Long languageUid = ClientSession.get().getLanguageUid();
    ICode languageCode = CODES.getCodeType(LanguageCodeType.class).getCode(languageUid);
    String languageKey = null;
    if (languageCode != null && !StringUtility.isNullOrEmpty(languageCode.getExtKey()) && languageUid != LanguageCodeType.English.ID) {
      languageKey = languageCode.getExtKey();
    }
    if (languageKey != null) {
      id = languageKey + ":" + id;
    }

    // Open URL
    String url = path + id;
    if (OperatingSystem.WINDOWS.equals(FMilaUtility.getPlatform())) {
      startWindowsHelp(url);
    }
    else {
      FMilaClientUtility.openDocument(url);
    }
  }

  private void startWindowsHelp(String path) throws ProcessingException {
    IDesktop desktop = ClientSession.get().getDesktop();

    HelpForm helpForm = desktop.findForm(HelpForm.class);
    if (helpForm == null) {
      helpForm = new HelpForm();
      helpForm.startNew();
    }
    else {
      helpForm.requestFocus(helpForm.getHelpField());
    }
    helpForm.getHelpField().setLocation(path);
  }
}
