package com.rtiming.server.settings.addinfo;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.entry.SharedCacheServerUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractCodeBoxData;
import com.rtiming.shared.common.security.permission.CreateAdditionalInformationAdministrationPermission;
import com.rtiming.shared.common.security.permission.DeleteAdditionalInformationAdministrationPermission;
import com.rtiming.shared.common.security.permission.ReadAdditionalInformationAdministrationPermission;
import com.rtiming.shared.common.security.permission.UpdateAdditionalInformationAdministrationPermission;
import com.rtiming.shared.dao.RtAdditionalInformationDef;
import com.rtiming.shared.dao.RtAdditionalInformationDefKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;

public class AdditionalInformationAdministrationProcessService implements IAdditionalInformationAdministrationProcessService {

  @Override
  public AdditionalInformationAdministrationFormData prepareCreate(AdditionalInformationAdministrationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateAdditionalInformationAdministrationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getCodeBox().getCodeTypeUid().setValue(AdditionalInformationCodeType.ID);
    BEANS.get(ICodeProcessService.class).prepareCreateCodeBox(formData.getCodeBox());

    return formData;
  }

  @Override
  public AdditionalInformationAdministrationFormData create(AdditionalInformationAdministrationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateAdditionalInformationAdministrationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getCodeBox().getCodeTypeUid().setValue(AdditionalInformationCodeType.ID);
    AbstractCodeBoxData codeBox = BEANS.get(ICodeProcessService.class).createCodeBox(formData.getCodeBox());

    // create column only for top level values, not for smartfield childs
    if (formData.getSmartfield().getValue() == null) {
      // create column because it does not yet exist
      AdditionalInformationDatabaseUtility.createColumn(formData.getEntity().getValue(), formData.getType().getValue(), formData.getCodeBox().getShortcut().getValue());
    }

    formData.setAdditionalInformationUid(codeBox.getCodeUid().getValue());
    formData = store(formData);

    return formData;
  }

  @Override
  public AdditionalInformationAdministrationFormData load(AdditionalInformationAdministrationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadAdditionalInformationAdministrationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtAdditionalInformationDefKey key = RtAdditionalInformationDefKey.create(formData.getAdditionalInformationUid());
    RtAdditionalInformationDef def = JPA.find(RtAdditionalInformationDef.class, key);
    if (def != null) {
      formData.getEntity().setValue(def.getEntityUid());
      formData.getMinimum().setValue(def.getValueMin());
      formData.getMaximum().setValue(def.getValueMax());
      formData.getType().setValue(def.getTypeUid());
      formData.getFeeGroup().setValue(def.getFeeGroupNr());
      formData.getDefaultValueInteger().setValue(def.getDefaultLong());
      formData.getDefaultValueBoolean().setValue(NumberUtility.nvl(def.getDefaultLong(), 0) != 0);
      formData.getDefaultValueSmartfield().setValue(def.getDefaultLong());
      formData.getDefaultValueDecimal().setValue(def.getDefaultDecimal());
      formData.getDefaultValueText().setValue(def.getDefaultText());
      formData.getMandatory().setValue(def.getMandatory());
    }

    formData.getCodeBox().getCodeUid().setValue(formData.getAdditionalInformationUid());
    BEANS.get(ICodeProcessService.class).loadCodeBox(formData.getCodeBox());

    return formData;
  }

  @Override
  public AdditionalInformationAdministrationFormData store(AdditionalInformationAdministrationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateAdditionalInformationAdministrationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // check if column needs to be renamed
    if (formData.getEntity().getValue() != null) {
      RtUc oldDef = JPA.find(RtUc.class, RtUcKey.create(formData.getAdditionalInformationUid()));
      if (!StringUtility.equalsIgnoreCase(formData.getCodeBox().getShortcut().getValue(), oldDef.getShortcut())) {
        // rename column
        AdditionalInformationDatabaseUtility.renameColumn(formData.getEntity().getValue(), formData.getCodeBox().getShortcut().getValue(), oldDef.getShortcut());
      }
    }

    Long value = null;
    if (CompareUtility.equals(formData.getType().getValue(), AdditionalInformationTypeCodeType.BooleanCode.ID)) {
      boolean bool = BooleanUtility.nvl(formData.getDefaultValueBoolean().getValue());
      value = (bool ? 1L : 0L);
    }
    else if (CompareUtility.equals(formData.getType().getValue(), AdditionalInformationTypeCodeType.SmartfieldCode.ID)) {
      value = formData.getDefaultValueSmartfield().getValue();
    }
    else {
      value = formData.getDefaultValueInteger().getValue();
    }

    RtAdditionalInformationDefKey key = new RtAdditionalInformationDefKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setId(formData.getAdditionalInformationUid());
    RtAdditionalInformationDef def = new RtAdditionalInformationDef();
    def.setId(key);
    def.setEntityUid(formData.getEntity().getValue());
    def.setValueMin(NumberUtility.toDouble(formData.getMinimum().getValue()));
    def.setValueMax(NumberUtility.toDouble(formData.getMaximum().getValue()));
    def.setTypeUid(formData.getType().getValue());
    def.setFeeGroupNr(formData.getFeeGroup().getValue());
    def.setDefaultLong(value);
    def.setDefaultDecimal(NumberUtility.toDouble(formData.getDefaultValueDecimal().getValue()));
    def.setDefaultText(formData.getDefaultValueText().getValue());
    def.setMandatory(BooleanUtility.nvl(formData.getMandatory().getValue()));
    def.setParentUid(formData.getSmartfield().getValue());
    JPA.merge(def);

    BEANS.get(ICodeProcessService.class).storeCodeBox(formData.getCodeBox());

    SharedCacheServerUtility.notifyClients();
    return formData;
  }

  @Override
  public void delete(AdditionalInformationAdministrationFormData formData) throws ProcessingException {
    if (!ACCESS.check(new DeleteAdditionalInformationAdministrationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null || formData.getAdditionalInformationUid() == null) {
      return;
    }

    formData = load(formData);

    // delete child
    String queryString = "DELETE FROM RtAdditionalInformationDef " + "WHERE parentUid = :additionalInformationUid " + "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("additionalInformationUid", formData.getAdditionalInformationUid());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();

    // delete parent
    queryString = "DELETE FROM RtAdditionalInformationDef " + "WHERE id.additionalInformationUid = :additionalInformationUid " + "AND id.clientNr = :sessionClientNr ";
    query = JPA.createQuery(queryString);
    query.setParameter("additionalInformationUid", formData.getAdditionalInformationUid());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();

    BEANS.get(ICodeProcessService.class).deleteCodeBox(formData.getCodeBox());

    // drop column
    if (formData.getEntity().getValue() != null) {
      AdditionalInformationDatabaseUtility.dropColumn(formData.getEntity().getValue(), formData.getCodeBox().getShortcut().getValue());
    }

    SharedCacheServerUtility.notifyClients();
  }
}
