package com.rtiming.shared.club;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationSearchBoxData;

public class ClubsSearchFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public ClubsSearchFormData() {
  }

  public AdditionalInformationBox getAdditionalInformationBox() {
    return getFieldByClass(AdditionalInformationBox.class);
  }

  public ClubBox getClubBox() {
    return getFieldByClass(ClubBox.class);
  }

  public static class AdditionalInformationBox extends AbstractAdditionalInformationSearchBoxData {
    private static final long serialVersionUID = 1L;

    public AdditionalInformationBox() {
    }
  }

  public static class ClubBox extends AbstractClubSearchBoxData {
    private static final long serialVersionUID = 1L;

    public ClubBox() {
    }
  }
}
