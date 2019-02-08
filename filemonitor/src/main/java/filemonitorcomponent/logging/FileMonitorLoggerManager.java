package filemonitorcomponent.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class FileMonitorLoggerManager {
  private static FileMonitorLoggerManager instance = null;
  private static Logger logger;

  private FileMonitorLoggerManager() {
    logger = Logger.getLogger("filemonitorcomponent");
    SimpleFormatter formatter = new SimpleFormatter();
    try {
      File file = new File("file_monitor/logs");
      file.mkdirs();
      FileHandler fileHandler = new FileHandler("file_monitor/logs/app_logs.txt", true);
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

  public static FileMonitorLoggerManager getInstance() {
    if (instance == null) {
      instance = new FileMonitorLoggerManager();
    }
    return instance;
  }

  public Logger getLogger() {
    return logger;
  }
}
