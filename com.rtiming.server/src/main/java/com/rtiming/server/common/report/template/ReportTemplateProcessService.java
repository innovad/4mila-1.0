package com.rtiming.server.common.report.template;

import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.entry.SharedCacheServerUtility;
import com.rtiming.shared.common.file.IFileProcessService;
import com.rtiming.shared.common.report.template.IReportTemplateProcessService;
import com.rtiming.shared.common.security.permission.CreateReportTemplatePermission;
import com.rtiming.shared.common.security.permission.DeleteReportTemplatePermission;
import com.rtiming.shared.common.security.permission.ReadReportTemplatePermission;
import com.rtiming.shared.common.security.permission.UpdateReportTemplatePermission;
import com.rtiming.shared.dao.RtReportTemplate;
import com.rtiming.shared.dao.RtReportTemplateFile;
import com.rtiming.shared.dao.RtReportTemplateFileKey;
import com.rtiming.shared.dao.RtReportTemplateFileKey_;
import com.rtiming.shared.dao.RtReportTemplateFile_;
import com.rtiming.shared.dao.RtReportTemplateKey;

public class ReportTemplateProcessService  implements IReportTemplateProcessService {

  private static final Charset UTF8 = Charset.forName("UTF8");

  @Override
  public RtReportTemplate prepareCreate(RtReportTemplate bean) throws ProcessingException {
    if (!ACCESS.check(new CreateReportTemplatePermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }
    bean.setActive(true);
    return bean;
  }

  @Override
  public RtReportTemplate create(RtReportTemplate bean) throws ProcessingException {
    if (!ACCESS.check(new CreateReportTemplatePermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    // new keys
    bean.setId(RtReportTemplateKey.create(bean.getId()));
    for (RtReportTemplateFile file : bean.getTemplateFiles()) {
      file.setId(RtReportTemplateFileKey.create(file.getId()));
    }

    JPA.persist(bean);
    for (RtReportTemplateFile file : bean.getTemplateFiles()) {
      file.setReportTemplateNr(bean.getId().getId());
      storeReportTemplateFile(file);
    }

    SharedCacheServerUtility.notifyClients();
    return bean;
  }

  @Override
  public RtReportTemplate load(RtReportTemplateKey key) throws ProcessingException {
    if (!ACCESS.check(new ReadReportTemplatePermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    key = RtReportTemplateKey.create(key);
    RtReportTemplate bean = JPA.find(RtReportTemplate.class, key);
    loadReportTemplateFiles(bean);

    return bean;
  }

  @Override
  public RtReportTemplate store(RtReportTemplate bean) throws ProcessingException {
    if (!ACCESS.check(new UpdateReportTemplatePermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    JPA.merge(bean);
    for (RtReportTemplateFile file : bean.getTemplateFiles()) {
      file.setReportTemplateNr(bean.getId().getId());
      storeReportTemplateFile(file);
    }

    SharedCacheServerUtility.notifyClients();
    return bean;
  }

  @Override
  public void delete(RtReportTemplateKey key) throws ProcessingException {
    if (!ACCESS.check(new DeleteReportTemplatePermission())) {
      throw new VetoException(TEXTS.get("AuthorizationFailed"));
    }

    RtReportTemplate bean = load(key);
    for (RtReportTemplateFile file : bean.getTemplateFiles()) {
      BEANS.get(IFileProcessService.class).deleteFile(file.getId().getId(), file.getId().getClientNr(), SERVER_MAP_DIR, new String[]{TEMPLATE_SUFFIX_JRXML, TEMPLATE_SUFFIX_JRTX});
    }

    JPA.remove(bean);
    SharedCacheServerUtility.notifyClients();
  }

  private void loadReportTemplateFiles(RtReportTemplate template) throws ProcessingException {
    template.setTemplateFiles(new HashSet<RtReportTemplateFile>());

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtReportTemplateFile> q = b.createQuery(RtReportTemplateFile.class);

    Root<RtReportTemplateFile> root = q.from(RtReportTemplateFile.class);
    q.where(
        b.equal(root.get(RtReportTemplateFile_.id).get(RtReportTemplateFileKey_.clientNr), template.getId().getClientNr()),
        b.equal(root.get(RtReportTemplateFile_.reportTemplateNr), template.getId().getId())
        );

    List<RtReportTemplateFile> files = JPA.createQuery(q).getResultList();
    for (RtReportTemplateFile file : files) {
      byte[] content = BEANS.get(IFileProcessService.class).loadFile(file.getId().getId(), file.getId().getClientNr(), IOUtility.getFileExtension(file.getTemplateFileName()), SERVER_MAP_DIR);
      String contentStr = "";
      if (content != null) {
        contentStr = new String(content, UTF8);
      }
      file.setTemplateContent(contentStr);
      template.getTemplateFiles().add(file);
    }

  }

  private void storeReportTemplateFile(RtReportTemplateFile file) throws ProcessingException {
    // store file
    byte[] content = file.getTemplateContent().getBytes(UTF8);
    BEANS.get(IFileProcessService.class).writeDataToFile(file.getId().getId(), file.getId().getClientNr(), IOUtility.getFileExtension(file.getTemplateFileName()), content, SERVER_MAP_DIR);

    // remove path from filename
    file.setTemplateFileName(IOUtility.getFileName(file.getTemplateFileName()));

    JPA.merge(file);
  }
}
