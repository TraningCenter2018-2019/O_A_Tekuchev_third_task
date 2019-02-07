package core;

import bytesockets.ByteClientSocket;
import contracts.ClientToServerContract;
import simulationattributes.Crane;
import simulationattributes.Ship;
import ui.UserInterface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class CargoPort extends Thread {
  static private final int SLEEP_TIME = 350;

  private final List<Ship> ships;
  private List<WorkCrane> cranes;
  private UserInterface userInterface;
  private boolean stop;
  private Thread thread;
  private int maxCountCranes;
  private ByteClientSocket byteClientSocke;
  private ClientToServerContract clientToServerContract;

  public CargoPort(int aMaxCountCranes, UserInterface ui, ByteClientSocket socket, ClientToServerContract contract) {
    ships = new LinkedList<>();
    cranes = new ArrayList<>();
    userInterface = ui;
    maxCountCranes = aMaxCountCranes;
    byteClientSocke = socket;
    clientToServerContract = contract;
  }

  public CargoPort(int aMaxCountCranes, UserInterface ui) {
    this(aMaxCountCranes, ui, null, null);
  }

  public CargoPort(int aMaxCountCranes, ByteClientSocket socket, ClientToServerContract contract) {
    this(aMaxCountCranes, null, socket, contract);
  }

  private synchronized boolean isStop() {
    return stop;
  }

  private synchronized void setStop(boolean stop) {
    this.stop = stop;
  }

  private synchronized List<WorkCrane> getSpareWorkCranes() {
    List<WorkCrane> ls = new LinkedList<>();
    for (WorkCrane workCrane : cranes) {
      if (!workCrane.isWorking()) {
        ls.add(workCrane);
      }
    }
    return ls;
  }

  private void startWork() {
    try {
      setStop(false);
      while (!isStop()) {
        if (countShipsOnRoadstead() > 0) {
          List<WorkCrane> spareWorkCranes = getSpareWorkCranes();
          for (WorkCrane workCrane : spareWorkCranes) {
            synchronized (ships) {
              for (Ship ship : ships) {
                if (ship.getType() == workCrane.getCrane().getType()) {
                  ships.remove(ship);
                  workCrane.startUnload(ship);
                }
              }
            }
          }
          /*if (spareWorkCranes != null) {
            Ship aShip = getShipFromRoadstead();
            spareWorkCranes.startUnload(aShip);
          }*/
        }
        Thread.sleep(SLEEP_TIME);
      }
    }
    catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  public synchronized int countShipsOnRoadstead() {
    return ships.size();
  }

  public synchronized void putShipToRoadstead(Ship ship) {
    ships.add(ship);
    if (userInterface != null) {
      userInterface.displayShipOnRoadstead(ship);
    }
  }

  public synchronized boolean addCrane(Crane crane) {
    if (cranes.size() == maxCountCranes) {
      return false;
    }
    cranes.add(new WorkCrane(crane, userInterface, byteClientSocke, clientToServerContract));
    if (userInterface != null) {
      userInterface.displayCrane(crane);
    }
    return true;
  }

  public void startPort() {
    thread = new Thread(this);
    thread.start();
  }

  public void stopPort() {
    setStop(true);
  }

  @Override
  public void run() {
    startWork();
  }
}
