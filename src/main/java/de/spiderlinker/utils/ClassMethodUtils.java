package de.spiderlinker.utils;

import java.lang.reflect.Field;

public class ClassMethodUtils {

  public static boolean equals(Object obj1, Object obj2) {
    if (obj1 == null || obj2 == null || !obj1.getClass().equals(obj2.getClass())) {
      return false;
    }

    boolean equal = true;
    Field[] fields = obj1.getClass().getDeclaredFields();
    for (Field f : fields) {
      if (equal) {
        try {
          f.setAccessible(true);
          equal &= compareObjects(f.get(obj1), f.get(obj2));
        } catch (IllegalArgumentException | IllegalAccessException e) {
          e.printStackTrace();
        }
      }
    }

    return equal;
  }

  public static boolean compareObjects(Object obj1, Object obj2) {
    if (obj1 == null && obj2 == null) {
      return true;
    } else if (obj1 != null && obj2 != null) {
      return obj1.equals(obj2);
    }
    return false;
  }

}
