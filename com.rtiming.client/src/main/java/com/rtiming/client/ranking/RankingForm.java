package com.rtiming.client.ranking;

import org.eclipse.scout.rt.client.dto.FormData;
import org.eclipse.scout.rt.client.dto.FormData.SdkCommand;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.ranking.RankingForm.MainBox.CancelButton;
import com.rtiming.client.ranking.RankingForm.MainBox.NameField;
import com.rtiming.client.ranking.RankingForm.MainBox.OkButton;
import com.rtiming.client.ranking.RankingForm.MainBox.RankingBox;
import com.rtiming.shared.common.database.sql.BeanUtility;
import com.rtiming.shared.common.security.permission.UpdateRankingPermission;
import com.rtiming.shared.dao.RtRankingKey;
import com.rtiming.shared.ranking.AbstractFormulaCode.RankingType;
import com.rtiming.shared.ranking.IRankingProcessService;
import com.rtiming.shared.ranking.RankingFormData;

@FormData(value = RankingFormData.class, sdkCommand = SdkCommand.CREATE)
public class RankingForm extends AbstractForm {

  private RtRankingKey key;

  public RankingForm() throws ProcessingException {
    super();
  }

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Ranking");
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  @FormData
  public RtRankingKey getKey() {
    return key;
  }

  @FormData
  public void setKey(RtRankingKey key) {
    this.key = key;
  }

  public void startModify() throws ProcessingException {
    startInternal(new ModifyHandler());
  }

  public void startNew() throws ProcessingException {
    startInternal(new NewHandler());
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public NameField getNameField() {
    return getFieldByClass(NameField.class);
  }

  public RankingBox getRankingBox() {
    return getFieldByClass(RankingBox.class);
  }

  public OkButton getOkButton() {
    return getFieldByClass(OkButton.class);
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Order(10.0)
    public class NameField extends AbstractStringField {

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Name");
      }

      @Override
      protected boolean getConfiguredMandatory() {
        return true;
      }

      @Override
      protected int getConfiguredMaxLength() {
        return 250;
      }
    }

    @Order(30.0)
    public class RankingBox extends AbstractRankingBox {

      @Override
      protected RankingType getConfiguredRankingType() {
        return RankingType.SUMMARY;
      }

    }

    @Order(60.0)
    public class TestFormulaButton extends AbstractFormulaTestButton {

      @Override
      protected String getForumula() {
        return getRankingBox().getFormulaField().getValue();
      }

    }

    @Order(62.0)
    public class HelpLink extends AbstractHelpLinkButton {
    }

    @Order(40.0)
    public class OkButton extends AbstractOkButton {
    }

    @Order(50.0)
    public class CancelButton extends AbstractCancelButton {
    }
  }

  public class ModifyHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IRankingProcessService service = BEANS.get(IRankingProcessService.class);
      RankingFormData formData = new RankingFormData();
      exportFormData(formData);
      formData = BeanUtility.rankingBean2FormData(service.load(BeanUtility.rankingFormData2bean(formData).getId()));
      importFormData(formData);
      setEnabledPermission(new UpdateRankingPermission());
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      getRankingBox().validateRankingFormat();
    }

    @Override
    public void execStore() throws ProcessingException {
      IRankingProcessService service = BEANS.get(IRankingProcessService.class);
      RankingFormData formData = new RankingFormData();
      exportFormData(formData);
      formData = BeanUtility.rankingBean2FormData(service.store(BeanUtility.rankingFormData2bean(formData)));
    }
  }

  public class NewHandler extends AbstractFormHandler {

    @Override
    public void execLoad() throws ProcessingException {
      IRankingProcessService service = BEANS.get(IRankingProcessService.class);
      RankingFormData formData = new RankingFormData();
      exportFormData(formData);
      formData = BeanUtility.rankingBean2FormData(service.prepareCreate(BeanUtility.rankingFormData2bean(formData)));
      importFormData(formData);
    }

    @Override
    protected void execPostLoad() throws ProcessingException {
      getRankingBox().updateDefaultFormulaValues();
      getRankingBox().validateRankingFormat();
    }

    @Override
    public void execStore() throws ProcessingException {
      IRankingProcessService service = BEANS.get(IRankingProcessService.class);
      RankingFormData formData = new RankingFormData();
      exportFormData(formData);
      formData = BeanUtility.rankingBean2FormData(service.create(BeanUtility.rankingFormData2bean(formData)));
      importFormData(formData);
    }
  }
}
