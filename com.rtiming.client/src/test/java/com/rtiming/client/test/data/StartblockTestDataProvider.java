package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.client.entry.startblock.StartblockForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.shared.entry.startblock.IStartblockProcessService;
import com.rtiming.shared.entry.startblock.StartblockFormData;
import com.rtiming.shared.settings.ICodeProcessService;

public class StartblockTestDataProvider extends AbstractTestDataProvider<StartblockForm> {

  public StartblockTestDataProvider() throws ProcessingException {
    callInitializer();
  }

  @Override
  protected StartblockForm createForm() throws ProcessingException {
    StartblockForm startblock = new StartblockForm();
    startblock.startNew();
    FormTestUtility.fillFormFields(startblock);
    startblock.doOk();
    return startblock;
  }

  @Override
  public void remove() throws ProcessingException {
    StartblockFormData startblock = new StartblockFormData();
    startblock.setStartblockUid(getStartblockUid());
    startblock = BEANS.get(IStartblockProcessService.class).load(startblock);
    BEANS.get(ICodeProcessService.class).deleteCodeBox(startblock.getCodeBox());
  }

  public Long getStartblockUid() throws ProcessingException {
    return getForm().getStartblockUid();
  }

}
