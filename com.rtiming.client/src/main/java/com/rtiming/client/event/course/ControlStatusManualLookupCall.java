package com.rtiming.client.event.course;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.shared.event.course.ControlStatusCodeType;

/**
 * 
 */
public class ControlStatusManualLookupCall extends LocalLookupCall<Long> {

  private static final long serialVersionUID = 1L;

  @Override
  protected List<LookupRow<Long>> execCreateLookupRows() throws ProcessingException {
    boolean isNewSelection = getKey() == null;
    List<LookupRow<Long>> result = new ArrayList<>();
    List<? extends ICode<Long>> codes = CODES.getCodeType(ControlStatusCodeType.class).getCodes();
    for (ICode code : codes) {
      LookupRow row = new LookupRow(code.getId(), code.getText());
      if (isNewSelection) {
        if (ControlStatusCodeType.OkCode.ID == (Long) code.getId() || ControlStatusCodeType.MissingCode.ID == (Long) code.getId()) {
          result.add(row);
        }
      }
      else {
        result.add(row);
      }
    }
    return result;
  }

}
