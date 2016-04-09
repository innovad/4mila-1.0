package com.rtiming.shared.ecard;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.ColorUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.shared.Texts;

public class ECardLookupCall extends LookupCall<Long> {

  private static final long serialVersionUID = 1L;

  @Override
  protected Class<IECardLookupService> getConfiguredService() {
    return IECardLookupService.class;
  }

  @Override
  public List<? extends ILookupRow<Long>> getDataByAll() throws ProcessingException {
    List<? extends ILookupRow<Long>> rows = super.getDataByAll();
    prepareRows(rows);
    return rows;
  }

  @Override
  public List<? extends ILookupRow<Long>> getDataByKey() throws ProcessingException {
    List<? extends ILookupRow<Long>> rows = super.getDataByKey();
    prepareRows(rows);
    return rows;
  }

  @Override
  public List<? extends ILookupRow<Long>> getDataByText() throws ProcessingException {
    List<? extends ILookupRow<Long>> rows = super.getDataByText();
    prepareRows(rows);
    return rows;
  }

  public void prepareRows(List<? extends ILookupRow<Long>> rows) {
    if (rows == null) {
      return;
    }
    for (ILookupRow<Long> row : rows) {
      String rentalCard = null;
      String typeText = null;
      row.setForegroundColor(ColorUtility.BLACK);
      if (!StringUtility.isNullOrEmpty(row.getTooltipText()) && row.getTooltipText().equalsIgnoreCase(Boolean.TRUE.toString())) {
        row.setForegroundColor(ColorUtility.BLUE);
        rentalCard = Texts.get("RentalCard");
      }
      row.setTooltipText(row.getText());
      if (!StringUtility.isNullOrEmpty(row.getBackgroundColor())) {
        Long id = NumberUtility.parseLong(row.getBackgroundColor());
        if (id != null && id != 0) {
          ECardTypeCodeType codeType = CODES.getCodeType(ECardTypeCodeType.class);
          ICode code = codeType.getCode(id);
          if (code != null) {
            typeText = code.getText();
          }
        }
      }
      row.setBackgroundColor(null);
      if (rentalCard != null && typeText != null) {
        row.setText(row.getText() + " (" + rentalCard + ", " + typeText + ")");
      }
      else if (rentalCard != null || typeText != null) {
        row.setText(row.getText() + " (" + (rentalCard != null ? rentalCard : typeText) + ")");
      }
    }
  }

}
