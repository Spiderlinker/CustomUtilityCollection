package de.spiderlinker.validate;

public class IdentityCheck {

  private static final int IDENTITY_LENGTH = 10;

  private IdentityCheck() {
    // Utility class
  }

  public static boolean isIdValid(String id) {
    Validate.requireNotNull(id);
    String correctedID = Validate.requireSpecificLengthOrTrim(id, IDENTITY_LENGTH);
    return checkId(correctedID);
  }

  private static boolean checkId(String id) {
    int sumOfId = calculateSumOfId(id);

    int checkDigit = CharacterUtils.getCharAtIndexAsDigit(id, id.length() - 1); // last digit of id
    int calculatedCheckDigit = CharacterUtils.getLastDigitOf(sumOfId);
    return checkDigit == calculatedCheckDigit;
  }

  private static int calculateSumOfId(String id) {
    int sum = 0;
    final int steps = 3;
    for (int i = 0; i + steps < id.length(); i += steps) {
      sum += 7 * CharacterUtils.getCharAtIndexAsDigit(id, i);
      sum += 3 * CharacterUtils.getCharAtIndexAsDigit(id, i + 1);
      sum += CharacterUtils.getCharAtIndexAsDigit(id, i + 2);
    }
    return sum;
  }

}
