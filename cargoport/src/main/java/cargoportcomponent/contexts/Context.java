package cargoportcomponent.contexts;

import cargoportcomponent.core.CargoPort;
import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;
import cargoportcomponent.ui.UserInterface;
import libs.socketconnection.bytesockets.ByteClientSocket;
import libs.socketconnection.bytesockets.ByteServerSocket;
import libs.socketconnection.contracts.ClientToServerContract;

import java.io.IOException;
import java.util.Scanner;

public class Context {
  static public final int DEFAULT_LISTEN_PORT = 6666;

  static private final String EXIT_KEY_WORD = "q";

  private int inputPort;
  private String outputIp;
  private int outputPort;

  private ClientToServerContract clientToServerContract;

  private CargoPort cargoPort;

  public Context(int anInputPort, UserInterface ui, String anOutputIp, int anOutputPort) {
    try {
      inputPort = anInputPort;
      outputIp = anOutputIp;
      outputPort = anOutputPort;
      clientToServerContract = new ClientToServerContract();
      if (anOutputIp != null) {
        cargoPort = new CargoPort(3, ui, new ByteClientSocket(outputIp, outputPort), clientToServerContract);
      }
      else {
        cargoPort = new CargoPort(3, ui);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public Context(int anInputPort, UserInterface ui) {
    this(anInputPort, ui, null, -1);
  }

  public Context(int anInputPort, String anOutputIp, int anOutputPort) {
    this(anInputPort, null, anOutputIp, anOutputPort);
  }

  private byte[] inputDataProcessor(byte[] data) {
    Object recievedObject = clientToServerContract.bytesToObject(data);
    String className = recievedObject.getClass().getName();
    if (className.equals(Ship.class.getName())) {
      cargoPort.putShipToRoadstead((Ship) recievedObject);
    }
    else if (className.equals(Crane.class.getName())) {
      cargoPort.addCrane((Crane) recievedObject);
    }
    else {
      System.out.println("fuuuuuuuuuu");
    }
    return new byte[1];
  }

  private void waitToExit() {
    Scanner scanner = new Scanner(System.in);
    String answer;
    while (!(answer = scanner.next()).equals(EXIT_KEY_WORD)) {
      scanner.skip(".");
    }
  }

  public void startApplication() {
    ByteServerSocket serverSocket = new ByteServerSocket(inputPort, this::inputDataProcessor);
    cargoPort.startPort();
    serverSocket.startListen();

    System.out.println("To close application type: " + EXIT_KEY_WORD);
    waitToExit();

    serverSocket.stopListen();
    cargoPort.stopPort();
  }

}
