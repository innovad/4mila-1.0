package com.rtiming.server.settings.fee;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.BooleanUtility;
import org.eclipse.scout.commons.TypeCastUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateFeePermission;
import com.rtiming.shared.common.security.permission.ReadFeePermission;
import com.rtiming.shared.common.security.permission.UpdateFeePermission;
import com.rtiming.shared.dao.RtAdditionalInformationDef;
import com.rtiming.shared.dao.RtAdditionalInformationDefKey_;
import com.rtiming.shared.dao.RtAdditionalInformationDef_;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey_;
import com.rtiming.shared.dao.RtEventClass_;
import com.rtiming.shared.dao.RtFee;
import com.rtiming.shared.dao.RtFeeGroup;
import com.rtiming.shared.dao.RtFeeGroup_;
import com.rtiming.shared.dao.RtFeeKey;
import com.rtiming.shared.dao.RtFeeKey_;
import com.rtiming.shared.dao.RtFee_;
import com.rtiming.shared.settings.fee.FeeFormData;
import com.rtiming.shared.settings.fee.IFeeProcessService;

public class FeeProcessService  implements IFeeProcessService {

  @Override
  public FeeFormData prepareCreate(FeeFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateFeePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public FeeFormData create(FeeFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateFeePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    if (formData.getFeeNr() == null) {
      RtFeeKey key = RtFeeKey.create((Long) null);
      RtFee fee = new RtFee();
      fee.setId(key);
      JPA.persist(fee);

      formData.setFeeNr(fee.getId().getId());
    }
    formData = store(formData);

    return formData;
  }

  @Override
  public FeeFormData load(FeeFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadFeePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT F.fee, " +
        "F.currencyUid, " +
        "F.evtFrom, " +
        "F.evtTo, " +
        "F.ageFrom, " +
        "F.ageTo, " +
        "COALESCE(FG.cashPaymentOnRegistration,FALSE) " +
        "FROM RtFee F " +
        "INNER JOIN F.rtFeeGroup FG " +
        "WHERE F.id.feeNr = :feeNr " +
        "AND F.id.clientNr = :sessionClientNr " +
        "INTO " +
        ":fee, " +
        ":currency, " +
        ":dateFrom, " +
        ":dateTo, " +
        ":ageFrom, " +
        ":ageTo, " +
        ":cashPaymentOnRegistration "
        , formData
        );

    return formData;
  }

  @Override
  public FeeFormData store(FeeFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateFeePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    String queryString = "UPDATE RtFee F " +
        "SET " +
        // only during creation set fee group nr
        (formData.getFeeGroupNr() != null ? "F.rtFeeGroup.id.feeGroupNr = :feeGroupNr, " : "") +
        "F.fee = :fee, " +
        "F.evtFrom = :dateFrom, " +
        "F.evtTo = :dateTo, " +
        "F.ageFrom = :ageFrom, " +
        "F.ageTo = :ageTo, " +
        "F.currencyUid = :currency " +
        "WHERE F.id.feeNr = :feeNr " +
        "AND F.id.clientNr = :sessionClientNr";

    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    return formData;
  }

  @Override
  public FeeFormData delete(FeeFormData formData) throws ProcessingException {
    if (formData != null) {
      RtFee fee = new RtFee();
      RtFeeKey key = RtFeeKey.create(formData.getFeeNr());
      fee.setId(key);
      JPA.remove(fee);
    }
    return formData;
  }

  @Override
  public List<FeeFormData> loadFeeConfiguration() throws ProcessingException {
    List<FeeFormData> result = new ArrayList<>();

    result.addAll(loadFeeConfigurationEventClass());
    result.addAll(loadFeeConfigurationAddInfo());

    return result;
  }

  protected List<FeeFormData> loadFeeConfigurationAddInfo() {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtFee> feeRoot = selectQuery.from(RtFee.class);
    Join<RtFee, RtFeeGroup> joinFeeGroup = feeRoot.join(RtFee_.rtFeeGroup);
    Join<RtFeeGroup, RtAdditionalInformationDef> joinAddInfoDef = joinFeeGroup.join(RtFeeGroup_.rtAdditionalInformationDefs);

    selectQuery.select(b.array(
        feeRoot.get(RtFee_.id).get(RtFeeKey_.feeNr),
        joinAddInfoDef.get(RtAdditionalInformationDef_.id).get(RtAdditionalInformationDefKey_.additionalInformationUid),
        feeRoot.get(RtFee_.fee),
        feeRoot.get(RtFee_.currencyUid),
        feeRoot.get(RtFee_.evtFrom),
        feeRoot.get(RtFee_.evtTo),
        feeRoot.get(RtFee_.ageFrom),
        feeRoot.get(RtFee_.ageTo),
        joinFeeGroup.get(RtFeeGroup_.cashPaymentOnRegistration)
        ));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    List<FeeFormData> result = new ArrayList<>();
    for (Object[] row : resultList) {
      FeeFormData feeData = new FeeFormData();
      feeData.setFeeNr(TypeCastUtility.castValue(row[0], Long.class));
      feeData.setAdditionalInformationUid(TypeCastUtility.castValue(row[1], Long.class));
      feeData.getFee().setValue(TypeCastUtility.castValue(row[2], Double.class));
      feeData.getCurrency().setValue(TypeCastUtility.castValue(row[3], Long.class));
      feeData.getDateFrom().setValue(TypeCastUtility.castValue(row[4], Date.class));
      feeData.getDateTo().setValue(TypeCastUtility.castValue(row[5], Date.class));
      feeData.getAgeFrom().setValue(TypeCastUtility.castValue(row[6], Long.class));
      feeData.getAgeTo().setValue(TypeCastUtility.castValue(row[7], Long.class));
      feeData.getCashPaymentOnRegistrationProperty().setValue(BooleanUtility.nvl(TypeCastUtility.castValue(row[8], Boolean.class)));
      result.add(feeData);
    }
    return result;
  }

  protected List<FeeFormData> loadFeeConfigurationEventClass() {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<Object[]> selectQuery = b.createQuery(Object[].class);
    Root<RtFee> feeRoot = selectQuery.from(RtFee.class);
    Join<RtFee, RtFeeGroup> joinFeeGroup = feeRoot.join(RtFee_.rtFeeGroup);
    Join<RtFeeGroup, RtEventClass> joinEventClass = joinFeeGroup.join(RtFeeGroup_.rtEventClasses);

    selectQuery.select(b.array(
        feeRoot.get(RtFee_.id).get(RtFeeKey_.feeNr),
        joinEventClass.get(RtEventClass_.id).get(RtEventClassKey_.eventNr),
        joinEventClass.get(RtEventClass_.id).get(RtEventClassKey_.classUid),
        feeRoot.get(RtFee_.fee),
        feeRoot.get(RtFee_.currencyUid),
        feeRoot.get(RtFee_.evtFrom),
        feeRoot.get(RtFee_.evtTo),
        feeRoot.get(RtFee_.ageFrom),
        feeRoot.get(RtFee_.ageTo),
        joinFeeGroup.get(RtFeeGroup_.cashPaymentOnRegistration)
        ));
    List<Object[]> resultList = JPA.createQuery(selectQuery).getResultList();

    List<FeeFormData> result = new ArrayList<>();
    for (Object[] row : resultList) {
      FeeFormData feeData = new FeeFormData();
      feeData.setFeeNr(TypeCastUtility.castValue(row[0], Long.class));
      feeData.setEventNr(TypeCastUtility.castValue(row[1], Long.class));
      feeData.setClassUid(TypeCastUtility.castValue(row[2], Long.class));
      feeData.getFee().setValue(TypeCastUtility.castValue(row[3], Double.class));
      feeData.getCurrency().setValue(TypeCastUtility.castValue(row[4], Long.class));
      feeData.getDateFrom().setValue(TypeCastUtility.castValue(row[5], Date.class));
      feeData.getDateTo().setValue(TypeCastUtility.castValue(row[6], Date.class));
      feeData.getAgeFrom().setValue(TypeCastUtility.castValue(row[7], Long.class));
      feeData.getAgeTo().setValue(TypeCastUtility.castValue(row[8], Long.class));
      feeData.getCashPaymentOnRegistrationProperty().setValue(BooleanUtility.nvl(TypeCastUtility.castValue(row[9], Boolean.class)));
      result.add(feeData);
    }
    return result;
  }
}
