package com.rtiming.shared.event.course;

import java.lang.reflect.Method;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

public class CourseControlRowDataTest {

  @Test
  public void testRemoveVariantInformation() throws Exception {
    CourseControlRowData data = new CourseControlRowData();
    data.setForkMasterCourseControlNo("31");
    data.setForkMasterCourseControlNr(2L);
    data.setForkVariantCode("ABC");
    data.removeVariantInformation();
    Assert.assertNull(data.getForkMasterCourseControlNo());
    Assert.assertNull(data.getForkVariantCode());
    Assert.assertNull(data.getForkMasterCourseControlNr());
  }

  @Test
  public void testClone() throws Exception {
    CourseControlRowData data = new CourseControlRowData();
    for (Method method : data.getClass().getMethods()) {
      if (method.getParameterTypes().length == 1) {
        Object param = method.getParameterTypes()[0];
        if (param == Long.class) {
          method.invoke(data, System.currentTimeMillis());
        }
        else if (param == String.class) {
          method.invoke(data, UUID.randomUUID().toString());
        }
      }
    }

    CourseControlRowData clone = data.clone();
    Assert.assertNotSame("Not same", data, clone);
    for (Method method : data.getClass().getMethods()) {
      if (method.getParameterTypes().length == 0 &&
          (method.getName().startsWith("get") || method.getName().startsWith("is"))) {
        Object result1 = method.invoke(data);
        Object result2 = method.invoke(clone);
        Assert.assertEquals("Value", result1, result2);
      }
    }
  }

}
