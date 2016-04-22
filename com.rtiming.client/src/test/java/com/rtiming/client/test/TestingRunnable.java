package com.rtiming.client.test;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Runnable used for testing blocking conditions.
 */
public abstract class TestingRunnable implements Runnable {

  private static final Logger LOG = LoggerFactory.getLogger(TestingRunnable.class);

  protected abstract void runTest() throws ProcessingException;

  @Override
  public final void run() {
    try {
      runTest();
    }
    catch (ProcessingException e) {
      // TODO MIG BEANS.get(IExceptionHandlerService.class).handleException(e);
    }
    finally {
      try {
        FormTestUtility.closeAllBlockingForms();
      }
      catch (ProcessingException e1) {
        LOG.error("Could not close forms", e1);
        Assert.fail("Could not close forms, " + e1.getMessage());
      }
    }
  }

}
