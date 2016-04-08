package com.rtiming.client.dataexchange;

import java.io.File;
import java.io.FileInputStream;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.sax.SAXSource;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.rtiming.shared.dataexchange.bom.BOMInputStream;

/**
 * 
 */
public class XMLUtility {

  public static Object unmarshal(String fullPathName, Object rootObject, boolean useFilter) throws ProcessingException {
    try {
      File file = new File(fullPathName);
      if (!file.exists()) {
        throw new ProcessingException("File does not exist: " + file.getAbsolutePath());
      }
      if (!file.canRead()) {
        throw new ProcessingException("Cannot read file");
      }
      XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
      reader.setFeature("http://apache.org/xml/features/nonvalidating/load-external-dtd", false);
      reader.setFeature("http://xml.org/sax/features/validation", false);

      // create an input source with a file reader
      // do not use the file name directly, otherwise there are issues with special chars (space, dot, ...)
      InputSource inputSource = new InputSource(new BOMInputStream(new FileInputStream(fullPathName)));

      if (useFilter) {
        NamespaceFilter filter = new NamespaceFilter();
        filter.setParent(reader);
        reader = filter;
      }

      SAXSource source = new SAXSource(reader, inputSource);

      JAXBContext context = JAXBContext.newInstance(rootObject.getClass());
      Unmarshaller um = context.createUnmarshaller();
      um.setSchema(null);

      Object unmarshal = um.unmarshal(source);
      return unmarshal;
    }
    catch (Exception e) {
      throw new ProcessingException("Reading XML file failed", e);
    }
  }

}
