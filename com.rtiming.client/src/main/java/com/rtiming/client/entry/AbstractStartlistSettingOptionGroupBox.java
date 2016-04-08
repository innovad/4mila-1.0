package com.rtiming.client.entry;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.rt.client.ui.form.fields.radiobuttongroup.AbstractRadioButtonGroup;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.lookup.ILookupCall;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.shared.entry.startlist.StartlistSettingOptionCodeType;

public class AbstractStartlistSettingOptionGroupBox extends AbstractRadioButtonGroup<Long> {

  @Override
  protected int getConfiguredGridW() {
    return 2;
  }

  @Override
  protected String getConfiguredLabel() {
    return TEXTS.get("StartlistOption");
  }

  @Override
  protected Class<? extends ICodeType<Long, Long>> getConfiguredCodeType() {
    return StartlistSettingOptionCodeType.class;
  }

  @Override
  protected void execFilterLookupResult(ILookupCall<Long> call, List<ILookupRow<Long>> result) {
    List<ILookupRow<Long>> filtered = new ArrayList<ILookupRow<Long>>();
    filtered.add(new LookupRow(null, TEXTS.get("None")));
    for (ILookupRow<Long> row : result) {
      if (CompareUtility.equals(row.getKey(), StartlistSettingOptionCodeType.GroupRegistrationsCode.ID) || CompareUtility.equals(row.getKey(), StartlistSettingOptionCodeType.SplitRegistrationsCode.ID)) {
        filtered.add(row);
      }
    }
    result.clear();
    result.addAll(filtered);
  }

}
