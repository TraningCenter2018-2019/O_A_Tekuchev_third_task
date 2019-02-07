package filemonitorcomponent;

import filemonitorcomponent.filemonitoring.FileMonitor;
import libs.parsers.jsonparsers.JsonConverter;
import libs.socketconnection.bytesockets.ByteClientSocket;
import libs.socketconnection.contracts.ClientToServerContract;

public class FileReaderMain {
  static public void main(String[] args) {
    try {
      JsonConverter jc = new JsonConverter();
      ByteClientSocket byteClientSocket = new ByteClientSocket("127.0.0.1", 6666);
      FileMonitor fileMonitorManager =
              new FileMonitor("./monitored", byteClientSocket, new ClientToServerContract(), jc);
      fileMonitorManager.startMonitoring();
      System.in.read();
      fileMonitorManager.stopMonitoring();
      byteClientSocket.stopConnection();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
}
