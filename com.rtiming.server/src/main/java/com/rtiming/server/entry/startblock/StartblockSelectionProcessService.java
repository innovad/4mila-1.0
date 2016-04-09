package com.rtiming.server.entry.startblock;

import java.util.ArrayList;
import java.util.Arrays;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.BooleanUtility;
import org.eclipse.scout.rt.shared.services.common.security.ACCESS;

import com.rtiming.server.ServerSession;
import com.rtiming.server.common.database.jpa.FMilaQuery;
import com.rtiming.server.common.database.jpa.JPA;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.security.permission.CreateStartblockSelectionPermission;
import com.rtiming.shared.common.security.permission.ReadStartblockSelectionPermission;
import com.rtiming.shared.common.security.permission.UpdateStartblockSelectionPermission;
import com.rtiming.shared.entry.startblock.IStartblockSelectionProcessService;
import com.rtiming.shared.entry.startblock.StartblockSelectionFormData;

public class StartblockSelectionProcessService  implements IStartblockSelectionProcessService {

  @Override
  public StartblockSelectionFormData prepareCreate(StartblockSelectionFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateStartblockSelectionPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    formData.getOverwrite().setValue(true);

    return formData;
  }

  @Override
  public StartblockSelectionFormData create(StartblockSelectionFormData formData) throws ProcessingException {
    if (!ACCESS.check(new CreateStartblockSelectionPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    String where = "AND COALESCE(startblockUid,0) = 0 ";
    if (BooleanUtility.nvl(formData.getOverwrite().getValue(), false)) {
      where = "";
    }

    String queryString = "UPDATE RtParticipation " +
        "SET startblockUid = :startblockUid " +
        "WHERE id.eventNr = :eventNr " +
        "AND id.entryNr IN :entryNrs " +
        "AND id.clientNr = :sessionClientNr " +
        where;
    FMilaQuery query = JPA.createQuery(queryString);
    query.setParameter("startblockUid", formData.getStartblockUid().getValue());
    query.setParameter("eventNr", formData.getEventNr());
    ArrayList<Long> list = new ArrayList<Long>();
    list.add(-1L);
    query.setParameter("entryNrs", formData.getEntryNrs() == null ? list : Arrays.asList(formData.getEntryNrs()));
    query.setParameter("sessionClientNr", ServerSession.get().getSessionClientNr());
    query.executeUpdate();

    return formData;
  }

  @Override
  public StartblockSelectionFormData load(StartblockSelectionFormData formData) throws ProcessingException {
    if (!ACCESS.check(new ReadStartblockSelectionPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }

  @Override
  public StartblockSelectionFormData store(StartblockSelectionFormData formData) throws ProcessingException {
    if (!ACCESS.check(new UpdateStartblockSelectionPermission())) {
      throw new VetoException(Texts.get("AuthorizationFailed"));
    }

    return formData;
  }
}
