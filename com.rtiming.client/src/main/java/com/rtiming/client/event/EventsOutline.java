package com.rtiming.client.event;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.AbstractOutline;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.AbstractIcons;

import com.rtiming.client.ClientSession;
import com.rtiming.client.club.ClubsTablePage;
import com.rtiming.client.ecard.ECardsTablePage;
import com.rtiming.client.map.MapsTablePage;
import com.rtiming.client.ranking.RankingsTablePage;
import com.rtiming.client.runner.RunnersTablePage;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.EventsOutlinePermission;

public class EventsOutline extends AbstractOutline {

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Star;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Events");
  }

  @Override
  protected void execInitTree() throws ProcessingException {
    setVisiblePermission(new EventsOutlinePermission());
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new EventsTablePage(ClientSession.get().getSessionClientNr()));
    pageList.add(new ECardsTablePage());
    pageList.add(new RunnersTablePage());
    pageList.add(new ClubsTablePage());
    pageList.add(new MapsTablePage());
    pageList.add(new RankingsTablePage());
  }

}
