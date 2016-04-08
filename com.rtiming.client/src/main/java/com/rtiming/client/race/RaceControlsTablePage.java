package com.rtiming.client.race;

import java.util.List;

import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.ClientSession;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.result.AbstractRaceControlTable;
import com.rtiming.shared.Texts;
import com.rtiming.shared.event.IEventsOutlineService;
import com.rtiming.shared.event.RaceControlRowData;

public class RaceControlsTablePage extends AbstractPageWithTable<RaceControlsTablePage.Table> implements IHelpEnabledPage {

  private final Long[] raceNr;
  private final Long clientNr;

  public RaceControlsTablePage(Long clientNr, Long... raceNr) {
    super();
    this.raceNr = raceNr;
    this.clientNr = clientNr;
  }

  public RaceControlsTablePage(Long raceNr) {
    super();
    this.raceNr = new Long[]{raceNr};
    this.clientNr = ClientSession.get().getSessionClientNr();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("RaceControl");
  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    List<RaceControlRowData> list = BEANS.get(IEventsOutlineService.class).getRaceControlTableData(clientNr, getRaceNr());
    Object[][] table = getTable().list2data(list);
    importTableData(table);
  }

  @FormData
  public Long[] getRaceNr() {
    return raceNr;
  }

  @Order(10.0)
  public class Table extends AbstractRaceControlTable {

    @Override
    public boolean getConfiguredIsDeleteAllowed() {
      return true;
    }

    @Override
    public void reloadTable() throws ProcessingException {
      reloadPage();
    }

    @Override
    protected Long[] getRaceNrs() {
      return raceNr;
    }

  }
}
