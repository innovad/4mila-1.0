package com.rtiming.shared.dataexchange.cache;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.BEANS;

import com.rtiming.shared.club.ClubFormData;
import com.rtiming.shared.club.IClubProcessService;
import com.rtiming.shared.common.database.sql.BeanUtility;

public class ClubDataCacher extends AbstractImportDataCacher<ClubFormData, String> {

  @Override
  protected Long getPrimaryKey(ClubFormData formData) {
    return formData.getClubNr();
  }

  @Override
  protected ClubFormData store(ClubFormData formData) throws ProcessingException {
    return BeanUtility.clubBean2formData(BEANS.get(IClubProcessService.class).store(BeanUtility.clubFormData2bean(formData)));
  }

  @Override
  protected ClubFormData create(ClubFormData formData) throws ProcessingException {
    return BeanUtility.clubBean2formData(BEANS.get(IClubProcessService.class).create(BeanUtility.clubFormData2bean(formData)));
  }

  @Override
  protected ClubFormData find(String string) throws ProcessingException {
    return BeanUtility.clubBean2formData(BEANS.get(IClubProcessService.class).findClub(string));
  }

  @Override
  protected void createNewData(ClubFormData formData, String value) {
    formData.getName().setValue(value);
  }

}
