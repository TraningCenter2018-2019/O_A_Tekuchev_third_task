package cargoportcomponent.core;

import cargoportcomponent.logging.CargoPortLoggerManager;
import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;
import databasewriter.core.tables.unloadingprocesstable.TableRow;
import cargoportcomponent.ui.UserInterface;
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
  private ClientToServerContract clientToServerContract;

  public WorkCrane(Crane aCrane, UserInterface ui, ByteClientSocket clientSocket, ClientToServerContract cToServerContract) {
    crane = aCrane;
    userInterface = ui;
    byteClientSocket = clientSocket;
    clientToServerContract = cToServerContract;
  }

  public Crane getCrane() {
    return crane;
  }

  public void startUnload(Ship aShip) {
    ship = aShip;
    thread = new Thread(this);
    thread.start();
  }

  private void unload() {
    try {
      final int initMass = ship.getCargoMass();
      while (ship.getCargoMass() > 0) {
        ship.setCargoMass(ship.getCargoMass() - crane.getSpeed());
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
          byte[] data =  clientToServerContract.objectToBytes(row);
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

  @Override
  public void run() {
    unload();
  }
}
