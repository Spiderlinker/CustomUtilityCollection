package de.spiderlinker.utils;

import java.io.PrintWriter;
import java.io.StringWriter;

public class StringUtils {

  /**
   * Converts the stack trace of the passed exception to a string
   *
   * @param e exception which stack trace should be converted
   * @return converted exception stack trace to a string
   */
  public static String exceptionToString(Exception e) {
    /* Create StringWriter to print exception to it*/
    StringWriter sw = new StringWriter();
    e.printStackTrace(new PrintWriter(sw));// print exception to StringWriter
    return sw.toString();// return exception as string
  }

  /**
   * Returns the passed String if it is not null. Otherwise a NullPointerException will be thrown.
   *
   * @param stringToTest String to check for null or empty
   * @return passed string if not null or empty
   * @throws NullPointerException if String is null or empty
   */
  public static String requireNonNullOrEmpty(String stringToTest) {
    if (isNullOrEmpty(stringToTest)) {
      throw new NullPointerException("String must not be null or empty!");
    }
    return stringToTest;
  }

  /**
   * Returns the passed String if it is not null. Otherwise the 'otherwise' String will be returned
   *
   * @param stringToTest String to check if null or empty
   * @param otherwise    alternative String to return
   * @return 'stringToTest' if not null, if 'stringToTest' is null or empty 'otherwise' will be returned
   */
  public static String requireNonNullOrEmptyElse(String stringToTest, String otherwise) {
    return isNullOrEmpty(stringToTest)
        ? otherwise
        : stringToTest;
  }

  /**
   * Tells if the passed String is null or en empty String
   *
   * @param stringToTest String which should be tested for null or Empty
   * @return true if String is null or empty, otherwise false
   */
  public static boolean isNullOrEmpty(String stringToTest) {
    return stringToTest == null || stringToTest.trim().isEmpty();
  }

}
