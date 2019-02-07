package core;

import bytesockets.ByteClientSocket;
import contracts.ClientToServerContract;
import simulationattributes.Crane;
import simulationattributes.Ship;
import tables.unloadingprocesstable.TableRow;
import ui.UserInterface;

import java.io.IOException;

public class WorkCrane extends Thread {
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
          byteClientSocket.sendMessage(data);
        }
        Thread.sleep(TIME_INTERVAL);
      }
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
    catch (IOException e) {
      e.printStackTrace();
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
