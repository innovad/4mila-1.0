package com.rtiming.client.settings;

import java.util.Locale;
import java.util.Map;

import org.eclipse.scout.rt.client.ui.basic.table.AbstractTable;
import org.eclipse.scout.rt.client.ui.basic.table.columns.AbstractStringColumn;
import org.eclipse.scout.rt.client.ui.desktop.outline.pages.AbstractPageWithTable;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.shared.AbstractIcons;
import org.eclipse.scout.rt.shared.TEXTS;
import org.eclipse.scout.rt.shared.services.common.jdbc.SearchFilter;

import com.rtiming.client.common.help.IHelpEnabledPage;
import com.rtiming.shared.FMilaTextProviderService;

public class TranslationsTablePage extends AbstractPageWithTable<TranslationsTablePage.Table> implements IHelpEnabledPage {

  @Override
  protected String getConfiguredTitle() {
    return TEXTS.get("Translations");
  }

  @Override
  protected String getConfiguredIconId() {
    return AbstractIcons.Folder;
  }

  @Order(10.0)
  public class Table extends AbstractTable {

    public GermanColumn getGermanColumn() {
      return getColumnSet().getColumnByClass(GermanColumn.class);
    }

    public EnglishColumn getEnglishColumn() {
      return getColumnSet().getColumnByClass(EnglishColumn.class);
    }

    public FrenchColumn getFrenchColumn() {
      return getColumnSet().getColumnByClass(FrenchColumn.class);
    }

    public KeyColumn getKeyColumn() {
      return getColumnSet().getColumnByClass(KeyColumn.class);
    }

    @Order(10.0)
    public class KeyColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("Key");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }

      @Override
      protected int getConfiguredSortIndex() {
        return 1;
      }

    }

    @Order(20.0)
    public class EnglishColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("English");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(30.0)
    public class GermanColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("German");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

    @Order(40.0)
    public class FrenchColumn extends AbstractStringColumn {

      @Override
      protected String getConfiguredHeaderText() {
        return TEXTS.get("French");
      }

      @Override
      protected int getConfiguredWidth() {
        return 250;
      }
    }

  }

  @Override
  protected void execLoadData(SearchFilter filter) throws ProcessingException {
    FMilaTextProviderService service = BEANS.get(FMilaTextProviderService.class);
    Map<String, String> germanTexts = service.getTextMap(Locale.GERMAN);
    Map<String, String> englishTexts = service.getTextMap(Locale.ENGLISH);
    Map<String, String> frenchTexts = service.getTextMap(Locale.FRENCH);
    Object[][] table = new Object[englishTexts.size()][3];
    int i = 0;
    for (String key : englishTexts.keySet()) {
      table[i] = new Object[]{key, englishTexts.get(key), germanTexts.get(key), frenchTexts.get(key)};
      i++;
    }
    importTableData(table);
  }

}
