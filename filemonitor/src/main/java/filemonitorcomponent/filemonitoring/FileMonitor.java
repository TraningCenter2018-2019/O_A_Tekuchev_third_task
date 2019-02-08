package filemonitorcomponent.filemonitoring;

import filemonitorcomponent.logging.FileMonitorLoggerManager;
import libs.parsers.jsonparsers.JsonConverter;
import libs.parsers.jsonparsers.exceptions.InvalidJsonException;
import libs.parsers.jsonparsers.exceptions.NoSuchClassException;
import libs.socketconnection.bytesockets.ByteClientSocket;
import libs.socketconnection.contracts.ClientToServerContract;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Checks a directory for income files
 */
public class FileMonitor extends Thread {
  static private Logger LOGGER = FileMonitorLoggerManager.getInstance().getLogger();
  static private final int sleepTime = 350;

  private File monitoredDir;
  private ByteClientSocket byteClientSocket;
  private boolean isStop = false;
  private ClientToServerContract clientToServerContract;
  private JsonConverter jsonConverter;
  private Thread thread;

  public FileMonitor(
          String dirName,
          ByteClientSocket sConnector,
          ClientToServerContract cToSClientToServerContract,
          JsonConverter jConverter) {
    monitoredDir = new File(dirName);
    if (!monitoredDir.exists() || !monitoredDir.isDirectory()) {
      monitoredDir.mkdirs();
    }
    byteClientSocket = sConnector;
    clientToServerContract = cToSClientToServerContract;
    jsonConverter = jConverter;
  }

  private synchronized boolean getIsStop() {
    return isStop;
  }

  private synchronized void setIsStop(boolean value) {
    isStop = value;
  }

  private void monitor() {
    try {
      setIsStop(false);
      while (!getIsStop()) {
        File[] listFiles = monitoredDir.listFiles();
        if (listFiles.length != 0) {
          for (File file : listFiles) {
            if (!file.isDirectory()) {
              String json = new String(Files.readAllBytes(file.toPath()));
              Object fileObj = null;
              try {
                fileObj = jsonConverter.jsonToObject(json);
              }
              catch (NoSuchClassException | InvalidJsonException e) {
                LOGGER.log(Level.WARNING, e.getMessage());
              }
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
    catch (InterruptedException | IOException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
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
    monitor();
  }

  public void stopMonitoring() {
    setIsStop(true);
  }

}
