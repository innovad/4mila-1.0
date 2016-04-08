package com.rtiming.server.event.course.loop;

import java.util.ArrayList;
import java.util.List;

import com.rtiming.server.event.course.loop.CourseLoopCalculator.Loop;
import com.rtiming.server.event.course.loop.CourseLoopCalculator.Variant;

/**
 * 
 */
public class PathCalculator {

  private PathCalculator() {
  }

  public final static List<List<List<Variant>>> calculatePath(List<Loop> loop) {
    if (loop == null) {
      throw new IllegalArgumentException("Arguments must not be null");
    }
    List<List<List<Variant>>> allResults = new ArrayList<>();
    List<List<Variant>> partResult = new ArrayList<List<Variant>>();
    if (loop.size() == 0) {
      return allResults;
    }
    calculatePath(allResults, partResult, loop);
    return allResults;
  }

  private final static void calculatePath(List<List<List<Variant>>> allResults, List<List<Variant>> partResult, List<Loop> loop) {
    if (loop.size() == 0) {
      allResults.add(partResult);
      return;
    }
    Loop f = loop.get(0);
    for (List<Variant> p : f.getPermutations()) {
      List<List<Variant>> path = new ArrayList<>();
      for (List<Variant> e : partResult) {
        path.add(e);
      }
      path.add(p);
      List<Loop> part = loop.subList(1, loop.size());
      calculatePath(allResults, path, part);
    }
  }

}
