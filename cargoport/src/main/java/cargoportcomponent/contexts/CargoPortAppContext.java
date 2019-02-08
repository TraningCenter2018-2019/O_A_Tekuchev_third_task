package cargoportcomponent.contexts;

import cargoportcomponent.core.CargoPort;
import cargoportcomponent.logging.CargoPortLoggerManager;
import cargoportcomponent.ui.UserInterface;
import libs.simulationattributes.Crane;
import libs.simulationattributes.DeleteCraneCommand;
import libs.simulationattributes.Ship;
import libs.socketconnection.bytesockets.ByteClientSocket;
import libs.socketconnection.bytesockets.ByteServerSocket;
import libs.socketconnection.contracts.ClientToServerClientToServerContractSerializationImpl;
import libs.socketconnection.contracts.ServerToClientByteImpl;
import libs.socketconnection.contracts.ServerToClientContract;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CargoPortAppContext {
  static private final Logger LOGGER = CargoPortLoggerManager.getInstance().getLogger();

  static public final int DEFAULT_LISTEN_PORT = 6666;
  static public final String DEFAULT_SEND_IP = "127.0.0.1";
  static public final int DEFAULT_SEND_PORT = 6667;

  //static private final String EXIT_KEY_WORD = "q";

  private int inputPort;
  private String outputIp;
  private int outputPort;

  private ClientToServerClientToServerContractSerializationImpl clientToServerContractSerializationImpl;
  private ServerToClientContract serverToClientContract;
  private ByteServerSocket serverSocket;

  private CargoPort cargoPort;

  public CargoPortAppContext(int anInputPort, UserInterface ui, String anOutputIp, int anOutputPort) {
    try {
      inputPort = anInputPort;
      outputIp = anOutputIp;
      outputPort = anOutputPort;
      clientToServerContractSerializationImpl = new ClientToServerClientToServerContractSerializationImpl();
      serverToClientContract = new ServerToClientByteImpl();
      if (anOutputIp != null) {
        cargoPort = new CargoPort(3, ui, new ByteClientSocket(outputIp, outputPort), clientToServerContractSerializationImpl);
      }
      else {
        cargoPort = new CargoPort(3, ui);
      }
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
  }

  public CargoPortAppContext(int anInputPort, UserInterface ui) {
    this(anInputPort, ui, null, -1);
  }

  private byte[] inputDataProcessor(byte[] data) {
    Object recievedObject = clientToServerContractSerializationImpl.bytesToObject(data);
    String className = recievedObject.getClass().getName();
    if (className.equals(Ship.class.getName())) {
      cargoPort.putShipToRoadstead((Ship) recievedObject);
      return serverToClientContract.sendOk();
    }
    else if (className.equals(Crane.class.getName())) {
      cargoPort.addCrane((Crane) recievedObject);
      return serverToClientContract.sendOk();
    }
    else if (className.equals(DeleteCraneCommand.class.getName())) {
      cargoPort.deleteCrane(((DeleteCraneCommand) recievedObject).getCraneNumber());
      return serverToClientContract.sendOk();
    }
    else {
      return serverToClientContract.sendError(ServerToClientContract.UNEXPECTED_SENT_DATA_CODE,
              "Unexpected type: " + className);
    }
  }

  /*private void waitToExit() {
    Scanner scanner = new Scanner(System.in);
    String answer;
    while (!(answer = scanner.next()).equals(EXIT_KEY_WORD)) {
      scanner.skip(".");
    }
  }*/

  public void startApplication() {
    try {
      serverSocket = new ByteServerSocket(inputPort, this::inputDataProcessor);
      cargoPort.startPort();
      serverSocket.startListen();
      System.in.read();
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
    finally {
      serverSocket.stopListen();
      cargoPort.stopPort();
    }
    /*System.out.println("To close application type: " + EXIT_KEY_WORD);
    waitToExit();*/
  }

}
