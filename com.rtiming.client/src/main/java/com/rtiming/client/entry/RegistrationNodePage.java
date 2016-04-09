package com.rtiming.client.entry;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.ClientSession;
import com.rtiming.client.entry.payment.PaymentsTablePage;
import com.rtiming.shared.Texts;

public class RegistrationNodePage extends AbstractPageWithNodes {

  private final Long registrationNr;

  public RegistrationNodePage(Long registrationNr) {
    super();
    this.registrationNr = registrationNr;
  }

  public Long getRegistrationNr() {
    return registrationNr;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Registration");
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new EntriesTablePage(null, ClientSession.get().getSessionClientNr(), getRegistrationNr(), null, null, null));
    pageList.add(new PaymentsTablePage(getRegistrationNr()));
  }
}
