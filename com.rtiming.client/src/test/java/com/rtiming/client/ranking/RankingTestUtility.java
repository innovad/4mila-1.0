package com.rtiming.client.ranking;

import org.mockito.Mockito;

import com.rtiming.shared.ranking.AbstractRankingBoxData;
import com.rtiming.shared.ranking.AbstractRankingBoxData.DecimalPlaces;
import com.rtiming.shared.ranking.AbstractRankingBoxData.Format;
import com.rtiming.shared.ranking.AbstractRankingBoxData.Sorting;

/**
 * @author amo
 */
public class RankingTestUtility {

  public static AbstractRankingBoxData createFormulaSettings(Long rankingFormatUid, Long decimalPlaces, Long sortingUid) {
    AbstractRankingBoxData formulaSettings = Mockito.mock(AbstractRankingBoxData.class);
    Sorting sorting = Mockito.mock(Sorting.class);
    Mockito.when(sorting.getValue()).thenReturn(sortingUid);
    Mockito.when(formulaSettings.getSorting()).thenReturn(sorting);
    Format format = Mockito.mock(Format.class);
    Mockito.when(format.getValue()).thenReturn(rankingFormatUid);
    Mockito.when(formulaSettings.getFormat()).thenReturn(format);
    DecimalPlaces decimal = Mockito.mock(DecimalPlaces.class);
    Mockito.when(decimal.getValue()).thenReturn(decimalPlaces);
    Mockito.when(formulaSettings.getDecimalPlaces()).thenReturn(decimal);
    return formulaSettings;
  }

}
