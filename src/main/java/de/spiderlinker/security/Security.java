package de.spiderlinker.security;

import de.mkammerer.argon2.Argon2;
import de.mkammerer.argon2.Argon2Factory;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

public class Security {

  private static final Logger LOG = LoggerFactory.getLogger(Security.class);

  private static final int ITERATIONS = 2;
  private static final int MEMORY     = 65536;
  private static final int THREADS    = 1;

  private static Argon2 argon2Instance;

  private static synchronized Argon2 getArgon2Instance() {
    if (argon2Instance == null) {
      argon2Instance = Argon2Factory.create();
      LOG.info("Instantiating CryptoLibrary Argon2: {}", argon2Instance.getClass());
    }
    return argon2Instance;
  }

  /**
   * Hashes the given password with a predefined algorithm (SHA)
   *
   * @param password password to hash
   * @return hash of password
   */
  public static String hashPasswordSimple(String password) {
    return DigestUtils.shaHex(password);
  }

  /**
   * Hashes (with secure unique salt) the given password and passes the created hash back
   *
   * @param password Password to hash (with salt)
   * @return Hash (with salt) of given password
   */
  public static String hashPasswordWithSalt(String password) {
    return getArgon2Instance().hash(ITERATIONS, MEMORY, THREADS, password);
  }

  /**
   * Checks if the passed password hash matches the password.
   * If the password hash matches the password this method will return true; false otherwise.
   *
   * @param passwordHash Hash to validate
   * @param password     Password to validate against
   * @return true, if password hash matches password; false otherwise
   */
  public static boolean checkPassword(String passwordHash, String password) {
    return getArgon2Instance().verify(passwordHash, password);
  }

  /**
   * Generates a unique and random 256bit token (64 chars)
   *
   * @return random token with a length of 64 chars
   */
  public static String generateRandomToken() {
    return (getUUIDAsString() + getUUIDAsString()).replaceAll("-", "");
  }

  /**
   * @return UUID as String
   */
  private static String getUUIDAsString() {
    return UUID.randomUUID().toString();
  }

}
