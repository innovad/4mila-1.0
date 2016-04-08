package com.rtiming.shared.dataexchange;

public interface IProgressMonitor {

  void addInfo(String string, String value);

  void addInfoCreated(String msg, String info);

  void addInfoUpdated(String msg, String info);

  void addInfoDeleted(String msg, String info);

  void update(int counter, int length);

  void addErrors(ImportMessageList list);

}
