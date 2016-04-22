package com.rtiming.server.race.validation;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import com.rtiming.server.event.course.loop.CourseCalculatorTest;
import com.rtiming.server.event.course.loop.CourseLoopCalculatorTest;
import com.rtiming.server.event.course.loop.PathCalculatorTest;
import com.rtiming.server.event.course.loop.VariantPermutatorTest;

/**
 * @author amo
 */
@RunWith(Suite.class)
@SuiteClasses({
    LevenshteinUtilityTest.class,
    RaceValidationUtilityTest.class,
    RaceTimeUtilityTest.class,
    FreeOrderUtilityTest.class,
    ControlStatusUtilityTest.class,
    CourseLoopCalculatorTest.class,
    CourseCalculatorTest.class,
    VariantPermutatorTest.class,
    PathCalculatorTest.class
})
public class ValidationTestSuite {

}
