package com.rtiming.client.dataexchange.oe2003;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.dataexchange.swiss.InterfaceTestUtility;
import com.rtiming.client.entry.EntriesSearchForm;
import com.rtiming.client.entry.EntriesTablePage;
import com.rtiming.client.entry.EntryForm;
import com.rtiming.client.entry.payment.PaymentsTablePage;
import com.rtiming.client.test.data.CountryTestDataProvider;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dataexchange.ImportExportFormatCodeType;
import com.rtiming.shared.ecard.IECardProcessService;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.test.helper.ITestingJPAService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(ClientSession.class)
public class OE2003EntryListInterfaceTest {

  private EventTestDataProvider event;
  private CountryTestDataProvider country;
  private CurrencyTestDataProvider currency;
  private Long defaultCountryUid;
  private Long defaultCurrencyUid;

  @Before
  public void before() throws ProcessingException {
    event = new EventTestDataProvider();
    currency = new CurrencyTestDataProvider();
    country = new CountryTestDataProvider();

    // defaults
    defaultCountryUid = BEANS.get(IDefaultProcessService.class).getDefaultCountryUid();
    BEANS.get(IDefaultProcessService.class).setDefaultCountryUid(country.getCountryUid());
    defaultCurrencyUid = BEANS.get(IDefaultProcessService.class).getDefaultCurrencyUid();
    BEANS.get(IDefaultProcessService.class).setDefaultCurrencyUid(currency.getCurrencyUid());
  }

  @Test
  public void testImport() throws ProcessingException {

    // import
    InterfaceTestUtility.doImport(ImportExportFormatCodeType.OE2003EntryListCode.ID, event.getEventNr(), "resources//dataexchange//oe2003entries.csv", 16L, 0L);

    // check results
    EntriesTablePage entries = new EntriesTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
    EntriesSearchForm entriesSearchForm = (EntriesSearchForm) entries.getSearchFormInternal();
    entriesSearchForm.getResetButton().doClick();
    entries.nodeAddedNotify();
    entriesSearchForm.getStartTimeFrom().setValue(null);
    entriesSearchForm.getStartTimeTo().setValue(null);
    entries.loadChildren();
    assertEquals("Event on EntriesTablePage", event.getEventNr(), entriesSearchForm.getEventField().getValue());
    assertEquals(16, entries.getTable().getRowCount());

    boolean entry106Found = false;
    boolean entry44Found = false;
    for (int row = 0; row < entries.getTable().getRowCount(); row++) {
      if (StringUtility.equalsIgnoreNewLines(entries.getTable().getBibNumberColumn().getValue(row), "106")) {
        entry106Found = true;

        // check entry list
        assertEquals("Chip No", "41191", entries.getTable().getECardColumn().getValue(row));
        assertEquals("Ext Key", "ID3", entries.getTable().getExtKeyColumn().getValue(row));
        assertEquals("Last Name", "Sharp", entries.getTable().getLastNameColumn().getValue(row));
        assertEquals("First Name", "Tim", entries.getTable().getFirstNameColumn().getValue(row));
        assertEquals("Year", 1955, entries.getTable().getYearColumn().getValue(row).longValue());
        assertEquals("Sex", SexCodeType.ManCode.ID, entries.getTable().getSexColumn().getValue(row).longValue());
        assertEquals("Startblock", "BLOCK", entries.getTable().getStartblockUidColumn().getDisplayText(entries.getTable().getRow(row)));
        assertEquals("Club", "PO A", entries.getTable().getClubColumn().getValue(row));
        assertEquals("Nation", "ACT", entries.getTable().getNationColumn().getValue(row));
        assertEquals("City", "CITY", entries.getTable().getCityColumn().getValue(row));
        assertEquals("Street", "Street" + FMilaUtility.LINE_SEPARATOR + "Line2", entries.getTable().getStreetColumn().getValue(row));
        assertEquals("ZIP", "9999", entries.getTable().getZipColumn().getValue(row));
        assertEquals("Phone", "Phone", entries.getTable().getPhoneColumn().getValue(row));
        assertEquals("Fax", "Fax", entries.getTable().getFaxColumn().getValue(row));
        assertEquals("E-Mail", "mail@mail.com", entries.getTable().getEMailColumn().getValue(row));
        assertEquals("Class", "O1MO", entries.getTable().getClazzShortcutColumn().getDisplayText(entries.getTable().getRow(row)));

        // check ecard
        RtEcard ecard = BEANS.get(IECardProcessService.class).findECard(entries.getTable().getECardColumn().getValue(row));
        assertTrue("Rented E-Card", ecard.getRentalCard());

        // Check Entry Form
        EntryForm entry = new EntryForm();
        entry.setEntryNr(entries.getTable().getEntryNrColumn().getValue(row));
        entry.startModify();

        boolean individualStartFee = false;
        for (int k = 0; k < entry.getAdditionalInformationEntryField().getTable().getRowCount(); k++) {
          if (entry.getAdditionalInformationEntryField().getTable().getAdditionalInformationUidColumn().getValue(k).longValue() == AdditionalInformationCodeType.IndividualStartFeeCode.ID) {
            individualStartFee = true;
            assertEquals("Individual Start Fee", 33.0d, entry.getAdditionalInformationEntryField().getTable().getDecimalColumn().getValue(k), 0.01d);
          }
        }
        assertTrue("Individual Start Fee", individualStartFee);
        assertEquals("1 Fee Row", 1, entry.getFeesField().getTable().getRowCount());
        assertEquals("Fee amount", 33, entry.getFeesField().getTable().getAmountColumn().getValue(0), 0.01d);
        System.out.println(entry.getFeesBox().getLabel());

        entry.doClose();

        // Check Payment
        PaymentsTablePage payments = new PaymentsTablePage(entry.getRegistrationField().getValue());
        payments.nodeAddedNotify();
        payments.loadChildren();
        assertEquals("2 Rows (Payment + Total)", 2, payments.getTable().getRowCount());

        Double amount = payments.getTable().getAmountColumn().getValue(0);
        assertEquals("Payment", 33, amount.doubleValue(), 0.01d);
        assertEquals("Currency", currency.getCurrencyUid(), payments.getTable().getCurrencyUidColumn().getValue(0));
      }
      else if (StringUtility.equalsIgnoreNewLines(entries.getTable().getBibNumberColumn().getValue(row), "44")) {
        entry44Found = true;
        assertEquals("Last Name", "Maclean", entries.getTable().getLastNameColumn().getValue(row));

        // check ecard
        RtEcard ecard = BEANS.get(IECardProcessService.class).findECard(entries.getTable().getECardColumn().getValue(row));
        assertFalse("Not Rented E-Card", ecard.getRentalCard());
      }
    }
    assertTrue("Bib. No 106 missing", entry106Found);
    assertTrue("Bib. No 44 missing", entry44Found);
  }

  @Test
  public void testImport2() throws ProcessingException {
    // import
    String result = InterfaceTestUtility.doImport(ImportExportFormatCodeType.OE2003EntryListCode.ID, event.getEventNr(), "resources//dataexchange//oe2003entries2.csv", 2L, 2L);
    assertTrue("Error Message", result.contains(TEXTS.get("UnknownSICardType", "3101966")));
    assertTrue("Error Message", result.contains(TEXTS.get("UnknownSICardType", "17110104")));

    // check results
    EntriesTablePage entries = new EntriesTablePage(event.getEventNr(), ClientSession.get().getSessionClientNr(), null, null, null, null);
    entries.nodeAddedNotify();
    entries.getSearchFormInternal().resetSearchFilter();
    ((EntriesSearchForm) entries.getSearchFormInternal()).getStartTimeFrom().setValue(null);
    ((EntriesSearchForm) entries.getSearchFormInternal()).getStartTimeTo().setValue(null);
    entries.loadChildren();
    assertEquals("Event on EntriesTablePage", event.getEventNr(), ((EntriesSearchForm) entries.getSearchFormInternal()).getEventField().getValue());
    assertEquals(0, entries.getTable().getRowCount());
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    BEANS.get(ITestingJPAService.class).deleteCities(country.getCountryUid());
    BEANS.get(IDefaultProcessService.class).setDefaultCountryUid(defaultCountryUid);
    BEANS.get(IDefaultProcessService.class).setDefaultCurrencyUid(defaultCurrencyUid);
    country.remove();
    currency.remove();
  }

}
