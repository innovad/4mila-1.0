package com.rtiming.client.dataexchange;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.LocalLookupCall;
import org.eclipse.scout.rt.shared.services.lookup.LookupRow;

import com.rtiming.shared.Icons;

public class CharacterSetLookupCall extends LocalLookupCall<String> {

  private static final long serialVersionUID = 1L;

  @Override
  protected List<LookupRow<String>> execCreateLookupRows() throws ProcessingException {
    ArrayList<LookupRow<String>> rows = new ArrayList<LookupRow<String>>();

    Map<String, Charset> map = Charset.availableCharsets();
    Iterator it = map.keySet().iterator();
    while (it.hasNext()) {

      // Get charset name
      String charsetName = (String) it.next();
      Charset cs = map.get(charsetName);
      String displayName = cs.displayName();

      LookupRow<String> e = new LookupRow<>(charsetName, displayName, Icons.FILE);
      rows.add(e);

    }

    // make sure we have default
    if (!rows.contains(System.getProperty("file.encoding"))) {
      LookupRow<String> e = new LookupRow(System.getProperty("file.encoding"), System.getProperty("file.encoding"), Icons.FILE);
      rows.add(e);
    }

    return rows;
  }
}
