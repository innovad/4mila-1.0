package com.rtiming.shared.settings;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.service.IService;
import org.eclipse.scout.rt.shared.TunnelToServer;

import com.rtiming.shared.common.AbstractCodeBoxData;

@TunnelToServer
public interface ICodeProcessService extends IService {

  AbstractCodeBoxData prepareCreateCodeBox(AbstractCodeBoxData codeBox) throws ProcessingException;

  AbstractCodeBoxData createCodeBox(AbstractCodeBoxData codeBox) throws ProcessingException;

  AbstractCodeBoxData loadCodeBox(AbstractCodeBoxData codeBox) throws ProcessingException;

  AbstractCodeBoxData storeCodeBox(AbstractCodeBoxData codeBox) throws ProcessingException;

  CodeFormData prepareCreate(CodeFormData formData) throws ProcessingException;

  CodeFormData create(CodeFormData formData) throws ProcessingException;

  CodeFormData load(CodeFormData formData) throws ProcessingException;

  CodeFormData store(CodeFormData formData) throws ProcessingException;

  String getTranslation(Long codeUid, Long clientNr) throws ProcessingException;

  CodeFormData find(String shortcut, long codeType) throws ProcessingException;

  void deleteCodeBox(AbstractCodeBoxData formData) throws ProcessingException;

}
