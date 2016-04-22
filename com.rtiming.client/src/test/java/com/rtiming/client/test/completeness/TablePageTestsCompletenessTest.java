package com.rtiming.client.test.completeness;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.junit.runner.RunWith;

/**
 *
 */
@RunWith(ClientTestRunner.class)
public class TablePageTestsCompletenessTest extends AbstractTablePageTestsCompletenessTest {

  @Override
  public List<Class<?>> getExclusionList() {
    List<Class<?>> list = new ArrayList<Class<?>>();
    return list;
  }

}
