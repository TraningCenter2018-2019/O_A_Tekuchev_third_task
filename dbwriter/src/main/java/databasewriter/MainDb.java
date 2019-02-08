package databasewriter;

import databasewriter.cla.DataWriterCmdArgsParser;
import databasewriter.contexts.DataWriterAppContext;
import org.apache.commons.cli.ParseException;

public class MainDb {
  static public void main(String[] args) {
    DataWriterCmdArgsParser dataWriterCmdArgsParser = new DataWriterCmdArgsParser();
    try {
      dataWriterCmdArgsParser.parse(args);
      if (dataWriterCmdArgsParser.hasFlag(DataWriterCmdArgsParser.HELP_NAME)) {
        dataWriterCmdArgsParser.printHelp("The database writer");
        return;
      }
      String strPort = dataWriterCmdArgsParser.getArgValue(DataWriterCmdArgsParser.PORT_NAME);
      DataWriterAppContext context;
      if (strPort == null) {
        context = new DataWriterAppContext(DataWriterAppContext.DEFAULT_PORT);
      }
      else {
        int port = Integer.valueOf(strPort);
        context = new DataWriterAppContext(port);
      }
      context.startApplication();
    }
    catch (ParseException | NumberFormatException exception) {
      dataWriterCmdArgsParser.printHelp("The database writer");
    }
  }
}
