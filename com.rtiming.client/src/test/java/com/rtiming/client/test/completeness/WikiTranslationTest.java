package com.rtiming.client.test.completeness;

import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.junit.Assert;
import org.junit.Test;

import com.rtiming.shared.FMilaUtility;

public class WikiTranslationTest {

  @Test
  public void testTranslationsExist() throws Exception {
    String root = WikiTestUtility.getRoot();

    Map<String, String> masterTranslations = getTranslations(root);

    List<String> languages = getLanguages();
    List<String> wrongEntries = new ArrayList<>();

    for (String language : languages) {
      System.out.println("*** Language " + language + " ***");
      Map<String, String> translations = getTranslations(root + FMilaUtility.FILE_SEPARATOR + language);
      for (String entry : translations.keySet()) {
        String master = masterTranslations.get(entry);
        if (master == null) {
          Assert.fail("master translation missing: " + entry + ", " + language);
        }
        String translation = translations.get(entry);
        if (translation.contains("FIXME")) {
          if (!translation.endsWith(master)) {
            wrongEntries.add(language + ", " + entry);
          }
        }
      }
    }

    if (wrongEntries.size() > 0) {
      Assert.fail("Inconsistent Wiki Translations: " + CollectionUtility.format(wrongEntries));
    }
  }

  private Map<String, String> getTranslations(String root) throws IOException, ProcessingException {
    Map<String, String> result = new HashMap<>();
    FileFilter filter = new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return !pathname.isDirectory();
      }
    };
    File[] files = new File(root).listFiles(filter);
    for (File file : files) {
      if (file.getName().endsWith(".txt")) {
        result.put(file.getName(), IOUtility.getContent(new FileReader(file)));
      }
    }
    return result;
  }

  private List<String> getLanguages() {
    String root = WikiTestUtility.getRoot();
    List<String> result = new ArrayList<>();
    FileFilter filter = new FileFilter() {
      @Override
      public boolean accept(File pathname) {
        return pathname.isDirectory();
      }
    };
    File[] languages = new File(root).listFiles(filter);
    for (File file : languages) {
      if (file.getName().length() == 2 || file.getName().length() == 5) {
        result.add(file.getName());
      }
    }
    return result;
  }
}
