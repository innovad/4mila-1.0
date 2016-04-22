package com.rtiming.client.dataexchange.cache;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.dataexchange.DataExchangeUtility;
import com.rtiming.shared.runner.SexCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class DataExchangeUtilityTest {

  @Test
  public void testParseBooleanNull() throws Exception {
    boolean bool = DataExchangeUtility.parseBoolean(null);
    Assert.assertFalse("Should be FALSE", bool);
  }

  @Test
  public void testParseBooleanFalse1() throws Exception {
    boolean bool = DataExchangeUtility.parseBoolean("0");
    Assert.assertFalse("Should be FALSE", bool);
  }

  @Test
  public void testParseBooleanFalse2() throws Exception {
    boolean bool = DataExchangeUtility.parseBoolean("");
    Assert.assertFalse("Should be FALSE", bool);
  }

  @Test
  public void testParseBooleanTrue1() throws Exception {
    boolean bool = DataExchangeUtility.parseBoolean("1");
    Assert.assertTrue("Should be TRUE", bool);
  }

  @Test
  public void testParseBooleanTrue2() throws Exception {
    boolean bool = DataExchangeUtility.parseBoolean("ABC");
    Assert.assertTrue("Should be TRUE", bool);
  }

  @Test
  public void testParseSexMan() throws Exception {
    Long sexUid = DataExchangeUtility.parseSex("M");
    Assert.assertEquals("Should be man", SexCodeType.ManCode.ID, sexUid.longValue());
  }

  @Test
  public void testParseSexWoman1() throws Exception {
    Long sexUid = DataExchangeUtility.parseSex("W");
    Assert.assertEquals("Should be woman", SexCodeType.WomanCode.ID, sexUid.longValue());
  }

  @Test
  public void testParseSexWoman2() throws Exception {
    Long sexUid = DataExchangeUtility.parseSex("F");
    Assert.assertEquals("Should be woman", SexCodeType.WomanCode.ID, sexUid.longValue());
  }

  @Test
  public void testParseSexNull() throws Exception {
    Long sexUid = DataExchangeUtility.parseSex(null);
    Assert.assertNull("Sex should be null", sexUid);
  }

  @Test
  public void testParseSexInvalid() throws Exception {
    Long sexUid = DataExchangeUtility.parseSex("ABC");
    Assert.assertNull("Sex should be null", sexUid);
  }

  @Test
  public void testParseLongNull() throws Exception {
    Long result = DataExchangeUtility.parseLong(null);
    Assert.assertNull("Should be null", result);
  }

  @Test
  public void testParseLong() throws Exception {
    Long result = DataExchangeUtility.parseLong("11");
    Assert.assertEquals("Should be equals", 11, result.longValue());
  }

  @Test
  public void testParseInvalid() throws Exception {
    Long result = DataExchangeUtility.parseLong("ABC");
    Assert.assertNull("Should be null", result);
  }

  @Test
  public void testParseDoubleNull() throws Exception {
    Double result = DataExchangeUtility.parseDouble(null);
    Assert.assertNull("Should be null", result);
  }

  @Test
  public void testParseDoublePoint() throws Exception {
    Double result = DataExchangeUtility.parseDouble("1.3");
    Assert.assertEquals("Should be equals", 1.3d, result.doubleValue());
  }

  @Test
  public void testParseDoubleComma() throws Exception {
    Double result = DataExchangeUtility.parseDouble("1,3");
    Assert.assertNull("Should be null", result);
  }

  @Test
  public void testParseYear() throws Exception {
    Long year = DataExchangeUtility.parseYear("ABC");
    Assert.assertNull("Year should be null", year);
  }

  @Test
  public void testParseYearNull() throws Exception {
    Long year = DataExchangeUtility.parseYear(null);
    Assert.assertNull("Year should be null", year);
  }

  @Test
  public void testParseYearMax() throws Exception {
    Long year = DataExchangeUtility.parseYear("9999");
    Assert.assertEquals("Year", 9999L, year.longValue());
  }

  @Test
  public void testParseYearTooLarge() throws Exception {
    Long year = DataExchangeUtility.parseYear("99999");
    Assert.assertNull("Year should be null", year);
  }

  @Test
  public void testParseYearTooSmall() throws Exception {
    Long year = DataExchangeUtility.parseYear("-5");
    Assert.assertNull("Year should be null", year);
  }

  @Test
  public void testParseYearMin1900() throws Exception {
    Long year = DataExchangeUtility.parseYear("1900");
    Assert.assertEquals("Year", 1900L, year.longValue());
  }

  @Test
  public void testParseYearMin2000() throws Exception {
    Long year = DataExchangeUtility.parseYear("0");
    Assert.assertEquals("Year", 2000L, year.longValue());
  }

  @Test
  public void testParseYear2digit() throws Exception {
    GregorianCalendar greg = new GregorianCalendar();
    greg.setTime(new Date());
    int currentYear = greg.get(Calendar.YEAR);
    int currentYear2digit = currentYear % 100;

    Long year = DataExchangeUtility.parseYear("" + (currentYear2digit + 1));
    Assert.assertEquals("Year", currentYear - 100 + 1, year.longValue());
  }

  @Test
  public void testParseYear2digit2() throws Exception {
    GregorianCalendar greg = new GregorianCalendar();
    greg.setTime(new Date());
    int currentYear = greg.get(Calendar.YEAR);
    int currentYear2digit = currentYear % 100;

    Long year = DataExchangeUtility.parseYear("" + (currentYear2digit - 1));
    Assert.assertEquals("Year", currentYear - 1, year.longValue());
  }

  @Test
  public void testParseYear2digit3() throws Exception {
    GregorianCalendar greg = new GregorianCalendar();
    greg.setTime(new Date());
    int currentYear = greg.get(Calendar.YEAR);
    int currentYear2digit = currentYear % 100;

    Long year = DataExchangeUtility.parseYear("" + currentYear2digit);
    Assert.assertEquals("Year", currentYear - 100, year.longValue());
  }

  @Test
  public void testParseLongExNull() throws Exception {
    Long result = DataExchangeUtility.parseLongEx(null);
    Assert.assertEquals("Should be 0", 0, result.longValue());
  }

  @Test
  public void testParseLongExEmpty() throws Exception {
    Long result = DataExchangeUtility.parseLongEx("");
    Assert.assertEquals("Should be 0", 0, result.longValue());
  }

  @Test
  public void testParseLongExNumber() throws Exception {
    Long result = DataExchangeUtility.parseLongEx("7");
    Assert.assertEquals("Should be 7", 7, result.longValue());
  }

  @Test
  public void testParseLongExInvalid() throws Exception {
    Long result = DataExchangeUtility.parseLongEx("E7");
    Assert.assertEquals("Should be 0", 0, result.longValue());
  }

  @Test
  public void testParseDateEmpty() throws Exception {
    Date result = DataExchangeUtility.parseDate(new Date(), null);
    Assert.assertNull("Year should be null", result);
  }

  @Test
  public void testParseDateNull() throws Exception {
    Date result = DataExchangeUtility.parseDate(new Date(), "");
    Assert.assertNull("Year should be null", result);
  }

  @Test
  public void testParseDateInvalid() throws Exception {
    Date result = DataExchangeUtility.parseDate(new Date(), "ABC");
    Assert.assertNull("Year should be null", result);
  }

  @Test
  public void testParseDate1() throws Exception {
    Date evtZero = new Date();
    Date result = DataExchangeUtility.parseDate(evtZero, "1:00:00");
    Assert.assertEquals("Should be equal", DateUtility.addHours(evtZero, 1), result);
  }

  @Test
  public void testParseDate2() throws Exception {
    Date evtZero = new Date();
    Date result = DataExchangeUtility.parseDate(evtZero, "0:0:1");
    Assert.assertEquals("Should be equal", FMilaUtility.addSeconds(evtZero, 1), result);
  }

  @Test
  public void testGetCodeText() throws Exception {
    String text = DataExchangeUtility.getCodeText(AdditionalInformationCodeType.IndividualStartFeeCode.ID, AdditionalInformationCodeType.class);
    Assert.assertEquals("Should be equal", Texts.get("IndividualStartFee"), text);
  }

  @Test
  public void testGetCodeTextNull1() throws Exception {
    String text = DataExchangeUtility.getCodeText(null, AdditionalInformationCodeType.class);
    Assert.assertEquals("Should be equal", "", text);
  }

  @Test
  public void testGetCodeTextNull2() throws Exception {
    String text = DataExchangeUtility.getCodeText(AdditionalInformationCodeType.IndividualStartFeeCode.ID, null);
    Assert.assertEquals("Should be equal", "", text);
  }

}
