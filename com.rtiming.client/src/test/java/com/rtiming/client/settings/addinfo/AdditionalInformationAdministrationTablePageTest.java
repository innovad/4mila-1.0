package com.rtiming.client.settings.addinfo;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.scout.rt.client.testenvironment.TestEnvironmentClientSession;
import org.eclipse.scout.rt.client.ui.basic.table.columns.IColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.IPage;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.testing.client.runner.ClientTestRunner;
import org.eclipse.scout.rt.testing.client.runner.RunWithClientSession;
import org.eclipse.scout.rt.testing.platform.runner.RunWithSubject;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.rtiming.client.settings.SettingsOutline;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.EntityField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MandatoryField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MaximumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.MinimumField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationForm.MainBox.TypeField;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationTablePage.Table.FeeGroupColumn;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationTablePage.Table.NameColumn;
import com.rtiming.client.settings.addinfo.AdditionalInformationAdministrationTablePage.Table.TypeColumn;
import com.rtiming.client.test.AbstractTablePageTest;
import com.rtiming.client.test.ClientTestingUtility;
import com.rtiming.client.test.data.AdditionalInformationAdministrationTestDataProvider;
import com.rtiming.client.test.field.FieldValue;
import com.rtiming.shared.common.EntityCodeType;
import com.rtiming.shared.settings.addinfo.AdditionalInformationTypeCodeType;

@RunWith(ClientTestRunner.class)
@RunWithSubject("admin")
@RunWithClientSession(TestEnvironmentClientSession.class)
public class AdditionalInformationAdministrationTablePageTest extends AbstractTablePageTest<AdditionalInformationAdministrationTablePage> {

  @Override
  protected AdditionalInformationAdministrationTablePage getTablePage() {
    return new AdditionalInformationAdministrationTablePage(null);
  }

  @Test
  public void testChildPageForSmartfield() throws ProcessingException {
    List<FieldValue> fieldValue = new ArrayList<FieldValue>();
    fieldValue.add(new FieldValue(EntityField.class, EntityCodeType.ClubCode.ID));
    fieldValue.add(new FieldValue(TypeField.class, AdditionalInformationTypeCodeType.SmartfieldCode.ID));
    fieldValue.add(new FieldValue(MandatoryField.class, true));
    fieldValue.add(new FieldValue(MinimumField.class, null));
    fieldValue.add(new FieldValue(MaximumField.class, null));
    AdditionalInformationAdministrationTestDataProvider admin = new AdditionalInformationAdministrationTestDataProvider(fieldValue);

    IPage root = ClientTestingUtility.gotoOutline(SettingsOutline.class);
    AdditionalInformationAdministrationTablePage addInfoPage = ClientTestingUtility.gotoChildPage(root, AdditionalInformationAdministrationTablePage.class);
    AdditionalInformationAdministrationTablePage page = ClientTestingUtility.gotoChildPage(addInfoPage, AdditionalInformationAdministrationTablePage.class);

    for (IColumn<?> col : page.getTable().getColumns()) {
      if (col instanceof NameColumn) {
        Assert.assertTrue(col.isVisible());
      }
      else if (col instanceof TypeColumn) {
        Assert.assertTrue(col.isVisible());
      }
      else if (col instanceof FeeGroupColumn) {
        Assert.assertTrue(col.isVisible());
      }
      else {
        Assert.assertFalse(col.isVisible());
      }
    }

    admin.remove();
  }

}
