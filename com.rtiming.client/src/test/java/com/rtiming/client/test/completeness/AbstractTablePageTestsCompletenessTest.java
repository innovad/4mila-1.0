package com.rtiming.client.test.completeness;

import java.util.List;

import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPageWithTable;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.shared.test.completeness.CompletenessResult;
import com.rtiming.shared.test.completeness.TestCompletenessUtility;

/**
 * Implement this test class to have all {@link IPageWithTable} checked if there exists a test extending
 * {@link AbstractTablePageTest} (and vice-versa).
 * This test class must be implented for each test fragment/bundle to be checked.
 */
@RunWith(ClientTestRunner.class)
public abstract class AbstractTablePageTestsCompletenessTest {

  @Test
  public void testCompleteness() throws Exception {
    CompletenessResult result = TestCompletenessUtility.testCompleteness(IPageWithTable.class, AbstractTablePageTest.class, "TablePage", "Test", "table page", getExclusionList());

    if (result != null) {
      Assert.fail(result.getMessage());
    }
  }

  /**
   * @return a list of classes/test classes that will be excluded from completeness checking. This may be used if there
   *         is a very special case where a successful standard test cannot be implemented.
   */
  public abstract List<Class<?>> getExclusionList();

}
