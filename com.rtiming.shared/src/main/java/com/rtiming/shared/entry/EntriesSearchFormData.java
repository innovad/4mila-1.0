package com.rtiming.shared.entry;

import java.util.Date;

import org.eclipse.scout.rt.shared.data.form.AbstractFormData;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;

import com.rtiming.shared.club.AbstractClubSearchBoxData;
import com.rtiming.shared.ecard.AbstractECardSearchBoxData;
import com.rtiming.shared.runner.AbstractRunnerDetailsSearchBoxData;
import com.rtiming.shared.runner.AbstractRunnerSearchBoxData;
import com.rtiming.shared.settings.addinfo.AbstractAdditionalInformationSearchBoxData;
import com.rtiming.shared.settings.city.AbstractCitySearchBoxData;

public class EntriesSearchFormData extends AbstractFormData {
  private static final long serialVersionUID = 1L;

  public EntriesSearchFormData() {
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

  public Event getEvent() {
    return getFieldByClass(Event.class);
  }

  public RunnerBox getRunnerBox() {
    return getFieldByClass(RunnerBox.class);
  }

  public Start getStart() {
    return getFieldByClass(Start.class);
  }

  public StartTimeFrom getStartTimeFrom() {
    return getFieldByClass(StartTimeFrom.class);
  }

  public StartTimeGroup getStartTimeGroup() {
    return getFieldByClass(StartTimeGroup.class);
  }

  public StartTimeTo getStartTimeTo() {
    return getFieldByClass(StartTimeTo.class);
  }

  public Startblocks getStartblocks() {
    return getFieldByClass(Startblocks.class);
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

  public static class Event extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Event() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class RunnerBox extends AbstractRunnerSearchBoxData {
    private static final long serialVersionUID = 1L;

    public RunnerBox() {
    }
  }

  public static class Start extends AbstractValueFieldData<Long> {
    private static final long serialVersionUID = 1L;

    public Start() {
    }

    /**
     * list of derived validation rules.
     */
     
  }

  public static class StartTimeFrom extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public StartTimeFrom() {
    }
  }

  public static class StartTimeGroup extends AbstractValueFieldData<Boolean> {
    private static final long serialVersionUID = 1L;

    public StartTimeGroup() {
    }
  }

  public static class StartTimeTo extends AbstractValueFieldData<Date> {
    private static final long serialVersionUID = 1L;

    public StartTimeTo() {
    }
  }

  public static class Startblocks extends AbstractValueFieldData<Long[]> {
    private static final long serialVersionUID = 1L;

    public Startblocks() {
    }

    /**
     * list of derived validation rules.
     */
     
  }
}
