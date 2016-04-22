/*******************************************************************************
 * Copyright (c) 2010 BSI Business Systems Integration AG.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the BSI CRM Software License v1.0
 * which accompanies this distribution as bsi-v10.html
 *
 * Contributors:
 *     BSI Business Systems Integration AG - initial API and implementation
 ******************************************************************************/
package com.rtiming.shared.test.completeness;

import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.inventory.ClassInventory;
import org.eclipse.scout.rt.platform.inventory.IClassInfo;
import org.eclipse.scout.rt.platform.util.StringUtility;

/**
 *
 */
public class TestCompletenessUtility {

  /**
   * @param bundle
   *          the bundle where test classes and classes to be tested are searched
   * @param classToBeTestedClass
   *          type of the classes to be tested
   * @param testClass
   *          type of the test classes
   * @param classToBeTestedClassFilter
   *          a suffix for quick filtering (e.g. TablePage)
   * @param testClassFilter
   *          a suffix for quick filtering (e.g. Test)
   * @param identifier
   *          a human-readable identifier the will be used in the JUnit assert message
   * @param exclusionList
   *          a list of classes that will be excluded from completeness checking
   * @return null if complete, {@link CompletenessResult} with error message otherwise
   * @throws ProcessingException
   */
  public static CompletenessResult testCompleteness(Class<?> classToBeTestedClass, Class<?> testClass, String classToBeTestedClassFilter, String testClassFilter, String identifier, List<Class<?>> exclusionList) throws ProcessingException {
    if (classToBeTestedClass == null || testClass == null || classToBeTestedClassFilter == null || testClassFilter == null || identifier == null) {
      throw new IllegalArgumentException("arguments must not be null");
    }

    List<String> tablePages = new ArrayList<String>();
    List<String> tests = new ArrayList<String>();
    if (exclusionList == null) {
      exclusionList = new ArrayList<Class<?>>();
    }

    // cannot use BundleBrowser, since classes from host and fragment must be found
    Set<IClassInfo> clazzes = ClassInventory.get().getAllKnownSubClasses(Object.class); // TODO MIG
    System.out.println("TestCompletenessUtility, Classes found: " + clazzes.size());

    for (IClassInfo clazzName : clazzes) {
      // quick pre-filtering
      if (clazzName.name().indexOf("$") >= 0) {
        continue;
      }
      if (!clazzName.name().endsWith(testClassFilter + ".class") && !clazzName.name().endsWith(classToBeTestedClassFilter + ".class")) {
        continue;
      }

      String convertedClassName = clazzName.name();
      try {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(convertedClassName);
        // real filtering
        if (!Modifier.isAbstract(clazz.getModifiers())) {
          if (classToBeTestedClass.isAssignableFrom(clazz)) {
            tablePages.add(clazz.getCanonicalName() + "Test");
          }
          else if (testClass.isAssignableFrom(clazz)) {
            tests.add(clazz.getCanonicalName());
          }
        }
      }
      catch (ClassNotFoundException e) {
        throw new ProcessingException("Failed to load " + convertedClassName);
      }
    }

    // Check missing tests
    List<String> missingTablePageTests = new ArrayList<>(tablePages);
    for (String test : tests) {
      missingTablePageTests.remove(test);
    }

    // Check wrong named tests
    List<String> wrongNamedTests = new ArrayList<>(tests);
    for (String tablePage : tablePages) {
      wrongNamedTests.remove(tablePage);
    }

    // Exclusion List
    for (Class<?> clazz : exclusionList) {
      missingTablePageTests.remove(clazz.getName() + "Test");
      wrongNamedTests.remove(clazz.getName());
    }

    // Output
    List<Object> testOutput = new ArrayList<Object>();
    for (String tablePage : missingTablePageTests) {
      testOutput.add(tablePage);
    }

    List<Object> tableOutput = new ArrayList<Object>();
    for (String tablePage : wrongNamedTests) {
      tableOutput.add(tablePage);
    }

    if (missingTablePageTests.size() > 0 || wrongNamedTests.size() > 0) {
      String missing = StringUtility.collectionToString(testOutput);
      String wrongNamed = StringUtility.collectionToString(tableOutput);
      int count = missingTablePageTests.size() + wrongNamedTests.size();
      String msg = count + " " + identifier + " Tests are missing/named incorrectly, please create the missing " + identifier + " tests (" + missing + ") and check the incorrectly named tests (" + wrongNamed + ")";
      return new CompletenessResult(msg, missingTablePageTests.size(), wrongNamedTests.size());
    }

    return null;
  }

  private static String convertToClassName(String clazzName) {
    // Eclipse
    clazzName = StringUtility.replace(clazzName, "/bin/", "");
    // Maven
    clazzName = StringUtility.replace(clazzName, "/target/classes/", "");
    // leading slashes
    if (clazzName.startsWith("/")) {
      clazzName = clazzName.substring(1);
    }
    // slashes
    clazzName = clazzName.replaceAll("[/]", ".");
    // suffix
    clazzName = StringUtility.removeSuffixes(clazzName, ".class");
    return clazzName;
  }

}
