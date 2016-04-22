package com.rtiming.server.map;

import java.net.URL;

import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.rt.testing.server.runner.RunWithServerSession;
import org.eclipse.scout.rt.testing.server.runner.ServerTestRunner;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.map.MapFormData;

@RunWith(ServerTestRunner.class)
@RunWithSubject("admin")
@RunWithServerSession(ServerSession.class)
public class GoogleEarthServiceTest {

  @Rule
  public ExpectedException expectedEx = ExpectedException.none();

  @Test(expected = IllegalArgumentException.class)
  public void testCreateMapFromKml1() throws Exception {
    GoogleEarthService svc = new GoogleEarthService();
    svc.createMapFromKml(null);
  }

  @Test
  public void testCreateMapFromKml2() throws Exception {
    expectedEx.expect(VetoException.class);
    expectedEx.expectMessage(TEXTS.get("KML_KMZ_ImportGroundOverlayMissing"));

    GoogleEarthService svc = new GoogleEarthService();

    URL file = FMilaUtility.findFileLocation("resources//map//nogroundoverlay.kml", "");
    byte[] kml = IOUtility.getContent(file.getPath());
    svc.createMapFromKml(kml);
  }

  @Test
  public void testCreateMapFromKml3() throws Exception {
    GoogleEarthService svc = new GoogleEarthService();

    URL file = FMilaUtility.findFileLocation("resources//map//BireggwaldSmall.kml", "");
    byte[] kml = IOUtility.getContent(file.getPath());
    MapFormData formData = svc.createMapFromKml(kml);

    Assert.assertEquals("Format contains filename", "map.jpg", formData.getFormat());
  }

  @Test
  public void testCreateMapFromKml4() throws Exception {
    expectedEx.expect(VetoException.class);
    expectedEx.expectMessage(TEXTS.get("KML_KMZ_ImportNoCoordinates"));

    GoogleEarthService svc = new GoogleEarthService();

    URL file = FMilaUtility.findFileLocation("resources//map//BireggwaldNoCoordinates.kml", "");
    byte[] kml = IOUtility.getContent(file.getPath());
    svc.createMapFromKml(kml);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testCreateKmlFromMap1() throws Exception {
    GoogleEarthService svc = new GoogleEarthService();
    MapFormData formData = new MapFormData();
    svc.createKmlFromMap(formData, "TESTURL");
  }

  @Test
  public void testCreateKmlFromMap2() throws Exception {
    GoogleEarthService svc = new GoogleEarthService();
    MapFormData formData = new MapFormData();
    // longitude
    formData.getSWCornerBox().getX().setValue(1d);
    formData.getSECornerBox().getX().setValue(2d);
    formData.getNECornerBox().getX().setValue(3d);
    formData.getNWCornerBox().getX().setValue(4d);

    // latitude
    formData.getSWCornerBox().getY().setValue(5d);
    formData.getSECornerBox().getY().setValue(6d);
    formData.getNECornerBox().getY().setValue(7d);
    formData.getNWCornerBox().getY().setValue(8d);
    String kml = svc.createKmlFromMap(formData, "TESTURL");

    Assert.assertNotNull("Kml", kml);
    Assert.assertTrue("Coordinates", kml.contains("<coordinates>1.0,5.0 2.0,6.0 3.0,7.0 4.0,8.0</coordinates>"));
    Assert.assertTrue("URL", kml.contains("<href>TESTURL</href>"));
  }

}
