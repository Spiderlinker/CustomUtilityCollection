package de.spiderlinker.validate;

import org.junit.Assert;
import org.junit.Test;

public class IdentityCheckTest {

  private String[] validIds = {"5108105415", "1220001297", "1530416505", "T220001293", "L22N1HGJK5", "1234123457"};
  private String[] randomIds = {"ABCDEFGHIJ", "1al3156463", "1530tosy05", "T20f01f2h9", "L22Jd78yK5", "1111111111"};

  @Test
  public void isIdValid() {
    for (String id : validIds) {
      Assert.assertTrue(IdentityCheck.isIdValid(id));
    }
  }

  @Test
  public void isIdValidUpperCase() {
    for (String id : validIds) {
      Assert.assertTrue(IdentityCheck.isIdValid(id.toUpperCase()));
    }
  }

  @Test
  public void isIdValidLowerCase() {
    for (String id : validIds) {
      Assert.assertTrue(IdentityCheck.isIdValid(id.toLowerCase()));
    }
  }

  @Test(expected = NullPointerException.class)
  public void isIdValidNull() {
    Assert.assertTrue(IdentityCheck.isIdValid(null));
  }

  @Test(expected = IllegalArgumentException.class)
  public void isIdValidEmpty() {
    Assert.assertTrue(IdentityCheck.isIdValid(""));
  }

  @Test
  public void isIdValidInvalidChar() {
    Assert.assertFalse(IdentityCheck.isIdValid("__A8sa6f5_"));
  }

  @Test
  public void isIdValidRandom() {
    for (String id : randomIds) {
      Assert.assertFalse(IdentityCheck.isIdValid(id));
    }
  }


}