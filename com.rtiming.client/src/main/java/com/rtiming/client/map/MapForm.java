package com.rtiming.client.map;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import org.eclipse.scout.commons.IOUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.FormData;
import org.eclipse.scout.commons.annotations.FormData.SdkCommand;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.client.ui.basic.filechooser.FileChooser;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.bigdecimalfield.AbstractBigDecimalField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.integerfield.AbstractIntegerField;
import org.eclipse.scout.rt.client.ui.form.fields.longfield.AbstractLongField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.client.ui.form.fields.tabbox.AbstractTabBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.shared.ScoutTexts;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractGoogleMapsButton;
import com.rtiming.client.common.ui.fields.AbstractImageGroupBox;
import com.rtiming.client.common.ui.fields.AbstractPositionBox;
import com.rtiming.client.map.MapForm.MainBox.CancelButton;
import com.rtiming.client.map.MapForm.MainBox.GoogleMapsButton;
import com.rtiming.client.map.MapForm.MainBox.ImportKMLButton;
import com.rtiming.client.map.MapForm.MainBox.MapField;
import com.rtiming.client.map.MapForm.MainBox.NameField;
import com.rtiming.client.map.MapForm.MainBox.OkButton;
import com.rtiming.client.map.MapForm.MainBox.OpenWithKMLButton;
import com.rtiming.client.map.MapForm.MainBox.ScaleField;
import com.rtiming.client.map.MapForm.MainBox.TabBox;
import com.rtiming.client.map.MapForm.MainBox.TabBox.CoordinateSystemBox;
import com.rtiming.client.map.MapForm.MainBox.TabBox.CoordinateSystemBox.LeftUpperCornerBox;
import com.rtiming.client.map.MapForm.MainBox.TabBox.CoordinateSystemBox.LeftUpperCornerBox.OriginXField;
import com.rtiming.client.map.MapForm.MainBox.TabBox.CoordinateSystemBox.LeftUpperCornerBox.OriginYField;
import com.rtiming.client.map.MapForm.MainBox.TabBox.CoordinateSystemBox.ResolutionField;
import com.rtiming.client.map.MapForm.MainBox.TabBox.CoordinateSystemBox.SizeBox;
import com.rtiming.client.map.MapForm.MainBox.TabBox.CoordinateSystemBox.SizeBox.HeightField;
import com.rtiming.client.map.MapForm.MainBox.TabBox.CoordinateSystemBox.SizeBox.WidthField;
import com.rtiming.client.map.MapForm.MainBox.TabBox.LongitudeLatitudeBox;
import com.rtiming.client.map.MapForm.MainBox.TabBox.LongitudeLatitudeBox.NECornerBox;
import com.rtiming.client.map.MapForm.MainBox.TabBox.LongitudeLatitudeBox.NWCornerBox;
import com.rtiming.client.map.MapForm.MainBox.TabBox.LongitudeLatitudeBox.SECornerBox;
import com.rtiming.client.map.MapForm.MainBox.TabBox.LongitudeLatitudeBox.SWCornerBox;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.security.permission.UpdateMapPermission;
import com.rtiming.shared.dao.RtMapKey;
import com.rtiming.shared.map.IGoogleEarthService;
import com.rtiming.shared.map.IMapProcessService;
import com.rtiming.shared.map.MapFormData;

@FormData(value = MapFormData.class, sdkCommand = SdkCommand.CREATE)
public class MapForm extends AbstractForm {

  private RtMapKey mapKey;
  private Long m_newEventNr;
  private byte[] m_mapData;
  private String m_format;
  private Date m_evtFileLastUpdate;
  private boolean m_fileChanged;

  public MapForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return Texts.get("Map");
  }

  @Override
  protected void execFormActivated() throws ProcessingException {
    getGoogleMapsButton().setEnabled(getNWCornerBox().getXField().getValue() != null && getNWCornerBox().getYField().getValue() != null);
    getOpenWithKMLButton().setEnabled(getNWCornerBox().getXField().getValue() != null && getNWCornerBox().getYField().getValue() != null);
  }

  @FormData
  public String getFormat() {
    return m_format;
  }

  @FormData
  public void setFormat(String format) {
    m_format = format;
  }

  @FormData
  public byte[] getMapData() {
    return m_mapData;
  }

  @FormData
  public void setMapData(byte[] mapData) {
    m_mapData = mapData;
  }

  @FormData
  public RtMapKey getMapKey() {
    return mapKey;
  }

  @FormData
  public void setMapKey(RtMapKey mapKey) {
    this.mapKey = mapKey;
  }

  @FormData
  public Long getNewEventNr() {
    return m_newEventNr;
  }

  @FormData
  public void setNewEventNr(Long newEventNr) {
    m_newEventNr = newEventNr;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public HeightField getHeightField() {
    return getFieldByClass(HeightField.class);
  }

  public ImportKMLButton getImportKMLButton() {
    return getFieldByClass(ImportKMLButton.class);
  }

  public OpenWithKMLButton getOpenWithKMLButton() {
    return getFieldByClass(OpenWithKMLButton.class);
  }

  public ResolutionField getResolutionField() {
    return getFieldByClass(ResolutionField.class);
  }

  public SECornerBox getSECornerBox() {
    return getFieldByClass(SECornerBox.class);
  }

  public MapField getMapField() {
    return getFieldByClass(MapField.class);
  }

  public SizeBox getSizeBox() {
    return getFieldByClass(SizeBox.class);
  }

  public TabBox getTabBox() {
    return getFieldByClass(TabBox.class);
  }

  public LeftUpperCornerBox getLeftUpperCornerBox() {
    return getFieldByClass(LeftUpperCornerBox.class);
  }

  public LongitudeLatitudeBox getLongitudeLatitudeBox() {
    return getFieldByClass(LongitudeLatitudeBox.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NECornerBox getNECornerBox() {
    return getFieldByClass(NECornerBox.class);
  }

  public NWCornerBox getNWCornerBox() {
    return getFieldByClass(NWCornerBox.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public SWCornerBox getSWCornerBox() {
    return getFieldByClass(SWCornerBox.class);
  }

  public ScaleField getScaleField() {
    return getFieldByClass(ScaleField.class);
  }

  public CoordinateSystemBox getCoordinateSystemBox() {
    return getFieldByClass(CoordinateSystemBox.class);
  }

  public GoogleMapsButton getGoogleMapsButton() {
    return getFieldByClass(GoogleMapsButton.class);
  }

  public WidthField getWidthField() {
    return getFieldByClass(WidthField.class);
  }

  public OriginXField getOriginXField() {
    return getFieldByClass(OriginXField.class);
  }

  public OriginYField getOriginYField() {
    return getFieldByClass(OriginYField.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class NameField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return ScoutTexts.get("Name");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }
    }

    @Order(20.0)
    public class ScaleField extends AbstractLongField {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("Scale");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected Long getConfiguredMaxValue() {
        return 1000000L;
      }

      @Override
      protected Long getConfiguredMinValue() {
        return 0L;
      }
    }

    @Order(30.0)
    public class MapField extends AbstractImageGroupBox {

      @Override
      protected String getImageFormat() {
        return MapForm.this.getFormat();
      }

      @Override
      protected byte[] getImageData() {
        return MapForm.this.getMapData();
      }

      @Override
      protected void setImageFormat(String format) {
        MapForm.this.setFormat(format);
      }

      @Override
      protected void updateFileChanged(boolean fileHasChanged) {
        setFileChanged(fileHasChanged);
      }

    }

    @Order(80.0)
    public class TabBox extends AbstractTabBox {

      @Order(10.0)
      public class LongitudeLatitudeBox extends AbstractGroupBox {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("LongitudeLatitude");
        }

        public void updateLinkButtons() {
          boolean empty = getNECornerBox().isFieldEmpty() || getNWCornerBox().isFieldEmpty() || getSECornerBox().isFieldEmpty() || getSWCornerBox().isFieldEmpty();
          getOpenWithKMLButton().setEnabled(!empty);
          getGoogleMapsButton().setEnabled(!empty);
        }

        @Order(10.0)
        @FormData(value = MapFormData.class, sdkCommand = SdkCommand.CREATE)
        public class NWCornerBox extends AbstractPositionBox {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("NWCorner");
          }

          @Override
          public void handleExecChangedValue() {
            updateLinkButtons();
          }

        }

        @Order(20.0)
        @FormData(value = MapFormData.class, sdkCommand = SdkCommand.CREATE)
        public class NECornerBox extends AbstractPositionBox {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("NECorner");
          }

          @Override
          public void handleExecChangedValue() {
            updateLinkButtons();
          }

        }

        @Order(30.0)
        @FormData(value = MapFormData.class, sdkCommand = SdkCommand.CREATE)
        public class SECornerBox extends AbstractPositionBox {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("SECorner");
          }

          @Override
          public void handleExecChangedValue() {
            updateLinkButtons();
          }

        }

        @Order(40.0)
        @FormData(value = MapFormData.class, sdkCommand = SdkCommand.CREATE)
        public class SWCornerBox extends AbstractPositionBox {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("SWCorner");
          }

          @Override
          public void handleExecChangedValue() {
            updateLinkButtons();
          }

        }
      }

      @Order(20.0)
      public class CoordinateSystemBox extends AbstractGroupBox {

        @Override
        protected String getConfiguredLabel() {
          return Texts.get("CoordinateSystem");
        }

        @Order(10.0)
        public class LeftUpperCornerBox extends AbstractGroupBox {

          @Override
          protected boolean getConfiguredBorderVisible() {
            return false;
          }

          @Order(10.0)
          public class OriginXField extends AbstractBigDecimalField {

            @Override
            protected String getConfiguredLabel() {
              return Texts.get("UpperLeftX");
            }
          }

          @Order(20.0)
          public class OriginYField extends AbstractBigDecimalField {

            @Override
            protected String getConfiguredLabel() {
              return Texts.get("UpperLeftY");
            }
          }
        }

        @Order(20.0)
        public class SizeBox extends AbstractGroupBox {

          @Override
          protected boolean getConfiguredBorderVisible() {
            return false;
          }

          @Order(10.0)
          public class WidthField extends AbstractIntegerField {

            @Override
            protected boolean getConfiguredEnabled() {
              return false;
            }

            @Override
            protected String getConfiguredLabel() {
              return Texts.get("Width");
            }
          }

          @Order(20.0)
          public class HeightField extends AbstractIntegerField {

            @Override
            protected boolean getConfiguredEnabled() {
              return false;
            }

            @Override
            protected String getConfiguredLabel() {
              return Texts.get("Height");
            }
          }
        }

        @Order(30.0)
        public class ResolutionField extends AbstractBigDecimalField {

          @Override
          protected String getConfiguredLabel() {
            return Texts.get("Resolution");
          }
        }
      }

    }

    @Order(88.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(90.0)
    public class ImportKMLButton extends AbstractLinkButton {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("ImportKMZ");
      }

      @Override
      protected void execInitField() throws ProcessingException {
        if (FMilaUtility.isRichClient()) {
          setLabel(Texts.get("ImportKML_KMZ"));
        }
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        FileChooser fileChooser = null;
        // TODO MIG fileChooser.setTypeLoad(true);
        if (FMilaUtility.isRichClient()) {
          fileChooser = new FileChooser(Arrays.asList(new String[]{"kml", "kmz"}));
        }
        else {
          fileChooser = new FileChooser(Arrays.asList(new String[]{"kmz"}));
        }
        List<BinaryResource> files = fileChooser.startChooser();
        if (files != null && files.size() == 1) {
          byte[] kml = null;
          try {
            if (files.get(0).getFilename().endsWith("kml")) {
              // read kml
              kml = IOUtility.getContent(files.get(0).getFilename());
              // TODO MIG readKml(files[0].getParent(), kml);
            }
            else {
              // unfortunately the library cannot read included images
              // read images with unzip
              // unzip kmz and read files
              ZipFile zip = new ZipFile(files.get(0).getFilename());
              Enumeration<? extends ZipEntry> entries = zip.entries();
              while (entries.hasMoreElements()) {
                ZipEntry entry = entries.nextElement();
                if (entry.getName().endsWith("kml")) {
                  // extract kml
                  ByteArrayOutputStream data = readData(zip, entry);
                  MapFormData map = BEANS.get(IGoogleEarthService.class).createMapFromKml(data.toByteArray());
                  importFormData(map);
                }
                else {
                  for (String suffix : FMilaUtility.getSupportedImageFormats()) {
                    if (entry.getName().endsWith(suffix)) {
                      // read first image file
                      ByteArrayOutputStream baos = readData(zip, entry);
                      getMapField().getImageField().setImage(baos.toByteArray(), suffix, true);
                      break;
                    }
                  }
                }
              }
            }
          }
          catch (VetoException e) {
            throw e;
          }
          catch (ProcessingException | IOException e) {
            throw new ProcessingException("Error reading " + files.get(0).getFilename() + ", Message: " + e.getMessage());
          }
        }
      }

      private ByteArrayOutputStream readData(ZipFile zip, ZipEntry entry) throws IOException {
        InputStream is = zip.getInputStream(entry);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int data;
        while ((data = is.read()) != -1) {
          baos.write(data);
        }
        is.close();
        return baos;
      }

      private void readKml(String dir, byte[] kml) throws ProcessingException {
        boolean fileImported = false;
        if (kml != null) {
          MapFormData map = BEANS.get(IGoogleEarthService.class).createMapFromKml(kml);
          importFormData(map);
          if (!StringUtility.isNullOrEmpty(map.getFormat())) {
            // try absolute URL
            File file = null;
            try {
              file = new File(map.getFormat().replace("file:///", ""));
              if (file.exists()) {
                getMapField().getImageField().insertFile(file);
                setFormat(getMapField().getImageFormat());
                fileImported = true;
              }
              else {
                file = null;
              }
            }
            catch (Exception e) {
              file = null;
            }
            if (file == null) {
              // try relative URL
              file = new File(dir, map.getFormat());
              if (file.exists()) {
                getMapField().getImageField().insertFile(file);
                setFormat(getMapField().getImageFormat());
                fileImported = true;
              }
            }
          }
        }
        if (!fileImported) {
          getMapField().setImageFormat(null);
          getMapField().getImageField().setImage(null);
          throw new VetoException(Texts.get("FileReadException"));
        }
      }
    }

    @Order(100.0)
    public class OpenWithKMLButton extends AbstractLinkButton {

      @Override
      protected String getConfiguredLabel() {
        return Texts.get("OpenWithKML");
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        MapFormData formData = new MapFormData();
        exportFormData(formData);
        File viewer = IOUtility.createTempFile(IOUtility.getTempFileName("." + getMapField().getImageFormat()), getMapField().getImageField().getByteArrayValue());
        String kml = BEANS.get(IGoogleEarthService.class).createKmlFromMap(formData, "file:///" + viewer.getAbsolutePath());

        try {
          String fileName = IOUtility.getTempFileName(".kml");
          File file = new File(fileName);
          IOUtility.writeContent(new FileWriter(file), kml, true);
          FMilaClientUtility.openDocument(file.getAbsolutePath());
        }
        catch (Exception e) {
          throw new ProcessingException(e.getMessage());
        }
      }
    }

    @Order(110.0)
    public class GoogleMapsButton extends AbstractGoogleMapsButton {

      @Override
      protected String getNumber() {
        return getNameField().getValue();
      }

      @Override
      protected Double getX() {
        if (getNWCornerBox().getXField().getValue() != null) {
          return getNWCornerBox().getXField().getValue().doubleValue();
        }
        return null;
      }

      @Override
      protected Double getY() {
        if (getNWCornerBox().getYField().getValue() != null) {
          return getNWCornerBox().getYField().getValue().doubleValue();
        }
        return null;
      }
    }

    @Order(120.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(130.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IMapProcessService service = BEANS.get(IMapProcessService.class);
      MapFormData formData = new MapFormData();
      exportFormData(formData);
      formData = BeanUtility.mapBean2FormData(service.load(formData.getMapKey()));
      importFormData(formData);
      getMapField().getImageField().setImage(formData.getMapData(), formData.getFormat(), false);
      setEnabledPermission(new UpdateMapPermission());

      init();
    }

    @Override
    public void execStore() throws ProcessingException {
      IMapProcessService service = BEANS.get(IMapProcessService.class);
      MapFormData formData = new MapFormData();
      exportFormData(formData);
      formData.setMapData(getMapField().getImageField().getByteArrayValue());
      formData = BeanUtility.mapBean2FormData(service.store(BeanUtility.mapFormData2bean(formData)));
    }
  }

  private void init() {
    getLongitudeLatitudeBox().updateLinkButtons();
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IMapProcessService service = BEANS.get(IMapProcessService.class);
      MapFormData formData = new MapFormData();
      exportFormData(formData);
      formData = BeanUtility.mapBean2FormData(service.prepareCreate(BeanUtility.mapFormData2bean(formData)));
      importFormData(formData);

      init();
    }

    @Override
    public void execStore() throws ProcessingException {
      IMapProcessService service = BEANS.get(IMapProcessService.class);
      MapFormData formData = new MapFormData();
      exportFormData(formData);
      formData.setMapData(getMapField().getImageField().getByteArrayValue());
      formData = BeanUtility.mapBean2FormData(service.create(BeanUtility.mapFormData2bean(formData)));
      importFormData(formData);
    }
  }

  @FormData
  public Date getEvtFileLastUpdate() {
    return m_evtFileLastUpdate;
  }

  @FormData
  public void setEvtFileLastUpdate(Date evtMapFileLastChanged) {
    m_evtFileLastUpdate = evtMapFileLastChanged;
  }

  @FormData
  public boolean isFileChanged() {
    return m_fileChanged;
  }

  @FormData
  public void setFileChanged(boolean isFileChanged) {
    m_fileChanged = isFileChanged;
  }
}
