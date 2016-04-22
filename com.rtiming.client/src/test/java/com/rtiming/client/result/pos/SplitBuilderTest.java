package com.rtiming.client.result.pos;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(ClientTestRunner.class) @RunWithSubject("admin") @RunWithClientSession(TestEnvironmentClientSession.class)
public class SplitBuilderTest {

  @Test
  public void testAppendPadLeft() throws Exception {
    SplitBuilder builder = new SplitBuilder(55);
    builder.appendPadLeft("12345", 12);
    Assert.assertEquals(12, builder.toString().length());
  }

  @Test
  public void testAppendPadLeftNull() throws Exception {
    SplitBuilder builder = new SplitBuilder(55);
    builder.appendPadLeft(null, 12);
    Assert.assertEquals(12, builder.toString().length());
  }

  @Test
  public void testAppendPadLeftZero() throws Exception {
    SplitBuilder builder = new SplitBuilder(55);
    builder.appendPadLeft("ABC", 0);
    Assert.assertEquals(3, builder.toString().length());
    Assert.assertEquals("ABC", builder.toString());
  }

  @Test
  public void testAppendPadLeftOversize() throws Exception {
    SplitBuilder builder = new SplitBuilder(55);
    builder.appendPadLeft("1234", 3);
    Assert.assertEquals(3, builder.toString().length());
  }

  @Test
  public void testAppendLeftRight() throws Exception {
    SplitBuilder builder = new SplitBuilder(55);
    builder.appendLeftRight("ABC", "XYZ");
    Assert.assertEquals(55, builder.toString().length());
    Assert.assertTrue(builder.toString().startsWith("ABC"));
    Assert.assertTrue(builder.toString().endsWith("XYZ"));
  }

  @Test
  public void testAppendLeftRightOversize() throws Exception {
    SplitBuilder builder = new SplitBuilder(5);
    builder.appendLeftRight("ABC", "XYZ");
    Assert.assertEquals(5, builder.toString().length());
    Assert.assertEquals("ABCXY", builder.toString());
  }

  @Test
  public void testAppendLeftRightWide() throws Exception {
    SplitBuilder builder = new SplitBuilder(40);
    builder.appendLeftRightWide("ABC", "XYZ");
    int wide = PosPrinterUtility.startWide().length();
    int normal = PosPrinterUtility.startNormal().length();
    Assert.assertEquals(wide + 20 + normal, builder.toString().length());
    Assert.assertTrue(builder.toString().startsWith(PosPrinterUtility.startWide() + "ABC"));
    Assert.assertTrue(builder.toString().endsWith("XYZ" + PosPrinterUtility.startNormal()));
  }

  @Test
  public void testAppendRight() throws Exception {
    SplitBuilder builder = new SplitBuilder(40);
    builder.appendRight("XYZ");
    Assert.assertEquals(40, builder.toString().length());
    Assert.assertTrue(builder.toString().endsWith("XYZ"));
  }

  @Test
  public void testAppendRightOversize() throws Exception {
    SplitBuilder builder = new SplitBuilder(5);
    builder.appendRight("12345678");
    Assert.assertEquals(5, builder.toString().length());
    Assert.assertEquals("12345", builder.toString());
  }

  @Test
  public void testAppendNewLine() throws Exception {
    SplitBuilder builder = new SplitBuilder(10);
    builder.appendNewLine();
    Assert.assertEquals(1, builder.toString().length());
  }

  @Test
  public void testAppend() throws Exception {
    SplitBuilder builder = new SplitBuilder(50);
    builder.append("1234567890");
    Assert.assertEquals("1234567890", builder.toString());
  }

  @Test
  public void testAppendNull() throws Exception {
    SplitBuilder builder = new SplitBuilder(50);
    builder.append(null);
    Assert.assertEquals("", builder.toString());
    Assert.assertEquals(0, builder.toString().length());
  }

  @Test
  public void testAppendWide() throws Exception {
    SplitBuilder builder = new SplitBuilder(50);
    builder.appendWide("TEST");
    Assert.assertTrue(builder.toString().startsWith(PosPrinterUtility.startWide() + "TEST"));
    Assert.assertTrue(builder.toString().endsWith("TEST" + PosPrinterUtility.startNormal()));
  }

  @Test
  public void testCut() throws Exception {
    SplitBuilder builder = new SplitBuilder(50);
    builder.cut();
    Assert.assertEquals(PosPrinterUtility.cut(), builder.toString());
  }

}
