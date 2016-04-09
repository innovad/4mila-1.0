package com.rtiming.client.common.infodisplay;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.scout.rt.platform.exception.ProcessingException;

import com.rtiming.shared.FMilaUtility;

/**
 * 
 */
public final class SafariMacUtility {

  public static void openURLinSafari(String location) throws ProcessingException {

    try {

      String script = "" +
          "tell application \"Safari\" " + FMilaUtility.LINE_SEPARATOR +
          "  if not (get running of application \"Safari\") then " + FMilaUtility.LINE_SEPARATOR +
          "     launch " + FMilaUtility.LINE_SEPARATOR +
          "     activate " + FMilaUtility.LINE_SEPARATOR +
          "     delay 1 " + FMilaUtility.LINE_SEPARATOR +
          "  end if " + FMilaUtility.LINE_SEPARATOR +
          "  set windowCount to number of windows " + FMilaUtility.LINE_SEPARATOR +
          "  if (windowCount < 1) then " + FMilaUtility.LINE_SEPARATOR +
          "     open location \"" + location + "\" " + FMilaUtility.LINE_SEPARATOR +
          "  else " + FMilaUtility.LINE_SEPARATOR +
          "     set the URL of the front document to \"" + location + "\"  " + FMilaUtility.LINE_SEPARATOR +
          "  end if " + FMilaUtility.LINE_SEPARATOR +
          "end tell";

      ScriptEngineManager mgr = new ScriptEngineManager();
      ScriptEngine engine = mgr.getEngineByName("AppleScript");
      engine.eval(script);

    }
    catch (ScriptException e) {
      e.printStackTrace();
      throw new ProcessingException("Could not open URL in Safari", e);
    }

  }

}
