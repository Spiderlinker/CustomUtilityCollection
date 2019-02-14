package de.spiderlinker.validate;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class IdentityCheckTest {

  private static final Logger LOGGER = LoggerFactory.getLogger(IdentityCheck.class);

  private String[] validIds = {"5108105415", "1220001297", "1530416505", "T220001293", "L22N1HGJK5", "1234123457"};
  private String[] randomIds = {"ABCDEFGHIJ", "1al3156463", "1530tosy05", "T20f01f2h9", "L22Jd78yK5", "1111111111"};

  @Test
  public void isIdValid() {
    for (String id : validIds) {
      LOGGER.info("Testing valid ID {}", id);
      Assertions.assertTrue(IdentityCheck.isIdValid(id));
    }
  }

  @Test
  public void isIdValidUpperCase() {
    for (String id : validIds) {
      LOGGER.info("Testing valid ID (UpperCase) {}", id);
      Assertions.assertTrue(IdentityCheck.isIdValid(id.toUpperCase()));
    }
  }

  @Test
  public void isIdValidLowerCase() {
    for (String id : validIds) {
      LOGGER.info("Testing valid ID (LowerCase) {}", id);
      Assertions.assertTrue(IdentityCheck.isIdValid(id.toLowerCase()));
    }
  }

  @Test
  public void isIdValidNull() {
    Assertions.assertThrows(NullPointerException.class, () -> IdentityCheck.isIdValid(null));
  }

  @Test
  public void isIdValidEmpty() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> IdentityCheck.isIdValid(""));
  }

  @Test
  public void isIdValidInvalidChar() {
    Assertions.assertFalse(IdentityCheck.isIdValid("__A8sa6f5_"));
  }

  @Test
  public void isIdValidTooShort() {
    Assertions.assertThrows(IllegalArgumentException.class, () -> IdentityCheck.isIdValid("510810541"));
  }

  @Test
  public void isIdValidValidIdTooLong() {
    Assertions.assertTrue(IdentityCheck.isIdValid("5108105415DTooLong"));
  }

  @Test
  public void isIdValidRandom() {
    for (String id : randomIds) {
      Assertions.assertFalse(IdentityCheck.isIdValid(id));
    }
  }


}