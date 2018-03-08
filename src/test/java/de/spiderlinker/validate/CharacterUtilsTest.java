package de.spiderlinker.validate;

import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.*;

public class CharacterUtilsTest {

  @Test
  public void getDigitOrLetterSubtractedBy() {
  }

  @Test
  public void getLastDigitOfSingleNumber() {
    int number = 1;
    Assert.assertEquals(number, CharacterUtils.getLastDigitOf(number));
  }

  @Test
  public void getLastDigitOfSingleNegativeNumber() {
    int number = -1;
    Assert.assertEquals(1, CharacterUtils.getLastDigitOf(number));
  }

  @Test
  public void getLastDigitOfBigNumber() {
    int bigNumber = Integer.MAX_VALUE; // 2147483647
    Assert.assertEquals(7, CharacterUtils.getLastDigitOf(bigNumber));
  }

  @Test(expected = NullPointerException.class)
  public void getCharAtIndexAsDigitNull() {
    CharacterUtils.getCharAtIndexAsDigit(null, 0);
  }

  @Test(expected = StringIndexOutOfBoundsException.class)
  public void getCharAtIndexAsDigitInvalidIndex() {
    CharacterUtils.getCharAtIndexAsDigit("Test", -1);
  }

  @Test(expected = StringIndexOutOfBoundsException.class)
  public void getCharAtIndexAsDigitEmpty() {
    CharacterUtils.getCharAtIndexAsDigit("", 0);
  }

  @Test
  public void getCharAtIndexAsDigitNoDigit() {
    Assert.assertEquals(-1, CharacterUtils.getCharAtIndexAsDigit("_", 0));
  }

  @Test
  public void getCharAtIndexAsDigitAlreadyDigit() {
    Assert.assertEquals(5, CharacterUtils.getCharAtIndexAsDigit("a5a", 1));
  }

  @Test
  public void getCharAtIndexAsDigitTestAlphabetStartsAtValue10() {
    for(char c = 'a', value = 10; c <= 'z'; c++, value++){
      String charAsString = String.valueOf(c);
      Assert.assertEquals(value, CharacterUtils.getCharAtIndexAsDigit(charAsString.toLowerCase(), 0));
      Assert.assertEquals(value, CharacterUtils.getCharAtIndexAsDigit(charAsString.toUpperCase(), 0));
    }
  }
}