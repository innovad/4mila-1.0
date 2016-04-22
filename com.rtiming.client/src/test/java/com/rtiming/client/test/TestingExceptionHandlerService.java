package com.rtiming.client.test;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class TestingExceptionHandlerService /* implements IExceptionHandlerService */ { // TODO MIG
  private ProcessingException m_lastException;
  private static final Logger LOG = LoggerFactory.getLogger(TestingExceptionHandlerService.class);

//  @Override
//  public void handleException(ProcessingException t) {
//    if (t != null) {
//      LOG.warn("consuming: " + t.toString());
//    }
//    m_lastException = t;
//  }
//
//  public void assertNoException() throws ProcessingException {
//    if (m_lastException == null) {
//      return;
//    }
//
//    if (m_lastException.getCause() instanceof AssertionError) {
//      throw (AssertionError) m_lastException.getCause();
//    }
//    else if (m_lastException.getCause() instanceof WrappedProcessingRuntimeException) {
//      WrappedProcessingRuntimeException ex = (WrappedProcessingRuntimeException) m_lastException.getCause();
//      if (ex.getCause() != null && ex.getCause() instanceof ProcessingException) {
//        throw (ProcessingException) ex.getCause();
//      }
//    }
//
//    throw m_lastException;
//  }

  public void clear() {
    m_lastException = null;
  }
}
