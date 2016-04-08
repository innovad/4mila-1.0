package com.rtiming.client.entry;

import com.rtiming.shared.entry.EntryList;

/**
 * 
 */
public class EntriesFinishTimesStoredTablePage extends AbstractEntriesTablePage {

  public EntriesFinishTimesStoredTablePage(Long eventNr, Long clientNr, Long registrationNr, Long classUid, Long courseNr, Long clubNr) {
    super(EntryList.FINISH_TIMES_STORED, eventNr, clientNr, registrationNr, classUid, courseNr, clubNr);
  }

}
