package cargoportcomponent;

import cargoportcomponent.cla.CargoPortCmdArgsParser;
import cargoportcomponent.contexts.CargoPortAppContext;
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
      CargoPortAppContext appContext;
      String strListenPort = cargoPortCmdArgsParser.getArgValue(CargoPortCmdArgsParser.LISTEN_PORT_NAME);
      int listenPort;
      if (strListenPort != null) {
        listenPort = Integer.valueOf(strListenPort);
      }
      else {
        listenPort = CargoPortAppContext.DEFAULT_LISTEN_PORT;
        System.out.println("Listen on default port " + listenPort);
      }
      String sendDataIp = cargoPortCmdArgsParser.getArgValue(CargoPortCmdArgsParser.SEND_DATA_IP_NAME);
      String sendDataPort = cargoPortCmdArgsParser.getArgValue(CargoPortCmdArgsParser.SEND_DATA_PORT_NAME);

      if (cargoPortCmdArgsParser.hasFlag(CargoPortCmdArgsParser.NOT_SEND_NAME)) {
        appContext = new CargoPortAppContext(listenPort, userInterface);
      }
      else {
        if (sendDataIp == null ^ sendDataPort == null) {
          System.out.println("The " + CargoPortCmdArgsParser.SEND_DATA_IP_NAME + " and " +  CargoPortCmdArgsParser.SEND_DATA_PORT_NAME +
                  " must be applied together");
          return;
        }
        if (sendDataIp != null && sendDataPort != null) {
            int sendPort = Integer.valueOf(sendDataPort);
            appContext = new CargoPortAppContext(listenPort, userInterface, sendDataIp, sendPort);
        }
        else {
          appContext = new CargoPortAppContext(listenPort, userInterface,
                  CargoPortAppContext.DEFAULT_SEND_IP, CargoPortAppContext.DEFAULT_SEND_PORT);
        }
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
