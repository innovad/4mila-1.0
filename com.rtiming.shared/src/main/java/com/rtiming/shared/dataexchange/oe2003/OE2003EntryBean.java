package com.rtiming.shared.dataexchange.oe2003;

import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.IDataExchangeService;
import com.rtiming.shared.dataexchange.IProgressMonitor;
import com.rtiming.shared.dataexchange.ImportMessageList;

public class OE2003EntryBean extends AbstractOE2003Bean {

  // Stnr
  // SIKarte
  // DatenbankId
  // NName
  // VName
  // Jg
  // Geschlecht
  // Block
  // AK
  // Start
  // Ziel
  // Zeit
  // Wertung
  // ClubNr
  // Abk
  // Ort
  // Nat
  // Katnr
  // Kurz
  // Lang
  // Num1
  // Num2
  // Num3
  // Text1
  // Text2
  // Text3
  // AdrName
  // Stra�e
  // Zeile2
  // PLZ
  // Wohnort
  // Tel
  // Fax
  // EMail
  // Id/Verein
  // Gemietet
  // Startgeld
  // Bezahlt
  // ID
  // OeV
  // Bezeichnung
  // Name
  // Bemerkung

  private static final long serialVersionUID = 1L;
  private final Long eventNr;

  public OE2003EntryBean(Long primaryKeyNr, Long eventNr) {
    super(primaryKeyNr);
    this.eventNr = eventNr;
  }

  @Override
  public void loadBeanFromDatabase() throws ProcessingException {
  }

  @Override
  public void storeBeanToDatabase(IProgressMonitor monitor) throws ProcessingException {
  }

  @Override
  public void storeBeansToDatabase(List<AbstractDataBean> batch, IProgressMonitor monitor) throws ProcessingException {
    ImportMessageList result = BEANS.get(IDataExchangeService.class).storeOE2003Entry(batch, eventNr);
    monitor.addErrors(result);
  }

}
