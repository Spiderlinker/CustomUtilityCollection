package de.spiderlinker.validate;

import org.junit.Assert;
import org.junit.Test;

public class ValidateTest {

  @Test(expected = NullPointerException.class)
  public void requireNotNullWithNull() {
    Validate.requireNotNull(null);
  }

  @Test
  public void requireNotNullWithNotNull() {
    Assert.assertNotNull(Validate.requireNotNull(new Object()));
  }

  @Test
  public void requireNotNullWithGetSameObjectBack() {
    String testString = "Test";
    String passedBack = Validate.requireNotNull(testString);
    Assert.assertEquals(testString, passedBack);
  }

  @Test
  public void requireNotNullElseWithNull() {
    Object testObject = new Object();
    Object passedBack = Validate.requireNotNullElse(null, testObject);
    Assert.assertEquals(testObject, passedBack);
  }

  @Test
  public void requireNotNullElseWithNotNull() {
    Object notNullObject = new Object();
    Object passedBack = Validate.requireNotNullElse(notNullObject, null);
    Assert.assertEquals(notNullObject, passedBack);
  }

  @Test
  public void requireNotNullElseWithGetSameObjectBack() {
    Assert.assertNull(Validate.requireNotNullElse(null, null));
  }

  @Test(expected = NullPointerException.class)
  public void requireSpecificLengthNull() {
    Validate.requireSpecificLength(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void requireSpecificLengthTooShort() {
    Validate.requireSpecificLength("Test", 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void requireSpecificLengthTooLong() {
    Validate.requireSpecificLength("Test", 3);
  }

  @Test
  public void requireSpecificLengthEmpty() {
    Assert.assertNotNull(Validate.requireSpecificLength("", 0));
  }

  @Test
  public void requireSpecificLength() {
    Assert.assertNotNull(Validate.requireSpecificLength("Test", 4));
  }

  @Test
  public void requireSpecificLengthGetSameObjectBack() {
    String testString = "Test";
    String passedBack = Validate.requireSpecificLength(testString, testString.length());
    Assert.assertEquals(testString, passedBack);
  }

  @Test(expected = NullPointerException.class)
  public void requireSpecificLengthOrTrimNull() {
    Validate.requireSpecificLengthOrTrim(null, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void requireSpecificLengthOrTrimTooShort() {
    Validate.requireSpecificLengthOrTrim("Test", 5);
  }

  @Test
  public void requireSpecificLengthOrTrimTooLong() {
    int maxLength = 3;
    String testString = "Test";
    String trimmedString = "Tes"; // or testString.subString(0, maxLength)
    String returnedString = Validate.requireSpecificLengthOrTrim(testString, maxLength);
    Assert.assertEquals(trimmedString, returnedString);
  }

  @Test
  public void requireSpecificLengthOrTrim() {
    String testString = "Test";
    String returnedString = Validate.requireSpecificLengthOrTrim(testString, testString.length());
    Assert.assertEquals(testString, returnedString);
  }

  @Test(expected = IllegalArgumentException.class)
  public void requireSpecificLengthOrTrimInvalidLength() {
    Assert.assertNotNull(Validate.requireSpecificLengthOrTrim("Test", -1));
  }

  @Test
  public void requireSpecificLengthOrTrimGetSameObjectBack() {
    String testString = "Test";
    String passedBack = Validate.requireSpecificLengthOrTrim(testString, testString.length());
    Assert.assertEquals(testString, passedBack);
  }

  @Test
  public void requireLetterOrDigitAllFine() {
    for (char c : "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".toCharArray()) {
      Validate.requireLetterOrDigit(c);
    }
  }

  @Test
  public void requireLetterOrDigitNonLetter() {
    for (char c : ("!\"§$%&/()=?\\{[]}*+~'#-_.:,;<>|@€´`").toCharArray()) {
      try {
        Validate.requireLetterOrDigit(c);
      } catch (IllegalArgumentException e) {
        /* The method has to throw an exception
         * at any passed character in this method.
         * If not the test should fail */
        continue;
      }
      Assert.fail("Character is letter or digit: " + c);
    }
  }
}