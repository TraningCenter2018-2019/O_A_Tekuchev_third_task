package cargoportcomponent.core;

import cargoportcomponent.core.exceptions.MaxCountCranesException;
import cargoportcomponent.core.exceptions.UniqueValueException;
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

/**
 * The class that simulates a cargo port work
 */
public class CargoPort extends Thread {
  static private final Logger LOGGER = CargoPortLoggerManager.getInstance().getLogger();
  static private final int SLEEP_TIME = 350;

  private final List<Ship> shipsOnRoadstead;
  private final List<Ship> priopityShips;
  private final List<WorkCrane> allCranes;
  private final Queue<WorkCrane> cranesToDelete;
  private final UserInterface userInterface;
  private boolean stop;
  private Thread thread;
  private final int maxCountCranes;
  private final ByteClientSocket byteClientSocke;
  private final ClientToServerContract clientToServerContractSerializationImpl;

  public CargoPort(int aMaxCountCranes, UserInterface ui, ByteClientSocket socket, ClientToServerContract contract) {
    shipsOnRoadstead = new LinkedList<>();
    priopityShips = new LinkedList<>();
    cranesToDelete = new LinkedList<>();
    allCranes = new ArrayList<>();
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

  /**
   * Gets the list of spare allCranes
   * @param cranes the allCranes where to find
   * @return the list of spare allCranes
   */
  private static synchronized List<WorkCrane> getSpareWorkCranes(List<WorkCrane> cranes) {
    List<WorkCrane> ls = new LinkedList<>();
    for (WorkCrane workCrane : cranes) {
      if (!workCrane.isWorking()) {
        ls.add(workCrane);
      }
    }
    return ls;
  }

  /**
   * Starts ship unloading, find spare appropriate crane and starts unloading
   * @param shipsToUnload the shipsOnRoadstead it need to unload
   * @param cranes the list of crane that can unload
   */
  private void startUnload(List<Ship> shipsToUnload, List<WorkCrane> cranes) {
    List<WorkCrane> spareWorkCranes = getSpareWorkCranes(cranes);
    for (WorkCrane workCrane : spareWorkCranes) {
      synchronized (shipsToUnload) {
        for (Ship ship : shipsToUnload) {
          if (ship.getType() == workCrane.getCrane().getType()) {
            shipsToUnload.remove(ship);
            workCrane.startUnload(ship);
            if (userInterface != null) {
              userInterface.deleteShip(ship);
            }
          }
        }
      }
    }
  }

  /**
   * Whether it needs to stop simulation
   * @return true if it is
   */
  private synchronized boolean isStop() {
    return stop;
  }

  /**
   * Sets whether it needs to stop simulation
   * @param stop value
   */
  private synchronized void setStop(boolean stop) {
    this.stop = stop;
  }

  /**
   * Gets the count of shipsOnRoadstead that has priority
   * @return the count of priority shipsOnRoadstead
   */
  private synchronized int countPriorityShips() {
    return priopityShips.size();
  }

  /**
   * Gets the count of allCranes that have to be deleted
   * @return the count of allCranes that have to be deleted
   */
  private synchronized int countCranesToDelete() {
    return cranesToDelete.size();
  }

  /**
   * Pulls the next crane that have to be deleted from the queue
   * @return crane
   */
  private synchronized WorkCrane pollCraneToDelete() {
    return cranesToDelete.poll();
  }

  /**
   * Finds crane by its number
   * @param num the crane number
   * @return the crane or null if no one crane was found
   */
  private synchronized WorkCrane findByNumber(int num) {
    for (WorkCrane crane : allCranes) {
      if (crane.getCrane().getNumber() == num) {
        return crane;
      }
    }
    return null;
  }

  /**
   * Deletes crane from the list
   * @param crane the crane to delete
   */
  private synchronized void deleteCrane(WorkCrane crane) {
    allCranes.remove(crane);
  }

  /**
   * Start port work
   */
  private void startWork() {
    try {
      setStop(false);
      while (!isStop()) {
        while (countCranesToDelete() > 0) {
        WorkCrane workCrane = pollCraneToDelete();
          if (workCrane.isWorking()) {
            workCrane.stopWork();
            workCrane.join();
            Ship notUnloadShip = workCrane.getShip();
            priopityShips.add(notUnloadShip);
            if (userInterface != null) {
              userInterface.addShip(notUnloadShip);
            }
          }
          if (userInterface != null) {
            userInterface.deleteCrane(workCrane.getCrane());
          }
          deleteCrane(workCrane);
          Thread.sleep(SLEEP_TIME / 2);
        }
        if (countPriorityShips() > 0) {
          startUnload(priopityShips, allCranes);
        }
        if (countShipsOnRoadstead() > 0) {
          startUnload(shipsOnRoadstead, allCranes);
        }
        Thread.sleep(SLEEP_TIME);
      }
    }
    catch (InterruptedException e) {
      LOGGER.log(Level.SEVERE, e.getMessage());
    }
  }

  public synchronized int countShipsOnRoadstead() {
    return shipsOnRoadstead.size();
  }

  public synchronized void putShipToRoadstead(Ship ship) {
    shipsOnRoadstead.add(ship);
    if (userInterface != null) {
      userInterface.addShip(ship);
    }
  }

  /**
   * Adds new crane
   * @param crane the new crane
   * @return true if the crane was successfully added, false id count of cranes is maximum
   */
  public synchronized void addCrane(Crane crane) throws MaxCountCranesException, UniqueValueException {
    String msg;
    if (allCranes.size() == maxCountCranes) {
      msg = "The count of cranes is maximum";
      if (userInterface != null) {
        userInterface.showWarning(msg);
      }
      throw new MaxCountCranesException(msg);
    }
    if (findByNumber(crane.getNumber()) != null) {
      msg = "The crane with number" + crane.getNumber() + " is already exists";
      if (userInterface != null) {
        userInterface.showWarning(msg);
      }
      throw new UniqueValueException(msg);
    }
    allCranes.add(new WorkCrane(crane, userInterface, byteClientSocke, clientToServerContractSerializationImpl));
    if (userInterface != null) {
      userInterface.addCrane(crane);
    }
  }

  /**
   * Gives a comand to delete crane
   * @param craneNum the number of crane to delete
   * @return false if no such crane else - true
   */
  public synchronized boolean deleteCrane(int craneNum) {
    WorkCrane crane = findByNumber(craneNum);
    if (crane == null) {
      if (userInterface != null) {
        userInterface.showWarning("No crane with this number: " + craneNum);
      }
      return false;
    }
    cranesToDelete.add(crane);
    return true;
  }

  /**
   * Starts port work in the new thread
   */
  public void startPort() {
    thread = new Thread(this);
    thread.start();
  }

  /**
   * Gives command to stop port work
   */
  public void stopPort() {
    setStop(true);
  }

  @Override
  public void run() {
    startWork();
  }
}
