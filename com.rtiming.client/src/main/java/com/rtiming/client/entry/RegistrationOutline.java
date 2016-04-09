package com.rtiming.client.entry;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.entry.payment.PaymentsTablePage;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Icons;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.RegistrationOutlinePermission;

public class RegistrationOutline extends AbstractOutline {

  @Override
  protected String getConfiguredIconId() {
    return Icons.FILE;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Registrations");
  }

  @Override
  protected void execInitTree() throws ProcessingException {
    setVisiblePermission(new RegistrationOutlinePermission());
  }

  @Override
  protected void initConfig() {
    super.initConfig();
    setVisible(FMilaUtility.isRichClient());
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new EntriesNodePage());
    pageList.add(new RegistrationsTablePage());
    pageList.add(new PaymentsTablePage(null));
  }
}
