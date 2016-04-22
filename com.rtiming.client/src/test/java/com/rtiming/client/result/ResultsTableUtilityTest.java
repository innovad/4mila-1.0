package com.rtiming.client.result;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import com.rtiming.client.ClientSession;
import com.rtiming.shared.results.ResultRowData;

/**
 * @author am
 */
@RunWith(ClientTestRunner.class)
public class ResultsTableUtilityTest {

  @Test
  public void testConvert() throws Exception {
    ClientSession mock = Mockito.mock(ClientSession.class);
    Mockito.when(mock.getSessionClientNr()).thenReturn(1L);
    // TODO MIG ClientSessionThreadLocal.set(mock);
    AbstractResultsTable table = new ResultsTablePage(0L, 0L, 0L, 0L).getTable();
    List<ResultRowData> list = new ArrayList<>();
    list.add(new ResultRowData());
    Object[][] result = ResultsTableUtility.convertListToObjectArray(list, table);

    Assert.assertEquals("Size", 1, result.length);
    Field[] fields = ResultRowData.class.getDeclaredFields();
    Assert.assertEquals("Size", fields.length - 3, result[0].length);
  }

}
