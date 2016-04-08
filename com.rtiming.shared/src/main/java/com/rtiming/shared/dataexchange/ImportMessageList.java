package com.rtiming.shared.dataexchange;

import java.io.Serializable;
import java.util.ArrayList;

import com.rtiming.shared.Texts;

public class ImportMessageList implements Serializable {

  private static final long serialVersionUID = 1L;
  private final ArrayList<ImportMessage> errors = new ArrayList<ImportMessage>();

  public ArrayList<ImportMessage> getErrors() {
    return errors;
  }

  public boolean hasMessages() {
    return (errors.size()) > 0;
  }

  public String toHtml() {
    StringBuffer errorReport = new StringBuffer();
    String style = "style=\"border-width:1px; border-style:solid;\"";

    if (this.hasMessages()) {
      errorReport.append("<table width=\"100%\">");
      errorReport.append("<tr valign=\"top\"><th align=\"left\" " + style + ">");
      errorReport.append(Texts.get("DataRecord"));
      errorReport.append("</th><th align=\"left\" " + style + ">");
      errorReport.append(Texts.get("Message"));
      errorReport.append("</th></tr>");
      for (ImportMessage line : this.getErrors()) {
        String message = line.getMessage();
        errorReport.append("<tr valign=\"top\">");
        errorReport.append("<td " + style + ">");
        errorReport.append(((line.getItem() != null) ? line.getItem() : "") + "</td>");
        errorReport.append("<td " + style + ">" + ((message != null) ? message : ""));
        errorReport.append("</td></tr>");
      }
      errorReport.append("</table>");
    }

    return errorReport.toString();
  }

}
