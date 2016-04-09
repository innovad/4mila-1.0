package com.rtiming.client.result.pos;

import org.eclipse.scout.rt.platform.util.StringUtility;

public class SplitBuilder {

  private final StringBuilder builder;
  private final int normalWidth;

  public SplitBuilder(int normalWidth) {
    builder = new StringBuilder();
    this.normalWidth = normalWidth;
  }

  public void appendPadLeft(String s, int n) {
    if (s == null) {
      s = "";
    }
    if (n <= 0) {
      builder.append(s);
    }
    else {
      if (s.length() > n) {
        s = s.substring(0, n);
      }
      builder.append(String.format("%1$" + n + "s", s));
    }
  }

  public void appendLeftRight(String left, String right) {
    int pad = Math.max(normalWidth - StringUtility.length(left), 0);
    append(left);
    appendPadLeft(right, pad);
  }

  public void appendLeftRightWide(String left, String right) {
    int pad = (normalWidth / 2) - StringUtility.length(left);
    builder.append(PosPrinterUtility.startWide());
    append(left);
    appendPadLeft(right, pad);
    builder.append(PosPrinterUtility.startNormal());
  }

  public void appendRight(String right) {
    appendPadLeft(right, normalWidth);
  }

  public void appendNewLine() {
    builder.append("\n");
  }

  public void append(String s) {
    if (!StringUtility.isNullOrEmpty(s)) {
      builder.append(s);
    }
  }

  public void appendWide(String s) {
    builder.append(PosPrinterUtility.startWide());
    append(s);
    builder.append(PosPrinterUtility.startNormal());
  }

  public void cut() {
    append(PosPrinterUtility.cut());
  }

  @Override
  public String toString() {
    return builder.toString();
  }

}
