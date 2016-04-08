package com.rtiming.shared.entry;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.rtiming.shared.settings.fee.FeeCalculator;

public class RegistrationRowData implements Serializable {

  private static final long serialVersionUID = 1L;

  private Long registrationNr;
  private String registrationNo;
  private String runnerName;
  private String clubs;
  private String events;
  private final List<Entry> entries;
  private Date evtRegistration;

  public RegistrationRowData() {
    entries = new ArrayList<>();
  }

  public Long getRegistrationNr() {
    return registrationNr;
  }

  public void setRegistrationNr(Long registrationNr) {
    this.registrationNr = registrationNr;
  }

  public String getRegistrationNo() {
    return registrationNo;
  }

  public void setRegistrationNo(String registrationNo) {
    this.registrationNo = registrationNo;
  }

  public String getRunnerName() {
    return runnerName;
  }

  public void setRunnerName(String runnerName) {
    this.runnerName = runnerName;
  }

  public String getClubs() {
    return clubs;
  }

  public void setClubs(String clubs) {
    this.clubs = clubs;
  }

  public String getEvents() {
    return events;
  }

  public void setEvents(String events) {
    this.events = events;
  }

  public List<Entry> getEntries() {
    return entries;
  }

  public Date getEvtRegistration() {
    return evtRegistration;
  }

  public void setEvtRegistration(Date evtRegistration) {
    this.evtRegistration = evtRegistration;
  }

  public class Entry {

    private final Long entryNr;
    private final Date evtEntry;
    private final Long currencyUid;
    private final Set<FeeCalculator.RaceInput> races;

    public Entry(Long entryNr, Date evtEntry, Long currencyUid) {
      super();
      this.entryNr = entryNr;
      this.evtEntry = evtEntry;
      this.currencyUid = currencyUid;
      races = new HashSet<FeeCalculator.RaceInput>();
    }

    public Date getEvtEntry() {
      return evtEntry;
    }

    public Long getEntryNr() {
      return entryNr;
    }

    public Long getCurrencyUid() {
      return currencyUid;
    }

    public Set<FeeCalculator.RaceInput> getRaces() {
      return races;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + getOuterType().hashCode();
      result = prime * result + ((entryNr == null) ? 0 : entryNr.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof Entry)) {
        return false;
      }
      Entry other = (Entry) obj;
      if (!getOuterType().equals(other.getOuterType())) {
        return false;
      }
      if (entryNr == null) {
        if (other.entryNr != null) {
          return false;
        }
      }
      else if (!entryNr.equals(other.entryNr)) {
        return false;
      }
      return true;
    }

    private RegistrationRowData getOuterType() {
      return RegistrationRowData.this;
    }

  }

  @Override
  public int hashCode() {
    final int prime = 31;
    int result = 1;
    result = prime * result + ((registrationNr == null) ? 0 : registrationNr.hashCode());
    return result;
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) {
      return true;
    }
    if (obj == null) {
      return false;
    }
    if (!(obj instanceof RegistrationRowData)) {
      return false;
    }
    RegistrationRowData other = (RegistrationRowData) obj;
    if (registrationNr == null) {
      if (other.registrationNr != null) {
        return false;
      }
    }
    else if (!registrationNr.equals(other.registrationNr)) {
      return false;
    }
    return true;
  }

}
