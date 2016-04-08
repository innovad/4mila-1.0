package com.rtiming.client.common.ui.fields;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.List;
import java.util.Set;

import javax.imageio.ImageIO;

import org.eclipse.scout.commons.CollectionUtility;
import org.eclipse.scout.commons.IOUtility;
import org.eclipse.scout.commons.StringUtility;
import org.eclipse.scout.commons.annotations.Order;
import org.eclipse.scout.commons.dnd.TransferObject;
import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.exception.VetoException;
import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.ValueFieldMenuType;
import org.eclipse.scout.rt.client.ui.dnd.IDNDSupport;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractLinkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.AbstractGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.imagefield.AbstractImageField;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.client.FMilaClientUtility;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;

public abstract class AbstractImageGroupBox extends AbstractGroupBox {

  @Override
  protected boolean getConfiguredBorderVisible() {
    return false;
  }

  public FileChooserButton getFileChooserButton() {
    return getFieldByClass(FileChooserButton.class);
  }

  public ImageField getImageField() {
    return getFieldByClass(ImageField.class);
  }

  @Order(10.0)
  public class ImageField extends AbstractImageField {

    private boolean fileChanged = false;

    @Override
    protected boolean getConfiguredAutoFit() {
      return true;
    }

    @Override
    protected int getConfiguredDragType() {
      return IDNDSupport.TYPE_FILE_TRANSFER;
    }

    @Override
    protected int getConfiguredDropType() {
      return IDNDSupport.TYPE_FILE_TRANSFER;
    }

// TODO MIG    
//    @Override
//    protected boolean getConfiguredFocusVisible() {
//      return false;
//    }

    @Override
    protected int getConfiguredGridH() {
      return 10;
    }

    @Override
    protected int getConfiguredGridW() {
      return 2;
    }

    @Override
    protected String getConfiguredLabel() {
      return Texts.get("Map");
    }

    @Override
    protected boolean getConfiguredLabelVisible() {
      return false;
    }

// TODO MIG    
//    @Override
//    protected void execDropRequest(TransferObject t) throws ProcessingException {
//      drop(t);
//    }

    public void drop(TransferObject t) throws ProcessingException {
// TODO MIG      
//      if (t.isFileList()) {
//        // file drag & drop
//        FileListTransferObject fileTransferable = (FileListTransferObject) t;
//        if (fileTransferable.getFiles() != null && fileTransferable.getFiles().length == 1) {
//          // get first item
//          File f = fileTransferable.getFiles()[0];
//          if (f != null && f.exists() && f.isFile()) {
//            if (Pattern.matches("[^\\.]*\\.(gif|png|jpg|jpeg)", f.getName().toLowerCase())) {
//              byte[] content = null;
//              try {
//                content = IOUtility.getContent(new FileInputStream(f), true);
//              }
//              catch (FileNotFoundException e) {
//                // nop
//                return;
//              }
//              this.setImage(content, getFileSuffix(f), true);
//            }
//            else {
//              throw new VetoException(TEXTS.get("FileTypeProcessingException", f.getName()));
//            }
//          }
//          else {
//            throw new VetoException(TEXTS.get("FileReadException"));
//          }
//        }
//      }
//      else if (t.isImage()) {
//        // image drag & drop
//        ImageTransferObject imageTransferable = (ImageTransferObject) t;
//        if (imageTransferable.getImage() instanceof BufferedImage) {
//          ByteArrayOutputStream buffer = new ByteArrayOutputStream(25000);
//          ImageWriter writer = ImageIO.getImageWritersByFormatName("jpeg").next();
//          try {
//            writer.setOutput(ImageIO.createImageOutputStream(buffer));
//            writer.write((BufferedImage) imageTransferable.getImage());
//          }
//          catch (IOException e) {
//            throw new ProcessingException("unable to drop image", e);
//          }
//          finally {
//            writer.dispose();
//          }
//          byte[] data = buffer.toByteArray();
//          try {
//            buffer.close();
//          }
//          catch (IOException e) {
//            throw new ProcessingException(e.getMessage());
//          }
//          buffer = null;
//          this.setImage(data, "jpeg");
//        }
//      }
    }

    public void setImage(Object imgObj, String format) {
      super.setImage(imgObj);
      // correct scout functionality, if an image is changed, touch dialog
      touch();
      // update file changed on form
      updateFileChanged(this.fileChanged);
      setImageFormat(format);
      getViewButton().setEnabled(!StringUtility.isNullOrEmpty(getImageFormat()) && getImage() != null);
    }

    /**
     * @param imgObj
     * @param mapFileChanged
     *          indicate that the file changed and has to be uploaded to the server
     */
    public void setImage(Object imgObj, String format, boolean mapFileChanged) {
      this.fileChanged = mapFileChanged;
      setImage(imgObj, format);
    }

    public boolean isFileChanged() {
      return fileChanged;
    }

    private String getFileSuffix(File f) {
      if (f == null) {
        return "";
      }
      else {
        for (String s : FMilaUtility.getSupportedImageFormats()) {
          if (f.getPath().toLowerCase().endsWith("." + s)) {
            return s;
          }
        }
      }
      return "";
    }

    public void insertFile(File file) throws ProcessingException {
      insertFile(file, null);
    }

    private void insertFile(File file, String description) throws ProcessingException {
      if (file != null && file.exists()) {
        if (file.getAbsolutePath() != null && (IOUtility.getFileSize(file.getAbsolutePath()) / 1024 > 50000)) {
          throw new VetoException(Texts.get("FileSizeExceedsXKB", "" + 50000));
        }
        this.setImage(IOUtility.getContent(file.getAbsolutePath()), getFileSuffix(file), true);
      }
    }

    @Order(10.0)
    public class FileChooserMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return Texts.get("FileChooserMenu");
      }

      @Override
      protected void execAction() throws ProcessingException {
        chooseFile();
      }
    }

    @Order(20.0)
    public class PasteFromClipboardMenu extends AbstractMenu {

      @Override
      protected Set<? extends IMenuType> getConfiguredMenuTypes() {
        return CollectionUtility.<IMenuType> hashSet(ValueFieldMenuType.Null);
      }

      @Override
      protected void execInitAction() throws ProcessingException {
        setVisible(FMilaUtility.isRichClient());
      }

      @Override
      protected String getConfiguredText() {
        return Texts.get("PasteFromClipboard");
      }

      @Override
      protected void execAction() throws ProcessingException {
        pasteFromClipboard();
      }
    }

    @Order(30.0)
    public class SeparatorMenu extends AbstractMenu {

      @Override
      protected boolean getConfiguredSeparator() {
        return true;
      }

    }

    @Order(40.0)
    public class ViewMenu extends AbstractMenu {

      @Override
      protected String getConfiguredText() {
        return Texts.get("ViewMenu");
      }

      @Override
      protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
        setEnabled(!StringUtility.isNullOrEmpty(getImageFormat()) && getImage() != null);
      }

      @Override
      protected void execAction() throws ProcessingException {
        viewWithExternalTool();
      }

    }

  }

  protected abstract void updateFileChanged(boolean fileHasChanged);

  protected abstract String getImageFormat();

  protected abstract void setImageFormat(String format);

  public PasteFromClipboardButton getPasteFromClipboardButton() {
    return getFieldByClass(PasteFromClipboardButton.class);
  }

  public ViewButton getViewButton() {
    return getFieldByClass(ViewButton.class);
  }

  protected abstract byte[] getImageData();

  private void chooseFile() throws ProcessingException {
// TODO MIG    
//    try {
//      List<BinaryResource> a = new FileChooser(Arrays.asList(FMilaUtility.getSupportedImageFormats()), true).startChooser();
//      if (a != null && a.size() == 1) {
//        byte[] data = a.get(0).getContent();
//        getImageField().setImage(data, getImageField().getFileSuffix(a.get(0).getFilename()), true);
//      }
//    }
//    catch (FileNotFoundException e) {
//      throw new ProcessingException(e.getMessage());
//    }
  }

  private void pasteFromClipboard() throws ProcessingException {
    try {
      Clipboard systemClipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
      Transferable transferData = systemClipboard.getContents(null);

      try {
        Object fl = transferData.getTransferData(DataFlavor.javaFileListFlavor);
        if (fl instanceof List<?>) {
          List<?> files = (List<?>) fl;
          for (Object file : files) {
            if (file instanceof File) {
              getImageField().insertFile((File) file);
            }
          }
        }
      }
      catch (UnsupportedFlavorException efl) {
        try {
          Object s = transferData.getTransferData(DataFlavor.stringFlavor);
          String string = (String) s;
          File tmpFile = getFileNameInternal("Text.txt");
          IOUtility.writeContent(tmpFile.getAbsolutePath(), string.getBytes());
          getImageField().insertFile(tmpFile, Texts.get("TextFromClipboard"));
        }
        catch (UnsupportedFlavorException es) {
          try {
            BufferedImage image = (BufferedImage) transferData.getTransferData(DataFlavor.imageFlavor);
            File tmpFile = getFileNameInternal("Picture.png");
            ImageIO.write(image, "PNG", tmpFile);
            getImageField().insertFile(tmpFile, Texts.get("PictureFromClipboard"));
          }
          catch (UnsupportedFlavorException ep) {
            throw new ProcessingException(ep.getMessage());
          }
        }
      }
    }
    catch (Throwable t) {
      throw new ProcessingException(t.getMessage());
    }
  }

  private File getFileNameInternal(String name) throws ProcessingException {
    File d = new File(IOUtility.getTempFileName(""));
    if (name == null || name.length() == 0) {
      return d;
    }

    File f = new File(d.getParent() + File.separatorChar + name);
    int i = 0;
    while (f.exists()) {
      if (name.indexOf(".") != -1) {
        f = new File(d.getParent() + File.separatorChar + name.replace(".", i + "."));
      }
      else {
        f = new File(d.getAbsolutePath() + File.separatorChar + name + i);
      }
      i++;
    }
    return f;
  }

  private void viewWithExternalTool() throws ProcessingException {
    File viewer = IOUtility.createTempFile(IOUtility.getTempFileName("." + getImageFormat()), (byte[]) getImageField().getImage());
    FMilaClientUtility.openDocument(viewer.getAbsolutePath());
  }

  @Order(20.0)
  public class FileChooserButton extends AbstractLinkButton {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("FileChooserMenu");
    }

    @Override
    protected void execClickAction() throws ProcessingException {
      chooseFile();
    }
  }

  @Order(30.0)
  public class PasteFromClipboardButton extends AbstractLinkButton {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("PasteFromClipboard");
    }

    @Override
    protected void execClickAction() throws ProcessingException {
      pasteFromClipboard();
    }

    @Override
    protected void execInitField() throws ProcessingException {
      setVisible(FMilaUtility.isRichClient());
    }
  }

  @Order(40.0)
  public class ViewButton extends AbstractLinkButton {

    @Override
    protected String getConfiguredLabel() {
      return TEXTS.get("ViewMenu");
    }

    @Override
    protected void execClickAction() throws ProcessingException {
      viewWithExternalTool();
    }

    @Override
    protected boolean getConfiguredEnabled() {
      return false;
    }
  }

}
