package com.rtiming.client.event.course;

import java.net.URL;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.map.EventMapForm;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.data.EventWithIndividualValidatedRaceTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.IControlProcessService;
import com.rtiming.shared.map.IGoogleEarthService;
import com.rtiming.shared.map.IMapProcessService;
import com.rtiming.shared.map.MapFormData;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class ControlsTablePageTest extends AbstractTablePageTest<ControlsTablePage> {

  private EventWithIndividualValidatedRaceTestDataProvider event;

  @Override
  protected ControlsTablePage getTablePage() throws ProcessingException {
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{}, new String[]{});
    return new ControlsTablePage(event.getEventNr());
  }

  @Test
  public void testGeoReference() throws Exception {
    // create event
    event = new EventWithIndividualValidatedRaceTestDataProvider(new String[]{"31"}, new String[]{});

    // create map
    URL file = FMilaUtility.findFileLocation("resources//map//BireggwaldSmall.kml", "");
    byte[] kml = IOUtility.getContent(file.getPath());
    MapFormData map = BEANS.get(IGoogleEarthService.class).createMapFromKml(kml);
    Assert.assertNotNull(map);
    map.getWidth().setValue(1000);
    map.getHeight().setValue(2000);
    map.getResolution().setValue(300d);
    map.getOriginX().setValue(10000d);
    map.getOriginY().setValue(10000d);
    map.getScale().setValue(10000L);
    map = BeanUtility.mapBean2FormData(BEANS.get(IMapProcessService.class).create(BeanUtility.mapFormData2bean(map)));

    // add map
    EventMapForm eventMap = new EventMapForm();
    eventMap.getEventField().setValue(event.getEventNr());
    eventMap.startNew();
    eventMap.getMapField().setValue(map.getMapKey().getId());
    eventMap.doOk();

    IControlProcessService controlService = BEANS.get(IControlProcessService.class);
    ControlFormData control = controlService.find("31", event.getEventNr());
    control.getPositionX().setValue(100d);
    control.getPositionY().setValue(200d);
    control = controlService.store(control);
    controlService.georeferenceFromLocalPosition(new Long[]{control.getControlNr()}, event.getEventNr());

    control = controlService.load(control);
    Assert.assertNotNull("Global X must be referenced", control.getGlobalX().getValue());
    Assert.assertNotNull("Global Y must be referenced", control.getGlobalY().getValue());
  }

  @After
  public void after() throws ProcessingException {
    if (event != null) {
      event.remove();
    }
  }

}
