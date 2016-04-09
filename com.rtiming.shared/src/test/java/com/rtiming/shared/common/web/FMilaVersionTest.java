package com.rtiming.shared.common.web;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.junit.Test;

import com.rtiming.shared.common.security.FMilaVersion;

import junit.framework.Assert;

public class FMilaVersionTest {

  @Test
  public void testSimpleVersion() throws Exception {
    FMilaVersion version = new FMilaVersion("1.2.3.201205111313");
    Assert.assertEquals("Simple Version", "1.2.3", version.getSimpleVersion());
  }

  @Test
  public void testDate() throws Exception {
    FMilaVersion version = new FMilaVersion("1.2.3.201205111314");
    GregorianCalendar cal = new GregorianCalendar();
    cal.setTime(version.getDate());
    Assert.assertEquals("Year", 2012, cal.get(Calendar.YEAR));
    Assert.assertEquals("Month", 4, cal.get(Calendar.MONTH));
    Assert.assertEquals("Day", 11, cal.get(Calendar.DAY_OF_MONTH));
    Assert.assertEquals("Hour", 13, cal.get(Calendar.HOUR_OF_DAY));
    Assert.assertEquals("Minute", 14, cal.get(Calendar.MINUTE));
  }

  @Test
  public void testEmptyDate() throws Exception {
    FMilaVersion version = new FMilaVersion("1.2.3.qualifier");
    Assert.assertEquals("Simple Version", "1.2.3", version.getSimpleVersion());
    Assert.assertNull("Date is null", version.getDate());
  }

  @Test
  public void testToString() throws Exception {
    FMilaVersion version = new FMilaVersion("1.2.3.qualifier");
    Assert.assertEquals("String value", "1.2.3.qualifier", version.toString());
  }

  @Test
  public void testToStringWithDate() throws Exception {
    FMilaVersion version = new FMilaVersion("1.2.3.201205111314");
    Assert.assertTrue("String value", version.toString().startsWith("1.2.3 ("));
  }

}
