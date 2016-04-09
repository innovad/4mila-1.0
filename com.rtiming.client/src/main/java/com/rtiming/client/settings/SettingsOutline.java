package com.rtiming.client.settings;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.database.DatabaseNodePage;
import com.rtiming.client.common.report.template.ReportTemplatesTablePage;
import com.rtiming.client.entry.startblock.StartblocksTablePage;
import com.rtiming.client.settings.account.AccountTablePage;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationTablePage;
import com.rtiming.client.settings.city.AreasTablePage;
import com.rtiming.client.settings.city.CitiesTablePage;
import com.rtiming.client.settings.city.CountriesTablePage;
import com.rtiming.client.settings.clazz.ClassesTablePage;
import com.rtiming.client.settings.currency.CurrenciesTablePage;
import com.rtiming.client.settings.fee.FeeGroupsTablePage;
import com.rtiming.client.settings.user.UsersTablePage;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.SettingsOutlinePermission;

public class SettingsOutline extends AbstractOutline {

  @Override
  protected String getConfiguredIconId() {
    return com.rtiming.shared.Icons.ENTRY;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Settings");
  }

  @Override
  protected void execInitTree() throws ProcessingException {
    setVisiblePermission(new SettingsOutlinePermission());
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new AreasTablePage());
    pageList.add(new CitiesTablePage());
    pageList.add(new CountriesTablePage());
    pageList.add(new CurrenciesTablePage());
    pageList.add(new ClassesTablePage());
    pageList.add(new AdditionalInformationAdministrationTablePage(null));
    pageList.add(new FeeGroupsTablePage());
    pageList.add(new StartblocksTablePage());
    pageList.add(new ReportTemplatesTablePage());
    if (FMilaUtility.isRichClient()) {
      pageList.add(new UsersTablePage());
      pageList.add(new DatabaseNodePage());
    }
    if (FMilaUtility.isWebClient() && FMilaClientUtility.isAdminUser()) {
      pageList.add(new AccountTablePage());
      pageList.add(new DatabaseNodePage());
    }
    pageList.add(new TranslationsTablePage());
  }
}
