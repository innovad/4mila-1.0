package com.rtiming.shared.ranking;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.dao.RtRankingEventKey;

public class RankingEventFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public RankingEventFormData() {
  }

  public KeyProperty getKeyProperty() {
    return getPropertyByClass(KeyProperty.class);
  }

  /**
   * access method for property Key.
   */
  public RtRankingEventKey getKey() {
    return getKeyProperty().getValue();
  }

  /**
   * access method for property Key.
   */
  public void setKey(RtRankingEventKey key) {
    getKeyProperty().setValue(key);
  }

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public RankingBox getRankingBox() {
    return getFieldByClass(RankingBox.class);
  }

  public SortCode getSortCode() {
    return getFieldByClass(SortCode.class);
  }

  public class KeyProperty extends AbstractPropertyData<RtRankingEventKey> {
    private static final long serialVersionUID = 1L;

    public KeyProperty() {
    }
  }

  public static class Event extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Event() {
    }

    /**
     * list of derived validation rules.
     */
 
  }

  public static class RankingBox extends AbstractRankingBoxData {
    private static final long serialVersionUID = 1L;

    public RankingBox() {
    }
  }

  public static class SortCode extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public SortCode() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
