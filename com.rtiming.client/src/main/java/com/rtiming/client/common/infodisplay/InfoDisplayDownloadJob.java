package com.rtiming.client.common.infodisplay;

import java.util.List;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.RaceBean;
import com.rtiming.shared.race.IRaceProcessService;
import com.rtiming.shared.race.RaceStatusCodeType;
import com.rtiming.shared.results.IResultsOutlineService;
import com.rtiming.shared.results.ResultRowData;

/**
 * 
 */
public class InfoDisplayDownloadJob extends AbstractInfoDisplayUpdateJob {

  private final Long raceNr;

  public InfoDisplayDownloadJob(Long raceNr, IClientSession session) {
    super(session);
    this.raceNr = raceNr;
  }

  @Override
  protected String prepareURL() throws ProcessingException {
    String location = InfoDisplayUtility.getInfoDisplayHtmlUrl("infoDisplayDownload.html");

    if (raceNr != null) {
      RaceBean race = new RaceBean();
      race.setRaceNr(raceNr);
      race = BEANS.get(IRaceProcessService.class).load(race);

      Long classUid = race.getLegClassUid();
      if (classUid != null) {
        List<ResultRowData> results = BEANS.get(IResultsOutlineService.class).getResultTableData(ClientSession.get().getSessionClientNr(), classUid, null, null, null);
        ResultRowData row = null;
        for (ResultRowData r : results) {
          if (CompareUtility.equals(r.getRaceNr(), raceNr)) {
            row = r;
          }
        }

        if (row != null) {
          String rank = StringUtility.emptyIfNull(row.getRank());
          String name = row.getRunner();
          String time = row.getTime();
          String club = row.getClub();

          if (CompareUtility.equals(row.getRaceStatus(), RaceStatusCodeType.OkCode.ID)) {
            location = InfoDisplayUtility.addParameter(location, "rank", Texts.get("Rank") + " " + rank + "/" + results.size());
          }
          else {
            location = InfoDisplayUtility.addParameter(location, "rank", "");
          }
          location = InfoDisplayUtility.addParameter(location, "name", name);
          location = InfoDisplayUtility.addParameter(location, "time", time);
          location = InfoDisplayUtility.addParameter(location, "club", club);

          return location;
        }
      }
    }
    return InfoDisplayUtility.addParameter(location, "name", Texts.get("NoEntryFound"));
  }
}
