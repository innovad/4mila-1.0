package com.rtiming.client.test.completeness;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.inventory.ClassInventory;
import org.eclipse.scout.rt.platform.inventory.IClassInfo;
import org.eclipse.scout.rt.platform.util.FileUtility;
import org.eclipse.scout.rt.platform.util.IOUtility;
import org.eclipse.scout.rt.platform.util.StringUtility;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.common.EmptyWorkaroundTablePage;
import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.client.settings.account.AccountClientTablePage;
import com.rtiming.client.settings.account.AccountTablePage;
import com.rtiming.shared.FMilaUtility;

@RunWith(ClientTestRunner.class)
public class TablePageHelpCompletenessTest {

  @Test
  public void testHelp() throws Exception {
    Set<IClassInfo> clazzes = ClassInventory.get().getAllKnownSubClasses(Object.class); // TODO MIG

    List<Class<?>> classesWithoutHelp = new ArrayList<>();
    List<Class<?>> classesWithHelp = new ArrayList<>();

    List<Class<?>> exceptions = new ArrayList<>();
    exceptions.add(AccountTablePage.class);
    exceptions.add(AccountClientTablePage.class);
    exceptions.add(EmptyWorkaroundTablePage.class);

    for (IClassInfo clazzStr : clazzes) {
      if (clazzStr.name().endsWith("TablePage")) {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(clazzStr.name());
        if (!exceptions.contains(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
          if (!IHelpEnabledPage.class.isAssignableFrom(clazz)) {
            classesWithoutHelp.add(clazz);
          }
          else {
            classesWithHelp.add(clazz);
          }
        }
      }
    }

    for (Class<?> clazz : classesWithoutHelp) {
      System.out.println(clazz.toString());
    }

    if (classesWithoutHelp.size() > 0) {
      Assert.fail("There are table pages without help, see system out");
    }

    // Check classes with help: Wiki File must exist
    File wikiPageRoot = new File(WikiTestUtility.getRoot());
    System.out.println("Looking for Pages: " + wikiPageRoot.getAbsolutePath());
    Assert.assertTrue("Page Root must exist", wikiPageRoot.exists());
    Assert.assertTrue("Page Root must be directory", wikiPageRoot.isDirectory());

    List<File> pages = FileUtility.listTree(wikiPageRoot, true, false);
    List<String> wikiPages = new ArrayList<>();
    for (File page : pages) {
      String name = page.getName();
      if (name.endsWith("tablepage.txt")) {
        name = StringUtility.removeSuffixes(name, ".txt");
        wikiPages.add(name);
      }
    }

    List<Object> missingWikiPages = new ArrayList<>();
    for (Class<?> clazz : classesWithHelp) {
      if (!wikiPages.contains(clazz.getSimpleName().toLowerCase())) {
        missingWikiPages.add(clazz.getSimpleName().toLowerCase());
      }
    }

    if (missingWikiPages.size() > 0) {
      Assert.fail("There are table pages without wiki page: " + StringUtility.collectionToString(missingWikiPages));
    }

    // check table page index
    checkIndex(classesWithHelp, wikiPageRoot, "tables.txt");
    checkIndex(classesWithHelp, wikiPageRoot, "de/tables.txt");
    checkIndex(classesWithHelp, wikiPageRoot, "fr/tables.txt");

  }

  private void checkIndex(List<Class<?>> classesWithHelp, File wikiPageRoot, String index) throws ProcessingException, FileNotFoundException {
    String tablePageIndexContent = IOUtility.getContent(new FileReader(new File(wikiPageRoot.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + index)));
    tablePageIndexContent = tablePageIndexContent.toLowerCase();
    for (Class<?> clazz : classesWithHelp) {
      String pageName = clazz.getSimpleName().toLowerCase();
      Assert.assertTrue("Index " + index + " contains Page " + pageName, tablePageIndexContent.contains(pageName));
    }
  }

}
