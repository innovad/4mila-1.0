package com.rtiming.client.entry.payment;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.entry.RegistrationOutline;
import com.rtiming.client.entry.payment.PaymentsTablePage.Table.DeleteMenu;
import com.rtiming.client.entry.payment.PaymentsTablePage.Table.EditMenu;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.ClientTestingUtility;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.CurrencyTestDataProvider;
import com.rtiming.client.test.data.RegistrationTestDataProvider;
import com.rtiming.shared.common.database.sql.PaymentBean;
import com.rtiming.shared.entry.payment.IPaymentProcessService;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class PaymentsTablePageTest extends AbstractTablePageTest<PaymentsTablePage> {

  private static CurrencyTestDataProvider currency;
  private RegistrationTestDataProvider registration;

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    currency = new CurrencyTestDataProvider();
  }

  @AfterClass
  public static void afterClass() throws ProcessingException {
    currency.remove();
  }

  @Override
  protected PaymentsTablePage getTablePage() {
    return new PaymentsTablePage(null);
  }

  @Test
  public void testMenuesOnSummaryRow() throws ProcessingException {
    registration = new RegistrationTestDataProvider();
    PaymentForm payment = new PaymentForm();
    payment.startNew();
    FormTestUtility.fillFormFields(payment);
    payment.getCurrencyUidField().setValue(currency.getCurrencyUid());
    payment.doOk();

    IPage root = ClientTestingUtility.gotoOutline(RegistrationOutline.class);
    PaymentsTablePage page = ClientTestingUtility.gotoChildPage(root, PaymentsTablePage.class);

    page.getTable().selectLastRow();
    boolean runMenu = page.getTable().runMenu(EditMenu.class);
    Assert.assertFalse(runMenu);
    runMenu = page.getTable().runMenu(DeleteMenu.class);
    Assert.assertFalse(runMenu);

    PaymentBean formData = new PaymentBean();
    formData.setPaymentNr(payment.getPaymentNr());
    BEANS.get(IPaymentProcessService.class).delete(formData);
  }

  @Test
  public void testMenuesOnStandardRow() throws ProcessingException {
    registration = new RegistrationTestDataProvider();
    PaymentForm payment = new PaymentForm();
    payment.startNew();
    FormTestUtility.fillFormFields(payment);
    payment.getCurrencyUidField().setValue(currency.getCurrencyUid());
    payment.doOk();

    IPage root = ClientTestingUtility.gotoOutline(RegistrationOutline.class);
    PaymentsTablePage page = ClientTestingUtility.gotoChildPage(root, PaymentsTablePage.class);

    page.getTable().selectFirstRow();
    // TODO MIG page.getTable().getMenu(EditMenu.class).prepareAction();
    Assert.assertTrue(page.getTable().getMenu(EditMenu.class).isVisible());
    boolean runMenu = page.getTable().runMenu(DeleteMenu.class);
    Assert.assertTrue(runMenu);

    PaymentBean formData = new PaymentBean();
    formData.setPaymentNr(payment.getPaymentNr());
    BEANS.get(IPaymentProcessService.class).delete(formData);
  }

  @Test
  public void after() throws ProcessingException {
    if (registration != null) {
      registration.remove();
    }
  }

}
