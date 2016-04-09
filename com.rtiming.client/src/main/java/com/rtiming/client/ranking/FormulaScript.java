package com.rtiming.client.ranking;

import javax.script.Bindings;
import javax.script.Compilable;
import javax.script.CompiledScript;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.eclipse.scout.rt.platform.exception.ProcessingException;
import org.eclipse.scout.rt.platform.exception.VetoException;
import org.eclipse.scout.rt.platform.util.StringUtility;

import com.rtiming.shared.FMilaUtility;
import com.rtiming.shared.Texts;

public class FormulaScript {

  private static final String STATUS = "status";

  private final String script;
  private Bindings bindings;
  private CompiledScript compiledScript;

  /**
   * Compiles the script JavaScript and evaluates points for the input parameters (bindings)
   * 
   * @param script
   * @throws ProcessingException
   */
  public FormulaScript(String script) throws ProcessingException {
    super();
    this.script = script;
    init();
  }

  private void init() throws ProcessingException {

    // check formula
    if (StringUtility.isNullOrEmpty(StringUtility.trim(script))) {
      throw new VetoException(Texts.get("FormulaEmptyMessage"));
    }

    // Script
    ScriptEngineManager manager = new ScriptEngineManager();
    ScriptEngine engine = manager.getEngineByName("JavaScript");

    // Compile Formula
    Compilable compilingEngine = (Compilable) engine;
    try {
      compiledScript = compilingEngine.compile(script);
    }
    catch (ScriptException e) {
      handleScriptException(e, script);
    }

    bindings = engine.createBindings();
  }

  public void putBinding(String name, Object value) {
    bindings.put(name, value);
  }

  public Object getBinding(String name) {
    return bindings.get(name);
  }

  public void eval() throws ProcessingException {
    try {
      compiledScript.eval(bindings);
    }
    catch (ScriptException e) {
      handleScriptException(e, script);
    }
  }

  private void handleScriptException(ScriptException e, String formula) throws ProcessingException {
    throw new VetoException(Texts.get("FormulaError") + ": " +
        e.getMessage() +
        FMilaUtility.LINE_SEPARATOR +
        FMilaUtility.LINE_SEPARATOR +
        Texts.get("Formula") + ": " +
        FMilaUtility.LINE_SEPARATOR +
        formula);
  }

}
