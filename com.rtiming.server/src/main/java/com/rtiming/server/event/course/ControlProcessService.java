package com.rtiming.server.event.course;

import java.awt.geom.Point2D;
import java.util.List;

import javax.media.jai.WarpPerspective;

import org.eclipse.scout.commons.BooleanUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.util.NumberUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.FMilaTypedQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.server.common.database.jpa.JPAUtility;
import com.rtiming.server.common.web.ServerWarpUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.security.permission.CreateControlPermission;
import com.rtiming.shared.common.security.permission.DeleteControlPermission;
import com.rtiming.shared.common.security.permission.ReadControlPermission;
import com.rtiming.shared.common.security.permission.UpdateControlPermission;
import com.rtiming.shared.dao.RtControl;
import com.rtiming.shared.dao.RtControlKey;
import com.rtiming.shared.event.course.ControlFormData;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.IControlProcessService;
import com.rtiming.shared.map.IMapProcessService;
import com.rtiming.shared.map.MapFormData;

public class ControlProcessService  implements IControlProcessService {

  @Override
  public ControlFormData prepareCreate(ControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getType().setValue(ControlTypeCodeType.ControlCode.ID);
    formData.getActive().setValue(true);

    String queryString = "SELECT controlNo FROM RtControl " +
        "WHERE eventNr = :event " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaTypedQuery<String> query = JPA.createQuery(queryString, String.class);
    query.setParameter("event", formData.getEvent().getValue());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    List<String> controlNos = query.getResultList();

    formData.getNumber().setValue(calculateNextControlNo(controlNos));
    return formData;
  }

  protected String calculateNextControlNo(List<String> controlNos) {
    if (controlNos == null || controlNos.size() == 0) {
      return FMilaUtility.FIRST_CONTROL_NO;
    }
    else {
      Long maxControlNo = null;
      for (String controlNo : controlNos) {
        try {
          Long controlNumber = Long.parseLong(controlNo);
          maxControlNo = Math.max(controlNumber, NumberUtility.nvl(maxControlNo, Long.MIN_VALUE));
        }
        catch (NumberFormatException e) {
          // nop
        }
      }
      if (maxControlNo == null) {
        return FMilaUtility.FIRST_CONTROL_NO;
      }
      else {
        maxControlNo++;
      }
      return String.valueOf(maxControlNo);
    }
  }

  @Override
  public ControlFormData create(ControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    RtControlKey key = RtControlKey.create((Long) null);
    RtControl control = new RtControl();
    control.setId(key);
    control.setActive(formData.getActive().getValue());
    JPA.persist(control);

    formData.setControlNr(control.getId().getId());
    formData = store(formData);

    return formData;
  }

  @Override
  public ControlFormData load(ControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    JPAUtility.select("SELECT " +
        "controlNo, " +
        "typeUid, " +
        "active, " +
        "localx, " +
        "localy, " +
        "eventNr," +
        "globalx," +
        "globaly " +
        "FROM RtControl " +
        "WHERE id.controlNr = :controlNr " +
        "AND id.clientNr = COALESCE(:clientNr, :sessionClientNr) " +
        "INTO " +
        ":number, " +
        ":type," +
        ":active," +
        ":positionX," +
        ":positionY," +
        ":event," +
        ":globalX," +
        ":globalY"
        , formData
        );

    return formData;
  }

  @Override
  public ControlFormData store(ControlFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    if (StringUtility.isNullOrEmpty(formData.getNumber().getValue())) {
      throw new VetoException(TEXTS.get("ControlNumberMandatoryMessage"));
    }

    String queryString = "SELECT COUNT(id.controlNr) " +
        "FROM RtControl " +
        "WHERE controlNo = :number " +
        "AND id.controlNr != :controlNr " +
        "AND eventNr = :event " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("number", formData.getNumber().getValue());
    query.setParameter("controlNr", formData.getControlNr());
    query.setParameter("event", formData.getEvent().getValue());
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Long otherControlsWithSameNumber = query.getSingleResult();

    if (otherControlsWithSameNumber >= 1) {
      throw new VetoException(TEXTS.get("ControlNumberUniqueMessage"));
    }

    formData.getActive().setValue(BooleanUtility.nvl(formData.getActive().getValue()));
    queryString = "UPDATE RtControl " +
        "SET controlNo = :number, " +
        "typeUid = :type, " +
        "active = :active, " +
        "localx = :positionX, " +
        "localy = :positionY, " +
        "eventNr = :event, " +
        "globalx = :globalX, " +
        "globaly = :globalY " +
        "WHERE id.controlNr = :controlNr " +
        "AND id.clientNr = :sessionClientNr ";
    FMilaQuery query2 = JPA.createQuery(queryString);
    JPAUtility.setAutoParameters(query2, queryString, formData);
    query2.executeUpdate();

    return formData;
  }

  @Override
  public ControlFormData find(String controlNo, long eventNr) throws ProcessingException {
    controlNo = StringUtility.uppercase(StringUtility.nvl(controlNo, "")).trim();

    String queryString = "SELECT MAX(id.controlNr) " +
        "FROM RtControl " +
        "WHERE UPPER(controlNo) = :controlNo " +
        "AND eventNr = :eventNr " +
        "AND id.clientNr = :sessionClientNr ";

    FMilaTypedQuery<Long> query = JPA.createQuery(queryString, Long.class);
    query.setParameter("controlNo", controlNo);
    query.setParameter("eventNr", eventNr);
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    Long controlNr = query.getSingleResult();

    ControlFormData control = new ControlFormData();
    if (controlNr != null) {
      control.setControlNr(controlNr);
      control = load(control);
    }
    else {
      control.getNumber().setValue(controlNo);
      control.getEvent().setValue(eventNr);
    }

    return control;
  }

  @Override
  public void georeferenceFromLocalPosition(Long[] controlNrs, long eventNr) throws ProcessingException {
    // Load Map Info
    MapFormData map = BeanUtility.mapBean2FormData(BEANS.get(IMapProcessService.class).findMap(eventNr, ServerSession.get().getSessionClientNr()));

    if (map.getResolution().getValue() == null ||
        map.getOriginX().getValue() == null ||
        map.getOriginY().getValue() == null ||
        map.getScale().getValue() == null ||
        map.getWidth().getValue() == null ||
        map.getHeight().getValue() == null) {
      throw new VetoException(TEXTS.get("GeoreferenceRequiredDataError"));
    }

    double resolution = map.getResolution().getValue();
    double mapX = map.getOriginX().getValue();
    double mapY = map.getOriginY().getValue();
    long scale = map.getScale().getValue();

    if (map.getWidth() != null && map.getHeight() != null) {
      for (Long controlNr : controlNrs) {
        // Load Control
        ControlFormData control = new ControlFormData();
        control.setControlNr(controlNr);
        control = load(control);

        // Calculate Longitude, Latitude
        if (control.getPositionX().getValue() != null && control.getPositionY().getValue() != null) {
          double x = control.getPositionX().getValue();
          double y = control.getPositionY().getValue();

          double lx = (x - mapX) * 10;
          double ly = -1 * (y - mapY) * 10;

          double pixM = ((resolution / FMilaUtility.ONE_INCH) * 100) / scale; // Pixel per Meter

          lx = lx * pixM;
          ly = ly * pixM;

          // Warp
          WarpPerspective warp = ServerWarpUtility.build(map);
          Point2D point = new Point2D.Double(lx, ly);
          point = warp.mapSourcePoint(point);

          control.getGlobalX().setValue(point.getX());
          control.getGlobalY().setValue(point.getY());
          control = store(control);
        }
      }
    }
  }

  @Override
  public void delete(ControlFormData control) throws ProcessingException {
    if (!ACCESS.check(new DeleteControlPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    if (control != null && control.getControlNr() != null) {
      RtControl c = new RtControl();
      RtControlKey key = new RtControlKey();
      key.setId(control.getControlNr());
      key.setClientNr(control.getClientNr());
      c.setId(RtControlKey.create(key));
      JPA.remove(c);
    }
  }

}
