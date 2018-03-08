package de.spiderlinker.validate;

public class Validate {

  /**
   * Returns the passed object if it is not null. Otherwise a NullPointerException will be thrown.
   *
   * @param object Object to check
   * @param <T>    Type of object
   * @return passed object if not null
   * @throws NullPointerException if object is null
   */
  public static <T> T requireNotNull(T object) throws NullPointerException {
    if (object == null) {
      throw new NullPointerException("Object must not be null!");
    }
    return object;
  }

  /**
   * Returns the passed object if it is not null. Otherwise the 'otherwise' object will be returned
   *
   * @param object    Object to check if null
   * @param otherwise alternative object to return
   * @param <T>       Type of objects
   * @return 'object' if not null, if 'object' is null 'otherwise' will be returned
   */
  public static <T> T requireNotNullElse(T object, T otherwise) {
    return object == null
        ? otherwise
        : object;
  }

  /**
   * The passed {@link String} needs to have the specified length. If the String is shorter or longer an {@link IllegalArgumentException} will be thrown
   *
   * @param string String which length should be tested
   * @param length required length
   * @return passed string if its length is equal to specified length
   * @throws IllegalArgumentException If the string is not equal specified length
   */
  public static String requireSpecificLength(String string, int length) throws IllegalArgumentException {
    if (string.length() != length) {
      throw new IllegalArgumentException("Length of String has to be " + length);
    }
    return string;
  }

  /**
   * The passed {@link String} needs to have at least the specified length.
   * If the string is longer than specified it will be shortened.
   * If the string is shorter than specified an {@link IllegalArgumentException} will be thrown
   *
   * @param string String which length should be tested
   * @param length required length
   * @return passed string is longer than specified the shortened version of this string will be returned
   * @throws IllegalArgumentException If the string is shorter than specified length
   */
  public static String requireSpecificLengthOrTrim(String string, int length) {
    if (length >= 0 && string.length() >= length) {
      return string.length() == length
          ? string
          : string.substring(0, length);
    }
    throw new IllegalArgumentException("Invalid length or String has to be at least " + length);
  }

  /**
   * The passed character needs to be a letter or a digit.
   * If non of this is true an {@link IllegalArgumentException} will be thrown
   *
   * @param c character to test
   * @return character if it is a letter or digit
   * @throws IllegalArgumentException If the char is not a letter or digit
   */
  public static char requireLetterOrDigit(char c) {
    if (!Character.isLetterOrDigit(c)) {
      throw new IllegalArgumentException("Character is neither letter nor digit");
    }
    return c;
  }

}
