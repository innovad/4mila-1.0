package com.rtiming.client.map;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.basic.filechooser.IFileChooser;
import org.eclipse.scout.rt.client.ui.desktop.DesktopEvent;
import org.eclipse.scout.rt.client.ui.desktop.DesktopListener;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.eclipse.scout.testing.client.ScoutClientAssert;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.ClientSession;
import com.rtiming.client.test.AbstractFormTest;
import com.rtiming.client.test.data.MapTestDataProvider;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dao.RtMapKey;
import com.rtiming.shared.map.IGoogleEarthService;
import com.rtiming.shared.map.IMapProcessService;
import com.rtiming.shared.map.MapFormData;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class MapFormTest extends AbstractFormTest<MapForm> {

  @Override
  protected MapForm getStartedForm() throws ProcessingException {
    MapForm form = new MapForm();
    form.startNew();
    return form;
  }

  @Override
  protected MapForm getModifyForm() throws ProcessingException {
    MapForm form = new MapForm();
    form.setMapKey(getForm().getMapKey());
    form.startModify();
    return form;
  }

  @Override
  public void cleanup() throws ProcessingException {
    BEANS.get(IMapProcessService.class).delete(getForm().getMapKey());
  }

  @Override
  protected List<String> getFieldIdsToIgnore() {
    List<String> list = new ArrayList<String>();
    list.add(getForm().getNECornerBox().getXField().getFieldId());
    list.add(getForm().getNECornerBox().getYField().getFieldId());
    list.add(getForm().getSECornerBox().getXField().getFieldId());
    list.add(getForm().getSECornerBox().getYField().getFieldId());
    list.add(getForm().getSWCornerBox().getXField().getFieldId());
    list.add(getForm().getSWCornerBox().getYField().getFieldId());
    list.add(getForm().getNWCornerBox().getXField().getFieldId());
    list.add(getForm().getNWCornerBox().getYField().getFieldId());
    return list;
  }

  @Test
  public void testNewGoogleEarthLink() throws ProcessingException {
    MapForm map = new MapForm();
    map.startNew();

    Assert.assertNull(map.getNECornerBox().getXField().getValue());
    Assert.assertNull(map.getNECornerBox().getYField().getValue());
    Assert.assertNull(map.getSECornerBox().getXField().getValue());
    Assert.assertNull(map.getSECornerBox().getYField().getValue());
    Assert.assertNull(map.getSWCornerBox().getXField().getValue());
    Assert.assertNull(map.getSWCornerBox().getYField().getValue());
    Assert.assertNull(map.getNWCornerBox().getXField().getValue());
    Assert.assertNull(map.getNWCornerBox().getYField().getValue());

    Assert.assertFalse(map.getOpenWithKMLButton().isEnabled());
    Assert.assertFalse(map.getGoogleMapsButton().isEnabled());

    map.getSWCornerBox().getXField().setValue(7.77777);
    Assert.assertFalse(map.getOpenWithKMLButton().isEnabled());
    Assert.assertFalse(map.getGoogleMapsButton().isEnabled());

    map.getSWCornerBox().getXField().setValue(null);
    Assert.assertFalse(map.getOpenWithKMLButton().isEnabled());
    Assert.assertFalse(map.getGoogleMapsButton().isEnabled());

    map.getNECornerBox().getXField().setValue(6.66666);
    map.getNECornerBox().getYField().setValue(6.66666);
    map.getSECornerBox().getXField().setValue(6.66666);
    map.getSECornerBox().getYField().setValue(6.66666);
    map.getSWCornerBox().getXField().setValue(6.66666);
    map.getSWCornerBox().getYField().setValue(6.66666);
    map.getNWCornerBox().getXField().setValue(6.66666);
    map.getNWCornerBox().getYField().setValue(6.66666);

    Assert.assertTrue(map.getOpenWithKMLButton().isEnabled());
    Assert.assertTrue(map.getGoogleMapsButton().isEnabled());

    map.getSWCornerBox().getXField().setValue(null);
    Assert.assertFalse(map.getOpenWithKMLButton().isEnabled());
    Assert.assertFalse(map.getGoogleMapsButton().isEnabled());
  }

  @Test
  public void testLinksInitially() throws Exception {
    MapTestDataProvider map = new MapTestDataProvider();
    map.getForm().startModify();
    ScoutClientAssert.assertDisabled(map.getForm().getMapField().getViewButton());
    ScoutClientAssert.assertEnabled(map.getForm().getMapField().getPasteFromClipboardButton());
    ScoutClientAssert.assertEnabled(map.getForm().getMapField().getFileChooserButton());
  }

  @Test
  public void testLinksAfterFileChooser() throws Exception {
    URL url = FMilaUtility.findFileLocation("resources//map//BireggwaldSmall.kml", "");
    URL mapUrl = FMilaUtility.findFileLocation("resources//map//map.jpg", "");

    final File file = new File(url.getPath());
    Assert.assertTrue("File must exist", file.exists());
    File mapFile = new File(mapUrl.getPath());
    Assert.assertTrue("Map must exist", mapFile.exists());

    DesktopListener listener = new DesktopListener() {
      @Override
      public void desktopChanged(DesktopEvent e) {
        if (e.getType() == DesktopEvent.TYPE_FILE_CHOOSER_SHOW) {
          IFileChooser chooser = e.getFileChooser();
          chooser.setFiles(Arrays.asList(new BinaryResource[]{FMilaUtility.createBinaryResource(file.getAbsolutePath())}));
        }
      }
    };
    ClientSession.get().getDesktop().addDesktopListener(listener);

    MapForm map = getForm();
    map.getImportKMLButton().doClick();

    ClientSession.get().getDesktop().removeDesktopListener(listener);

    ScoutClientAssert.assertEnabled(map.getMapField().getViewButton());
    ScoutClientAssert.assertEnabled(map.getMapField().getPasteFromClipboardButton());
    ScoutClientAssert.assertEnabled(map.getMapField().getFileChooserButton());
  }

  @Test
  public void testModifyGoogleEarhLink() throws ProcessingException {
    MapTestDataProvider map = new MapTestDataProvider();
    map.getForm().startModify();

    map.getForm().getNECornerBox().getXField().setValue(6.66666);
    map.getForm().getNECornerBox().getYField().setValue(6.66666);
    map.getForm().getSECornerBox().getXField().setValue(6.66666);
    map.getForm().getSECornerBox().getYField().setValue(6.66666);
    map.getForm().getSWCornerBox().getXField().setValue(6.66666);
    map.getForm().getSWCornerBox().getYField().setValue(6.66666);
    map.getForm().getNWCornerBox().getXField().setValue(6.66666);
    map.getForm().getNWCornerBox().getYField().setValue(6.66666);

    Assert.assertTrue(map.getForm().getOpenWithKMLButton().isEnabled());
    Assert.assertTrue(map.getForm().getGoogleMapsButton().isEnabled());

    map.getForm().doOk();

    MapForm form = new MapForm();
    form.setMapKey(RtMapKey.create(map.getMapNr()));
    form.startModify();

    Assert.assertTrue(form.getOpenWithKMLButton().isEnabled());
    Assert.assertTrue(form.getGoogleMapsButton().isEnabled());

    map.remove();
  }

  @Test
  public void testKmlImportNameAndPosition() throws Exception {
    URL file = FMilaUtility.findFileLocation("resources//map//BireggwaldSmall.kml", "");
    byte[] kml = IOUtility.getContent(file.getPath());
    MapFormData map = BEANS.get(IGoogleEarthService.class).createMapFromKml(kml);

    Assert.assertNotNull(map);
    Assert.assertEquals("BireggwaldOverlay ÄÖÜ", map.getName().getValue());

    Assert.assertEquals(8.303963774812175, map.getSWCornerBox().getX().getValue().doubleValue(), 0.000000000000001);
    Assert.assertEquals(47.02387030047724, map.getSWCornerBox().getY().getValue().doubleValue(), 0.000000000000001);

    Assert.assertEquals(8.334002331248216, map.getSECornerBox().getX().getValue().doubleValue(), 0.000000000000001);
    Assert.assertEquals(47.02315821956876, map.getSECornerBox().getY().getValue().doubleValue(), 0.000000000000001);

    Assert.assertEquals(8.333212556275935, map.getNECornerBox().getX().getValue().doubleValue(), 0.000000000000001);
    Assert.assertEquals(47.0372731068498, map.getNECornerBox().getY().getValue().doubleValue(), 0.000000000000001);

    Assert.assertEquals(8.304340737297363, map.getNWCornerBox().getX().getValue().doubleValue(), 0.000000000000001);
    Assert.assertEquals(47.03711682404286, map.getNWCornerBox().getY().getValue().doubleValue(), 0.000000000000001);

    Assert.assertEquals("map.jpg", map.getFormat());
  }

  @Test
  public void testImportKmlButton() throws Exception {
    URL url = FMilaUtility.findFileLocation("resources//map//BireggwaldSmall.kml", "");
    URL mapUrl = FMilaUtility.findFileLocation("resources//map//map.jpg", "");

    final File file = new File(url.getPath());
    Assert.assertTrue("File must exist", file.exists());

    DesktopListener listener = new DesktopListener() {
      @Override
      public void desktopChanged(DesktopEvent e) {
        if (e.getType() == DesktopEvent.TYPE_FILE_CHOOSER_SHOW) {
          IFileChooser chooser = e.getFileChooser();
          Assert.assertNotNull("There must be a file chooser", chooser);
          chooser.setFiles(Arrays.asList(new BinaryResource[]{FMilaUtility.createBinaryResource(file.getAbsolutePath())}));
        }
      }
    };
    ClientSession.get().getDesktop().addDesktopListener(listener);

    MapForm map = getForm();
    map.getImportKMLButton().doClick();

    ClientSession.get().getDesktop().removeDesktopListener(listener);

    assertMapData(map);

    byte[] original = IOUtility.getContent(mapUrl.getPath());
    Assert.assertEquals(original.length, ((byte[]) map.getMapField().getImageField().getImage()).length);
  }

  @Test
  public void testImportKmzButton() throws Exception {
    URL url = FMilaUtility.findFileLocation("resources//map//BireggwaldSmall.kmz", "");
    URL mapUrl = FMilaUtility.findFileLocation("resources//map//map.jpg", "");

    final File file = new File(url.getPath());
    Assert.assertTrue("File must exist", file.exists());

    DesktopListener listener = new DesktopListener() {
      @Override
      public void desktopChanged(DesktopEvent e) {
        if (e.getType() == DesktopEvent.TYPE_FILE_CHOOSER_SHOW) {
          IFileChooser chooser = e.getFileChooser();
          Assert.assertNotNull("There must be a file chooser", chooser);
          chooser.setFiles(Arrays.asList(new BinaryResource[]{FMilaUtility.createBinaryResource(file.getAbsolutePath())}));
        }
      }
    };
    ClientSession.get().getDesktop().addDesktopListener(listener);

    MapForm map = getForm();
    map.getImportKMLButton().doClick();

    ClientSession.get().getDesktop().removeDesktopListener(listener);

    assertMapData(map);

    byte[] original = IOUtility.getContent(mapUrl.getPath());
    Assert.assertEquals(original.length, ((byte[]) map.getMapField().getImageField().getImage()).length);
  }

  @Test
  public void testNewMapViewButton() throws Exception {
    MapForm map = new MapForm();
    map.startNew();
    ScoutClientAssert.assertDisabled(map.getMapField().getViewButton());
  }

  private void assertMapData(MapForm map) {
    Assert.assertNotNull(map);
    Assert.assertEquals("BireggwaldOverlay ÄÖÜ", map.getNameField().getValue());

    Assert.assertEquals(8.303963774812175, map.getSWCornerBox().getXField().getValue().doubleValue(), 0.000000000000001);
    Assert.assertEquals(47.02387030047724, map.getSWCornerBox().getYField().getValue().doubleValue(), 0.000000000000001);

    Assert.assertEquals(8.334002331248216, map.getSECornerBox().getXField().getValue().doubleValue(), 0.000000000000001);
    Assert.assertEquals(47.02315821956876, map.getSECornerBox().getYField().getValue().doubleValue(), 0.000000000000001);

    Assert.assertEquals(8.333212556275935, map.getNECornerBox().getXField().getValue().doubleValue(), 0.000000000000001);
    Assert.assertEquals(47.0372731068498, map.getNECornerBox().getYField().getValue().doubleValue(), 0.000000000000001);

    Assert.assertEquals(8.304340737297363, map.getNWCornerBox().getXField().getValue().doubleValue(), 0.000000000000001);
    Assert.assertEquals(47.03711682404286, map.getNWCornerBox().getYField().getValue().doubleValue(), 0.000000000000001);

    Assert.assertEquals("jpg", map.getFormat());
  }

}
