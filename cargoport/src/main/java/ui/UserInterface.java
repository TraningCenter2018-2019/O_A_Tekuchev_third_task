package ui;

import simulationattributes.Crane;
import simulationattributes.Ship;

public interface UserInterface {
  void displayShipOnRoadstead(Ship ship);

  void displayCrane(Crane crane);

  void displayCraneWork(Crane crane, Ship ship);
}
