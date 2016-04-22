package com.rtiming.client.test.completeness;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.form.IForm;
import org.eclipse.scout.rt.client.ui.form.fields.IFormField;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractCancelButton;
import org.eclipse.scout.rt.client.ui.form.fields.button.AbstractOkButton;
import org.eclipse.scout.rt.client.ui.form.fields.groupbox.IGroupBox;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBoxFilterBox.ActiveStateRadioButtonGroup.ActiveAndInactiveButton;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBoxFilterBox.ActiveStateRadioButtonGroup.ActiveButton;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBoxFilterBox.ActiveStateRadioButtonGroup.InactiveButton;
import org.eclipse.scout.rt.client.ui.form.fields.listbox.AbstractListBoxFilterBox.CheckedStateRadioButtonGroup.CheckedButton;
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

import com.rtiming.client.common.database.BackupSettingsForm;
import com.rtiming.client.common.exception.ExceptionForm;
import com.rtiming.client.common.help.AbstractHelpLinkButton;
import com.rtiming.client.common.help.HelpForm;
import com.rtiming.client.common.infodisplay.InfoDisplayForm;
import com.rtiming.client.common.ui.desktop.ToolsForm;
import com.rtiming.client.common.ui.fields.AbstractCodeBox.ShortcutField;
import com.rtiming.client.dataexchange.DataExchangeFinalizationForm;
import com.rtiming.client.dataexchange.DataExchangePreviewForm;
import com.rtiming.client.dataexchange.DataExchangeStartForm.MainBox.FileBox.FieldSeparatorField;
import com.rtiming.client.ecard.download.ECardStationStatusForm;
import com.rtiming.client.ecard.download.RaceControlsTableForm;
import com.rtiming.client.entry.EntryForm.MainBox.AddEventButton;
import com.rtiming.client.entry.EntryForm.MainBox.AddRunnerButton;
import com.rtiming.client.entry.EntryForm.MainBox.CancelAndNextButton;
import com.rtiming.client.entry.EntryForm.MainBox.SaveAndNextButton;
import com.rtiming.client.entry.ParticipationForm;
import com.rtiming.client.event.course.CourseControlForm.MainBox.SaveButton;
import com.rtiming.client.map.MapForm.MainBox.ImportKMLButton;
import com.rtiming.client.settings.DefaultForm;
import com.rtiming.client.settings.TestDataForm;
import com.rtiming.client.settings.account.AccountForm;
import com.rtiming.client.setup.SetupForm;
import com.rtiming.client.setup.SetupWizardContainerForm;
import com.rtiming.client.setup.SetupWizardFinalizationForm;
import com.rtiming.client.setup.WelcomeForm;
import com.rtiming.shared.FMilaUtility;

@RunWith(ClientTestRunner.class)
public class FormHelpCompletenessTest {

  @Test
  public void testHelp() throws Exception {
    Set<IClassInfo> clazzes = ClassInventory.get().getAllKnownSubClasses(Object.class); // TODO MIG

    List<Class<?>> classesWithoutHelp = new ArrayList<>();
    List<Class<?>> classesWithHelp = new ArrayList<>();

    List<Class<?>> exceptions = new ArrayList<>();
    exceptions.add(RaceControlsTableForm.class);
    exceptions.add(InfoDisplayForm.class);
    exceptions.add(HelpForm.class);
    exceptions.add(ToolsForm.class);
    exceptions.add(BackupSettingsForm.class);
    exceptions.add(WelcomeForm.class);
    exceptions.add(AccountForm.class);
    exceptions.add(SetupForm.class);
    exceptions.add(SetupWizardFinalizationForm.class);
    exceptions.add(SetupWizardContainerForm.class);
    exceptions.add(TestDataForm.class);
    exceptions.add(ECardStationStatusForm.class);
    exceptions.add(DefaultForm.class);
    exceptions.add(ExceptionForm.class);
    exceptions.add(DataExchangePreviewForm.class);
    exceptions.add(DataExchangeFinalizationForm.class);
    exceptions.add(ParticipationForm.class);

    for (IClassInfo clazzStr : clazzes) {
      if (clazzStr.name().endsWith("Form") && !clazzStr.name().endsWith("SearchForm")) {
        Class<?> clazz = Thread.currentThread().getContextClassLoader().loadClass(clazzStr.name());
        if (!exceptions.contains(clazz) && !Modifier.isAbstract(clazz.getModifiers())) {
          // check if class contains AbstractHelpLinkButton
          boolean found = false;
          for (Class<?> innerClass : clazz.getDeclaredClasses()) {
            for (Class<?> innerClass2 : innerClass.getDeclaredClasses()) {
              if (AbstractHelpLinkButton.class.isAssignableFrom(innerClass2)) {
                classesWithHelp.add(clazz);
                found = true;
              }
            }
          }
          if (!found) {
            classesWithoutHelp.add(clazz);
          }
        }
      }
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
      if (name.endsWith("form.txt")) {
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
    checkIndex(classesWithHelp, wikiPageRoot, "forms.txt");
    checkIndex(classesWithHelp, wikiPageRoot, "de/forms.txt");
    checkIndex(classesWithHelp, wikiPageRoot, "fr/forms.txt");

    // check fields
    for (Class<?> clazz : classesWithHelp) {
      checkFields(clazz, wikiPageRoot);
    }
  }

  private void checkIndex(List<Class<?>> classesWithHelp, File wikiPageRoot, String index) throws ProcessingException, FileNotFoundException {
    String tablePageIndexContent = IOUtility.getContent(new FileReader(new File(wikiPageRoot.getAbsolutePath() + FMilaUtility.FILE_SEPARATOR + index)));
    tablePageIndexContent = tablePageIndexContent.toLowerCase();
    for (Class<?> clazz : classesWithHelp) {
      String pageName = clazz.getSimpleName().toLowerCase();
      Assert.assertTrue("Index " + index + " contains Page " + pageName, tablePageIndexContent.contains(pageName));
    }
  }

  private void checkFields(Class<?> formClass, File wikiPageRoot) {
    File pageHelp = new File(wikiPageRoot.getAbsolutePath() + "\\" + formClass.getSimpleName().toLowerCase() + ".txt");
    try {
      String pageHelpString = IOUtility.getContent(new FileReader(pageHelp));
      Object formInstance = formClass.newInstance();
      for (IFormField field : ((IForm) formInstance).getAllFields()) {
        findFields(formClass, field, pageHelpString);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
      Assert.fail("Could not create form, " + e.getLocalizedMessage());
    }
  }

  private void findFields(Class<?> formClass, IFormField field, String pageHelpString) {
    if (!field.isVisible()) {
      return;
    }
    if (getIgnoredFields().contains(field.getClass())) {
      return;
    }
    if (field instanceof IGroupBox || field instanceof AbstractOkButton || field instanceof CheckedButton || field instanceof ActiveButton || field instanceof InactiveButton || field instanceof ActiveAndInactiveButton || field instanceof AbstractCancelButton || field instanceof AbstractHelpLinkButton) {
      return;
    }
    if (field.getLabel() != null) {
      Assert.assertTrue("Fields missing in help text for form: " + formClass.getSimpleName() + ", field:" + field.getClass().getSimpleName() + " label: " + field.getLabel(), pageHelpString.toLowerCase().contains(field.getLabel().toLowerCase()));
    }
  }

  private List<Class<?>> getIgnoredFields() {
    List<Class<?>> ignoredFields = new ArrayList<>();
    ignoredFields.add(ImportKMLButton.class);
    ignoredFields.add(FieldSeparatorField.class);
    ignoredFields.add(ShortcutField.class);
    ignoredFields.add(AddRunnerButton.class);
    ignoredFields.add(AddEventButton.class);
    ignoredFields.add(SaveAndNextButton.class);
    ignoredFields.add(CancelAndNextButton.class);
    ignoredFields.add(SaveButton.class);
    return ignoredFields;
  }

}
