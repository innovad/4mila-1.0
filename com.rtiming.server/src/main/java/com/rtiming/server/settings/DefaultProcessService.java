package com.rtiming.server.settings;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateDefaultPermission;
import com.rtiming.shared.common.security.permission.ReadDefaultPermission;
import com.rtiming.shared.common.security.permission.UpdateDefaultPermission;
import com.rtiming.shared.dao.RtDefault;
import com.rtiming.shared.dao.RtDefaultKey;
import com.rtiming.shared.settings.DefaultCodeType;
import com.rtiming.shared.settings.DefaultFormData;
import com.rtiming.shared.settings.IDefaultProcessService;

public class DefaultProcessService  implements IDefaultProcessService {

  @Override
  public DefaultFormData prepareCreate(DefaultFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateDefaultPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    return formData;
  }

  @Override
  public DefaultFormData create(DefaultFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateDefaultPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData = store(formData);
    return formData;
  }

  @Override
  public DefaultFormData load(DefaultFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadDefaultPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtDefault def = JPA.find(RtDefault.class, RtDefaultKey.create(formData.getDefaultUid().getValue()));
    if (def != null) {
      formData.getValueInteger().setValue(def.getValueLong());
      formData.getValueString().setValue(def.getValueString());
    }

    return formData;
  }

  @Override
  public DefaultFormData store(DefaultFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateDefaultPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtDefaultKey key = new RtDefaultKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setId(formData.getDefaultUid().getValue());
    RtDefault def = new RtDefault();
    def.setId(key);
    def.setValueLong(formData.getValueInteger().getValue());
    def.setValueString(formData.getValueString().getValue());
    JPA.merge(def);

    return formData;
  }

  @Override
  public Long getDefaultEventNr() throws ProcessingException {
    Long eventNr = getLongValue(DefaultCodeType.DefaultEventCode.ID);
    if (eventNr != null) {
      // double check this event really exists
      String queryString = "SELECT COUNT(id.eventNr) " +
          "FROM RtEvent " +
          "WHERE id.eventNr = :eventNr " +
          "AND id.clientNr = :sessionClientNr ";

      FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
      query.setParameter("eventNr", eventNr);
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      Long count = query.getSingleResult();

      if (count <= 0) {
        return null;
      }
    }
    return eventNr;
  }

  @Override
  public Long getDefaultCurrencyUid() throws ProcessingException {
    return getLongValue(DefaultCodeType.DefaultCurrencyCode.ID);
  }

  @Override
  public Long getDefaultCountryUid() throws ProcessingException {
    return getLongValue(DefaultCodeType.DefaultCountryCode.ID);
  }

  @Override
  public Long getBackupInterval() throws ProcessingException {
    return getLongValue(DefaultCodeType.BackupIntervalCode.ID);
  }

  @Override
  public void setDefaultEventNr(Long eventNr) throws ProcessingException {
    setLongValue(DefaultCodeType.DefaultEventCode.ID, eventNr);
  }

  @Override
  public void setDefaultCurrencyUid(Long currencyUid) throws ProcessingException {
    setLongValue(DefaultCodeType.DefaultCurrencyCode.ID, currencyUid);
  }

  @Override
  public void setDefaultCountryUid(Long countryUid) throws ProcessingException {
    setLongValue(DefaultCodeType.DefaultCountryCode.ID, countryUid);
  }

  @Override
  public void setBackupInterval(Long backupInterval) throws ProcessingException {
    setLongValue(DefaultCodeType.BackupIntervalCode.ID, backupInterval);
  }

  @Override
  public String getBackupDirectory() throws ProcessingException {
    return getStringValue(DefaultCodeType.BackupDirectoryCode.ID);
  }

  @Override
  public void setBackupDirectory(String backupDirectory) throws ProcessingException {
    setStringValue(DefaultCodeType.BackupDirectoryCode.ID, backupDirectory);
  }

  @Override
  public Long getBackupMaxNumber() throws ProcessingException {
    return NumberUtility.nvl(getLongValue(DefaultCodeType.BackupMaxNumberCode.ID), 50L);
  }

  @Override
  public void setBackupMaxNumber(Long backupMaxNumber) throws ProcessingException {
    setLongValue(DefaultCodeType.BackupMaxNumberCode.ID, backupMaxNumber);
  }

  private Long getLongValue(long uid) throws ProcessingException {
    DefaultFormData d = new DefaultFormData();
    d.getDefaultUid().setValue(uid);
    d = load(d);
    return d.getValueInteger().getValue();
  }

  private void setLongValue(long uid, Long longValue) throws ProcessingException {
    DefaultFormData defaultFormData = new DefaultFormData();
    defaultFormData.getDefaultUid().setValue(uid);
    defaultFormData.getValueInteger().setValue(longValue);
    create(defaultFormData);
  }

  private String getStringValue(long uid) throws ProcessingException {
    DefaultFormData d = new DefaultFormData();
    d.getDefaultUid().setValue(uid);
    d = load(d);
    return d.getValueString().getValue();
  }

  private void setStringValue(long uid, String stringValue) throws ProcessingException {
    DefaultFormData defaultFormData = new DefaultFormData();
    defaultFormData.getDefaultUid().setValue(uid);
    defaultFormData.getValueString().setValue(stringValue);
    create(defaultFormData);
  }

}
