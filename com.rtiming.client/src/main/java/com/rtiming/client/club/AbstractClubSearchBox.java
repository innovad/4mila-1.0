package com.rtiming.client.club;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.DefaultSubtypeSdkCommand;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.smartfield.AbstractSmartField;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.shared.ScoutTexts;
import org.eclipse.scout.rt.shared.services.lookup.LookupCall;

import com.rtiming.shared.Texts;
import com.rtiming.shared.club.AbstractClubSearchBoxData;
import com.rtiming.shared.club.ClubLookupCall;
import com.rtiming.shared.runner.RunnerLookupCall;

@FormData(value = AbstractClubSearchBoxData.class, defaultSubtypeSdkCommand = DefaultSubtypeSdkCommand.CREATE, sdkCommand = SdkCommand.CREATE)
public abstract class AbstractClubSearchBox extends AbstractGroupBox {

  @Override
  protected String getConfiguredLabel() {
    return Texts.get("Clubs");
  }

  @Order(10.0)
  public class ShortcutField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Shortcut");
    }
  }

  @Order(20.0)
  public class NameField extends AbstractStringField {

    @Override
    protected String getConfiguredLabel() {
      return ScoutTexts.get("Name");
    }
  }

  @Order(30.0)
  public class ClubField extends AbstractSmartField<Long> {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Club");
    }

    @Override
    protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
      return ClubLookupCall.class;
    }
  }

  @Order(40.0)
  public class ContactPersonField extends AbstractSmartField<Long> {

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("ContactPerson");
    }

    @Override
    protected Class<? extends LookupCall<Long>> getConfiguredLookupCall() {
      return RunnerLookupCall.class;
    }
  }

}
