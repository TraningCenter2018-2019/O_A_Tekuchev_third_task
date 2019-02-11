package filemonitorcomponent;

import filemonitorcomponent.cla.FileMonitorCmdArgsParser;
import filemonitorcomponent.contexts.FileMonitorAppContext;
import org.apache.commons.cli.ParseException;

public class FileMonitorMain {
  static public void main(String[] args) {
    FileMonitorCmdArgsParser fileMonitorCmdArgsParser = new FileMonitorCmdArgsParser();
    try {
      fileMonitorCmdArgsParser.parse(args);
      if (fileMonitorCmdArgsParser.hasFlag(FileMonitorCmdArgsParser.HELP_NAME)) {
        fileMonitorCmdArgsParser.printHelp(FileMonitorCmdArgsParser.HELP_DESCRIPTION);
        return;
      }
      if (fileMonitorCmdArgsParser.hasFlag(FileMonitorCmdArgsParser.IP_NAME) ^
              fileMonitorCmdArgsParser.hasFlag(FileMonitorCmdArgsParser.PORT_NAME)) {
        System.out.println("The " + FileMonitorCmdArgsParser.PORT_NAME + " and " +  FileMonitorCmdArgsParser.PORT_NAME +
                " must be applied together");
        return;
      }
      FileMonitorAppContext appContext;
      String directory = fileMonitorCmdArgsParser.hasFlag(FileMonitorCmdArgsParser.DIR_NAME) ?
              fileMonitorCmdArgsParser.getArgValue(FileMonitorCmdArgsParser.DIR_NAME) :
              FileMonitorAppContext.DEFAULT_MONITORED_DIRECTORY;
      if (fileMonitorCmdArgsParser.hasFlag(FileMonitorCmdArgsParser.IP_NAME) &&
              fileMonitorCmdArgsParser.hasFlag(FileMonitorCmdArgsParser.PORT_NAME)) {
        String ip = fileMonitorCmdArgsParser.getArgValue(FileMonitorCmdArgsParser.IP_NAME);
        String strPort = fileMonitorCmdArgsParser.getArgValue(FileMonitorCmdArgsParser.PORT_NAME);
        int port = Integer.valueOf(strPort);
        appContext = new FileMonitorAppContext(ip, port, directory);
      }
      else {
        appContext = new FileMonitorAppContext(FileMonitorAppContext.DEFAULT_SERVER_IP,
                FileMonitorAppContext.DEFAULT_PORT, directory);
      }
      appContext.startApplication();
    }
    catch (NumberFormatException e) {
      System.out.println("Invalid ip value");
    }
    catch (ParseException e) {
      fileMonitorCmdArgsParser.printHelp(FileMonitorCmdArgsParser.HELP_DESCRIPTION);
    }
  }
}
