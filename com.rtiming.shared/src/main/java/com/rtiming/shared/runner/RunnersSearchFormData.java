package com.rtiming.shared.runner;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;

import com.rtiming.shared.club.AbstractClubSearchBoxData;
import com.rtiming.shared.ecard.AbstractECardSearchBoxData;
import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationSearchBoxData;
import com.rtiming.shared.settings.city.AbstractCitySearchBoxData;

public class RunnersSearchFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public RunnersSearchFormData() {
  }

  public AdditionalInformationBox getAdditionalInformationBox() {
    return getFieldByClass(AdditionalInformationBox.class);
  }

  public CityBox getCityBox() {
    return getFieldByClass(CityBox.class);
  }

  public ClubBox getClubBox() {
    return getFieldByClass(ClubBox.class);
  }

  public DetailsBox getDetailsBox() {
    return getFieldByClass(DetailsBox.class);
  }

  public ECardBox getECardBox() {
    return getFieldByClass(ECardBox.class);
  }

  public RunnerBox getRunnerBox() {
    return getFieldByClass(RunnerBox.class);
  }

  public static class AdditionalInformationBox extends AbstractAdditionalInformationSearchBoxData {
    private static final long serialVersionUID = 1L;

    public AdditionalInformationBox() {
    }
  }

  public static class CityBox extends AbstractCitySearchBoxData {
    private static final long serialVersionUID = 1L;

    public CityBox() {
    }
  }

  public static class ClubBox extends AbstractClubSearchBoxData {
    private static final long serialVersionUID = 1L;

    public ClubBox() {
    }
  }

  public static class DetailsBox extends AbstractRunnerDetailsSearchBoxData {
    private static final long serialVersionUID = 1L;

    public DetailsBox() {
    }
  }

  public static class ECardBox extends AbstractECardSearchBoxData {
    private static final long serialVersionUID = 1L;

    public ECardBox() {
    }
  }

  public static class RunnerBox extends AbstractRunnerSearchBoxData {
    private static final long serialVersionUID = 1L;

    public RunnerBox() {
    }
  }
}
