package de.spiderlinker.security;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

public class SecurityTest {

  @BeforeEach
  public void setup() {
    long start = System.currentTimeMillis();
    Security.hashPasswordWithSalt("SamplePassword");
    long end = System.currentTimeMillis();
    System.out.println("Needed time: " + (end - start) + "ms");
  }

  @Test
  public void hashPasswordSimpleWithNull() {
    Assertions.assertThrows(NullPointerException.class, () -> Security.hashPasswordSimple(null));
  }

  @Test
  public void hashPasswordSimpleNotSameAsInput() {
    String simplePassword = "SimplePassword";
    Assertions.assertNotEquals(simplePassword, Security.hashPasswordSimple(simplePassword), "Hashed password should be different from original password!");
  }

  @Test
  public void hashPasswordSimpleSameResult() {
    String simplePassword = "SimplePassword";

    String firstHash = Security.hashPasswordSimple(simplePassword);
    String secondHash = Security.hashPasswordSimple(simplePassword);

    Assertions.assertEquals(firstHash, secondHash, "Hashing same password should produce same hash!");
  }

  @Test
  public void hashPasswordSimpleDifferentPasswordAndResult() {
    String simplePassword = "SimplePassword";
    String otherPassword = "OtherPassword";

    String firstHash = Security.hashPasswordSimple(simplePassword);
    String secondHash = Security.hashPasswordSimple(otherPassword);

    Assertions.assertNotEquals(firstHash, secondHash, "Hashing different password should produce different hash!");
  }

  @Test
  public void hashPasswordSimpleMinimalDifferentPasswordAndDifferentResult() {
    String simplePassword = "SimplePassword";
    String otherPassword = "simplePassword";

    String firstHash = Security.hashPasswordSimple(simplePassword);
    String secondHash = Security.hashPasswordSimple(otherPassword);

    Assertions.assertNotEquals(firstHash, secondHash, "Hashing different password should produce different hash!");
  }

  @Test
  public void hashPasswordWithSaltDifferentResult() {
    String simplePassword = "SimplePassword";

    String firstHash = Security.hashPasswordWithSalt(simplePassword);
    String secondHash = Security.hashPasswordWithSalt(simplePassword);

    Assertions.assertNotEquals(firstHash, secondHash, "Hashing same password with secure random salt should produce different hash!");
  }

  @Test
  public void hashPasswordWithSaltWithNull() {
    Assertions.assertThrows(NullPointerException.class, () -> Security.hashPasswordWithSalt(null));
  }

  @Test
  public void checkPasswordTwoHashNotSame() {
    String simplePassword = "SimplePassword";

    String firstHash = Security.hashPasswordWithSalt(simplePassword);
    String secondHash = Security.hashPasswordWithSalt(simplePassword);

    Assertions.assertTrue(Security.checkHashMatchingPassword(firstHash, simplePassword), "Verifying against original password should be true");
    Assertions.assertTrue(Security.checkHashMatchingPassword(secondHash, simplePassword), "Verifying against original password should be true");
  }

  @Test
  public void checkPasswordNotCorrect() {
    String simplePassword = "SimplePassword";
    String otherPassword = "OtherPassword";

    String simplePasswordHash = Security.hashPasswordWithSalt(simplePassword);

    Assertions.assertTrue(Security.checkHashMatchingPassword(simplePasswordHash, simplePassword), "Verifying against original password should be true");
    Assertions.assertFalse(Security.checkHashMatchingPassword(simplePasswordHash, otherPassword), "Verifying against wrong password should be false");
  }

  @Test
  public void checkPasswordWithNullFirst() {
    Assertions.assertThrows(NullPointerException.class, () -> Security.checkHashMatchingPassword(null, "Test"));
  }

  @Test
  public void checkPasswordWithNullSecond() {
    Assertions.assertThrows(NullPointerException.class, () -> Security.checkHashMatchingPassword("Test", null));
  }

  @Test
  public void checkPasswordWithNullBoth() {
    Assertions.assertThrows(NullPointerException.class, () -> Security.checkHashMatchingPassword(null, null));
  }

  @Test
  public void generateRandomToken() {
    List<String> generatedTokens = new ArrayList<>();
    for (int i = 0; i < 10_000; i++) {
      String token = Security.generateRandomToken();
      Assertions.assertFalse(generatedTokens.contains(token));
      generatedTokens.add(token);
    }
  }

}