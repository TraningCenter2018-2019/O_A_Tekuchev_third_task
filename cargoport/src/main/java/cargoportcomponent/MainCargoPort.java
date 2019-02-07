package cargoportcomponent;

import cargoportcomponent.cla.CargoPortCmdArgsParser;
import cargoportcomponent.contexts.Context;
import cargoportcomponent.ui.CLI;
import cargoportcomponent.ui.UserInterface;
import org.apache.commons.cli.ParseException;

public class MainCargoPort {
  static public void main(String[] args) {
    CargoPortCmdArgsParser cargoPortCmdArgsParser = new CargoPortCmdArgsParser();
    try {
      cargoPortCmdArgsParser.parse(args);
      if (cargoPortCmdArgsParser.hasFlag(CargoPortCmdArgsParser.HELP_NAME)) {
        cargoPortCmdArgsParser.printHelp("cargo port");
        return;
      }
      UserInterface userInterface = new CLI();
      Context appContext;
      String strListenPort = cargoPortCmdArgsParser.getArgValue(CargoPortCmdArgsParser.LISTEN_PORT_NAME);
      int listenPort;
      if (strListenPort != null) {
        listenPort = Integer.valueOf(strListenPort);
      }
      else {
        listenPort = Context.DEFAULT_LISTEN_PORT;
        System.out.println("Listen on default port " + listenPort);
      }
      String sendDataIp = cargoPortCmdArgsParser.getArgValue(CargoPortCmdArgsParser.SEND_DATA_IP_NAME);
      String sendDataPort = cargoPortCmdArgsParser.getArgValue(CargoPortCmdArgsParser.SEND_DATA_PORT_NAME);
      if (sendDataIp == null ^ sendDataPort == null) {
        cargoPortCmdArgsParser.printHelp("cargo port");
        return;
      }
      if (sendDataIp != null && sendDataPort != null) {
          int sendPort = Integer.valueOf(sendDataPort);
          appContext = new Context(listenPort, userInterface, sendDataIp, sendPort);
      }
      else {
        appContext = new Context(listenPort, userInterface);
      }
      appContext.startApplication();
    }
    catch (ParseException e) {
      System.out.println("Unknown flag");
      cargoPortCmdArgsParser.printHelp("cargo port");
    }
    catch (NumberFormatException e) {
      System.out.println("Invalid ip value");
    }
  }
}
