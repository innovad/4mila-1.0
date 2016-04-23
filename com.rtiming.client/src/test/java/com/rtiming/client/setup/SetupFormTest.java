package com.rtiming.client.setup;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.eclipse.scout.rt.client.ui.basic.table.ITableRow;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.FMilaClientTestUtility;
import com.rtiming.client.test.data.CountryTestDataProvider;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;
import com.rtiming.shared.dao.RtUserKey;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.user.IUserProcessService;
import com.rtiming.shared.settings.user.RoleCodeType;
import com.rtiming.shared.settings.user.UserFormData;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(ClientSession.class)
public class SetupFormTest {

  private Long defaultCountryUid;
  private Long defaultCurrencyUid;
  private static CurrencyTestDataProvider currency;
  private static CountryTestDataProvider country;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    currency = new CurrencyTestDataProvider();
    country = new CountryTestDataProvider();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    currency.remove();
    country.remove();
  }

  @Before
  public void before() throws ProcessingException {
    defaultCountryUid = BEANS.get(IDefaultProcessService.class).getDefaultCountryUid();
    defaultCurrencyUid = BEANS.get(IDefaultProcessService.class).getDefaultCurrencyUid();
  }

  @Test
  public void testNew() throws Exception {
    SetupForm form = new SetupForm();
    form.startNew();

    assertEquals(4, form.getUserField().getTable().getRowCount());
  }

  @Test
  public void testDefaultCountry() throws Exception {
    CountryTestDataProvider country2 = new CountryTestDataProvider();

    SetupForm form = new SetupForm();
    form.startNew();
    MaxFormFieldValueProvider provider = new MaxFormFieldValueProvider();
    provider.fillValueField(form.getLanguageField(), null);
    form.getCurrencyField().setValue(currency.getCurrencyUid());
    form.getCountryField().setValue(country2.getCountryUid());
    form.doOk();

    assertEquals(country2.getCountryUid(), BEANS.get(IDefaultProcessService.class).getDefaultCountryUid());

    country.remove();
  }

  @Test
  public void testDefaultCurrency() throws Exception {
    CurrencyTestDataProvider currency2 = new CurrencyTestDataProvider();

    SetupForm form = new SetupForm();
    form.startNew();
    MaxFormFieldValueProvider provider = new MaxFormFieldValueProvider();
    provider.fillValueField(form.getLanguageField(), null);
    form.getCountryField().setValue(country.getCountryUid());
    form.getCurrencyField().setValue(currency2.getCurrencyUid());
    form.doOk();

    assertEquals(currency2.getCurrencyUid(), BEANS.get(IDefaultProcessService.class).getDefaultCurrencyUid());

    currency2.remove();
  }

  @Test
  public void testUserCreation() throws Exception {
    SetupForm form = new SetupForm();
    form.startNew();
    MaxFormFieldValueProvider provider = new MaxFormFieldValueProvider();
    provider.fillValueField(form.getLanguageField(), null);
    form.getCountryField().setValue(country.getCountryUid());
    form.getCurrencyField().setValue(currency.getCurrencyUid());

    ITableRow newRow = form.getUserField().getTable().createRow();
    newRow = form.getUserField().getTable().addRow(newRow);
    form.getUserField().getTable().getUsernameColumn().setValue(newRow, "ABCDEFG");
    form.getUserField().getTable().getPasswordColumn().setValue(newRow, "1234567");
    form.getUserField().getTable().getRoleColumn().setValue(newRow, RoleCodeType.SpeakerCode.ID);
    form.doOk();

    UserFormData user = BEANS.get(IUserProcessService.class).find("ABCDEFG");
    assertNotNull(user.getUserNr());
    assertEquals(user.getLanguage().getValue(), form.getLanguageField().getValue());
    assertEquals("abcdefg", user.getUsername().getValue());
    assertEquals(1, user.getRoles().getValue().size());
    assertEquals(RoleCodeType.SpeakerCode.ID, user.getRoles().getValue().toArray(new Long[0])[0].longValue());

    BEANS.get(IUserProcessService.class).delete(RtUserKey.create(user.getUserNr()));
  }

  @Test(expected = VetoException.class)
  public void testUniqueUsername() throws Exception {
    SetupForm form = new SetupForm();
    form.startNew();
    MaxFormFieldValueProvider provider = new MaxFormFieldValueProvider();
    provider.fillValueField(form.getCurrencyField(), null);
    provider.fillValueField(form.getLanguageField(), null);
    provider.fillValueField(form.getCountryField(), null);

    ITableRow newRow = form.getUserField().getTable().createRow();
    newRow = form.getUserField().getTable().addRow(newRow);
    form.getUserField().getTable().getUsernameColumn().setValue(newRow, "ABCDEFG");
    form.getUserField().getTable().getPasswordColumn().setValue(newRow, "1234567");
    form.getUserField().getTable().getRoleColumn().setValue(newRow, RoleCodeType.SpeakerCode.ID);

    ITableRow newRow2 = form.getUserField().getTable().createRow();
    newRow2 = form.getUserField().getTable().addRow(newRow2);
    form.getUserField().getTable().getUsernameColumn().setValue(newRow2, "ABCDEFG");
    form.getUserField().getTable().getPasswordColumn().setValue(newRow2, "1234567");
    form.getUserField().getTable().getRoleColumn().setValue(newRow2, RoleCodeType.SpeakerCode.ID);

    form.doOk();
  }

  @Test
  public void testFields() throws Exception {
    FMilaClientTestUtility.testFormFields(new SetupForm());
  }

  @After
  public void after() throws ProcessingException {
    if (defaultCountryUid != null) {
      BEANS.get(IDefaultProcessService.class).setDefaultCountryUid(defaultCountryUid);
    }
    if (defaultCurrencyUid != null) {
      BEANS.get(IDefaultProcessService.class).setDefaultCurrencyUid(defaultCurrencyUid);
    }
  }

}
