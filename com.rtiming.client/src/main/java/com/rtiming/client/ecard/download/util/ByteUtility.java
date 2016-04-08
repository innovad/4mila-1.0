package com.rtiming.client.ecard.download.util;

public final class ByteUtility {

  private ByteUtility() {
  }

  public static byte[] getSubarray(byte[] array, int offset, int length) {
    length = Math.min(length, array.length - offset);
    byte[] result = new byte[length];
    System.arraycopy(array, offset, result, 0, length);
    return result;
  }

  public static byte[] concatArrays(byte[] a, byte[] b) {
    byte[] c = new byte[a.length + b.length];
    System.arraycopy(a, 0, c, 0, a.length);
    System.arraycopy(b, 0, c, a.length, b.length);
    return c;
  }

  public static long getLongFromBytes(byte b0, byte b1, byte b2, byte b3) {
    return ((0xff & b0) << 24) | ((0xff & b1) << 16) | ((0xff & b2) << 8) | (0xff & b3);
  }

  public static long getLongFromBytes(byte b0, byte b1, byte b2) {
    return ((0xff & b0) << 16) | ((0xff & b1) << 8) | (0xff & b2);
  }

  public static long getLongFromBytes(byte b0, byte b1) {
    return (((b0 & 0xff) << 8) | (b1 & 0xff));
  }

  public static long getLongFromByte(byte b0) {
    return (b0 & 0xff);
  }

  public static String dumpBytes(byte[] buffer) {
    final byte[] hexChar = new byte[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    if (buffer == null) {
      return "";
    }

    StringBuilder sb = new StringBuilder(buffer.length * 4);

    for (int i = 0; i < buffer.length; i++) {
      sb.append("0x").append((char) (hexChar[(buffer[i] & 0x00F0) >> 4])).append(
              (char) (hexChar[buffer[i] & 0x000F])).append(" ");
    }

    return sb.toString();
  }

}
