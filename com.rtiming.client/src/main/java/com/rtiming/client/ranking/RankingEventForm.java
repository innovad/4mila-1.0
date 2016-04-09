package com.rtiming.client.ranking;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.ui.fields.AbstractSortCodeField;
import com.rtiming.client.event.AbstractEventField;
import com.rtiming.client.ranking.RankingEventForm.MainBox.CancelButton;
import com.rtiming.client.ranking.RankingEventForm.MainBox.EventField;
import com.rtiming.client.ranking.RankingEventForm.MainBox.OkButton;
import com.rtiming.client.ranking.RankingEventForm.MainBox.RankingBox;
import com.rtiming.client.ranking.RankingEventForm.MainBox.SortCodeField;
import com.rtiming.client.ranking.RankingEventForm.MainBox.TestFormulaButton;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.security.permission.UpdateRankingEventPermission;
import com.rtiming.shared.dao.RtRankingEventKey;
import com.rtiming.shared.ranking.AbstractFormulaCode.RankingType;
import com.rtiming.shared.ranking.IRankingEventProcessService;
import com.rtiming.shared.ranking.RankingEventFormData;

@FormData(value = RankingEventFormData.class, sdkCommand = SdkCommand.CREATE)
public class RankingEventForm extends AbstractForm {

  private RtRankingEventKey key;

  public RankingEventForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Ranking");
  }

  public RankingBox getRankingBox() {
    return getFieldByClass(RankingBox.class);
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public EventField getEventField() {
    return getFieldByClass(EventField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  public SortCodeField getSortCodeField() {
    return getFieldByClass(SortCodeField.class);
  }

  public TestFormulaButton getTestFormulaButton() {
    return getFieldByClass(TestFormulaButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class EventField extends AbstractEventField {

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }
    }

    @Order(20.0)
    public class SortCodeField extends AbstractSortCodeField {

    }

    @Order(30.0)
    public class RankingBox extends AbstractRankingBox {

      @Override
      protected RankingType getConfiguredRankingType() {
        return RankingType.EVENT;
      }

    }

    @Order(60.0)
    public class TestFormulaButton extends AbstractFormulaTestButton {

      @Override
      protected String getForumula() {
        return getRankingBox().getFormulaField().getValue();
      }

    }

    @Order(68.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(70.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(80.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IRankingEventProcessService service = BEANS.get(IRankingEventProcessService.class);
      RankingEventFormData formData = new RankingEventFormData();
      exportFormData(formData);
      formData = BeanUtility.rankingEventBean2FormData(service.load(BeanUtility.rankingEventFormData2bean(formData).getId()));
      importFormData(formData);
      setEnabledPermission(new UpdateRankingEventPermission());
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      getEventField().setEnabled(false);
      getRankingBox().validateRankingFormat();
    }

    @Override
    public void execStore() throws ProcessingException {
      IRankingEventProcessService service = BEANS.get(IRankingEventProcessService.class);
      RankingEventFormData formData = new RankingEventFormData();
      exportFormData(formData);
      formData = BeanUtility.rankingEventBean2FormData(service.store(BeanUtility.rankingEventFormData2bean(formData)));
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IRankingEventProcessService service = BEANS.get(IRankingEventProcessService.class);
      RankingEventFormData formData = new RankingEventFormData();
      exportFormData(formData);
      formData = BeanUtility.rankingEventBean2FormData(service.prepareCreate(BeanUtility.rankingEventFormData2bean(formData)));
      importFormData(formData);
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      getRankingBox().updateDefaultFormulaValues();
      getRankingBox().validateRankingFormat();
    }

    @Override
    public void execStore() throws ProcessingException {
      IRankingEventProcessService service = BEANS.get(IRankingEventProcessService.class);
      RankingEventFormData formData = new RankingEventFormData();
      exportFormData(formData);
      formData = BeanUtility.rankingEventBean2FormData(service.create(BeanUtility.rankingEventFormData2bean(formData)));
    }
  }

  @FormData
  public RtRankingEventKey getKey() {
    return key;
  }

  @FormData
  public void setKey(RtRankingEventKey key) {
    this.key = key;
  }

}
