package filemonitoring;

import bytesockets.ByteClientSocket;
import contracts.Contract;
import json.JsonConverter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

/**
 * Checks a directory for income files
 */
public class FileMonitor extends Thread {
  static private final int sleepTime = 350;

  private File monitoredDir;
  private ByteClientSocket byteClientSocket;
  private boolean isStop = false;
  private Contract clientToServerContract;
  private JsonConverter jsonConverter;
  private Thread thread;

  public FileMonitor(
          String dirName,
          ByteClientSocket sConnector,
          Contract cToSContract,
          JsonConverter jConverter) {
    monitoredDir = new File(dirName);
    if (!monitoredDir.exists() || !monitoredDir.isDirectory()) {
      monitoredDir.mkdirs();
    }
    byteClientSocket = sConnector;
    clientToServerContract = cToSContract;
    jsonConverter = jConverter;
  }

  private synchronized boolean getIsStop() {
    return isStop;
  }

  private synchronized void setIsStop(boolean value) {
    isStop = value;
  }

  private void monitor() throws IOException {
    try {
      while (!getIsStop()) {
        File[] listFiles = monitoredDir.listFiles();
        if (listFiles.length != 0) {
          for (File file : listFiles) {
            if (!file.isDirectory()) {
              System.out.println(file.getName());
              String json = new String(Files.readAllBytes(file.toPath()));
              Object fileObj = jsonConverter.jsonToObject(json);
              byte[] objBytes = clientToServerContract.objectToBytes(fileObj);
              byteClientSocket.sendMessage(objBytes);
              boolean suc = file.delete();
            }
          }
        }
        if (!getIsStop()) {
          Thread.sleep(sleepTime);
        }
      }
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public String getMonitoredDirectory() {
    return monitoredDir.getAbsolutePath();
  }

  public void startMonitoring() throws IOException {
    thread = new Thread(this);
    thread.start();
  }

  @Override
  public void run() {
    try {
      monitor();
    }
    catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void stopMonitoring() {
    setIsStop(true);
  }

}
