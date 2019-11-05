package de.spiderlinker.utils;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class StringUtilsTest {

  @Test
  public void exceptionToStringWithNullException() {
    Assertions.assertThrows(NullPointerException.class, () -> StringUtils.exceptionToString(null));
  }

  @Test
  public void exceptionToStringValid() {
    String exceptionMessage = "This is a test exception";
    Exception exception = new Exception(exceptionMessage);
    Assertions.assertNotEquals(exceptionMessage, StringUtils.exceptionToString(exception));
    Assertions.assertTrue(StringUtils.exceptionToString(exception).contains(exceptionMessage));
  }

  @Test
  public void requireNonNullOrEmptyWithNull() {
    Assertions.assertThrows(NullPointerException.class, () -> StringUtils.requireNonNullOrEmpty(null));
  }

  @Test
  public void requireNonNullOrEmptyWithEmptyString() {
    Assertions.assertThrows(NullPointerException.class, () -> StringUtils.requireNonNullOrEmpty("  "));
  }

  @Test
  public void requireNonNullOrEmptyValid() {
    String testString = "notEmpty";
    Assertions.assertEquals(testString, StringUtils.requireNonNullOrEmpty(testString));
  }

  @Test
  public void requireNonNullOrEmptyElseWithNull() {
    String elseString = "elseString";
    Assertions.assertEquals(elseString, StringUtils.requireNonNullOrEmptyElse(null, elseString));
  }

  @Test
  public void requireNonNullOrEmptyElseWithEmptyString() {
    String elseString = "elseString";
    Assertions.assertEquals(elseString, StringUtils.requireNonNullOrEmptyElse("  ", elseString));
  }

  @Test
  public void requireNonNullOrEmptyElseValid() {
    String testString = "notEmpty";
    Assertions.assertEquals(testString, StringUtils.requireNonNullOrEmptyElse(testString, ""));
  }

  @Test
  public void isNullOrEmpty() {
    Assertions.assertFalse(StringUtils.isNullOrEmpty("Test"));

    Assertions.assertTrue(StringUtils.isNullOrEmpty(null));
    Assertions.assertTrue(StringUtils.isNullOrEmpty(""));
    Assertions.assertTrue(StringUtils.isNullOrEmpty("  "));
  }
}