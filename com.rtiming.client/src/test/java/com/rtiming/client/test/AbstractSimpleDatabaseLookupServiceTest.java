package com.rtiming.client.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.List;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.services.lookup.ILookupRow;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public abstract class AbstractSimpleDatabaseLookupServiceTest {

  private LookupCall m_lookupCall;
  private long m_key = 0;
  private String m_text;

  /**
   * sets the lookup call, through which the corresponding lookup service is tested
   * 
   * @param lookupCall
   */
  public void setLookupCall(LookupCall lookupCall) {
    this.m_lookupCall = lookupCall;
  }

  /**
   * sets the key for testing the lookup by key
   * 
   * @param key
   * @throws ProcessingException
   */
  public void setKey(long key) throws ProcessingException {
    this.m_key = key;
  }

  /**
   * sets the text for testing the lookup by text
   * 
   * @param text
   * @throws ProcessingException
   */
  public void setText(String text) throws ProcessingException {
    this.m_text = text;
  }

  public long getKey() throws ProcessingException {
    return m_key;
  }

  public String getText() throws ProcessingException {
    return m_text;
  }

  /**
   * Scan lookup rows for the inserted test row
   * 
   * @param rows
   * @return
   */
  private ILookupRow searchRows(List<? extends ILookupRow<?>> rows) {
    assertNotNull(rows);
    assertTrue("lookup should provide at least one row (the inserted one)", rows.size() > 0);
    for (ILookupRow row : rows) {
      assertNotNull(row.getKey());
      if (row.getKey().equals(m_key)) {
        return row;
      }
    }
    fail("inserted row should be in lookup result");
    return null;
  }

  /**
   * Checks if the lookup text is valid, depends on the lookup call. Intended to be overwritten (strengthen) by child
   * class
   * 
   * @param textToCheck
   */
  protected void checkText(String textToCheck) {
    assertNotNull("text from lookup must not be null", textToCheck);
    assertTrue(textToCheck.contains(m_text));
  }

  /**
   * test case: lookup by key, using <code> getKey() </code> as key
   */
  @Test
  public void testLookupServiceByKey() throws ProcessingException {
    m_lookupCall.setKey(m_key);
    List<? extends ILookupRow<?>> rows = m_lookupCall.getDataByKey();
    assertNotNull(rows);
    assertEquals("key lookup should provide just one row", 1, rows.size());
    assertEquals("key obtained from key lookup should be equal to the key set before lookup", m_key, rows.get(0).getKey());
    checkText(rows.get(0).getText());
  }

  /**
   * test case: lookup by text, using <code> getText() </code> as text
   */
  @Test
  public void testLookupServiceByText() throws ProcessingException {
    m_lookupCall.setText(m_text);
    List<? extends ILookupRow<?>> rows = m_lookupCall.getDataByText();
    ILookupRow insertedRow = searchRows(rows);
    checkText(insertedRow.getText());
  }

  /**
   * test case: lookup by all
   */
  @Test
  public void testLookupServiceByAll() throws ProcessingException {
    List<? extends ILookupRow<?>> rows = m_lookupCall.getDataByAll();
    ILookupRow insertedRow = searchRows(rows);
    checkText(insertedRow.getText());
  }

  @Before
  public final void before() throws ProcessingException {
    insertTestRow();
    init();
    assertNotNull("No lookup call set", m_lookupCall);
    assertNotNull("No text for lookup call set", m_text);
    assertTrue("No key for lookup call set", m_key > 0);
  }

  @After
  public final void after() throws ProcessingException {
    deleteTestRow();
  }

  /**
   * <P>
   * Intended to initialize lookup call, key and text
   * </P>
   * <P>
   * If no key is set (or set to <code>0</code>), a new value from ORS_SEQ is drawn.
   * </P>
   * 
   * @throws ProcessingException
   */
  protected abstract void init() throws ProcessingException;

  /**
   * <p>
   * Insert test row
   * </p>
   * <p>
   * The inserted row must be found by a lookup for all rows (e.g. it should be active where applicable)
   * </p>
   */
  protected abstract void insertTestRow() throws ProcessingException;

  /**
   * Delete test row
   * 
   * @throws ProcessingException
   */
  protected abstract void deleteTestRow() throws ProcessingException;
}
