package cargoportcomponent.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The logger manager for cargo port app
 */
public class CargoPortLoggerManager {
  private static CargoPortLoggerManager instance = null;
  private static Logger logger;

  private CargoPortLoggerManager() {
    logger = Logger.getLogger("cargoportcomponent");
    SimpleFormatter formatter = new SimpleFormatter();
    try {
      File file = new File("cargo_port/logs");
      file.mkdirs();
      FileHandler fileHandler = new FileHandler("cargo_port/logs/app_logs.txt", true);
      fileHandler.setFormatter(formatter);
      logger.addHandler(fileHandler);
    }
    catch (IOException e) {
      System.out.println("It is impossible to create log file, logs will be written into console");
      ConsoleHandler consolehandler = new ConsoleHandler();
      consolehandler.setFormatter(formatter);
      logger.addHandler(consolehandler);
    }
  }

  public static CargoPortLoggerManager getInstance() {
    if (instance == null) {
       instance = new CargoPortLoggerManager();
    }
    return instance;
  }

  public Logger getLogger() {
    return logger;
  }
}
