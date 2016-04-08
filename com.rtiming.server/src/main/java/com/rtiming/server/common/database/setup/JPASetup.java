package com.rtiming.server.common.database.setup;

import javax.persistence.ValidationMode;

import org.eclipse.scout.rt.platform.Platform;
import org.hibernate.cfg.Configuration;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.database.jpa.JPAStyle;
import com.rtiming.shared.dao.RtAccount;
import com.rtiming.shared.dao.RtAccountClient;
import com.rtiming.shared.dao.RtAccountClientKey;
import com.rtiming.shared.dao.RtAdditionalInformationDef;
import com.rtiming.shared.dao.RtAdditionalInformationDefKey;
import com.rtiming.shared.dao.RtAddress;
import com.rtiming.shared.dao.RtAddressKey;
import com.rtiming.shared.dao.RtCity;
import com.rtiming.shared.dao.RtCityKey;
import com.rtiming.shared.dao.RtClassAge;
import com.rtiming.shared.dao.RtClassAgeKey;
import com.rtiming.shared.dao.RtClient;
import com.rtiming.shared.dao.RtClub;
import com.rtiming.shared.dao.RtClubKey;
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControlKey;
import com.rtiming.shared.dao.RtControlReplacement;
import com.rtiming.shared.dao.RtControlReplacementKey;
import com.rtiming.shared.dao.RtCountry;
import com.rtiming.shared.dao.RtCountryKey;
import com.rtiming.shared.dao.RtCourse;
import com.rtiming.shared.dao.RtCourseControl;
import com.rtiming.shared.dao.RtCourseControlKey;
import com.rtiming.shared.dao.RtCourseKey;
import com.rtiming.shared.dao.RtCurrency;
import com.rtiming.shared.dao.RtCurrencyKey;
import com.rtiming.shared.dao.RtDefault;
import com.rtiming.shared.dao.RtDefaultKey;
import com.rtiming.shared.dao.RtEcard;
import com.rtiming.shared.dao.RtEcardKey;
import com.rtiming.shared.dao.RtEcardStation;
import com.rtiming.shared.dao.RtEcardStationKey;
import com.rtiming.shared.dao.RtEntry;
import com.rtiming.shared.dao.RtEntryKey;
import com.rtiming.shared.dao.RtEvent;
import com.rtiming.shared.dao.RtEventAdditionalInformation;
import com.rtiming.shared.dao.RtEventAdditionalInformationKey;
import com.rtiming.shared.dao.RtEventClass;
import com.rtiming.shared.dao.RtEventClassKey;
import com.rtiming.shared.dao.RtEventKey;
import com.rtiming.shared.dao.RtEventMap;
import com.rtiming.shared.dao.RtEventMapKey;
import com.rtiming.shared.dao.RtEventStartblock;
import com.rtiming.shared.dao.RtEventStartblockKey;
import com.rtiming.shared.dao.RtFee;
import com.rtiming.shared.dao.RtFeeGroup;
import com.rtiming.shared.dao.RtFeeGroupKey;
import com.rtiming.shared.dao.RtFeeKey;
import com.rtiming.shared.dao.RtKey;
import com.rtiming.shared.dao.RtMap;
import com.rtiming.shared.dao.RtMapKey;
import com.rtiming.shared.dao.RtParticipation;
import com.rtiming.shared.dao.RtParticipationKey;
import com.rtiming.shared.dao.RtPayment;
import com.rtiming.shared.dao.RtPaymentKey;
import com.rtiming.shared.dao.RtPosition;
import com.rtiming.shared.dao.RtPunch;
import com.rtiming.shared.dao.RtPunchKey;
import com.rtiming.shared.dao.RtPunchSession;
import com.rtiming.shared.dao.RtPunchSessionKey;
import com.rtiming.shared.dao.RtRace;
import com.rtiming.shared.dao.RtRaceControl;
import com.rtiming.shared.dao.RtRaceControlKey;
import com.rtiming.shared.dao.RtRaceKey;
import com.rtiming.shared.dao.RtRanking;
import com.rtiming.shared.dao.RtRankingEvent;
import com.rtiming.shared.dao.RtRankingEventKey;
import com.rtiming.shared.dao.RtRankingKey;
import com.rtiming.shared.dao.RtRegistration;
import com.rtiming.shared.dao.RtRegistrationKey;
import com.rtiming.shared.dao.RtReportTemplate;
import com.rtiming.shared.dao.RtReportTemplateFile;
import com.rtiming.shared.dao.RtReportTemplateFileKey;
import com.rtiming.shared.dao.RtReportTemplateKey;
import com.rtiming.shared.dao.RtRunner;
import com.rtiming.shared.dao.RtRunnerKey;
import com.rtiming.shared.dao.RtStartlistSetting;
import com.rtiming.shared.dao.RtStartlistSettingKey;
import com.rtiming.shared.dao.RtStartlistSettingOption;
import com.rtiming.shared.dao.RtStartlistSettingOptionKey;
import com.rtiming.shared.dao.RtStartlistSettingVacant;
import com.rtiming.shared.dao.RtStartlistSettingVacantKey;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.dao.RtUcl;
import com.rtiming.shared.dao.RtUclKey;
import com.rtiming.shared.dao.RtUser;
import com.rtiming.shared.dao.RtUserKey;
import com.rtiming.shared.dao.RtUserRole;
import com.rtiming.shared.dao.RtUserRoleKey;
import com.rtiming.shared.dao.RtWebSession;

public final class JPASetup {

  private static JPASetup singleton;

  private AbstractCreateSchema database;
  private final JPAStyle style;
  private final String jdbcMappingName;
  private final String jdbcDriverName;
  private final String hibernateDialect;
  private final String hibernateConnectionUsername;
  private final String hibernateConnectionPassword;
  private final String hibernateHbm2ddlAuto;
  private final boolean recreateDatabase;
  private final boolean insertDevData;

  private JPASetup() {
    String configIniMappingName = getProperty("jdbcMappingName", "jdbc:postgresql://localhost:5432/test?user=fmila");
    if (FMilaUtility.isStandalone()) {
      jdbcMappingName = configIniMappingName + "_" + FMilaUtility.getVersion();
    }
    else {
      jdbcMappingName = configIniMappingName;
    }
    jdbcDriverName = getProperty("jdbcDriverName", "org.postgresql.Driver");
    hibernateDialect = getProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL82Dialect");
    hibernateConnectionUsername = getProperty("hibernate.connection.username", "");
    hibernateConnectionPassword = getProperty("hibernate.connection.password", "");
    hibernateHbm2ddlAuto = getProperty("hibernate.hbm2ddl.auto", "validate");
    recreateDatabase = Boolean.parseBoolean(getProperty("database.recreate", "false"));
    insertDevData = Boolean.parseBoolean(getProperty("database.devdata", "false"));
    style = getStyle();
  }

  public static JPASetup get() {
    if (singleton == null) {
      singleton = new JPASetup();
    }
    return singleton;
  }

  private String getProperty(String property, String defaultValue) {
    return null; // TODO MIG
//    String systemProperty = System.getProperty(property);
//    if (StringUtility.isNullOrEmpty(systemProperty)) {
//      String configIni = Activator.getDefault().getBundle().getBundleContext().getProperty(property);
//      if (StringUtility.isNullOrEmpty(configIni)) {
//        return defaultValue;
//      }
//      return configIni;
//    }
//    return systemProperty;
  }

  public void initDatabase() {
    if (JPAStyle.H2_EMBEDDED.equals(getStyle())) {
      database = new CreateH2Schema(this, recreateDatabase, insertDevData);
    }
    else if (JPAStyle.POSTGRES.equals(getStyle())) {
      database = new CreatePostgresqlSchema(this, recreateDatabase, insertDevData);
    }
    else throw new RuntimeException("Database style not implemented: " + getStyle());
    database.run();
  }

  public void createSessionFactoryBuilder() {
    // TODO MIG
    Configuration config = new Configuration();

    // Hibernate
    config.setProperty("hibernate.current_session_context_class", "thread");
    config.setProperty("javax.persistence.validation.mode", ValidationMode.NONE.toString());
    config.setProperty("hibernate.check_nullability", Boolean.FALSE.toString());

    config.setProperty("hibernate.show_sql", Boolean.toString(Platform.get().inDevelopmentMode()));
    config.setProperty("hibernate.format_sql", Boolean.FALSE.toString());
    config.setProperty("hibernate.dialect", getHibernateDialect());
    config.setProperty("hibernate.connection.driver_class", getJdbcDriverName());
    config.setProperty("hibernate.connection.url", getJdbcMappingName());
    config.setProperty("hibernate.connection.username", getHibernateConnectionUsername());
    config.setProperty("hibernate.connection.password", getHibernateConnectionPassword());
    config.setProperty("hibernate.hbm2ddl.auto", getHibernateHbm2ddlAuto());

    // C3P0
    config.setProperty("hibernate.c3p0.min_size", "1");
    config.setProperty("hibernate.c3p0.max_size", "250");
    config.setProperty("hibernate.c3p0.timeout", "600");
    config.setProperty("hibernate.c3p0.max_statements", "100");
    config.setProperty("hibernate.c3p0.idle_test_period", "0");
    config.setProperty("hibernate.c3p0.acquire_increment", "1");
    config.setProperty("hibernate.c3p0.statementCacheNumDeferredCloseThreads", "0");

    // ORM
    config.addAnnotatedClass(RtAccount.class);
    config.addAnnotatedClass(RtAccountClient.class);
    config.addAnnotatedClass(RtAccountClientKey.class);
    config.addAnnotatedClass(RtAdditionalInformationDef.class);
    config.addAnnotatedClass(RtAdditionalInformationDefKey.class);
    config.addAnnotatedClass(RtAddress.class);
    config.addAnnotatedClass(RtAddressKey.class);
    config.addAnnotatedClass(RtCity.class);
    config.addAnnotatedClass(RtCityKey.class);
    config.addAnnotatedClass(RtClassAge.class);
    config.addAnnotatedClass(RtClassAgeKey.class);
    config.addAnnotatedClass(RtClient.class);
    config.addAnnotatedClass(RtClub.class);
    config.addAnnotatedClass(RtClubKey.class);
    config.addAnnotatedClass(RtControl.class);
    config.addAnnotatedClass(RtControlKey.class);
    config.addAnnotatedClass(RtControlReplacement.class);
    config.addAnnotatedClass(RtControlReplacementKey.class);
    config.addAnnotatedClass(RtCountry.class);
    config.addAnnotatedClass(RtCountryKey.class);
    config.addAnnotatedClass(RtCourse.class);
    config.addAnnotatedClass(RtCourseKey.class);
    config.addAnnotatedClass(RtCourseControl.class);
    config.addAnnotatedClass(RtCourseControlKey.class);
    config.addAnnotatedClass(RtCurrency.class);
    config.addAnnotatedClass(RtCurrencyKey.class);
    config.addAnnotatedClass(RtDefault.class);
    config.addAnnotatedClass(RtDefaultKey.class);
    config.addAnnotatedClass(RtEcard.class);
    config.addAnnotatedClass(RtEcardKey.class);
    config.addAnnotatedClass(RtEcardStation.class);
    config.addAnnotatedClass(RtEcardStationKey.class);
    config.addAnnotatedClass(RtEntry.class);
    config.addAnnotatedClass(RtEntryKey.class);
    config.addAnnotatedClass(RtEvent.class);
    config.addAnnotatedClass(RtEventKey.class);
    config.addAnnotatedClass(RtEventAdditionalInformation.class);
    config.addAnnotatedClass(RtEventAdditionalInformationKey.class);
    config.addAnnotatedClass(RtEventClass.class);
    config.addAnnotatedClass(RtEventClassKey.class);
    config.addAnnotatedClass(RtEventMap.class);
    config.addAnnotatedClass(RtEventMapKey.class);
    config.addAnnotatedClass(RtEventStartblock.class);
    config.addAnnotatedClass(RtEventStartblockKey.class);
    config.addAnnotatedClass(RtFee.class);
    config.addAnnotatedClass(RtFeeKey.class);
    config.addAnnotatedClass(RtFeeGroup.class);
    config.addAnnotatedClass(RtFeeGroupKey.class);
    config.addAnnotatedClass(RtKey.class);
    config.addAnnotatedClass(RtMap.class);
    config.addAnnotatedClass(RtMapKey.class);
    config.addAnnotatedClass(RtParticipation.class);
    config.addAnnotatedClass(RtParticipationKey.class);
    config.addAnnotatedClass(RtPayment.class);
    config.addAnnotatedClass(RtPaymentKey.class);
    config.addAnnotatedClass(RtPosition.class);
    config.addAnnotatedClass(RtPunch.class);
    config.addAnnotatedClass(RtPunchKey.class);
    config.addAnnotatedClass(RtPunchSession.class);
    config.addAnnotatedClass(RtPunchSessionKey.class);
    config.addAnnotatedClass(RtRace.class);
    config.addAnnotatedClass(RtRaceKey.class);
    config.addAnnotatedClass(RtRaceControl.class);
    config.addAnnotatedClass(RtRaceControlKey.class);
    config.addAnnotatedClass(RtRanking.class);
    config.addAnnotatedClass(RtRankingKey.class);
    config.addAnnotatedClass(RtRankingEvent.class);
    config.addAnnotatedClass(RtRankingEventKey.class);
    config.addAnnotatedClass(RtRegistration.class);
    config.addAnnotatedClass(RtRegistrationKey.class);
    config.addAnnotatedClass(RtReportTemplate.class);
    config.addAnnotatedClass(RtReportTemplateKey.class);
    config.addAnnotatedClass(RtReportTemplateFile.class);
    config.addAnnotatedClass(RtReportTemplateFileKey.class);
    config.addAnnotatedClass(RtRunner.class);
    config.addAnnotatedClass(RtRunnerKey.class);
    config.addAnnotatedClass(RtStartlistSetting.class);
    config.addAnnotatedClass(RtStartlistSettingKey.class);
    config.addAnnotatedClass(RtStartlistSettingOption.class);
    config.addAnnotatedClass(RtStartlistSettingOptionKey.class);
    config.addAnnotatedClass(RtStartlistSettingVacant.class);
    config.addAnnotatedClass(RtStartlistSettingVacantKey.class);
    config.addAnnotatedClass(RtUc.class);
    config.addAnnotatedClass(RtUcKey.class);
    config.addAnnotatedClass(RtUcl.class);
    config.addAnnotatedClass(RtUclKey.class);
    config.addAnnotatedClass(RtUser.class);
    config.addAnnotatedClass(RtUserKey.class);
    config.addAnnotatedClass(RtUserRole.class);
    config.addAnnotatedClass(RtUserRoleKey.class);
    config.addAnnotatedClass(RtWebSession.class);

    //add loader bundles
    // hibContext.addBundle(Activator.getDefault().getBundle());
  }

  public JPAStyle getStyle() {
    return JPAStyle.H2_EMBEDDED; // TODO MIG
//    if (getJdbcMappingName().contains(":h2:")) {
//      return JPAStyle.H2_EMBEDDED;
//    }
//    else if (getJdbcMappingName().contains(":postgresql")) {
//      return JPAStyle.POSTGRES;
//    }
  }

  public String getJdbcDriverName() {
    return "org.h2.Driver";
    // TODO MIG return jdbcDriverName;
  }

  public String getJdbcMappingName() {
    return "jdbc:h2:~/dev";
    // TODO MIG return jdbcMappingName;
  }

  public String getHibernateDialect() {
    return hibernateDialect;
  }

  public String getHibernateConnectionPassword() {
    return hibernateConnectionPassword;
  }

  public String getHibernateConnectionUsername() {
    return "fmila";
    // return hibernateConnectionUsername;
  }

  public String getHibernateHbm2ddlAuto() {
    return hibernateHbm2ddlAuto;
  }

  public AbstractCreateSchema getDatabase() {
    return database;
  }

}
