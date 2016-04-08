package com.rtiming.server.common.database;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Embeddable;
import javax.persistence.Query;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;

import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.server.services.common.file.RemoteFileService;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.common.database.setup.JPASetup;
import com.rtiming.server.common.infodisplay.InfoDisplayFilesInstaller;
import com.rtiming.shared.common.database.DatabaseInfoBean;
import com.rtiming.shared.common.database.IDatabaseService;
import com.rtiming.shared.common.database.SharedBackupUtility;
import com.rtiming.shared.common.database.TableInfoBean;
import com.rtiming.shared.common.database.jpa.JPAStyle;
import com.rtiming.shared.settings.IDefaultProcessService;

public class DatabaseService implements IDatabaseService {

  private static final IScoutLogger LOG = ScoutLogManager.getLogger(DatabaseService.class);

  @Override
  public List<String> getTables() throws ProcessingException {

    Set<EntityType<?>> entities = JPA.currentEntityManager().getMetamodel().getEntities();
    List<String> list = new ArrayList<>();
    for (EntityType<?> entity : entities) {
      list.add(entity.getJavaType().getSimpleName());
    }

    return list;
  }

  @Override
  public Object[][] getBackupsTableData() throws ProcessingException {
    return ServerBackupUtility.getBackupTableData();
  }

  @Override
  public void createBackup() throws ProcessingException {
    ServerBackupUtility.backup();
  }

  @Override
  public void deleteBackup(String... files) throws ProcessingException {
    ServerBackupUtility.delete(files);
  }

  @Override
  public String getDataDirectory() throws ProcessingException {
    JPASetup setup = JPASetup.get();
    if (JPAStyle.POSTGRES.equals(setup.getStyle())) {
      String query = "SELECT SETTING " + "FROM PG_SETTINGS " + "WHERE NAME = :name ";

      Query nativeQuery = JPA.currentEntityManager().createNativeQuery(query);
      nativeQuery.setParameter("name", "data_directory");
      List result = nativeQuery.getResultList();

      String setting = "";
      if (result.size() > 0) {
        setting = StringUtility.emptyIfNull(result.get(0));
      }

      return setting;
    }
    else if (JPAStyle.H2_EMBEDDED.equals(setup.getStyle())) {
      return System.getProperty("user.home");
    }
    return "not available";
  }

  @Override
  public void setScheduledBackupStatus(boolean force, boolean throwException) throws ProcessingException {
    IDefaultProcessService service = BEANS.get(IDefaultProcessService.class);
    String dir = service.getBackupDirectory();
    Long interval = service.getBackupInterval();

    boolean scheduledBackupActive = !StringUtility.isNullOrEmpty(dir) && SharedBackupUtility.backupIsActive(interval);
// TODO MIG
//    Scheduler scheduler = Activator.getDefault().getScheduler();
//    if (scheduler != null) {
//      if (force) {
//        scheduler.removeJobs(BackupJob.ID, BackupJob.ID);
//      }
//      if (scheduledBackupActive) {
//        // only add a job if job is not already active
//        if (scheduler.getJobs(BackupJob.ID, BackupJob.ID).size() <= 0) {
//          scheduler.addJob(new BackupJob(interval));
//        }
//      }
//      else {
//        scheduler.removeJobs(BackupJob.ID, BackupJob.ID);
//      }
//    }
//    else {
//      if (throwException) {
//        throw new ProcessingException("Scheduler not installed!");
//      }
//      LOG.info("Scheduler not installed!");
//    }
  }

  @Override
  public void setupApplication() throws ProcessingException {
    // TODO MIG
    // start backend session (jobs and probably webservices)
//    final ServerSession session = BEANS.get(IServerSessionRegistryService.class).newServerSession(ServerSession.class, Activator.getDefault().getBackendSubject(), UserAgent.createDefault());
//    Activator.getDefault().setBackendSession(session);
//
//    // install scheduler
//    Scheduler scheduler = Activator.getDefault().getScheduler();
//    if (Activator.getDefault().getScheduler() == null) {
//      scheduler = new Scheduler(Activator.getDefault().getBackendSubject(), ServerSession.class, new Ticker(Calendar.MINUTE));
//      scheduler.start();
//      Activator.getDefault().setScheduler(scheduler);
//    }
//
//    // start backup
//    BackupStart job = new BackupStart(Activator.getDefault().getBackendSession(), Activator.getDefault().getBackendSubject());
//    job.schedule();

    // ensure info display cache is populated
    InfoDisplayFilesInstaller.installFilesOnLocation();
  }

  @Override
  public Date getLastBackup() throws ProcessingException {
    return ServerBackupUtility.getLastBackup();
  }

  @Override
  public void restoreBackup(String backupFileName) throws ProcessingException {
    ServerBackupUtility.restore(backupFileName);
  }

  @Override
  public DatabaseInfoBean getDatabaseInfo() throws ProcessingException {
    DatabaseInfoBean bean = new DatabaseInfoBean();

    bean.setClientSessionNr(ServerSession.get().getSessionClientNr());
    bean.setDataDirectory(getDataDirectory());
    bean.setFileStoreRootPath(BEANS.get(RemoteFileService.class).getRootPath());
    bean.setLastBackup(getLastBackup());
    bean.setStyle(JPASetup.get().getStyle());
    bean.setJdbcUrl(JPASetup.get().getJdbcMappingName());

    return bean;
  }

  @SuppressWarnings("unchecked")
  @Override
  public TableInfoBean getColumns(String tableId) throws ProcessingException {

    EntityType<?> foundEntity = null;
    Set<EntityType<?>> entities = JPA.currentEntityManager().getMetamodel().getEntities();
    for (EntityType<?> entity : entities) {
      if (StringUtility.equalsIgnoreCase(entity.getJavaType().getSimpleName(), tableId)) {
        foundEntity = entity;
      }
    }
    if (foundEntity == null) {
      throw new ProcessingException("Unable to fetch columns for " + tableId);
    }

    List<String> columns = new ArrayList<>();
    List<String> pkColumns = new ArrayList<>();
    for (Attribute attr : foundEntity.getAttributes()) {
      if (attr.getJavaType().isAssignableFrom(Long.class) || attr.getJavaType().isAssignableFrom(String.class) || attr.getJavaType().isAssignableFrom(Boolean.class) || attr.getJavaType().isAssignableFrom(Timestamp.class) || attr.getJavaType().isAssignableFrom(Double.class) || attr.getJavaType().isAssignableFrom(Date.class)) {
        columns.add(attr.getName());
      }
      if (attr.getJavaType().getAnnotation(Embeddable.class) != null) {
        for (Field field : attr.getJavaType().getDeclaredFields()) {
          if (!Modifier.isStatic(field.getModifiers())) {
            pkColumns.add("id." + field.getName());
          }
        }
      }
    }
    Collections.sort(columns);
    Collections.sort(pkColumns);
    columns = CollectionUtility.combine(pkColumns, columns);
    String string = "SELECT " + CollectionUtility.format(columns, ",") + " FROM " + tableId;
    Object[][] data = JPAUtility.convertList2Array(JPA.createQuery(string).getResultList());

    TableInfoBean bean = new TableInfoBean();
    bean.setColumns(columns.toArray(new String[0]));
    bean.setData(data);

    return bean;
  }

  @Override
  public String getGlobalWebRootURL() throws ProcessingException {
    // TODO MIG
//    String jaxwsServerUrl = BEANS.get(OnlineServiceClient.class).getUrl();
//    String serverUrl = StringUtility.replace(jaxwsServerUrl, "jaxws/onlineService", "web/");
//    return serverUrl;
    return null;
  }

}
