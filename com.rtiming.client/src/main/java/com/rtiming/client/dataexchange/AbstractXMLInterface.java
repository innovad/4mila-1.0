package com.rtiming.client.dataexchange;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.rt.shared.TEXTS;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.dataexchange.AbstractDataBean;
import com.rtiming.shared.dataexchange.AbstractXMLDataBean;
import com.rtiming.shared.dataexchange.DataExchangeStartFormData;

/**
 * @param <Bean>
 */
public abstract class AbstractXMLInterface<Bean extends AbstractXMLDataBean> extends AbstractInterface {

  private Object jaxbObject;

  public AbstractXMLInterface(DataExchangeStartFormData interfaceConfig) throws ProcessingException {
    super(interfaceConfig);
  }

  public void setJaxbObject(Object jaxbObject) {
    this.jaxbObject = jaxbObject;
  }

  @Override
  public void createPreviewData() throws ProcessingException {
    if (getData() != null) {
      int numRows = getData().size();
      getPreview().setData(new Object[numRows][getColumnHeaders().size()]);
      ArrayList<Bean> x = getData();
      int rowCounter = 0;
      for (Bean b : x) {
        ArrayList<String> data = b.getPreviewRowData();
        for (int k = 0; k < data.size(); k++) {
          getPreview().getData()[rowCounter][k] = data.get(k);
        }
        rowCounter++;
      }
    }
  }

  protected ArrayList<? extends Bean> createBeansFromXMLObject(Object xmlData) throws ProcessingException {
    return new ArrayList<Bean>();
  }

  @Override
  public void previewToFile(ProgressMonitor monitor) throws ProcessingException {

    ArrayList<Bean> beans = getData();
    Object xmlRoot = createXMLRootObject();
    if (beans != null) {
      for (Bean bean : beans) {
        xmlRoot = bean.createXMLObject(xmlRoot);
      }
    }

    ByteArrayOutputStream baos = new ByteArrayOutputStream();

    try {
      JAXBContext context = JAXBContext.newInstance(xmlRoot.getClass());
      Marshaller m = context.createMarshaller();
      m.marshal(xmlRoot, baos);
    }
    catch (Exception e) {
      throw new ProcessingException("XML Export failed", e);
    }

    monitor.addInfo(TEXTS.get("File"), getFullPathName());
    try {
      File export = new File(getFullPathName());
      export.delete();
      export.createNewFile();
      BufferedWriter buf = new BufferedWriter(new FileWriter(export));
      buf.write(baos.toString(getCharacterSet()));
      buf.close();
      monitor.addInfo(TEXTS.get("FileSize"), FMilaUtility.formatFileSize(export.length()));
    }
    catch (IOException e) {
      throw new ProcessingException("Writing the file failed.", e);
    }

    if (getData() != null) {
      monitor.update(getData().size(), getData().size());
    }
    else {
      monitor.update(0, 0);
    }

  }

  /**
   * required for XML export only
   * 
   * @return XML Root Object
   * @throws ProcessingException
   */
  public Object createXMLRootObject() throws ProcessingException {
    return null;
  }

  /**
   * required for XML export only
   * 
   * @return exported primary key nrs
   * @throws ProcessingException
   */
  @Override
  public List<Long> getPrimaryKeyNrs() throws ProcessingException {
    return null;
  }

  @Override
  public void fileToPreview() throws ProcessingException {
    jaxbObject = XMLUtility.unmarshal(getFullPathName(), jaxbObject, isUseXmlFilter());
    ArrayList<? extends Bean> beans = createBeansFromXMLObject(jaxbObject);
    for (Bean b : beans) {
      addBean(b);
    }
  }

  @SuppressWarnings("unchecked")
  @Override
  public ArrayList<Bean> getData() {
    return super.getData();
  }

  @SuppressWarnings("unchecked")
  @Override
  public void addBean(AbstractDataBean bean) {
    super.addBean(bean);
  }

  protected boolean isUseXmlFilter() {
    return false;
  }

}
