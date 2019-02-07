package databasewriter;

import databasewriter.cla.DataBaseCmdArgsParser;
import databasewriter.contexts.Context;
import databasewriter.logging.DataWriterLoggerManager;
import org.apache.commons.cli.ParseException;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class MainDb {
  static public void main(String[] args) {
    DataBaseCmdArgsParser dataBaseCmdArgsParser = new DataBaseCmdArgsParser();
    try {
      dataBaseCmdArgsParser.parse(args);
      String strPort = dataBaseCmdArgsParser.getArgValue(DataBaseCmdArgsParser.PORT_NAME);
      Context context;
      if (strPort == null) {
        System.out.println("Listen on default port" + Context.DEFAULT_PORT);
        context = new Context(Context.DEFAULT_PORT);
      }
      else {
        int port = Integer.valueOf(strPort);
        context = new Context(port);
      }
      context.startApplication();
    }
    catch (ParseException | NumberFormatException exception) {
      dataBaseCmdArgsParser.printHelp("database writer");
    }
    catch (SQLException e) {
      DataWriterLoggerManager.getInstance().getLoggerApp().log(Level.SEVERE, e.getMessage());
    }
  }
}
