package com.rtiming.shared.dataexchange;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
public @interface CSVElement {

  int value();

  String[] title() default "";

  boolean ignore() default false;

  boolean isMandatory() default false;

  boolean isYear() default false;

  boolean isSex() default false;

  long maxLength() default 60;

  long minLength() default 0;

}
