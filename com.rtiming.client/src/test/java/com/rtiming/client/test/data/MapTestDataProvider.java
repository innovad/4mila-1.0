package com.rtiming.client.test.data;

import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.junit.Assert;

import com.rtiming.client.map.MapForm;
import com.rtiming.client.test.FormTestUtility;
import com.rtiming.client.test.field.MaxFormFieldValueProvider;
import com.rtiming.shared.map.IMapProcessService;

public class MapTestDataProvider extends AbstractTestDataProvider<MapForm> {

  public MapTestDataProvider() throws ProcessingException {
    callInitializer();
  }

  @Override
  protected MapForm createForm() throws ProcessingException {
    MapForm map = new MapForm();
    FormTestUtility.fillFormFields(map, new MaxFormFieldValueProvider());
    map.startNew();
    map.touch();
    map.doOk();
    Assert.assertNotNull(map.getMapKey().getId());
    return map;
  }

  @Override
  public void remove() throws ProcessingException {
    BEANS.get(IMapProcessService.class).delete(getForm().getMapKey());
  }

  public Long getMapNr() throws ProcessingException {
    return getForm().getMapKey().getId();
  }

}
