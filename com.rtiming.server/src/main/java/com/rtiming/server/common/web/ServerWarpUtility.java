package com.rtiming.server.common.web;

import javax.media.jai.PerspectiveTransform;
import javax.media.jai.WarpPerspective;

import com.rtiming.shared.map.MapFormData;

public final class ServerWarpUtility {

  private ServerWarpUtility() {

  }

  // CHECKSTYLE:OFF
  public static WarpPerspective build(double nwx, double nwy, double nex, double ney, double sex, double sey, double swx, double swy, double w, double h) {
    // CHECKSTYLE:ON

    PerspectiveTransform transform = PerspectiveTransform.getQuadToQuad(nwx, nwy, nex, ney, sex, sey, swx, swy, 0, h, w, h, w, 0, 0, 0);
    WarpPerspective wp = new WarpPerspective(transform);

    return wp;
  }

  public static WarpPerspective build(MapFormData map) {
    return build(map.getNWCornerBox().getX().getValue().doubleValue(), map.getNWCornerBox().getY().getValue().doubleValue(), map.getNECornerBox().getX().getValue().doubleValue(), map.getNECornerBox().getY().getValue().doubleValue(), map.getSECornerBox().getX().getValue().doubleValue(), map.getSECornerBox().getY().getValue().doubleValue(), map.getSWCornerBox().getX().getValue().doubleValue(), map.getSWCornerBox().getY().getValue().doubleValue(), map.getWidth().getValue().doubleValue(), map.getHeight().getValue());
  }

}
