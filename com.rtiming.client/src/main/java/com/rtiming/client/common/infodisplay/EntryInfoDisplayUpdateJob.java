package com.rtiming.client.common.infodisplay;

import org.eclipse.scout.rt.client.IClientSession;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.services.common.code.CODES;

import com.rtiming.client.entry.EntryForm;
import com.rtiming.shared.settings.currency.CurrencyCodeType;

public class EntryInfoDisplayUpdateJob extends AbstractInfoDisplayUpdateJob {

  private final EntryForm form;
  private final IClientSession session;

  public EntryInfoDisplayUpdateJob(EntryForm form, IClientSession session) throws ProcessingException {
    super(session);
    this.form = form;
    this.session = session;
  }

  @Override
  protected String prepareURL() throws ProcessingException {
    String lastName = form.getLastNameField().getDisplayText();
    String firstName = form.getFirstNameField().getDisplayText();
    String clazz = form.getClazzField().getDisplayText();
    String ecard = form.getECardField().getTooltipText();
    String fee = form.getFeesBox().getLabel();
    String currencyText = CODES.getCodeType(CurrencyCodeType.class).getCode(form.getCurrencyField().getValue()).getExtKey();

    // create html file
    String location = InfoDisplayUtility.getInfoDisplayHtmlUrl("infoDisplayEntry.html");

    location = InfoDisplayUtility.addParameter(location, "name", lastName + " " + StringUtility.emptyIfNull(firstName));
    location = InfoDisplayUtility.addParameter(location, "ecard", ecard);
    location = InfoDisplayUtility.addParameter(location, "clazz", clazz);
    fee = fee.replaceAll("[^\\.,0123456789]", "");
    location = InfoDisplayUtility.addParameter(location, "fee", currencyText + " " + fee);

    return location;
  }
}
