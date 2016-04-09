package com.rtiming.client.common.ui;

import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;

/**
 * 
 */
public class TimeDifferenceUtility {

  public static String formatValue(Long validValue) {
    String result = "";
    if (validValue != null) {
      DecimalFormat d = getSecondsDecimalFormat();
      result = d.format(validValue / 1000d);
    }
    return result;
  }

  public static Long parseValue(String text) throws ProcessingException {
    Long result = null;
    if (text != null) {
      // fix comma
      text = text.replace(",", ".");
      // fix spaces
      text = StringUtility.cleanup(text);
      text = StringUtility.replace(text, " ", "");
      // remove "s" 
      if (text.endsWith("s")) {
        text = StringUtility.removeSuffixes(text, "s");
      }
      // add " s"
      text = text + " s";

      // parse
      DecimalFormat d = getSecondsDecimalFormat();
      try {
        result = Math.round(NumberUtility.toDouble(d.parse(text)) * 1000);
      }
      catch (ParseException e) {
        throw new ProcessingException("?", e);
      }
    }
    return result;
  }

  private static DecimalFormat getSecondsDecimalFormat() {
    DecimalFormat d = new DecimalFormat();
    DecimalFormatSymbols newSymbols = new DecimalFormatSymbols();
    newSymbols.setDecimalSeparator('.');
    d.setDecimalFormatSymbols(newSymbols);
    d.setMinimumFractionDigits(3);
    d.setMaximumFractionDigits(3);
    d.setDecimalSeparatorAlwaysShown(true);
    d.setGroupingUsed(true);
    d.setPositiveSuffix(" s");
    d.setNegativeSuffix(" s");
    return d;
  }

}
