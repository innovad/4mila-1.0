package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.club.ClubForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.shared.club.IClubProcessService;
import com.rtiming.shared.dao.RtClubKey;

public class ClubTestDataProvider extends AbstractTestDataProvider<ClubForm> {

  public ClubTestDataProvider() throws ProcessingException {
    callInitializer();
  }

  @Override
  protected ClubForm createForm() throws ProcessingException {
    ClubForm club = new ClubForm();
    club.startNew();
    FormTestUtility.fillFormFields(club);
    club.doOk();
    return club;
  }

  @Override
  public void remove() throws ProcessingException {
    BEANS.get(IClubProcessService.class).delete(RtClubKey.create(getForm().getClubNr()));
  }

  public Long getClubNr() throws ProcessingException {
    return getForm().getClubNr();
  }

}
