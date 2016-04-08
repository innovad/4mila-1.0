package com.rtiming.server.settings;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.runner.SexCodeType;

public class ClassAgeUtility {

  public static List<String> calculateAgeText(List<RtClassAge> classAgeList) {
    List<String> ageTexts = new ArrayList<>();
    if (classAgeList == null || classAgeList.isEmpty()) {
      StringBuilder text = new StringBuilder();
      text.append(TEXTS.get("Man") + "/" + TEXTS.get("Woman"));
      text.append(": ");
      text.append(TEXTS.get("NoAgeLimit"));
      ageTexts.add(text.toString());
    }
    else {
      for (RtClassAge classAge : classAgeList) {
        StringBuilder text = new StringBuilder();
        if (CompareUtility.equals(classAge.getSexUid(), SexCodeType.ManCode.ID)) {
          text.append(TEXTS.get("Man"));
        }
        else if (CompareUtility.equals(classAge.getSexUid(), SexCodeType.WomanCode.ID)) {
          text.append(TEXTS.get("Woman"));
        }
        else {
          text.append(TEXTS.get("Man") + "/" + TEXTS.get("Woman"));
        }
        text.append(": ");
        if (classAge.getAgeFrom() == null && classAge.getAgeTo() == null) {
          text.append(TEXTS.get("NoAgeLimit"));
        }
        else {
          text.append(StringUtility.emptyIfNull(classAge.getAgeFrom()));
          text.append(" - ");
          text.append(StringUtility.emptyIfNull(classAge.getAgeTo()));
        }
        ageTexts.add(text.toString());
      }
    }
    return ageTexts;
  }

}
