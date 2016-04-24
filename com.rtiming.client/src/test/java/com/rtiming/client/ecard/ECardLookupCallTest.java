package com.rtiming.client.ecard;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.TestClientSession;
import com.rtiming.client.test.AbstractDefaultLookupCallTest;
import com.rtiming.client.test.data.ECardTestDataProvider;
import com.rtiming.shared.ecard.ECardLookupCall;
import com.rtiming.shared.ecard.ECardTypeCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestClientSession.class)
public class ECardLookupCallTest extends AbstractDefaultLookupCallTest {

  private ECardTestDataProvider ecard;

  @Override
  protected void insertTestRow() throws ProcessingException {
    ecard = new ECardTestDataProvider();
  }

  @Override
  protected LookupCall createLookupCall() {
    return new ECardLookupCall();
  }

  @Override
  protected void deleteTestRow() throws ProcessingException {
    ecard.remove();
  }

  @Override
  @Test
  public void testLookupServiceByText() throws ProcessingException {
    ECardLookupCall call = new ECardLookupCall();
    call.setText(ecard.getForm().getNumberField().getValue());
    List<? extends ILookupRow> rows = call.getDataByText();
    Assert.assertTrue(rows.size() > 0);
  }

  @Test
  public void testPrepareRows1() throws Exception {
    ECardLookupCall call = new ECardLookupCall();
    call.prepareRows(null);
  }

  @Test
  public void testPrepareRows2() throws Exception {
    ECardLookupCall call = new ECardLookupCall();
    List<? extends ILookupRow<Long>> rows = new ArrayList<>();
    call.prepareRows(rows);
    Assert.assertEquals("0 Rows", 0, rows.size());
  }

  @Test
  public void testPrepareRows3() throws Exception {
    ECardLookupCall call = new ECardLookupCall();
    List<ILookupRow<Long>> rows = new ArrayList<>();
    rows.add(new LookupRow<Long>(-1L, "Text").withIconId("Icon"));
    call.prepareRows(rows);
    Assert.assertEquals("1 Row", 1, rows.size());
    Assert.assertEquals("Text", "Text", rows.get(0).getText());
  }

  @Test
  public void testPrepareRows4() throws Exception {
    ECardLookupCall call = new ECardLookupCall();
    List<ILookupRow<Long>> rows = new ArrayList<>();
    rows.add(new LookupRow<Long>(-1L, "Text").withIconId("Icon").withTooltipText("true"));
    call.prepareRows(rows);
    Assert.assertEquals("1 Row", 1, rows.size());
    Assert.assertEquals("Text", "Text " + "(" + TEXTS.get("RentalCard") + ")", rows.get(0).getText());
  }

  @Test
  public void testPrepareRows5() throws Exception {
    ECardLookupCall call = new ECardLookupCall();
    List<ILookupRow<Long>> rows = new ArrayList<>();
    rows.add(new LookupRow<Long>(-1L, "Text").withIconId("Icon").withTooltipText("ffffalse"));
    call.prepareRows(rows);
    Assert.assertEquals("1 Row", 1, rows.size());
    Assert.assertEquals("Text", "Text", rows.get(0).getText());
  }

  @Test
  public void testPrepareRows6() throws Exception {
    ECardLookupCall call = new ECardLookupCall();
    List<ILookupRow<Long>> rows = new ArrayList<>();
    rows.add(new LookupRow(-1L, "Text", "Icon", "false", "" + ECardTypeCodeType.SICard11Code.ID));
    call.prepareRows(rows);
    Assert.assertEquals("1 Row", 1, rows.size());
    Assert.assertEquals("Text", "Text" + " (" + TEXTS.get("SICard11") + ")", rows.get(0).getText());
  }

  @Test
  public void testPrepareRows7() throws Exception {
    ECardLookupCall call = new ECardLookupCall();
    List<ILookupRow<Long>> rows = new ArrayList<>();
    rows.add(new LookupRow(-1L, "Text", "Icon", "true", "" + ECardTypeCodeType.SICard11Code.ID));
    call.prepareRows(rows);
    Assert.assertEquals("1 Row", 1, rows.size());
    Assert.assertEquals("Text", "Text" + " (" + TEXTS.get("RentalCard") + ", " + TEXTS.get("SICard11") + ")", rows.get(0).getText());
  }

  @Test
  public void testPrepareRowsTooltipTextForInfoWindow() throws Exception {
    ECardLookupCall call = new ECardLookupCall();
    List<ILookupRow<Long>> rows = new ArrayList<>();
    rows.add(new LookupRow(-1L, "Text", "Icon", "true", "" + ECardTypeCodeType.SICard11Code.ID));
    call.prepareRows(rows);
    Assert.assertEquals("1 Row", 1, rows.size());
    Assert.assertEquals("Text", "Text" + " (" + TEXTS.get("RentalCard") + ", " + TEXTS.get("SICard11") + ")", rows.get(0).getText());
    Assert.assertEquals("Tooltip", "Text", rows.get(0).getTooltipText());
  }

}
