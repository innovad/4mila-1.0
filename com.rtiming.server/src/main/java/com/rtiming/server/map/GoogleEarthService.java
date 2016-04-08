package com.rtiming.server.map;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.shared.map.IGoogleEarthService;
import com.rtiming.shared.map.MapFormData;

import de.micromata.opengis.kml.v_2_2_0.AbstractObject;
import de.micromata.opengis.kml.v_2_2_0.Coordinate;
import de.micromata.opengis.kml.v_2_2_0.Feature;
import de.micromata.opengis.kml.v_2_2_0.Folder;
import de.micromata.opengis.kml.v_2_2_0.GroundOverlay;
import de.micromata.opengis.kml.v_2_2_0.Icon;
import de.micromata.opengis.kml.v_2_2_0.Kml;
import de.micromata.opengis.kml.v_2_2_0.KmlFactory;
import de.micromata.opengis.kml.v_2_2_0.LatLonBox;
import de.micromata.opengis.kml.v_2_2_0.gx.LatLonQuad;

public class GoogleEarthService implements IGoogleEarthService {

  @Override
  public String createKmlFromMap(MapFormData formData, String mapUrl) throws ProcessingException {

    final Kml kml = new Kml();

    Folder folder = kml.createAndSetFolder();
    folder.setName(formData.getName().getValue());
    folder.setOpen(true);

    GroundOverlay overlay = folder.createAndAddGroundOverlay();
    overlay.setName(formData.getName().getValue());
    overlay.setColor("5cffffff"); // transparency
    overlay.setDescription("Produced by 4mila.com");

    // map file
    Icon icon = new Icon();
    icon.setHref(mapUrl);
    overlay.setIcon(icon);

    // quad
    if (formData.getSWCornerBox().getY().getValue() == null || formData.getSECornerBox().getY().getValue() == null || formData.getNECornerBox().getY().getValue() == null || formData.getNWCornerBox().getY().getValue() == null || formData.getSWCornerBox().getX().getValue() == null || formData.getSECornerBox().getX().getValue() == null || formData.getNECornerBox().getX().getValue() == null || formData.getNWCornerBox().getX().getValue() == null) {
      throw new IllegalArgumentException("Coordinates required for KML/KMZ export");
    }
    LatLonQuad quad = KmlFactory.createGxLatLonQuad();
    List<Coordinate> coordinates = new ArrayList<Coordinate>();
    coordinates.add(new Coordinate(formData.getSWCornerBox().getX().getValue(), formData.getSWCornerBox().getY().getValue()));
    coordinates.add(new Coordinate(formData.getSECornerBox().getX().getValue(), formData.getSECornerBox().getY().getValue()));
    coordinates.add(new Coordinate(formData.getNECornerBox().getX().getValue(), formData.getNECornerBox().getY().getValue()));
    coordinates.add(new Coordinate(formData.getNWCornerBox().getX().getValue(), formData.getNWCornerBox().getY().getValue()));
    quad.setCoordinates(coordinates);
    overlay.addToGroundOverlayObjectExtension(quad);

    String kmlString = null;
    try {
      ByteArrayOutputStream out = new ByteArrayOutputStream();
      kml.marshal(out);
      kmlString = out.toString();
    }
    catch (IOException e) {
      throw new ProcessingException(e.getMessage());
    }

    return kmlString;
  }

  private GroundOverlay findGroundOverlay(Feature feature) {
    if (feature instanceof GroundOverlay) {
      return (GroundOverlay) feature;
    }
    else if (feature instanceof Folder) {
      Folder folder = (Folder) feature;
      for (Feature child : folder.getFeature()) {
        return findGroundOverlay(child);
      }
    }
    return null;
  }

  @Override
  public MapFormData createMapFromKml(byte[] kmlData) throws ProcessingException {
    if (kmlData == null) {
      throw new IllegalArgumentException("KML Data must not be null");
    }
    MapFormData formData = new MapFormData();

    final Kml kml = Kml.unmarshal(new ByteArrayInputStream(kmlData));
    Feature feature = kml.getFeature();
    GroundOverlay overlay = findGroundOverlay(feature);

    if (overlay != null) {
      // name
      formData.getName().setValue(overlay.getName());

      // map
      Icon icon = overlay.getIcon();
      if (icon != null) {
        formData.setFormat(icon.getHref());
      }

      // position
      for (AbstractObject ext : overlay.getGroundOverlayObjectExtension()) {
        if (ext instanceof LatLonQuad) {
          LatLonQuad quad = (LatLonQuad) ext;
          if (quad.getCoordinates() != null && quad.getCoordinates().size() >= 4) {
            // longitude
            formData.getSWCornerBox().getX().setValue(quad.getCoordinates().get(0).getLongitude());
            formData.getSECornerBox().getX().setValue(quad.getCoordinates().get(1).getLongitude());
            formData.getNECornerBox().getX().setValue(quad.getCoordinates().get(2).getLongitude());
            formData.getNWCornerBox().getX().setValue(quad.getCoordinates().get(3).getLongitude());

            // latitude
            formData.getSWCornerBox().getY().setValue(quad.getCoordinates().get(0).getLatitude());
            formData.getSECornerBox().getY().setValue(quad.getCoordinates().get(1).getLatitude());
            formData.getNECornerBox().getY().setValue(quad.getCoordinates().get(2).getLatitude());
            formData.getNWCornerBox().getY().setValue(quad.getCoordinates().get(3).getLatitude());
          }
        }
      }
      LatLonBox box = overlay.getLatLonBox();
      if (box != null) {
        // longitude
        formData.getSWCornerBox().getX().setValue(box.getWest());
        formData.getSECornerBox().getX().setValue(box.getEast());
        formData.getNECornerBox().getX().setValue(box.getEast());
        formData.getNWCornerBox().getX().setValue(box.getWest());

        // latitude
        formData.getSWCornerBox().getY().setValue(box.getSouth());
        formData.getSECornerBox().getY().setValue(box.getSouth());
        formData.getNECornerBox().getY().setValue(box.getNorth());
        formData.getNWCornerBox().getY().setValue(box.getNorth());
      }
      if (formData.getSWCornerBox().getY().getValue() == null || formData.getSECornerBox().getY().getValue() == null || formData.getNECornerBox().getY().getValue() == null || formData.getNWCornerBox().getY().getValue() == null || formData.getSWCornerBox().getX().getValue() == null || formData.getSECornerBox().getX().getValue() == null || formData.getNECornerBox().getX().getValue() == null || formData.getNWCornerBox().getX().getValue() == null) {
        throw new VetoException(TEXTS.get("KML_KMZ_ImportNoCoordinates"));
      }
    }
    else {
      throw new VetoException(TEXTS.get("KML_KMZ_ImportGroundOverlayMissing"));
    }

    return formData;
  }
}
