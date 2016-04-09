package com.rtiming.client.common.exception;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.eclipse.scout.rt.client.services.common.clipboard.IClipboardService;
import org.eclipse.scout.rt.client.ui.action.keystroke.AbstractKeyStroke;
import org.eclipse.scout.rt.client.ui.basic.filechooser.FileChooser;
import org.eclipse.scout.rt.client.ui.form.AbstractForm;
import org.eclipse.scout.rt.client.ui.form.AbstractFormHandler;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.stringfield.AbstractStringField;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.resource.BinaryResource;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.shared.TEXTS;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.rtiming.client.ClientInfoUtility;
import com.rtiming.client.common.exception.ExceptionForm.MainBox.CancelButton;
import com.rtiming.client.common.exception.ExceptionForm.MainBox.CopyButton;
import com.rtiming.client.common.exception.ExceptionForm.MainBox.GroupBox;
import com.rtiming.client.common.exception.ExceptionForm.MainBox.GroupBox.MessageField;
import com.rtiming.client.common.exception.ExceptionForm.MainBox.SaveButton;
import com.rtiming.shared.FMilaUtility;

public class ExceptionForm extends AbstractForm {

  private static final Logger LOG = LoggerFactory.getLogger(ExceptionForm.class);
  private String messageText;
  private static String SEP = FMilaUtility.LINE_SEPARATOR;

  public ExceptionForm() throws ProcessingException {
    super();
  }

  @Override
  protected boolean getConfiguredAskIfNeedSave() {
    return false;
  }

  public void startDefault() throws ProcessingException {
    startInternal(new ExceptionForm.DefaultHandler());
  }

  public CancelButton getCancelButton() {
    return getFieldByClass(CancelButton.class);
  }

  public CopyButton getCopyButton() {
    return getFieldByClass(CopyButton.class);
  }

  public SaveButton getSaveButton() {
    return getFieldByClass(SaveButton.class);
  }

  public GroupBox getGroupBox() {
    return getFieldByClass(GroupBox.class);
  }

  public MessageField getMessageField() {
    return getFieldByClass(MessageField.class);
  }

  public MainBox getMainBox() {
    return getFieldByClass(MainBox.class);
  }

  public String getMessageText() {
    return messageText;
  }

  public void setMessageText(String messageText) {
    this.messageText = messageText;
  }

  @Order(10.0)
  public class MainBox extends AbstractGroupBox {

    @Override
    protected int getConfiguredGridColumnCount() {
      return 1;
    }

    @Override
    protected int getConfiguredWidthInPixel() {
      return 450;
    }

    @Order(10.0)
    public class GroupBox extends AbstractGroupBox {

      @Override
      protected boolean getConfiguredBorderVisible() {
        return false;
      }

      @Override
      protected int getConfiguredGridColumnCount() {
        return 1;
      }

      @Order(10.0)
      public class MessageField extends AbstractStringField {

        @Override
        protected int getConfiguredGridH() {
          return 10;
        }

        @Override
        protected boolean getConfiguredEnabled() {
          return false;
        }

        @Override
        protected double getConfiguredGridWeightY() {
          return 1;
        }

        @Override
        protected boolean getConfiguredLabelVisible() {
          return false;
        }

        @Override
        protected boolean getConfiguredMultilineText() {
          return true;
        }

        @Override
        protected int getConfiguredMaxLength() {
          return Integer.MAX_VALUE;
        }

      }
    }

    @Order(20.0)
    public class CopyButton extends AbstractButton {

      @Override
      protected int getConfiguredHorizontalAlignment() {
        return -1;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("Copy");
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        IClipboardService service = BEANS.get(IClipboardService.class);
        service.setTextContents(getMessageText());
      }
    }

    @Order(30.0)
    public class SaveButton extends AbstractButton {

      @Override
      protected int getConfiguredHorizontalAlignment() {
        return -1;
      }

      @Override
      protected String getConfiguredLabel() {
        return TEXTS.get("SaveAs");
      }

      @Override
      protected void execClickAction() throws ProcessingException {
        FileChooser chooser = new FileChooser(Arrays.asList(new String[]{"txt"}));
        // TODO MIG chooser.setTypeLoad(false);
        String version = FMilaUtility.getVersion();
        // TODO MIG chooser.setFileName("4mila_error_" + version + ".txt");
        List<BinaryResource> files = chooser.startChooser();

        if (files != null && files.size() == 1) {
          File file = new File(files.get(0).getFilename());
          try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(getMessageField().getValue().getBytes());
            fos.close();
          }
          catch (IOException e) {
            throw new ProcessingException("Unable to write file", e);
          }
        }
        else {
          throw new ProcessingException("Only one file should be selected.");
        }
      }
    }

    @Order(40.0)
    public class CancelButton extends AbstractCancelButton {

      @Override
      protected String getConfiguredTooltipText() {
        return null;
      }
    }

    @Order(10.0)
    public class EscapeKeyStroke extends AbstractKeyStroke {

      @Override
      protected String getConfiguredKeyStroke() {
        return "escape";
      }

      @Override
      protected void execAction() throws ProcessingException {
        doClose();
      }
    }
  }

  public class DefaultHandler extends AbstractFormHandler {

    @Override
    protected void execLoad() throws ProcessingException {
      // fix scout stack trace formatting
      String msgText = StringUtility.replace(getMessageText(), "\n\n", "\n");

      StringBuilder text = new StringBuilder();
      text.append("*** Error Message ***");
      text.append(SEP);
      text.append(SEP);
      text.append(msgText);
      text.append(SEP);
      text.append(SEP);
      text.append(ClientInfoUtility.buildInstallationInfo(null));
      getMessageField().setValue(text.toString());
    }
  }
}
