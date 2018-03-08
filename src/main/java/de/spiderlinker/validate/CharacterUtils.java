package de.spiderlinker.validate;

public class CharacterUtils {

//  public static int getDigitOrLetterSubtractedBy(char c, int subtractBy) {
//    Validate.requireLetterOrDigit(c);
//    return Character.isDigit(c)
//        ? Character.getNumericValue(c)
//        : Character.toUpperCase(c) - subtractBy;
//  }

  public static int getLastDigitOf(int number) {
    String numberAsString = String.valueOf(number);
    return getCharAtIndexAsDigit(numberAsString, numberAsString.length() - 1);
  }

  /**
   * Returns the value of the char at the specified index.
   * If the character does not have a numeric value, then -1 is returned.
   * If the character has a numeric value that cannot be represented as a
   * nonnegative integer (for example, a fractional value), then -2 is returned.
   * Have a look at {@link Character#getNumericValue(char)}
   *
   * @param chars characters
   * @param index index of character
   * @return numeric value of specified char (at index)
   */
  public static int getCharAtIndexAsDigit(String chars, int index) {
    return Character.getNumericValue(chars.charAt(index));
  }


}
