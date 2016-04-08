package com.rtiming.client.event.course;

import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithNodes;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.event.course.variant.CourseVariantsTablePage;

public class CourseNodePage extends AbstractPageWithNodes {

  private final Long courseNr;

  public CourseNodePage(Long courseNr) {
    super();
    this.courseNr = courseNr;
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Course");
  }

  @Override
  protected void execCreateChildPages(List<IPage<?>> pageList) throws ProcessingException {
    pageList.add(new CourseControlsTablePage(courseNr, null));
    pageList.add(new CourseVariantsTablePage(courseNr));
  }
}
