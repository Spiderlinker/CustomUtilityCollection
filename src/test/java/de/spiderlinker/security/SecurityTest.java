package de.spiderlinker.security;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SecurityTest {

  @Test(expected = NullPointerException.class)
  public void hashPasswordSimpleWithNull() {
    Security.hashPasswordSimple(null);
  }

  @Test
  public void hashPasswordSimpleNotSameAsInput() {
    String simplePassword = "SimplePassword";
    Assert.assertNotEquals("Hashed password should be different from original password!",
        simplePassword, Security.hashPasswordSimple(simplePassword));
  }

  @Test
  public void hashPasswordSimpleSameResult() {
    String simplePassword = "SimplePassword";

    String firstHash = Security.hashPasswordSimple(simplePassword);
    String secondHash = Security.hashPasswordSimple(simplePassword);

    Assert.assertEquals("Hashing same password should produce same hash!", firstHash, secondHash);
  }

  @Test
  public void hashPasswordSimpleDifferentPasswordAndResult() {
    String simplePassword = "SimplePassword";
    String otherPassword = "OtherPassword";

    String firstHash = Security.hashPasswordSimple(simplePassword);
    String secondHash = Security.hashPasswordSimple(otherPassword);

    Assert.assertNotEquals("Hashing different password should produce different hash!", firstHash, secondHash);
  }

  @Test
  public void hashPasswordSimpleMinimalDifferentPasswordAndDifferentResult() {
    String simplePassword = "SimplePassword";
    String otherPassword = "simplePassword";

    String firstHash = Security.hashPasswordSimple(simplePassword);
    String secondHash = Security.hashPasswordSimple(otherPassword);

    Assert.assertNotEquals("Hashing different password should produce different hash!", firstHash, secondHash);
  }

  @Test
  public void hashPasswordWithSaltDifferentResult() {
    String simplePassword = "SimplePassword";

    String firstHash = Security.hashPasswordWithSalt(simplePassword);
    String secondHash = Security.hashPasswordWithSalt(simplePassword);

    Assert.assertNotEquals("Hashing same password with secure random salt should produce different hash!",
        firstHash, secondHash);
  }

  @Test(expected = NullPointerException.class)
  public void hashPasswordWithSaltWithNull() {
    Security.hashPasswordWithSalt(null);
  }

  @Test
  public void checkPasswordTwoHashNotSame() {
    String simplePassword = "SimplePassword";

    String firstHash = Security.hashPasswordWithSalt(simplePassword);
    String secondHash = Security.hashPasswordWithSalt(simplePassword);

    Assert.assertTrue("Verifying against original password should be true",
        Security.checkPassword(firstHash, simplePassword));
    Assert.assertTrue("Verifying against original password should be true",
        Security.checkPassword(secondHash, simplePassword));
  }

  @Test
  public void checkPasswordNotCorrect() {
    String simplePassword = "SimplePassword";
    String otherPassword = "OtherPassword";

    String simplePasswordHash = Security.hashPasswordWithSalt(simplePassword);

    Assert.assertTrue("Verifying against original password should be true",
        Security.checkPassword(simplePasswordHash, simplePassword));
    Assert.assertFalse("Verifying against wrong password should be false",
        Security.checkPassword(simplePasswordHash, otherPassword));
  }

  @Test(expected = NullPointerException.class)
  public void checkPasswordWithNullFirst() {
    Security.checkPassword(null, "Test");
  }

  @Test(expected = NullPointerException.class)
  public void checkPasswordWithNullSecond() {
    Security.checkPassword("Test", null);
  }

  @Test(expected = NullPointerException.class)
  public void checkPasswordWithNullBoth() {
    Security.checkPassword(null, null);
  }

  @Test
  public void generateRandomToken(){
    List<String> generatedTokens = new ArrayList<>();
    for(int i = 0; i< 10_000; i++) {
      String token = Security.generateRandomToken();
      Assert.assertFalse(generatedTokens.contains(token));
      generatedTokens.add(token);
    }
  }

}