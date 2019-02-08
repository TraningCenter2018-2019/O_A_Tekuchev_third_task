package cargoportcomponent.core;

import cargoportcomponent.logging.CargoPortLoggerManager;
import cargoportcomponent.ui.UserInterface;
import libs.simulationattributes.Crane;
import libs.simulationattributes.Ship;
import libs.socketconnection.bytesockets.ByteClientSocket;
import libs.socketconnection.contracts.ClientToServerContract;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CargoPort extends Thread {
  static private final Logger LOGGER = CargoPortLoggerManager.getInstance().getLogger();
  static private final int SLEEP_TIME = 350;

  private final List<Ship> ships;
  private final List<Ship> priopityShips;
  private List<WorkCrane> cranes;
  private Queue<Integer> cranesToDelete;
  private UserInterface userInterface;
  private boolean stop;
  private Thread thread;
  private int maxCountCranes;
  private ByteClientSocket byteClientSocke;
  private ClientToServerContract clientToServerContractSerializationImpl;

  public CargoPort(int aMaxCountCranes, UserInterface ui, ByteClientSocket socket, ClientToServerContract contract) {
    ships = new LinkedList<>();
    priopityShips = new LinkedList<>();
    cranesToDelete = new LinkedList<>();
    cranes = new ArrayList<>();
    userInterface = ui;
    maxCountCranes = aMaxCountCranes;
    byteClientSocke = socket;
    clientToServerContractSerializationImpl = contract;
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

  private synchronized int countPriorityShips() {
    return priopityShips.size();
  }

  private synchronized int countCranesToDelete() {
    return cranesToDelete.size();
  }

  private synchronized int pollCraneToDelete() {
    return cranesToDelete.poll();
  }

  private static synchronized List<WorkCrane> getSpareWorkCranes(List<WorkCrane> cranes) {
    List<WorkCrane> ls = new LinkedList<>();
    for (WorkCrane workCrane : cranes) {
      if (!workCrane.isWorking()) {
        ls.add(workCrane);
      }
    }
    return ls;
  }

  private synchronized WorkCrane findByNumber(int num) {
    for (WorkCrane crane : cranes) {
      if (crane.getCrane().getNumber() == num) {
        return crane;
      }
    }
    return null;
  }

  private synchronized void deleteCrane(WorkCrane crane) {
    cranes.remove(crane);
  }

  private static void startUnload(List<Ship> shipsToUnload, List<WorkCrane> cranes) {
    List<WorkCrane> spareWorkCranes = getSpareWorkCranes(cranes);
    for (WorkCrane workCrane : spareWorkCranes) {
      synchronized (shipsToUnload) {
        for (Ship ship : shipsToUnload) {
          if (ship.getType() == workCrane.getCrane().getType()) {
            shipsToUnload.remove(ship);
            workCrane.startUnload(ship);
          }
        }
      }
    }
  }

  private void startWork() {
    try {
      setStop(false);
      while (!isStop()) {
        while (countCranesToDelete() > 0) {
          WorkCrane workCrane = findByNumber(pollCraneToDelete());
          if (workCrane != null) {
            if (workCrane.isWorking()) {
              workCrane.stopWork();
              workCrane.join();
              Ship notUnloadShip = workCrane.getShip();
              priopityShips.add(notUnloadShip);
            }
            userInterface.deleteCrane(workCrane.getCrane());
            deleteCrane(workCrane);
          }
          Thread.sleep(SLEEP_TIME / 2);
        }
        if (countPriorityShips() > 0) {
          startUnload(priopityShips, cranes);
        }
        if (countShipsOnRoadstead() > 0) {
          startUnload(ships, cranes);
        }
        Thread.sleep(SLEEP_TIME);
      }
    }
    catch (InterruptedException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
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
    cranes.add(new WorkCrane(crane, userInterface, byteClientSocke, clientToServerContractSerializationImpl));
    if (userInterface != null) {
      userInterface.displayCrane(crane);
    }
    return true;
  }

  public synchronized void deleteCrane(int craneNum) {
    cranesToDelete.add(craneNum);
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
