package com.rtiming.client.common.ui.desktop;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.eclipse.scout.rt.client.ui.action.menu.AbstractMenu;
import org.eclipse.scout.rt.client.ui.action.menu.IMenuType;
import org.eclipse.scout.rt.client.ui.action.menu.TableMenuType;
import org.eclipse.scout.rt.platform.BEANS;
import org.eclipse.scout.rt.platform.Order;
import org.eclipse.scout.rt.platform.Platform;
import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.CollectionUtility;
import org.eclipse.scout.rt.platform.util.CompareUtility;

import com.rtiming.client.ClientSession;
import com.rtiming.client.ecard.download.job.SICardDownloadJob;
import com.rtiming.client.ecard.download.job.SICardEntryJob;
import com.rtiming.client.ecard.download.processor.AbstractSICardProcessor;
import com.rtiming.client.settings.TestDataForm;
import com.rtiming.client.setup.SetupWizard;
import com.rtiming.serial.FMilaSerialEventListener;
import com.rtiming.serial.FMilaSerialPort;
import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;
import com.rtiming.shared.common.database.sql.EventBean;
import com.rtiming.shared.dao.RtEcardStation;
import com.rtiming.shared.ecard.download.ECardStationDownloadModusCodeType;
import com.rtiming.shared.ecard.download.ECardStationFormData;
import com.rtiming.shared.ecard.download.IECardStationProcessService;
import com.rtiming.shared.ecard.download.PunchFormData;
import com.rtiming.shared.event.IEventProcessService;
import com.rtiming.shared.results.IResultsOutlineService;
import com.rtiming.shared.settings.IDefaultProcessService;
import com.rtiming.shared.settings.ISettingsOutlineService;

public class FMilaDevelopmentMenu extends AbstractMenu {

  @Override
  protected void execInitAction() throws ProcessingException {
    setVisible(FMilaUtility.isRichClient());
  }

  @Override
  protected String getConfiguredText() {
    return "Development";
  }

  @Override
  protected void execOwnerValueChanged(Object newOwnerValue) throws ProcessingException {
    setVisible(Platform.get().inDevelopmentMode());
  }

  @Order(10.0)
  public class TestDataMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return Texts.get("CreateTestData");
    }

    @Override
    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
      return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
    }

    @Override
    protected void execAction() throws ProcessingException {
      TestDataForm form = new TestDataForm();
      form.startNew();
    }
  }

  @Order(20.0)
  public class InitialDataLoadMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return "Initiale Daten erstellen";
    }

    @Override
    protected void execAction() throws ProcessingException {
      BEANS.get(ISettingsOutlineService.class).intialDataLoad();
    }
  }

  @Order(30.0)
  public class SetupMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return Texts.get("Setup");
    }

    @Override
    protected void execAction() throws ProcessingException {
      SetupWizard wiz = new SetupWizard();
      wiz.start();
    }
  }

  @Order(40.0)
  public class SICardSimulatorMenu extends AbstractMenu {

    @Override
    protected String getConfiguredText() {
      return "Simulate SI-Card Action";
    }

    @Override
    protected Set<? extends IMenuType> getConfiguredMenuTypes() {
      return CollectionUtility.<IMenuType> hashSet(TableMenuType.EmptySpace);
    }

    @Override
    protected void execAction() throws ProcessingException {
      Long eventNr = BEANS.get(IDefaultProcessService.class).getDefaultEventNr();
      if (eventNr == null) {
        throw new VetoException("Default Event required.");
      }
      EventBean event = new EventBean();
      event.setEventNr(eventNr);
      event = BEANS.get(IEventProcessService.class).load(event);

      List<RtEcardStation> stations = BEANS.get(IResultsOutlineService.class).getECardStationTableData();
      if (stations == null || stations.isEmpty()) {
        throw new VetoException("E-Card Station required. Try to connect to a COM Port to create a Station.");
      }
      ECardStationFormData formData = new ECardStationFormData();
      Long eCardStationNr = stations.get(0).getId().getId();
      formData.setECardStationNr(eCardStationNr);
      formData = BEANS.get(IECardStationProcessService.class).load(formData);

      FMilaSerialPort port = new FMilaSerialPort() {

        @Override
        public void write(byte... data) throws IOException {
        }

        @Override
        public void close() {
        }

        @Override
        public void addEventListener(FMilaSerialEventListener listener) {
        }
      };

      AbstractSICardProcessor processor = new AbstractSICardProcessor(formData, port, event.getEvtZero(), eventNr, ClientSession.get()) {

        @Override
        protected Long readStartTime(byte[] data) throws ProcessingException {
          return null;
        }

        @Override
        protected long readSICardNrOfInsertedCard(byte[] data) {
          return 61321;
        }

        @Override
        protected String readSICardNoFromData(byte[] data) {
          return "61321";
        }

        @Override
        public String getECardNo() {
          return "61321";
        }

        @Override
        public ArrayList<PunchFormData> getControlData() {
          return new ArrayList<>();
        }

        @Override
        protected Long readFinishTime(byte[] data) throws ProcessingException {
          return null;
        }

        @Override
        protected ArrayList<PunchFormData> readControlsFromData(byte[] data) throws ProcessingException {
          return null;
        }

        @Override
        protected Long readClearTime(byte[] data) throws ProcessingException {
          return null;
        }

        @Override
        protected Long readCheckTime(byte[] data) throws ProcessingException {
          return null;
        }

        @Override
        protected byte[] getRequestDataCommand(int messageNr) throws ProcessingException {
          return null;
        }

        @Override
        public int getNumberOfDataMessages() throws ProcessingException {
          return 0;
        }
      };

      if (CompareUtility.equals(formData.getModus().getValue(), ECardStationDownloadModusCodeType.EntryCode.ID)) {
        SICardEntryJob job = new SICardEntryJob(ClientSession.get(), port, processor.getECardNo());
        job.schedule();
      }
      else {
        SICardDownloadJob job = new SICardDownloadJob(formData, ClientSession.get(), port, formData.getModus().getValue(), event.getEvtZero(), eventNr, processor);
        job.schedule();
      }
    }
  }

}
