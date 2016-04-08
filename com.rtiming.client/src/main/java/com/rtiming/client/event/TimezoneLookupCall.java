package com.rtiming.client.event;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;
import java.util.TreeSet;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.date.DateUtility;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

public class TimezoneLookupCall extends LocalLookupCall<Integer> {

  private static final long serialVersionUID = 1L;
  private Date m_date;
  private boolean m_longText;

  @Override
  protected List<LookupRow<Integer>> execCreateLookupRows() throws ProcessingException {
    List<LookupRow<Integer>> rows = new ArrayList<LookupRow<Integer>>();
    String[] zoneIds = TimeZone.getAvailableIDs();
    Date date = DateUtility.nvl(getDate(), new Date());

    TreeSet<Integer> distinctOffsets = new TreeSet<Integer>();
    for (String zone : zoneIds) {
      TimeZone timeZone = TimeZone.getTimeZone(zone);
      distinctOffsets.add(timeZone.getOffset(date.getTime()));
    }

    for (int offset : distinctOffsets) {
      TreeSet<String> displayNames = new TreeSet<String>();
      for (String zone : zoneIds) {
        if (TimeZone.getTimeZone(zone).getOffset(date.getTime()) == offset) {
          boolean inDaylightTime = TimeZone.getTimeZone(zone).inDaylightTime(date);
          String name = TimeZone.getTimeZone(zone).getDisplayName(inDaylightTime, TimeZone.LONG);
          if (!displayNames.contains(name)) {
            displayNames.add(name);
          }
        }
      }
      String displayName = null;
      if (m_longText) {
        displayName = NumberUtility.format(Double.valueOf(offset) / 1000 / 3600) + " " + displayNames.toString();
      }
      else {
        displayName = NumberUtility.format(Double.valueOf(offset) / 1000 / 3600);
        if (offset > 0) {
          displayName = "+" + displayName;
        }
      }
      LookupRow row = new LookupRow(offset, displayName);
      rows.add(row);
    }
    return rows;
  }

  @FormData
  public Date getDate() {
    return m_date;
  }

  @FormData
  public void setDate(Date date) {
    m_date = date;
  }

  @FormData
  public boolean isLongText() {
    return m_longText;
  }

  @FormData
  public void setLongText(boolean longText) {
    m_longText = longText;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((m_date == null) ? 0 : m_date.hashCode());
    result = prime * result + (m_longText ? 1231 : 1237);
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (!super.equals(obj)) {
      return false;
    }
    if (getClass() != obj.getClass()) {
      return false;
    }
    TimezoneLookupCall other = (TimezoneLookupCall) obj;
    if (m_date == null) {
      if (other.m_date != null) {
        return false;
      }
    }
    else if (!m_date.equals(other.m_date)) {
      return false;
    }
    if (m_longText != other.m_longText) {
      return false;
    }
    return true;
  }

}
