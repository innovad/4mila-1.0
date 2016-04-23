package com.rtiming.client.dataexchange.swiss;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.entry.EntriesSearchForm;
import com.rtiming.client.entry.EntriesTablePage;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.entry.payment.PaymentsTablePage;
import com.rtiming.client.test.data.CountryTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;
import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType.StartTimeWishLateStartCode;
import com.rtiming.shared.settings.city.AreaCodeType;
import com.rtiming.shared.settings.city.ICountryProcessService;
import com.rtiming.shared.settings.currency.CurrencyFormData;
import com.rtiming.shared.settings.currency.ICurrencyProcessService;
import com.rtiming.shared.test.helper.ITestingJPAService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class GO2OLEntriesInterfaceTest {

  private EventTestDataProvider event;
  private CountryTestDataProvider country;
  private Long defaultCountryUid;

  @Before
  public void before() throws ProcessingException {
    event = new EventTestDataProvider();
    defaultCountryUid = BEANS.get(IDefaultProcessService.class).getDefaultCountryUid();
    country = new CountryTestDataProvider();
    BEANS.get(IDefaultProcessService.class).setDefaultCountryUid(country.getCountryUid());
  }

  @Test
  public void testImport() throws ProcessingException {

    // do import
    InterfaceTestUtility.doImport(ImportExportFormatCodeType.GO2OLEntriesCode.ID, event.getEventNr(), "resources//dataexchange//GO2OLentries.csv", 12L, 0L);

    // check Results
    EntriesTablePage entries = new EntriesTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
    entries.nodeAddedNotify();
    entries.getSearchFormInternal().resetSearchFilter();
    ((EntriesSearchForm) entries.getSearchFormInternal()).getStartTimeFrom().setValue(null);
    ((EntriesSearchForm) entries.getSearchFormInternal()).getStartTimeTo().setValue(null);
    entries.loadChildren();
    Assert.assertEquals(12, entries.getTable().getRowCount());

    boolean entryABCFound = false;
    boolean entryDEFFound = false;
    for (int row = 0; row < entries.getTable().getRowCount(); row++) {
      if (StringUtility.equalsIgnoreNewLines(entries.getTable().getBibNumberColumn().getValue(row), "ABC")) {
        entryABCFound = true;

        // Data not imported
        //         [FOREIGNKEY]
        //         [SENDPAPER]
        //         [TRAIN]
        //         [NURSERY]
        //         [REMARK]
        //         [EXCELTERM]

        // Check Entry Table Page
        Assert.assertEquals("Last Name", "Hubmann", entries.getTable().getLastNameColumn().getValue(row));
        Assert.assertEquals("First Name", "Daniel", entries.getTable().getFirstNameColumn().getValue(row));
        Assert.assertEquals("Sex", SexCodeType.ManCode.ID, entries.getTable().getSexColumn().getValue(row).longValue());
        Assert.assertEquals("Year", 1983, entries.getTable().getYearColumn().getValue(row).longValue());
        Assert.assertEquals("City", "Bern", entries.getTable().getCityColumn().getValue(row));
        Assert.assertEquals("ZIP", "3012", entries.getTable().getZipColumn().getValue(row));
        Assert.assertEquals("Ext Key", "EF8HUD", entries.getTable().getExtKeyColumn().getValue(row));
        Assert.assertEquals("E-Card", "1160483", entries.getTable().getECardColumn().getValue(row));
        Assert.assertEquals("E-Mail", "daniel_hubmann@gmx.ch", entries.getTable().getEMailColumn().getValue(row));
        Assert.assertEquals("Mobile", "MOBILE", entries.getTable().getMobilePhoneColumn().getValue(row));
        Assert.assertEquals("Club", "OL Regio Wil", entries.getTable().getClubColumn().getValue(row));
        Assert.assertEquals("Startblock", "X", entries.getTable().getStartblockUidColumn().getDisplayText(entries.getTable().getRow(row)));
        Assert.assertEquals("Class", "HE", entries.getTable().getClazzShortcutColumn().getDisplayText(entries.getTable().getRow(row)));
        Assert.assertEquals("Street", "Seidenweg" + FMilaUtility.LINE_SEPARATOR + "14", entries.getTable().getStreetColumn().getValue(row));
        Assert.assertEquals("Region", "BE", entries.getTable().getRegionColumn().getValue(row));
        Long areaUid = BEANS.get(ICodeProcessService.class).find("WE", AreaCodeType.ID).getCodeUid();
        Assert.assertEquals("Area", areaUid, entries.getTable().getAreaColumn().getValue(row));
        Long countryUid = BEANS.get(ICountryProcessService.class).find(null, "CH", null).getCountryUid();
        Assert.assertEquals("Country", countryUid, entries.getTable().getCountryColumn().getValue(row));
        Assert.assertEquals("Nation", "SUI", entries.getTable().getNationColumn().getValue(row));
        Assert.assertEquals("Split", StartlistSettingOptionCodeType.SplitRegistrationsCode.ID, entries.getTable().getStartlistSettingOptionColumn().getValue(row));
        Assert.assertEquals("Start Time", (11 * 3600 + 12 * 60 + 13) * 1000, entries.getTable().getRelativeStartTimeColumn().getValue(row).longValue());

        // Check Entry Form
        EntryForm entry = new EntryForm();
        entry.setEntryNr(entries.getTable().getEntryNrColumn().getValue(row));
        entry.startModify();

        // Check Anti Doping
        boolean antiDopingFound = false;
        for (int k = 0; k < entry.getAdditionalInformationRunnerField().getTable().getRowCount(); k++) {
          if (entry.getAdditionalInformationRunnerField().getTable().getAdditionalInformationUidColumn().getValue(k).longValue() == AdditionalInformationCodeType.SwissOrienteeringAntiDopingCode.ID) {
            antiDopingFound = true;
            Assert.assertEquals("Anti Doping", Texts.get("Yes"), entry.getAdditionalInformationRunnerField().getTable().getValueColumn().getValue(k));
          }
        }
        Assert.assertTrue("Anti Doping", antiDopingFound);

        // Check Start Time Wish
        boolean startTimeWishFound = false;
        for (int k = 0; k < entry.getAdditionalInformationEntryField().getTable().getRowCount(); k++) {
          if (entry.getAdditionalInformationEntryField().getTable().getAdditionalInformationUidColumn().getValue(k).longValue() == AdditionalInformationCodeType.StartTimeWishCode.ID) {
            startTimeWishFound = true;
            Assert.assertEquals("Start Time Wish", StartTimeWishLateStartCode.ID, entry.getAdditionalInformationEntryField().getTable().getIntegerColumn().getValue(k));
          }
        }
        Assert.assertTrue("Start Time Wish", startTimeWishFound);

        // Check Fee
        boolean individualStartFee = false;
        for (int k = 0; k < entry.getAdditionalInformationEntryField().getTable().getRowCount(); k++) {
          if (entry.getAdditionalInformationEntryField().getTable().getAdditionalInformationUidColumn().getValue(k).longValue() == AdditionalInformationCodeType.IndividualStartFeeCode.ID) {
            individualStartFee = true;
            Assert.assertEquals("Individual Start Fee", 25.0d, entry.getAdditionalInformationEntryField().getTable().getDecimalColumn().getValue(k), 0.01d);
          }
        }
        Assert.assertTrue("Individual Start Fee", individualStartFee);
        Assert.assertEquals("1 Fee Row", 1, entry.getFeesField().getTable().getRowCount());
        Long currencyUid = entry.getCurrencyField().getValue();
        CurrencyFormData currency = loadCurrency(currencyUid);
        Assert.assertEquals("Currency", currencyUid, entry.getCurrencyField().getValue());
        Assert.assertEquals("Fee amount", 25, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.01d);
        System.out.println(entry.getFeesBox().getLabel());

        entry.doClose();

        // Check Payment
        PaymentsTablePage payments = new PaymentsTablePage(entry.getRegistrationField().getValue());
        payments.nodeAddedNotify();
        payments.loadChildren();
        Assert.assertEquals("2 Rows (Payment + Total)", 2, payments.getTable().getRowCount());

        Double amount = payments.getTable().getAmountColumn().getValue(0);
        Assert.assertEquals("Payment", 26, amount.doubleValue(), 0.01d);
        Assert.assertEquals("Currency", "CHF", currency.getCodeBox().getShortcut().getValue());
      }
      else if (StringUtility.equalsIgnoreNewLines(entries.getTable().getBibNumberColumn().getValue(row), "DEF")) {
        entryDEFFound = true;
        Assert.assertEquals("Group", StartlistSettingOptionCodeType.GroupRegistrationsCode.ID, entries.getTable().getStartlistSettingOptionColumn().getValue(row));
      }

    }
    Assert.assertTrue("Bib. No ABC missing", entryABCFound);
    Assert.assertTrue("Bib. No DEF missing", entryDEFFound);
  }

  private CurrencyFormData loadCurrency(Long currencyUid) throws ProcessingException {
    CurrencyFormData currency = new CurrencyFormData();
    currency.setCurrencyUid(currencyUid);
    currency = BEANS.get(ICurrencyProcessService.class).load(currency);
    return currency;
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    BEANS.get(ITestingJPAService.class).deleteCities(country.getCountryUid());
    BEANS.get(IDefaultProcessService.class).setDefaultCountryUid(defaultCountryUid);
    country.remove();
  }

}
