package com.rtiming.client.ranking;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author amo
 */
@RunWith(Suite.class)
@SuiteClasses({
    FormulaScriptTest.class,
    FormulaTest.class,
    RankingUtilityTest.class,
    EventRankingTest.class,
    FormulaScriptDefaultTest.class,
    SummaryRankingTest.class,
    AbstractRankingTest.class,
    FormulaUtilityTest.class})
public class RankingTestSuite {

}
