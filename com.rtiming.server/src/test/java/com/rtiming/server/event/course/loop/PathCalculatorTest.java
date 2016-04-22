package com.rtiming.server.event.course.loop;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.rtiming.server.event.course.loop.CourseLoopCalculator.Loop;
import com.rtiming.server.event.course.loop.CourseLoopCalculator.Variant;
import com.rtiming.shared.event.course.CourseControlRowData;

public class PathCalculatorTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNull() throws Exception {
    PathCalculator.calculatePath(null);
  }

  @Test
  public void testEmpty1() throws Exception {
    List<Loop> list = new ArrayList<>();
    List<List<List<Variant>>> result = PathCalculator.calculatePath(list);
    Assert.assertNotNull("Not empty", result);
    Assert.assertEquals("0 Item", 0, result.size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testEmpty2() throws Exception {
    List<Loop> list = new ArrayList<>();
    Loop loop = new Loop(1L);
    list.add(loop);
    PathCalculator.calculatePath(list);
  }

  @Test
  public void testEmpty4() throws Exception {
    List<Loop> list = new ArrayList<>();
    Loop loop = new Loop(1L);
    loop.setPermutations(new ArrayList<List<Variant>>());
    list.add(loop);
    List<List<List<Variant>>> result = PathCalculator.calculatePath(list);
    Assert.assertNotNull("Not empty", result);
    Assert.assertEquals("0 Item", 0, result.size());
  }

  @Test
  public void testEmpty5() throws Exception {
    List<Loop> list = new ArrayList<>();
    Loop loop = new Loop(1L);
    List<List<Variant>> variantListList = new ArrayList<List<Variant>>();
    variantListList.add(new ArrayList<Variant>());
    loop.setPermutations(variantListList);
    list.add(loop);
    List<List<List<Variant>>> result = PathCalculator.calculatePath(list);
    Assert.assertNotNull("Not empty", result);
    Assert.assertEquals("1 Item", 1, result.size());
    Assert.assertEquals("1 Variant List", 1, result.get(0).size());
    Assert.assertEquals("0 Variants", 0, result.get(0).get(0).size());
  }

  @Test
  public void test1() throws Exception {
    List<Loop> list = new ArrayList<>();
    Loop loop = new Loop(1L);
    list.add(loop);
    List<List<Variant>> variantListList = createListOfVariantLists(
        createVariantList(
        createVariant(loop, "A")
        ));
    loop.setPermutations(variantListList);

    List<List<List<Variant>>> result = PathCalculator.calculatePath(list);
    Assert.assertNotNull("Not empty", result);
    Assert.assertEquals("1 Item", 1, result.size());
    Assert.assertEquals("1 Variant List", 1, result.get(0).size());
    Assert.assertEquals("1 Variant", 1, result.get(0).get(0).size());
    Assert.assertEquals("Variant Code", "A", result.get(0).get(0).get(0).getVariantId());
    Assert.assertEquals("Result", "[[[A]]]", result.toString());
  }

  @Test
  public void test2() throws Exception {
    List<Loop> list = new ArrayList<>();
    Loop loop = new Loop(1L);
    list.add(loop);
    loop.setPermutations(createListOfVariantLists(
        createVariantList(
        createVariant(loop, "A")
        )));

    List<List<List<Variant>>> result = PathCalculator.calculatePath(list);
    Assert.assertNotNull("Not empty", result);
    Assert.assertEquals("Result", "[[[A]]]", result.toString());
  }

  @Test
  public void test3() throws Exception {
    List<Loop> list = new ArrayList<>();
    Loop loop = new Loop(1L);
    list.add(loop);
    loop.setPermutations(createListOfVariantLists(
        createVariantList(
            createVariant(loop, "A", 31L),
            createVariant(loop, "B", 32L)
        )));

    List<List<List<Variant>>> result = PathCalculator.calculatePath(list);
    Assert.assertNotNull("Not empty", result);
    Assert.assertEquals("Result", "[[[A, B]]]", result.toString());
    Assert.assertEquals(1, result.size());
    Assert.assertEquals(1, result.get(0).size());
    Assert.assertEquals(2, result.get(0).get(0).size());
    Assert.assertEquals("[31]", controlsToString(result.get(0).get(0).get(0).getControls()));
    Assert.assertEquals("[32]", controlsToString(result.get(0).get(0).get(1).getControls()));
  }

  @Test
  public void test4() throws Exception {
    List<Loop> list = new ArrayList<>();
    Loop loop = new Loop(1L);
    list.add(loop);
    loop.setPermutations(createListOfVariantLists(
        createVariantList(
            createVariant(loop, "A", 31L),
            createVariant(loop, "B", 32L)
        )));

    Loop loop2 = new Loop(2L);
    list.add(loop2);
    loop2.setPermutations(createListOfVariantLists(
        createVariantList(
            createVariant(loop2, "B", 33L),
            createVariant(loop2, "A", 34L)
        )));

    List<List<List<Variant>>> result = PathCalculator.calculatePath(list);
    Assert.assertNotNull("Not empty", result);
    Assert.assertEquals("Result", "[[[A, B], [B, A]]]", result.toString());
    Assert.assertEquals(1, result.size());
    Assert.assertEquals(2, result.get(0).size());

    Assert.assertEquals(2, result.get(0).get(0).size());
    Assert.assertEquals("[31]", controlsToString(result.get(0).get(0).get(0).getControls()));
    Assert.assertEquals("[32]", controlsToString(result.get(0).get(0).get(1).getControls()));

    Assert.assertEquals(2, result.get(0).get(1).size());
    Assert.assertEquals("[33]", controlsToString(result.get(0).get(1).get(0).getControls()));
    Assert.assertEquals("[34]", controlsToString(result.get(0).get(1).get(1).getControls()));
  }

  @Test
  public void test5() throws Exception {
    List<Loop> list = new ArrayList<>();
    Loop loop = new Loop(1L);
    list.add(loop);
    loop.setPermutations(createListOfVariantLists(
        createVariantList(
            createVariant(loop, "A", 31L),
            createVariant(loop, "B", 32L)
        ),
        createVariantList(
            createVariant(loop, "B", 32L),
            createVariant(loop, "A", 31L)
        )));

    List<List<List<Variant>>> result = PathCalculator.calculatePath(list);
    Assert.assertNotNull("Not empty", result);
    Assert.assertEquals("Result", "[[[A, B]], [[B, A]]]", result.toString());

    Assert.assertEquals(2, result.size());

    Assert.assertEquals(1, result.get(0).size());
    Assert.assertEquals(2, result.get(0).get(0).size());
    Assert.assertEquals("[31]", controlsToString(result.get(0).get(0).get(0).getControls()));
    Assert.assertEquals("[32]", controlsToString(result.get(0).get(0).get(1).getControls()));

    Assert.assertEquals(1, result.get(1).size());
    Assert.assertEquals(2, result.get(1).get(0).size());
    Assert.assertEquals("[32]", controlsToString(result.get(1).get(0).get(0).getControls()));
    Assert.assertEquals("[31]", controlsToString(result.get(1).get(0).get(1).getControls()));
  }

  @Test
  public void test6() throws Exception {
    List<Loop> list = new ArrayList<>();
    Loop loop1 = new Loop(1L);
    list.add(loop1);
    loop1.setPermutations(createListOfVariantLists(
        createVariantList(
            createVariant(loop1, "A", 31L),
            createVariant(loop1, "B", 32L)
        ),
        createVariantList(
            createVariant(loop1, "B", 32L),
            createVariant(loop1, "A", 31L)
        )));

    Loop loop2 = new Loop(2L);
    list.add(loop2);
    loop2.setPermutations(createListOfVariantLists(
        createVariantList(
            createVariant(loop2, "C", 33L),
            createVariant(loop2, "D", 34L)
        ),
        createVariantList(
            createVariant(loop2, "D", 34L),
            createVariant(loop2, "C", 33L)
        )));

    List<List<List<Variant>>> result = PathCalculator.calculatePath(list);
    Assert.assertNotNull("Not empty", result);
    Assert.assertEquals("Result", "[[[A, B], [C, D]], [[A, B], [D, C]], [[B, A], [C, D]], [[B, A], [D, C]]]", result.toString());

    Assert.assertEquals(4, result.size());

    Assert.assertEquals(2, result.get(0).size());
    Assert.assertEquals(2, result.get(0).get(0).size());
    Assert.assertEquals("[31]", controlsToString(result.get(0).get(0).get(0).getControls()));
    Assert.assertEquals("[32]", controlsToString(result.get(0).get(0).get(1).getControls()));
    Assert.assertEquals("[33]", controlsToString(result.get(0).get(1).get(0).getControls()));
    Assert.assertEquals("[34]", controlsToString(result.get(0).get(1).get(1).getControls()));

    Assert.assertEquals(2, result.get(1).size());
    Assert.assertEquals(2, result.get(1).get(0).size());
    Assert.assertEquals("[31]", controlsToString(result.get(1).get(0).get(0).getControls()));
    Assert.assertEquals("[32]", controlsToString(result.get(1).get(0).get(1).getControls()));
    Assert.assertEquals("[34]", controlsToString(result.get(1).get(1).get(0).getControls()));
    Assert.assertEquals("[33]", controlsToString(result.get(1).get(1).get(1).getControls()));

    Assert.assertEquals(2, result.get(2).size());
    Assert.assertEquals(2, result.get(2).get(0).size());
    Assert.assertEquals("[32]", controlsToString(result.get(2).get(0).get(0).getControls()));
    Assert.assertEquals("[31]", controlsToString(result.get(2).get(0).get(1).getControls()));
    Assert.assertEquals("[33]", controlsToString(result.get(2).get(1).get(0).getControls()));
    Assert.assertEquals("[34]", controlsToString(result.get(2).get(1).get(1).getControls()));

    Assert.assertEquals(2, result.get(3).size());
    Assert.assertEquals(2, result.get(3).get(0).size());
    Assert.assertEquals("[32]", controlsToString(result.get(3).get(0).get(0).getControls()));
    Assert.assertEquals("[31]", controlsToString(result.get(3).get(0).get(1).getControls()));
    Assert.assertEquals("[34]", controlsToString(result.get(3).get(1).get(0).getControls()));
    Assert.assertEquals("[33]", controlsToString(result.get(3).get(1).get(1).getControls()));
  }

  private Variant createVariant(Loop loop, String code, Long... controls) {
    Variant variant = new Variant(loop, code);
    for (Long controlNr : controls) {
      CourseControlRowData c = new CourseControlRowData();
      c.setControlNr(controlNr);
      variant.getControls().add(c);
    }
    return variant;
  }

  private List<Variant> createVariantList(Variant... variants) {
    List<Variant> variantList = new ArrayList<Variant>();
    for (Variant variant : variants) {
      variantList.add(variant);
    }
    return variantList;
  }

  private String controlsToString(List<CourseControlRowData> controls) {
    List<Long> nrs = new ArrayList<>();
    for (CourseControlRowData row : controls) {
      nrs.add(row.getControlNr());
    }
    return nrs.toString();
  }

  @SafeVarargs
  private final List<List<Variant>> createListOfVariantLists(List<Variant>... lists) {
    List<List<Variant>> listsOfvariantList = new ArrayList<List<Variant>>();
    for (List<Variant> list : lists) {
      listsOfvariantList.add(list);
    }
    return listsOfvariantList;
  }

}
