package com.rtiming.client.entry;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.shared.settings.IDefaultProcessService;

/**
 * @author amo
 */
@RunWith(ClientTestRunner.class)
public class EntryFormNewTest {

  @BeforeClass
  public static void beforeClass() throws ProcessingException {
    BEANS.get(IDefaultProcessService.class).setDefaultEventNr(null);
  }

  @Test
  public void testNewWithoutDefaultEvent() throws Exception {
    EntryForm form = new EntryForm();
    form.startNew();
  }

}
