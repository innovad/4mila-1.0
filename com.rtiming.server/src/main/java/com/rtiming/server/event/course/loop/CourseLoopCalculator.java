package com.rtiming.server.event.course.loop;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.scout.commons.CompareUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.exception.ProcessingException;

import com.rtiming.server.event.course.ServerCache;
import com.rtiming.server.race.RaceControlBean;
import com.rtiming.shared.event.course.ControlTypeCodeType;
import com.rtiming.shared.event.course.CourseControlRowData;
import com.rtiming.shared.services.code.CourseForkTypeCodeType;

public class CourseLoopCalculator {

  private CourseLoopCalculator() {
  }

  public static List<List<CourseControlRowData>> calculateCourse(List<CourseControlRowData> definitions, Long loopTypeUid) {
    List<List<CourseControlRowData>> result = new ArrayList<>();
    calculateCourse(result, definitions, loopTypeUid);
    return result;
  }

  private static void calculateCourse(List<List<CourseControlRowData>> result, List<CourseControlRowData> definitions, Long loopTypeUid) {
    if (definitions == null) {
      throw new IllegalArgumentException("definitions required");
    }

    // find top-level masters and check data
    Set<Long> topLevelMasters = new HashSet<>();
    Set<Long> masterCourseControlNrs = new HashSet<>();
    for (CourseControlRowData definition : definitions) {
      if (definition.getLoopTypeUid() != null) {
        masterCourseControlNrs.add(definition.getCourseControlNr());
      }
      if (definition.getForkMasterCourseControlNr() != null && StringUtility.isNullOrEmpty(definition.getForkVariantCode())) {
        throw new IllegalArgumentException("Illegal control settings: If a loop master is defined, there must be a variant code");
      }
      if (definition.getForkMasterCourseControlNr() == null && !StringUtility.isNullOrEmpty(definition.getForkMasterCourseControlNo())) {
        throw new IllegalArgumentException("Illegal control settings: Loop master not correctly defined (Nr required).");
      }
      if (CompareUtility.equals(definition.getForkMasterCourseControlNr(), definition.getCourseControlNr())) {
        throw new IllegalArgumentException("Loop master cannot be the same control.");
      }
      if (definition.getForkMasterCourseControlNr() != null) {
        if (!masterCourseControlNrs.contains(definition.getForkMasterCourseControlNr())) {
          throw new IllegalArgumentException("Master for control " + definition.getControlNo() + " not found or wrong order of master control.");
        }
        topLevelMasters.add(definition.getForkMasterCourseControlNr());
      }
    }
    for (CourseControlRowData definition : definitions) {
      if (!StringUtility.isNullOrEmpty(definition.getForkVariantCode())) {
        topLevelMasters.remove(definition.getCourseControlNr());
      }
      if (!CompareUtility.equals(definition.getLoopTypeUid(), loopTypeUid)) {
        topLevelMasters.remove(definition.getCourseControlNr());
      }
    }
    if (topLevelMasters.size() == 0) {
      result.add(definitions);
      return;
    }

    // find all master controls
    Set<Loop> masterControlNrs = new HashSet<>();
    Map<Long, Loop> loopIndex = new HashMap<Long, Loop>();
    for (CourseControlRowData definition : definitions) {
      // only top masters
      if (definition.getForkMasterCourseControlNr() != null &&
          topLevelMasters.contains(definition.getForkMasterCourseControlNr())) {
        if (loopIndex.get(definition.getForkMasterCourseControlNr()) == null) {
          Loop loop = new Loop(definition.getForkMasterCourseControlNr());
          masterControlNrs.add(loop);
          loopIndex.put(loop.getMasterControlNr(), loop);
        }
        Loop loop = loopIndex.get(definition.getForkMasterCourseControlNr());
        if (loop.findVariant(definition.getForkVariantCode()) == null) {
          Variant variant = new Variant(loop, definition.getForkVariantCode());
          loop.addVariant(variant);
        }
        loop.findVariant(definition.getForkVariantCode()).getControls().add(definition);
      }
    }

    for (Loop loop : masterControlNrs) {
      if (CompareUtility.equals(loopTypeUid, CourseForkTypeCodeType.ButterflyCode.ID)) {
        loop.setPermutations(VariantPermutator.permutation(Arrays.asList(loop.getVariants().toArray(new Variant[loop.getVariants().size()]))));
      }
      else {
        List<List<Variant>> permutations = new ArrayList<>();
        for (Variant v : loop.getVariants()) {
          ArrayList<Variant> fork = new ArrayList<Variant>();
          fork.add(v);
          permutations.add(fork);
        }
        loop.setPermutations(permutations);
      }
    }

    // Calculate All Paths
    List<List<List<Variant>>> allPaths = PathCalculator.calculatePath(Arrays.asList(masterControlNrs.toArray(new Loop[masterControlNrs.size()])));

    // Build Variants
    List<List<CourseControlRowData>> variantList = new ArrayList<>();
    for (List<List<Variant>> path : allPaths) {
      List<CourseControlRowData> course = new ArrayList<>();
      Map<Long, List<Variant>> variantLookup = new HashMap<>();

      // add variants
      for (List<Variant> p : path) {
        variantLookup.put(p.get(0).getLoop().getMasterControlNr(), p);
      }

      // add standard controls
      for (CourseControlRowData definition : definitions) {
        // add non-masters and non top-level masters
        if (definition.getForkMasterCourseControlNr() == null ||
            !topLevelMasters.contains(definition.getForkMasterCourseControlNr())) {
          course.add(definition);
          if (variantLookup.get(definition.getCourseControlNr()) != null) {
            if (CompareUtility.equals(loopTypeUid, CourseForkTypeCodeType.ButterflyCode.ID)) {
              // Butterfly type
              List<Variant> variant = variantLookup.get(definition.getCourseControlNr());
              for (Variant v : variant) {
                for (CourseControlRowData control : v.getControls()) {
                  course.add(control);
                }
                course.add(definition);
              }
            }
            else if (CompareUtility.equals(loopTypeUid, CourseForkTypeCodeType.ForkCode.ID)) {
              // Fork
              Variant variant = variantLookup.get(definition.getCourseControlNr()).get(0); // TODO
              course.addAll(variant.getControls());
            }
            else {
              throw new IllegalArgumentException("Undefined Fork Type");
            }
          }
        }
      }
      variantList.add(course);
    }

    for (List<CourseControlRowData> v : variantList) {
      // Reset used master
      for (int k = 0; k < v.size(); k++) {
        CourseControlRowData control = v.get(k);
        if (control.getForkMasterCourseControlNr() != null && topLevelMasters.contains(control.getForkMasterCourseControlNr())) {
          CourseControlRowData copy = control.clone();
          copy.removeVariantInformation();
          v.remove(k);
          v.add(k, copy);
        }
      }

      calculateCourse(result, v, loopTypeUid);
    }
  }

  public static List<RaceControlBean> convertToRaceControls(List<CourseControlRowData> input, Long eventNr) throws ProcessingException {
    if (input == null) {
      throw new IllegalArgumentException("input mandatory");
    }
    Map<Long, List<String>> replacementControls = null;
    if (eventNr != null) {
      replacementControls = ServerCache.getReplacementControls(eventNr);
    }

    List<RaceControlBean> plannedControls = new ArrayList<RaceControlBean>();
    long k = 0;
    Long lastSortCode = null;
    if (input.size() > 0 && CompareUtility.equals(input.get(0).getTypeUid(), ControlTypeCodeType.StartCode.ID)) {
      k = -1; // make sure a start has order 0, the first control = 1
    }
    for (CourseControlRowData row : input) {
      RaceControlBean bean = new RaceControlBean();
      bean.setCourseControlNr(row.getCourseControlNr());
      bean.setControlNr(row.getControlNr());
      bean.setControlNo(row.getControlNo());
      // handle freeorder sets
      if (row.getSortCode() == null || !CompareUtility.equals(lastSortCode, row.getSortCode())) {
        k++;
      }
      bean.setSortcode(k);
      bean.setCountLeg(row.isCountLeg());
      bean.setMandatory(row.isMandatory());
      bean.setTypeUid(row.getTypeUid());
      // replacement controls
      if (replacementControls != null) {
        List<String> controlList = replacementControls.get(bean.getControlNr());
        bean.setReplacementControlsByList(controlList);
      }
      plannedControls.add(bean);

      lastSortCode = row.getSortCode();
    }
    return plannedControls;
  }

  static class Variant {
    private final String variantId;
    private final Loop fork;
    private final List<CourseControlRowData> controls;

    public Variant(Loop fork, String variantId) {
      super();
      this.fork = fork;
      this.variantId = variantId;
      controls = new ArrayList<>();
    }

    public Loop getLoop() {
      return fork;
    }

    public String getVariantId() {
      return variantId;
    }

    public List<CourseControlRowData> getControls() {
      return controls;
    }

    @Override
    public String toString() {
      return variantId;
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((fork == null) ? 0 : fork.hashCode());
      result = prime * result + ((variantId == null) ? 0 : variantId.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof Variant)) {
        return false;
      }
      Variant other = (Variant) obj;
      if (fork == null) {
        if (other.fork != null) {
          return false;
        }
      }
      else if (!fork.equals(other.fork)) {
        return false;
      }
      if (variantId == null) {
        if (other.variantId != null) {
          return false;
        }
      }
      else if (!variantId.equals(other.variantId)) {
        return false;
      }
      return true;
    }

  }

  static class Loop {
    private final Long masterControlNr;
    private final Set<Variant> variants;
    private final Map<String, Variant> variantIndex;
    private List<List<Variant>> permutations;

    public Loop(Long masterControlNr) {
      super();
      this.masterControlNr = masterControlNr;
      this.variants = new HashSet<>();
      variantIndex = new HashMap<>();
    }

    public void addVariant(Variant variant) {
      variants.add(variant);
      variantIndex.put(variant.getVariantId(), variant);
    }

    public Set<Variant> getVariants() {
      return variants;
    }

    public Variant findVariant(String variantCode) {
      return variantIndex.get(variantCode);
    }

    public Long getMasterControlNr() {
      return masterControlNr;
    }

    public List<List<Variant>> getPermutations() {
      if (permutations == null) {
        throw new IllegalArgumentException("Permutations have not yet been calculated");
      }
      return permutations;
    }

    public void setPermutations(List<List<Variant>> permutations) {
      this.permutations = permutations;
    }

    @Override
    public String toString() {
      return String.valueOf(masterControlNr);
    }

    @Override
    public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((masterControlNr == null) ? 0 : masterControlNr.hashCode());
      return result;
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) {
        return true;
      }
      if (obj == null) {
        return false;
      }
      if (!(obj instanceof Loop)) {
        return false;
      }
      Loop other = (Loop) obj;
      if (masterControlNr == null) {
        if (other.masterControlNr != null) {
          return false;
        }
      }
      else if (!masterControlNr.equals(other.masterControlNr)) {
        return false;
      }
      return true;
    }

  }

}
