package com.rtiming.client.dataexchange.cache;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.common.ui.fields.AbstractCodeBox.ShortcutField;
import com.rtiming.client.entry.startblock.StartblockForm;
import com.rtiming.client.entry.startblock.StartblockForm.MainBox.CodeBox;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.data.EventTestDataProvider;
import com.rtiming.shared.dataexchange.cache.StartblockDataCacher;
import com.rtiming.shared.entry.startblock.IStartblockProcessService;
import com.rtiming.shared.entry.startblock.StartblockFormData;
import com.rtiming.shared.settings.CodeFormData;
import com.rtiming.shared.settings.ICodeProcessService;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class StartblockDataCacherTest {

  private EventTestDataProvider event;
  private StartblockForm startblock;

  @Before
  public void before() throws ProcessingException {
    event = new EventTestDataProvider();

    startblock = new StartblockForm();
    startblock.startNew();
    FormTestUtility.fillFormFields(startblock);
    startblock.doOk();

  }

  @Test
  public void testGet() throws ProcessingException {
    StartblockDataCacher cacher = new StartblockDataCacher();
    CodeFormData result = cacher.get(startblock.getMainBox().getFieldByClass(CodeBox.class).getFieldByClass(ShortcutField.class).getValue());
    Assert.assertEquals(startblock.getStartblockUid(), result.getCodeUid());
  }

  @Test
  public void testPut() throws ProcessingException {
    StartblockDataCacher cacher = new StartblockDataCacher();

    CodeFormData formData = new CodeFormData();
    formData.setCodeUid(startblock.getStartblockUid());
    formData = BEANS.get(ICodeProcessService.class).load(formData);
    Assert.assertNotNull(formData.getCodeUid());
    Assert.assertNotNull(formData.getCodeType());

    cacher.put("ABC", formData);

    CodeFormData result = cacher.get("ABC");
    Assert.assertEquals(formData.getCodeUid(), result.getCodeUid());

    String key = startblock.getMainBox().getFieldByClass(CodeBox.class).getFieldByClass(ShortcutField.class).getValue();
    CodeFormData result2 = cacher.get(key);
    Assert.assertEquals(formData.getCodeUid(), result2.getCodeUid());
  }

  @Test
  public void testDefault() throws ProcessingException {
    StartblockDataCacher cacher = new StartblockDataCacher();

    CodeFormData formData = new CodeFormData();
    formData.setCodeUid(startblock.getStartblockUid());
    formData = BEANS.get(ICodeProcessService.class).load(formData);
    Assert.assertNotNull(formData.getCodeUid());
    Assert.assertNotNull(formData.getCodeType());

    cacher.put("default", formData);

    CodeFormData result = cacher.getDefault();
    Assert.assertEquals(formData.getCodeUid(), result.getCodeUid());
  }

  @After
  public void after() throws ProcessingException {
    event.remove();
    StartblockFormData formData = new StartblockFormData();
    formData.setStartblockUid(startblock.getStartblockUid());
    formData = BEANS.get(IStartblockProcessService.class).load(formData);
    BEANS.get(ICodeProcessService.class).deleteCodeBox(formData.getCodeBox());
  }

}
