package com.rtiming.server.common.database.jpa;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.SQLException;
import java.util.List;

import javax.persistence.PersistenceException;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.holders.IHolder;
import org.eclipse.scout.rt.platform.holders.NVPair;
import org.eclipse.scout.rt.platform.reflect.FastBeanInfo;
import org.eclipse.scout.rt.platform.reflect.FastPropertyDescriptor;
import org.eclipse.scout.rt.platform.util.BeanUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.server.jdbc.parsers.BindModel;
import org.eclipse.scout.rt.server.jdbc.parsers.BindParser;
import org.eclipse.scout.rt.server.jdbc.parsers.token.IToken;
import org.eclipse.scout.rt.server.jdbc.parsers.token.ValueInputToken;
import org.eclipse.scout.rt.shared.data.form.fields.AbstractValueFieldData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.server.ServerSession;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;

/**
 * 
 */
public final class JPAUtility {
  private static final Logger LOG = LoggerFactory.getLogger(JPAUtility.class);

  public static Object[][] convertList2Array(List list) {
    Object[][] result = new Object[list.size()][0];
    for (int lineNumber = 0; lineNumber < list.size(); lineNumber++) {
      Object rawRow = list.get(lineNumber);
      Object[] row = null;
      try {
        // jpa return array
        row = (Object[]) rawRow;
      }
      catch (ClassCastException e) {
        // jpa returns single value
        row = new Object[1];
        row[0] = rawRow;
      }
      result[lineNumber] = row;
    }
    return result;
  }

  public static void setAutoParameters(FMilaQuery query, String queryString, Object bean) {
    if (queryString.contains(":sessionClientNr")) {
      query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    }
    FastBeanInfo info = BeanUtility.getFastBeanInfo(bean.getClass(), null);
    for (final FastPropertyDescriptor desc : info.getPropertyDescriptors()) {
      if (queryString.contains(":" + desc.getName() + " ") || queryString.contains(":" + desc.getName() + ",")) {
        Method readMethod = desc.getReadMethod();
        try {
          Object value = readMethod.invoke(bean);
          if (value instanceof AbstractValueFieldData<?>) {
            value = ((AbstractValueFieldData<?>) value).getValue();
          }
          query.setParameter(desc.getName(), value);
        }
        catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
          e.printStackTrace();
        }
      }
    }
  }

  public static final void select(String queryString, Object... bindBases) throws ProcessingException {
    String[] queryStringParts = StringUtility.split(queryString, " INTO ");
    BindParser parser = new BindParser(queryStringParts[0]);
    BindModel model = parser.parse();
    IToken[] tokens = model.getIOTokens();
    FMilaTypedQuery<Object[]> query = JPA.createQuery(queryStringParts[0], Object[].class);
    for (IToken token : tokens) {
      if (token.isInput()) {
        Object value = null;
        for (Object bindBase : bindBases) {
          if (bindBase == null) {
            continue;
          }
          if (bindBase instanceof NVPair) {
            NVPair bindBaseNVPair = (NVPair) bindBase;
            if (queryStringParts[0].contains(":" + bindBaseNVPair.getName())) {
              query.setParameter(bindBaseNVPair.getName(), bindBaseNVPair.getValue());
            }
          }
          else {
            FastBeanInfo info = BeanUtility.getFastBeanInfo(bindBase.getClass(), null);
            for (final FastPropertyDescriptor desc : info.getPropertyDescriptors()) {
              String tokenString = token.getParsedToken();
              tokenString = StringUtility.removePrefixes(tokenString, ":");
              if (tokenString.equalsIgnoreCase(desc.getName())) {
                Method readMethod = desc.getReadMethod();
                try {
                  value = readMethod.invoke(bindBase);
                  if (value instanceof AbstractValueFieldData<?>) {
                    value = ((AbstractValueFieldData<?>) value).getValue();
                  }
                }
                catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                  e.printStackTrace();
                }
              }
            }
          }
        }
        String name = ((ValueInputToken) token).getName();
        if (name.equals("clientNr") && value == null) {
          query.setParameter(name, ServerSession.get().getSessionClientNr());
        }
        else {
          query.setParameter(name, value);
        }
      }
      if (queryStringParts[0].contains(":sessionClientNr")) {
        query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
      }
    }
    List result = query.getResultList();
    if (result.size() > 0) {
      Object[] row;
      if (result.get(0) instanceof Object[]) {
        row = (Object[]) result.get(0);
      }
      else {
        row = new Object[]{result.get(0)};
      }
      String[] intos = StringUtility.split(queryStringParts[1], ",");
      int k = 0;
      if (row == null) {
        row = new Object[]{null};
      }
      for (Object value : row) {
        String into = intos[k];
        into = StringUtility.removePrefixes(StringUtility.trim(into), ":");
        k++;
        for (Object bindBase : bindBases) {
          if (bindBase instanceof NVPair) {
            NVPair bindBaseNVPair = (NVPair) bindBase;
            if (bindBaseNVPair.getName().equals(into)) {
              IHolder holder = (IHolder) bindBaseNVPair.getValue();
              setHolderValueInternal(value, holder);
            }
          }
          else {
            FastBeanInfo info = BeanUtility.getFastBeanInfo(bindBase.getClass(), null);
            for (final FastPropertyDescriptor desc : info.getPropertyDescriptors()) {
              if (into.equalsIgnoreCase(desc.getName())) {
                if (desc.getWriteMethod() != null) {
                  Method writeMethod = desc.getWriteMethod();
                  try {
                    writeMethod.invoke(bindBase, value);
                  }
                  catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                  }
                }
                else {
                  Method readMethod = desc.getReadMethod();
                  try {
                    Object object = readMethod.invoke(bindBase);
                    if (object instanceof AbstractValueFieldData<?>) {
                      setValueFieldValueInternal(value, object);
                    }
                  }
                  catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
                    e.printStackTrace();
                  }
                }
              }
            }
          }
        }
      }
    }
  }

  @SuppressWarnings("unchecked")
  private static void setValueFieldValueInternal(Object value, Object object) {
    ((AbstractValueFieldData) object).setValue(value);
  }

  @SuppressWarnings("unchecked")
  private static void setHolderValueInternal(Object value, IHolder holder) {
    holder.setValue(value);
  }

  public static void handlePersistenceException(PersistenceException e) throws VetoException {
    if (e.getCause().getCause() instanceof SQLException) {
      SQLException sqlException = (SQLException) e.getCause().getCause();
      if (StringUtility.equalsIgnoreCase(sqlException.getSQLState(), "23505")) {
        // unique_violation
        handleDataAlreadyExistsViolation(sqlException);
      }
      else if (StringUtility.equalsIgnoreCase(sqlException.getSQLState(), "23503")) {
        // foreign_key_violation
        handleDataInUseViolation(sqlException);
      }
      else {
        LOG.error("unknown error", e);
        throw new VetoException(sqlException.getLocalizedMessage());
      }
    }
    else {
      LOG.error("unknown error", e);
      throw new VetoException(e.getLocalizedMessage());
    }
  }

  private static void handleDataAlreadyExistsViolation(SQLException e) throws VetoException {
    String message = Texts.get("DuplicateKeyMessage") + FMilaUtility.LINE_SEPARATOR + FMilaUtility.LINE_SEPARATOR + " (" + e.getLocalizedMessage() + ")";
    throw new VetoException(message);
  }

  private static void handleDataInUseViolation(SQLException e) throws VetoException {
    String message = Texts.get("DataInUseMessage") + FMilaUtility.LINE_SEPARATOR + FMilaUtility.LINE_SEPARATOR + " (" + e.getLocalizedMessage() + ")";
    throw new VetoException(message);
  }

}
