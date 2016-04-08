package com.rtiming.server.map;

import java.awt.Dimension;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;
import javax.imageio.stream.FileImageInputStream;
import javax.imageio.stream.ImageInputStream;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.file.IFileProcessService;
import com.rtiming.shared.common.security.permission.CreateMapPermission;
import com.rtiming.shared.common.security.permission.ReadMapPermission;
import com.rtiming.shared.common.security.permission.UpdateMapPermission;
import com.rtiming.shared.dao.RtEventMap;
import com.rtiming.shared.dao.RtEventMapKey_;
import com.rtiming.shared.dao.RtEventMap_;
import com.rtiming.shared.dao.RtMap;
import com.rtiming.shared.dao.RtMapKey;
import com.rtiming.shared.dao.RtMapKey_;
import com.rtiming.shared.dao.RtMap_;
import com.rtiming.shared.event.EventMapFormData;
import com.rtiming.shared.map.IMapProcessService;

public class MapProcessService  implements IMapProcessService {

  @Override
  public RtMap prepareCreate(RtMap formData) throws ProcessingException {
    if (!ACCESS.check(new CreateMapPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public RtMap create(RtMap bean) throws ProcessingException {
    if (!ACCESS.check(new CreateMapPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // new key
    bean.setId(RtMapKey.create(bean.getId()));

    bean = store(bean);

    return bean;
  }

  @Override
  public RtMap load(RtMapKey key) throws ProcessingException {
    if (!ACCESS.check(new ReadMapPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }
    if (key == null) {
      return null;
    }

    key = RtMapKey.create(key);
    RtMap bean = JPA.find(RtMap.class, key);

    // load map
    if (bean != null) {
      bean.setMapData(BEANS.get(IFileProcessService.class).loadFile(bean.getId().getId(), NumberUtility.nvl(bean.getId().getClientNr(), ServerSession.get().getSessionClientNr()), bean.getFormat(), SERVER_MAP_DIR));
    }

    return bean;
  }

  @Override
  public RtMap store(RtMap formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateMapPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    // save map
    if (formData.getIsFileChanged()) {
      String file = BEANS.get(IFileProcessService.class).writeDataToFile(formData.getId().getId(), NumberUtility.nvl(formData.getId().getClientNr(), ServerSession.get().getSessionClientNr()), formData.getFormat(), formData.getMapData(), SERVER_MAP_DIR);
      Dimension d = getImageDim(file, formData.getFormat());
      formData.setW(d.width);
      formData.setH(d.height);
      formData.setEvtFileLastUpdate(new Date());
    }

    JPA.merge(formData);

    // store event-map
    if (formData.getNewEventNr() != null) {
      EventMapFormData eventMap = new EventMapFormData();
      eventMap.getMap().setValue(formData.getId().getId());
      eventMap.getEvent().setValue(formData.getNewEventNr());
      BEANS.get(EventMapProcessService.class).create(eventMap);
    }

    return formData;
  }

  private Dimension getImageDim(final String path, final String suffix) throws ProcessingException {
    Dimension result = null;
    Iterator<ImageReader> iter = ImageIO.getImageReadersBySuffix(suffix);
    if (iter.hasNext()) {
      ImageReader reader = iter.next();
      try {
        ImageInputStream stream = new FileImageInputStream(new File(path));
        reader.setInput(stream);
        int width = reader.getWidth(reader.getMinIndex());
        int height = reader.getHeight(reader.getMinIndex());
        result = new Dimension(width, height);
      }
      catch (IOException e) {
        throw new ProcessingException(e.getMessage());
      }
      finally {
        reader.dispose();
      }
    }
    else {
      throw new ProcessingException("No reader found for given format: " + suffix);
    }
    return result;
  }

  @Override
  public RtMap findMap(long eventNr, long clientNr) throws ProcessingException {
    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtEventMap> select = b.createQuery(RtEventMap.class);
    Root<RtEventMap> rteventmap = select.from(RtEventMap.class);

    select.where(
        b.and(
            b.equal(rteventmap.get(RtEventMap_.id).get(RtEventMapKey_.eventNr), eventNr),
            b.equal(rteventmap.get(RtEventMap_.id).get(RtEventMapKey_.clientNr), clientNr)
            ));

    List<RtEventMap> maps = JPA.createQuery(select).getResultList();

    RtMapKey mapKey = new RtMapKey();
    mapKey.setClientNr(clientNr);

    if (maps.size() > 0) {
      mapKey.setId(maps.get(0).getId().getMapNr());
      return load(mapKey);
    }
    else {
      RtMap map = new RtMap();
      map.setId(mapKey);
      return map;
    }
  }

  @Override
  public List<Long> getAllMaps(Long clientNr) throws ProcessingException {

    CriteriaBuilder b = JPA.getCriteriaBuilder();
    CriteriaQuery<RtMap> select = b.createQuery(RtMap.class);
    Root<RtMap> rtmap = select.from(RtMap.class);

    select.where(
        b.and(
            b.equal(rtmap.get(RtMap_.key).get(RtMapKey_.clientNr), clientNr))
        );

    List<RtMap> maps = JPA.createQuery(select).getResultList();

    if (maps != null && maps.size() > 0) {
      List<Long> result = new ArrayList<>();
      for (RtMap map : maps) {
        result.add(map.getId().getId());
      }
      return result;
    }

    return new ArrayList<Long>();
  }

  @Override
  public void delete(RtMapKey key) throws ProcessingException {
    if (key != null) {
      RtMap map = load(key);
      if (map != null) {
        JPA.remove(map);
        BEANS.get(IFileProcessService.class).deleteFile(map.getId().getId(), ServerSession.get().getSessionClientNr(), SERVER_MAP_DIR, FMilaUtility.getSupportedImageFormats());
      }
    }
  }
}
