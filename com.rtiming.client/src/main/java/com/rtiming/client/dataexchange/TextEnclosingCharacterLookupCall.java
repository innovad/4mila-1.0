package com.rtiming.client.dataexchange;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

public class TextEnclosingCharacterLookupCall extends LocalLookupCall<Character> {

  private static final long serialVersionUID = 1L;

  @Override
  protected List<LookupRow<Character>> execCreateLookupRows() throws ProcessingException {
    ArrayList<LookupRow<Character>> rows = new ArrayList<LookupRow<Character>>();

    rows.add(new LookupRow('"', "\""));

    return rows;
  }
}
