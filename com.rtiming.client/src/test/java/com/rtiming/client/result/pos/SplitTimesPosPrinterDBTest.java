package com.rtiming.client.result.pos;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.test.data.EntryTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualClassTestDataProvider;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.event.course.ControlStatusCodeType;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class SplitTimesPosPrinterDBTest {

  private EventWithIndividualClassTestDataProvider event;
  private EventWithIndividualValidatedRaceTestDataProvider validatedEvent;
  private EntryTestDataProvider entry;

  @Test(expected = VetoException.class)
  public void testSplitTimesPosPrinterNotValidated() throws Exception {
    event = new EventWithIndividualClassTestDataProvider();
    entry = new EntryTestDataProvider(event.getEventNr(), event.getClassUid());
    TestPosPrinter printer = new TestPosPrinter();
    SplitTimesPosPrinter.printSplitTimesReport(entry.getRaceNr(), printer);
  }

  @Test
  public void testSplitTimesPosPrinter() throws Exception {
    validatedEvent = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31"});
    TestPosPrinter printer = new TestPosPrinter();
    SplitTimesPosPrinter.printSplitTimesReport(validatedEvent.getRaceNr(), printer);
    String printout = printer.getPrintout();
    Assert.assertTrue(printout.contains(" 31 "));
    Assert.assertFalse(printout.contains(CODES.getCode(ControlStatusCodeType.MissingCode.class).getText()));
    Assert.assertFalse(printout.contains(CODES.getCode(ControlStatusCodeType.WrongCode.class).getText()));
    Assert.assertFalse(printout.contains(CODES.getCode(ControlStatusCodeType.AdditionalCode.class).getText()));
  }

  @Test
  public void testSplitTimesPosPrinterMissingControl() throws Exception {
    validatedEvent = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{});
    TestPosPrinter printer = new TestPosPrinter();
    SplitTimesPosPrinter.printSplitTimesReport(validatedEvent.getRaceNr(), printer);
    String printout = printer.getPrintout();
    Assert.assertTrue("Printout should contain No. 31", printout.contains(" 31 "));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.MissingCode.class).getText()));
    Assert.assertFalse(printout.contains(CODES.getCode(ControlStatusCodeType.WrongCode.class).getText()));
    Assert.assertFalse(printout.contains(CODES.getCode(ControlStatusCodeType.AdditionalCode.class).getText()));
  }

  @Test
  public void testSplitTimesPosPrinterAdditionalControl() throws Exception {
    validatedEvent = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"31", "32"});
    TestPosPrinter printer = new TestPosPrinter();
    SplitTimesPosPrinter.printSplitTimesReport(validatedEvent.getRaceNr(), printer);
    String printout = printer.getPrintout();
    Assert.assertTrue("Printout should contain No. 31", printout.contains(" 31 "));
    Assert.assertTrue("Printout should contain No. 32", printout.contains(" 32 "));
    Assert.assertFalse(printout.contains(CODES.getCode(ControlStatusCodeType.MissingCode.class).getText()));
    Assert.assertFalse(printout.contains(CODES.getCode(ControlStatusCodeType.WrongCode.class).getText()));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.AdditionalCode.class).getText()));
  }

  @Test
  public void testSplitTimesPosPrinterMissingAndWrong1() throws Exception {
    validatedEvent = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31", "32"}, new String[]{"32", "31"});
    TestPosPrinter printer = new TestPosPrinter();
    SplitTimesPosPrinter.printSplitTimesReport(validatedEvent.getRaceNr(), printer);
    String printout = printer.getPrintout();
    Assert.assertTrue("Printout should contain No. 31", printout.contains(" 31 "));
    Assert.assertTrue("Printout should contain No. 32", printout.contains(" 32 "));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.MissingCode.class).getText()));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.WrongCode.class).getText()));
    Assert.assertFalse(printout.contains(CODES.getCode(ControlStatusCodeType.AdditionalCode.class).getText()));
  }

  @Test
  public void testSplitTimesPosPrinterMissingAndWrong2() throws Exception {
    validatedEvent = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31", "32"}, new String[]{"32", "31", "99"});
    TestPosPrinter printer = new TestPosPrinter();
    SplitTimesPosPrinter.printSplitTimesReport(validatedEvent.getRaceNr(), printer);
    String printout = printer.getPrintout();
    Assert.assertTrue("Printout should contain No. 31", printout.contains(" 31 "));
    Assert.assertTrue("Printout should contain No. 32", printout.contains(" 32 "));
    Assert.assertTrue("Printout should contain No. 99", printout.contains(" 99 "));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.MissingCode.class).getText()));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.WrongCode.class).getText()));
    Assert.assertFalse(printout.contains(CODES.getCode(ControlStatusCodeType.AdditionalCode.class).getText()));
  }

  @Test
  public void testSplitTimesPosPrinterMissingAndWrong3() throws Exception {
    validatedEvent = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31", "32"}, new String[]{"99"});
    TestPosPrinter printer = new TestPosPrinter();
    SplitTimesPosPrinter.printSplitTimesReport(validatedEvent.getRaceNr(), printer);
    String printout = printer.getPrintout();
    Assert.assertTrue("Printout should contain No. 31", printout.contains(" 31 "));
    Assert.assertTrue("Printout should contain No. 32", printout.contains(" 32 "));
    Assert.assertTrue("Printout should contain No. 99", printout.contains(" 99 "));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.MissingCode.class).getText()));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.WrongCode.class).getText()));
    Assert.assertFalse(printout.contains(CODES.getCode(ControlStatusCodeType.AdditionalCode.class).getText()));
  }

  @Test
  public void testSplitTimesPosPrinterMissingAndWrong4() throws Exception {
    validatedEvent = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31", "32", "33"}, new String[]{"32", "31"});
    TestPosPrinter printer = new TestPosPrinter();
    SplitTimesPosPrinter.printSplitTimesReport(validatedEvent.getRaceNr(), printer);
    String printout = printer.getPrintout();
    Assert.assertTrue("Printout should contain No. 31", printout.contains(" 31 "));
    Assert.assertTrue("Printout should contain No. 32", printout.contains(" 32 "));
    Assert.assertTrue("Printout should contain No. 33", printout.contains(" 33 "));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.MissingCode.class).getText()));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.WrongCode.class).getText()));
    Assert.assertFalse(printout.contains(CODES.getCode(ControlStatusCodeType.AdditionalCode.class).getText()));
  }

  @Test
  public void testSplitTimesPosPrinterMissingAndWrongAndAdditional() throws Exception {
    validatedEvent = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{"32", "99"});
    TestPosPrinter printer = new TestPosPrinter();
    SplitTimesPosPrinter.printSplitTimesReport(validatedEvent.getRaceNr(), printer);
    String printout = printer.getPrintout();
    Assert.assertTrue("Printout should contain No. 31", printout.contains(" 31 "));
    Assert.assertTrue("Printout should contain No. 32", printout.contains(" 32 "));
    Assert.assertTrue("Printout should contain No. 99", printout.contains(" 99 "));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.MissingCode.class).getText()));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.WrongCode.class).getText()));
    Assert.assertTrue(printout.contains(CODES.getCode(ControlStatusCodeType.AdditionalCode.class).getText()));
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
    if (validatedEvent != null) {
      validatedEvent.remove();
    }
    if (entry != null) {
      entry.remove();
    }
  }

}
