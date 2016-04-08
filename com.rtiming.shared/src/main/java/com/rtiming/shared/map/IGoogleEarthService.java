package com.rtiming.shared.map;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;


public interface IGoogleEarthService extends IService {

  String createKmlFromMap(MapFormData formData, String mapUrl) throws ProcessingException;

  MapFormData createMapFromKml(byte[] kml) throws ProcessingException;
}
