package databasewriter.logging;

import java.io.File;
import java.io.IOException;
import java.util.logging.ConsoleHandler;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * The log manager for data writer application (singleton)
 */
public class DataWriterLoggerManager {
  private static  DataWriterLoggerManager instance = null;
  private static Logger loggerDb;
  private static Logger loggerApp;

  private DataWriterLoggerManager() {
    loggerApp = Logger.getLogger("databasewriter");
    loggerDb = Logger.getLogger("databasewriter.core");
    SimpleFormatter formatter = new SimpleFormatter();
    try {
      File file = new File("databasewriter/logs");
      file.mkdirs();

      FileHandler coreHandler = new FileHandler("databasewriter/logs/db_logs.txt", true);
      FileHandler appHandler = new FileHandler("databasewriter/logs/app_logs.txt", true);
      coreHandler.setFormatter(formatter);
      appHandler.setFormatter(formatter);
      loggerDb.addHandler(coreHandler);
      loggerApp.addHandler(appHandler);
    }
    catch (IOException e) {
      System.out.println("It is impossible to create log file, logs will be written into console");
      ConsoleHandler consolehandler = new ConsoleHandler();
      consolehandler.setFormatter(formatter);
      loggerApp.addHandler(consolehandler);
      loggerDb.addHandler(consolehandler);
    }
  }

  public static DataWriterLoggerManager getInstance() {
    if (instance == null) {
       instance = new DataWriterLoggerManager();
    }
    return instance;
  }

  public Logger getLoggerDb() {
    return loggerDb;
  }

  public Logger getLoggerApp() {
    return loggerApp;
  }
}
