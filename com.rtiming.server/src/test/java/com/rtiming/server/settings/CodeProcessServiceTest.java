package com.rtiming.server.settings;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.data.basic.table.AbstractTableRowData;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.entry.startblock.StartblockProcessService;
import com.rtiming.shared.common.AbstractCodeBoxData;
import com.rtiming.shared.common.AbstractCodeBoxData.Language.LanguageRowData;
import com.rtiming.shared.dao.RtUc;
import com.rtiming.shared.dao.RtUcKey;
import com.rtiming.shared.dao.RtUcl;
import com.rtiming.shared.dao.RtUclKey;
import com.rtiming.shared.entry.startblock.StartblockCodeType;
import com.rtiming.shared.entry.startblock.StartblockFormData;
import com.rtiming.shared.settings.CodeFormData;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class CodeProcessServiceTest {

  @Test
  public void testDeleteCodeBox1() throws ProcessingException {
    CodeProcessService svc = new CodeProcessService();
    svc.deleteCodeBox(null);
  }

  @Test
  public void testDeleteCodeBox2() throws ProcessingException {
    CodeProcessService svc = new CodeProcessService();
    StartblockFormData formData = new StartblockFormData();
    svc.deleteCodeBox(formData.getCodeBox());
  }

  @Test
  public void testDeleteCodeBox3() throws ProcessingException {
    CodeProcessService svc = new CodeProcessService();
    StartblockProcessService codeSvc = new StartblockProcessService();
    StartblockFormData formData = new StartblockFormData();
    formData = codeSvc.prepareCreate(formData);
    for (int k = 0; k < formData.getCodeBox().getLanguage().getRowCount(); k++) {
      formData.getCodeBox().getLanguage().getRows()[k].setTranslation("abc");
    }
    formData = codeSvc.create(formData);

    svc.deleteCodeBox(formData.getCodeBox());

    RtUc uc = JPA.find(RtUc.class, RtUcKey.create(formData.getCodeBox().getCodeUid().getValue()));
    Assert.assertNull("deleted", uc);
  }

  @Test
  public void testFind1() throws Exception {
    CodeProcessService svc = new CodeProcessService();
    svc.find(null, StartblockCodeType.ID);
  }

  @Test
  public void testFind2() throws Exception {
    CodeProcessService svc = new CodeProcessService();
    svc.find("", StartblockCodeType.ID);
  }

  @Test
  public void testFind3() throws Exception {
    String shortcut = "TEST" + System.currentTimeMillis();

    RtUc startblock = new RtUc();
    startblock.setCodeType(StartblockCodeType.ID);
    startblock.setActive(true);
    startblock.setShortcut(shortcut);
    startblock.setId(RtUcKey.create((Long) null));
    JPA.merge(startblock);

    CodeProcessService svc = new CodeProcessService();
    CodeFormData find = svc.find(shortcut.toLowerCase(), StartblockCodeType.ID);
    assertEquals("found", startblock.getId().getId(), find.getCodeUid());

    JPA.remove(startblock);
  }

  @Test
  public void testGetTranslation1() throws Exception {
    CodeProcessService svc = new CodeProcessService();
    String result = svc.getTranslation(null, null);
    assertNull(result);
  }

  @Test
  public void testGetTranslation2() throws Exception {
    RtUc startblock = new RtUc();
    startblock.setCodeType(StartblockCodeType.ID);
    startblock.setActive(true);
    startblock.setId(RtUcKey.create((Long) null));
    JPA.merge(startblock);

    RtUcl lang = new RtUcl();
    RtUclKey id = new RtUclKey();
    id.setClientNr(ServerSession.get().getSessionClientNr());
    id.setLanguageUid(ServerSession.get().getLanguageUid());
    id.setUcUid(startblock.getId().getId());
    lang.setId(id);
    lang.setCodeName("blubber");
    JPA.merge(lang);

    CodeProcessService svc = new CodeProcessService();
    String result = svc.getTranslation(startblock.getId().getId(), ServerSession.get().getSessionClientNr());
    assertEquals("translation correct", "blubber", result);

    JPA.remove(lang);
    JPA.remove(startblock);
  }

  @Test
  public void testCreateCodeBox1() throws Exception {
    doTestCreate(true);
  }

  @Test
  public void testCreateCodeBox2() throws Exception {
    doTestCreate(false);
  }

  @Test
  public void testCreateCodeBox3() throws Exception {
    long testId = -12345L;
    CodeProcessService svc = new CodeProcessService();
    CodeFormData formData = new CodeFormData();
    formData.getMainBox().getActive().setValue(false);
    formData.getMainBox().getCodeTypeUid().setValue(StartblockCodeType.ID);
    formData.getMainBox().getCodeUid().setValue(testId);
    formData.setCodeType(StartblockCodeType.ID);
    LanguageRowData rowId = formData.getMainBox().getLanguage().addRow(AbstractTableRowData.STATUS_INSERTED);
    rowId.setLanguage(ServerSession.get().getLanguageUid());
    String translation = "TRANSLATION" + System.currentTimeMillis();
    rowId.setTranslation(translation);
    AbstractCodeBoxData codeBoxData = svc.createCodeBox(formData.getMainBox());
    assertEquals("created", testId, formData.getMainBox().getCodeUid().getValue().longValue());
    assertEquals("created", testId, codeBoxData.getCodeUid().getValue().longValue());

    RtUcKey key = new RtUcKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setId(testId);
    RtUc find = JPA.find(RtUc.class, key);
    assertFalse("updated", find.getActive());

    svc.deleteCodeBox(codeBoxData);
  }

  private void doTestCreate(boolean createUcl) throws ProcessingException {
    RtUc startblock = new RtUc();
    startblock.setCodeType(StartblockCodeType.ID);
    startblock.setActive(true);
    startblock.setId(RtUcKey.create((Long) null));
    JPA.merge(startblock);

    if (createUcl) {
      RtUclKey keyUcl = new RtUclKey();
      keyUcl.setClientNr(ServerSession.get().getSessionClientNr());
      keyUcl.setLanguageUid(ServerSession.get().getLanguageUid());
      keyUcl.setUcUid(startblock.getId().getId());
      RtUcl ucl = new RtUcl();
      ucl.setId(keyUcl);
      ucl.setCodeName("BLABLA");
      JPA.persist(ucl);
    }

    CodeProcessService svc = new CodeProcessService();
    CodeFormData formData = new CodeFormData();
    formData.getMainBox().getActive().setValue(false);
    formData.getMainBox().getCodeUid().setValue(startblock.getId().getId());
    formData.getMainBox().getCodeTypeUid().setValue(StartblockCodeType.ID);
    formData.setCodeType(StartblockCodeType.ID);
    LanguageRowData rowId = formData.getMainBox().getLanguage().addRow(AbstractTableRowData.STATUS_INSERTED);
    rowId.setLanguage(ServerSession.get().getLanguageUid());
    String translation = "TRANSLATION" + System.currentTimeMillis();
    rowId.setTranslation(translation);
    svc.createCodeBox(formData.getMainBox());

    RtUc find = JPA.find(RtUc.class, startblock.getId());
    assertFalse("updated", find.getActive());

    RtUclKey key = new RtUclKey();
    key.setClientNr(ServerSession.get().getSessionClientNr());
    key.setLanguageUid(ServerSession.get().getLanguageUid());
    key.setUcUid(startblock.getId().getId());
    RtUcl findLang = JPA.find(RtUcl.class, key);
    assertNotNull("lang created", findLang);
    assertEquals("translation", translation, findLang.getCodeName());

    JPA.remove(findLang);
    JPA.remove(startblock);
  }

}
