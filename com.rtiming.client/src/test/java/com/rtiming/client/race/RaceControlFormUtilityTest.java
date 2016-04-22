package com.rtiming.client.race;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import com.rtiming.client.race.RaceControlForm.MainBox.ControlStatusField;
import com.rtiming.client.race.RaceControlForm.MainBox.ManualStatusField;
import com.rtiming.client.race.RaceControlForm.MainBox.TimeField;

/**
 * @author amo
 */
public class RaceControlFormUtilityTest {

  @Test
  public void testValidateManualStatus1() throws Exception {
    ManualStatusField manualStatusField = Mockito.mock(ManualStatusField.class);
    ControlStatusField controlStatusField = Mockito.mock(ControlStatusField.class);
    TimeField timeField = Mockito.mock(TimeField.class);
    Mockito.when(manualStatusField.getValue()).thenReturn(true);

    boolean result = RaceControlFormUtility.validateManualStatus(manualStatusField, controlStatusField, timeField);

    Assert.assertFalse("No reset", result);
    Mockito.verify(controlStatusField).setMandatory(true);
    Mockito.verify(controlStatusField).setEnabled(true);
    Mockito.verify(timeField).setEnabled(true);
  }

  @Test
  public void testValidateManualStatus2() throws Exception {
    ManualStatusField manualStatusField = Mockito.mock(ManualStatusField.class);
    ControlStatusField controlStatusField = Mockito.mock(ControlStatusField.class);
    Mockito.when(controlStatusField.isValueChanging()).thenReturn(true);
    TimeField timeField = Mockito.mock(TimeField.class);
    Mockito.when(manualStatusField.getValue()).thenReturn(false);

    boolean result = RaceControlFormUtility.validateManualStatus(manualStatusField, controlStatusField, timeField);
    Assert.assertTrue("Reset", result);
    Mockito.verify(controlStatusField).setMandatory(false);
    Mockito.verify(controlStatusField).setEnabled(false);
    Mockito.verify(timeField).setEnabled(false);
  }

}
