package de.spiderlinker.validate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class ValidateTest {

  @Test
  public void requireNotNullWithNull() {
    Assertions.assertThrows(NullPointerException.class, () -> Validate.requireNotNull(null));
  }

  @Test
  public void requireNotNullWithNotNull() {
    Assertions.assertNotNull(Validate.requireNotNull(new Object()));
  }

  @Test
  public void requireNotNullWithGetSameObjectBack() {
    String testString = "Test";
    String passedBack = Validate.requireNotNull(testString);
    Assertions.assertEquals(testString, passedBack);
  }

  @Test
  public void requireNotNullElseWithNull() {
    Object testObject = new Object();
    Object passedBack = Validate.requireNotNullElse(null, testObject);
    Assertions.assertEquals(testObject, passedBack);
  }

  @Test
  public void requireNotNullElseWithNotNull() {
    Object notNullObject = new Object();
    Object passedBack = Validate.requireNotNullElse(notNullObject, null);
    Assertions.assertEquals(notNullObject, passedBack);
  }

  @Test
  public void requireNotNullElseWithGetSameObjectBack() {
    Assertions.assertNull(Validate.requireNotNullElse(null, null));
  }

  @Test
  public void requireSpecificLengthNull() {
    Assertions.assertThrows(NullPointerException.class, () -> Validate.requireSpecificLength(null, 0));
  }

  @Test
  public void requireSpecificLengthTooShort() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Validate.requireSpecificLength("Test", 5));
  }

  @Test
  public void requireSpecificLengthTooLong() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Validate.requireSpecificLength("Test", 3));
  }

  @Test
  public void requireSpecificLengthEmpty() {
    Assertions.assertNotNull(Validate.requireSpecificLength("", 0));
  }

  @Test
  public void requireSpecificLength() {
    Assertions.assertNotNull(Validate.requireSpecificLength("Test", 4));
  }

  @Test
  public void requireSpecificLengthGetSameObjectBack() {
    String testString = "Test";
    String passedBack = Validate.requireSpecificLength(testString, testString.length());
    Assertions.assertEquals(testString, passedBack);
  }

  @Test
  public void requireSpecificLengthOrTrimNull() {
    Assertions.assertThrows(NullPointerException.class, () -> Validate.requireSpecificLengthOrTrim(null, 0));
  }

  @Test
  public void requireSpecificLengthOrTrimTooShort() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Validate.requireSpecificLengthOrTrim("Test", 5));
  }

  @Test
  public void requireSpecificLengthOrTrimTooLong() {
    int maxLength = 3;
    String testString = "Test";
    String trimmedString = "Tes"; // or testString.subString(0, maxLength)
    String returnedString = Validate.requireSpecificLengthOrTrim(testString, maxLength);
    Assertions.assertEquals(trimmedString, returnedString);
  }

  @Test
  public void requireSpecificLengthOrTrim() {
    String testString = "Test";
    String returnedString = Validate.requireSpecificLengthOrTrim(testString, testString.length());
    Assertions.assertEquals(testString, returnedString);
  }

  @Test
  public void requireSpecificLengthOrTrimInvalidLength() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> Validate.requireSpecificLengthOrTrim("Test", -1));
  }

  @Test
  public void requireSpecificLengthOrTrimGetSameObjectBack() {
    String testString = "Test";
    String passedBack = Validate.requireSpecificLengthOrTrim(testString, testString.length());
    Assertions.assertEquals(testString, passedBack);
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
      Assertions.fail("Character is letter or digit: " + c);
    }
  }
}