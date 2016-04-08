package com.rtiming.client.entry;

import com.rtiming.shared.entry.EntryList;

/**
 * 
 */
public class EntriesTablePage extends AbstractEntriesTablePage {

  public EntriesTablePage(Long eventNr, Long clientNr, Long registrationNr, Long classUid, Long courseNr, Long clubNr) {
    super(EntryList.ALL, eventNr, clientNr, registrationNr, classUid, courseNr, clubNr);
  }

}
