package com.rtiming.server.settings;

import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.holders.ITableHolder;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.shared.services.common.code.ICodeType;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.FMilaServerUtility;
import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.AbstractCodeBoxData;
import com.rtiming.shared.common.AbstractCodeBoxData.Language.LanguageRowData;
import com.rtiming.shared.common.AbstractSqlCodeType;
import com.rtiming.shared.common.security.permission.CreateCodePermission;
import com.rtiming.shared.common.security.permission.DeleteCodePermission;
import com.rtiming.shared.common.security.permission.ReadCodePermission;
import com.rtiming.shared.common.security.permission.UpdateCodePermission;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.dao.RtUcl;
import com.rtiming.shared.dao.RtUclKey;
import com.rtiming.shared.dao.RtUclKey_;
import com.rtiming.shared.dao.RtUcl_;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;
import com.rtiming.shared.settings.user.LanguageCodeType;

public class CodeProcessService implements ICodeProcessService {

  @Override
  public CodeFormData prepareCreate(CodeFormData formData) throws ProcessingException {
    formData.getMainBox().getCodeUid().setValue(formData.getCodeUid());
    formData.getMainBox().getCodeTypeUid().setValue(formData.getCodeType());
    BEANS.get(ICodeProcessService.class).prepareCreateCodeBox(formData.getMainBox());
    return formData;
  }

  @Override
  public CodeFormData create(CodeFormData formData) throws ProcessingException {
    formData.getMainBox().getCodeUid().setValue(formData.getCodeUid());
    formData.getMainBox().getCodeTypeUid().setValue(formData.getCodeType());
    AbstractCodeBoxData result = BEANS.get(ICodeProcessService.class).createCodeBox(formData.getMainBox());
    formData.setCodeUid(result.getCodeUid().getValue());
    return formData;
  }

  @Override
  public CodeFormData load(CodeFormData formData) throws ProcessingException {
    formData.getMainBox().getCodeUid().setValue(formData.getCodeUid());
    formData.getMainBox().getCodeTypeUid().setValue(formData.getCodeType());
    AbstractCodeBoxData result = BEANS.get(ICodeProcessService.class).loadCodeBox(formData.getMainBox());
    formData.setCodeType(result.getCodeTypeUid().getValue());
    return formData;
  }

  @Override
  public CodeFormData store(CodeFormData formData) throws ProcessingException {
    formData.getMainBox().getCodeUid().setValue(formData.getCodeUid());
    formData.getMainBox().getCodeTypeUid().setValue(formData.getCodeType());
    BEANS.get(ICodeProcessService.class).storeCodeBox(formData.getMainBox());
    return formData;
  }

  @Override
  public AbstractCodeBoxData prepareCreateCodeBox(AbstractCodeBoxData codeBox) throws ProcessingException {
    if (!ACCESS.check(new CreateCodePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    codeBox.getActive().setValue(true);

    int k = 0;
    for (ICode code : CODES.getCodeType(LanguageCodeType.class).getCodes()) {
      codeBox.getLanguage().addRow();
      codeBox.getLanguage().rowAt(k).setLanguage((Long) code.getId());
      k++;
    }

    return codeBox;
  }

  @Override
  public AbstractCodeBoxData createCodeBox(AbstractCodeBoxData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateCodePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    if (formData.getCodeUid().getValue() == null) {
      RtUcKey key = RtUcKey.create((Long) null);
      RtUc uc = new RtUc();
      uc.setCodeType(formData.getCodeTypeUid().getValue());
      uc.setActive(formData.getActive().getValue());
      uc.setId(key);
      JPA.persist(uc);

      formData.getCodeUid().setValue(uc.getId().getId());
    }
    else {
      RtUcKey key = RtUcKey.create(formData.getCodeUid().getValue());
      if (JPA.find(RtUc.class, key) == null) {
        RtUc uc = new RtUc();
        uc.setId(key);
        uc.setCodeType(formData.getCodeTypeUid().getValue());
        uc.setActive(formData.getActive().getValue());
        JPA.persist(uc);
      }
    }
    formData = storeCodeBox(formData);

    return formData;
  }

  @Override
  public AbstractCodeBoxData loadCodeBox(AbstractCodeBoxData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadCodePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT U.active, U.shortcut, U.codeType " + "FROM RtUc U " + "WHERE U.id.ucUid = :codeUid " + "AND U.id.clientNr = :sessionClientNr " + "INTO :active, :shortcut, :codeTypeUid ", formData);

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtUcl> select = b.createQuery(RtUcl.class);
    Root<RtUcl> rtecard = select.from(RtUcl.class);
    select.where(b.and(b.equal(rtecard.get(RtUcl_.id).get(RtUclKey_.ucUid), formData.getCodeUid().getValue()), b.equal(rtecard.get(RtUcl_.id).get(RtUclKey_.clientNr), ServerSession.get().getSessionClientNr())));
    List<RtUcl> result = JPA.createQuery(select).getResultList();
    for (RtUcl ucl : result) {
      LanguageRowData rowId = formData.getLanguage().addRow(ITableHolder.STATUS_INSERTED);
      rowId.setTranslation(ucl.getCodeName());
      rowId.setLanguage(ucl.getId().getLanguageUid());
    }

    return formData;
  }

  @Override
  public AbstractCodeBoxData storeCodeBox(AbstractCodeBoxData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateCodePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    String queryString = "UPDATE RtUc " + "SET active = :active," + "shortcut = :shortcut, " + "codeType = :codeTypeUid " + "WHERE id.ucUid = :codeUid " + "AND id.clientNr = :sessionClientNr";
    FMilaQuery query = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query, queryString, formData);
    query.executeUpdate();

    for (int i = 0; i < formData.getLanguage().getRowCount(); i++) {
      if (StringUtility.isNullOrEmpty(formData.getLanguage().rowAt(i).getTranslation())) {
        throw new VetoException(Texts.get("CodeTranslationRequiredForEachLanguageMessage"));
      }

      RtUclKey key = new RtUclKey();
      key.setClientNr(ServerSession.get().getSessionClientNr());
      key.setLanguageUid(formData.getLanguage().rowAt(i).getLanguage());
      key.setUcUid(formData.getCodeUid().getValue());
      RtUcl ucl = new RtUcl();
      ucl.setId(key);
      ucl.setCodeName(formData.getLanguage().rowAt(i).getTranslation());
      JPA.merge(ucl);
    }

    ICodeType codeTypeClass = CODES.findCodeTypeById(formData.getCodeTypeUid().getValue());
    if (codeTypeClass instanceof AbstractSqlCodeType) {
      if (!FMilaServerUtility.isTestEnvironment()) {
        // for some reason, reload in junit mode causes problems
        CODES.reloadCodeType(codeTypeClass.getClass());
      }
    }

    return formData;
  }

  @Override
  public String getTranslation(Long codeUid, Long clientNr) throws ProcessingException {
    String queryString = "SELECT MAX(codeName) " + "FROM RtUcl " + "WHERE id.ucUid = :codeUid " + "AND id.clientNr = :clientNr) " + "AND id.languageUid = " + ServerSession.get().getLanguageUid();
    FMilaTypedQuery<String> query = JPA.createQuery(queryString, String.class);
    query.setParameter("codeUid", codeUid);
    query.setParameter("clientNr", NumberUtility.nvl(clientNr, ServerSession.get().getSessionClientNr()));
    String translation = query.getSingleResult();

    return translation;
  }

  @Override
  public CodeFormData find(String shortcut, long codeType) throws ProcessingException {
    shortcut = StringUtility.trim(shortcut);
    String shortcutUppercase = StringUtility.uppercase(shortcut);

    String queryString = "SELECT MAX(id.ucUid) " + "FROM RtUc " + "WHERE UPPER(shortcut) = :shortcut " + "AND codeType = :codeType " + "AND id.clientNr = :sessionClientNr ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("shortcut", shortcutUppercase);
    query.setParameter("codeType", codeType);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Long codeUid = query.getSingleResult();

    CodeFormData code = new CodeFormData();
    if (codeUid != null) {
      code.setCodeUid(codeUid);
      code.setCodeType(codeType);
      code = load(code);
    }
    else {
      code.getMainBox().getShortcut().setValue(shortcut);
      code.getMainBox().getActive().setValue(true);
      code.setCodeType(codeType);

      // loop through available languages
      for (ICode<?> c : CODES.getCodeType(LanguageCodeType.class).getCodes()) {
        LanguageRowData newRow = code.getMainBox().getLanguage().addRow(ITableHolder.STATUS_INSERTED);
        newRow.setTranslation(shortcut);
        newRow.setLanguage((Long) c.getId());
      }
    }

    return code;
  }

  @Override
  public void deleteCodeBox(AbstractCodeBoxData formData) throws ProcessingException {
    if (!ACCESS.check(new DeleteCodePermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (formData == null || formData.getCodeUid().getValue() == null) {
      return;
    }

    String queryString = "DELETE FROM RtUcl " + "WHERE id.clientNr = :sessionClientNr " + "AND id.ucUid = :codeUid";
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("codeUid", formData.getCodeUid().getValue());
    query.executeUpdate();

    queryString = "DELETE FROM RtUc " + "WHERE id.clientNr = :sessionClientNr " + "AND id.ucUid = :codeUid";
    query = JPA.createQuery(queryString);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.setParameter("codeUid", formData.getCodeUid().getValue());
    query.executeUpdate();

    if (formData.getCodeTypeUid().getValue() != null) {
      ICodeType codeType = CODES.findCodeTypeById(formData.getCodeTypeUid().getValue());
      if (codeType != null) {
        CODES.reloadCodeType(codeType.getClass());
      }
    }
  }

}
