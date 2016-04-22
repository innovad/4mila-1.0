package com.rtiming.client.result.pos;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.rtiming.shared.event.course.ControlStatusCodeType;
import com.rtiming.shared.event.course.ControlTypeCodeType;

public class SplitTimesPosPrinterTest {

  @Test
  public void testStatus1() throws Exception {
    Map<String, String> generalData = new HashMap<String, String>();
    TestPosPrinter printer = new TestPosPrinter();
    List<SplitDataBean> splitData = new ArrayList<>();

    SplitTimesPosPrinter.printSplitTimesInternal(splitData, generalData, printer, "ADDITIONAL", "WRONG", "MISSING");
  }

  @Test
  public void testStatus2() throws Exception {
    Map<String, String> generalData = new HashMap<String, String>();
    TestPosPrinter printer = new TestPosPrinter();
    List<SplitDataBean> splitData = new ArrayList<>();
    addControlWithStatus(splitData, ControlStatusCodeType.MissingCode.ID);

    SplitTimesPosPrinter.printSplitTimesInternal(splitData, generalData, printer, "ADDITIONAL", "WRONG", "MISSING");
    Assert.assertFalse("Additional", printer.getPrintout().contains("ADDITIONAL"));
    Assert.assertFalse("Wrong", printer.getPrintout().contains("WRONG"));
    Assert.assertTrue("Missing", printer.getPrintout().contains("MISSING"));
  }

  @Test
  public void testStatus3() throws Exception {
    Map<String, String> generalData = new HashMap<String, String>();
    TestPosPrinter printer = new TestPosPrinter();
    List<SplitDataBean> splitData = new ArrayList<>();
    addControlWithStatus(splitData, ControlStatusCodeType.AdditionalCode.ID);

    SplitTimesPosPrinter.printSplitTimesInternal(splitData, generalData, printer, "ADDITIONAL", "WRONG", "MISSING");
    Assert.assertTrue("Additional", printer.getPrintout().contains("ADDITIONAL"));
    Assert.assertFalse("Wrong", printer.getPrintout().contains("WRONG"));
    Assert.assertFalse("Missing", printer.getPrintout().contains("MISSING"));
  }

  @Test
  public void testStatus4() throws Exception {
    Map<String, String> generalData = new HashMap<String, String>();
    TestPosPrinter printer = new TestPosPrinter();
    List<SplitDataBean> splitData = new ArrayList<>();
    addControlWithStatus(splitData, ControlStatusCodeType.WrongCode.ID);

    SplitTimesPosPrinter.printSplitTimesInternal(splitData, generalData, printer, "ADDITIONAL", "WRONG", "MISSING");
    Assert.assertFalse("Additional", printer.getPrintout().contains("ADDITIONAL"));
    Assert.assertTrue("Wrong", printer.getPrintout().contains("WRONG"));
    Assert.assertFalse("Missing", printer.getPrintout().contains("MISSING"));
  }

  @Test
  public void testStatus5() throws Exception {
    Map<String, String> generalData = new HashMap<String, String>();
    TestPosPrinter printer = new TestPosPrinter();
    List<SplitDataBean> splitData = new ArrayList<>();
    addControlWithStatus(splitData, ControlStatusCodeType.AdditionalCode.ID);
    addControlWithStatus(splitData, ControlStatusCodeType.WrongCode.ID);
    addControlWithStatus(splitData, ControlStatusCodeType.MissingCode.ID);
    addControlWithStatus(splitData, ControlStatusCodeType.OkCode.ID);

    SplitTimesPosPrinter.printSplitTimesInternal(splitData, generalData, printer, "ADDITIONAL", "WRONG", "MISSING");
    Assert.assertTrue("Additional", printer.getPrintout().contains("ADDITIONAL"));
    Assert.assertTrue("Wrong", printer.getPrintout().contains("WRONG"));
    Assert.assertTrue("Missing", printer.getPrintout().contains("MISSING"));
  }

  @Test
  public void testStatus6() throws Exception {
    Map<String, String> generalData = new HashMap<String, String>();
    TestPosPrinter printer = new TestPosPrinter();
    List<SplitDataBean> splitData = new ArrayList<>();
    addControlWithStatus(splitData, ControlStatusCodeType.OkCode.ID);

    SplitTimesPosPrinter.printSplitTimesInternal(splitData, generalData, printer, "ADDITIONAL", "WRONG", "MISSING");
    Assert.assertFalse("Additional", printer.getPrintout().contains("ADDITIONAL"));
    Assert.assertFalse("Wrong", printer.getPrintout().contains("WRONG"));
    Assert.assertFalse("Missing", printer.getPrintout().contains("MISSING"));
  }

  @Test
  public void testInitialControl1() throws Exception {
    Map<String, String> generalData = new HashMap<String, String>();
    TestPosPrinter printer = new TestPosPrinter();
    List<SplitDataBean> splitData = new ArrayList<>();
    SplitDataBean control1 = Mockito.mock(SplitDataBean.class);
    Mockito.when(control1.getControlTypeUid()).thenReturn(ControlTypeCodeType.ControlCode.ID);
    Mockito.when(control1.getControlStatusUid()).thenReturn(ControlStatusCodeType.OkCode.ID);
    Mockito.when(control1.getControlNo()).thenReturn("123");
    splitData.add(control1);

    SplitTimesPosPrinter.printSplitTimesInternal(splitData, generalData, printer, "ADDITIONAL", "WRONG", "MISSING");
    Assert.assertTrue("Control exists", printer.getPrintout().contains("123"));
  }

  @Test
  public void testInitialControl2() throws Exception {
    Map<String, String> generalData = new HashMap<String, String>();
    TestPosPrinter printer = new TestPosPrinter();
    List<SplitDataBean> splitData = new ArrayList<>();
    SplitDataBean control1 = Mockito.mock(SplitDataBean.class);
    Mockito.when(control1.getControlTypeUid()).thenReturn(ControlTypeCodeType.ControlCode.ID);
    Mockito.when(control1.getControlStatusUid()).thenReturn(ControlStatusCodeType.InitialStatusCode.ID);
    Mockito.when(control1.getControlNo()).thenReturn("123");
    splitData.add(control1);

    SplitTimesPosPrinter.printSplitTimesInternal(splitData, generalData, printer, "ADDITIONAL", "WRONG", "MISSING");
    Assert.assertFalse("Control exists", printer.getPrintout().contains("123"));
  }

  @Test
  public void testAdditionalSortCode1() throws Exception {
    Map<String, String> generalData = new HashMap<String, String>();
    TestPosPrinter printer = new TestPosPrinter();
    List<SplitDataBean> splitData = new ArrayList<>();
    SplitDataBean control1 = Mockito.mock(SplitDataBean.class);
    Mockito.when(control1.getControlTypeUid()).thenReturn(ControlTypeCodeType.ControlCode.ID);
    Mockito.when(control1.getControlStatusUid()).thenReturn(ControlStatusCodeType.OkCode.ID);
    Mockito.when(control1.getSortCode()).thenReturn(99999L);
    splitData.add(control1);

    SplitTimesPosPrinter.printSplitTimesInternal(splitData, generalData, printer, "ADDITIONAL", "WRONG", "MISSING");
    Assert.assertTrue("SortCode exists", printer.getPrintout().contains("99999"));
  }

  @Test
  public void testAdditionalSortCode2() throws Exception {
    Map<String, String> generalData = new HashMap<String, String>();
    TestPosPrinter printer = new TestPosPrinter();
    List<SplitDataBean> splitData = new ArrayList<>();
    SplitDataBean control1 = Mockito.mock(SplitDataBean.class);
    Mockito.when(control1.getControlTypeUid()).thenReturn(ControlTypeCodeType.ControlCode.ID);
    Mockito.when(control1.getControlStatusUid()).thenReturn(ControlStatusCodeType.AdditionalCode.ID);
    Mockito.when(control1.getSortCode()).thenReturn(99999L);
    splitData.add(control1);

    SplitTimesPosPrinter.printSplitTimesInternal(splitData, generalData, printer, "ADDITIONAL", "WRONG", "MISSING");
    Assert.assertFalse("SortCode exists", printer.getPrintout().contains("99999"));
  }

  @Test
  public void testWrongSortCode1() throws Exception {
    Map<String, String> generalData = new HashMap<String, String>();
    TestPosPrinter printer = new TestPosPrinter();
    List<SplitDataBean> splitData = new ArrayList<>();
    SplitDataBean control1 = Mockito.mock(SplitDataBean.class);
    Mockito.when(control1.getControlTypeUid()).thenReturn(ControlTypeCodeType.ControlCode.ID);
    Mockito.when(control1.getControlStatusUid()).thenReturn(ControlStatusCodeType.WrongCode.ID);
    Mockito.when(control1.getSortCode()).thenReturn(99999L);
    splitData.add(control1);

    SplitTimesPosPrinter.printSplitTimesInternal(splitData, generalData, printer, "ADDITIONAL", "WRONG", "MISSING");
    Assert.assertFalse("SortCode exists", printer.getPrintout().contains("99999"));
  }

  private void addControlWithStatus(List<SplitDataBean> splitData, long statusUid) {
    SplitDataBean control1 = Mockito.mock(SplitDataBean.class);
    Mockito.when(control1.getControlStatusUid()).thenReturn(statusUid);
    splitData.add(control1);
  }

}
