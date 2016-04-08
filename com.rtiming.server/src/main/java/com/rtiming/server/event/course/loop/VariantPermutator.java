package com.rtiming.server.event.course.loop;

import java.util.ArrayList;
import java.util.List;

import com.rtiming.server.event.course.loop.CourseLoopCalculator.Variant;

/**
 * 
 */
public final class VariantPermutator {

  private VariantPermutator() {
  }

  public static List<List<Variant>> permutation(List<Variant> numbers) {
    if (numbers == null) {
      throw new IllegalArgumentException("null not allowed");
    }
    List<List<Variant>> result = new ArrayList<>();
    permutation(new ArrayList<Variant>(), numbers, result);
    return result;
  }

  private static void permutation(List<Variant> prefix, List<Variant> str, List<List<Variant>> result) {
    int n = str.size();
    if (n == 0) {
      result.add(prefix);
    }
    else {
      for (int i = 0; i < n; i++) {
        List<Variant> newPrefix = new ArrayList<>(prefix);
        newPrefix.add(str.get(i));

        List<Variant> newStr = new ArrayList<>(str.subList(0, i));
        newStr.addAll(str.subList(i + 1, n));
        permutation(newPrefix, newStr, result);
      }
    }
  }

}
