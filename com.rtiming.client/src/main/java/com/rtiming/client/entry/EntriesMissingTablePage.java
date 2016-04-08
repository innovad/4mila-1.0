package com.rtiming.client.entry;

import com.rtiming.shared.entry.EntryList;

/**
 * 
 */
public class EntriesMissingTablePage extends AbstractEntriesTablePage {

  public EntriesMissingTablePage(Long eventNr, Long clientNr, Long registrationNr, Long classUid, Long courseNr, Long clubNr) {
    super(EntryList.MISSING, eventNr, clientNr, registrationNr, classUid, courseNr, clubNr);
  }

}
