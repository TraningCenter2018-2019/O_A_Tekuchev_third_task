package filemonitorcomponent.contexts;

import filemonitorcomponent.filemonitoring.FileMonitor;
import filemonitorcomponent.logging.FileMonitorLoggerManager;
import libs.parsers.jsonparsers.JsonConverter;
import libs.socketconnection.bytesockets.ByteClientSocket;
import libs.socketconnection.contracts.ClientToServerClientToServerContractSerializationImpl;
import libs.socketconnection.contracts.ClientToServerContract;
import libs.socketconnection.contracts.ServerToClientByteImpl;
import libs.socketconnection.contracts.ServerToClientContract;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * File monitor application context (the start point)
 */
public class FileMonitorAppContext {
  static private final Logger LOGGER = FileMonitorLoggerManager.getInstance().getLogger();

  static public final String DEFAULT_SERVER_IP = "127.0.0.1";
  static public final int DEFAULT_PORT = 6666;
  static public final String DEFAULT_MONITORED_DIRECTORY = "./monitored_dir";

  private String ip;
  private int port;
  private ByteClientSocket byteClientSocket;
  private ClientToServerContract clientToServerContract;
  private ServerToClientContract serverToClientContract;
  private FileMonitor fileMonitor;
  private String monitoredDir;

  /**
   * A constructor
   *
   * @param anIp an ip te send files
   * @param aPort a port
   * @param aMonitoredDir a directory to monitor
   */
  public FileMonitorAppContext(String anIp, int aPort, String aMonitoredDir) {
    ip = anIp;
    port = aPort;
    clientToServerContract = new ClientToServerClientToServerContractSerializationImpl();
    serverToClientContract = new ServerToClientByteImpl();
    monitoredDir = aMonitoredDir;
  }

  /**
   * Handles server responses
   * @param inputData
   */
  private void responseHandler(byte[] inputData) {
    switch (inputData[0]) {
      case ServerToClientContract.OK_CODE:
        break;

      case ServerToClientContract.UNEXPECTED_SENT_DATA_CODE:
      case ServerToClientContract.INTERNAL_SERVER_ERROR_CODE:
      case ServerToClientContract.CANNOT_PERFORM_OPERATION:
        LOGGER.log(Level.WARNING, serverToClientContract.getMessage(inputData));
        break;

      default:
        LOGGER.log(Level.WARNING,"The response in not conforned to the contract");
        break;
    }
  }

  /**
   * The start of the app
   */
  public void startApplication() {
    try {
      byteClientSocket = new ByteClientSocket(ip, port, this::responseHandler);
      fileMonitor = new FileMonitor(monitoredDir, byteClientSocket, clientToServerContract, new JsonConverter());
      fileMonitor.startMonitoring();

      System.in.read();

      fileMonitor.stopMonitoring();
      byteClientSocket.stopConnection();
    }
    catch (IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
  }
}
