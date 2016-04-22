package com.rtiming.server.settings.addinfo;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.common.code.CODES;
import org.eclipse.scout.rt.shared.services.common.code.ICode;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.common.database.sql.AdditionalInformationValueBean;
import com.rtiming.shared.settings.addinfo.AdditionalInformationAdministrationFormData;
import com.rtiming.shared.settings.addinfo.AdditionalInformationCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;
import com.rtiming.shared.settings.addinfo.IAdditionalInformationAdministrationProcessService;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class AdditionalInformationDatabaseUtilityServerTest {

  private static int counter = 0;

  @Test
  public void testCreate() throws ProcessingException {
    List<? extends ICode<Long>> codes = BEANS.get(AdditionalInformationTypeCodeType.class).getCodes();
    for (ICode<?> code : codes) {
      AdditionalInformationAdministrationFormData bean = testAdditionalInformationColumn((Long) code.getId());
      dropColumn(bean);
    }
  }

  @Test
  public void testRename() throws Exception {
    AdditionalInformationAdministrationFormData bean = testAdditionalInformationColumn(AdditionalInformationTypeCodeType.IntegerCode.ID);
    String oldShortcut = bean.getCodeBox().getShortcut().getValue();
    AdditionalInformationDatabaseUtility.renameColumn(bean.getEntity().getValue(), "renamed" + counter++, oldShortcut);
    doSelect(bean);
    dropColumn(bean);
  }

  @Test
  public void testUpdate() throws Exception {
    List<? extends ICode<Long>> codes = BEANS.get(AdditionalInformationTypeCodeType.class).getCodes();
    for (ICode<?> code : codes) {
      AdditionalInformationAdministrationFormData bean = doCreateColumn(code);
      AdditionalInformationValueBean value = new AdditionalInformationValueBean();
      value.setAdditionalInformationUid(bean.getAdditionalInformationUid());
      value.setTypeUid(bean.getType().getValue());
      AdditionalInformationDatabaseUtility.updateValue(bean.getEntity().getValue(), 1L, ServerSession.get().getSessionClientNr(), value);
      JPA.commit();
      dropColumn(bean);
    }
  }

  @Test
  public void testSelect() throws Exception {
    List<? extends ICode<Long>> codes = BEANS.get(AdditionalInformationTypeCodeType.class).getCodes();
    for (ICode<?> code : codes) {
      AdditionalInformationAdministrationFormData bean = doCreateColumn(code);
      AdditionalInformationValueBean value = new AdditionalInformationValueBean();
      value.setAdditionalInformationUid(bean.getAdditionalInformationUid());
      value.setTypeUid(bean.getType().getValue());
      List<AdditionalInformationValueBean> values = new ArrayList<>();
      values.add(value);
      AdditionalInformationDatabaseUtility.selectValue(EntityCodeType.ClubCode.ID, null, ServerSession.get().getSessionClientNr(), values);
      JPA.commit();
      dropColumn(bean);
    }
  }

  private AdditionalInformationAdministrationFormData doCreateColumn(ICode<?> code) throws ProcessingException {
    AdditionalInformationAdministrationFormData bean = new AdditionalInformationAdministrationFormData();
    IAdditionalInformationAdministrationProcessService svc = BEANS.get(IAdditionalInformationAdministrationProcessService.class);
    bean = svc.prepareCreate(bean);
    bean.getType().setValue((Long) code.getId());
    bean.getCodeBox().getShortcut().setValue("test");
    bean.getEntity().setValue(EntityCodeType.ClubCode.ID);
    for (int k = 0; k < bean.getCodeBox().getLanguage().getRowCount(); k++) {
      bean.getCodeBox().getLanguage().getRows()[k].setTranslation("test");
    }
    bean = svc.create(bean);
    CODES.reloadCodeType(AdditionalInformationCodeType.class);
    return bean;
  }

  @Test(expected = ProcessingException.class)
  public void testJPAexecuteDDL() throws Exception {
    AdditionalInformationDatabaseUtility.executeDDL("wrong ddl");
  }

  private AdditionalInformationAdministrationFormData testAdditionalInformationColumn(Long typeUid) throws ProcessingException {
    AdditionalInformationAdministrationFormData bean = new AdditionalInformationAdministrationFormData();
    bean.getEntity().setValue(EntityCodeType.RunnerCode.ID);
    bean.getType().setValue(typeUid);
    String column = "test" + counter++;
    bean.getCodeBox().getShortcut().setValue(column);
    doSelect(bean);
    return bean;
  }

  private void doSelect(AdditionalInformationAdministrationFormData bean) throws ProcessingException {
    AdditionalInformationDatabaseUtility.createColumn(bean.getEntity().getValue(), bean.getType().getValue(), bean.getCodeBox().getShortcut().getValue());

    String test = "SELECT " + AdditionalInformationDatabaseUtility.getColumnName(bean.getCodeBox().getShortcut().getValue()) + " FROM RT_RUNNER";
    List<?> result = JPA.currentEntityManager().createNativeQuery(test).getResultList();
    assertTrue(result.isEmpty());
  }

  private void dropColumn(AdditionalInformationAdministrationFormData bean) throws ProcessingException {
    AdditionalInformationDatabaseUtility.dropColumn(bean.getEntity().getValue(), bean.getCodeBox().getShortcut().getValue());
  }
}
