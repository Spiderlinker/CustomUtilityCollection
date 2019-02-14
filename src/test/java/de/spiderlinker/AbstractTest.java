package de.spiderlinker;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;

public abstract class AbstractTest {

  @BeforeEach
  void printTestMethodName(TestInfo info) {
    info.getTestMethod().ifPresent(method -> System.out.println("-------- Starting test: " + method.getName() + "\n"));
  }

  @AfterEach
  void printTestFinished(TestInfo info) {
    info.getTestMethod().ifPresent(method -> System.out.println("\n------ Finished test ------\n"));
  }

}
