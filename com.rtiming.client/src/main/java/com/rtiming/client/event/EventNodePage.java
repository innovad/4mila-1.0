package com.rtiming.client.event;

import java.util.List;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.AbstractIcons;

import com.rtiming.client.entry.startlist.StartListNodePage;
import com.rtiming.client.event.course.ControlsTablePage;
import com.rtiming.client.event.course.CoursesTablePage;
import com.rtiming.client.event.course.EventClassesTablePage;
import com.rtiming.client.map.MapsTablePage;
import com.rtiming.client.settings.addinfo.EventAdditionalInformationTablePage;
import com.rtiming.shared.Texts;

public class EventNodePage extends AbstractPageWithNodes {

  private Long m_eventNr;

  public EventNodePage(Long eventNr) {
    super();
    m_eventNr = eventNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Event");
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new StartListNodePage(getEventNr()));
    pageList.add(new EventAdditionalInformationTablePage(getEventNr()));
    pageList.add(new EventClassesTablePage(getEventNr(), null));
    pageList.add(new CoursesTablePage(getEventNr()));
    pageList.add(new ControlsTablePage(getEventNr()));
    pageList.add(new MapsTablePage(getEventNr()));
  }

  @FormData
  public Long getEventNr() {
    return m_eventNr;
  }

  @FormData
  public void setEventNr(Long eventNr) {
    m_eventNr = eventNr;
  }
}
