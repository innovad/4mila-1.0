package com.rtiming.shared.common.security.permission;

import java.security.BasicPermission;

public class CreateMapPermission extends BasicPermission {

  private static final long serialVersionUID = 0L;

  public CreateMapPermission() {
  super("CreateMap");
  }
}
