package com.rtiming.client.entry.startlist;

import java.util.Date;

import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;

import com.rtiming.shared.Texts;

public abstract class AbstractStartlistPreviewColumn extends AbstractStringColumn {

  private Date date;

  public void setDate(Date date) {
    this.date = date;
  }

  public Date getDate() {
    return date;
  }

  @Override
  protected String getConfiguredHeaderText() {
    return Texts.get("Preview");
  }

  @Override
  protected int getConfiguredWidth() {
    return 65;
  }

  @Override
  protected boolean getConfiguredTextWrap() {
    return true;
  }

}
