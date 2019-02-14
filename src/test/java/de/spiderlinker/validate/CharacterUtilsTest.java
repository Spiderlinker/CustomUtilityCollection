package de.spiderlinker.validate;


import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CharacterUtilsTest {

  @Test
  public void getLastDigitOfSingleNumber() {
    int number = 1;
    Assertions.assertEquals(number, CharacterUtils.getLastDigitOf(number));
  }

  @Test
  public void getLastDigitOfSingleNegativeNumber() {
    int number = -1;
    Assertions.assertEquals(1, CharacterUtils.getLastDigitOf(number));
  }

  @Test
  public void getLastDigitOfBigNumber() {
    int bigNumber = Integer.MAX_VALUE; // 2147483647
    Assertions.assertEquals(7, CharacterUtils.getLastDigitOf(bigNumber));
  }

  @Test
  public void getCharAtIndexAsDigitNull() {
    Assertions.assertThrows(NullPointerException.class, () -> CharacterUtils.getCharAtIndexAsDigit(null, 0));
  }

  @Test
  public void getCharAtIndexAsDigitInvalidIndex() {
    Assertions.assertThrows(StringIndexOutOfBoundsException.class, () -> CharacterUtils.getCharAtIndexAsDigit("Test", -1));
  }

  @Test
  public void getCharAtIndexAsDigitEmpty() {
    Assertions.assertThrows(StringIndexOutOfBoundsException.class, () -> CharacterUtils.getCharAtIndexAsDigit("", 0));
  }

  @Test
  public void getCharAtIndexAsDigitNoDigit() {
    Assertions.assertEquals(-1, CharacterUtils.getCharAtIndexAsDigit("_", 0));
  }

  @Test
  public void getCharAtIndexAsDigitAlreadyDigit() {
    Assertions.assertEquals(5, CharacterUtils.getCharAtIndexAsDigit("a5a", 1));
  }

  @Test
  public void getCharAtIndexAsDigitTestAlphabetStartsAtValue10() {
    for (char c = 'a', value = 10; c <= 'z'; c++, value++) {
      String charAsString = String.valueOf(c);
      Assertions.assertEquals(value, CharacterUtils.getCharAtIndexAsDigit(charAsString.toLowerCase(), 0));
      Assertions.assertEquals(value, CharacterUtils.getCharAtIndexAsDigit(charAsString.toUpperCase(), 0));
    }
  }
}