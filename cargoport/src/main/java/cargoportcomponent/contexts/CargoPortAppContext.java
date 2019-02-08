package cargoportcomponent.contexts;

import cargoportcomponent.core.CargoPort;
import cargoportcomponent.core.exceptions.MaxCountCranesException;
import cargoportcomponent.core.exceptions.UniqueValueException;
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
  static public final int COUNT_CRANES = 3;

  //static private final String EXIT_KEY_WORD = "q";

  private int inputPort;
  private String outputIp;
  private int outputPort;
  private UserInterface userInterface;
  private ClientToServerClientToServerContractSerializationImpl clientToServerContractSerializationImpl;
  private ServerToClientContract serverToClientContract;
  private ByteServerSocket serverSocket;

  private CargoPort cargoPort;

  public CargoPortAppContext(int anInputPort, UserInterface ui, String anOutputIp, int anOutputPort) {
    try {
      userInterface = ui;
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
      Crane crane = (Crane) recievedObject;
      try  {
        cargoPort.addCrane(crane);
        return serverToClientContract.sendOk();
      }
      catch (UniqueValueException e) {
        return serverToClientContract.sendError(ServerToClientContract.CANNOT_PERFORM_OPERATION,
                "The crane with number " + crane.getNumber() + " is already exists");
      }
      catch (MaxCountCranesException e) {
        return serverToClientContract.sendError(ServerToClientContract.CANNOT_PERFORM_OPERATION,
                "The count of cranes is maximum");
      }
    }
    else if (className.equals(DeleteCraneCommand.class.getName())) {
      int number = ((DeleteCraneCommand) recievedObject).getCraneNumber();
      if (cargoPort.deleteCrane(number)) {
        return serverToClientContract.sendOk();
      }
      else {
        return serverToClientContract.sendError(ServerToClientContract.CANNOT_PERFORM_OPERATION,
                "No crane with this number: " + number);
      }
    }
    else {
      return serverToClientContract.sendError(ServerToClientContract.UNEXPECTED_SENT_DATA_CODE,
              "Unexpected type: " + className);
    }
  }

  public void startApplication() {
    try {
      serverSocket = new ByteServerSocket(inputPort, this::inputDataProcessor);
      cargoPort.startPort();
      serverSocket.startListen();
      userInterface.show();
      //System.in.read();
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
    finally {
      serverSocket.stopListen();
      cargoPort.stopPort();
    }
  }

}
