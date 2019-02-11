package cargoportcomponent;

import cargoportcomponent.cla.CargoPortCmdArgsParser;
import cargoportcomponent.contexts.CargoPortAppContext;
import cargoportcomponent.ui.UserInterface;
import cargoportcomponent.ui.pseudoGui.MainForm;
import org.apache.commons.cli.ParseException;

public class MainCargoPort {
  static public void main(String[] args) {
    CargoPortCmdArgsParser cargoPortCmdArgsParser = new CargoPortCmdArgsParser();
    try {
      cargoPortCmdArgsParser.parse(args);
      if (cargoPortCmdArgsParser.hasFlag(CargoPortCmdArgsParser.HELP_NAME)) {
        cargoPortCmdArgsParser.printHelp(CargoPortCmdArgsParser.HELP_DESCRIPTION);
        return;
      }
      UserInterface userInterface = new MainForm(CargoPortAppContext.MAX_COUNT_CRANES);
      CargoPortAppContext appContext;
      String strListenPort = cargoPortCmdArgsParser.getArgValue(CargoPortCmdArgsParser.LISTEN_PORT);
      int listenPort;
      if (strListenPort != null) {
        listenPort = Integer.valueOf(strListenPort);
      }
      else {
        listenPort = CargoPortAppContext.DEFAULT_LISTEN_PORT;
      }
      String sendDataIp = cargoPortCmdArgsParser.getArgValue(CargoPortCmdArgsParser.SERVER_IP);
      String sendDataPort = cargoPortCmdArgsParser.getArgValue(CargoPortCmdArgsParser.SERVER_PORT);

      if (cargoPortCmdArgsParser.hasFlag(CargoPortCmdArgsParser.NOT_SEND)) {
        appContext = new CargoPortAppContext(listenPort, userInterface);
      }
      else {
        if (sendDataIp == null ^ sendDataPort == null) {
          System.out.println("The " + CargoPortCmdArgsParser.SERVER_IP + " and " +  CargoPortCmdArgsParser.SERVER_PORT +
                  " must be applied together");
          return;
        }
        if (sendDataIp != null && sendDataPort != null) {
            int sendPort = Integer.valueOf(sendDataPort);
            appContext = new CargoPortAppContext(listenPort, userInterface, sendDataIp, sendPort);
        }
        else {
          appContext = new CargoPortAppContext(listenPort, userInterface,
                  CargoPortAppContext.DEFAULT_SERVER_IP, CargoPortAppContext.DEFAULT_SERVER_PORT);
        }
      }
      appContext.startApplication();
    }
    catch (ParseException e) {
      System.out.println("Unknown flag");
      cargoPortCmdArgsParser.printHelp(CargoPortCmdArgsParser.HELP_DESCRIPTION);
    }
    catch (NumberFormatException e) {
      System.out.println("Invalid ip value");
    }
  }
}
