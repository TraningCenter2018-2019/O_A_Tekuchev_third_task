import bytesockets.ByteClientSocket;
import bytesockets.ByteServerSocket;
import contracts.ClientToServerContract;
import filemonitoring.FileMonitor;
import json.JsonConverter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import simulationattributes.Ship;
import simulationattributes.ShipTypes;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class TestTransfer {
  static private final int port = 6666;
  static private final String ip = "127.0.0.1";

  private ByteServerSocket byteServerSocket = new ByteServerSocket(port, this::requestProcessor);
  private Random random = new Random();
  private boolean stop;
  private FileMonitor fileMonitor;
  private ByteClientSocket byteClientSocket;
  private Ship sentShip;

  byte[] requestProcessor(byte[] bytes) {
    ClientToServerContract clientToServerContract = new ClientToServerContract();
    Object recievedObject = clientToServerContract.bytesToObject(bytes);
    Ship recievedShip;
    if (recievedObject.getClass().getName().equals(Ship.class.getName())) {
      recievedShip = (Ship) recievedObject;
    }
    else {
      recievedShip = null;
    }
    assertEquals(sentShip.getCargoMass(), recievedShip.getCargoMass());
    assertEquals(sentShip.getType(), recievedShip.getType());
    setStop(true);
    return new byte[] {(byte) 200};
  }

  private Ship getRandomShip() {
    int mass = random.nextInt(500);
    ShipTypes type = ShipTypes.values()[random.nextInt(3)];
    return new Ship(type, mass);
  }

  @Before
  public void initServer() {
    try {
      byteServerSocket.startListen();
      byteClientSocket = new ByteClientSocket(ip, port, b->{});
      JsonConverter jsonConverter = new JsonConverter();
      fileMonitor = new FileMonitor("monitored", byteClientSocket, new ClientToServerContract(), jsonConverter);
      try (FileWriter fileWriter = new FileWriter(fileMonitor.getMonitoredDirectory() + "/test.json")) {
        sentShip = getRandomShip();
        String content = jsonConverter.objectToJson(sentShip);
        fileWriter.write(content);
      }
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  @After
  public void closeServer() {
    byteServerSocket.stopListen();
  }

  @Test
  public void testShipTransfer() throws InterruptedException, IOException {
    fileMonitor.startMonitoring();
    while (!isStop()) {
      Thread.sleep(200);
    }
    fileMonitor.stopMonitoring();
  }

  private synchronized boolean isStop() {
    return stop;
  }

  private synchronized void setStop(boolean stop) {
    this.stop = stop;
  }
}
