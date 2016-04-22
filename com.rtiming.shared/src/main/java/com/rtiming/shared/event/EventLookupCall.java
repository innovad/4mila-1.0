package com.rtiming.shared.event;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.ILookupService;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

public class EventLookupCall extends LookupCall<Long> {

  private static final long serialVersionUID = 1L;
  private List<Long> restrictToEventNrs;

  @Override
  protected Class<? extends ILookupService<Long>> getConfiguredService() {
    return IEventLookupService.class;
  }

  @Override
  public List<? extends ILookupRow<Long>> getDataByAll() throws ProcessingException {
    List<? extends ILookupRow<Long>> rows = super.getDataByAll();
    if (restrictToEventNrs != null) {
      List<ILookupRow<Long>> filtered = new ArrayList<ILookupRow<Long>>();
      for (ILookupRow<Long> row : rows) {
        if (restrictToEventNrs.contains(row.getKey())) {
          filtered.add(row);
        }
      }
      return filtered;
    }
    return rows;
  }

  @Override
  public List<? extends ILookupRow<Long>> getDataByText() throws ProcessingException {
    List<? extends ILookupRow<Long>> rows = super.getDataByText();
    if (restrictToEventNrs != null) {
      List<ILookupRow<Long>> filtered = new ArrayList<ILookupRow<Long>>();
      for (ILookupRow<Long> row : rows) {
        if (restrictToEventNrs.contains(row.getKey())) {
          filtered.add(row);
        }
      }
      return filtered;
    }
    return rows;
  }

  public void setRestrictToEventNrs(List<Long> restrictToEventNrs) {
    this.restrictToEventNrs = restrictToEventNrs;
  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = super.hashCode();
    result = prime * result + ((restrictToEventNrs == null) ? 0 : restrictToEventNrs.hashCode());
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
    EventLookupCall other = (EventLookupCall) obj;
    if (restrictToEventNrs == null) {
      if (other.restrictToEventNrs != null) {
        return false;
      }
    }
    else if (!restrictToEventNrs.equals(other.restrictToEventNrs)) {
      return false;
    }
    return true;
  }

}
