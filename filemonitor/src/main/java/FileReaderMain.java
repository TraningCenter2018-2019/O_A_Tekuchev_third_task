import bytesockets.ByteClientSocket;
import contracts.ClientToServerContract;
import filemonitoring.FileMonitor;
import json.JsonConverter;
import simulationattributes.CargoTypes;
import simulationattributes.Crane;
import simulationattributes.Ship;

import java.io.FileWriter;
import java.io.IOException;

public class FileReaderMain {
  static public void main(String[] args) {
    try {
      JsonConverter jc = new JsonConverter();
      /*try (FileWriter fileWriter = new FileWriter("ship.json")) {
        String content = jc.objectToJson(new Ship(CargoTypes.bulkСarrier, 400, "jjjj"));
        fileWriter.write(content);
      }

      try (FileWriter fileWriter = new FileWriter("crane.json")) {
        String content = jc.objectToJson(new Crane(1, CargoTypes.bulkСarrier, 50));
        fileWriter.write(content);
      }*/

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
