package com.rtiming.shared.ranking;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.eclipse.scout.rt.shared.data.form.properties.AbstractPropertyData;

import com.rtiming.shared.dao.RtRankingKey;

public class RankingFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public RankingFormData() {
  }

  public KeyProperty getKeyProperty() {
    return getPropertyByClass(KeyProperty.class);
  }

  /**
   * access method for property Key.
   */
  public RtRankingKey getKey() {
    return getKeyProperty().getValue();
  }

  /**
   * access method for property Key.
   */
  public void setKey(RtRankingKey key) {
    getKeyProperty().setValue(key);
  }

  public Name getName() {
    return getFieldByClass(Name.class);
  }

  public RankingBox getRankingBox() {
    return getFieldByClass(RankingBox.class);
  }

  public class KeyProperty extends AbstractPropertyData<RtRankingKey> {
    private static final long serialVersionUID = 1L;

    public KeyProperty() {
    }
  }

  public static class Name extends AbstractValueFieldData<String> {
    private static final long serialVersionUID = 1L;

    public Name() {
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
}
