package com.rtiming.client.entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CompareUtility;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import com.rtiming.client.entry.EntryForm.MainBox.TabBox.EventsBox.EventsField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.EventsBox.EventsField.Table.EventClassColumn;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.EventsBox.EventsField.Table.EventNrColumn;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.EventsBox.EventsField.Table.StartTimeColumn;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table.LegColumn;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table.LegStartTimeColumn;
import com.rtiming.client.entry.EntryForm.MainBox.TabBox.RacesBox.RacesField.Table.RaceEventColumn;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.entry.EventConfiguration;
import com.rtiming.shared.event.EventClassFormData;
import com.rtiming.shared.event.course.ClassTypeCodeType;

/**
 * @author amo
 */
public class EntryFormUtilityTest {

  private enum updateMethod {
    UPDATE_RACE, UPDATE_PARTICIPATION
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateRacesTable1() throws Exception {
    EntryFormUtility.validateRacesTable(null, null, null);
  }

  @Test(expected = VetoException.class)
  public void testValidateRacesTable2() throws Exception {
    EventConfiguration configuration = new EventConfiguration();
    RacesField.Table raceTable = mockRacesField(5L, null);
    List<Long> availableEvents = new ArrayList<>();
    EntryFormUtility.validateRacesTable(raceTable, availableEvents, configuration);
  }

  @Test
  public void testValidateRacesTable3() throws Exception {
    EventConfiguration configuration = new EventConfiguration();
    RacesField.Table raceTable = mockRacesField(5L, null);
    List<Long> availableEvents = new ArrayList<>();
    availableEvents.add(5L);
    EntryFormUtility.validateRacesTable(raceTable, availableEvents, configuration);
    // column cell should be cleared
    Mockito.verify(raceTable.getLegColumn(), Mockito.times(1)).setValue(0, null);
  }

  @Test
  public void testValidateRacesTable4() throws Exception {
    EventConfiguration configuration = new EventConfiguration();
    RacesField.Table raceTable = mockRacesField(null, null);
    List<Long> availableEvents = new ArrayList<>();
    availableEvents.add(5L);
    EntryFormUtility.validateRacesTable(raceTable, availableEvents, configuration);
    // column cell should NOT be cleared
    Mockito.verify(raceTable.getLegColumn(), Mockito.times(0)).setValue(0, null);
  }

  @Test
  public void testValidateRacesTable5() throws Exception {
    EventConfiguration configuration = createConfiguration(5L, 5L, 37L, ClassTypeCodeType.IndividualEventCode.ID);
    RacesField.Table raceTable = mockRacesField(5L, 37L);
    List<Long> availableEvents = new ArrayList<>();
    availableEvents.add(5L);
    EntryFormUtility.validateRacesTable(raceTable, availableEvents, configuration);
    // column cell should NOT be cleared
    Mockito.verify(raceTable.getLegColumn(), Mockito.times(0)).setValue(0, null);
  }

  @Test
  public void testValidateRacesTable6() throws Exception {
    EventConfiguration configuration = createConfiguration(5L, 6L, 37L, ClassTypeCodeType.IndividualEventCode.ID);
    RacesField.Table raceTable = mockRacesField(5L, 37L);
    List<Long> availableEvents = new ArrayList<>();
    availableEvents.add(5L);
    EntryFormUtility.validateRacesTable(raceTable, availableEvents, configuration);
    // column cell should be cleared
    Mockito.verify(raceTable.getLegColumn(), Mockito.times(1)).setValue(0, null);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testValidateTeamSize1() throws Exception {
    EntryFormUtility.validateTeamSize(null, null, 0L, null);
  }

  @Test(expected = ProcessingException.class)
  public void testValidateTeamSize2() throws Exception {
    Long eventNr = 5L;
    Long classUid = 5L;
    long count = 3;
    EventConfiguration configuration = new EventConfiguration();
    EntryFormUtility.validateTeamSize(eventNr, classUid, count, configuration);
  }

  @Test(expected = ProcessingException.class)
  public void testValidateTeamSize3a() throws Exception {
    Long eventNr = 5L;
    Long classUid = 6L;
    long count = 3;
    EventConfiguration configuration = createTeamConfiguration(eventNr, classUid, ClassTypeCodeType.IndividualEventCode.ID, null, null);
    EntryFormUtility.validateTeamSize(eventNr, classUid, count, configuration);
  }

  @Test(expected = ProcessingException.class)
  public void testValidateTeamSize3b() throws Exception {
    Long eventNr = 5L;
    Long classUid = 6L;
    long count = 3;
    EventConfiguration configuration = createTeamConfiguration(eventNr, classUid, ClassTypeCodeType.IndividualEventCode.ID, 1L, null);
    EntryFormUtility.validateTeamSize(eventNr, classUid, count, configuration);
  }

  @Test(expected = ProcessingException.class)
  public void testValidateTeamSize3c() throws Exception {
    Long eventNr = 5L;
    Long classUid = 6L;
    long count = 3;
    EventConfiguration configuration = createTeamConfiguration(eventNr, classUid, ClassTypeCodeType.IndividualEventCode.ID, null, 1L);
    EntryFormUtility.validateTeamSize(eventNr, classUid, count, configuration);
  }

  @Test(expected = ProcessingException.class)
  public void testValidateTeamSize3d() throws Exception {
    Long eventNr = 5L;
    Long classUid = 6L;
    long count = 3;
    EventConfiguration configuration = createTeamConfiguration(eventNr, classUid, null, 2L, 1L);
    EntryFormUtility.validateTeamSize(eventNr, classUid, count, configuration);
  }

  @Test
  public void testValidateTeamSize4() throws Exception {
    Long eventNr = 5L;
    Long classUid = 6L;
    long count = 0;
    EventConfiguration configuration = createTeamConfiguration(eventNr, classUid, ClassTypeCodeType.IndividualEventCode.ID, 0L, 1L);
    EntryFormUtility.validateTeamSize(eventNr, classUid, count, configuration);
  }

  @Test
  public void testValidateTeamSize5() throws Exception {
    Long eventNr = 5L;
    Long classUid = 6L;
    long count = 1;
    EventConfiguration configuration = createTeamConfiguration(eventNr, classUid, ClassTypeCodeType.IndividualEventCode.ID, 0L, 1L);
    EntryFormUtility.validateTeamSize(eventNr, classUid, count, configuration);
  }

  @Test(expected = VetoException.class)
  public void testValidateTeamSize6() throws Exception {
    Long eventNr = 5L;
    Long classUid = 6L;
    long count = 6;
    EventConfiguration configuration = createTeamConfiguration(eventNr, classUid, ClassTypeCodeType.IndividualEventCode.ID, 3L, 5L);
    EntryFormUtility.validateTeamSize(eventNr, classUid, count, configuration);
  }

  @Test
  public void testValidateTeamSize7() throws Exception {
    Long eventNr = 5L;
    Long classUid = 6L;
    long count = 1;
    EventConfiguration configuration = createTeamConfiguration(eventNr, classUid, ClassTypeCodeType.IndividualEventCode.ID, 1L, 1L);
    EntryFormUtility.validateTeamSize(eventNr, classUid, count, configuration);
  }

  @Test(expected = VetoException.class)
  public void testValidateTeamSize8() throws Exception {
    Long eventNr = 5L;
    Long classUid = 6L;
    long count = 2;
    EventConfiguration configuration = createTeamConfiguration(eventNr, classUid, ClassTypeCodeType.IndividualEventCode.ID, 1L, 1L);
    EntryFormUtility.validateTeamSize(eventNr, classUid, count, configuration);
  }

  @Test(expected = ProcessingException.class)
  public void testCheckClassesNotEmpty1() throws Exception {
    EntryFormUtility.checkClassesNotEmpty(null);
  }

  @Test
  public void testCheckClassesNotEmpty2() throws Exception {
    RacesField.Table racesTable = mockRacesFieldWithClassUids(new Long[]{1L});
    EntryFormUtility.checkClassesNotEmpty(racesTable);
  }

  @Test(expected = VetoException.class)
  public void testCheckClassesNotEmpty3() throws Exception {
    RacesField.Table racesTable = mockRacesFieldWithClassUids(new Long[]{1L, null, 3L});
    EntryFormUtility.checkClassesNotEmpty(racesTable);
  }

  @Test
  public void testCheckClassesNotEmpty4() throws Exception {
    RacesField.Table racesTable = mockRacesFieldWithClassUids(new Long[]{1L, 2L, 3L});
    EntryFormUtility.checkClassesNotEmpty(racesTable);
  }

  @Test
  public void testSyncStartTimes1() throws Exception {
    Date[] participationStartTimes = new Date[]{new Date(555555)};
    Date[] raceStartTimes = new Date[]{new Date(444444)};
    Long[] eventNrs = new Long[]{1L};
    Long[] classEventNrs = new Long[]{1L};
    Long[] classUids = new Long[]{100L};
    Long[] classTypeUids = new Long[]{ClassTypeCodeType.IndividualEventCode.ID};
    Date[] expectedSetterCalls = new Date[]{new Date(555555)};
    Integer[] expectedSetterRows = new Integer[]{0};

    doSyncStartTimesTest(updateMethod.UPDATE_RACE, eventNrs, classEventNrs, classUids, classTypeUids, participationStartTimes, raceStartTimes, expectedSetterCalls, expectedSetterRows);
  }

  @Test
  public void testSyncStartTimes2() throws Exception {
    Date[] participationStartTimes = new Date[]{new Date(111111), new Date(222222)};
    Date[] raceStartTimes = new Date[]{new Date(333333), new Date(444444)};
    Long[] eventNrs = new Long[]{1L, 2L};
    Long[] classEventNrs = new Long[]{1L, 2L};
    Long[] classUids = new Long[]{100L, 100L};
    Long[] classTypeUids = new Long[]{ClassTypeCodeType.IndividualEventCode.ID, ClassTypeCodeType.IndividualEventCode.ID};
    Date[] expectedSetterCalls = new Date[]{new Date(111111), new Date(222222)};
    Integer[] expectedSetterRows = new Integer[]{0, 1};

    doSyncStartTimesTest(updateMethod.UPDATE_RACE, eventNrs, classEventNrs, classUids, classTypeUids, participationStartTimes, raceStartTimes, expectedSetterCalls, expectedSetterRows);
  }

  @Test
  public void testSyncStartTimes3() throws Exception {
    Date[] participationStartTimes = new Date[]{new Date(111111), new Date(222222)};
    Date[] raceStartTimes = new Date[]{new Date(333333), new Date(444444)};
    Long[] eventNrs = new Long[]{1L, 2L};
    Long[] classEventNrs = new Long[]{1L, 2L};
    Long[] classUids = new Long[]{100L, 100L};
    Long[] classTypeUids = new Long[]{ClassTypeCodeType.IndividualEventCode.ID, ClassTypeCodeType.IndividualEventCode.ID};
    Date[] expectedSetterCalls = new Date[]{new Date(333333), new Date(444444)};
    Integer[] expectedSetterRows = new Integer[]{0, 1};

    doSyncStartTimesTest(updateMethod.UPDATE_PARTICIPATION, eventNrs, classEventNrs, classUids, classTypeUids, participationStartTimes, raceStartTimes, expectedSetterCalls, expectedSetterRows);
  }

  @Test
  public void testSyncStartTimes4() throws Exception {
    Date[] participationStartTimes = new Date[]{new Date(111111), new Date(222222)};
    Date[] raceStartTimes = new Date[]{new Date(333333), new Date(444444)};
    Long[] eventNrs = new Long[]{1L, 2L};
    Long[] classEventNrs = new Long[]{1L, 2L};
    Long[] classUids = new Long[]{100L, 100L};
    Long[] classTypeUids = new Long[]{ClassTypeCodeType.RelayCode.ID, ClassTypeCodeType.RelayCode.ID};
    Date[] expectedSetterCalls = new Date[]{};
    Integer[] expectedSetterRows = new Integer[]{};

    doSyncStartTimesTest(updateMethod.UPDATE_PARTICIPATION, eventNrs, classEventNrs, classUids, classTypeUids, participationStartTimes, raceStartTimes, expectedSetterCalls, expectedSetterRows);
  }

  @Test
  public void testSyncStartTimes5() throws Exception {
    Date[] participationStartTimes = new Date[]{new Date(111111), new Date(222222)};
    Date[] raceStartTimes = new Date[]{new Date(333333), new Date(444444)};
    Long[] eventNrs = new Long[]{1L, 2L};
    Long[] classEventNrs = new Long[]{1L, 2L};
    Long[] classUids = new Long[]{100L, 100L};
    Long[] classTypeUids = new Long[]{ClassTypeCodeType.RelayCode.ID, ClassTypeCodeType.IndividualEventCode.ID};
    Date[] expectedSetterCalls = new Date[]{new Date(444444)};
    Integer[] expectedSetterRows = new Integer[]{1};

    doSyncStartTimesTest(updateMethod.UPDATE_PARTICIPATION, eventNrs, classEventNrs, classUids, classTypeUids, participationStartTimes, raceStartTimes, expectedSetterCalls, expectedSetterRows);
  }

  @Test
  public void testSyncStartTimes6() throws Exception {
    Date[] participationStartTimes = new Date[]{new Date(111111)};
    Date[] raceStartTimes = new Date[]{new Date(333333), new Date(444444)};
    Long[] eventNrs = new Long[]{1L};
    Long[] classEventNrs = new Long[]{1L, 1L};
    Long[] classUids = new Long[]{100L, 100L};
    Long[] classTypeUids = new Long[]{ClassTypeCodeType.IndividualEventCode.ID, ClassTypeCodeType.IndividualEventCode.ID};
    Date[] expectedSetterCalls = new Date[]{new Date(333333)};
    Integer[] expectedSetterRows = new Integer[]{0};

    doSyncStartTimesTest(updateMethod.UPDATE_PARTICIPATION, eventNrs, classEventNrs, classUids, classTypeUids, participationStartTimes, raceStartTimes, expectedSetterCalls, expectedSetterRows);
  }

  private void doSyncStartTimesTest(updateMethod method, Long[] eventNrs, Long[] classEventNrs, Long[] classUids, Long[] classTypeUids, Date[] participationStartTimes, Date[] raceStartTimes, Date[] expectedSetterCalls, Integer[] expectedSetterRows) throws ProcessingException {
    RacesField.Table racesTable = Mockito.mock(RacesField.Table.class);
    Mockito.when(racesTable.getRowCount()).thenReturn(raceStartTimes.length);

    LegStartTimeColumn legStartTimeColumn = Mockito.mock(LegStartTimeColumn.class);
    for (int k = 0; k < raceStartTimes.length; k++) {
      Mockito.when(racesTable.getLegStartTimeColumn()).thenReturn(legStartTimeColumn);
      Mockito.when(legStartTimeColumn.getValue(k)).thenReturn(raceStartTimes[k]);
    }

    RaceEventColumn raceEventColumn = Mockito.mock(RaceEventColumn.class);
    for (int k = 0; k < eventNrs.length; k++) {
      Mockito.when(raceEventColumn.getValue(k)).thenReturn(eventNrs[k]);
      Mockito.when(racesTable.getRaceEventColumn()).thenReturn(raceEventColumn);
    }

    EventsField.Table eventsTable = Mockito.mock(EventsField.Table.class);
    Mockito.when(eventsTable.getRowCount()).thenReturn(participationStartTimes.length);

    StartTimeColumn startTimeColumn = Mockito.mock(StartTimeColumn.class);
    for (int k = 0; k < participationStartTimes.length; k++) {
      Mockito.when(eventsTable.getStartTimeColumn()).thenReturn(startTimeColumn);
      Mockito.when(startTimeColumn.getValue(k)).thenReturn(participationStartTimes[k]);
    }

    EventNrColumn eventNrColumn = Mockito.mock(EventNrColumn.class);
    for (int k = 0; k < eventNrs.length; k++) {
      Mockito.when(eventsTable.getEventNrColumn()).thenReturn(eventNrColumn);
      Mockito.when(eventNrColumn.getValue(k)).thenReturn(eventNrs[k]);
    }

    EventClassColumn eventClassColumn = Mockito.mock(EventClassColumn.class);
    for (int k = 0; k < classUids.length; k++) {
      Mockito.when(eventsTable.getEventClassColumn()).thenReturn(eventClassColumn);
      Mockito.when(eventClassColumn.getValue(k)).thenReturn(classUids[k]);
    }

    EventConfiguration configuration = new EventConfiguration();
    for (int k = 0; k < classEventNrs.length; k++) {
      EventClassFormData clazz = new EventClassFormData();
      clazz.getEvent().setValue(classEventNrs[k]);
      clazz.getClazz().setValue(classUids[k]);
      clazz.getType().setValue(classTypeUids[k]);
      configuration.addClass(clazz);
    }
    for (int k = 0; k < eventNrs.length; k++) {
      EventBean event = new EventBean();
      event.setEventNr(eventNrs[k]);
      configuration.addEvents(event);
    }

    if (CompareUtility.equals(method, updateMethod.UPDATE_RACE)) {
      EntryFormUtility.updateRaceStartTimes(racesTable, eventsTable, configuration);
      Mockito.verify(startTimeColumn, Mockito.never()).setValue(Mockito.anyInt(), Mockito.any(Date.class));

      ArgumentCaptor<Integer> rowR = ArgumentCaptor.forClass(Integer.class);
      ArgumentCaptor<Date> dateR = ArgumentCaptor.forClass(Date.class);
      Mockito.verify(legStartTimeColumn, Mockito.times(expectedSetterCalls.length)).setValue(rowR.capture(), dateR.capture());
      for (int k = 0; k < expectedSetterCalls.length; k++) {
        Assert.assertEquals("Race Date", expectedSetterCalls[k], dateR.getAllValues().get(k));
        Assert.assertEquals("Race Row", expectedSetterRows[k], rowR.getAllValues().get(k));
      }

    }
    else {
      EntryFormUtility.updateParticipationStartTimes(racesTable, eventsTable, configuration);
      Mockito.verify(legStartTimeColumn, Mockito.never()).setValue(Mockito.anyInt(), Mockito.any(Date.class));

      ArgumentCaptor<Integer> rowP = ArgumentCaptor.forClass(Integer.class);
      ArgumentCaptor<Date> dateP = ArgumentCaptor.forClass(Date.class);
      Mockito.verify(startTimeColumn, Mockito.times(expectedSetterCalls.length)).setValue(rowP.capture(), dateP.capture());
      for (int k = 0; k < expectedSetterCalls.length; k++) {
        Assert.assertEquals("Participation Date", expectedSetterCalls[k], dateP.getAllValues().get(k));
        Assert.assertEquals("Participation Row", expectedSetterRows[k], rowP.getAllValues().get(k));
      }

    }

  }

  private RacesField.Table mockRacesFieldWithClassUids(Long[] classUids) {
    RacesField.Table racesTable = Mockito.mock(RacesField.Table.class);
    LegColumn legColumn = Mockito.mock(LegColumn.class);
    Mockito.when(racesTable.getLegColumn()).thenReturn(legColumn);
    Mockito.when(legColumn.getValues()).thenReturn(Arrays.asList(classUids));
    return racesTable;
  }

  private EventConfiguration createTeamConfiguration(Long eventNr, Long classUid, Long typeUid, Long min, Long max) {
    EventConfiguration configuration = new EventConfiguration();
    EventClassFormData formData = new EventClassFormData();
    formData.getEvent().setValue(eventNr);
    formData.getClazz().setValue(classUid);
    formData.getType().setValue(typeUid);
    formData.getTeamSizeMin().setValue(min);
    formData.getTeamSizeMax().setValue(max);
    configuration.addClass(formData);
    return configuration;
  }

  private RacesField.Table mockRacesField(Long... eventAndClassNrs) {
    Assert.assertTrue(eventAndClassNrs.length % 2 == 0);
    RacesField.Table raceTable = Mockito.mock(RacesField.Table.class);
    Mockito.when(raceTable.getRowCount()).thenReturn(eventAndClassNrs.length / 2);
    for (int k = 0; k < eventAndClassNrs.length; k = k + 2) {
      RaceEventColumn raceEventColumn = Mockito.mock(RaceEventColumn.class);
      LegColumn legColumn = Mockito.mock(LegColumn.class);
      Mockito.when(raceTable.getRaceEventColumn()).thenReturn(raceEventColumn);
      Mockito.when(raceEventColumn.getValue(k / 2)).thenReturn(eventAndClassNrs[k / 2]);
      Mockito.when(raceTable.getLegColumn()).thenReturn(legColumn);
      Mockito.when(legColumn.getValue(k / 2)).thenReturn(eventAndClassNrs[k / 2 + 1]);
    }
    return raceTable;
  }

  private EventConfiguration createConfiguration(Long eventNr, Long classEventNr, Long classUid, Long typeUid) {
    EventConfiguration configuration = new EventConfiguration();
    EventBean event = new EventBean();
    event.setEventNr(eventNr);
    configuration.addEvents(event);
    EventClassFormData clazz = new EventClassFormData();
    clazz.getEvent().setValue(classEventNr);
    clazz.getClazz().setValue(classUid);
    clazz.getType().setValue(typeUid);
    configuration.addClass(clazz);
    return configuration;
  }

}
