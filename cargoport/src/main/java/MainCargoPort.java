import bytesockets.ByteClientSocket;
import bytesockets.ByteServerSocket;
import bytesockets.requestresponseprocessors.ServerRequestProcessor;
import contexts.Context;
import contracts.ClientToServerContract;
import core.CargoPort;
import simulationattributes.Crane;
import simulationattributes.Ship;
import ui.CLI;

import java.io.IOException;

public class MainCargoPort {
  static public void main(String[] args) {
    Context appContext = new Context(6666, new CLI());
    appContext.startApplication();
    /*CargoPort cargoPort = new CargoPort(3, new CLI());

    ServerRequestProcessor func = bytes -> {
      ClientToServerContract cc = new ClientToServerContract();
      Object obj = cc.bytesToObject(bytes);
      String className = obj.getClass().getName();
      if (className.equals(Ship.class.getName())) {
        cargoPort.putShipToRoadstead((Ship) obj);
      }
      else if (className.equals(Crane.class.getName())) {
        cargoPort.addCrane((Crane) obj);
      }
      else {
        System.out.println("fuuuuuuuuuu");
      }
      return new byte[1];
    };

    ByteServerSocket serverSocket = new ByteServerSocket(6666, func);
    cargoPort.startPort();
    serverSocket.startListen();
    try {
      System.in.read();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
    serverSocket.stopListen();
    cargoPort.stopPort();*/
  }
}
