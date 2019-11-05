package de.spiderlinker.network.data;

import de.spiderlinker.utils.ClassMethodUtils;
import org.apache.commons.lang3.SerializationUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DataPackageTest {

  private String testID = "#testID";

  @Test
  void testSetSessionTokenAndOthersNull() {
  }

  @Test
  void testSetUsernameAndOthersNull() {
  }

  @Test
  void testSerializationDataPackage() {
    String testString1 = "#testString_1";
    String testString2 = "#testString_2";
    Integer testInteger = 404;
    DataPackage dataPackage = new DataPackage(testID, testString1, testString2, testInteger);
    DataPackage serializedDataPackage = SerializationUtils.clone(dataPackage);

    System.out.println("DataPackages compare:\n\t" + dataPackage + "\n\t" + serializedDataPackage);

    Assertions.assertTrue(ClassMethodUtils.equals(dataPackage, serializedDataPackage));
  }

}
