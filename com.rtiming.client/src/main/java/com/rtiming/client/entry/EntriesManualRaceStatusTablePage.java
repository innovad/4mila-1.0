package com.rtiming.client.entry;

import com.rtiming.shared.entry.EntryList;

/**
 * 
 */
public class EntriesManualRaceStatusTablePage extends AbstractEntriesTablePage {

  public EntriesManualRaceStatusTablePage(Long eventNr, Long clientNr, Long registrationNr, Long classUid, Long courseNr, Long clubNr) {
    super(EntryList.MANUAL_RACE_STATUS, eventNr, clientNr, registrationNr, classUid, courseNr, clubNr);
  }

}
