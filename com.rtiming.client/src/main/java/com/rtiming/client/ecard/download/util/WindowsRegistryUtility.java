package com.rtiming.client.ecard.download.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

public final class WindowsRegistryUtility {

  private WindowsRegistryUtility() {
  }

  public static String readRegistry(String location, String key) {
    try {
      Process process = Runtime.getRuntime().exec("reg query \"" + location + "\" /v " + key); //$NON-NLS-1$ //$NON-NLS-2$
      StreamReader reader = new StreamReader(process.getInputStream());
      reader.start();
      process.waitFor();
      reader.join();

      String output = reader.getResult();
      // Output has the following format:
      // \n<Version information>\n\n<key>\t<registry type>\t<value>
      if (!output.contains("\t")) { //$NON-NLS-1$
        return null;
      }
      // Parse out the value
      String[] parsed = output.split("\t"); //$NON-NLS-1$
      return parsed[parsed.length - 1];
    }
    catch (Exception e) {
      return null;
    }
  }

  public static String listRegistryEntries(String location) {
    try {
      Process process = Runtime.getRuntime().exec("reg query \"" + location + "\" /s"); //$NON-NLS-1$ //$NON-NLS-2$
      StreamReader reader = new StreamReader(process.getInputStream());
      reader.start();
      process.waitFor();
      reader.join();

      return reader.getResult();
    }
    catch (Exception e) {
      return null;
    }
  }

  static class StreamReader extends Thread {
    private final InputStream is;
    private final StringWriter sw = new StringWriter();

    public StreamReader(InputStream is) {
      this.is = is;
    }

    @Override
    public void run() {
      try {
        int c;
        while ((c = is.read()) != -1) {
          sw.write(c);
        }
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }

    public String getResult() {
      return sw.toString();
    }
  }
}
