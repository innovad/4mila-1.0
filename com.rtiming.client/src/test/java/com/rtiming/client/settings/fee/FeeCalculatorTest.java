package com.rtiming.client.settings.fee;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.junit.Test;

import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.fee.FeeCalculator;
import com.rtiming.shared.settings.fee.FeeCalculator.AddInfoInput;
import com.rtiming.shared.settings.fee.FeeCalculator.FeeResult;
import com.rtiming.shared.settings.fee.FeeCalculator.RaceInput;
import com.rtiming.shared.settings.fee.FeeFormData;

public class FeeCalculatorTest {

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor1() throws Exception {
    new FeeCalculator(null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor2() throws Exception {
    new FeeCalculator(null, new EventConfiguration());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructor3() throws Exception {
    new FeeCalculator(new ArrayList<FeeFormData>(), null);
  }

  @Test
  public void testCalculateFees1() throws Exception {
    FeeCalculator calculator = new FeeCalculator(new ArrayList<FeeFormData>(), new EventConfiguration());
    FeeResult result = calculator.calculateFees(null, null, null, null);
    assertEquals("Sum", 0, result.getSum(), 0.0d);
  }

  @Test
  public void testCalculateAddInfoFee1() throws Exception {
    List<FeeFormData> feeConfiguration = new ArrayList<FeeFormData>();
    FeeFormData fee = new FeeFormData();
    fee.setAdditionalInformationUid(AdditionalInformationCodeType.IndividualStartFeeCode.ID);
    fee.getFee().setValue(100d);
    feeConfiguration.add(fee);

    FeeCalculator calculator = new FeeCalculator(feeConfiguration, new EventConfiguration());
    List<AddInfoInput> addInfos = new ArrayList<>();
    AddInfoInput addInfo = calculator.new AddInfoInput();
    addInfo.setAdditionalInformationUid(AdditionalInformationCodeType.IndividualStartFeeCode.ID);
    addInfo.setTypeUid(AdditionalInformationTypeCodeType.DoubleCode.ID);
    addInfo.setDecimal(2.5d);
    addInfos.add(addInfo);
    FeeResult result = calculator.calculateFees(null, addInfos, null, null);
    assertEquals("Sum", 250, result.getSum(), 0.0d);
  }

  @Test
  public void testClassFee1() throws Exception {
    EventConfiguration eventConfiguration = new EventConfiguration();
    EventBean event = new EventBean();
    event.setEventNr(11L);
    event.setEvtZero(DateUtility.parse("02072012", "ddMMYYY"));
    eventConfiguration.addEvents(event);

    List<FeeFormData> feeConfiguration = new ArrayList<FeeFormData>();
    FeeFormData feeFormData = new FeeFormData();
    feeFormData.setEventNr(11L);
    feeFormData.setClassUid(33L);
    feeFormData.getFee().setValue(123d);
    feeConfiguration.add(feeFormData);

    FeeCalculator calculator = new FeeCalculator(feeConfiguration, eventConfiguration);
    List<RaceInput> races = new ArrayList<>();
    RaceInput raceInput = calculator.new RaceInput(1L, 33L, 11L, 1980L);
    races.add(raceInput);
    FeeResult result = calculator.calculateFees(races, null, null, null);
    assertEquals("Sum", 123, result.getSum(), 0.0d);
  }

  @Test
  public void testClassFee2() throws Exception {
    FeeCalculator calculator = new FeeCalculator(new ArrayList<FeeFormData>(), new EventConfiguration());
    List<RaceInput> races = new ArrayList<>();
    RaceInput raceInput = calculator.new RaceInput(1L, 33L, null, 1980L);
    races.add(raceInput);
    FeeResult result = calculator.calculateFees(races, null, null, null);
    assertEquals("Sum", 0, result.getSum(), 0.0d);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMissingEvtZeroTime1() throws Exception {
    FeeCalculator calculator = new FeeCalculator(new ArrayList<FeeFormData>(), new EventConfiguration());
    List<RaceInput> races = new ArrayList<>();
    RaceInput raceInput = calculator.new RaceInput(1L, 33L, 11L, 1980L);
    races.add(raceInput);
    calculator.calculateFees(races, null, null, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testMissingEvtZeroTime2() throws Exception {
    EventConfiguration eventConfiguration = new EventConfiguration();
    EventBean event = new EventBean();
    event.setEventNr(11L);
    eventConfiguration.addEvents(event);
    FeeCalculator calculator = new FeeCalculator(new ArrayList<FeeFormData>(), eventConfiguration);
    List<RaceInput> races = new ArrayList<>();
    RaceInput raceInput = calculator.new RaceInput(1L, 33L, 11L, 1980L);
    races.add(raceInput);
    calculator.calculateFees(races, null, null, null);
  }

}
