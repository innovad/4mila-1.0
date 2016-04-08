package com.rtiming.client.race;

import org.eclipse.scout.commons.BooleanUtility;

import com.rtiming.client.race.RaceControlForm.MainBox.ControlStatusField;
import com.rtiming.client.race.RaceControlForm.MainBox.ManualStatusField;
import com.rtiming.client.race.RaceControlForm.MainBox.TimeField;
import com.rtiming.shared.event.course.ControlStatusCodeType;

/**
 * 
 */
public final class RaceControlFormUtility {

  public static boolean validateManualStatus(ManualStatusField manualStatusField, ControlStatusField controlStatusField, TimeField timeField) {
    boolean isManualStatus = BooleanUtility.nvl(manualStatusField.getValue());
    controlStatusField.setEnabled(isManualStatus);
    controlStatusField.setMandatory(isManualStatus);
    timeField.setEnabled(isManualStatus);

    Long controlStatusUid = controlStatusField.getValue();
    if (controlStatusUid != null &&
        controlStatusUid != ControlStatusCodeType.OkCode.ID &&
        controlStatusUid != ControlStatusCodeType.MissingCode.ID) {
      controlStatusField.setValue(null);
    }

    if (!isManualStatus) {
      return true;
    }
    return false;
  }

}
