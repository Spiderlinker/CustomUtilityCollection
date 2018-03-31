package de.spiderlinker.utils;

import org.junit.Assert;
import org.junit.Test;

public class StringUtilsTest {

  @Test(expected = NullPointerException.class)
  public void exceptionToStringWithNullException() {
    StringUtils.exceptionToString(null);
  }

  @Test
  public void exceptionToStringValid() {
    String exceptionMessage = "This is a test exception";
    Exception exception = new Exception(exceptionMessage);
    Assert.assertNotEquals(exceptionMessage, StringUtils.exceptionToString(exception));
    Assert.assertTrue(StringUtils.exceptionToString(exception).contains(exceptionMessage));
  }

  @Test(expected = NullPointerException.class)
  public void requireNonNullOrEmptyWithNull() {
    StringUtils.requireNonNullOrEmpty(null);
  }

  @Test(expected = NullPointerException.class)
  public void requireNonNullOrEmptyWithEmptyString() {
    StringUtils.requireNonNullOrEmpty("  ");
  }

  @Test
  public void requireNonNullOrEmptyValid() {
    String testString = "notEmpty";
    Assert.assertEquals(testString, StringUtils.requireNonNullOrEmpty(testString));
  }

  @Test
  public void requireNonNullOrEmptyElseWithNull() {
    String elseString = "elseString";
    Assert.assertEquals(elseString, StringUtils.requireNonNullOrEmptyElse(null, elseString));
  }

  @Test
  public void requireNonNullOrEmptyElseWithEmptyString() {
    String elseString = "elseString";
    Assert.assertEquals(elseString, StringUtils.requireNonNullOrEmptyElse("  ", elseString));
  }

  @Test
  public void requireNonNullOrEmptyElseValid() {
    String testString = "notEmpty";
    Assert.assertEquals(testString, StringUtils.requireNonNullOrEmptyElse(testString, ""));
  }

  @Test
  public void isNullOrEmpty() {
    Assert.assertFalse(StringUtils.isNullOrEmpty("Test"));

    Assert.assertTrue(StringUtils.isNullOrEmpty(null));
    Assert.assertTrue(StringUtils.isNullOrEmpty(""));
    Assert.assertTrue(StringUtils.isNullOrEmpty("  "));
  }
}