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
    return build(map.getNWCornerBox().getX().getValue(), map.getNWCornerBox().getY().getValue(), map.getNECornerBox().getX().getValue(), map.getNECornerBox().getY().getValue(), map.getSECornerBox().getX().getValue(), map.getSECornerBox().getY().getValue(), map.getSWCornerBox().getX().getValue(), map.getSWCornerBox().getY().getValue(), map.getWidth().getValue(), map.getHeight().getValue());
  }

}
