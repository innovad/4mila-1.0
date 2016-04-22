package com.rtiming.server.event.course.loop;

import java.util.ArrayList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import com.rtiming.server.event.course.loop.CourseLoopCalculator.Loop;
import com.rtiming.server.event.course.loop.CourseLoopCalculator.Variant;

public class VariantPermutatorTest {

  @Test(expected = IllegalArgumentException.class)
  public void testNull() throws Exception {
    VariantPermutator.permutation(null);
  }

  @Test
  public void testPermutation1() throws Exception {
    List<List<Variant>> result = VariantPermutator.permutation(new ArrayList<Variant>());
    Assert.assertNotNull(result);
    Assert.assertEquals(1, result.size());
    Assert.assertEquals(0, result.get(0).size());
  }

  @Test
  public void testPermutation2() throws Exception {
    ArrayList<Variant> numbers = new ArrayList<Variant>();
    numbers.add(createVariant("1"));
    List<List<Variant>> result = VariantPermutator.permutation(numbers);
    Assert.assertNotNull(result);
    Assert.assertEquals(1, result.size());
    Assert.assertEquals(1, result.get(0).size());
    Assert.assertEquals("1", result.get(0).get(0).getVariantId());
  }

  @Test
  public void testPermutation3() throws Exception {
    ArrayList<Variant> numbers = new ArrayList<Variant>();
    numbers.add(createVariant("1"));
    numbers.add(createVariant("2"));
    List<List<Variant>> result = VariantPermutator.permutation(numbers);
    Assert.assertEquals("[[1, 2], [2, 1]]", result.toString());
  }

  @Test
  public void testPermutation4() throws Exception {
    ArrayList<Variant> numbers = new ArrayList<Variant>();
    numbers.add(createVariant("44"));
    numbers.add(createVariant("33"));
    List<List<Variant>> result = VariantPermutator.permutation(numbers);
    Assert.assertEquals("[[44, 33], [33, 44]]", result.toString());
  }

  @Test
  public void testPermutation5() throws Exception {
    ArrayList<Variant> numbers = new ArrayList<Variant>();
    numbers.add(createVariant("1"));
    numbers.add(createVariant("2"));
    numbers.add(createVariant("3"));
    List<List<Variant>> result = VariantPermutator.permutation(numbers);
    Assert.assertEquals("[[1, 2, 3], [1, 3, 2], [2, 1, 3], [2, 3, 1], [3, 1, 2], [3, 2, 1]]", result.toString());
  }

  protected Variant createVariant(String variantId) {
    return new Variant(new Loop(99L), variantId);
  }

}
