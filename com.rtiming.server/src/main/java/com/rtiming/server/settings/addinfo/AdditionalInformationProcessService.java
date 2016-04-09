package com.rtiming.server.settings.addinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.TypeCastUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.AdditionalInformationBean;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.common.security.permission.CreateAdditionalInformationPermission;
import com.rtiming.shared.common.security.permission.ReadAdditionalInformationPermission;
import com.rtiming.shared.common.security.permission.UpdateAdditionalInformationPermission;
import com.rtiming.shared.entry.SharedCache;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationProcessService;

public class AdditionalInformationProcessService  implements IAdditionalInformationProcessService {

  @Override
  public AdditionalInformationBean prepareCreate(AdditionalInformationBean bean) throws ProcessingException {
    if (!ACCESS.check(new CreateAdditionalInformationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (bean != null) {
      bean.getValues().clear();
      bean.getValues().addAll(SharedCache.getAddInfoForEntity(bean.getEntityUid(), NumberUtility.nvl(bean.getClientNr(), ServerSession.get().getSessionClientNr())));
    }
    return bean;
  }

  @Override
  public Map<Long, List<AdditionalInformationValueBean>> loadAddInfoForEntity(Long clientNr) {
    Map<Long, List<AdditionalInformationValueBean>> map = new HashMap<>();
    Long[] entites = new Long[]{EntityCodeType.ClubCode.ID, EntityCodeType.RunnerCode.ID, EntityCodeType.EntryCode.ID};
    for (Long entityUid : entites) {
      map.put(entityUid, BEANS.get(AdditionalInformationProcessService.class).loadAdditionalInfoDefaults(entityUid, clientNr));
    }
    return map;
  }

  private List<AdditionalInformationValueBean> loadAdditionalInfoDefaults(Long entityUid, Long clientNr) {
    List<AdditionalInformationValueBean> result = new ArrayList<>();
    // RT_ADDITIONAL_INFORMATION
    String queryString = "SELECT " +
        "AD.id.additionalInformationUid, " +
        "AD.typeUid, " +
        "AD.valueMin, " +
        "AD.valueMax, " +
        "AD.defaultInteger, " +
        "AD.defaultDecimal, " +
        "AD.defaultText, " +
        "AD.mandatory, " +
        "AD.feeGroupNr " +
        "FROM RtAdditionalInformationDef AD, RtUc UC " +
        "WHERE UC.id.ucUid = AD.id.additionalInformationUid " +
        "AND UC.id.clientNr = AD.id.clientNr " +
        "AND AD.parentUid IS NULL " + // no smartfield type childs
        "AND AD.entityUid = :entityUid " +
        "AND AD.id.clientNr = :clientNr ";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("entityUid", entityUid);
    query.setParameter("clientNr", NumberUtility.nvl(clientNr, ServerSession.get().getSessionClientNr()));
    List list = query.getResultList();

    for (Object rowObject : list) {
      Object[] row = (Object[]) rowObject;
      AdditionalInformationValueBean value = new AdditionalInformationValueBean();
      value.setAdditionalInformationUid(TypeCastUtility.castValue(row[0], Long.class));
      value.setTypeUid(TypeCastUtility.castValue(row[1], Long.class));
      value.setValueMin(TypeCastUtility.castValue(row[2], Double.class));
      value.setValueMax(TypeCastUtility.castValue(row[3], Double.class));
      value.setDefaultInteger(TypeCastUtility.castValue(row[4], Long.class));
      value.setDefaultDecimal(TypeCastUtility.castValue(row[5], Double.class));
      value.setDefaultText(TypeCastUtility.castValue(row[6], String.class));
      value.setMandatory(TypeCastUtility.castValue(row[7], boolean.class));
      value.setFeeGroupNr(TypeCastUtility.castValue(row[8], Long.class));
      result.add(value);
    }

    // apply default values
    for (AdditionalInformationValueBean value : result) {
      if (CompareUtility.equals(value.getTypeUid(), AdditionalInformationTypeCodeType.DoubleCode.ID)) {
        value.setValueDouble(value.getDefaultDecimal());
      }
      else if (CompareUtility.equals(value.getTypeUid(), AdditionalInformationTypeCodeType.TextCode.ID)) {
        value.setValueString(value.getDefaultText());
      }
      else {
        value.setValueInteger(value.getDefaultInteger());
      }
    }
    return result;
  }

  @Override
  public AdditionalInformationBean create(AdditionalInformationBean formData) throws ProcessingException {
    if (!ACCESS.check(new CreateAdditionalInformationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public AdditionalInformationBean load(AdditionalInformationBean bean) throws ProcessingException {
    if (!ACCESS.check(new ReadAdditionalInformationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    bean.getValues().clear();
    List<AdditionalInformationValueBean> requiredValues = SharedCache.getAddInfoForEntity(bean.getEntityUid(), NumberUtility.nvl(bean.getClientNr(), ServerSession.get().getSessionClientNr()));
    List<AdditionalInformationValueBean> list = AdditionalInformationDatabaseUtility.selectValue(bean.getEntityUid(), Arrays.asList(new Long[]{bean.getJoinNr()}), bean.getClientNr(), requiredValues);
    bean.getValues().addAll(list);

    return bean;
  }

  @Override
  public AdditionalInformationBean store(AdditionalInformationBean bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateAdditionalInformationPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    for (AdditionalInformationValueBean value : bean.getValues()) {
      AdditionalInformationDatabaseUtility.updateValue(bean.getEntityUid(), bean.getJoinNr(), bean.getClientNr(), value);
    }

    return bean;
  }

}
