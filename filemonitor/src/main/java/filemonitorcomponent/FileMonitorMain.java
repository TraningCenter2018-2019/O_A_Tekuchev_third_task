package filemonitorcomponent;

import filemonitorcomponent.cla.FileMonitorCmdArgsParser;
import filemonitorcomponent.contexts.FileMonitorAppContext;
import libs.parsers.jsonparsers.JsonConverter;
import libs.parsers.jsonparsers.exceptions.CannotConvertObjectException;
import libs.simulationattributes.DeleteCraneCommand;
import org.apache.commons.cli.ParseException;

import java.io.FileWriter;
import java.io.IOException;

public class FileMonitorMain {
  static public void main(String[] args) {
    FileMonitorCmdArgsParser fileMonitorCmdArgsParser = new FileMonitorCmdArgsParser();
    try {
      fileMonitorCmdArgsParser.parse(args);
      if (fileMonitorCmdArgsParser.hasFlag(FileMonitorCmdArgsParser.HELP_NAME)) {
        fileMonitorCmdArgsParser.printHelp("file monitor");
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
        appContext = new FileMonitorAppContext(FileMonitorAppContext.DEFAULT_SEND_IP,
                FileMonitorAppContext.DEFAULT_PORT, directory);
      }

     /* try (FileWriter fw = new FileWriter("delcrane.json")) {
        fw.write(new JsonConverter().objectToJson(new DeleteCraneCommand(1)));
      }
      catch (IOException e) {
        e.printStackTrace();
      }
      catch (CannotConvertObjectException e) {
        e.printStackTrace();
      }*/

      appContext.startApplication();

      /*JsonConverter jc = new JsonConverter();
      ByteClientSocket byteClientSocket = new ByteClientSocket("127.0.0.1", 6666);
      FileMonitor fileMonitorManager =
              new FileMonitor("./monitored", byteClientSocket, new ClientToServerClientToServerContractSerializationImpl(), jc);
      fileMonitorManager.startMonitoring();
      System.in.read();
      fileMonitorManager.stopMonitoring();
      byteClientSocket.stopConnection();*/
    }
    catch (NumberFormatException e) {
      System.out.println("Invalid ip value");
    }
    catch (ParseException e) {
      fileMonitorCmdArgsParser.printHelp("file monitor");
    }
  }
}
