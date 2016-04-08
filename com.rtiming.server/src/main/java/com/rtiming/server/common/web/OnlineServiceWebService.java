package com.rtiming.server.common.web;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import javax.jws.WebService;

import org.eclipse.scout.commons.exception.ProcessingException;
import org.eclipse.scout.commons.logger.IScoutLogger;
import org.eclipse.scout.commons.logger.ScoutLogManager;
import org.eclipse.scout.rt.platform.BEANS;

import com._4mila._4mila.jaxws.online.Account;
import com._4mila._4mila.jaxws.online.CreateAccount;
import com._4mila._4mila.jaxws.online.FileData;
import com._4mila._4mila.jaxws.online.LoginAccount;
import com._4mila._4mila.jaxws.online.LoginAccountResponse;
import com._4mila._4mila.jaxws.online.OnlineServiceSoap;
import com._4mila._4mila.jaxws.online.Result;
import com._4mila._4mila.jaxws.online.TableData;
import com._4mila._4mila.jaxws.online.TableDataList;
import com._4mila._4mila.jaxws.online.UploadAccountResponse;
import com.rtiming.shared.common.file.IFileProcessService;
import com.rtiming.shared.common.security.Download;
import com.rtiming.shared.common.security.IUpdateService;
import com.rtiming.shared.map.IMapProcessService;
import com.rtiming.shared.runner.IRunnerProcessService;
import com.rtiming.shared.settings.account.AccountFormData;
import com.rtiming.shared.settings.account.IAccountProcessService;

// @ScoutWebService(sessionFactory = DefaultServerSessionFactory.class, authenticationHandler = NONE.class) TODO MIG
@WebService(endpointInterface = "com._4mila._4mila.jaxws.online.OnlineServiceSoap")
public class OnlineServiceWebService implements OnlineServiceSoap {

  private static IScoutLogger logger = ScoutLogManager.getLogger(OnlineServiceWebService.class);

  @Override
  public Result upload(String username, String password, long clientNr, long eventNr, TableDataList data) {

    Result result = new Result();
    result.setStatusNr(0);

    try {
      // upload all tables
      for (TableData tableData : data.getTables()) {
        UploadUtility.storeTable(tableData, clientNr, eventNr);
      }

      // delete all tables (reverse)
      ListIterator li = data.getTables().listIterator(data.getTables().size());
      while (li.hasPrevious()) {
        UploadUtility.deleteTable((TableData) li.previous(), clientNr, eventNr);
      }

      // cleanup files for all supplied paths
      String[] paths = new String[]{IMapProcessService.SERVER_MAP_DIR};
      for (String path : paths) {
        // load all maps
        List<Long> maps = BEANS.get(IMapProcessService.class).getAllMaps(clientNr);
        BEANS.get(IFileProcessService.class).cleanUpFiles(maps, clientNr, path);
      }

      // store files
      for (FileData fileData : data.getFiles()) {
        if (fileData.getContent() != null && fileData.getContent().length > 0) {
          BEANS.get(IFileProcessService.class).writeDataToFile(fileData.getPkNr(), fileData.getClientNr(), fileData.getFormat(), fileData.getContent(), fileData.getPath());
        }
      }

      // post upload tasks
      // update link between online accounts and uploaded runners (based on email address)
      BEANS.get(IRunnerProcessService.class).updateOnlineAccountRunnerLinks(null, clientNr, null);
    }
    catch (ProcessingException e) {
      // TODO MIG BEANS.get(IExceptionHandlerService.class).handleException(e);
      result.setStatusNr(1);
      result.setStatusMessage(e.getMessage());
    }

    return result;
  }

  @Override
  public UploadAccountResponse createAccountOnline(CreateAccount createaccount) {

    UploadAccountResponse response = new UploadAccountResponse();

    if (createaccount != null && createaccount.getAccountData() != null) {
      try {
        IAccountProcessService accountSvc = BEANS.get(IAccountProcessService.class);

        AccountFormData formData = AccountUtility.wsdlToFormData(createaccount.getAccountData());
        formData.setUpdatePassword(true); // account creation, force password encryption
        formData = accountSvc.create(formData, true);

        Account result = AccountUtility.formDataToWsdl(formData);
        response.setAccountData(result);
      }
      catch (ProcessingException e) {
        if (e.getCause() != null && e.getMessage() != null) {
          if (e.getCause().getMessage().contains("unique_username")) {
            logger.warn("unique username required", e);
          }
          else if (e.getCause().getMessage().contains("unique_email")) {
            logger.warn("unique email required", e);
          }
          else {
            // TODO MIG BEANS.get(IExceptionHandlerService.class).handleException(e);
          }
        }
        else {
          // TODO MIG BEANS.get(IExceptionHandlerService.class).handleException(e);
        }
      }
    }

    return response;
  }

  @Override
  public LoginAccountResponse loginAccountOnline(LoginAccount loginaccount) {
    LoginAccountResponse response = new LoginAccountResponse();

    if (loginaccount != null && loginaccount.getAccountData() != null) {
      try {
        IAccountProcessService accountSvc = BEANS.get(IAccountProcessService.class);

        // only create a new client nr when there is no client nr on event server
        // user may cancel setup wizard and retry
        boolean createNewClientNr = loginaccount.getAccountData().getClientNr() == null;

        AccountFormData formData = new AccountFormData();
        formData.getUsername().setValue(loginaccount.getAccountData().getUsername());
        formData.getPassword().setValue(loginaccount.getAccountData().getPassword());
        formData = accountSvc.login(formData, createNewClientNr);

        if (formData != null) {
          // login successful
          response.setAccountData(AccountUtility.formDataToWsdl(formData));
        }

      }
      catch (ProcessingException e) {
        // TODO MIG BEANS.get(IExceptionHandlerService.class).handleException(e);
      }
    }
    return response;
  }

  @Override
  public List<String> checkForUpdate(String versionToBeChecked) {
    List<Download> downloads = null;
    List<String> availableDownloadFiles = new ArrayList<String>();
    try {
      downloads = BEANS.get(IUpdateService.class).loadDownloads();
    }
    catch (ProcessingException e) {
      // TODO MIG BEANS.get(IExceptionHandlerService.class).handleException(e);
    }
    if (downloads != null && downloads.size() > 0) {
      for (Download download : downloads) {
        availableDownloadFiles.add(download.getFile());
      }
    }
    return availableDownloadFiles;
  }

}
