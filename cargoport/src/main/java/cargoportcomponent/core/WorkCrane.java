package cargoportcomponent.core;

import cargoportcomponent.logging.CargoPortLoggerManager;
import cargoportcomponent.ui.UserInterface;
import databasewriter.core.tables.unloadingprocesstable.TableRow;
import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;
import libs.socketconnection.bytesockets.ByteClientSocket;
import libs.socketconnection.contracts.ClientToServerContract;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class WorkCrane extends Thread {
  static private Logger LOGGER = CargoPortLoggerManager.getInstance().getLogger();
  static private final int TIME_INTERVAL = 1000;

  private boolean stop;
  private Thread thread;
  private Crane crane;
  private Ship ship;
  private UserInterface userInterface;
  private ByteClientSocket byteClientSocket;
  private ClientToServerContract clientToServerContractSerializationImpl;

  public WorkCrane(Crane aCrane, UserInterface ui, ByteClientSocket clientSocket, ClientToServerContract cToServerContract) {
    crane = aCrane;
    userInterface = ui;
    byteClientSocket = clientSocket;
    clientToServerContractSerializationImpl = cToServerContract;
  }

  public Crane getCrane() {
    return crane;
  }

  public Ship getShip() {
    return ship;
  }

  public void startUnload(Ship aShip) {
    ship = aShip;
    thread = new Thread(this);
    thread.start();
  }

  private void unload() {
    try {
      setStop(false);
      final int initMass = ship.getCargoMass();
      while (!isStop() && ship.getCargoMass() > 0) {
        int ost = ship.getCargoMass() - crane.getSpeed();
        ship.setCargoMass(ost >= 0 ? ost : 0);
        if (userInterface != null) {
          userInterface.displayCraneWork(crane, ship);
        }
        if (byteClientSocket != null) {
          TableRow row = new TableRow(
                  crane.getNumber(),
                  ship.getName(),
                  initMass,
                  ship.getCargoMass()
          );
          byte[] data =  clientToServerContractSerializationImpl.objectToBytes(row);
          try {
            byteClientSocket.sendMessage(data);
          }
          catch (IOException e) {
            LOGGER.log(Level.WARNING, "Error due sending data: " + e.getMessage());
          }
        }
        Thread.sleep(TIME_INTERVAL);
      }
    }
    catch (InterruptedException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
  }

  public boolean isWorking() {
    return thread != null && thread.isAlive();
  }

  public void stopWork() {
    setStop(true);
  }

  @Override
  public void run() {
    unload();
  }

  private synchronized boolean isStop() {
    return stop;
  }

  private synchronized void setStop(boolean stop) {
    this.stop = stop;
  }
}
